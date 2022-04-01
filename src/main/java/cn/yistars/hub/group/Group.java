package cn.yistars.hub.group;

import java.util.ArrayList;

public class Group {
    private final String name;
    private String type;
    private String displayName;
    private Boolean available = true;
    private final ArrayList<String> serverList = new ArrayList<>();
    private final ArrayList<String> valueList = new ArrayList<>();

    public Group(String name, String type) {
        this.name = name;

        switch (type.toUpperCase()) {
            case "SERVER": case "COMMAND": case "QUEUE":
                this.type = type.toUpperCase();
                break;
            default:
                this.available = false;
        }
    }

    public Boolean getAvailable() {
        return available;
    }

    public String getName() {
        return this.name;
    }

    public String getType() {
        return this.type;
    }

    public String getDisplayName() {
        if (this.displayName != null) return this.displayName;
        else return this.name.replace("%name%", this.name);
    }

    public ArrayList<String> getValueList() {
        return this.valueList;
    }

    public Boolean checkInGroup (String serverName) {
        return serverList.contains(serverName);
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void addServer(String serverName) {
        this.serverList.add(serverName);
    }

    public void addValue(String value) {
        if (this.type.equals("COMMAND")) {
            valueList.add(value);
        } else {
            this.valueList.set(0, value);
        }
    }
}
