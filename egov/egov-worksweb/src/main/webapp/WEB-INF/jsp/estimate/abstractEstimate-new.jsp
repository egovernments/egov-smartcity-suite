<!-- -------------------------------------------------------------------------------
# eGov suite of products aim to improve the internal efficiency,transparency,
#    accountability and the service delivery of the government  organizations.
# 
#     Copyright (C) <2015>  eGovernments Foundation
# 
#     The updated version of eGov suite of products as by eGovernments Foundation
#     is available at http://www.egovernments.org
# 
#     This program is free software: you can redistribute it and/or modify
#     it under the terms of the GNU General Public License as published by
#     the Free Software Foundation, either version 3 of the License, or
#     any later version.
# 
#     This program is distributed in the hope that it will be useful,
#     but WITHOUT ANY WARRANTY; without even the implied warranty of
#     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#     GNU General Public License for more details.
# 
#     You should have received a copy of the GNU General Public License
#     along with this program. If not, see http://www.gnu.org/licenses/ or
#     http://www.gnu.org/licenses/gpl.html .
# 
#     In addition to the terms of the GPL license to be adhered to in using this
#     program, the following additional terms are to be complied with:
# 
# 	1) All versions of this program, verbatim or modified must carry this
# 	   Legal Notice.
# 
# 	2) Any misrepresentation of the origin of the material is prohibited. It
# 	   is required that all modified versions of this material be marked in
# 	   reasonable ways as different from the original version.
# 
# 	3) This license does not grant any rights to any user of the program
# 	   with regards to rights under trademark law for use of the trade names
# 	   or trademarks of eGovernments Foundation.
# 
#   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
#------------------------------------------------------------------------------- -->
<%@ include file="/includes/taglibs.jsp" %> 
<html>
<title><s:text name='page.title.estimate'/></title>
<body onload="onPageLoad()" onpageshow="if(event.persisted) noBack();" onunload="" class="yui-skin-sam">
<script src="<egov:url path='resources/js/works.js'/>"></script>
<script src="../resources/js/jquery-1.7.2.min.js"></script>
<script>
var jq = jQuery.noConflict(true);

window.history.forward(1);
function noBack() {
	window.history.forward(); 
}

function onPageLoad() {
	showHideMap();
	setCurrentdate();
	<s:if test="%{sourcepage!='search' && getNextAction()!='END'}">
		loadDesignationFromMatrix();
	</s:if>
	noBack();
}

designationLoadHandler = function(req,res){  
  results=res.results;
  dom.get("designation").value=results[0].Designation;
}

designationLoadFailureHandler= function(){
    dom.get("errorstyle").style.display='';
	document.getElementById("errorstyle").innerHTML='Unable to load designation';
}

function showHideMap()
{
	var lat = document.getElementById("latitude").value ;
	var lon = document.getElementById("longitude").value ;
	if(lat!='' && lon!='')
	{
		document.getElementById("latlonDiv").style.display="";
	}
	else
	{
		document.getElementById("latlonDiv").style.display="none";
		var status = '<s:property value="%{egwStatus.code}" />';
		if(status==null || status=='' || status =='NEW' || status=='REJECTED')
		{
			return;
		}
		else
		{
			document.getElementById("mapAnchor").style.display="none";
			document.getElementById("mapAnchor").onclick=function(){return false;};
		}	
	}		
}

function hideSORTab(){
  document.getElementById('estimate_sor').style.display='none';
  document.getElementById('baseSORTable').style.display='none';
  document.getElementById('sorHeaderTable').style.display='none';
  document.getElementById('sorTable').style.display='none';
  document.getElementById('nonSorHeaderTable').style.display='none';
  document.getElementById('nonSorTable').style.display='none';
}

function showSORTab(){
  clearMessage('sor_error')
  if(dom.get('estimateDate').value==''){
  	 showMessage('sor_error','Fill in the estimate date before adding work details');
  }
  document.getElementById('estimate_sor').style.display='';
  document.getElementById('baseSORTable').style.display='';
  document.getElementById('sorHeaderTable').style.display='';
  document.getElementById('sorTable').style.display='';
  document.getElementById('nonSorHeaderTable').style.display='';
  document.getElementById('nonSorTable').style.display='';
  document.getElementById('sorTab').setAttribute('class','Active');
  document.getElementById('sorTab').setAttribute('className','Active');   
  hideHeaderTab();
  hideOverheadsTab();
  hideAssetTab();
  setCSSClasses('assetTab','Last');
  setCSSClasses('sorTab','Active');
  setCSSClasses('headerTab','First BeforeActive');
  setCSSClasses('overheadsTab','');
  disableTables();
}

