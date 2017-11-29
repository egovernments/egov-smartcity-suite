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
function gotoPage(obj)
{
	var currRow=getRow(obj);
	var packageIden = getControlInBranch(currRow,'packageIden');
	var packageStateId = getControlInBranch(currRow,'packageStateId');
	var docNumber = getControlInBranch(currRow,'docNumber');
	var approvedDate = getControlInBranch(currRow,'approvedDate');
	var objNo = getControlInBranch(currRow,'wpNum');
	var showActions = getControlInBranch(currRow,'searchActions');
	if(showActions[1]!=null && obj.value==showActions[1].value)
	{
		window.open("${pageContext.request.contextPath}/tender/worksPackage-edit.action?id="+packageIden.value+
		"&sourcepage=search",'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
	}
	if(showActions[2]!=null && obj.value==showActions[2].value)
	{
		document.location.href="${pageContext.request.contextPath}/tender/worksPackage-viewWorksPackagePdf.action?id="+packageIden.value;
	}
	if(showActions[3]!=null && obj.value==showActions[3].value)
	{
		window.open("${pageContext.request.contextPath}/estimate/abstractEstimate-workflowHistory.action?stateId="+
		packageStateId.value,'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
	}
	if(showActions[4]!=null && obj.value==showActions[4].value)
	{
		if(docNumber.value!= null && docNumber.value!='') {
			viewDocumentManager(docNumber.value);return false;
		}
		else {
			alert("No Documents Found");
			return;
		}
	}
	if(showActions[5]!=null && obj.value==showActions[5].value)
	{
		window.open("${pageContext.request.contextPath}/tender/offlineStatus-retenderEdit.action?objectType=WorksPackage&objId="+
		packageIden.value+"&setStatus="+dom.get('setStatus').value+"&appDate="+approvedDate.value+"&objNo="+objNo.value,'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
	}
}
//For cancel Works Package
function toggleCancelRemarks(obj) { 
	if(obj.value=='OTHER') {
		document.getElementById("cancelRemarksDtls").style.display='';
	}
	else {
		document.getElementById("cancelRemarksDtls").style.display='none';
		dom.get("cancelRemarks").value='';
	}	
}
function cancelWorksPackage(){
	// Set onclick of radiobutton
	var wpId = dom.get("wpCancelId").value; 	
	if(wpId!=''){		
		 getTNForWP(wpId);			
		}		
	else{
		dom.get("searchwp_error").style.display='';
		document.getElementById("searchwp_error").innerHTML='<s:text name="wp.cancel.not.selected" />';
		window.scroll(0,0);
		return false;
	  }
	  dom.get("searchwp_error").style.display='none';
	  document.getElementById("searchwp_error").innerHTML='';
	  if(dom.get("searchwp_error")){
	  	dom.get("searchwp_error").style.display='none';
	 	dom.get("searchwp_error").innerHTML='';
	}
	

}

function getTNForWP(wpId){ 
    makeJSONCall(["istenderResponsePresent","tnNumber"],'${pageContext.request.contextPath}/tender/ajaxWorksPackage-isTRPresentForWPCheck.action',{wpId:wpId},wpSearchHandler,wpLoadFailureHandler);
}

wpSearchHandler = function(req,res){
  results=res.results;
  var istenderResponsePresent='';
  var tnNumber='';
  if(results != '') {
	  istenderResponsePresent = results[0].istenderResponsePresent;
	  tnNumber = results[0].tnNumber;
  }
	if(istenderResponsePresent == 'true'){
		dom.get("searchwp_error").style.display='';
		document.getElementById("searchwp_error").innerHTML='<s:text name="cancel.wp.tendernegotiation.present.part1"/> '+tnNumber+' <s:text name="cancel.wp.tendernegotiation.present.part2"/>';
		window.scroll(0,0);
		return false;
	}	
	var cancellationReason = document.workspackageForm.cancellationReason.value; 
	var cancelRemarks = document.workspackageForm.cancelRemarks.value; 
	if(cancellationReason==''){
		dom.get("searchwp_error").style.display='';
		document.getElementById("searchwp_error").innerHTML='<s:text name="validate.cancel.cancelReasons"/>'; 
		window.scroll(0,0);
		return false;
	}	
	if(cancellationReason=='OTHER' && cancelRemarks == ''){
		dom.get("searchwp_error").style.display='';
		document.getElementById("searchwp_error").innerHTML='<s:text name="validate.cancel.wp.remarks"/>'; 
		window.scroll(0,0);
		return false;
	}	
	if(validateCancel()){
		doLoadingMask();
		window.open('${pageContext.request.contextPath}/tender/searchWorksPackage!cancelWP.action?source=cancelWP&wpCancelId='+dom.get("wpCancelId").value+'&cancelRemarks='+cancelRemarks+'&cancellationReason='+cancellationReason,'_self');
	}	
	else
		return false;
}

wpLoadFailureHandler= function(){
    dom.get("searchwp_error").style.display='';
	document.getElementById("searchwp_error").innerHTML='<s:text name="cancel.wp.tendernegotiation.check.failed"/>';
}
function setWPCancelId(elem){ 
	var currRow=getRow(elem);
	dom.get("wpCancelId").value = elem.value; 
	dom.get("wpCancelNo").value=getControlInBranch(currRow,'worksPackageNumber').value;
}

function validateCancel() {
	var msg='<s:text name="wp.cancel.confirm"/>';
	var wpNo=dom.get("wpCancelNo").value; 
	if(!confirmCancel(msg,wpNo)) {
		return false;
	}
	else {
		return true;
	}
}
</script>
<table width="100%" border="0" cellspacing="0" cellpadding="0"> 	
	<tr><td>&nbsp;</td></tr>
	<tr>
		<td>
		<s:hidden name="setStatus" id="setStatus" value="%{setStatus}"/>
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td colspan="9" class="headingwk" align="left">
						<div class="arrowiconwk">
							<img src="/egworks/resources/erp2/images/arrow.gif"/>
						</div>
						<div class="headerplacer">
								<s:text name='page.result.search.estimate' />
						</div>
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td>
			<s:if test="%{source=='cancelWP'}">
				<s:hidden id="wpCancelId" name="wpCancelId"/>
				<s:hidden id="wpCancelNo" name="wpCancelNo"/>
			</s:if>	
			<display:table name="searchResult" pagesize="30" uid="currentRow"
				cellpadding="0" cellspacing="0" requestURI=""
				style="border:1px;width:100%;empty-cells:show;border-collapse:collapse;">
				<s:if test="%{source=='cancelWP'}">
					<display:column headerClass="pagetableth" class="pagetabletd" title="Select" titleKey="column.title.select" style="width:1%;text-align:center">
						<input name="radio" type="radio" id="radio"
							value="<s:property value='%{#attr.currentRow.id}'/>"
							onClick="setWPCancelId(this);" />
						<s:hidden id="worksPackageNumber" name="worksPackageNumber"
							value="%{#attr.currentRow.wpNumber}" />	
					</display:column>
				</s:if>
				<display:column title="Sl.No" titleKey='estimate.search.slno'
					headerClass="pagetableth" class="pagetabletd"
					style="width:1%;text-align:right">
					<s:property value="#attr.currentRow_rowNum + (page-1)*pageSize"/>
				</display:column>

				<display:column title="Works Package Number" titleKey='wp.No'
					property="wpNumber" headerClass="pagetableth" class="pagetabletd"
					style="width:5%;text-align:center"></display:column>

				<display:column title="Name" titleKey='wp.name' property="name"
					headerClass="pagetableth" class="pagetabletd"
					style="width:14%;text-align:left">
				</display:column>

				<display:column title="Works Package Date" titleKey='wp.date'
					headerClass="pagetableth" class="pagetabletd" style="width:5%;text-align:center">
					<s:date name="#attr.currentRow.wpDate" format="dd/MM/yyyy" />
				</display:column>

				<display:column title="Department" titleKey='wp.dept'
					property="department.name" headerClass="pagetableth"
					class="pagetabletd" style="width:5%;text-align:left">
				</display:column>

				<s:if test="%{status=='-1' || status=='CREATED' || status=='CHECKED' || status=='REJECTED' || status=='RESUBMITTED'}">
				<display:column title="Owner" titleKey='estimate.search.owner'
					property="employeeName" headerClass="pagetableth"
					class="pagetabletd" style="width:5%;text-align:left">
				</display:column>
				</s:if>
				<display:column title="Status" titleKey='Status'
					property="worksPackageStatus" headerClass="pagetableth" 
					class="pagetabletd" style="width:5%;text-align:left">
				</display:column>
				<display:column title="Total" titleKey='estimate.search.total'
					headerClass="pagetableth" class="pagetabletd"
					style="width:5%;text-align:right">
					<s:text name="contractor.format.number">
						<s:param name="value" value="%{#attr.currentRow.totalAmount}" />
					</s:text>
				</display:column>

				<display:column title="Actions" titleKey='action'
					headerClass="pagetableth" class="pagetabletd"
					style="width:4%;text-align:center">
					<s:hidden name="wpNum" id="wpNum"
						value="%{#attr.currentRow.wpNumber}" />
					<s:hidden name="docNumber" id="docNumber"
						value="%{#attr.currentRow.documentNumber}" />
					<s:hidden name="packageIden" id="packageIden"
						value="%{#attr.currentRow.id}" />
					<s:hidden name="packageStateId" id="packageStateId"
						value="%{#attr.currentRow.state.id}" />
						
					<s:if
						test="%{#attr.currentRow.egwStatus!=null && approvedValue!=null 
						&& approvedValue.equals(#attr.currentRow.egwStatus.code) && #attr.currentRow.state!=null}">
						<s:hidden name="approvedDate" id="approvedDate"	value="%{#attr.currentRow.state.createdDate}" />
					</s:if>
					<s:else>
						<s:hidden name="approvedDate" id="approvedDate" value="%{#attr.currentRow.wpDate}" />
					</s:else>

					<s:select theme="simple" id="searchActions" name="searchActions"
						list="#attr.currentRow.worksPackageActions"
						headerValue="%{getText('estimate.default.select')}" headerKey="-1"
						onchange="gotoPage(this);"></s:select>

				</display:column>

			</display:table>
		</td>
	</tr>
</table>
	 <s:if test="%{source.equals('cancelWP')}">
<table width="100%" border="0" cellspacing="0" cellpadding="0">	
	 <tr>												   
	  <td align="left" class="whitebox2wk">
		<b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span class="mandatory">*</span><s:text name="cancellation.reason" />:</b>&nbsp;&nbsp;
		<s:select id="cancellationReason" name="cancellationReason" cssClass="selectwk" list="#{'':'---------Select---------','DATA ENTRY MISTAKE':'DATA ENTRY MISTAKE','OTHER':'OTHER'}" onChange="toggleCancelRemarks(this)" />
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<span id="cancelRemarksDtls" style="display:none"><b><span class="mandatory">*</span><s:text name="cancel.remarks" />:</b>&nbsp;&nbsp;
		<s:textarea id="cancelRemarks" name="cancelRemarks" rows="2" cols="35" />
		</span> 
	  </td>
	</tr> 
	<tr>
		<td colspan="4">
			<div class="buttonholderwk">
			<input type="button" class="buttonadd"
				value="Cancel Works Package" id="addButton"
				name="cancelWPbutton" onclick="cancelWorksPackage();"
				align="center" />
			</div>
		</td>
	</tr>
</table>
</s:if>
