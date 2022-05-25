package com.delinea.secrets.vault.spring;

import java.util.Date;
import java.util.UUID;

/**
 * Java representation of meta-data that is common to several entities retrieved
 * from DevOps Secrets Vault.
 */
public class ResourceMetadata {
    private UUID id;
    private String description, createdBy, lastModifiedBy;
    private Date created, lastModified;
    int version;

    public UUID getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public Date getCreated() {
        return new Date(created.getTime());
    }

    public Date getLastModified() {
        return new Date(lastModified.getTime());
    }

    public int getVersion() {
        return version;
    }
}
