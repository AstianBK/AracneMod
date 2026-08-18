package com.astianbk.arachnemod;

import com.astianbk.arachnemod.common.quests.QuestManager;
import com.astianbk.arachnemod.common.registry.NRegistry;
import com.astianbk.arachnemod.server.cap.ArachneAttachment;
import com.astianbk.arachnemod.server.entity.*;
import com.astianbk.arachnemod.server.network.PacketNerubianData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.AddServerReloadListenersEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingEquipmentChangeEvent;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import net.neoforged.neoforge.event.entity.player.ItemEntityPickupEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

import java.util.Random;

@EventBusSubscriber(modid = AracneMod.MODID)
public class Events {
    @SubscribeEvent
    public static void tickEvent(PlayerTickEvent.Post event){
        if(event.getEntity() instanceof Player player){
            ArachneAttachment.get(player).ifPresent(cap->{
                cap.tick(player);
            });
        }

    }

    @SubscribeEvent
    public static void onBlock(BlockEvent.EntityPlaceEvent event){

    }
    @SubscribeEvent
    public static void applyEffect(MobEffectEvent.Applicable event){
        if (!event.getEffectInstance().getEffect().value().isBeneficial())return;
        if (event.getEntity().hasEffect(NRegistry.SILENT)){
            event.setResult(MobEffectEvent.Applicable.Result.DO_NOT_APPLY);
        }
    }

    @SubscribeEvent
    public static void onDie(LivingDeathEvent event){
        Entity entity = event.getSource().getEntity();
        if(entity instanceof Player player){
            ArachneAttachment.get(player).ifPresent(e->{
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
            ArachneAttachment.get(player).ifPresent(ArachneAttachment::init);
        }
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        if(event.getEntity().level().isClientSide())return;
        if (!event.isWasDeath()) return;

        Player oldPlayer = event.getOriginal();
        Player newPlayer = event.getEntity();


        ArachneAttachment.get(oldPlayer).ifPresent(oldCap->{
            ArachneAttachment cap = ArachneAttachment.get(newPlayer).orElse(null);
            if(cap!=null){
                cap.copyFrom(oldCap);
                cap.init();
            }

        });
    }

    @SubscribeEvent
    public static void onEquip(LivingEquipmentChangeEvent event) {

        if (!(event.getEntity() instanceof Player player)) return;

    }
    @SubscribeEvent
    public static void spawnEvent(RegisterSpawnPlacementsEvent event) {
        event.register(NRegistry.VOID_HOPPER.get(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Monster::checkAnyLightMonsterSpawnRules,
                RegisterSpawnPlacementsEvent.Operation.REPLACE
        );
        event.register(NRegistry.VOID_VEILMOTH.get(),
                SpawnPlacementTypes.NO_RESTRICTIONS,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Mob::checkMobSpawnRules,
                RegisterSpawnPlacementsEvent.Operation.REPLACE
        );
        event.register(NRegistry.VOID_BEETLE.get(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Mob::checkMobSpawnRules,
                RegisterSpawnPlacementsEvent.Operation.REPLACE
        );


        event.register(NRegistry.VOID_NEEDLE.get(),
                SpawnPlacementTypes.NO_RESTRICTIONS,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Monster::checkAnyLightMonsterSpawnRules,
                RegisterSpawnPlacementsEvent.Operation.REPLACE
        );

        event.register(
                NRegistry.SCARAB.get(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Monster::checkMonsterSpawnRules,
                RegisterSpawnPlacementsEvent.Operation.REPLACE);
    }

    @SubscribeEvent
    public static void registerPackets(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(AracneMod.MODID).versioned("1.0");

        registrar.playToClient(PacketNerubianData.TYPE, PacketNerubianData.STREAM_CODEC, PacketNerubianData::handle);
    }

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(NRegistry.SCARAB.get(), ScarabEntity.createAttributes().build());
        event.put(NRegistry.VOID_NEEDLE.get(), VoidNeedleEntity.createAttributes().build());
        event.put(NRegistry.VOID_HOPPER.get(), VoidHopperEntity.createAttributes().build());
        event.put(NRegistry.VOID_BEETLE.get(), VoidBeetleEntity.createAttributes().build());
        event.put(NRegistry.VOID_VEILMOTH.get(), VoidVeilmothEntity.createAttributes().build());
    }

    @SubscribeEvent
    public static void onPick(ItemEntityPickupEvent.Pre event){
        ArachneAttachment.get(event.getPlayer()).ifPresent(e->{
            if(e.currentQuest!=null && !e.currentQuest.isComplete(e)){
                if(e.currentQuest.canAddProgress(event.getItemEntity().getItem().getItem().toString())){
                    e.refreshQuest(event.getPlayer());
                }
            }
        });
    }

    @SubscribeEvent
    public static void onUse(PlayerInteractEvent.RightClickItem event){
        if (!event.getItemStack().getItem().equals(Items.STICK))return;

    }

    public static void teleportToVoid(Vec3 position, Level level, LivingEntity living){
        ServerLevel serverLevel = ((ServerLevel)level).getServer().getLevel(ResourceKey.create(Registries.DIMENSION, Identifier.fromNamespaceAndPath("arachnemod", "void")));
        ChunkPos pos = serverLevel.getChunk(living.blockPosition()).getPos();
        living.teleport(new TeleportTransition(serverLevel,createIsland(serverLevel.getSeed(),pos.x(),pos.z()), Vec3.ZERO,0.0F,0.0F,(entity)->{
            if (entity instanceof ServerPlayer serverPlayer){
                ArachneAttachment.get(serverPlayer).ifPresent(arachneAttachment -> {
                    arachneAttachment.setTeleportBackPos(new BlockPos((int) position.x, (int) position.y, (int) position.z));
                });
            }
        }));
    }
    private static Vec3 createIsland(long seed,int cellX, int cellZ) {
        int cellSize = 256;

        Random random = new Random(seed + cellX * 341873128712L + cellZ * 132897987541L);

        double centerX = cellX * cellSize + random.nextInt(cellSize);
        double centerZ = cellZ * cellSize + random.nextInt(cellSize);
        return new Vec3(centerX,252,centerZ);
    }
    @SubscribeEvent
    public static void addQuestsData(AddServerReloadListenersEvent event){
        event.addListener(Identifier.parse("manager"),new QuestManager());
    }

}
