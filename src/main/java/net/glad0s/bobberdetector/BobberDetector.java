package net.glad0s.bobberdetector;

import com.mojang.logging.LogUtils;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.item.TooltipHelper;
import com.simibubi.create.foundation.item.TooltipModifier;
import net.glad0s.bobberdetector.block.ModBlocks;
import net.glad0s.bobberdetector.block.TileEntityInit;
import net.glad0s.bobberdetector.item.ModItems;
import net.glad0s.bobberdetector.register.ModPonders;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(BobberDetector.MOD_ID)
public class BobberDetector
{
    public static final String MOD_ID = "bobberdetector";
    private static final Logger LOGGER = LogUtils.getLogger();

    public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(MOD_ID);
    static {
        REGISTRATE.setTooltipModifierFactory(item -> {
            return new ItemDescription.Modifier(item, TooltipHelper.Palette.STANDARD_CREATE);

        });
    }
    public BobberDetector()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        REGISTRATE.registerEventListeners(modEventBus);

        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        TileEntityInit.TILE_ENTITY_TYPES.register(modEventBus);

        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::doClientStuff);

        MinecraftForge.EVENT_BUS.register(this);

        generateLangEntries();
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        event.enqueueWork(ModPonders::register);
    }
    public static CreateRegistrate registrate() {
        return REGISTRATE;
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

    private void generateLangEntries(){

        registrate().addRawLang("bobberdetector.ponder.detector.header", "Automatic Fishing");
        registrate().addRawLang("bobberdetector.ponder.detector.text_1", "temp 1");
        registrate().addRawLang("bobberdetector.ponder.detector.text_2", "temp 2");
        registrate().addRawLang("bobberdetector.ponder.detector.text_3", "temp 3");
        registrate().addRawLang("bobberdetector.ponder.detector.text_4", "temp 4");

    }

    public static ResourceLocation asResource(String path) {
        return new ResourceLocation(MOD_ID, path);
    }

}
