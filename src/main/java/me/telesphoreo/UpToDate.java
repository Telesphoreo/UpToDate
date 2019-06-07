package me.telesphoreo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Properties;
import me.telesphoreo.commands.UpToDateCommand;
import me.telesphoreo.commands.UpdateCommand;
import me.telesphoreo.util.NLog;
import me.telesphoreo.util.Updater;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.plugin.java.JavaPlugin;

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
        build.load(this);
        new Metrics(this);
        registerCommands();
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

    public static void downloadFile(String url, File output, boolean verbose) throws java.lang.Exception
    {
        try
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
        catch (FileNotFoundException ex)
        {
            NLog.info(url + " does not exist.");
        }

    }

    public static String getPlugin(String pluginName)
    {
        return "https://updater.telesphoreo.me/" + getNMSVersion() + "/" + pluginName + ".jar";
        // Example: https://updater.telesphoreo.me/v1_14_R1/WorldEdit.jar"
    }

    public static String getNMSVersion()
    {
        return Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
    }

    public void registerCommands()
    {
        getCommand("update").setExecutor(new UpdateCommand());
        getCommand("uptodate").setExecutor(new UpToDateCommand());
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