package com.agent772.createmoregirder.mixin;

import com.agent772.createmoregirder.content.bracket.CopycatBracketBakedModel;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.simibubi.create.content.fluids.PipeAttachmentModel;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.ChunkRenderTypeSet;
import net.minecraftforge.client.model.data.ModelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * Union the mimicked block's render types into the pipe host's render-type
 * set. The chunk renderer only iterates the passes the host model advertises
 * — {@link PipeAttachmentModel#getRenderTypes} unions pipe/casing/attachment
 * types but never the bracket's — so without this a mimicked translucent glow
 * shell on a copycat bracket has no pass to render on (issue #167).
 */
@Mixin(value = PipeAttachmentModel.class, remap = false)
public abstract class PipeAttachmentModelMixin {

    @ModifyReturnValue(
        method = "getRenderTypes(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/util/RandomSource;Lnet/minecraftforge/client/model/data/ModelData;)Lnet/minecraftforge/client/ChunkRenderTypeSet;",
        at = @At("RETURN"))
    private ChunkRenderTypeSet cmg$unionMimicRenderTypes(ChunkRenderTypeSet original,
                                                         BlockState state,
                                                         RandomSource rand,
                                                         ModelData data) {
        return CopycatBracketBakedModel.unionWithMimicTypes(original, rand, data);
    }
}
