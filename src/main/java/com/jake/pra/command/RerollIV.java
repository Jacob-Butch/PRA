package com.jake.pra.command;

import com.google.common.collect.Lists;
import com.jake.pra.command.permissions.EnumPerms;
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
        if(!EnumPerms.hasPermission(EnumPerms.reroll, sender)){
            throw new WrongUsageException("You do not have permission to use this command!");
        }
        if(args.length != 2 && args.length != 4) {
            throw new WrongUsageException(this.getUsage(sender));
        } else {
            try {
                EntityPlayerMP player = getPlayer(server, sender, args[0]);
                PlayerPartyStorage partyStorage = Pixelmon.storageManager.getParty(player);
                int minIV, maxIV;                       //min and max iv values
                int hp, atk, def, spatk, spdef, spd;    //iv values
                String hpiv, atkiv, defiv, spatkiv, spdefiv, spdiv;
                String slot;                   //target player and target slot
                int slotInt;                            //integer for slot
                Pokemon poke;
                EnumSpecies pokemon;

                slot = args[1];
                if (!slot.equals("1") && !slot.equals("2") && !slot.equals("3") && !slot.equals("4") && !slot.equals("5") && !slot.equals("6") && !slot.toLowerCase().equals("random")) {
                    CommandChatHandler.sendFormattedChat(sender, TextFormatting.RED, "Invalid slot choice. Must be 1-6 or random");
                    return;
                }

                if (!slot.equals("1") && !slot.equals("2") && !slot.equals("3") && !slot.equals("4") && !slot.equals("5") && !slot.equals("6")) {
                    slotInt = (int) (Math.random() * ((6 - 1) + 1) + 1);
                    poke = partyStorage.get(slotInt - 1);
                }
                else {
                    slotInt = Integer.parseInt(slot);
                    poke = partyStorage.get(slotInt - 1);
                }
                assert poke != null;
                pokemon = poke.getSpecies();
                String[] specs = new String[6];
                if (args.length == 2) {
                    hp = (int) (Math.random() * (32));
                    atk = (int) (Math.random() * (32));
                    def = (int) (Math.random() * (32));
                    spatk = (int) (Math.random() * (32));
                    spdef = (int) (Math.random() * (32));
                    spd = (int) (Math.random() * (32));
                }
                if (args.length == 4) {
                    if (args[2].equals(args[3])) {
                        hp = atk = def = spatk = spdef = spd = Integer.parseInt(args[3]);
                    } else {
                        minIV = Integer.parseInt(args[2]);
                        maxIV = Integer.parseInt(args[3]);
                        hp = (int) (Math.random() * ((maxIV - minIV) + 1)) + minIV;
                        atk = (int) (Math.random() * ((maxIV - minIV) + 1)) + minIV;
                        def = (int) (Math.random() * ((maxIV - minIV) + 1)) + minIV;
                        spatk = (int) (Math.random() * ((maxIV - minIV) + 1)) + minIV;
                        spdef = (int) (Math.random() * ((maxIV - minIV) + 1)) + minIV;
                        spd = (int) (Math.random() * ((maxIV - minIV) + 1)) + minIV;
                    }
                } else {
                    CommandChatHandler.sendFormattedChat(sender, TextFormatting.RED, "Invalid amount of arguments.");
                    return;
                }
                hpiv = ("ivhp:" + hp);
                atkiv = ("ivattack:" + atk);
                defiv = ("ivdefence:" + def);
                spatkiv = ("ivspecialattack:" + spatk);
                spdefiv = ("ivspecialdefence:" + spdef);
                spdiv = ("ivspeed:" + spd);
                if(legendaries.contains(pokemon.name) && legendRerollKeepIVs) {
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
                    if(iv1 == 1 || iv2 == 1 || iv3 == 1) {
                        hpiv = ("ivhp:31");
                    }
                    if(iv1 == 2 || iv2 == 2 || iv3 == 2) {
                        atkiv = ("ivattack:31");
                    }
                    if(iv1 == 3 || iv2 == 3 || iv3 == 3) {
                        defiv = ("ivdefence:31");
                    }
                    if(iv1 == 4 || iv2 == 4 || iv3 == 4) {
                        spatkiv = ("ivspecialattack:31");
                    }
                    if(iv1 == 5 || iv2 == 5 || iv3 == 5) {
                        spdefiv = ("ivspecialdefence:31");
                    }
                    if(iv1 == 6 || iv2 == 6 || iv3 == 6) {
                        spdiv = ("ivspeed:31");
                    }
                }
                specs[0] = hpiv;
                specs[1] = atkiv;
                specs[2] = defiv;
                specs[3] = spatkiv;
                specs[4] = spdefiv;
                specs[5] = spdiv;

                partyStorage.retrieveAll();
                PokemonSpec spec = PokemonSpec.from(specs);
                spec.apply(poke);
                partyStorage.set(slotInt - 1, poke);
                CommandChatHandler.sendFormattedChat(sender, TextFormatting.GREEN, "Your " + pokemon.name + "'s IVs have been rerolled.");
            } catch (PlayerNotFoundException var9) {
                CommandChatHandler.sendFormattedChat(sender, TextFormatting.RED, "Invalid Name! Try again!");
            } catch (NumberFormatException var10) {
                CommandChatHandler.sendFormattedChat(sender, TextFormatting.RED, "Invalid number given!");
            }
        }
    }

    @Nonnull
    public List<String> getTabCompletions(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args, @Nullable BlockPos targetPos) {
        if (args.length == 1) {
            return getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
        }
        else if(args.length == 2) {
            List<String> slots = new ArrayList<>();
            slots.add("1");
            slots.add("2");
            slots.add("3");
            slots.add("4");
            slots.add("5");
            slots.add("6");
            slots.add("random");

            return getListOfStringsMatchingLastWord(args, slots);
        }
        return new ArrayList<>();
    }

    public boolean isUsernameIndex(@Nonnull String[] args, int i) {
        return false;
    }
}