function showHeaderTab(){
  var hiddenid = document.forms[0].id.value;
  document.getElementById('estimate_header').style.display='';
  setCSSClasses('assetTab','Last');
  setCSSClasses('sorTab','');
  setCSSClasses('headerTab','First Active');
  setCSSClasses('overheadsTab','');
  hideSORTab();
  hideOverheadsTab();
  hideAssetTab();
  disableTables();
}

function hideHeaderTab(){
  document.getElementById('estimate_header').style.display='none';
}

function showOverheadsTab(){
    hideSORTab();
    hideHeaderTab();
    hideAssetTab();
    clearMessage('overheads_error');
  	if(dom.get('estimateDate').value==''){
  	 	showMessage('overheads_error','Fill in the estimate date before adding over head details');
  }
    document.getElementById('estimate_overheads').style.display='';
    setCSSClasses('headerTab','First');
    setCSSClasses('sorTab','BeforeActive');
    setCSSClasses('overheadsTab','Active');
	setCSSClasses('assetTab','Last');
	document.getElementById('overheadsHeaderTable').style.display='';
    document.getElementById('overheadTable').style.display='';
    disableTables();    
}

function showAssetTab(){
    hideSORTab();
    hideHeaderTab();
    hideOverheadsTab();
    clearMessage('asset_error');
    var elem = document.getElementById('type');
  	if(elem.options[elem.selectedIndex].value=='-1'){
  	 	showMessage('asset_error','Select the nature of work before adding asset details');
  }
    document.getElementById('estimate_asset').style.display='';
    setCSSClasses('headerTab','First');
    setCSSClasses('sorTab','');
    
    setCSSClasses('overheadsTab','BeforeActive');
    setCSSClasses('assetTab','Last Active ActiveLast');
    
   
	document.getElementById('assetsHeaderTable').style.display='';
    document.getElementById('assetTable').style.display='';
	setAssetTableMessage();
	disableTables();    
	makeAssetClickable();
}

/**
 * This function makes Asset code clickable for search screens and
 * estimates in workflow.
 */
function makeAssetClickable(){
	var inputTags=document.getElementById("assetTable").getElementsByTagName('input');
		for(var i=2 ;i<=inputTags.length; i+=3){
			inputTags[i].disabled=false;
			inputTags[i].readonly=true;
		}
}

function hideAssetTab(){
  document.getElementById('assetsHeaderTable').style.display='none';
  document.getElementById('assetTable').style.display='none';
  document.getElementById('estimate_asset').style.display='none';
}

function setCSSClasses(id,classes){
    document.getElementById(id).setAttribute('class',classes);
    document.getElementById(id).setAttribute('className',classes);

}
function hideOverheadsTab(){
  document.getElementById('overheadsHeaderTable').style.display='none';
  document.getElementById('overheadTable').style.disply='none';
  dom.get("overheads_error").style.display='none'
}

function validateDataBeforeSubmit(abstractEstimateForm) {
	setupDocNumberBeforeSave();
    return validateHeaderBeforeSubmit(abstractEstimateForm) && validateMultiYearEstimateForm();
}

function enableFields(){
	for(i=0;i<document.abstractEstimateForm.elements.length;i++){
	        document.abstractEstimateForm.elements[i].disabled=false;
	}   
	setAssetStatusHiddenField();
}

function refreshInbox() {
		//TODO:Fixme - commented out for time being
       /* var x=opener.top.opener;
        if(x==null){
            x=opener.top;
        }
        x.document.getElementById('inboxframe').contentWindow.egovInbox.from = 'Inbox';
	    x.document.getElementById('inboxframe').contentWindow.egovInbox.refresh();*/
}

function setCurrentdate(){
	
    <s:if test="%{sourcepage!='search'}">
    	//TODO:Fixme - commented out for time being
		//populateDesignation();
	</s:if>	
	var estdate=document.getElementById('estimateDate').value;
	if(estdate=='') {
		document.getElementById('estimateDate').value='<%=new java.text.SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date())%>';
	}else{	
		document.getElementById('estimateDate').value=estdate;
	}		
}

