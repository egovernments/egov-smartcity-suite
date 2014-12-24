<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<html>
	<head>
		<title>
			<s:text name="retention.money.refund.workorder.search"/>
		</title>
</head>	
<script src="<egov:url path='/js/works.js'/>"></script>
<script src="<egov:url path='/css/works.css'/>"></script>
<style>
.ui-button
{
	position: absolute;
	height: 2.0em;
}
</style>
<body onload="init();">
		<div class="errorstyle" id="searchRMR_error" style="display: none;"></div>
		<s:if test="%{hasErrors()}">
			<div class="errorstyle">
				<s:actionerror />
				<s:fielderror />
			</div>
		</s:if>			
	<s:form action="retentionMoneyRefund" theme="simple" name="retentionMoneyForm">
		<s:hidden name="sourcepage" id="sourcepage" value="search" />
		<div class="formmainbox">
			<div class="insidecontent">
				<div id="printContent" class="rbroundbox2">
					<div class="rbtop2">
						<div></div>
					</div>
					<div class="rbcontent2">
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td>
									<table width="100%" border="0" cellspacing="0" cellpadding="0">
										<tr>
											<td colspan="4" class="headingwk">
												<div class="arrowiconwk">
													<img
														src="${pageContext.request.contextPath}/image/arrow.gif" />
												</div>
												<div class="headplacer">
													<s:text name="page.subheader.search.workorder" />
												</div>
											</td>
										</tr>
										<tr>
											<td class="whiteboxwk">
												<s:text name="workorder.search.status" />:
											</td>
											<td class="whitebox2wk">
												<s:select id="model.egwStatus.code" name="model.egwStatus.code" headerKey="-1"
													headerValue="--- Select ---" cssClass="selectwk"
													list="%{retentionMRStatuses}" value="%{model.egwStatus.code}"
													listKey="code" listValue="description"/>
											</td>
											<td class="whiteboxwk">
												<s:text name="workorder.search.workordernumber" />:
											</td>
											<td class="whitebox2wk">
												 <s:textfield name="workOrder.workOrderNumber"
													value="%{workOrder.workOrderNumber}" id="workOrder.workOrderNumber"
													cssClass="selectwk" />
											</td>
										</tr>
										<tr>	
											<td class="greyboxwk">
												<s:text name="workorder.search.workorderFromdate" />:
											</td>
											<td class="greybox2wk">
													<s:date name="fromDate" var="fromDateFormat"
														format="dd/MM/yyyy" />
												<s:textfield name="fromDate" id="fromDate"
													cssClass="selectwk" value="%{fromDateFormat}"
													onfocus="javascript:vDateType='3';"
													onkeyup="DateFormat(this,this.value,event,false,'3')" />
												<a
													href="javascript:show_calendar('forms[0].fromDate',null,null,'DD/MM/YYYY');"
													onmouseover="window.status='Date Picker';return true;"
													onmouseout="window.status='';return true;"> <img
														src="${pageContext.request.contextPath}/image/calendar.png"
														alt="Calendar" width="16" height="16" border="0"
														align="absmiddle" />
												</a>
											</td>
											<td class="greyboxwk">
												<s:text name="workorder.search.workorderTodate" />:
											</td>
											<td class="greybox2wk">
													<s:date name="toDate" var="toDateFormat"
														format="dd/MM/yyyy" />
												<s:textfield name="toDate" id="toDate"
													value="%{toDateFormat}" cssClass="selectwk"
													onfocus="javascript:vDateType='3';"
													onkeyup="DateFormat(this,this.value,event,false,'3')" />
												<a
													href="javascript:show_calendar('forms[0].toDate',null,null,'DD/MM/YYYY');"
													onmouseover="window.status='Date Picker';return true;"
													onmouseout="window.status='';return true;"> <img
														src="${pageContext.request.contextPath}/image/calendar.png"
														alt="Calendar" width="16" height="16" border="0"
														align="absmiddle" />
												</a>
											</td>
										</tr>
										<tr>
											<td class="whiteboxwk">
												<s:text name="workorder.search.contractor" />:
											</td>
												
											<td colspan="3" class="whitebox2wk">
												<s:select id="contractorId" name="workOrder.contractor.id"
													cssClass="selectwk"
													list="%{contractorForApprovedWorkOrder}"  headerKey="-1"
													headerValue="" value="%{workOrder.contractor.id}" />
											</td>
										</tr>
										<tr>
											<td class="greyboxwk">
												<s:text name="workOrder.executingDepartment" />:
											</td>
											<td class="greybox2wk">
												<s:select id="deptId" name="deptId"
													cssClass="selectwk" list="%{dropdownData.executingDepartmentList}"
													listKey="id" listValue="deptName" headerKey="-1"
													headerValue="--- Select ---" />
											</td>	
											<td class="greyboxwk">
												<s:text name="estimate.number" />:
											</td>
											<td class="greybox2wk">
												<s:textfield name="estimateNumber" id="estimateNumber" cssClass="selectboldwk" />
											</td>
										</tr>
										<tr>
											<td colspan="4" class="shadowwk"></td>
										</tr>
										<tr>
											<td colspan="4">
												<div  class="buttonholderwk" align="center">
													<s:submit  cssClass="buttonadd" value="SEARCH"
														id="saveButton" name="button"  method="searchList" 
														/>
													<input type="button" class="buttonfinal" value="CLOSE"
														id="closeButton" name="button" onclick="window.close();" />
												</div>
											</td>
										</tr>
									</table>
								</td>
							</tr>
							<%@ include file='retentionMoneyRefund-searchList.jsp'%>
						</table>			
					</div>
				</div>
			</div>
		</div>
	</s:form>
	</body>
</html>