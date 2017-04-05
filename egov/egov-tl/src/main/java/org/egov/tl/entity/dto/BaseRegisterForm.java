/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *     accountability and the service delivery of the government  organizations.
 *
 *      Copyright (C) 2016  eGovernments Foundation
 *
 *      The updated version of eGov suite of products as by eGovernments Foundation
 *      is available at http://www.egovernments.org
 *
 *      This program is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      any later version.
 *
 *      This program is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU General Public License for more details.
 *
 *      You should have received a copy of the GNU General Public License
 *      along with this program. If not, see http://www.gnu.org/licenses/ or
 *      http://www.gnu.org/licenses/gpl.html .
 *
 *      In addition to the terms of the GPL license to be adhered to in using this
 *      program, the following additional terms are to be complied with:
 *
 *          1) All versions of this program, verbatim or modified must carry this
 *             Legal Notice.
 *
 *          2) Any misrepresentation of the origin of the material is prohibited. It
 *             is required that all modified versions of this material be marked in
 *             reasonable ways as different from the original version.
 *
 *          3) This license does not grant any rights to any user of the program
 *             with regards to rights under trademark law for use of the trade names
 *             or trademarks of eGovernments Foundation.
 *
 *    In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.tl.entity.dto;

import java.math.BigInteger;
import java.text.SimpleDateFormat;


public class BaseRegisterForm {
    private Long categoryId;
    private Long subCategoryId;
    private Long statusId;
    private Long wardId;
    private String filterName;
    private BigInteger licenseid;
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
    private BigInteger arrearlicensefee = BigInteger.ZERO;
    private BigInteger arrearpenaltyfee = BigInteger.ZERO;
    private BigInteger curlicensefee = BigInteger.ZERO;
    private BigInteger curpenaltyfee = BigInteger.ZERO;
    private String unitofmeasure;
    private BigInteger tradewt = BigInteger.ZERO;
    private BigInteger rateval = BigInteger.ZERO;

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

    public void setCommencementdate(java.sql.Date commencementdate) {
        final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        this.commencementdate = sdf.format(commencementdate);
    }

    public String getStatusname() {
        return statusname;
    }

    public void setStatusname(String statusname) {
        this.statusname = statusname;
    }

    public BigInteger getArrearlicensefee() {
        return arrearlicensefee;
    }

    public void setArrearlicensefee(BigInteger arrearlicensefee) {
        this.arrearlicensefee = arrearlicensefee;
    }

    public BigInteger getArrearpenaltyfee() {
        return arrearpenaltyfee;
    }

    public void setArrearpenaltyfee(BigInteger arrearpenaltyfee) {
        this.arrearpenaltyfee = arrearpenaltyfee;
    }

    public BigInteger getCurlicensefee() {
        return curlicensefee;
    }

    public void setCurlicensefee(BigInteger curlicensefee) {
        this.curlicensefee = curlicensefee;
    }

    public BigInteger getCurpenaltyfee() {
        return curpenaltyfee;
    }

    public void setCurpenaltyfee(BigInteger curpenaltyfee) {
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

    public BigInteger getRateval() {
        return rateval;
    }

    public void setRateval(BigInteger rateval) {
        this.rateval = rateval;
    }

    public String getFilterName() {
        return filterName;
    }

    public void setFilterName(String filterName) {
        this.filterName = filterName;
    }
}
