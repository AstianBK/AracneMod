package com.astianbk.arachnemod.common;

import com.astianbk.arachnemod.AracneMod;
import com.astianbk.arachnemod.QuestsType;
import com.astianbk.arachnemod.common.block.ArachneIdolBlock;
import com.astianbk.arachnemod.common.compendium.CompendiumManager;
import com.astianbk.arachnemod.common.quests.QuestManager;
import com.astianbk.arachnemod.common.registry.NRegistry;
import com.astianbk.arachnemod.server.cap.ArachneAttachment;
import com.astianbk.arachnemod.server.cap.data.BlessingData;
import com.astianbk.arachnemod.server.cap.data.CompendiumData;
import com.astianbk.arachnemod.server.entity.EnterDimensionEntity;
import com.astianbk.arachnemod.server.entity.OrbEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ArachneIdolBlockEntity extends BlockEntity {
    public List<OrbEntity> orbs = new ArrayList<>();
    public State currentState = State.NONE;
    public ArachneIdolBlockEntity(BlockPos worldPosition, BlockState blockState) {
        super(NRegistry.ARACHNE_IDOL_BLOCK_ENTITY.get(), worldPosition, blockState);
    }

    public void addOrb(OrbEntity orbEntity){
        orbs.add(orbEntity);
    }

    public void selectOrb(OrbEntity orbEntity, Player player, OrbEntity.Type type, Level level, BlockPos pos){
        switch (type){
            case CANCEL -> {
                orbs.forEach(Entity::discard);
                orbs.clear();
                level.playSound(null,pos,NRegistry.ORB_SELECT.get(), SoundSource.NEUTRAL,2.0F,1.0F);

                currentState = State.NONE;
                level.setBlock(pos,level.getBlockState(pos).setValue(ArachneIdolBlock.LIT,false),3);
            }
            case QUEST -> {
                orbs.forEach(Entity::discard);
                orbs.clear();
                level.playSound(null,pos,NRegistry.ORB_SELECT.get(), SoundSource.NEUTRAL,2.0F,1.0F);

                currentState = State.SELECT_QUEST;
                startQuestMenu(level,pos);
            }
            case QUEST_GET -> {
                orbs.forEach(Entity::discard);
                orbs.clear();
                level.playSound(null,pos,NRegistry.ORB_SELECT.get(), SoundSource.NEUTRAL,2.0F,1.0F);

                currentState = State.NONE;
                ArachneAttachment.get(player).ifPresent(arachneAttachment -> {
                    arachneAttachment.acceptQuest((ServerPlayer) player,getRandomQuest(QuestManager.getQuestForType(QuestsType.COLLECT)));
                    arachneAttachment.refreshQuest(player);
                });
                Direction direction = level.getBlockState(pos).getValue(ArachneIdolBlock.DIRECTION);
                Vec3 forward = direction.getUnitVec3();

                Vec3 center = Vec3.atCenterOf(pos).add(forward.scale(2.0));
                EnterDimensionEntity enterDimension = new EnterDimensionEntity(NRegistry.ENTER_DIMENSION.get(), level);
                enterDimension.setPos(center.x,getEmptyY(BlockPos.containing(center),level),center.z);
                level.addFreshEntity(enterDimension);
                if (!level.isClientSide()){
                    level.broadcastEntityEvent(enterDimension,(byte) 8);
                }
            }
            case QUEST_KILL -> {
                orbs.forEach(Entity::discard);
                orbs.clear();
                level.playSound(null,pos,NRegistry.ORB_SELECT.get(), SoundSource.NEUTRAL,2.0F,1.0F);

                currentState = State.NONE;
                ArachneAttachment.get(player).ifPresent(arachneAttachment -> {
                    arachneAttachment.acceptQuest((ServerPlayer) player,getRandomQuest(QuestManager.getQuestForType(QuestsType.HUNT)));
                });
                Direction direction = level.getBlockState(pos).getValue(ArachneIdolBlock.DIRECTION);
                Vec3 forward = direction.getUnitVec3();

                Vec3 center = Vec3.atCenterOf(pos).add(forward.scale(2.0));
                EnterDimensionEntity enterDimension = new EnterDimensionEntity(NRegistry.ENTER_DIMENSION.get(), level);
                enterDimension.setPos(center.x,getEmptyY(BlockPos.containing(center),level),center.z);
                level.addFreshEntity(enterDimension);
                if (!level.isClientSide()){
                    level.broadcastEntityEvent(enterDimension,(byte) 8);
                }
            }
            case BLESSING -> {
                orbs.forEach(Entity::discard);
                orbs.clear();
                level.playSound(null,pos,NRegistry.ORB_SELECT.get(), SoundSource.NEUTRAL,2.0F,1.0F);

                currentState = State.MENU_BLESSING;

                this.startBlessing(player,level,pos,ArachneIdolBlock.typesForState.get(currentState));
                level.setBlock(pos,level.getBlockState(pos).setValue(ArachneIdolBlock.LIT,false),3);
            }
            case QUEST_REPUTATION -> {
                orbs.forEach(Entity::discard);
                orbs.clear();
                level.playSound(null,pos,NRegistry.ORB_SELECT.get(), SoundSource.NEUTRAL,2.0F,1.0F);

                currentState = State.NONE;
                level.setBlock(pos,level.getBlockState(pos).setValue(ArachneIdolBlock.LIT,false),3);
                ArachneAttachment.get(player).ifPresent(arachnePlayer->{
                    if (arachnePlayer.currentReputation==100){
                        arachnePlayer.playDialog(Identifier.parse(CompendiumManager.getCompendiumForId(Identifier.fromNamespaceAndPath(AracneMod.MODID,"reputation_full")).getDialog()));
                    }else if (arachnePlayer.currentReputation>75){
                        arachnePlayer.playDialog(Identifier.parse(CompendiumManager.getCompendiumForId(Identifier.fromNamespaceAndPath(AracneMod.MODID,"reputation_high")).getDialog()));
                    }else if (arachnePlayer.currentReputation>35){
                        arachnePlayer.playDialog(Identifier.parse(CompendiumManager.getCompendiumForId(Identifier.fromNamespaceAndPath(AracneMod.MODID,"reputation_medium")).getDialog()));
                    }else if (arachnePlayer.currentReputation > 5){
                        arachnePlayer.playDialog(Identifier.parse(CompendiumManager.getCompendiumForId(Identifier.fromNamespaceAndPath(AracneMod.MODID,"reputation_low")).getDialog()));
                    }else if (arachnePlayer.currentReputation>=0){
                        arachnePlayer.playDialog(Identifier.parse(CompendiumManager.getCompendiumForId(Identifier.fromNamespaceAndPath(AracneMod.MODID,"reputation_none")).getDialog()));
                    }
                    player.syncData(NRegistry.ARACNE);
                });
            }
            default -> {
                ArachneAttachment.get(player).ifPresent(arachneAttachment -> {
                    for (BlessingData data : arachneAttachment.blessingData){
                        if (data.type.name().equals(type.name()) && arachneAttachment.currentReputation >= data.reputation){
                            data.unlock = !data.unlock;
                            orbEntity.setLock(!data.unlock);
                            break;
                        }
                    }
                    player.syncData(NRegistry.ARACNE);
                });
            }
        }
    }
    public int getEmptyY(BlockPos start,Level level){
        if (level.isEmptyBlock(start)){
            return start.getY();
        }
        BlockPos.MutableBlockPos pos = start.mutable();
        for (int i = start.getY(); i >level.getMinY() ; i--){
            if (level.isEmptyBlock(pos.above())){
                return start.getY();
            }
        }
        return start.getY();
    }
    public static <T> T getRandomQuest(List<T> list) {
        if (list == null || list.isEmpty()) {
            throw new IllegalArgumentException("La lista puede estar vacía o ser null.");
        }
        Random random = new Random();
        return list.get(random.nextInt(list.size()));
    }
    public void startBlessing(Player player, Level level, BlockPos pos, OrbEntity.Type[] types) {
        if (!level.isClientSide()) {
            Direction direction = level.getBlockState(pos).getValue(ArachneIdolBlock.DIRECTION);

            Vec3 forward = direction.getUnitVec3();
            Vec3 right = forward.yRot(90.0F * Mth.DEG_TO_RAD);

            Vec3 center = Vec3.atCenterOf(pos).add(forward.scale(0.2));

            double maxOffset = (types.length - 1) / 2.0;
            int i = 0;
            ArachneAttachment arachneAttachment = ArachneAttachment.get(player).orElseGet(null);
            List<BlessingData> blessings = arachneAttachment.blessingData;
            for (BlessingData data : blessings){
                if (data.type== BlessingData.BlessingType.ARACHNE_FORM){
                    i++;
                    break;
                }
                OrbEntity orbEntity = new OrbEntity(NRegistry.ORB.get(), level);

                orbEntity.setType(OrbEntity.Type.valueOf(data.type.name()));

                double centered = i - maxOffset;
                double xOffset = centered * 1.2;
                double yOffset = maxOffset > 0 ? 0.5 * (1.0 - Math.pow(centered / maxOffset, 2.0)) : 0.0;

                Vec3 orbPos = center.add(right.scale(xOffset)).add(0, yOffset, 0);

                orbEntity.setPos(orbPos);

                orbEntity.sourceBlock = pos;
                orbEntity.setLock(arachneAttachment.currentReputation<data.reputation || !data.unlock);
                level.addFreshEntity(orbEntity);
                addOrb(orbEntity);
                i++;
            }
            OrbEntity orbEntity = new OrbEntity(NRegistry.ORB.get(), level);

            orbEntity.setType(OrbEntity.Type.CANCEL);

            double centered = i - maxOffset;
            double xOffset = centered * 1.2;
            double yOffset = maxOffset > 0 ? 0.5 * (1.0 - Math.pow(centered / maxOffset, 2.0)) : 0.0;

            Vec3 orbPos = center.add(right.scale(xOffset)).add(0, yOffset, 0);

            orbEntity.setPos(orbPos);

            orbEntity.sourceBlock = pos;
            level.addFreshEntity(orbEntity);
            addOrb(orbEntity);


        }

        currentState = State.MENU_BLESSING;
    }
    public void startMenu(Player player, Level level, BlockPos pos, OrbEntity.Type[] types) {
        if (!level.isClientSide()) {
            Direction direction = level.getBlockState(pos).getValue(ArachneIdolBlock.DIRECTION);

            Vec3 forward = direction.getUnitVec3();
            Vec3 right = forward.yRot(90.0F * Mth.DEG_TO_RAD);

            Vec3 center = Vec3.atCenterOf(pos).add(forward.scale(0.2));

            double maxOffset = (types.length - 1) / 2.0;

            for (int i = 0; i < types.length; i++) {
                OrbEntity orbEntity = new OrbEntity(NRegistry.ORB.get(), level);

                orbEntity.setType(types[i]);

                double centered = i - maxOffset;
                double xOffset = centered * 2.0;
                double yOffset = maxOffset > 0 ? 0.5 * (1.0 - Math.pow(centered / maxOffset, 2.0)) : 0.0;

                Vec3 orbPos = center.add(right.scale(xOffset)).add(0, yOffset, 0);

                orbEntity.setPos(orbPos);

                orbEntity.sourceBlock = pos;

                level.addFreshEntity(orbEntity);
                addOrb(orbEntity);
            }
        }else {
            ArachneAttachment.get(player).ifPresent(arachneAttachment -> {

            });
        }

        currentState = State.MENU;
    }

    public void startQuestMenu(Level level,BlockPos pos){
        OrbEntity.Type[] types = ArachneIdolBlock.typesForState.get(State.SELECT_QUEST);
        if (!level.isClientSide()) {

            Direction direction = level.getBlockState(pos).getValue(ArachneIdolBlock.DIRECTION);

            Vec3 forward = direction.getUnitVec3();
            Vec3 right = forward.yRot(90.0F * Mth.DEG_TO_RAD);

            Vec3 center = Vec3.atCenterOf(pos).add(forward.scale(0.2));

            double maxOffset = (types.length - 1) / 2.0;

            for (int i = 0; i < types.length; i++) {

                OrbEntity orbEntity = new OrbEntity(NRegistry.ORB.get(), level);

                orbEntity.setType(types[i]);

                double centered = i - maxOffset;
                double xOffset = centered * 2.0;
                double yOffset = maxOffset > 0 ? 0.5 * (1.0 - Math.pow(centered / maxOffset, 2.0)) : 0.0;

                Vec3 orbPos = center.add(right.scale(xOffset)).add(0, yOffset, 0);

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
        SELECT_QUEST,
        MENU_BLESSING
    }
}
