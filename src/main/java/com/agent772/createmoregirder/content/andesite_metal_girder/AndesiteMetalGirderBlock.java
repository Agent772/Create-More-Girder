package com.agent772.createmoregirder.content.andesite_metal_girder;

import com.agent772.createmoregirder.CMGBlocks;
import com.agent772.createmoregirder.content.girder.CMGGirderBlock;
import net.createmod.catnip.placement.IPlacementHelper;
import net.createmod.catnip.placement.PlacementHelpers;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

/**
 * The OG-style {@code metal_girder} silhouette in andesite, and the visual base
 * for the rest of the {@code *_metal_girder} family.
 */
public class AndesiteMetalGirderBlock extends CMGGirderBlock {

    private static final int placementHelperId =
            PlacementHelpers.register(new AndesiteMetalGirderPlacementHelper());

    public AndesiteMetalGirderBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (player == null)
            return InteractionResult.PASS;
        ItemStack stack = player.getItemInHand(hand);

        InteractionResult shaftResult = tryShaftEncasing(stack, state, level, pos, player, hand, hitResult,
                CMGBlocks.ANDESITE_METAL_GIRDER_ENCASED_SHAFT);
        if (shaftResult != null)
            return shaftResult;

        InteractionResult wrenchResult = tryGirderWrenchInteraction(stack, state, level, pos, player, hitResult);
        if (wrenchResult != null)
            return wrenchResult;

        IPlacementHelper helper = PlacementHelpers.get(placementHelperId);
        if (helper.matchesItem(stack))
            return helper.getOffset(player, level, state, pos, hitResult)
                    .placeInWorld(level, (BlockItem) stack.getItem(), player, hand, hitResult);

        return InteractionResult.PASS;
    }
}
