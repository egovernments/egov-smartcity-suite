package org.egov.infra.workflow.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.egov.infra.persistence.entity.AbstractAuditable;
import org.egov.infstr.commons.Module;
import org.egov.lib.rjbac.user.UserImpl;

@Entity
@Table(name="EG_WF_TYPES")
@NamedQueries({
    @NamedQuery(name="MODULE_FOR_TYPE",query="select wt.module.moduleName from WorkflowTypes wt where wt.type=?"),
    @NamedQuery(name="TYPE_FOR_NAME",query="select wt from WorkflowTypes wt where wt.displayName = ?"),
    @NamedQuery(name="TYPE_LIKE_NAME",query="select wt from WorkflowTypes wt where lower(wt.displayName) like ?")
})
public class WorkflowTypes extends AbstractAuditable<UserImpl, Long> {

    private static final long serialVersionUID = 1L;
    public static final String MODULE_FOR_TYPE = "MODULE_FOR_TYPE";
    public static final String TYPE_FOR_NAME = "TYPE_FOR_NAME";
    public static final String TYPE_LIKE_NAME = "TYPE_LIKE_NAME";
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="module")
    private Module module;
    private String type;
    private String typeFQN;
    private String link;
    private String displayName;
    private Character renderYN;
    private Character groupYN;

    public Module getModule() {
        return module;
    }

    public void setModule(final Module module) {
        this.module = module;
    }

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(final String displayName) {
        this.displayName = displayName;
    }

    public String getLink() {
        return link;
    }

    public void setLink(final String link) {
        this.link = link;
    }

    public String getTypeFQN() {
        return typeFQN;
    }

    public void setTypeFQN(final String typeFQN) {
        this.typeFQN = typeFQN;
    }

    public Character getRenderYN() {
        return renderYN;
    }

    public void setRenderYN(final Character renderYN) {
        this.renderYN = renderYN;
    }

    public Character getGroupYN() {
        return groupYN;
    }

    public void setGroupYN(final Character groupYN) {
        this.groupYN = groupYN;
    }
}
