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

<style type="text/css">
#yui-dt0-bodytable,#yui-dt1-bodytable,#yui-dt2-bodytable {
	Width: 100%;
}
</style>
<script>
function checkMBLegacy(obj){ 
	   if(obj.checked){
		 	document.getElementById('isLegacyMB').value=true;
		 	document.getElementById('isLegacyMB').checked=true;
		}
		else if(!obj.checked){
			document.getElementById('isLegacyMB').value=false;
			document.getElementById('isLegacyMB').checked=false;
		}
}
designationLoadHandler = function(req,res){
  results=res.results;
  dom.get("designation").value=results[0].Designation;
}

designationLoadFailureHandler= function(){
    dom.get("mb_error").style.display='';
	document.getElementById("mb_error").innerHTML='Unable to load designation';
}

function showDesignation(elem){
	var empId = elem.options[elem.selectedIndex].value;
    makeJSONCall(["Designation"],'${pageContext.request.contextPath}/measurementbook/ajaxMeasurementBook!designationForUser.action',{empID:empId},designationLoadHandler,designationLoadFailureHandler) ;
}

function validateHeaderBeforeSubmit(measurementBookForm) {
//validating for cancel mb with respect to  bill status	
		if(document.getElementById("actionName").value=='cancel'){
 			<s:if test="%{model.getEgBillregister()!=null && model.getEgBillregister().getStatus().getCode() != 'CANCELLED'}">
				dom.get("mb_error").style.display='';     
    			document.getElementById("mb_error").innerHTML='<s:text name="measurementbook.cancel.failure" />';
    			window.scroll(0,0);
    			return false;
			</s:if>
		} else {
			if(!validateEntryDate())
				return false;
		} 
	//workOrderNumber
	//mbDate mbRefNo  fromPageNo toPageNo  mbPreparedBy mbAbstract
	
	var workOrderNumber = measurementBookForm.workOrderNumber.value;
    if (workOrderNumber.length == 0 || measurementBookForm.workOrderNumber.value == '' || measurementBookForm.workOrderNumber.value == -1) {    	
    	dom.get("mb_error").style.display='';     
    	document.getElementById("mb_error").innerHTML='<s:text name="mbheader.workorder.null" />';
    	window.scroll(0,0);
    	return false;
    } 

    
    if (measurementBookForm.mbDate.value == '') {    	
    	dom.get("mb_error").style.display='';     
    	document.getElementById("mb_error").innerHTML='Please enter the MB date';
    	window.scroll(0,0);
    	return false;
    } 
    
    var mbRefNo = measurementBookForm.mbRefNo.value;
    if (measurementBookForm.mbRefNo.value == '') {    	
    	dom.get("mb_error").style.display='';     
    	document.getElementById("mb_error").innerHTML='<s:text name="mbheader.mbrefno.null" />';
    	window.scroll(0,0);
    	return false;
    }
     
    if (measurementBookForm.mbRefNo.value > 50) {    	
    	dom.get("mb_error").style.display='';     
    	document.getElementById("mb_error").innerHTML='<s:text name="mbheader.mbrefno.length" />';
    	window.scroll(0,0);
    	return false;
    }     

    var fromPageNo = measurementBookForm.fromPageNo.value;
    if ( measurementBookForm.fromPageNo.value == '') {    	
    	dom.get("mb_error").style.display='';     
    	document.getElementById("mb_error").innerHTML='<s:text name="mbheader.fromPageNo.null" />';
    	window.scroll(0,0);
    	return false;
    }
    
    if (measurementBookForm.fromPageNo.value == -1) {    	
    	dom.get("mb_error").style.display='';     
    	document.getElementById("mb_error").innerHTML='<s:text name="fromPageNo.non.negative" />';
    	window.scroll(0,0);
    	return false;
    }

    if(isNaN(fromPageNo)){
    	dom.get("mb_error").style.display='';     
    	document.getElementById("mb_error").innerHTML='<s:text name="invalid.fieldvalue.fromPageNo" />';
    	window.scroll(0,0);
    	return false;
    }
    
    var toPageNo = measurementBookForm.toPageNo.value;
    if (measurementBookForm.toPageNo.value == -1) {    	
    	dom.get("mb_error").style.display='';     
    	document.getElementById("mb_error").innerHTML='<s:text name="mbheader.toPageNo.non.negative" />';
    	window.scroll(0,0);
    	return false;
    }

    if(isNaN(toPageNo)){
    	dom.get("mb_error").style.display='';     
    	document.getElementById("mb_error").innerHTML='<s:text name="invalid.fieldvalue.toPageNo" />';
    	window.scroll(0,0);
    	return false;
    }
    
    var mbAbstract = measurementBookForm.mbAbstract.value;
    if (measurementBookForm.mbAbstract.value  == '') {    	
    	dom.get("mb_error").style.display='';     
    	document.getElementById("mb_error").innerHTML='<s:text name="mbheader.mbabstract.null" />';
    	window.scroll(0,0);
    	return false;
    }	
    return true; 
}

