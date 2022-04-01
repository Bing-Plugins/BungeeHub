package cn.yistars.hub.config;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.HashMap;

public class MessageManager {
    private HashMap<String, String> Messages;

    public MessageManager(HashMap<String, String> messages) {
        this.Messages = messages;
    }

    public void updateMessages(HashMap<String, String> messages) {
        this.Messages = messages;
    }

    public void SendMessage(ProxiedPlayer player, String key) {
        String msg = this.Messages.get(key);
        if (!(msg == null || msg.length() <= 0)) {
            msg = ChatColor.translateAlternateColorCodes('&', msg);
            player.sendMessage(new TextComponent(msg));
        }
    }

    public void sendArgMessgage(ProxiedPlayer player, String key, HashMap<String, String> args) {
        String msg = this.Messages.get(key);

        for (String placeholder : args.keySet()) {
            msg = msg.replace(placeholder, args.get(placeholder));
        }

        if (!(msg == null || msg.length() <= 0)) {
            msg = ChatColor.translateAlternateColorCodes('&', msg);
            player.sendMessage(new TextComponent(msg));
        }
    }

    public String getMessage(String key) {
        return this.Messages.get(key);
    }

}
