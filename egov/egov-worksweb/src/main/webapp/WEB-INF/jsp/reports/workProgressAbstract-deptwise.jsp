<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ page import="java.util.*" %>
<script src="<egov:url path='js/workProgressAbstractReportHelper.js'/>"></script>
<html>
<title><s:text name='page.title.workprogressabstract.bydepartment' /></title>
<script type="text/javascript">
function disableMasking(){
	document.getElementById('loadingMask').remove();
	generateSubHeader();
}
</script>

<body >
	<div class="errorstyle" id="workProgress_error"	style="display: none;"></div>
	<s:if test="%{hasErrors()}">
		<div class="errorstyle">
			<s:actionerror />
			<s:fielderror />
		</div>
	</s:if>
	<s:form name="workProgressAbstractForm" id="workProgressAbstractForm" theme="simple">
		<s:hidden name="subHeader" id="subHeader" />
		<s:hidden name="departmentName" id="departmentName" />
		<s:hidden name="budgetHeadParams" id="budgetHeadParams" />
		
		<div class="formmainbox">
		<div class="insidecontent">
		<div id="printContent" class="rbroundbox2">
		<div class="rbtop2"><div></div></div>
		<div class="rbcontent2">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr><td>&nbsp;</td></tr>
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
									<s:text name="page.subheader.search.estimate" />
								</div>
							</td>
						</tr>
						<tr>
							<td class="greyboxwk">
								<s:text name="workprogressabstract.fromdate" />
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
							<td width="17%" class="greyboxwk">
								<s:text name="workprogressabstract.todate" />
							</td>
							<td width="17%" class="greybox2wk">
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
							<td width="11%" class="whiteboxwk">
								<s:text name='workprogressabstract.executing.department'/> :
							</td>
							<td width="21%" class="whitebox2wk">
								<s:select headerKey="-1" headerValue="%{getText('default.dropdown.select')}" name="executingDepartment" 
								id="executingDepartment" cssClass="selectwk" list="dropdownData.executingDepartmentList" listKey="id" 
								listValue="deptName" value="%{executingDepartment}" />
								
							</td>
								<td width="15%" class="whiteboxwk">
								<s:text name="workprogressabstract.work.nature" />
								:
							</td>
							<td width="53%" class="whitebox2wk">
								<s:select headerKey="-1" headerValue="%{getText('default.dropdown.select')}"
									name="natureOfWork" id="natureOfWork" cssClass="selectwk"
									list="dropdownData.typeList" listKey="id" listValue="name"
									value="%{natureOfWork}" />
							</td>
						</tr>
						<tr>
							<td class="greyboxwk">
								<s:text name="workprogressabstract.work.type" />
								:
							</td>
							<td class="greybox2wk">
								<s:select headerKey="-1" headerValue="%{getText('default.dropdown.select')}"
									name="worksType" id="worksType"
									cssClass="selectwk" list="dropdownData.worksTypeList"
									listKey="id" listValue="description"
									value="%{worksType}"
									onChange="setupSubTypes(this);" />
								<egov:ajaxdropdown id="worksSubTypeDropdown"
									fields="['Text','Value']" dropdownId='worksSubType'
									url='estimate/ajaxEstimate!subcategories.action'
									selectedValue="%{category.id}" />
							</td>

							<td class="greyboxwk">
								<s:text name="workprogressabstract.work.subtype" />
								:
							</td>
							<td class="greybox2wk">
								<s:select headerKey="-1" headerValue="%{getText('default.dropdown.select')}" name="worksSubType"
									value="%{worksSubType}" id="worksSubType" cssClass="selectwk"
									list="dropdownData.worksSubTypeList" listKey="id"
									listValue="description" />
							</td>
						</tr>
						<tr>
							<td width="15%" class="whiteboxwk">
								<s:text name='workprogressabstract.fund'/> :				
							</td>
							<td width="53%" class="whitebox2wk">
								<s:select headerKey="-1" headerValue="%{getText('default.dropdown.select')}"
								name="fund" id="fund" cssClass="selectwk" list="dropdownData.fundList" listKey="id" 
								listValue="name" value="%{fund}" />
							</td>
							<td width="11%" class="whiteboxwk">
								<s:text name='workprogressabstract.function'/> :
							</td>
							<td width="21%" class="whitebox2wk">
								<s:select headerKey="-1" headerValue="%{getText('default.dropdown.select')}"
				 					name="function" id="function" cssClass="selectwk" list="dropdownData.functionList" 
				 					listKey="id" listValue="name" value="%{function}"   />
							</td>
						</tr>
						<tr><td class="greyboxwk"><s:text name='workprogressabstract.createdby'/> :
						</td >
						<td class="greybox2wk" colspan="3">
						<s:select headerKey="-1"
										headerValue="%{getText('default.dropdown.select')}"
										name="preparedBy" value="%{preparedBy}" id="preparedBy"
										cssClass="selectwk" list="dropdownData.preparedByList"
										listKey="id" listValue="employeeName" />
						</td>
							
							
						</tr>
						
						<tr>
						<tr>
			                <td class="whiteboxwk"><s:text name='workprogressabstract.scheme'/> : </td>
			                <td class="whitebox2wk"><s:select headerKey="-1" headerValue="%{getText('default.dropdown.select')}" name="scheme" id="scheme" cssClass="selectwk" list="dropdownData.schemeList" listKey="id" listValue="name" value="%{scheme}"  onChange="setupSubSchemes(this);"/>
							<egov:ajaxdropdown id="subSchemeDropdown" fields="['Text','Value']" dropdownId='subScheme' url='reports/ajaxWorkProgress!loadSubSchemes.action' selectedValue="%{scheme.id}"/></td>
			                <td class="whiteboxwk"><s:text name='workprogressabstract.subscheme'/> : </td>
			                <td class="whitebox2wk"><s:select headerKey="-1" headerValue="%{getText('estimate.default.select')}" name="subScheme" id="subScheme" cssClass="selectwk" list="dropdownData.subSchemeList" listKey="id" listValue="name" value="%{subScheme}" /></td>
						</tr>
						
						<tr>
							<td width="11%" class="greyboxwk">
								<s:text name='workprogressabstract.budgethead'/> :
							</td>
							<td class="greybox2wk" colspan="3">
								<s:select multiple="true" size="6" headerKey="-1" headerValue="%{getText('default.dropdown.select')}"
									name="budgetHeads" id="budgetHeads" cssClass="selectwk" list="dropdownData.budgetHeadList" 
									listKey="id" listValue="name" value="%{budgetHeads}" /> 
							</td>
						</tr>
						<tr>
			                <td class="whiteboxwk"><s:text name='workprogressabstract.depositCOA'/> : </td>
			                <td class="whitebox2wk" colspan="3">
								<s:select headerKey="-1" headerValue="%{getText('default.dropdown.select')}" 
									name="coa" id="coa" cssClass="selectwk" list="dropdownData.coaList"
									listKey="id" listValue='glcode  + " : " + name' value="%{coa}"/>
							</td>
						</tr>
						<tr>
							<td colspan="4">
								<div class="buttonholdersearch">
									<s:hidden name="reportType" id="reportType" value="deptwise"/>
								    <s:submit cssClass="buttonadd" value="SEARCH" id="saveButton" name="button" onclick="hideResult();generateSubHeader();" method="search"/> &nbsp;&nbsp;&nbsp;
								    <input type="button" class="buttonfinal" value="CLEAR" id="button" name="clear" onclick="this.form.reset();">&nbsp;&nbsp;&nbsp;
								    <input type="button" class="buttonfinal" value="CLOSE" id="closeButton" name="button" onclick="window.close();" />
								</div>
							</td>
						</tr>
						<tr>
							
						</tr>
						<tr><td colspan="4">&nbsp;</td></tr>
						<tr id="resultRow" ><td colspan="4"> 
						 <s:if test="%{searchResult.size()!=0}">
						 <div style="display:block;width:1175px;height:auto;overflow-x: scroll;"> 
	  				        <display:table name="searchResult" uid="currentRowObject"
            							 cellpadding="0" cellspacing="0" export="false" id="currentRow"
             						 style="border:1px;width:100%;empty-cells:show;border-collapse:collapse;" requestURI="">
             							
       							<display:column  headerClass="pagetableth" class="pagetabletd" style="width:10%;text-align:left" title="Department"  property="department" />
       							<display:column  headerClass="pagetableth" class="pagetabletd" title="Estimates Prepared"
              							style="text-align:right;width:10%">    
              							<s:if test="%{#attr.currentRow.department!='Total' && #attr.currentRow.estimatesPrepared!=0}">             							
	              							<a href="Javascript:viewEstimatesDrillDown('1','${pageContext.request.contextPath}','<s:property  value='%{#attr.currentRow.department}' />','EST_PREPARED')">
												<s:property value="#attr.currentRow.estimatesPrepared" />
											</a>
										</s:if>
										<s:else>
												<s:property value="#attr.currentRow.estimatesPrepared" />
										</s:else>
								</display:column>
								<display:column  headerClass="pagetableth" class="pagetabletd" title="Value of Estimate Prepared (Cr)"
              							style="text-align:right;width:10%">               							
									 <s:property  value='%{#attr.currentRow.estPreparedValue}' />
								</display:column>
								<display:column  headerClass="pagetableth" class="pagetabletd" title="Admin Sanctioned"
              							style="text-align:right;width:10%">     
              							<s:if test="%{#attr.currentRow.department!='Total' && #attr.currentRow.adminSancEstimate!=0}"> 
	              							<a href="Javascript:viewEstimatesDrillDown('1','${pageContext.request.contextPath}','<s:property  value='%{#attr.currentRow.department}' />','adminSanctioned')">
												<s:property value="#attr.currentRow.adminSancEstimate" />
											</a>    
										</s:if>
										<s:else>
												<s:property value="#attr.currentRow.adminSancEstimate" />
										</s:else>   							
								</display:column>
								<display:column  headerClass="pagetableth" class="pagetabletd" title="AS - Estimate Value (Cr)"
              							style="text-align:right;width:10%">               							
									 <s:property  value='%{#attr.currentRow.adminSancEstValue}' />
								</display:column>
								<display:column  headerClass="pagetableth" class="pagetabletd" title="Balance No of Estimates"
              							style="text-align:right;width:10%">  
              							<s:if test="%{#attr.currentRow.department!='Total' && #attr.currentRow.estimateBalance!=0}"> 
	              							<a href="Javascript:viewEstimatesDrillDown('1','${pageContext.request.contextPath}','<s:property  value='%{#attr.currentRow.department}' />','BALANCE_EST')">
												<s:property value="#attr.currentRow.estimateBalance" />
											</a>     
										</s:if>     
										<s:else>
												<s:property value="#attr.currentRow.estimateBalance" />
										</s:else>
								</display:column>
								<display:column  headerClass="pagetableth" class="pagetabletd" title="Balance Estimates Value (Cr)"
              							style="text-align:right;width:10%">         							
									 <s:property  value='%{#attr.currentRow.estBalanceValue}' />
								</display:column>
								
								<display:column  headerClass="pagetableth" class="pagetabletd" title="Tender Called - WP"
              							style="text-align:right;width:10%">  
              						<s:if test="%{#attr.currentRow.department!='Total' && #attr.currentRow.tenderCalledWP!=0}">
              							<a href="#" onclick="showWPDetails('1','${pageContext.request.contextPath}','<s:property value='%{#attr.currentRow.department}' />')"> 
              								<s:property  value='%{#attr.currentRow.tenderCalledWP}' />
										</a>
              						</s:if>	
              						<s:else>
              								<s:property  value='%{#attr.currentRow.tenderCalledWP}' />
              						</s:else>
              					</display:column>
              					
								<display:column  headerClass="pagetableth" class="pagetabletd" title="Tender Called - Estimates"
              							style="text-align:right;width:10%">  
              						<s:if test="%{#attr.currentRow.department!='Total' && #attr.currentRow.tenderCalledEst!=0}">
              						<a href="#" onclick="showEstDetails('1','${pageContext.request.contextPath}','<s:property value='%{#attr.currentRow.department}' />')"> 
              							<s:property  value='%{#attr.currentRow.tenderCalledEst}' />
									</a>         
									</s:if>
									<s:else>
										<s:property  value='%{#attr.currentRow.tenderCalledEst}' />
									</s:else>   							
								</display:column>
								<display:column  headerClass="pagetableth" class="pagetabletd" title="Tender Finalized - TN"
              							style="text-align:right;width:10%">  
									
										<s:if test="%{#attr.currentRow.department!='Total' && #attr.currentRow.tenderFinalisedWP!=0}"> 
	              							<a href="Javascript:viewTenderFinalizedDrillDown('1','${pageContext.request.contextPath}','<s:property  value='%{#attr.currentRow.department}' />', 'TenderFinalizedTN')">
												<s:property value="#attr.currentRow.tenderFinalisedWP" />
											</a>     
										</s:if>     
										<s:else>
												<s:property value="#attr.currentRow.tenderFinalisedWP" />
										</s:else>           							
								</display:column>
								<display:column  headerClass="pagetableth" class="pagetabletd" title="Tender Finalized - Estimates"
              							style="text-align:right;width:10%">       
              							<s:if test="%{#attr.currentRow.department!='Total' && #attr.currentRow.tenderFinalisedEst!=0}"> 
	              							<a href="Javascript:viewTenderFinalizedDrillDown('1','${pageContext.request.contextPath}','<s:property  value='%{#attr.currentRow.department}' />', 'TenderFinalizedEst')">
												<s:property value="#attr.currentRow.tenderFinalisedEst" />
											</a>     
										</s:if>     
										<s:else>
												<s:property value="#attr.currentRow.tenderFinalisedEst" />
										</s:else> 
								</display:column>
								<display:column  headerClass="pagetableth" class="pagetabletd" title="Work Order"
              							style="text-align:right;width:10%">         
              						<s:if test="%{#attr.currentRow.department!='Total' && #attr.currentRow.workOrderWP!=0}">
              							<a href="#" onclick="showWODetails('1','${pageContext.request.contextPath}','<s:property value='%{#attr.currentRow.department}' />','WorkOrder')"> 
              								 <s:property  value='%{#attr.currentRow.workOrderWP}' />
										</a>
              						</s:if>	
              						<s:else>
              								 <s:property  value='%{#attr.currentRow.workOrderWP}' />
              						</s:else>	     							
									
								</display:column>
								<display:column  headerClass="pagetableth" class="pagetabletd" title="Work Order Estimates"
              							style="text-align:right;width:10%">    
              						<s:if test="%{#attr.currentRow.department!='Total' && #attr.currentRow.workOrderEst!=0}">
              							<a href="#" onclick="showWOEstDetails('1','${pageContext.request.contextPath}','<s:property value='%{#attr.currentRow.department}' />','WorkOrderEstimates')"> 
              								<s:property  value='%{#attr.currentRow.workOrderEst}' />
										</a>
              						</s:if>	
              						<s:else>
              								<s:property  value='%{#attr.currentRow.workOrderEst}' />
              						</s:else>	          							
								</display:column>
								<display:column  headerClass="pagetableth" class="pagetabletd" title="Work Value (Cr)"
              							style="text-align:right;width:10%">    
              						<s:if test="%{#attr.currentRow.department!='Total' && #attr.currentRow.workOrderAmt!=0.0000}">
              							<a href="#" onclick="ViewWorkValueDrillDown('1','${pageContext.request.contextPath}','<s:property value='%{#attr.currentRow.department}' />','WorkValue')"> 
              								<s:property  value='%{#attr.currentRow.workOrderAmt}' />
										</a>
              						</s:if>	
              						<s:else>
              								<s:property  value='%{#attr.currentRow.workOrderAmt}' />
              						</s:else>
								</display:column>
								<display:column  headerClass="pagetableth" class="pagetabletd" title="Work Not Started - Estimates"
              							style="text-align:right;width:10%">  
              						<s:if test="%{#attr.currentRow.department!='Total' && #attr.currentRow.workNotStartedEst!=0}">
              							<a href="#" onclick="showWOEstDetails('1','${pageContext.request.contextPath}','<s:property value='%{#attr.currentRow.department}' />','WorkNotStartedEstimates')"> 
              								<s:property  value='%{#attr.currentRow.workNotStartedEst}' />
										</a>
              						</s:if>	
              						<s:else>
              								<s:property  value='%{#attr.currentRow.workNotStartedEst}' />
              						</s:else>
								</display:column>
								<display:column  headerClass="pagetableth" class="pagetabletd" title="Work Not Started-Work Value (Cr)"
              							style="text-align:right;width:10%">
              						<s:if test="%{#attr.currentRow.department!='Total' && #attr.currentRow.workNotStartedAmt!=0.0000}">
              							<a href="#" onclick="ViewWorkValueDrillDown('1','${pageContext.request.contextPath}','<s:property value='%{#attr.currentRow.department}' />','WorkNotStartedValue')"> 
              								<s:property  value='%{#attr.currentRow.workNotStartedAmt}' />
										</a>
              						</s:if>	
              						<s:else>
              							<s:property  value='%{#attr.currentRow.workNotStartedAmt}' />
              						</s:else>               							
								</display:column>
							
								<display:column  headerClass="pagetableth" class="pagetabletd" title="Work Started - Estimates"
              							style="text-align:right;width:10%"> 
              						<s:if test="%{#attr.currentRow.department!='Total' && #attr.currentRow.workStartedEst!=0}">
              							<a href="#" onclick="showWOEstDetails('1','${pageContext.request.contextPath}','<s:property value='%{#attr.currentRow.department}' />','WorkStartedEstimates')"> 
              								<s:property  value='%{#attr.currentRow.workStartedEst}' />
										</a>
              						</s:if>	
              						<s:else>
              								<s:property  value='%{#attr.currentRow.workStartedEst}' />
              						</s:else>	             							
								</display:column>
								<display:column  headerClass="pagetableth" class="pagetabletd" title="Work Started - Work Value (Cr)"
              							style="text-align:right;width:10%">  
              						<s:if test="%{#attr.currentRow.department!='Total' && #attr.currentRow.workStartedAmt!=0.0000}">
              							<a href="#" onclick="ViewWorkValueDrillDown('1','${pageContext.request.contextPath}','<s:property value='%{#attr.currentRow.department}' />','WorkStartedWorkValue')"> 
              								<s:property  value='%{#attr.currentRow.workStartedAmt}' />
										</a>
              						</s:if>	
              						<s:else>
              							<s:property  value='%{#attr.currentRow.workStartedAmt}' />
              						</s:else>             							
								</display:column>
								<display:column  headerClass="pagetableth" class="pagetabletd" title="Inprogress 1- 25 %"
              							style="text-align:right;width:10%"> 
              						 <s:if test="%{#attr.currentRow.department!='Total' && #attr.currentRow.inProgress25!=0}">
              							<a href="#" onclick="showMilestoneDetails('1','${pageContext.request.contextPath}','<s:property value='%{#attr.currentRow.department}' />','InProgress25')">	             							
									 		<s:property  value='%{#attr.currentRow.inProgress25}' />
									 	</a>
									 </s:if>
									 <s:else>
									 		<s:property  value='%{#attr.currentRow.inProgress25}' />
									 </s:else>	
								</display:column>
								<display:column  headerClass="pagetableth" class="pagetabletd" title="Inprogress 26- 50 %"
              							style="text-align:right;width:10%">      
              						 <s:if test="%{#attr.currentRow.department!='Total' && #attr.currentRow.inProgress50!=0}">
              							<a href="#" onclick="showMilestoneDetails('1','${pageContext.request.contextPath}','<s:property value='%{#attr.currentRow.department}' />','InProgress50')">	             							
									 		<s:property  value='%{#attr.currentRow.inProgress50}' />
									 	</a>
									 </s:if>
									 <s:else>
									 		<s:property  value='%{#attr.currentRow.inProgress50}' />
									 </s:else>	        							
								</display:column>
								<display:column  headerClass="pagetableth" class="pagetabletd" title="Inprogress 51- 75 %"
              							style="text-align:right;width:10%"> 
              						 <s:if test="%{#attr.currentRow.department!='Total' && #attr.currentRow.inProgress75!=0}">
              							<a href="#" onclick="showMilestoneDetails('1','${pageContext.request.contextPath}','<s:property value='%{#attr.currentRow.department}' />','InProgress75')">	             							
									 		<s:property  value='%{#attr.currentRow.inProgress75}' />
									 	</a>
									 </s:if>
									 <s:else>
									 		<s:property  value='%{#attr.currentRow.inProgress75}' />
									 </s:else>	             							
									 
								</display:column>
								<display:column  headerClass="pagetableth" class="pagetabletd" title="Inprogress 76- 99 %"
              							style="text-align:right;width:10%">  
              						 <s:if test="%{#attr.currentRow.department!='Total' && #attr.currentRow.inProgress99!=0}">
              							<a href="#" onclick="showMilestoneDetails('1','${pageContext.request.contextPath}','<s:property value='%{#attr.currentRow.department}' />','InProgress99')">	             							
									 		<s:property  value='%{#attr.currentRow.inProgress99}' />
									 	</a>
									 </s:if>
									 <s:else>
									 		<s:property  value='%{#attr.currentRow.inProgress99}' />
									 </s:else>		
              					</display:column>
								<display:column  headerClass="pagetableth" class="pagetabletd" title="Works Completed"
              							style="text-align:right;width:10%">
              						 <s:if test="%{#attr.currentRow.department!='Total' && #attr.currentRow.worksCompleted!=0}">
              							<a href="#" onclick="showMilestoneDetails('1','${pageContext.request.contextPath}','<s:property value='%{#attr.currentRow.department}' />','WorksCompleted')">	             							
									 		<s:property  value='%{#attr.currentRow.worksCompleted}' />
									 	</a>
									 </s:if>
									 <s:else>
									 		<s:property  value='%{#attr.currentRow.worksCompleted}' />
									 </s:else>	              							
								</display:column>
								<display:column  headerClass="pagetableth" class="pagetabletd" title="Nos of Works yet to be completed"
              							style="text-align:right;width:10%">              							
									 <s:if test="%{#attr.currentRow.department!='Total' && #attr.currentRow.worksNotCompleted!=0}">
              							<a href="#" onclick="showMilestoneDetails('1','${pageContext.request.contextPath}','<s:property value='%{#attr.currentRow.department}' />','WorksNotCompleted')">	             							
									 		<s:property  value='%{#attr.currentRow.worksNotCompleted}' />
									 	</a>
									 </s:if>
									 <s:else>
									 		<s:property  value='%{#attr.currentRow.worksNotCompleted}' />
									 </s:else>
								</display:column>
								<display:column  headerClass="pagetableth" class="pagetabletd" title="Total Nos of completed works"
              							style="text-align:right;width:10%">              							
									 <s:property  value='%{#attr.currentRow.numberOfCompletedWorks}' />
								</display:column>
								<display:column  headerClass="pagetableth" class="pagetabletd" title="Work value of completed works(Cr)"
              							style="text-align:right;width:10%">              							
									 <s:property  value='%{#attr.currentRow.valueOfCompletedWorks}' />
								</display:column>
								<display:column  headerClass="pagetableth" class="pagetabletd" title="Nos of vouchers"
              							style="text-align:right;width:10%">              							
									 <s:property  value='%{#attr.currentRow.voucherCount}' />
								</display:column>
								<display:column  headerClass="pagetableth" class="pagetabletd" title="Payment Released (Cr)"
              							style="text-align:right;width:10%">               							
									 <s:property  value='%{#attr.currentRow.paymentReleased}' />
								</display:column>
								<display:caption  media="html"style='font-weight:bold' >
									<s:property value="%{subHeader}" />
								</display:caption>
						</display:table>
						<br />
						<div class="buttonholderwk" id="divButRow1" name="divButRow1">
							<s:submit cssClass="buttonpdf" value="VIEW PDF" onclick="disableMasking();" id="pdfButton" name="pdfButton" method="generatePDF"/> 
							<s:submit cssClass="buttonpdf" value="VIEW XLS" onclick="disableMasking();" id="pdfButton" name="pdfButton" method="generateXLS"/>
						</div>
						</div>
						</s:if>
						<s:else>
							<s:if test="%{resultStatus=='afterSearch'}">
								<div align="center"><font color="red">No record Found.</font></div>
							</s:if>	
						</s:else>
						</td></tr>
				</table>
			</td>
		</tr>
		</table>

	</div>
	</div>
	</div>
	</div>									
</s:form>
</body>
</html>