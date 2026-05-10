package com.agent772.createmoregirder.mixin;

import com.agent772.createmoregirder.CMGBezierData;
import com.simibubi.create.content.trains.track.BezierConnection;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = BezierConnection.class, remap = false)
public abstract class BezierConnectionMixin implements CMGBezierData {

    @Unique
    @Nullable
    private Block cmg$girderBlock;

    @Override
    @Nullable
    public Block cmg$getGirderBlock() {
        return cmg$girderBlock;
    }

    @Override
    public void cmg$setGirderBlock(@Nullable Block block) {
        cmg$girderBlock = block;
    }

    @Inject(method = "write(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/nbt/CompoundTag;", at = @At("RETURN"))
    private void cmg$writeNbt(BlockPos pos, CallbackInfoReturnable<CompoundTag> cir) {
        if (cmg$girderBlock != null) {
            ResourceLocation key = BuiltInRegistries.BLOCK.getKey(cmg$girderBlock);
            cir.getReturnValue().putString("CMGGirder", key.toString());
        }
    }

    @Inject(method = "<init>(Lnet/minecraft/nbt/CompoundTag;Lnet/minecraft/core/BlockPos;)V", at = @At("RETURN"))
    private void cmg$readNbt(CompoundTag tag, BlockPos pos, CallbackInfo ci) {
        if (tag.contains("CMGGirder")) {
            ResourceLocation id = new ResourceLocation(tag.getString("CMGGirder"));
            BuiltInRegistries.BLOCK.getOptional(id).ifPresent(b -> cmg$girderBlock = b);
        }
    }
}
