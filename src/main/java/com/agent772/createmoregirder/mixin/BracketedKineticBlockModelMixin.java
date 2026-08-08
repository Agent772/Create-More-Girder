package com.agent772.createmoregirder.mixin;

import com.agent772.createmoregirder.CMGTags;
import com.agent772.createmoregirder.content.bracket.CopycatBracketAccess;
import com.agent772.createmoregirder.content.bracket.CopycatBracketBakedModel;
import com.agent772.createmoregirder.content.bracket.CopycatBracketModelProperties;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.simibubi.create.content.decoration.bracket.BracketedBlockEntityBehaviour;
import com.simibubi.create.content.kinetics.simpleRelays.BracketedKineticBlockModel;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;

import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.ChunkRenderTypeSet;
import net.minecraftforge.client.model.BakedModelWrapper;
import net.minecraftforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * Enrich the host's {@link ModelData} with the copycat bracket's mimicked
 * state so it flows through {@link BracketedKineticBlockModel#getQuads} into
 * the wrapper baked model registered on the bracket block.
 *
 * <p>Also overrides {@code getRenderTypes}: the chunk renderer only iterates
 * the passes the <em>host</em> model advertises (the wrapped shaft/cog model),
 * never consulting the bracket model. Without the union a mimicked translucent
 * glow shell has no pass to render on and the bracket collapses to a single
 * opaque layer (issue #167 — "bracket brighter than the original mod").
 */
@Mixin(value = BracketedKineticBlockModel.class, remap = false)
public abstract class BracketedKineticBlockModelMixin extends BakedModelWrapper<BakedModel> {

    protected BracketedKineticBlockModelMixin(BakedModel originalModel) {
        super(originalModel);
    }

    @Override
    public @NotNull ChunkRenderTypeSet getRenderTypes(@NotNull BlockState state, @NotNull RandomSource rand,
                                                      @NotNull ModelData data) {
        return CopycatBracketBakedModel.unionWithMimicTypes(super.getRenderTypes(state, rand, data), rand, data);
    }

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
