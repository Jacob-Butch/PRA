package com.jake.pra.command;

import com.jake.pra.command.permissions.EnumPerms;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonSpec;
import com.pixelmonmod.pixelmon.api.storage.PCStorage;
import com.pixelmonmod.pixelmon.battles.BattleRegistry;
import com.pixelmonmod.pixelmon.comm.CommandChatHandler;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.items.EnumPokeballs;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import com.google.common.collect.Lists;
import net.minecraft.command.*;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import scala.util.Random;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class RandomIV extends CommandBase implements ICommand {
    private final List<String> aliases = Lists.newArrayList("riv");

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
        return this.aliases;
    }

    /* getTabCompletionOptions */
    public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args) throws CommandException {
        if(!EnumPerms.hasPermission(EnumPerms.randomiv, sender)){
            throw new WrongUsageException("You do not have permission to use this command!");
        }
        if(args.length < 4) {
            throw new WrongUsageException(this.getUsage(sender));
        }
        int miniv, maxiv;                       //min and max iv values
        int hp, atk, def, spatk, spdef, spd;    //iv values
        String  pokemon;                        //target pokemon
        EnumSpecies name;

        pokemon = args[1];
        if(args[2].equals(args[3])) {
            hp = atk = def = spatk = spdef = spd = Integer.parseInt(args[3]);
        } else {
            miniv = Integer.parseInt(args[2]);
            maxiv = Integer.parseInt(args[3]);
            hp    = (int)(Math.random() * ((maxiv-miniv) + 1)) + miniv;
            atk   = (int)(Math.random() * ((maxiv-miniv) + 1)) + miniv;
            def   = (int)(Math.random() * ((maxiv-miniv) + 1)) + miniv;
            spatk = (int)(Math.random() * ((maxiv-miniv) + 1)) + miniv;
            spdef = (int)(Math.random() * ((maxiv-miniv) + 1)) + miniv;
            spd   = (int)(Math.random() * ((maxiv-miniv) + 1)) + miniv;
        }
        int i = args.length;
        int n = i + 6;
        String[] specs = new String[n];
        System.arraycopy(args, 0, specs, 0, args.length);
        specs[i] = ("ivhp:" + hp);
        specs[i + 1] = ("ivattack:" + atk);
        specs[i + 2] = ("ivdefence:" + def);
        specs[i + 3] = ("ivspecialattack:" + spatk);
        specs[i + 4] = ("ivspecialdefence:" + spdef);
        specs[i + 5] = ("ivspeed:" + spd);

        EntityPlayerMP player = getPlayer(server, sender, args[0]);
        if(pokemon.equals("random")) {
            int legendchance;
            java.util.Random rand = new java.util.Random();
            legendchance = rand.nextInt(100);
            if(legendchance > 0) {
                name = EnumSpecies.randomPoke();
            } else {
                int sizeleg = EnumSpecies.legendaries.size();
                int random = (new Random()).nextInt(sizeleg);
                name = EnumSpecies.getFromNameAnyCase(EnumSpecies.legendaries.get(random));
            }
            Pokemon p = Pixelmon.pokemonFactory.create(name);
            PokemonSpec spec = PokemonSpec.from(specs);
            p.setCaughtBall(EnumPokeballs.PokeBall);
            spec.apply(p);
            PlayerPartyStorage storage = Pixelmon.storageManager.getParty(player);
            if (BattleRegistry.getBattle(player) == null) {
                storage.add(p);
            } else {
                PCStorage pcStorage = Pixelmon.storageManager.getPCForPlayer(player);
                pcStorage.add(p);
            }
        } else {
            name = EnumSpecies.getFromNameAnyCase(pokemon);
            Pokemon p = Pixelmon.pokemonFactory.create(name);
            PokemonSpec spec = PokemonSpec.from(specs);
            p.setCaughtBall(EnumPokeballs.PokeBall);
            spec.apply(p);
            PlayerPartyStorage storage = Pixelmon.storageManager.getParty(player);
            if (BattleRegistry.getBattle(player) == null) {
                storage.add(p);
            } else {
                PCStorage pcStorage = Pixelmon.storageManager.getPCForPlayer(player);
                pcStorage.add(p);
            }
            CommandChatHandler.sendFormattedChat(player, TextFormatting.GREEN, "You were given a " + p.getDisplayName() + "!");
        }
    }

    @Nonnull
    public List<String> getTabCompletions(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args, @Nullable BlockPos targetPos){
        if (args.length == 1) {
            return getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
        } else if (args.length == 2){
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