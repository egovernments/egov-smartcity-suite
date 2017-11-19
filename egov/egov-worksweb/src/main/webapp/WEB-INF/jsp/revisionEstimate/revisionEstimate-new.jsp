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
<title><s:text name="revisionEstimate.label.title" /></title>
<body class="yui-skin-sam" onload="defaultApproverDept();noBack();" onpageshow="if(event.persisted) noBack();" onunload=""> 
<script src="<egov:url path='resources/js/works.js?${app_release_no}'/>"></script> 

<script>
window.history.forward(1);
function noBack() {
	window.history.forward(); 
}

	function defaultApproverDept() {
		if(document.getElementById('approverDepartment')!=null){
			document.getElementById('approverDepartment').value='<s:property value="%{logedInUserDept}"/>';
			loadDesignationFromMatrix();	
		}
	}
	
	function showRevEstHeaderTab(){
		document.getElementById('revEst_header').style.display='';
		setCSSClasses('revEstHeaderTab','First Active');
		setCSSClasses('OrginialEstDtlsTab','');
		setCSSClasses('RevEstNonTenderedItemsTab','');
		setCSSClasses('changeQtyDtlsTab','Last');
		hideOrginialEstDetailsTab();
		hideRevEstDetailsTab();
		hideChangeQtyDtlsTab();
	}

	function showOrginialEstDtlsTab(){
		document.getElementById('originalEst_details').style.display='';
		setCSSClasses('revEstHeaderTab','First BeforeActive');
		setCSSClasses('OrginialEstDtlsTab','Active');
		setCSSClasses('RevEstNonTenderedItemsTab','');
		setCSSClasses('changeQtyDtlsTab','Last');
		hideRevEstHeaderTab();
		hideRevEstDetailsTab();
		hideChangeQtyDtlsTab();
	}

	function showNonTenderedItemsTab(){
		document.getElementById('re_nonTenderedItems').style.display='';
		setCSSClasses('revEstHeaderTab','First');
		setCSSClasses('OrginialEstDtlsTab','BeforeActive');
		setCSSClasses('RevEstNonTenderedItemsTab','Active');
		setCSSClasses('changeQtyDtlsTab','Last');
		hideRevEstHeaderTab();
		hideOrginialEstDetailsTab();
  		document.getElementById('baseSORTable').style.display='';
  		document.getElementById('sorHeaderTable').style.display=''; 
  		document.getElementById('sorTable').style.display='';
  		document.getElementById('nonSorHeaderTable').style.display='';
  		document.getElementById('nonSorTable').style.display='';
  		hideChangeQtyDtlsTab();
	} 
	
	function setCSSClasses(id,classes){
    		 document.getElementById(id).setAttribute('class',classes);
   		     document.getElementById(id).setAttribute('className',classes);
	}
	function hideRevEstHeaderTab(){
			document.getElementById('revEst_header').style.display='none';
	}
	function hideOrginialEstDetailsTab(){
			document.getElementById('originalEst_details').style.display='none';
	}
	function hideRevEstDetailsTab(){
			document.getElementById('re_nonTenderedItems').style.display='none';
			document.getElementById('baseSORTable').style.display='none';
			document.getElementById('sorHeaderTable').style.display='none';
  			document.getElementById('sorTable').style.display='none';
  			document.getElementById('nonSorHeaderTable').style.display='none';
 			document.getElementById('nonSorTable').style.display='none';
	}

	function hideChangeQtyDtlsTab()
	{
		document.getElementById('changeQty_details').style.display='none';
		document.getElementById('changeQuantityHeaderTable').style.display='none';
	}
	
