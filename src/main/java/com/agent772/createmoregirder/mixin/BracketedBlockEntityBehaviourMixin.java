package com.agent772.createmoregirder.mixin;

import com.agent772.createmoregirder.CMGTags;
import com.agent772.createmoregirder.content.bracket.CopycatBracketAccess;
import com.agent772.createmoregirder.content.copycat_girder.MimickedStateStorage;

import com.simibubi.create.content.contraptions.StructureTransform;
import com.simibubi.create.content.decoration.bracket.BracketedBlockEntityBehaviour;
import com.simibubi.create.content.schematics.requirement.ItemRequirement;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

@Mixin(value = BracketedBlockEntityBehaviour.class, remap = false)
public abstract class BracketedBlockEntityBehaviourMixin implements CopycatBracketAccess {

    // NOTE: do NOT @Shadow the `blockEntity` field — it is declared in
    // BlockEntityBehaviour (the superclass), and Mixin cannot shadow fields
    // declared in a target's ancestor. We cast `this` to BlockEntityBehaviour
    // instead (see cmg$blockEntity()).

    @Shadow
    public abstract @Nullable BlockState getBracket();

    @Shadow
    public abstract boolean isBracketPresent();

    @Unique
    private final MimickedStateStorage cmg$mimic = new MimickedStateStorage();

    /**
     * When a mimic is cleared via wrench or apply-replace, the corresponding
     * ItemStack is stashed here so the caller (which has the Player) can
     * decide whether to return it to inventory and whether to honour creative.
     * Block-break (onRemove path) does NOT use this stash; it pops on ground
     * the same way Create itself pops the bracket.
     */
    @Unique
    private ItemStack cmg$pendingMimicReturn = ItemStack.EMPTY;

    @Unique
    private SmartBlockEntity cmg$blockEntity() {
        return ((BlockEntityBehaviour) (Object) this).blockEntity;
    }

    @Override
    public MimickedStateStorage cmg$mimickedStorage() {
        return cmg$mimic;
    }

    @Override
    public void cmg$applyMimickedState(BlockState state, int rotation) {
        cmg$mimic.adoptFrom(state, rotation);
        cmg$notifyMimicChange();
    }

    @Override
    public void cmg$clearMimickedState() {
        cmg$mimic.clear();
        cmg$notifyMimicChange();
    }

    @Override
    public boolean cmg$cycleMimickedTexture() {
        if (!cmg$mimic.cycleTexture()) return false;
        cmg$notifyMimicChange();
        return true;
    }

    @Override
    public ItemStack cmg$consumePendingMimicReturn() {
        ItemStack out = cmg$pendingMimicReturn;
        cmg$pendingMimicReturn = ItemStack.EMPTY;
        return out;
    }

    @Unique
    private void cmg$notifyMimicChange() {
        SmartBlockEntity be = cmg$blockEntity();
        if (be == null) return;
        be.notifyUpdate();
        Level world = be.getLevel();
        if (world != null) {
            BlockPos pos = be.getBlockPos();
            BlockState bs = be.getBlockState();
            world.sendBlockUpdated(pos, bs, bs, Block.UPDATE_CLIENTS);
        }
    }

    @Unique
    private boolean cmg$isCopycatBracket() {
        BlockState bracket = getBracket();
        return bracket != null && bracket.is(CMGTags.COPYCAT_BRACKET_BLOCK);
    }

    @Unique
    private ItemStack cmg$mimicStack() {
        return new ItemStack(cmg$mimic.getMimickedState().getBlock());
    }

    @Inject(method = "write", at = @At("RETURN"))
    private void cmg$writeMimic(CompoundTag nbt, HolderLookup.Provider registries, boolean clientPacket,
                                CallbackInfo ci) {
        if (isBracketPresent() && cmg$mimic.hasMimickedState()) {
            CompoundTag sub = new CompoundTag();
            cmg$mimic.write(sub);
            nbt.put("CMGMimickedBracket", sub);
        }
    }

