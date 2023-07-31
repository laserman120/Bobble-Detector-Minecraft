package net.glad0s.bobberdetector.block;

import net.glad0s.bobberdetector.BobberDetector;
import net.glad0s.bobberdetector.block.entity.BobberDetectorBlock;
import net.glad0s.bobberdetector.item.ModItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {
    public  static  final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, BobberDetector.MOD_ID);

    public static final RegistryObject<Block> BOBBER_DETECTOR = registerBlock("bobber_detector",
            () -> new BobberDetectorBlock(BlockBehaviour.Properties.of(Material.STONE)
                    .lightLevel(state -> state.getValue(BobberDetectorBlock.LIT) ? 5 : 0)
                    ), CreativeModeTab.TAB_REDSTONE);

    private  static <T extends Block>RegistryObject<T> registerBlock(String name, Supplier<T> block, CreativeModeTab tab){
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn, tab);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block, CreativeModeTab tab) {
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties().tab(tab)));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
