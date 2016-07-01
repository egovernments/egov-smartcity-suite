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
package org.egov.works.models.tender;

import java.util.Date;

import javax.persistence.Column;
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
import javax.validation.constraints.NotNull;

import org.egov.commons.EgwStatus;
import org.egov.infra.persistence.entity.AbstractAuditable;
import org.egov.infra.persistence.validator.annotation.ValidateDate;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name = "EGW_OFFLINE_STATUS")
@NamedQueries({
        @NamedQuery(name = OfflineStatus.GETSTATUSBYOBJECTID, query = " from OfflineStatus st where st.objectId=? and st.objectType=? order by id "),
        @NamedQuery(name = OfflineStatus.GETSTATUSDATEBYOBJECTID_TYPE_DESC, query = " from OfflineStatus st where st.objectId=? and st.objectType=? and st.egwStatus.description=? order by id "),
        @NamedQuery(name = OfflineStatus.GETMAXSTATUSBYOBJECTID, query = " from OfflineStatus st where st.objectId=? and st.id=(select max(id) from OfflineStatus where objectId=?) and st.objectType=? "),
        @NamedQuery(name = OfflineStatus.GETMAXSTATUSBYOBJECTID_TYPE, query = " from OfflineStatus st where st.objectId=? and st.id=(select max(id) from OfflineStatus where objectId=? and objectType=?) and st.objectType=? ") })
@SequenceGenerator(name = OfflineStatus.SEQ_EGW_OFFLINE_STATUS, sequenceName = OfflineStatus.SEQ_EGW_OFFLINE_STATUS, allocationSize = 1)
public class OfflineStatus extends AbstractAuditable {

    private static final long serialVersionUID = -1056415004063322298L;
    public static final String SEQ_EGW_OFFLINE_STATUS = "SEQ_EGW_OFFLINE_STATUS";
    public static final String GETSTATUSBYOBJECTID = "getStatusByObjectId";
    public static final String GETSTATUSDATEBYOBJECTID_TYPE_DESC = "getStatusDateByObjectId_Type_Desc";
    public static final String GETMAXSTATUSBYOBJECTID = "getmaxStatusByObjectId";
    public static final String GETMAXSTATUSBYOBJECTID_TYPE = "getmaxStatusByObjectId_Type";

    @Id
    @GeneratedValue(generator = SEQ_EGW_OFFLINE_STATUS, strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotEmpty(message = "ws.name.is.null")
    @Column(name = "OBJECT_TYPE")
    private String objectType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STATUS_ID")
    @NotNull(message = "ws.status.is.null")
    private EgwStatus egwStatus;

    @Column(name = "STATUS_DATE")
    @NotNull(message = "ws.statusDate.is.null")
    @ValidateDate(allowPast = true, dateFormat = "dd/MM/yyyy", message = "invalid.statusDate")
    private Date statusDate;

    @Column(name = "OBJECT_ID")
    @NotNull(message = "ws.objectId.is.null")
    private Long objectId;

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(final String objectType) {
        this.objectType = objectType;
    }

    public Date getStatusDate() {
        return statusDate;
    }

    public void setStatusDate(final Date statusDate) {
        this.statusDate = statusDate;
    }

    public Long getObjectId() {
        return objectId;
    }

    public void setObjectId(final Long objectId) {
        this.objectId = objectId;
    }

    public EgwStatus getEgwStatus() {
        return egwStatus;
    }

    public void setEgwStatus(final EgwStatus egwStatus) {
        this.egwStatus = egwStatus;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

}
