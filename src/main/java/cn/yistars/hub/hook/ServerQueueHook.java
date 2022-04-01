package cn.yistars.hub.hook;

import net.md_5.bungee.api.connection.ProxiedPlayer;

import cn.yistars.queue.api.QueueHook;

public class ServerQueueHook implements QueueHook{
	
	public static String getServer(ProxiedPlayer player,String group) {
		return QueueHook.getServer(player, group);
	}
	
	public static String getQueue(String server) {
		return QueueHook.getQueue(server);
	}
	
}
