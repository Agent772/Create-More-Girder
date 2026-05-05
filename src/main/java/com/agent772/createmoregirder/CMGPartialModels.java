package com.agent772.createmoregirder;

import com.simibubi.create.AllPartialModels;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

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

    private static final Map<String, PartialModel[]> SEGMENT_MODELS = new HashMap<>();

    private static final String[] GIRDER_VARIANTS = {
        "andesite_girder",
        "brass_girder",
        "copper_girder",
        "exposed_copper_girder",
        "weathered_copper_girder",
        "oxidized_copper_girder",
        "waxed_copper_girder",
        "waxed_exposed_copper_girder",
        "waxed_weathered_copper_girder",
        "waxed_oxidized_copper_girder",
        "industrial_iron_girder",
        "weathered_iron_girder"
    };

    static {
        for (String variant : GIRDER_VARIANTS) {
            SEGMENT_MODELS.put(variant, new PartialModel[]{
                block(variant + "/segment_middle"),
                block(variant + "/segment_top"),
                block(variant + "/segment_bottom"),
                block(variant + "/segment_middle_alt")
            });
        }
    }

    @Nullable
    public static PartialModel getSegmentModel(Block girderBlock, PartialModel original) {
        ResourceLocation blockId = BuiltInRegistries.BLOCK.getKey(girderBlock);
        if (!blockId.getNamespace().equals(CreateMoreGirder.MODID)) return null;

        PartialModel[] models = SEGMENT_MODELS.get(blockId.getPath());
        if (models == null) return null;

        if (original == AllPartialModels.GIRDER_SEGMENT_MIDDLE) return models[0];
        if (original == AllPartialModels.GIRDER_SEGMENT_TOP) return models[1];
        if (original == AllPartialModels.GIRDER_SEGMENT_BOTTOM) return models[2];

        return null;
    }

    @Nullable
    public static PartialModel getAltMiddleModel(Block girderBlock) {
        ResourceLocation blockId = BuiltInRegistries.BLOCK.getKey(girderBlock);
        if (!blockId.getNamespace().equals(CreateMoreGirder.MODID)) return null;

        PartialModel[] models = SEGMENT_MODELS.get(blockId.getPath());
        if (models == null) return null;

        return models[3];
    }

    private static PartialModel block(String path) {
        return PartialModel.of(ResourceLocation.fromNamespaceAndPath(CreateMoreGirder.MODID, "block/" + path));
    }

    public static void init() {
    }
}
