package com.ameerhamza6733.okAmeer.utial.models;

/**
 * Created by AmeerHamza on 7/16/2017.
 */

public class Command {
    private long votebySize;
    private CommandPOJO commandPOJO;

    public Command(long voteby, CommandPOJO commandPOJO) {
        this.votebySize = voteby;
        this.commandPOJO = commandPOJO;
    }

    public long getVotebySize() {
        return votebySize;
    }

    public CommandPOJO getCommandPOJO() {
        return commandPOJO;
    }

    public Command(CommandPOJO commandPOJO) {
        this.commandPOJO = commandPOJO;
    }

    public Command(int voteby) {
        this.votebySize = voteby;
    }
}
