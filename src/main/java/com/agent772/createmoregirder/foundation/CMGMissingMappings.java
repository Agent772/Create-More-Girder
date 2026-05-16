package com.agent772.createmoregirder.foundation;

import com.agent772.createmoregirder.CreateMoreGirder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.MissingMappingsEvent;

/**
 * World-save migration for the #113 backport block id rename.
 *
 * <p>Block and item ids are stored as strings in chunk and inventory NBT, so
 * without a remap every renamed block already placed in an existing 1.20.1
 * world would load as air, and stored items would vanish. NeoForge 1.21.1 has
 * {@code Registry#addAlias} (used by {@code CMGRegistryAliases} on {@code main});
 * Forge 1.20.1 does not, so we listen for {@link MissingMappingsEvent} and remap
 * each unknown {@code createmoregirder:*_girder*} id to its renamed counterpart
 * ({@code *_truss*} / {@code *_beam*}) on world load.
 *
 * <p>{@code create_metal_girder_strut} is intentionally excluded from the rename
 * and therefore from the migration — it keeps its legacy id. Block entity type
 * ids were not renamed either, so the BLOCK_ENTITY_TYPE registry needs no
 * remapping.
 *
 * <p>{@link MissingMappingsEvent} fires on the Forge event bus (not the mod
 * bus), so the listener is registered against {@link MinecraftForge#EVENT_BUS}.
 */
public final class CMGMissingMappings {

    private CMGMissingMappings() {}

    public static void register() {
        MinecraftForge.EVENT_BUS.addListener(CMGMissingMappings::onMissingMappings);
    }

    private static void onMissingMappings(MissingMappingsEvent event) {
        remap(event, ForgeRegistries.Keys.BLOCKS, ForgeRegistries.BLOCKS);
        remap(event, ForgeRegistries.Keys.ITEMS, ForgeRegistries.ITEMS);
    }

    private static <T> void remap(MissingMappingsEvent event,
                                  ResourceKey<? extends Registry<T>> registryKey,
                                  IForgeRegistry<T> registry) {
        for (MissingMappingsEvent.Mapping<T> mapping : event.getMappings(registryKey, CreateMoreGirder.MOD_ID)) {
            String newPath = renamedPath(mapping.getKey().getPath());
            if (newPath == null) {
                continue;
            }
            ResourceLocation newId = ResourceLocation.fromNamespaceAndPath(CreateMoreGirder.MOD_ID, newPath);
            T replacement = registry.getValue(newId);
            if (replacement != null && registry.containsKey(newId)) {
                mapping.remap(replacement);
            }
        }
    }

    /**
     * Reverses the id rename: given an old id path, returns the renamed path.
     * Returns {@code null} for ids that were never renamed so Forge surfaces
     * them as genuinely missing rather than silently aliasing.
     */
    static String renamedPath(String oldPath) {
        if ("create_metal_girder_strut".equals(oldPath)) {
            return null;
        }
        if (oldPath.contains("metal_girder")) {
            return oldPath.replace("metal_girder", "beam");
        }
        if (oldPath.contains("girder")) {
            return oldPath.replace("girder", "truss");
        }
        return null;
    }
}
