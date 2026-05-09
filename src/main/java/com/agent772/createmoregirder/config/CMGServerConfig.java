package com.agent772.createmoregirder.config;

import net.minecraftforge.common.ForgeConfigSpec;

public final class CMGServerConfig {
    public static final ForgeConfigSpec SPEC;
    public static final ForgeConfigSpec.IntValue MAX_CONNECTIONS_PER_ANCHOR;

    static {
        ForgeConfigSpec.Builder b = new ForgeConfigSpec.Builder();
        b.push("strut");
        MAX_CONNECTIONS_PER_ANCHOR = b
                .comment("Maximum number of strut connections a single anchor block can hold.",
                         "Default 1 enforces 'one anchor = one strut'. Increase to restore legacy multi-connection behavior.")
                .defineInRange("maxConnectionsPerAnchor", 1, 1, 16);
        b.pop();
        SPEC = b.build();
    }

    private CMGServerConfig() {}
}
