package com.agent772.createmoregirder;

import com.agent772.createmoregirder.content.andesite_girder.AndesiteGirderBlock;
import com.agent772.createmoregirder.content.andesite_girder.AndesiteGirderEncasedShaftBlock;
import com.agent772.createmoregirder.content.andesite_girder.AndesiteGirderGenerator;
import com.agent772.createmoregirder.content.copycat_girder.CopycatGirderBakedModel;
import com.agent772.createmoregirder.content.copycat_girder.CopycatGirderBlock;
import com.agent772.createmoregirder.content.copycat_girder.CopycatGirderEncasedShaftBlock;
import com.agent772.createmoregirder.content.copycat_strut.CopycatGirderStrutBakedModel;
import com.agent772.createmoregirder.content.copycat_strut.CopycatGirderStrutBlock;
import com.agent772.createmoregirder.content.copycat_strut.CopycatGirderStrutBlockItem;
import com.agent772.createmoregirder.content.copycat_metal_girder.CopycatMetalGirderBakedModel;
import com.agent772.createmoregirder.content.copycat_metal_girder.CopycatMetalGirderBlock;
import com.agent772.createmoregirder.content.copycat_metal_girder.CopycatMetalGirderEncasedShaftBlock;
import com.agent772.createmoregirder.content.copycat_metal_girder.CopycatMetalGirderStrutBlock;
import com.agent772.createmoregirder.content.brass_girder.BrassGirderBlock;
import com.agent772.createmoregirder.content.brass_girder.BrassGirderEncasedShaftBlock;
import com.agent772.createmoregirder.content.andesite_metal_girder.AndesiteMetalGirderBlock;
import com.agent772.createmoregirder.content.andesite_metal_girder.AndesiteMetalGirderConnectedModel;
import com.agent772.createmoregirder.content.andesite_metal_girder.AndesiteMetalGirderEncasedShaftBlock;
import com.agent772.createmoregirder.content.brass_metal_girder.BrassMetalGirderBlock;
import com.agent772.createmoregirder.content.brass_metal_girder.BrassMetalGirderEncasedShaftBlock;
import com.agent772.createmoregirder.content.copper_metal_girder.CopperMetalGirderBlock;
import com.agent772.createmoregirder.content.copper_metal_girder.CopperMetalGirderEncasedShaftBlock;
import com.agent772.createmoregirder.content.copper_metal_girder.ExposedCopperMetalGirderBlock;
import com.agent772.createmoregirder.content.copper_metal_girder.ExposedCopperMetalGirderEncasedShaftBlock;
import com.agent772.createmoregirder.content.copper_metal_girder.WeatheredCopperMetalGirderBlock;
import com.agent772.createmoregirder.content.copper_metal_girder.WeatheredCopperMetalGirderEncasedShaftBlock;
import com.agent772.createmoregirder.content.copper_metal_girder.OxidizedCopperMetalGirderBlock;
import com.agent772.createmoregirder.content.copper_metal_girder.OxidizedCopperMetalGirderEncasedShaftBlock;
import com.agent772.createmoregirder.content.copper_metal_girder.WaxedOxidizedCopperMetalGirderBlock;
import com.agent772.createmoregirder.content.copper_metal_girder.WaxedOxidizedCopperMetalGirderEncasedShaftBlock;
import com.agent772.createmoregirder.content.copper_metal_girder.WaxedCopperMetalGirderBlock;
import com.agent772.createmoregirder.content.copper_metal_girder.WaxedCopperMetalGirderEncasedShaftBlock;
import com.agent772.createmoregirder.content.copper_metal_girder.WaxedExposedCopperMetalGirderBlock;
import com.agent772.createmoregirder.content.copper_metal_girder.WaxedExposedCopperMetalGirderEncasedShaftBlock;
import com.agent772.createmoregirder.content.copper_metal_girder.WaxedWeatheredCopperMetalGirderBlock;
import com.agent772.createmoregirder.content.copper_metal_girder.WaxedWeatheredCopperMetalGirderEncasedShaftBlock;
import com.agent772.createmoregirder.content.copper_girder.CopperGirderBlock;
import com.agent772.createmoregirder.content.industrial_iron_girder.IndustrialIronGirderBlock;
import com.agent772.createmoregirder.content.industrial_iron_girder.IndustrialIronGirderEncasedShaftBlock;
import com.agent772.createmoregirder.content.weathered_iron_girder.WeatheredIronGirderBlock;
import com.agent772.createmoregirder.content.weathered_iron_girder.WeatheredIronGirderEncasedShaftBlock;
import com.agent772.createmoregirder.content.weathered_iron_metal_girder.WeatheredIronMetalGirderBlock;
import com.agent772.createmoregirder.content.weathered_iron_metal_girder.WeatheredIronMetalGirderEncasedShaftBlock;
import com.agent772.createmoregirder.content.strut.GirderStrutBlock;
import com.agent772.createmoregirder.content.strut.GirderStrutBlockItem;
import com.agent772.createmoregirder.content.strut.GirderStrutModelBuilder;
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
import com.agent772.createmoregirder.content.girder.ConnectedGirderModel;
import com.agent772.createmoregirder.content.girder.GenericGirderGenerator;
import com.simibubi.create.AllTags;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.util.entry.BlockEntry;

