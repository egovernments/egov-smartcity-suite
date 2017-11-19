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

package org.egov.council.service.es;

import org.egov.council.entity.CouncilMember;
import org.egov.council.entity.es.CouncilMemberIndex;
import org.egov.council.repository.es.CouncilMemberIndexRepository;
import org.egov.infra.admin.master.entity.City;
import org.egov.infra.admin.master.service.CityService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CouncilMemberIndexService {

    @Autowired
    private CityService cityService;
    
    @Autowired
    private CouncilMemberIndexRepository councilMemberIndexRepository;
    
    
    public CouncilMemberIndex createCouncilMemberIndex(final CouncilMember councilMember){
        final City cityWebsite = cityService.getCityByURL(ApplicationThreadLocals.getDomainName());
        CouncilMemberIndex memberIndex = new CouncilMemberIndex();
        memberIndex.setDistrictName(cityWebsite.getDistrictName());
        memberIndex.setUlbGrade(cityWebsite.getGrade());
        memberIndex.setUlbCode(cityWebsite.getCode());
        memberIndex.setRegionName(cityWebsite.getRegionName());
        memberIndex.setUlbName(cityWebsite.getName());
        if(councilMember != null){
            memberIndex.setId(cityWebsite.getCode().concat("-").concat(councilMember.getId().toString()));
            memberIndex.setName(councilMember.getName() != null ?councilMember.getName():"");
            memberIndex.setAddress(councilMember.getResidentialAddress() != null ?councilMember.getResidentialAddress():"");
            memberIndex.setBirthDate(councilMember.getBirthDate()!= null ?councilMember.getBirthDate():null);
            memberIndex.setGender(councilMember.getGender() != null ?councilMember.getGender().toString():"");
            memberIndex.setEmailId(councilMember.getEmailId() != null ?councilMember.getEmailId():"");
            memberIndex.setMobileNumber(councilMember.getMobileNumber() != null ?councilMember.getMobileNumber():"");
            memberIndex.setCaste(councilMember.getCaste() != null ?councilMember.getCaste().getName():"");
            memberIndex.setDesignation(councilMember.getDesignation() != null ?councilMember.getDesignation().getName():"");
            memberIndex.setQualification(councilMember.getQualification()!= null ?councilMember.getQualification().getDescription():"");
            memberIndex.setPartyAffiliation(councilMember.getPartyAffiliation() != null ?councilMember.getPartyAffiliation().getName():"");
            memberIndex.setStatus(councilMember.getStatus() != null ?councilMember.getStatus().name():"");
            memberIndex.setElectionDate(councilMember.getElectionDate()!=null?councilMember.getElectionDate():null);
            memberIndex.setOathDate(councilMember.getOathDate()!=null?councilMember.getOathDate():null);
            memberIndex.setElectionWard(councilMember.getElectionWard() != null ?councilMember.getElectionWard().getName():"");
            memberIndex.setCreatedDate(councilMember.getCreatedDate());
            memberIndex.setCategory(councilMember.getCategory()!= null ?councilMember.getCategory():"");
            memberIndex.setDateOfJoining(councilMember.getDateOfJoining()!=null?councilMember.getDateOfJoining():null);
            councilMemberIndexRepository.save(memberIndex);
        }
        
        return memberIndex;
    }
    
}