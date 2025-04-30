/*
 * Copyright (c) 2025 macuguita. All Rights Reserved.
 */

package com.macuguita.daisy.homestpa;

import com.macuguita.daisy.components.DaisyComponents;
import com.macuguita.daisy.components.HomesComponent;
import com.macuguita.daisy.components.WarpsComponent;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class HomesTpaCommands {
    private static final Map<ServerPlayerEntity, ServerPlayerEntity> pendingRequestsHere = new HashMap<>();
    private static final Map<ServerPlayerEntity, ServerPlayerEntity> pendingRequestsTo = new HashMap<>();
    private static final Map<ServerPlayerEntity, Long> lastRequestTimes = new HashMap<>();

    public static void init() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            registerCommands(dispatcher);
        });
    }

    private static void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("sethome")
                .requires(source -> source.hasPermissionLevel(0))
                .executes(context -> {
                    ServerCommandSource source = context.getSource();
                    ServerPlayerEntity player = source.getPlayer();
                    if (player != null) {
                        HomesComponent homesComponent = DaisyComponents.HOMES_COMPONENT.get(player);
                        int homeCount = homesComponent.getAllHomes().size();
                        int maxHomes = homesComponent.getMaxHomes();

                        if (homeCount < maxHomes) {
                            RegistryKey<World> dimension = player.getWorld().getRegistryKey();
                            if (homesComponent.getHome("home") == null) {
                                homesComponent.addHome("home", player.getBlockPos(), dimension);
                                source.sendFeedback(() -> Text.translatable("commands.daisy.sethome.success"), false);
                                return 1;
                            } else {
                                source.sendFeedback(() -> Text.translatable("commands.daisy.sethome.fail.exists").formatted(Formatting.RED), false);
                                return 0;
                            }
                        } else {
                            source.sendFeedback(() -> Text.translatable("commands.daisy.sethome.fail.maxhomes", maxHomes).formatted(Formatting.RED), false);
                            return 0;
                        }
                    }
                    return 0;
                })
                .then(CommandManager.argument("name", StringArgumentType.string())
                        .executes(context -> {
                            String name = StringArgumentType.getString(context, "name");
                            ServerCommandSource source = context.getSource();
                            ServerPlayerEntity player = source.getPlayer();
                            if (player != null) {
                                HomesComponent homesComponent = DaisyComponents.HOMES_COMPONENT.get(player);
                                int homeCount = homesComponent.getAllHomes().size();
                                int maxHomes = homesComponent.getMaxHomes();

                                if (homeCount < maxHomes) {
                                    RegistryKey<World> dimension = player.getWorld().getRegistryKey();
                                    if (homesComponent.getHome(name) == null) {
                                        homesComponent.addHome(name, player.getBlockPos(), dimension);
                                        source.sendFeedback(() -> Text.translatable("commands.daisy.sethome.success", name), false);
                                        return 1;
                                    } else {
                                        source.sendFeedback(() -> Text.translatable("commands.daisy.sethome.fail.exists", name).formatted(Formatting.RED), false);
                                        return 0;
                                    }
                                } else {
                                    source.sendFeedback(() -> Text.translatable("commands.daisy.sethome.fail.maxhomes", maxHomes).formatted(Formatting.RED), false);
                                    return 0;
                                }
                            }
                            return 0;
                        })));

        dispatcher.register(CommandManager.literal("home")
                .requires(source -> source.hasPermissionLevel(0))
                .executes(context -> {
                    ServerCommandSource source = context.getSource();
                    ServerPlayerEntity player = source.getPlayer();
                    if (player != null && DaisyComponents.HOMES_COMPONENT.get(player).getHome("home") != null) {
                        HomeLocation home = DaisyComponents.HOMES_COMPONENT.get(player).getHome("home");
                        player.teleport(source.getServer().getWorld(home.getDimension()), (double) home.getPosition().getX(), (double) home.getPosition().getY(), (double) home.getPosition().getZ(), player.getYaw(), player.getPitch());
                        source.sendFeedback(() -> Text.translatable("commands.daisy.home.success"), false);
                        return 1;
                    } else {
                        source.sendFeedback(() -> Text.translatable("commands.daisy.home.fail").formatted(Formatting.RED), false);
                        return 0;
                    }
                })
                .then(CommandManager.argument("name", StringArgumentType.string())
                        .suggests(new HomeSuggestionProvider())
                        .executes(context -> {
                            String name = StringArgumentType.getString(context, "name");
                            ServerCommandSource source = context.getSource();
                            ServerPlayerEntity player = source.getPlayer();
                            if (player != null && DaisyComponents.HOMES_COMPONENT.get(player).getHome(name) != null) {
                                HomeLocation home = DaisyComponents.HOMES_COMPONENT.get(player).getHome(name);
                                player.teleport(source.getServer().getWorld(home.getDimension()), (double) home.getPosition().getX(), (double) home.getPosition().getY(), (double) home.getPosition().getZ(), player.getYaw(), player.getPitch());
                                source.sendFeedback(() -> Text.translatable("commands.daisy.home.success.name", name), false);
                                return 1;
                            } else {
                                source.sendFeedback(() -> Text.translatable("commands.daisy.home.fail").formatted(Formatting.RED), false);
                                return 0;
                            }
                        })));
        dispatcher.register(CommandManager.literal("homes")
                .requires(source -> source.hasPermissionLevel(0))
                .executes(context -> {
                    ServerPlayerEntity player = context.getSource().getPlayer();
                    if (player != null) {
                        Set<String> homeNames = DaisyComponents.HOMES_COMPONENT.get(player).getAllHomes().keySet();
                        if (homeNames.isEmpty()) {
                            context.getSource().sendFeedback(() -> Text.translatable("commands.daisy.homes.fail").formatted(Formatting.RED), false);
                        } else {
                            String homesList = String.join(", ", homeNames);
                            context.getSource().sendFeedback(() -> Text.translatable("commands.daisy.homes.success", homesList), false);
                        }
                        return 1;
                    }
                    return 0;
                }));
        dispatcher.register(CommandManager.literal("delhome")
                .requires(source -> source.hasPermissionLevel(0))
                .executes(context -> {
                    ServerCommandSource source = context.getSource();
                    source.sendFeedback(() -> Text.translatable("commands.daisy.delhome.fail").formatted(Formatting.RED), false);
                    return 0;
                })
                .then(CommandManager.argument("name", StringArgumentType.string())
                        .suggests(new HomeSuggestionProvider())
                        .executes(context -> {
                            String name = StringArgumentType.getString(context, "name");
                            ServerCommandSource source = context.getSource();
                            ServerPlayerEntity player = source.getPlayer();
                            if (player != null && DaisyComponents.HOMES_COMPONENT.get(player).getHome(name) != null) {
                                DaisyComponents.HOMES_COMPONENT.get(player).removeHome(name);
                                source.sendFeedback(() -> Text.translatable("commands.daisy.delhome.success", name), false);
                                return 1;
                            } else {
                                source.sendFeedback(() -> Text.translatable("commands.daisy.delhome.fail").formatted(Formatting.RED), false);
                                return 0;
                            }
                        })));
        dispatcher.register(CommandManager.literal("setmaxhomes")
                .requires(source -> source.hasPermissionLevel(2))
                .then(CommandManager.argument("player", EntityArgumentType.player())
                        .then(CommandManager.argument("number", IntegerArgumentType.integer(0))
                                .executes(context -> {
                                    ServerCommandSource source = context.getSource();
                                    ServerPlayerEntity targetPlayer = EntityArgumentType.getPlayer(context, "player");
                                    int maxHomes = IntegerArgumentType.getInteger(context, "number");

                                    if (targetPlayer == null) {
                                        source.sendFeedback(() -> Text.translatable("commands.daisy.setmaxhomes.fail").formatted(Formatting.RED), false);
                                        return 0;
                                    }
                                    HomesComponent homesComponent = DaisyComponents.HOMES_COMPONENT.get(targetPlayer);

                                    homesComponent.setMaxHomes(maxHomes);

                                    source.sendFeedback(() -> Text.translatable("commands.daisy.setmaxhomes.success", targetPlayer.getName().getString(), maxHomes), false);
                                    return 1;
                                }))));

        dispatcher.register(CommandManager.literal("tpa")
                .then(CommandManager.argument("target", EntityArgumentType.player())
                        .executes(context -> {
                            ServerCommandSource source = context.getSource();
                            ServerPlayerEntity sender = source.getPlayer();
                            ServerPlayerEntity target = EntityArgumentType.getPlayer(context, "target");

                            if (sender == null || target == null) return 0;
                            if (sender.equals(target)) {
                                source.sendFeedback(() -> Text.translatable("commands.tpa.fail.self").formatted(Formatting.RED), false);
                                return 0;
                            }
                            if (!canSendRequest(sender)) {
                                source.sendFeedback(() -> Text.translatable("commands.tpa.fail.cooldown").formatted(Formatting.RED), false);
                                return 0;
                            }

                            pendingRequestsTo.put(sender, target);
                            lastRequestTimes.put(sender, System.currentTimeMillis());

                            source.sendFeedback(() -> Text.translatable("commands.tpa.success.sent", target.getName().getString()), false);
                            target.sendMessage(
                                    Text.translatable("commands.tpa.request", sender.getName().getString())
                                            .styled(style -> style
                                                    .withClickEvent(new ClickEvent(
                                                            ClickEvent.Action.RUN_COMMAND,
                                                            "/tpaccept " + sender.getName().getString()))
                                                    .withColor(Formatting.GREEN)
                                                    .withHoverEvent(new HoverEvent(
                                                            HoverEvent.Action.SHOW_TEXT,
                                                            Text.translatable("commands.tpa.hover")
                                                    ))
                                            ),
                                    false
                            );
                            return 1;
                        })));
        dispatcher.register(CommandManager.literal("tpahere")
                .then(CommandManager.argument("target", EntityArgumentType.player())
                        .executes(context -> {
                            ServerCommandSource source = context.getSource();
                            ServerPlayerEntity sender = source.getPlayer();
                            ServerPlayerEntity target = EntityArgumentType.getPlayer(context, "target");

                            if (sender == null || target == null) return 0;
                            if (sender.equals(target)) {
                                source.sendFeedback(() -> Text.translatable("commands.tpahere.fail.self").formatted(Formatting.RED), false);
                                return 0;
                            }
                            if (!canSendRequest(sender)) {
                                source.sendFeedback(() -> Text.translatable("commands.tpahere.fail.cooldown").formatted(Formatting.RED), false);
                                return 0;
                            }

                            pendingRequestsHere.put(target, sender);
                            lastRequestTimes.put(sender, System.currentTimeMillis());

                            source.sendFeedback(() -> Text.translatable("commands.tpahere.success.sent", target.getName().getString()), false);
                            target.sendMessage(
                                    Text.translatable("commands.tpahere.request", sender.getName().getString())
                                            .styled(style -> style
                                                    .withClickEvent(new ClickEvent(
                                                            ClickEvent.Action.RUN_COMMAND,
                                                            "/tpaccept " + sender.getName().getString()))
                                                    .withColor(Formatting.GREEN)
                                                    .withHoverEvent(new HoverEvent(
                                                            HoverEvent.Action.SHOW_TEXT,
                                                            Text.translatable("commands.tpahere.hover")
                                                    ))
                                            ),
                                    false
                            );
                            return 1;
                        })));
        dispatcher.register(CommandManager.literal("tpaccept")
                .then(CommandManager.argument("requester", EntityArgumentType.player())
                        .executes(context -> {
                            ServerCommandSource source = context.getSource();
                            ServerPlayerEntity acceptor = source.getPlayer();
                            ServerPlayerEntity requester = EntityArgumentType.getPlayer(context, "requester");

                            if (acceptor == null || requester == null) return 0;

                            if (pendingRequestsTo.containsKey(requester) &&
                                    pendingRequestsTo.get(requester).equals(acceptor)) {

                                requester.teleport(
                                        acceptor.getServerWorld(),
                                        acceptor.getX(),
                                        acceptor.getY(),
                                        acceptor.getZ(),
                                        acceptor.getYaw(),
                                        acceptor.getPitch()
                                );

                                source.sendFeedback(() -> Text.translatable("commands.tpaccept.success.tpa", requester.getName().getString()), false);
                                requester.sendMessage(Text.translatable("commands.tpaccept.success.requester.tpa", acceptor.getName().getString()), false);

                                pendingRequestsTo.remove(requester);
                                return 1;
                            }

                            if (pendingRequestsHere.containsKey(acceptor) &&
                                    pendingRequestsHere.get(acceptor).equals(requester)) {

                                acceptor.teleport(
                                        requester.getServerWorld(),
                                        requester.getX(),
                                        requester.getY(),
                                        requester.getZ(),
                                        requester.getYaw(),
                                        requester.getPitch()
                                );

                                source.sendFeedback(() -> Text.translatable("commands.tpaccept.success.tpahere", requester.getName().getString()), false);
                                requester.sendMessage(Text.translatable("commands.tpaccept.success.requester.tpahere", acceptor.getName().getString()), false);

                                pendingRequestsHere.remove(acceptor);
                                return 1;
                            }

                            source.sendFeedback(() -> Text.translatable("commands.tpaccept.fail.noRequest", requester.getName().getString())
                                    .formatted(Formatting.RED), false);
                            return 0;
                        }))
                .executes(context -> {
                    ServerCommandSource source = context.getSource();
                    ServerPlayerEntity player = source.getPlayer();

                    if (player == null) return 0;

                    MutableText message = Text.translatable("commands.tpaccept.pending.header");
                    boolean hasRequests = false;

                    for (Map.Entry<ServerPlayerEntity, ServerPlayerEntity> entry : pendingRequestsTo.entrySet()) {
                        if (entry.getValue().equals(player)) {
                            hasRequests = true;
                            message.append("\n")
                                    .append(Text.translatable("commands.tpaccept.pending.tpa", entry.getKey().getName().getString())
                                            .styled(style -> style
                                                    .withClickEvent(new ClickEvent(
                                                            ClickEvent.Action.RUN_COMMAND,
                                                            "/tpaccept " + entry.getKey().getName().getString()))
                                                    .withColor(Formatting.GREEN)
                                                    .withHoverEvent(new HoverEvent(
                                                            HoverEvent.Action.SHOW_TEXT,
                                                            Text.translatable("commands.tpaccept.hover.tpa")
                                                    ))));
                        }
                    }

                    if (pendingRequestsHere.containsKey(player)) {
                        hasRequests = true;
                        ServerPlayerEntity requester = pendingRequestsHere.get(player);
                        message.append("\n")
                                .append(Text.translatable("commands.tpaccept.pending.tpahere", requester.getName().getString())
                                        .styled(style -> style
                                                .withClickEvent(new ClickEvent(
                                                        ClickEvent.Action.RUN_COMMAND,
                                                        "/tpaccept " + requester.getName().getString()))
                                                .withColor(Formatting.GREEN)
                                                .withHoverEvent(new HoverEvent(
                                                        HoverEvent.Action.SHOW_TEXT,
                                                        Text.translatable("commands.tpaccept.hover.tpahere")
                                                ))));
                    }

                    if (hasRequests) {
                        source.sendFeedback(() -> message, false);
                        return 1;
                    }

                    source.sendFeedback(() -> Text.translatable("commands.tpaccept.fail.noRequests").formatted(Formatting.RED), false);
                    return 0;
                }));

        dispatcher.register(CommandManager.literal("spawn")
                .requires(source -> source.hasPermissionLevel(0))
                .executes(context -> {
                    ServerCommandSource source = context.getSource();
                    ServerPlayerEntity player = source.getPlayer();
                    if (player != null) {
                        ServerWorld overworld = Objects.requireNonNull(player.getServer()).getOverworld();
                        Vec3d spawnPos = Vec3d.ofBottomCenter(overworld.getSpawnPos());

                        player.teleport(overworld, spawnPos.x, spawnPos.y, spawnPos.z, player.getYaw(), player.getPitch());

                        return 1;
                    } else {
                        return 0;
                    }
                }));

        dispatcher.register(CommandManager.literal("addwarp")
                .requires(source -> source.hasPermissionLevel(2))
                .then(CommandManager.argument("name", StringArgumentType.string())
                        .executes(context -> {
                            String name = StringArgumentType.getString(context, "name");
                            ServerCommandSource source = context.getSource();
                            ServerPlayerEntity player = source.getPlayer();
                            if (player != null) {
                                WarpsComponent warpsComponent = DaisyComponents.WARPS_COMPONENT.get(source.getServer().getScoreboard());

                                RegistryKey<World> dimension = player.getWorld().getRegistryKey();
                                if (warpsComponent.getWarp(name) == null) {
                                    warpsComponent.addWarp(name, player.getBlockPos(), dimension);
                                    source.sendFeedback(() -> Text.translatable("commands.daisy.addwarp.success", name), false);
                                    return 1;
                                } else {
                                    source.sendFeedback(() -> Text.translatable("commands.daisy.addwarp.fail.exists", name).formatted(Formatting.RED), false);
                                    return 0;
                                }
                            }
                            return 0;
                        })));
        dispatcher.register(CommandManager.literal("removewarp")
                .requires(source -> source.hasPermissionLevel(2))
                .then(CommandManager.argument("name", StringArgumentType.string())
                        .suggests(new WarpSuggestionProvider())
                        .executes(context -> {
                            String name = StringArgumentType.getString(context, "name");
                            ServerCommandSource source = context.getSource();
                            ServerPlayerEntity player = source.getPlayer();
                            if (player != null) {
                                WarpsComponent warpsComponent = DaisyComponents.WARPS_COMPONENT.get(source.getServer().getScoreboard());

                                if (warpsComponent.getWarp(name) != null) {
                                    warpsComponent.removeWarp(name);
                                    source.sendFeedback(() -> Text.translatable("commands.daisy.removewarp.success", name), false);
                                    return 1;
                                } else {
                                    source.sendFeedback(() -> Text.translatable("commands.daisy.removewarp.fail.notexists", name).formatted(Formatting.RED), false);
                                    return 0;
                                }
                            }
                            return 0;
                        })));
        dispatcher.register(CommandManager.literal("warp")
                .requires(source -> source.hasPermissionLevel(0))
                .then(CommandManager.argument("name", StringArgumentType.string())
                        .suggests(new WarpSuggestionProvider())
                        .executes(context -> {
                            String name = StringArgumentType.getString(context, "name");
                            ServerCommandSource source = context.getSource();
                            ServerPlayerEntity player = source.getPlayer();

                            WarpsComponent warpsComponent = DaisyComponents.WARPS_COMPONENT.get(source.getServer().getScoreboard());
                            if (player != null && warpsComponent.getWarp(name) != null) {
                                HomeLocation warp = warpsComponent.getWarp(name);
                                player.teleport(source.getServer().getWorld(warp.getDimension()), (double) warp.getPosition().getX(), (double) warp.getPosition().getY(), (double) warp.getPosition().getZ(), player.getYaw(), player.getPitch());
                                source.sendFeedback(() -> Text.translatable("commands.daisy.warp.success", name), false);
                                return 1;
                            } else {
                                source.sendFeedback(() -> Text.translatable("commands.daisy.warp.fail").formatted(Formatting.RED), false);
                                return 0;
                            }
                        })));
    }

    private static boolean canSendRequest(ServerPlayerEntity player) {
        long currentTime = System.currentTimeMillis();
        return !lastRequestTimes.containsKey(player) ||
                currentTime - lastRequestTimes.get(player) >= 60 * 1000;
    }
}
