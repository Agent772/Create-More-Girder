package com.agent772.createmoregirder.content.strut;

import com.agent772.createmoregirder.CreateMoreGirder;
import com.agent772.createmoregirder.CMGPartialModels;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.minecraft.resources.ResourceLocation;

public enum StrutModelType {
    ANDESITE(CMGPartialModels.ANDESITE_GIRDER_STRUT, CreateMoreGirder.asResource("block/andesite_girder")),
    BRASS(CMGPartialModels.BRASS_GIRDER_STRUT, CreateMoreGirder.asResource("block/brass_girder")),
    WAXED_COPPER(CMGPartialModels.WAXED_COPPER_GIRDER_STRUT, CreateMoreGirder.asResource("block/copper_girder")),
    WAXED_EXPOSED_COPPER(CMGPartialModels.WAXED_EXPOSED_COPPER_GIRDER_STRUT, CreateMoreGirder.asResource("block/exposed_copper_girder")),
    WAXED_WEATHERED_COPPER(CMGPartialModels.WAXED_WEATHERED_COPPER_GIRDER_STRUT, CreateMoreGirder.asResource("block/weathered_copper_girder")),
    WAXED_OXIDIZED_COPPER(CMGPartialModels.WAXED_OXIDIZED_COPPER_GIRDER_STRUT, CreateMoreGirder.asResource("block/oxidized_copper_girder")),
    INDUSTRIAL_IRON(CMGPartialModels.INDUSTRIAL_IRON_GIRDER_STRUT, CreateMoreGirder.asResource("block/industrial_iron_girder")),
    WEATHERED_IRON(CMGPartialModels.WEATHERED_IRON_GIRDER_STRUT, CreateMoreGirder.asResource("block/weathered_iron_girder"));

    private final PartialModel segmentPartial;
    private final ResourceLocation capTexture;

    StrutModelType(final PartialModel segmentPartial, final ResourceLocation capTexture) {
        this.segmentPartial = segmentPartial;
        this.capTexture = capTexture;
    }

    public PartialModel getPartialModel() {
        return segmentPartial;
    }

    public ResourceLocation getCapTexture() {
        return capTexture;
    }

}
