package com.agent772.createmoregirder.content.girder;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.content.decoration.girder.GirderBlock;
import com.simibubi.create.content.decoration.girder.GirderEncasedShaftBlock;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.schematics.requirement.ItemRequirement;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.WATERLOGGED;

/**
 * Shared base for the encased-shaft variant of every CMG girder, regardless of
 * variant. Implements the common wrench bracket toggle, shaft-removal rotation
 * and schematic requirements; subclasses only supply the two per-variant values
 * via {@link #getGirderBlock()} and {@link #getEncasedBEType()}.
 */
public abstract class CMGGirderEncasedShaftBlock extends GirderEncasedShaftBlock {

    public CMGGirderEncasedShaftBlock(Properties properties) {
        super(properties);
    }

    /**
     * The girder block this encased shaft turns back into when the shaft is
     * wrenched out.
     */
    protected abstract BlockEntry<? extends Block> getGirderBlock();

    /**
     * The block-entity type bound to this encased-shaft variant.
     */
    protected abstract BlockEntityType<? extends KineticBlockEntity> getEncasedBEType();

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (player != null) {
            ItemStack stack = player.getItemInHand(hand);
            if (AllItems.WRENCH.isIn(stack) && !player.isShiftKeyDown()) {
                if (CMGGirderWrenchBehaviour.handleClick(level, pos, state, hitResult))
                    return InteractionResult.sidedSuccess(level.isClientSide);
            }
        }
        return super.use(state, level, pos, player, hand, hitResult);
    }

    @Override
    public BlockState getRotatedBlockState(BlockState originalState, Direction targetedFace) {
        boolean hasVerticalConnection = originalState.getValue(TOP) || originalState.getValue(BOTTOM);
        if (hasVerticalConnection) {
            // Return vertical girder if encased shaft has vertical connections
            return getGirderBlock().get().defaultBlockState()
                    .setValue(WATERLOGGED, originalState.getValue(WATERLOGGED))
                    .setValue(GirderBlock.X, false)
                    .setValue(GirderBlock.Z, false)
                    .setValue(GirderBlock.AXIS, Direction.Axis.Y)
                    .setValue(GirderBlock.BOTTOM, originalState.getValue(BOTTOM))
                    .setValue(GirderBlock.TOP, originalState.getValue(TOP));
        } else {
            // Return horizontal girder based on shaft axis
            return getGirderBlock().get().defaultBlockState()
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
                .union(ItemRequirement.of(getGirderBlock().getDefaultState(), be));
    }

    @Override
    public BlockEntityType<? extends KineticBlockEntity> getBlockEntityType() {
        return getEncasedBEType();
    }
}
