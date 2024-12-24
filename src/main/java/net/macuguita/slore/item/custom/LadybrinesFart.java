package net.macuguita.slore.item.custom;

import net.macuguita.slore.sound.ModSounds;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.mob.PhantomEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Box;
import net.minecraft.entity.Entity;
import net.minecraft.text.Text;
import net.minecraft.util.TypedActionResult;

public class LadybrinesFart extends Item {

    public LadybrinesFart(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient && world instanceof ServerWorld) {
            ServerWorld serverWorld = (ServerWorld) world;
            BlockPos playerPos = user.getBlockPos();

            // Define the radius (100 blocks)
            int radius = 100;

            Vec3d corner1 = new Vec3d(playerPos.getX() - radius, playerPos.getY() - radius, playerPos.getZ() - radius);
            Vec3d corner2 = new Vec3d(playerPos.getX() + radius, playerPos.getY() + radius, playerPos.getZ() + radius);

            // Create a bounding box for the search area
            Box searchArea = new Box(corner1, corner2);

            // Find all entities within the radius
            for (Entity entity : serverWorld.getEntitiesByClass(PhantomEntity.class, searchArea, e -> true)) {
                entity.remove(Entity.RemovalReason.DISCARDED); // Remove each phantom found with a reason
            }

            // Optionally, send a message to the player
            MinecraftClient.getInstance().inGameHud.setOverlayMessage(Text.translatable("item.slore_tweaks.ladybrines_fart.message"), false);
        }

        world.playSound(user, user.getX(), user.getY(), user.getZ(), ModSounds.FART, SoundCategory.PLAYERS);

        user.getItemCooldownManager().set(this, 200);

        return TypedActionResult.success(user.getStackInHand(hand));
    }
}
