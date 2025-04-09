package com.macuguita.slore.mixin.secret;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.macuguita.slore.utils.SecretSpectator;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.GameMode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PlayerManager.class)
public class PlayerManagerMixin {

    @WrapOperation(
            method = "onPlayerConnect",
            at = @At(
                    value = "INVOKE",
                    ordinal = 8,
                    target = "Lnet/minecraft/server/network/ServerPlayNetworkHandler;sendPacket(Lnet/minecraft/network/packet/Packet;)V"
            )
    )
    private void slore$wrapSelfSendPacket(ServerPlayNetworkHandler instance, Packet<?> packet,
                                          Operation<Void> original) {
        if (packet instanceof PlayerListS2CPacket listPacket) {
            ServerPlayerEntity player = instance.getPlayer();
            if (!SecretSpectator.canSeeOtherSpectators(player)) {
                listPacket = SecretSpectator.copyPacketWithModifiedEntries(
                        listPacket, entry -> SecretSpectator.cloneEntryWithGamemode(entry, GameMode.SURVIVAL)
                );
            }
            original.call(instance, listPacket);
        } else {
            original.call(instance, packet);
        }
    }

    @WrapOperation(
            method = "onPlayerConnect",
            at = @At(
                    value = "INVOKE",
                    ordinal = 0,
                    target = "Lnet/minecraft/server/PlayerManager;sendToAll(Lnet/minecraft/network/packet/Packet;)V"
            )
    )
    private void slore$wrapBroadcastPacket(PlayerManager playerManager, Packet<?> packet,
                                           Operation<Void> original) {
        if (packet instanceof PlayerListS2CPacket listPacket) {
            PlayerListS2CPacket.Entry entry = listPacket.getEntries().get(0);
            ServerPlayerEntity player = playerManager.getPlayer(entry.profileId());
            if (player != null && player.isSpectator()) {
                PlayerListS2CPacket fakePacket = SecretSpectator.copyPacketWithModifiedEntries(
                        listPacket, entry1 -> SecretSpectator.cloneEntryWithGamemode(entry1, GameMode.SURVIVAL)
                );
                for (ServerPlayerEntity serverPlayerEntity : playerManager.getPlayerList()) {
                    Packet<?> toSend = SecretSpectator.canPlayerSeeSpectatorOf(serverPlayerEntity, player)
                            ? packet
                            : fakePacket;
                    serverPlayerEntity.networkHandler.sendPacket(toSend);
                }
                return;
            }
        }

        original.call(playerManager, packet);
    }
}
