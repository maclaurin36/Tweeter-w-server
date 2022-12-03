package edu.byu.cs.tweeter.server.service.dto;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;

public class BatchStatusMessage {
    private List<String> aliases;
    private Status status;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public BatchStatusMessage() {

    }

    public List<String> getAliases() {
        return aliases;
    }

    public void setAliases(List<String> aliases) {
        this.aliases = aliases;
    }

    public BatchStatusMessage(List<String> aliases, Status status) {
        this.aliases = aliases;
        this.status = status;
    }
}
