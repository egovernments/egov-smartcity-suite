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
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org
 ******************************************************************************/
package org.egov.ptis.utils;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.persistence.entity.Address;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.ConstructionTypeImpl;
import org.egov.ptis.domain.entity.property.PropertyCreationReason;
import org.egov.ptis.domain.entity.property.PropertyOccupation;
import org.egov.ptis.domain.entity.property.PropertyOwner;
import org.egov.ptis.domain.entity.property.PropertySource;
import org.egov.ptis.domain.entity.property.PropertyStatus;
import org.egov.ptis.domain.entity.property.PropertyUsage;
import org.egov.ptis.domain.entity.property.StructureClassification;


/**
 *
 *  @author deepak YN
 *
 */


public interface PTISCacheManagerInteface
{
	public java.util.List getPropertyCreationReasons();
	public List getAllStructuralFactors();
	public java.util.List getAllConstructionTypes();
	public java.util.List getAllPropUsage();
	public java.util.List getAllPropOccTypes();
	public java.util.List getAllPropertyStatus();
	public java.util.List getAllPropertySource();
	public PropertySource getPropertySourceById(Integer propSrcId);
	public PropertyStatus getPropertyStatusById(Integer propStatusId);
	public PropertyUsage getPropertyUsageById(Integer propUsageId);
	public PropertyOccupation getPropertyOccupationById(Integer propOccId);
	public StructureClassification getStructureClassificationById(Integer strucClsfnId);
	public ConstructionTypeImpl getConstructionTypeById(Integer ConstTypeId);
	public PropertyCreationReason getReasonById(Integer reasonId);
	public Boundary getBoundary(Integer bndryId);
	public java.util.List getAllTaxRates();
	public java.util.List getAllDepreciationRates();
	public List getAllCategories();
	public String buildAddress(BasicProperty basicProperty);
	public String buildOwnerFullName(Set<PropertyOwner> ownerSet);
    public Map getTxPercWithUsg(List lstTaxRates);
    public String  buildOwnerFullName(BasicProperty basicProp);
    public String buildAddressFromAddress(Address add);
    public String buildAddressByImplemetation(Address address);


}
