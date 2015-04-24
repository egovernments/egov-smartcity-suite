package org.egov.erpcollection.scheduler;

import static org.junit.Assert.assertEquals;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.egov.commons.EgwStatus;
import org.egov.erpcollection.integration.models.BillReceiptInfo;
import org.egov.erpcollection.integration.models.BillReceiptInfoImpl;
import org.egov.erpcollection.models.CollectionObjectFactory;
import org.egov.erpcollection.models.ReceiptHeader;
import org.egov.erpcollection.models.ReceiptPayeeDetails;
import org.egov.erpcollection.models.ReceiptVoucher;
import org.egov.erpcollection.services.ReceiptService;
import org.egov.erpcollection.util.CollectionsNumberGenerator;
import org.egov.erpcollection.web.constants.CollectionConstants;
import org.egov.infstr.commons.dao.GenericHibernateDaoFactory;
import org.egov.infstr.config.AppData;
import org.egov.infstr.config.dao.AppDataDAO;
import org.egov.infstr.config.dao.AppDataHibernateDAO;
import org.egov.infstr.models.Script;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.services.ScriptService;
import org.egov.infstr.utils.SequenceNumberGenerator;
import org.egov.model.instrument.InstrumentType;
import org.egov.model.instrument.InstrumentVoucher;
import org.egov.models.AbstractPersistenceServiceTest;
import org.egov.services.instrument.InstrumentService;
import org.hibernate.Session;
import org.junit.Before;
import org.junit.Test;
import org.junit.Ignore;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleTrigger;
import org.quartz.impl.StdSchedulerFactory;

public class UpdateDishonoredInstrumentsJobTest extends AbstractPersistenceServiceTest<ReceiptPayeeDetails, Long>  {
	private CollectionObjectFactory objectFactory;
	private InstrumentService instrService;
	private UpdateDishonoredInstrumentsJob delegate;
	private ReceiptService receiptService;
	private GenericHibernateDaoFactory genericDao;
	private CollectionsNumberGenerator collectionsNumberGenerator;
	private SequenceNumberGenerator sequenceGenerator;
	private ScriptService scriptExecutionService;
	
	@Before
	public void setupService(){

		objectFactory = new CollectionObjectFactory(session);
		service.setSessionFactory(egovSessionFactory);
		
		instrService = new InstrumentService();
		instrService.setPersistenceService(genericService);
		receiptService = new ReceiptService()
		{
			public Boolean updateBillingSystem(String serviceCode,Set<BillReceiptInfo> billReceipts){
				return true;
			}
		};
		receiptService.setSessionFactory(egovSessionFactory);
		
		PersistenceService<Script, Long> scriptService = new PersistenceService<Script, Long>();
		scriptService.setSessionFactory(egovSessionFactory);
		
		collectionsNumberGenerator=new CollectionsNumberGenerator();

		sequenceGenerator = new SequenceNumberGenerator(egovSessionFactory);
		collectionsNumberGenerator.setSequenceGenerator(sequenceGenerator);

		scriptExecutionService = new ScriptService(2, 5, 10, 30);
		scriptExecutionService.setSessionFactory(egovSessionFactory);
		collectionsNumberGenerator.setScriptExecutionService(scriptExecutionService);
		
		receiptService.setCollectionsNumberGenerator(collectionsNumberGenerator);
		
		genericDao = new GenericHibernateDaoFactory(){
			protected Session getCurrentSession(){
				return session;
			}
			public  AppDataDAO getAppDataDAO()
			{
				return new AppDataHibernateDAO(AppData.class,session);
			}
		};
		delegate = new UpdateDishonoredInstrumentsJob();
		delegate.setPersistenceService(genericService);
		delegate.setInstrumentService(instrService);
		delegate.setReceiptPayeeDetailsService(receiptService);
		delegate.setGenericDao(genericDao);
	}
	
	/**
	 * This test case basically tests the functionality of the EGI method 
	 * getValueByModuleAndKey
	 */
	@Test
	public void testEGIGetAppDataForKeyAndModule(){		
		AppData appData = objectFactory.createAppData(
				"testCollections",
				"testKey",String.valueOf(new Date()));
		
		AppData savedAppData = genericDao.getAppDataDAO().getValueByModuleAndKey(
				"testCollections","testKey");
		assertEquals(appData.getId(),savedAppData.getId());
		assertEquals(appData.getValue(),savedAppData.getValue());
	}
	
