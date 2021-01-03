package com.jake.pra.command;

import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.storage.PCStorage;
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

import static com.pixelmonmod.pixelmon.enums.EnumSpecies.legendaries;

public class Release extends CommandBase implements ICommand {

    private static final HashMap<UUID, Integer> confirmPartyRelease = new HashMap<>();
    private static final HashMap<UUID, Integer> confirmPCRelease = new HashMap<>();

    public int getRequiredPermissionLevel() { return 2; }

    /* getCommandName */
    @Nonnull
    public String getName() {
        return "release";
    }

    /* getCommandUsage */
    @Nonnull
    public String getUsage(@Nonnull ICommandSender sender) {
        return (TextFormatting.RED + "/release [player] <party slot>\n" + TextFormatting.RED + "/release [player] <box> <slot in box>\n");
    }

    /* getCommandAliases */
    @Nonnull
    public List<String> getAliases() {
        return Lists.newArrayList( "rl");
    }

    /* getTabCompletionOptions */
    public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args) throws CommandException {
        int box, slot;
        EntityPlayerMP player;
        if((args.length < 1) || (args.length > 3)) {
            throw new WrongUsageException(this.getUsage(sender));
        } else if(args.length == 1) {
            player = getPlayer(server, sender, sender.getName());
            slot = Integer.parseInt(args[0]);
            clearPartySlot(sender, player, slot);
        } else if(args.length == 2) {
            String[] players = server.getOnlinePlayerNames();
            if (getListOfStringsMatchingLastWord(players, server.getOnlinePlayerNames()).contains(args[0])) {
                player = getPlayer(server, sender, args[0]);
                slot = Integer.parseInt(args[1]);
                clearPartySlot(sender, player, slot);
            } else {
                player = getPlayer(server, sender, sender.getName());
                box = Integer.parseInt(args[0]);
                slot = Integer.parseInt(args[1]);
                clearBoxSlot(sender, player, box, slot);
            }
        } else {
            player = getPlayer(server, sender, args[0]);
            box = Integer.parseInt(args[1]);
            slot = Integer.parseInt(args[2]);
            clearBoxSlot(sender, player, box, slot);
        }
    }

    @Nonnull
    public List<String> getTabCompletions(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args, @Nullable BlockPos targetPos){
        return args.length == 1 ? getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames()) : new ArrayList<>();
    }

    public boolean isUsernameIndex(@Nonnull String[] args, int i) { return false; }

    private void clearPartySlot(ICommandSender sender, EntityPlayerMP player, int slot) {
        PlayerPartyStorage pStorage = Pixelmon.storageManager.getParty(player);
        Pokemon pokemon = pStorage.get(slot - 1);
        if (pokemon == null) {
            CommandChatHandler.sendFormattedChat(sender, TextFormatting.RED, "There is nothing in that slot.");
            return;
        }
        if(legendaries.contains(pokemon.getDisplayName())){
            if (confirmPartyRelease.containsKey(player.getUniqueID())) {
                confirmPartyRelease.remove(player.getUniqueID());
            } else {
                confirmPartyRelease.put(player.getUniqueID(), 0);
                if(pokemon.isShiny()) {
                    CommandChatHandler.sendFormattedChat(sender, TextFormatting.RED, "Warning: The " + pokemon.getDisplayName() + " in this slot is a shiny legendary pokemon.");
                } else {
                    CommandChatHandler.sendFormattedChat(sender, TextFormatting.RED, "Warning: The " + pokemon.getDisplayName() + " in this slot is a legendary pokemon.");
                }
                CommandChatHandler.sendFormattedChat(sender, TextFormatting.RED, "Run '/release' again on it to continue.");
                return;
            }
        } else if(pokemon.isShiny()) {
            if (confirmPartyRelease.containsKey(player.getUniqueID())) {
                confirmPartyRelease.remove(player.getUniqueID());
            } else {
                confirmPartyRelease.put(player.getUniqueID(), 0);
                CommandChatHandler.sendFormattedChat(sender, TextFormatting.RED, "Warning: The " + pokemon.getDisplayName() + " in this slot is shiny.");
                CommandChatHandler.sendFormattedChat(sender, TextFormatting.RED, "Run '/release' again on it to continue.");
                return;
            }
        }
        pStorage.set(slot - 1, null);
        CommandChatHandler.sendFormattedChat(sender, TextFormatting.GREEN, pokemon.getDisplayName() + " has been released.");
    }

    private void clearBoxSlot(ICommandSender sender, EntityPlayerMP player, int box, int slot) {
        PCStorage targetStorage = Pixelmon.storageManager.getPCForPlayer(player);
        Pokemon pokemon = targetStorage.getBox(box - 1).get(slot - 1);
        if (pokemon == null) {
            CommandChatHandler.sendFormattedChat(sender, TextFormatting.RED, "There is nothing in that box's slot.");
            return;
        }
        if(legendaries.contains(pokemon.getDisplayName())){
            if (confirmPCRelease.containsKey(player.getUniqueID())) {
                confirmPCRelease.remove(player.getUniqueID());
            } else {
                confirmPCRelease.put(player.getUniqueID(), 0);
                if(pokemon.isShiny()) {
                    CommandChatHandler.sendFormattedChat(sender, TextFormatting.RED, "Warning: The " + pokemon.getDisplayName() + " in this slot is a shiny legendary pokemon.");
                } else {
                    CommandChatHandler.sendFormattedChat(sender, TextFormatting.RED, "Warning: The " + pokemon.getDisplayName() + " in this slot is a legendary pokemon.");
                }
                CommandChatHandler.sendFormattedChat(sender, TextFormatting.RED, "Run '/release' again on it to continue.");
                return;
            }
        }
        else if(pokemon.isShiny()) {
            if (confirmPCRelease.containsKey(player.getUniqueID())) {
                confirmPCRelease.remove(player.getUniqueID());
            } else {
                confirmPCRelease.put(player.getUniqueID(), 0);
                CommandChatHandler.sendFormattedChat(sender, TextFormatting.RED, "Warning: The " + pokemon.getDisplayName() + " in this slot is shiny.");
                CommandChatHandler.sendFormattedChat(sender, TextFormatting.RED, "Run '/release' again on it to continue.");
                return;
            }
        }
        targetStorage.getBox(box - 1).set(slot - 1, null);
        CommandChatHandler.sendFormattedChat(sender, TextFormatting.GREEN, pokemon.getDisplayName() + " has been released.");
    }
}

