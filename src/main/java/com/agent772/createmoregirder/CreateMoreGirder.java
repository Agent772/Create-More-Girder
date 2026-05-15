package com.agent772.createmoregirder;

import org.slf4j.Logger;

import com.agent772.createmoregirder.config.CMGServerConfig;
import com.agent772.createmoregirder.content.strut.GirderStrutMovementBehaviour;
import com.agent772.createmoregirder.foundation.CMGRegistryAliases;
import com.mojang.logging.LogUtils;
import com.simibubi.create.api.behaviour.movement.MovementBehaviour;
import com.simibubi.create.foundation.data.CreateRegistrate;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;


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
        CMGDataComponents.register(modEventBus);
        
        // Register blocks and block entities
        CMGBlocks.register();
        CMGBlockEntityTypes.register();
        CMGCreativeTabs.register(modEventBus);
        
        // Initialize partial models
        CMGPartialModels.init();

        // Register server config
        modContainer.registerConfig(ModConfig.Type.SERVER, CMGServerConfig.SPEC);

        // Defer movement behaviour registration until registries are bound
        modEventBus.addListener(this::onCommonSetup);

        LOGGER.info("Create: More Girder mod initialized!");
    }

    private void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            // World-save migration for the #97 and #118 girder id renames; see CMGRegistryAliases.
            CMGRegistryAliases.register();

            GirderStrutMovementBehaviour behaviour = new GirderStrutMovementBehaviour();
            for (Block block : CMGBlockEntityTypes.GIRDER_STRUT.get().getValidBlocks()) {
                MovementBehaviour.REGISTRY.register(block, behaviour);
            }
            for (Block block : CMGBlockEntityTypes.COPYCAT_GIRDER_STRUT.get().getValidBlocks()) {
                MovementBehaviour.REGISTRY.register(block, behaviour);
            }
        });
    }

    public static ResourceLocation asResource(final String s) {
        return ResourceLocation.fromNamespaceAndPath(MODID, s);
    }
}