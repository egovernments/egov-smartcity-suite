<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ page import="java.util.*" %>

<html>
<title><s:text name='page.title.workprogressabstract.bydepartment' /></title>
	<script type="text/javascript">
	function setupSubTypes(elem){
		categoryId=elem.options[elem.selectedIndex].value;
    	populatecategory({category:categoryId});
	}	
	function openWODetails(source,criteria){
		var url="${pageContext.request.contextPath}/reports/workProgress!getWoDetails.action?source="+source+"&criteria="+criteria;
		url=url+"&fromDate="+document.getElementById("fromDate").value+"&toDate="+document.getElementById("toDate").value+"&executingDepartment="+document.getElementById("executingDepartment").value;
		url=url+"&category="+document.getElementById("category").value+"&parentCategory="+document.getElementById("parentCategory").value+"&expenditureType="+document.getElementById("expenditureType").value;
		url=url+"&workStatus="+document.getElementById("workStatus").value+"&fund="+document.getElementById("fund").value+"&function="+document.getElementById("function").value+"&budgetHead="+document.getElementById("budgetHead").value;
		url=url+"&preparedBy="+document.getElementById("preparedBy").value+"&reportType="+dom.get('reportType').value+"&scheme="+document.getElementById("scheme").value+"&subScheme="+document.getElementById("subScheme").value;
		window.open(url,'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
	}

	function openEstDetails(source,criteria){
		var url="${pageContext.request.contextPath}/reports/workProgress!getEstimateDetails.action?source="+source+"&criteria="+criteria;
		url=url+"&fromDate="+document.getElementById("fromDate").value+"&toDate="+document.getElementById("toDate").value+"&executingDepartment="+document.getElementById("executingDepartment").value;
		url=url+"&category="+document.getElementById("category").value+"&parentCategory="+document.getElementById("parentCategory").value+"&expenditureType="+document.getElementById("expenditureType").value;
		url=url+"&workStatus="+document.getElementById("workStatus").value+"&fund="+document.getElementById("fund").value+"&function="+document.getElementById("function").value+"&budgetHead="+document.getElementById("budgetHead").value;
		url=url+"&preparedBy="+document.getElementById("preparedBy").value+"&reportType="+dom.get('reportType').value+"&scheme="+document.getElementById("scheme").value+"&subScheme="+document.getElementById("subScheme").value;
		window.open(url,'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
	}

	function openWPDetails(source,criteria){
		var url="${pageContext.request.contextPath}/reports/workProgress!getWPDetails.action?source="+source+"&criteria="+criteria;
		url=url+"&fromDate="+document.getElementById("fromDate").value+"&toDate="+document.getElementById("toDate").value+"&executingDepartment="+document.getElementById("executingDepartment").value;
		url=url+"&category="+document.getElementById("category").value+"&parentCategory="+document.getElementById("parentCategory").value+"&expenditureType="+document.getElementById("expenditureType").value;
		url=url+"&workStatus="+document.getElementById("workStatus").value+"&fund="+document.getElementById("fund").value+"&function="+document.getElementById("function").value+"&budgetHead="+document.getElementById("budgetHead").value;
		url=url+"&preparedBy="+document.getElementById("preparedBy").value+"&reportType="+dom.get('reportType').value+"&scheme="+document.getElementById("scheme").value+"&subScheme="+document.getElementById("subScheme").value;
		window.open(url,'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
	}

	function openTNDetails(source,criteria){ 
		var url="${pageContext.request.contextPath}/reports/workProgress!getTNDetails.action?source="+source+"&criteria="+criteria;
		url=url+"&fromDate="+document.getElementById("fromDate").value+"&toDate="+document.getElementById("toDate").value+"&executingDepartment="+document.getElementById("executingDepartment").value;
		url=url+"&category="+document.getElementById("category").value+"&parentCategory="+document.getElementById("parentCategory").value+"&expenditureType="+document.getElementById("expenditureType").value;
		url=url+"&workStatus="+document.getElementById("workStatus").value+"&fund="+document.getElementById("fund").value+"&function="+document.getElementById("function").value+"&budgetHead="+document.getElementById("budgetHead").value;
		url=url+"&preparedBy="+document.getElementById("preparedBy").value+"&reportType="+dom.get('reportType').value+"&scheme="+document.getElementById("scheme").value+"&subScheme="+document.getElementById("subScheme").value;
		window.open(url,'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
	}


	function openWPEstDetails(source,criteria){
		var url="${pageContext.request.contextPath}/reports/workProgress!getWPEstimateDetails.action?source="+source+"&criteria="+criteria;
		url=url+"&fromDate="+document.getElementById("fromDate").value+"&toDate="+document.getElementById("toDate").value+"&executingDepartment="+document.getElementById("executingDepartment").value;
		url=url+"&category="+document.getElementById("category").value+"&parentCategory="+document.getElementById("parentCategory").value+"&expenditureType="+document.getElementById("expenditureType").value;
		url=url+"&workStatus="+document.getElementById("workStatus").value+"&fund="+document.getElementById("fund").value+"&function="+document.getElementById("function").value+"&budgetHead="+document.getElementById("budgetHead").value;
		url=url+"&preparedBy="+document.getElementById("preparedBy").value+"&reportType="+dom.get('reportType').value+"&scheme="+document.getElementById("scheme").value+"&subScheme="+document.getElementById("subScheme").value;
		window.open(url,'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
	}

	function openWOEstDetails(source,criteria){
		var url="${pageContext.request.contextPath}/reports/workProgress!getWOEstimateDetails.action?source="+source+"&criteria="+criteria;
		url=url+"&fromDate="+document.getElementById("fromDate").value+"&toDate="+document.getElementById("toDate").value+"&executingDepartment="+document.getElementById("executingDepartment").value;
		url=url+"&category="+document.getElementById("category").value+"&parentCategory="+document.getElementById("parentCategory").value+"&expenditureType="+document.getElementById("expenditureType").value;
		url=url+"&workStatus="+document.getElementById("workStatus").value+"&fund="+document.getElementById("fund").value+"&function="+document.getElementById("function").value+"&budgetHead="+document.getElementById("budgetHead").value;
		url=url+"&preparedBy="+document.getElementById("preparedBy").value+"&reportType="+dom.get('reportType').value+"&scheme="+document.getElementById("scheme").value+"&subScheme="+document.getElementById("subScheme").value;
		window.open(url,'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
	}

	function setupSubSchemes(elem){
		var id=elem.options[elem.selectedIndex].value;
		populatesubScheme({schemeId:id});
	}
	
	</script>
	<body>
		<div class="errorstyle" id="workProgress_error"
			style="display: none;"></div>
		<s:if test="%{hasErrors()}">
			<div class="errorstyle">
				<s:actionerror />
				<s:fielderror />
			</div>
		</s:if>
		<s:form name="workProgressForm" action="workProgress!getWorkProgress.action"
			id="workProgressForm" theme="simple"
			>
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
									
										&nbsp;
									</td>
								</tr>
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
													<s:select headerKey="-1" headerValue="%{getText('workprogressabstract.select')}" name="executingDepartment" 
													id="executingDepartment" cssClass="selectwk" list="dropdownData.executingDepartmentList" listKey="id" 
													listValue="deptName" value="%{executingDepartment}" />
													
												</td>
													<td width="15%" class="whiteboxwk">
													<s:text name="workprogressabstract.work.nature" />
													:
												</td>
												<td width="53%" class="whitebox2wk">
													<s:select headerKey="-1" headerValue="%{getText('workprogressabstract.select')}"
														name="expenditureType" id="expenditureType" cssClass="selectwk"
														list="dropdownData.typeList" listKey="id" listValue="name"
														value="%{expenditureType}" />
												</td>
											</tr>
											<tr>
												<td class="greyboxwk">
													<s:text name="workprogressabstract.work.type" />
													:
												</td>
												<td class="greybox2wk">
													<s:select headerKey="-1" headerValue="%{getText('workprogressabstract.select')}"
														name="parentCategory" id="parentCategory"
														cssClass="selectwk" list="dropdownData.parentCategoryList"
														listKey="id" listValue="description"
														value="%{parentCategory}"
														onChange="setupSubTypes(this);" />
													<egov:ajaxdropdown id="categoryDropdown"
														fields="['Text','Value']" dropdownId='category'
														url='estimate/ajaxEstimate!subcategories.action'
														selectedValue="%{category.id}" />
												</td>

												<td class="greyboxwk">
													<s:text name="workprogressabstract.work.subtype" />
													:
												</td>
												<td class="greybox2wk">
													<s:select headerKey="-1" headerValue="%{getText('workprogressabstract.select')}" name="category"
														value="%{category}" id="category" cssClass="selectwk"
														list="dropdownData.categoryList" listKey="id"
														listValue="description" />
												</td>
											</tr>
											<tr>
												<td width="15%" class="whiteboxwk">
													<s:text name='workprogressabstract.fund'/> :				
												</td>
												<td width="53%" class="whitebox2wk">
													<s:select headerKey="-1" headerValue="%{getText('workprogressabstract.select')}"
													name="fund" id="fund" cssClass="selectwk" list="dropdownData.fundList" listKey="id" 
													listValue="name" value="%{fund}" />
												</td>
												<td width="11%" class="whiteboxwk">
													<s:text name='workprogressabstract.function'/> :
												</td>
												<td width="21%" class="whitebox2wk">
													<s:select headerKey="-1" headerValue="%{getText('workprogressabstract.select')}"
									 					name="function" id="function" cssClass="selectwk" list="dropdownData.functionList" 
									 					listKey="id" listValue="name" value="%{function}"   />
												</td>
											</tr>
											<tr><td class="greyboxwk"><s:text name='workprogressabstract.createdby'/> :
											</td >
											<td class="greybox2wk">
											<s:select headerKey="-1"
															headerValue="%{getText('workprogressabstract.select')}"
															name="preparedBy" value="%{preparedBy}" id="preparedBy"
															cssClass="selectwk" list="dropdownData.preparedByList"
															listKey="id" listValue="employeeName" />
											</td>
												
												<td class="greyboxwk"><s:text name='workprogressabstract.work.status'/> :
												</td>
												<td class="greybox2wk">	<s:select 
		headerKey="-1" headerValue="%{getText('workprogressabstract.select')}"
		list="#{'given':'Work Order Given', 'total0to25':'Inprogress 0-25 %',
		'total26to50':'Inprogress 26-50 %', 'total51to75':'Inprogress 51-75 %', 'total76to100':'Inprogress 76-99 %', 'completed':'Completed',
		'cancelled':'Work Order Cancelled'}" 
		name="workStatus" id="workStatus" value="%{workStatus}" />
												</td>
											</tr>
											
											<tr>
											<tr>
								                <td class="whiteboxwk"><s:text name='workprogressabstract.scheme'/> : </td>
								                <td class="whitebox2wk"><s:select headerKey="-1" headerValue="%{getText('estimate.default.select')}" name="scheme" id="scheme" cssClass="selectwk" list="dropdownData.schemeList" listKey="id" listValue="name" value="%{scheme}"  onChange="setupSubSchemes(this);"/>
												<egov:ajaxdropdown id="subSchemeDropdown" fields="['Text','Value']" dropdownId='subScheme' url='reports/ajaxWorkProgress!loadSubSchemes.action' selectedValue="%{scheme.id}"/></td>
								                <td class="whiteboxwk"><s:text name='workprogressabstract.subscheme'/> : </td>
								                <td class="whitebox2wk"><s:select headerKey="-1" headerValue="%{getText('estimate.default.select')}" name="subScheme" id="subScheme" cssClass="selectwk" list="dropdownData.subSchemeList" listKey="id" listValue="name" value="%{subScheme}" /></td>
											</tr>
											
											<tr><td width="11%" class="greyboxwk">
													<s:text name='workprogressabstract.budgethead'/> :
												</td>
												<td class="greybox2wk" colspan="3">
													<s:select headerKey="-1" headerValue="%{getText('workprogressabstract.select')}"
														name="budgetHead" id="budgetHead" cssClass="selectwk" list="dropdownData.budgetHeadList" 
														listKey="id" listValue="name" value="%{budgetHead}"   />
												</td>
											</tr>
											<tr>
								<td colspan="4">
									<div class="buttonholdersearch">
										<s:hidden name="reportType" id="reportType" value="deptwise"/>
									    <s:submit cssClass="buttonadd" value="SEARCH" id="saveButton" name="button" method="getWorkProgress"/> &nbsp;&nbsp;&nbsp;
									    <input type="button" class="buttonfinal" value="CLEAR" id="button" name="clear" onclick="this.form.reset();">&nbsp;&nbsp;&nbsp;
									    <input type="button" class="buttonfinal" value="CLOSE"
															id="closeButton" name="button" onclick="window.close();" />
									</div>
								</td>
							</tr>
							<tr><td colspan="4"> <logic:notEmpty name="woList">
				  				        <display:table name="woList" uid="currentRowObject"
               							 cellpadding="0" cellspacing="0" export="true" id="reportsmodule"
                						 style="border:1px;width:100%;empty-cells:show;border-collapse:collapse;"
               							 requestURI="">
               							 <display:caption media="pdf"> <center><s:text name='page.title.workprogressabstract.bydepartment' /></center>
               							 </display:caption>
               							   <display:column  headerClass="pagetableth" class="pagetabletd" style="width:10%;text-align:center" title="Department"  property="CONDITION">
                								<s:text name ="CONDITION"/> 
                							</display:column>
                							
                							<display:column  headerClass="pagetableth" class="pagetabletd" title="Admin Sanctioned Estimates"
                    							style="text-align:right;width:10%">
                    						<s:if test="%{#attr.reportsmodule.CONDITION!='Total'}">
	                    						<s:if test="%{#attr.reportsmodule.adminSanEstimates !=0 }"> 
	                    							<a href="Javascript:openEstDetails('adminSanctionEst','<s:property  value='%{#attr.reportsmodule.CONDITION}' />')">
														 <s:property  value='%{#attr.reportsmodule.adminSanEstimates}' />
													</a>
												</s:if>
												<s:else>
													0
												</s:else> 
											</s:if>
											<s:else>
												<s:property  value='%{#attr.reportsmodule.adminSanEstimates}' />
											</s:else>	
										</display:column>
										
														
										
										<display:column  headerClass="pagetableth" class="pagetabletd" title="Works Package Approved"
                    							style="text-align:right;width:10%" >
                    						<s:if test="%{#attr.reportsmodule.CONDITION!='Total'}">
	                    						<s:if test="%{#attr.reportsmodule.approvedWorksPackages !=0 }"> 
	                    							<a href="Javascript:openWPDetails('wpApproved','<s:property  value='%{#attr.reportsmodule.CONDITION}' />')">
														 <s:property  value='%{#attr.reportsmodule.approvedWorksPackages}' />
													</a>
												</s:if>
												<s:else>
													0
												</s:else>
											</s:if>
											<s:else>
												<s:property  value='%{#attr.reportsmodule.approvedWorksPackages}' />
											</s:else>		 
										</display:column> 
										
										<display:column  headerClass="pagetableth" class="pagetabletd" title="Estimates Covered in WP approved"
                    							style="text-align:right;width:10%" >
                    						<s:if test="%{#attr.reportsmodule.CONDITION!='Total'}">
	                    						<s:if test="%{#attr.reportsmodule.approvedWPLinkedEst !=0 }"> 
	                    							<a href="Javascript:openWPEstDetails('wpEstimate','<s:property  value='%{#attr.reportsmodule.CONDITION}' />')">
														 <s:property  value='%{#attr.reportsmodule.approvedWPLinkedEst}' />
													</a>
												</s:if>
												<s:else>
													0
												</s:else>
											</s:if>
											<s:else>
												<s:property  value='%{#attr.reportsmodule.approvedWPLinkedEst}' />
											</s:else>		 
										</display:column>
										
										
										<display:column  headerClass="pagetableth" class="pagetabletd" title="Tender Negotiated"
                    							style="text-align:right;width:10%" >
                    						<s:if test="%{#attr.reportsmodule.CONDITION!='Total'}">	
	                    						<s:if test="%{#attr.reportsmodule.approvedTenderNegotiations !=0 }"> 
	                    							<a href="Javascript:openTNDetails('tnApproved','<s:property  value='%{#attr.reportsmodule.CONDITION}' />')">
														 <s:property  value='%{#attr.reportsmodule.approvedTenderNegotiations}' />
													</a>
												</s:if>
												<s:else>
													0
												</s:else>
											</s:if>
											<s:else>
												<s:property  value='%{#attr.reportsmodule.approvedTenderNegotiations}' />
											</s:else>	 
										</display:column>
                							
               							 <display:column  headerClass="pagetableth" class="pagetabletd" title="Work Order Given"
                    							style="text-align:right;width:10%" >
                    						<s:if test="%{#attr.reportsmodule.CONDITION!='Total'}">		
	                    						<s:if test="%{#attr.reportsmodule.given !=0 }"> 
	                    							<a href="Javascript:openWODetails('given','<s:property  value='%{#attr.reportsmodule.CONDITION}' />')">
														 <s:property  value='%{#attr.reportsmodule.given}' />
													</a>
												</s:if>
												<s:else>
													0
												</s:else> 
											</s:if>
											<s:else>
												<s:property  value='%{#attr.reportsmodule.given}' />
											</s:else>	
										</display:column>
										
										
										<display:column  headerClass="pagetableth" class="pagetabletd" title="Estimates covered in WO given"
                    							style="text-align:right;width:10%" >
                    						<s:if test="%{#attr.reportsmodule.CONDITION!='Total'}">		
	                    						<s:if test="%{#attr.reportsmodule.workOrderGivenLinkedEst !=0 }"> 
	                    							<a href="Javascript:openWOEstDetails('woEstimate','<s:property  value='%{#attr.reportsmodule.CONDITION}' />')">
														 <s:property  value='%{#attr.reportsmodule.workOrderGivenLinkedEst}' />
													</a>
												</s:if>
												<s:else>
													0
												</s:else> 
											</s:if>
											<s:else>
												<s:property  value='%{#attr.reportsmodule.workOrderGivenLinkedEst}' />
											</s:else>	
										</display:column>
                    					
                    					<display:column headerClass="pagetableth" class="pagetabletd" title="Inprogress 0-25 %"
                    							style="text-align:right;width:10%" >
                    						<s:if test="%{#attr.reportsmodule.CONDITION!='Total'}">		
	                    						<s:if test="%{#attr.reportsmodule.total0to25 !=0 }">  
	                    							<a href="Javascript:openWODetails('total0to25','<s:property  value='%{#attr.reportsmodule.CONDITION}' />')">
														 <s:property  value='%{#attr.reportsmodule.total0to25}' />
													</a>
												</s:if>
												<s:else>
													0
												</s:else>
											</s:if>
											<s:else>
												<s:property  value='%{#attr.reportsmodule.total0to25}' />
											</s:else>	 
										</display:column>
                    					<display:column headerClass="pagetableth" class="pagetabletd" title="Inprogress 26-50 %"
                    							style="text-align:right;width:10%" >
                    						<s:if test="%{#attr.reportsmodule.CONDITION!='Total'}">			
	                    						<s:if test="%{#attr.reportsmodule.total26to50 !=0 }"> 
	                    							<a href="Javascript:openWODetails('total26to50','<s:property  value='%{#attr.reportsmodule.CONDITION}' />')">
														 <s:property  value='%{#attr.reportsmodule.total26to50}' />
													</a>
												</s:if>
												<s:else>
													0
												</s:else>
											</s:if>
											<s:else>
												<s:property  value='%{#attr.reportsmodule.total26to50}' />
											</s:else>		 
										</display:column>
                    					<display:column headerClass="pagetableth" class="pagetabletd" title="Inprogress 51-75 %"
                    							style="text-align:right;width:10%" >
                    						<s:if test="%{#attr.reportsmodule.CONDITION!='Total'}">		
	                    						<s:if test="%{#attr.reportsmodule.total51to75 !=0 }"> 
	                    							<a href="Javascript:openWODetails('total51to75','<s:property  value='%{#attr.reportsmodule.CONDITION}' />')">
														 <s:property  value='%{#attr.reportsmodule.total51to75}' />
													</a>
												</s:if>
												<s:else>
													0
												</s:else>
											</s:if> 
											<s:else>
												<s:property  value='%{#attr.reportsmodule.total51to75}' />
											</s:else>		
										</display:column>
                    					<display:column headerClass="pagetableth" class="pagetabletd" title="Inprogress 76-99 %"
                    							style="text-align:right;width:10%">
                    						<s:if test="%{#attr.reportsmodule.CONDITION!='Total'}">		
	                    						<s:if test="%{#attr.reportsmodule.total76to100 !=0 }"> 
	                    							<a href="Javascript:openWODetails('total76to100','<s:property  value='%{#attr.reportsmodule.CONDITION}' />')">
														 <s:property  value='%{#attr.reportsmodule.total76to100}' />
													</a>
												</s:if>
												<s:else>
													0
												</s:else>
											</s:if> 
											<s:else>
												<s:property  value='%{#attr.reportsmodule.total76to100}' />
											</s:else>	 
										</display:column>
                    					<display:column headerClass="pagetableth" class="pagetabletd" title="Work Completed"
                    							style="text-align:right;width:10%" >
                    						<s:if test="%{#attr.reportsmodule.CONDITION!='Total'}">		
	                    						<s:if test="%{#attr.reportsmodule.completed !=0 }">
	                    							<a href="Javascript:openWODetails('completed','<s:property  value='%{#attr.reportsmodule.CONDITION}' />')">
														 <s:property  value='%{#attr.reportsmodule.completed}' /> 
													</a>
												</s:if>
												<s:else>
													0
												</s:else>
											</s:if> 
											<s:else>
												<s:property  value='%{#attr.reportsmodule.completed}' />
											</s:else>	 
										</display:column>
										
										
										<display:column headerClass="pagetableth" class="pagetabletd" title="Balance Nos of works"
                    							style="text-align:right;width:10%">
                    							 <s:property  value='%{#attr.reportsmodule.BalanceWorks}' /> 
                    					</display:column>
                    					<display:column headerClass="pagetableth" class="pagetabletd" title="Work Order Value"
                    							style="text-align:right;width:10%" property="wovalue" />
                    							
                    					<display:column headerClass="pagetableth" class="pagetabletd" title="Payment Released (Nos, Amount)"
                    							style="text-align:right;width:10%" property="paymentInfo" />
                    					
                    					 <display:setProperty name="export.csv" value="false" />
                <display:setProperty name="export.excel" value="true" />
                <display:setProperty name="export.xml" value="false" />
                <display:setProperty name="export.pdf" value="true" />
                <display:setProperty name="export.pdf.filename" value="WorkProgress-ByDepartment.pdf" />
		<display:setProperty name="export.excel.filename" value="WorkProgress-ByDepartment.xls" />
               							</display:table>
				  				        </logic:notEmpty></td></tr>
				  				       
				 			<tr><td colspan="4">&nbsp;</td></tr>
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
