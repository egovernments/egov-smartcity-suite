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
	<title><s:text name="revisionEstimate.search.title" /></title>
</head>
<script src="<egov:url path='resources/js/works.js?${app_release_no}'/>"></script>
<script>

function validateSearch() {	
	var bool=false;
	if($F('natureOfWork') == -1  && $F('reStatus') == -1  &&
	$F('estimateNumberSearch').blank() && $F('execDept')==-1 && 
	$F('fromDate').blank() && $F('toDate').blank() &&
	$F('parentCategory')==-1 && $F('category')==-1  && $F('workOrderNumberSearch').blank()) {
		bool=true;
		dom.get("searchRevisionEstimate_error").innerHTML='<s:text name="search.criteria.atleastone.mandatory" />'; 
		dom.get("searchRevisionEstimate_error").style.display='block';
	} 
		if(!bool){ 
		  	return true;
		}else{return false;}	 
}

function enableSelect(){
	document.getElementById('reStatus').disabled=false;
	return true;	
}

function disableSelect(){
	document.getElementById('reStatus').disabled=true
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


</script>
<body class="yui-skin-sam">
	<div class="errorstyle" id="searchRevisionEstimate_error"
			style="display: none;"></div>
	<s:if test="%{hasErrors()}">
		<div class="errorstyle">
			<s:actionerror />
			<s:fielderror />
		</div>
	</s:if>

	<s:if test="%{hasActionMessages()}">
		<div class="messagestyle">
			<s:actionmessage theme="simple" />
		</div>
	</s:if>

	<s:form action="searchRevisionEstimate" theme="simple" name="searchRevisionEstimateForm" onsubmit="return validateSearch();enableSelect();">
	<div class="formmainbox">
	<div class="insidecontent">
	<div id="printContent" class="rbroundbox2">
	<div class="rbtop2"><div></div></div>
	<div class="rbcontent2">
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<s:push value="model">
		<s:hidden id="source" name="source" value="%{source}" />
		<s:hidden id="revWOId" name="revWOId" /> 
		<s:hidden id="reWOEstimateId" name="reWOEstimateId" /> 
			<table id="formTable" width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr><td>&nbsp;</td></tr>
				<tr>
					<td>
						<table  width="100%" border="0"
							cellspacing="0" cellpadding="0">
							<tr>
								<td colspan="4" class="headingwk">
									<div class="arrowiconwk">
										<img
											src="/egworks/resources/erp2/images/arrow.gif" />
									</div>
									<div class="headplacer" align="left">
										 <s:text name="revisionEstimate.search" /> 
									</div>
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<table width="100%" border="0" cellspacing="0" cellpadding="0" >
					<tr>				
						<td class="greyboxwk"  width="25%" ><s:text name="revisionEstimate.search.fromdate" />:</td>
						<td class="greybox2wk" width="25%" > <s:date name="fromDate" var="fromDateFormat" format="dd/MM/yyyy" />
								<s:textfield name="fromDate" id="fromDate" cssClass="selectwk" value="%{fromDateFormat}" onfocus="javascript:vDateType='3';" onkeyup="DateFormat(this,this.value,event,false,'3')" />
								 <a href="javascript:show_calendar('forms[0].fromDate',null,null,'DD/MM/YYYY');" onmouseover="window.status='Date Picker';return true;"
									onmouseout="window.status='';return true;"> 
									<img src="/egworks/resources/erp2/images/calendar.png" alt="Calendar" width="16" height="16" border="0" align="absmiddle" />
								</a>
						</td>
						<td class="greyboxwk" width="25%" ><s:text name="revisionEstimate.search.todate" />:</td>	
						<td class="greybox2wk" width="25%">
						<s:date name="toDate" var="toDateFormat"format="dd/MM/yyyy" />
						<s:textfield name="toDate" id="toDate" value="%{toDateFormat}" cssClass="selectwk" onfocus="javascript:vDateType='3';"onkeyup="DateFormat(this,this.value,event,false,'3')" />
							<a href="javascript:show_calendar('forms[0].toDate',null,null,'DD/MM/YYYY');" onmouseover="window.status='Date Picker';return true;"
								onmouseout="window.status='';return true;"> <img src="/egworks/resources/erp2/images/calendar.png" alt="Calendar" width="16" height="16" border="0" align="absmiddle" />
							</a>
						</td>
					</tr>
					  <tr>
						 <td class="whiteboxwk"><s:text name="revisionEstimate.search.revisionEstimateStatus" /></td>					 
						 <td class="whitebox2wk"><s:select headerKey="-1" headerValue="%{getText('list.default.select')}"
								name="reStatus" id="reStatus" cssClass="selectwk" list="dropdownData.statusList" listKey="id"
								listValue='description' value="%{reStatus}" />
						</td> 
						<s:if test="%{source.equals('cancelRE') }">
							<script>disableSelect();
								window.history.forward(1);
							</script> 
						</s:if>
						<td class="whiteboxwk"><s:text name="estimate.executing.department" /></td>
						<td class="whitebox2wk"><s:select headerKey="-1"headerValue="%{getText('list.default.select')}"
							name="execDept" id="execDept" cssClass="selectwk" list="dropdownData.executingDepartmentList" listKey="id"
							listValue='deptName' value="%{execDept}" />
						</td> 
					</tr>
					<tr>
						<td class="greyboxwk"> <s:text name="revisionEstimate.search.estimateNo" />:</td>
						<td class="greybox2wk" >
       						<div class="yui-skin-sam">
       							<div id="estimateNumberSearch_autocomplete">
              							<div>
       									<s:textfield id="estimateNumberSearch" name="estimateNumber" value="%{estimateNumber}" cssClass="selectwk" />
       								</div>
       								<span id="estimateNumberSearchResults"></span>
       							</div>	
       						</div>
       						<egov:autocomplete name="estimateNumberSearch" width="20" field="estimateNumberSearch" url="ajaxRevisionEstimate!searchRevisionEstimateNumber.action?" queryQuestionMark="false" results="estimateNumberSearchResults" handler="estimateNumberSearchSelectionHandler" queryLength="3"/>
         				</td>
						</td>
						<td class="greyboxwk"><s:text name="estimate.work.nature" />:</td>
						<td class="greybox2wk">
							<s:select headerKey="-1" headerValue="ALL" name="natureOfWork" id="natureOfWork" cssClass="selectwk" list="dropdownData.typeList" listKey="id" listValue="name"
											value="%{natureOfWork.id}" />
						</td> 
					</tr>				
													
					<tr>
						<td class="whiteboxwk"><s:text name="estimate.work.type" />	:	</td>
						<td class="whitebox2wk">
							<s:select headerKey="-1" headerValue="ALL" name="parentCategory" id="parentCategory" cssClass="selectwk" list="dropdownData.parentCategoryList"
							listKey="id" listValue="description" value="%{parentCategory.id}" onChange="setupSubTypes(this);" />
							<egov:ajaxdropdown id="categoryDropdown" fields="['Text','Value']" dropdownId='category' url='estimate/ajaxEstimate!subcategories.action' selectedValue="%{category.id}" />
						</td>
						<td class="whiteboxwk"><s:text name="estimate.work.subtype" />:</td>
						<td class="whitebox2wk"><s:select headerKey="-1" headerValue="ALL" name="category" value="%{category.id}" id="category" cssClass="selectwk"
							list="dropdownData.categoryList" listKey="id" listValue="description" />
						</td>
					</tr>
					<s:if test="%{source.equals('cancelRE') }">
					<tr>
						<td class="greyboxwk"> <s:text name="workorder.search.workordernumber" />:</td>
						<td class="greybox2wk" colspan="3">
       						<div class="yui-skin-sam">
       							<div id="workOrderNumberSearch_autocomplete">
              							<div> 
       									<s:textfield id="workOrderNumberSearch" name="workOrderNumber" value="%{workOrderNumber}" cssClass="selectwk" />
       								</div>
       								<span id="workOrderNumberSearchResults"></span>
       							</div>		
       						</div>
       						<egov:autocomplete name="workOrderNumberSearch" width="20" field="workOrderNumberSearch" url="ajaxRevisionEstimate!searchApprovedWONumberForRE.action?" queryQuestionMark="false" results="workOrderNumberSearchResults" handler="workOrderNumberSearchSelectionHandler" queryLength="3" />
         				</td>
					</tr>
					</s:if>
					<s:else>	
					<tr>
						<td class="greyboxwk"> <s:text name="workorder.search.workordernumber" />:</td>
						<td class="greybox2wk" colspan="3">
       						<div class="yui-skin-sam">
       							<div id="workOrderNumberSearch_autocomplete">
              							<div> 
       									<s:textfield id="workOrderNumberSearch" name="workOrderNumber" value="%{workOrderNumber}" cssClass="selectwk" />
       								</div>
       								<span id="workOrderNumberSearchResults"></span>
       							</div>		
       						</div>
       						<egov:autocomplete name="workOrderNumberSearch" width="20" field="workOrderNumberSearch" url="ajaxRevisionEstimate!searchREWorkOrderNumber.action?" queryQuestionMark="false" results="workOrderNumberSearchResults" handler="workOrderNumberSearchSelectionHandler" queryLength="3" />
         				</td>
					</tr>
					</s:else>
				</table>
			</table>
					
			<table width="100%">							
				<tr>
					<td colspan="4">
						<div class="buttonholderwk">
						<s:submit type="submit" id="search" value="SEARCH" cssClass="buttonadd" method="list"  
						onClick="return validateSearch();enableSelect();"/>
							
							<input type="button" class="buttonfinal" value="CLOSE"
								id="closeButton" name="button" onclick="window.close();" />
						</div>
					</td>
				</tr>							
			</table>
			<jsp:include page="searchRevisionEstimate-list.jsp" /> 
		</s:push>
	</table>
	</div>
	</div>
	</div>
	</div>
	</s:form>
</body>
</html>
