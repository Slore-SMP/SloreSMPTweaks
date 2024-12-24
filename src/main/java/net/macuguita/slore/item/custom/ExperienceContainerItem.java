package net.macuguita.slore.item.custom;

import net.macuguita.slore.utils.ExperienceState;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.text.DecimalFormat;
import java.util.List;

public class ExperienceContainerItem extends Item {

    private static final SoundEvent ERROR_SOUND = SoundEvents.BLOCK_NOTE_BLOCK_BASS.value();
    private static final SoundEvent SUCCESS_SOUND = SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP;

    private final int INITIAL_MAX_XP_POINTS;

    /**
     * To translate levels to points use {@link net.macuguita.slore.utils.ExperienceState#levelToPoints(float)}
     **/
    public ExperienceContainerItem(Settings settings, int maxXpPoints) {
        super(settings.maxCount(1).fireproof());
        INITIAL_MAX_XP_POINTS = maxXpPoints;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        final int oldContainedXp = getContainedXp(stack);

        useItem:
        if (!user.isSneaking()) {
            int userXp = ExperienceState.levelAndProgressToPoints(user.experienceLevel, user.experienceProgress);
            int newContainedXp = Math.min(oldContainedXp + userXp, getMaxXpPoints(stack));
            int addedXp = newContainedXp - oldContainedXp;
            if (addedXp <= 0) {
                playSound(world, user, ERROR_SOUND);
                break useItem;
            }

            setContainedXp(stack, newContainedXp);
            user.addExperience(-addedXp);
            playSound(world, user, SUCCESS_SOUND);
            createParticles(world, user);
        } else {
            if (oldContainedXp <= 0) {
                playSound(world, user, ERROR_SOUND);
                break useItem;
            }

            setContainedXp(stack, 0);
            user.addExperience(oldContainedXp);
            playSound(world, user, SUCCESS_SOUND);
            createParticles(world, user);
        }

        return TypedActionResult.pass(user.getStackInHand(hand));
    }

    private void createParticles(World world, PlayerEntity user) {
        for (int i = 0; i <= 60; i++) {
            float haX = user.getRandom().nextFloat() * (user.getRandom().nextBoolean() ? -0.75f : 0.75f);
            float haZ = user.getRandom().nextFloat() * (user.getRandom().nextBoolean() ? -0.75f : 0.75f);
            world.addParticle(ParticleTypes.COMPOSTER, user.getX() + haX, user.getY() + (i / 30d), user.getZ() + haZ, 0.01, 100, 100);
        }
    }

    private void playSound(World world, PlayerEntity user, SoundEvent sound) {
        world.playSound(user, user.getX(), user.getY(), user.getZ(), sound, SoundCategory.NEUTRAL, 1, 1);
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        return stack;
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return getContainedXp(stack) > 0;
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        if (stack.hasGlint()) {
            var toolTipPart1 = Text.translatable("itemtooltip.slore_tweaks.experience_container.part1").getString();
            var toolTipFull = Text.translatable("itemtooltip.slore_tweaks.experience_container.part2").getString();

            DecimalFormat df = new DecimalFormat("#.##");
            df.setMinimumFractionDigits(0);
            df.setMaximumFractionDigits(2);

            String stored_level = toolTipPart1.formatted(
                    df.format(ExperienceState.pointsToLevelsDecimal(getContainedXp(stack))),
                    df.format(ExperienceState.pointsToLevelsDecimal(getMaxXpPoints(stack))),
                    getContainedXp(stack) == getMaxXpPoints(stack) ? toolTipFull : ""
            ).trim();
            tooltip.add(Text.literal(stored_level).formatted(Formatting.GREEN));
        }
    }

    public int getMaxXpPoints(ItemStack stack) {
        return INITIAL_MAX_XP_POINTS;
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    public static float isFilled(ItemStack stack) {
        if (stack.getItem() instanceof ExperienceContainerItem)
            return getContainedXp(stack) > 0 ? 1f : 0f;
        return 0f;
    }

    public static void setContainedXp(ItemStack stack, int xp) {
        stack.apply(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT, comp -> comp.apply(currentNbt -> currentNbt.putInt("xp", xp)));
    }

    public static int getContainedXp(ItemStack stack) {
        if (stack.get(DataComponentTypes.CUSTOM_DATA) == null)
            return 0;

        NbtCompound nbtData = stack.get(DataComponentTypes.CUSTOM_DATA).copyNbt();
        return nbtData.contains("xp") ? nbtData.getInt("xp") : 0;
    }

    public static void removeContainedXp(ItemStack stack) {
        stack.apply(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT, comp -> comp.apply(currentNbt -> currentNbt.putInt("xp", 0)));
    }
}