package com.astianbk.aracnemod.server;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.level.Level;

public class VoidNeedleEntity extends Phantom {
    public VoidNeedleEntity(EntityType<? extends Phantom> type, Level level) {
        super(type, level);
    }
}
