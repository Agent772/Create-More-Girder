package com.agent772.createmoregirder;

import com.agent772.createmoregirder.content.brass_girder.BrassGirderBlock;
import com.agent772.createmoregirder.content.brass_girder.BrassGirderEncasedShaftBlock;
import com.agent772.createmoregirder.content.copper_girder.CopperGirderBlock;
import com.agent772.createmoregirder.content.copper_girder.CopperGirderEncasedShaftBlock;
import com.agent772.createmoregirder.content.copper_girder.ExposedCopperGirderBlock;
import com.agent772.createmoregirder.content.copper_girder.ExposedCopperGirderEncasedShaftBlock;
import com.agent772.createmoregirder.content.copper_girder.WeatheredCopperGirderBlock;
import com.agent772.createmoregirder.content.copper_girder.WeatheredCopperGirderEncasedShaftBlock;
import com.agent772.createmoregirder.content.copper_girder.OxidizedCopperGirderBlock;
import com.agent772.createmoregirder.content.copper_girder.OxidizedCopperGirderEncasedShaftBlock;
import com.agent772.createmoregirder.content.copper_girder.WaxedCopperGirderBlock;
import com.agent772.createmoregirder.content.copper_girder.WaxedExposedCopperGirderBlock;
import com.agent772.createmoregirder.content.copper_girder.WaxedWeatheredCopperGirderBlock;
import com.agent772.createmoregirder.content.copper_girder.WaxedOxidizedCopperGirderBlock;
import com.agent772.createmoregirder.content.copper_girder.WaxedCopperGirderEncasedShaftBlock;
import com.agent772.createmoregirder.content.copper_girder.WaxedExposedCopperGirderEncasedShaftBlock;
import com.agent772.createmoregirder.content.copper_girder.WaxedWeatheredCopperGirderEncasedShaftBlock;
import com.agent772.createmoregirder.content.copper_girder.WaxedOxidizedCopperGirderEncasedShaftBlock;
import com.agent772.createmoregirder.content.girder.GenericGirderGenerator;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.util.entry.BlockEntry;

import net.minecraft.world.level.block.*;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import static com.agent772.createmoregirder.CreateMoreGirder.REGISTRATE;

import static com.simibubi.create.foundation.data.TagGen.pickaxeOnly;

public class CMGBlocks {

    public static final BlockEntry<BrassGirderBlock> BRASS_GIRDER =
            REGISTRATE.block("brass_girder", BrassGirderBlock::new)
                    .initialProperties(SharedProperties::softMetal)
                    .properties(p -> p.mapColor(MapColor.COLOR_YELLOW).sound(SoundType.NETHERITE_BLOCK))
                    .transform(pickaxeOnly())
                    .blockstate(GenericGirderGenerator::blockState)
                    .item().model(GenericGirderGenerator::itemModel).build()
                    .register();

    public static final BlockEntry<BrassGirderEncasedShaftBlock> BRASS_GIRDER_ENCASED_SHAFT =
            REGISTRATE.block("brass_girder_encased_shaft", BrassGirderEncasedShaftBlock::new)
                    .initialProperties(SharedProperties::softMetal)
                    .properties(p -> p.mapColor(MapColor.COLOR_GRAY).sound(SoundType.NETHERITE_BLOCK))
                    .transform(pickaxeOnly())
                    .blockstate(GenericGirderGenerator::blockStateWithShaft)
                    .loot((p, b) -> p.add(b, p.createSingleItemTable(BRASS_GIRDER.get())
                            .withPool(p.applyExplosionCondition(AllBlocks.SHAFT.get(), LootPool.lootPool()
                                    .setRolls(ConstantValue.exactly(1.0F))
                                    .add(LootItem.lootTableItem(AllBlocks.SHAFT.get()))))))
                    .register();

    public static final BlockEntry<CopperGirderBlock> COPPER_GIRDER =
            REGISTRATE.block("copper_girder", CopperGirderBlock::new)
                    .initialProperties(SharedProperties::softMetal)
                    .properties(p -> p.mapColor(MapColor.COLOR_YELLOW).sound(SoundType.NETHERITE_BLOCK))
                    .transform(pickaxeOnly())
                    .blockstate(GenericGirderGenerator::blockState)
                    .item().model(GenericGirderGenerator::itemModel).build()
                    .register();

