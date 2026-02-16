package com.agent772.createmoregirder;

import com.mojang.logging.LogUtils;
import com.simibubi.create.foundation.data.CreateRegistrate;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;


// The value here should match an entry in the META-INF/mods.toml file
@Mod(CreateMoreGirder.MOD_ID)
public class CreateMoreGirder {
    public static final String MOD_ID = "createmoregirder";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(MOD_ID);

    public CreateMoreGirder() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        REGISTRATE.defaultCreativeTab((ResourceKey<CreativeModeTab>) null);
        REGISTRATE.registerEventListeners(modEventBus);
        
        // Register blocks and block entities
        CMGBlocks.register();
        CMGBlockEntityTypes.register();
        CMGCreativeTabs.register(modEventBus);
        
        // Initialize partial models
        CMGPartialModels.init();
        
        LOGGER.info("Create: More Girder mod initialized!");
    }

    public static ResourceLocation asResource(final String s) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, s);
    }
}