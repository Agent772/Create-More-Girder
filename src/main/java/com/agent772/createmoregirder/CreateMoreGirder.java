package com.agent772.createmoregirder;

import com.agent772.createmoregirder.config.CMGServerConfig;
import com.agent772.createmoregirder.content.strut.GirderStrutMovementBehaviour;
import com.mojang.logging.LogUtils;
import com.simibubi.create.api.behaviour.movement.MovementBehaviour;
import com.simibubi.create.foundation.data.CreateRegistrate;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
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
            // Register for all strut block variants
            MovementBehaviour.REGISTRY.register(CMGBlocks.ANDESITE_GIRDER_STRUT.get(), behaviour);
            MovementBehaviour.REGISTRY.register(CMGBlocks.BRASS_GIRDER_STRUT.get(), behaviour);
            MovementBehaviour.REGISTRY.register(CMGBlocks.WAXED_COPPER_GIRDER_STRUT.get(), behaviour);
            MovementBehaviour.REGISTRY.register(CMGBlocks.WAXED_EXPOSED_COPPER_GIRDER_STRUT.get(), behaviour);
            MovementBehaviour.REGISTRY.register(CMGBlocks.WAXED_WEATHERED_COPPER_GIRDER_STRUT.get(), behaviour);
            MovementBehaviour.REGISTRY.register(CMGBlocks.WAXED_OXIDIZED_COPPER_GIRDER_STRUT.get(), behaviour);
            MovementBehaviour.REGISTRY.register(CMGBlocks.INDUSTRIAL_IRON_GIRDER_STRUT.get(), behaviour);
            MovementBehaviour.REGISTRY.register(CMGBlocks.WEATHERED_IRON_GIRDER_STRUT.get(), behaviour);
            MovementBehaviour.REGISTRY.register(CMGBlocks.COPYCAT_GIRDER_STRUT.get(), behaviour);
        });
    }

    public static ResourceLocation asResource(final String s) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, s);
    }
}
