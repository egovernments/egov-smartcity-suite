<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt"%>
<%@page import="org.egov.lib.admbndry.CityWebsiteDAO,org.egov.lib.admbndry.CityWebsite"%>
<html>
	<head>
		<title>Acknowledgement Slip for Renewal of Trade License</title>
		<script>
	function refreshInbox() {
		if (opener && opener.top.document.getElementById('inboxframe')) {
			opener.top.document.getElementById('inboxframe').contentWindow.egovInbox
					.refresh();
		}
	}
	function printLicense() {
		var html = "<html>";
		html += document.getElementById('content').innerHTML;
		html += "</html>";

		var printWin = window
				.open('', '',
						'left=0,top=0,width=1,height=1,toolbar=0,scrollbars=0,status=0');
		printWin.document.write(html);
		printWin.document.close();
		printWin.focus();
		printWin.print();
		printWin.close();
	}
</script>
		<%
			String cityUrl = (String) session.getAttribute("cityurl");
			CityWebsiteDAO cityWebsiteDAO = new CityWebsiteDAO();
			CityWebsite cityWebsite = cityWebsiteDAO.getCityWebSiteByURL(cityUrl);
			String cityName = cityWebsite.getCityName();
			String logoName = cityWebsite.getLogo();
		%>
	</head>
	<body onload="refreshInbox()">
		<center>
			<div id="message">
				<s:actionmessage />
			</div>
			<div id="content">
				<s:form name="certificateform" action="viewTradeLicense">
					<s:push value="model">
						<table width="100%" border="0" cellpadding="5" cellspacing="5" style="margin-left: 25px">
							<tr>
								<td colspan="4" align="center">
									<img src="/egi/images/<%=logoName%>" width="91" height="90" />
								</td>
							</tr>

							<tr>
								<td colspan="4" align="center" style="font-size: 15px; font-weight: bolder;">
									<%=cityName%>
									<br />
								</td>
							</tr>
							<tr>
								<td colspan="4" align="center" style="font-size: 15px; font-weight: bolder;">
									<s:text name="license.acknowledgement.slip.for.renew.tradelicense" />
									<br />
									<br />
									<br />
								</td>

							</tr>
							<tr>
								<td width="40%">
									<s:text name="license.licensenumber" />
									:
								</td>
								<td colspan="3">
									<b><s:property value="licenseNumber" />&nbsp;</b>
								</td>
							</tr>
							<tr>
								<td>
									<s:text name="license.applied.for" />
									:
								</td>
								<td colspan="3">
									<b><s:property value="tradeName.name" />&nbsp;</b>
								</td>
							</tr>
							<tr>
								<td width="40%">
									<s:text name="license.zone" />
									:
								</td>
								<td width="40%">
									<b><s:property value="boundary.parent.name" />&nbsp;</b>
								</td>
							</tr>
							<tr>
								<td>
									<s:text name="license.division" />
									:
								</td>
								<td colspan="3">
									<b><s:property value="boundary.name" />&nbsp;</b>
								</td>
							</tr>
							<tr>
								<td width="40%">
									<s:text name="licensee.applicantname" />
									:
								</td>
								<td width="40%">
									<b><s:property value="licensee.applicantName" />&nbsp;</b>
								</td>
								<td colspan="2" />
							</tr>
							<tr>
								<td width="40%">
									<s:text name="licensee.address" />
									:
								</td>
								<td width="40%">
									<b> <s:if test="%{licensee.address.houseNo!=''}">
											<s:property value="licensee.address.houseNo" />, </s:if> <s:if test="%{licensee.address.streetAddress1!=''}">
											<s:property value="licensee.address.streetAddress1" />,</s:if> <s:if test="%{licensee.boundary.parent.name!=''}">
											<s:property value="licensee.boundary.parent.name" />,</s:if> <s:if test="%{licensee.boundary.name!=''}">
											<s:property value="licensee.boundary.name" />
										</s:if> <s:if test="%{licensee.address.pinCode!=null}">,&nbsp;<s:property value="licensee.address.pinCode" />
										</s:if> </b>
								</td>
								<td colspan="2" />
							</tr>
							<tr>
								<td width="20%">
									<s:text name="license.amount.to.be.paid" />
									:
								</td>
								<td colspan="3">
									<b><s:property value="getPayableAmountInWords()" />&nbsp;</b>
								</td>
							</tr>
							<tr>
								<td colspan="4">
									<s:text name="license.renew.acknowledgement.bottom.text" />
									<%=cityName%>.
								</td>
							</tr>
						</table>
					</s:push>
				</s:form>
			</div>
			<div align="center" >
				<input type="button" id="print" class="button" value="Print" onclick="return printLicense()" />
				&nbsp;&nbsp;
				<input type="button" id="close" value="Close" class="button" onclick="javascript:window.close();" />
			</div>
		</center>
	</body>
</html>