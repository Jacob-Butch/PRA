package com.jake.pra.command.permissions;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.server.permission.DefaultPermissionLevel;
import net.minecraftforge.server.permission.PermissionAPI;

public enum EnumPerms {

    pra("pradditions.command.pra", "Allows use of /pra"),
    randomiv("pradditions.command.randomiv", "Allows use of /randomiv"),
    reroll("pradditions.command.reroll", "Allows use of /reroll"),
    clearparty("pradditions.command.clearparty", "Allows use of /clearparty"),
    clearbox("pradditions.command.clearbox", "Allows use of /clearparty"),
    pokecry("pradditions.command.pokecry", "Allows use of /pokecry"),
    pokesound("pradditions.command.pokesound", "Allows use of /pokesound"),
    release("pradditions.command.release", "Allows use of /release"),
    releaseOther("pradditions.command.release.other", "Allows use of /release on other players");

    EnumPerms(String node, String description) {
        this.node = node;
        this.description = description;
    }

    private final String node;
    private final String description;

    public void register(){
        PermissionAPI.registerNode(this.node, DefaultPermissionLevel.OP, this.description);
    }

    public static void registerPermissions(){
        for(EnumPerms perm : EnumPerms.values()){ perm.register(); }
    }

    public static boolean hasPermission(EnumPerms perm, ICommandSender sender){
        if(sender instanceof EntityPlayerMP){
            return PermissionAPI.hasPermission((EntityPlayerMP) sender, perm.node);
        }
        return true;
    }
}
