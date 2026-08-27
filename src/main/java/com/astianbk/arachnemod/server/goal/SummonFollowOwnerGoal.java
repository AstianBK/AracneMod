package com.astianbk.arachnemod.server.goal;

import com.astianbk.arachnemod.server.entity.SummoneableSpiderEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class SummonFollowOwnerGoal extends Goal {
    private final SummoneableSpiderEntity summoneableSpider;
    private @Nullable LivingEntity owner;
    private final double speedModifier;
    private final PathNavigation navigation;
    private int timeToRecalcPath;
    private final float stopDistance;
    private final float startDistance;
    private float oldWaterCost;

    public SummonFollowOwnerGoal(SummoneableSpiderEntity tamable, double speedModifier, float startDistance, float stopDistance) {
        this.summoneableSpider = tamable;
        this.speedModifier = speedModifier;
        this.navigation = tamable.getNavigation();
        this.startDistance = startDistance;
        this.stopDistance = stopDistance;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        if (!(tamable.getNavigation() instanceof GroundPathNavigation) && !(tamable.getNavigation() instanceof FlyingPathNavigation)) {
            throw new IllegalArgumentException("Unsupported mob type for FollowOwnerGoal");
        }
    }

    public boolean canUse() {
        LivingEntity owner = this.summoneableSpider.getOwner();
        if (owner == null) {
            return false;
        } else if (this.summoneableSpider.distanceToSqr(owner) < (double)(this.startDistance * this.startDistance)) {
            return false;
        } else {
            this.owner = owner;
            return true;
        }
    }

    public boolean canContinueToUse() {
        if (this.navigation.isDone()) {
            return false;
        } else {
            return !(this.summoneableSpider.distanceToSqr(this.owner) <= (double)(this.stopDistance * this.stopDistance));
        }
    }

    public void start() {
        this.timeToRecalcPath = 0;
        this.oldWaterCost = this.summoneableSpider.getPathfindingMalus(PathType.WATER);
        this.summoneableSpider.setPathfindingMalus(PathType.WATER, 0.0F);
    }

    public void stop() {
        this.owner = null;
        this.navigation.stop();
        this.summoneableSpider.setPathfindingMalus(PathType.WATER, this.oldWaterCost);
    }

    public void tick() {
        boolean isOwnerFarAway = shouldTryTeleportToOwner();
        if (!isOwnerFarAway) {
            this.summoneableSpider.getLookControl().setLookAt(this.owner, 10.0F, (float)this.summoneableSpider.getMaxHeadXRot());
        }

        if (--this.timeToRecalcPath <= 0) {
            this.timeToRecalcPath = this.adjustedTickDelay(10);
            if (isOwnerFarAway) {
                tryToTeleportToOwner();
            } else {
                this.navigation.moveTo(this.owner, this.speedModifier);
            }
        }

    }
    public boolean shouldTryTeleportToOwner() {
        LivingEntity owner = summoneableSpider.getOwner();
        return owner != null && summoneableSpider.distanceToSqr(summoneableSpider.getOwner()) >= 144.0;
    }
    public void tryToTeleportToOwner() {
        LivingEntity owner = summoneableSpider.getOwner();
        if (owner != null) {
            teleportToAroundBlockPos(owner.blockPosition());
        }
    }
    private void teleportToAroundBlockPos(BlockPos targetPos) {
        for(int attempt = 0; attempt < 10; ++attempt) {
            int xd = summoneableSpider.getRandom().nextIntBetweenInclusive(-3, 3);
            int zd = summoneableSpider.getRandom().nextIntBetweenInclusive(-3, 3);
            if (Math.abs(xd) >= 2 || Math.abs(zd) >= 2) {
                int yd = summoneableSpider.getRandom().nextIntBetweenInclusive(-1, 1);
                if (maybeTeleportTo(targetPos.getX() + xd, targetPos.getY() + yd, targetPos.getZ() + zd)) {
                    return;
                }
            }
        }
    }

    private boolean maybeTeleportTo(int x, int y, int z) {
        if (!this.canTeleportTo(new BlockPos(x, y, z))) {
            return false;
        } else {
            summoneableSpider.snapTo((double)x + 0.5, (double)y, (double)z + 0.5, summoneableSpider.getYRot(), summoneableSpider.getXRot());
            this.navigation.stop();
            return true;
        }
    }

    private boolean canTeleportTo(BlockPos pos) {
        PathType pathType = WalkNodeEvaluator.getPathTypeStatic(summoneableSpider, pos);
        if (pathType != PathType.WALKABLE) {
            return false;
        } else {
            BlockState blockStateBelow = summoneableSpider.level().getBlockState(pos.below());
            if (blockStateBelow.getBlock() instanceof LeavesBlock) {
                return false;
            } else {
                BlockPos delta = pos.subtract(summoneableSpider.blockPosition());
                return summoneableSpider.level().noCollision(summoneableSpider, summoneableSpider.getBoundingBox().move(delta));
            }
        }
    }
}