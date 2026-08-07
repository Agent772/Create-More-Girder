package com.agent772.createmoregirder.config;

import net.minecraftforge.common.ForgeConfigSpec;

public final class CMGServerConfig {
    public static final ForgeConfigSpec SPEC;
    public static final ForgeConfigSpec.IntValue MAX_CONNECTIONS_PER_ANCHOR;
    public static final ForgeConfigSpec.BooleanValue CREATE_GIRDER_PLACEMENT_SYSTEM;

    static {
        ForgeConfigSpec.Builder b = new ForgeConfigSpec.Builder();
        b.push("strut");
        MAX_CONNECTIONS_PER_ANCHOR = b
                .comment("Maximum number of strut connections a single anchor block can hold.",
                         "Default 1 enforces 'one anchor = one strut'. Increase to restore legacy multi-connection behavior.")
                .defineInRange("maxConnectionsPerAnchor", 1, 1, 16);
        b.pop();

        b.push("placement");
        CREATE_GIRDER_PLACEMENT_SYSTEM = b
                .comment("Create Girder Placement System.",
                         "If true (default), CMG girders use base Create's placement/update system, so they",
                         "behave exactly like Create's metal girder:",
                         "  - TOP/BOTTOM bracket connectors only attach to bracket-compatible blocks and are",
                         "    cleared again when their support disappears.",
                         "  - Horizontal beams collapse back into poles when they lose their horizontal",
                         "    connections, and poles promote back to beams when connections appear.",
                         "If false, the CMG placement system is used instead:",
                         "  - Connectors are enabled by ANY block above/below and are never cleared by neighbor",
                         "    updates (so paved tracks keep their auto-brackets).",
                         "  - Horizontal beams never collapse back into poles via neighbor updates.",
                         "Note: changing this does not rewrite existing blockstates; each girder adopts the",
                         "active rules on its next neighbor update.")
                .define("createGirderPlacementSystem", true);
        b.pop();

        SPEC = b.build();
    }

    /**
     * Whether CMG girders should defer to base Create's placement/update system.
     * Falls back to the defined default when the config spec is not yet bound
     * (e.g. Ponder scenes / main-menu contexts with no world loaded), where
     * reading the value directly would throw.
     */
    public static boolean createGirderPlacementSystem() {
        return SPEC.isLoaded() ? CREATE_GIRDER_PLACEMENT_SYSTEM.get() : CREATE_GIRDER_PLACEMENT_SYSTEM.getDefault();
    }

    private CMGServerConfig() {}
}
