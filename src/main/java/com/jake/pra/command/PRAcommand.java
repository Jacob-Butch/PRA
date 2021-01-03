package com.jake.pra.command;

import com.google.common.collect.Lists;
import com.jake.pra.PixelmonReforgedAdditions;
import com.pixelmonmod.pixelmon.comm.CommandChatHandler;
import net.minecraft.command.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.config.Config.Type;
import net.minecraftforge.common.config.ConfigManager;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PRAcommand extends CommandBase implements ICommand {

    /* getCommandName */
    @Nonnull
    public String getName() {
        return "pra";
    }

    /* getCommandUsage */
    @Nonnull
    public String getUsage(@Nonnull ICommandSender sender) {
        return (TextFormatting.RED + "/pra <help | setConfig>");
    }

    /* getCommandAliases */
    @Nonnull
    public List<String> getAliases() {
        return Lists.newArrayList("pra");
    }

    /* getTabCompletionOptions */
    public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args) throws CommandException {
        if(args.length < 1) {
            throw new WrongUsageException(this.getUsage(sender));
        } else if(args[0].equals("help")) {
            if(args.length == 1) {
                CommandChatHandler.sendFormattedChat(sender, TextFormatting.BLUE, "=-=-= " + TextFormatting.YELLOW + "List of PRA Commands" + TextFormatting.BLUE + " =-=-=");
                CommandChatHandler.sendFormattedChat(sender, TextFormatting.BLUE, "=    " + TextFormatting.WHITE + "-" + TextFormatting.GREEN + "/randomiv");
                CommandChatHandler.sendFormattedChat(sender, TextFormatting.BLUE, "=    " + TextFormatting.WHITE + "-" + TextFormatting.GREEN + "/rerolliv");
                CommandChatHandler.sendFormattedChat(sender, TextFormatting.BLUE, "=    " + TextFormatting.WHITE + "-" + TextFormatting.GREEN + "/clearparty");
                CommandChatHandler.sendFormattedChat(sender, TextFormatting.BLUE, "=    " + TextFormatting.WHITE + "-" + TextFormatting.GREEN + "/pokecry");
                CommandChatHandler.sendFormattedChat(sender, TextFormatting.BLUE, "=    " + TextFormatting.WHITE + "-" + TextFormatting.GREEN + "/pokesound");
                CommandChatHandler.sendFormattedChat(sender, TextFormatting.BLUE, "=    " + TextFormatting.WHITE + "-" + TextFormatting.GREEN + "/release");
                CommandChatHandler.sendFormattedChat(sender, TextFormatting.BLUE, "============================");
                CommandChatHandler.sendFormattedChat(sender, TextFormatting.YELLOW,"Do '/help <commandname>' to get usage of a command.");
            } else if(args.length == 2) {
                switch (args[1]) {
                    case "randomiv":
                        CommandChatHandler.sendFormattedChat(sender, TextFormatting.RED, new RandomIV().getUsage(sender));
                        break;
                    case "rerolliv":
                        CommandChatHandler.sendFormattedChat(sender, TextFormatting.RED, new RerollIV().getUsage(sender));
                        break;
                    case "clearparty":
                        CommandChatHandler.sendFormattedChat(sender, TextFormatting.RED, new ClearParty().getUsage(sender));
                        break;
                    case "pokecry":
                        CommandChatHandler.sendFormattedChat(sender, TextFormatting.RED, new PokeCry().getUsage(sender));
                        break;
                    case "pokesound":
                        CommandChatHandler.sendFormattedChat(sender, TextFormatting.RED, new PokeSound().getUsage(sender));
                        break;
                    case "release":
                        CommandChatHandler.sendFormattedChat(sender, TextFormatting.RED, new Release().getUsage(sender));
                        break;
                    default:
                        CommandChatHandler.sendFormattedChat(sender, TextFormatting.RED, "Invalid argument, please try again.");
                        break;
                }
            }
        } else if(args[0].equals("setConfig")) {
            if(args.length >= 2) {
                if (args[1].equals("legendRerollKeepIVs")) {
                    if(args.length == 3) {
                        if(args[2].equals("true")) {
                            new PixelmonReforgedAdditions.CONFIG(true);
                            ConfigManager.sync("praddtions", Type.INSTANCE);
                            CommandChatHandler.sendFormattedChat(sender, TextFormatting.DARK_GREEN,"legendRerollKeepIVs has been set to true");
                        } else if(args[2].equals("false")) {
                            new PixelmonReforgedAdditions.CONFIG(false);
                            ConfigManager.sync("praddtions", Type.INSTANCE);
                            CommandChatHandler.sendFormattedChat(sender, TextFormatting.DARK_GREEN,"legendRerollKeepIVs has been set to false");
                        } else {
                            throw new CommandException(TextFormatting.RED + "Invalid argument, please try again.");
                        }
                    } else {
                        throw new CommandException(TextFormatting.RED + "legendRerollKeepIVs is currently set to " + PixelmonReforgedAdditions.CONFIG.legendRerollKeepIVs);
                    }
                }
            } else {
                throw new CommandException(TextFormatting.RED + "Not enough arguments, please try again.");
            }
        } else {
            throw new CommandException(this.getUsage(sender));
        }
    }

    @Nonnull
    public List<String> getTabCompletions(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args, @Nullable BlockPos targetPos){
        if(args.length == 1) {
            return getListOfStringsMatchingLastWord(args, Arrays.asList("setConfig", "help"));
        }
        if(args.length == 2) {
            return getListOfStringsMatchingLastWord(args, Arrays.asList("randomiv", "reroll", "clearparty", "pokecry",
                    "release", "pokesound", "legendRerollKeepIVs"));
        }
        if(args.length == 3) {
            return getListOfStringsMatchingLastWord(args, Arrays.asList("true", "false"));
        }
        return new ArrayList<>();
    }

    public boolean isUsernameIndex(@Nonnull String[] args, int i) { return false; }
}
