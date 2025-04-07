package com.macuguita.slore.mixin.secret;

import com.macuguita.slore.utils.SecretSpectator;
import net.minecraft.network.listener.ClientPlayPacketListener;
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
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerPlayerInteractionManager.class)
public class ServerPlayerInteractionManagerMixin {
    @Shadow @Final protected ServerPlayerEntity player;

    @Redirect(
            method = "changeGameMode",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/PlayerManager;sendToAll(Lnet/minecraft/network/packet/Packet;)V"
            )
    )
    private void slore$sendPackets(PlayerManager playerManager, Packet<ClientPlayPacketListener> packet) {
        if(this.player.isSpectator()) {
            for (ServerPlayerEntity serverPlayerEntity : playerManager.getPlayerList()) {
                if(SecretSpectator.canPlayerSeeSpectatorOf(serverPlayerEntity, this.player)) {
                    serverPlayerEntity.networkHandler.sendPacket(packet);
                }
                if(!serverPlayerEntity.equals(this.player) && serverPlayerEntity.isSpectator() && SecretSpectator.canPlayerSeeSpectatorOf(this.player, serverPlayerEntity)) {
                    this.player.networkHandler.sendPacket(new PlayerListS2CPacket(PlayerListS2CPacket.Action.UPDATE_GAME_MODE, serverPlayerEntity));
                }
            }
        } else {
            this.player.server.getPlayerManager().sendToAll(packet);
            if(!SecretSpectator.canSeeOtherSpectators(this.player)) {
                for (ServerPlayerEntity serverPlayerEntity : playerManager.getPlayerList()) {
                    if (this.player != serverPlayerEntity && serverPlayerEntity.isSpectator()) {
                        PlayerListS2CPacket backToSurvivalPacket = SecretSpectator.copyPacketWithModifiedEntries(
                                new PlayerListS2CPacket(PlayerListS2CPacket.Action.UPDATE_GAME_MODE, serverPlayerEntity),
                                entry -> SecretSpectator.cloneEntryWithGamemode(entry, GameMode.SURVIVAL)
                        );
                        this.player.networkHandler.sendPacket(backToSurvivalPacket);
                    }
                }
            }
        }
    }
}
