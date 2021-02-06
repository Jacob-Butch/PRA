package com.jake.pra.util;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketCustomSound;
import net.minecraft.util.SoundCategory;

public class Utils {

    // Function to send the target player the sound with target volume and pitch
    public static void sendSound(EntityPlayerMP player, String sound, float volume, float pitch){
        player.connection.sendPacket(getSoundPacket(player, sound, volume, pitch));
    }

    // Function to get a sound packet given the player, the sound, and the sound's volume and pitch
    private static SPacketCustomSound getSoundPacket(EntityPlayerMP player, String sound, float volume, float pitch){
        return new SPacketCustomSound(sound, SoundCategory.AMBIENT, player.posX, player.posY, player.posZ, volume, pitch);
    }

}
