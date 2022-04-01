package cn.yistars.hub.group;
import cn.yistars.hub.hook.ServerQueueHook;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerConnectEvent;

import java.util.ArrayList;
import java.util.logging.Logger;

public class GroupManager {
    private final Logger logger;
    private Group DefaultGroup;
    private final ArrayList<String> BlackServer = new ArrayList<>();
    private final ArrayList<Group> GroupList = new ArrayList<>();

    public GroupManager (Logger logger) {
        this.logger = logger;
    }

    public void addGroup(Group group) {
        if (group.getAvailable()) {
            this.GroupList.add(group);
        } else {
            logger.warning(String.format("An error in the configuration of the group %s, ignored", group.getName()));
        }
    }

    public void addBlackServer(String blackServer) {
        this.BlackServer.add(blackServer);
    }

    public void setDefaultGroup(Group group) {
        this.DefaultGroup = group;
    }

    public void clearAll() {
        this.GroupList.clear();
        this.BlackServer.clear();
    }

    public Boolean isBlackServer(String serverName) {
        return BlackServer.contains(serverName);
    }

    public Group getServerGroup(String serverName) {
        for (Group group : this.GroupList) {
            if (group.checkInGroup(serverName)) return group;
        }
        return DefaultGroup;
    }

    public void goHub(ProxiedPlayer player, Group group) {
        switch (group.getType()) {
            case "SERVER":
                ServerInfo server = ProxyServer.getInstance().getServerInfo(group.getName());
                player.connect(server, ServerConnectEvent.Reason.PLUGIN);
                break;
            case "COMMAND":
                for (String command : group.getValueList()) {
                    command = command.replace("%player%", player.getName());
                    ProxyServer.getInstance().getPluginManager().dispatchCommand(ProxyServer.getInstance().getConsole(), command);
                }
                break;
            case "QUEUE":
                String serverName = ServerQueueHook.getServer(player, group.getValueList().get(0));
                // 无法找到可用服务器, 自动走默认
                if (serverName == null) {
                    this.goHub(player, this.DefaultGroup);
                }
                server = ProxyServer.getInstance().getServerInfo(serverName);
                player.connect(server, ServerConnectEvent.Reason.PLUGIN);
                break;
        }
    }
}
