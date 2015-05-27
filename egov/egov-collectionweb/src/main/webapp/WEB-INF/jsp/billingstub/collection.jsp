#-------------------------------------------------------------------------------
# <!-- eGov suite of products aim to improve the internal efficiency,transparency, 
#     accountability and the service delivery of the government  organizations.
#  
#      Copyright (C) <2015>  eGovernments Foundation
#  
#      The updated version of eGov suite of products as by eGovernments Foundation 
#      is available at http://www.egovernments.org
#  
#      This program is free software: you can redistribute it and/or modify
#      it under the terms of the GNU General Public License as published by
#      the Free Software Foundation, either version 3 of the License, or
#      any later version.
#  
#      This program is distributed in the hope that it will be useful,
#      but WITHOUT ANY WARRANTY; without even the implied warranty of
#      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#      GNU General Public License for more details.
#  
#      You should have received a copy of the GNU General Public License
#      along with this program. If not, see http://www.gnu.org/licenses/ or 
#      http://www.gnu.org/licenses/gpl.html .
#  
#      In addition to the terms of the GPL license to be adhered to in using this
#      program, the following additional terms are to be complied with:
#  
#  	1) All versions of this program, verbatim or modified must carry this 
#  	   Legal Notice.
#  
#  	2) Any misrepresentation of the origin of the material is prohibited. It 
#  	   is required that all modified versions of this material be marked in 
#  	   reasonable ways as different from the original version.
#  
#  	3) This license does not grant any rights to any user of the program 
#  	   with regards to rights under trademark law for use of the trade names 
#  	   or trademarks of eGovernments Foundation.
#  
#    In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
# -->
#-------------------------------------------------------------------------------
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

<%@ taglib prefix="egov" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator"%>
<%@ taglib prefix="s" uri="/WEB-INF/taglibs/struts-tags.tld" %>  
<html>
<head>
<title>Billing stub</title>
<script>

	function collect()
	{
	      var postURL = "receipt.action";
	      var request = initiateRequest();
	      request.open("POST", link, false);
	      request.setRequestHeader("Content-type","text/xml");
	      request.send(document.form.xmlToSend);
	      if (request.status == 200)
	      {
			var response=request.responseText;
	      }
	}
	function onlinePayment(){
		document.forms[0].action="${pageContext.request.contextPath}"+"/citizen/onlineReceipt!create.action";
		document.forms[0].submit();
	}
	function testOnlinePaymentMessage(){
		//window.open('../WEB-INF/jsp/receipts/onlineReceipt-PaytGatewayTest.jsp','_self');
		document.forms[0].action="${pageContext.request.contextPath}"+"/citizen/onlineReceipt!testOnlinePaytMsg.action";
		document.forms[0].submit();
	}
	
</script>
</head>
<body >

<s:form name="collDetails" action="/receipts/receipt.action">

<s:set name="defaultxml">
<bill-collect>
  <serviceCode>PT</serviceCode>
  <fundCode>0101</fundCode>
  <functionaryCode>999</functionaryCode>
  <fundSourceCode>01</fundSourceCode>
  <departmentCode>R</departmentCode>
  <displayMessage>HELLO USER</displayMessage>
  <partPaymentAllowed>1</partPaymentAllowed>
  <overrideAccountHeadsAllowed>0</overrideAccountHeadsAllowed>
  <collectionModeNotAllowed>cash</collectionModeNotAllowed>
  <collectionModeNotAllowed>cheque</collectionModeNotAllowed>
  <callbackForApportioning>0</callbackForApportioning>	
  <payees>
    <payee>
      <payeeName>Mrs. ABC</payeeName>
      <payeeAddress>221/16 LMN Street, Bangalore</payeeAddress>
      <bills>
        <bill refNo="testReferenceNo1" billDate="21/09/2009" consumerCode="123456">
          <boundaryNum>3</boundaryNum>
          <boundaryType>ZONE</boundaryType>
          <description>Property: 221/16 LMN Street, Bangalore for period 2008-09</description>
          <totalAmount>400.0</totalAmount>
          <minimumAmount>100.0</minimumAmount>
          <accounts>
            <account glCode="3501001" order="1" description="3501001 - GL Code description" isActualDemand="0">
              <crAmount>100.0</crAmount>
              <drAmount>0.0</drAmount>
              <functionCode>91</functionCode>
            </account>
            <account glCode="3501002" order="2" description="3501002 - GL Code description" isActualDemand="1">
              <crAmount>300.0</crAmount>
              <drAmount>0.0</drAmount>
              <functionCode>99</functionCode>
            </account>
          </accounts>
        </bill>
       </bills>
    </payee>
  </payees>
</bill-collect>
</s:set>
  
 <table width="100%" border="0" cellspacing="0" cellpadding="0" border="1">
   <tr><td colspan="2">
        <p>Bills to collect:</p>
        </td></tr>
        <tr>
	<td >
	<s:textarea name="collectXML" cols="100" rows="30">
		<s:param name="value" value="#defaultxml" />
	</s:textarea>
	</td>
	</tr>
	<tr><td>
	<s:submit type="submit"  value="create" method="create" align="left"/>
	</td></tr>
	<tr>
	<td><s:submit type="button" value="Online Payment" onclick="onlinePayment()" align="left"/></td>
	</tr>
	
	<tr>
		<td><s:submit type="button" value="Online Payment Gateway Test" onclick="testOnlinePaymentMessage()" align="left"/></td>
	</tr>
	
  </table>



</s:form>

</body>
</html>
