package com.agent772.createmoregirder.content.weathered_iron_girder;

import com.agent772.createmoregirder.CMGBlockEntityTypes;
import com.agent772.createmoregirder.CMGBlocks;
import com.agent772.createmoregirder.content.andesite_girder.AndesiteGirderEncasedShaftBlock;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.decoration.girder.GirderBlock;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.schematics.requirement.ItemRequirement;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.WATERLOGGED;

public class WeatheredIronGirderEncasedShaftBlock extends AndesiteGirderEncasedShaftBlock {
    public WeatheredIronGirderEncasedShaftBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockState getRotatedBlockState(BlockState originalState, Direction targetedFace) {
        boolean hasVerticalConnection = originalState.getValue(TOP) || originalState.getValue(BOTTOM);
        if (hasVerticalConnection) {
            // Return vertical girder if encased shaft has vertical connections
            return CMGBlocks.WEATHERED_IRON_GIRDER.get().defaultBlockState()
                    .setValue(WATERLOGGED, originalState.getValue(WATERLOGGED))
                    .setValue(GirderBlock.X, false)
                    .setValue(GirderBlock.Z, false)
                    .setValue(GirderBlock.AXIS, Direction.Axis.Y)
                    .setValue(GirderBlock.BOTTOM, originalState.getValue(BOTTOM))
                    .setValue(GirderBlock.TOP, originalState.getValue(TOP));
        } else {
            // Return horizontal girder based on shaft axis
            return CMGBlocks.WEATHERED_IRON_GIRDER.get().defaultBlockState()
                    .setValue(WATERLOGGED, originalState.getValue(WATERLOGGED))
                    .setValue(GirderBlock.X, originalState.getValue(HORIZONTAL_AXIS) == Direction.Axis.Z)
                    .setValue(GirderBlock.Z, originalState.getValue(HORIZONTAL_AXIS) == Direction.Axis.X)
                    .setValue(GirderBlock.AXIS, originalState.getValue(HORIZONTAL_AXIS) == Direction.Axis.X ? Direction.Axis.Z : Direction.Axis.X)
                    .setValue(GirderBlock.BOTTOM, false)
                    .setValue(GirderBlock.TOP, false);
        }
    }

    @Override
    public ItemRequirement getRequiredItems(BlockState state, BlockEntity be) {
        return ItemRequirement.of(AllBlocks.SHAFT.getDefaultState(), be)
                .union(ItemRequirement.of(CMGBlocks.WEATHERED_IRON_GIRDER.getDefaultState(), be));
    }

    @Override
    public BlockEntityType<? extends KineticBlockEntity> getBlockEntityType() {
        return CMGBlockEntityTypes.ENCASED_WEATHERED_IRON_GIRDER.get();
    }
}