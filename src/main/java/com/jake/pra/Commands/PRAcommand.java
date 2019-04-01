package com.jake.pra.Commands;

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
import java.util.List;

import static com.jake.pra.Info.PRA.MODID;

@SuppressWarnings("ConstantConditions")
public class PRAcommand extends CommandBase implements ICommand {
    private final List<String> aliases = Lists.newArrayList();


    public PRAcommand() {
    }

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
        return this.aliases;
    }

    /* getTabCompletionOptions */
    public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args) throws CommandException
    {
        if(args.length < 1)
        {
            throw new WrongUsageException(this.getUsage(sender));
        }
        else if(args[0].equals("help"))
        {
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
            }
            else if(args.length == 2)
            {
                switch (args[1])
                {
                    case "randomiv":
                        RandomIV riv = new RandomIV();
                        CommandChatHandler.sendFormattedChat(sender, TextFormatting.RED, riv.getUsage(sender));
                        break;
                    case "rerolliv":
                        RerollIV rr = new RerollIV();
                        CommandChatHandler.sendFormattedChat(sender, TextFormatting.RED, rr.getUsage(sender));
                        break;
                    case "clearparty":
                        ClearParty cp = new ClearParty();
                        CommandChatHandler.sendFormattedChat(sender, TextFormatting.RED, cp.getUsage(sender));
                        break;
                    case "pokecry":
                        PokeCry pc = new PokeCry();
                        CommandChatHandler.sendFormattedChat(sender, TextFormatting.RED, pc.getUsage(sender));
                        break;
                    case "pokesound":
                        PokeSound ps = new PokeSound();
                        CommandChatHandler.sendFormattedChat(sender, TextFormatting.RED, ps.getUsage(sender));
                        break;
                    case "release":
                        Release rl = new Release();
                        CommandChatHandler.sendFormattedChat(sender, TextFormatting.RED, rl.getUsage(sender));
                        break;
                    default:
                        CommandChatHandler.sendFormattedChat(sender, TextFormatting.RED, "Invalid argument, please try again.");
                        break;
                }
            }
        }
        else if(args[0].equals("setConfig"))
        {
            if(args.length >= 2)
            {
                if (args[1].equals("legendRerollKeepIVs"))
                {
                    if(args.length == 3)
                    {
                        if(args[2].equals("true"))
                        {
                            new PixelmonReforgedAdditions.CONFIG(true);
                            ConfigManager.sync(MODID, Type.INSTANCE);
                            CommandChatHandler.sendFormattedChat(sender, TextFormatting.DARK_GREEN,"legendRerollKeepIVs has been set to true");
                        }
                        else if(args[2].equals("false"))
                        {
                            new PixelmonReforgedAdditions.CONFIG(false);
                            ConfigManager.sync(MODID, Type.INSTANCE);
                            CommandChatHandler.sendFormattedChat(sender, TextFormatting.DARK_GREEN,"legendRerollKeepIVs has been set to false");
                        }
                        else
                            CommandChatHandler.sendFormattedChat(sender, TextFormatting.RED, "Invalid argument, please try again.");
                    }
                    else
                        CommandChatHandler.sendFormattedChat(sender, TextFormatting.RED, "legendRerollKeepIVs is currently set to " + PixelmonReforgedAdditions.CONFIG.legendRerollKeepIVs);
                }
            }
            else
                CommandChatHandler.sendFormattedChat(sender, TextFormatting.RED, "Not enough arguments, please try again.");
        }
        else
            CommandChatHandler.sendFormattedChat(sender, TextFormatting.RED, getUsage(sender));
    }

    @Nonnull
    public List<String> getTabCompletions(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args, @Nullable BlockPos targetPos){
        if(args.length == 1)
        {
            List<String> args1 = new ArrayList<>();
            args1.add("setConfig");
            args1.add("help");

            return getListOfStringsMatchingLastWord(args, args1);
        }
        if(args.length == 2)
        {
            List<String> args2 = new ArrayList<>();
            args2.add("randomiv");
            args2.add("reroll");
            args2.add("clearparty");
            args2.add("pokecry");
            args2.add("release");
            args2.add("pokesound");
            args2.add("legendRerollKeepIVs");

            return getListOfStringsMatchingLastWord(args, args2);
        }
        if(args.length == 3)
        {
            List<String> args3 = new ArrayList<>();
            args3.add("true");
            args3.add("false");

            return getListOfStringsMatchingLastWord(args, args3);
        }
        return null;
    }

    public boolean isUsernameIndex(@Nonnull String[] args, int i) {
        return false;
    }

}
