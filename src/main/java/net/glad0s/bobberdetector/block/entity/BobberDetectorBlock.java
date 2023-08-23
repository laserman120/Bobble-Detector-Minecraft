package net.glad0s.bobberdetector.block.entity;

import net.glad0s.bobberdetector.block.TileEntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;

public class BobberDetectorBlock extends Block implements EntityBlock {
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    private final int maxRange;

    public BobberDetectorBlock(final Properties properties, int maxRange){
        super(Properties.copy(Blocks.STONE));
        this.maxRange = maxRange;
        this.registerDefaultState(this.defaultBlockState().setValue(POWERED, false));
        this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH));
        this.registerDefaultState(this.defaultBlockState().setValue(LIT, false));
    }

    public static void setPowered(BlockState state, Level level, BlockPos pos, boolean powered) {
        if (!level.isClientSide) {
            level.setBlock(pos, state.setValue(POWERED, powered), 3);
        }
    }

    public static void setLit(BlockState state, Level level, BlockPos pos, boolean lit) {
        if (!level.isClientSide) {
            level.setBlock(pos, state.setValue(LIT, lit), 3);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(POWERED);
        builder.add(FACING);
        builder.add(LIT);
    }
    @Override
    public boolean isSignalSource(BlockState state) {
        return true;
    }
    @Override
    public int getSignal(BlockState blockState, BlockGetter blockAccess, BlockPos pos, Direction side) {
        return blockState.getValue(POWERED) ? 15 : 0;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {

        return TileEntityInit.BOBBER_DETECTOR.get().create(blockPos, blockState);
    }


    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
        return type == TileEntityInit.BOBBER_DETECTOR.get() ? BobberDetectorTileEntity::tick : null;
    }
    public static Direction getFacingDirection(BlockState state) {
        return state.getValue(FACING);
    }

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context){
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection());
    }



    /*@Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit){
        if (!pLevel.isClientSide()){
            BlockEntity entity = pLevel.getBlockEntity(pPos);
            if(entity instanceof BobberDetectorTileEntity) {
                NetworkHooks.openScreen(((ServerPlayer)pPlayer), (BobberDetectorTileEntity)entity, pPos);
             } else {
                throw new IllegalStateException("The Container Provider is missing - Bobber Detector");
            }
        }

        return InteractionResult.sidedSuccess(pLevel.isClientSide());
    }
    */



}
