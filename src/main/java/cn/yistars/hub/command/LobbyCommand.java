package cn.yistars.hub.command;

import cn.yistars.hub.config.ConfigManager;
import cn.yistars.hub.config.MessageManager;
import cn.yistars.hub.group.Group;
import cn.yistars.hub.group.GroupManager;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.HashMap;
import java.util.logging.Logger;

public class LobbyCommand extends Command {
    private final ConfigManager configManager;
    private final GroupManager groupManager;
    private final MessageManager messageManager;
    private final Logger logger;

    public LobbyCommand(ConfigManager configManager, GroupManager groupManager, MessageManager messageManager, Logger logger) {
        super("lobby");

        this.configManager = configManager;
        this.messageManager = messageManager;
        this.groupManager = groupManager;
        this.logger = logger;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        if (!(sender instanceof ProxiedPlayer)) {
            this.logger.warning(messageManager.getMessage("Console"));
            return;
        }

        ProxiedPlayer player = (ProxiedPlayer) sender;
        if (this.configManager.getNeedPermission() && !sender.hasPermission("bungeehub.use")) {
            this.messageManager.SendMessage((ProxiedPlayer) sender, "NoPermission");
            return;
        }

        if (groupManager.isBlackServer(((ProxiedPlayer) sender).getServer().getInfo().getName()) && !player.hasPermission("bungeehub.admin")) {
            this.messageManager.SendMessage((ProxiedPlayer) sender, "BlackServe");
            return;
        }

        Group group = this.groupManager.getServerGroup(((ProxiedPlayer) sender).getServer().getInfo().getName());
        this.groupManager.goHub((ProxiedPlayer) sender, group);

        HashMap<String, String> placeholders = new HashMap<>();
        placeholders.put("%group%", group.getDisplayName());
        this.messageManager.sendArgMessgage((ProxiedPlayer) sender, "UseHub",placeholders);
    }
}

