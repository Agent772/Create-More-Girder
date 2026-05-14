package com.agent772.createmoregirder.foundation;

import com.agent772.createmoregirder.CreateMoreGirder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;

/**
 * World-save migration for the issue #97 block id rename
 * ({@code *_girder -> *_strut_girder}, {@code *_metal_girder -> *_plate_girder}).
 *
 * <p>Block and item ids are stored as strings in chunk NBT, so without a remap every
 * renamed block already placed in an existing world would load as air. Registry
 * aliases make the old ids resolve to the renamed blocks/items when a save is loaded.
 *
 * <p>{@code create_metal_girder_strut} is intentionally excluded from the rename, so it
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
            String oldPath = oldPath(id.getPath());
            if (!oldPath.equals(id.getPath())) {
                registry.addAlias(
                        ResourceLocation.fromNamespaceAndPath(CreateMoreGirder.MODID, oldPath), id);
            }
        }
    }

    /**
     * Reverses the #97 id transform: given a current (renamed) path, returns the path it
     * had before the rename, or the same path when it was never renamed.
     */
    private static String oldPath(String newPath) {
        // create_metal_girder_strut was excluded from the rename: it contains neither
        // "plate_girder" nor "strut_girder", so it already falls through unchanged below.
        // This explicit guard just documents that exclusion.
        if (newPath.contains("create_metal_girder_strut")) {
            return newPath;
        }
        if (newPath.contains("plate_girder")) {
            return newPath.replace("plate_girder", "metal_girder");
        }
        if (newPath.contains("strut_girder")) {
            return newPath.replace("strut_girder", "girder");
        }
        return newPath;
    }
}