function showEstimateDetails(estimateId){
	window.open("${pageContext.request.contextPath}/estimate/abstractEstimate!edit.action?id="+estimateId+"&sourcepage=search",'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
}

function viewDocument(){
	  viewDocumentManager(dom.get("docNumber").value); 
}
function showChangeQtyDtlsTab(){
	document.getElementById('changeQty_details').style.display='';
	document.getElementById('changeQuantityHeaderTable').style.display='';
	document.getElementById('itemsTable').style.display='';
	setCSSClasses('revEstHeaderTab','First');
	setCSSClasses('OrginialEstDtlsTab','');
	setCSSClasses('RevEstNonTenderedItemsTab','BeforeActive');
	setCSSClasses('changeQtyDtlsTab','Last Active ActiveLast');
	hideOrginialEstDetailsTab();
	hideRevEstDetailsTab();
	hideRevEstHeaderTab();
}
	
</script>

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
<s:form  action="revisionEstimate" theme="simple" name="revisionEstimateForm" >
<div class="errorstyle" id="revisionEstimate_error" style="display:none;"></div>
<s:if test="%{sourcepage!='search'}">
 <s:token/>
 </s:if>
<s:push value="model">
<s:if test="%{model.estimateNumber!=null}">
	<s:hidden name="id" value="%{id}" id="id" />
	</s:if>
<s:hidden id="additionalRuleValue" name="additionalRuleValue" value="%{additionalRuleValue}" />
<s:hidden id="amountRuleValue" name="amountRuleValue" value="%{amountRuleValue}" />
<s:hidden id="originalEstimateId" name="originalEstimateId" value="%{originalEstimateId}" />
<s:hidden id="originalWOId" name="originalWOId" value="%{originalWOId}" />
<s:hidden id="workorderNo" name="workorderNo" value="%{workOrder.workOrderNumber}" />
<s:hidden id="sourcepage" name="sourcepage" value="%{sourcepage}" />
<s:hidden name="model.documentNumber" id="docNumber" />
<div class="formmainbox"><div class="insidecontent">
  <div class="rbroundbox2">
	<div class="rbtop2"><div></div></div>
	  <div class="rbcontent2">
	  <div class="datewk"> 
	  	<div class="estimateno"> <s:text name="revisionEstimate.revEstNo" /> : <s:if test="%{not model.estimateNumber}">&lt; <s:text name="message.notAssigned" /> &gt;</s:if><s:property value="model.estimateNumber" /></div>
	 </div>
	    <table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td><div id="header">
				<ul id="Tabs">
					<li id="revEstHeaderTab" class="First Active"><a id="header_1" href="#" onclick="showRevEstHeaderTab();"><s:text name="estimate.header" /></a></li>
					<li id="OrginialEstDtlsTab" class="Befor"><a id="header_2" href="#" onclick="showOrginialEstDtlsTab();"><s:text name="re.original.estimate.details" /></a></li>
					<li id="RevEstNonTenderedItemsTab" class=""><a id="header_3" href="#" onclick="showNonTenderedItemsTab();"><s:text name="revisionEstimate.nonTenderedItems" /></a></li>
					<li id="changeQtyDtlsTab" class="Last"><a id="header_4" href="#" onclick="showChangeQtyDtlsTab();"><s:text name="revisionEstimate.changeQuantity" /></a></li>					
 				</ul>
            </div></td>
          </tr>
      	<tr><td>&nbsp;</td></tr>
          <tr>
            <td>
            <div id="revEst_header">
 				<table width="100%"  border="0" cellspacing="0" cellpadding="0">
	 				<tr>
	                	<td colspan="4" class="headingwk"><div class="arrowiconwk"><img src="/egworks/resources/erp2/images/arrow.gif" /></div>
	                	<div class="headplacer"><s:text name="estimate.header" />:</div></td>
	                </tr>
	                
				 <tr>
						<td width="25%" class="whiteboxwk"><s:text name="revisionEstimate.original.estimateNo.label" />:</td> 
						<td width="25%" class="whitebox2wk" >
							<a href="#" id="estNumb" onclick="showEstimateDetails(<s:property  value="%{abstractEstimate.id}"/>)"><s:property  value="%{abstractEstimate.estimateNumber}"   /></a>
						</td>
						
						<td width="25%" class="whiteboxwk"> <s:text name="estimate.estimatevalue" />:</td>
						<td width="25%" class="whitebox2wk"><s:text name="contractor.format.number" ><s:param  value="%{abstractEstimate.totalAmount.value}" /></s:text></td>						 
					</tr>
					<tr>
						<td width="25%" class="greyboxwk"> <s:text name="estimate.projectcode" />:</td>
						<td width="25%" class="greybox2wk" colspan="3"><s:property  value="%{abstractEstimate.projectCode.code}"   /></td>

					</tr>
					<tr>
						<td width="25%" class="whiteboxwk"> <s:text name="workOrder.originalWONumber" />:</td>
						<td width="25%" class="whitebox2wk"><s:property  value="%{workOrder.workOrderNumber}"   /></td>
						
						<td width="25%" class="whiteboxwk"><s:text name="revisionEstimate.workOrder.contractor" />:</td>
						<td width="25%" class="whitebox2wk"><s:property  value="%{workOrder.contractor.name}"   /></td>
					</tr>
					<tr>
		         		<td colspan="12" class="shadowwk"> </td>               
		         	</tr>
		         	<tr><td>&nbsp;</td></tr>
				</table>
			
            </div>            
            </td> 
          </tr>   <!-- revEst_header End  -->   
           <tr>
            <td>
            <div id="originalEst_details" style="display:none;">
  				   
					<table border="0" width="100%" cellspacing="0" cellpadding="0">		
					<tr>
						<th class="pagetableth" width="3%"><s:text name="revisionEstimate.column.title.slNo" /></th>
						<th class="pagetableth" width="12%"><s:text name="revisionEstimate.column.title.estNo" /></th>
						<th class="pagetableth" width="12%"><s:text name="revisionEstimate.column.title.revType" /></th>
						<th class="pagetableth" width="6%"><s:text name="revisionEstimate.column.title.code" /></th>
						<th class="pagetableth" width="30%"><s:text name="revisionEstimate.column.title.description" /></th>
						<th class="pagetableth" width="10%">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<s:text name="revisionEstimate.column.title.uom" /></th>
						<th class="pagetableth" width="7%">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<s:text name="revisionEstimate.column.title.rate" /></th>
						<th class="pagetableth" width="10%"><s:text name="revisionEstimate.column.title.estQty" /></th>
						<th class="pagetableth" width="10%"><s:text name="revisionEstimate.column.title.estAmount" /></th>
					<!--	<th class="pagetableth" width="5%"><s:text name="revisionEstimate.column.title.serviceTaxPerc" /></th>
						<th class="pagetableth" width="10%"><s:text name="revisionEstimate.column.title.serviceTaxAmount" /></th>
						<th class="pagetableth" width="10%">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						    <s:text name="revisionEstimate.column.title.total" /></th> -->
					</tr>
				<c:set var="tdclass" value="whitebox2wk" scope="request" />
				<s:iterator id="soriterator" value="originalRevisedActivityList" status="row_status">
				<s:if test="%{schedule!=null}"> 
				<tr>
					<td width="3%" ><s:property value="#row_status.count"/></td>
					
					<td width="5%" ><s:property value="abstractEstimate.estimateNumber"/></td>
					<td width="7%" ><s:property value="revisionType"/></td>
					<td width="6%" ><s:property value="schedule.code"/></td>
					<td width="25%" ><s:property value="schedule.summaryJS"/></td>
					<td width="10%" ><div align="center"><s:property value="schedule.uom.uom"/></div></td>
					<td width="7%" ><div align="right"><s:property value="sORCurrentRate"/></div></td>
					<td width="10%" ><div align="right"><s:property value="quantity"/></div></td>
					<td width="10%" ><div align="right"><s:text name="contractor.format.number" >
                                                    <s:param name="value" value="%{amount.value}"/></s:text></div></td>
					<!-- <td width="5%" ><div align="right"><s:property value="serviceTaxPerc"/></div></td>
					<td width="10%" ><div align="right"><s:text name="contractor.format.number" >
                                                    <s:param name="value" value="%{taxAmount.value}"/></s:text></div></td>
					<td width="10%" ><div align="right"><s:text name="contractor.format.number" >
                                                    <s:param name="value" value="%{amountIncludingTax.value}"/></s:text></div></td>  -->
				</tr>
				</s:if>
				<s:else>
				<tr>
					<td width="3%"><s:property value="#row_status.count"/></td>
					<td width="5%" ><s:property value="abstractEstimate.estimateNumber"/></td>
					<td width="7%" ><s:property value="revisionType"/></td>
					<td width="6%" ></td> 
					<td width="25%" ><s:property value="nonSor.descriptionJS"/></td>
					<td width="10%" ><div align="center"><s:property value="nonSor.uom.uom"/></div></td>
					<td width="7%" ><div align="right"><s:property value="rate"/></div></td>
					<td width="10%" ><div align="right"><s:property value="quantity"/></div></td>
					<td width="10%" ><div align="right"><s:text name="contractor.format.number" >
                                                    <s:param name="value" value="%{amount.value}"/></s:text></div></td>
				<!--	<td width="5%" ><div align="right"><s:property value="serviceTaxPerc"/></div></td>
					<td width="10%" ><div align="right"><s:text name="contractor.format.number" >
                                                    <s:param name="value" value="%{taxAmount.value}"/></s:text></div></td> 
					<td width="10%" ><div align="right"><s:text name="contractor.format.number" >
                                                    <s:param name="value" value="%{amountIncludingTax.value}"/></s:text></div></td> -->
				</tr>
				</s:else>
			</s:iterator>
			
			<tr>
					<td  colspan="7" class="whitebox4wk"></td>
					<td width="10%"  class="whitebox4wk"><div align="right"><b><s:text name="revisionEstimate.column.title.total" /></b></div></td>
					<td width="10%"  class="whitebox4wk">
						<div align="right"><b><s:text name="contractor.format.number" ><s:param name="value" value="%{originalTotalAmount}"/></s:text></b></div>
					</td>
				<!--	<td width="5%"  class="whitebox4wk"></td> 
					<td width="10%"  class="whitebox4wk">
						<div align="right"><b><s:text name="contractor.format.number" ><s:param name="value" value="%{originalTotalTax}"/></s:text></b></div>
					</td>
					<td width="10%" class="whitebox4wk">
						<div align="right"><b><s:text name="contractor.format.number" ><s:param name="value" value="%{originalWorkValueIncludingTax}"/></s:text></b></div>
					</td> -->
			</tr>
			
			<tr>
         		<td colspan="12" class="shadowwk"> </td>               
         	</tr>
         	<tr><td>&nbsp;</td></tr>
		</table>
		
		</div>
    
            </td>
          </tr>
           <!-- originalEst_details End  -->
             
          <tr>
            <td>
            <div id="re_nonTenderedItems" style="display:none;"> 
            	<%@ include file='revisionEstimate-nonTenderedItems.jsp'%>
            	<%@ include file='revisionEstimate-lumpSumItems.jsp'%> 
            </div>
            </td>
          </tr>
          
          <tr>
            <td>
            <div id="changeQty_details" style="display:none">            
            	 <%@ include file="revisionEstimate-changeQuantity.jsp"%>  
            </div>
            </td>
          </tr>
         
          <tr>
          <td><table width="100%" align="center" border="0" cellspacing="0" cellpadding="0">
           	<tr>
	            <td width="17%" class="whiteboxwk"><s:text name="revision.estimate.value" />:</td>
                <td width="17%" class="whitebox2wk"><s:textfield name="estimateValue" value="%{estimateValue}"  id="estimateValue" cssClass="selectamountwk" readonly="true" align="right" />
              	</td>
	            <td class="whiteboxwk">&nbsp;</td>
	            <td class="whiteboxwk">&nbsp;</td>
            </tr>
            </table></td>
          </tr>
          
          
         <s:if test="%{sourcepage!='search'}" >
         <tr>
			<td class="shadowwk" colspan="4"> </td>
		 </tr>
	 	 <tr> 
		    <td>
		    	<div id="manual_workflow">
         			<c:set var="approverHeadCSS" value="headingwk" scope="request" />
         			<c:set var="approverCSS" value="bluebox" scope="request" />
         			<s:hidden name="departmentName" id="departmentName" value="%{departmentName}"/>
					<%@ include file="../commons/commonWorkflow.jsp"%>
  				</div>
 		    </td>
            </tr>
          </s:if>         
          
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
	<!--<s:if test="%{model.id==null || model.egwStatus.code=='NEW'}">
	 			<s:submit type="submit" cssClass="buttonfinal"
					value="Save" id="Save" name="Save"
					method="save"
					onclick="document.revisionEstimateForm.actionName.value='save';return validate('Save');" />
	 </s:if>-->
	
	<s:if test="%{sourcepage!='search' && (hasErrors() || sourcepage=='inbox' || model.egwStatus==null || model.egwStatus.code=='NEW' 
|| model.egwStatus.code=='REJECTED')}">
        	<s:iterator value="%{getValidActions()}" var="p">
			<!-- <s:submit type="submit" cssClass="buttonsubmit" value="%{p}" id="%{p}" name="%{p}" method="create" onclick="return validateUser('%{p}')"/>  -->
			 <s:submit type="submit" cssClass="buttonfinal" value="%{p}" id="%{p}" name="%{p}" method="save" onclick="document.revisionEstimateForm.actionName.value='%{p}';return validate('%{p}');"/>
				
		</s:iterator>
		</s:if>
	<s:if test="%{sourcepage=='search'}">
	  	<input type="submit" class="buttonadd" value="View Document" id="docViewButton" onclick="viewDocument();return false;" /> 
	  </s:if>
	  <s:else>
	  	<input type="submit" class="buttonadd" value="Upload Document" id="docUploadButton" onclick="showDocumentManager();return false;" />
	  </s:else>
	  <s:if test="%{sourcepage!='search'}">
		<input type="button" class="buttonfinal" value="CLOSE" id="closeButton" name="closeButton" onclick="return confirmClose('<s:text name='revisionEstimate.close.confirm'/>')"/>
		</s:if>
		<s:else>
		<input type="button" class="buttonfinal" value="CLOSE" id="closeButton" name="closeButton" onclick="window.close();"/>
		</s:else>
		<s:if test="%{showBudgetFolio}" >
			<input type="button" onclick="window.open('${pageContext.request.contextPath}/estimate/financialDetail!viewBudgetFolio.action?estimateId=<s:property value='%{model.id}'/>', '', 'height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');" class="buttonadd" 
     			value="View Budget Folio" id="viewBudgetFolio" name="viewBudgetFolio"/>
     	</s:if>
     	<s:if test="%{showDepositFolio}" >
     		<input type="button" onclick="window.open('${pageContext.request.contextPath}/estimate/financialDetail!viewDepositWorksFolio.action?estimateId=<s:property value='%{model.id}'/>', '', 'height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');" class="buttonadd" value="View Deposit Folio" id="depositfolioreportButton" name="depositfolioreportButton"/>
     	</s:if>			
