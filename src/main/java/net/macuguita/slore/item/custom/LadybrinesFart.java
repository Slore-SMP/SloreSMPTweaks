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
        if (!world.isClient()) {
            // Server-side logic (removing phantoms)
            if (world instanceof ServerWorld serverWorld) {
                BlockPos playerPos = user.getBlockPos();
                int radius = 100;
                Box searchArea = new Box(
                    new Vec3d(playerPos.getX() - radius, playerPos.getY() - radius, playerPos.getZ() - radius),
                    new Vec3d(playerPos.getX() + radius, playerPos.getY() + radius, playerPos.getZ() + radius)
                );

                for (Entity entity : serverWorld.getEntitiesByClass(PhantomEntity.class, searchArea, e -> true)) {
                    entity.remove(Entity.RemovalReason.DISCARDED);
                }
            }
        } else {
            // Client-side logic (overlay message)
            MinecraftClient.getInstance().inGameHud.setOverlayMessage(
                Text.translatable("item.slore_tweaks.ladybrines_fart.message"), 
                false
            );
        }

        // Play sound (works on both sides)
        world.playSound(user, user.getX(), user.getY(), user.getZ(), ModSounds.FART, SoundCategory.PLAYERS, 1.0f, 1.0f);

        user.getItemCooldownManager().set(this, 200);
        return TypedActionResult.success(user.getStackInHand(hand));
    }
}