function validateCancel() {
	var msg='<s:text name="estimate.cancel.confirm"/>';
	var estNo='<s:property value="model.estimateNumber"/>';
	if(!confirmCancel(msg,estNo)) {
		return false;
	}
	else {
		return true;
	}
}
function viewDocument(){
	  viewDocumentManager(dom.get("docNumber").value); 
}
function onSubmit() {
	action = document.getElementById("workFlowAction").value;
	return validate(action);
}
function validate(text){
	<s:if test="%{model.id==null || model.egwStatus.code=='NEW' || model.egwStatus.code=='REJECTED'}">
		if(text=='Forward'){
			if(document.getElementById("estimateValue").value!=''){
				var estmValue=document.getElementById("estimateValue").value;
				var parts=estmValue.split(".");
				if(parts.length>1){
					if(parts[1]>0){
						dom.get("worktypeerror").style.display='';
				        dom.get("worktypeerror").innerHTML='<s:text name="abstractEstimate.estimateValue.warningMessgae" />';
				        window.scroll(0,0);
				        return false;
					}
				}
			}
		}
	</s:if>
	var forms = document.getElementsByName('abstractEstimateForm');
	
	if(!validateDataBeforeSubmit(forms[0]))
		return false;

	if(text != 'Reject' || text != 'Cancel') {
		if (!validateNonSorUomDropDown()) {
			return false;	
		}
	}
	/*if(!validateUser(text))
		return false;*/
	enableFields();
	//setDocumentValues();
	document.abstractEstimateForm.action='${pageContext.request.contextPath}/estimate/abstractEstimate-save.action';
	document.abstractEstimateForm.submit();
	return true;
}


jq(document).on('click', '#wpView', function(){
	var wpId = jq(this).attr("data-wpId");
    var url="${pageContext.request.contextPath}/tender/worksPackage-edit.action?id="+wpId+"&sourcepage=search";
    window.open(url,'','height=650,width=980,scrollbars=yes,status=yes');
});

jq(document).on('click', '#woView', function(){
	var woId = jq(this).attr("data-woId");
	var url = "${pageContext.request.contextPath}/workorder/workOrder-edit.action?id="+woId+"&mode=search";
	window.open(url,'', 'height=650,width=980,scrollbars=yes,status=yes');
});

</script>

<div id="worktypeerror" class="errorstyle" style="display:none;"></div>
    <s:if test="%{hasErrors()}">
        <div id="errorstyle" class="errorstyle" >
          <s:actionerror/>
          <s:fielderror/>
        </div>
    </s:if>
    <s:if test="%{hasActionMessages()}">
        <div class="messagestyle">
        	<s:property value="%{estimateNumber}"/> &nbsp; <s:actionmessage theme="simple"/>
        	
        </div>
    </s:if>
      <s:form theme="simple" name="abstractEstimateForm" onsubmit="return validateDataBeforeSubmit(this);">
       <s:if test="%{sourcepage!='search'}">
      	<s:token name="%{tokenName()}"/>
      </s:if>
<s:push value="model">
<s:if test="%{model.estimateNumber!=null}">
	<s:hidden name="id"/>
	</s:if>
<s:hidden name="mode" id="mode"/>
<s:hidden name="isAllowEstDateModify" id=""/> 
<div class="formmainbox"><div class="insidecontent">
  <div class="rbroundbox2">
	<div class="rbtop2"><div></div></div>
	  <div class="rbcontent2"><div class="datewk">
	  <s:if test="%{not model.projectCode}">
	       <div class="estimateno">Estimate No: <s:if test="%{not model.estimateNumber}">&lt; Not Assigned &gt;</s:if><s:property value="model.estimateNumber" /></div>
	  </s:if>
	  <s:else>
	       <div class="estimateno">
	       Estimate No:  <s:property value="model.estimateNumber" /> </div>
	       <div class="estimateno" style="text-align: right"> 
	       Project Code: <s:property value="model.projectCode.code" /> </div>
	  </s:else>
	 <!-- <span class="bold"><s:text name="message.today" /></span> <egov:now/>--></div>
	 <s:if test="%{model.projectCode}">
	 	<div class="datewk" style="position: relative;">
	 		<s:if test="%{wpDetails.size!=0}">
	 			<div class="estimateno" style="padding-top: 10px;width:10%;"> <s:text name="label.estimate.works.package" />: </div>
	 			<div class="estimateno" style="padding-top: 10px;width: 22%;white-space: wrap;">
	 				<s:iterator value="wpDetails" var="wpDetails" status="wpStatus"> 
			 			<a href="javascript:void(0)" id="wpView" data-wpId='<s:property value="#wpDetails[0]"/>'><s:property value="%{#wpDetails[1]}"/></a>
			 			<s:if test="!#wpStatus.last">,</s:if>
			 	 	</s:iterator>
			 	</div>
		 	 </s:if>
		 	 <s:if test="%{woDetails.size!=0}">
		 		<div class="estimateno" style="padding-top: 10px;text-align: right;width:8%;"><s:text name="label.estimate.work.order" />: </div>
		 	 	<div class="estimateno" style="padding-top: 10px;padding-left:5px; width:25%;white-space: wrap;">
		 	 		<s:iterator value="woDetails" var="woDetails" status="woStatus">
						 <a href="javascript:void(0)" id="woView" data-woId='<s:property value="#woDetails[0]"/>'><s:property value="%{#woDetails[1]}"/></a>
						 <s:if test="!#woStatus.last">,</s:if>
		 	 		</s:iterator>
		 	 	</div>
		 	 </s:if>
		 	 <s:if test="%{wpDetails.size!=0 || woDetails.size!=0}">
		 	 	 <div class="estimateno" style="position: absolute; right:0px;top:26px;text-align: right;width: 33%">
			 		 <s:text name="label.estimate.payments.released" />: <s:property value="paymentReleased"/>
		 	 	 </div>
		 	 </s:if>
		 	 <s:elseif  test="%{paymentReleased != 0.0}">
		 	 	<div class="estimateno" style="padding-top: 10px;">
			 		 <s:text name="label.estimate.payments.released" />:<s:property value="paymentReleased"/>
		 	 	</div>
		 	 </s:elseif>
	  	</div>
	  </s:if>