import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
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
                        .onRegister(CreateRegistrate.blockModel(() -> ConnectedGirderModel::new))
                        .tag(CMGTags.GIRDER_BLOCK, CMGTags.PAVING_GIRDER)
                        .blockstate(AndesiteGirderGenerator::blockState)
                        .item().tag(CMGTags.GIRDER_ITEM).model((c, p) -> p.blockItem(c, "/item")).build()
                        .register();

        public static final BlockEntry<AndesiteGirderEncasedShaftBlock> ANDESITE_GIRDER_ENCASED_SHAFT =
                REGISTRATE.block("andesite_girder_encased_shaft", AndesiteGirderEncasedShaftBlock::new)
                        .initialProperties(SharedProperties::softMetal)
                        .properties(p -> p.mapColor(MapColor.COLOR_GRAY).sound(SoundType.NETHERITE_BLOCK))
                        .transform(pickaxeOnly())
                        .tag(CMGTags.GIRDER_ENCASED_SHAFT_BLOCK)
                        .blockstate(AndesiteGirderGenerator::blockStateWithShaft)
                        .loot((lt, block) -> lt.add(block, LootTable.lootTable()
                                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                                        .add(LootItem.lootTableItem(ANDESITE_GIRDER.get()))
                                        .when(ExplosionCondition.survivesExplosion()))
                                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                                        .add(LootItem.lootTableItem(AllBlocks.SHAFT.get()))
                                        .when(ExplosionCondition.survivesExplosion()))))
                        .register();

        // Strut
        public static final BlockEntry<GirderStrutBlock> ANDESITE_GIRDER_STRUT =
                REGISTRATE.block("andesite_girder_strut", GirderStrutBlock.andesite())
                        .initialProperties(SharedProperties::softMetal)
                        .properties(p -> p.destroyTime(0.3f).noOcclusion())
                        .transform(pickaxeOnly())
                        .blockstate((c, p) -> p.directionalBlock(c.get(),
                                (state) -> p.models().getExistingFile(CreateMoreGirder.asResource(
                                        "block/andesite_girder_strut/attachment")
                                )))
                        .onRegister(CreateRegistrate.blockModel(() -> GirderStrutModelBuilder::new))
                        .tag(AllTags.AllBlockTags.SAFE_NBT.tag)
                        .loot((lt, block) -> lt.add(block, LootTable.lootTable()
                                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                                        .add(LootItem.lootTableItem(block))
                                        .when(ExplosionCondition.survivesExplosion()))))
                        .item(GirderStrutBlockItem::new)
                        .tag(CMGTags.STRUT_ITEM)
                        .model((c, p) ->
                                p.withExistingParent(c.getName(), CreateMoreGirder.asResource("block/andesite_girder_strut/item"))
                        )
                        .build()
                        .register();

        // Brass Girder
        public static final BlockEntry<BrassGirderBlock> BRASS_GIRDER =
                REGISTRATE.block("brass_girder", BrassGirderBlock::new)
                        .initialProperties(SharedProperties::softMetal)
                        .properties(p -> p.mapColor(MapColor.COLOR_YELLOW).sound(SoundType.NETHERITE_BLOCK))
                        .transform(pickaxeOnly())
                        .onRegister(CreateRegistrate.blockModel(() -> ConnectedGirderModel::new))
                        .tag(CMGTags.GIRDER_BLOCK, CMGTags.PAVING_GIRDER)
                        .blockstate(GenericGirderGenerator::blockState)
                        .item().tag(CMGTags.GIRDER_ITEM).model(GenericGirderGenerator::itemModel).build()
                        .register();

        public static final BlockEntry<BrassGirderEncasedShaftBlock> BRASS_GIRDER_ENCASED_SHAFT =
                REGISTRATE.block("brass_girder_encased_shaft", BrassGirderEncasedShaftBlock::new)
                        .initialProperties(SharedProperties::softMetal)
                        .properties(p -> p.mapColor(MapColor.COLOR_GRAY).sound(SoundType.NETHERITE_BLOCK))
                        .transform(pickaxeOnly())
                        .tag(CMGTags.GIRDER_ENCASED_SHAFT_BLOCK)
                        .blockstate(GenericGirderGenerator::blockStateWithShaft)
                        .loot((lt, block) -> lt.add(block, LootTable.lootTable()
                                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                                        .add(LootItem.lootTableItem(BRASS_GIRDER.get()))
                                        .when(ExplosionCondition.survivesExplosion()))
                                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                                        .add(LootItem.lootTableItem(AllBlocks.SHAFT.get()))
                                        .when(ExplosionCondition.survivesExplosion()))))
                        .register();

        // Andesite Metal Girder (OG metal_girder silhouette in andesite) - base for the _metal_girder family
        public static final BlockEntry<AndesiteMetalGirderBlock> ANDESITE_METAL_GIRDER =
                REGISTRATE.block("andesite_metal_girder", AndesiteMetalGirderBlock::new)
                        .initialProperties(SharedProperties::softMetal)
                        .properties(p -> p.mapColor(MapColor.COLOR_GRAY).sound(SoundType.NETHERITE_BLOCK))
                        .transform(pickaxeOnly())
                        .onRegister(CreateRegistrate.blockModel(() -> AndesiteMetalGirderConnectedModel::new))
                        .tag(CMGTags.GIRDER_BLOCK, CMGTags.PAVING_GIRDER)
                        .blockstate(GenericGirderGenerator::blockState)
                        .item().tag(CMGTags.GIRDER_ITEM).model(GenericGirderGenerator::itemModel).build()
                        .register();

        public static final BlockEntry<AndesiteMetalGirderEncasedShaftBlock> ANDESITE_METAL_GIRDER_ENCASED_SHAFT =
                REGISTRATE.block("andesite_metal_girder_encased_shaft", AndesiteMetalGirderEncasedShaftBlock::new)
                        .initialProperties(SharedProperties::softMetal)
                        .properties(p -> p.mapColor(MapColor.COLOR_GRAY).sound(SoundType.NETHERITE_BLOCK))
                        .transform(pickaxeOnly())
                        .tag(CMGTags.GIRDER_ENCASED_SHAFT_BLOCK)
                        .blockstate(GenericGirderGenerator::blockStateWithShaft)
                        .loot((lt, block) -> lt.add(block, LootTable.lootTable()
                                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                                        .add(LootItem.lootTableItem(ANDESITE_METAL_GIRDER.get()))
                                        .when(ExplosionCondition.survivesExplosion()))
                                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                                        .add(LootItem.lootTableItem(AllBlocks.SHAFT.get()))
                                        .when(ExplosionCondition.survivesExplosion()))))
                        .register();

        // Strut
        public static final BlockEntry<GirderStrutBlock> ANDESITE_METAL_GIRDER_STRUT =
                REGISTRATE.block("andesite_metal_girder_strut", GirderStrutBlock.andesite_metal())
                        .initialProperties(SharedProperties::softMetal)
                        .properties(p -> p.destroyTime(0.3f).noOcclusion())
                        .transform(pickaxeOnly())
                        .blockstate((c, p) -> p.directionalBlock(c.get(),
                                (state) -> p.models().getExistingFile(CreateMoreGirder.asResource(
                                        "block/andesite_metal_girder_strut/attachment")
                                )))
                        .onRegister(CreateRegistrate.blockModel(() -> GirderStrutModelBuilder::new))
                        .tag(AllTags.AllBlockTags.SAFE_NBT.tag)
                        .loot((lt, block) -> lt.add(block, LootTable.lootTable()
                                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                                        .add(LootItem.lootTableItem(block))
                                        .when(ExplosionCondition.survivesExplosion()))))
                        .item(GirderStrutBlockItem::new)
                        .tag(CMGTags.STRUT_ITEM)
                        .model((c, p) ->
                                p.withExistingParent(c.getName(), CreateMoreGirder.asResource("block/andesite_metal_girder_strut/item"))
                        )
                        .build()
                        .register();

        // Brass Metal Girder (OG metal_girder silhouette in brass)
        public static final BlockEntry<BrassMetalGirderBlock> BRASS_METAL_GIRDER =
                REGISTRATE.block("brass_metal_girder", BrassMetalGirderBlock::new)
                        .initialProperties(SharedProperties::softMetal)
                        .properties(p -> p.mapColor(MapColor.COLOR_YELLOW).sound(SoundType.NETHERITE_BLOCK))
                        .transform(pickaxeOnly())
                        .onRegister(CreateRegistrate.blockModel(() -> AndesiteMetalGirderConnectedModel::new))
                        .tag(CMGTags.GIRDER_BLOCK, CMGTags.PAVING_GIRDER)
                        .blockstate(GenericGirderGenerator::blockState)
                        .item().tag(CMGTags.GIRDER_ITEM).model(GenericGirderGenerator::itemModel).build()
                        .register();

        public static final BlockEntry<BrassMetalGirderEncasedShaftBlock> BRASS_METAL_GIRDER_ENCASED_SHAFT =
                REGISTRATE.block("brass_metal_girder_encased_shaft", BrassMetalGirderEncasedShaftBlock::new)
                        .initialProperties(SharedProperties::softMetal)
                        .properties(p -> p.mapColor(MapColor.COLOR_GRAY).sound(SoundType.NETHERITE_BLOCK))
                        .transform(pickaxeOnly())
                        .tag(CMGTags.GIRDER_ENCASED_SHAFT_BLOCK)
                        .blockstate(GenericGirderGenerator::blockStateWithShaft)
                        .loot((lt, block) -> lt.add(block, LootTable.lootTable()
                                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                                        .add(LootItem.lootTableItem(BRASS_METAL_GIRDER.get()))
                                        .when(ExplosionCondition.survivesExplosion()))
                                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                                        .add(LootItem.lootTableItem(AllBlocks.SHAFT.get()))
                                        .when(ExplosionCondition.survivesExplosion()))))
                        .register();

        // Strut
        public static final BlockEntry<GirderStrutBlock> BRASS_METAL_GIRDER_STRUT =
                REGISTRATE.block("brass_metal_girder_strut", GirderStrutBlock.brass_metal())
                        .initialProperties(SharedProperties::softMetal)
                        .properties(p -> p.destroyTime(0.3f).noOcclusion())
                        .transform(pickaxeOnly())
                        .blockstate((c, p) -> p.directionalBlock(c.get(),
                                (state) -> p.models().getExistingFile(CreateMoreGirder.asResource(
                                        "block/brass_metal_girder_strut/attachment")
                                )))
                        .onRegister(CreateRegistrate.blockModel(() -> GirderStrutModelBuilder::new))
                        .tag(AllTags.AllBlockTags.SAFE_NBT.tag)
                        .loot((lt, block) -> lt.add(block, LootTable.lootTable()
                                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                                        .add(LootItem.lootTableItem(block))
                                        .when(ExplosionCondition.survivesExplosion()))))
                        .item(GirderStrutBlockItem::new)
                        .tag(CMGTags.STRUT_ITEM)
                        .model((c, p) ->
                                p.withExistingParent(c.getName(), CreateMoreGirder.asResource("block/brass_metal_girder_strut/item"))
                        )
                        .build()
                        .register();

        // Copper Metal Girder + weathering chain (OG metal_girder silhouette in copper)
        public static final BlockEntry<CopperMetalGirderBlock> COPPER_METAL_GIRDER =
                REGISTRATE.block("copper_metal_girder", CopperMetalGirderBlock::new)
                        .initialProperties(SharedProperties::softMetal)
                        .properties(p -> p.mapColor(MapColor.COLOR_ORANGE).sound(SoundType.NETHERITE_BLOCK).randomTicks())
                        .transform(pickaxeOnly())
                        .onRegister(CreateRegistrate.blockModel(() -> com.agent772.createmoregirder.content.andesite_metal_girder.AndesiteMetalGirderConnectedModel::new))
                        .tag(CMGTags.GIRDER_BLOCK, CMGTags.PAVING_GIRDER)
                        .blockstate(GenericGirderGenerator::blockState)
                        .item().tag(CMGTags.GIRDER_ITEM).model(GenericGirderGenerator::itemModel).build()
                        .register();

        public static final BlockEntry<CopperMetalGirderEncasedShaftBlock> COPPER_METAL_GIRDER_ENCASED_SHAFT =
                REGISTRATE.block("copper_metal_girder_encased_shaft", CopperMetalGirderEncasedShaftBlock::new)
                        .initialProperties(SharedProperties::softMetal)
                        .properties(p -> p.mapColor(MapColor.COLOR_GRAY).sound(SoundType.NETHERITE_BLOCK).randomTicks())
                        .transform(pickaxeOnly())
                        .tag(CMGTags.GIRDER_ENCASED_SHAFT_BLOCK)
                        .blockstate(GenericGirderGenerator::blockStateWithShaft)
                        .loot((lt, block) -> lt.add(block, LootTable.lootTable()
                                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                                        .add(LootItem.lootTableItem(COPPER_METAL_GIRDER.get()))
                                        .when(ExplosionCondition.survivesExplosion()))
                                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                                        .add(LootItem.lootTableItem(AllBlocks.SHAFT.get()))
                                        .when(ExplosionCondition.survivesExplosion()))))
                        .register();

        public static final BlockEntry<ExposedCopperMetalGirderBlock> EXPOSED_COPPER_METAL_GIRDER =
                REGISTRATE.block("exposed_copper_metal_girder", ExposedCopperMetalGirderBlock::new)
                        .initialProperties(SharedProperties::softMetal)
                        .properties(p -> p.mapColor(MapColor.TERRACOTTA_LIGHT_GRAY).sound(SoundType.NETHERITE_BLOCK).randomTicks())
                        .transform(pickaxeOnly())
                        .onRegister(CreateRegistrate.blockModel(() -> com.agent772.createmoregirder.content.andesite_metal_girder.AndesiteMetalGirderConnectedModel::new))
                        .tag(CMGTags.GIRDER_BLOCK, CMGTags.PAVING_GIRDER)
                        .blockstate(GenericGirderGenerator::blockState)
                        .item().tag(CMGTags.GIRDER_ITEM).model(GenericGirderGenerator::itemModel).build()
                        .register();

        public static final BlockEntry<ExposedCopperMetalGirderEncasedShaftBlock> EXPOSED_COPPER_METAL_GIRDER_ENCASED_SHAFT =
                REGISTRATE.block("exposed_copper_metal_girder_encased_shaft", ExposedCopperMetalGirderEncasedShaftBlock::new)
                        .initialProperties(SharedProperties::softMetal)
                        .properties(p -> p.mapColor(MapColor.COLOR_GRAY).sound(SoundType.NETHERITE_BLOCK).randomTicks())
                        .transform(pickaxeOnly())
                        .tag(CMGTags.GIRDER_ENCASED_SHAFT_BLOCK)
                        .blockstate(GenericGirderGenerator::blockStateWithShaft)
                        .loot((lt, block) -> lt.add(block, LootTable.lootTable()
                                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                                        .add(LootItem.lootTableItem(EXPOSED_COPPER_METAL_GIRDER.get()))
                                        .when(ExplosionCondition.survivesExplosion()))
                                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                                        .add(LootItem.lootTableItem(AllBlocks.SHAFT.get()))
                                        .when(ExplosionCondition.survivesExplosion()))))
                        .register();

        public static final BlockEntry<WeatheredCopperMetalGirderBlock> WEATHERED_COPPER_METAL_GIRDER =
                REGISTRATE.block("weathered_copper_metal_girder", WeatheredCopperMetalGirderBlock::new)
                        .initialProperties(SharedProperties::softMetal)
                        .properties(p -> p.mapColor(MapColor.WARPED_STEM).sound(SoundType.NETHERITE_BLOCK).randomTicks())
                        .transform(pickaxeOnly())
                        .onRegister(CreateRegistrate.blockModel(() -> com.agent772.createmoregirder.content.andesite_metal_girder.AndesiteMetalGirderConnectedModel::new))
                        .tag(CMGTags.GIRDER_BLOCK, CMGTags.PAVING_GIRDER)
                        .blockstate(GenericGirderGenerator::blockState)
                        .item().tag(CMGTags.GIRDER_ITEM).model(GenericGirderGenerator::itemModel).build()
                        .register();

        public static final BlockEntry<WeatheredCopperMetalGirderEncasedShaftBlock> WEATHERED_COPPER_METAL_GIRDER_ENCASED_SHAFT =
                REGISTRATE.block("weathered_copper_metal_girder_encased_shaft", WeatheredCopperMetalGirderEncasedShaftBlock::new)
                        .initialProperties(SharedProperties::softMetal)
                        .properties(p -> p.mapColor(MapColor.COLOR_GRAY).sound(SoundType.NETHERITE_BLOCK).randomTicks())
                        .transform(pickaxeOnly())
                        .tag(CMGTags.GIRDER_ENCASED_SHAFT_BLOCK)
                        .blockstate(GenericGirderGenerator::blockStateWithShaft)
                        .loot((lt, block) -> lt.add(block, LootTable.lootTable()
                                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                                        .add(LootItem.lootTableItem(WEATHERED_COPPER_METAL_GIRDER.get()))
                                        .when(ExplosionCondition.survivesExplosion()))
                                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                                        .add(LootItem.lootTableItem(AllBlocks.SHAFT.get()))
                                        .when(ExplosionCondition.survivesExplosion()))))
                        .register();

        public static final BlockEntry<OxidizedCopperMetalGirderBlock> OXIDIZED_COPPER_METAL_GIRDER =
                REGISTRATE.block("oxidized_copper_metal_girder", OxidizedCopperMetalGirderBlock::new)
                        .initialProperties(SharedProperties::softMetal)
                        .properties(p -> p.mapColor(MapColor.WARPED_NYLIUM).sound(SoundType.NETHERITE_BLOCK).randomTicks())
                        .transform(pickaxeOnly())
                        .onRegister(CreateRegistrate.blockModel(() -> com.agent772.createmoregirder.content.andesite_metal_girder.AndesiteMetalGirderConnectedModel::new))
                        .tag(CMGTags.GIRDER_BLOCK, CMGTags.PAVING_GIRDER)
                        .blockstate(GenericGirderGenerator::blockState)
                        .item().tag(CMGTags.GIRDER_ITEM).model(GenericGirderGenerator::itemModel).build()
                        .register();

        public static final BlockEntry<OxidizedCopperMetalGirderEncasedShaftBlock> OXIDIZED_COPPER_METAL_GIRDER_ENCASED_SHAFT =
                REGISTRATE.block("oxidized_copper_metal_girder_encased_shaft", OxidizedCopperMetalGirderEncasedShaftBlock::new)
                        .initialProperties(SharedProperties::softMetal)
                        .properties(p -> p.mapColor(MapColor.COLOR_GRAY).sound(SoundType.NETHERITE_BLOCK).randomTicks())
                        .transform(pickaxeOnly())
                        .tag(CMGTags.GIRDER_ENCASED_SHAFT_BLOCK)
                        .blockstate(GenericGirderGenerator::blockStateWithShaft)
                        .loot((lt, block) -> lt.add(block, LootTable.lootTable()
                                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                                        .add(LootItem.lootTableItem(OXIDIZED_COPPER_METAL_GIRDER.get()))
                                        .when(ExplosionCondition.survivesExplosion()))
                                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                                        .add(LootItem.lootTableItem(AllBlocks.SHAFT.get()))
                                        .when(ExplosionCondition.survivesExplosion()))))
                        .register();

        public static final BlockEntry<WaxedCopperMetalGirderBlock> WAXED_COPPER_METAL_GIRDER =
                REGISTRATE.block("waxed_copper_metal_girder", WaxedCopperMetalGirderBlock::new)
                        .initialProperties(SharedProperties::softMetal)
                        .properties(p -> p.mapColor(MapColor.COLOR_ORANGE).sound(SoundType.NETHERITE_BLOCK))
                        .transform(pickaxeOnly())
                        .onRegister(CreateRegistrate.blockModel(() -> com.agent772.createmoregirder.content.andesite_metal_girder.AndesiteMetalGirderConnectedModel::new))
                        .tag(CMGTags.GIRDER_BLOCK, CMGTags.PAVING_GIRDER)
                        .blockstate(GenericGirderGenerator::blockState)
                        .item().tag(CMGTags.GIRDER_ITEM).model(GenericGirderGenerator::itemModel).build()
                        .register();

        public static final BlockEntry<WaxedCopperMetalGirderEncasedShaftBlock> WAXED_COPPER_METAL_GIRDER_ENCASED_SHAFT =
                REGISTRATE.block("waxed_copper_metal_girder_encased_shaft", WaxedCopperMetalGirderEncasedShaftBlock::new)
                        .initialProperties(SharedProperties::softMetal)
                        .properties(p -> p.mapColor(MapColor.COLOR_GRAY).sound(SoundType.NETHERITE_BLOCK))
                        .transform(pickaxeOnly())
                        .tag(CMGTags.GIRDER_ENCASED_SHAFT_BLOCK)
                        .blockstate(GenericGirderGenerator::blockStateWithShaft)
                        .loot((lt, block) -> lt.add(block, LootTable.lootTable()
                                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                                        .add(LootItem.lootTableItem(WAXED_COPPER_METAL_GIRDER.get()))
                                        .when(ExplosionCondition.survivesExplosion()))
                                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                                        .add(LootItem.lootTableItem(AllBlocks.SHAFT.get()))
                                        .when(ExplosionCondition.survivesExplosion()))))
                        .register();

        // Strut
        public static final BlockEntry<GirderStrutBlock> WAXED_COPPER_METAL_GIRDER_STRUT =
                REGISTRATE.block("waxed_copper_metal_girder_strut", GirderStrutBlock.waxed_copper_metal())
                        .initialProperties(SharedProperties::softMetal)
                        .properties(p -> p.destroyTime(0.3f).noOcclusion())
                        .transform(pickaxeOnly())
                        .blockstate((c, p) -> p.directionalBlock(c.get(),
                                (state) -> p.models().getExistingFile(CreateMoreGirder.asResource(
                                        "block/waxed_copper_metal_girder_strut/attachment")
                                )))
                        .onRegister(CreateRegistrate.blockModel(() -> GirderStrutModelBuilder::new))
                        .tag(AllTags.AllBlockTags.SAFE_NBT.tag)
                        .loot((lt, block) -> lt.add(block, LootTable.lootTable()
                                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                                        .add(LootItem.lootTableItem(block))
                                        .when(ExplosionCondition.survivesExplosion()))))
                        .item(GirderStrutBlockItem::new)
                        .tag(CMGTags.STRUT_ITEM)
                        .model((c, p) ->
                                p.withExistingParent(c.getName(), CreateMoreGirder.asResource("block/waxed_copper_metal_girder_strut/item"))
                        )
                        .build()
                        .register();

        public static final BlockEntry<WaxedExposedCopperMetalGirderBlock> WAXED_EXPOSED_COPPER_METAL_GIRDER =
                REGISTRATE.block("waxed_exposed_copper_metal_girder", WaxedExposedCopperMetalGirderBlock::new)
                        .initialProperties(SharedProperties::softMetal)
                        .properties(p -> p.mapColor(MapColor.TERRACOTTA_LIGHT_GRAY).sound(SoundType.NETHERITE_BLOCK))
                        .transform(pickaxeOnly())
                        .onRegister(CreateRegistrate.blockModel(() -> com.agent772.createmoregirder.content.andesite_metal_girder.AndesiteMetalGirderConnectedModel::new))
                        .tag(CMGTags.GIRDER_BLOCK, CMGTags.PAVING_GIRDER)
                        .blockstate(GenericGirderGenerator::blockState)
                        .item().tag(CMGTags.GIRDER_ITEM).model(GenericGirderGenerator::itemModel).build()
                        .register();

        public static final BlockEntry<WaxedExposedCopperMetalGirderEncasedShaftBlock> WAXED_EXPOSED_COPPER_METAL_GIRDER_ENCASED_SHAFT =
                REGISTRATE.block("waxed_exposed_copper_metal_girder_encased_shaft", WaxedExposedCopperMetalGirderEncasedShaftBlock::new)
                        .initialProperties(SharedProperties::softMetal)
                        .properties(p -> p.mapColor(MapColor.COLOR_GRAY).sound(SoundType.NETHERITE_BLOCK))
                        .transform(pickaxeOnly())
                        .tag(CMGTags.GIRDER_ENCASED_SHAFT_BLOCK)
                        .blockstate(GenericGirderGenerator::blockStateWithShaft)
                        .loot((lt, block) -> lt.add(block, LootTable.lootTable()
                                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                                        .add(LootItem.lootTableItem(WAXED_EXPOSED_COPPER_METAL_GIRDER.get()))
                                        .when(ExplosionCondition.survivesExplosion()))
                                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                                        .add(LootItem.lootTableItem(AllBlocks.SHAFT.get()))
                                        .when(ExplosionCondition.survivesExplosion()))))
                        .register();

        // Strut
        public static final BlockEntry<GirderStrutBlock> WAXED_EXPOSED_COPPER_METAL_GIRDER_STRUT =
                REGISTRATE.block("waxed_exposed_copper_metal_girder_strut", GirderStrutBlock.waxed_exposed_copper_metal())
                        .initialProperties(SharedProperties::softMetal)
                        .properties(p -> p.destroyTime(0.3f).noOcclusion())
                        .transform(pickaxeOnly())
                        .blockstate((c, p) -> p.directionalBlock(c.get(),
                                (state) -> p.models().getExistingFile(CreateMoreGirder.asResource(
                                        "block/waxed_exposed_copper_metal_girder_strut/attachment")
                                )))
                        .onRegister(CreateRegistrate.blockModel(() -> GirderStrutModelBuilder::new))
                        .tag(AllTags.AllBlockTags.SAFE_NBT.tag)
                        .loot((lt, block) -> lt.add(block, LootTable.lootTable()
                                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                                        .add(LootItem.lootTableItem(block))
                                        .when(ExplosionCondition.survivesExplosion()))))
                        .item(GirderStrutBlockItem::new)
                        .tag(CMGTags.STRUT_ITEM)
                        .model((c, p) ->
                                p.withExistingParent(c.getName(), CreateMoreGirder.asResource("block/waxed_exposed_copper_metal_girder_strut/item"))
                        )
                        .build()
                        .register();

        public static final BlockEntry<WaxedWeatheredCopperMetalGirderBlock> WAXED_WEATHERED_COPPER_METAL_GIRDER =
                REGISTRATE.block("waxed_weathered_copper_metal_girder", WaxedWeatheredCopperMetalGirderBlock::new)
                        .initialProperties(SharedProperties::softMetal)
                        .properties(p -> p.mapColor(MapColor.WARPED_STEM).sound(SoundType.NETHERITE_BLOCK))
                        .transform(pickaxeOnly())
                        .onRegister(CreateRegistrate.blockModel(() -> com.agent772.createmoregirder.content.andesite_metal_girder.AndesiteMetalGirderConnectedModel::new))
                        .tag(CMGTags.GIRDER_BLOCK, CMGTags.PAVING_GIRDER)
                        .blockstate(GenericGirderGenerator::blockState)
                        .item().tag(CMGTags.GIRDER_ITEM).model(GenericGirderGenerator::itemModel).build()
                        .register();

        public static final BlockEntry<WaxedWeatheredCopperMetalGirderEncasedShaftBlock> WAXED_WEATHERED_COPPER_METAL_GIRDER_ENCASED_SHAFT =
                REGISTRATE.block("waxed_weathered_copper_metal_girder_encased_shaft", WaxedWeatheredCopperMetalGirderEncasedShaftBlock::new)
                        .initialProperties(SharedProperties::softMetal)
                        .properties(p -> p.mapColor(MapColor.COLOR_GRAY).sound(SoundType.NETHERITE_BLOCK))
                        .transform(pickaxeOnly())
                        .tag(CMGTags.GIRDER_ENCASED_SHAFT_BLOCK)
                        .blockstate(GenericGirderGenerator::blockStateWithShaft)
                        .loot((lt, block) -> lt.add(block, LootTable.lootTable()
                                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                                        .add(LootItem.lootTableItem(WAXED_WEATHERED_COPPER_METAL_GIRDER.get()))
                                        .when(ExplosionCondition.survivesExplosion()))
                                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                                        .add(LootItem.lootTableItem(AllBlocks.SHAFT.get()))
                                        .when(ExplosionCondition.survivesExplosion()))))
                        .register();

        // Strut
        public static final BlockEntry<GirderStrutBlock> WAXED_WEATHERED_COPPER_METAL_GIRDER_STRUT =
                REGISTRATE.block("waxed_weathered_copper_metal_girder_strut", GirderStrutBlock.waxed_weathered_copper_metal())
                        .initialProperties(SharedProperties::softMetal)
                        .properties(p -> p.destroyTime(0.3f).noOcclusion())
                        .transform(pickaxeOnly())
                        .blockstate((c, p) -> p.directionalBlock(c.get(),
                                (state) -> p.models().getExistingFile(CreateMoreGirder.asResource(
                                        "block/waxed_weathered_copper_metal_girder_strut/attachment")
                                )))
                        .onRegister(CreateRegistrate.blockModel(() -> GirderStrutModelBuilder::new))
                        .tag(AllTags.AllBlockTags.SAFE_NBT.tag)
                        .loot((lt, block) -> lt.add(block, LootTable.lootTable()
                                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                                        .add(LootItem.lootTableItem(block))
                                        .when(ExplosionCondition.survivesExplosion()))))
                        .item(GirderStrutBlockItem::new)
                        .tag(CMGTags.STRUT_ITEM)
                        .model((c, p) ->
                                p.withExistingParent(c.getName(), CreateMoreGirder.asResource("block/waxed_weathered_copper_metal_girder_strut/item"))
                        )
                        .build()
                        .register();

        public static final BlockEntry<WaxedOxidizedCopperMetalGirderBlock> WAXED_OXIDIZED_COPPER_METAL_GIRDER =
                REGISTRATE.block("waxed_oxidized_copper_metal_girder", WaxedOxidizedCopperMetalGirderBlock::new)
                        .initialProperties(SharedProperties::softMetal)
                        .properties(p -> p.mapColor(MapColor.WARPED_NYLIUM).sound(SoundType.NETHERITE_BLOCK))
                        .transform(pickaxeOnly())
                        .onRegister(CreateRegistrate.blockModel(() -> com.agent772.createmoregirder.content.andesite_metal_girder.AndesiteMetalGirderConnectedModel::new))
                        .tag(CMGTags.GIRDER_BLOCK, CMGTags.PAVING_GIRDER)
                        .blockstate(GenericGirderGenerator::blockState)
                        .item().tag(CMGTags.GIRDER_ITEM).model(GenericGirderGenerator::itemModel).build()
                        .register();

        public static final BlockEntry<WaxedOxidizedCopperMetalGirderEncasedShaftBlock> WAXED_OXIDIZED_COPPER_METAL_GIRDER_ENCASED_SHAFT =
                REGISTRATE.block("waxed_oxidized_copper_metal_girder_encased_shaft", WaxedOxidizedCopperMetalGirderEncasedShaftBlock::new)
                        .initialProperties(SharedProperties::softMetal)
                        .properties(p -> p.mapColor(MapColor.COLOR_GRAY).sound(SoundType.NETHERITE_BLOCK))
                        .transform(pickaxeOnly())
                        .tag(CMGTags.GIRDER_ENCASED_SHAFT_BLOCK)
                        .blockstate(GenericGirderGenerator::blockStateWithShaft)
                        .loot((lt, block) -> lt.add(block, LootTable.lootTable()
                                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                                        .add(LootItem.lootTableItem(WAXED_OXIDIZED_COPPER_METAL_GIRDER.get()))
                                        .when(ExplosionCondition.survivesExplosion()))
                                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                                        .add(LootItem.lootTableItem(AllBlocks.SHAFT.get()))
                                        .when(ExplosionCondition.survivesExplosion()))))
                        .register();

        // Strut
        public static final BlockEntry<GirderStrutBlock> WAXED_OXIDIZED_COPPER_METAL_GIRDER_STRUT =
                REGISTRATE.block("waxed_oxidized_copper_metal_girder_strut", GirderStrutBlock.waxed_oxidized_copper_metal())
                        .initialProperties(SharedProperties::softMetal)
                        .properties(p -> p.destroyTime(0.3f).noOcclusion())
                        .transform(pickaxeOnly())
                        .blockstate((c, p) -> p.directionalBlock(c.get(),
                                (state) -> p.models().getExistingFile(CreateMoreGirder.asResource(
                                        "block/waxed_oxidized_copper_metal_girder_strut/attachment")
                                )))
                        .onRegister(CreateRegistrate.blockModel(() -> GirderStrutModelBuilder::new))
                        .tag(AllTags.AllBlockTags.SAFE_NBT.tag)
                        .loot((lt, block) -> lt.add(block, LootTable.lootTable()
                                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                                        .add(LootItem.lootTableItem(block))
                                        .when(ExplosionCondition.survivesExplosion()))))
                        .item(GirderStrutBlockItem::new)
                        .tag(CMGTags.STRUT_ITEM)
                        .model((c, p) ->
                                p.withExistingParent(c.getName(), CreateMoreGirder.asResource("block/waxed_oxidized_copper_metal_girder_strut/item"))
                        )
                        .build()
                        .register();

        // Strut
        public static final BlockEntry<GirderStrutBlock> BRASS_GIRDER_STRUT =
                REGISTRATE.block("brass_girder_strut", GirderStrutBlock.brass())
                        .initialProperties(SharedProperties::softMetal)
                        .properties(p -> p.destroyTime(0.3f).noOcclusion())
                        .transform(pickaxeOnly())
                        .blockstate((c, p) -> p.directionalBlock(c.get(),
                                (state) -> p.models().getExistingFile(CreateMoreGirder.asResource(
                                        "block/brass_girder_strut/attachment")
                                )))
                        .onRegister(CreateRegistrate.blockModel(() -> GirderStrutModelBuilder::new))
                        .tag(AllTags.AllBlockTags.SAFE_NBT.tag)
                        .loot((lt, block) -> lt.add(block, LootTable.lootTable()
                                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                                        .add(LootItem.lootTableItem(block))
                                        .when(ExplosionCondition.survivesExplosion()))))
                        .item(GirderStrutBlockItem::new)
                        .tag(CMGTags.STRUT_ITEM)
                        .model((c, p) ->
                                p.withExistingParent(c.getName(), CreateMoreGirder.asResource("block/brass_girder_strut/item"))
                        )
                        .build()
                        .register();

        // Copper Girder and its weathering stages
        public static final BlockEntry<CopperGirderBlock> COPPER_GIRDER =
                REGISTRATE.block("copper_girder", CopperGirderBlock::new)
                        .initialProperties(SharedProperties::softMetal)
                        .properties(p -> p.mapColor(MapColor.COLOR_YELLOW).sound(SoundType.NETHERITE_BLOCK).randomTicks())
                        .transform(pickaxeOnly())
                        .onRegister(CreateRegistrate.blockModel(() -> ConnectedGirderModel::new))
                        .tag(CMGTags.GIRDER_BLOCK, CMGTags.PAVING_GIRDER)
                        .blockstate(GenericGirderGenerator::blockState)
                        .item().tag(CMGTags.GIRDER_ITEM).model(GenericGirderGenerator::itemModel).build()
                        .register();
        public static final BlockEntry<CopperGirderEncasedShaftBlock> COPPER_GIRDER_ENCASED_SHAFT =
                REGISTRATE.block("copper_girder_encased_shaft", CopperGirderEncasedShaftBlock::new)
                        .initialProperties(SharedProperties::softMetal)
                        .properties(p -> p.mapColor(MapColor.COLOR_GRAY).sound(SoundType.NETHERITE_BLOCK).randomTicks())
                        .transform(pickaxeOnly())
                        .tag(CMGTags.GIRDER_ENCASED_SHAFT_BLOCK)
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
                        .onRegister(CreateRegistrate.blockModel(() -> ConnectedGirderModel::new))
                        .tag(CMGTags.GIRDER_BLOCK, CMGTags.PAVING_GIRDER)
                        .blockstate(GenericGirderGenerator::blockState)
                        .item().tag(CMGTags.GIRDER_ITEM).model(GenericGirderGenerator::itemModel).build()
                        .register();
        public static final BlockEntry<ExposedCopperGirderEncasedShaftBlock> EXPOSED_COPPER_GIRDER_ENCASED_SHAFT =
                REGISTRATE.block("exposed_copper_girder_encased_shaft", ExposedCopperGirderEncasedShaftBlock::new)
                        .initialProperties(SharedProperties::softMetal)
                        .properties(p -> p.mapColor(MapColor.COLOR_GRAY).sound(SoundType.NETHERITE_BLOCK).randomTicks())
                        .transform(pickaxeOnly())
                        .tag(CMGTags.GIRDER_ENCASED_SHAFT_BLOCK)
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
                        .onRegister(CreateRegistrate.blockModel(() -> ConnectedGirderModel::new))
                        .tag(CMGTags.GIRDER_BLOCK, CMGTags.PAVING_GIRDER)
                        .blockstate(GenericGirderGenerator::blockState)
                        .item().tag(CMGTags.GIRDER_ITEM).model(GenericGirderGenerator::itemModel).build()
                        .register();
        public static final BlockEntry<WeatheredCopperGirderEncasedShaftBlock> WEATHERED_COPPER_GIRDER_ENCASED_SHAFT =
            REGISTRATE.block("weathered_copper_girder_encased_shaft", WeatheredCopperGirderEncasedShaftBlock::new)
                    .initialProperties(SharedProperties::softMetal)
                    .properties(p -> p.mapColor(MapColor.COLOR_GRAY).sound(SoundType.NETHERITE_BLOCK).randomTicks())
                    .transform(pickaxeOnly())
                    .tag(CMGTags.GIRDER_ENCASED_SHAFT_BLOCK)
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
                        .onRegister(CreateRegistrate.blockModel(() -> ConnectedGirderModel::new))
                        .tag(CMGTags.GIRDER_BLOCK, CMGTags.PAVING_GIRDER)
                        .blockstate(GenericGirderGenerator::blockState)
                        .item().tag(CMGTags.GIRDER_ITEM).model(GenericGirderGenerator::itemModel).build()
                        .register();
        public static final BlockEntry<OxidizedCopperGirderEncasedShaftBlock> OXIDIZED_COPPER_GIRDER_ENCASED_SHAFT =
            REGISTRATE.block("oxidized_copper_girder_encased_shaft", OxidizedCopperGirderEncasedShaftBlock::new)
                    .initialProperties(SharedProperties::softMetal)
                    .properties(p -> p.mapColor(MapColor.COLOR_GRAY).sound(SoundType.NETHERITE_BLOCK).randomTicks())
                    .transform(pickaxeOnly())
                    .tag(CMGTags.GIRDER_ENCASED_SHAFT_BLOCK)
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
                        .onRegister(CreateRegistrate.blockModel(() -> ConnectedGirderModel::new))
                        .tag(CMGTags.GIRDER_BLOCK, CMGTags.PAVING_GIRDER)
                        .blockstate(GenericGirderGenerator::blockState)
                        .item().tag(CMGTags.GIRDER_ITEM).model(GenericGirderGenerator::itemModel).build()
                        .register();
        public static final BlockEntry<WaxedCopperGirderEncasedShaftBlock> WAXED_COPPER_GIRDER_ENCASED_SHAFT =
                REGISTRATE.block("waxed_copper_girder_encased_shaft", WaxedCopperGirderEncasedShaftBlock::new)
                        .initialProperties(SharedProperties::softMetal)
                        .properties(p -> p.mapColor(MapColor.COLOR_GRAY).sound(SoundType.NETHERITE_BLOCK))
                        .transform(pickaxeOnly())
                        .tag(CMGTags.GIRDER_ENCASED_SHAFT_BLOCK)
                        .blockstate(GenericGirderGenerator::blockStateWithShaft)
                        .loot((lt, block) -> lt.add(block, LootTable.lootTable()
                                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                                        .add(LootItem.lootTableItem(WAXED_COPPER_GIRDER.get()))
                                        .when(ExplosionCondition.survivesExplosion()))
                                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                                        .add(LootItem.lootTableItem(AllBlocks.SHAFT.get()))
                                        .when(ExplosionCondition.survivesExplosion()))))
                        .register();
        
        // Strut
        public static final BlockEntry<GirderStrutBlock> WAXED_COPPER_GIRDER_STRUT =
                REGISTRATE.block("waxed_copper_girder_strut", GirderStrutBlock.waxed_copper())
                        .initialProperties(SharedProperties::softMetal)
                        .properties(p -> p.destroyTime(0.3f).noOcclusion())
                        .transform(pickaxeOnly())
                        .blockstate((c, p) -> p.directionalBlock(c.get(),
                                (state) -> p.models().getExistingFile(CreateMoreGirder.asResource(
                                        "block/waxed_copper_girder_strut/attachment")
                                )))
                        .onRegister(CreateRegistrate.blockModel(() -> GirderStrutModelBuilder::new))
                        .tag(AllTags.AllBlockTags.SAFE_NBT.tag)
                        .loot((lt, block) -> lt.add(block, LootTable.lootTable()
                                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                                        .add(LootItem.lootTableItem(block))
                                        .when(ExplosionCondition.survivesExplosion()))))
                        .item(GirderStrutBlockItem::new)
                        .tag(CMGTags.STRUT_ITEM)
                        .model((c, p) ->
                                p.withExistingParent(c.getName(), CreateMoreGirder.asResource("block/waxed_copper_girder_strut/item"))
                        )
                        .build()
                        .register();

        public static final BlockEntry<WaxedExposedCopperGirderBlock> WAXED_EXPOSED_COPPER_GIRDER =
                REGISTRATE.block("waxed_exposed_copper_girder", WaxedExposedCopperGirderBlock::new)
                        .initialProperties(SharedProperties::softMetal)
                        .properties(p -> p.mapColor(MapColor.COLOR_YELLOW).sound(SoundType.NETHERITE_BLOCK))
                        .transform(pickaxeOnly())
                        .onRegister(CreateRegistrate.blockModel(() -> ConnectedGirderModel::new))
                        .tag(CMGTags.GIRDER_BLOCK, CMGTags.PAVING_GIRDER)
                        .blockstate(GenericGirderGenerator::blockState)
                        .item().tag(CMGTags.GIRDER_ITEM).model(GenericGirderGenerator::itemModel).build()
                        .register();
        public static final BlockEntry<WaxedExposedCopperGirderEncasedShaftBlock> WAXED_EXPOSED_COPPER_GIRDER_ENCASED_SHAFT =
                REGISTRATE.block("waxed_exposed_copper_girder_encased_shaft", WaxedExposedCopperGirderEncasedShaftBlock::new)
                        .initialProperties(SharedProperties::softMetal)
                        .properties(p -> p.mapColor(MapColor.COLOR_GRAY).sound(SoundType.NETHERITE_BLOCK))
                        .transform(pickaxeOnly())
                        .tag(CMGTags.GIRDER_ENCASED_SHAFT_BLOCK)
                        .blockstate(GenericGirderGenerator::blockStateWithShaft)
                        .loot((lt, block) -> lt.add(block, LootTable.lootTable()
                                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                                        .add(LootItem.lootTableItem(WAXED_EXPOSED_COPPER_GIRDER.get()))
                                        .when(ExplosionCondition.survivesExplosion()))
                                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                                        .add(LootItem.lootTableItem(AllBlocks.SHAFT.get()))
                                        .when(ExplosionCondition.survivesExplosion()))))
                        .register();
        // Strut
        public static final BlockEntry<GirderStrutBlock> WAXED_EXPOSED_COPPER_GIRDER_STRUT =
                REGISTRATE.block("waxed_exposed_copper_girder_strut", GirderStrutBlock.waxed_exposed_copper())
                        .initialProperties(SharedProperties::softMetal)
                        .properties(p -> p.destroyTime(0.3f).noOcclusion())
                        .transform(pickaxeOnly())
                        .blockstate((c, p) -> p.directionalBlock(c.get(),
                                (state) -> p.models().getExistingFile(CreateMoreGirder.asResource(
                                        "block/waxed_exposed_copper_girder_strut/attachment")
                                )))
                        .onRegister(CreateRegistrate.blockModel(() -> GirderStrutModelBuilder::new))
                        .tag(AllTags.AllBlockTags.SAFE_NBT.tag)
                        .loot((lt, block) -> lt.add(block, LootTable.lootTable()
                                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                                        .add(LootItem.lootTableItem(block))
                                        .when(ExplosionCondition.survivesExplosion()))))
                        .item(GirderStrutBlockItem::new)
                        .tag(CMGTags.STRUT_ITEM)
                        .model((c, p) ->
                                p.withExistingParent(c.getName(), CreateMoreGirder.asResource("block/waxed_exposed_copper_girder_strut/item"))
                        )
                        .build()
                        .register();


        public static final BlockEntry<WaxedWeatheredCopperGirderBlock> WAXED_WEATHERED_COPPER_GIRDER =
                REGISTRATE.block("waxed_weathered_copper_girder", WaxedWeatheredCopperGirderBlock::new)
                        .initialProperties(SharedProperties::softMetal)
                        .properties(p -> p.mapColor(MapColor.COLOR_YELLOW).sound(SoundType.NETHERITE_BLOCK))
                        .transform(pickaxeOnly())
                        .onRegister(CreateRegistrate.blockModel(() -> ConnectedGirderModel::new))
                        .tag(CMGTags.GIRDER_BLOCK, CMGTags.PAVING_GIRDER)
                        .blockstate(GenericGirderGenerator::blockState)
                        .item().tag(CMGTags.GIRDER_ITEM).model(GenericGirderGenerator::itemModel).build()
                        .register();
        public static final BlockEntry<WaxedWeatheredCopperGirderEncasedShaftBlock> WAXED_WEATHERED_COPPER_GIRDER_ENCASED_SHAFT =
                REGISTRATE.block("waxed_weathered_copper_girder_encased_shaft", WaxedWeatheredCopperGirderEncasedShaftBlock::new)
                        .initialProperties(SharedProperties::softMetal)
                        .properties(p -> p.mapColor(MapColor.COLOR_GRAY).sound(SoundType.NETHERITE_BLOCK))
                        .transform(pickaxeOnly())
                        .tag(CMGTags.GIRDER_ENCASED_SHAFT_BLOCK)
                        .blockstate(GenericGirderGenerator::blockStateWithShaft)
                        .loot((lt, block) -> lt.add(block, LootTable.lootTable()
                                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                                        .add(LootItem.lootTableItem(WAXED_WEATHERED_COPPER_GIRDER.get()))
                                        .when(ExplosionCondition.survivesExplosion()))
                                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                                        .add(LootItem.lootTableItem(AllBlocks.SHAFT.get()))
                                        .when(ExplosionCondition.survivesExplosion()))))
                        .register();

        // Strut
        public static final BlockEntry<GirderStrutBlock> WAXED_WEATHERED_COPPER_GIRDER_STRUT =
                REGISTRATE.block("waxed_weathered_copper_girder_strut", GirderStrutBlock.waxed_weathered_copper())
                        .initialProperties(SharedProperties::softMetal)
                        .properties(p -> p.destroyTime(0.3f).noOcclusion())
                        .transform(pickaxeOnly())
                        .blockstate((c, p) -> p.directionalBlock(c.get(),
                                (state) -> p.models().getExistingFile(CreateMoreGirder.asResource(
                                        "block/waxed_weathered_copper_girder_strut/attachment")
                                )))
                        .onRegister(CreateRegistrate.blockModel(() -> GirderStrutModelBuilder::new))
                        .tag(AllTags.AllBlockTags.SAFE_NBT.tag)
                        .loot((lt, block) -> lt.add(block, LootTable.lootTable()
                                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                                        .add(LootItem.lootTableItem(block))
                                        .when(ExplosionCondition.survivesExplosion()))))
                        .item(GirderStrutBlockItem::new)
                        .tag(CMGTags.STRUT_ITEM)
                        .model((c, p) ->
                                p.withExistingParent(c.getName(), CreateMoreGirder.asResource("block/waxed_weathered_copper_girder_strut/item"))
                        )
                        .build()
                        .register();

        public static final BlockEntry<WaxedOxidizedCopperGirderBlock> WAXED_OXIDIZED_COPPER_GIRDER =
                REGISTRATE.block("waxed_oxidized_copper_girder", WaxedOxidizedCopperGirderBlock::new)
                        .initialProperties(SharedProperties::softMetal)
                        .properties(p -> p.mapColor(MapColor.COLOR_YELLOW).sound(SoundType.NETHERITE_BLOCK))
                        .transform(pickaxeOnly())
                        .onRegister(CreateRegistrate.blockModel(() -> ConnectedGirderModel::new))
                        .tag(CMGTags.GIRDER_BLOCK, CMGTags.PAVING_GIRDER)
                        .blockstate(GenericGirderGenerator::blockState)
                        .item().tag(CMGTags.GIRDER_ITEM).model(GenericGirderGenerator::itemModel).build()
                        .register();
        public static final BlockEntry<WaxedOxidizedCopperGirderEncasedShaftBlock> WAXED_OXIDIZED_COPPER_GIRDER_ENCASED_SHAFT =
                REGISTRATE.block("waxed_oxidized_copper_girder_encased_shaft", WaxedOxidizedCopperGirderEncasedShaftBlock::new)
                        .initialProperties(SharedProperties::softMetal)
                        .properties(p -> p.mapColor(MapColor.COLOR_GRAY).sound(SoundType.NETHERITE_BLOCK))
                        .transform(pickaxeOnly())
                        .tag(CMGTags.GIRDER_ENCASED_SHAFT_BLOCK)
                        .blockstate(GenericGirderGenerator::blockStateWithShaft)
                        .loot((lt, block) -> lt.add(block, LootTable.lootTable()
                                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                                        .add(LootItem.lootTableItem(WAXED_OXIDIZED_COPPER_GIRDER.get()))
                                        .when(ExplosionCondition.survivesExplosion()))
                                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                                        .add(LootItem.lootTableItem(AllBlocks.SHAFT.get()))
                                        .when(ExplosionCondition.survivesExplosion()))))
                        .register();
        // Strut
        public static final BlockEntry<GirderStrutBlock> WAXED_OXIDIZED_COPPER_GIRDER_STRUT =
                REGISTRATE.block("waxed_oxidized_copper_girder_strut", GirderStrutBlock.waxed_oxidized_copper())
                        .initialProperties(SharedProperties::softMetal)
                        .properties(p -> p.destroyTime(0.3f).noOcclusion())
                        .transform(pickaxeOnly())
                        .blockstate((c, p) -> p.directionalBlock(c.get(),
                                (state) -> p.models().getExistingFile(CreateMoreGirder.asResource(
                                        "block/waxed_oxidized_copper_girder_strut/attachment")
                                )))
                        .onRegister(CreateRegistrate.blockModel(() -> GirderStrutModelBuilder::new))
                        .tag(AllTags.AllBlockTags.SAFE_NBT.tag)
                        .loot((lt, block) -> lt.add(block, LootTable.lootTable()
                                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                                        .add(LootItem.lootTableItem(block))
                                        .when(ExplosionCondition.survivesExplosion()))))
                        .item(GirderStrutBlockItem::new)
                        .tag(CMGTags.STRUT_ITEM)
                        .model((c, p) ->
                                p.withExistingParent(c.getName(), CreateMoreGirder.asResource("block/waxed_oxidized_copper_girder_strut/item"))
                        )
                        .build()
                        .register();
        
        // Industrial Iron Girder
        public static final BlockEntry<IndustrialIronGirderBlock> INDUSTRIAL_IRON_GIRDER =
                REGISTRATE.block("industrial_iron_girder", IndustrialIronGirderBlock::new)
                        .initialProperties(SharedProperties::softMetal)
                        .properties(p -> p.mapColor(MapColor.COLOR_YELLOW).sound(SoundType.NETHERITE_BLOCK))
                        .transform(pickaxeOnly())
                        .onRegister(CreateRegistrate.blockModel(() -> ConnectedGirderModel::new))
                        .tag(CMGTags.GIRDER_BLOCK, CMGTags.PAVING_GIRDER)
                        .blockstate(GenericGirderGenerator::blockState)
                        .item().tag(CMGTags.GIRDER_ITEM).model(GenericGirderGenerator::itemModel).build()
                        .register();

        public static final BlockEntry<IndustrialIronGirderEncasedShaftBlock> INDUSTRIAL_IRON_GIRDER_ENCASED_SHAFT =
                REGISTRATE.block("industrial_iron_girder_encased_shaft", IndustrialIronGirderEncasedShaftBlock::new)
                        .initialProperties(SharedProperties::softMetal)
                        .properties(p -> p.mapColor(MapColor.COLOR_GRAY).sound(SoundType.NETHERITE_BLOCK))
                        .transform(pickaxeOnly())
                        .tag(CMGTags.GIRDER_ENCASED_SHAFT_BLOCK)
                        .blockstate(GenericGirderGenerator::blockStateWithShaft)
                        .loot((lt, block) -> lt.add(block, LootTable.lootTable()
                                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                                        .add(LootItem.lootTableItem(INDUSTRIAL_IRON_GIRDER.get()))
                                        .when(ExplosionCondition.survivesExplosion()))
                                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                                        .add(LootItem.lootTableItem(AllBlocks.SHAFT.get()))
                                        .when(ExplosionCondition.survivesExplosion()))))
                        .register();
        
        // Strut
        public static final BlockEntry<GirderStrutBlock> INDUSTRIAL_IRON_GIRDER_STRUT =
                REGISTRATE.block("industrial_iron_girder_strut", GirderStrutBlock.industrial_iron())
                        .initialProperties(SharedProperties::softMetal)
                        .properties(p -> p.destroyTime(0.3f).noOcclusion())
                        .transform(pickaxeOnly())
                        .blockstate((c, p) -> p.directionalBlock(c.get(),
                                (state) -> p.models().getExistingFile(CreateMoreGirder.asResource(
                                        "block/industrial_iron_girder_strut/attachment")
                                )))
                        .onRegister(CreateRegistrate.blockModel(() -> GirderStrutModelBuilder::new))
                        .tag(AllTags.AllBlockTags.SAFE_NBT.tag)
                        .loot((lt, block) -> lt.add(block, LootTable.lootTable()
                                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                                        .add(LootItem.lootTableItem(block))
                                        .when(ExplosionCondition.survivesExplosion()))))
                        .item(GirderStrutBlockItem::new)
                        .tag(CMGTags.STRUT_ITEM)
                        .model((c, p) ->
                                p.withExistingParent(c.getName(), CreateMoreGirder.asResource("block/industrial_iron_girder_strut/item"))
                        )
                        .build()
                        .register();

        // Weathered Iron Girder
        public static final BlockEntry<WeatheredIronGirderBlock> WEATHERED_IRON_GIRDER =
                REGISTRATE.block("weathered_iron_girder", WeatheredIronGirderBlock::new)
                        .initialProperties(SharedProperties::softMetal)
                        .properties(p -> p.mapColor(MapColor.COLOR_YELLOW).sound(SoundType.NETHERITE_BLOCK))
                        .transform(pickaxeOnly())
                        .onRegister(CreateRegistrate.blockModel(() -> ConnectedGirderModel::new))
                        .tag(CMGTags.GIRDER_BLOCK, CMGTags.PAVING_GIRDER)
                        .blockstate(GenericGirderGenerator::blockState)
                        .item().tag(CMGTags.GIRDER_ITEM).model(GenericGirderGenerator::itemModel).build()
                        .register();

        public static final BlockEntry<WeatheredIronGirderEncasedShaftBlock> WEATHERED_IRON_GIRDER_ENCASED_SHAFT =
                REGISTRATE.block("weathered_iron_girder_encased_shaft", WeatheredIronGirderEncasedShaftBlock::new)
                        .initialProperties(SharedProperties::softMetal)
                        .properties(p -> p.mapColor(MapColor.COLOR_GRAY).sound(SoundType.NETHERITE_BLOCK))
                        .transform(pickaxeOnly())
                        .tag(CMGTags.GIRDER_ENCASED_SHAFT_BLOCK)
                        .blockstate(GenericGirderGenerator::blockStateWithShaft)
                        .loot((lt, block) -> lt.add(block, LootTable.lootTable()
                                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                                        .add(LootItem.lootTableItem(WEATHERED_IRON_GIRDER.get()))
                                        .when(ExplosionCondition.survivesExplosion()))
                                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                                        .add(LootItem.lootTableItem(AllBlocks.SHAFT.get()))
                                        .when(ExplosionCondition.survivesExplosion()))))
                        .register();

        // Weathered Iron Metal Girder (OG metal_girder silhouette, weathered-iron palette)
        public static final BlockEntry<WeatheredIronMetalGirderBlock> WEATHERED_IRON_METAL_GIRDER =
                REGISTRATE.block("weathered_iron_metal_girder", WeatheredIronMetalGirderBlock::new)
                        .initialProperties(SharedProperties::softMetal)
                        .properties(p -> p.mapColor(MapColor.COLOR_YELLOW).sound(SoundType.NETHERITE_BLOCK))
                        .transform(pickaxeOnly())
                        .onRegister(CreateRegistrate.blockModel(() -> com.agent772.createmoregirder.content.andesite_metal_girder.AndesiteMetalGirderConnectedModel::new))
                        .tag(CMGTags.GIRDER_BLOCK, CMGTags.PAVING_GIRDER)
                        .blockstate(GenericGirderGenerator::blockState)
                        .item().tag(CMGTags.GIRDER_ITEM).model(GenericGirderGenerator::itemModel).build()
                        .register();

        public static final BlockEntry<WeatheredIronMetalGirderEncasedShaftBlock> WEATHERED_IRON_METAL_GIRDER_ENCASED_SHAFT =
                REGISTRATE.block("weathered_iron_metal_girder_encased_shaft", WeatheredIronMetalGirderEncasedShaftBlock::new)
                        .initialProperties(SharedProperties::softMetal)
                        .properties(p -> p.mapColor(MapColor.COLOR_GRAY).sound(SoundType.NETHERITE_BLOCK))
                        .transform(pickaxeOnly())
                        .tag(CMGTags.GIRDER_ENCASED_SHAFT_BLOCK)
                        .blockstate(GenericGirderGenerator::blockStateWithShaft)
                        .loot((lt, block) -> lt.add(block, LootTable.lootTable()
                                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                                        .add(LootItem.lootTableItem(WEATHERED_IRON_METAL_GIRDER.get()))
                                        .when(ExplosionCondition.survivesExplosion()))
                                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                                        .add(LootItem.lootTableItem(AllBlocks.SHAFT.get()))
                                        .when(ExplosionCondition.survivesExplosion()))))
                        .register();

        // Strut
        public static final BlockEntry<GirderStrutBlock> WEATHERED_IRON_METAL_GIRDER_STRUT =
                REGISTRATE.block("weathered_iron_metal_girder_strut", GirderStrutBlock.weathered_iron_metal())
                        .initialProperties(SharedProperties::softMetal)
                        .properties(p -> p.destroyTime(0.3f).noOcclusion())
                        .transform(pickaxeOnly())
                        .blockstate((c, p) -> p.directionalBlock(c.get(),
                                (state) -> p.models().getExistingFile(CreateMoreGirder.asResource(
                                        "block/weathered_iron_metal_girder_strut/attachment")
                                )))
                        .onRegister(CreateRegistrate.blockModel(() -> GirderStrutModelBuilder::new))
                        .tag(AllTags.AllBlockTags.SAFE_NBT.tag)
                        .loot((lt, block) -> lt.add(block, LootTable.lootTable()
                                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                                        .add(LootItem.lootTableItem(block))
                                        .when(ExplosionCondition.survivesExplosion()))))
                        .item(GirderStrutBlockItem::new)
                        .tag(CMGTags.STRUT_ITEM)
                        .model((c, p) ->
                                p.withExistingParent(c.getName(), CreateMoreGirder.asResource("block/weathered_iron_metal_girder_strut/item"))
                        )
                        .build()
                        .register();

        // Strut for Create's metal_girder (BnB-gated visibility — see CMGCreativeTabs / recipe condition)
        public static final BlockEntry<GirderStrutBlock> CREATE_METAL_GIRDER_STRUT =
                REGISTRATE.block("create_metal_girder_strut", GirderStrutBlock.create_metal())
                        .initialProperties(SharedProperties::softMetal)
                        .properties(p -> p.destroyTime(0.3f).noOcclusion())
                        .transform(pickaxeOnly())
                        .blockstate((c, p) -> p.directionalBlock(c.get(),
                                (state) -> p.models().getExistingFile(CreateMoreGirder.asResource(
                                        "block/create_metal_girder_strut/attachment")
                                )))
                        .onRegister(CreateRegistrate.blockModel(() -> GirderStrutModelBuilder::new))
                        .tag(AllTags.AllBlockTags.SAFE_NBT.tag)
                        .loot((lt, block) -> lt.add(block, LootTable.lootTable()
                                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                                        .add(LootItem.lootTableItem(block))
                                        .when(ExplosionCondition.survivesExplosion()))))
                        .item(GirderStrutBlockItem::new)
                        .tag(CMGTags.STRUT_ITEM)
                        .model((c, p) ->
                                p.withExistingParent(c.getName(), CreateMoreGirder.asResource("block/create_metal_girder_strut/item"))
                        )
                        .build()
                        .register();

        // Strut
        public static final BlockEntry<GirderStrutBlock> WEATHERED_IRON_GIRDER_STRUT =
                REGISTRATE.block("weathered_iron_girder_strut", GirderStrutBlock.weathered_iron())
                        .initialProperties(SharedProperties::softMetal)
                        .properties(p -> p.destroyTime(0.3f).noOcclusion())
                        .transform(pickaxeOnly())
                        .blockstate((c, p) -> p.directionalBlock(c.get(),
                                (state) -> p.models().getExistingFile(CreateMoreGirder.asResource(
                                        "block/weathered_iron_girder_strut/attachment")
                                )))
                        .onRegister(CreateRegistrate.blockModel(() -> GirderStrutModelBuilder::new))
                        .tag(AllTags.AllBlockTags.SAFE_NBT.tag)
                        .loot((lt, block) -> lt.add(block, LootTable.lootTable()
                                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                                        .add(LootItem.lootTableItem(block))
                                        .when(ExplosionCondition.survivesExplosion()))))
                        .item(GirderStrutBlockItem::new)
                        .tag(CMGTags.STRUT_ITEM)
                        .model((c, p) ->
                                p.withExistingParent(c.getName(), CreateMoreGirder.asResource("block/weathered_iron_girder_strut/item"))
                        )
                        .build()
                        .register();

        // Copycat Girder Strut
        public static final BlockEntry<CopycatGirderStrutBlock> COPYCAT_GIRDER_STRUT =
                REGISTRATE.block("copycat_girder_strut", CopycatGirderStrutBlock::new)
                        .initialProperties(SharedProperties::softMetal)
                        .properties(p -> p.destroyTime(0.3f).noOcclusion())
                        .transform(pickaxeOnly())
                        .blockstate((c, p) -> p.directionalBlock(c.get(),
                                (state) -> p.models().getExistingFile(CreateMoreGirder.asResource(
                                        "block/copycat_girder_strut/attachment")
                                )))
                        .onRegister(CreateRegistrate.blockModel(() -> CopycatGirderStrutBakedModel::new))
                        .tag(AllTags.AllBlockTags.SAFE_NBT.tag)
                        .loot((lt, block) -> lt.add(block, LootTable.lootTable()
                                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                                        .add(LootItem.lootTableItem(block))
                                        .when(ExplosionCondition.survivesExplosion()))))
                        .item(CopycatGirderStrutBlockItem::new)
                        .tag(CMGTags.STRUT_ITEM)
                        .model((c, p) ->
                                p.withExistingParent(c.getName(), CreateMoreGirder.asResource("block/copycat_girder_strut/item"))
                        )
                        .build()
                        .register();

        // Copycat Girder
        public static final BlockEntry<CopycatGirderBlock> COPYCAT_GIRDER =
                REGISTRATE.block("copycat_girder", CopycatGirderBlock::new)
                        .initialProperties(SharedProperties::softMetal)
                        .properties(p -> p.mapColor(MapColor.STONE).sound(SoundType.NETHERITE_BLOCK).noOcclusion())
                        .transform(pickaxeOnly())
                        .tag(CMGTags.GIRDER_BLOCK, AllTags.AllBlockTags.SAFE_NBT.tag)
                        .blockstate(GenericGirderGenerator::blockState)
                        .onRegister(CreateRegistrate.blockModel(() -> CopycatGirderBakedModel::new))
                        .item().model(GenericGirderGenerator::itemModel).build()
                        .register();

        public static final BlockEntry<CopycatGirderEncasedShaftBlock> COPYCAT_GIRDER_ENCASED_SHAFT =
                REGISTRATE.block("copycat_girder_encased_shaft", CopycatGirderEncasedShaftBlock::new)
                        .initialProperties(SharedProperties::softMetal)
                        .properties(p -> p.mapColor(MapColor.STONE).sound(SoundType.NETHERITE_BLOCK))
                        .transform(pickaxeOnly())
                        .tag(AllTags.AllBlockTags.SAFE_NBT.tag)
                        .blockstate(GenericGirderGenerator::blockStateWithShaft)
                        .onRegister(CreateRegistrate.blockModel(() -> CopycatGirderBakedModel::new))
                        .loot((lt, block) -> lt.add(block, LootTable.lootTable()
                                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                                        .add(LootItem.lootTableItem(COPYCAT_GIRDER.get()))
                                        .when(ExplosionCondition.survivesExplosion()))
                                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                                        .add(LootItem.lootTableItem(AllBlocks.SHAFT.get()))
                                        .when(ExplosionCondition.survivesExplosion()))))
                        .register();

        // Copycat Metal Girder Strut
        public static final BlockEntry<CopycatMetalGirderStrutBlock> COPYCAT_METAL_GIRDER_STRUT =
                REGISTRATE.block("copycat_metal_girder_strut", CopycatMetalGirderStrutBlock::new)
                        .initialProperties(SharedProperties::softMetal)
                        .properties(p -> p.destroyTime(0.3f).noOcclusion())
                        .transform(pickaxeOnly())
                        .blockstate((c, p) -> p.directionalBlock(c.get(),
                                (state) -> p.models().getExistingFile(CreateMoreGirder.asResource(
                                        "block/copycat_metal_girder_strut/attachment")
                                )))
                        .onRegister(CreateRegistrate.blockModel(() -> CopycatGirderStrutBakedModel::new))
                        .tag(AllTags.AllBlockTags.SAFE_NBT.tag)
                        .loot((lt, block) -> lt.add(block, LootTable.lootTable()
                                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                                        .add(LootItem.lootTableItem(block))
                                        .when(ExplosionCondition.survivesExplosion()))))
                        .item(CopycatGirderStrutBlockItem::new)
                        .tag(CMGTags.STRUT_ITEM)
                        .model((c, p) ->
                                p.withExistingParent(c.getName(), CreateMoreGirder.asResource("block/copycat_metal_girder_strut/item"))
                        )
                        .build()
                        .register();

        // Copycat Metal Girder (OG metal_girder silhouette, mimics adjacent block textures)
        public static final BlockEntry<CopycatMetalGirderBlock> COPYCAT_METAL_GIRDER =
                REGISTRATE.block("copycat_metal_girder", CopycatMetalGirderBlock::new)
                        .initialProperties(SharedProperties::softMetal)
                        .properties(p -> p.mapColor(MapColor.STONE).sound(SoundType.NETHERITE_BLOCK).noOcclusion())
                        .transform(pickaxeOnly())
                        .tag(CMGTags.GIRDER_BLOCK, AllTags.AllBlockTags.SAFE_NBT.tag)
                        .blockstate(GenericGirderGenerator::blockState)
                        .onRegister(CreateRegistrate.blockModel(() -> CopycatMetalGirderBakedModel::new))
                        .item().model(GenericGirderGenerator::itemModel).build()
                        .register();

        public static final BlockEntry<CopycatMetalGirderEncasedShaftBlock> COPYCAT_METAL_GIRDER_ENCASED_SHAFT =
                REGISTRATE.block("copycat_metal_girder_encased_shaft", CopycatMetalGirderEncasedShaftBlock::new)
                        .initialProperties(SharedProperties::softMetal)
                        .properties(p -> p.mapColor(MapColor.STONE).sound(SoundType.NETHERITE_BLOCK))
                        .transform(pickaxeOnly())
                        .tag(AllTags.AllBlockTags.SAFE_NBT.tag)
                        .blockstate(GenericGirderGenerator::blockStateWithShaft)
                        .onRegister(CreateRegistrate.blockModel(() -> CopycatGirderBakedModel::new))
                        .loot((lt, block) -> lt.add(block, LootTable.lootTable()
                                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                                        .add(LootItem.lootTableItem(COPYCAT_METAL_GIRDER.get()))
                                        .when(ExplosionCondition.survivesExplosion()))
                                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                                        .add(LootItem.lootTableItem(AllBlocks.SHAFT.get()))
                                        .when(ExplosionCondition.survivesExplosion()))))
                        .register();

    private static volatile java.util.Set<Block> GIRDER_BLOCKS;
    private static volatile java.util.Set<Block> GIRDER_ENCASED_SHAFT_BLOCKS;

    private static java.util.Set<Block> girderBlocks() {
        java.util.Set<Block> s = GIRDER_BLOCKS;
        if (s == null) {
            s = java.util.Set.of(
                    ANDESITE_GIRDER.get(),
                    ANDESITE_METAL_GIRDER.get(),
                    BRASS_GIRDER.get(),
                    BRASS_METAL_GIRDER.get(),
                    COPPER_GIRDER.get(),
                    EXPOSED_COPPER_GIRDER.get(),
                    WEATHERED_COPPER_GIRDER.get(),
                    OXIDIZED_COPPER_GIRDER.get(),
                    WAXED_COPPER_GIRDER.get(),
                    WAXED_EXPOSED_COPPER_GIRDER.get(),
                    WAXED_WEATHERED_COPPER_GIRDER.get(),
                    WAXED_OXIDIZED_COPPER_GIRDER.get(),
                    COPPER_METAL_GIRDER.get(),
                    EXPOSED_COPPER_METAL_GIRDER.get(),
                    WEATHERED_COPPER_METAL_GIRDER.get(),
                    OXIDIZED_COPPER_METAL_GIRDER.get(),
                    WAXED_COPPER_METAL_GIRDER.get(),
                    WAXED_EXPOSED_COPPER_METAL_GIRDER.get(),
                    WAXED_WEATHERED_COPPER_METAL_GIRDER.get(),
                    WAXED_OXIDIZED_COPPER_METAL_GIRDER.get(),
                    INDUSTRIAL_IRON_GIRDER.get(),
                    WEATHERED_IRON_GIRDER.get(),
                    WEATHERED_IRON_METAL_GIRDER.get(),
                    COPYCAT_GIRDER.get(),
                    COPYCAT_METAL_GIRDER.get()
            );
            GIRDER_BLOCKS = s;
        }
        return s;
    }

    private static java.util.Set<Block> girderEncasedShaftBlocks() {
        java.util.Set<Block> s = GIRDER_ENCASED_SHAFT_BLOCKS;
        if (s == null) {
            s = java.util.Set.of(
                    ANDESITE_GIRDER_ENCASED_SHAFT.get(),
                    ANDESITE_METAL_GIRDER_ENCASED_SHAFT.get(),
                    BRASS_GIRDER_ENCASED_SHAFT.get(),
                    BRASS_METAL_GIRDER_ENCASED_SHAFT.get(),
                    COPPER_GIRDER_ENCASED_SHAFT.get(),
                    EXPOSED_COPPER_GIRDER_ENCASED_SHAFT.get(),
                    WEATHERED_COPPER_GIRDER_ENCASED_SHAFT.get(),
                    OXIDIZED_COPPER_GIRDER_ENCASED_SHAFT.get(),
                    WAXED_COPPER_GIRDER_ENCASED_SHAFT.get(),
                    WAXED_EXPOSED_COPPER_GIRDER_ENCASED_SHAFT.get(),
                    WAXED_WEATHERED_COPPER_GIRDER_ENCASED_SHAFT.get(),
                    WAXED_OXIDIZED_COPPER_GIRDER_ENCASED_SHAFT.get(),
                    COPPER_METAL_GIRDER_ENCASED_SHAFT.get(),
                    EXPOSED_COPPER_METAL_GIRDER_ENCASED_SHAFT.get(),
                    WEATHERED_COPPER_METAL_GIRDER_ENCASED_SHAFT.get(),
                    OXIDIZED_COPPER_METAL_GIRDER_ENCASED_SHAFT.get(),
                    WAXED_COPPER_METAL_GIRDER_ENCASED_SHAFT.get(),
                    WAXED_EXPOSED_COPPER_METAL_GIRDER_ENCASED_SHAFT.get(),
                    WAXED_WEATHERED_COPPER_METAL_GIRDER_ENCASED_SHAFT.get(),
                    WAXED_OXIDIZED_COPPER_METAL_GIRDER_ENCASED_SHAFT.get(),
                    INDUSTRIAL_IRON_GIRDER_ENCASED_SHAFT.get(),
                    WEATHERED_IRON_GIRDER_ENCASED_SHAFT.get(),
                    WEATHERED_IRON_METAL_GIRDER_ENCASED_SHAFT.get(),
                    COPYCAT_GIRDER_ENCASED_SHAFT.get(),
                    COPYCAT_METAL_GIRDER_ENCASED_SHAFT.get()
            );
            GIRDER_ENCASED_SHAFT_BLOCKS = s;
        }
        return s;
    }

    public static boolean isAnyCMGGirder(BlockState state) {
        return girderBlocks().contains(state.getBlock());
    }

    public static boolean isAnyCMGGirderEncasedShaft(BlockState state) {
        return girderEncasedShaftBlocks().contains(state.getBlock());
    }

    public static void register() {

    }

}
