package com.narola.model;

public class Metadata{
    private boolean primary;
    private Source source;
    private boolean sourcePrimary;
    private boolean verified;

    public boolean isPrimary() {
        return primary;
    }

    public void setPrimary(boolean primary) {
        this.primary = primary;
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public boolean isSourcePrimary() {
        return sourcePrimary;
    }

    public void setSourcePrimary(boolean sourcePrimary) {
        this.sourcePrimary = sourcePrimary;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }
}
