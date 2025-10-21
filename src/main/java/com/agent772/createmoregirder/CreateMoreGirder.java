package com.agent772.createmoregirder;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;
import com.simibubi.create.AllCreativeModeTabs;
import com.simibubi.create.foundation.data.CreateRegistrate;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;


// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(CreateMoreGirder.MODID)
public class CreateMoreGirder {
    public static final String MODID = "createmoregirder";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final CreateRegistrate REGISTRATE = CreateRegistrate
            .create(MODID)
            .defaultCreativeTab(AllCreativeModeTabs.BASE_CREATIVE_TAB.getKey());

    public CreateMoreGirder(IEventBus modEventBus, ModContainer modContainer) {
        // Register the registrate to the mod event bus FIRST before registering any blocks/entities
        REGISTRATE.registerEventListeners(modEventBus);
        
        // Only register CDG blocks if CDG mod is not installed
        if (!ModList.get().isLoaded("createdieselgenerators")) {
            com.jesz.createdieselgenerators.CDGBlocks.register();
            com.jesz.createdieselgenerators.CDGBlockEntityTypes.register();
            LOGGER.info("Registered andesite girders (CDG not detected)");
        } else {
            LOGGER.info("CDG detected - skipping andesite girder registration");
        }

        CMGBlocks.register();
        CMGBlockEntityTypes.register();
        
        LOGGER.info("Create: More Girder mod initialized!");
    }
}