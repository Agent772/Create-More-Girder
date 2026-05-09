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
    public static final PartialModel COPYCAT_GIRDER_STRUT = block("copycat_girder_strut/strut");

    private static final java.util.Map<String, java.util.EnumMap<net.minecraft.core.Direction, PartialModel>> BRACKET_MODELS = new java.util.HashMap<>();
    private static volatile java.util.Map<net.minecraft.world.level.block.Block, java.util.EnumMap<net.minecraft.core.Direction, PartialModel>> BRACKET_MODELS_BY_BLOCK;

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
        "weathered_iron_girder",
        "copycat_girder"
    };

    static {
        for (String variant : GIRDER_VARIANTS) {
            java.util.EnumMap<net.minecraft.core.Direction, PartialModel> brackets = new java.util.EnumMap<>(net.minecraft.core.Direction.class);
            brackets.put(net.minecraft.core.Direction.EAST, block(variant + "/bracket_east"));
            brackets.put(net.minecraft.core.Direction.WEST, block(variant + "/bracket_west"));
            brackets.put(net.minecraft.core.Direction.NORTH, block(variant + "/bracket_north"));
            brackets.put(net.minecraft.core.Direction.SOUTH, block(variant + "/bracket_south"));
            BRACKET_MODELS.put(variant, brackets);
        }
    }

    private static java.util.Map<net.minecraft.world.level.block.Block, java.util.EnumMap<net.minecraft.core.Direction, PartialModel>> bracketModelsByBlock() {
        java.util.Map<net.minecraft.world.level.block.Block, java.util.EnumMap<net.minecraft.core.Direction, PartialModel>> result = BRACKET_MODELS_BY_BLOCK;
        if (result == null) {
            java.util.Map<net.minecraft.world.level.block.Block, java.util.EnumMap<net.minecraft.core.Direction, PartialModel>> map = new java.util.IdentityHashMap<>();
            for (var entry : BRACKET_MODELS.entrySet()) {
                net.minecraft.resources.ResourceLocation id = ResourceLocation.fromNamespaceAndPath(CreateMoreGirder.MOD_ID, entry.getKey());
                net.minecraft.core.registries.BuiltInRegistries.BLOCK.getOptional(id).ifPresent(block -> map.put(block, entry.getValue()));
            }
            BRACKET_MODELS_BY_BLOCK = map;
            result = map;
        }
        return result;
    }

    @org.jetbrains.annotations.Nullable
    public static PartialModel getBracketModel(net.minecraft.world.level.block.Block girderBlock, net.minecraft.core.Direction direction) {
        java.util.EnumMap<net.minecraft.core.Direction, PartialModel> brackets = bracketModelsByBlock().get(girderBlock);
        if (brackets == null) return null;
        return brackets.get(direction);
    }

    private static PartialModel block(String path) {
        return PartialModel.of(ResourceLocation.fromNamespaceAndPath(CreateMoreGirder.MOD_ID, "block/" + path));
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


