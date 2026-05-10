package com.agent772.createmoregirder;

import com.simibubi.create.AllPartialModels;
import com.simibubi.create.Create;

import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.IdentityHashMap;
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
    public static final PartialModel COPYCAT_GIRDER_STRUT = block("copycat_girder_strut/strut");

    private static final Map<String, PartialModel[]> SEGMENT_MODELS = new HashMap<>();
    private static final Map<String, EnumMap<Direction, PartialModel>> BRACKET_MODELS = new HashMap<>();

    private static volatile Map<Block, PartialModel[]> SEGMENT_MODELS_BY_BLOCK;
    private static volatile Map<Block, EnumMap<Direction, PartialModel>> BRACKET_MODELS_BY_BLOCK;

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
            SEGMENT_MODELS.put(variant, new PartialModel[]{
                block(variant + "/segment_middle"),
                block(variant + "/segment_top"),
                block(variant + "/segment_bottom"),
                block(variant + "/segment_middle_alt")
            });

            EnumMap<Direction, PartialModel> brackets = new EnumMap<>(Direction.class);
            brackets.put(Direction.EAST, block(variant + "/bracket_east"));
            brackets.put(Direction.WEST, block(variant + "/bracket_west"));
            brackets.put(Direction.NORTH, block(variant + "/bracket_north"));
            brackets.put(Direction.SOUTH, block(variant + "/bracket_south"));
            BRACKET_MODELS.put(variant, brackets);
        }
    }

    private static Map<Block, PartialModel[]> segmentModelsByBlock() {
        Map<Block, PartialModel[]> result = SEGMENT_MODELS_BY_BLOCK;
        if (result == null) {
            result = buildBlockMap(SEGMENT_MODELS);
            SEGMENT_MODELS_BY_BLOCK = result;
        }
        return result;
    }

    private static Map<Block, EnumMap<Direction, PartialModel>> bracketModelsByBlock() {
        Map<Block, EnumMap<Direction, PartialModel>> result = BRACKET_MODELS_BY_BLOCK;
        if (result == null) {
            result = buildBlockMap(BRACKET_MODELS);
            BRACKET_MODELS_BY_BLOCK = result;
        }
        return result;
    }

    private static <V> Map<Block, V> buildBlockMap(Map<String, V> source) {
        Map<Block, V> map = new IdentityHashMap<>();
        for (var entry : source.entrySet()) {
            ResourceLocation id = ResourceLocation.fromNamespaceAndPath(CreateMoreGirder.MOD_ID, entry.getKey());
            BuiltInRegistries.BLOCK.getOptional(id).ifPresent(block -> map.put(block, entry.getValue()));
        }
        return map;
    }

    @Nullable
    public static PartialModel getSegmentModel(Block girderBlock, PartialModel original) {
        PartialModel[] models = segmentModelsByBlock().get(girderBlock);
        if (models == null) return null;

        if (original == AllPartialModels.GIRDER_SEGMENT_MIDDLE) return models[0];
        if (original == AllPartialModels.GIRDER_SEGMENT_TOP) return models[1];
        if (original == AllPartialModels.GIRDER_SEGMENT_BOTTOM) return models[2];

        return null;
    }

    @Nullable
    public static PartialModel getBracketModel(Block girderBlock, Direction direction) {
        EnumMap<Direction, PartialModel> brackets = bracketModelsByBlock().get(girderBlock);
        if (brackets == null) return null;

        return brackets.get(direction);
    }

    @Nullable
    public static PartialModel getAltMiddleModel(Block girderBlock) {
        PartialModel[] models = segmentModelsByBlock().get(girderBlock);
        if (models == null) return null;

        return models[3];
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
