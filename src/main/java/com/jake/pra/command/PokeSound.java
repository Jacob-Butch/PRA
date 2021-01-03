package com.jake.pra.command;

import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.comm.CommandChatHandler;
import net.minecraft.command.*;
import net.minecraft.util.SoundCategory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketCustomSound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;

public class PokeSound extends CommandBase implements ICommand {

    @Nonnull
    public String getName()
    {
        return "pokesound";
    }

    public int getRequiredPermissionLevel() { return 2; }

    @Nonnull
    public String getUsage(@Nonnull ICommandSender sender)
    {
        return "/ps <player> <sound> [volume] [pitch]";
    }

    @Nonnull
    public List<String> getAliases() {
        return Lists.newArrayList("ps");
    }

    public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args) throws CommandException {
        if (args.length < 2) {
            throw new WrongUsageException(this.getUsage(sender));
        } else {
            String sound = "pixelmon:pixelmon";
            if(args[1].equals("camerashutter") || args[1].equals("ultrawormhole")) {
                sound += (".item." + args[1]);
            } else {
                sound += (".block." + args[1]);
            }
            SoundCategory soundcategory = SoundCategory.AMBIENT;

            double x, y, z, volume, pitch;

            if(args.length == 3) {
                volume = parseDouble(args[2], 0.0D, 3.4028234663852886E38D);
                pitch = 1;
            } else if(args.length == 4) {
                volume = parseDouble(args[2], 0.0D, 3.4028234663852886E38D);
                pitch = parseDouble(args[3], 0.0D, 2.0D);
            } else {
                volume = 1;
                pitch = 1;
            }

            if(args[0].equals("@a")){
                for(String playerName : server.getOnlinePlayerNames()){
                    EntityPlayerMP player = getPlayer(server, sender, playerName);
                    x = player.posX;
                    y = player.posY;
                    z = player.posZ;
                    player.connection.sendPacket(new SPacketCustomSound(sound, soundcategory, x, y, z, (float)volume, (float)pitch));
                }
                if(getListOfStringsMatchingLastWord(server.getOnlinePlayerNames(), server.getOnlinePlayerNames()).contains(sender.getName())) {
                    CommandChatHandler.sendFormattedChat(sender, TextFormatting.GREEN, "Played " + args[1] + " sound to everyone");
                }
            } else {
                EntityPlayerMP player = getPlayer(server, sender, args[0]);
                x = player.posX;
                y = player.posY;
                z = player.posZ;
                player.connection.sendPacket(new SPacketCustomSound(sound, soundcategory, x, y, z, (float)volume, (float)pitch));
                if(getListOfStringsMatchingLastWord(server.getOnlinePlayerNames(), server.getOnlinePlayerNames()).contains(sender.getName())) {
                    CommandChatHandler.sendFormattedChat(sender, TextFormatting.GREEN, "Played " + args[1] + " sound to " + args[0]);
                }
            }
        }
    }

    @Nonnull
    public List<String> getTabCompletions(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args, @Nullable BlockPos targetPos) {
        if(args.length == 1) {
            List<String> players = Arrays.asList(server.getOnlinePlayerNames());
            players.add("@a");
            return getListOfStringsMatchingLastWord(args, players);
        } else if (args.length == 2) {
            List<String> sounds = new ArrayList<>();
            /* Blocks */
            sounds.add("bellring");
            sounds.add("healeractivate");
            sounds.add("pc");
            sounds.add("pokeballcapture");
            sounds.add("pokeballcapturesuccess");
            sounds.add("pokeballclose");
            sounds.add("pokeballrelease");
            sounds.add("pokelootobtained");
            /* Items */
            sounds.add("camerashutter");
            sounds.add("ultrawormhole");

            return getListOfStringsMatchingLastWord(args, sounds);
        }
        return new ArrayList<>();
    }

    public boolean isUsernameIndex(@Nonnull String[] args, int index) { return index == 2; }
}
