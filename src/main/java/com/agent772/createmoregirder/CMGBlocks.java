package com.agent772.createmoregirder;

import com.agent772.createmoregirder.content.brass_girder.BrassGirderBlock;
import com.agent772.createmoregirder.content.brass_girder.BrassGirderEncasedShaftBlock;
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
    
    public static void register() {
    }

}
