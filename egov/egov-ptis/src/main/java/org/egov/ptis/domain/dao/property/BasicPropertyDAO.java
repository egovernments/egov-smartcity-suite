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
package org.egov.ptis.domain.dao.property;

import java.util.List;

import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.BasicPropertyImpl;
import org.egov.ptis.domain.entity.property.PropertyID;
import org.egov.ptis.domain.entity.property.PropertyMaterlizeView;

public interface BasicPropertyDAO {

    public BasicProperty getBasicPropertyByRegNum(String RegNum);

    public BasicProperty getBasicPropertyByPropertyID(PropertyID propertyID);

    public BasicProperty getBasicPropertyByPropertyID(String propertyId);

    public BasicProperty getInActiveBasicPropertyByPropertyID(String propertyID);

    public BasicProperty getBasicPropertyByID_PropertyID(String ID_PropertyID);

    public BasicProperty getBasicPropertyByRegNumNew(String RegNum);

    public Integer getRegNum();

    public Integer getVoucherNum();

    public List getBasicPropertyByOldMunipalNo(String oldMuncipalNo);

    public BasicProperty getAllBasicPropertyByPropertyID(String propertyId);

    public List<BasicPropertyImpl> getChildBasicPropsForParent(BasicProperty basicProperty);

    public BasicProperty getBasicPropertyByIndexNumAndParcelID(String indexNum, String parcelID);

    public BasicProperty findById(Integer id, boolean lock);

    public List<BasicProperty> findAll();

    public BasicProperty create(BasicProperty basicProperty);

    public void delete(BasicProperty basicProperty);

    public BasicProperty update(BasicProperty basicProperty);

    public List<BasicProperty> getBasicPropertiesForTaxDetails(String circleName, String zoneName,
            String wardName, String blockName, String ownerName, String doorNo, String aadhaarNumber, String mobileNumber);

    public List<Long> getBoundaryIds(String boundaryName);

    public Boolean isBoundaryExist(String boundaryName);

    public Boolean isOwnerNameExist(String ownerName);

    public Boolean isDoorNoExist(String doorNo);

    public Boolean isAssessmentNoExist(String assessmentNo);

    public BasicProperty getParentBasicPropertyByBasicPropertyId(Long basicPropertyId);

    public List<BasicProperty> getBasicPropertiesForTaxDetails(String assessmentNo, String ownerName, String mobileNumber);
    
    public List<BasicProperty> getBasicPropertiesForTaxDetails(String assessmentNo, String ownerName, String mobileNumber, String propertyType, String doorNo);
    
    public List<BasicProperty> getActiveBasicPropertiesForWard(Long wardId, String upicNo, String doorNo, String oldUpicNo);
    
    public BasicProperty getBasicPropertyByProperty(Long propertyId);
}
