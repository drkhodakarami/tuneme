package jiraiyah.tuneme.item;

import jiraiyah.tuneme.TuneMe;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ModItems
{
    public static final Item TUNER = registerItem("tuner", new TunerItem(new FabricItemSettings()));

    public static void register()
    {
        TuneMe.LOGGER.info(">>> Registering Items");
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(ModItems::addItemsToFunctionalItemGroup);
    }

    private static Item registerItem(String name, Item item)
    {
        return Registry.register(Registries.ITEM, TuneMe.identifier(name), item);
    }

    private static void addItemsToFunctionalItemGroup(FabricItemGroupEntries entries)
    {
        entries.add(TUNER);
    }
}