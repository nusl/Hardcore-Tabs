package io.github.ye7i.hardcore_tabs;

import net.milkbowl.vault.permission.Permission;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.RegisteredServiceProvider;

public class PlayerJoinListener implements Listener {

	private static HardcoreTabs plugin;
	private Permission perms = null;
	
	private final char MOD_SEPERATOR = '�';
	
	public PlayerJoinListener(HardcoreTabs plugin)
	{
		PlayerJoinListener.plugin = plugin;

		if (!setupPermissions()) {
			 plugin.getServer().getPluginManager().disablePlugin(plugin);
	         
			 return;
	    }
		
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent joinEvent)
	{
		
		Player player = joinEvent.getPlayer();
		
		if (!player.hasPermission("hardcoretabs.enabled"))
		{
			return;
		}
		
		String group = perms.getPrimaryGroup(player);
		
		if (plugin.groupConfig.getConfig().getString(group + ".color") == null)
		{
			plugin.getLogger().info(String.format("[%s] Attempted to load color configuration for group %s, but could not find them in %s.", plugin.getDescription().getName(), group, plugin.getConfig().getString("GROUP_CONFIG_FILE_NAME")));
			return;
		}
		
		String playerListMod = "";
		
		playerListMod += MOD_SEPERATOR;
		playerListMod += plugin.getConfig().getString("color." + plugin.groupConfig.getConfig().getString(group + ".color"));
		
		for (String style : plugin.groupConfig.getConfig().getStringList(group + ".font-styles"))
		{
			playerListMod += MOD_SEPERATOR;
			playerListMod += plugin.getConfig().getString("style." + style) == null ? "" : plugin.getConfig().getString("style." + style);
		}
		
		player.setDisplayName(playerListMod + player.getName());
		player.setPlayerListName(playerListMod + player.getName());
		
	}
	
	private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = plugin.getServer().getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();
        return perms != null;
    }
	
}
