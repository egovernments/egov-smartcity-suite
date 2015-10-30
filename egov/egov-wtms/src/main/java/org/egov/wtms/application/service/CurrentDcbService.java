package org.egov.wtms.application.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.dcb.bean.DCBDisplayInfo;
import org.egov.demand.model.EgReasonCategory;
import org.egov.wtms.application.repository.WaterConnectionDetailsRepository;
import org.egov.wtms.utils.constants.WaterTaxConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CurrentDcbService {
    
    @Autowired
    private WaterConnectionDetailsRepository waterConnectionDetailsRepository;
    
    
    public DCBDisplayInfo getdcbDispInfo() {
        DCBDisplayInfo dcbDispInfo = new DCBDisplayInfo();
        List<String> reasonMasterCodes = new ArrayList<String>();
        List<String> reasonCategoryCodes = new ArrayList<String>();
       reasonMasterCodes.add(WaterTaxConstants.WATERTAXREASONCODE);
        
       // EgReasonCategory category=waterConnectionDetailsRepository.findReasonCategory();
        /*if(category!=null)
                reasonCategoryCodes.add(category.getCode());*/
       
        dcbDispInfo.setReasonCategoryCodes(reasonCategoryCodes);
        dcbDispInfo.setReasonMasterCodes(reasonMasterCodes);
        return dcbDispInfo;

}

}
