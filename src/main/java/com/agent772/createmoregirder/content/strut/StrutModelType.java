package com.agent772.createmoregirder.content.strut;

import com.agent772.createmoregirder.CreateMoreGirder;
import com.agent772.createmoregirder.CMGPartialModels;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.minecraft.resources.ResourceLocation;

public enum StrutModelType {
    ANDESITE(CMGPartialModels.ANDESITE_GIRDER_STRUT, CreateMoreGirder.asResource("block/andesite_truss")),
    BRASS(CMGPartialModels.BRASS_GIRDER_STRUT, CreateMoreGirder.asResource("block/brass_truss")),
    WAXED_COPPER(CMGPartialModels.WAXED_COPPER_GIRDER_STRUT, CreateMoreGirder.asResource("block/copper_truss")),
    WAXED_EXPOSED_COPPER(CMGPartialModels.WAXED_EXPOSED_COPPER_GIRDER_STRUT, CreateMoreGirder.asResource("block/exposed_copper_truss")),
    WAXED_WEATHERED_COPPER(CMGPartialModels.WAXED_WEATHERED_COPPER_GIRDER_STRUT, CreateMoreGirder.asResource("block/weathered_copper_truss")),
    WAXED_OXIDIZED_COPPER(CMGPartialModels.WAXED_OXIDIZED_COPPER_GIRDER_STRUT, CreateMoreGirder.asResource("block/oxidized_copper_truss")),
    INDUSTRIAL_IRON(CMGPartialModels.INDUSTRIAL_IRON_GIRDER_STRUT, CreateMoreGirder.asResource("block/industrial_iron_truss")),
    IRON(CMGPartialModels.IRON_GIRDER_STRUT, CreateMoreGirder.asResource("block/iron_truss")),
    WEATHERED_IRON(CMGPartialModels.WEATHERED_IRON_GIRDER_STRUT, CreateMoreGirder.asResource("block/weathered_iron_truss")),
    COPYCAT(CMGPartialModels.COPYCAT_GIRDER_STRUT, CreateMoreGirder.asResource("block/copycat_truss")),
    ANDESITE_METAL(CMGPartialModels.ANDESITE_METAL_GIRDER_STRUT, CreateMoreGirder.asResource("block/andesite_beam")),
    BRASS_METAL(CMGPartialModels.BRASS_METAL_GIRDER_STRUT, CreateMoreGirder.asResource("block/brass_beam")),
    WAXED_COPPER_METAL(CMGPartialModels.WAXED_COPPER_METAL_GIRDER_STRUT, CreateMoreGirder.asResource("block/copper_beam")),
    WAXED_EXPOSED_COPPER_METAL(CMGPartialModels.WAXED_EXPOSED_COPPER_METAL_GIRDER_STRUT, CreateMoreGirder.asResource("block/exposed_copper_beam")),
    WAXED_WEATHERED_COPPER_METAL(CMGPartialModels.WAXED_WEATHERED_COPPER_METAL_GIRDER_STRUT, CreateMoreGirder.asResource("block/weathered_copper_beam")),
    WAXED_OXIDIZED_COPPER_METAL(CMGPartialModels.WAXED_OXIDIZED_COPPER_METAL_GIRDER_STRUT, CreateMoreGirder.asResource("block/oxidized_copper_beam")),
    IRON_METAL(CMGPartialModels.IRON_METAL_GIRDER_STRUT, CreateMoreGirder.asResource("block/iron_beam")),
    WEATHERED_IRON_METAL(CMGPartialModels.WEATHERED_IRON_METAL_GIRDER_STRUT, CreateMoreGirder.asResource("block/weathered_iron_beam")),
    COPYCAT_METAL(CMGPartialModels.COPYCAT_METAL_GIRDER_STRUT, CreateMoreGirder.asResource("block/copycat_beam")),
    CREATE_METAL(CMGPartialModels.CREATE_METAL_GIRDER_STRUT, ResourceLocation.fromNamespaceAndPath("create", "block/girder"));

    private final PartialModel segmentPartial;
    private final ResourceLocation capTexture;
    /** Built once per constant; the collision descriptor is constant per variant, so it is never rebuilt. */
    private final com.cake.struts.content.StrutModelType collisionModelType;

    StrutModelType(final PartialModel segmentPartial, final ResourceLocation capTexture) {
        this.segmentPartial = segmentPartial;
        this.capTexture = capTexture;
        this.collisionModelType = new com.cake.struts.content.StrutModelType(capTexture, capTexture, 8, 12);
    }

    public PartialModel getPartialModel() {
        return segmentPartial;
    }

    public ResourceLocation getCapTexture() {
        return capTexture;
    }

    /**
     * Builds the collision-geometry descriptor consumed by Strut Your Stuff's
     * {@link com.cake.struts.content.structure.GirderStrutStructureShapes}. Only the pixel dimensions
     * are read for collision; the model/cap locations are irrelevant here because CMG keeps its own
     * renderer. Uses the library's default 8x12 cross-section so the beam collision roughly matches
     * the rendered girder.
     */
    public com.cake.struts.content.StrutModelType toCollisionModelType() {
        return collisionModelType;
    }

}
