package com.astianbk.arachnemod.common.block;

import com.astianbk.arachnemod.AracneMod;
import com.astianbk.arachnemod.common.ArachneIdolBlockEntity;
import com.astianbk.arachnemod.common.registry.NRegistry;
import com.astianbk.arachnemod.server.entity.OrbEntity;
import com.astianbk.arachnemod.server.cap.ArachneAttachment;
import com.astianbk.arachnemod.server.network.PacketPlayDialog;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class ArachneIdolBlock extends BaseEntityBlock {
    public static final MapCodec<ArachneIdolBlock> CODEC = simpleCodec(ArachneIdolBlock::new);
    public static EnumProperty<Direction> DIRECTION = BlockStateProperties.HORIZONTAL_FACING;
    public static BooleanProperty LIT = BlockStateProperties.LIT;
    public static Map<ArachneIdolBlockEntity.State,OrbEntity.Type[]> typesForState = Map.of(ArachneIdolBlockEntity.State.MENU
            ,new OrbEntity.Type[]{OrbEntity.Type.CANCEL, OrbEntity.Type.QUEST, OrbEntity.Type.BLESSING, OrbEntity.Type.QUEST_REPUTATION}
    , ArachneIdolBlockEntity.State.SELECT_QUEST
            ,new OrbEntity.Type[]{OrbEntity.Type.QUEST_GET, OrbEntity.Type.QUEST_KILL});

    public ArachneIdolBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.getStateDefinition().any().setValue(LIT,false).setValue(DIRECTION,Direction.EAST));
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        return super.getStateForPlacement(context).setValue(DIRECTION,context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        ArachneAttachment.get(player).ifPresent(e->{
            ArachneIdolBlockEntity arachneIdol = (ArachneIdolBlockEntity) level.getBlockEntity(pos);
            if (!level.isClientSide()){
                if (arachneIdol==null)return;
                if (arachneIdol.currentState == ArachneIdolBlockEntity.State.NONE){
                    if(!state.getValue(LIT)){
                        if (e.currentQuest==null){
                            level.setBlock(pos,state.setValue(LIT,true),3);
                            PacketDistributor.sendToPlayer((ServerPlayer) player,new PacketPlayDialog(Identifier.fromNamespaceAndPath(AracneMod.MODID,"arachne_first_introduction"),player.getId()));
                            arachneIdol.startMenu(player,level,pos,typesForState.get(ArachneIdolBlockEntity.State.MENU));
                        }
                    }else {
                        if (e.currentQuest==null){
                            level.setBlock(pos,state.setValue(LIT,false),3);
                        }else if (e.currentQuest.isComplete(e)){
                            level.setBlock(pos,state.setValue(LIT,false),3);

                            e.completeQuest((ServerPlayer) player);
                        }

                    }
                }
            }
        });
        return InteractionResult.SUCCESS;
    }



    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LIT).add(DIRECTION);
        super.createBlockStateDefinition(builder);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new ArachneIdolBlockEntity(blockPos,blockState);
    }
}
