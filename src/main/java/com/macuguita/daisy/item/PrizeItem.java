package com.macuguita.daisy.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.List;

public class PrizeItem extends Item {

    private final Identifier lootTableId;

    public PrizeItem(Settings settings, Identifier lootTableId) {
        super(settings);
        this.lootTableId = lootTableId;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient && world instanceof ServerWorld serverWorld) {
            LootTable lootTable = serverWorld.getServer().getLootManager().getLootTable(lootTableId);

            // Build the loot context parameter set
            LootContextParameterSet parameters = new LootContextParameterSet.Builder(serverWorld)
                    .add(LootContextParameters.ORIGIN, user.getPos())
                    .add(LootContextParameters.THIS_ENTITY, user)
                    .build(LootContextTypes.GIFT);

            // Generate loot from the table using the proper method
            List<ItemStack> loot = lootTable.generateLoot(parameters);

            for (ItemStack stack : loot) {
                if (!user.getInventory().insertStack(stack)) {
                    user.dropItem(stack, true);
                }
            }

            user.getStackInHand(hand).decrement(1); // consume item
            return TypedActionResult.success(user.getStackInHand(hand), true);
        }

        return TypedActionResult.pass(user.getStackInHand(hand));
    }
}