</div>
<s:hidden name="actionName"  id="actionName"/>
</s:push>
</s:form>
<script>hideRevEstDetailsTab()</script>
<script>

function enableFields(){
	for(i=0;i<document.revisionEstimateForm.elements.length;i++){
	        document.revisionEstimateForm.elements[i].disabled=false; 
	} 
}

function loadDesignationFromMatrix(){
  		 var dept=dom.get('departmentName').value;
  		 var currentState = dom.get('currentState').value;
  		 var amountRuleValue =  dom.get('amountRuleValue').value;
  		 var additionalRuleValue =  dom.get('additionalRuleValue').value; 
  		 var pendingAction=document.getElementById('pendingActions').value;
       	 loadDesignationByDeptAndType('RevisionAbstractEstimate',dept,currentState,amountRuleValue,additionalRuleValue,pendingAction);
}

function populateApprover()
{
  getUsersByDesignationAndDept();
}

function validateUser(actionName){
	
	dom.get('actionName').value=actionName;
}

function validateCancel() {
	var msg='<s:text name="re.cancel.confirm"/>';
	var revEstNo='<s:property value="model.revisionEstimate.estimateNumber"/>'; 
	if(!confirmCancel(msg,revEstNo)) {
		return false;
	}
	else {
		return true;
	}
}