<s:hidden name="model.documentNumber" id="docNumber" />

	    <table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td><div id="header">
				<ul id="Tabs">
					<li id="headerTab" class="First Active"><a id="header_1" href="#" onclick="showHeaderTab();">Header</a></li>
		 			<li id="sorTab" class=""><a id="header_2" href="#" onclick="showSORTab();">Work Details</a></li>					
					<li id="overheadsTab" class="Befor"><a id="header_3" href="#" onclick="showOverheadsTab();">Overheads</a></li>
					<li id="assetTab" class="Last"><a id="header_4" href="#" onclick="showAssetTab();">Asset Info</a></li>
				</ul>
            </div></td>
          </tr>
      	<tr><td>&nbsp;</td></tr>
           <tr>
            <td>
            <div id="estimate_overheads" style="display:none;">
                 <%@ include file="estimate-overheads.jsp"%>                
            </div>
            </td>
          </tr>
          <tr>
            <td>
            <div id="estimate_asset" style="display:none;">
                 <%@ include file="estimate-asset.jsp"%>                
            </div>
            </td>
          </tr>      
          <tr>
            <td>
            <div id="estimate_header">
            <%@ include file="estimate-header.jsp"%>            
			<br />	
            <%@ include file="estimate-multiYearEstimate.jsp"%>  
            </div>            
            </td> 
          </tr>            
          <tr>
            <td>
            <div id="estimate_sor" style="display:none;"> 
            	<%@ include file="estimate-template.jsp"%> 
                <%@ include file="estimate-sor.jsp"%>            
            	<%@ include file="estimate-nonSor.jsp"%>
            </div>
            </td>
          </tr>
        <tr>
            <td><table width="100%" align="center" border="0" cellspacing="0" cellpadding="0">
            <tr>
	            <td width="17%" class="whiteboxwk"><s:text name="estimate.value" />:</td>
                <td width="17%" class="whitebox2wk"><s:textfield name="estimateValue" value="%{estimateValue}"  id="estimateValue" cssClass="selectamountwk" readonly="true" align="right" tabindex="-1" />
              </td>
	            <td class="whiteboxwk">&nbsp;</td>
	            <td class="whiteboxwk">&nbsp;</td>
            </tr>            
            </table></td>
          </tr>
          
          <tr><td>&nbsp;</td></tr>
          <!-- TODO:Fixeme - Commented out for time being Need to implement new workflow which is based on matrix and correspodning jsp needs to be included here -->
	  	<tr> 
		    <td>
		    <div id="manual_workflow">
		    <s:if test="%{sourcepage!='search'}">
				<%@ include file="../workflow/commonWorkflowMatrix.jsp"%> 
				<%@ include file="../workflow/commonWorkflowMatrix-button.jsp"%>
		         <!--%@ include file="workflowApproval.jsp"% -->   
		  </s:if>
		    </div>
		    </td>
          </tr>
          <tr>
          	<td colspan="4" class="shadowwk"> </td>                  
          </tr>          
                   
         <tr>
            <td><div align="right" class="mandatory" style="font-size:11px;padding-right:20px;">* <s:text name="message.mandatory" /></div></td>
          </tr>
        </table>
        <div class="rbbot2"><div></div></div>
      </div>     
	
</div>
  </div>
