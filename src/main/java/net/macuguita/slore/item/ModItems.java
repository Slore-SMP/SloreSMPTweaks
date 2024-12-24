package net.macuguita.slore.item;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.macuguita.slore.SloreSMPTweaks;
import net.macuguita.slore.item.custom.*;
import net.macuguita.slore.utils.ExperienceState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;
import net.minecraft.item.SwordItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

public class ModItems {

    public static final Item GLASS_SWORD = registerItem("glass_sword",
            new SwordItem(ModToolMaterials.GLASS, new Item.Settings()
                    .rarity(Rarity.EPIC)
                    .attributeModifiers(SwordItem.createAttributeModifiers(ModToolMaterials.GLASS, -1, -2.4f)
                    )));

    public static final Item BLOBFISH = registerItem("blobfish", new BlobfishItem(new Item.Settings()
            .rarity(Rarity.EPIC)
            .food(ModFoodComponents.BLOBFISH)
    ));

    public static final Item BIBLE = registerItem("bible", new BibleItem(new Item.Settings()
            .rarity(Rarity.EPIC)
            .fireproof()
    ));

    public static final Item EXPERIENCE_BOTTLE = registerItem("experience_bottle", new ExperienceContainerItem(new Item.Settings()
            .rarity(Rarity.RARE), ExperienceState.levelToPoints(16)));
    public static final Item EXPERIENCE_JAR = registerItem("experience_jar", new ExperienceContainerItem(new Item.Settings()
            .rarity(Rarity.RARE), ExperienceState.levelToPoints(64)));

    public static final Item PRESENT = registerItem("present", new PresentItem(new Item.Settings()));

    public static final Item LADYBRINES_FART = registerItem("ladybrines_fart", new LadybrinesFart(new Item.Settings()));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(SloreSMPTweaks.MOD_ID, name), item);
    }

    public static void registerToVanillaItemGroups() {
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FOOD_AND_DRINK).register(content -> {
            content.addAfter(Items.TROPICAL_FISH, ModItems.BLOBFISH);
        });
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(content -> {
            content.addAfter(Items.END_CRYSTAL, ModItems.EXPERIENCE_BOTTLE);
        });
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(content -> {
            content.addAfter(ModItems.EXPERIENCE_BOTTLE, ModItems.EXPERIENCE_JAR);
        });
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(content -> {
            content.addBefore(Items.CHEST, ModItems.PRESENT);
        });
    }

    public static void registerModItems() {
        SloreSMPTweaks.LOGGER.info("Registering Mod Items for " + SloreSMPTweaks.MOD_ID);
    }
}
