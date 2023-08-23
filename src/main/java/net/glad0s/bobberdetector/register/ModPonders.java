package net.glad0s.bobberdetector.register;

import com.simibubi.create.foundation.ponder.PonderRegistrationHelper;
import com.simibubi.create.foundation.ponder.PonderRegistry;
import com.simibubi.create.infrastructure.ponder.AllPonderTags;

import net.glad0s.bobberdetector.BobberDetector;
import net.glad0s.bobberdetector.block.ModBlocks;
import net.glad0s.bobberdetector.ponder.PonderScenes;

public class ModPonders {
    static final PonderRegistrationHelper HELPER = new PonderRegistrationHelper(BobberDetector.MOD_ID);

    public static void register(){

        HELPER.addStoryBoard(ModBlocks.BOBBER_DETECTOR.getId(), "bobber_detector_ponder", PonderScenes::bobberBasic, AllPonderTags.REDSTONE);

        PonderRegistry.TAGS.forTag(AllPonderTags.REDSTONE).add(ModBlocks.BOBBER_DETECTOR.getId());
    }
}
