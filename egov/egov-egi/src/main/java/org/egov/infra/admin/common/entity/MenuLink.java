package org.egov.infra.admin.common.entity;

public class MenuLink {
    private Long id;
    private String name;
    private String contextRoot;
    private String displayName;
    private String url;
    private boolean enabled;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getContextRoot() {
        return contextRoot;
    }

    public void setContextRoot(final String contextRoot) {
        this.contextRoot = contextRoot;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(final String displayName) {
        this.displayName = displayName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(final String url) {
        this.url = url;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }
}
