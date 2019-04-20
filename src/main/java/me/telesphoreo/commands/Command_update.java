package me.telesphoreo.commands;

import java.io.File;
import me.telesphoreo.TFMBridge;
import me.telesphoreo.UpToDate;
import me.telesphoreo.util.NLog;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

@CommandPermissions(source = SourceType.BOTH)
@CommandParameters(description = "Update server plugins.", usage = "/<command>")
public class Command_update extends BaseCommand
{
    public static final String[] FILES =
            {
                    "https://updater.telesphoreo.me/Aero.jar",
                    "https://updater.telesphoreo.me/EssentialsX.jar",
                    "https://updater.telesphoreo.me/EssentialsXSpawn.jar",
                    "https://updater.telesphoreo.me/LibsDisguises.jar",
                    "https://updater.telesphoreo.me/WorldEdit.jar",
                    "https://updater.telesphoreo.me/WorldGuard.jar"
            };

    @Override
    public boolean run(final CommandSender sender, final Player playerSender, final Command cmd, final String commandLabel, final String[] args, final boolean senderIsConsole)
    {
        if (!TFMBridge.isAdmin(playerSender))
        {
            sender.sendMessage(Messages.MSG_NO_PERMS);
            return true;
        }

        sender.sendMessage(ChatColor.GRAY + "Updating server plugins.");

        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                try
                {
                    for (final String url : FILES)
                    {
                        NLog.info("Downloading: " + url);

                        File file = new File("./plugins/" + url.substring(url.lastIndexOf("/") + 1));
                        if (file.exists())
                        {
                            file.delete();
                        }
                        if (!file.getParentFile().exists())
                        {
                            file.getParentFile().mkdirs();
                        }

                        UpToDate.downloadFile(url, file, true);
                    }
                    sender.sendMessage(ChatColor.GRAY + "Successfully updated. Restarting server.");
                    Bukkit.broadcastMessage(ChatColor.LIGHT_PURPLE + sender.getName() + " updated the plugins, restarting the server.");
                    Bukkit.getServer().shutdown();
                }
                catch (Exception ex)
                {
                    NLog.severe(ex);
                }
            }
        }.runTaskAsynchronously(plugin);
        return true;
    }
}