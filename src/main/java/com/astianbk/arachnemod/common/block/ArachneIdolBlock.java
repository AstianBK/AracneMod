package com.astianbk.arachnemod.common.block;

import com.astianbk.arachnemod.client.screen.IdolScreen;
import com.astianbk.arachnemod.common.registry.NRegistry;
import com.astianbk.arachnemod.server.entity.OrbEntity;
import com.astianbk.arachnemod.server.cap.NerubianCap;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;

import java.util.List;
import java.util.Random;

public class ArachneIdolBlock extends Block {

    public static BooleanProperty LIT = BlockStateProperties.LIT;
    public OrbEntity.Type[] types ={
            OrbEntity.Type.QUEST_KILL, OrbEntity.Type.QUEST, OrbEntity.Type.QUEST_GET
    };

    public ArachneIdolBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.getStateDefinition().any().setValue(LIT,false));
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        NerubianCap.get(player).ifPresent(e->{

            if(e.currentQuest!=null && !e.currentQuest.isComplete(e)){

                if (level.isClientSide()){
                    e.speechTime = 160;
                    e.speechTimeO = 160;
                    Minecraft.getInstance().setScreen(new IdolScreen(e.currentQuest));
                }else {

                }
                level.setBlock(pos,state.setValue(LIT,true),3);
                return;
            }
            if(!level.isClientSide()){
                for (int i = 0; i < 3 ; i++){
                    OrbEntity orbEntity = new OrbEntity(NRegistry.ORB.get(),level);
                    orbEntity.setType(types[i]);
                    orbEntity.setPos(pos.getX()-1 + i,pos.getY(), pos.getZ()+1 );
                    level.addFreshEntity(orbEntity);
                }
//                if(e.currentQuest!=null && e.currentQuest.isComplete(e)){
//                    e.currentReputation = Math.min(e.currentQuest.getReputation()+e.currentReputation,100);
//                    if(e.currentReputation == 100 && !e.itemTransformDrop){
//                        e.itemTransformDrop = true;
//                    }
//                    int xp = e.currentQuest.getXp();
//                    if(e.currentReputation == 100){
//                        xp*=2;
//                    }
//                    ExperienceOrb.award((ServerLevel) level,pos.getCenter(),xp);
//                    level.playSound(null,player, SoundEvents.ENDER_DRAGON_GROWL, SoundSource.BLOCKS,2.0F,-1.0F);
//                    if(e.currentQuest.getType() == QuestsType.COLLECT){
//                        int shrink = e.currentQuest.getMaxProgress();
//                        Item shrinkItem = BuiltInRegistries.ITEM.get(Identifier.parse(e.currentQuest.getTargetId())).get().value();
//                        Inventory inventory = player.getInventory();
//
//                        for (ItemStack item : inventory.getNonEquipmentItems()){
//                            if(item.is(shrinkItem)){
//                                int count = item.getCount();
//                                item.shrink(Math.min(shrink,count));
//                                shrink-=count;
//                            }
//                            if(shrink<=0){
//                                return;
//                            }
//                        }
//                    }
//                    level.setBlock(pos,state.setValue(LIT,false),3);
//                    e.progressQuest = 0;
//                    e.currentQuest = null;
//                }else {
//                    e.timeQuest = 22000;
//                    e.progressQuest = 0;
//                    e.currentQuest = getRandomQuest(QuestManager.getQuests());
//                    e.refreshQuest(player);
//                }



            }
            if(level.isClientSide()){
                player.sendSystemMessage(Component.literal("Reputation :"+e.currentReputation));
            }

        });
        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    public static <T> T getRandomQuest(List<T> list) {
        if (list == null || list.isEmpty()) {
            throw new IllegalArgumentException("La lista no puede estar vacía o ser null.");
        }
        Random random = new Random();
        return list.get(random.nextInt(list.size()));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LIT);
        super.createBlockStateDefinition(builder);
    }
}
