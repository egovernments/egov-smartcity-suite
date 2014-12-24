package org.egov.dcb.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.commons.Installment;
import org.egov.dcb.bean.DCBDisplayInfo;
import org.egov.dcb.bean.DCBRecord;
import org.egov.dcb.bean.DCBReport;
import org.egov.dcb.bean.Receipt;
import org.egov.demand.interfaces.Billable;
import org.egov.demand.mocks.MocksUtils;
import org.egov.demand.model.BillReceipt;
import org.egov.demand.model.EgDemand;
import org.egov.demand.model.EgDemandReasonMaster;
import org.egov.infstr.commons.Module;
import org.egov.infstr.junit.EgovHibernateTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class DCBServiceImplTest {

    private transient EgovHibernateTest egovHibernateTest;

    private transient DCBDisplayInfo dcbDispInfo = null;

    private transient DCBServiceImpl dcbserviceimpl = null;
    private transient Module mockModule = null;
    private transient List<EgDemandReasonMaster> reasonMasters = null;
    private transient List<String> rsonMsterCodes = null;
    private transient Map<String, BigDecimal> demand = null;
    private transient Map<String, BigDecimal> collections = null;
    private transient Map<String, BigDecimal> rebates = null;
    private transient Object[] object = null;
    private transient Map<Installment, DCBRecord> dcbReportMap = null;
    private transient List<String> fieldNames = null;
    private transient List dcbList = null;
    private transient DCBReport dcbReport = null;
    private transient DCBRecord dcbRecord = null;
    private static final transient String NOTNULLMSG = "Not Null";
    private static final transient String NULLMSG = "Null";
    private transient String reason = null;
    private static final transient String EMPTYLISTMSG = "List is Empty";
    private static final transient String NONEMPTYMSG = "List is Empty";
    private static final transient String assertEqualsMESSAGE = "X equals to Y";
    private transient MocksUtils objectMocks = null;
    private transient EgDemand egDemandMock = null;
    private transient Installment installMock = null;
    private transient BillReceipt billReceipt = null;
    private transient Billable billable = null;
    Map receipts=null;

    @Before
    public void setUp() throws Exception {
        egovHibernateTest = new EgovHibernateTest();
        egovHibernateTest.setUp();
        objectMocks = new MocksUtils();
        billable = objectMocks.prepareBillable();
        dcbserviceimpl = new DCBServiceImpl(billable);
        mockSettersForServiceImpl(dcbserviceimpl, objectMocks);
        dcbDispInfo = prepareDCBInfoObject();
        reasonMasters = new ArrayList<EgDemandReasonMaster>();
        reasonMasters.add(objectMocks.prepareReasonMaster());
        rsonMsterCodes = new ArrayList<String>();
        demand = new HashMap<String, BigDecimal>();
        collections = new HashMap<String, BigDecimal>();
        rebates = new HashMap<String, BigDecimal>();
        dcbReportMap = new HashMap<Installment, DCBRecord>();
        object = prepareObjectArray();
        dcbReport = new DCBReport();
        fieldNames = new ArrayList<String>();
        fieldNames.add(objectMocks.prepareEgCategoryReason().getCode());
        dcbList = prepareDCBList();
        egDemandMock = objectMocks.prepareEgDemand();
        installMock = objectMocks.prepareInstallment();
        mockModule = objectMocks.prepareModule();
        billReceipt = objectMocks.prepareBillReceipt();
        receipts=new HashMap<Installment, List<Receipt>>();
    }

    @After
    public void tearDown() throws Exception {
        egovHibernateTest.tearDown();
    }

    @Test
    public void getEgdemandReasonMastersWithNullModule() {
        reasonMasters = dcbserviceimpl.getEgdemandReasonMasters(null);
        assertNull(NULLMSG, reasonMasters);
    }

    @Test
    public void getModuleFromReasonMasterWithNull() {
        mockModule = dcbserviceimpl.getModuleFromDemand(null);
        assertNull(NULLMSG, mockModule);
    }

    @Test
    public void getModuleFromReasonMasterWithData() {
        mockModule = dcbserviceimpl.getModuleFromDemand(egDemandMock);
        assertEquals(assertEqualsMESSAGE, mockModule.getId(), Integer.valueOf(113));
    }

    @Test
    public void getModuleFromReasonMasterWithNullParams() {
        reasonMasters = dcbserviceimpl.getEgdemandReasonMasters(null, null);
        assertNull(NULLMSG, reasonMasters);
    }

    @Test
    public void getModuleFromReasonMasterWithModuleNull() {
        reasonMasters = dcbserviceimpl.getEgdemandReasonMasters(dcbDispInfo.getReasonMasterCodes(),
                null);
        assertNull(NULLMSG, reasonMasters);
    }

    @Test
    public void getModuleFromReasonMasterWithListNull() {
        reasonMasters = dcbserviceimpl.getEgdemandReasonMasters(null, objectMocks.prepareModule());
        assertNull(NULLMSG, reasonMasters);
    }

    @Test
    public void getEgdemandReasonMastersWithData() {
        reasonMasters = dcbserviceimpl.getEgdemandReasonMasters(dcbDispInfo.getReasonMasterCodes(),
                objectMocks.prepareModule());
        assertEquals(assertEqualsMESSAGE, reasonMasters.get(0).getCode(), "GENTAX-RESD");
    }

    @Test
    public void prepareReasonMastersWithNull() {
        reasonMasters = dcbserviceimpl.prepareReasonMasters(null, null);
        assertNull(NULLMSG, reasonMasters);
    }

    @Test
    public void prepareReasonMastersWithDemandNull() {
        reasonMasters = dcbserviceimpl.prepareReasonMasters(null, dcbDispInfo);
        assertNull(NULLMSG, reasonMasters);
    }

    @Test
    public void prepareReasonMastersWithDispInfoNull() {

        reasonMasters = dcbserviceimpl.prepareReasonMasters(egDemandMock, null);
        assertEquals(assertEqualsMESSAGE, reasonMasters.get(0).getCode(), "GENTAX-RESD");
    }

    @Test
    public void prepareReasonMastersWithData() {
        reasonMasters = dcbserviceimpl.prepareReasonMasters(egDemandMock, dcbDispInfo);
        assertEquals(assertEqualsMESSAGE, reasonMasters.get(0).getCode(), "GENTAX-RESD");
    }

    @Test
    public void prepareFieldNamesWithDataNull() {
        rsonMsterCodes = dcbserviceimpl.prepareFieldNames(null, null);
        assertTrue(NONEMPTYMSG, rsonMsterCodes.isEmpty());
    }

    @Test
    public void prepareFieldNamesWithDataListNull() {
        rsonMsterCodes = dcbserviceimpl.prepareFieldNames(null, dcbDispInfo);
        assertTrue(NONEMPTYMSG, rsonMsterCodes.isEmpty());
    }

    @Test
    public void prepareFieldNamesWithDataDispInfoNull() {
        rsonMsterCodes = dcbserviceimpl.prepareFieldNames(reasonMasters, null);
        assertFalse(EMPTYLISTMSG, rsonMsterCodes.isEmpty());
    }

    @Test
    public void prepareFieldNamesWithData() {
        reasonMasters = dcbserviceimpl.prepareReasonMasters(egDemandMock, dcbDispInfo);
        rsonMsterCodes = dcbserviceimpl.prepareFieldNames(reasonMasters, dcbDispInfo);
        assertEquals(assertEqualsMESSAGE, reasonMasters.get(0).getCode(), "GENTAX-RESD");
    }

    @Test
    public void prepareDemandAndCollectionMapWithNull() {
        dcbserviceimpl.initDemandAndCollectionMap(null, demand, collections, rebates);
        assertTrue(NONEMPTYMSG, demand.isEmpty());
    }

    @Test
    public void prepareDemandAndCollectionMapWithData() {
        rsonMsterCodes = dcbserviceimpl.prepareFieldNames(reasonMasters, dcbDispInfo);
        dcbserviceimpl.initDemandAndCollectionMap(rsonMsterCodes, demand, collections, rebates);
        assertFalse(EMPTYLISTMSG, demand.isEmpty());
    }

    @Test
    public void getReasonWithNull() {
        reason = dcbserviceimpl.getReason(null, null);
        assertEquals(assertEqualsMESSAGE, reason, "");
    }

    @Test
    public void getReasonWithObjectNull() {
        reason = dcbserviceimpl.getReason(null, dcbserviceimpl.prepareFieldNames(reasonMasters,
                dcbDispInfo));
        assertEquals(assertEqualsMESSAGE, reason, "");
    }

    @Test
    public void getReasonWithListNull() {
        reason = dcbserviceimpl.getReason(object, null);
        assertEquals(assertEqualsMESSAGE, reason, "");
    }

    @Test
    public void getReasonWithData() {
        reason = dcbserviceimpl.getReason(object, dcbserviceimpl.prepareFieldNames(reasonMasters,
                dcbDispInfo));
        assertEquals(assertEqualsMESSAGE, reason, "TAX");
    }

    @Test
    public void prepareDCMapWithNullData() {
        dcbRecord = dcbserviceimpl.prepareDCMap(null, null, null, null, null, null, null);
        assertEquals(assertEqualsMESSAGE, dcbRecord, null);
    }

    @Test
    public void prepareDCMapWithObjectNull() {
        dcbRecord = dcbserviceimpl.prepareDCMap(null, dcbRecord, demand, collections, rebates,
                egDemandMock, fieldNames);
        assertEquals(assertEqualsMESSAGE, dcbRecord, null);
    }

    @Test
    public void prepareDCMapWithDemandNull() {
        dcbRecord = dcbserviceimpl.prepareDCMap(object, dcbRecord, demand, collections, rebates, null,
                fieldNames);
        assertNull(NULLMSG, dcbRecord);
    }

    @Ignore
    public void prepareDCMapWithData() {
        dcbserviceimpl.initDemandAndCollectionMap(fieldNames, demand, collections, rebates);
        dcbRecord = dcbserviceimpl.prepareDCMap(object, dcbRecord, demand, collections, rebates,
                egDemandMock, fieldNames);
        assertFalse(EMPTYLISTMSG, dcbRecord.getDemands().isEmpty());
    }

    @Test
    public void iterateDCBWithNull() {
        dcbReportMap = dcbserviceimpl.iterateDCB(null, null, null, null);
        assertEquals(assertEqualsMESSAGE, dcbReportMap, null);
    }

    @Test
    public void iterateDCBWithDCBListNull() {
        dcbReportMap = dcbserviceimpl.iterateDCB(null, dcbReportMap, fieldNames, egDemandMock);
        assertTrue(NONEMPTYMSG, dcbReportMap.isEmpty());
    }

    @Test
    public void iterateDCBWithFieldListNull() {
        dcbReportMap = dcbserviceimpl.iterateDCB(dcbList, dcbReportMap, null, egDemandMock);
        assertTrue(NONEMPTYMSG, dcbReportMap.isEmpty());
    }

    @Ignore
    public void iterateDCBWithData() {
        dcbReportMap = dcbserviceimpl.iterateDCB(dcbList, dcbReportMap, fieldNames, egDemandMock);
        assertEquals(assertEqualsMESSAGE, dcbReportMap.containsKey(installMock), true);
    }

    @Ignore
    public void getBalances() {
        demand.put(fieldNames.get(0), BigDecimal.ZERO);
        collections.put(fieldNames.get(0), BigDecimal.ZERO);
        rebates.put(fieldNames.get(0), BigDecimal.ZERO);
        dcbRecord = dcbserviceimpl.prepareDCMap(object, dcbRecord, demand, collections, rebates,
                egDemandMock, fieldNames);
        assertEquals(dcbRecord.getBalances().get("TAX"), new BigDecimal(495));
    }

    private Object[] prepareObjectArray() {
        Object[] objectArray = new Object[6];
        objectArray[4] = "1";
        objectArray[3] = objectMocks.prepareReasonMaster().getId();
        objectArray[1] = new BigDecimal(1000);
        objectArray[2] = new BigDecimal(505);

        return objectArray;
    }

    private DCBDisplayInfo prepareDCBInfoObject() {
        DCBDisplayInfo dcbInfoObj = mock(DCBDisplayInfo.class);
        List<String> rsonCtegryCodes = new ArrayList<String>();
        List<String> reasonMasterCodes = new ArrayList<String>();

        rsonCtegryCodes.add("TAX");
        rsonCtegryCodes.add("PENALTY");

        reasonMasterCodes.add("GENTAX-RESD");
        /*
         * reasonMasterCodes.add("EDUTAX-RESD");
         * reasonMasterCodes.add("LIGHTAX-RESD");
         * reasonMasterCodes.add("LIBCESS-RESD");
         * reasonMasterCodes.add("PENALTY");
         */

        when(dcbInfoObj.getReasonCategoryCodes()).thenReturn(rsonCtegryCodes);
        when(dcbInfoObj.getReasonMasterCodes()).thenReturn(reasonMasterCodes);

        return dcbInfoObj;
    }

    private void mockSettersForServiceImpl(DCBServiceImpl dcbserviceimpl, MocksUtils objectMocks) {
        dcbserviceimpl.setDmdGenDao(objectMocks.mockDemandGenDAO());
        dcbserviceimpl.setReasonMasterDAO(objectMocks.mockEgDemandReasonMasterDao());
        dcbserviceimpl.setReasonCategoryDAO(objectMocks.mockEgReasonCategoryDao());
        dcbserviceimpl.setInstalDAO(objectMocks.mockInstallmentDAO());

    }

    private List prepareDCBList() {
        List dcbList = new ArrayList();

        Object a[] = new Object[6];

        a[0] = Integer.valueOf(1);
        a[1] = BigDecimal.valueOf(1000);
        a[2] = BigDecimal.valueOf(800);
        a[3] = Long.valueOf(3);
        a[4] = Long.valueOf(3);

        dcbList.add(a);
        return dcbList;
    }
}
