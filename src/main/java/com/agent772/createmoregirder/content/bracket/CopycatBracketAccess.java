package com.agent772.createmoregirder.content.bracket;

import com.agent772.createmoregirder.content.copycat_girder.MimickedStateStorage;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Interface exposed by {@link com.simibubi.create.content.decoration.bracket.BracketedBlockEntityBehaviour}
 * via mixin so that copycat-bracket logic (item, baked model, drop handlers)
 * can read and mutate the mimicked block state stored alongside the bracket.
 */
public interface CopycatBracketAccess {

    MimickedStateStorage cmg$mimickedStorage();

    default BlockState cmg$getMimickedState() {
        return cmg$mimickedStorage().getMimickedState();
    }

    default boolean cmg$hasMimickedState() {
        return cmg$mimickedStorage().hasMimickedState();
    }

    default int cmg$getFaceRotation() {
        return cmg$mimickedStorage().getFaceRotation();
    }

    void cmg$applyMimickedState(BlockState state, int rotation);

    void cmg$clearMimickedState();

    boolean cmg$cycleMimickedTexture();

    ItemStack cmg$consumePendingMimicReturn();
}
