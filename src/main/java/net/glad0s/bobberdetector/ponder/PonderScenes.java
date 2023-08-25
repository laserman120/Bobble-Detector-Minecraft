package net.glad0s.bobberdetector.ponder;

import com.simibubi.create.content.kinetics.deployer.DeployerBlockEntity;
import com.simibubi.create.foundation.ponder.*;
import com.simibubi.create.foundation.ponder.element.EntityElement;
import com.simibubi.create.foundation.ponder.instruction.EmitParticlesInstruction;
import net.glad0s.bobberdetector.block.entity.BobberDetectorTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.projectile.FishingHook;

public class PonderScenes {
    public static void bobberBasic(SceneBuilder scene, SceneBuildingUtil util) {
        scene.title("detector", "Automatic Fishing");
        scene.configureBasePlate(0, 0, 5);

        //show whole scene
        scene.world.showSection(util.select.layer(0), Direction.UP);
        scene.world.showSection(util.select.layer(1), Direction.UP);
        scene.world.showSection(util.select.layer(2), Direction.UP);

        BlockPos deployerPos = util.grid.at(1, 1, 3);
        BlockPos detectorPos = util.grid.at(5, 1, 3);
        BlockPos bobberPos = util.grid.at(3,0,3);
        BlockPos link1 = util.grid.at(6,1,3);
        BlockPos link2 = util.grid.at(1,2,0);
        BlockPos redstoneTorch = util.grid.at(1,1,1);
        BlockPos redstoneRepeater = util.grid.at(1,1,2);
        BlockPos searchArea1 = util.grid.at(4,-2,1);
        BlockPos searchArea2 = util.grid.at(0,2,5);
        BlockPos smallCogwheel = util.grid.at(2,0,7);

        Selection deployerSelection = util.select.position(deployerPos);
        Selection detectorSelection = util.select.position(detectorPos);
        Selection link1Selection = util.select.position(link1);
        Selection link2Selection = util.select.position(link2);
        Selection redstoneTorchSelection = util.select.position(redstoneTorch);
        Selection redstoneRepeaterSelection = util.select.position(redstoneRepeater);
        Selection searchArea1Selection = util.select.position(searchArea1);
        Selection searchArea2Selection = util.select.position(searchArea2);
        Selection smallCogwheelSelection = util.select.position(smallCogwheel);

        Vec3 bobberParticles = util.vector.topOf(3,0,3);
        Vec3 itemMovement = util.vector.of(-.2,.6,0);


        //begin sequence
        scene.idle(30);

        scene.world.setKineticSpeed(smallCogwheelSelection, 64);


        ElementLink<EntityElement> bobber = scene.world.createEntity(w -> {
            FishingHook entity = EntityType.FISHING_BOBBER.create(w);
            Vec3 p = util.vector.topOf(util.grid.at(3, 1, 3));
            entity.setPos(p.x, p.y, p.z);
            entity.xo = p.x;
            entity.yo = p.y;
            entity.zo = p.z;
            return entity;
        });

        Class<DeployerBlockEntity> deployerType = DeployerBlockEntity.class;
        Class<BobberDetectorTileEntity> detectorType = BobberDetectorTileEntity.class;

        //give deployer fishing rod
        ItemStack fishing_rod = new ItemStack(Items.FISHING_ROD);

        scene.world.modifyBlockEntityNBT(deployerSelection, deployerType,
                nbt -> nbt.put("HeldItem", fishing_rod.serializeNBT()));

        scene.overlay.showText(80)
                .text("Deployers can be given fishing rods to fish for you")
                .placeNearTarget()
                .attachKeyFrame()
                .pointAt(util.vector.topOf(deployerPos));

        scene.idle(30);

        //rotate deployer
        scene.world.moveDeployer(deployerPos, 1, 25);

        scene.idle(26);

        //Emit particles where the bobber should be
        scene.effects.emitParticles(bobberParticles, EmitParticlesInstruction.Emitter.simple(ParticleTypes.BUBBLE, Vec3.ZERO), .2f,390 );
        scene.effects.emitParticles(bobberParticles, EmitParticlesInstruction.Emitter.simple(ParticleTypes.SPLASH, Vec3.ZERO),.3f,390 );

        //Turn on top detector lamp
        scene.world.modifyBlocks(detectorSelection, s ->{
            s = s.cycle(BlockStateProperties.LIT);
            return s;
        }, false);

        //rotate deployer
        scene.world.moveDeployer(deployerPos, -1, 25);

        scene.idle(50);

        scene.overlay.showText(80)
                .text("Sadly you wont be able to see the fishing bobber in the water, only the particles")
                .placeNearTarget()
                .attachKeyFrame()
                .pointAt(util.vector.topOf(bobberPos));

        scene.idle(100);

        scene.overlay.showText(80)
                .text("But whenever a bobber is inside it´s range the bobber detector lights up")
                .placeNearTarget()
                .attachKeyFrame()
                .pointAt(util.vector.topOf(detectorPos));

        AABB box = new AABB(searchArea1).minmax(new AABB(searchArea2));
        scene.overlay.chaseBoundingBoxOutline(PonderPalette.GREEN, Object.class, box, 80);

        scene.idle(140);

        scene.world.setKineticSpeed(smallCogwheelSelection, 128);

        scene.overlay.showText(80)
                .attachKeyFrame()
                .text("When a fish is being caught the detector emits a redstone signal")
                .pointAt(util.vector.topOf(detectorPos))
                .placeNearTarget();

        scene.idle(100);

        scene.effects.emitParticles(bobberParticles, EmitParticlesInstruction.Emitter.simple(ParticleTypes.SPLASH, Vec3.ZERO),1.2f,20 );
        scene.effects.emitParticles(bobberParticles, EmitParticlesInstruction.Emitter.withinBlockSpace(ParticleTypes.SPLASH, Vec3.ZERO),.2f,20 );
        scene.effects.emitParticles(bobberParticles, EmitParticlesInstruction.Emitter.withinBlockSpace(ParticleTypes.BUBBLE, Vec3.ZERO),.2f,20 );

        //Redstone Output
        scene.world.modifyBlocks(detectorSelection, s ->{
            s = s.cycle(BlockStateProperties.POWERED);
            return s;
        }, false);
        scene.world.toggleRedstonePower(redstoneTorchSelection);
        scene.world.toggleRedstonePower(redstoneRepeaterSelection);
        scene.world.toggleRedstonePower(link1Selection);
        scene.world.toggleRedstonePower(link2Selection);


        scene.idle(5);

        scene.world.moveDeployer(deployerPos, 1, 15);

        scene.idle(16);

        //Turn off top detector lamp
        scene.world.modifyBlocks(detectorSelection, s ->{
            s = s.cycle(BlockStateProperties.LIT);
            return s;
        }, false);

        //yeet cod
        var stack = new ItemStack(Items.COD, 1);
        scene.world.createItemEntity(bobberParticles, itemMovement, stack);

        scene.world.moveDeployer(deployerPos, -1, 15);

        scene.idle(10);

        //Turn off Redstone
        scene.world.modifyBlocks(detectorSelection, s ->{
            s = s.cycle(BlockStateProperties.POWERED);
            return s;
        }, false);
        scene.world.toggleRedstonePower(redstoneTorchSelection);
        scene.world.toggleRedstonePower(redstoneRepeaterSelection);
        scene.world.toggleRedstonePower(link1Selection);
        scene.world.toggleRedstonePower(link2Selection);

        scene.idle(50);

        scene.overlay.showText(80)
                .text("You will have to figure out how to pickup the items though")
                .pointAt(util.vector.blockSurface(deployerPos, Direction.WEST))
                .placeNearTarget();

        scene.idle(80);

    }
}
