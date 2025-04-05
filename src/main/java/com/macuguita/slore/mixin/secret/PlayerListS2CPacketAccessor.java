package com.macuguita.slore.mixin.secret;

import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(PlayerListS2CPacket.class)
public interface PlayerListS2CPacketAccessor {

    @Mutable @Accessor("entries") void slore$setEntries(List<PlayerListS2CPacket.Entry> entries);
}
