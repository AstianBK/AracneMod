package com.astianbk.arachnemod.common.items;

import com.astianbk.arachnemod.common.registry.NRegistry;
import com.astianbk.arachnemod.server.entity.SealingCrystalEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.dimension.end.EnderDragonFight;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class SealingCrystalItem extends Item {
    public SealingCrystalItem(Item.Properties properties) {
        super(properties);
    }

    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState blockState = level.getBlockState(pos);
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
                    SealingCrystalEntity crystal = new SealingCrystalEntity(NRegistry.SEALING_CRYSTAL.get(),level);
                    crystal.setPos(x + 0.5, y, z + 0.5);
                    crystal.setShowBottom(false);
                    level.addFreshEntity(crystal);
                    level.gameEvent(context.getPlayer(), GameEvent.ENTITY_PLACE, above);

                }

                context.getItemInHand().shrink(1);
                return InteractionResult.SUCCESS;
            }
        }
    }
}