    @Inject(method = "read", at = @At("RETURN"))
    private void cmg$readMimic(CompoundTag nbt, HolderLookup.Provider registries, boolean clientPacket,
                               CallbackInfo ci) {
        SmartBlockEntity be = cmg$blockEntity();
        Level world = be == null ? null : be.getLevel();
        if (nbt.contains("CMGMimickedBracket")) {
            cmg$mimic.read(nbt.getCompound("CMGMimickedBracket"), world);
        } else {
            cmg$mimic.clear();
        }
        if (clientPacket && be != null && world != null) {
            be.requestModelDataUpdate();
            BlockPos pos = be.getBlockPos();
            BlockState bs = be.getBlockState();
            world.sendBlockUpdated(pos, bs, bs, Block.UPDATE_CLIENTS);
        }
    }

    /**
     * When the bracket is removed:
     *  - block-break path (inOnReplacedContext == true): pop the mimic on the
     *    ground at the host pos, matching how Create pops the bracket itself.
     *  - wrench path (inOnReplacedContext == false): stash the mimic for the
     *    wrench caller to deposit into the player's inventory (creative-aware).
     */
    @Inject(method = "removeBracket", at = @At("HEAD"))
    private void cmg$handleMimicOnRemove(boolean inOnReplacedContext,
                                         CallbackInfoReturnable<BlockState> cir) {
        SmartBlockEntity be = cmg$blockEntity();
        if (be == null) return;
        if (!cmg$isCopycatBracket()) return;
        if (!cmg$mimic.hasMimickedState()) return;
        Level world = be.getLevel();
        if (world == null || world.isClientSide) {
            cmg$mimic.clear();
            return;
        }
        ItemStack drop = cmg$mimicStack();
        cmg$mimic.clear();
        if (inOnReplacedContext) {
            Block.popResource(world, be.getBlockPos(), drop);
        } else {
            cmg$pendingMimicReturn = drop;
        }
    }

    /**
     * If a bracket is being replaced with one that is NOT a copycat bracket,
     * stash the mimic for the caller (BracketBlockItem.useOn) to deposit into
     * the player's inventory.
     */
    @Inject(method = "applyBracket", at = @At("HEAD"))
    private void cmg$handleMimicOnReplace(BlockState newBracket, CallbackInfo ci) {
        SmartBlockEntity be = cmg$blockEntity();
        if (be == null) return;
        if (!cmg$mimic.hasMimickedState()) return;
        if (newBracket != null && newBracket.is(CMGTags.COPYCAT_BRACKET_BLOCK)) return;
        Level world = be.getLevel();
        if (world == null || world.isClientSide) {
            cmg$mimic.clear();
            return;
        }
        ItemStack drop = cmg$mimicStack();
        cmg$mimic.clear();
        cmg$pendingMimicReturn = drop;
    }

    /**
     * Rotate the mimicked state alongside the bracket when the host is
     * transformed (contraptions). Without this, a rotated bracket would
     * keep its old mimic facing.
     */
    @WrapOperation(method = "transformBracket",
        at = @At(value = "INVOKE",
            target = "Lcom/simibubi/create/content/decoration/bracket/BracketedBlockEntityBehaviour;applyBracket(Lnet/minecraft/world/level/block/state/BlockState;)V"))
    private void cmg$transformMimic(BracketedBlockEntityBehaviour self, BlockState transformedBracket,
                                    Operation<Void> original, StructureTransform transform) {
        original.call(self, transformedBracket);
        if (cmg$mimic.hasMimickedState() && cmg$isCopycatBracket()) {
            BlockState rotated = transform.apply(cmg$mimic.getMimickedState());
            cmg$mimic.adoptFrom(rotated, cmg$mimic.getFaceRotation());
        }
    }

    /**
     * Schematic / contraption: union the mimicked block into the bill of
     * required items so the bracket+mimic pair places correctly.
     */
    @Inject(method = "getRequiredItems", at = @At("RETURN"), cancellable = true)
    private void cmg$unionMimicRequirement(CallbackInfoReturnable<ItemRequirement> cir) {
        if (!cmg$isCopycatBracket()) return;
        if (!cmg$mimic.hasMimickedState()) return;
        ItemRequirement base = cir.getReturnValue();
        ItemRequirement mimic = new ItemRequirement(ItemRequirement.ItemUseType.CONSUME,
            new ItemStack(cmg$mimic.getMimickedState().getBlock()));
        cir.setReturnValue(base.union(mimic));
    }
}
