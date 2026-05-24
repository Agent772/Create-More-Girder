package com.agent772.createmoregirder.content.bracket;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelProperty;

/**
 * ModelProperties carrying the mimicked state for copycat brackets.
 * They are populated on the host's ModelData via a mixin into
 * {@code BracketedKineticBlockModel#getModelData}, and consumed by
 * {@link CopycatBracketBakedModel}.
 */
public final class CopycatBracketModelProperties {

    public static final ModelProperty<BlockState> MIMICKED_STATE = new ModelProperty<>();
    public static final ModelProperty<Integer> FACE_ROTATION = new ModelProperty<>();

    private CopycatBracketModelProperties() {}
}
