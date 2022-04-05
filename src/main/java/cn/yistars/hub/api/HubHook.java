package cn.yistars.hub.api;

import cn.yistars.hub.group.Group;
import cn.yistars.hub.group.GroupManager;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class HubHook {
    public static GroupManager groupManager;

    public static Group getServerGroup(String serverName) {
        return groupManager.getServerGroup(serverName);
    }

    public static void goHub (ProxiedPlayer player, Group group) {
        groupManager.goHub(player, group);
    }

	/*
	static String getGroupName(String server) {

	}
	
	static String getGroupType(String groupname) {
		  return Hub.TypeMap.get(groupname);
	}

	static String getGroupQueue(String groupname) {
		  return Hub.QueueMap.get(groupname);
	  }

	  static List<String> getGroupCommand(String groupname) {
		  List<String> Commands = new ArrayList<String>();
		  for (String command : Hub.GroupMap.get(groupname)) {
			  Commands.add(command);
		  }
		  return Commands;
	  }
	  
	  static String getDefaultGroup() {
		  return Hub.DefaultGroup;
	  }

	 */
}
