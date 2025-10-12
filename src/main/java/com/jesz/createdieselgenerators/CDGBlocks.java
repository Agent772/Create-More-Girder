package com.jesz.createdieselgenerators;

import com.jesz.createdieselgenerators.content.andesite_girder.AndesiteGirderBlock;
import com.jesz.createdieselgenerators.content.andesite_girder.AndesiteGirderEncasedShaftBlock;
import com.jesz.createdieselgenerators.content.andesite_girder.AndesiteGirderGenerator;

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

public class CDGBlocks {

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
                    .loot((p, b) -> p.add(b, p.createSingleItemTable(ANDESITE_GIRDER.get())
                            .withPool(p.applyExplosionCondition(AllBlocks.SHAFT.get(), LootPool.lootPool()
                                    .setRolls(ConstantValue.exactly(1.0F))
                                    .add(LootItem.lootTableItem(AllBlocks.SHAFT.get()))))))
                    .register();
    
    public static void register() {
    }

}
