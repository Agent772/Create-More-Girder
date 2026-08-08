package com.agent772.createmoregirder.content.strut;

import com.agent772.createmoregirder.content.copycat_strut.CopycatGirderStrutBlockEntity;
import com.agent772.createmoregirder.content.copycat_strut.CopycatStrutTextureRemapper;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.simibubi.create.content.contraptions.ContraptionWorld;
import com.simibubi.create.foundation.blockEntity.renderer.SmartBlockEntityRenderer;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import dev.engine_room.flywheel.lib.transform.TransformStack;
import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SuperBufferFactory;
import net.minecraft.client.GraphicsStatus;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.ChunkRenderTypeSet;
import net.minecraftforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Renders girder beams between strut attachment blocks
 *
 * Adapted from Bits-n-Bobs by Industrialists-Of-Create
 * Original: https://github.com/Industrialists-Of-Create/Bits-n-Bobs
 * Licensed under MIT License
 */
public class GirderStrutBlockEntityRenderer extends SmartBlockEntityRenderer<GirderStrutBlockEntity> {

    public GirderStrutBlockEntityRenderer(final BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected void renderSafe(final GirderStrutBlockEntity blockEntity, final float partialTicks, final PoseStack ms, final MultiBufferSource buffer, final int light, final int overlay) {
        super.renderSafe(blockEntity, partialTicks, ms, buffer, light, overlay);

        final StrutModelType modelType;
        if (blockEntity.getBlockState().getBlock() instanceof final GirderStrutBlock girderStrutBlock) {
            modelType = girderStrutBlock.getModelType();
        } else {
            return;
        }

        // Resolve copycat texture data if applicable
        CopycatStrutTextureRemapper.BeamLayers beamLayers = null;
        int copycatLightEmission = 0;
        RenderType beamRenderType = RenderType.cutout();
        if (blockEntity instanceof CopycatGirderStrutBlockEntity copycatBe && copycatBe.hasMimickedState()) {
            BlockState mimicked = copycatBe.getMimickedState();
            beamLayers = CopycatStrutTextureRemapper.resolveBeamLayers(mimicked, copycatBe.getFaceRotation());
            copycatLightEmission = mimicked.getLightEmission();
            beamRenderType = resolveBeamRenderType(mimicked);
        }

        boolean onContraption = blockEntity.getLevel() instanceof ContraptionWorld;

        if (onContraption || Minecraft.getInstance().options.graphicsMode().get() == GraphicsStatus.FAST) {
            // Render the girder strut segment
            for (BlockPos pos : blockEntity.getConnectionsCopy()) {
                pos = pos.offset(blockEntity.getBlockPos());
                final BlockState state = blockEntity.getLevel().getBlockState(pos);
                if (!(state.getBlock() instanceof GirderStrutBlock)) {
                    continue;
                }

                final Vec3i relative = pos.subtract(blockEntity.getBlockPos());
                final Direction thisFacing = blockEntity.getBlockState().getValue(GirderStrutBlock.FACING);
                final BlockState otherState = blockEntity.getLevel().getBlockState(pos);
                final Direction otherFacing = otherState.getValue(GirderStrutBlock.FACING);
                final double thisDepth = anchorDepth(blockEntity.getLevel(), blockEntity.getBlockPos(), thisFacing);
                final double otherDepth = anchorDepth(blockEntity.getLevel(), pos, otherFacing);
                final Vec3 thisAttachment = Vec3.atCenterOf(blockEntity.getBlockPos()).relative(thisFacing, thisDepth);
                final Vec3 otherAttachment = Vec3.atCenterOf(pos).relative(otherFacing, otherDepth);

                final double length = thisAttachment.distanceTo(otherAttachment);
                final int segments = (int) Math.ceil(length);
                final double lengthOffset = (length - segments) / 2.0;

                ms.pushPose();

                final Vec3 relativeVec = otherAttachment.subtract(thisAttachment);
                final float distHorizontal = (float) Math.sqrt(relativeVec.x() * relativeVec.x() + relativeVec.z() * relativeVec.z());
                final double yRot = distHorizontal == 0 ? 0 : Math.atan2(relativeVec.x(), relativeVec.z());
                final double xRot = (float) Math.atan2(relativeVec.y(), distHorizontal);

                TransformStack.of(ms)
                        .translate(Vec3.atLowerCornerOf(thisFacing.getNormal()).scale(thisDepth))
                        .center()
                        .rotateY((float) yRot)
                        .rotateX(-(float) xRot)
                        .uncenter();

                ms.translate(0, 0, lengthOffset + 0.5);
                if (getRenderPriority(relative) > getRenderPriority(relative.multiply(-1))) {
                    final Vec3 segDir = relativeVec.normalize();
                    List<RemappedQuad> baseQuads = null;
                    List<RemappedQuad> overlayQuads = null;
                    if (beamLayers != null) {
                        baseQuads = buildRemappedSegmentQuads(modelType.getPartialModel(), beamLayers.base(), false);
                        overlayQuads = new ArrayList<>();
                        for (final CopycatStrutTextureRemapper.FaceData[] depth : beamLayers.overlays()) {
                            overlayQuads.addAll(buildRemappedSegmentQuads(modelType.getPartialModel(), depth, true));
                        }
                    }
                    renderSegments(state, modelType.getPartialModel(), ms, segments, buffer, light, onContraption ? null : blockEntity.getLevel(), thisAttachment, segDir, baseQuads, overlayQuads, copycatLightEmission, beamRenderType);
                }
                ms.popPose();
            }
        } else { // Forge 1.20.1 version
            if (blockEntity.connectionRenderBufferCache == null) {
                blockEntity.connectionOverlayRenderBufferCache = null;

                final GirderStrutModelBuilder.GirderStrutModelData connectionData =
                        GirderStrutModelBuilder.GirderStrutModelData.collect(
                                blockEntity.getLevel(),
                                blockEntity.getBlockPos(),
                                blockEntity.getBlockState(),
                                blockEntity
                        );

                final CopycatStrutTextureRemapper.BeamLayers finalBeamLayers = beamLayers;
                final int emissionLevel = copycatLightEmission;
                java.util.function.Function<org.joml.Vector3f, Integer> lighter = blockEntity.createLighter();
                if (emissionLevel > 0) {
                    final java.util.function.Function<org.joml.Vector3f, Integer> baseLighter = lighter;
                    lighter = pos -> {
                        int base = baseLighter.apply(pos);
                        int emissionLight = net.minecraft.client.renderer.LightTexture.pack(emissionLevel, 0);
                        return IBlockEntityRelighter.maximizeLight(base, emissionLight);
                    };
                }
                final java.util.function.Function<org.joml.Vector3f, Integer> finalLighter = lighter;

                final List<Consumer<BufferBuilder>> quads = connectionData.connections()
                        .stream()
                        .flatMap(c -> GirderStrutModelManipulator
                                .bakeConnectionToConsumer(c, modelType, finalLighter, finalBeamLayers == null ? null : finalBeamLayers.base())
                                .stream())
                        .toList();

                // 1.20.1 style BufferBuilder
                BufferBuilder builder = new BufferBuilder(256);
                builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.BLOCK);

                for (Consumer<BufferBuilder> quad : quads) {
                    quad.accept(builder);
                }

                BufferBuilder.RenderedBuffer renderedBuffer = builder.end();

                blockEntity.connectionRenderBufferCache =
                        SuperBufferFactory.getInstance().create(renderedBuffer);

                // Bake the mimic's overlay layers (glow shells etc.) into a second buffer that is
                // alpha-blended over the base mesh — the same layer compositing the copycat girder
                // gets from the chunk renderer.
                if (finalBeamLayers != null && finalBeamLayers.overlays().length > 0) {
                    final List<Consumer<BufferBuilder>> overlayQuads = new ArrayList<>();
                    for (final CopycatStrutTextureRemapper.FaceData[] depth : finalBeamLayers.overlays()) {
                        for (final GirderStrutModelBuilder.GirderConnection c : connectionData.connections()) {
                            overlayQuads.addAll(GirderStrutModelManipulator.bakeConnectionOverlayToConsumer(c, modelType, finalLighter, depth));
                        }
                    }
                    if (!overlayQuads.isEmpty()) {
                        BufferBuilder overlayBuilder = new BufferBuilder(256);
                        overlayBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.BLOCK);
                        for (final Consumer<BufferBuilder> quad : overlayQuads) {
                            quad.accept(overlayBuilder);
                        }
                        BufferBuilder.RenderedBuffer overlayRenderedBuffer = overlayBuilder.end();
                        blockEntity.connectionOverlayRenderBufferCache =
                                SuperBufferFactory.getInstance().create(overlayRenderedBuffer);
                    }
                }
            }

            if (beamLayers != null) {
                // Mimicking meshes reproduce the girder's per-quad shading themselves: the vanilla
                // per-face diffuse is baked into the vertex colours of shaded layers (see
                // GirderGeometry#diffuseColor) and unshaded glow layers stay white, so the buffer's
                // own all-or-nothing diffuse must not darken the mesh a second time.
                blockEntity.connectionRenderBufferCache.disableDiffuse();
            }
            blockEntity.connectionRenderBufferCache
                    .renderInto(ms, buffer.getBuffer(beamRenderType));
            if (blockEntity.connectionOverlayRenderBufferCache != null) {
                blockEntity.connectionOverlayRenderBufferCache.disableDiffuse();
                blockEntity.connectionOverlayRenderBufferCache
                        .renderInto(ms, buffer.getBuffer(RenderType.translucentMovingBlock()));
            }
        }
    }

    /**
     * Returns the depth (signed length along {@code facing}) at which the
     * anchor attaches relative to the strut block center. Defaults to
     * {@code -0.4}, shifted outward by 1 px when the supporting neighbor is the
     * long side of a horizontal girder.
     */
    private static double anchorDepth(final Level level, final BlockPos pos, final Direction facing) {
        double depth = -0.4;
        if (level != null && GirderStrutAnchorOffset.shouldOffset(level, pos, facing)) {
            depth -= GirderStrutAnchorOffset.OFFSET_BLOCKS;
        }
        return depth;
    }

    /** A remapped mimic quad paired with its source layer's emissive lightmap. */
    private record RemappedQuad(BakedQuad quad, int lightmap) {}

    protected void renderSegments(final BlockState state, final PartialModel model, final PoseStack ms, final int length, final MultiBufferSource buffer, final int fallbackLight, final Level level, final Vec3 segmentStart, final Vec3 segmentDir, @Nullable final List<RemappedQuad> baseQuads, @Nullable final List<RemappedQuad> overlayQuads, final int lightEmission, final RenderType renderType) {
        for (int i = 0; i < length; i++) {
            ms.pushPose();
            ms.translate(0, 0, i);
            int segLight;
            if (level != null) {
                final Vec3 segWorldPos = segmentStart.add(segmentDir.scale(i + 0.5));
                segLight = LevelRenderer.getLightColor(level, BlockPos.containing(segWorldPos));
            } else {
                segLight = fallbackLight;
            }
            if (lightEmission > 0) {
                int emissionLight = net.minecraft.client.renderer.LightTexture.pack(lightEmission, 0);
                segLight = IBlockEntityRelighter.maximizeLight(segLight, emissionLight);
            }
            if (baseQuads != null) {
                final PoseStack.Pose pose = ms.last();
                emitRemappedQuads(buffer.getBuffer(renderType), pose, baseQuads, segLight);
                if (overlayQuads != null && !overlayQuads.isEmpty()) {
                    // Overlay layers (glow shells etc.) blend over the base layer on the
                    // translucent pass, matching the copycat girder's layer compositing.
                    emitRemappedQuads(buffer.getBuffer(RenderType.translucentMovingBlock()), pose, overlayQuads, segLight);
                }
            } else {
                CachedBuffers.partial(model, state)
                        .light(segLight)
                        .renderInto(ms, buffer.getBuffer(renderType));
            }
            ms.popPose();
        }
    }

    private static void emitRemappedQuads(final VertexConsumer consumer, final PoseStack.Pose pose, final List<RemappedQuad> quads, final int segLight) {
        for (final RemappedQuad rq : quads) {
            int quadLight = segLight;
            if (rq.lightmap() != 0) {
                quadLight = IBlockEntityRelighter.maximizeLight(segLight, rq.lightmap());
            }
            // putBulkData applies the quad's own shade flag, so shaded layers get the same
            // per-face diffuse the girder gets and unshaded glow layers stay full-bright.
            consumer.putBulkData(pose, rq.quad(), 1f, 1f, 1f, quadLight, OverlayTexture.NO_OVERLAY);
        }
    }

    /**
     * Builds the strut segment quads remapped to one layer of the mimicked block's textures,
     * used by the Fast/contraption render path which otherwise plays back the original
     * (non-mimic) partial model. The Fabulous path goes through
     * {@link GirderStrutModelManipulator} which does its own remap during mesh baking.
     *
     * <p>{@code overlayPass} builds one overlay depth: faces without a layer at that depth
     * are skipped instead of falling back to the original strut texture.
     */
    private static List<RemappedQuad> buildRemappedSegmentQuads(final PartialModel partial, final CopycatStrutTextureRemapper.FaceData[] faceData, final boolean overlayPass) {
        final BakedModel model = partial.get();
        // strut.json places all its quads under side=null (no cullface in the JSON), so a
        // single side=null query yields every quad we need.
        final List<BakedQuad> source = model.getQuads(
                null,
                null,
                RandomSource.create(42L),
                ModelData.EMPTY,
                null
        );
        final List<RemappedQuad> remapped = new ArrayList<>(source.size());
        for (final BakedQuad quad : source) {
            final Direction face = quad.getDirection();
            final CopycatStrutTextureRemapper.FaceData fd = face != null
                    ? faceData[face.get3DDataValue()]
                    : faceData[Direction.UP.get3DDataValue()];
            if (fd != null && fd.sprite() != null) {
                // Carry the source layer's shade flag so putBulkData shades (or doesn't shade)
                // this quad exactly like the chunk renderer shades the girder's copy of it.
                remapped.add(new RemappedQuad(CopycatStrutTextureRemapper.remapQuadUVs(quad, fd.sprite(), fd.shade()), fd.lightmap()));
            } else if (!overlayPass) {
                remapped.add(new RemappedQuad(quad, 0));
            }
        }
        return remapped;
    }

    /**
     * Picks the {@link RenderType} the beam segments should render onto. With no mimic, segments
     * use {@link RenderType#cutout()} as before.
     *
     * <p>The beam should look see-through <em>only</em> when it mimics a genuinely glass-like
     * block. The tricky case is a block like Foxy's neon concrete: it is a solid, light-emitting
     * block, but its Blockbench model is authored entirely on the {@code translucent} layer with a
     * semi-transparent glow shell as its outermost cuboid. Choosing the render pass purely from the
     * declared layers therefore routed it onto the translucent pass and it rendered as see-through
     * green glass. So we discriminate by the block's nature, not just its declared layers: a
     * light-emitting block is treated as a solid (neon) block and rendered opaque even when its only
     * declared layer is translucent. A non-emitting translucent-only block is genuine glass/ice and
     * keeps a see-through pass. Opaque geometry uses {@link RenderType#cutout()} (or
     * {@link RenderType#cutoutMipped()} when declared). Glass falls back to
     * {@link RenderType#translucentMovingBlock()} rather than {@link RenderType#translucent()},
     * because in Fabulous graphics the latter is reserved for chunk geometry routed through the OIT
     * compositing pass — BE-rendered geometry on that layer ends up invisible. The moving-block
     * translucent variant is the BE-safe equivalent (used by vanilla pistons).
     */
    private static RenderType resolveBeamRenderType(final BlockState mimicked) {
        try {
            final BakedModel srcModel = Minecraft.getInstance().getBlockRenderer().getBlockModel(mimicked);
            final ChunkRenderTypeSet srcTypes = srcModel.getRenderTypes(mimicked, RandomSource.create(0L), ModelData.EMPTY);

            final boolean hasOpaqueLayer = srcTypes.contains(RenderType.solid())
                    || srcTypes.contains(RenderType.cutout())
                    || srcTypes.contains(RenderType.cutoutMipped());

            // Glass-like: translucent, non-emitting, with no opaque layer at all. Only this case
            // stays see-through; everything else (opaque blocks, and light-emitting "solid glow"
            // blocks like neon concrete) renders on an opaque, alpha-tested pass.
            final boolean glassLike = srcTypes.contains(RenderType.translucent())
                    && !hasOpaqueLayer
                    && mimicked.getLightEmission() == 0;
            if (glassLike) {
                return RenderType.translucentMovingBlock();
            }

            if (srcTypes.contains(RenderType.cutoutMipped())) return RenderType.cutoutMipped();
            // Everything else renders opaque via cutout: solid/cutout blocks, and light-emitting
            // translucent-only blocks whose sampled sprite may have transparent gaps that the solid
            // pass would fill in.
            return RenderType.cutout();
        } catch (final Exception ignored) {
        }
        return RenderType.cutout();
    }

    /**
     * Used to track which one of the two is more positive
     */
    protected int getRenderPriority(final Vec3i relative) {
        return relative.getY() * 10000 + relative.getX() * 100 + relative.getZ();
    }

    @Override
    public boolean shouldRender(final GirderStrutBlockEntity blockEntity, final Vec3 cameraPos) {
        return true;
    }

    @Override
    public boolean shouldRenderOffScreen(final GirderStrutBlockEntity blockEntity) {
        return true;
    }
}