function populateWorkName(obj){
  if(obj.value=="-1"){
  		dom.get("workName").value='';
  		dom.get("projectCode").value='';  		
  	}
  	else{
  		dom.get("mb_error").style.display='none'; 
    	document.getElementById("mb_error").innerHTML='';
    	
    }
    var estVal=dom.get("workName").value;
    var estProjCode=dom.get("projectCode").value=''; 
    <s:iterator value="dropdownData.workOrderEstimateList" status="row_status">	
		if('<s:property value="%{estimate.id}"/>'==obj.value){
			dom.get("workName").value='<s:property value="%{estimate.name}"/>';
			dom.get("projectCode").value='<s:property value="%{estimate.projectCode.code}"/>';
		}
	</s:iterator>
	if(mbDataTable.getRecordSet().getLength()>0){
	  var ans=confirm('<s:text name="reset.on.workorderestimate"/>'+": "+obj.options[obj.selectedIndex].text+" ?");
	  if(ans) {
		resetMBDataTable();
		resetMBNTDataTable();
		resetMBLSDataTable();
		<s:iterator value="dropdownData.workOrderEstimateList" status="row_status">	
		if('<s:property value="%{estimate.id}"/>'==obj.value){
			dom.get("workName").value='<s:property value="%{estimate.name}"/>';
			dom.get("projectCode").value='<s:property value="%{estimate.projectCode.code}"/>';
		}
		</s:iterator>
	  }
	  else{
	  	<s:iterator value="dropdownData.workOrderEstimateList" status="row_status">	
		if('<s:property value="%{estimate.name}"/>'==estVal){
			obj.value='<s:property value="%{estimate.id}"/>';
		}
		</s:iterator>
		dom.get("workName").value=estVal;
		dom.get("workName").value=estProjCode;
	  }
	}
}
function disablePreparedBy(){
	if(document.getElementById('defaultPreparedById').value!='')
	{
		document.getElementById('mbPreparedBy').value = document.getElementById('defaultPreparedById').value;
		document.getElementById('mbPreparedBy').disabled = true;
		if(document.getElementById('designation').value =='')
			document.getElementById('designation').value = '<s:property value="%{defaultDesgination}"/>' ;
		
	}
}

function checkForRCEstimate(){
	var estimateId =  document.getElementById('estimateId').value;
	if(estimateId != '')
	{
		makeJSONCall(["Value","estimateNum","estimateId","woId"],'${pageContext.request.contextPath}/estimate/ajaxEstimate!checkIfRCEstimate.action',{estimateId:estimateId},validateRCEstimateSuccess,validateRCEstimateFailure) ;		
	}
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
	
	if(checkResult != '' && checkResult=='yes'){
		dom.get("isRCEstimate").value = 'yes';
		document.getElementById('nonTenderedLumpSumpTab').style.visibility='hidden';
	}	
	else {
		dom.get("isRCEstimate").value = '';
		document.getElementById('nonTenderedLumpSumpTab').style.visibility='visible';
	}
}

