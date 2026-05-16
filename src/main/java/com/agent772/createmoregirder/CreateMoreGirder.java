package com.agent772.createmoregirder;

import com.agent772.createmoregirder.config.CMGServerConfig;
import com.agent772.createmoregirder.content.strut.GirderStrutMovementBehaviour;
import com.agent772.createmoregirder.foundation.CMGMissingMappings;
import com.mojang.logging.LogUtils;
import com.simibubi.create.api.behaviour.movement.MovementBehaviour;
import com.simibubi.create.foundation.data.CreateRegistrate;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
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

        // World-save migration for renamed block/item ids (issue #113).
        // MissingMappingsEvent fires on the Forge event bus and the listener
        // wires itself up there directly.
        CMGMissingMappings.register();

        // Register blocks and block entities
        CMGBlocks.register();
        CMGBlockEntityTypes.register();
        CMGCreativeTabs.register(modEventBus);

        // Initialize partial models
        CMGPartialModels.init();

        // Register server config
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, CMGServerConfig.SPEC);

        // Defer movement behaviour registration until registries are bound
        modEventBus.addListener(this::onCommonSetup);

        LOGGER.info("Create: More Girder mod initialized!");
    }

    private void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            GirderStrutMovementBehaviour behaviour = new GirderStrutMovementBehaviour();
            for (Block block : CMGBlockEntityTypes.GIRDER_STRUT.get().validBlocks) {
                MovementBehaviour.REGISTRY.register(block, behaviour);
            }
            for (Block block : CMGBlockEntityTypes.COPYCAT_GIRDER_STRUT.get().validBlocks) {
                MovementBehaviour.REGISTRY.register(block, behaviour);
            }
        });
    }

    public static ResourceLocation asResource(final String s) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, s);
    }
}
