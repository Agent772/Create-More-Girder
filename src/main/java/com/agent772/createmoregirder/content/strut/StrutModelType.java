package com.agent772.createmoregirder.content.strut;

import com.agent772.createmoregirder.CreateMoreGirder;
import com.agent772.createmoregirder.CMGPartialModels;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.minecraft.resources.ResourceLocation;

public enum StrutModelType {
    ANDESITE(CMGPartialModels.ANDESITE_GIRDER_STRUT, CreateMoreGirder.asResource("block/andesite_strut_girder")),
    BRASS(CMGPartialModels.BRASS_GIRDER_STRUT, CreateMoreGirder.asResource("block/brass_strut_girder")),
    WAXED_COPPER(CMGPartialModels.WAXED_COPPER_GIRDER_STRUT, CreateMoreGirder.asResource("block/copper_strut_girder")),
    WAXED_EXPOSED_COPPER(CMGPartialModels.WAXED_EXPOSED_COPPER_GIRDER_STRUT, CreateMoreGirder.asResource("block/exposed_copper_strut_girder")),
    WAXED_WEATHERED_COPPER(CMGPartialModels.WAXED_WEATHERED_COPPER_GIRDER_STRUT, CreateMoreGirder.asResource("block/weathered_copper_strut_girder")),
    WAXED_OXIDIZED_COPPER(CMGPartialModels.WAXED_OXIDIZED_COPPER_GIRDER_STRUT, CreateMoreGirder.asResource("block/oxidized_copper_strut_girder")),
    INDUSTRIAL_IRON(CMGPartialModels.INDUSTRIAL_IRON_GIRDER_STRUT, CreateMoreGirder.asResource("block/industrial_iron_strut_girder")),
    WEATHERED_IRON(CMGPartialModels.WEATHERED_IRON_GIRDER_STRUT, CreateMoreGirder.asResource("block/weathered_iron_strut_girder")),
    COPYCAT(CMGPartialModels.COPYCAT_GIRDER_STRUT, CreateMoreGirder.asResource("block/copycat_strut_girder")),
    COPYCAT_METAL(CMGPartialModels.COPYCAT_METAL_GIRDER_STRUT, CreateMoreGirder.asResource("block/copycat_plate_girder")),
    ANDESITE_METAL(CMGPartialModels.ANDESITE_METAL_GIRDER_STRUT, CreateMoreGirder.asResource("block/andesite_plate_girder")),
    BRASS_METAL(CMGPartialModels.BRASS_METAL_GIRDER_STRUT, CreateMoreGirder.asResource("block/brass_plate_girder")),
    WAXED_COPPER_METAL(CMGPartialModels.WAXED_COPPER_METAL_GIRDER_STRUT, CreateMoreGirder.asResource("block/copper_plate_girder")),
    WAXED_EXPOSED_COPPER_METAL(CMGPartialModels.WAXED_EXPOSED_COPPER_METAL_GIRDER_STRUT, CreateMoreGirder.asResource("block/exposed_copper_plate_girder")),
    WAXED_WEATHERED_COPPER_METAL(CMGPartialModels.WAXED_WEATHERED_COPPER_METAL_GIRDER_STRUT, CreateMoreGirder.asResource("block/weathered_copper_plate_girder")),
    WAXED_OXIDIZED_COPPER_METAL(CMGPartialModels.WAXED_OXIDIZED_COPPER_METAL_GIRDER_STRUT, CreateMoreGirder.asResource("block/oxidized_copper_plate_girder")),
    WEATHERED_IRON_METAL(CMGPartialModels.WEATHERED_IRON_METAL_GIRDER_STRUT, CreateMoreGirder.asResource("block/weathered_iron_plate_girder")),
    CREATE_METAL(CMGPartialModels.CREATE_METAL_GIRDER_STRUT, ResourceLocation.fromNamespaceAndPath("create", "block/girder"));

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
