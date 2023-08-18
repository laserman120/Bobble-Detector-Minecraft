package net.glad0s.bobberdetector;

import com.mojang.logging.LogUtils;
import net.glad0s.bobberdetector.block.ModBlocks;
import net.glad0s.bobberdetector.block.TileEntityInit;
import net.glad0s.bobberdetector.item.ModItems;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.tags.ITag;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(BobberDetector.MOD_ID)
public class BobberDetector
{
    public static final String MOD_ID = "bobberdetector";
    private static final Logger LOGGER = LogUtils.getLogger();


    public BobberDetector()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        TileEntityInit.TILE_ENTITY_TYPES.register(modEventBus);

        modEventBus.addListener(this::commonSetup);


        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {



        }

        public static final TagKey<EntityType<?>> BobberTag = ForgeRegistries.ENTITY_TYPES.tags().createTagKey(new ResourceLocation("forge", "bobber"));

    }
}
