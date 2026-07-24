package com.astianbk.aracnemod;

import com.astianbk.aracnemod.common.quests.QuestManager;
import com.astianbk.aracnemod.common.registry.NRegistry;
import com.astianbk.aracnemod.server.ScarabEntity;
import com.astianbk.aracnemod.server.cap.NerubianCap;
import com.astianbk.aracnemod.server.network.PacketNerubianData;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.AddServerReloadListenersEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingEquipmentChangeEvent;
import net.neoforged.neoforge.event.entity.player.ItemEntityPickupEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = AracneMod.MODID)
public class Events {
    @SubscribeEvent
    public static void tickEvent(PlayerTickEvent.Post event){
        if(event.getEntity() instanceof Player player){
            NerubianCap.get(player).ifPresent(cap->{
                cap.tick(player);
            });
        }
    }

    @SubscribeEvent
    public static void onDie(LivingDeathEvent event){
        Entity entity = event.getSource().getEntity();
        if(entity instanceof Player player){
            NerubianCap.get(player).ifPresent(e->{
                if(e.currentQuest!=null && !e.currentQuest.isComplete(e)){
                    if(e.currentQuest.canAddProgress(event.getEntity().getEncodeId())){
                        e.progressQuest++;
                    }
                }
            });
        }
    }

    @SubscribeEvent
    public static void onDie(EntityJoinLevelEvent event){
        Entity entity = event.getEntity();
        if(entity instanceof Player player){
            NerubianCap.get(player).ifPresent(NerubianCap::init);
        }
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        if(event.getEntity().level().isClientSide())return;
        if (!event.isWasDeath()) return;

        Player oldPlayer = event.getOriginal();
        Player newPlayer = event.getEntity();


        NerubianCap.get(oldPlayer).ifPresent(oldCap->{
            NerubianCap cap = NerubianCap.get(newPlayer).orElse(null);
            if(cap!=null){
                cap.copyFrom(oldCap);
                cap.init();
            }


            //cap.syncNewPlayer((ServerPlayer) newPlayer,oldCap,true);
        });
    }

    @SubscribeEvent
    public static void onEquip(LivingEquipmentChangeEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

    }

    @SubscribeEvent
    public static void registerPackets(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(AracneMod.MODID).versioned("1.0");

        registrar.playToClient(
                PacketNerubianData.TYPE,
                PacketNerubianData.STREAM_CODEC,
                PacketNerubianData::handle
        );

    }
    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(NRegistry.SCARAB.get(), ScarabEntity.createAttributes().build());
    }
    @SubscribeEvent
    public static void onPick(ItemEntityPickupEvent.Pre event){
        NerubianCap.get(event.getPlayer()).ifPresent(e->{
            if(e.currentQuest!=null && !e.currentQuest.isComplete(e)){
                if(e.currentQuest.canAddProgress(event.getItemEntity().getItem().getItem().toString())){
                    e.refreshQuest(event.getPlayer());
                }
            }
        });
    }

    @SubscribeEvent
    public static void onUse(PlayerInteractEvent.RightClickItem event){
        NerubianCap.get(event.getEntity()).ifPresent(e->{
            e.timeQuest = 2000;
            e.currentQuest = QuestManager.getQuests().stream().findAny().orElse(null);
            AracneMod.LOGGER.info("{}",e.currentQuest.getType());
        });
    }

    @SubscribeEvent
    public static void addQuestsData(AddServerReloadListenersEvent event){
        AracneMod.LOGGER.info("AddQuestsData");
        event.addListener(Identifier.parse("manager"),new QuestManager());
    }

}
