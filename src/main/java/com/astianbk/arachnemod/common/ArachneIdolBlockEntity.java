package com.astianbk.arachnemod.common;

import com.astianbk.arachnemod.common.block.ArachneIdolBlock;
import com.astianbk.arachnemod.common.registry.NRegistry;
import com.astianbk.arachnemod.server.cap.ArachneAttachment;
import com.astianbk.arachnemod.server.entity.EnterDimensionEntity;
import com.astianbk.arachnemod.server.entity.OrbEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ArachneIdolBlockEntity extends BlockEntity {
    public List<OrbEntity> orbs = new ArrayList<>();
    public State currentState = State.NONE;
    public Map<State, Vec3i> offsetForState = Map.of(State.MENU,new Vec3i(0,0,2),State.SELECT_QUEST,new Vec3i(0,0,0));
    public ArachneIdolBlockEntity(BlockPos worldPosition, BlockState blockState) {
        super(NRegistry.ARACHNE_IDOL_BLOCK_ENTITY.get(), worldPosition, blockState);
    }

    public void addOrb(OrbEntity orbEntity){
        orbs.add(orbEntity);
    }

    public void selectOrb(Player player,OrbEntity.Type type,Level level,BlockPos pos){
        orbs.forEach(Entity::discard);
        orbs.clear();
        switch (type){
            case CANCEL -> {
                currentState = State.NONE;
            }
            case QUEST -> {
                currentState = State.SELECT_QUEST;
                startQuestMenu(level,pos);
            }
            case QUEST_GET -> {
                currentState = State.NONE;
                Direction direction = level.getBlockState(pos).getValue(ArachneIdolBlock.DIRECTION);
                Vec3 forward = direction.getUnitVec3();
                Vec3 right = forward.yRot(90.0F * Mth.DEG_TO_RAD);

                Vec3 center = Vec3.atCenterOf(pos).add(forward.scale(2.0));
                EnterDimensionEntity enterDimension = new EnterDimensionEntity(NRegistry.ENTER_DIMENSION.get(), level);
                enterDimension.setPos(center);
                level.addFreshEntity(enterDimension);
                player.sendSystemMessage(Component.literal("Obtuviste mision de recolectar de mentira jijjijiij wiwiwi...."));

            }
            case QUEST_KILL -> {
                currentState = State.NONE;
                Direction direction = level.getBlockState(pos).getValue(ArachneIdolBlock.DIRECTION);
                Vec3 forward = direction.getUnitVec3();
                Vec3 right = forward.yRot(90.0F * Mth.DEG_TO_RAD);

                Vec3 center = Vec3.atCenterOf(pos).add(forward.scale(2.0));
                EnterDimensionEntity enterDimension = new EnterDimensionEntity(NRegistry.ENTER_DIMENSION.get(), level);
                enterDimension.setPos(center);
                level.addFreshEntity(enterDimension);
                player.sendSystemMessage(Component.literal("Obtuviste mision de mentira jijji"));

            }
            case BLESSING -> {
                currentState = State.NONE;
                ArachneAttachment.get(player).ifPresent(arachnePlayer->{
                    player.sendSystemMessage(Component.literal("Reputation :"+arachnePlayer.currentReputation));

                });
            }
        }
    }

    public void startMenu(Player player, Level level, BlockPos pos, OrbEntity.Type[] types){
        if (!level.isClientSide()) {
            Direction direction = level.getBlockState(pos).getValue(ArachneIdolBlock.DIRECTION);
            Vec3 forward = direction.getUnitVec3();
            Vec3 right = forward.yRot(90.0F * Mth.DEG_TO_RAD);

            Vec3 center = Vec3.atCenterOf(pos).add(forward.scale(2.0));

            for (int i = 0; i < types.length; i++) {
                OrbEntity orbEntity = new OrbEntity(NRegistry.ORB.get(), level);
                orbEntity.setType(types[i]);
                double centered = i - (types.length - 1) / 2.0;
                Vec3 orbPos = center.add(right.scale(centered * 2.0));

                orbEntity.setPos(orbPos);

                orbEntity.sourceBlock = pos;

                level.addFreshEntity(orbEntity);
                addOrb(orbEntity);
            }
        }
        currentState = State.MENU;
    }
    public void startQuestMenu(Level level,BlockPos pos){
        OrbEntity.Type[] types = ArachneIdolBlock.typesForState.get(State.SELECT_QUEST);
        if (!level.isClientSide()) {
            Direction direction = level.getBlockState(pos).getValue(ArachneIdolBlock.DIRECTION);
            Vec3 forward = direction.getUnitVec3();
            Vec3 right = forward.yRot(90.0F * Mth.DEG_TO_RAD);

            Vec3 center = Vec3.atCenterOf(pos).add(forward.scale(2.0));

            for (int i = 0; i < types.length; i++) {
                OrbEntity orbEntity = new OrbEntity(NRegistry.ORB.get(), level);
                orbEntity.setType(types[i]);
                double centered = i - (types.length - 1) / 2.0;
                Vec3 orbPos = center.add(right.scale(centered * 2.0));

                orbEntity.setPos(orbPos);

                orbEntity.sourceBlock = pos;

                level.addFreshEntity(orbEntity);
                addOrb(orbEntity);
            }
        }
        currentState = State.MENU;
    }
    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.putString("state_menu",currentState.name());
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        this.currentState = State.valueOf(input.getStringOr("state_menu","NONE"));
    }
    public enum State{
        NONE,
        MENU,
        SELECT_QUEST
    }
}
