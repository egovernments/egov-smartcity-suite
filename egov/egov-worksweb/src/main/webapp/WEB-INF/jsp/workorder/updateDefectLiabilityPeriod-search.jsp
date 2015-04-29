<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>

<script src="<egov:url path='js/works.js'/>"></script>
<script src="<egov:url path='js/helper.js'/>"></script>

<html>
	<head>	
		<s:if test="%{mode=='edit'}">
			<title><s:text name='modify.dlp.page.title'/> </title>
		</s:if>
		<s:else>
			<title><s:text name='dlp.update.page.title'/> </title>
		</s:else>
	</head>
<style>
#warning {
  display:none;
  color:blue;
}
</style>
<script type="text/javascript">
var projectCodeSearchSelectionHandler = function(sType, arguments){ 
	var oData = arguments[2];
	dom.get("projectCodeId").value = oData[1];
};

var estimateNumberSearchSelectionHandler = function(sType, arguments){ 
	var oData = arguments[2];
	 dom.get("estimateId").value = oData[1];
};

var workOrderNumberSearchSelectionHandler = function(sType, arguments){ 
	var oData = arguments[2];
	dom.get("workOrderId").value = oData[1];
};

function validateSearch(){
	if(document.getElementById('projectCodeSearch').value=="" && document.getElementById('estimateNumberSearch').value==""  && document.getElementById('workOrderNumberSearch').value=="") {
				dom.get("updateDLP_error").innerHTML='<s:text name="plannedEstimateReport.validation.error"/>';
				dom.get("updateDLP_error").style.display='';
	        	return false;
	}
	if(document.getElementById('projectCodeSearch').value!="" && dom.get("projectCodeId").value == ""){
		dom.get("updateDLP_error").innerHTML='<s:text name="dlp.search.invalid.value.selection"/>';
		dom.get("updateDLP_error").style.display='';
    	return false;
	}
	if(document.getElementById('estimateNumberSearch').value!="" && dom.get("estimateId").value == ""){
		dom.get("updateDLP_error").innerHTML='<s:text name="dlp.search.invalid.value.selection"/>';
		dom.get("updateDLP_error").style.display='';
    	return false;
	}
	if(document.getElementById('workOrderNumberSearch').value!="" && dom.get("workOrderId").value == ""){
		dom.get("updateDLP_error").innerHTML='<s:text name="dlp.search.invalid.value.selection"/>';
		dom.get("updateDLP_error").style.display='';
    	return false;
	}
	if(document.getElementById("workOrderNumberSearch").value != "" && (document.getElementById('projectCodeSearch').value=="" && document.getElementById('estimateNumberSearch').value=="")){
			dom.get("updateDLP_error").innerHTML='<s:text name="dlp.validate.workorder.number.search"/>';
			dom.get("updateDLP_error").style.display='';
        	return false;
	}
	else {
		document.getElementById("updateDLP_error").innerHTML='';
		document.getElementById("updateDLP_error").style.display="none";
	}
	return true;
}

function setValues(obj){
	var currRow=getRow(obj);
	var billRegisterId = getControlInBranch(currRow,'billRegisterId');
    var billDate =  getControlInBranch(currRow,'billDate');
    var multipleProjCodes =  getControlInBranch(currRow,'multipleProjCodesPresent');
    
	dom.get("billId").value = billRegisterId.value;
	dom.get("partBillDate").value = billDate.value;
	dom.get("areMultipleProjCodesPresentForVoucher").value = multipleProjCodes.value;
	if(multipleProjCodes.value != 'yes'){
		document.getElementById("updateButton").value = 'Make bill as Final Bill';
	}
	else{
		document.getElementById("updateButton").value = 'Update DLP';
	}
}

function clearHiddenProjCodeId(obj)
{
	if(obj.value=="")
	{
		document.getElementById("projectCodeId").value="";
	}	
}
function clearHiddenEstimateId(obj)
{
	if(obj.value=="")
	{
		document.getElementById("estimateId").value="";
	}	
}
function clearHiddenWOId(obj)
{
	if(obj.value=="")
	{
		document.getElementById("workOrderId").value="";
	}	
}

