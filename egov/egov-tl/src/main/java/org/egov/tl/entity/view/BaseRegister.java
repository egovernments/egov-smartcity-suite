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

package org.egov.tl.entity.view;

import org.egov.infra.utils.DateUtils;
import org.hibernate.annotations.Immutable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

@Entity
@Immutable
@Table(name = "EGTL_MV_BASEREGISTER_VIEW")
public class BaseRegister implements Serializable {

    private static final long serialVersionUID = -5366096182840879108L;

    @Column(name = "cat")
    private Long categoryId;

    @Column(name = "subcat")
    private Long subCategoryId;

    @Column(name = "status")
    private Long statusId;

    @Column(name = "ward")
    private Long wardId;

    @Id
    private BigInteger licenseid;

    @Transient
    private String filterName;

    private String licensenumber;
    private String tradetitle;
    private String owner;
    private String mobile;
    private String categoryname;
    private String subcategoryname;
    private String assessmentno;
    private String wardname;

    private String localityname;
    private String tradeaddress;
    private String commencementdate;
    private String statusname;
    private BigDecimal arrearlicensefee;
    private BigDecimal arrearpenaltyfee;
    private BigDecimal curlicensefee;
    private BigDecimal curpenaltyfee;
    private String unitofmeasure;
    private BigInteger tradewt;
    private BigDecimal rateval;
    private Long locality;
    private Long uom;
    private Long apptype;
    private Date appdate;

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getSubCategoryId() {
        return subCategoryId;
    }

    public void setSubCategoryId(Long subCategoryId) {
        this.subCategoryId = subCategoryId;
    }

    public Long getStatusId() {
        return statusId;
    }

    public void setStatusId(Long statusId) {
        this.statusId = statusId;
    }

    public Long getWardId() {
        return wardId;
    }

    public void setWardId(Long wardId) {
        this.wardId = wardId;
    }

    public String getLicensenumber() {
        return licensenumber;
    }

    public void setLicensenumber(String licensenumber) {
        this.licensenumber = licensenumber;
    }

    public String getTradetitle() {
        return tradetitle;
    }

    public void setTradetitle(String tradetitle) {
        this.tradetitle = tradetitle;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCategoryname() {
        return categoryname;
    }

    public void setCategoryname(String categoryname) {
        this.categoryname = categoryname;
    }

    public String getSubcategoryname() {
        return subcategoryname;
    }

    public void setSubcategoryname(String subcategoryname) {
        this.subcategoryname = subcategoryname;
    }

    public String getAssessmentno() {
        return assessmentno;
    }

    public void setAssessmentno(String assessmentno) {
        this.assessmentno = assessmentno;
    }

    public String getWardname() {
        return wardname;
    }

    public void setWardname(String wardname) {
        this.wardname = wardname;
    }

    public String getLocalityname() {
        return localityname;
    }

    public void setLocalityname(String localityname) {
        this.localityname = localityname;
    }

    public String getTradeaddress() {
        return tradeaddress;
    }

    public void setTradeaddress(String tradeaddress) {
        this.tradeaddress = tradeaddress;
    }

    public String getCommencementdate() {
        return commencementdate;
    }

    public void setCommencementdate(Date commencementdate) {
        this.commencementdate = DateUtils.getDefaultFormattedDate(commencementdate);
    }

    public String getStatusname() {
        return statusname;
    }

    public void setStatusname(String statusname) {
        this.statusname = statusname;
    }

    public BigDecimal getArrearlicensefee() {
        return arrearlicensefee;
    }

    public void setArrearlicensefee(BigDecimal arrearlicensefee) {
        this.arrearlicensefee = arrearlicensefee;
    }

    public BigDecimal getArrearpenaltyfee() {
        return arrearpenaltyfee;
    }

    public void setArrearpenaltyfee(BigDecimal arrearpenaltyfee) {
        this.arrearpenaltyfee = arrearpenaltyfee;
    }

    public BigDecimal getCurlicensefee() {
        return curlicensefee;
    }

    public void setCurlicensefee(BigDecimal curlicensefee) {
        this.curlicensefee = curlicensefee;
    }

    public BigDecimal getCurpenaltyfee() {
        return curpenaltyfee;
    }

    public void setCurpenaltyfee(BigDecimal curpenaltyfee) {
        this.curpenaltyfee = curpenaltyfee;
    }

    public BigInteger getLicenseid() {
        return licenseid;
    }

    public void setLicenseid(BigInteger licenseid) {
        this.licenseid = licenseid;
    }

    public String getUnitofmeasure() {
        return unitofmeasure;
    }

    public void setUnitofmeasure(String unitofmeasure) {
        this.unitofmeasure = unitofmeasure;
    }

    public BigInteger getTradewt() {
        return tradewt;
    }

    public void setTradewt(BigInteger tradewt) {
        this.tradewt = tradewt;
    }

    public BigDecimal getRateval() {
        return rateval;
    }

    public void setRateval(BigDecimal rateval) {
        this.rateval = rateval;
    }

    public Long getLocality() {
        return locality;
    }

    public void setLocality(Long locality) {
        this.locality = locality;
    }

    public Long getUom() {
        return uom;
    }

    public void setUom(Long uom) {
        this.uom = uom;
    }

    public Long getApptype() {
        return apptype;
    }

    public void setApptype(Long apptype) {
        this.apptype = apptype;
    }

    public Date getAppdate() {
        return appdate;
    }

    public void setAppdate(Date appdate) {
        this.appdate = appdate;
    }

    public void setCommencementdate(final String commencementdate) {
        this.commencementdate = commencementdate;
    }

    public String getFilterName() {
        return filterName;
    }

    public void setFilterName(final String filterName) {
        this.filterName = filterName;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (licenseid == null ? 0 : licenseid.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final BaseRegister other = (BaseRegister) obj;
        if (licenseid == null) {
            if (other.licenseid != null)
                return false;
        } else if (!licenseid.equals(other.licenseid))
            return false;
        return true;
    }

}