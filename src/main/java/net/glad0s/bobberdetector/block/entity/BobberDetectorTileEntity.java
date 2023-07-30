package net.glad0s.bobberdetector.block.entity;

import net.glad0s.bobberdetector.block.TileEntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.AABB;


import java.util.List;

public class BobberDetectorTileEntity extends BlockEntity {
    public BobberDetectorTileEntity(BlockPos pos, BlockState state) {
        super(TileEntityInit.BOBBER_DETECTOR.get(), pos, state);
    }

    public static <T extends BlockEntity> void tick(Level level, BlockPos pos, BlockState state, T be) {
        BobberDetectorTileEntity tile = (BobberDetectorTileEntity) be;

        if (!level.isClientSide()) {
            tile.bobberScan();

        }
    }

    final int RANGE = 5;
    private int catchTimer = 0;
    final int CATCHCOOLDOWN = 20; //Ticks until the next detection can take place
    private int redstoneTimer = 0;
    private final int REDSTONE_DURATION = 10; //Duration of the redstone pulse
    private boolean powered;



    private void updatePower(boolean powered){
        BlockState blockstate = this.getBlockState();
        Block block = blockstate.getBlock();
        if(block instanceof BobberDetectorBlock){
            this.powered = powered;
            BobberDetectorBlock.setPowered(blockstate, this.level, this.worldPosition, powered);
        }
    }

    private void bobberScan() {
        if (!level.isClientSide && catchTimer == 0) {
            //try to get the direction the block is facing
            BlockState blockstate = this.getBlockState();
            Direction facing = BobberDetectorBlock.getFacingDirection(blockstate);

            //create the search area
            BlockPos topCorner = this.worldPosition.relative(facing).relative(facing.getClockWise(), RANGE / 2).offset(0, RANGE / 2,0);
            BlockPos bottomCorner = this.worldPosition.relative(facing, RANGE).relative(facing.getClockWise().getClockWise().getClockWise(), RANGE / 2).offset(0, -RANGE / 2,0);

            AABB box = new AABB(bottomCorner).minmax(new AABB(topCorner));

            List<net.minecraft.world.entity.Entity> entities = this.level.getEntities(null, box);
            for (Entity target : entities) {
                if (target.getType() == EntityType.FISHING_BOBBER) {

                    double x = Math.round((target.getDeltaMovement().x * 100) * 10) / 10.0;
                    double y = target.getDeltaMovement().y;
                    double z = Math.round((target.getDeltaMovement().z * 100) * 10) / 10.0;
                    System.out.println("--- " + "X: " + x + "Y: " + y + "Z: " + z + "   " + this.getBlockState());
                    if (y < -0.075 && x == 0 && z == 0) {
                            System.out.println("-------------------------------------- Maybe a Catch? -------------------------------------- " + redstoneTimer);
                            catchTimer = CATCHCOOLDOWN;
                            redstoneTimer = REDSTONE_DURATION;
                            updatePower(true);
                            level.updateNeighborsAt(this.worldPosition, this.getBlockState().getBlock());
                    }
                }
            }
        }

        if (redstoneTimer > 0) {
            redstoneTimer--;
            if (redstoneTimer == 0) {
                System.out.println(" redstone off " + redstoneTimer);
                updatePower(false);

            }
        }

        if (catchTimer > 0) {
            catchTimer--;
        }
    }
}