function showDLPTable(){
	<s:if test="%{finalBillPresent!='yes' && isLegacyProjectCode=='yes' && mode!='edit'}">
		var billId = dom.get("billId").value;
	
		if(billId == ''){
			dom.get("updateDLP_error").innerHTML='<s:text name="dlp.search.result.marking.finalBill.selection.mandatory"/>';
			dom.get("updateDLP_error").style.display='';
	    	return false;
		}
		var billDate = dom.get("partBillDate").value;
		var latestBillDate =  '<s:date name="latestPartBillDate" format="dd/MM/yyyy"/>';
		var mutlipleProjCodes = dom.get("areMultipleProjCodesPresentForVoucher").value;

		if(mutlipleProjCodes != 'yes'){
			if(compareDate(billDate,latestBillDate) == 1){
				dom.get("updateDLP_error").innerHTML='<s:text name="dlp.part.bill.date.validate"/>';
				dom.get("updateDLP_error").style.display='';
				return false;
			}
		}
	</s:if>
		document.getElementById("dlpDataTable").style.display='block';
		document.getElementById("saveCloseBtnsDiv").style.display='block';
		
		<s:if test="%{isLegacyProjectCode=='yes'}">
			document.getElementById("dlpDateFieldsForLegacyTR").style.display = 'block';
			if(dom.get("estimateNo").value =='' && dom.get("workOrderNo").value ==''){
				document.getElementById("estimateAndWONumberTR").style.display = 'block';
			}
			
			if(dom.get("estimateNo").value !='' && dom.get("workOrderNo").value ==''){
				document.getElementById("estimateAndWONumberTR").style.display = 'block';
				document.getElementById("estimateNoLabel").style.display = 'none';
				document.getElementById("estimateNoField").style.display = 'none';
			}
		</s:if>
		<s:if test="%{isLegacyProjectCode=='no'}">
			if(dom.get("estimateNo").value !='' && dom.get("workOrderNo").value !=''){
				document.getElementById("dlpDateFieldsForWorksTR").style.display='block';
			}
		</s:if>
	
}
function roundOffDLP()
{
	document.updateDefectLiabilityPeriodForm.defectLiabilityPeriod.value=roundTo(document.updateDefectLiabilityPeriodForm.defectLiabilityPeriod.value);
}
function viewEstimate(estId) {
	window.open("${pageContext.request.contextPath}/estimate/abstractEstimate!edit.action?id="+estId+"&sourcepage=search",'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes,resizable=yes');
}
function viewWO(id){
	window.open("${pageContext.request.contextPath}/workorder/workOrder!edit.action?id="+id+"&mode=search",'', 'height=650,width=980,scrollbars=yes,left=0,top=0,status=yes,resizable=yes');
}
function viewVoucher(id){
	window.open("${pageContext.request.contextPath}/../EGF/voucher/preApprovedVoucher!loadvoucherview.action?vhid="+id,'', 'height=650,width=980,scrollbars=yes,left=0,top=0,status=yes,resizable=yes');
}
function viewBill(id,woId,billNumber){
	window.open("${pageContext.request.contextPath}/contractorBill/contractorBill!edit.action?id="+id+"&workOrderId="+woId+"&billnumber="+billNumber+"&sourcepage=search",'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
}

function validateInput(){
	
	var dlp = document.getElementById("defectLiabilityPeriod").value;

	if(dlp == ''){
		dom.get("updateDLP_error").innerHTML='<s:text name="dlp.validate.dlp"/>';
		dom.get("updateDLP_error").style.display='';
    	return false;
	}
	if(dlp != '' && dlp <= 0 ){
		dom.get("updateDLP_error").innerHTML='<s:text name="defectLiabilityPeriod.validate"/>';
		dom.get("updateDLP_error").style.display='';
    	return false;
	}
	
	<s:if test="%{isLegacyProjectCode=='yes'}">
		var completionDate = document.getElementById("workCompletionDate").value;
		var currDate =  '<s:date name="currentDate" format="dd/MM/yyyy"/>'; 
		if(completionDate == ''){
			dom.get("updateDLP_error").innerHTML='<s:text name="dlp.validate.workCompletionDate"/>';
			dom.get("updateDLP_error").style.display='';
	    	return false;
		}

		if(compareDate(completionDate,currDate) != 1){
			dom.get("updateDLP_error").innerHTML='<s:text name="dlp.workCompletionDate.validate"/>';
			dom.get("updateDLP_error").style.display='';
	    	return false;
		}
	</s:if>
	var dlpRange = '<s:property value="%{maxDLPRange}"/>';
	if(dlp != '' && dlp > dlpRange){
		var msg = '<s:text name="dlp.validate.dlp.greater.than.five"/>';
			if(!confirmSave(msg)) {
				return false;
			}
			else {
				return true;
			}
	}
	return true;
}
function confirmSave(msg) {  
	var ans=confirm(msg + " ?");	
	if(ans) {
		return true;
	}
	else {
		return false;		
	}
}
function estimateNoUniqueCheck(obj){
	if(obj.value !="")
	{
		<s:if test="%{mode=='edit'}">
			var id = '<s:property value="%{model.id}" />';
			var mode = dom.get("mode").value;
			makeJSONCall(["Value"],'${pageContext.request.contextPath}/workorder/ajaxUpdateDLP!estimateNoUniqueCheck.action',{estimateNo:obj.value,id:id,mode:mode},estimateNoUniqueCheckSuccess,estimateNoUniqueCheckFailure) ;
		</s:if>	
		<s:else>
			makeJSONCall(["Value"],'${pageContext.request.contextPath}/workorder/ajaxUpdateDLP!estimateNoUniqueCheck.action',{estimateNo:obj.value},estimateNoUniqueCheckSuccess,estimateNoUniqueCheckFailure) ;
		</s:else>	
	}
}
estimateNoUniqueCheckSuccess = function(req,res){
	results=res.results;
	var checkResult='';

	 if(results != '') {
			checkResult =   results[0].Value;
	 }
			if(checkResult != '' && checkResult=='no'){
				
				dom.get("updateDLP_error").innerHTML='<s:text name="dlp.estimateNo.isunique" />';
			    dom.get("updateDLP_error").style.display='';
			    return false;
			}	
			else{
				if(dom.get("updateDLP_error").innerHTML==('<s:text name="dlp.estimateNo.isunique" />'))
				{
					dom.get("updateDLP_error").innerHTML='';
				    dom.get("updateDLP_error").style.display='none';
				}
			}
}

estimateNoUniqueCheckFailure= function(){
    dom.get("updateDLP_error").style.display='';
	document.getElementById("updateDLP_error").innerHTML='<s:text name="dlp.estimateNo.isunique.failure" />';
}

</script>
		
	<body >
	<div class="errorstyle" id="updateDLP_error"	style="display: none;"></div>
	<s:if test="%{hasErrors()}">
		<div class="errorstyle">
			<s:actionerror />
			<s:fielderror />
		</div>
	</s:if>
	<s:form action="updateDefectLiabilityPeriod" name="updateDefectLiabilityPeriodForm" id="updateDefectLiabilityPeriodForm" theme="simple"  >
	<s:token />
	<s:push value="model">
	<s:hidden id="pcCode" name="pcCode" value="%{pcCode}"/>
	<s:hidden id="estimateNo" name="estimateNo" value="%{estimateNo}"/>
	<s:hidden id="workOrderNo" name="workOrderNo" value="%{workOrderNo}"/>
	<s:hidden id="isLegacyProjectCode" name="isLegacyProjectCode" value="%{isLegacyProjectCode}"/>
	<s:hidden id="finalBillPresent" name="finalBillPresent" value="%{finalBillPresent}"/>
	<s:hidden id="billId" name="billId" value="%{billId}"/>
	<s:hidden id="partBillDate" name="partBillDate" value="%{partBillDate}"/>
	<s:hidden id="latestPartBillDate" name="latestPartBillDate" value="%{latestPartBillDate}"/>
	<s:hidden id="areMultipleProjCodesPresentForVoucher" name="areMultipleProjCodesPresentForVoucher" value="%{areMultipleProjCodesPresentForVoucher}"/>
	<s:hidden id="finalBillMultipleProjCodesCheck" name="finalBillMultipleProjCodesCheck" value="%{finalBillMultipleProjCodesCheck}"/>
	<s:hidden id="mode" name="mode" value="%{mode}"/>
	

	
		<div class="formmainbox">
		<div class="insidecontent">
		<div class="rbroundbox2">
		<div class="rbtop2"><div></div></div>
		<div class="rbcontent2">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr><td>&nbsp;</td></tr>
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
							<td class="whiteboxwk"><s:text name="projectCompletionReport.projectCode" />:</td>
							<td class="whitebox2wk">
	                					<div class="yui-skin-sam">
	        							<div id="projectCodeSearch_autocomplete">
                							<div>
	        									<s:textfield id="projectCodeSearch" name="searchProjectCode" value="%{searchProjectCode}" onBlur="clearHiddenProjCodeId(this)" cssClass="selectwk" />
	        									<s:hidden id="projectCodeId" name="projectCodeId" value="%{projectCodeId}"/>
	        								</div>
	        								<span id="projectCodeSearchResults"></span>
	        							</div>	
	        							</div>
	        							<s:if test="%{mode=='edit'}">
	        								<egov:autocomplete name="projectCodeSearch" width="20" 
		        								field="projectCodeSearch" url="ajaxUpdateDLP!searchProjectCode.action?mode=edit&" 
		        								queryQuestionMark="false" results="projectCodeSearchResults" 
		        								handler="projectCodeSearchSelectionHandler" queryLength="3"/>
	        							</s:if>
	        							<s:else>	
		        							<egov:autocomplete name="projectCodeSearch" width="20" 
		        								field="projectCodeSearch" url="ajaxUpdateDLP!searchProjectCode.action?" 
		        								queryQuestionMark="false" results="projectCodeSearchResults" 
		        								handler="projectCodeSearchSelectionHandler" queryLength="3"/>
	        							</s:else>
							</td> 
							<td class="whiteboxwk" ><s:text name="projectCompletionReport.estimate.number" />:</td>
												<td class="whitebox2wk">
														<div class="yui-skin-sam">
					        							<div id="estimateNumberSearch_autocomplete">
				                							<div>
					        									<s:textfield id="estimateNumberSearch" name="searchEstimateNumber" 
					        										value="%{searchEstimateNumber}" onBlur="clearHiddenEstimateId(this)" cssClass="selectwk" />
					        								<s:hidden id="estimateId" name="estimateId" value="%{estimateId}"/>
					        								</div>
					        								<span id="estimateNumberSearchResults"></span>
					        							</div>	
					        						</div>
					        						<s:if test="%{mode=='edit'}">
						        						<egov:autocomplete name="estimateNumberSearch" width="20" 
						        							field="estimateNumberSearch" url="ajaxUpdateDLP!searchEstimateNumber.action?mode=edit&" 
						        							queryQuestionMark="false" results="estimateNumberSearchResults" 
						        							handler="estimateNumberSearchSelectionHandler" queryLength="3"/>
					        						</s:if>
					        						<s:else>
					        							<egov:autocomplete name="estimateNumberSearch" width="20" 
					        							field="estimateNumberSearch" url="ajaxUpdateDLP!searchEstimateNumber.action?" 
					        							queryQuestionMark="false" results="estimateNumberSearchResults" 
					        							handler="estimateNumberSearchSelectionHandler" queryLength="3"/>
					        						</s:else>
							</td>
													
						</tr>
						
						<tr>
							<td class="greyboxwk"><s:text name="workorder.search.workordernumber" />:</td>
							<td class="greybox2wk" >
						        						<div class="yui-skin-sam">
						        							<div id="workOrderNumberSearch_autocomplete">
					                							<div> 
						        									<s:textfield id="workOrderNumberSearch" name="searchWorkOrderNumber" value="%{searchWorkOrderNumber}"  onBlur="clearHiddenWOId(this)" cssClass="selectwk" />
						        									<s:hidden id="workOrderId" name="workOrderId" value="%{workOrderId}"/>
						        								</div>
						        								<span id="workOrderNumberSearchResults"></span>
						        							</div>		
						        						</div>
						        						<s:if test="%{mode=='edit'}">
							        						<egov:autocomplete name="workOrderNumberSearch" width="20" field="workOrderNumberSearch" 
								        						url="ajaxUpdateDLP!searchWorkOrderNumber.action?mode=edit&" queryQuestionMark="false" results="workOrderNumberSearchResults" 
								        						handler="workOrderNumberSearchSelectionHandler" queryLength="3" />
						        						</s:if>
						        						<s:else>
						        							<egov:autocomplete name="workOrderNumberSearch" width="20" field="workOrderNumberSearch" 
								        						url="ajaxUpdateDLP!searchWorkOrderNumber.action?" queryQuestionMark="false" results="workOrderNumberSearchResults" 
								        						handler="workOrderNumberSearchSelectionHandler" queryLength="3" />
						        						</s:else>
							</td>
							<td class="greyboxwk" colspan="2" />
						
						</tr>
						
						<tr>
							<td colspan="4" class="shadowwk"></td>
						</tr>
					
						<tr>
							<td colspan="4">
								<div class="buttonholdersearch">
								    <s:submit cssClass="buttonadd" value="SEARCH" id="saveButton" name="button" onclick="return validateSearch();" method="searchResult" /> &nbsp;&nbsp;&nbsp;
								    <input type="button" class="buttonfinal" value="CLEAR" id="clearButton" name="button"
			  						 onclick="this.form.reset();"/>
							
								    <input type="button" class="buttonfinal" value="CLOSE" id="closeButton" name="button" onclick="window.close();" />
								</div>
							</td>
						</tr>
						
						<tr><td colspan="4">&nbsp;</td></tr>
				</table>
			</td>
		</tr>
		</table>

			<s:if test="%{isLegacyProjectCode=='yes'}">
					<%@ include file="updateDefectLiabilityPeriod-legacyResults.jsp"%>
			</s:if>
			<s:else>
					<%@ include file="updateDefectLiabilityPeriod-worksResults.jsp"%>
			</s:else>
			<br />
			<s:if test="%{resultantList.size() != 0}">
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td colspan="4">
								<div class="buttonholdersearch">
								<s:if test="%{mode=='edit'}">
									<input type="button" class="buttonfinal" value="Modify DLP" id="updateButton" name="button" onclick="showDLPTable();" />
								</s:if>
								<s:else>
									<input type="button" class="buttonfinal" value="Update DLP" id="updateButton" name="button" onclick="showDLPTable();" />
								</s:else>
									 
								</div>
							</td>
						</tr>
				</table>
				<br />
				<%@ include file="updateDefectLiabilityPeriod-details.jsp"%>
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td colspan="4">
								<div id="mandatary" align="right" class="mandatory"
									style="font-size: 11px; padding-right: 20px;">
									*
									<s:text name="message.mandatory" />
								</div>
								<div class="buttonholdersearch" id="saveCloseBtnsDiv" style="display: none;">
									<s:submit cssClass="buttonadd" value="SAVE" id="saveBtn" name="saveBtn" method="save" onclick="return validateInput();"/>&nbsp;&nbsp;&nbsp;
									<input type="button" class="buttonfinal" value="CLOSE" id="closeButton2" name="button" onclick="window.close();" />
								</div>
							</td>
						</tr>
				</table>
			</s:if>
			<br />
		</div>
	</div>
	</div>
	</div>	
</s:push>									
</s:form>
</body>
</html>