package com.agent772.createmoregirder.content.copycat_strut;

import com.agent772.createmoregirder.content.strut.GirderStrutBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.BakedModelWrapper;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.data.ModelProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CopycatGirderStrutBakedModel extends BakedModelWrapper<BakedModel> {

    public static final ModelProperty<BlockState> MIMICKED_STATE = new ModelProperty<>();
    public static final ModelProperty<Integer> FACE_ROTATION = new ModelProperty<>();

    private static final long QUAD_SAMPLING_SEED = 42L;

    private static final Direction[][] FACE_MAPPINGS = {
        { Direction.DOWN, Direction.UP, Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST },
        { Direction.UP, Direction.DOWN, Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST },
        { Direction.NORTH, Direction.SOUTH, Direction.UP, Direction.DOWN, Direction.WEST, Direction.EAST },
        { Direction.SOUTH, Direction.NORTH, Direction.DOWN, Direction.UP, Direction.WEST, Direction.EAST },
        { Direction.EAST, Direction.WEST, Direction.NORTH, Direction.SOUTH, Direction.DOWN, Direction.UP },
        { Direction.WEST, Direction.EAST, Direction.NORTH, Direction.SOUTH, Direction.UP, Direction.DOWN },
    };

    public CopycatGirderStrutBakedModel(BakedModel originalModel) {
        super(originalModel);
    }

    @Override
    public @NotNull ModelData getModelData(BlockAndTintGetter level, BlockPos pos, BlockState state, ModelData blockEntityData) {
        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof CopycatGirderStrutBlockEntity copycatBe && copycatBe.hasMimickedState()) {
            copycatBe.connectionRenderBufferCache = null;
            return ModelData.builder()
                    .with(MIMICKED_STATE, copycatBe.getMimickedState())
                    .with(FACE_ROTATION, copycatBe.getFaceRotation())
                    .build();
        }
        if (be instanceof GirderStrutBlockEntity strutBe) {
            strutBe.connectionRenderBufferCache = null;
        }
        return ModelData.EMPTY;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, RandomSource rand,
                                    ModelData data, RenderType renderType) {
        List<BakedQuad> base = super.getQuads(state, side, rand, data, renderType);

        BlockState mimicked = data.get(MIMICKED_STATE);
        if (mimicked == null || mimicked.isAir() || base.isEmpty()) {
            return base;
        }
        Integer rot = data.get(FACE_ROTATION);
        int orientation = rot == null ? 0 : Math.floorMod(rot, 6);

        FaceData[] faceData = resolveFaceData(mimicked, orientation);
        if (faceData == null) {
            return base;
        }

        List<BakedQuad> out = new ArrayList<>(base.size());
        for (BakedQuad quad : base) {
            Direction face = quad.getDirection();
            FaceData fd = face != null ? faceData[face.get3DDataValue()] : faceData[0];
            if (fd != null && fd.sprite != null) {
                out.add(remapQuadUVs(quad, fd.sprite, fd.shade));
            } else {
                out.add(quad);
            }
        }
        return out;
    }

    @Override
    public TextureAtlasSprite getParticleIcon(@NotNull ModelData data) {
        BlockState mimicked = data.get(MIMICKED_STATE);
        if (mimicked != null && !mimicked.isAir()) {
            try {
                BakedModel src = Minecraft.getInstance().getBlockRenderer().getBlockModel(mimicked);
                TextureAtlasSprite particle = src.getParticleIcon(ModelData.EMPTY);
                if (particle != null) {
                    return particle;
                }
            } catch (Exception ignored) {}
        }
        return super.getParticleIcon(data);
    }

    @Override
    public boolean useAmbientOcclusion() {
        return false;
    }

    @Override
    public boolean usesBlockLight() {
        return true;
    }

    private record FaceData(TextureAtlasSprite sprite, int lightmap, boolean shade) {}

    @Nullable
    private FaceData[] resolveFaceData(BlockState mimicked, int orientation) {
        try {
            BakedModel srcModel = Minecraft.getInstance().getBlockRenderer().getBlockModel(mimicked);
            TextureAtlasSprite fallback = srcModel.getParticleIcon(ModelData.EMPTY);
            FaceData[] faces = new FaceData[6];
            Direction[] mapping = FACE_MAPPINGS[orientation];
            int emission = mimicked.getLightEmission();
            boolean srcUsesAO = srcModel.useAmbientOcclusion();

            for (Direction dir : Direction.values()) {
                Direction sourceFace = mapping[dir.get3DDataValue()];
                List<BakedQuad> quads = srcModel.getQuads(mimicked, sourceFace,
                        RandomSource.create(QUAD_SAMPLING_SEED), ModelData.EMPTY, null);
                if (!quads.isEmpty()) {
                    BakedQuad srcQuad = quads.get(0);
                    boolean shade = srcQuad.isShade() && srcUsesAO;
                    if (emission > 0) shade = false;
                    faces[dir.get3DDataValue()] = new FaceData(srcQuad.getSprite(), 0, shade);
                } else {
                    boolean shade = emission <= 0 && srcUsesAO;
                    faces[dir.get3DDataValue()] = new FaceData(fallback, 0, shade);
                }
            }
            return faces;
        } catch (Exception e) {
            return null;
        }
    }

    private BakedQuad remapQuadUVs(BakedQuad orig, TextureAtlasSprite sourceSprite, boolean shade) {
        TextureAtlasSprite girderSprite = orig.getSprite();
        int[] src = orig.getVertices();
        int[] dst = src.clone();
        int vertexSize = dst.length / 4;
        float gU0 = girderSprite.getU0();
        float gV0 = girderSprite.getV0();
        float gUSpan = girderSprite.getU1() - gU0;
        float gVSpan = girderSprite.getV1() - gV0;
        if (gUSpan == 0f || gVSpan == 0f) {
            return orig;
        }
        float sU0 = sourceSprite.getU0();
        float sV0 = sourceSprite.getV0();
        float sUSpan = sourceSprite.getU1() - sU0;
        float sVSpan = sourceSprite.getV1() - sV0;
        for (int v = 0; v < 4; v++) {
            int off = v * vertexSize;
            float u = Float.intBitsToFloat(dst[off + 4]);
            float vv = Float.intBitsToFloat(dst[off + 5]);
            float fu = (u - gU0) / gUSpan;
            float fv = (vv - gV0) / gVSpan;
            dst[off + 4] = Float.floatToRawIntBits(sU0 + fu * sUSpan);
            dst[off + 5] = Float.floatToRawIntBits(sV0 + fv * sVSpan);
        }
        return new BakedQuad(dst, orig.getTintIndex(), orig.getDirection(), sourceSprite, shade);
    }
}
