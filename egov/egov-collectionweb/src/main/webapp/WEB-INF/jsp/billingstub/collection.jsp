<%--
  ~    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) 2017  eGovernments Foundation
  ~
  ~     The updated version of eGov suite of products as by eGovernments Foundation
  ~     is available at http://www.egovernments.org
  ~
  ~     This program is free software: you can redistribute it and/or modify
  ~     it under the terms of the GNU General Public License as published by
  ~     the Free Software Foundation, either version 3 of the License, or
  ~     any later version.
  ~
  ~     This program is distributed in the hope that it will be useful,
  ~     but WITHOUT ANY WARRANTY; without even the implied warranty of
  ~     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  ~     GNU General Public License for more details.
  ~
  ~     You should have received a copy of the GNU General Public License
  ~     along with this program. If not, see http://www.gnu.org/licenses/ or
  ~     http://www.gnu.org/licenses/gpl.html .
  ~
  ~     In addition to the terms of the GPL license to be adhered to in using this
  ~     program, the following additional terms are to be complied with:
  ~
  ~         1) All versions of this program, verbatim or modified must carry this
  ~            Legal Notice.
  ~            Further, all user interfaces, including but not limited to citizen facing interfaces,
  ~            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
  ~            derived works should carry eGovernments Foundation logo on the top right corner.
  ~
  ~            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
  ~            For any further queries on attribution, including queries on brand guidelines,
  ~            please contact contact@egovernments.org
  ~
  ~         2) Any misrepresentation of the origin of the material is prohibited. It
  ~            is required that all modified versions of this material be marked in
  ~            reasonable ways as different from the original version.
  ~
  ~         3) This license does not grant any rights to any user of the program
  ~            with regards to rights under trademark law for use of the trade names
  ~            or trademarks of eGovernments Foundation.
  ~
  ~   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
  ~
  --%>

<%@ include file="/includes/taglibs.jsp"%>
<html>
<head>
<title>Billing stub</title>
<script type="text/javascript">

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

	function onSubmit(obj){
		document.forms[0].action=obj;
		document.forms[0].submit();
	}
	function onBodyLoad() {
		document.getElementById("collectXML").value='%3Cbill-collect%3E%0D%0A++%3CserviceCode%3EPT%3C%2FserviceCode%3E%0D%0A++%3CfundCode%3E01%3C%2FfundCode%3E%0D%0A++%3CfunctionaryCode%3E999%3C%2FfunctionaryCode%3E%0D%0A++%3CfundSourceCode%3E01%3C%2FfundSourceCode%3E%0D%0A++%3CdepartmentCode%3EREV%3C%2FdepartmentCode%3E%0D%0A++%3CdisplayMessage%3EHELLO+USER%3C%2FdisplayMessage%3E%0D%0A++%3CpartPaymentAllowed%3E1%3C%2FpartPaymentAllowed%3E%0D%0A++%3CoverrideAccountHeadsAllowed%3E0%3C%2FoverrideAccountHeadsAllowed%3E%0D%0A++%3CcollectionModeNotAllowed%3Ecard%3C%2FcollectionModeNotAllowed%3E%0D%0A++%3CcollectionModeNotAllowed%3Echeque%3C%2FcollectionModeNotAllowed%3E%0D%0A++%3CcallbackForApportioning%3E0%3C%2FcallbackForApportioning%3E%09%0D%0A++%3Cpayees%3E%0D%0A++++%3Cpayee%3E%0D%0A++++++%3CpayeeName%3EMrs.+ABC%3C%2FpayeeName%3E%0D%0A++++++%3CpayeeAddress%3E221%2F16+LMN+Street%2C+Bangalore%3C%2FpayeeAddress%3E%0D%0A++++++%3Cbills%3E%0D%0A++++++++%3Cbill+refNo%3D%22testReferenceNo1%22+billDate%3D%2221%2F09%2F2009%22+consumerCode%3D%22123456%22%3E%0D%0A++++++++++%3CboundaryNum%3E3%3C%2FboundaryNum%3E%0D%0A++++++++++%3CboundaryType%3EWard%3C%2FboundaryType%3E%0D%0A++++++++++%3Cdescription%3EProperty%3A+221%2F16+LMN+Street%2C+Bangalore+for+period+2008-09%3C%2Fdescription%3E%0D%0A++++++++++%3CtotalAmount%3E400.0%3C%2FtotalAmount%3E%0D%0A++++++++++%3CminimumAmount%3E100.0%3C%2FminimumAmount%3E%0D%0A++++++++++%3Caccounts%3E%0D%0A++++++++++++%3Caccount+glCode%3D%223501001%22+order%3D%221%22+description%3D%223501001+-+GL+Code+description%22+isActualDemand%3D%220%22%3E%0D%0A++++++++++++++%3CcrAmount%3E100.0%3C%2FcrAmount%3E%0D%0A++++++++++++++%3CdrAmount%3E0.0%3C%2FdrAmount%3E%0D%0A++++++++++++++%3CfunctionCode%3E91%3C%2FfunctionCode%3E%0D%0A++++++++++++%3C%2Faccount%3E%0D%0A++++++++++++%3Caccount+glCode%3D%223501002%22+order%3D%222%22+description%3D%223501002+-+GL+Code+description%22+isActualDemand%3D%221%22%3E%0D%0A++++++++++++++%3CcrAmount%3E300.0%3C%2FcrAmount%3E%0D%0A++++++++++++++%3CdrAmount%3E0.0%3C%2FdrAmount%3E%0D%0A++++++++++++++%3CfunctionCode%3E99%3C%2FfunctionCode%3E%0D%0A++++++++++++%3C%2Faccount%3E%0D%0A++++++++++%3C%2Faccounts%3E%0D%0A++++++++%3C%2Fbill%3E%0D%0A+++++++%3C%2Fbills%3E%0D%0A++++%3C%2Fpayee%3E%0D%0A++%3C%2Fpayees%3E%0D%0A%3C%2Fbill-collect%3E';
	}
	
