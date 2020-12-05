package com.jake.pra.command;

import com.google.common.collect.Lists;
import com.jake.pra.command.permissions.EnumPerms;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.storage.PCBox;
import com.pixelmonmod.pixelmon.api.storage.PCStorage;
import com.pixelmonmod.pixelmon.comm.CommandChatHandler;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
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

public class ClearBox extends CommandBase implements ICommand
{
    private static final HashMap<UUID, Integer> confirmClearBox = new HashMap<>();
    private final List<String> aliases = Lists.newArrayList( "clearb");

    public int getRequiredPermissionLevel() { return 2; }

    /* getCommandName */
    @Nonnull
    public String getName() {
        return "clearbox";
    }

    /* getCommandUsage */
    @Nonnull
    public String getUsage(@Nonnull ICommandSender sender) {
        return (TextFormatting.RED + "/clearbox <box>\n" + TextFormatting.RED + "/clearbox <player> <box>");
    }

    /* getCommandAliases */
    @Nonnull
    public List<String> getAliases() {
        return this.aliases;
    }

    /* getTabCompletionOptions */
    public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args) throws CommandException {
        if(!EnumPerms.hasPermission(EnumPerms.clearbox, sender)){
            throw new WrongUsageException("You do not have permission to use this command!");
        }
        int box;
        EntityPlayerMP player;
        PCStorage boxStorage;
        ArrayList<String> players = Lists.newArrayList(server.getOnlinePlayerNames());
         if (args.length == 1) {
            if (isBox(args[0])) {
                player = getPlayer(server, sender, sender.getName());
                boxStorage = Pixelmon.storageManager.getPCForPlayer(player);
                box = Integer.parseInt(args[0]) - 1;
            } else {
                getUsage(sender);
                return;
            }
        } else if(args.length == 2) {
            box = Integer.parseInt(args[1]) - 1;
            if(!players.contains(args[0])){
                CommandChatHandler.sendFormattedChat(sender, TextFormatting.RED, "Invalid name, try again.");
                return;
            }
            player = getPlayer(server, sender, args[0]);
            boxStorage = Pixelmon.storageManager.getPCForPlayer(player);
        } else {
             throw new WrongUsageException(this.getUsage(sender));
        }
        if (confirmClearBox.containsKey(player.getUniqueID())) {
            confirmClearBox.remove(player.getUniqueID());
        } else {
            confirmClearBox.put(player.getUniqueID(), 0);
            CommandChatHandler.sendFormattedChat(sender, TextFormatting.RED, "Warning: This will delete all of the pokemon in box " + args[0] + " permanently!");
            CommandChatHandler.sendFormattedChat(sender, TextFormatting.RED, "Run '/release' again on it to continue.");
            return;

        }
        clearBox(boxStorage, box);
        if(args.length == 1) {
            CommandChatHandler.sendFormattedChat(sender, TextFormatting.DARK_AQUA, "Box " + args[0] + " has been cleared!");
        }
        if(args.length == 2) {
            CommandChatHandler.sendFormattedChat(sender, TextFormatting.DARK_AQUA, player.getName() + "'s box " + args[1] + " has been cleared!");
        }

    }

    @Nonnull
    public List<String> getTabCompletions(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args, @Nullable BlockPos targetPos){
        if(args.length == 1) {
            return getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
        }
        return new ArrayList<>();
    }

    public boolean isUsernameIndex(@Nonnull String[] args, int i) {
        return false;
    }

    private static boolean isBox(String arg) {
        int box = Integer.parseInt(arg) - 1;
        for(int i = 0; i < PixelmonConfig.computerBoxes; ++i) {
            if (box == i) { return true; }
        }
        return false;
    }

    private static void clearBox(PCStorage boxStorage, int boxNumber) {
        PCBox box = boxStorage.getBox(boxNumber);
        for(int i = 0; i < 30; ++i) {
            if (box.get(i) != null) { box.set(i, null); }
        }
    }
}
