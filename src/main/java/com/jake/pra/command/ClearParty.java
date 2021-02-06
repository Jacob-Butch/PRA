package com.jake.pra.command;

import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.comm.CommandChatHandler;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import net.minecraft.command.*;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ClearParty extends CommandBase implements ICommand {

    private static final HashMap<UUID, Integer> confirmcp = new HashMap<>();

    public int getRequiredPermissionLevel() { return 2; }

    /* getCommandName */
    @Nonnull
    public String getName() {
        return "clearparty";
    }

    /* getCommandUsage */
    @Nonnull
    public String getUsage(@Nonnull ICommandSender sender) {
        return (TextFormatting.RED + "/clearparty [player]\n" + TextFormatting.RED + "/clearparty <slot to keep>\n" + TextFormatting.RED + "/clearparty <player> <slot to keep>");
    }

    /* getCommandAliases */
    @Nonnull
    public List<String> getAliases() {
        return Lists.newArrayList( "cp");
    }

    /* getTabCompletionOptions */
    public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args) throws CommandException {
        // Party slot #
        int slot;
        // Target player
        EntityPlayerMP player;
        if(args.length < 1) {
            player = getPlayer(server, sender, sender.getName());
            if (confirmcp.containsKey(player.getUniqueID())) {
                confirmcp.remove(player.getUniqueID());
                PlayerPartyStorage pStorage = Pixelmon.storageManager.getParty(player);
                clearAll(pStorage);
            } else {
                confirmcp.put(player.getUniqueID(), 0);
                CommandChatHandler.sendFormattedChat(sender, TextFormatting.RED, "Warning: You are about to delete all of the pokemon in your party.");
                CommandChatHandler.sendFormattedChat(sender, TextFormatting.RED, "Do '/clearparty' again to continue.");
            }
        } else if (args.length == 1) {
            if (args[0].equals("1") || args[0].equals("2") || args[0].equals("3") || args[0].equals("4") || args[0].equals("5") || args[0].equals("6")) {
                player = getPlayer(server, sender, sender.getName());
                PlayerPartyStorage pStorage = Pixelmon.storageManager.getParty(player);
                slot = Integer.parseInt(args[0]) - 1;
                clearAllButOne(pStorage, slot);
            } else {
                player = getPlayer(server, sender, args[0]);
                if(confirmcp.containsKey(player.getUniqueID())) {
                    confirmcp.remove(player.getUniqueID());
                    PlayerPartyStorage pStorage = Pixelmon.storageManager.getParty(player);
                    clearAll(pStorage);
                } else {
                   confirmcp.put(player.getUniqueID(), 0);
                   CommandChatHandler.sendFormattedChat(sender, TextFormatting.RED, "Warning: You are about to delete all of the pokemon in " + player.getName() + "'s party.");
                   CommandChatHandler.sendFormattedChat(sender, TextFormatting.RED, "Do '/clearparty " + player.getName() + "' again to continue.");
                }
            }
        } else if(args.length == 2) {
            slot = Integer.parseInt(args[1]) - 1;
            try {
                player = getPlayer(server, sender, args[0]);
            } catch (Exception ex){
                // Player not found
                CommandChatHandler.sendFormattedChat(sender, TextFormatting.RED, "Invalid name, try again.");
                return;
            }
            PlayerPartyStorage pStorage = Pixelmon.storageManager.getParty(player);
            clearAllButOne(pStorage, slot);
        } else {
            throw new WrongUsageException(this.getUsage(sender));
        }
    }

    @Nonnull
    public List<String> getTabCompletions(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args, @Nullable BlockPos targetPos) {
        if (args.length == 1) {
            return getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
        }
        return new ArrayList<>();
    }

    public boolean isUsernameIndex(@Nonnull String[] args, int i) {
        return false;
    }

    private void clearAll(PlayerPartyStorage pStorage){
        for(int i = 0; i < 6; ++i){
            pStorage.set(i, null);
        }
    }

    private void clearAllButOne(PlayerPartyStorage pStorage, int slot) {
        for(int i = 0; i < 6; ++i){
            if(i != slot){
                pStorage.set(i, null);
            }
        }
    }
}
