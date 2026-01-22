package com.agent772.createmoregirder;

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

    public static final PartialModel ANDESITE_GIRDER_STRUT = block("girder_strut/andesite_girder_strut");

    private static PartialModel block(String path) {
        return PartialModel.of(ResourceLocation.fromNamespaceAndPath(CreateMoreGirder.MODID, "block/" + path));
    }

    public static void init() {
        // Called to ensure static initialization
    }
}


