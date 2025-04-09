package com.macuguita.slore.mixin.secret;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.macuguita.slore.utils.SecretSpectator;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.world.GameMode;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ServerPlayerInteractionManager.class)
public class ServerPlayerInteractionManagerMixin {

    @Shadow @Final protected ServerPlayerEntity player;

    @WrapOperation(
            method = "changeGameMode",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/PlayerManager;sendToAll(Lnet/minecraft/network/packet/Packet;)V"
            )
    )
    private void slore$wrapSendGameModeChange(PlayerManager playerManager, Packet<?> packet, Operation<Void> original) {
        if (this.player.isSpectator()) {
            for (ServerPlayerEntity other : playerManager.getPlayerList()) {
                if (SecretSpectator.canPlayerSeeSpectatorOf(other, this.player)) {
                    other.networkHandler.sendPacket(packet);
                }
                if (!other.equals(this.player) && other.isSpectator() && SecretSpectator.canPlayerSeeSpectatorOf(this.player, other)) {
                    this.player.networkHandler.sendPacket(new PlayerListS2CPacket(PlayerListS2CPacket.Action.UPDATE_GAME_MODE, other));
                }
            }
        } else {
            original.call(playerManager, packet);

            if (!SecretSpectator.canSeeOtherSpectators(this.player)) {
                for (ServerPlayerEntity other : playerManager.getPlayerList()) {
                    if (!other.equals(this.player) && other.isSpectator()) {
                        PlayerListS2CPacket fakePacket = SecretSpectator.copyPacketWithModifiedEntries(
                                new PlayerListS2CPacket(PlayerListS2CPacket.Action.UPDATE_GAME_MODE, other),
                                entry -> SecretSpectator.cloneEntryWithGamemode(entry, GameMode.SURVIVAL)
                        );
                        this.player.networkHandler.sendPacket(fakePacket);
                    }
                }
            }
        }
    }
}

