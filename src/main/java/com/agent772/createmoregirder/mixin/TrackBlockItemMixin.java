package com.agent772.createmoregirder.mixin;

import com.agent772.createmoregirder.CMGBezierData;
import com.agent772.createmoregirder.CMGTags;
import com.simibubi.create.content.trains.track.BezierConnection;
import com.simibubi.create.content.trains.track.TrackBlockItem;
import com.simibubi.create.content.trains.track.TrackPlacement;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = TrackBlockItem.class)
public abstract class TrackBlockItemMixin {

    @Redirect(
        method = "useOn",
        at = @At(value = "INVOKE", target = "Lcom/tterrag/registrate/util/entry/BlockEntry;isIn(Lnet/minecraft/world/item/ItemStack;)Z", ordinal = 0, remap = false)
    )
    private boolean cmg$isGirderItem(BlockEntry<?> entry, ItemStack stack) {
        if (stack.getItem() instanceof BlockItem blockItem
                && blockItem.getBlock().defaultBlockState().is(CMGTags.PAVING_GIRDER)) {
            return true;
        }
        return entry.isIn(stack);
    }

    @Redirect(
        method = "useOn",
        at = @At(value = "INVOKE", target = "Lcom/simibubi/create/content/trains/track/TrackPlacement;tryConnect(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/item/ItemStack;ZZ)Lcom/simibubi/create/content/trains/track/TrackPlacement$PlacementInfo;", remap = false)
    )
    private TrackPlacement.PlacementInfo cmg$storeCurveGirder(
        net.minecraft.world.level.Level level,
        Player player,
        net.minecraft.core.BlockPos pos,
        BlockState state,
        ItemStack trackStack,
        boolean hasGirder,
        boolean secondEnd
    ) {
        TrackPlacement.PlacementInfo info = TrackPlacement.tryConnect(
            level, player, pos, state, trackStack, hasGirder, secondEnd
        );

        if (info != null && hasGirder) {
            BezierConnection curve = ((PlacementInfoAccessor) info).getCurve();
            if (curve != null) {
                ItemStack offhand = player.getOffhandItem();
                if (offhand.getItem() instanceof BlockItem bi) {
                    Block girderBlock = bi.getBlock();
                    if (girderBlock.defaultBlockState().is(CMGTags.PAVING_GIRDER)) {
                        ((CMGBezierData) curve).cmg$setGirderBlock(girderBlock);
                    }
                }
            }
        }

        return info;
    }
}
