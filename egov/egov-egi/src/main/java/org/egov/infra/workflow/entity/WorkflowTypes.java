/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.infra.workflow.entity;

import org.egov.infra.admin.master.entity.Module;
import org.egov.infra.persistence.entity.AbstractAuditable;
import org.hibernate.search.annotations.DocumentId;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "EG_WF_TYPES")
@NamedQueries({
        @NamedQuery(name = WorkflowTypes.MODULE_FOR_TYPE, query = "select wt.module.name from WorkflowTypes wt where wt.type=?"),
        @NamedQuery(name = WorkflowTypes.TYPE_FOR_NAME, query = "select wt from WorkflowTypes wt where wt.displayName = ?"),
        @NamedQuery(name = WorkflowTypes.TYPE_LIKE_NAME, query = "select wt from WorkflowTypes wt where lower(wt.displayName) like ?"),
        @NamedQuery(name = WorkflowTypes.WF_TYPE_BY_TYPE_AND_RENDER_Y, query = "select wt from WorkflowTypes wt where wt.type =? and  renderYN ='Y'") })
@SequenceGenerator(name = WorkflowTypes.SEQ_WORKFLOWTYPES, sequenceName = WorkflowTypes.SEQ_WORKFLOWTYPES, allocationSize = 1)
public class WorkflowTypes extends AbstractAuditable {

    private static final long serialVersionUID = 1L;
    public static final String MODULE_FOR_TYPE = "MODULE_FOR_TYPE";
    public static final String TYPE_FOR_NAME = "TYPE_FOR_NAME";
    public static final String TYPE_LIKE_NAME = "TYPE_LIKE_NAME";
    public static final String SEQ_WORKFLOWTYPES = "SEQ_EG_WF_TYPES";
    public static final String WF_TYPE_BY_TYPE_AND_RENDER_Y = "WF_TYPE_BY_TYPE_AND_RENDER_Y";

    @DocumentId
    @Id
    @GeneratedValue(generator = SEQ_WORKFLOWTYPES, strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "module")
    private Module module;

    private String type;

    private String typeFQN;

    private String link;

    private String displayName;

    private Character renderYN;

    private Character groupYN;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

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
