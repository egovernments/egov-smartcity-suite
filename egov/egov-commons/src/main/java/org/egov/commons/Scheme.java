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
package org.egov.commons;

import org.egov.infra.admin.master.entity.User;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Scheme implements java.io.Serializable {

    private static final long serialVersionUID = 825465695975976653L;

    private Integer id;

    private Fund fund;

    private String code;

    private String name;

    private Date validfrom;

    private Date validto;

    private Boolean isactive=false;

    private String description;

    private BigDecimal sectorid;

    private BigDecimal aaes;

    private BigDecimal fieldid;

    private Set<SubScheme> subSchemes = new HashSet<SubScheme>(0);

    private User createdBy;

    private User lastModifiedBy;

    private Date createdDate;

    private Date lastModifiedDate;

    public Scheme() {
    }

    public Scheme(final Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public Fund getFund() {
        return fund;
    }

    public void setFund(final Fund fund) {
        this.fund = fund;
    }

    public String getCode() {
        return code;
    }

    public void setCode(final String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Date getValidfrom() {
        return validfrom;
    }

    public void setValidfrom(final Date validfrom) {
        this.validfrom = validfrom;
    }

    public Date getValidto() {
        return validto;
    }

    public void setValidto(final Date validto) {
        this.validto = validto;
    }

    public Boolean getIsactive() {
		return isactive;
	}

	public void setIsactive(Boolean isactive) {
		this.isactive = isactive;
	}

	public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public BigDecimal getSectorid() {
        return sectorid;
    }

    public void setSectorid(final BigDecimal sectorid) {
        this.sectorid = sectorid;
    }

    public Scheme(final Integer id, final Fund fund, final String code, final String name, final Date validfrom,
            final Date validto, final boolean isactive, final String description, final BigDecimal sectorid,
            final BigDecimal aaes, final BigDecimal fieldid, final Set<SubScheme> subSchemes) {
        this.id = id;
        this.fund = fund;
        this.code = code;
        this.name = name;
        this.validfrom = validfrom;
        this.validto = validto;
        this.isactive = isactive;
        this.description = description;
        this.sectorid = sectorid;
        this.aaes = aaes;
        this.fieldid = fieldid;
        this.subSchemes = subSchemes;
    }

    public void reset() {
        id = null;
        code = null;
        name = null;
        isactive = false;
        description = null;
        fund = null;
        validfrom = null;
        validto = null;

    }

    public BigDecimal getAaes() {
        return aaes;
    }

    public void setAaes(final BigDecimal aaes) {
        this.aaes = aaes;
    }

    public BigDecimal getFieldid() {
        return fieldid;
    }

    public void setFieldid(final BigDecimal fieldid) {
        this.fieldid = fieldid;
    }

    public Set<SubScheme> getSubSchemes() {
        return subSchemes;
    }

    public void setSubSchemes(final Set<SubScheme> subSchemes) {
        this.subSchemes = subSchemes;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(final User createdBy) {
        this.createdBy = createdBy;
    }

    public User getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(final User lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(final Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(final Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    @Override
    public String toString() {

        return "id:" + id + ",Code:" + code + "," + "isActive:" + isactive;
    }

}
