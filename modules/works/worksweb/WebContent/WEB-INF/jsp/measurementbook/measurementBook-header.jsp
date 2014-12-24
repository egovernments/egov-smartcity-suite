
<style type="text/css">
#yui-dt0-bodytable,#yui-dt1-bodytable,#yui-dt2-bodytable {
	Width: 100%;
}
</style>
<script>

function showDesignation(elem){
	//Added to show employee name and designation in preparedby dropdown
	dom.get("mb_error").style.display='none';
	document.getElementById("mb_error").innerHTML="";
	if(elem.value != -1){
		var preparedByDesgn=preparedByDesignationForMB(elem);
		if(preparedByDesgn!="")
		 	dom.get("designation").value=preparedByDesgn;
		else{
		 	dom.get("mb_error").style.display='';
		 	document.getElementById("mb_error").innerHTML='Unable to load designation';
		} 
	}
	else
		dom.get("designation").value="";
}

function loadDesignation(){
	showDesignation(dom.get("mbPreparedBy"));
}

function preparedByDesignationForMB(elem){
	var temp, result;
	if(elem.type=='hidden')
	{
		result = document.getElementById("mbPreparedByTF").value.split('-');
		
	}
	else
	{
		temp=elem.options[elem.selectedIndex].innerHTML; 
		result=temp.split('-');
		
	}
	if(result.length>1 && result[1]!="" && result[1]!=null && result[1]!=undefined)
		return result[1];
	else
		return "";
}