validateRCEstimateFailure= function(){
    dom.get("mb_error").style.display='';
	document.getElementById("mb_error").innerHTML='<s:text name="wp.rcEstimate.check.failure" />';
}
var isDateFormAjxCall = false;
 function validateEntryDate(){
	 	var woCommencedDate = '<s:date name="workCommencedDate" format="dd/MM/yyyy"/>';
		var mbDateVal = measurementBookForm.mbDate.value ;
	 	var latestMbDate='';
	  	if(mbDateVal != ''){
			 if(woCommencedDate != '' && compareDate(woCommencedDate,mbDateVal) == -1){
				 dom.get("mb_error").style.display='';
				 document.getElementById("mb_error").innerHTML='<s:text name="mb.lessThan.wrk.cmmncd.date" /> '+woCommencedDate+'. ' +'<s:text name="pls.enter.valid.date" />';
				 window.scroll(0,0);
				 return false;
			 } else {
				 clearMessage("mb_error");
			 }
		if(isDateFormAjxCall){
			 latestMbDate = dom.get("latestMBDate").value;
		 } else {
			 latestMbDate = '<s:date name="latestMBDate" format="dd/MM/yyyy" var="lastMB"/><s:property value="lastMB"/>';
		 }
		 if(latestMbDate != ""){
			 if(compareDate(latestMbDate,mbDateVal) == -1){
				 dom.get("mb_error").style.display='';
				 document.getElementById("mb_error").innerHTML='<s:text name="mb.lessThan.latest.mbdate.date" /> '+latestMbDate+'. ' +'<s:text name="pls.enter.valid.date" />';
				 window.scroll(0,0);
				 return false;
			     }
			 }
		 }
		  return true;
	 }
 
 function getLatestMBDateForEstimateSelected(obj){
		if(obj.value != "-1")
			makeJSONCall(["value"],'${pageContext.request.contextPath}/measurementbook/ajaxMeasurementBook!getLatestMBDateforSelectedEstimate.action',{woId :'<s:property value="%{woId}"/>',estId:obj.value},latestMBDateSuccessHandler,latestMBDateFailureHandler) ;
	}

	latestMBDateSuccessHandler = function(req,res){
		results=res.results;
		if(results != ''){
			dom.get("latestMBDate").value= results[0].value;
			isDateFormAjxCall = true;
		}
	}

	latestMBDateFailureHandler = function(){
		dom.get("mb_error").style.display='';
		document.getElementById("mb_error").innerHTML='<s:text name="est.check.failure" />';
	}
