package com.astianbk.arachnemod;

import com.mojang.serialization.Codec;

public enum QuestsType {
    HUNT,
    COLLECT,
    SACRIFICE;
    public static final Codec<QuestsType> CODEC = Codec.STRING.xmap(QuestsType::valueOf, QuestsType::name);
}
