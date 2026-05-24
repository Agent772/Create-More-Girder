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
    public static final TagKey<Block> STRUT_BLOCK = BlockTags.create(CreateMoreGirder.asResource("strut"));
    public static final TagKey<Block> BRACKET_BLOCK = BlockTags.create(CreateMoreGirder.asResource("brackets"));
    public static final TagKey<Block> COPYCAT_BRACKET_BLOCK = BlockTags.create(CreateMoreGirder.asResource("copycat_brackets"));

    public static final TagKey<Item> GIRDER_ITEM = ItemTags.create(CreateMoreGirder.asResource("girder"));
    public static final TagKey<Item> STRUT_ITEM = ItemTags.create(CreateMoreGirder.asResource("strut"));
    public static final TagKey<Item> BRACKET_ITEM = ItemTags.create(CreateMoreGirder.asResource("brackets"));
    public static final TagKey<Item> COPYCAT_BRACKET_ITEM = ItemTags.create(CreateMoreGirder.asResource("copycat_brackets"));

    public static void init() {
    }
}
