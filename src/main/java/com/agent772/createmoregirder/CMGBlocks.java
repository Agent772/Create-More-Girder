package com.agent772.createmoregirder;

import com.agent772.createmoregirder.content.andesite_girder.AndesiteGirderBlock;
import com.agent772.createmoregirder.content.andesite_girder.AndesiteGirderEncasedShaftBlock;
import com.agent772.createmoregirder.content.andesite_girder.AndesiteGirderGenerator;
import com.agent772.createmoregirder.content.andesite_girder.AndesiteGirderStrutBlock;
import com.agent772.createmoregirder.content.brass_girder.BrassGirderBlock;
import com.agent772.createmoregirder.content.brass_girder.BrassGirderEncasedShaftBlock;
import com.agent772.createmoregirder.content.copper_girder.CopperGirderBlock;
import com.agent772.createmoregirder.content.industrial_iron_girder.IndustrialIronGirderBlock;
import com.agent772.createmoregirder.content.industrial_iron_girder.IndustrialIronGirderEncasedShaftBlock;
import com.agent772.createmoregirder.content.weathered_iron_girder.WeatheredIronGirderBlock;
import com.agent772.createmoregirder.content.weathered_iron_girder.WeatheredIronGirderEncasedShaftBlock;
import com.agent772.createmoregirder.content.strut.GirderStrutBlockItem;
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
import com.simibubi.create.AllTags;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.util.entry.BlockEntry;

import net.minecraft.world.level.block.*;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;


import static com.agent772.createmoregirder.CreateMoreGirder.REGISTRATE;

import static com.simibubi.create.foundation.data.TagGen.pickaxeOnly;

public class CMGBlocks {

        public static final BlockEntry<AndesiteGirderBlock> ANDESITE_GIRDER =
                REGISTRATE.block("andesite_girder", AndesiteGirderBlock::new)
                        .initialProperties(SharedProperties::softMetal)
                        .properties(p -> p.mapColor(MapColor.COLOR_GRAY).sound(SoundType.NETHERITE_BLOCK))
                        .transform(pickaxeOnly())
                        .blockstate(AndesiteGirderGenerator::blockState)
                        .item().model((c, p) -> p.blockItem(c, "/item")).build()
                        .register();

