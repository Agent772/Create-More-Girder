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

    @Shadow
    public abstract @Nullable BlockState getBracket();

    @Shadow
    public abstract boolean isBracketPresent();

    @Unique
    private final MimickedStateStorage cmg$mimic = new MimickedStateStorage();

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
    private void cmg$writeMimic(CompoundTag nbt, boolean clientPacket, CallbackInfo ci) {
        if (isBracketPresent() && cmg$mimic.hasMimickedState()) {
            CompoundTag sub = new CompoundTag();
            cmg$mimic.write(sub);
            nbt.put("CMGMimickedBracket", sub);
        }
    }

    @Inject(method = "read", at = @At("RETURN"))
    private void cmg$readMimic(CompoundTag nbt, boolean clientPacket, CallbackInfo ci) {
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
            // Host-break path: Create's onRemove calls removeBracket(true)
            // and we want the mimic to land on the ground alongside the
            // bracket (matching survival pickaxe / hand break behaviour).
            Block.popResource(world, be.getBlockPos(), drop);
        } else {
            // Wrench path / BlockBracketDropMixin creative path: stash the
            // mimic so the caller can decide what to do with it (drop into
            // the player's inventory, or discard in creative).
            cmg$pendingMimicReturn = drop;
        }
    }

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