</script>
<s:hidden id="latestMBDate" name="latestMBDate" value="%{latestMBDate}"/>
<table id="mbheaderTable" width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td>
			&nbsp;
		</td>
	</tr>
	<tr>
		<td>
			<table id="catDetailsTable" width="100%" border="0" cellspacing="0"
				cellpadding="0">
				<tr>
					<td width="11%" class="greyboxwk">
						<s:text name="measurementbook.workorder" />
						:
					</td>
					<td width="21%" class="greybox2wk">
						<s:textfield name="workorderNo"  id="workOrderNumber" value="%{workOrder.workOrderNumber}" cssClass="selectboldwk" 
							disabled="true"/>
						<s:hidden id="workOrder" name="workOrderId" value="%{workOrder.id}"/>
						<s:hidden id="workOrder1" name="workOrder" value="%{workOrder.id}"/>
					</td>
					<td width="15%" class="greyboxwk">
						<span class="mandatory">*</span>
						<s:text name="measurementbook.entrydate" />
						:
					</td>
					<td width="53%" class="greybox2wk">
						<s:date name="mbDate" var="mbDateFormat"
							format="dd/MM/yyyy" />
						<s:textfield name="mbDate" value="%{mbDateFormat}"
							id="mbDate" cssClass="selectwk" onfocus="javascript:vDateType='3';"
							onkeyup="DateFormat(this,this.value,event,false,'3')" maxlength="10" onblur="validateEntryDate();"/>
						<a	href="javascript:show_calendar('forms[0].mbDate',null,null,'DD/MM/YYYY');"
							onmouseover="window.status='Date Picker';return true;"
							onmouseout="window.status='';return true;"><img
								src="/egworks/resources/erp2/images/calendar.png"
								alt="Calendar" width="16" height="16" border="0"
								align="absmiddle" />
						</a>
					</td>
				</tr>
				<tr>
					<td width="11%" class="whiteboxwk">
						<span class="mandatory">*</span>
						<s:text name="measurementbook.estNumber" />	:
					</td>
					<td width="21%" class="whitebox2wk">
					<s:if test="%{dropdownData.workOrderEstimateList.size()==1}">
						<s:iterator value="dropdownData.workOrderEstimateList" status="row_status">	
						<s:textfield name="estimateNo" id="estimateNo" cssClass="selectboldwk" 
						value="%{estimate.estimateNumber}" disabled="true"/>
						<s:hidden name="estimateId" id="estimateId" value="%{estimate.id}"/>
						</s:iterator>
					</s:if>
					<s:else>
					<s:if test="%{id==null}">
						<s:select headerKey="-1"
							headerValue="%{getText('estimate.default.select')}"
							name="estimateId"
							value="%{estimateId}"
							id="estimateId" cssClass="selectwk"
							list="dropdownData.workOrderEstimateList" listKey="estimate.id"
							listValue="estimate.estimateNumber" onchange="checkForRCEstimate();populateWorkName(this);getLatestMBDateForEstimateSelected(this);"/>
							</s:if><s:else>
							<s:textfield name="estimateNo" id="estimateNo" cssClass="selectboldwk" 
								value="%{workOrderEstimate.estimate.estimateNumber}" disabled="true"/>
								<s:hidden name="estimateId" id="estimateId" value="%{workOrderEstimate.estimate.id}"/>
							</s:else>
					</s:else>
					
					</td>
					<td width="11%" class="whiteboxwk">
						<s:text name="measurementbook.estName" /> :
					</td>
					<td width="21%" class="whitebox2wk">
					<s:if test="%{dropdownData.workOrderEstimateList.size()==1}">
					<s:iterator value="dropdownData.workOrderEstimateList" status="row_status">	
						<s:textfield name="workName" value="%{estimate.name}" disabled="true" id="workName" cssClass="selectboldwk"/>
					</s:iterator>
					</s:if>
					<s:else>
						<s:if test="%{id==null}">
						  <s:textfield name="workName" id="workName" readonly="true" cssClass="selectboldwk"/>
						</s:if>
						<s:else>
							<s:textfield name="workName" value="%{workOrderEstimate.estimate.name}" disabled="true" id="workName" cssClass="selectboldwk"/>
						</s:else>
					</s:else>
					</td>
				</tr>
				<tr>
				<td width="11%" class="greyboxwk">
						<s:text name="measurementbook.projectcode" /> :
					</td>
					<td width="21%" class="greybox2wk">
					<s:if test="%{dropdownData.workOrderEstimateList.size()==1}">
					<s:iterator value="dropdownData.workOrderEstimateList" status="row_status">	
						<s:textfield name="projectCode" value="%{estimate.projectCode.code}" disabled="true" id="projectCode" cssClass="selectboldwk"/>
					</s:iterator>
					</s:if>
					<s:else>
						<s:if test="%{id==null}">
						  <s:textfield name="projectCode" id="projectCode" readonly="true" cssClass="selectboldwk"/>
						</s:if>
						<s:else>
							<s:textfield name="projectCode" value="%{workOrderEstimate.estimate.projectCode.code}" disabled="true" id="projectCode" cssClass="selectboldwk"/>
						</s:else>
					</s:else>
					</td>
				<td width="11%" class="greyboxwk">
						<span class="mandatory">*</span>
						<s:text name="measurementbook.mbref" />
						:
					</td>
					<td width="21%" class="greybox2wk">
						<s:textfield name="mbRefNo" value="%{mbRefNo}" id="mbRefNo" cssClass="selectwk"/>
					</td>
					<td width="11%" class="greyboxwk">
						&nbsp;
					</td>
					<td width="21%" colspan="3"  class="greybox2wk">
						&nbsp;
					</td>
				</tr>
				<tr>
					<td width="11%" class="whiteboxwk">
						<span class="mandatory">*</span>
						<s:text name="measurementbook.frompage" />
						:
					</td>
					<td width="21%" class="whitebox2wk">
						<s:textfield name="fromPageNo"  value="%{fromPageNo}" id="fromPageNo" cssClass="selectwk" maxlength="5"/>
					</td>
						<td width="11%" class="whiteboxwk">
						<s:text name="measurementbook.topage" />
						:
					</td>
					<td width="21%" colspan="3"  class="whitebox2wk">
						<s:textfield name="toPageNo" value="%{toPageNo}" id="toPageNo" cssClass="selectwk" maxlength="5"/>
					</td>
				</tr>
				<tr>
					<s:hidden id="defaultPreparedById" name="defaultPreparedById"/>
					
					<td width="17%" class="greyboxwk">
						<span class="mandatory">*</span>
						<s:text	name="measurementbook.preparedby" />:
					</td>
					<td width="17%" class="greybox2wk">
						<s:select headerKey="-1"
							headerValue="%{getText('estimate.default.select')}"
							name="mbPreparedBy"
							value="%{mbPreparedBy.idPersonalInformation}"
							id="mbPreparedBy" cssClass="selectwk"
							list="dropdownData.preparedByList" listKey="id"
							listValue="%{employeeName+ ' ~ ' +desigId.designationName}" onchange='showDesignation(this);' />
					</td>
					<td width="12%" class="greyboxwk">
						<s:text name="measurementbook.designation" />
						:
					</td>
					<td width="54%" class="greybox2wk">
						<s:textfield value="%{mbPreparedByView.desigId.designationName}"
							type="text" disabled="true"
							cssClass="selectboldwk" id="designation" />
					</td>
					<s:if test="%{dropdownData.preparedByList.size==1}" >
		                <script>
		                	disablePreparedBy();
		                </script>
                	</s:if>
				</tr>
				<tr>
					<td width="11%" class="whiteboxwk">
						<span class="mandatory">*</span>
						<s:text name="measurementbook.abstract" />
						:
					</td>
					<td width="21%" colspan="3"  class="whitebox2wk">
						<s:textarea name="mbAbstract" value="%{mbAbstract}" id="mbAbstract" cssClass="selectwk" 
							cols="56" rows="3"/>
					</td>
				</tr>
				<tr>
					<td width="11%" class="greyboxwk">
						<s:text name="measurementbook.contractor" />
						:
					</td>
					<td width="21%" class="greybox2wk">
						<s:textfield value="%{workOrder.contractor.name}" id="contractor" 
						cssClass="selectboldwk" disabled="true"/>
					</td>
					<td width="15%" class="greyboxwk">
						<s:text name="measurementbook.contractor.code" />
						:
					</td>
					<td width="53%" class="greybox2wk">
						<s:textfield value="%{workOrder.contractor.code}" id="contractorCode" 
						cssClass="selectboldwk" disabled="true"/>
					</td>
				</tr>
				<tr>
					<td width="15%" class="whiteboxwk">
						<s:text name="measurementbook.contractor.comments" />
						:
					</td>
					<td colspan="3" width="11%" class="whitebox2wk">
						<s:textarea name="contractorComments" value="%{contractorComments}" id="contractorComments" 
							cssClass="selectwk" cols="56" rows="3"/>
					</td>
				</tr>
				<tr>
			    	<td class="greyboxwk">
			    		<s:text name="measurementbook.islegacy" />
			    	</td>
			    	<td class="greybox2wk">
			    		<s:checkbox id="isLegacyMB" name="isLegacyMB" value="%{isLegacyMB}" onclick="checkMBLegacy(this)" />
			    	</td>
			    	<td class="greyboxwk"></td>
			    	<td class="greybox2wk"></td>
			    </tr>			   
			</table>
		</td>
	</tr>
 	<tr>
 		<td>&nbsp;</td>
 	</tr>
</table>
