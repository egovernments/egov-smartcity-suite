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

package org.egov.collection.entity;

public class ChallanInfoTest  { /*extends AbstractPersistenceServiceTest {
	private CollectionObjectFactory objectFactory;
	private EgovCommon egovCommon;
	
	@Before
	public void setUp() {
		objectFactory = new CollectionObjectFactory(session);
		egovCommon = createMock(EgovCommon.class);
	}
	
	@Test
	public void testChallanInfoWithChildObj() throws ApplicationException
	{
		ReceiptHeader receiptHeader=objectFactory.createReceiptHeaderForChallan();
		ReceiptHeader receiptHeaderref=objectFactory.createReceiptHeaderWithChallan();
		EasyMock.expect(egovCommon.getEntityType(EasyMock.isA(Accountdetailtype.class), EasyMock.isA(Integer.class))).
			andReturn(null).anyTimes();
		replay(egovCommon);
		
		ChallanInfo challanInfo = new ChallanInfo(receiptHeader,egovCommon,receiptHeaderref);
		
		assertEquals(challanInfo.getAccountDetails().iterator().next().getGlCode().substring(0, 6),
				receiptHeader.getReceiptDetails().iterator().next().getAccounthead().getGlcode().substring(0, 6));
		assertEquals(challanInfo.getAccountPayeeDetails().iterator().next().getGlCode(),
				receiptHeader.getReceiptDetails().iterator().next().
					getAccountPayeeDetails().iterator().next().
						getReceiptDetail().getAccounthead().getGlcode());
		assertTrue(challanInfo.getInstrumentDetails().isEmpty());
		
		assertEquals(challanInfo.getBillReferenceNum(),receiptHeader.getReferencenumber());
		assertEquals(challanInfo.getDescription(),receiptHeader.getReferenceDesc());
		assertEquals(challanInfo.getFunctionName(),receiptHeader.getReceiptDetails().iterator().next().getFunction().getName());
		assertEquals(challanInfo.getPayeeAddress(),receiptHeader.getReceiptPayeeDetails().getPayeeAddress());
		assertEquals(challanInfo.getPayeeName(),receiptHeader.getReceiptPayeeDetails().getPayeename());
		assertEquals(challanInfo.getReceiptMisc(),receiptHeader.getReceiptMisc());
		assertEquals(challanInfo.getServiceName(),receiptHeader.getService().getServiceName());
		assertEquals(challanInfo.getTotalAmount(),receiptHeader.getTotalAmount());
		
		
		assertEquals(challanInfo.getChallanDate(),receiptHeaderref.getChallan().getChallanDate());
		assertEquals(challanInfo.getChallanNumber(),receiptHeaderref.getChallan().getChallanNumber());
		assertEquals(challanInfo.getChallanServiceName(),receiptHeaderref.getChallan().getService().getServiceName());
		assertEquals(challanInfo.getChallanStatus(),receiptHeaderref.getChallan().getStatus());
		assertEquals(challanInfo.getCreatedBy(),receiptHeaderref.getChallan().getCreatedBy());
		assertEquals(challanInfo.getChallanVoucherNum(),receiptHeaderref.getChallan().getVoucherHeader().getVoucherNumber());
	}
	
	@Test
	public void testChallanInfoWithChallanReceipt() throws ApplicationException
	{
		ReceiptHeader receiptHeader=objectFactory.createReceiptHeaderWithChallan();
		EasyMock.expect(egovCommon.getEntityType(EasyMock.isA(Accountdetailtype.class), EasyMock.isA(Integer.class))).
			andReturn(null).anyTimes();
		replay(egovCommon);
		
		ChallanInfo challanInfo = new ChallanInfo(receiptHeader,egovCommon,new ReceiptHeader());
		
		assertEquals(challanInfo.getChallanDate(),receiptHeader.getChallan().getChallanDate());
		assertEquals(challanInfo.getChallanNumber(),receiptHeader.getChallan().getChallanNumber());
		assertEquals(challanInfo.getChallanServiceName(),receiptHeader.getChallan().getService().getServiceName());
		assertEquals(challanInfo.getChallanStatus(),receiptHeader.getChallan().getStatus());
		assertEquals(challanInfo.getCreatedBy(),receiptHeader.getChallan().getCreatedBy());
		assertEquals(challanInfo.getChallanVoucherNum(),receiptHeader.getChallan().getVoucherHeader().getVoucherNumber());
	}*/
}
