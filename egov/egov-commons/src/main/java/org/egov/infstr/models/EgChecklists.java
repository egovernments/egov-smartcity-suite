/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
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
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
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
 *
 */
package org.egov.infstr.models;

import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.persistence.entity.AbstractAuditable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;

@Entity
@Table(name = "EG_CHECKLISTS")
@NamedQueries({
        @NamedQuery(name = "checklist.by.appconfigid.and.objectid", query = "from org.egov.infstr.models.EgChecklists as checkList where checkList.objectid =? and checkList.appconfigvalue.config.id in(?)"),
        @NamedQuery(name = "checklist.by.objectid", query = "from org.egov.infstr.models.EgChecklists as checkList where checkList.objectid =?") })
@SequenceGenerator(name = EgChecklists.SEQ_EG_CHECKLISTS, sequenceName = EgChecklists.SEQ_EG_CHECKLISTS, allocationSize = 1)
public class EgChecklists extends AbstractAuditable implements Serializable {

    private static final long serialVersionUID = -3245474955686333063L;

    public static final String SEQ_EG_CHECKLISTS = "SEQ_EG_CHECKLISTS";

    @Id
    @GeneratedValue(generator = SEQ_EG_CHECKLISTS, strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "OBJECT_ID")
    private Long objectid;

    private String checklistvalue;

    @ManyToOne
    @JoinColumn(name = "APPCONFIG_VALUES_ID", nullable = false)
    private AppConfigValues appconfigvalue;

    @Transient
    private String remarks;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    public Long getObjectid() {
        return objectid;
    }

    public void setObjectid(final Long objectid) {
        this.objectid = objectid;
    }

    public String getChecklistvalue() {
        return checklistvalue;
    }

    public void setChecklistvalue(final String checklistvalue) {
        this.checklistvalue = checklistvalue;
    }

    public AppConfigValues getAppconfigvalue() {
        return appconfigvalue;
    }

    public void setAppconfigvalue(final AppConfigValues appconfigvalue) {
        this.appconfigvalue = appconfigvalue;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(final String remarks) {
        this.remarks = remarks;
    }

}