        public static final BlockEntry<AndesiteGirderEncasedShaftBlock> ANDESITE_GIRDER_ENCASED_SHAFT =
                REGISTRATE.block("andesite_girder_encased_shaft", AndesiteGirderEncasedShaftBlock::new)
                        .initialProperties(SharedProperties::softMetal)
                        .properties(p -> p.mapColor(MapColor.COLOR_GRAY).sound(SoundType.NETHERITE_BLOCK))
                        .transform(pickaxeOnly())
                        .blockstate(AndesiteGirderGenerator::blockStateWithShaft)
                        .loot((lt, block) -> lt.add(block, LootTable.lootTable()
                                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                                        .add(LootItem.lootTableItem(ANDESITE_GIRDER.get()))
                                        .when(ExplosionCondition.survivesExplosion()))
                                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                                        .add(LootItem.lootTableItem(AllBlocks.SHAFT.get()))
                                        .when(ExplosionCondition.survivesExplosion()))))
                        .register();

        // Andesite Girder Strut
        public static final BlockEntry<AndesiteGirderStrutBlock> ANDESITE_GIRDER_STRUT =
                REGISTRATE.block("andesite_girder_strut", AndesiteGirderStrutBlock::new)
                        .initialProperties(() -> Blocks.IRON_BLOCK)
                        .properties(p -> p.mapColor(MapColor.COLOR_GRAY).sound(SoundType.NETHERITE_BLOCK)
                                .noOcclusion().destroyTime(2.0f).explosionResistance(6.0f))
                        .transform(pickaxeOnly())
                        .blockstate((c, p) -> p.simpleBlock(c.get(),
                                p.models().getExistingFile(p.modLoc("block/girder_strut/andesite_girder_strut_attachment"))))
                        .tag(AllTags.AllBlockTags.SAFE_NBT.tag)
                        .simpleItem()
                        .register();

        // Brass Girder
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
                        .loot((lt, block) -> lt.add(block, LootTable.lootTable()
                                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                                        .add(LootItem.lootTableItem(BRASS_GIRDER.get()))
                                        .when(ExplosionCondition.survivesExplosion()))
                                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                                        .add(LootItem.lootTableItem(AllBlocks.SHAFT.get()))
                                        .when(ExplosionCondition.survivesExplosion()))))
                        .register();

        // Copper Girder and its weathering stages
        public static final BlockEntry<CopperGirderBlock> COPPER_GIRDER =
                REGISTRATE.block("copper_girder", CopperGirderBlock::new)
                        .initialProperties(SharedProperties::softMetal)
                        .properties(p -> p.mapColor(MapColor.COLOR_YELLOW).sound(SoundType.NETHERITE_BLOCK).randomTicks())
                        .transform(pickaxeOnly())
                        .blockstate(GenericGirderGenerator::blockState)
                        .item().model(GenericGirderGenerator::itemModel).build()
                        .register();
        public static final BlockEntry<CopperGirderEncasedShaftBlock> COPPER_GIRDER_ENCASED_SHAFT =
                REGISTRATE.block("copper_girder_encased_shaft", CopperGirderEncasedShaftBlock::new)
                        .initialProperties(SharedProperties::softMetal)
                        .properties(p -> p.mapColor(MapColor.COLOR_GRAY).sound(SoundType.NETHERITE_BLOCK).randomTicks())
                        .transform(pickaxeOnly())
                        .blockstate(GenericGirderGenerator::blockStateWithShaft)
                        .loot((lt, block) -> lt.add(block, LootTable.lootTable()
                                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                                        .add(LootItem.lootTableItem(COPPER_GIRDER.get()))
                                        .when(ExplosionCondition.survivesExplosion()))
                                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                                        .add(LootItem.lootTableItem(AllBlocks.SHAFT.get()))
                                        .when(ExplosionCondition.survivesExplosion()))))
                        .register();

        public static final BlockEntry<ExposedCopperGirderBlock> EXPOSED_COPPER_GIRDER =
                REGISTRATE.block("exposed_copper_girder", ExposedCopperGirderBlock::new)
                        .initialProperties(SharedProperties::softMetal)
                        .properties(p -> p.mapColor(MapColor.COLOR_YELLOW).sound(SoundType.NETHERITE_BLOCK).randomTicks())
                        .transform(pickaxeOnly())
                        .blockstate(GenericGirderGenerator::blockState)
                        .item().model(GenericGirderGenerator::itemModel).build()
                        .register();
        public static final BlockEntry<ExposedCopperGirderEncasedShaftBlock> EXPOSED_COPPER_GIRDER_ENCASED_SHAFT =
                REGISTRATE.block("exposed_copper_girder_encased_shaft", ExposedCopperGirderEncasedShaftBlock::new)
                        .initialProperties(SharedProperties::softMetal)
                        .properties(p -> p.mapColor(MapColor.COLOR_GRAY).sound(SoundType.NETHERITE_BLOCK).randomTicks())
                        .transform(pickaxeOnly())
                        .blockstate(GenericGirderGenerator::blockStateWithShaft)
                        .loot((lt, block) -> lt.add(block, LootTable.lootTable()
                                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                                        .add(LootItem.lootTableItem(EXPOSED_COPPER_GIRDER.get()))
                                        .when(ExplosionCondition.survivesExplosion()))
                                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                                        .add(LootItem.lootTableItem(AllBlocks.SHAFT.get()))
                                        .when(ExplosionCondition.survivesExplosion()))))
                        .register();

        public static final BlockEntry<WeatheredCopperGirderBlock> WEATHERED_COPPER_GIRDER =
                REGISTRATE.block("weathered_copper_girder", WeatheredCopperGirderBlock::new)
                        .initialProperties(SharedProperties::softMetal)
                        .properties(p -> p.mapColor(MapColor.COLOR_YELLOW).sound(SoundType.NETHERITE_BLOCK).randomTicks())
                        .transform(pickaxeOnly())
                        .blockstate(GenericGirderGenerator::blockState)
                        .item().model(GenericGirderGenerator::itemModel).build()
                        .register();
        public static final BlockEntry<WeatheredCopperGirderEncasedShaftBlock> WEATHERED_COPPER_GIRDER_ENCASED_SHAFT =
            REGISTRATE.block("weathered_copper_girder_encased_shaft", WeatheredCopperGirderEncasedShaftBlock::new)
                    .initialProperties(SharedProperties::softMetal)
                    .properties(p -> p.mapColor(MapColor.COLOR_GRAY).sound(SoundType.NETHERITE_BLOCK).randomTicks())
                    .transform(pickaxeOnly())
                    .blockstate(GenericGirderGenerator::blockStateWithShaft)
                    .loot((lt, block) -> lt.add(block, LootTable.lootTable()
                            .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                                    .add(LootItem.lootTableItem(WEATHERED_COPPER_GIRDER.get()))
                                    .when(ExplosionCondition.survivesExplosion()))
                            .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                                    .add(LootItem.lootTableItem(AllBlocks.SHAFT.get()))
                                    .when(ExplosionCondition.survivesExplosion()))))
                    .register();

        public static final BlockEntry<OxidizedCopperGirderBlock> OXIDIZED_COPPER_GIRDER =
                REGISTRATE.block("oxidized_copper_girder", OxidizedCopperGirderBlock::new)
                        .initialProperties(SharedProperties::softMetal)
                        .properties(p -> p.mapColor(MapColor.COLOR_YELLOW).sound(SoundType.NETHERITE_BLOCK).randomTicks())
                        .transform(pickaxeOnly())
                        .blockstate(GenericGirderGenerator::blockState)
                        .item().model(GenericGirderGenerator::itemModel).build()
                        .register();
        public static final BlockEntry<OxidizedCopperGirderEncasedShaftBlock> OXIDIZED_COPPER_GIRDER_ENCASED_SHAFT =
            REGISTRATE.block("oxidized_copper_girder_encased_shaft", OxidizedCopperGirderEncasedShaftBlock::new)
                    .initialProperties(SharedProperties::softMetal)
                    .properties(p -> p.mapColor(MapColor.COLOR_GRAY).sound(SoundType.NETHERITE_BLOCK).randomTicks())
                    .transform(pickaxeOnly())
                    .blockstate(GenericGirderGenerator::blockStateWithShaft)
                    .loot((lt, block) -> lt.add(block, LootTable.lootTable()
                            .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                                    .add(LootItem.lootTableItem(OXIDIZED_COPPER_GIRDER.get()))
                                    .when(ExplosionCondition.survivesExplosion()))
                            .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                                    .add(LootItem.lootTableItem(AllBlocks.SHAFT.get()))
                                    .when(ExplosionCondition.survivesExplosion()))))
                    .register();

        // Waxed Copper Girders
        public static final BlockEntry<WaxedCopperGirderBlock> WAXED_COPPER_GIRDER =
                REGISTRATE.block("waxed_copper_girder", WaxedCopperGirderBlock::new)
                        .initialProperties(SharedProperties::softMetal)
                        .properties(p -> p.mapColor(MapColor.COLOR_YELLOW).sound(SoundType.NETHERITE_BLOCK))
                        .transform(pickaxeOnly())
                        .blockstate(GenericGirderGenerator::blockState)
                        .item().model(GenericGirderGenerator::itemModel).build()
                        .register();
        public static final BlockEntry<WaxedCopperGirderEncasedShaftBlock> WAXED_COPPER_GIRDER_ENCASED_SHAFT =
                REGISTRATE.block("waxed_copper_girder_encased_shaft", WaxedCopperGirderEncasedShaftBlock::new)
                        .initialProperties(SharedProperties::softMetal)
                        .properties(p -> p.mapColor(MapColor.COLOR_GRAY).sound(SoundType.NETHERITE_BLOCK))
                        .transform(pickaxeOnly())
                        .blockstate(GenericGirderGenerator::blockStateWithShaft)
                        .loot((lt, block) -> lt.add(block, LootTable.lootTable()
                                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                                        .add(LootItem.lootTableItem(WAXED_COPPER_GIRDER.get()))
                                        .when(ExplosionCondition.survivesExplosion()))
                                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                                        .add(LootItem.lootTableItem(AllBlocks.SHAFT.get()))
                                        .when(ExplosionCondition.survivesExplosion()))))
                        .register();

        public static final BlockEntry<WaxedExposedCopperGirderBlock> WAXED_EXPOSED_COPPER_GIRDER =
                REGISTRATE.block("waxed_exposed_copper_girder", WaxedExposedCopperGirderBlock::new)
                        .initialProperties(SharedProperties::softMetal)
                        .properties(p -> p.mapColor(MapColor.COLOR_YELLOW).sound(SoundType.NETHERITE_BLOCK))
                        .transform(pickaxeOnly())
                        .blockstate(GenericGirderGenerator::blockState)
                        .item().model(GenericGirderGenerator::itemModel).build()
                        .register();
        public static final BlockEntry<WaxedExposedCopperGirderEncasedShaftBlock> WAXED_EXPOSED_COPPER_GIRDER_ENCASED_SHAFT =
                REGISTRATE.block("waxed_exposed_copper_girder_encased_shaft", WaxedExposedCopperGirderEncasedShaftBlock::new)
                        .initialProperties(SharedProperties::softMetal)
                        .properties(p -> p.mapColor(MapColor.COLOR_GRAY).sound(SoundType.NETHERITE_BLOCK))
                        .transform(pickaxeOnly())
                        .blockstate(GenericGirderGenerator::blockStateWithShaft)
                        .loot((lt, block) -> lt.add(block, LootTable.lootTable()
                                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                                        .add(LootItem.lootTableItem(WAXED_EXPOSED_COPPER_GIRDER.get()))
                                        .when(ExplosionCondition.survivesExplosion()))
                                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                                        .add(LootItem.lootTableItem(AllBlocks.SHAFT.get()))
                                        .when(ExplosionCondition.survivesExplosion()))))
                        .register();

        public static final BlockEntry<WaxedWeatheredCopperGirderBlock> WAXED_WEATHERED_COPPER_GIRDER =
                REGISTRATE.block("waxed_weathered_copper_girder", WaxedWeatheredCopperGirderBlock::new)
                        .initialProperties(SharedProperties::softMetal)
                        .properties(p -> p.mapColor(MapColor.COLOR_YELLOW).sound(SoundType.NETHERITE_BLOCK))
                        .transform(pickaxeOnly())
                        .blockstate(GenericGirderGenerator::blockState)
                        .item().model(GenericGirderGenerator::itemModel).build()
                        .register();
        public static final BlockEntry<WaxedWeatheredCopperGirderEncasedShaftBlock> WAXED_WEATHERED_COPPER_GIRDER_ENCASED_SHAFT =
                REGISTRATE.block("waxed_weathered_copper_girder_encased_shaft", WaxedWeatheredCopperGirderEncasedShaftBlock::new)
                        .initialProperties(SharedProperties::softMetal)
                        .properties(p -> p.mapColor(MapColor.COLOR_GRAY).sound(SoundType.NETHERITE_BLOCK))
                        .transform(pickaxeOnly())
                        .blockstate(GenericGirderGenerator::blockStateWithShaft)
                        .loot((lt, block) -> lt.add(block, LootTable.lootTable()
                                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                                        .add(LootItem.lootTableItem(WAXED_WEATHERED_COPPER_GIRDER.get()))
                                        .when(ExplosionCondition.survivesExplosion()))
                                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                                        .add(LootItem.lootTableItem(AllBlocks.SHAFT.get()))
                                        .when(ExplosionCondition.survivesExplosion()))))
                        .register();

        public static final BlockEntry<WaxedOxidizedCopperGirderBlock> WAXED_OXIDIZED_COPPER_GIRDER =
                REGISTRATE.block("waxed_oxidized_copper_girder", WaxedOxidizedCopperGirderBlock::new)
                        .initialProperties(SharedProperties::softMetal)
                        .properties(p -> p.mapColor(MapColor.COLOR_YELLOW).sound(SoundType.NETHERITE_BLOCK))
                        .transform(pickaxeOnly())
                        .blockstate(GenericGirderGenerator::blockState)
                        .item().model(GenericGirderGenerator::itemModel).build()
                        .register();
        public static final BlockEntry<WaxedOxidizedCopperGirderEncasedShaftBlock> WAXED_OXIDIZED_COPPER_GIRDER_ENCASED_SHAFT =
                REGISTRATE.block("waxed_oxidized_copper_girder_encased_shaft", WaxedOxidizedCopperGirderEncasedShaftBlock::new)
                        .initialProperties(SharedProperties::softMetal)
                        .properties(p -> p.mapColor(MapColor.COLOR_GRAY).sound(SoundType.NETHERITE_BLOCK))
                        .transform(pickaxeOnly())
                        .blockstate(GenericGirderGenerator::blockStateWithShaft)
                        .loot((lt, block) -> lt.add(block, LootTable.lootTable()
                                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                                        .add(LootItem.lootTableItem(WAXED_OXIDIZED_COPPER_GIRDER.get()))
                                        .when(ExplosionCondition.survivesExplosion()))
                                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                                        .add(LootItem.lootTableItem(AllBlocks.SHAFT.get()))
                                        .when(ExplosionCondition.survivesExplosion()))))
                        .register();
        
        // Industrial Iron Girder
        public static final BlockEntry<IndustrialIronGirderBlock> INDUSTRIAL_IRON_GIRDER =
                REGISTRATE.block("industrial_iron_girder", IndustrialIronGirderBlock::new)
                        .initialProperties(SharedProperties::softMetal)
                        .properties(p -> p.mapColor(MapColor.COLOR_YELLOW).sound(SoundType.NETHERITE_BLOCK))
                        .transform(pickaxeOnly())
                        .blockstate(GenericGirderGenerator::blockState)
                        .item().model(GenericGirderGenerator::itemModel).build()
                        .register();

        public static final BlockEntry<IndustrialIronGirderEncasedShaftBlock> INDUSTRIAL_IRON_GIRDER_ENCASED_SHAFT =
                REGISTRATE.block("industrial_iron_girder_encased_shaft", IndustrialIronGirderEncasedShaftBlock::new)
                        .initialProperties(SharedProperties::softMetal)
                        .properties(p -> p.mapColor(MapColor.COLOR_GRAY).sound(SoundType.NETHERITE_BLOCK))
                        .transform(pickaxeOnly())
                        .blockstate(GenericGirderGenerator::blockStateWithShaft)
                        .loot((lt, block) -> lt.add(block, LootTable.lootTable()
                                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                                        .add(LootItem.lootTableItem(INDUSTRIAL_IRON_GIRDER.get()))
                                        .when(ExplosionCondition.survivesExplosion()))
                                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                                        .add(LootItem.lootTableItem(AllBlocks.SHAFT.get()))
                                        .when(ExplosionCondition.survivesExplosion()))))
                        .register();

        // Weathered Iron Girder
        public static final BlockEntry<WeatheredIronGirderBlock> WEATHERED_IRON_GIRDER =
                REGISTRATE.block("weathered_iron_girder", WeatheredIronGirderBlock::new)
                        .initialProperties(SharedProperties::softMetal)
                        .properties(p -> p.mapColor(MapColor.COLOR_YELLOW).sound(SoundType.NETHERITE_BLOCK))
                        .transform(pickaxeOnly())
                        .blockstate(GenericGirderGenerator::blockState)
                        .item().model(GenericGirderGenerator::itemModel).build()
                        .register();

        public static final BlockEntry<WeatheredIronGirderEncasedShaftBlock> WEATHERED_IRON_GIRDER_ENCASED_SHAFT =
                REGISTRATE.block("weathered_iron_girder_encased_shaft", WeatheredIronGirderEncasedShaftBlock::new)
                        .initialProperties(SharedProperties::softMetal)
                        .properties(p -> p.mapColor(MapColor.COLOR_GRAY).sound(SoundType.NETHERITE_BLOCK))
                        .transform(pickaxeOnly())
                        .blockstate(GenericGirderGenerator::blockStateWithShaft)
                        .loot((lt, block) -> lt.add(block, LootTable.lootTable()
                                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                                        .add(LootItem.lootTableItem(WEATHERED_IRON_GIRDER.get()))
                                        .when(ExplosionCondition.survivesExplosion()))
                                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                                        .add(LootItem.lootTableItem(AllBlocks.SHAFT.get()))
                                        .when(ExplosionCondition.survivesExplosion()))))
                        .register();
    
    public static void register() {

    }

}
