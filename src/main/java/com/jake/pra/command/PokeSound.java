package com.jake.pra.command;

import com.google.common.collect.Lists;
import com.jake.pra.util.Utils;
import com.pixelmonmod.pixelmon.comm.CommandChatHandler;
import net.minecraft.command.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
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
            // Sound string
            String sound = "pixelmon:pixelmon";
            if(args[1].equals("camerashutter") || args[1].equals("ultrawormhole")) {
                sound += (".item." + args[1]);
            } else {
                sound += (".block." + args[1]);
            }
            // Sound volume and pitch
            double volume, pitch;
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
                // Send sound to all online players
                for(String playerName : server.getOnlinePlayerNames()){
                    Utils.sendSound(getPlayer(server, sender, playerName), sound, (float) volume, (float) pitch);
                }
                if(getListOfStringsMatchingLastWord(server.getOnlinePlayerNames(), server.getOnlinePlayerNames()).contains(sender.getName())) {
                    CommandChatHandler.sendFormattedChat(sender, TextFormatting.GREEN, "Played " + args[1] + " sound to everyone");
                }
            } else {
                // Send sound to target player
                Utils.sendSound(getPlayer(server, sender, args[0]), sound, (float) volume, (float) pitch);
                if(getListOfStringsMatchingLastWord(server.getOnlinePlayerNames(), server.getOnlinePlayerNames()).contains(sender.getName())) {
                    CommandChatHandler.sendFormattedChat(sender, TextFormatting.GREEN, "Played " + args[1] + " sound to " + args[0]);
                }
            }
        }
    }

    @Nonnull
    public List<String> getTabCompletions(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args, @Nullable BlockPos targetPos) {
        if(args.length == 1) {
            // Get player argument
            List<String> players = Arrays.asList(server.getOnlinePlayerNames());
            players.add("@a");
            return getListOfStringsMatchingLastWord(args, players);
        } else if (args.length == 2) {
            // Get sound argument
            return getListOfStringsMatchingLastWord(args, Arrays.asList("bellring", "healeractivate", "pc",
                    "pokeballcapture", "pokeballcapturesuccess", "pokeballclose", "pokeballrelease", "pokelootobtained",
                    "camerashutter" , "ultrawormhole"));
        }
        return new ArrayList<>();
    }

    public boolean isUsernameIndex(@Nonnull String[] args, int index) { return index == 2; }


}
