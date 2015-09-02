<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt"%>
<%@page
	import="org.egov.lib.admbndry.CityWebsiteDAO,org.egov.lib.admbndry.CityWebsite"%>
<html>
<head>
<title>Rejection Certificate for TradeLicense</title>
<script>
	function refreshInbox() {
		if (opener && opener.top.document.getElementById('inboxframe')) {
			opener.top.document.getElementById('inboxframe').contentWindow.egovInbox
					.refresh();
		}
	}

	function printLicense() {
		window.print();
	}

	function submitandclose() {
		var printcomplete = confirm(
				"Are you sure License Certificate print completed ?", "YES",
				"NO");
		if (printcomplete) {
			document.getElementById('workflowBean.actionName').value = 'generatedcertificate';
			return true;
		} else {
			return false;
		}
	}
</script>
<%
	String cityUrl = (String) session.getAttribute("cityurl");
	CityWebsiteDAO cityWebsiteDAO = new CityWebsiteDAO();
	CityWebsite cityWebsite = cityWebsiteDAO
			.getCityWebSiteByURL(cityUrl);
	String cityName = cityWebsite.getCityName();
	String logoName = cityWebsite.getLogo();
%>
</head>
<body onload="refreshInbox();printLicense()">
	<center>
		<s:form name="certificateform" action="viewTradeLicense"
			theme="simple">
			<s:token />
			<s:push value="model">
				<table width="100%" border="0" cellpadding="5" cellspacing="5"
					style="margin-left: 25px">
					<tr>
						<td colspan="4" align="center"><img
							src="/egi/images/<%=logoName%>" width="91" height="90" /></td>
					</tr>
					<tr>
						<td colspan="4" align="center"
							style="font-size: 15px; font-weight: bolder;"><%=cityName%>
							<br /> <br /></td>
					</tr>
					<tr>
						<td colspan="4" align="center"
							style="font-size: 15px; font-weight: bolder;"><s:text
								name="license.heading.trade.rejction.letter" /> <br /> <br />
							<br /> <br /></td>
					</tr>
					<tr>
						<td width="40%"><s:text name="license.applicationnumber" />
							:</td>
						<td width="20%"><b><s:property value="applicationNumber" />&nbsp;</b>
						</td>
					</tr>
					<tr>
						<td width="40%"><s:text name="license.applied.for" /> :</td>
						<td width="20%"><b><s:property value="tradeName.name" />
						</b></td>
					</tr>
					<s:if
						test="%{boundary.parent.name.equalsIgnoreCase(@org.egov.license.utils.Constants@CITY_NAME)}">
						<tr>
							<td width="40%"><s:text name="license.zone" /> :</td>
							<td><b><s:property value="boundary.name" /> </b></td>
						</tr>
					</s:if>
					<s:else>
						<tr>
							<td width="40%"><s:text name="license.division" /> :</td>
							<td><b><s:property value="boundary.name" /> </b></td>
						</tr>
						<tr>
							<td width="40%"><s:text name="license.zone" /> :</td>
							<td><b><s:property value="boundary.parent.name" /> </b></td>
						</tr>
					</s:else>
					<tr>
						<td width="40%"><s:text name="licensee.applicantname" /> :</td>
						<td width="20%">Shri/ Smt &nbsp;&nbsp; <b><s:property
									value="licensee.applicantName" /> </b>
						</td>
					</tr>
					<tr>
						<td width="40%"><s:text name="license.establishmentname" />
							:</td>
						<td width="20%"><b><s:property
									value="nameOfEstablishment" /> </b></td>
					</tr>
					<tr>
						<td width="40%"><s:text name='license.address' /> :</td>
						<td width="20%"><b><s:property value="address.houseNo" />
								<s:if test="address.remainingAddress!=null">,</s:if>
								<s:property value="address.remainingAddress" /> <s:if
									test="address.streetAddress2!=null">,</s:if>
								<s:property value="address.streetAddress2" /> <s:if
									test="boundary.name!=null">,</s:if>
								<s:property value="boundary.name" /> <s:if
									test="boundary.parent.name!=null">,</s:if>
								<s:property value="boundary.parent.name" /> <s:if
									test="address.pinCode!=null">,</s:if>
								<s:property value="address.pinCode" /></b></td>
					</tr>
					<tr>
						<td colspan="4"><span style="width: 90%; color: black">
								<p>
									<s:text name="license.reject.certificate.note" />
									&nbsp; <b><s:property value="tradeName.licenseType.name" />
									</b>&nbsp;
									<s:text name="license.reject.certificate.grounds" />
									&nbsp; <b><s:property value="rejectreason" /> </b>
								</p>
						</span></td>
					</tr>
				</table>
			</s:push>
		</s:form>
	</center>
</body>
</html>