</div>
<div class="buttonholderwk">
<input type="hidden" name="actionName" id="actionName"/> 
<!-- Action buttons have to displayed only if the page is directed from the inbox -->	
<s:if test="%{(hasErrors() || sourcepage=='inbox' || model.egwStatus==null || model.egwStatus.code=='NEW' 
|| model.egwStatus.code=='REJECTED') && (sourcepage=='inbox' || model.egwStatus==null || hasErrors())}">
<!-- TODO:Fixeme - hard coded save button for time being till we implement common workflow -->
<!--<input type="submit" class="buttonfinal" value="SAVE" id="save" name="save" onclick="document.abstractEstimateForm.actionName.value='save';return validate('save');" />	  
<input type="submit" class="buttonfinal" value="SAVE & SUBMIT" id="submit_for_approval" name="submit_for_approval" onclick="document.abstractEstimateForm.actionName.value='submit_for_approval';return validate('submit_for_approval');" />		
	<s:iterator value="%{validActions}"> 
	  <s:if test="%{description!=''}">
	  	<s:if test="%{description=='CANCEL' && model.estimateNumber!=null}">
			<s:submit type="submit" cssClass="buttonfinal" value="%{description}" id="%{name}" name="%{name}" method="cancel" onclick="return validateCancel();document.abstractEstimateForm.actionName.value='%{name}'"/>
	  	</s:if>
	    <s:else>
	  	  <s:submit type="submit" cssClass="buttonfinal" value="%{description}" id="%{name}" name="%{name}" method="moveEstimate" onclick="document.abstractEstimateForm.actionName.value='%{name}';return validate('%{name}');"/>	  	  
	  </s:else>
	  </s:if>
	</s:iterator> -->
	<s:if test="%{(model.egwStatus.code=='TECH_SANCTIONED' || model.egwStatus.code=='REJECTED') && 
	(model.currentState.nextAction=='Pending Budgetary Appropriation' || model.currentState.nextAction=='Pending Deposit Code Appropriation') }">
		 	<input type="button" onclick="window.open('${pageContext.request.contextPath}/estimate/financialDetail!add.action?estimateId=<s:property value='%{model.id}'/>&sourcepage=<s:property value='%{sourcepage}'/>&source=UpdateFinancialDetail', '_self');" class="buttonadd" value="Update Financial Details " id="updateFinancialDetailButton" name="updateFinancialDetailButton"/>
  </s:if>
</s:if>
<%-- 
<s:if test="%{model.id==null}">
	  <input type="button" class="buttonfinal" value="CLEAR" id="button" name="button" onclick="window.open('${pageContext.request.contextPath}/estimate/abstractEstimate-newform.action','_self');"/>
</s:if> --%>
<%-- <s:if test="%{sourcepage!='search'}">
	<input type="button" class="buttonfinal" value="CLOSE" id="closeButton" name="closeButton" onclick="confirmClose('<s:text name='estimate.close.confirm'/>');"/>
</s:if> --%>
<s:if test="%{sourcepage=='search'}" >
	<input type="button" class="buttonfinal" value="CLOSE" id="closeButton" name="closeButton" onclick="window.close();"/>
</s:if>
<s:if test="%{model.id!=null && model.estimateNumber!=null}">
  	<input type="button" onclick="window.open('${pageContext.request.contextPath}/estimate/abstractEstimatePDF.action?estimateID=<s:property value='%{model.id}'/>');" class="buttonpdf" value="VIEW PDF" id="pdfButton" name="pdfButton"/>
  	<input type="button" onclick="window.open('${pageContext.request.contextPath}/estimate/abstractEstimate-viewBillOfQuantitiesXls.action?sourcepage=boqPDF&id=<s:property value='%{model.id}'/>');" class="buttonpdf" value="VIEW BOQ XLS" id="BOQxlsButton" name="BOQxlsButton"/>
  </s:if>
  <!-- TODO:Fixeme - Commented out for time being. Need to replace with new file upload feature -->
 <!-- <s:if test="%{sourcepage=='search' || (sourcepage=='inbox' && (model.egwStatus.code=='ADMIN_SANCTIONED' || model.egwStatus.code=='CANCELLED'))}">
  	<input type="submit" class="buttonadd" value="View Document" id="docViewButton" onclick="viewDocument();return false;" />
  </s:if>
  <s:else>
  	<input type="submit" class="buttonadd" value="Upload Document" id="docUploadButton" onclick="showDocumentManager();return false;" />
  </s:else> --> 
 <s:if test="%{(model.egwStatus.code=='TECH_SANCTIONED' && (model.currentState.nextAction!='Pending Budgetary Appropriation' 
 || model.currentState.nextAction=='Pending Deposit Code Appropriation')) || 
 
 (model.egwStatus.code=='RESUBMITTED' && (model.currentState.nextAction=='Pending Budgetary Appropriation Check' || model.currentState.nextAction=='Pending Budgetary Appropriation Approval' 
 || model.currentState.nextAction=='Pending Deposit Code Appropriation Check' || model.currentState.nextAction=='Pending Deposit Code Appropriation Approval')) || 
 
 (model.egwStatus.code=='RESUBMITTED' && model.currentState.nextAction=='Pending Admin Sanction Check') || 
 
 (model.egwStatus.code=='ADMIN_SANCTIONED' || model.egwStatus.code=='BUDGETARY_APPR_CHECKED' || 
 model.egwStatus.code=='ADMIN_CHECKED' || model.egwStatus.code=='BUDGETARY_APPROPRIATION_DONE' || 
 model.egwStatus.code=='DEPOSIT_CODE_APPR_CHECKED' || model.egwStatus.code=='DEPOSIT_CODE_APPR_DONE')}">
  	<input type="button" onclick="window.open('${pageContext.request.contextPath}/estimate/financialDetail!add.action?estimateId=<s:property value='%{model.id}'/>', '', 'height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');" class="buttonadd" 
  	value="View Financial Details" id="financialDetailButton" name="financialDetailButton"/>
    <!--  for View Budget Folio  -->
   
