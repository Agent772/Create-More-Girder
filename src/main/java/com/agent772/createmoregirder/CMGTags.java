package com.agent772.createmoregirder;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class CMGTags {

    public static final TagKey<Block> GIRDER_BLOCK = BlockTags.create(CreateMoreGirder.asResource("girder"));
    public static final TagKey<Block> GIRDER_ENCASED_SHAFT_BLOCK = BlockTags.create(CreateMoreGirder.asResource("girder_encased_shaft"));
    public static final TagKey<Block> PAVING_GIRDER = BlockTags.create(CreateMoreGirder.asResource("paving_girder"));

    public static final TagKey<Item> GIRDER_ITEM = ItemTags.create(CreateMoreGirder.asResource("girder"));

    public static void init() {
    }
}
