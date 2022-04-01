package cn.yistars.hub.config;

import cn.yistars.hub.command.CommandManager;
import cn.yistars.hub.group.Group;
import cn.yistars.hub.group.GroupManager;

import cn.yistars.hub.utilities.UpdateChecker;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.logging.Logger;

public class ConfigManager {
    private final GroupManager groupManager;
    private CommandManager commandManager;
    private UpdateChecker updateChecker;
    private final Plugin plugin;
    private final Logger logger;
    private final HashMap<String, String> Messages = new HashMap<>();
    private Boolean CheckUpdate = false;
    private Boolean DebugMode = false;
    private Boolean NeedPermission = false;
    private Boolean UseHub, UseLobby;

    public ConfigManager(GroupManager groupManager, Plugin plugin, Logger logger) {
        this.groupManager = groupManager;
        this.plugin = plugin;
        this.logger = logger;
    }

    public void setCommandManager(CommandManager commandManager) {
        this.commandManager = commandManager;
    }

    public void setUpdateChecker(UpdateChecker updateChecker) {
        this.updateChecker = updateChecker;
    }

    public void readConfig() {
        this.groupManager.clearAll();
        this.checkConfig();
        this.loadConfig();

        this.commandManager.unregisterAllCommand();
        this.commandManager.registerHubCommand(this.UseHub, this.UseLobby);

        this.updateChecker.setCheckUpdate(this.getCheckUpdate());

        logger.info(this.Messages.get("Loaded"));
    }

    private void checkConfig() {
        plugin.getDataFolder();

        File file = new File(plugin.getDataFolder(), "config.yml");

        if (!file.exists()) {
            try (InputStream in = plugin.getResourceAsStream("config.yml")) {
                Files.copy(in, file.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        File msg_file = new File(plugin.getDataFolder(), "messages.yml");
        if (!msg_file.exists()) {
            try (InputStream in = plugin.getResourceAsStream("messages.yml")) {
                Files.copy(in, msg_file.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadConfig() {
        try {
            Configuration msg_config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(plugin.getDataFolder(), "messages.yml"));

            for (String key : msg_config.getKeys()) {
                Messages.put(key, msg_config.getString(key));
            }

            Configuration config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(plugin.getDataFolder(), "config.yml"));

            String DefaultGroup = config.getString("DefaultGroup");
            this.DebugMode = config.getBoolean("DebugMode");
            this.CheckUpdate = config.getBoolean("CheckUpdate");
            this.NeedPermission = config.getBoolean("NeedPermission");
            this.UseHub = config.getBoolean("Commands.hub");
            this.UseLobby = config.getBoolean("Commands.lobby");

            for (String blackServer : config.getStringList("BlackServer")) {
                this.groupManager.addBlackServer(blackServer);
            }

            Configuration GroupList = config.getSection("Group");
            for (String groupName : GroupList.getKeys()) {
                try {
                    Group group = new Group(groupName, GroupList.getString(groupName + ".type"));

                    group.setDisplayName(GroupList.getString(groupName + ".name"));

                    switch (group.getType()) {
                        case "SERVER":
                            break;
                        case "COMMAND":
                            for (String command : GroupList.getStringList(groupName + ".command")) {
                                group.addValue(command);
                            }
                            break;
                        case "QUEUE":
                            group.addValue(GroupList.getString(groupName + ".queue"));
                            break;
                    }

                    if (!groupName.equals(DefaultGroup)) {
                        for (String serverName : GroupList.getStringList(group + ".server")) {
                            group.addServer(serverName);
                        }
                        this.groupManager.addGroup(group);
                    } else {
                        this.groupManager.setDefaultGroup(group);
                    }
                }
                catch (Exception ex) {
                    logger.warning("An error occurred in config " + groupName + " loading!");
                    logger.warning("Error: " + ex);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Unable to load configuration file", e);
        }
    }

    public Boolean getUseHub() {
        return UseHub;
    }

    public Boolean getUseLobby() {
        return UseLobby;
    }

    public Boolean getNeedPermission() {
        return NeedPermission;
    }

    public Boolean getDebugMode() {
        return DebugMode;
    }

    public Boolean getCheckUpdate() {
        return CheckUpdate;
    }

    public HashMap<String, String> getMessages() {
        return Messages;
    }
}
