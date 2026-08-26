package com.astianbk.arachnemod.common.compendium;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class CompendiumEntity extends Compendium{
    public static final MapCodec<CompendiumEntity> CODEC =
            RecordCodecBuilder.mapCodec(instance ->
                    instance.group(
                            Codec.STRING.fieldOf("action").forGetter(CompendiumEntity::getIdAction),
                            Codec.STRING.fieldOf("idEntity").forGetter(CompendiumEntity::getIdEntity),
                            Codec.STRING.fieldOf("idDialog").forGetter(Compendium::getDialog)
                    ).apply(instance,(action,id,dialog)->new CompendiumEntity(Action.valueOf(action),id,dialog)));
    public Action action;
    public String idEntity;
    public CompendiumEntity(Action action,String idEntity,String dialog) {
        super(CompendiumType.ENTITY, dialog);
        this.idEntity = idEntity;
        this.action = action;
    }

    public String getIdEntity() {
        return idEntity;
    }

    public Action getAction() {
        return action;
    }
    public String getIdAction() {
        return action.name();
    }

    @Override
    public String toString() {
        return super.toString()+" identity :"+this.idEntity + " action :"+action;
    }
}
