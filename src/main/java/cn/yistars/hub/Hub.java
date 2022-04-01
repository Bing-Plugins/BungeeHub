package cn.yistars.hub;

import cn.yistars.hub.command.CommandManager;
import cn.yistars.hub.command.HubAdminCommand;
import cn.yistars.hub.config.ConfigManager;
import cn.yistars.hub.config.MessageManager;
import cn.yistars.hub.group.GroupManager;
import cn.yistars.hub.utilities.Metrics;
import cn.yistars.hub.utilities.UpdateChecker;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.logging.Logger;

public class Hub extends Plugin {
	private final Logger logger = Logger.getLogger("BungeeHub");
	private final GroupManager groupManager = new GroupManager(this.logger);
	private final ConfigManager configManager = new ConfigManager(groupManager, this, logger);
	private final MessageManager messageManager = new MessageManager(this.configManager.getMessages());
	private final UpdateChecker updateChecker = new UpdateChecker(this, this.messageManager, false);
	private final CommandManager commandManager = new CommandManager(this.configManager, groupManager, this.messageManager, this.logger, this);


	@Override
	public void onEnable() {
		this.configManager.setCommandManager(this.commandManager);
		this.configManager.setUpdateChecker(this.updateChecker);
		this.configManager.readConfig();

		ProxyServer.getInstance().getPluginManager().registerCommand(this, new HubAdminCommand(this.messageManager, this.configManager, updateChecker, this.getProxy().getVersion()));
		
		this.getProxy().getPluginManager().registerListener(this, new UpdateChecker(this, this.messageManager, this.configManager.getCheckUpdate()));
		
		Metrics metrics = new Metrics(this, 11231);
		metrics.addCustomChart(new Metrics.SimplePie("hub_command", this::HubCommandType));
		metrics.addCustomChart(new Metrics.SimplePie("update_check", () -> configManager.getCheckUpdate() ? "true" : "false"));
		metrics.addCustomChart(new Metrics.SimplePie("need_permission", () -> configManager.getNeedPermission() ? "true" : "false"));
		metrics.addCustomChart(new Metrics.SimplePie("debug_mode", () -> configManager.getDebugMode() ? "true" : "false"));
        
		this.logger.info(configManager.getMessages().get("Enabled"));
	}
	
	public void onDisable() {
		ProxyServer.getInstance().getPluginManager().unregisterCommands(this);
		this.logger.info(configManager.getMessages().get("Disabled"));
	}
	
	private String HubCommandType() {
		Boolean useHub = configManager.getUseHub();
		Boolean useLobby = configManager.getUseLobby();

		if (useHub && useLobby) {
			return "Hub and Lobby";
		} else if (useHub) {
			return "Hub";
		} else if (useLobby) {
			return "Lobby";
		} else {
			return "None";
		}
	}
}