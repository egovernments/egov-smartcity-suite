<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>

<script src="<egov:url path='js/works.js'/>"></script>
<script src="<egov:url path='js/helper.js'/>"></script>
 
<html>
	<head>	
		<title><s:text name='plannedEstimateReport.page.title' />
		</title>
	</head>
<script type="text/javascript">

var estimateNumberSearchSelectionHandler = function(sType, arguments){ 
	var oData = arguments[2];
};
   
function setupSubTypes(elem) {
	categoryId=elem.options[elem.selectedIndex].value;
    populatecategory({category:categoryId});
}

function setupPreparedByList(elem) {
 
    deptId=elem.options[elem.selectedIndex].value;
    populatepreparedBy({executingDepartment:deptId});
 
}
function validateInput(){	
	if(dom.get('type').value==-1 && dom.get('executingDepartment').value==-1 && dom.get('userDepartment').value==-1	&&
			dom.get('fromDate').value=="" && dom.get('toDate').value==""  &&  dom.get('preparedBy').value==-1 && dom.get('estimateNumberSearch').value=="" &&
			dom.get('parentCategory').value==-1 && dom.get('category').value==-1 && dom.get('description').value=="") {
				dom.get("plannedEstimateReport_error").innerHTML='<s:text name="plannedEstimateReport.validation.error"/>'
				dom.get("plannedEstimateReport_error").style.display='';
	        	return false;
	}
	else {
		document.getElementById("plannedEstimateReport_error").innerHTML='';
		document.getElementById("plannedEstimateReport_error").style.display="none";
	}
	hideResult(); 
	return true;
}

