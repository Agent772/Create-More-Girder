package com.agent772.createmoregirder.foundation;

import com.agent772.createmoregirder.CreateMoreGirder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

/**
 * World-save migration for the issue #97 and issue #118 block id renames.
 *
 * <p>Issue #97 renamed {@code *_girder -> *_strut_girder} and
 * {@code *_metal_girder -> *_plate_girder}.<br>
 * Issue #118 then renamed {@code *_strut_girder -> *_truss} and
 * {@code *_plate_girder -> *_beam}.
 *
 * <p>Block and item ids are stored as strings in chunk NBT, so without a remap every
 * renamed block already placed in an existing world would load as air. Registry
 * aliases make the old ids resolve to the renamed blocks/items when a save is loaded.
 *
 * <p>Each current id therefore needs aliases for <em>both</em> prior hops so that a save
 * last loaded on either the pre-#97 or the 2.0 codebase still migrates cleanly.
 *
 * <p>The same alias chain also covers Create schematics that were captured before a
 * rename. Schematics are loaded through Mojang's {@code StructureTemplate.load} with the
 * level's {@code holderLookup(Registries.BLOCK)} — that lookup is backed by the BLOCK
 * registry, which resolves aliases on every {@code getOptional(ResourceLocation)} call.
 * A schematic placed by a Schematicannon or pasted from a Schematic-and-Quill therefore
 * sees the new blocks regardless of which version captured it.
 *
 * <p>{@code create_metal_girder_strut} is intentionally excluded from both renames, so it
 * gets no alias. Block entity type ids were not renamed either, so the
 * BLOCK_ENTITY_TYPE registry needs no aliasing.
 */
public final class CMGRegistryAliases {

    private CMGRegistryAliases() {}

    /** Must run after content registration (e.g. from common setup) and before worlds load. */
    public static void register() {
        registerFor(BuiltInRegistries.BLOCK);
        registerFor(BuiltInRegistries.ITEM);
    }

    private static void registerFor(Registry<?> registry) {
        for (ResourceLocation id : registry.keySet()) {
            if (!CreateMoreGirder.MODID.equals(id.getNamespace())) {
                continue;
            }
            for (String oldPath : oldPaths(id.getPath())) {
                registry.addAlias(
                        ResourceLocation.fromNamespaceAndPath(CreateMoreGirder.MODID, oldPath), id);
            }
        }
    }

    /**
     * Reverses both the #97 and the #118 id transforms: given a current (renamed) path,
     * returns the paths it had in each prior version of the mod so worlds saved on either
     * release migrate to the current id. Returns an empty list when the id was never renamed.
     */
    private static List<String> oldPaths(String newPath) {
        // create_metal_girder_strut was excluded from both renames; it contains neither
        // "truss" nor "beam" so it naturally yields no aliases here, but the explicit
        // guard documents the exclusion and protects against accidental future matches.
        if (newPath.contains("create_metal_girder_strut")) {
            return List.of();
        }
        List<String> result = new ArrayList<>(2);
        if (newPath.contains("truss")) {
            // 2.0 id (post-#97, pre-#118) and pre-#97 id.
            result.add(newPath.replace("truss", "strut_girder"));
            result.add(newPath.replace("truss", "girder"));
        } else if (newPath.contains("beam")) {
            result.add(newPath.replace("beam", "plate_girder"));
            result.add(newPath.replace("beam", "metal_girder"));
        }
        return result;
    }
}
