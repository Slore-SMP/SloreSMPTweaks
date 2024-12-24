package net.macuguita.slore.utils;

import net.macuguita.slore.item.ModItems;
import net.macuguita.slore.item.custom.ExperienceContainerItem;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

public class ExperienceContainerModelPredicate {
    public static void registerModels() {
        registerXpContainer(ModItems.EXPERIENCE_BOTTLE);
        registerXpContainer(ModItems.EXPERIENCE_JAR);
    }

    private static void registerXpContainer(Item item) {
        ModelPredicateProviderRegistry.register(item, Identifier.of("filled"), ((stack, world, entity, seed) -> ExperienceContainerItem.isFilled(stack)));
    }
}
