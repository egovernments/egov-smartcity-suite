package org.egov.lcms.web.controller.transactions;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.egov.commons.Functionary;
import org.egov.commons.dao.FunctionaryHibernateDAO;
import org.egov.lcms.masters.entity.CasetypeMaster;
import org.egov.lcms.masters.entity.CourtMaster;
import org.egov.lcms.masters.entity.CourtTypeMaster;
import org.egov.lcms.masters.entity.GovernmentDepartment;
import org.egov.lcms.masters.entity.PetitionTypeMaster;
import org.egov.lcms.masters.entity.enums.LCNumberType;
import org.egov.lcms.masters.service.CaseTypeMasterService;
import org.egov.lcms.masters.service.CourtMasterService;
import org.egov.lcms.masters.service.CourtTypeMasterService;
import org.egov.lcms.masters.service.GovernmentDepartmentService;
import org.egov.lcms.masters.service.PetitionTypeMasterService;
import org.egov.lcms.utils.constants.LcmsConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;

public class GenericLegalCaseController {

    @Autowired
    private CourtTypeMasterService courtTypeMasterService;

    @Autowired
    private CaseTypeMasterService caseTypeMasterService;
    
    @Autowired
    private GovernmentDepartmentService governmentDepartmentService;

    @Autowired
    private PetitionTypeMasterService petitiontypeMasterService;

    @Autowired
    private CourtMasterService courtMasterService;

    @Autowired
    private FunctionaryHibernateDAO functionaryDAO;

    public @ModelAttribute("courtTypeList") List<CourtTypeMaster> courtTypeList() {
        return courtTypeMasterService.getCourtTypeList();
    }

    public @ModelAttribute("courtsList") List<CourtMaster> courtList() {
        return courtMasterService.findAll();
    }
    
    public @ModelAttribute("govtDeptList") List<GovernmentDepartment> getGovtDeptList() {
        return governmentDepartmentService.findAll();
    }

    public @ModelAttribute("sectionlist") List<String> getFunctionaryList() {
        final List<String> funcLcmsList = new ArrayList<String>();
        final List<Functionary> functionaryList = functionaryDAO.findAllActiveFunctionary();
        for (final Functionary func : functionaryList)
            funcLcmsList.add("LC" + func.getCode());
        return funcLcmsList;
    }

    public @ModelAttribute("lcNumberTypes") Map<String, String> getLcNumberTypeTypes() {
        return getLcNumberTypesMap();
    }

    public Map<String, String> getLcNumberTypesMap() {
        final Map<String, String> connectionTypeMap = new LinkedHashMap<String, String>(0);
        connectionTypeMap.put(LCNumberType.AUTOMATED.toString(), LcmsConstants.LC_NUMBER_AUTOMATED_TYPE);
        connectionTypeMap.put(LCNumberType.MANUAL.toString(), LcmsConstants.LC_NUMBER_OPTIONAL_TYPE);
        return connectionTypeMap;
    }

    public @ModelAttribute("caseTypeList") List<CasetypeMaster> caseTypeList() {
        return caseTypeMasterService.getCaseTypeList();
    }

    public @ModelAttribute("petitiontypeList") List<PetitionTypeMaster> petitiontypeList() {
        return petitiontypeMasterService.getPetitiontypeList();
    }

    public @ModelAttribute("wPYearList") List<Integer> getWPYearList() {
        final List<Integer> wPYearList = new ArrayList<Integer>();
        int startYear = Integer.parseInt("1980");
        final Calendar cal = Calendar.getInstance();
        while (startYear <= cal.get(Calendar.YEAR)) {
            wPYearList.add(Integer.valueOf(startYear));
            startYear++;
        }
        return wPYearList;
    }

  
}
