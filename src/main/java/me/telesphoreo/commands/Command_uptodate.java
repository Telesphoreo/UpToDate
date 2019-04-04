package me.telesphoreo.commands;

import me.telesphoreo.UpToDate;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermissions(source = SourceType.BOTH)
@CommandParameters(description = "Shows information about, reload, or update UpToDate", usage = "/<command> [reload]")
public class Command_uptodate extends BaseCommand
{
    @Override
    public boolean run(final CommandSender sender, final Player sender_p, final Command cmd, final String commandLabel, final String[] args, final boolean senderIsConsole)
    {
        UpToDate.BuildProperties build = UpToDate.build;
        sender.sendMessage(ChatColor.GOLD + "UpToDate is a plugin that allows you to easily update server plugins..");
        sender.sendMessage(ChatColor.GOLD + String.format("Version "
                        + ChatColor.BLUE + "%s Build %s " + ChatColor.GOLD + "("
                        + ChatColor.BLUE + "%s" + ChatColor.GOLD + ")",
                build.version,
                build.number,
                build.head));
        sender.sendMessage(String.format(ChatColor.GOLD + "Compiled on "
                        + ChatColor.BLUE + "%s" + ChatColor.GOLD + " by "
                        + ChatColor.BLUE + "%s",
                build.date,
                build.author));
        sender.sendMessage(ChatColor.GOLD + "Visit " + ChatColor.BLUE + "https://github.com/Telesphoreo/UpToDate" + ChatColor.GOLD + " for more information");
        return true;
    }
}