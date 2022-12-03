package edu.byu.cs.tweeter.server.service.dto;

import java.util.List;

public class BatchStatusMessage {
    private List<String> aliases;
    private String feedAlias;

    public BatchStatusMessage() {

    }

    public List<String> getAliases() {
        return aliases;
    }

    public void setAliases(List<String> aliases) {
        this.aliases = aliases;
    }

    public String getFeedAlias() {
        return feedAlias;
    }

    public void setFeedAlias(String feedAlias) {
        this.feedAlias = feedAlias;
    }

    public BatchStatusMessage(List<String> aliases, String feedAlias) {
        this.aliases = aliases;
        this.feedAlias = feedAlias;
    }
}
