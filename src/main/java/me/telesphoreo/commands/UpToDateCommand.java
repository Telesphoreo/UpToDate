package me.telesphoreo.commands;

import me.telesphoreo.UpToDate;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class UpToDateCommand implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings)
    {
        UpToDate.BuildProperties build = UpToDate.build;
        sender.sendMessage(ChatColor.GOLD + "UpToDate allows you to easily update available server plugins.");
        sender.sendMessage(ChatColor.GOLD + String.format("Version "
                        + ChatColor.BLUE + "%s.%s.%s " + ChatColor.GOLD + "("
                        + ChatColor.BLUE + "%s" + ChatColor.GOLD + ")",
                build.version,
                build.number,
                build.head,
                UpToDate.getNMSVersion()));
        sender.sendMessage(String.format(ChatColor.GOLD + "Compiled on "
                        + ChatColor.BLUE + "%s" + ChatColor.GOLD + " by "
                        + ChatColor.BLUE + "%s",
                build.date,
                build.author));
        sender.sendMessage(ChatColor.GREEN + "Visit " + ChatColor.AQUA + "https://github.com/Telesphoreo/UpToDate" + ChatColor.GREEN + " for more information");
        return true;
    }
}