</s:if>
<s:if test="%{model.egwStatus!=null && model.currentState.previous!=null && (model.egwStatus.code=='TECH_SANCTIONED' && 
  (model.currentState.nextAction!='Pending Budgetary Appropriation' || model.currentState.nextAction=='Pending Deposit Code Appropriation')) || 
  
 (model.egwStatus.code=='RESUBMITTED' && (model.currentState.nextAction=='Pending Admin Sanction' || 
 model.currentState.nextAction=='Pending Budgetary Appropriation Check' || model.currentState.nextAction=='Pending Budgetary Appropriation Approval' || 
 model.currentState.nextAction=='Pending Deposit Code Appropriation Check' || model.currentState.nextAction=='Pending Deposit Code Appropriation Approval')) || 
 
 (model.egwStatus.code=='ADMIN_SANCTIONED' || model.egwStatus.code=='BUDGETARY_APPR_CHECKED' || 
 model.egwStatus.code=='ADMIN_CHECKED' || model.egwStatus.code=='BUDGETARY_APPROPRIATION_DONE' ||
 model.egwStatus.code=='DEPOSIT_CODE_APPR_CHECKED' || model.egwStatus.code=='DEPOSIT_CODE_APPR_DONE') }">
  <s:if test="%{appConfigValuesToSkipBudget.contains(model.type.name) }">
 	<input type="button" onclick="window.open('${pageContext.request.contextPath}/estimate/financialDetail!viewDepositWorksFolio.action?estimateId=<s:property value='%{model.id}'/>', '', 'height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');" class="buttonadd" value="View Deposit Folio" id="depositfolioreportButton" name="depositfolioreportButton"/>
 </s:if>
  <s:else> <input type="button" onclick="window.open('${pageContext.request.contextPath}/estimate/financialDetail!viewBudgetFolio.action?estimateId=<s:property value='%{model.id}'/>', '', 'height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');" class="buttonadd" 
     value="View Budget Folio" id="viewBudgetFolio" name="viewBudgetFolio"/>
 </s:else>   
	
 </s:if>
   <s:if test="%{sourcepage=='search'}">
   		<input type="button" onclick="window.open('${pageContext.request.contextPath}/estimate/abstractEstimate-workflowHistory.action?stateId=<s:property value='%{state.id}'/>', '', 'height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');" class="buttonfinal" value="History" id="history" name="History"/>
   </s:if>
</div>
</s:push>
</s:form>
<script>hideSORTab()</script>
   
