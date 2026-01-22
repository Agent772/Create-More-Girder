package com.agent772.createmoregirder.content.strut;

import com.agent772.createmoregirder.CMGShapes;
import com.simibubi.create.AllTags;
import com.simibubi.create.content.decoration.girder.GirderBlock;
import com.simibubi.create.content.equipment.wrench.IWrenchable;

import com.simibubi.create.foundation.block.IBE;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

/**
 * Base Girder Strut Block implementation
 * 
 * Adapted from Bits-n-Bobs by Industrialists-Of-Create
 * Original: https://github.com/Industrialists-Of-Create/Bits-n-Bobs
 * Licensed under MIT License
 * 
 * Modifications:
 * - Adapted for Create: More Girder mod structure
 * - Added variant system for different girder types
 * - Simplified to use FAST rendering only
 */
public class GirderStrutBlock extends Block implements IBE<GirderStrutBlockEntity>, SimpleWaterloggedBlock, IWrenchable {

    public static final DirectionProperty FACING = DirectionalBlock.FACING;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final int MAX_SPAN = 8;

    public GirderStrutBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(FACING, Direction.UP).setValue(WATERLOGGED, false));
    }

    @Override
    public InteractionResult onWrenched(final BlockState state, final UseOnContext context) {
        return InteractionResult.PASS;
    }

    @Override
    public InteractionResult onSneakWrenched(final BlockState state, final UseOnContext context) {
        final Player player = context.getPlayer();
        final Level level = context.getLevel();
        final BlockPos pos = context.getClickedPos();
        if (!level.isClientSide) {
            final BlockState currentState = level.getBlockState(pos);
            final ItemStack itemToReturn = new ItemStack(currentState.getBlock());
            destroyConnectedStrut(level, pos, false);
            if (player != null && !player.hasInfiniteMaterials())
                player.getInventory().placeItemBackInInventory(itemToReturn);
        }
        return IWrenchable.super.onSneakWrenched(state, context);
    }

    @Override
    public @NotNull BlockState updateShape(final BlockState state, final @NotNull Direction direction, final @NotNull BlockState neighbourState, final @NotNull LevelAccessor world,
                                           final @NotNull BlockPos pos, final @NotNull BlockPos neighbourPos) {
        if (state.getValue(WATERLOGGED))
            world.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
        return state;
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : Fluids.EMPTY.defaultFluidState();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, WATERLOGGED);
        super.createBlockStateDefinition(builder);
    }

    // @Nullable
    // @Override
    // public BlockState getStateForPlacement(BlockPlaceContext context) {
    //     FluidState fluidState = context.getLevel().getFluidState(context.getClickedPos());
    //     return defaultBlockState()
    //             .setValue(FACING, context.getClickedFace())
    //             .setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
    // }
    @Nullable
    @Override
    public BlockState getStateForPlacement(final BlockPlaceContext context) {
        final Level level = context.getLevel();
        final BlockPos pos = context.getClickedPos();
        final FluidState ifluidstate = level.getFluidState(pos);
        final BlockState state = super.getStateForPlacement(context);
        if (state == null)
            return null;
        return state.setValue(FACING, context.getClickedFace()).setValue(WATERLOGGED, ifluidstate.getType() == Fluids.WATER);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return CMGShapes.GIRDER_STRUT.get(state.getValue(FACING));
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Shapes.empty();
    }

    @Override
    public PushReaction getPistonPushReaction(BlockState state) {
        return PushReaction.NORMAL;
    }

    @Override
    public @NotNull BlockState playerWillDestroy(final @NotNull Level level, final @NotNull BlockPos pos, final @NotNull BlockState state, final Player player) {
        final boolean shouldPreventDrops = player.hasInfiniteMaterials();

        if (shouldPreventDrops && !level.isClientSide) {
            destroyConnectedStrut(level, pos, false);
        }

        return super.playerWillDestroy(level, pos, state, player);
    }

    // private void destroyConnectedStrut(final Level level, final BlockPos pos, final boolean dropBlock) {
    //     withBlockEntityDo(level, pos, (gbe) -> {
    //         for (BlockPos otherPos : gbe.getConnectionsCopy()) {
    //             otherPos = otherPos.offset(pos);
    //             if (level.getBlockEntity(otherPos) instanceof final GirderStrutBlockEntity other) {
    //                 other.removeConnection(pos);
    //                 if (other.connectionCount() == 0) {
    //                     level.destroyBlock(otherPos, dropBlock);
    //                 }
    //             }
    //         }
    //     });
    // }

    private void destroyConnectedStrut(final Level level, final BlockPos pos, final boolean dropBlock) {
        withBlockEntityDo(level, pos, (gbe) -> {
            for (BlockPos otherPos : gbe.getConnectionsCopy()) {
                otherPos = otherPos.offset(pos);
                final BlockEntity otherBe = level.getBlockEntity(otherPos);
                if (otherBe instanceof final GirderStrutBlockEntity other) {
                    other.removeConnection(pos);
                    if (other.connectionCount() == 0) {
                        level.destroyBlock(otherPos, dropBlock);
                    }
                }
            }
        });
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            if (!level.isClientSide) {
                // Only destroy connected struts without dropping items
                destroyConnectedStrut(level, pos, true);
            }
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }

    public String getTextureName() {
        return "andesite_girder"; // Override in subclasses for different textures
    }

    public PartialModel getPartialModel() {
        return com.agent772.createmoregirder.CMGPartialModels.ANDESITE_GIRDER_STRUT; // Override in subclasses
    }

    @Override
    public Class<GirderStrutBlockEntity> getBlockEntityClass() {
        return GirderStrutBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends GirderStrutBlockEntity> getBlockEntityType() {
        return com.agent772.createmoregirder.CMGBlockEntityTypes.GIRDER_STRUT.get();
    }
}
