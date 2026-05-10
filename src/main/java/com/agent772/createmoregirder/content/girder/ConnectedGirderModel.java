package com.agent772.createmoregirder.content.girder;

import com.agent772.createmoregirder.CMGPartialModels;
import com.simibubi.create.content.decoration.girder.GirderBlock;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.ChunkRenderTypeSet;
import net.minecraftforge.client.model.BakedModelWrapper;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.data.ModelProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class ConnectedGirderModel extends BakedModelWrapper<BakedModel> {

    private static final ModelProperty<ConnectionData> CONNECTION_PROPERTY = new ModelProperty<>();

    public ConnectedGirderModel(BakedModel originalModel) {
        super(originalModel);
    }

    @Override
    public @NotNull ModelData getModelData(@NotNull BlockAndTintGetter level, @NotNull BlockPos pos,
                                           @NotNull BlockState state, @NotNull ModelData modelData) {
        ConnectionData connectionData = new ConnectionData();
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            if (GirderBlock.isConnected(level, pos, state, direction)) {
                connectionData.connected.add(direction);
            }
        }
        return modelData.derive().with(CONNECTION_PROPERTY, connectionData).build();
    }

    @Override
    public ChunkRenderTypeSet getRenderTypes(@NotNull BlockState state, @NotNull RandomSource rand,
                                             @NotNull ModelData data) {
        ChunkRenderTypeSet baseTypes = super.getRenderTypes(state, rand, data);
        ConnectionData connectionData = data.get(CONNECTION_PROPERTY);
        if (connectionData == null || connectionData.connected.isEmpty()) {
            return baseTypes;
        }

        List<ChunkRenderTypeSet> sets = new ArrayList<>();
        sets.add(baseTypes);
        for (Direction direction : connectionData.connected) {
            PartialModel partial = CMGPartialModels.getBracketModel(state.getBlock(), direction);
            if (partial != null) {
                sets.add(partial.get().getRenderTypes(state, rand, data));
            }
        }
        return ChunkRenderTypeSet.union(sets);
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, RandomSource rand,
                                    ModelData data, @Nullable RenderType renderType) {
        List<BakedQuad> quads = super.getQuads(state, side, rand, data, renderType);

        if (side != null || !data.has(CONNECTION_PROPERTY)) {
            return quads;
        }

        ConnectionData connectionData = data.get(CONNECTION_PROPERTY);
        if (connectionData == null || connectionData.connected.isEmpty()) {
            return quads;
        }

        List<BakedQuad> result = new ArrayList<>(quads);
        for (Direction direction : connectionData.connected) {
            PartialModel partial = CMGPartialModels.getBracketModel(state.getBlock(), direction);
            if (partial != null) {
                result.addAll(partial.get().getQuads(state, null, rand, data, renderType));
            }
        }
        return result;
    }

    private static class ConnectionData {
        final EnumSet<Direction> connected = EnumSet.noneOf(Direction.class);
    }
}
