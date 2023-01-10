package com.narola.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Photo{
    private Metadata metadata;
    private String url;
    @JsonProperty("default")
    private boolean mydefault;

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isMydefault() {
        return mydefault;
    }

    public void setMydefault(boolean mydefault) {
        this.mydefault = mydefault;
    }
}