    public static final BlockEntry<ExposedCopperGirderBlock> EXPOSED_COPPER_GIRDER =
            REGISTRATE.block("exposed_copper_girder", ExposedCopperGirderBlock::new)
                    .initialProperties(SharedProperties::softMetal)
                    .properties(p -> p.mapColor(MapColor.COLOR_YELLOW).sound(SoundType.NETHERITE_BLOCK))
                    .transform(pickaxeOnly())
                    .blockstate(GenericGirderGenerator::blockState)
                    .item().model(GenericGirderGenerator::itemModel).build()
                    .register();

    public static final BlockEntry<WeatheredCopperGirderBlock> WEATHERED_COPPER_GIRDER =
            REGISTRATE.block("weathered_copper_girder", WeatheredCopperGirderBlock::new)
                    .initialProperties(SharedProperties::softMetal)
                    .properties(p -> p.mapColor(MapColor.COLOR_YELLOW).sound(SoundType.NETHERITE_BLOCK))
                    .transform(pickaxeOnly())
                    .blockstate(GenericGirderGenerator::blockState)
                    .item().model(GenericGirderGenerator::itemModel).build()
                    .register();

    public static final BlockEntry<OxidizedCopperGirderBlock> OXIDIZED_COPPER_GIRDER =
            REGISTRATE.block("oxidized_copper_girder", OxidizedCopperGirderBlock::new)
                    .initialProperties(SharedProperties::softMetal)
                    .properties(p -> p.mapColor(MapColor.COLOR_YELLOW).sound(SoundType.NETHERITE_BLOCK))
                    .transform(pickaxeOnly())
                    .blockstate(GenericGirderGenerator::blockState)
                    .item().model(GenericGirderGenerator::itemModel).build()
                    .register();

    public static final BlockEntry<WaxedCopperGirderBlock> WAXED_COPPER_GIRDER =
            REGISTRATE.block("waxed_copper_girder", WaxedCopperGirderBlock::new)
                    .initialProperties(SharedProperties::softMetal)
                    .properties(p -> p.mapColor(MapColor.COLOR_YELLOW).sound(SoundType.NETHERITE_BLOCK))
                    .transform(pickaxeOnly())
                    .blockstate(GenericGirderGenerator::blockState)
                    .item().model(GenericGirderGenerator::itemModel).build()
                    .register();

    public static final BlockEntry<CopperGirderEncasedShaftBlock> COPPER_GIRDER_ENCASED_SHAFT =
            REGISTRATE.block("copper_girder_encased_shaft", CopperGirderEncasedShaftBlock::new)
                    .initialProperties(SharedProperties::softMetal)
                    .properties(p -> p.mapColor(MapColor.COLOR_GRAY).sound(SoundType.NETHERITE_BLOCK))
                    .transform(pickaxeOnly())
                    .blockstate(GenericGirderGenerator::blockStateWithShaft)
                    .loot((p, b) -> p.add(b, p.createSingleItemTable(COPPER_GIRDER.get())
                            .withPool(p.applyExplosionCondition(AllBlocks.SHAFT.get(), LootPool.lootPool()
                                    .setRolls(ConstantValue.exactly(1.0F))
                                    .add(LootItem.lootTableItem(AllBlocks.SHAFT.get()))))))
                    .register();
    
    // Additional weathering stage registrations
    public static final BlockEntry<WaxedExposedCopperGirderBlock> WAXED_EXPOSED_COPPER_GIRDER =
            REGISTRATE.block("waxed_exposed_copper_girder", WaxedExposedCopperGirderBlock::new)
                    .initialProperties(SharedProperties::softMetal)
                    .properties(p -> p.mapColor(MapColor.COLOR_YELLOW).sound(SoundType.NETHERITE_BLOCK))
                    .transform(pickaxeOnly())
                    .blockstate(GenericGirderGenerator::blockState)
                    .item().model(GenericGirderGenerator::itemModel).build()
                    .register();

