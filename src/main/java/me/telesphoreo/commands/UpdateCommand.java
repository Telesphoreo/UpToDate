package me.telesphoreo.commands;

import java.io.File;
import me.telesphoreo.TFMBridge;
import me.telesphoreo.UpToDate;
import me.telesphoreo.util.NLog;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;

public class UpdateCommand implements CommandExecutor
{
    private final String[] FILES =
            {
                    UpToDate.getPlugin("Aero"),
                    UpToDate.getPlugin("BukkitTelnet"),
                    UpToDate.getPlugin("JDA"),
                    UpToDate.getPlugin("EssentialsX"),
                    UpToDate.getPlugin("EssentialsXSpawn"),
                    UpToDate.getPlugin("LibsDisguises"),
                    UpToDate.getPlugin("ProtocolLib"),
                    UpToDate.getPlugin("WorldEdit"),
                    UpToDate.getPlugin("WorldGuard"),
            };

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        boolean noTFM = false;
        if (TFMBridge.getTFM() == null)
        {
            noTFM = true;
        }
        if (noTFM || TFMBridge.isAdmin(Bukkit.getPlayer(sender.getName())))
        {
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
            }.runTaskAsynchronously(UpToDate.plugin);
        } else {
            sender.sendMessage(Messages.MSG_NO_PERMS);
            return true;
        }
        return true;
    }
}