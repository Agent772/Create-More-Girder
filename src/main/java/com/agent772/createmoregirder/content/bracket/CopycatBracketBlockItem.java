package com.agent772.createmoregirder.content.bracket;

import com.agent772.createmoregirder.CMGTags;

import com.simibubi.create.content.decoration.bracket.BracketBlockItem;
import com.simibubi.create.content.decoration.bracket.BracketedBlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.EmptyBlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Bracket item variant that captures a held block as a "mimic" texture on
 * the attached bracket — see {@link CopycatBracketAccess}.
 */
public class CopycatBracketBlockItem extends BracketBlockItem {

    public CopycatBracketBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();

        BracketedBlockEntityBehaviour beh =
            BlockEntityBehaviour.get(level, pos, BracketedBlockEntityBehaviour.TYPE);

        if (player != null && player.isShiftKeyDown() && beh != null && beh.isBracketPresent()
            && isOurBracket(beh.getBracket()) && beh instanceof CopycatBracketAccess access
            && access.cmg$hasMimickedState()) {
            if (level.isClientSide) return InteractionResult.SUCCESS;
            BlockState mimic = access.cmg$getMimickedState();
            access.cmg$clearMimickedState();
            if (!player.isCreative()) {
                ItemStack returned = new ItemStack(mimic.getBlock());
                player.getInventory().placeItemBackInInventory(returned);
            }
            level.playSound(null, pos, SoundEvents.ITEM_FRAME_REMOVE_ITEM, SoundSource.BLOCKS, 1.0f, 1.0f);
            return InteractionResult.SUCCESS;
        }

        if (beh != null && beh.isBracketPresent() && isOurBracket(beh.getBracket())
            && beh instanceof CopycatBracketAccess access) {
            ItemStack offhand = player == null ? ItemStack.EMPTY
                : player.getItemInHand(InteractionHand.OFF_HAND);
            BlockState candidate = candidateMaterial(offhand);
            if (candidate != null) {
                if (level.isClientSide) return InteractionResult.SUCCESS;
                if (access.cmg$hasMimickedState()
                    && access.cmg$getMimickedState().getBlock() == candidate.getBlock()) {
                    if (access.cmg$cycleMimickedTexture()) {
                        level.playSound(null, pos, SoundEvents.ITEM_FRAME_ROTATE_ITEM, SoundSource.BLOCKS, 1.0f, 1.0f);
                    }
                    return InteractionResult.SUCCESS;
                }
                if (!access.cmg$hasMimickedState()) {
                    access.cmg$applyMimickedState(candidate, 0);
                    if (player != null && !player.isCreative()) offhand.shrink(1);
                    level.playSound(null, pos,
                        candidate.getSoundType().getPlaceSound(), SoundSource.BLOCKS, 0.75f, 1f);
                    return InteractionResult.SUCCESS;
                }
                return InteractionResult.SUCCESS;
            }
        }

        boolean hadBracketBefore = beh != null && beh.isBracketPresent();
        InteractionResult result = super.useOn(context);

        if (!level.isClientSide && result == InteractionResult.SUCCESS && !hadBracketBefore) {
            BracketedBlockEntityBehaviour after =
                BlockEntityBehaviour.get(level, pos, BracketedBlockEntityBehaviour.TYPE);
            if (after != null && after.isBracketPresent() && isOurBracket(after.getBracket())
                && after instanceof CopycatBracketAccess access && !access.cmg$hasMimickedState()
                && player != null) {
                ItemStack offhand = player.getItemInHand(InteractionHand.OFF_HAND);
                BlockState candidate = candidateMaterial(offhand);
                if (candidate != null) {
                    access.cmg$applyMimickedState(candidate, 0);
                    if (!player.isCreative()) offhand.shrink(1);
                    level.playSound(null, pos,
                        candidate.getSoundType().getPlaceSound(), SoundSource.BLOCKS, 0.75f, 1f);
                }
            }
        }
        return result;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, level, tooltipComponents, tooltipFlag);
        tooltipComponents.add(Component.translatable("tooltip.createmoregirder.copycat_bracket.placement")
            .withStyle(ChatFormatting.GOLD));
        if (net.minecraft.client.gui.screens.Screen.hasShiftDown()) {
            tooltipComponents.add(Component.translatable("tooltip.createmoregirder.copycat_bracket.cycle_detail")
                .withStyle(ChatFormatting.GOLD));
            tooltipComponents.add(Component.translatable("tooltip.createmoregirder.copycat_bracket.clear_detail")
                .withStyle(ChatFormatting.GOLD));
        } else {
            tooltipComponents.add(Component.translatable("tooltip.createmoregirder.copycat_bracket.hold_shift")
                .withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC));
        }
    }

    private boolean isOurBracket(BlockState bracket) {
        return bracket != null && bracket.is(CMGTags.COPYCAT_BRACKET_BLOCK);
    }

    private static BlockState candidateMaterial(ItemStack stack) {
        if (stack.isEmpty() || !(stack.getItem() instanceof BlockItem blockItem)) return null;
        Block block = blockItem.getBlock();
        BlockState defaultState = block.defaultBlockState();
        if (!isValidMaterial(defaultState)) return null;
        return defaultState;
    }

    public static boolean isValidMaterial(BlockState state) {
        return state.isCollisionShapeFullBlock(EmptyBlockGetter.INSTANCE, BlockPos.ZERO)
            && !state.hasBlockEntity();
    }
}
