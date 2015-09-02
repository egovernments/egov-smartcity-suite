<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@page import="org.egov.lib.admbndry.CityWebsiteDAO,org.egov.lib.admbndry.CityWebsite"%>

<html>
	<head>
		<script>
			function refreshInbox() {
				if (opener && opener.top.document.getElementById('inboxframe')) {
					opener.top.document.getElementById('inboxframe').contentWindow.egovInbox.refresh();
				}
			}
			function printLicense(){				
				window.print();
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
	<body onload="refreshInbox();printLicense()">
		<center>
			<s:form name="preNoticeform" action="objection" theme="simple">
			<s:token/>
			<s:hidden name="workflowBean.actionName" id="workflowBean.actionName" />
					<s:hidden name="activities[0].type" value="preNotice" />
					<s:hidden name="model.id" />
				<s:push value="model">
					<table width="100%" border="0" cellpadding="5" cellspacing="5" style="margin-left: 25px  font-size: 12px">
						<tr>
							<td colspan="4" align="center">
								<img src="/egi/images/<%=logoName%>" width="91" height="90" />
							</td>
						</tr>
						<tr>
							<td colspan="4" align="center" style="font-size: 15px; font-weight: bolder;">
								<%=cityName%>
								<br />
								<br />
							</td>
						</tr>
						<tr>
							<td colspan="4" align="center" style="font-size: 15px; font-weight: bolder;">
								<s:text name="Preliminary Notice on" />
								&nbsp;
								<b><s:property value="license.tradeName.licenseType.name" />
								</b>&nbsp;
								<br />
								<br />
								<br/>
							</td>
						</tr>
						<s:if
							test="%{license.boundary.parent.name.equalsIgnoreCase(@org.egov.license.utils.Constants@CITY_NAME)}">
							<tr>
								<td colspan="4">&nbsp;</td>
								<td><s:text name="license.zone" /> : &nbsp; <b><s:property
											value="license.boundary.name" /> </b></td>
							</tr>
						</s:if>
						<s:else>
							<tr>
								<td colspan="4">&nbsp;</td>
								<td><s:text name="license.division" /> :
									&nbsp; <b><s:property value="license.boundary.name" /> </b></td>
							</tr>
							<tr>
								<td colspan="4">&nbsp;</td>
								<td><s:text name="license.zone" /> : &nbsp; <b><s:property
											value="license.boundary.parent.name" /> </b></td>
							</tr>
						</s:else>
						<tr>
							<td colspan="4">
								&nbsp;
							</td>
							<td>
								<s:text name="Date" />
								: &nbsp;
								<b><%=new java.text.SimpleDateFormat("dd-MMM-yyyy").format(new java.util.Date())%></b>
							</td>
						</tr>

						<tr>
							<td colspan="4" style="font-size: 15px; font-weight: bolder;">

								<br />
								<br />
								<s:text name="To" />
								,
							</td>
						</tr>
						<tr>
							<td colspan="4" style="font-size: 15px; font-weight: bolder;">
								<s:property value="name" />
							</td>
						</tr>
						<tr>
							<td colspan="4" style="font-size: 15px; font-weight: bolder;">
								<s:property value="address" />
							</td>
						</tr>
						</table>
                    <table width="100%" border="0" cellpadding="5" cellspacing="5" style="margin-left: 25px font-size: 12px">
						<tr>
							<td colspan="4">
								<br />
								<br />
								<b><s:text name="Sub" />&nbsp;:&nbsp;<s:text name="Preliminary Notice of license for the objection Raised by" />&nbsp;<s:property value="name" />,&nbsp;<s:property value="number" />.</b>
							</td>
						</tr>
						<tr>
							<td colspan="4">
								<br />
							</td>
						</tr>
						</table>
						<table width="100%" border="0" cellpadding="5" cellspacing="5" style="margin-left: 25px font-size: 12px">
						<tr>
							<td colspan="4">
								<s:text name="An objection raised on" />
								&nbsp;
								<b><s:property value="license.tradeName.licenseType.name" /> </b>&nbsp;
								<s:text name="issued to Shri/ Smt" />
								&nbsp;
								<s:property value="license.licensee.applicantName" />
								,&nbsp;
								<s:property value="license.licenseNumber" />
								<b> </b>&nbsp;
								<s:text name="a preliminary notice has been issued for reasons stated below." />
							</td>
							</tr>
							<tr>
								<td colspan="4">
									<br />
								</td>
							</tr>
							</table>
							<table width="100%" border="0" cellpadding="5" cellspacing="5" style="margin-left: 25px font-size: 12px">
							<tr>
								<td colspan="4">
									<s:property value="details" />
								</td>
							</tr>
							<tr>
								<td colspan="4">
									&nbsp;
								</td>
								<td width="50%">
									<br/>
									<b><%=cityName%></b>
								</td>
							</tr>
					</table>
				</s:push>
			</s:form>
		</center>
	</body>
</html>