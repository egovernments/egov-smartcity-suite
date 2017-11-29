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
<script>

function setupSubTypes(elem){
    categoryId=elem.options[elem.selectedIndex].value;
    populatecategory({category:categoryId});
}


function submitworkProgressSearchForm() {
	if(dom.get('contractorNameSearch').value=="") {
		dom.get('contractorId').value=null;
	}
	hideResult();
    document.workProgressSearchForm.action='${pageContext.request.contextPath}/reports/workProgressRegister!searchDetails.action';
    document.workProgressSearchForm.submit();
    doLoadingMask();
}

function setupSubSchemes(elem){
	var id=elem.options[elem.selectedIndex].value;
	populatesubScheme({schemeId:id});
}

function hideResult()
{
	dom.get("resultRow").style.display='none';
}

var contractorNameSearchSelectionHandler = function(sType, arguments){ 
	var oData = arguments[2];
	dom.get('contractorId').value = oData[1];
};

var wardSearchSelectionHandler = function(sType, arguments) { 
    var oData = arguments[2];
    dom.get("wardSearch").value=oData[0];
    dom.get("wardId").value = oData[1];
}

var wardSelectionEnforceHandler = function(sType, arguments) {
	warn('improperWardSelection');
}

function clearHiddenWardId(obj)
{
	if(obj.value=="")
	{
		document.getElementById("wardId").value="";
	}	
}

function jurisdictionSearchParameters(){
	return "isBoundaryHistory=true";
}
</script>

<html>
<title><s:text name='page.title.workprogress.register'/></title>
<body  >

<script src="<egov:url path='resources/js/works.js?${app_release_no}'/>"></script>
   <s:if test="%{hasErrors()}">
        <div id="errorstyle" class="errorstyle" >
          <s:actionerror/>
          <s:fielderror/>
        </div>
   </s:if>
   <s:if test="%{hasActionMessages()}">
       <div class="messagestyle">
        	<s:actionmessage theme="simple"/>
       </div>
   </s:if>
<s:form theme="simple" name="workProgressSearchForm" action="workProgress!searchDetails.action">
  <s:hidden name="typeOfWork" id="typeOfWork" />
  <s:hidden name="subTypeOfWork" id="subTypeOfWork" />
  <s:hidden name="sourcePage" id="sourcePage" />
   <s:hidden name="woId" id="woId" />
   <s:hidden name="estId" id="estId" />
<div class="errorstyle" id="workProgressSearch_error"
				style="display: none;"></div>
