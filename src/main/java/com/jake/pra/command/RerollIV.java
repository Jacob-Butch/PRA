package com.jake.pra.command;

import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonSpec;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.comm.CommandChatHandler;
import net.minecraft.command.*;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.jake.pra.PixelmonReforgedAdditions.CONFIG.legendRerollKeepIVs;
import static com.pixelmonmod.pixelmon.enums.EnumSpecies.legendaries;

public class RerollIV extends CommandBase implements ICommand {

    private final List<String> aliases = Lists.newArrayList("rr", "rerollivs");

    /* getCommandName */
    @Nonnull
    public String getName() {
        return "rerolliv";
    }

    /* getCommandUsage */
    @Nonnull
    public String getUsage(@Nonnull ICommandSender sender) {
        return (TextFormatting.RED + "/rerolliv <player> <slot> <lowiv> <highiv>\n" + TextFormatting.RED + "/rerolliv <player> <slot>");
    }

    /* getCommandAliases */
    @Nonnull
    public List<String> getAliases() {
        return this.aliases;
    }

    /* getTabCompletionOptions */
    @SuppressWarnings("all")
    public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args) throws CommandException {
        if(args.length != 2 && args.length != 4) {
            throw new WrongUsageException(this.getUsage(sender));
        } else {
            EntityPlayerMP player;
            try {
                player = getPlayer(server, sender, args[0]);
            } catch (PlayerNotFoundException ex) {
                CommandChatHandler.sendFormattedChat(sender, TextFormatting.RED, "Invalid Name! Try again!");
                return;
            }
            int slot;
            if(args[1].equalsIgnoreCase("random")){
                slot = (int) ((Math.random() * 6) + 1);
            } else {
                try {
                    slot = Integer.parseInt(args[1]);
                } catch (NumberFormatException ex){
                    CommandChatHandler.sendFormattedChat(sender, TextFormatting.RED, "Invalid number given!");
                    return;
                }
                if(slot < 1 || slot > 6){
                    CommandChatHandler.sendFormattedChat(sender, TextFormatting.RED, "Invalid slot choice. Must be 1-6 or random");
                    return;
                }
            }
            PlayerPartyStorage partyStorage = Pixelmon.storageManager.getParty(player);
            Pokemon poke = partyStorage.get(slot - 1);
            if(poke == null){
                CommandChatHandler.sendFormattedChat(sender, TextFormatting.RED, "There is no pokemon in that slot!");
                return;
            }
            // Species of pokemon
            EnumSpecies species = poke.getSpecies();
            // Min and Max IV values
            int minIV, maxIV;
            // IV values for each stat
            int hp, atk, def, spAtk, spDef, spd;
            String hpIV, atkIV, defIV, spatkIV, spdefIV, spdIV;

            String[] specs = new String[6];
            if (args.length == 2) {
                hp = (int) (Math.random() * (32));
                atk = (int) (Math.random() * (32));
                def = (int) (Math.random() * (32));
                spAtk = (int) (Math.random() * (32));
                spDef = (int) (Math.random() * (32));
                spd = (int) (Math.random() * (32));
            }
            if (args.length == 4) {
                if (args[2].equals(args[3])) {
                    hp = atk = def = spAtk = spDef = spd = Integer.parseInt(args[3]);
                } else {
                    minIV = Integer.parseInt(args[2]);
                    maxIV = Integer.parseInt(args[3]);
                    hp = (int) (Math.random() * ((maxIV - minIV) + 1)) + minIV;
                    atk = (int) (Math.random() * ((maxIV - minIV) + 1)) + minIV;
                    def = (int) (Math.random() * ((maxIV - minIV) + 1)) + minIV;
                    spAtk = (int) (Math.random() * ((maxIV - minIV) + 1)) + minIV;
                    spDef = (int) (Math.random() * ((maxIV - minIV) + 1)) + minIV;
                    spd = (int) (Math.random() * ((maxIV - minIV) + 1)) + minIV;
                }
            } else {
                CommandChatHandler.sendFormattedChat(sender, TextFormatting.RED, "Invalid amount of arguments.");
                return;
            }
            hpIV = ("ivhp:" + hp);
            atkIV = ("ivattack:" + atk);
            defIV = ("ivdefence:" + def);
            spatkIV = ("ivspecialattack:" + spAtk);
            spdefIV = ("ivspecialdefence:" + spDef);
            spdIV = ("ivspeed:" + spd);
            if(legendaries.contains(species.name) && legendRerollKeepIVs) {
                int iv1, iv2, iv3;
                iv1 = (int) (Math.random() * ((6 - 1) + 1) + 1);
                iv2 = iv1;
                while(iv2 == iv1) {
                    iv2 = (int) (Math.random() * ((6 - 1) + 1) + 1);
                }
                iv3 = iv2;
                while(iv3 == iv2 || iv3 == iv1) {
                    iv3 = (int) (Math.random() * ((6 - 1) + 1) + 1);
                }
                if(iv1 == 1 || iv2 == 1 || iv3 == 1) hpIV = ("ivhp:31");
                if(iv1 == 2 || iv2 == 2 || iv3 == 2) atkIV = ("ivattack:31");
                if(iv1 == 3 || iv2 == 3 || iv3 == 3) defIV = ("ivdefence:31");
                if(iv1 == 4 || iv2 == 4 || iv3 == 4) spatkIV = ("ivspecialattack:31");
                if(iv1 == 5 || iv2 == 5 || iv3 == 5) spdefIV = ("ivspecialdefence:31");
                if(iv1 == 6 || iv2 == 6 || iv3 == 6) spdIV = ("ivspeed:31");
            }
            specs[0] = hpIV;
            specs[1] = atkIV;
            specs[2] = defIV;
            specs[3] = spatkIV;
            specs[4] = spdefIV;
            specs[5] = spdIV;

            partyStorage.retrieveAll();
            PokemonSpec spec = PokemonSpec.from(specs);
            spec.apply(poke);
            partyStorage.set(slot - 1, poke);

            CommandChatHandler.sendFormattedChat(sender, TextFormatting.GREEN, "Your " + spec.name + "'s IVs have been rerolled.");
        }
    }

    @Nonnull
    public List<String> getTabCompletions(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args, @Nullable BlockPos targetPos) {
        if (args.length == 1) {
            // Player Argument
            return getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
        } else if(args.length == 2) {
            // Slot argument
            return getListOfStringsMatchingLastWord(args, Arrays.asList("1", "2", "3", "4", "5", "6", "random"));
        }
        return new ArrayList<>();
    }

    public boolean isUsernameIndex(@Nonnull String[] args, int i) { return false; }
}