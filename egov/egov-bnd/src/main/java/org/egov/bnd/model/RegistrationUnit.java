/*******************************************************************************
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
 * 	1) All versions of this program, verbatim or modified must carry this
 * 	   Legal Notice.
 *
 * 	2) Any misrepresentation of the origin of the material is prohibited. It
 * 	   is required that all modified versions of this material be marked in
 * 	   reasonable ways as different from the original version.
 *
 * 	3) This license does not grant any rights to any user of the program
 * 	   with regards to rights under trademark law for use of the trade names
 * 	   or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 ******************************************************************************/
/*
 * Created on Jul 7, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.egov.bnd.model;

import org.egov.infra.persistence.entity.Address;
import org.egov.infra.workflow.entity.StateAware;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class RegistrationUnit extends StateAware {
    private static final long serialVersionUID = 2095268643402349855L;
    private Long id;
    private String regUnitConst;
    private String regUnitDesc;
    private String regUnitDescLocal;
    private Address regUnitAddress;
    // private Integer createdBy;
    // private Date createdDate;
    // private User updatedBy;
    // private Date updatedDate;
    private String isactive = "Y";
    private List<DistrictChangeDetails> districtChangeDetails = new ArrayList<DistrictChangeDetails>();
    private Boolean ismainregunit;
    private Set<CrematoriumMaster> crematoriums = new HashSet<CrematoriumMaster>(0);

    public String getIsactive() {
        return isactive;
    }

    public void setIsactive(final String isactive) {
        this.isactive = isactive;
    }

    /**
     * @return Returns the createdDate.
     */
    /*
     * public Date getCreatedDate() { return createdDate; }
     *//**
     * @param createdDate
     *            The createdDate to set.
     */
    /*
     * public void setCreatedDate(Date createdDate) { this.createdDate =
     * createdDate; }
     */

    /**
     * @return Returns the updatedDate.
     */
    /*
     * public Date getUpdatedDate() { return updatedDate; }
     *//**
     * @param updatedDate
     *            The updatedDate to set.
     */
    /*
     * public void setUpdatedDate(Date updatedDate) { this.updatedDate =
     * updatedDate; }
     */

    /**
     * default constructor. initializes an Address object too.
     */
    public RegistrationUnit() {
        super();
        regUnitAddress = new Address();
    }

    /**
     * @return Returns the id.
     */
    /*
     * public Long getId() { return id; }
     *//**
     * @param id
     *            The id to set.
     */
    /*
     * public void setId(Long id) { this.id = id; }
     */
    /**
     * @return Returns the regUnitAddress.
     */
    public Address getRegUnitAddress() {
        return regUnitAddress;
    }

    /**
     * @param regUnitAddress
     *            The regUnitAddress to set.
     */
    public void setRegUnitAddress(final Address regUnitAddress) {
        this.regUnitAddress = regUnitAddress;
    }

    /**
     * @return Returns the regUnitConst.
     */
    public String getRegUnitConst() {
        return regUnitConst;
    }

    /**
     * @param regUnitConst
     *            The regUnitConst to set.
     */
    public void setRegUnitConst(final String regUnitConst) {
        this.regUnitConst = regUnitConst;
    }

    /**
     * @return Returns the regUnitDesc.
     */
    public String getRegUnitDesc() {
        return regUnitDesc;
    }

    /**
     * @param regUnitDesc
     *            The regUnitDesc to set.
     */
    public void setRegUnitDesc(final String regUnitDesc) {
        this.regUnitDesc = regUnitDesc;
    }

    /**
     * @return Returns the regUnitDescLocal.
     */
    public String getRegUnitDescLocal() {
        return regUnitDescLocal;
    }

    /**
     * @param regUnitDescLocal
     *            The regUnitDescLocal to set.
     */
    public void setRegUnitDescLocal(final String regUnitDescLocal) {
        this.regUnitDescLocal = regUnitDescLocal;
    }

    /*	*//**
     * @return Returns the createdBy.
     */
    /*
     * public Integer getCreatedBy() { return createdBy; }
     *//**
     * @param createdBy
     *            The createdBy to set.
     */
    /*
     * public void setCreatedBy(Integer createdBy) { this.createdBy = createdBy;
     * }
     */

    /**
     * @return Returns the updatedBy.
     */
    /*
     * public User getUpdatedBy() { return updatedBy; }
     *//**
     * @param updatedBy
     *            The updatedBy to set.
     */
    /*
     * public void setUpdatedBy(User updatedBy) { this.updatedBy = updatedBy; }
     */

    public void setNewDistrictName(final DistrictChangeDetails dcDetails) {
        // There can be multiple district name changes. When a district name
        // change is
        // entered into the system, it takes effect only when the effective date
        // has occured.
        // So when a new district name is introduced first we check history of
        // name
        // changes and turn on the history flag for all those records that are
        // no longer
        // effective any more.

        final Iterator<DistrictChangeDetails> dcIterator = getDistrictChangeDetails().iterator();
        while (dcIterator.hasNext()) {
            final DistrictChangeDetails aDCDetails = dcIterator.next();
            if ('Y' != aDCDetails.getIsHistory())
                aDCDetails.setIsHistory('Y');
        }

        addDistrictChangeDetails(dcDetails);
    }

    /**
     * @return Returns the CDistrictChangeDetails.
     */
    public List<DistrictChangeDetails> getDistrictChangeDetails() {
        return districtChangeDetails;
    }

    /**
     * @param DistrictChangeDetails
     *            The CDistrictChangeDetails to set.
     */
    public void setDistrictChangeDetails(final List<DistrictChangeDetails> dcDetailList) {
        districtChangeDetails = dcDetailList;
    }

    public List<DistrictChangeDetails> addDistrictChangeDetails(final DistrictChangeDetails dcDetails) {
        dcDetails.setRegUnit(this);
        getDistrictChangeDetails().add(dcDetails);
        return districtChangeDetails;
    }

    public List<DistrictChangeDetails> removeDistrictChangeDetails(final DistrictChangeDetails dcDetails) {
        getDistrictChangeDetails().remove(dcDetails);
        return districtChangeDetails;
    }

    public DistrictChangeDetails getRecentDistrictChange() {
        final Iterator<DistrictChangeDetails> dcIterator = getDistrictChangeDetails().iterator();
        if (dcIterator != null)
            while (dcIterator.hasNext()) {
                final DistrictChangeDetails dcDetails = dcIterator.next();
                if (!dcDetails.getEffectiveDate().after(new Date()) && dcDetails.getIsHistory() != 'Y')
                    return dcDetails;
            }
        // If we reached here, there is no district name change.
        return null;
    }

    public Boolean getIsmainregunit() {
        return ismainregunit;
    }

    public void setIsmainregunit(final Boolean ismainregunit) {
        this.ismainregunit = ismainregunit;
    }

    public Set<CrematoriumMaster> getCrematoriums() {
        return crematoriums;
    }

    public void setCrematoriums(final Set<CrematoriumMaster> crematoriums) {
        this.crematoriums = crematoriums;
    }

    @Override
    public String getStateDetails() {
        return "TODO";
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }
}
