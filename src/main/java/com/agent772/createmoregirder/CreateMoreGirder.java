package com.agent772.createmoregirder;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;
import com.simibubi.create.foundation.data.CreateRegistrate;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;


// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(CreateMoreGirder.MODID)
public class CreateMoreGirder {
    public static final String MODID = "createmoregirder";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final CreateRegistrate REGISTRATE = CreateRegistrate
            .create(MODID);


    public CreateMoreGirder(IEventBus modEventBus, ModContainer modContainer) {
        REGISTRATE.defaultCreativeTab((ResourceKey<CreativeModeTab>) null);
        REGISTRATE.registerEventListeners(modEventBus);
        
        // Register data components
        CMGDataComponents.DATA_COMPONENTS.register(modEventBus);
        
        // Register blocks and block entities
        CMGBlocks.register();
        CMGBlockEntityTypes.register();
        CMGCreativeTabs.register(modEventBus);
        
        // Initialize partial models
        CMGPartialModels.init();
        
        LOGGER.info("Create: More Girder mod initialized!");
    }
}