function validate(text){
	if(document.getElementById("actionName").value=='Cancel'){
		if(!validateCancel()){
			return false;
		}
	}
	if(!validateTextFieldErrors())
		return false;
	if(!validateQuantities(sorDataTable,'sorquantity'))
		return false;
	if(!validateLumpSumItems(nonSorDataTable,'nonSordescription','<s:text name="revisionEstimate.enter.nonsor.description" />'))
		return false;
	if(!validateLumpSumItems(nonSorDataTable,'nonsorrate','<s:text name="revisionEstimate.enter.nonsor.rate" />'))
		return false;
	if(!validateQuantities(nonSorDataTable,'nonsorquantity'))
		return false;
	if(!validateQuantities(changeQuantityDataTable,'CQquantity'))
		return false;
	if(!validateREAmount())
		return false;
	if(text!='Approve' && text!='Reject' && text!='Save'){
		if(!validateWorkFlowApprover(text))
			return false;
	}
	if (!validateNonSorUomDropDown()) {
		return false;	
	}
	enableFields();
	return true;  
}

function validateREAmount()
{
	var estVal =  document.getElementById("estimateValue").value;
	clearMessage('revisionEstimate_error');
	if(estVal =='' || getNumber(estVal)==0 || getNumber(estVal)<0)
	{	
		dom.get("revisionEstimate_error").style.display=''; 
		document.getElementById("revisionEstimate_error").innerHTML='<s:text name="revisionEstimate.amount.error" />';
		window.scroll(0,0);
	 	return false;
	}
	return true;	
}

