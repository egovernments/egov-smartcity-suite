/**
 * eGov suite of products aim to improve the internal efficiency,transparency,
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2016>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

        1) All versions of this program, verbatim or modified must carry this
           Legal Notice.

        2) Any misrepresentation of the origin of the material is prohibited. It
           is required that all modified versions of this material be marked in
           reasonable ways as different from the original version.

        3) This license does not grant any rights to any user of the program
           with regards to rights under trademark law for use of the trade names
           or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.stms.elasticSearch.service;

import java.util.Iterator;

import org.egov.config.search.Index;
import org.egov.config.search.IndexType;
import org.egov.infra.admin.master.entity.City;
import org.egov.infra.admin.master.service.CityService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.search.elastic.annotation.Indexing;
import org.egov.ptis.domain.model.AssessmentDetails;
import org.egov.ptis.domain.model.OwnerName;
import org.egov.stms.elasticSearch.entity.SewerageSearch;
import org.egov.stms.transactions.entity.SewerageApplicationDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class SewerageIndexService {
	
	@Autowired
	private CityService cityService;
	
	@Indexing(name = Index.SEWARAGE, type = IndexType.SEWARAGESEARCH) 
	public SewerageSearch createSewarageIndex(final SewerageApplicationDetails sewerageApplicationDetails, final AssessmentDetails assessmentDetails){ 
		
		final City cityWebsite = cityService.getCityByURL(ApplicationThreadLocals.getDomainName());
		
		SewerageSearch sewarageSearch = new SewerageSearch(sewerageApplicationDetails.getApplicationNumber(),
				 cityWebsite.getName(),cityWebsite.getGrade(), sewerageApplicationDetails.getCreatedDate(), cityWebsite.getDistrictName(), cityWebsite.getRegionName(),
					cityWebsite.getGrade());
		
		sewarageSearch.setApplicationCreatedBy(sewerageApplicationDetails.getCreatedBy().getName());
		sewarageSearch.setApplicationDate(sewerageApplicationDetails.getApplicationDate());
		sewarageSearch.setApplicationNumber(sewerageApplicationDetails.getApplicationNumber());
		sewarageSearch.setApplicationStatus(sewerageApplicationDetails.getStatus()!=null?sewerageApplicationDetails.getStatus().getDescription():"");
		sewarageSearch.setApplicationType(sewerageApplicationDetails.getApplicationType()!=null?sewerageApplicationDetails.getApplicationType().getName():"");
		sewarageSearch.setConnectionStatus(sewerageApplicationDetails.getConnection().getStatus()!=null?sewerageApplicationDetails.getConnection().getStatus().name():"");
		sewarageSearch.setCreatedDate(sewerageApplicationDetails.getCreatedDate());
		sewarageSearch.setShscNumber(sewerageApplicationDetails.getConnection().getShscNumber()!=null?sewerageApplicationDetails.getConnection().getShscNumber():"");
		sewarageSearch.setDisposalDate(sewerageApplicationDetails.getDisposalDate());
		sewarageSearch.setExecutionDate(sewerageApplicationDetails.getConnection().getExecutionDate());
		sewarageSearch.setIslegacy(sewerageApplicationDetails.getConnection().getLegacy());
		sewarageSearch.setNoOfClosets_nonResidential(sewerageApplicationDetails.getConnectionDetail().getNoOfClosetsNonResidential());
		sewarageSearch.setNoOfClosets_residential(sewerageApplicationDetails.getConnectionDetail().getNoOfClosetsResidential());
		sewarageSearch.setPropertyIdentifier(sewerageApplicationDetails.getConnectionDetail().getPropertyIdentifier()!=null?sewerageApplicationDetails.getConnectionDetail().getPropertyIdentifier():"");
		sewarageSearch.setPropertyType(sewerageApplicationDetails.getConnectionDetail().getPropertyType()!=null?sewerageApplicationDetails.getConnectionDetail().getPropertyType().name():"");
        
                sewarageSearch.setEstimationDate(sewerageApplicationDetails.getEstimationDate());
                sewarageSearch
                        .setEstimationNumber(sewerageApplicationDetails.getEstimationNumber() != null ? sewerageApplicationDetails
                                .getEstimationNumber() : "");
                sewarageSearch.setWorkOrderDate(sewerageApplicationDetails.getWorkOrderDate());
                sewarageSearch
                        .setWorkOrderNumber(sewerageApplicationDetails.getWorkOrderNumber() != null ? sewerageApplicationDetails
                                .getWorkOrderNumber() : "");
        
                Iterator<OwnerName> ownerNameItr = null;
	        if (null != assessmentDetails.getOwnerNames())
	            ownerNameItr = assessmentDetails.getOwnerNames().iterator();
	        final StringBuilder consumerName = new StringBuilder();
	        final StringBuilder mobileNumber = new StringBuilder();
	        if (null != ownerNameItr && ownerNameItr.hasNext()) {
	            final OwnerName primaryOwner = ownerNameItr.next();
	            consumerName.append(primaryOwner.getOwnerName() != null ? primaryOwner.getOwnerName() : "");
	            mobileNumber.append(primaryOwner.getMobileNumber() != null ? primaryOwner.getMobileNumber() : "");
	            while (ownerNameItr.hasNext()) {
	                final OwnerName secondaryOwner = ownerNameItr.next();
	                consumerName.append(",").append(secondaryOwner.getOwnerName() != null ? secondaryOwner.getOwnerName() : "");
	                mobileNumber.append(",").append(secondaryOwner.getMobileNumber() != null ? secondaryOwner.getMobileNumber() : "");
	            }

	        }
		sewarageSearch.setMobileNumber(mobileNumber.toString());
		sewarageSearch.setConsumerName(consumerName.toString());
		sewarageSearch.setDoorNo(assessmentDetails.getHouseNo()!=null?assessmentDetails.getHouseNo():"");
		sewarageSearch.setWard(assessmentDetails.getBoundaryDetails()!=null?assessmentDetails.getBoundaryDetails().getWardName():"");
		sewarageSearch.setAddress(assessmentDetails.getPropertyAddress()!=null?assessmentDetails.getPropertyAddress():"");
		// Setting application status is active or in-active
		sewarageSearch.setIsActive(sewerageApplicationDetails.isActive());
		return sewarageSearch;
	}
	
}