	@Test
	public void testInvokeEgovJobScheduler() throws SchedulerException{
		SchedulerFactory sf =new StdSchedulerFactory("test-quartz.properties");
		
		Scheduler scheduler = sf.getScheduler();
		scheduler.start();
		
		JobDetail jobDetail = new JobDetail("testScheduler",Scheduler.DEFAULT_GROUP,UpdateDishonoredInstrumentsJob.class);
		SimpleTrigger simpleTrigger = new SimpleTrigger("testTrigger",Scheduler.DEFAULT_GROUP,
                new Date());
		jobDetail.setRequestsRecovery(true);
		scheduler.scheduleJob(jobDetail,simpleTrigger);
	}
	
	
	@Ignore
	public void testProcessDishonoredInstruments() throws ParseException {
		Date bouncedDate = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	    String strDate = sdf.format(bouncedDate);
	    bouncedDate = sdf.parse(strDate);
			
	    EgwStatus instrStatus = objectFactory.createEgwStatus(
	    		CollectionConstants.INSTRUMENT_DISHONORED_STATUS,
	    		CollectionObjectFactory.MODULE_NAME_TESTINSTRUMENTSTATUS);
	    
	    InstrumentType instrumentType = objectFactory.createInstrumentType("testInstrumentType");
		InstrumentVoucher instrVoucher=objectFactory.createInstrumentVoucher(
				instrStatus,instrumentType);
		objectFactory.createInstrumentOtherDetails(
				instrVoucher,bouncedDate);
		ReceiptVoucher receiptVoucher = objectFactory.createReceiptVoucher(
				instrVoucher.getVoucherHeaderId());
		
					
		delegate.executeJob();
		
		/*for(ReceiptHeader receiptHeader:delegate.getReceiptHeaders()){
			assertTrue(receiptHeader.getEgwStatus().getCode().equals(
					CollectionConstants.RECEIPT_STATUS_CODE_INSTRUMENT_BOUNCED));
		}*/
		
		assert(populateBillDetails(Arrays.asList(receiptVoucher.getReceiptHeader())).
				contains(delegate.getBillReceipts()));
		
			
		AppData appData2 = (AppData) genericService.find(
				"from AppData a where a.module =?  and a.key=?", 
				CollectionConstants.MODULE_NAME_COLLECTIONS,
				CollectionConstants.BOUNCEDINSTRUPDATE_RECONDATE);
		//assertEquals(strDate,appData2.getValue());
	}
	
