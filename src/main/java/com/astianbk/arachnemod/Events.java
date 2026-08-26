package com.astianbk.arachnemod;

import com.astianbk.arachnemod.common.compendium.Action;
import com.astianbk.arachnemod.common.compendium.CompendiumManager;
import com.astianbk.arachnemod.common.dialogs.DialogsManager;
import com.astianbk.arachnemod.common.quests.QuestManager;
import com.astianbk.arachnemod.common.registry.NRegistry;
import com.astianbk.arachnemod.server.cap.ArachneAttachment;
import com.astianbk.arachnemod.server.cap.TheVoidAttachment;
import com.astianbk.arachnemod.server.cap.data.BlessingData;
import com.astianbk.arachnemod.server.entity.*;
import com.astianbk.arachnemod.server.goal.SpiderTargetEnemyGoal;
import com.astianbk.arachnemod.server.goal.SpiderTargetGoal;
import com.astianbk.arachnemod.server.network.PacketNerubianData;
import com.astianbk.arachnemod.server.network.PacketPlayDialog;
import com.astianbk.arachnemod.server.network.PacketSetScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.golem.IronGolem;
import net.minecraft.world.entity.monster.Silverfish;
import net.minecraft.world.entity.monster.spider.Spider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ViewportEvent;
import net.neoforged.neoforge.event.AddServerReloadListenersEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingEquipmentChangeEvent;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import net.neoforged.neoforge.event.entity.player.*;
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
                if (e.blessingIsActive(BlessingData.BlessingType.ARACHNE_INFECTION)){
                    if (entity.getRandom().nextFloat()<0.3F){
                        SummoneableSpiderEntity summoneableSpider = new SummoneableSpiderEntity(NRegistry.VOID_SPIDER.get(),player.level());
                        summoneableSpider.setPos(event.getEntity().position());
                        summoneableSpider.setOwner(player);
                        player.level().addFreshEntity(summoneableSpider);
                    }
                }
                String id = event.getEntity().getEncodeId();
                if(e.currentQuest!=null && !e.currentQuest.isComplete(e)){
                    if(e.currentQuest.canAddProgress(id)){
                        e.progressQuest++;
                        player.syncData(NRegistry.ARACNE);
                    }
                }
                if (id == null)return;
                if (player instanceof ServerPlayer){
                    e.checkCompendiumEvents((ServerPlayer) player,Identifier.parse(id), Action.KILL);
                }
            });
        }
    }

    @SubscribeEvent
    public static void onJoin(EntityJoinLevelEvent event){
        Entity entity = event.getEntity();
        if(entity instanceof Player player){
            ArachneAttachment.get(player).ifPresent(ArachneAttachment::init);
        }
        if (entity instanceof Spider spider){
            spider.targetSelector.removeAllGoals(goal -> true);
            spider.targetSelector.addGoal(2,new SpiderTargetEnemyGoal<>(spider));
            spider.targetSelector.addGoal(1, new HurtByTargetGoal(spider, new Class[0]));
            spider.targetSelector.addGoal(3, new SpiderTargetGoal<>(spider, IronGolem.class));
        }
        if (entity instanceof Silverfish silverfish){
            silverfish.targetSelector.removeAllGoals(goal -> true);

            silverfish.targetSelector.addGoal(1, (new HurtByTargetGoal(silverfish, new Class[0])).setAlertOthers(new Class[0]));
            silverfish.targetSelector.addGoal(2, new NearestAttackableTargetGoal(silverfish, Player.class, true){
                @Override
                public boolean canUse() {
                    if (!super.canUse()){
                        return false;
                    }
                    if (target instanceof Player player){
                        return !ArachneAttachment.get(player).get().blessingIsActive(BlessingData.BlessingType.ARACHNE_ALLIE);
                    }
                    return true;
                }
            });

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
                Mob::checkMobSpawnRules,
                RegisterSpawnPlacementsEvent.Operation.REPLACE);

        event.register(NRegistry.VOID_VEILMOTH.get(),
                SpawnPlacementTypes.NO_RESTRICTIONS,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Mob::checkMobSpawnRules,
                RegisterSpawnPlacementsEvent.Operation.REPLACE);

        event.register(NRegistry.VOID_BEETLE.get(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Mob::checkMobSpawnRules,
                RegisterSpawnPlacementsEvent.Operation.REPLACE);


        event.register(NRegistry.VOID_NEEDLE.get(),
                SpawnPlacementTypes.NO_RESTRICTIONS,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Mob::checkMobSpawnRules,
                RegisterSpawnPlacementsEvent.Operation.REPLACE);

        event.register(NRegistry.SCARAB.get(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Mob::checkMobSpawnRules,
                RegisterSpawnPlacementsEvent.Operation.REPLACE);
    }

    @SubscribeEvent
    public static void registerPackets(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(AracneMod.MODID).versioned("1.0");
        registrar.playToClient(PacketPlayDialog.TYPE, PacketPlayDialog.STREAM_CODEC, PacketPlayDialog::handle);
        registrar.playToClient(PacketNerubianData.TYPE, PacketNerubianData.STREAM_CODEC, PacketNerubianData::handle);
        registrar.playToClient(PacketSetScreen.TYPE, PacketSetScreen.STREAM_CODEC,PacketSetScreen::handle);
    }

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(NRegistry.SCARAB.get(), ScarabEntity.createAttributes().build());
        event.put(NRegistry.VOID_NEEDLE.get(), VoidNeedleEntity.createAttributes().build());
        event.put(NRegistry.VOID_HOPPER.get(), VoidHopperEntity.createAttributes().build());
        event.put(NRegistry.VOID_BEETLE.get(), VoidBeetleEntity.createAttributes().build());
        event.put(NRegistry.VOID_GRUB.get(), VoidGrubEntity.createAttributes().build());
        event.put(NRegistry.VOID_SPIDER.get(),SummoneableSpiderEntity.createAttributes().build());
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
    public static void hurtEvent(LivingDamageEvent.Pre event){
        if (!event.getEntity().hasEffect(NRegistry.DAMNATION))return;
        if (event.getEntity() instanceof Player){
            event.setNewDamage(event.getOriginalDamage()+3);
        }
        if (event.getSource().getEntity() instanceof Player){
            event.setNewDamage(event.getOriginalDamage()-3);
        }
    }
    @SubscribeEvent
    public static void onSwing(AttackEntityEvent event){
        if (event.getEntity().hasEffect(NRegistry.ARACHNOPHOBIA)) {
            if (event.getTarget().is(EntityTypeTags.ARTHROPOD)){
                if (event.getTarget().level().getRandom().nextFloat() < 0.25F){
                    ItemStack dropItem = event.getEntity().getMainHandItem().copy();
                    if (!dropItem.isEmpty()){
                        event.getEntity().getMainHandItem().shrink(1);
                        if (event.getEntity().level() instanceof ServerLevel){
                            event.getEntity().spawnAtLocation((ServerLevel) event.getEntity().level(),dropItem);
                        }
                    }
                }
            }
        }

    }
    @SubscribeEvent
    public static void povMove(ViewportEvent.ComputeCameraAngles event){
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null)return;
        TheVoidAttachment voidAttachment = mc.level.getData(NRegistry.THE_VOID_ATTACHMENT.get());
        if (voidAttachment.bedrockfall){
            float offset = (float) Math.cos((event.getPartialTick() + event.getCamera().entity().tickCount) * Math.PI / 8) * voidAttachment.getIntensityShake((float) event.getPartialTick());
            event.setRoll((float) (50.0F / Math.PI * offset) + event.getRoll());
        }

        if (mc.player !=null){
            if (mc.player.hasEffect(NRegistry.ARACHNOPHOBIA)){
                ArachneAttachment.get(mc.player).ifPresent(arachneAttachment -> {
                    float amount = ArachneAttachment.getSpiderCrosshairAmount(mc.player, 16.0F);
                    float sin = (float) Math.sin((event.getPartialTick() +  mc.player.tickCount) * Math.PI / 2) * amount;
                    event.setYaw((float) (15.0F / Math.PI * sin) + event.getYaw());
                });

            }

        }

//        event.setRoll(30 * offset);
        //event.getCamera().move()
    }

    @SubscribeEvent
    public static void onFall(LivingDamageEvent.Pre event){
        if (event.getSource().is(DamageTypeTags.IS_FALL)){
            if (event.getEntity() instanceof Player player){
                Level level = player.level();
                ArachneAttachment.get(player).ifPresent(arachneAttachment -> {
                    if (arachneAttachment.blessingIsActive(BlessingData.BlessingType.ARACHNE_ANTI_FALL)){
                        event.setNewDamage(0.0F);
                        BlockPos pos = player.blockPosition();
                        BlockPos north = pos.north();
                        BlockPos south = pos.south();
                        BlockPos east = pos.east();
                        BlockPos west = pos.west();
                        BlockPos above = pos.above();
                        if (level.isEmptyBlock(north)){
                            level.setBlock(north,NRegistry.VOID_WEB_BLOCK.get().defaultBlockState(),3);
                        }
                        if (level.isEmptyBlock(south)){
                            level.setBlock(south,NRegistry.VOID_WEB_BLOCK.get().defaultBlockState(),3);
                        }
                        if (level.isEmptyBlock(east)){
                            level.setBlock(east,NRegistry.VOID_WEB_BLOCK.get().defaultBlockState(),3);
                        }
                        if (level.isEmptyBlock(west)){
                            level.setBlock(west,NRegistry.VOID_WEB_BLOCK.get().defaultBlockState(),3);
                        }
                        if (level.isEmptyBlock(above)){
                            level.setBlock(above,NRegistry.VOID_WEB_BLOCK.get().defaultBlockState(),3);
                        }
                        if (level.isEmptyBlock(pos)){
                            level.setBlock(pos,NRegistry.VOID_WEB_BLOCK.get().defaultBlockState(),3);
                        }
                        arachneAttachment.startBlessingCooldown(BlessingData.BlessingType.ARACHNE_ANTI_FALL);
                    }
                });
            }
        }
    }

    @SubscribeEvent
    public static void onUse(PlayerInteractEvent.RightClickItem event){
        if (!event.getItemStack().getItem().equals(Items.STICK)) {
        }

    }
    public static void teleportToTheDepth(Vec3 position, Level level, LivingEntity living){
        ServerLevel serverLevel = ((ServerLevel)level).getServer().getLevel(ResourceKey.create(Registries.DIMENSION, Identifier.fromNamespaceAndPath("arachnemod", "the_depths")));
        ChunkPos pos = serverLevel.getChunk(living.blockPosition()).getPos();
        living.teleport(new TeleportTransition(serverLevel,new Vec3(position.x,245,position.z), Vec3.ZERO,0.0F,0.0F,(entity)->{

        }));
    }
    public static void teleportToVoid(Vec3 position, Level level, LivingEntity living){
        ServerLevel serverLevel = ((ServerLevel)level).getServer().getLevel(ResourceKey.create(Registries.DIMENSION, Identifier.fromNamespaceAndPath("arachnemod", "void")));
        ChunkPos pos = serverLevel.getChunk(living.blockPosition()).getPos();
        Vec3 vec3 = getCenterIsland(serverLevel,serverLevel.getSeed(),pos.x(),pos.z());
        living.teleport(new TeleportTransition(serverLevel,vec3, Vec3.ZERO,0.0F,0.0F,(entity)->{
            if (entity instanceof ServerPlayer serverPlayer){
                ArachneAttachment.get(serverPlayer).ifPresent(arachneAttachment -> {
                    BlockPos pos1 = new BlockPos((int) position.x, (int) position.y, (int) position.z);
                    arachneAttachment.setTeleportBackPos(pos1);
                    if (serverLevel.getEntitiesOfClass(WebElevatorEntity.class,serverPlayer.getBoundingBox().inflate(40.0F)).isEmpty()){
                        WebElevatorEntity webElevator = new WebElevatorEntity(NRegistry.WEB_ELEVATOR.get(), level);
                        webElevator.setPos(vec3);
                        serverLevel.addFreshEntity(webElevator);
                    }
                });
            }
        }));
    }
    private static Vec3 getCenterIsland(ServerLevel serverLevel, long seed, int cellX, int cellZ) {
        int cellSize = 256;

        Random random = new Random(seed + cellX * 341873128712L + cellZ * 132897987541L);

        double centerX = cellX * cellSize + random.nextInt(cellSize);
        double centerZ = cellZ * cellSize + random.nextInt(cellSize);
        for (int y = 398; y >= 240; y--) {

            BlockPos pos = new BlockPos(cellX, y, cellZ);
            BlockState state = serverLevel.getBlockState(pos);

            if (!state.isAir()) {
                return Vec3.atCenterOf(pos.above());
            }
        }
        return new Vec3(centerX,serverLevel.getHeight(),centerZ);
    }
    @SubscribeEvent
    public static void addQuestsData(AddServerReloadListenersEvent event){
        event.addListener(Identifier.parse("manager"),new QuestManager());
        event.addListener(Identifier.parse("dialogs"),new DialogsManager());
        event.addListener(Identifier.parse("compendium"),new CompendiumManager());
    }

}