    public static final BlockEntry<WaxedWeatheredCopperGirderBlock> WAXED_WEATHERED_COPPER_GIRDER =
            REGISTRATE.block("waxed_weathered_copper_girder", WaxedWeatheredCopperGirderBlock::new)
                    .initialProperties(SharedProperties::softMetal)
                    .properties(p -> p.mapColor(MapColor.COLOR_YELLOW).sound(SoundType.NETHERITE_BLOCK))
                    .transform(pickaxeOnly())
                    .blockstate(GenericGirderGenerator::blockState)
                    .item().model(GenericGirderGenerator::itemModel).build()
                    .register();

    public static final BlockEntry<WaxedOxidizedCopperGirderBlock> WAXED_OXIDIZED_COPPER_GIRDER =
            REGISTRATE.block("waxed_oxidized_copper_girder", WaxedOxidizedCopperGirderBlock::new)
                    .initialProperties(SharedProperties::softMetal)
                    .properties(p -> p.mapColor(MapColor.COLOR_YELLOW).sound(SoundType.NETHERITE_BLOCK))
                    .transform(pickaxeOnly())
                    .blockstate(GenericGirderGenerator::blockState)
                    .item().model(GenericGirderGenerator::itemModel).build()
                    .register();

    // Encased shaft variants for weathering stages
    public static final BlockEntry<ExposedCopperGirderEncasedShaftBlock> EXPOSED_COPPER_GIRDER_ENCASED_SHAFT =
            REGISTRATE.block("exposed_copper_girder_encased_shaft", ExposedCopperGirderEncasedShaftBlock::new)
                    .initialProperties(SharedProperties::softMetal)
                    .properties(p -> p.mapColor(MapColor.COLOR_GRAY).sound(SoundType.NETHERITE_BLOCK))
                    .transform(pickaxeOnly())
                    .blockstate(GenericGirderGenerator::blockStateWithShaft)
                    .loot((p, b) -> p.add(b, p.createSingleItemTable(EXPOSED_COPPER_GIRDER.get())
                            .withPool(p.applyExplosionCondition(AllBlocks.SHAFT.get(), LootPool.lootPool()
                                    .setRolls(ConstantValue.exactly(1.0F))
                                    .add(LootItem.lootTableItem(AllBlocks.SHAFT.get()))))))
                    .register();

    public static final BlockEntry<WeatheredCopperGirderEncasedShaftBlock> WEATHERED_COPPER_GIRDER_ENCASED_SHAFT =
            REGISTRATE.block("weathered_copper_girder_encased_shaft", WeatheredCopperGirderEncasedShaftBlock::new)
                    .initialProperties(SharedProperties::softMetal)
                    .properties(p -> p.mapColor(MapColor.COLOR_GRAY).sound(SoundType.NETHERITE_BLOCK))
                    .transform(pickaxeOnly())
                    .blockstate(GenericGirderGenerator::blockStateWithShaft)
                    .loot((p, b) -> p.add(b, p.createSingleItemTable(WEATHERED_COPPER_GIRDER.get())
                            .withPool(p.applyExplosionCondition(AllBlocks.SHAFT.get(), LootPool.lootPool()
                                    .setRolls(ConstantValue.exactly(1.0F))
                                    .add(LootItem.lootTableItem(AllBlocks.SHAFT.get()))))))
                    .register();

    public static final BlockEntry<OxidizedCopperGirderEncasedShaftBlock> OXIDIZED_COPPER_GIRDER_ENCASED_SHAFT =
            REGISTRATE.block("oxidized_copper_girder_encased_shaft", OxidizedCopperGirderEncasedShaftBlock::new)
                    .initialProperties(SharedProperties::softMetal)
                    .properties(p -> p.mapColor(MapColor.COLOR_GRAY).sound(SoundType.NETHERITE_BLOCK))
                    .transform(pickaxeOnly())
                    .blockstate(GenericGirderGenerator::blockStateWithShaft)
                    .loot((p, b) -> p.add(b, p.createSingleItemTable(OXIDIZED_COPPER_GIRDER.get())
                            .withPool(p.applyExplosionCondition(AllBlocks.SHAFT.get(), LootPool.lootPool()
                                    .setRolls(ConstantValue.exactly(1.0F))
                                    .add(LootItem.lootTableItem(AllBlocks.SHAFT.get()))))))
                    .register();

