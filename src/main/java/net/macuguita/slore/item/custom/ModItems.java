package net.macuguita.slore.item.custom;

import net.macuguita.slore.SloreSMPTweaks;
import net.minecraft.item.Item;
import net.minecraft.item.SwordItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {

    public static final Item GLASS_SWORD = registerItem("glass_sword",
            new SwordItem(ModToolMaterials.GLASS, new Item.Settings()
                    .attributeModifiers(SwordItem.createAttributeModifiers(ModToolMaterials.GLASS, -1, -2.4f))));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(SloreSMPTweaks.MOD_ID, name), item);
    }

    public static void registerModItems() {
        SloreSMPTweaks.LOGGER.info("Registering Mod Items for " + SloreSMPTweaks.MOD_ID);
    }
}
