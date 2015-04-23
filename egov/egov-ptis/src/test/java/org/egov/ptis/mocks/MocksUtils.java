package org.egov.ptis.mocks;

import static org.egov.ptis.constants.PropertyTaxConstants.STATUS_ISACTIVE;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.egov.commons.Installment;
import org.egov.dcb.bean.DCBDisplayInfo;
import org.egov.dcb.bean.DCBReport;
import org.egov.dcb.service.DCBService;
import org.egov.infstr.flexfields.model.EgAttributetype;
import org.egov.infstr.flexfields.model.EgAttributevalues;
import org.egov.infstr.utils.DateUtils;
import org.egov.lib.citizen.model.Owner;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.entity.UserImpl;
import org.egov.lib.rjbac.user.dao.UserDAO;
import org.egov.pims.commons.DesignationMaster;
import org.egov.ptis.domain.bill.PropertyTaxBillable;
import org.egov.ptis.domain.dao.demand.PtDemandDao;
import org.egov.ptis.domain.dao.property.BasicPropertyDAO;
import org.egov.ptis.domain.entity.demand.PTDemandCalculations;
import org.egov.ptis.domain.entity.demand.Ptdemand;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.BasicPropertyImpl;
import org.egov.ptis.domain.entity.property.Property;
import org.egov.ptis.domain.entity.property.PropertyAddress;
import org.egov.ptis.domain.entity.property.PropertyDetail;
import org.egov.ptis.domain.entity.property.PropertyImpl;
import org.egov.ptis.domain.entity.property.PropertyTypeMaster;
import org.egov.ptis.domain.entity.property.VacantProperty;
import org.egov.ptis.utils.PTISCacheManagerInteface;

public class MocksUtils {
    private BasicProperty basiProp;
    public static final  String NULLMSG = "Null";
    public static final  String NOTNULLMSG = "Not Null";
    public BasicProperty getBasiProp() {
        return basiProp;
    }

    public void setBasiProp(BasicProperty basiProp) {
        this.basiProp = basiProp;
    }

    public BasicPropertyDAO mockBasicPropertyDAO() {
        BasicPropertyDAO basicPropertyDAO = mock(BasicPropertyDAO.class);
        when(basicPropertyDAO.getAllBasicPropertyByPropertyID("08-119-0000-001")).thenReturn(getBasiProp());
        return basicPropertyDAO;
    }

    public BasicProperty mockBasicProperty() {
        BasicProperty basicProperty = mock(BasicPropertyImpl.class);
        when(basicProperty.getProperty()).thenReturn(null);
        basicProperty.setUpicNo("08-119-0000-001");
        basicProperty.setId(Long.valueOf(1));
        basicProperty.setAddress(preparePropertyAddress());
        Set<Property> propertySet = new HashSet<Property>();
        propertySet.add(prepareProperty());
        basicProperty.setPropertySet(propertySet);
        return basicProperty;
    }

    public PTISCacheManagerInteface mockPTISCacheManagerInteface() {
        PTISCacheManagerInteface ptisCacheManagerInteface = mock(PTISCacheManagerInteface.class);
        String pinCode = "1";
        when(ptisCacheManagerInteface.buildOwnerFullName(prepareProperty().getPropertyOwnerSet())).thenReturn(
                "checkOwnerName");
        when(ptisCacheManagerInteface.buildAddressByImplemetation(preparePropertyAddress())).thenReturn(pinCode);
        return ptisCacheManagerInteface;
    }

    public PropertyTaxBillable mockBillable() {
    	PropertyTaxBillable billable = mock(PropertyTaxBillable.class);
        billable.setBasicProperty(mockBasicProperty());
        return billable;
    }

    public DCBService mockDCBService() {
        DCBService dcbService = mock(DCBService.class);
        when(dcbService.getCurrentDCBAndReceipts((DCBDisplayInfo) anyObject())).thenReturn(
                prepareDCBReport());
        return dcbService;
    }

    public DCBReport prepareDCBReport() {
        DCBReport dcbReport = new DCBReport();
        List<String> fieldNames = new ArrayList<String>();
        fieldNames.add("TAX");
        fieldNames.add("PENALTY");
        dcbReport.setFieldNames(fieldNames);
        return dcbReport;
    }