function hideResult() {
	dom.get("resultRow").style.display='none';
}
function disableMasking(){
	document.getElementById('loadingMask').remove();
}
</script>
		
	<body >
		<div class="errorstyle" id="plannedEstimateReport_error"
			style="display: none;"></div>
		<s:if test="%{hasErrors()}">
			<div class="errorstyle">
				<s:actionerror />
				<s:fielderror />
			</div>
		</s:if>
		<s:form name="plannedEstimateReportForm" action="plannedEstimateReport"	theme="simple">
			
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
														<s:text name="page.subheader.search.estimate" />
													</div>
												</td>
											</tr>
											<tr>
												<td width="15%" class="greyboxwk">
														<s:text name="estimate.executing.department" /> :
													</td>
													
													<td width="53%" class="greybox2wk">
															<s:select headerKey="-1"
																headerValue="%{getText('estimate.default.select')}"
																name="execDept" id="executingDepartment"
																cssClass="selectwk"
																list="dropdownData.executingDepartmentList" listKey="id"
																listValue="deptName" value="%{execDept}"
																onChange="setupPreparedByList(this);" />
															<egov:ajaxdropdown id="preparedBy"
																fields="['Text','Value','Designation']"
																dropdownId='preparedBy' optionAttributes='Designation'
																url='estimate/ajaxEstimate!usersInExecutingDepartment.action' />
													</td>
													<td width="15%" class="greyboxwk">
														<s:text name="plannedEstimateReport.userDept" /> :
													</td>
													<td width="53%" class="greybox2wk">
															<s:select headerKey="-1" headerValue="%{getText('estimate.default.select')}"
																name="userDept" id="userDepartment" cssClass="selectwk"
																list="dropdownData.userDepartmentList" listKey="id"	listValue="deptName" value="%{userDept}" />
															
													</td>
											</tr>
											<tr>
												<td width="11%" class="whiteboxwk">
													<s:text name="estimate.search.estimateNo" /> :
												</td>
												
												<td class="whitebox2wk">
														<div class="yui-skin-sam">
					        							<div id="estimateNumberSearch_autocomplete">
				                							<div>
					        									<s:textfield id="estimateNumberSearch" name="estimatenumber" 
					        										value="%{estimatenumber}" cssClass="selectwk" />
					        								</div>
					        								<span id="estimateNumberSearchResults"></span>
					        							</div>	
					        						</div>
					        						<egov:autocomplete name="estimateNumberSearch" width="20" 
					        							field="estimateNumberSearch" url="../estimate/ajaxEstimate!searchEstimateNumberForDraftEstimates.action?" 
					        							queryQuestionMark="false" results="estimateNumberSearchResults" 
					        							handler="estimateNumberSearchSelectionHandler" queryLength="3"/>
													</td>
			
												
												<td width="15%" class="whiteboxwk">
													<s:text name="estimate.work.nature" /> :
												</td>
												<td width="53%" class="whitebox2wk">
													<s:select headerKey="-1" headerValue="ALL"
														name="expenditureType" id="type" cssClass="selectwk"
														list="dropdownData.typeList" listKey="id" listValue="name"
														value="%{expenditureType}" />
												</td>
												<s:hidden name="expenditureTypeid"
													value="%{expenditureType}"></s:hidden>
											</tr>
											<tr>
												<td class="greyboxwk">
													<s:text name="plannedEstimateReport.estimate.fromDate" /> :
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
													<s:text name="plannedEstimateReport.estimate.toDate" /> :
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
												<td class="whiteboxwk">
													<s:text name="estimate.work.type" /> :
												</td>
												<td class="whitebox2wk">
													<s:select headerKey="-1" headerValue="ALL"
														name="parentCategory" id="parentCategory"
														cssClass="selectwk" list="dropdownData.parentCategoryList"
														listKey="id" listValue="description"
														value="%{parentCategory.id}"
														onChange="setupSubTypes(this);" />
													<egov:ajaxdropdown id="categoryDropdown"
														fields="['Text','Value']" dropdownId='category'
														url='estimate/ajaxEstimate!subcategories.action'
														selectedValue="%{category.id}" />
												</td>

												<td class="whiteboxwk">
													<s:text name="estimate.work.subtype" />
													:
												</td>
												<td class="whitebox2wk">
													<s:select headerKey="-1" headerValue="ALL" name="category"
														value="%{category.id}" id="category" cssClass="selectwk"
														list="dropdownData.categoryList" listKey="id"
														listValue="description" />
												</td>
											</tr>
											<tr>
													<td class="greyboxwk">
														<s:text name="estimate.preparedBy" />
														:
													</td>
													<td class="greybox2wk">
														<s:select headerKey="-1"
															headerValue="%{getText('estimate.default.select')}"
															name="preparedById" value="%{preparedById}" id="preparedBy"
															cssClass="selectwk" list="dropdownData.preparedByList"
															listKey="id" listValue="employeeName" />

													</td>

													<td class="greyboxwk">
														<s:text name="estimate.description" />
														:
													</td>
													<td class="greybox2wk">
														<s:textarea name="description" cols="50" rows="3" cssClass="selectwk"
														 id="description" value="%{description}"/>
													</td>
											</tr>
											
											<tr>
												<td colspan="4" class="shadowwk"></td>
											</tr>
											<tr>
												<td colspan="4">
													<div  class="buttonholderwk" align="center">
														<s:submit cssClass="buttonadd" value="SEARCH"
															id="saveButton" name="button" onClick="return validateInput();" 
															 method="searchList"/>
														<input type="button" class="buttonfinal" value="CLOSE" id="closeButton" name="button" onclick="window.close();"/>	 
													</div>
												</td>
											</tr>
											
											<div class="errorstyle" id="error_search"
												style="display: none;"></div>
											<s:hidden id="estimateId" name="estimateId" />
											
											<table width="100%" border="0" cellspacing="0"
												cellpadding="0">
												
												<tr><td colspan="4">&nbsp;</td></tr>
												<tr id="resultRow" ><td colspan="4">												
													<%@ include file='plannedEstimateReport-searchResults.jsp'%>
												</td>
												</tr>																							
											</table>
											
										</table>
									</td>
								</tr>
							</table>
						</div>
						
						
						<!-- end of rbroundbox2 -->
						<div class="rbbot2">
							<div></div>
						</div>
					</div>
					<!-- end of insidecontent -->
				</div>
				<!-- end of formmainbox -->
		</s:form>
	</body>
</html>