function validateTextFieldErrors()
{
	links=document.revisionEstimateForm.getElementsByTagName("span");
	errors=false;
	for(i=0;i<links.length;i++) {
        if(links[i].innerHTML=='&nbsp;x' && links[i].style.display!='none'){
            errors=true;
            break;
        }
    }
    if(errors) {
        dom.get("revisionEstimate_error").style.display='';
    	document.getElementById("revisionEstimate_error").innerHTML='<s:text name="re.validate_x.message" />';
    	window.scroll(0,0);
    	return false;
    }
    return true;
}

function disableForm(){
    <s:if test="%{(sourcepage=='search') || (sourcepage=='inbox' 
    	&& model.egwStatus!=null && model.egwStatus.code!='NEW' && model.egwStatus.code!='REJECTED')}" >
		toggleFields(false,['approverDepartment','approverDesignation','approverPositionId','approverComments']);
		for(i=0;i<document.revisionEstimateForm.elements.length;i++){
   			document.revisionEstimateForm.elements[i].disabled=true;
	  	}
		document.getElementById('re_nonTenderedItems').style.display="none";
		document.getElementById('baseSORTable').style.display="none";
		links=document.forms[0].getElementsByTagName("a");
				for(i=0;i<links.length;i++){
			     if(links[i].id=="estNumb" ||links[i].id=="reNumb" )
	             {
    
	             }
	           else if(links[i].id.indexOf("header_")!=0)
       			{
			links[i].onclick=function(){return false;};
	        }
			}
    </s:if>
}
<s:if test="%{(sourcepage=='search')}">
changeQuantityGrandTotal = 0;
if(document.getElementById("changeQuantityGrandTotal"))
	  changeQuantityGrandTotal = eval(document.getElementById("changeQuantityGrandTotal").innerHTML);