<div class="formmainbox">
<div class="insidecontent">
  <div class="rbroundbox2">
	<div class="rbtop2"></div>
	<div class="rbcontent2"><div class="datewk">
	 <table width="100%" border="0" cellspacing="0" cellpadding="0">
			 <tr>
				<td colspan="4">&nbsp;</td>
			 </tr>
			 
			 <s:if test="%{sourcePage==null || sourcePage==''}"> 
			 
			 <tr>
			 <td colspan="4" class="headingwk" align="left">
				<div class="arrowiconwk">
				  <img src="/egworks/resources/erp2/images/arrow.gif" />
				</div>
				<div class="headplacer">
				  <s:text name='title.search.criteria' />
				</div>
			  </td>
			 </tr>
									<tr>
									<td class="whiteboxwk">
										<s:text name="workprogress.fromdate" />
									</td>
									<td class="whitebox2wk">
										<s:date name="fromDate" var="fromDateFormat"
											format="dd/MM/yyyy" />
										<s:textfield name="fromDate" id="fromDate"
											cssClass="selectwk" value="%{fromDateFormat}"
											onfocus="javascript:vDateType='3';"
											onkeyup="DateFormat(this,this.value,event,false,'3')" />
										<a href="javascript:show_calendar('forms[0].fromDate',null,null,'DD/MM/YYYY');"
											onmouseover="window.status='Date Picker';return true;"
											onmouseout="window.status='';return true;"> <img
												src="/egworks/resources/erp2/images/calendar.png"
												alt="Calendar" width="16" height="16" border="0"
												align="absmiddle" />
										</a>
										<span id="errorfromDate" style="display:none;color:red;font-weight:bold">&nbsp;x</span>

									</td>
									<td width="17%" class="whiteboxwk">
										<s:text name="workprogress.todate" />
									</td>
									<td width="17%" class="whitebox2wk">
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
										<span id="errortoDate" style="display:none;color:red;font-weight:bold">&nbsp;x</span>
									</td>
								</tr>
								<tr>
									<td class="greyboxwk">
										<s:text name="workprogress.work.type" />:
									</td>
									<td class="greybox2wk">
										<s:select headerKey="-1" headerValue="ALL"
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
										<s:text name="workprogress.work.subtype" />:
									</td>
									<td class="greybox2wk">
										<s:select headerKey="-1" headerValue="ALL" name="category"
											value="%{category}" id="category" cssClass="selectwk"
											list="dropdownData.categoryList" listKey="id"
											listValue="description"/>
									</td>
								</tr>
								<tr>
										<td width="11%" class="whiteboxwk">
											<s:text name="workprogress.search.wo.status" />:
										</td>
										<td width="21%" class="whitebox2wk">
											<s:select id="workOrderStatus" name="workOrderStatus" headerKey="-1"
												headerValue="ALL" cssClass="selectwk"
												list="dropdownData.workOrderStatuses" listKey="code"
												listValue="description" value="%{workOrderStatus}" />

										</td>
										<td width="11%" class="whiteboxwk">
											<s:text name="workprogress.search.milestone.status" />:
										</td>
										<td width="21%" class="whitebox2wk">
											<s:select id="milestoneStatus" name="milestoneStatus" headerKey="-1"
												headerValue="ALL" cssClass="selectwk"
												list="#{'Inprogress1to25':'Inprogress 1-25 %','Inprogress26to50':'Inprogress 26-50 %', 'Inprogress51to75':'Inprogress 51-75 %', 'Inprogress76to99':'Inprogress 76-99 %', 'Completed':'Completed'}"
												value="%{milestoneStatus}" />

										</td>


									</tr>
									<tr>
										<td width="15%" class="greyboxwk">
											<s:text name="workprogress.work.nature" />:
										</td>
										<td width="20%" class="greybox2wk">
											<s:select headerKey="-1" headerValue="ALL"
												name="expenditureType" id="type" cssClass="selectwk"
												list="dropdownData.typeList" listKey="id" listValue="name"
												value="%{expenditureType}" />
										</td>

										<td width="15%" class="greyboxwk">
											<s:text name="workprogress.fund" />:
										</td>
										<td width="20%" class="greybox2wk">
											<s:select headerKey="-1" headerValue="ALL"
												name="fund" id="fund" cssClass="selectwk"
												list="dropdownData.fundList" listKey="id" listValue="name" value="%{fund}" />
										</td>
									</tr>
									<tr>
										<td width="15%" class="whiteboxwk">
											<s:text name="workprogress.function" />:
										</td>
										<td width="20%" class="whitebox2wk">
											<s:select headerKey="-1" headerValue="ALL"
												name="function" id="function" cssClass="selectwk"
												list="dropdownData.functionList" listKey="id" listValue="name"
												value="%{function}" />
										</td>

										<td class="whiteboxwk"><s:text name='workprogress.contractorname'/> : </td>
						                <td class="whitebox2wk">
			        						<div class="yui-skin-sam">
			        							<div id="contractorNameSearch_autocomplete">
		                							<div>
			        									<s:textfield id="contractorNameSearch" name="contractorName" 
			        										value="%{contractorName}" cssClass="selectwk" />
			        									
			        								</div>
			        								<span id="contractorNameSearchResults"></span>
			        							</div>	
			        						</div>
			        						<egov:autocomplete name="contractorNameSearch" width="20" 
			        							field="contractorNameSearch" url="ajaxWorkProgress!searchAllContractorsForWorkOrder.action?" 
			        							queryQuestionMark="false" results="contractorNameSearchResults" 
			        							handler="contractorNameSearchSelectionHandler" queryLength="3"/>
			        						<s:hidden id="contractorId" name="contractorId" value="%{contractorId}"/>
					        			</td>
									</tr>
									<tr>
										<td width="15%" class="greyboxwk">
											<s:text name="workprogress.executing.department" />:
										</td>	
										<td width="20%" class="greybox2wk">
											<s:select headerKey="-1"
												headerValue="%{getText('estimate.default.select')}"
												name="execDept" id="executingDepartment"
												cssClass="selectwk"
												list="dropdownData.executingDepartmentList" listKey="id"
												listValue="deptName" value="%{execDept}" />
										</td>

										<td width="15%" class="greyboxwk">
											<s:text name="workprogress.preparedBy" />:
										</td>	
										<td width="20%" class="greybox2wk">
											<s:select headerKey="-1" headerValue="%{getText('estimate.default.select')}" 
												name="preparedBy" value="%{preparedBy}" 
												id="preparedBy" cssClass="selectwk" list="dropdownData.preparedByList" listKey="id" 
												listValue="employeeName"/>
										</td>
									</tr>

    											<tr>
        											<td class="whiteboxwk">
        												<s:text name="milestone.search.user1"/> :
        											</td>
        											<td class="whitebox2wk">
        												<s:select headerKey="-1" headerValue="%{getText('estimate.default.select')}" name="engineerIncharge" 
	         												id="engineerIncharge"  cssClass="selectwk" 
	         												list="dropdownData.assignedUserList1" listKey="id" listValue="employeeName" value="%{engineerIncharge}"/>
        											</td>
        											<td class="whiteboxwk">
        												<s:text name="milestone.search.user2"/> :
        											</td>
        											<td class="whitebox2wk">
        												<s:select headerKey="-1" headerValue="%{getText('estimate.default.select')}" name="engineerIncharge2" 
	         												id="engineerIncharge2"  cssClass="selectwk" 
	         												list="dropdownData.assignedUserList2" listKey="id" listValue="employeeName" value="%{engineerIncharge2}"/>
        											</td>
   												</tr>
   												
   									<tr>
						                <td class="greyboxwk">
						                	<s:text name='workprogress.search.scheme'/> : 
						                </td>
						                <td class="greybox2wk">
						                	<s:select headerKey="-1" headerValue="%{getText('estimate.default.select')}" 
						                		name="scheme" id="scheme" cssClass="selectwk" list="dropdownData.schemeList" 
						                		listKey="id" listValue="name" value="%{scheme}"  onChange="setupSubSchemes(this);"/>
											<egov:ajaxdropdown id="subSchemeDropdown" fields="['Text','Value']" dropdownId='subScheme' 
												url='reports/ajaxWorkProgress!loadSubSchemes.action' selectedValue="%{scheme.id}"/>
										</td>
						                <td class="greyboxwk">
						                	<s:text name='workprogress.search.subscheme'/> : 
						                </td>
						                <td class="greybox2wk">
						                	<s:select headerKey="-1" headerValue="%{getText('estimate.default.select')}" 
						                		name="subScheme" id="subScheme" cssClass="selectwk" list="dropdownData.subSchemeList" 
						                		listKey="id" listValue="name" value="%{subScheme}" />
						                </td>
									</tr>
									<tr>
										<td width="15%" class="whiteboxwk">
											<s:text name="workprogress.budget.head" />:
										</td>
										<td width="50%" class="whitebox2wk" colspan="3">
											<s:select headerKey="-1" headerValue="ALL"
												name="budgetHead" id="budgetHead" cssClass="selectwk"
												list="dropdownData.budgetGroupList" listKey="id" listValue="name" value="%{budgetHead}" />
										</td>
									</tr>
									<tr>
											<td class="greyboxwk"><s:text name="estimate.ward" />:</td>
							                <td class="greybox2wk">
							                <div class="yui-skin-sam">
							                <div id="wardSearch_autocomplete">
							                <div><s:textfield id="wardSearch" type="text" name="wardName" value="%{wardName}" onBlur="clearHiddenWardId(this)" class="selectwk"/><s:hidden id="wardId" name="wardId" value="%{wardId}"/></div>
							                <span id="wardSearchResults"></span>
							                </div>
							                </div>
							                <egov:autocomplete name="wardSearch" width="20" field="wardSearch" url="../estimate/wardSearch!searchAjax.action?" queryQuestionMark="false" results="wardSearchResults" handler="wardSearchSelectionHandler" forceSelectionHandler="wardSelectionEnforceHandler" paramsFunction="jurisdictionSearchParameters" queryLength="3"/>
							                <span class='warning' id="improperWardSelectionWarning"></span>
							                </td> 
							                <td class="greyboxwk" colspan="2"></td>
							           
									</tr>
   								
               <tr>
                	<td  colspan="4" class="shadowwk"> </td>               
               </tr>
               <tr><td>&nbsp;</td></tr>			
               <tr>
                 <td colspan="4"> 
                   <div class="buttonholderwk">
		             <p>
			           <input type="button" class="buttonadd" value="SEARCH" id="searchButton" name="button" onclick="submitworkProgressSearchForm()" />&nbsp;
			           <input type="button" class="buttonfinal" value="RESET" id="resetbutton" name="clear" onclick="this.form.reset();">&nbsp;
		               <input type="button" class="buttonfinal" value="CLOSE" id="closeButton" name="closeButton" onclick="window.close();" />
	                 </p>
		          </div>
                </td>
              </tr>
              
              </s:if>
              
     		
			<tr><td colspan="4">&nbsp;</td></tr>
     </table>               
     </div>
     <div id="resultRow">
        <%@ include file='workProgressRegister-searchResults.jsp'%>
     </div> 	
	 <div class="rbbot2"><div></div></div>
</div>
</div>
</div>
</div>
</s:form>
</body>

</html>
