package com.electrifiedded.necromancy.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class CommandMinion extends CommandBase {

    @Override
    public String getName() {
        return "minion";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/minion <set aggressive|passive | friend <player> | enemy <player>>";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return sender instanceof EntityPlayer;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length < 2) throw new WrongUsageException(getUsage(sender));
        EntityPlayerMP player = getCommandSenderAsPlayer(sender);
        NBTTagCompound nbt = player.getEntityData();
        switch (args[0]) {
            case "set" -> {
                if (args[1].equals("aggressive")) {
                    nbt.setBoolean("aggressive", true);
                    sender.sendMessage(new TextComponentString("Minions are set to aggressive"));
                } else if (args[1].equals("passive")) {
                    nbt.setBoolean("aggressive", false);
                    sender.sendMessage(new TextComponentString("Minions are set to passive"));
                } else {
                    throw new WrongUsageException(getUsage(sender));
                }
            }
            case "friend" -> {
                nbt.setString(args[1], "friend");
                sender.sendMessage(new TextComponentString(args[1] + " is now a friend"));
            }
            case "enemy" -> {
                nbt.setString(args[1], "enemy");
                sender.sendMessage(new TextComponentString(args[1] + " is now an enemy"));
            }
            default -> throw new WrongUsageException(getUsage(sender));
        }
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        if (args.length == 1) return getListOfStringsMatchingLastWord(args, "set", "friend", "enemy");
        if (args.length == 2) {
            if (args[0].equals("set")) return getListOfStringsMatchingLastWord(args, "aggressive", "passive");
            if (args[0].equals("friend") || args[0].equals("enemy")) return getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
        }
        return Collections.emptyList();
    }
}
