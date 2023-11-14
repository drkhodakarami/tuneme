package jiraiyah.tuneme.datagen;

import jiraiyah.tuneme.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.util.Identifier;

public class ModRecipeProvider extends FabricRecipeProvider
{
    public ModRecipeProvider(FabricDataOutput output)
    {
        super(output);
    }

    @Override
    public void generate(RecipeExporter exporter)
    {
        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.TUNER, 1)
                .pattern("P P")
                .pattern("IEI")
                .pattern(" I ")
                .input('I', Items.IRON_INGOT)
                .input('P', Items.ENDER_PEARL)
                .input('E', Items.ENDER_EYE)
                .criterion(hasItem(Items.IRON_INGOT), conditionsFromItem(Items.IRON_INGOT))
                .criterion(hasItem(Items.ENDER_PEARL), conditionsFromItem(Items.ENDER_PEARL))
                .criterion(hasItem(Items.ENDER_EYE), conditionsFromItem(Items.ENDER_EYE))
                .offerTo(exporter, new Identifier(getRecipeName(ModItems.TUNER)));
    }
}