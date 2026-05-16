package com.agent772.createmoregirder.content.strut;

import com.simibubi.create.foundation.model.BakedQuadHelper;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.model.BakedModelWrapper;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.data.ModelProperty;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GirderStrutModelBuilder extends BakedModelWrapper<BakedModel> {

    public static final ModelProperty<Boolean> ANCHOR_OFFSET_PROPERTY = new ModelProperty<>();
    private static final double SURFACE_OFFSET = (6 / 16f) + 1e-3;
    private static final double FACE_OFFSET = (10 / 16f) + 1e-3;

    public GirderStrutModelBuilder(BakedModel originalModel) {
        super(originalModel);
    }

    @Override
    public @NotNull ModelData getModelData(BlockAndTintGetter level, BlockPos pos, BlockState state, ModelData blockEntityData) {
        boolean shouldOffset = state.getBlock() instanceof GirderStrutBlock
                && GirderStrutAnchorOffset.shouldOffset(level, pos, state.getValue(GirderStrutBlock.FACING));

        if (level.getBlockEntity(pos) instanceof GirderStrutBlockEntity blockEntity) {
            blockEntity.connectionRenderBufferCache = null; // Invalidate cache on model data request
        }

        return ModelData.builder()
                .with(ANCHOR_OFFSET_PROPERTY, shouldOffset)
                .build();
    }

    @Override
    public List<BakedQuad> getQuads(BlockState state, Direction side, RandomSource rand, ModelData data, RenderType renderType) {
        List<BakedQuad> base = super.getQuads(state, side, rand, data, renderType);
        if (data == null || !(state.getBlock() instanceof GirderStrutBlock)) {
            return base;
        }
        Boolean offsetFlag = data.get(ANCHOR_OFFSET_PROPERTY);
        if (!Boolean.TRUE.equals(offsetFlag) || base.isEmpty()) {
            return base;
        }
        Direction facing = state.getValue(GirderStrutBlock.FACING);
        Vec3 offset = Vec3.atLowerCornerOf(facing.getNormal()).scale(-GirderStrutAnchorOffset.OFFSET_BLOCKS);
        List<BakedQuad> translated = new ArrayList<>(base.size());
        for (BakedQuad quad : base) {
            translated.add(translateQuad(quad, offset));
        }
        return translated;
    }

    private static BakedQuad translateQuad(BakedQuad quad, Vec3 offset) {
        int[] vertices = quad.getVertices().clone();
        for (int v = 0; v < 4; v++) {
            Vec3 xyz = BakedQuadHelper.getXYZ(vertices, v);
            BakedQuadHelper.setXYZ(vertices, v, xyz.add(offset));
        }
        return new BakedQuad(vertices, quad.getTintIndex(), quad.getDirection(), quad.getSprite(), quad.isShade());
    }

    @Override
    public boolean useAmbientOcclusion(BlockState state) {
        return false;
    }

    @Override
    public boolean useAmbientOcclusion(BlockState state, RenderType renderType) {
        return false;
    }

    @Override
    public boolean useAmbientOcclusion() {
        return false;
    }

    @Override
    public boolean usesBlockLight() {
        return true;
    }

    static final class GirderStrutModelData {
        private final List<GirderConnection> connections;
        private final BlockPos pos;

        private GirderStrutModelData(List<GirderConnection> connections, BlockPos pos) {
            this.connections = connections;
            this.pos = pos;
        }

        public BlockPos getPos() {
            return pos;
        }

        List<GirderConnection> connections() {
            return connections;
        }

        static GirderStrutModelData collect(BlockAndTintGetter level, BlockPos pos, BlockState state, GirderStrutBlockEntity blockEntity) {
            if (!(state.getBlock() instanceof GirderStrutBlock)) {
                return new GirderStrutModelData(List.of(), pos);
            }
            Direction facing = state.getValue(GirderStrutBlock.FACING);
            boolean thisOffset = GirderStrutAnchorOffset.shouldOffset(level, pos, facing);
            double thisExtra = thisOffset ? GirderStrutAnchorOffset.OFFSET_BLOCKS : 0;
            Vec3 blockOrigin = Vec3.atLowerCornerOf(pos);
            Vec3 facePoint = Vec3.atCenterOf(pos).relative(facing, -(FACE_OFFSET + thisExtra));
            Vec3 thisSurface = Vec3.atCenterOf(pos).relative(facing, -(SURFACE_OFFSET + thisExtra));

            List<GirderConnection> connections = new ArrayList<>();

            for (BlockPos otherPos : blockEntity.getConnectionsCopy()) {
                otherPos = otherPos.offset(pos);
                BlockState otherState = level.getBlockState(otherPos);
                if (!(otherState.getBlock() instanceof GirderStrutBlock)) {
                    continue;
                }
                Direction otherFacing = otherState.getValue(GirderStrutBlock.FACING);
                boolean otherOffset = GirderStrutAnchorOffset.shouldOffset(level, otherPos, otherFacing);
                double otherExtra = otherOffset ? GirderStrutAnchorOffset.OFFSET_BLOCKS : 0;
                Vec3 otherSurface = Vec3.atCenterOf(otherPos).relative(otherFacing, -(SURFACE_OFFSET + otherExtra));
                Vec3 span = otherSurface.subtract(thisSurface);
                if (span.lengthSqr() < 1.0e-4) {
                    continue;
                }
                Vec3 halfVector = span.scale(0.5);
                double renderLength = halfVector.length() + 0.5f;
                if (renderLength <= 1.0e-4) {
                    continue;
                }

                Vec3 direction = halfVector.normalize();
                Vec3 startLocal = thisSurface.subtract(blockOrigin);
                Vec3 planePointLocal = facePoint.subtract(blockOrigin);

                connections.add(new GirderConnection(
                    startLocal,
                    direction,
                    renderLength,
                    planePointLocal,
                    Vec3.atLowerCornerOf(facing.getNormal())
                ));
            }

            return new GirderStrutModelData(Collections.unmodifiableList(connections), pos);
        }
    }

    static final class GirderConnection {
        private final Vec3 start;
        private final Vec3 direction;
        private final double renderLength;
        private final Vec3 surfacePlanePoint;
        private final Vec3 surfaceNormal;

        GirderConnection(Vec3 start, Vec3 direction, double renderLength, Vec3 surfacePlanePoint, Vec3 surfaceNormal) {
            this.start = start;
            this.direction = direction;
            this.renderLength = renderLength;
            this.surfacePlanePoint = surfacePlanePoint;
            this.surfaceNormal = surfaceNormal;
        }

        Vec3 start() {
            return start;
        }

        Vec3 direction() {
            return direction;
        }

        double renderLength() {
            return renderLength;
        }

        Vec3 surfacePlanePoint() {
            return surfacePlanePoint;
        }

        Vec3 surfaceNormal() {
            return surfaceNormal;
        }
    }
}