	@Ignore
	public void testProcessDishonoredInstrumentsFailure() throws ParseException {
		
		receiptService = new ReceiptService(){
			public Boolean updateBillingSystem(String serviceCode,Set<BillReceiptInfo> billReceipts){
				return false;
			}
		};
		receiptService.setSessionFactory(egovSessionFactory);
		delegate.setReceiptPayeeDetailsService(receiptService);
		
		Date bouncedDate = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	    String strDate = sdf.format(bouncedDate);
	    bouncedDate = sdf.parse(strDate);
	    AppData appData1 = (AppData) genericService.find(
				"from AppData a where a.module =?  and a.key=?", 
				CollectionConstants.MODULE_NAME_COLLECTIONS,
				CollectionConstants.BOUNCEDINSTRUPDATE_RECONDATE);
			
	    EgwStatus instrStatus = objectFactory.createEgwStatus(
	    		CollectionConstants.INSTRUMENT_DISHONORED_STATUS,
	    		CollectionObjectFactory.MODULE_NAME_TESTINSTRUMENTSTATUS);
	    
	    InstrumentType instrumentType = objectFactory.createInstrumentType("testInstrumentType");
		InstrumentVoucher instrVoucher=objectFactory.createInstrumentVoucher(
				instrStatus,instrumentType);
		objectFactory.createInstrumentOtherDetails(instrVoucher,bouncedDate);
		ReceiptVoucher receiptVoucher = objectFactory.createReceiptVoucher(
				instrVoucher.getVoucherHeaderId());
				
		delegate.processDishonoredInstruments();
		
		// TO DO : Test this scenario
		/*for(ReceiptPayeeDetails payee : delegate.getReceiptPayeeDetails()){
			for(ReceiptHeader receiptHeader:payee.getReceiptHeaders()){
				assertTrue(receiptHeader.getEgwStatus().getCode().equals(
						CollectionConstants.RECEIPT_STATUS_CODE_INSTRUMENT_BOUNCED));
			}
		}*/
		
		assert(populateBillDetails(Arrays.asList(receiptVoucher.getReceiptHeader())).
				contains(delegate.getBillReceipts()));
		
		AppData appData2 = (AppData) genericService.find(
				"from AppData a where a.module =?  and a.key=?", 
				CollectionConstants.MODULE_NAME_COLLECTIONS,
				CollectionConstants.BOUNCEDINSTRUPDATE_RECONDATE);
		
		// assert that the recon date has not changed
		assertEquals(appData1.getValue(),appData2.getValue());
	}
	
	
	@Ignore
	public void testProcessDishonoredInstrumentsMulitpleServicesSuccess() throws ParseException {
		
		receiptService = new ReceiptService(){
			public Boolean updateBillingSystem(String serviceCode,Set<BillReceiptInfo> billReceipts){
				return true;
			}
		};
		receiptService.setSessionFactory(egovSessionFactory);
		
		PersistenceService<Script, Long> scriptService = new PersistenceService<Script, Long>();
		scriptService.setSessionFactory(egovSessionFactory);
		
		sequenceGenerator = new SequenceNumberGenerator(egovSessionFactory);
		
		collectionsNumberGenerator=new CollectionsNumberGenerator();
		collectionsNumberGenerator.setScriptExecutionService(scriptExecutionService);
		collectionsNumberGenerator.setSequenceGenerator(sequenceGenerator);
		
		
		receiptService.setCollectionsNumberGenerator(collectionsNumberGenerator);
		
		delegate.setReceiptPayeeDetailsService(receiptService);
		
		Date bouncedDate = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	    String strDate = sdf.format(bouncedDate);
	    bouncedDate = sdf.parse(strDate);

	    EgwStatus instrStatus = objectFactory.createEgwStatus(
	    		CollectionConstants.INSTRUMENT_DISHONORED_STATUS,
	    		CollectionObjectFactory.MODULE_NAME_TESTINSTRUMENTSTATUS);
	    InstrumentType instrumentType = objectFactory.createInstrumentType("testInstrType");
		InstrumentVoucher instrVoucher1=objectFactory.createInstrumentVoucher(
				instrStatus,instrumentType);
		objectFactory.createInstrumentOtherDetails(
				instrVoucher1,bouncedDate);
		ReceiptVoucher receiptVoucher1 = objectFactory.createReceiptVoucher(
				instrVoucher1.getVoucherHeaderId());
		
		InstrumentVoucher instrVoucher2=objectFactory.createInstrumentVoucher(
				instrStatus,instrumentType);
		objectFactory.createInstrumentOtherDetails(
				instrVoucher2,bouncedDate);
		ReceiptVoucher receiptVoucher2 = objectFactory.createReceiptVoucher(
				instrVoucher2.getVoucherHeaderId());
		receiptVoucher2.getReceiptHeader().setService(
				objectFactory.createServiceDetails("testService2"));
		session.saveOrUpdate(receiptVoucher2.getReceiptHeader());
		
		delegate.processDishonoredInstruments();
		
		// TO DO : Test this scenario
		/*for(ReceiptPayeeDetails payee : delegate.getReceiptPayeeDetails()){
			for(ReceiptHeader receiptHeader:payee.getReceiptHeaders()){
				
				//to ensure that comparison is only for receipts created by test data
				if(receiptHeader.getId()==receiptVoucher1.getReceiptHeader().getId() ||
						receiptHeader.getId()==receiptVoucher2.getReceiptHeader().getId()){
					assertTrue(receiptHeader.getEgwStatus().getCode().equals(
							CollectionConstants.RECEIPT_STATUS_CODE_INSTRUMENT_BOUNCED));
				}
			}
		}*/
		
		assert(populateBillDetails(Arrays.asList(receiptVoucher1.getReceiptHeader())).
				contains(delegate.getBillReceipts()));
		
		assert(populateBillDetails(Arrays.asList(receiptVoucher2.getReceiptHeader())).
				contains(delegate.getBillReceipts()));
		
		AppData appData2 = (AppData) genericService.find(
				"from AppData a where a.module =?  and a.key=?", 
				CollectionConstants.MODULE_NAME_COLLECTIONS,
				CollectionConstants.BOUNCEDINSTRUPDATE_RECONDATE);
		// assert that the recon date has changed
		//assertEquals(appData2.getValue(),strDate);
		
	}
	
