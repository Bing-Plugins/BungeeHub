package cn.yistars.hub.command;

import java.util.Arrays;
import java.util.Collections;

import cn.yistars.hub.config.ConfigManager;
import cn.yistars.hub.config.MessageManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import cn.yistars.hub.utilities.UpdateChecker;

public class HubAdminCommand extends Command implements TabExecutor {
	private final MessageManager messageManager;
	private final ConfigManager configManager;
	private final UpdateChecker updateChecker;
	private final String version;

	public HubAdminCommand(MessageManager messageManager, ConfigManager configManager, UpdateChecker updateChecker, String version) {
        super("bungeehub");

		this.messageManager = messageManager;
		this.configManager = configManager;
		this.updateChecker = updateChecker;
		this.version = version;
    }
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		if (args.length == 1) {
			if (!sender.hasPermission("bungeehub.admin")) {
				messageManager.SendMessage((ProxiedPlayer) sender,"NoPermission");
				return;
			} else {
				if (args[0].equalsIgnoreCase("reload")) {
					this.configManager.readConfig();
					this.messageManager.updateMessages(configManager.getMessages());
					messageManager.SendMessage((ProxiedPlayer) sender,"Reload");
					return;
				} else if (args[0].equalsIgnoreCase("check")) {
					messageManager.SendMessage((ProxiedPlayer) sender,"Checking");
					this.updateChecker.Checker((ProxiedPlayer) sender, true);
					return;
				} else if (args[0].equalsIgnoreCase("help")) {
					messageManager.SendMessage((ProxiedPlayer) sender,"HelpTitle");
					messageManager.SendMessage((ProxiedPlayer) sender,"HelpHelp");
					messageManager.SendMessage((ProxiedPlayer) sender,"HelpCheck");
					messageManager.SendMessage((ProxiedPlayer) sender,"HelpReload");
					return;
				}
			}
		}
		
		String msg = "&aBungeeHub v%version% By Bing_Yanchi".replace("%version%", this.version);
		msg = ChatColor.translateAlternateColorCodes('&', msg);
		sender.sendMessage(new TextComponent(msg));
			
		msg = "&ePlugin Page: https://www.spigotmc.org/resources/bungeehub.89845/";
		msg = ChatColor.translateAlternateColorCodes('&', msg);
		TextComponent message = new TextComponent(msg);
		message.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://www.spigotmc.org/resources/bungeehub.89845/"));
			
		sender.sendMessage(new TextComponent(message));
	}
	
	public Iterable<String> onTabComplete(final CommandSender sender, final String[] args) {
		if(args.length == 1) {
			return Arrays.asList("help", "check", "reload");
		}
		return Collections.emptyList();
	}
}