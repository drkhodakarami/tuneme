package jiraiyah.tuneme;

import jiraiyah.tuneme.datagen.ModEntityTagProvider;
import jiraiyah.tuneme.datagen.ModModelProvider;
import jiraiyah.tuneme.datagen.ModRecipeProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class TuneMeDataGenerator implements DataGeneratorEntrypoint
{
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator)
    {
        TuneMe.LOGGER.info(">>> Generating Data");

        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

        pack.addProvider(ModEntityTagProvider::new);
        pack.addProvider(ModModelProvider::new);
        pack.addProvider(ModRecipeProvider::new);
    }
}