</script>
</head>
<body onload="onBodyLoad();">

	<s:form name="collDetails" action="collection" theme="simple"
		method="post">

		<s:set name="defaultxml">
			<bill-collect> <serviceCode>PT</serviceCode> <fundCode>01</fundCode>
			<functionaryCode>999</functionaryCode> <fundSourceCode>01</fundSourceCode>
			<departmentCode>REV</departmentCode> <displayMessage>HELLO
			USER</displayMessage> <partPaymentAllowed>1</partPaymentAllowed> <overrideAccountHeadsAllowed>0</overrideAccountHeadsAllowed>
			<collectionModeNotAllowed>card</collectionModeNotAllowed> <collectionModeNotAllowed>cheque</collectionModeNotAllowed>
			<callbackForApportioning>0</callbackForApportioning> <payees>
			<payee> <payeeName>Mrs. ABC</payeeName> <payeeAddress>221/16
			LMN Street, Bangalore</payeeAddress> <payeeEmail>test@test.com</payeeEmail> <bills> <bill refNo="testReferenceNo1"
				billDate="21/09/2009" consumerCode="123456"> <boundaryNum>3</boundaryNum>
			<boundaryType>Ward</boundaryType> <description>Property:
			221/16 LMN Street, Bangalore for period 2008-09</description> <totalAmount>400.0</totalAmount>
			<minimumAmount>100.0</minimumAmount> <accounts> <account
				glCode="3501001" order="1"
				description="3501001 - GL Code description" isActualDemand="0"
				purpose="OTHERS"> <crAmount>100.0</crAmount> <drAmount>0.0</drAmount>
			<functionCode>91</functionCode> </account> <account glCode="3501002" order="2"
				description="3501002 - GL Code description" isActualDemand="1"
				purpose="OTHERS"> <crAmount>300.0</crAmount> <drAmount>0.0</drAmount>
			<functionCode>99</functionCode> </account> </accounts> </bill> </bills> </payee> </payees> </bill-collect>
		</s:set>

		<table width="100%" border="0" cellspacing="0" cellpadding="0"
			border="1">
			<tr>
				<td colspan="2">
					<p>Bills to collect:</p>
				</td>
			</tr>
			<tr>
				<td><s:hidden id="collectXML" name="collectXML" /> <!--<s:textarea name="collectXML" cols="100" rows="30">
					<s:param name="value" value="#defaultxml" />
				</s:textarea>--></td>
			</tr>
			<tr>
				<td><s:submit type="submit" value="create"
						cssClass="buttonsubmit"
						onclick="onSubmit('/collection/receipts/receipt-newform.action')"
						align="left" /></td>
			</tr>
			<tr>
				<td><s:submit type="button" value="Online Payment"
						cssClass="buttonsubmit"
						onclick="onSubmit('/collection/citizen/onlineReceipt-newform.action')"
						align="left" /></td>
			</tr>

			<tr>
				<td><s:submit type="button" value="Online Payment Gateway Test"
						cssClass="buttonsubmit"
						onclick="onSubmit('/collection/citizen/onlineReceipt-testOnlinePaytMsg.action')"
						align="left" /></td>
			</tr>

		</table>



	</s:form>

</body>
</html>
