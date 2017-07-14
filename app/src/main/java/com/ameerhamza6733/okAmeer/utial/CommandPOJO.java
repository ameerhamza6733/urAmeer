package com.ameerhamza6733.okAmeer.utial;

/**
 * Created by AmeerHamza on 7/15/2017.
 */

public class CommandPOJO {
    private String commandVotsCount;
    private String CommandName;
    private String CommandAction;
    private String CommandRequstedby;

    public CommandPOJO() {
    }

    public CommandPOJO(String commandVotsCount, String commandName, String commandAction, String commandRequstedby) {
        this.commandVotsCount = commandVotsCount;
        CommandName = commandName;
        CommandAction = commandAction;
        CommandRequstedby = commandRequstedby;
    }

    public String getCommandVotsCount() {
        return commandVotsCount;
    }

    public String getCommandName() {
        return CommandName;
    }

    public String getCommandAction() {
        return CommandAction;
    }

    public String getCommandRequstedby() {
        return CommandRequstedby;
    }
}
