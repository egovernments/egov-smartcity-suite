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