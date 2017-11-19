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


<script type="text/javascript">

function setEstimateId(elem){
	document.techSanctionEstimatesForm.estimateId.value = elem.value;
}

var estNum="";
var estimateIden="";
var execDeptName=""
var copyCancelEstResults;

function gotoPage(obj)
{
	var currRow=getRow(obj);
	estimateIden = getControlInBranch(currRow,'estimateIden');
	var estimateStateId = getControlInBranch(currRow,'estimateStateId');
	var docNumber = getControlInBranch(currRow,'docNumber');
	execDeptName = getControlInBranch(currRow,'execDeptName');
	estNum = getControlInBranch(currRow,'estNum');
	
	if(dom.get('searchActions')[1]!=null && obj.value==dom.get('searchActions')[1].value)
	{
		window.open("${pageContext.request.contextPath}/estimate/abstractEstimate-edit.action?id="+estimateIden.value+
		"&sourcepage=search",'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
	}
	if(dom.get('searchActions')[2]!=null && obj.value==dom.get("searchActions")[2].value)
	{
		document.location.href="${pageContext.request.contextPath}/estimate/abstractEstimatePDF.action?estimateID="+estimateIden.value;
	}
	if(dom.get('searchActions')[3]!=null && obj.value==dom.get("searchActions")[3].value)
	{
		window.open("${pageContext.request.contextPath}/estimate/abstractEstimate-workflowHistory.action?stateId="+
		estimateStateId.value,'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
	}
	if(dom.get('searchActions')[4]!=null && obj.value==dom.get("searchActions")[4].value)
	{
		//viewDocumentManager(docNumber.value);return false;
	}
	if(dom.get('searchActions')[5]!=null && obj.value==dom.get("searchActions")[5].value)
	{
		checkEstNumberForCopy(estNum.value);
	}
}

function checkEstNumberForCopy(estimateNum){ 
	makeJSONCall(["isCancelEstCopyExists"],'${pageContext.request.contextPath}/estimate/ajaxEstimate!validateCancelledEstForCopy.action',{estimateNum:estimateNum},copyCancelEstSuccessHandler,copyCancelEstFailureHandler) ;
}

copyCancelEstSuccessHandler = function(req,res) {
	copyCancelEstResults=res.results;
	var loginUserDept = document.getElementById('loginUserDeptName').value;
	var estimateNo=estNum.value
	var cancelledEst = estimateNo.slice(-2);

	if(execDeptName.value!=loginUserDept) {
		alert('<s:text name="estimate.copy.loginuserdept.execdept.different"/>');
		return false;
	} 
	else {
		var msg1='<s:text name="estimate.copy.confirm"/>';
		if(!confirm(msg1+": "+estimateNo+" ?")) {
			return false;
		}
		else if(cancelledEst=="/C") {
			popup('popUpDiv');		
			window.scrollTo(document.body.parentNode.scrollWidth/2,document.body.parentNode.scrollHeight/2);
		}
		else {
			document.getElementById("copyCancelledEstNum").value="no";
			window.open("${pageContext.request.contextPath}/estimate/copyEstimate!copyEstimate.action?copyCancelledEstNum="+
					document.getElementById("copyCancelledEstNum").value+"&estimateId="+estimateIden.value,'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
			return true;
		}
	}
} 
          
copyCancelEstFailureHandler = function() {
	alert('<s:text name="estimate.copy.cancelled.fail"/>');
	return false;
}

function createNewEstimate() {
	document.getElementById("copyCancelledEstNum").value="no";
	window.open("${pageContext.request.contextPath}/estimate/copyEstimate!copyEstimate.action?copyCancelledEstNum="+
			document.getElementById("copyCancelledEstNum").value+"&estimateId="+estimateIden.value,'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
	toggle('blanket');
	toggle('popUpDiv');
}

function copyCancelledEst() {
	if(copyCancelEstResults[0].isCancelEstCopyExists=='true') {
		alert('<s:text name="estimate.copy.alredy.copied.estimate"/>');
		toggle('blanket');
		toggle('popUpDiv');
		return false;
  	}
	else {
		document.getElementById("copyCancelledEstNum").value="yes";
		toggle('blanket');
		toggle('popUpDiv');
		window.open("${pageContext.request.contextPath}/estimate/copyEstimate!copyEstimate.action?copyCancelledEstNum="+
				document.getElementById("copyCancelledEstNum").value+"&estimateId="+estimateIden.value,'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
		return true;
	}
}

function closePopUp() {
	toggle('blanket');
	toggle('popUpDiv');
	return false;
}

function checkWorksPackage(){ 
	var estimateId = document.techSanctionEstimatesForm.estimateId.value;
	makeJSONCall(["wpNumber","isVoucherExists","yearEndApprOwner",'woNumber'],'${pageContext.request.contextPath}/estimate/ajaxEstimate!validateEstimateForCancel.action',{estimateId:estimateId},myWorksPackageSuccessHandler,myWorksPackageFailureHandler) ;
}

myWorksPackageSuccessHandler = function(req,res) {
	clearMessage('searchEstimate_error');
	results=res.results;	
	if(results[0].woNumber != '') {
		document.getElementById("searchEstimate_error").style.display='';
	  	document.getElementById("searchEstimate_error").innerHTML='<s:text name="cancelEstimate.rc.wo.created.message.part1"/>'+results[0].woNumber+' <s:text name="cancelEstimate.rc.wo.created.message.part2"/>';
	  	window.scroll(0,0);
	  	return false;
  	}
	if(results[0].isVoucherExists=='true') {
		document.getElementById("searchEstimate_error").style.display='';
	  	document.getElementById("searchEstimate_error").innerHTML='<s:text name="cancelEstimate.vouchers.created.message"/>';
	  	window.scroll(0,0);
	  	return false;
  	}
	if(results[0].wpNumber!='') {
		document.getElementById("searchEstimate_error").style.display='';
	  	document.getElementById("searchEstimate_error").innerHTML='<s:text name="cancelEstimate.WP.created.message.part1"/>'+results[0].wpNumber+', <s:text name="cancelEstimate.WP.created.message.part2"/>';
	  	window.scroll(0,0);
	  	return false;
  	}
	if(results[0].yearEndApprOwner!='') {
		document.getElementById("searchEstimate_error").style.display='';
	  	document.getElementById("searchEstimate_error").innerHTML='<s:text name="cancelEstimate.multiyear.validate.message.part"/>: '+results[0].yearEndApprOwner;
	  	window.scroll(0,0);
	  	return false;
  	}	
	
	var cancellationReason = document.techSanctionEstimatesForm.cancellationReason.value;  
	var cancelRemarks = document.techSanctionEstimatesForm.cancelRemarks.value; 
	if(cancellationReason==''){
		dom.get("searchEstimate_error").style.display='';
		document.getElementById("searchEstimate_error").innerHTML='<s:text name="validate.cancel.cancelReasons"/>'; 
		window.scroll(0,0);
		return false;
	}
	if(cancellationReason=='OTHER' && cancelRemarks == ''){
		dom.get("searchEstimate_error").style.display='';
		document.getElementById("searchEstimate_error").innerHTML='<s:text name="validate.cancel.estimate.remarks"/>';
		window.scroll(0,0);
		return false;
	}	
    if(validateCancel()) {
    	doLoadingMask();
    	document.techSanctionEstimatesForm.action='${pageContext.request.contextPath}/estimate/abstractEstimate!cancelApprovedEstimate.action';
		document.getElementById('status').disabled=false
    	document.techSanctionEstimatesForm.submit();
    }
} 
          
myWorksPackageFailureHandler = function() {
	dom.get("searchEstimate_error").style.display='';
	document.getElementById("searchEstimate_error").innerHTML='<s:text name="estimate.check.fail"/>';
}

function setEstimateIds(elem){
	var currRow=getRow(elem);
	document.techSanctionEstimatesForm.estimateId.value = elem.value;
	dom.get("pcStatus").value=getControlInBranch(currRow,'projectCodeStatus').value;
	dom.get("pcCode").value=getControlInBranch(currRow,'prjCode').value;
}

function setEstimateNumber(elem){
	document.techSanctionEstimatesForm.estimateNumber.value = elem;
}


function cancelAbstractEstimate() {
	clearMessage('searchEstimate_error');
	var projectCodeStatus=document.techSanctionEstimatesForm.pcStatus.value;
	var projectCode=document.techSanctionEstimatesForm.pcCode.value;
	if(dom.get('estimateId').value=='') {		
    	dom.get("searchEstimate_error").style.display='';
		document.getElementById("searchEstimate_error").innerHTML+='<s:text name="estimate.cancel.select.null" /><br>';
	  	window.scroll(0,0);
		return false;
	}
	else if(projectCodeStatus=='CLOSED') {		
    	dom.get("searchEstimate_error").style.display='';
    	document.getElementById("searchEstimate_error").innerHTML='<s:text name="cancelEstimate.projectClosed.message.part1"/>'+projectCode+', <s:text name="cancelEstimate.projectClosed.message.part2"/>';
	  	window.scroll(0,0);
		return false;
	}
	else {
		checkWorksPackage();
	}
		
}

function validateCancel() {
	var msg='<s:text name="estimate.cancel.confirm"/>';
	var estimateNo=document.techSanctionEstimatesForm.estimateNumber.value;
	if(!confirm(msg+": "+estimateNo+" ?")) {
		return false;
	}
	else {
		return true;
	}
}

function toggleCancelRemarks(obj) { 
	if(obj.value=='OTHER') {
		document.getElementById("cancelRemarksDtls").style.display='';
	}
	else {
		document.getElementById("cancelRemarksDtls").style.display='none';
		dom.get("cancelRemarks").value='';
	}	
}  

</script>
<div id="blanket" style="display:none;"></div>
<div id="popUpDiv" style="display:none;" >
	<s:text name="estimate.copy.cancelledEstimate"/>(<a href="#" onclick="copyCancelledEst();">Yes</a>/<a href="#" onclick="createNewEstimate();">No</a>)?
	<br> <a href="#" onclick="closePopUp();">Close</a>
</div>
<s:hidden id="copyCancelledEstNum" name="copyCancelledEstNum"/>

<s:if test= "%{searchResult.fullListSize!= 0 || searchResult.fullListSize== 0}">
	<div class="col-md-12 table-header text-left">
		<s:if test="%{source=='financialdetail'}">
				<s:text name='page.title.financial.detail' />
		</s:if>
		<s:elseif test="%{source=='technical sanction'}">
				<s:text name='page.title.Technical.Sanction' />
		</s:elseif>
		<s:elseif test="%{source=='Financial Sanction'}">
				<s:text name='page.title.Financial.Sanction' />
		</s:elseif>
		<s:elseif test="%{source=='AdministrativeSanction'}">
				<s:text name='page.title.Admin.Sanction' />
		</s:elseif>
		<s:elseif test="%{source=='createNegotiation'}">
				<s:text name='page.result.search.estimate' />
		</s:elseif>
		<s:else>
				<s:text name='page.title.search.estimates' />
		</s:else>
	</div>
</s:if>

<s:if test= "%{searchResult.fullListSize!= 0}">	
<display:table name="searchResult" pagesize="30" uid="currentRow"
	cellpadding="0" cellspacing="0" requestURI=""
	class="table table-hover">

	<s:if
		test="%{source=='financialdetail' || source=='createNegotiation'}">
		<display:column headerClass="pagetableth" class="pagetabletd" style="width:4%;text-align:center">
			<input name="radio" type="radio" id="radio"
				value="<s:property value='%{#attr.currentRow.id}'/>"
				onClick="setEstimateId(this);" />
		</display:column>
	</s:if>
 	<s:if test="%{source=='cancelEstimate'}">
        <display:column headerClass="pagetableth" class="pagetabletd" title="Select" style="width:4%;" titleKey="column.title.select">
			<input name="radio" type="radio" id="radio"
				value="<s:property value='%{#attr.currentRow.id}'/>"
				onClick='setEstimateIds(this);setEstimateNumber("<s:property value='%{#attr.currentRow.estimateNumber}'/>");' />
		</display:column>
    </s:if>
	<display:column title="Sl.No" titleKey='estimate.search.slno' headerClass="pagetableth" class="pagetabletd" style="width:4%;text-align:right" >
		<s:property value="#attr.currentRow_rowNum + (page-1)*pageSize" />
	</display:column> 

	<display:column headerClass="pagetableth" class="pagetabletd" title="Estimate Number" style="width:10%;text-align:left" property="estimateNumber" />
	<display:column headerClass="pagetableth" class="pagetabletd" title="Executing Department" titleKey='estimate.search.executingdept' style="width:8%;text-align:left" property='executingDepartment.name' />
	<s:if test="%{source==''}">
		<display:column headerClass="pagetableth" class="pagetabletd" title="User Department" titleKey='estimate.search.userdept' style="width:8%;text-align:left" property='userDepartment.name' />
	</s:if>	
	
	<display:column headerClass="pagetableth" class="pagetabletd" title="Name" titleKey='estimate.search.name' style="width:20%;text-align:left" property='name' />

	<display:column headerClass="pagetableth" class="pagetabletd" title="Status" titleKey='estimate.search.status' style="width:10%;text-align:left">
		<s:property value="#attr.currentRow.egwStatus.description" />
	</display:column>
	
	<display:column headerClass="pagetableth" class="pagetabletd" title="Type" titleKey='estimate.search.type' style="width:10%;text-align:left" property='natureOfWork.name' />

	<display:column headerClass="pagetableth" class="pagetabletd" title="Estimate Date" titleKey='estimate.search.estimateDate' style="width:10%;text-align:center" >
		<s:date name="#attr.currentRow.estimateDate" format="dd/MM/yyyy" />
	</display:column>	
	
	<s:if test="%{status!='ADMIN_SANCTIONED' && status!='CANCELLED'}">
		<display:column headerClass="pagetableth" class='pagetabletd' title="Owner" titleKey='estimate.search.owner' style="width:10%;text-align:left" property='positionAndUserName' />
	</s:if>
	
	<s:if test="%{source=='cancelEstimate'}">
		<display:column headerClass="pagetableth" class="pagetabletd" title="Project Code" titleKey='estimate.search.projectcode' style="width:10%;text-align:left" >
			<s:property value="%{#attr.currentRow.projectCode.code}" />
			<s:hidden name="prjCode" id="prjCode" value="%{#attr.currentRow.projectCode.code}" />
			<s:hidden name="projectCodeStatus" id="projectCodeStatus" value="%{#attr.currentRow.projectCode.egwStatus.code}" />
		</display:column>
	</s:if>	
			
	<display:column headerClass="pagetableth" class="pagetabletd" title="Total" titleKey='estimate.search.total' style="width:10%;text-align:right" property='totalAmount.formattedString' />
	
	<display:column title='Action' titleKey="estimate.search.action" headerClass="pagetableth" class="pagetabletd" style="width:16%;text-align:center">
	<s:hidden name="docNumber" id="docNumber" value="%{#attr.currentRow.documentNumber}" />
	<s:hidden name="estimateIden" id="estimateIden" value="%{#attr.currentRow.id}" />
	<s:hidden name="estimateStateId" id="estimateStateId" value="%{#attr.currentRow.state.id}" />
	<s:hidden name="execDeptName" id="execDeptName" value="%{#attr.currentRow.executingDepartment.name}" />
	<s:hidden name="estNum" id="estNum" value="%{#attr.currentRow.estimateNumber}" />
	<s:select theme="simple" id="searchActions" name="searchActions"
			list="estimateActions"
			headerValue="%{getText('estimate.default.select')}" headerKey="-1"
			onchange="gotoPage(this);"></s:select>
	</display:column>

</display:table>
</s:if>
<s:elseif test= "%{searchResult.fullListSize== 0}">	
	   <div class="col-xs-12 mtb-5">
	     <div class="alert alert-warning"><s:text name='label.no.records.found'></s:text></div>
   	   </div>   	   
</s:elseif>	  

<s:if test="%{searchResult.fullListSize != 0 && source=='cancelEstimate'}" > -->
	
		<div class="form-group">
		<label class="col-sm-2 control-label text-right">
		   <s:text name="cancellation.reason" /><span class="mandatory"></span>
		</label>
		<div class="col-sm-3 add-margin">
			<s:select id="cancellationReason" name="cancellationReason" cssClass="form-control" list="#{'':'---------Select---------','DATA ENTRY MISTAKE':'DATA ENTRY MISTAKE','OTHER':'OTHER'}" onChange="toggleCancelRemarks(this)" />
		</div>
	</div>
	
	<div class="form-group" id="cancelRemarksDtls" style="display:none">
		<label class="col-sm-2 control-label text-right">
		   <s:text name="cancel.remarks" /><span class="mandatory"></span>
		</label>
		<div class="col-sm-3 add-margin">
			<s:textarea cssClass="form-control" id="cancelRemarks" name="cancelRemarks" rows="2" cols="35" />
		</div>
	</div>
	
	<div class="form-group">
		<div class="col-sm-offset-2 col-sm-3">
			<input type="button" class="btn btn-primary"
				value="Cancel Estimate" id="addButton"
				name="cancelEstimate" onclick="cancelAbstractEstimate();"
				align="center" />
		</div>
	</div>
	
</s:if>
