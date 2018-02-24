package org.egov.restapi.service;

import java.util.ArrayList;
import java.util.List;

import org.egov.council.entity.CouncilPreamble;
import org.egov.council.entity.CouncilPreambleBidderDetails;
import org.egov.council.enums.PreambleTypeEnum;
import org.egov.council.service.CouncilPreambleService;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.BoundaryType;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.admin.master.service.BoundaryTypeService;
import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.restapi.web.contracts.councilpreamble.CouncilPreambleRequest;
import org.egov.works.master.service.ContractorService;
import org.egov.works.models.masters.Contractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PreambleCreateAPIService {
    
    @Autowired
    private DepartmentService departmentService;
    
    @Autowired
    private CouncilPreambleService councilPreambleService;
    @Autowired
    private ContractorService contractorService;
    @Autowired
    private BoundaryService boundaryService;
    @Autowired
    private BoundaryTypeService boundaryTypeService;
    
    
    public CouncilPreamble createPreamble(CouncilPreambleRequest councilPreambleRequest) {
        CouncilPreamble councilPreamble = new CouncilPreamble();
        councilPreamble.setReferenceNumber(councilPreambleRequest.getReferenceNumber());
        councilPreamble.setDepartment(departmentService.getDepartmentByCode(councilPreambleRequest.getDepartmentcode()));
        councilPreamble.setGistOfPreamble(councilPreambleRequest.getGistOfPreamble());
        councilPreamble.setTypeOfPreamble(PreambleTypeEnum.valueOf(councilPreambleRequest.getPreambleType()));
        councilPreamble.setSanctionAmount(councilPreambleRequest.getEstimateAmount());
        final BoundaryType boundaryType = boundaryTypeService
                .getBoundaryTypeByNameAndHierarchyTypeName( "Ward", "ADMINISTRATION");
        List<Boundary> wards = new ArrayList<>();
        for(Boundary boundary: councilPreambleRequest.getWards()){
            Boundary ward =boundaryService.getBoundaryByTypeAndNo( boundaryType,boundary.getBoundaryNum());
            wards.add(ward);
            
        }
        councilPreamble.setWards(wards);
        List<CouncilPreambleBidderDetails> bidderList = new ArrayList<>();
        for(CouncilPreambleBidderDetails bidder:councilPreambleRequest.getBidders()){
            Contractor contracterDetail = contractorService.getContractorByCode(bidder.getCode());
            bidder.setBidder(contracterDetail); 
            bidder.setPreamble(councilPreamble);
            bidderList.add(bidder);

        }
        councilPreamble.setBidderDetails(bidderList);
        return councilPreambleService.createPreambleAPI(councilPreamble);
    }


}
