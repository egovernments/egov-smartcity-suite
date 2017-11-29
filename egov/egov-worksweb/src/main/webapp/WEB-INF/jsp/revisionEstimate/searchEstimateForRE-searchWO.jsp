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

<%@ include file="/includes/taglibs.jsp" %>
<html>
<head>
	<title><s:text name='page.title.search.workorder' /></title>
	</head>
<script src="<egov:url path='resources/js/works.js?${app_release_no}'/>"></script>

<script type="text/javascript">
function validateAndSubmit(){
	   if($F('contractorId') == -1 && $F('deptId') == -1 && $F('typeId') == -1 && $F('parentCategory') == -1 
			   				&& $F('category') == -1
							&& $F('workOrderNumberSearch').blank() && $F('estimateNumberSearch').blank()
							&& $F('fromDate').blank()
							&& $F('toDate').blank()){
							var bool=true;
							if(document.getElementById('estimateNumberSearch')){
								if(document.getElementById('estimateNumberSearch').value=="")
									bool=false;
								}
		if(!bool){
		$('workOrder_error').show();					
	  	$('mandatory_error').show();
	  	$('mandatory_length_error').hide();
	  	return false;
	  	}
	  }
	  
	 
	   if(!$F('workOrderNumberSearch').blank()  && $F('workOrderNumberSearch').length < 4){
			  	$('workOrder_error').show();
			  	$('mandatory_length_error').show();
			  	$('mandatory_error').hide();
			     return false;
		 }
		    $('workOrder_error').hide();
		    enableDept();
		}
function gotoCreateRE(){
	var table=document.getElementById('currentRow');
	var lastRow = table.rows.length-1;
	var wo="";
	var est="";
	
	if(lastRow==1){
		if(document.getElementById("radio").checked)
			wo=document.getElementById("woId").value;	
		    est=document.getElementById("estId").value;							
	}
	else{
		for(i=0;i<lastRow;i++){			
			if(document.forms[0].radio[i].checked){
				wo=document.forms[0].woId[i].value;
				est=document.forms[0].estId[i].value;						
			}
		}
	}

	if(wo!='' && est!=''){
		validateRCEstimate(est,wo);
	}
	else{
		dom.get("workOrder_error").style.display='';
		document.getElementById("workOrder_error").innerHTML='<s:text name="revisionEstimate.create.select.estimate.validate" />';
		window.scroll(0, 0);
		return false;
	  }
	  dom.get("workOrder_error").style.display='none';
	  document.getElementById("workOrder_error").innerHTML=''; 

	  
	}
function validateRCEstimate(estimateId,woId){
		makeJSONCall(["Value","estimateNum","estimateId","woId"],'${pageContext.request.contextPath}/estimate/ajaxEstimate!checkIfRCEstimate.action',{estimateId:estimateId,woId:woId},validateRCEstimateSuccess,validateRCEstimateFailure) ;
	}

validateRCEstimateSuccess = function(req,res){
	results=res.results;
	var checkResult='';
	var estimateNum='';
	var woId='';
	var estId='';
	
	if(results != '') {
		checkResult =   results[0].Value;
		estimateNum =   results[0].estimateNum;
		woId = results[0].woId;
		estId = results[0].estimateId;
	}
	

	if(checkResult != '' && checkResult=='no'){
		window.open('${pageContext.request.contextPath}/revisionEstimate/revisionEstimate!newform.action?sourcepage=createRE&originalWOId='+woId+'&originalEstimateId='+estId+'','_self');
	}	
	else {
		dom.get("workOrder_error").innerHTML='<s:text name="re.rcEstimate.check" />';
	    dom.get("workOrder_error").style.display='';
	    return false;
	}
	if(dom.get("workOrder_error").innerHTML=='<s:text name="re.rcEstimate.check" />')
	{
		dom.get("workOrder_error").innerHTML='';
	    dom.get("workOrder_error").style.display='none';
	}
		
}

validateRCEstimateFailure= function(){
    dom.get("workOrder_error").style.display='';
	document.getElementById("workOrder_error").innerHTML='<s:text name="wp.rcEstimate.check.failure" />';
}

function setupSubTypes(elem){
	categoryId=elem.options[elem.selectedIndex].value;
	populatecategory({category:categoryId});
}
var estimateNumberSearchSelectionHandler = function(sType, arguments){ 
	var oData = arguments[2];
};

var workOrderNumberSearchSelectionHandler = function(sType, arguments){ 
	var oData = arguments[2];
};

function disableDept() {
	dom.get('deptId').disabled=true;
}

function enableDept() {
	dom.get('deptId').disabled=false;
}

