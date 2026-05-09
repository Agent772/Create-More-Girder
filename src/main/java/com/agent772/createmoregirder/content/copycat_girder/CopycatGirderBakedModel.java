package com.agent772.createmoregirder.content.copycat_girder;

import com.agent772.createmoregirder.CMGPartialModels;
import com.simibubi.create.content.decoration.girder.GirderBlock;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
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
import java.util.EnumSet;
import java.util.List;

import static net.minecraft.core.Direction.*;

public class CopycatGirderBakedModel extends BakedModelWrapper<BakedModel> {

    public static final ModelProperty<BlockState> MIMICKED_STATE = new ModelProperty<>();
    public static final ModelProperty<Integer> FACE_ROTATION = new ModelProperty<>();
    public static final ModelProperty<EnumSet<Direction>> CONNECTED_DIRECTIONS = new ModelProperty<>();

    public static final int ORIENTATION_COUNT = 6;

    private static final long QUAD_SAMPLING_SEED = 42L;

    private static final Direction[][] FACE_MAPPINGS = {
        { DOWN,  UP,   NORTH, SOUTH, WEST, EAST },
        { UP,    DOWN, NORTH, SOUTH, EAST, WEST },
        { NORTH, SOUTH, UP,   DOWN,  WEST, EAST },
        { SOUTH, NORTH, DOWN, UP,    WEST, EAST },
        { EAST,  WEST,  NORTH, SOUTH, DOWN, UP  },
        { WEST,  EAST,  NORTH, SOUTH, UP,  DOWN },
    };

    public CopycatGirderBakedModel(BakedModel originalModel) {
        super(originalModel);
    }

    @Override
    public @NotNull ModelData getModelData(BlockAndTintGetter level, BlockPos pos, BlockState state, ModelData blockEntityData) {
        ModelData.Builder builder = ModelData.builder();

        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof MimickedBlockEntity mimicked && mimicked.hasMimickedState()) {
            builder.with(MIMICKED_STATE, mimicked.getMimickedState())
                   .with(FACE_ROTATION, mimicked.getFaceRotation());
        }

        EnumSet<Direction> connected = EnumSet.noneOf(Direction.class);
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            if (GirderBlock.isConnected(level, pos, state, direction)) {
                connected.add(direction);
            }
        }
        if (!connected.isEmpty()) {
            builder.with(CONNECTED_DIRECTIONS, connected);
        }

        return builder.build();
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, RandomSource rand,
                                    ModelData data, RenderType renderType) {
        List<BakedQuad> base = super.getQuads(state, side, rand, data, renderType);

        // Append bracket arm quads for connected directions
        EnumSet<Direction> connected = data.get(CONNECTED_DIRECTIONS);
        if (side == null && state != null && connected != null && !connected.isEmpty()) {
            base = new ArrayList<>(base);
            for (Direction direction : connected) {
                PartialModel partial = CMGPartialModels.getBracketModel(state.getBlock(), direction);
                if (partial != null) {
                    base.addAll(partial.get().getQuads(state, null, rand, data, renderType));
                }
            }
        }

        BlockState mimicked = data.get(MIMICKED_STATE);
        if (mimicked == null || mimicked.isAir() || base.isEmpty()) {
            return base;
        }
        Integer rot = data.get(FACE_ROTATION);
        int orientation = rot == null ? 0 : Math.floorMod(rot, ORIENTATION_COUNT);

        FaceData[] faceData = resolveFaceData(mimicked, orientation);
        if (faceData == null) {
            return base;
        }

        List<BakedQuad> out = new ArrayList<>(base.size());
        for (BakedQuad quad : base) {
            String spriteName = quad.getSprite().contents().name().getPath();
            if (spriteName.endsWith("bearing_hole_fixed")) {
                out.add(quad);
                continue;
            }
            Direction face = quad.getDirection();
            FaceData fd = face != null ? faceData[face.get3DDataValue()] : faceData[0];
            if (fd != null && fd.sprite != null) {
                out.add(remapQuadUVs(quad, fd.sprite, fd.lightmap, fd.shade));
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
            } catch (Exception ignored) {
            }
        }
        return super.getParticleIcon(data);
    }

    @Override
    public boolean useAmbientOcclusion() {
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
                    int[] verts = srcQuad.getVertices();
                    int vertexSize = verts.length / 4;
                    int lightmap = vertexSize > 6 ? verts[6] : 0;
                    boolean shade = srcQuad.isShade() && srcUsesAO;
                    if (emission > 0) {
                        lightmap = LightTexture.FULL_BRIGHT;
                        shade = false;
                    }
                    faces[dir.get3DDataValue()] = new FaceData(srcQuad.getSprite(), lightmap, shade);
                } else {
                    boolean shade = emission <= 0 && srcUsesAO;
                    int lightmap = emission > 0 ? LightTexture.FULL_BRIGHT : 0;
                    faces[dir.get3DDataValue()] = new FaceData(fallback, lightmap, shade);
                }
            }
            return faces;
        } catch (Exception e) {
            return null;
        }
    }

    private BakedQuad remapQuadUVs(BakedQuad orig, TextureAtlasSprite sourceSprite, int sourceLightmap, boolean shade) {
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
            if (sourceLightmap != 0 && vertexSize > 6) {
                dst[off + 6] = sourceLightmap;
            }
        }
        return new BakedQuad(dst, orig.getTintIndex(), orig.getDirection(), sourceSprite, shade);
    }
}
