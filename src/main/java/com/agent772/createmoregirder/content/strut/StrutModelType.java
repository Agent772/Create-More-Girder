package com.agent772.createmoregirder.content.strut;

import com.agent772.createmoregirder.CreateMoreGirder;
import com.agent772.createmoregirder.CMGPartialModels;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.minecraft.resources.ResourceLocation;

public enum StrutModelType {
    ANDESITE(CMGPartialModels.ANDESITE_GIRDER_STRUT, CreateMoreGirder.asResource("block/weathered_industrial_iron_block"));

    private final PartialModel segmentPartial;
    private final ResourceLocation capTexture;

    StrutModelType(final PartialModel segmentPartial, final ResourceLocation capTexture) {
        this.segmentPartial = segmentPartial;
        this.capTexture = capTexture;
    }

    public PartialModel getPartialModel() {
        return segmentPartial;
    }

    public ResourceLocation getCapTexture() {
        return capTexture;
    }

}
