<%@ taglib prefix="egov" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator"%>
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %> 
<script src="<egov:url path='js/works.js'/>"></script> 

<table  width="100%" border="0" cellspacing="0" cellpadding="0">
 		<tr>
            <td colspan="6" class="headingwk"><div class="arrowiconwk"><image src="<egov:url path='/image/arrow.gif'/>" /></div><div class="headplacer"><s:text name='rateContract.template.header'/></div></td>
        </tr>
        
        <tr><td>&nbsp;</td></tr>
 		
 		<tr>
 			<td  class="whiteboxwk"><span class="mandatory">*</span><s:text name="Date" />:</td>
			<td class="whitebox2wk"><s:date name="indentDate" var="indentDateFormat" format="dd/MM/yyyy"/><s:textfield name="indentDate" value="%{indentDateFormat}" id="indentDate" cssClass="selectwk" onfocus="javascript:vDateType='3';" onkeyup="DateFormat(this,this.value,event,false,'3');" />
             <s:if test="%{id==null || model.egwStatus==null || model.egwStatus.code=='NEW' || model.egwStatus.code=='REJECTED' }">
             <a href="javascript:show_calendar('forms[0].indentDate',null,null,'DD/MM/YYYY');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"><img src="${pageContext.request.contextPath}/image/calendar.png" alt="Calendar" width="16" height="16" border="0" align="absmiddle" /></a>
             </s:if>
                    <span id='errorindentDate' style="display:none;color:red;font-weight:bold">&nbsp;x</span>
            </td>
            <td class="whiteboxwk"><span class="mandatory">*</span><s:text name="rateContract.type" />:</td>
            <td class="whitebox2wk"><s:select headerKey="-1" headerValue="%{getText('rateContract.default.select')}" name="indentType" id="indentType" value="%{indentType}" cssClass="selectwk" list="#{'Amount':'Amount', 'Item':'Item'}" onchange="getInit();" /></td>
            <td class="whiteboxwk"><span class="mandatory">*</span><s:text name="rateContract.department" />:</td>
			<td class="whitebox2wk"><s:select headerKey="-1" headerValue="%{getText('indent.default.select')}" name="department" id="department" cssClass="selectwk" list="dropdownData.departmentList" listKey="id" listValue="deptName" value="%{department.id}" onchange="setupPreparedByList(this);clearDesignation(this);" />
            <div id="ajaxCall" style="display: none;">
		      <egov:ajaxdropdown id="preparedBy" fields="['Text','Value','Designation']" dropdownId='preparedBy' 
		        	  optionAttributes='Designation' url='estimate/ajaxEstimate!usersInExecutingDepartment.action'/>
		    </div>
            </td>
        </tr>
        
        <tr>
        	<td class="greyboxwk"><span class="mandatory">*</span><s:text name="rateContract.contractor.class" />:</td>
            <td class="greybox2wk"><s:select headerKey="-1" headerValue="%{getText('rateContract.default.select')}" name="contractorGrade" id="contractorGrade" value="%{contractorGrade}" cssClass="selectwk" list="dropdownData.contractorGradeList" listKey="id" listValue="grade" value="%{contractorGrade.id}" /></td>
            <td class="greyboxwk"><span class="mandatory">*</span><s:text name="rateContract.zone" />:</td>
            <td class="greybox2wk" colspan="3"><s:select headerKey="-1" headerValue="%{getText('rateContract.default.select')}" name="boundary" value="%{boundary.value}" id="boundary" cssClass="selectwk" list="dropdownData.boundaryList" listKey="id" listValue="name" value="%{boundary.id}" />
            </td>
            </td>
            </td>
        </tr>
        
        
        <tr>
        	<td  class="whiteboxwk"><span class="mandatory">*</span><s:text name="rateContract.start.date" />:</td>
			<td class="whitebox2wk"><s:date name="validity.startDate" var="startDate" format="dd/MM/yyyy" />
									<s:textfield name="validity.startDate" id="startDate"
												 cssClass="selectwk" value="%{startDate}"
												 onfocus="javascript:vDateType='3';"
												 onkeyup="DateFormat(this,this.value,event,false,'3')" />
										<s:if test="%{id==null || model.egwStatus==null || model.egwStatus.code=='NEW' || model.egwStatus.code=='REJECTED' }">		 
										<a href="javascript:show_calendar('forms[0].startDate',null,null,'DD/MM/YYYY');"
											onmouseover="window.status='Date Picker';return true;"
											onmouseout="window.status='';return true;"> <img
											src="${pageContext.request.contextPath}/image/calendar.png"
											alt="Calendar" width="16" height="16" border="0" align="absmiddle" />
										</a></s:if>
									<span id='errorstartDate' style="display:none;color:red;font-weight:bold">&nbsp;x</span>
			</td>
			<td class="whiteboxwk"><span class="mandatory">*</span><s:text name="rateContract.end.date" />:</td>
			<td class="whitebox2wk"><s:date name="validity.endDate" var="endDate" format="dd/MM/yyyy" />
									<s:textfield name="validity.endDate" id="endDate"
												value="%{endDate}" cssClass="selectwk"
												onfocus="javascript:vDateType='3';"
												onkeyup="DateFormat(this,this.value,event,false,'3')" />
												<s:if test="%{id==null || model.egwStatus==null || model.egwStatus.code=='NEW' || model.egwStatus.code=='REJECTED' }">
										<a	href="javascript:show_calendar('forms[0].endDate',null,null,'DD/MM/YYYY');"
											onmouseover="window.status='Date Picker';return true;"
											onmouseout="window.status='';return true;"> <img
											src="${pageContext.request.contextPath}/image/calendar.png"
											alt="Calendar" width="16" height="16" border="0" align="absmiddle" />
													</a></s:if>
										<span id='errorendDate' style="display:none;color:red;font-weight:bold">&nbsp;x</span>
			</td>
        </tr>
        		    <tr>
				 <td width="11%" class="greyboxwk"><span class="mandatory">*</span><s:text name="rateContract.emp"/>:</td>
		         <td width="21%" class="greybox2wk">
		         <s:if test="%{(sourcepage=='inbox' && model.currentState.value!='NEW' && model.currentState.value!='REJECTED') || sourcepage=='search' }" >
		           <s:textfield id="preparedByTF" value="%{preparedBy.employeeName}" type="text" readonly="true" disabled="disabled" cssClass="selectboldwk" />
		           <s:hidden name="preparedBy" id="preparedBy" value="%{preparedBy.id}"/>     
            	</s:if>
            	<s:else>
			         <s:select headerKey="-1" headerValue="%{getText('estimate.default.select')}" name="preparedBy" 
			         id="preparedBy" value="%{preparedBy}" cssClass="selectwk" 
			         list="dropdownData.preparedByList" listKey="id" listValue="employeeName" value="%{preparedBy.id}" onchange='showDesignation(this);'/>
	         	</s:else>
		         </td>
		         <td width="15%" class="greyboxwk"><s:text name="rateContract.desg"/>:</td>
		         <td width="53%" class="greybox2wk"><s:textfield name="designation" type="text"  readonly="true"
		          cssClass="selectboldwk" id="designation" size="45" tabIndex="-1" value="%{designation}"/></td>
		        <td  class="greyboxwk">&nbsp;</td> 
		        <td  class="greybox2wk">&nbsp;</td> 
			</tr>
        
        <tr>
            <td colspan="6">
              	<div id="amount" > 
             		<%@ include file='amountRateContract.jsp'%>
      		  	</div>
      		</td>
        </tr>
 </table>
<script type="text/javascript">
  <s:if test="%{mode=='view'}">
	for(i=0;i<document.rateContractForm.elements.length;i++){
		document.rateContractForm.elements[i].disabled=true;
		document.rateContractForm.elements[i].readonly=true;
	} 
  </s:if>
  
 function setupPreparedByList(elem){
   deptId=elem.options[elem.selectedIndex].value;
   populatepreparedBy({executingDepartment:deptId});
   }
 function clearDesignation(elem) {
   dom.get('designation').value='';
 }
designationLoadHandler = function(req,res){ 
  results=res.results;
  dom.get('designation').value=results[0].Designation;
}

designationLoadFailureHandler= function(){
    dom.get('rateContract_error').style.display='';
	document.getElementById("rateContract_error").innerHTML='<s:text name="unable.des"/>';
}

function showDesignation(elem){
	var empId = elem.options[elem.selectedIndex].value;
    makeJSONCall(["Designation"],'${pageContext.request.contextPath}/estimate/ajaxEstimate!designationForUser.action',{empID:empId},designationLoadHandler,designationLoadFailureHandler) ;
}

</script>  