    public static final BlockEntry<WaxedCopperGirderEncasedShaftBlock> WAXED_COPPER_GIRDER_ENCASED_SHAFT =
            REGISTRATE.block("waxed_copper_girder_encased_shaft", WaxedCopperGirderEncasedShaftBlock::new)
                    .initialProperties(SharedProperties::softMetal)
                    .properties(p -> p.mapColor(MapColor.COLOR_GRAY).sound(SoundType.NETHERITE_BLOCK))
                    .transform(pickaxeOnly())
                    .blockstate(GenericGirderGenerator::blockStateWithShaft)
                    .loot((p, b) -> p.add(b, p.createSingleItemTable(WAXED_COPPER_GIRDER.get())
                            .withPool(p.applyExplosionCondition(AllBlocks.SHAFT.get(), LootPool.lootPool()
                                    .setRolls(ConstantValue.exactly(1.0F))
                                    .add(LootItem.lootTableItem(AllBlocks.SHAFT.get()))))))
                    .register();

    public static final BlockEntry<WaxedExposedCopperGirderEncasedShaftBlock> WAXED_EXPOSED_COPPER_GIRDER_ENCASED_SHAFT =
            REGISTRATE.block("waxed_exposed_copper_girder_encased_shaft", WaxedExposedCopperGirderEncasedShaftBlock::new)
                    .initialProperties(SharedProperties::softMetal)
                    .properties(p -> p.mapColor(MapColor.COLOR_GRAY).sound(SoundType.NETHERITE_BLOCK))
                    .transform(pickaxeOnly())
                    .blockstate(GenericGirderGenerator::blockStateWithShaft)
                    .loot((p, b) -> p.add(b, p.createSingleItemTable(WAXED_EXPOSED_COPPER_GIRDER.get())
                            .withPool(p.applyExplosionCondition(AllBlocks.SHAFT.get(), LootPool.lootPool()
                                    .setRolls(ConstantValue.exactly(1.0F))
                                    .add(LootItem.lootTableItem(AllBlocks.SHAFT.get()))))))
                    .register();

    public static final BlockEntry<WaxedWeatheredCopperGirderEncasedShaftBlock> WAXED_WEATHERED_COPPER_GIRDER_ENCASED_SHAFT =
            REGISTRATE.block("waxed_weathered_copper_girder_encased_shaft", WaxedWeatheredCopperGirderEncasedShaftBlock::new)
                    .initialProperties(SharedProperties::softMetal)
                    .properties(p -> p.mapColor(MapColor.COLOR_GRAY).sound(SoundType.NETHERITE_BLOCK))
                    .transform(pickaxeOnly())
                    .blockstate(GenericGirderGenerator::blockStateWithShaft)
                    .loot((p, b) -> p.add(b, p.createSingleItemTable(WAXED_WEATHERED_COPPER_GIRDER.get())
                            .withPool(p.applyExplosionCondition(AllBlocks.SHAFT.get(), LootPool.lootPool()
                                    .setRolls(ConstantValue.exactly(1.0F))
                                    .add(LootItem.lootTableItem(AllBlocks.SHAFT.get()))))))
                    .register();

    public static final BlockEntry<WaxedOxidizedCopperGirderEncasedShaftBlock> WAXED_OXIDIZED_COPPER_GIRDER_ENCASED_SHAFT =
            REGISTRATE.block("waxed_oxidized_copper_girder_encased_shaft", WaxedOxidizedCopperGirderEncasedShaftBlock::new)
                    .initialProperties(SharedProperties::softMetal)
                    .properties(p -> p.mapColor(MapColor.COLOR_GRAY).sound(SoundType.NETHERITE_BLOCK))
                    .transform(pickaxeOnly())
                    .blockstate(GenericGirderGenerator::blockStateWithShaft)
                    .loot((p, b) -> p.add(b, p.createSingleItemTable(WAXED_OXIDIZED_COPPER_GIRDER.get())
                            .withPool(p.applyExplosionCondition(AllBlocks.SHAFT.get(), LootPool.lootPool()
                                    .setRolls(ConstantValue.exactly(1.0F))
                                    .add(LootItem.lootTableItem(AllBlocks.SHAFT.get()))))))
                    .register();
    
    public static void register() {
    }

}
