package me.telesphoreo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Properties;
import me.telesphoreo.commands.CMD_Handler;
import me.telesphoreo.commands.CMD_Loader;
import me.telesphoreo.util.NLog;
import me.telesphoreo.util.Updater;
import org.bstats.bukkit.Metrics;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class UpToDate extends JavaPlugin
{
    public static UpToDate plugin;
    public static final BuildProperties build = new BuildProperties();
    public static Server server;
    public static String pluginName;
    public static String pluginVersion;

    @Override
    public void onLoad()
    {
        UpToDate.plugin = this;
        UpToDate.server = plugin.getServer();
        NLog.setPluginLogger(plugin.getLogger());
        NLog.setServerLogger(server.getLogger());
        UpToDate.pluginName = plugin.getDescription().getName();
        UpToDate.pluginVersion = plugin.getDescription().getVersion();
    }

    @Override
    public void onEnable()
    {
        build.load(plugin);
        new Metrics(this);
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                CMD_Loader.getCommandMap();
                CMD_Loader.scan();
            }
        };
    }

    @Override
    public void onDisable()
    {
        try
        {
            Updater updater = new Updater(plugin);
            updater.update();
        }
        catch (NoClassDefFoundError ex)
        {
            NLog.info("Failed to check for an update.");
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
    {
        return CMD_Handler.handleCommand(sender, cmd, commandLabel, args);
    }

    public static void downloadFile(String url, File output, boolean verbose) throws java.lang.Exception
    {
        final URL website = new URL(url);
        ReadableByteChannel rbc = Channels.newChannel(website.openStream());
        FileOutputStream fos = new FileOutputStream(output);
        fos.getChannel().transferFrom(rbc, 0, 1 << 24);
        fos.close();

        if (verbose)
        {
            NLog.info("Downloaded " + url + " to " + output.toString() + ".");
        }
    }

    public static class BuildProperties
    {
        public String author;
        public String version;
        public String number;
        public String date;
        public String head;

        void load(UpToDate plugin)
        {
            try
            {
                final Properties props;

                try (InputStream in = plugin.getResource("build.properties"))
                {
                    props = new Properties();
                    props.load(in);
                }

                author = props.getProperty("buildAuthor", "unknown");
                version = props.getProperty("buildVersion", pluginVersion);
                number = props.getProperty("buildNumber", "1");
                date = props.getProperty("buildDate", "unknown");
                head = props.getProperty("buildHead", "unknown").replace("${git.commit.id.abbrev}", "unknown");
            }
            catch (Exception ex)
            {
                NLog.severe("Could not load build properties! Did you compile with NetBeans/Maven?");
                NLog.severe(ex);
            }
        }
    }
}