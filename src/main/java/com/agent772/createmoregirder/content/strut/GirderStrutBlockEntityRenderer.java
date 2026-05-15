package com.agent772.createmoregirder.content.strut;

import com.agent772.createmoregirder.content.copycat_strut.CopycatGirderStrutBlockEntity;
import com.agent772.createmoregirder.content.copycat_strut.CopycatStrutTextureRemapper;
import com.mojang.blaze3d.vertex.*;
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
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.ChunkRenderTypeSet;
import net.neoforged.neoforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;

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

        // // Render the girder strut segments (works in all graphics modes)
        // for (BlockPos pos : blockEntity.getConnectionsCopy()) {
        //     pos = pos.offset(blockEntity.getBlockPos());
        //     final BlockState state = blockEntity.getLevel().getBlockState(pos);
        //     if (!(state.getBlock() instanceof GirderStrutBlock)) {
        //         continue; // Skip if the block is not a Girder Strut
        //     }

        //     final Vec3i relative = pos.subtract(blockEntity.getBlockPos());
        //     // Calculate the length of the strut segment based on the distance to the connected block
        //     final Vec3 thisAttachment = Vec3.atCenterOf(blockEntity.getBlockPos()).relative(blockEntity.getBlockState().getValue(GirderStrutBlock.FACING), -0.4);
        //     final BlockState otherState = blockEntity.getLevel().getBlockState(pos);
        //     final Vec3 otherAttachment = Vec3.atCenterOf(pos).relative(otherState.getValue(GirderStrutBlock.FACING), -0.4);

        //     final double length = thisAttachment.distanceTo(otherAttachment);
        //     final int segments = (int) Math.ceil(length);
        //     final double lengthOffset = (length - segments) / 2.0;

        //     // Render the segments of the girder strut
        //     ms.pushPose();

        //     final Vec3 relativeVec = otherAttachment.subtract(thisAttachment);
        //     final float distHorizontal = (float) Math.sqrt(relativeVec.x() * relativeVec.x() + relativeVec.z() * relativeVec.z());
        //     final double yRot = distHorizontal == 0 ? 0 : Math.atan2(relativeVec.x(), relativeVec.z());
        //     final double xRot = (float) Math.atan2(relativeVec.y(), distHorizontal);

        //     TransformStack.of(ms)
        //             .translate(Vec3.atLowerCornerOf(blockEntity.getBlockState().getValue(GirderStrutBlock.FACING).getNormal()).scale(-0.4))
        //             .center()
        //             .rotateY((float) yRot)
        //             .rotateX(-(float) xRot)
        //             .uncenter();

        //     ms.translate(0, 0, lengthOffset + 0.5); // Adjust the translation based on segment length
        //     if (getRenderPriority(relative) > getRenderPriority(relative.multiply(-1))) {
        //         renderSegments(state, modelType, ms, segments, buffer, blockEntity.getLevel() == null ? light : LevelRenderer.getLightColor(blockEntity.getLevel(), pos));
        //     }
        //     ms.popPose();
        // }
        // Resolve copycat texture data if applicable
        CopycatStrutTextureRemapper.FaceData[] copycatFaceData = null;
        int copycatLightEmission = 0;
        RenderType beamRenderType = RenderType.cutout();
        if (blockEntity instanceof CopycatGirderStrutBlockEntity copycatBe && copycatBe.hasMimickedState()) {
            BlockState mimicked = copycatBe.getMimickedState();
            copycatFaceData = CopycatStrutTextureRemapper.resolveFaceData(mimicked, copycatBe.getFaceRotation());
            copycatLightEmission = mimicked.getLightEmission();
            beamRenderType = resolveBeamRenderType(mimicked);
        }

        boolean onContraption = blockEntity.getLevel() instanceof ContraptionWorld;

        if (onContraption || Minecraft.getInstance().options.graphicsMode().get() == GraphicsStatus.FAST) {
            // Render the girder strut segment
            for (BlockPos pos : blockEntity.getConnectionsCopy()) {
                pos = pos.offset(blockEntity.getBlockPos());
                final BlockState state = blockEntity.getLevel().getBlockState(pos);
                if (!
                        (state.getBlock() instanceof GirderStrutBlock)) {
                    continue; // Skip if the block is not a Girder Strut
                }

                final Vec3i relative = pos.subtract(blockEntity.getBlockPos());
                // Calculate the length of the strut segment based on the distance to the connected block
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

                // Render the segments of the girder strut
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

                ms.translate(0, 0, lengthOffset + 0.5); // Adjust the translation based on segment length
                if (getRenderPriority(relative) > getRenderPriority(relative.multiply(-1))) {
                    final Vec3 segDir = relativeVec.normalize();
                    renderSegments(state, modelType.getPartialModel(), ms, segments, buffer, light, onContraption ? null : blockEntity.getLevel(), thisAttachment, segDir, copycatFaceData, copycatLightEmission, beamRenderType);
                }
                ms.popPose();
            }
        } else { //use GirderStrutModelManipulator
            if (blockEntity.connectionRenderBufferCache == null) {
                try (final ByteBufferBuilder bufferBuilder = new ByteBufferBuilder(256)) {
                    final GirderStrutModelBuilder.GirderStrutModelData connectionData = GirderStrutModelBuilder.GirderStrutModelData.collect(blockEntity.getLevel(), blockEntity.getBlockPos(), blockEntity.getBlockState(), blockEntity);
                    final CopycatStrutTextureRemapper.FaceData[] finalCopycatFaceData = copycatFaceData;
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
                            .flatMap(c -> GirderStrutModelManipulator.bakeConnectionToConsumer(c, modelType, finalLighter, finalCopycatFaceData).stream())
                            .toList();

                    final BufferBuilder builder = new BufferBuilder(bufferBuilder, VertexFormat.Mode.QUADS, DefaultVertexFormat.BLOCK);

                    for (final Consumer<BufferBuilder> quad : quads) {
                        quad.accept(builder);
                    }
                    final MeshData meshData = builder.build();

                    if (meshData == null) return;

                    blockEntity.connectionRenderBufferCache = SuperBufferFactory.getInstance().create(meshData);
                }
            }
            blockEntity.connectionRenderBufferCache.renderInto(ms, buffer.getBuffer(beamRenderType));
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

    protected void renderSegments(final BlockState state, final PartialModel model, final PoseStack ms, final int length, final MultiBufferSource buffer, final int fallbackLight, final Level level, final Vec3 segmentStart, final Vec3 segmentDir, final CopycatStrutTextureRemapper.FaceData[] faceData, final int lightEmission, final RenderType renderType) {
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
            CachedBuffers.partial(model, state)
                    .light(segLight)
                    .renderInto(ms, buffer.getBuffer(renderType));
            ms.popPose();
        }
    }

    private static RenderType resolveBeamRenderType(final BlockState mimicked) {
        try {
            final BakedModel srcModel = Minecraft.getInstance().getBlockRenderer().getBlockModel(mimicked);
            final ChunkRenderTypeSet srcTypes = srcModel.getRenderTypes(mimicked, RandomSource.create(0L), ModelData.EMPTY);
            final List<RenderType> typeList = srcTypes.asList();
            if (!typeList.isEmpty()) {
                return typeList.get(0);
            }
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
    public @NotNull AABB getRenderBoundingBox(@NotNull final GirderStrutBlockEntity blockEntity) {
        return super.getRenderBoundingBox(blockEntity).inflate(GirderStrutBlock.MAX_SPAN + 2);
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
