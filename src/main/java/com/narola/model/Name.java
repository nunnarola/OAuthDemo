package com.narola.model;

public class Name{
    private Metadata metadata;
    private String displayName;
    private String familyName;
    private String givenName;
    private String displayNameLastFirst;
    private String unstructuredName;

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getDisplayNameLastFirst() {
        return displayNameLastFirst;
    }

    public void setDisplayNameLastFirst(String displayNameLastFirst) {
        this.displayNameLastFirst = displayNameLastFirst;
    }

    public String getUnstructuredName() {
        return unstructuredName;
    }

    public void setUnstructuredName(String unstructuredName) {
        this.unstructuredName = unstructuredName;
    }
}