</script>
	<body>
		<div class="errorstyle" id="searchEstimate_error"
			style="display: none;"></div>
		<s:if test="%{hasErrors()}">
			<div class="errorstyle">
				<s:actionerror />
				<s:fielderror />
			</div>
		</s:if>
		<s:form name="searchWorkOrderForReForm" 
			id="searchWorkOrderForReForm" theme="simple" onsubmit="return validateAndSubmit(); ">
			
			<div class="errorstyle" id="workOrder_error" style="display: none;">
			<span id="mandatory_error" style="display: none;"><s:text name="workorder.serach.mandatory.error" /></span>
			<span id="mandatory_length_error" style="display: none;"><s:text name="workorder.serach.mandatory.length.error" /></span>
		    </div>
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
								<s:hidden id="workOrderId" name="workOrderId" />
								<s:hidden id="woNumber" name="woNumber" />	
								<tr>
									<td>
										<table width="100%" border="0" cellspacing="0" cellpadding="0">
											<tr>
												<td width="100%" colspan="4" class="headingwk">
													<div class="arrowiconwk">
														<img
															src="/egworks/resources/erp2/images/arrow.gif" />
													</div>
													<div class="headplacer">
														<s:text name="page.subheader.search.workorder" />
													</div>
												</td>
											</tr>
											<tr>
											<td class="whiteboxwk" width="25%"><s:text
													name="estimate.executing.department" /></td>
											<td class="whitebox2wk" width="25%">
											<s:select name="deptId" id="deptId" cssClass="selectwk"
													list="dropdownData.executingDepartmentList" listKey="id"
													listValue='deptName' value="%{execDept}" /></td>
											 <s:if test="%{dropdownData.executingDepartmentList.size==1}" >
								                <script>
								                	disableDept(); 
								                </script>
					                 		 </s:if>
											<td  width="25%" class="whiteboxwk"><s:text name="estimate.number" />
												:</td>
											<td class="whitebox2wk" >
				        						<div class="yui-skin-sam">
				        							<div id="estimateNumberSearch_autocomplete">
			                							<div>
				        									<s:textfield id="estimateNumberSearch" name="estimateNumber" value="%{estimateNumber}" cssClass="selectwk" />
				        								</div>
				        								<span id="estimateNumberSearchResults"></span>
				        							</div>	
				        						</div>
				        						<egov:autocomplete name="estimateNumberSearch" width="20" field="estimateNumberSearch" url="ajaxRevisionEstimate!searchEstimateNumber.action?" queryQuestionMark="false" results="estimateNumberSearchResults" handler="estimateNumberSearchSelectionHandler" queryLength="3"/>
					         				</td>
											<!-- <td  width="25%" class="whitebox2wk"><s:textfield
													name="estimateNumber" id="estimateNumber" value="%{estimateNumber}"
													cssClass="selectboldwk" /></td> -->
										</tr>
										<tr>
											<td class="greyboxwk"><s:text
													name="estimate.work.nature" />:</td>
											<td class="greybox2wk"><s:select
													headerKey="-1" headerValue="ALL" name="typeId" id="typeId"
													cssClass="selectwk" list="dropdownData.typeList"
													listKey="id" listValue="name" /></td>

											<td class="greyboxwk"><s:text
													name="workOrder.search.label.workOrderNo" />:</td>
											<td class="greybox2wk" >
				        						<div class="yui-skin-sam">
				        							<div id="workOrderNumberSearch_autocomplete">
			                							<div> 
				        									<s:textfield id="workOrderNumberSearch" name="workOrderNumber" value="%{workOrderNumber}" cssClass="selectwk" />
				        								</div>
				        								<span id="workOrderNumberSearchResults"></span>
				        							</div>		
				        						</div>
				        						<egov:autocomplete name="workOrderNumberSearch" width="20" field="workOrderNumberSearch" url="ajaxRevisionEstimate!searchWorkOrderNumber.action?" queryQuestionMark="false" results="workOrderNumberSearchResults" handler="workOrderNumberSearchSelectionHandler" queryLength="3" />
					         				</td>
										<!-- 	<td class="greybox2wk"><s:textfield
													name="workOrderNumber" value="%{workOrderNumber}"
													id="workOrderNumber" cssClass="selectwk" /></td>
										</tr>-->
										<tr>
											<td class="whiteboxwk"><s:text name="estimate.work.type" />
												:</td>
											<td class="whitebox2wk"><s:select headerKey="-1"
													headerValue="ALL" name="parentCategory" id="parentCategory"
													cssClass="selectwk" list="dropdownData.parentCategoryList"
													listKey="id" listValue="description"
													value="%{parentCategory.id}"
													onChange="setupSubTypes(this);" />
													<egov:ajaxdropdown id="categoryDropdown" 
													fields="['Text','Value']" dropdownId='category'
													url='estimate/ajaxEstimate!subcategories.action' 
													selectedValue="%{category.id}" /></td>

											<td class="whiteboxwk"><s:text
													name="estimate.work.subtype" /> :</td>
											<td class="whitebox2wk"><s:select headerKey="-1"
													headerValue="ALL" name="category" value="%{category.id}"
													id="category" cssClass="selectwk"
													list="dropdownData.categoryList" listKey="id"
													listValue="description" /></td>
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
															src="/egworks/resources/erp2/images/calendar.png"
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
															src="/egworks/resources/erp2/images/calendar.png"
															alt="Calendar" width="16" height="16" border="0"
															align="absmiddle" />
													</a>
												</td>
											</tr>
										<tr>
											<td class="whiteboxwk"><s:text
													name="workorder.search.contractor" />:</td>
											<td colspan="3" class="whitebox2wk">
												<s:select id="contractorId" name="contractorId"
													cssClass="selectwk"
													list="%{contractorForApprovedWorkOrder}"  headerKey="-1"
													headerValue="--- Select ---" value="%{contractorId}" />
											</td>
										</tr>
										<tr>
												<td colspan="4" class="shadowwk"></td>
											</tr>
											
											<tr>
												<td colspan="4">
													<div  class="buttonholderwk" align="center">
														<s:submit  cssClass="buttonadd" value="SEARCH"
															id="saveButton" name="button"  onclick="return validateAndSubmit();" method="search" />
														<input type="button" class="buttonfinal" value="CLOSE"
															id="closeButton" name="button" onclick="window.close();" />
											</td>
											</tr>
											<%@ include file='woListSearchForRE-list.jsp'%>
										</table>
									<P align="center">
										<input type="button" class="buttonadd"
											value="Create Revision Estimate" id="addButton"
											name="createRevisionEstimateButton" onclick="gotoCreateRE();"
											align="center" />
									</P>
								</td>
								</tr>
								
							</table>
							
						</div>
						<!-- end of rbroundbox2 -->
					</div>
					<!-- end of insidecontent -->
				</div>
				<!-- end of formmainbox -->
		</s:form>
	</body>
</html>

