package me.telesphoreo.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLConnection;
import me.telesphoreo.UpToDate;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class Updater
{
    private Plugin plugin;
    private UpToDate.BuildProperties build = UpToDate.build;
    private String oldHead = build.head;
    private String path = this.getFilePath();

    public Updater(Plugin plugin)
    {
        this.plugin = plugin;
    }

    public void update()
    {
        try
        {
            String versionLink = "https://updater.telesphoreo.me/version.txt";
            URL url = new URL(versionLink);
            URLConnection con = url.openConnection();
            InputStreamReader isr = new InputStreamReader(con.getInputStream());
            BufferedReader reader = new BufferedReader(isr);

            if (!reader.ready())
            {
                return;
            }

            String newHead = reader.readLine();
            reader.close();

            if (oldHead.equals("${git.commit.id.abbrev}") || oldHead.equals("unknown"))
            {
                NLog.info("No Git head detected, not updating UpToDate.");
                return;
            }

            if (newHead.equals(oldHead))
            {
                NLog.info("There are no updates available.");
                return;
            }

            if (!newHead.equals(oldHead))
            {
                String dlLink = "https://updater.telesphoreo.me/UpToDate.jar";
                url = new URL(dlLink);
                con = url.openConnection();
                InputStream in = con.getInputStream();
                FileOutputStream out = new FileOutputStream(path);
                byte[] buffer = new byte[1024];
                int size = 0;
                while ((size = in.read(buffer)) != -1)
                {
                    out.write(buffer, 0, size);
                }

                out.close();
                in.close();
                NLog.info("Update successfully applied.");
            }
        }
        catch (IOException ex)
        {
            NLog.info("There was an issue fetching the server for an update.");
        }
    }

    private String getFilePath()
    {
        if (plugin instanceof JavaPlugin)
        {
            try
            {
                Method method = JavaPlugin.class.getDeclaredMethod("getFile");
                boolean wasAccessible = method.isAccessible();
                method.setAccessible(true);
                File file = (File)method.invoke(plugin);
                method.setAccessible(wasAccessible);

                return file.getPath();
            }
            catch (Exception e)
            {
                return "plugins" + File.separator + plugin.getName();
            }
        }
        else
        {
            return "plugins" + File.separator + "UpToDate.jar";
        }
    }
}