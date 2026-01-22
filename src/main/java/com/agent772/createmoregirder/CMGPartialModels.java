package com.agent772.createmoregirder;

import com.simibubi.create.Create;

import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.minecraft.resources.ResourceLocation;

/**
 * Partial Models for Create: More Girder
 * 
 * Adapted from Bits-n-Bobs by Industrialists-Of-Create
 * Original: https://github.com/Industrialists-Of-Create/Bits-n-Bobs
 * Licensed under MIT License
 */
public class CMGPartialModels {

    public static final PartialModel ANDESITE_GIRDER_STRUT = block("andesite_girder_strut/strut");
    public static final PartialModel BRASS_GIRDER_STRUT = block("brass_girder_strut/strut");
    public static final PartialModel WAXED_COPPER_GIRDER_STRUT = block("waxed_copper_girder_strut/strut");
    public static final PartialModel WAXED_EXPOSED_COPPER_GIRDER_STRUT = block("waxed_exposed_copper_girder_strut/strut");
    public static final PartialModel WAXED_WEATHERED_COPPER_GIRDER_STRUT = block("waxed_weathered_copper_girder_strut/strut");
    public static final PartialModel WAXED_OXIDIZED_COPPER_GIRDER_STRUT = block("waxed_oxidized_copper_girder_strut/strut");
    public static final PartialModel INDUSTRIAL_IRON_GIRDER_STRUT = block("industrial_iron_girder_strut/strut");
    public static final PartialModel WEATHERED_IRON_GIRDER_STRUT = block("weathered_iron_girder_strut/strut");

    private static PartialModel block(String path) {
        return PartialModel.of(ResourceLocation.fromNamespaceAndPath(CreateMoreGirder.MODID, "block/" + path));
    }

    private static PartialModel createBlock(String path) {
        return PartialModel.of(Create.asResource("block/" + path));
    }

    public static void init() {
        // Called to ensure static initialization
    }

    public static void register() {
    }
}


