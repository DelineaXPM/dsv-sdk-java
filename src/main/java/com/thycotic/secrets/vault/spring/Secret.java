package com.thycotic.secrets.vault.spring;

import java.util.Collections;
import java.util.Map;

/**
 * Java representation of a <i>Secret</i> retrieved from DevOps Secrets Vault.
 */
public class Secret extends ResourceMetadata {
    private final Map<String, String> attributes = Collections.emptyMap();
    private final Map<String, String> data = Collections.emptyMap();
    private String path;

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public Map<String, String> getData() {
        return data;
    }

    public String getPath() {
        return path;
    }

    @Override
    public String toString() {
        return String.format("Secret { path: %s, version: %d, lastModified: %s, lastModifiedBy: %s }", this.path,
                this.version, super.getLastModified(), super.getLastModifiedBy());
    }
}
