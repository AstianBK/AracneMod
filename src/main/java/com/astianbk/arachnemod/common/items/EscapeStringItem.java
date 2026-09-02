package com.astianbk.arachnemod.common.items;

import com.astianbk.arachnemod.common.registry.NRegistry;
import com.astianbk.arachnemod.server.entity.SealingCrystalEntity;
import com.astianbk.arachnemod.server.entity.WebElevatorEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class EscapeStringItem extends Item {
    public EscapeStringItem(Properties properties) {
        super(properties);
    }

    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        if (level.dimension() != NRegistry.THE_VOID && level.dimension() != NRegistry.THE_DEPTH){
            return InteractionResult.FAIL;
        }
        BlockPos pos = context.getClickedPos();
        BlockPos above = pos.above();
        if (!level.isEmptyBlock(above)) {
            return InteractionResult.FAIL;
        } else {
            double x = (double)above.getX();
            double y = (double)above.getY();
            double z = (double)above.getZ();
            List<Entity> entities = level.getEntities((Entity)null, new AABB(x, y, z, x + 1.0, y + 2.0, z + 1.0));
            if (!entities.isEmpty()) {
                return InteractionResult.FAIL;
            } else {
                if (level instanceof ServerLevel) {
                    WebElevatorEntity crystal = new WebElevatorEntity(NRegistry.WEB_ELEVATOR.get(),level);
                    crystal.setPos(x + 0.5, y, z + 0.5);
                    level.addFreshEntity(crystal);
                    level.gameEvent(context.getPlayer(), GameEvent.ENTITY_PLACE, above);
                    level.broadcastEntityEvent(crystal,(byte) 4);
                }

                context.getItemInHand().shrink(1);
                return InteractionResult.SUCCESS;
            }
        }
    }
}
