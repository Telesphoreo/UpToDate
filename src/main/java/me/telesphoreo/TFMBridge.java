package me.telesphoreo;

import java.util.function.Function;
import me.telesphoreo.util.NLog;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

public class TFMBridge
{
    private static Function<Player, Boolean> superAdminProvider;

    public static Plugin getTFM()
    {
        final Plugin tfm = Bukkit.getPluginManager().getPlugin("TotalFreedomMod");
        if (tfm == null)
        {
            NLog.warning("Could not resolve plugin: TotalFreedomMod");
        }

        return tfm;
    }
    @SuppressWarnings("unchecked")
    public static boolean isAdmin(Player player)
    {
        if (superAdminProvider == null)
        {
            final Plugin tfm = getTFM();
            if (tfm == null)
            {
                return false;
            }

            Object provider = null;
            for (RegisteredServiceProvider<?> serv : Bukkit.getServicesManager().getRegistrations(tfm))
            {
                if (Function.class.isAssignableFrom(serv.getService()))
                {
                    provider = serv.getProvider();
                }
            }

            if (provider == null)
            {
                NLog.warning("Could not obtain Super Admin service provider!");
                return false;
            }

            superAdminProvider = (Function<Player, Boolean>)provider;
        }

        return superAdminProvider.apply(player);
    }
}