	@Ignore
	public void testProcessDishonoredInstrumentsMultipleServicesFailure() throws ParseException {
		
		receiptService = new ReceiptService(){
			boolean flag = false;
			public Boolean updateBillingSystem(String serviceCode,Set<BillReceiptInfo> billReceipts){
				return flag=!flag;
			}
		};
		receiptService.setSessionFactory(egovSessionFactory);
		
		PersistenceService<Script, Long> scriptService = new PersistenceService<Script, Long>();
		scriptService.setSessionFactory(egovSessionFactory);
		
		sequenceGenerator = new SequenceNumberGenerator(egovSessionFactory);
		
		collectionsNumberGenerator=new CollectionsNumberGenerator();
		collectionsNumberGenerator.setScriptExecutionService(scriptExecutionService);
		collectionsNumberGenerator.setSequenceGenerator(sequenceGenerator);
		
		
		receiptService.setCollectionsNumberGenerator(collectionsNumberGenerator);
		
		
		delegate.setReceiptPayeeDetailsService(receiptService);
		
		Date bouncedDate = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	    String strDate = sdf.format(bouncedDate);
	    bouncedDate = sdf.parse(strDate);
	    AppData appData1 = (AppData) genericService.find(
				"from AppData a where a.module =?  and a.key=?", 
				CollectionConstants.MODULE_NAME_COLLECTIONS,
				CollectionConstants.BOUNCEDINSTRUPDATE_RECONDATE);
	    String value=appData1.getValue();
			
	    EgwStatus instrStatus = objectFactory.createEgwStatus(
	    		CollectionConstants.INSTRUMENT_DISHONORED_STATUS,
	    		CollectionObjectFactory.MODULE_NAME_TESTINSTRUMENTSTATUS);
	    InstrumentType instrumentType = objectFactory.createInstrumentType("testInstrType");
		InstrumentVoucher instrVoucher1=objectFactory.createInstrumentVoucher(
				instrStatus,instrumentType);
		objectFactory.createInstrumentOtherDetails(
				instrVoucher1,bouncedDate);
		ReceiptVoucher receiptVoucher1 = objectFactory.createReceiptVoucher(
				instrVoucher1.getVoucherHeaderId());
		
		InstrumentVoucher instrVoucher2=objectFactory.createInstrumentVoucher(
				instrStatus,instrumentType);
		objectFactory.createInstrumentOtherDetails(
				instrVoucher2,bouncedDate);
		ReceiptVoucher receiptVoucher2 = objectFactory.createReceiptVoucher(
				instrVoucher2.getVoucherHeaderId());
		receiptVoucher2.getReceiptHeader().setService(
				objectFactory.createServiceDetails("testService2"));
		session.saveOrUpdate(receiptVoucher2.getReceiptHeader());
		
		delegate.processDishonoredInstruments();
		
		// TO DO : Test this scenario
		
		/*for(ReceiptPayeeDetails payee : delegate.getReceiptPayeeDetails()){
			for(ReceiptHeader receiptHeader:payee.getReceiptHeaders()){
				
				//to ensure that comparison is only for receipts created by test data
				if(receiptHeader.getId()==receiptVoucher1.getReceiptHeader().getId() ||
						receiptHeader.getId()==receiptVoucher2.getReceiptHeader().getId()){
					assertTrue(receiptHeader.getEgwStatus().getCode().equals(
					CollectionConstants.RECEIPT_STATUS_CODE_INSTRUMENT_BOUNCED));
				}
			}
		}*/
		
		assert(populateBillDetails(Arrays.asList(receiptVoucher1.getReceiptHeader())).
				contains(delegate.getBillReceipts()));
		
		assert(populateBillDetails(Arrays.asList(receiptVoucher2.getReceiptHeader())).
				contains(delegate.getBillReceipts()));
		
		AppData appData2 = (AppData) genericService.find(
				"from AppData a where a.module =?  and a.key=?", 
				CollectionConstants.MODULE_NAME_COLLECTIONS,
				CollectionConstants.BOUNCEDINSTRUPDATE_RECONDATE);
		// assert that the recon date has not changed
		//assertEquals(appData2.getValue(),value);
		
	}
	
	private Set<BillReceiptInfo> populateBillDetails(List<ReceiptHeader> receiptHeaders){
		Set<BillReceiptInfo> billReceipts = new HashSet<BillReceiptInfo>();
		for (ReceiptHeader receiptHeader:receiptHeaders)
		{
			BillReceiptInfoImpl billReceipt = new BillReceiptInfoImpl(receiptHeader);
			billReceipts.add(billReceipt);
		}
		return billReceipts;
	}
}
