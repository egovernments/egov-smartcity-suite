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
package org.egov.ptis.domain.entity.property;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import java.util.*;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.pims.commons.Position;
import org.egov.ptis.bean.demand.DemandDetail;

@Entity
@Table(name = "egpt_write_off")
@SequenceGenerator(name = WriteOff.SEQ_WRITEOFF, sequenceName = WriteOff.SEQ_WRITEOFF, allocationSize = 1)

public class WriteOff extends StateAware<Position> {

	private static final long serialVersionUID = 6700204182767909671L;

	public static final String SEQ_WRITEOFF = "SEQ_EGPT_WRITE_OFF";

	@Id
	@GeneratedValue(generator = SEQ_WRITEOFF, strategy = GenerationType.SEQUENCE)
	public Long id;

	@ManyToOne(targetEntity = PropertyImpl.class, cascade = CascadeType.ALL)
	@NotNull
	@JoinColumn(name = "property_id")
	private PropertyImpl property;

	@ManyToOne(targetEntity = BasicPropertyImpl.class, cascade = CascadeType.ALL)
	@NotNull
	@JoinColumn(name = "basicproperty_id", nullable = false)
	private BasicPropertyImpl basicProperty;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "egpt_write_off_docs", joinColumns = @JoinColumn(name = "writeOff"), inverseJoinColumns = @JoinColumn(name = "document"))
	private List<Document> documents = new ArrayList<>();

	@Column(name = "status")
	private Character status;

	@Column(name = "source")
	private String source;

	@Column(name = "applicationno")
	private String applicationNumber;

	@Column(name = "type")
	private String writeOffType;

	@Column(name = "reason")
	private String WriteOffReason;

	private String resolutionNo;

	private String resolutionDate;

	@Column(name = "deactivate")
	private Boolean propertydeactivateFlag;

	private WriteOffReasons writeOffReasons;

	@Transient
	private transient List<DemandDetail> demandDetailBeanList = new ArrayList<>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public PropertyImpl getProperty() {
		return property;
	}

	public void setProperty(PropertyImpl property) {
		this.property = property;
	}

	public BasicPropertyImpl getBasicProperty() {
		return basicProperty;
	}

	public void setBasicProperty(BasicPropertyImpl basicProperty) {
		this.basicProperty = basicProperty;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getApplicationNumber() {
		return applicationNumber;
	}

	public void setApplicationNumber(String applicationNumber) {
		this.applicationNumber = applicationNumber;
	}

	public String getWriteOffReason() {
		return WriteOffReason;
	}

	public void setWriteOffReason(String writeOffReason) {
		WriteOffReason = writeOffReason;
	}

	public String getResolutionNo() {
		return resolutionNo;
	}

	public void setResolutionNo(String resolutionNo) {
		this.resolutionNo = resolutionNo;
	}

	public String getResolutionDate() {
		return resolutionDate;
	}

	public void setResolutionDate(String resolutionDate) {
		this.resolutionDate = resolutionDate;
	}

	public String getWriteOffType() {
		return writeOffType;
	}

	public void setWriteOffType(String writeOffType) {
		this.writeOffType = writeOffType;
	}

	@Override
	public String getStateDetails() {
		final StringBuilder stateDetails = new StringBuilder();
		BasicProperty basicPropertyObj = getBasicProperty();

		String upicNo = EMPTY;
		String applicationNo = EMPTY;

		if (isNotBlank(basicPropertyObj.getUpicNo())) {
			upicNo = basicPropertyObj.getUpicNo();
		}
		if (isNotBlank(getApplicationNumber())) {
			applicationNo = getApplicationNumber();
		}

		stateDetails.append(upicNo.isEmpty() ? applicationNo : upicNo).append(", ");
		return stateDetails.toString();
	}

	public List<DemandDetail> getDemandDetailBeanList() {
		return demandDetailBeanList;
	}

	public void setDemandDetailBeanList(List<DemandDetail> demandDetailBeanList) {
		this.demandDetailBeanList = demandDetailBeanList;
	}

	public Boolean getPropertydeactivateFlag() {
		return propertydeactivateFlag;
	}

	public void setPropertydeactivateFlag(Boolean propertydeactivateFlag) {
		this.propertydeactivateFlag = propertydeactivateFlag;
	}

	public WriteOffReasons getWriteOffReasons() {
		return writeOffReasons;
	}

	public void setWriteOffReasons(WriteOffReasons writeOffReasons) {
		this.writeOffReasons = writeOffReasons;
	}

	public Position getPositionById(Long approvalPosition) {
		// TODO Auto-generated method stub
		return null;
	}

}
