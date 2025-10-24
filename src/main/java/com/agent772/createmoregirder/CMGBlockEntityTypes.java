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

        public static final BlockEntityEntry<KineticBlockEntity> ENCASED_EXPOSED_COPPER_GIRDER = REGISTRATE
                .blockEntity("encased_exposed_copper_girder", KineticBlockEntity::new)
                .visual(() -> ShaftVisual::new, false)
                .validBlocks(CMGBlocks.EXPOSED_COPPER_GIRDER_ENCASED_SHAFT)
                .renderer(() -> ShaftRenderer::new)
                .register();

        public static final BlockEntityEntry<KineticBlockEntity> ENCASED_WEATHERED_COPPER_GIRDER = REGISTRATE
                .blockEntity("encased_weathered_copper_girder", KineticBlockEntity::new)
                .visual(() -> ShaftVisual::new, false)
                .validBlocks(CMGBlocks.WEATHERED_COPPER_GIRDER_ENCASED_SHAFT)
                .renderer(() -> ShaftRenderer::new)
                .register();

        public static final BlockEntityEntry<KineticBlockEntity> ENCASED_OXIDIZED_COPPER_GIRDER = REGISTRATE
                .blockEntity("encased_oxidized_copper_girder", KineticBlockEntity::new)
                .visual(() -> ShaftVisual::new, false)
                .validBlocks(CMGBlocks.OXIDIZED_COPPER_GIRDER_ENCASED_SHAFT)
                .renderer(() -> ShaftRenderer::new)
                .register();

        public static final BlockEntityEntry<KineticBlockEntity> ENCASED_WAXED_COPPER_GIRDER = REGISTRATE
                .blockEntity("encased_waxed_copper_girder", KineticBlockEntity::new)
                .visual(() -> ShaftVisual::new, false)
                .validBlocks(CMGBlocks.WAXED_COPPER_GIRDER_ENCASED_SHAFT)
                .renderer(() -> ShaftRenderer::new)
                .register();

        public static final BlockEntityEntry<KineticBlockEntity> ENCASED_WAXED_EXPOSED_COPPER_GIRDER = REGISTRATE
                .blockEntity("encased_waxed_exposed_copper_girder", KineticBlockEntity::new)
                .visual(() -> ShaftVisual::new, false)
                .validBlocks(CMGBlocks.WAXED_EXPOSED_COPPER_GIRDER_ENCASED_SHAFT)
                .renderer(() -> ShaftRenderer::new)
                .register();

        public static final BlockEntityEntry<KineticBlockEntity> ENCASED_WAXED_WEATHERED_COPPER_GIRDER = REGISTRATE
                .blockEntity("encased_waxed_weathered_copper_girder", KineticBlockEntity::new)
                .visual(() -> ShaftVisual::new, false)
                .validBlocks(CMGBlocks.WAXED_WEATHERED_COPPER_GIRDER_ENCASED_SHAFT)
                .renderer(() -> ShaftRenderer::new)
                .register();

        public static final BlockEntityEntry<KineticBlockEntity> ENCASED_WAXED_OXIDIZED_COPPER_GIRDER = REGISTRATE
                .blockEntity("encased_waxed_oxidized_copper_girder", KineticBlockEntity::new)
                .visual(() -> ShaftVisual::new, false)
                .validBlocks(CMGBlocks.WAXED_OXIDIZED_COPPER_GIRDER_ENCASED_SHAFT)
                .renderer(() -> ShaftRenderer::new)
                .register();

        public static final BlockEntityEntry<KineticBlockEntity> ENCASED_INDUSTRIAL_IRON_GIRDER = REGISTRATE
                .blockEntity("encased_industrial_iron_girder", KineticBlockEntity::new)
                .visual(() -> ShaftVisual::new, false)
                .validBlocks(CMGBlocks.INDUSTRIAL_IRON_GIRDER_ENCASED_SHAFT)
                .renderer(() -> ShaftRenderer::new)
                .register();

        public static final BlockEntityEntry<KineticBlockEntity> ENCASED_WEATHERED_IRON_GIRDER = REGISTRATE
                .blockEntity("encased_weathered_iron_girder", KineticBlockEntity::new)
                .visual(() -> ShaftVisual::new, false)
                .validBlocks(CMGBlocks.WEATHERED_IRON_GIRDER_ENCASED_SHAFT)
                .renderer(() -> ShaftRenderer::new)
                .register(); 

    public static void register() {
    }
}