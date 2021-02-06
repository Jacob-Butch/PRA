package com.jake.pra.command;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonSpec;
import com.pixelmonmod.pixelmon.battles.BattleRegistry;
import com.pixelmonmod.pixelmon.comm.CommandChatHandler;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import com.google.common.collect.Lists;
import net.minecraft.command.*;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomIV extends CommandBase implements ICommand {

    /* getCommandName */
    @Nonnull
    public String getName() {
        return "randomiv";
    }

    /* getCommandUsage */
    @Nonnull
    public String getUsage(@Nonnull ICommandSender sender) {
        return (TextFormatting.RED + "/randomiv <player> <pokemon> <lowiv> <highiv> [specs]");
    }

    /* getCommandAliases */
    @Nonnull
    public List<String> getAliases() {
        return Lists.newArrayList("riv");
    }

    /* getTabCompletionOptions */
    public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args) throws CommandException {
        if(args.length < 4) {
            throw new WrongUsageException(this.getUsage(sender));
        }
        // Min and Max IV values
        int minIV, maxIV;
        // IV values for each stat
        int hp, atk, def, spAtk, spDef, spd;
        // Target pokemon
        String pokemon = args[1];
        if(args[2].equals(args[3])) {
            hp = atk = def = spAtk = spDef = spd = Integer.parseInt(args[3]);
        } else {
            minIV = Integer.parseInt(args[2]);
            maxIV = Integer.parseInt(args[3]);
            hp    = (int) (Math.random() * ((maxIV - minIV) + 1)) + minIV;
            atk   = (int) (Math.random() * ((maxIV - minIV) + 1)) + minIV;
            def   = (int) (Math.random() * ((maxIV - minIV) + 1)) + minIV;
            spAtk = (int) (Math.random() * ((maxIV - minIV) + 1)) + minIV;
            spDef = (int) (Math.random() * ((maxIV - minIV) + 1)) + minIV;
            spd   = (int) (Math.random() * ((maxIV - minIV) + 1)) + minIV;
        }
        // Fill in specs
        int i = args.length;
        int n = i + 6;
        String[] specs = new String[n];
        System.arraycopy(args, 0, specs, 0, args.length);
        specs[i] = ("ivhp:" + hp);
        specs[i + 1] = ("ivattack:" + atk);
        specs[i + 2] = ("ivdefence:" + def);
        specs[i + 3] = ("ivspecialattack:" + spAtk);
        specs[i + 4] = ("ivspecialdefence:" + spDef);
        specs[i + 5] = ("ivspeed:" + spd);
        // Target player
        EntityPlayerMP player = getPlayer(server, sender, args[0]);
        // Pokemon's species
        EnumSpecies species;
        if(pokemon.equalsIgnoreCase("random")) {
            int legendChance;
            legendChance = new Random().nextInt(100);
            if(legendChance > 0) {
                species = EnumSpecies.randomPoke();
            } else {
                int random = new Random().nextInt(EnumSpecies.legendaries.size());
                species = EnumSpecies.getFromNameAnyCase(EnumSpecies.legendaries.get(random));
            }
        } else {
            species = EnumSpecies.getFromNameAnyCase(pokemon);
        }
        // Create pokemon
        Pokemon poke = Pixelmon.pokemonFactory.create(species);
        PokemonSpec spec = PokemonSpec.from(specs);
        spec.apply(poke);
        PlayerPartyStorage storage = Pixelmon.storageManager.getParty(player);
        if (BattleRegistry.getBattle(player) == null) {
            // Add pokemon to player's party
            storage.add(poke);
        } else {
            // Add pokemon to player's PC
            Pixelmon.storageManager.getPCForPlayer(player).add(poke);
        }
        CommandChatHandler.sendFormattedChat(player, TextFormatting.GREEN, "You were given a " + poke.getDisplayName() + "!");
    }

    @Nonnull
    public List<String> getTabCompletions(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args, @Nullable BlockPos targetPos){
        if (args.length == 1) {
            // Player argument
            return getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
        } else if (args.length == 2){
            // Pokemon argument
            List<String> pokes = new ArrayList<>();
            for (EnumSpecies poke : EnumSpecies.values()) {
                pokes.add(poke.name);
            }
            return getListOfStringsMatchingLastWord(args, pokes);
        }
        return new ArrayList<>();
    }

    public boolean isUsernameIndex(@Nonnull String[] args, int i) { return false; }
}