function validateHeaderBeforeSubmit(measurementBookForm) {
//validate mb details
validateMBDetailsTable(mbDataTable,'quantity');
validateMBDetailsTable(extraItemsDataTable,'extraItemsquantity');
//validating for cancel mb with respect to  bill status	
		if(document.getElementById("actionName").value=='cancel'){
 			<s:if test="%{model.getMbBills()!=null && model.getMbBills().getEgBillregister()!=null && model.getMbBills().getEgBillregister().getCurrentState().getValue()!='CANCELLED'}">
				dom.get("mb_error").style.display='';     
    			document.getElementById("mb_error").innerHTML='<s:text name="measurementbook.cancel.failure" />';
    			window.scroll(0,0);
    			return false;
			</s:if>
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
    
    var mbDate = measurementBookForm.mbDate.value;
    if (mbDate == '') {    	
    	dom.get("mb_error").style.display='';     
    	document.getElementById("mb_error").innerHTML='<s:text name="mbheader.mbdate.null" />';
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
     
    if (measurementBookForm.mbRefNo.length > 50) {    	
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
    
    if (measurementBookForm.fromPageNo.value <0) {    	
    	dom.get("mb_error").style.display='';     
    	document.getElementById("mb_error").innerHTML='<s:text name="mbheader.fromPageNo.non.negative" />';
    	window.scroll(0,0);
    	return false;
    }
    
    var toPageNo = measurementBookForm.toPageNo.value;
    if (toPageNo  < 0) {    	
    	dom.get("mb_error").style.display='';     
    	document.getElementById("mb_error").innerHTML='<s:text name="mbheader.toPageNo.non.negative" />';
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
    
   var frmpageno=measurementBookForm.fromPageNo.value;
   for (var i = 0; i < frmpageno.length; i++) 
    {
        var ch = frmpageno.charAt(i)
        if (ch == ".")
        {
        	dom.get("mb_error").style.display='';
        	document.getElementById("mb_error").innerHTML='<s:text name="invalid.fieldvalue.fromPageNo" />';
 			window.scroll(0,0);
            return false;
        }
    }
    
   var topageno=measurementBookForm.toPageNo.value;
   for (var i = 0; i < topageno.length; i++) 
    {
    	var ch = topageno.charAt(i)
        if (ch == ".")
        {
        	dom.get("mb_error").style.display='';
 			document.getElementById("mb_error").innerHTML='<s:text name="invalid.fieldvalue.toPageNo" />';
 			window.scroll(0,0);
            return false;
        }
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
			dom.get("workName").value='<s:property value="%{estimate.nameJS}"/>';
			dom.get("projectCode").value='<s:property value="%{estimate.projectCode.code}"/>';
		}
	</s:iterator>
	if(mbDataTable.getRecordSet().getLength()>0){
	  var ans=confirm('<s:text name="reset.on.workorderestimate"/>'+": "+obj.options[obj.selectedIndex].text+" ?");
	  if(ans) {
		resetMBDataTable();
		<s:iterator value="dropdownData.workOrderEstimateList" status="row_status">	
		if('<s:property value="%{estimate.id}"/>'==obj.value){
			dom.get("workName").value='<s:property value="%{estimate.nameJS}"/>';
			dom.get("projectCode").value='<s:property value="%{estimate.projectCode.code}"/>';
		}
		</s:iterator>
	  }
	  else{
	  	<s:iterator value="dropdownData.workOrderEstimateList" status="row_status">	
		if('<s:property value="%{estimate.nameJS}"/>'==estVal){
			obj.value='<s:property value="%{estimate.id}"/>';
		}
		</s:iterator>
		dom.get("workName").value=estVal;
		dom.get("workName").value=estProjCode;
	  }
	}
}

function validateMbEntryDate(){
    var appConfig = '<s:property value="%{mbAppConfigValue}"/>';
     var spillOverWork;
     <s:if test="%{isSpillOverWorks}">
		spillOverWork=true;
	</s:if>
	<s:else>
		spillOverWork=false;
	</s:else>

     if(dom.get("spilloverWork")!=null) {
     	spillOverWork=dom.get("spilloverWork").value;
     }
    
    var mbEntryDate = dom.get("mbDate").value; 
	var workCompletionDate = '<s:property value="%{workCompletionDate}"/>';
	if(appConfig == 'yes' &&  !spillOverWork  &&  workCompletionDate!=null &&  compareDate( formatDate6(mbEntryDate),formatDate6(workCompletionDate) ) == -1){
	        dom.get("mb_error").style.display='';     
		    document.getElementById("mb_error").innerHTML='<s:text name="mbheader.mbDate.invalid.entryDate" />';
		    window.scroll(0,0);
		    return false;
	 }
	return true; 
 } 

</script>

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
						<s:textfield name="workorderNo"  id="workOrderNumber" value="%{workOrder.workOrderNumber}" cssClass="selectboldwk" disabled="true"/>
						<s:hidden id="workOrder" name="workOrderId" value="%{workOrder.id}"/>
						<s:hidden id="workOrder1" name="workOrder" value="%{workOrder.id}"/>
					</td>
					<td width="15%" class="greyboxwk">
						<span class="mandatory">*</span>
						<s:text name="measurementbook.entrydate" />
						:
					</td>
					<td width="53%" class="greybox2wk">
						<s:date name="mbDate" var="mbDateFormat" format="dd/MM/yyyy" />
						<s:textfield name="mbDate" value="%{mbDateFormat}"
							id="mbDate" cssClass="selectwk" onfocus="javascript:vDateType='3';"
							onkeyup="DateFormat(this,this.value,event,false,'3')" maxlength="10"/>
						<a	href="javascript:show_calendar('forms[0].mbDate',null,null,'DD/MM/YYYY');"
							onmouseover="window.status='Date Picker';return true;"
							onmouseout="window.status='';return true;"><img
								src="${pageContext.request.contextPath}/image/calendar.png"
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
						<s:textfield name="estimateNo" id="estimateNo" cssClass="selectboldwk" value="%{estimate.estimateNumber}" disabled="true"/>
						<s:hidden name="estimateId" id="estimateId" value="%{estimate.id}"/>
						<s:hidden name="spilloverWork" id="spilloverWork" value="%{estimate.isSpillOverWorks}" />
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
							listValue="estimate.estimateNumber" onchange="populateWorkName(this)"/>
							</s:if>
							<s:else>
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
					<td width="17%" class="greyboxwk">
						<span class="mandatory">*</span>
						<s:text	name="measurementbook.preparedby" />:
					</td>
					<td width="17%" class="greybox2wk">
						<s:if test="%{(sourcepage=='inbox' && model.currentState.value!='NEW' && model.currentState.value!='REJECTED') || mode=='search' }" >
			                <s:textfield id="mbPreparedByTF" value="%{preparedByTF}" type="text" readonly="true" disabled="disabled" cssClass="selectboldwk" />
			                <s:hidden name="mbPreparedBy" id="mbPreparedBy" value="%{mbPreparedBy.idPersonalInformation}"/>
			            </s:if>
			            <s:else>
							<s:select headerKey="-1"
								headerValue="%{getText('estimate.default.select')}"
								name="mbPreparedBy"
								value="%{mbPreparedBy.idPersonalInformation}"
								id="mbPreparedBy" cssClass="selectwk"
								list="dropdownData.preparedByList" listKey="id"
								listValue='employeeName+ "-" +desigId.designationName' onchange='showDesignation(this);' />
						</s:else>		
					</td>
					<td width="12%" class="greyboxwk">
						<s:text name="measurementbook.designation" />
						:
					</td>
					<td width="54%" class="greybox2wk">
						<s:textfield type="text" disabled="true"
							cssClass="selectboldwk" id="designation" />
					</td>

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
			    	<td colspan="4" class="shadowwk"></td>
			    </tr>			   
			</table>
		</td>
	</tr>
 	<tr>
 		<td>&nbsp;</td>
 	</tr>
</table>