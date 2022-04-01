package cn.yistars.hub.command;

import cn.yistars.hub.config.ConfigManager;
import cn.yistars.hub.config.MessageManager;
import cn.yistars.hub.group.GroupManager;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.logging.Logger;

public class CommandManager {
    private final ConfigManager configManager;
    private final GroupManager groupManager;
    private final MessageManager messageManager;
    private final Logger logger;
    private final Plugin plugin;

    public CommandManager(ConfigManager configManager, GroupManager groupManager, MessageManager messageManager, Logger logger, Plugin plugin) {
        this.configManager = configManager;
        this.groupManager = groupManager;
        this.messageManager = messageManager;
        this.logger = logger;
        this.plugin = plugin;
    }

    public void registerHubCommand(Boolean hub, Boolean lobby) {
        if (hub) ProxyServer.getInstance().getPluginManager().registerCommand(this.plugin, new HubCommand(this.configManager, this.groupManager, this.messageManager, this.logger));
        if (lobby) ProxyServer.getInstance().getPluginManager().registerCommand(this.plugin, new LobbyCommand(this.configManager, this.groupManager, this.messageManager, this.logger));
    }

    public void unregisterAllCommand() {
        ProxyServer.getInstance().getPluginManager().unregisterCommand(new HubCommand(this.configManager, this.groupManager, this.messageManager, this.logger));
        ProxyServer.getInstance().getPluginManager().unregisterCommand(new LobbyCommand(this.configManager, this.groupManager, this.messageManager, this.logger));
    }
}
