package cn.yistars.hub.utilities;

import cn.yistars.hub.config.MessageManager;
import net.md_5.bungee.api.plugin.Plugin;
import org.inventivetalent.update.spiget.SpigetUpdate;
import org.inventivetalent.update.spiget.UpdateCallback;
import org.inventivetalent.update.spiget.comparator.VersionComparator;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;


public class UpdateChecker implements Listener {
	private final Plugin plugin;
	private final MessageManager messageManager;
	private Boolean checkUpdate;

	public UpdateChecker(Plugin plugin, MessageManager messageManager, Boolean checkUpdate) {
		this.plugin = plugin;
		this.checkUpdate = checkUpdate;
		this.messageManager = messageManager;
	}

	public void setCheckUpdate(Boolean checkUpdate) {
		this.checkUpdate = checkUpdate;
	}

	@EventHandler
	public void onPlayerJoin(PostLoginEvent event) {
		if (this.checkUpdate && event.getPlayer().hasPermission("bungeehub.admin")) {
			Checker(event.getPlayer(), false);
		}
	}

	public void Checker(ProxiedPlayer player, boolean hasCommand) {
		SpigetUpdate updater = new SpigetUpdate(this.plugin, 89845);

		updater.setVersionComparator(VersionComparator.EQUAL);

		updater.checkForUpdate(new UpdateCallback() {
			@Override
			public void updateAvailable(String newVersion, String downloadUrl, boolean hasDirectDownload) {

				String msg = messageManager.getMessage("UpdatePage");
				msg = msg.replace("%url%", "https://www.spigotmc.org/resources/bungeehub.89845/");
				msg = ChatColor.translateAlternateColorCodes((char)'&', (String)msg);
				TextComponent message = new TextComponent(msg);
				message.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://www.spigotmc.org/resources/bungeehub.89845/"));
				player.sendMessage((BaseComponent)new TextComponent(message));
				
				if (hasDirectDownload) {
					msg = messageManager.getMessage("UpdateDownload");
					msg = msg.replace("%url%", downloadUrl);
					msg = ChatColor.translateAlternateColorCodes((char)'&', (String)msg);
					message = new TextComponent(msg);
					message.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, downloadUrl));
					player.sendMessage((BaseComponent)new TextComponent(message));
				}
			}

			@Override
			public void upToDate() {
				if (hasCommand) {
					String msg = messageManager.getMessage("NoUpdate");
					msg = ChatColor.translateAlternateColorCodes((char)'&', (String)msg);
					TextComponent message = new TextComponent(msg);
					player.sendMessage((BaseComponent)new TextComponent(message));
				}
			}
		});
	}
}
