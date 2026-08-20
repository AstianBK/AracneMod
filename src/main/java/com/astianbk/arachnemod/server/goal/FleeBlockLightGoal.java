package com.astianbk.arachnemod.server.goal;

import com.astianbk.arachnemod.AracneMod;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class FleeBlockLightGoal extends Goal {

    protected final PathfinderMob mob;

    private double wantedX;
    private double wantedY;
    private double wantedZ;

    private final double speedModifier;
    private final Level level;

    public FleeBlockLightGoal(PathfinderMob mob, double speedModifier) {
        this.mob = mob;
        this.speedModifier = speedModifier;
        this.level = mob.level();

        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {

        if (this.level.getBrightness(LightLayer.BLOCK, this.mob.blockPosition()) <= 0) {
            return false;
        }

        return this.setWantedPos();
    }

    protected boolean setWantedPos() {

        Vec3 pos = this.getDarkPos();

        if (pos == null) {
            return false;
        }

        this.wantedX = pos.x;
        this.wantedY = pos.y;
        this.wantedZ = pos.z;

        return true;
    }

    @Override
    public boolean canContinueToUse() {

        if (this.level.getBrightness(LightLayer.BLOCK, this.mob.blockPosition()) <= 0) {
            return false;
        }

        return !this.mob.getNavigation().isDone();
    }

    @Override
    public void start() {
        this.mob.getNavigation().moveTo(this.wantedX, this.wantedY, this.wantedZ, this.speedModifier);
    }

    @Nullable
    protected Vec3 getDarkPos() {

        RandomSource random = this.mob.getRandom();
        BlockPos origin = this.mob.blockPosition();

        for (int i = 0; i < 10; i++) {

            BlockPos randomPos = origin.offset(random.nextInt(20) - 10, random.nextInt(6) - 3, random.nextInt(20) - 10);

            int blockLight = this.level.getBrightness(LightLayer.BLOCK, randomPos);

            if (blockLight == 0) {
                return Vec3.atBottomCenterOf(randomPos);
            }
        }

        return null;
    }
}