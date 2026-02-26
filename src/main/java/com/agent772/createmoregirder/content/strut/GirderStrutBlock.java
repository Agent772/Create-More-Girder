package com.agent772.createmoregirder.content.strut;

import com.agent772.createmoregirder.CMGShapes;
import com.agent772.createmoregirder.CMGBlockEntityTypes;

import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.IBE;
import com.tterrag.registrate.util.nullness.NonNullFunction;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.storage.loot.LootParams;

import java.util.List;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


import static net.minecraft.world.level.block.state.properties.BlockStateProperties.WATERLOGGED;
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
 */
public class GirderStrutBlock extends Block implements IBE<GirderStrutBlockEntity>, SimpleWaterloggedBlock, IWrenchable {

    public static final DirectionProperty FACING = DirectionalBlock.FACING;
    public static final int MAX_SPAN = 8;

    private StrutModelType modelType;

    public GirderStrutBlock(final Properties properties, final StrutModelType modelType) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(FACING, Direction.UP).setValue(WATERLOGGED, false));
        this.modelType = modelType;

    }

    @Override
    protected BlockState rotate(final BlockState state, final Rotation rotation) {
        return super.rotate(state, rotation)
                .setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    protected BlockState mirror(final BlockState state, final Mirror mirror) {
        return super.mirror(state, mirror)
                .setValue(FACING, mirror.mirror(state.getValue(FACING)));
    }

    @Override
    public InteractionResult onWrenched(final BlockState state, final UseOnContext context) {
        return InteractionResult.PASS;
    }

    @Override
    public InteractionResult onSneakWrenched(final BlockState state, final UseOnContext context) {
        final Level level = context.getLevel();
        final BlockPos pos = context.getClickedPos();
        if (!level.isClientSide) {
            destroyConnectedStrut(level, pos, false);
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

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        List<ItemStack> drops = super.getDrops(state, builder);
        // If loot table didn't work, manually create the drops
        if (drops.isEmpty()) {
            ItemStack stack = new ItemStack(this, 2);
            drops.add(stack);
        }
            drops.stream().mapToInt(ItemStack::getCount).sum();
        return drops;
    }

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
                destroyConnectedStrut(level, pos, true);
            }
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Override
    public Class<GirderStrutBlockEntity> getBlockEntityClass() {
        return GirderStrutBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends GirderStrutBlockEntity> getBlockEntityType() {
        return CMGBlockEntityTypes.GIRDER_STRUT.get();
    }

    public StrutModelType getModelType() {
        return modelType;
    }

    public void setModelType(final StrutModelType modelType) {
        this.modelType = modelType;
    }

    // Variant constructors
    public static NonNullFunction<Properties, GirderStrutBlock> andesite() {
        return properties -> new GirderStrutBlock(properties, StrutModelType.ANDESITE);
    }

    public static NonNullFunction<Properties, GirderStrutBlock> brass() {
        return properties -> new GirderStrutBlock(properties, StrutModelType.BRASS);
    }

    public static NonNullFunction<Properties, GirderStrutBlock> waxed_copper() {
        return properties -> new GirderStrutBlock(properties, StrutModelType.WAXED_COPPER);
    }

    public static NonNullFunction<Properties, GirderStrutBlock> waxed_exposed_copper() {
        return properties -> new GirderStrutBlock(properties, StrutModelType.WAXED_EXPOSED_COPPER);
    }

    public static NonNullFunction<Properties, GirderStrutBlock> waxed_weathered_copper() {
        return properties -> new GirderStrutBlock(properties, StrutModelType.WAXED_WEATHERED_COPPER);
    }

    public static NonNullFunction<Properties, GirderStrutBlock> waxed_oxidized_copper() {
        return properties -> new GirderStrutBlock(properties, StrutModelType.WAXED_OXIDIZED_COPPER);
    }

    public static NonNullFunction<Properties, GirderStrutBlock> industrial_iron() {
        return properties -> new GirderStrutBlock(properties, StrutModelType.INDUSTRIAL_IRON);
    }

    public static NonNullFunction<Properties, GirderStrutBlock> weathered_iron() {
        return properties -> new GirderStrutBlock(properties, StrutModelType.WEATHERED_IRON);
    }
}
