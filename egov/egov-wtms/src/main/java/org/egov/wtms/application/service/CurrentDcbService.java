package org.egov.wtms.application.service;

import java.util.ArrayList;
import java.util.List;

import org.egov.dcb.bean.DCBDisplayInfo;
import org.egov.wtms.application.repository.WaterConnectionDetailsRepository;
import org.egov.wtms.utils.constants.WaterTaxConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CurrentDcbService {

   
    public DCBDisplayInfo getDcbDispInfo() {
        final DCBDisplayInfo dcbDispInfo = new DCBDisplayInfo();
        final List<String> reasonMasterCodes = new ArrayList<String>();
        final List<String> reasonCategoryCodes = new ArrayList<String>();
        reasonMasterCodes.add(WaterTaxConstants.WATERTAXREASONCODE);
        dcbDispInfo.setReasonCategoryCodes(reasonCategoryCodes);
        dcbDispInfo.setReasonMasterCodes(reasonMasterCodes);
        return dcbDispInfo;

    }

}