<script>
    function disableTables(){
	    <s:if test="%{model.egwStatus.code!='NEW' && model.egwStatus.code!='REJECTED'}" >
	        multiYearEstimateDataTable.removeListener('cellClickEvent');
	        sorDataTable.removeListener('cellClickEvent');
	        nonSorDataTable.removeListener('cellClickEvent');
	        overheadsTable.removeListener('cellClickEvent');
	        assetsTable.removeListener('cellClickEvent');
	    </s:if>
    }
	<s:if test="%{model.id!=null && model.egwStatus.code!='NEW'}" >
	for(i=0;i<document.abstractEstimateForm.elements.length;i++){ 
		if(document.abstractEstimateForm.elements[i].id!='designation'){
		document.abstractEstimateForm.elements[i].disabled=true;
		document.abstractEstimateForm.elements[i].readonly=true;
		}
	} 
	<s:if test="%{model.egwStatus.code!='REJECTED'}">
			links=document.abstractEstimateForm.getElementsByTagName("a");
			for(i=0;i<links.length;i++){
					if(links[i].id!='mapAnchor')
					{
						if(links[i].id.indexOf("header_")!=0)
		     				links[i].onclick=function(){return false;};
					}	
				}
		</s:if> 
	</s:if> 

    <s:if test="%{(sourcepage=='search' && model.egwStatus.code!='NEW')|| (sourcepage=='inbox' && (model.egwStatus.code!='NEW' && model.egwStatus.code!='REJECTED') || (model.egwStatus.code!='NEW' && hasErrors())}" > 
            disableTables();
			for(i=0;i<document.abstractEstimateForm.elements.length;i++){
	        	document.abstractEstimateForm.elements[i].disabled=true;
				document.abstractEstimateForm.elements[i].readonly=true;
			}   
			document.abstractEstimateForm.pdfButton.readonly=false;
			document.abstractEstimateForm.pdfButton.disabled=false;
			document.abstractEstimateForm.BOQxlsButton.readonly=false;
			document.abstractEstimateForm.BOQxlsButton.disabled=false;
			if(document.abstractEstimateForm.closeButton != null) {
				document.abstractEstimateForm.closeButton.readonly=false;
				document.abstractEstimateForm.closeButton.disabled=false;
			}
			links=document.abstractEstimateForm.getElementsByTagName("a");
			for(i=0;i<links.length;i++){
		    	if(links[i].id.indexOf("header_")!=0)
     				links[i].onclick=function(){return false;};
			}
	</s:if>
	
	<s:if test="%{model.egwStatus.code=='REJECTED'}">
		enableFields();
	</s:if>
	<s:if test="%{model.egwStatus.code=='CREATED' || model.egwStatus.code=='RESUBMITTED'}"> 		
		disableTables()
		for(i=0;i<document.abstractEstimateForm.elements.length;i++){
        	document.abstractEstimateForm.elements[i].disabled=true;
			document.abstractEstimateForm.elements[i].readonly=true;
		}   
	</s:if>

	<s:if test="%{hasErrors() && model.egwStatus.code=='ADMIN_SANCTIONED'}"> 
		for(i=0;i<document.abstractEstimateForm.elements.length;i++){
			document.abstractEstimateForm.elements[i].disabled=true;
			document.abstractEstimateForm.elements[i].readonly=true;
		} 
		disableTables();
		document.abstractEstimateForm.pdfButton.readonly=false;
		document.abstractEstimateForm.pdfButton.disabled=false;
		document.abstractEstimateForm.BOQxlsButton.readonly=false;
		document.abstractEstimateForm.BOQxlsButton.disabled=false;
		if(document.abstractEstimateForm.closeButton != null) { 
			document.abstractEstimateForm.closeButton.readonly=false;
			document.abstractEstimateForm.closeButton.disabled=false;
		}
		if(document.abstractEstimateForm.financialDetailButton!=null){  
			document.abstractEstimateForm.financialDetailButton.readonly=false;
			document.abstractEstimateForm.financialDetailButton.disabled=false;		
		}
		
		if(document.abstractEstimateForm.viewBudgetFolio!=null){
			document.abstractEstimateForm.viewBudgetFolio.readonly=false;
			document.abstractEstimateForm.viewBudgetFolio.disabled=false;
		}	
		if(document.abstractEstimateForm.depositfolioreportButton!=null){
			document.abstractEstimateForm.depositfolioreportButton.readonly=false;
			document.abstractEstimateForm.depositfolioreportButton.disabled=false;
		}
	</s:if>  
     <s:if test="%{sourcepage=='inbox'}">
		     <s:if test="%{model.id!=null}">		
				var tempEstimateValue=Math.round(eval(document.getElementById("grandTotal").innerHTML)+eval(document.getElementById("nonSorGrandTotal").innerHTML)+eval(document.getElementById("overHeadTotalAmnt").innerHTML));
		 		document.getElementById("estimateValue").value=roundTo(tempEstimateValue);
			</s:if>		
			if(document.abstractEstimateForm.closeButton != null) {
	 			document.abstractEstimateForm.closeButton.readonly=false;
				document.abstractEstimateForm.closeButton.disabled=false;
			}	
			document.abstractEstimateForm.pdfButton.readonly=false;
			document.abstractEstimateForm.pdfButton.disabled=false;
			document.abstractEstimateForm.BOQxlsButton.readonly=false;
			document.abstractEstimateForm.BOQxlsButton.disabled=false;
			<s:if test="%{model.egwStatus.code!='NEW' && model.egwStatus.code!='REJECTED'}"> 		
				toggleFields(true,['approverDepartment','approverDesignation','approverPositionId','approverComments','Save',
			                     'Forward','Reject','button2','Approve']); 
            </s:if>
	     	if(document.abstractEstimateForm.tech_sanction){
				document.abstractEstimateForm.tech_sanction.readonly=false;
				document.abstractEstimateForm.tech_sanction.disabled=false;
			}
	     	if(document.abstractEstimateForm.reject){
				document.abstractEstimateForm.reject.readonly=false;
				document.abstractEstimateForm.reject.disabled=false;
	     	}
		
	       if(document.abstractEstimateForm.updateFinancialDetailButton){  	
				document.abstractEstimateForm.updateFinancialDetailButton.readonly=false;
				document.abstractEstimateForm.updateFinancialDetailButton.disabled=false;
			}	
	       if(document.abstractEstimateForm.financialDetailButton){  
				document.abstractEstimateForm.financialDetailButton.readonly=false;
				document.abstractEstimateForm.financialDetailButton.disabled=false;
	       }
			if(document.abstractEstimateForm.budget_appropriation){
				document.abstractEstimateForm.budget_appropriation.readonly=false;
				document.abstractEstimateForm.budget_appropriation.disabled=false;
			} 
			
			if(document.abstractEstimateForm.viewBudgetFolio!=null){
					document.abstractEstimateForm.viewBudgetFolio.readonly=false;
					document.abstractEstimateForm.viewBudgetFolio.disabled=false;
			}	
			 if(document.abstractEstimateForm.depositfolioreportButton!=null){
				document.abstractEstimateForm.depositfolioreportButton.readonly=false;
				document.abstractEstimateForm.depositfolioreportButton.disabled=false;
			 }	
			
	        if(document.abstractEstimateForm.admin_sanction){  		
				document.abstractEstimateForm.admin_sanction.readonly=false;
				document.abstractEstimateForm.admin_sanction.disabled=false;
			}
	 </s:if>
		
	<s:if test="%{model.id==null && model.egwStatus.code=='CREATED'}"> 
		document.abstractEstimateForm.cancel.visible=false;
		document.abstractEstimateForm.pdfButton.visible=false;
		document.abstractEstimateForm.BOQxlsButton.visible=false;
	</s:if>
	
	<s:if test="%{sourcepage=='search'}">          
        var tempEstimateValue=Math.round(eval(document.getElementById("grandTotal").innerHTML)+eval(document.getElementById("nonSorGrandTotal").innerHTML)+eval(document.getElementById("overHeadTotalAmnt").innerHTML));
        document.getElementById("estimateValue").value=roundTo(tempEstimateValue);
       
        //document.getElementById('docViewButton').style.visibility=''; 
        document.getElementById('history').style.visibility='';
        if(document.abstractEstimateForm.closeButton != null) {
	        document.abstractEstimateForm.closeButton.readonly=false;
			document.abstractEstimateForm.closeButton.disabled=false;
        }
		document.abstractEstimateForm.history.readonly=false;
		document.abstractEstimateForm.history.disabled=false;
		
       	bodyOnLoad();
        load(); 
        
        multiYearEstimateDataTable.removeListener('cellClickEvent');
        sorDataTable.removeListener('cellClickEvent');
        nonSorDataTable.removeListener('cellClickEvent');
        assetsTable.removeListener('cellClickEvent');
        overheadsTable.removeListener('cellClickEvent');      
  	</s:if>
  	
  	
    function bodyOnLoad() {
                
        for(var i=0;i<document.forms[0].length;i++) {
      		document.forms[0].elements[i].disabled =true;
      	}	
        if(document.abstractEstimateForm.closeButton != null) {
		    document.abstractEstimateForm.closeButton.readonly=false;
			document.abstractEstimateForm.closeButton.disabled=false;
        }
				
	    document.abstractEstimateForm.pdfButton.readonly=false;
		document.abstractEstimateForm.pdfButton.disabled=false;
		document.abstractEstimateForm.BOQxlsButton.readonly=false;
		document.abstractEstimateForm.BOQxlsButton.disabled=false;
		 document.abstractEstimateForm.history.readonly=false; 
		document.abstractEstimateForm.history.disabled=false;
		if(document.abstractEstimateForm.financialDetailButton)
			document.abstractEstimateForm.financialDetailButton.disabled=false;
		if(document.abstractEstimateForm.viewBudgetFolio)
			document.abstractEstimateForm.viewBudgetFolio.disabled=false;
    }
    function load(){
        links=document.abstractEstimateForm.getElementsByTagName("a");
		for(i=0;i<links.length;i++){
		   	if(links[i].id=='addnonSorRow')
		  	   links[i].onclick=function(){return false;};
		}
    }

	<s:if test="%{!isAllowEstDateModify}">
		document.abstractEstimateForm.estimateDate.readonly=true;
		document.abstractEstimateForm.estimateDate.disabled=true;
		document.getElementById('estDatePicker').onclick = function(){return false};
	</s:if>
	
	
</script>

	<script src="<c:url value='/resources/global/js/egov/inbox.js' context='/egi'/>"></script>
</body>

</html>
