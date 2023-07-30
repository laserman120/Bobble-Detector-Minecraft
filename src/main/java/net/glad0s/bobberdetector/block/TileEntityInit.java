package net.glad0s.bobberdetector.block;

import net.glad0s.bobberdetector.BobberDetector;
import net.glad0s.bobberdetector.block.entity.BobberDetectorTileEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class TileEntityInit {
    public static final DeferredRegister<BlockEntityType<?>> TILE_ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, BobberDetector.MOD_ID);

    public static final RegistryObject<BlockEntityType<BobberDetectorTileEntity>> BOBBER_DETECTOR = TILE_ENTITY_TYPES.register("bobber_detector",
            () -> BlockEntityType.Builder.of(BobberDetectorTileEntity::new, ModBlocks.BOBBER_DETECTOR.get()).build(null));
}
