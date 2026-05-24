package com.agent772.createmoregirder.mixin;

import com.agent772.createmoregirder.CMGTags;
import com.agent772.createmoregirder.content.bracket.CopycatBracketAccess;
import com.agent772.createmoregirder.content.bracket.CopycatBracketModelProperties;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.simibubi.create.content.decoration.bracket.BracketedBlockEntityBehaviour;
import com.simibubi.create.content.kinetics.simpleRelays.BracketedKineticBlockModel;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = BracketedKineticBlockModel.class, remap = false)
public abstract class BracketedKineticBlockModelMixin {

    @ModifyReturnValue(
        method = "getModelData(Lnet/minecraft/world/level/BlockAndTintGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraftforge/client/model/data/ModelData;)Lnet/minecraftforge/client/model/data/ModelData;",
        at = @At("RETURN"))
    private ModelData cmg$attachMimicProperties(ModelData original,
                                                BlockAndTintGetter world,
                                                BlockPos pos,
                                                BlockState state,
                                                ModelData blockEntityData) {
        BracketedBlockEntityBehaviour beh = BlockEntityBehaviour.get(world, pos, BracketedBlockEntityBehaviour.TYPE);
        if (beh == null) return original;
        BlockState bracket = beh.getBracket();
        if (bracket == null || !bracket.is(CMGTags.COPYCAT_BRACKET_BLOCK)) return original;
        if (!(beh instanceof CopycatBracketAccess access) || !access.cmg$hasMimickedState()) return original;

        return original.derive()
            .with(CopycatBracketModelProperties.MIMICKED_STATE, access.cmg$getMimickedState())
            .with(CopycatBracketModelProperties.FACE_ROTATION, access.cmg$getFaceRotation())
            .build();
    }
}
