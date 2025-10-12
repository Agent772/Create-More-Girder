package com.agent772.createmoregirder;

import com.simibubi.create.content.kinetics.base.*;
import com.tterrag.registrate.util.entry.BlockEntityEntry;

import static com.agent772.createmoregirder.CreateMoreGirder.REGISTRATE;

public class CMGBlockEntityTypes {

    public static final BlockEntityEntry<KineticBlockEntity> ENCASED_BRASS_GIRDER = REGISTRATE
            .blockEntity("encased_brass_girder", KineticBlockEntity::new)
            .visual(() -> ShaftVisual::new, false)
            .validBlocks(CMGBlocks.BRASS_GIRDER_ENCASED_SHAFT)
            .renderer(() -> ShaftRenderer::new)
            .register();

    public static final BlockEntityEntry<KineticBlockEntity> ENCASED_COPPER_GIRDER = REGISTRATE
            .blockEntity("encased_copper_girder", KineticBlockEntity::new)
            .visual(() -> ShaftVisual::new, false)
            .validBlocks(CMGBlocks.COPPER_GIRDER_ENCASED_SHAFT)
            .renderer(() -> ShaftRenderer::new)
            .register();

    public static void register() {
    }
}