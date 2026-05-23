package com.agent772.createmoregirder.content.bracket;

import com.agent772.createmoregirder.CreateMoreGirder;
import com.simibubi.create.Create;
import com.simibubi.create.content.decoration.bracket.BracketBlock;
import com.simibubi.create.foundation.data.DirectionalAxisBlockStateGen;
import com.tterrag.registrate.builders.ItemBuilder;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.util.nullness.NonNullFunction;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.generators.ModelFile;

public class CMGBracketGenerator extends DirectionalAxisBlockStateGen {

    private final String registrationKey;
    private final String textureKey;

    public CMGBracketGenerator(String registrationKey, String textureKey) {
        this.registrationKey = registrationKey;
        this.textureKey = textureKey;
    }

    private static ResourceLocation createAsset(String path) {
        return ResourceLocation.fromNamespaceAndPath(Create.ID, path);
    }

    @Override
    public <T extends Block> String getModelPrefix(DataGenContext<Block, T> ctx, RegistrateBlockstateProvider prov,
        BlockState state) {
        return "";
    }

    @Override
    public <T extends Block> ModelFile getModel(DataGenContext<Block, T> ctx, RegistrateBlockstateProvider prov,
        BlockState state) {
        String type = state.getValue(BracketBlock.TYPE)
            .getSerializedName();
        boolean vertical = state.getValue(BracketBlock.FACING)
            .getAxis()
            .isVertical();

        String parentPath = "block/bracket/" + type + "/" + (vertical ? "ground" : "wall");
        String generatedPath = parentPath + "_" + registrationKey;

        return prov.models()
            .withExistingParent(generatedPath, createAsset(parentPath))
            .texture("bracket", CreateMoreGirder.asResource("block/brackets/bracket_" + textureKey))
            .texture("plate", CreateMoreGirder.asResource("block/brackets/bracket_plate_" + textureKey));
    }

    public static <I extends BlockItem, P> NonNullFunction<ItemBuilder<I, P>, P> itemModel(String textureKey) {
        return b -> b.model((c, p) -> p.withExistingParent(c.getName(), createAsset("block/bracket/item"))
            .texture("bracket", CreateMoreGirder.asResource("block/brackets/bracket_" + textureKey))
            .texture("plate", CreateMoreGirder.asResource("block/brackets/bracket_plate_" + textureKey)))
            .build();
    }
}
