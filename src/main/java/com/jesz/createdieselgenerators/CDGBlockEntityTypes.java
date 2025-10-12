package com.jesz.createdieselgenerators;

import com.simibubi.create.content.kinetics.base.*;
import com.tterrag.registrate.util.entry.BlockEntityEntry;

import static com.agent772.createmoregirder.CreateMoreGirder.REGISTRATE;

public class CDGBlockEntityTypes {

    public static final BlockEntityEntry<KineticBlockEntity> ENCASED_GIRDER = REGISTRATE
            .blockEntity("encased_girder", KineticBlockEntity::new)
            .visual(() -> ShaftVisual::new, false)
            .validBlocks(CDGBlocks.ANDESITE_GIRDER_ENCASED_SHAFT)
            .renderer(() -> ShaftRenderer::new)
            .register();
    public static void register() {
    }
}
