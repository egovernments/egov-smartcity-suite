package org.egov.collection.integration.models;


/**
 * The bill receipt information class. Provides details of a bill receipt.
 */
public class PaymentInfoTest { /*extends AbstractPersistenceServiceTest{
	private CollectionObjectFactory objectFactory;
	
	@Before
	public void setupService(){
		objectFactory = new CollectionObjectFactory(session);
	}
	
	@Test
	public void testPaymentInfoBank(){
		Bankaccount account = objectFactory.createBankAccount("testGLCode");
		PaymentInfoBank paytInfoBank = new PaymentInfoBank();
		Date date = new Date();
		
		paytInfoBank.setBankAccountId(account.getId().longValue());
		paytInfoBank.setBankId(account.getBankbranch().getBank().getId().longValue());
		paytInfoBank.setInstrumentAmount(BigDecimal.valueOf(1000));
		paytInfoBank.setTransactionDate(date);
		paytInfoBank.setTransactionNumber(1001);
		
		assertEquals(paytInfoBank.getBankId(),account.getBankbranch().getBank().getId().longValue());
		assertEquals(paytInfoBank.getBankAccountId(),account.getId().longValue());
		assertEquals(paytInfoBank.getInstrumentAmount(),BigDecimal.valueOf(1000));
		assertEquals(paytInfoBank.getTransactionDate(),date);
		assertEquals(paytInfoBank.getTransactionNumber(),1001);
		assertEquals(paytInfoBank.getInstrumentType(),TYPE.bank);
	}
	
	@Test
	public void testPaymentInfoChequeDD(){
		Bankaccount account = objectFactory.createBankAccount("testGLCode");
		PaymentInfoChequeDD paytInfoChqDD = new PaymentInfoChequeDD();
		Date date = new Date();
		
		paytInfoChqDD.setBankId(account.getBankbranch().getBank().getId().longValue());
		paytInfoChqDD.setBranchName(account.getBankbranch().getBranchname());
		paytInfoChqDD.setInstrumentAmount(BigDecimal.valueOf(1000));
		paytInfoChqDD.setInstrumentDate(date);
		paytInfoChqDD.setInstrumentNumber("123456");
		paytInfoChqDD.setInstrumentType(TYPE.cheque);	
		
		assertEquals(paytInfoChqDD.getBankId(),account.getBankbranch().getBank().getId().longValue());
		assertEquals(paytInfoChqDD.getBranchName(),account.getBankbranch().getBranchname());
		assertEquals(paytInfoChqDD.getInstrumentAmount(),BigDecimal.valueOf(1000));
		assertEquals(paytInfoChqDD.getInstrumentDate(),date);
		assertEquals(paytInfoChqDD.getInstrumentNumber(),"123456");
		assertEquals(paytInfoChqDD.getInstrumentType(),TYPE.cheque);
	}
	
	@Test
	public void testPaymentInfoCash(){
		PaymentInfoCash paytInfoCash = new PaymentInfoCash();
		
		paytInfoCash.setInstrumentAmount(BigDecimal.valueOf(1000));
		
		assertEquals(paytInfoCash.getInstrumentAmount(),BigDecimal.valueOf(1000));
		assertEquals(paytInfoCash.getInstrumentType(),TYPE.cash);
	}
	
	@Test
	public void testPaymentInfoCard(){
		PaymentInfoCard paytInfoCard = new PaymentInfoCard();
		
		paytInfoCard.setInstrumentAmount(BigDecimal.valueOf(1000));
		paytInfoCard.setInstrumentNumber("123456");
		paytInfoCard.setTransactionNumber("7890*#");
		
		assertEquals(paytInfoCard.getInstrumentAmount(),BigDecimal.valueOf(1000));
		assertEquals(paytInfoCard.getInstrumentNumber(),"123456");
		assertEquals(paytInfoCard.getTransactionNumber(),"7890*#");
		assertEquals(paytInfoCard.getInstrumentType(),TYPE.card);
	}
	*/

}