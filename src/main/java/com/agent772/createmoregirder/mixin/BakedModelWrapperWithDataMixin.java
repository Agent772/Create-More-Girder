package com.agent772.createmoregirder.mixin;

import com.agent772.createmoregirder.CMGTags;
import com.agent772.createmoregirder.content.bracket.CopycatBracketAccess;
import com.agent772.createmoregirder.content.bracket.CopycatBracketModelProperties;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.simibubi.create.content.decoration.bracket.BracketedBlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.model.BakedModelWrapperWithData;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.data.ModelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * Universal copycat-bracket mimic enrichment for any baked model that extends
 * {@link BakedModelWrapperWithData}. The wrapper's {@code getModelData} method
 * is {@code final}, so this single hook fires for every subclass (Create's own
 * {@code PipeAttachmentModel}, factory-panel/table-cloth models, and — most
 * importantly — third-party Create-derived models like the ones shipped by
 * mods that extend Create's pipe/cog/shaft families).
 *
 * Replaces the narrower {@code PipeAttachmentModelMixin}: by mixing into the
 * abstract parent we no longer have to chase every individual mod's custom
 * pipe/attachment model class.
 */
@Mixin(value = BakedModelWrapperWithData.class, remap = false)
public abstract class BakedModelWrapperWithDataMixin {

    @ModifyReturnValue(
        method = "getModelData(Lnet/minecraft/world/level/BlockAndTintGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/neoforged/neoforge/client/model/data/ModelData;)Lnet/neoforged/neoforge/client/model/data/ModelData;",
        at = @At("RETURN"))
    private ModelData cmg$attachMimicProperties(ModelData original,
                                                BlockAndTintGetter world,
                                                BlockPos pos,
                                                BlockState state,
                                                ModelData blockEntityData) {
        BracketedBlockEntityBehaviour beh =
            BlockEntityBehaviour.get(world, pos, BracketedBlockEntityBehaviour.TYPE);
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
