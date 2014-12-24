<%@ include file="/includes/taglibs.jsp"%>
<%@ page import="java.util.*,
org.egov.infstr.utils.HibernateUtil,org.egov.exceptions.EGOVRuntimeException,
org.apache.log4j.Logger,
java.text.SimpleDateFormat,
org.egov.commons.Bankbranch,
org.egov.infstr.config.*,
org.egov.commons.Bankaccount,
org.egov.commons.service.*"%>

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>Master Setup</title>
	</head>
	<body>
	<%	
		AppConfig ap= new AppConfig();
		final Logger logger = Logger.getLogger("masterSetUp.jsp");
		List appKeyList = null;
		appKeyList = (List)request.getAttribute("appDataKey");
		String module =null;
		for(Iterator it = appKeyList.iterator();it.hasNext();) {
	  		ap = (AppConfig)it.next();
	    	module = ap.getModule();
	   		Set appValSeT = ap.getAppDataValues();
	  	}
	%>
		<html:form action="config/MasterSetUpAction.do?submitType=createNewValues">

			<p align="center">
				<font color="#0000A0">Master SetUp Screen for <%=module%></font>
			</p>

			<table WIDTH=90% cellpadding="0" cellspacing="0" border="0" id="mainTable">
				<c:forEach var="appConfig" items="${appDataKey}" varStatus="status">
					<c:choose>
						<c:when test="${appConfig.keyName!='BankPension'}">
							<tr>
								<td class="labelcell" colspan="5" height="5px">
							</tr>
								
							<tr id="keyRowId">
								<td class="fieldcell" colspan="5">
									<label for="description">
										${status.count} ${appConfig.description}
									</label>
								</td>
							</tr>
							<tr>
								<td colspan="7">
									<table id="secondtable" align="left" class="eGovInnerTable" border="0" cellpadding="0" cellspacing="0">
										<tr>
											<td class="labelcell" colspan="2" height="5px">
										</tr>
										<tr>
											<td class="labelcell">
												Value
											</td>
											<td class="labelcell">
												Effective From
											</td>
										</tr>
										<c:choose>
											<c:when test="${appConfig.appDataValues  != null && fn:length(appConfig.appDataValues) > 0}">
											<c:forEach var="configValues" items="${appConfig.appDataValues}">
												<tr>
													<td class="labelcell">
														<input type="text" id="values" name="values" value="${configValues.value}"  readOnly="readonly">
													</td>

													<td class="labelcell">
														<input type="text" name="effectiveFrom" id="effectiveFrom" value="<fmt:formatDate  type="date" value="${configValues.effectiveFrom}"  pattern="dd/MM/yyyy"/>" readOnly="readonly" />
													</td>
												</tr>
											</c:forEach>
										</c:when>
										<c:otherwise>
											<tr>
												<td class="labelcell">
													<input type="text" id="values" name="values" readOnly>
												</td>
												<td class="labelcell">
													<input type="text" name="effectiveFrom" id="effectiveFrom" readOnly />
												</td>
											</tr>
										</c:otherwise>
									</c:choose>										
								</table>
						</c:when>
					</c:choose>
				</c:forEach>
				</td>
				</tr>
				<tr>
					<td colspan="10" height="100px">
						<table id="submit" WIDTH="80%" cellpadding="0" cellspacing="0" border="0">
							<tr>
								<td WIDTH="30%" align="center"><html:button  value="   Back   " property="b4" onclick="javascript:	window.history.back()" />
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</html:form>
	</body>
</html>