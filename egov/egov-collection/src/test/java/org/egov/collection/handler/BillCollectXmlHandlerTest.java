/**
 * eGov suite of products aim to improve the internal efficiency,transparency, 
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation 
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or 
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

	1) All versions of this program, verbatim or modified must carry this 
	   Legal Notice.

	2) Any misrepresentation of the origin of the material is prohibited. It 
	   is required that all modified versions of this material be marked in 
	   reasonable ways as different from the original version.

	3) This license does not grant any rights to any user of the program 
	   with regards to rights under trademark law for use of the trade names 
	   or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.collection.handler;

public class BillCollectXmlHandlerTest {
	private static final String SERVICECODE = "testServiceCode";
	private static final String FUNDCODE = "testFundCode";
	private static final String FUNCTIONARYCODE = "001";
	private static final String FUNDSOURCECODE = "testfundSourceCode";
	private static final String DEPTCODE = "testDeptCode";
	private static final String PARTPAYTALLOWED_1="1";
	private static final String CALLBACKFORAPPORTIONING_1="1";
	private static final String OVERRIDEACCHEADSALLOWED_1="1";
	private static final String COLLMODENOTALLWD_CASH="cash";
	private static final String COLLMODENOTALLWD_CHEQUE="cheque";
	private static final String DISPLAYMSG="HELLO USER";
	//private static final String PAYEENAME="AMATUL MALIK & OTHERS";//"Mrs. ABC";
	private static final String PAYEENAME="Mrs. ABC";
	private static final String PAYEEADDRESS="221/16 LMN Street, Bangalore";
	private static final String REFNO="testReferenceNo";
	private static final String BILLDATE="21/09/2009";
	private static final String BOUNDARYNUM="1";
	private static final String BOUNDARYTYPE="testZone";
	private static final String DESCRIPTION="Property: 221/16 LMN Street, Bangalore for period 2008-09";
	private static final String CONSUMERCODE="10-10-111-20";
	private static final String TOTALAMOUNT="1000.0";
	private static final String MINIMUMAMOUNT="300.0";
	private static final String GLCODE1="testGLCode1";
	private static final String ORDER1="1";
	private static final String DESCRIPTION1="GL CODE DESCRIPTION1";
	private static final String CREDITAMOUNT1="567.9";
	private static final String DEBITAMOUNT1="0.0";
	private static final String FUNCTIONCODE1="testFunctionCode1";
	private static final String ISACTUALDEMAND1="1";
	private static final String GLCODE2="testGLCode";
	private static final String ORDER2="2";
	private static final String DESCRIPTION2="GL CODE DESCRIPTION2";
	private static final String CREDITAMOUNT2="222.0";
	private static final String DEBITAMOUNT2="0.0";
	private static final String FUNCTIONCODE2="testFunctionCode2";
	private static final String ISACTUALDEMAND2="1";
	
	
	BillCollectXmlHandler handler = new BillCollectXmlHandler();
	
	private static final String XML = 
"<bill-collect>" +  "\n" +
"  <serviceCode>"+SERVICECODE+"</serviceCode>" + "\n" +
"  <fundCode>"+FUNDCODE+"</fundCode>" + "\n" +
"  <functionaryCode>"+FUNCTIONARYCODE+"</functionaryCode>" + "\n" +
"  <fundSourceCode>"+FUNDSOURCECODE+"</fundSourceCode>" + "\n" +
"  <departmentCode>"+DEPTCODE+"</departmentCode>" + "\n" +
"  <partPaymentAllowed>"+PARTPAYTALLOWED_1+"</partPaymentAllowed>" + "\n" +
"  <callbackForApportioning>"+CALLBACKFORAPPORTIONING_1+"</callbackForApportioning>" + "\n" +
"  <overrideAccountHeadsAllowed>"+OVERRIDEACCHEADSALLOWED_1+"</overrideAccountHeadsAllowed>" + "\n" +
"   <collectionType>C</collectionType>" + "\n" +
"  <collectionModeNotAllowed>"+ COLLMODENOTALLWD_CASH + "</collectionModeNotAllowed>" + "\n" +
"  <collectionModeNotAllowed>"+ COLLMODENOTALLWD_CHEQUE + "</collectionModeNotAllowed>" + "\n" +
"  <displayMessage>"+DISPLAYMSG+"</displayMessage>" + "\n" +
"  <payees>"+ "\n" +
"    <payee>"+ "\n" +
"      <payeeName>"+PAYEENAME+"</payeeName>"+ "\n" +
"      <payeeAddress>"+PAYEEADDRESS+"</payeeAddress>"+ "\n" +
"      <bills>" +  "\n" +
"        <bill refNo=\""+ REFNO + "\" billDate=\""+BILLDATE+"\" consumerCode=\"" + CONSUMERCODE + "\">" + "\n" +
"          <boundaryNum>"+BOUNDARYNUM+"</boundaryNum>" + "\n" +
"          <boundaryType>"+BOUNDARYTYPE+"</boundaryType>" + "\n" +
"          <description>"+DESCRIPTION+"</description>" + "\n" +
"          <totalAmount>"+TOTALAMOUNT+"</totalAmount>" + "\n" +
"          <minimumAmount>"+MINIMUMAMOUNT+"</minimumAmount>"+ "\n" +
"          <accounts>"+ "\n" +
"            <account glCode=\""+GLCODE1+"\" order=\""+ORDER1+ "\" description=\"" + DESCRIPTION1 + "\" isActualDemand=\"" + ISACTUALDEMAND1+ "\">"+ "\n" +
"              <crAmount>"+CREDITAMOUNT1+"</crAmount>"+ "\n" +
"              <drAmount>"+DEBITAMOUNT1+"</drAmount>"+ "\n" +
"              <functionCode>"+FUNCTIONCODE1+"</functionCode>"+ "\n" +
"            </account>"+ "\n" +
"            <account glCode=\""+GLCODE2+"\" order=\""+ORDER2+ "\" description=\"" + DESCRIPTION2 + "\" isActualDemand=\"" + ISACTUALDEMAND2+ "\">"+ "\n" +
"              <crAmount>"+CREDITAMOUNT2+"</crAmount>"+ "\n" +
"              <drAmount>"+DEBITAMOUNT2+"</drAmount>"+ "\n" +
"              <functionCode>"+FUNCTIONCODE2+"</functionCode>"+ "\n" +
"              </account>"+ "\n" +
"          </accounts>"+ "\n" +
"        </bill>"+ "\n" +
"      </bills>"+ "\n" +
"    </payee>"+ "\n" +
"  </payees>"+ "\n" +
"</bill-collect>";
	
	
	/*@Test
	public void testConvertBillCollectionObjectToXML() throws ParseException {
		
		String actualReturnXml = handler.toXML(createBillCollectionObject());
		
		String expectedReturnXML = "<bill-collect>"+"\n"+
		  "  <serviceCode>testServiceCode</serviceCode>"+"\n"+
		  "  <fundCode>testFundCode</fundCode>"+"\n"+
		  "  <functionaryCode>1</functionaryCode>"+"\n"+
		  "  <fundSourceCode>testfundSourceCode</fundSourceCode>"+"\n"+
		  "  <departmentCode>testDeptCode</departmentCode>"+"\n"+
		  "  <displayMessage>HELLO USER</displayMessage>"+"\n"+
		  "  <paidBy>testPaidBy</paidBy>"+"\n"+
		  "  <partPaymentAllowed>1</partPaymentAllowed>"+"\n"+
		  "  <callbackForApportioning>1</callbackForApportioning>"+"\n"+
		  "  <overrideAccountHeadsAllowed>1</overrideAccountHeadsAllowed>"+"\n"+
		  "  <collectionType>C</collectionType>"+"\n"+
		  "  <collectionModeNotAllowed>cash</collectionModeNotAllowed>"+"\n"+
		  "  <collectionModeNotAllowed>cheque</collectionModeNotAllowed>"+"\n"+
		  "  <payees>"+"\n"+
		  "    <payee>"+"\n"+
		  "      <payeeName>Mrs. ABC</payeeName>"+"\n"+
		  "      <payeeAddress>221/16 LMN Street, Bangalore</payeeAddress>"+"\n"+
		  "      <bills>"+"\n"+
		  "        <bill refNo=\"testReferenceNo\" billDate=\"21/09/2009\" consumerCode=\"10-10-111-20\">"+"\n"+
		  "          <boundaryNum>1</boundaryNum>"+"\n"+
		  "          <boundaryType>testZone</boundaryType>"+"\n"+
		  "          <description>Property: 221/16 LMN Street, Bangalore for period 2008-09</description>"+"\n"+
		  "          <totalAmount>1000.0</totalAmount>"+"\n"+
		  "          <minimumAmount>300.0</minimumAmount>"+"\n"+
		  "          <accounts>"+"\n"+
		  "            <account glCode=\"testGLCode1\" order=\"1\" description=\"GL CODE DESCRIPTION1\" isActualDemand=\"1\">"+"\n"+
		  "              <crAmount>567.9</crAmount>"+"\n"+
		  "              <drAmount>0.0</drAmount>"+"\n"+
		  "              <functionCode>testFunctionCode1</functionCode>"+"\n"+
		  "            </account>"+"\n"+
		  "            <account glCode=\"testGLCode\" order=\"2\" description=\"GL CODE DESCRIPTION2\" isActualDemand=\"1\">"+"\n"+
		  "              <crAmount>222.0</crAmount>"+"\n"+
		  "              <drAmount>0.0</drAmount>"+"\n"+
		  "              <functionCode>testFunctionCode2</functionCode>"+"\n"+
		  "            </account>"+"\n"+
		  "          </accounts>"+"\n"+
		  "        </bill>"+"\n"+
		  "      </bills>"+"\n"+
		  "    </payee>"+"\n"+
		  "  </payees>"+"\n"+
		  "</bill-collect>";
		assertEquals(actualReturnXml,expectedReturnXML);
	}
	
	@Test
	public void testSortBillInfo(){
		BillAccountDetails account1 = new BillAccountDetails(
				GLCODE1,Integer.valueOf(ORDER2),new BigDecimal(CREDITAMOUNT1),
				new BigDecimal(DEBITAMOUNT1),FUNCTIONCODE1,DESCRIPTION1,Integer.valueOf(ISACTUALDEMAND1));
		BillAccountDetails account2 = new BillAccountDetails(
				GLCODE2,Integer.valueOf(ORDER1),new BigDecimal(CREDITAMOUNT2),
				new BigDecimal(DEBITAMOUNT2),FUNCTIONCODE2,DESCRIPTION2,Integer.valueOf(ISACTUALDEMAND2));
		
		List<BillAccountDetails> billAccountDetails = new ArrayList<BillAccountDetails>();
		billAccountDetails.add(0, account1);
		billAccountDetails.add(1, account2);
		
		assertEquals(billAccountDetails.get(0).getOrder(),Integer.valueOf(ORDER2));
		assertEquals(billAccountDetails.get(1).getOrder(),Integer.valueOf(ORDER1));
		
		Collections.sort(billAccountDetails);
		
		assertEquals(billAccountDetails.get(0).getOrder(),Integer.valueOf(ORDER1));
		assertEquals(billAccountDetails.get(1).getOrder(),Integer.valueOf(ORDER2));
	}
	
	
	
	@Test
	public void testConvertXMLToBillCollectionObject() throws ParseException {
		
		BillInfoImpl billColl = (BillInfoImpl)handler.toObject(XML);
		BillInfoImpl expectedBillColl = createBillCollectionObject();
		
		assertTrue(billColl.equals(expectedBillColl));
		
		List<BillDetails> actualBillDetails = new ArrayList<BillDetails>();			
		List<BillDetails> expectedBillDetails = new ArrayList<BillDetails>();
		List<BillAccountDetails> actualBillAccountDetails = new ArrayList<BillAccountDetails>();			
		List<BillAccountDetails> expectedBillAccountDetails = new ArrayList<BillAccountDetails>();
		
		for(BillPayeeDetails payee:billColl.getPayees()){
			actualBillDetails.addAll(payee.getBillDetails());
			for(BillDetails billDetail : payee.getBillDetails()){
				actualBillAccountDetails.addAll(billDetail.getAccounts());
			}
		}
		for(BillPayeeDetails payee:expectedBillColl.getPayees()){
			expectedBillDetails.addAll(payee.getBillDetails());
			for(BillDetails billDetail : payee.getBillDetails()){
				expectedBillAccountDetails.addAll(billDetail.getAccounts());
			}
		}
		assertTrue(actualBillDetails.containsAll(expectedBillDetails));
		assertTrue(actualBillAccountDetails.containsAll(expectedBillAccountDetails));
		
		assertTrue(billColl.hashCode()==expectedBillColl.hashCode());
		assertTrue(billColl.getPayees().get(0).hashCode()==expectedBillColl.getPayees().get(0).hashCode());
		assertTrue(actualBillDetails.get(0).hashCode()==expectedBillDetails.get(0).hashCode());
		assertTrue(actualBillAccountDetails.get(0).hashCode()==expectedBillAccountDetails.get(0).hashCode());
		
	} 
	
	@Test
	public void testBillCollectionObjects(){
		BillInfoImpl billColl1 = new BillInfoImpl(
				"ServiceCode1",FUNDCODE,new BigDecimal(FUNCTIONARYCODE),FUNDSOURCECODE,
				DEPTCODE,DISPLAYMSG,"testPaidBy",Boolean.TRUE,Boolean.TRUE,null,BillInfo.COLLECTIONTYPE.C);
		
		BillInfoImpl billColl2 = new BillInfoImpl(
				"ServiceCode12",FUNDCODE,new BigDecimal(FUNCTIONARYCODE),FUNDSOURCECODE,
				DEPTCODE,DISPLAYMSG,"testPaidBy",Boolean.TRUE,Boolean.TRUE,null,BillInfo.COLLECTIONTYPE.C);
		
		BillAccountDetails account1 = new BillAccountDetails(
				"GLCode1",Integer.valueOf(ORDER1),new BigDecimal(CREDITAMOUNT1),
				new BigDecimal(DEBITAMOUNT1),FUNCTIONCODE1,DESCRIPTION1,Integer.valueOf(ISACTUALDEMAND1));
		
		BillAccountDetails account2 = new BillAccountDetails(
				"GLCode2",Integer.valueOf(ORDER1),new BigDecimal(CREDITAMOUNT1),
				new BigDecimal(DEBITAMOUNT1),FUNCTIONCODE1,DESCRIPTION1,Integer.valueOf(ISACTUALDEMAND1));
		
		BillDetails billDetail1 = new BillDetails("refno1",null,null,"",
				"","", null,null);
		BillDetails billDetail2 = new BillDetails("refno2",null,null,"",
				"","", null,null);
		
		BillPayeeDetails payee1 = new BillPayeeDetails("Name1",null);
		BillPayeeDetails payee2 = new BillPayeeDetails("Name2",null);
		
		List<BillPayeeDetails> payeeList = new ArrayList<BillPayeeDetails>();
		payeeList.add(payee1);
		payeeList.add(payee2);
		billColl1.setPayees(payeeList);
		
		assertFalse(billColl1.equals(billColl2));
		assertFalse(billColl1.equals(payee1));
		assertFalse(account1.equals(account2));
		assertFalse(billDetail1.equals(billDetail2));
		assertFalse(payee1.equals(payee2));
	}
	
	private BillInfoImpl createBillCollectionObject() throws ParseException{
		
		List<String>  collModesNotAllwd = new ArrayList<String>();
		collModesNotAllwd.add(COLLMODENOTALLWD_CASH);
		collModesNotAllwd.add(COLLMODENOTALLWD_CHEQUE);
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date billDate = sdf.parse("21/09/2009");
		
		BillAccountDetails account1 = new BillAccountDetails(
				GLCODE1,Integer.valueOf(ORDER1),new BigDecimal(CREDITAMOUNT1),
				new BigDecimal(DEBITAMOUNT1),FUNCTIONCODE1,DESCRIPTION1,Integer.valueOf(ISACTUALDEMAND1));
		BillAccountDetails account2 = new BillAccountDetails(
				GLCODE2,Integer.valueOf(ORDER2),new BigDecimal(CREDITAMOUNT2),
				new BigDecimal(DEBITAMOUNT2),FUNCTIONCODE2,DESCRIPTION2,Integer.valueOf(ISACTUALDEMAND2));
		BillDetails billDetail = new BillDetails(REFNO,billDate,CONSUMERCODE,BOUNDARYNUM,
				BOUNDARYTYPE,DESCRIPTION, new BigDecimal(TOTALAMOUNT),
				new BigDecimal(MINIMUMAMOUNT));
		BillPayeeDetails payee = new BillPayeeDetails(PAYEENAME,PAYEEADDRESS);
		BillInfoImpl billInfo = new BillInfoImpl(
				SERVICECODE,FUNDCODE,new BigDecimal(FUNCTIONARYCODE),FUNDSOURCECODE,
				DEPTCODE,DISPLAYMSG,"testPaidBy",Boolean.TRUE,Boolean.TRUE,collModesNotAllwd,BillInfo.COLLECTIONTYPE.C);
		billInfo.setCallbackForApportioning(Boolean.TRUE);
		
		billDetail.addBillAccountDetails(account1);
		billDetail.addBillAccountDetails(account2);
		payee.addBillDetails(billDetail);
		billInfo.addPayees(payee);
		
		return billInfo;
	}*/
}