document.getElementById("estimateValue").value=roundTo(eval(document.getElementById("grandTotal").innerHTML)+eval(document.getElementById("nonSorGrandTotal").innerHTML)+changeQuantityGrandTotal);
	disableForm();
	if(document.getElementById('Approve')!=null)
		document.getElementById('Approve').style.display="none";
	document.revisionEstimateForm.closeButton.readonly=false;
	document.revisionEstimateForm.closeButton.disabled=false;
	document.revisionEstimateForm.docViewButton.readonly=false;
	document.revisionEstimateForm.docViewButton.disabled=false;

</s:if> 
<s:if test="%{sourcepage=='inbox'}">
	changeQuantityGrandTotal = 0;
	if(document.getElementById("changeQuantityGrandTotal"))
		  changeQuantityGrandTotal = eval(document.getElementById("changeQuantityGrandTotal").innerHTML);
	document.getElementById("estimateValue").value=roundTo(eval(document.getElementById("grandTotal").innerHTML)+eval(document.getElementById("nonSorGrandTotal").innerHTML)+changeQuantityGrandTotal);
     <s:if test="%{model.egwStatus!=null && model.egwStatus.code!='REJECTED' }">
     	disableForm();
		if(document.getElementById("Approve")!=null){
			document.revisionEstimateForm.Approve.disabled=false;
		}
		if(document.getElementById("approverPositionId")!=null){
			document.revisionEstimateForm.approverPositionId.disabled=false;
		}
		if(document.getElementById("approverDesignation")!=null){
			document.revisionEstimateForm.approverDesignation.disabled=false;
		}
		if(document.getElementById("approverDepartment")!=null){
			document.revisionEstimateForm.approverDepartment.disabled=false;
		}
		if(document.getElementById("Reject")!=null){
			document.revisionEstimateForm.Reject.disabled=false;
		}
		if(document.getElementById("Forward")!=null){
			document.revisionEstimateForm.Forward.disabled=false;
		}
		
		if(document.getElementById("approverComments")!=null){
			document.revisionEstimateForm.approverComments.disabled=false;
		}
			
		document.revisionEstimateForm.docUploadButton.readonly=false;
		document.revisionEstimateForm.docUploadButton.disabled=false;
		document.revisionEstimateForm.closeButton.readonly=false;
		document.revisionEstimateForm.closeButton.disabled=false;		
		
	 </s:if>
	 <s:elseif test="%{model.egwStatus!=null && model.egwStatus.code=='CANCELLED'|| model.egwStatus.code=='APPROVED'}">
		disableForm();
		document.revisionEstimateForm.closeButton.readonly=false;
		document.revisionEstimateForm.closeButton.disabled=false;
	 </s:elseif>
	 <s:if test="%{model.currentState.value!='END' || hasErrors()}">
  		if(document.getElementById("approverPositionId")!=null){
	  		document.revisionEstimateForm.approverPositionId.readonly=false;
			document.revisionEstimateForm.approverPositionId.disabled=false;
			document.revisionEstimateForm.approverDesignation.readonly=false;
			document.revisionEstimateForm.approverDesignation.disabled=false;
			document.revisionEstimateForm.approverDepartment.readonly=false;
			document.revisionEstimateForm.approverDepartment.disabled=false;
		}
  	</s:if>
	 <s:if test="%{model.id!=null 
		 && (model.egwStatus!=null && model.egwStatus.code=='CREATED' || model.egwStatus.code=='RESUBMITTED')}">
		document.getElementById('approverComments').readonly=false;	
 			document.getElementById('approverComments').disabled=false;	
	 </s:if>
</s:if>
if(document.revisionEstimateForm.viewBudgetFolio)
{
	document.revisionEstimateForm.viewBudgetFolio.readonly=false;
	document.revisionEstimateForm.viewBudgetFolio.disabled=false;
}
if(document.revisionEstimateForm.depositfolioreportButton)
{
	document.revisionEstimateForm.depositfolioreportButton.readonly=false;
	document.revisionEstimateForm.depositfolioreportButton.disabled=false;
}	
</script>
</body>

</html>