    public DCBDisplayInfo prepareDisplayInfo() {
        DCBDisplayInfo dcbDispInfo = new DCBDisplayInfo();
        List<String> reasonCategoryCodes = new ArrayList<String>();
        reasonCategoryCodes.add("TAX");
        reasonCategoryCodes.add("PENALTY");
        dcbDispInfo.setReasonCategoryCodes(reasonCategoryCodes);
        return dcbDispInfo;
    }

    public PropertyDetail preparePropertyDetail() {
        PropertyDetail propertyDetail = new VacantProperty();
        propertyDetail.setPropertyTypeMaster(preparePropertyTypeMaster());
        return propertyDetail;
    }

    public PropertyTypeMaster preparePropertyTypeMaster() {
        PropertyTypeMaster propertyTypeMaster = new PropertyTypeMaster();
        propertyTypeMaster.setType("vacant");
        return propertyTypeMaster;
    }

    public Property prepareProperty() {
        PropertyImpl property = new PropertyImpl();
        Owner owner = new Owner();
        property.setId(Long.valueOf(1));
        Set propertyOwner = new HashSet();
        propertyOwner.add(owner);
        property.setPropertyOwnerSet(propertyOwner);
        owner.setFirstName("checkOwnerName");
        property.setStatus(STATUS_ISACTIVE);
        property.setIsDefaultProperty('Y');
        property.setInstallment(prepareInstallment());
        property.setPropertyDetail(preparePropertyDetail());
        return property;
    }

    public Installment prepareInstallment() {
        Installment installment = new Installment();
        installment.setId(Integer.valueOf(1));
        installment.setFromDate(DateUtils.getFinancialYear().getStartOnDate());
        installment.setToDate(DateUtils.getFinancialYear().getEndOnOnDate());
        return installment;
    }

    public PropertyAddress preparePropertyAddress() {
        PropertyAddress propertyAddress = new PropertyAddress();
        propertyAddress.setPinCode(1);
        return propertyAddress;
    }

    public PtDemandDao mockEGPTDemandDao() {
        PtDemandDao egptDemandDao = mock(PtDemandDao.class);
        when(egptDemandDao.getNonHistoryDemandForProperty(prepareProperty())).thenReturn(prepareEgptPtdemand());
        return egptDemandDao;
    }

    public Ptdemand prepareEgptPtdemand() {
        Ptdemand egptPtDemand = new Ptdemand();
        egptPtDemand.setEgptProperty(prepareProperty());
        egptPtDemand.setDmdCalculations(preparePTDemandCalculations());
        egptPtDemand.setIsHistory("N");
        egptPtDemand.setBaseDemand(BigDecimal.TEN);
        return egptPtDemand;
    }

    public PTDemandCalculations preparePTDemandCalculations() {
        PTDemandCalculations ptDemandCalculations = new PTDemandCalculations();
        ptDemandCalculations.setAttributeValues(preapreEGAttributeValues());
        return ptDemandCalculations;
    }

    public Set<EgAttributevalues> preapreEGAttributeValues() {
        EgAttributevalues egAttributeValues = new EgAttributevalues();
        Set<EgAttributevalues> egAttributeValue = new HashSet<EgAttributevalues>();
        egAttributeValues.setAttValue("0");
        egAttributeValues.setEgAttributetype(prepareEgAttributeType());
        egAttributeValue.add(egAttributeValues);
        return egAttributeValue;
    }

    private EgAttributetype prepareEgAttributeType() {
        EgAttributetype egAttributeType = new EgAttributetype();
        egAttributeType.setAttName("ANNUALVALUE");
        return egAttributeType;
    }

    private DesignationMaster prepareDesignationMstr() {
        DesignationMaster disignationMaster = new DesignationMaster();
        disignationMaster.setDesignationName("citizenUser");
        return disignationMaster;
    }

    public UserDAO mockUserDao() {
        UserDAO userDao = mock(UserDAO.class);
        when(userDao.getUserByUserName("citizenUser")).thenReturn(prepareUser());
        return userDao;
    }

    public User prepareUser() {
        UserImpl user = new UserImpl();
        user.setId(50);
        user.setFirstName("citizenUser");
        user.setUserName("citizenUser");
        return user;
    }
}
