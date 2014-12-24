<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator"%>
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>  
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<title>Revision Estimate</title>
<body onload="spillOverWorks()"  class="yui-skin-sam"> 
<script src="<egov:url path='js/works.js'/>"></script> 
<script src="<egov:url path='js/helper.js'/>"></script>

<script >
	function showRevEstHeaderTab(){
		document.getElementById('revEst_header').style.display='';
		setCSSClasses('revEstHeaderTab','First Active');
		setCSSClasses('OrginialEstDtlsTab','');
		setCSSClasses('RevEstExtraItemsTab','');
		setCSSClasses('changeQtyDtlsTab','');
		setCSSClasses('overheadsTab','Last');
		
		hideOrginialEstDetailsTab();
		hideRevEstDetailsTab();
		hideOverheadsTab();
		hideChangeQuantityTab();
	}

	function showOrginialEstDtlsTab(){
		document.getElementById('originalEst_details').style.display='';
		setCSSClasses('revEstHeaderTab','First BeforeActive');
		setCSSClasses('OrginialEstDtlsTab','Active');
		setCSSClasses('RevEstExtraItemsTab','');
		setCSSClasses('changeQtyDtlsTab','');
		setCSSClasses('overheadsTab','Last');
		hideRevEstHeaderTab();
		hideRevEstDetailsTab();
		hideOverheadsTab();
		hideChangeQuantityTab();
	
	}

	function showExtraItemsTab(){
		document.getElementById('re_extraItems').style.display='';
		setCSSClasses('revEstHeaderTab','First');
		setCSSClasses('OrginialEstDtlsTab','BeforeActive');
		setCSSClasses('RevEstExtraItemsTab','Active');
		setCSSClasses('changeQtyDtlsTab','');
		setCSSClasses('overheadsTab','Last');
		hideRevEstHeaderTab();
		hideOrginialEstDetailsTab();
		hideOverheadsTab();
		hideChangeQuantityTab();
  		document.getElementById('baseSORTable').style.display='';
  		document.getElementById('sorHeaderTable').style.display=''; 
  		document.getElementById('sorTable').style.display='';
  		document.getElementById('nonSorHeaderTable').style.display='';
  		document.getElementById('nonSorTable').style.display='';
	} 

	function showChangeQtyDtlsTab(){
		document.getElementById('changeQty_details').style.display='';
		document.getElementById('changeQuantityHeaderTable').style.display='';
		document.getElementById('itemsTable').style.display='';
		setCSSClasses('revEstHeaderTab','First');
		setCSSClasses('OrginialEstDtlsTab','');
		setCSSClasses('RevEstExtraItemsTab','BeforeActive');
		setCSSClasses('changeQtyDtlsTab','Active');
		setCSSClasses('overheadsTab','Last');
		hideRevEstHeaderTab();
		hideOrginialEstDetailsTab();
		hideOverheadsTab();
		hideRevEstDetailsTab();
	} 
	
	function showOverheadsTab(){
		document.getElementById('revision_overheads').style.display='';
		setCSSClasses('revEstHeaderTab','First');
		setCSSClasses('OrginialEstDtlsTab','');
		setCSSClasses('RevEstExtraItemsTab','');
		setCSSClasses('changeQtyDtlsTab','BeforeActive');
		setCSSClasses('overheadsTab','Last Active ActiveLast');
		hideRevEstHeaderTab();
		hideRevEstDetailsTab();
		hideOrginialEstDetailsTab();
		hideChangeQuantityTab();
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
			document.getElementById('re_extraItems').style.display='none';
			document.getElementById('baseSORTable').style.display='none';
			document.getElementById('sorHeaderTable').style.display='none';
  			document.getElementById('sorTable').style.display='none';
  			document.getElementById('nonSorHeaderTable').style.display='none';
 			document.getElementById('nonSorTable').style.display='none';
	}
	function hideOverheadsTab(){
			document.getElementById('revision_overheads').style.display='none';
	}
	
function hideChangeQuantityTab(){
	document.getElementById('changeQty_details').style.display='none';
	document.getElementById('changeQuantityHeaderTable').style.display='none';
	document.getElementById('itemsTable').style.display='none';
	
}

function showEstimateDetails(estimateId){
	window.open("${pageContext.request.contextPath}/estimate/abstractEstimate!edit.action?id="+estimateId+"&sourcepage=search",'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
}

function showRevisionEstimateDetails(estimateId){
	window.open("${pageContext.request.contextPath}/revisionEstimate/revisionEstimate!view.action?revEstimateId="+estimateId+"&sourcepage=search",'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
} 
  
function spillOverWorks(){ 
	<s:if test="%{abstractEstimate.isSpillOverWorks}">
		if(dom.get("manual_workflow")!=null){ 
			dom.get("manual_workflow").style.display='none';
		}
	</s:if>
}

function viewDocument(){
	  viewDocumentManager(dom.get("docNumber").value); 
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
	<s:hidden name="revEstimateId" value="%{id}" id="revEstimateId" />
	</s:if>
<s:hidden id="additionalRuleValue" name="additionalRuleValue" value="%{additionalRuleValue}" />
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
	  	<div class="estimateno">Revision Estimate No: <s:if test="%{not model.estimateNumber}">&lt; Not Assigned &gt;</s:if><s:property value="model.estimateNumber" /></div>
	 </div>
	    <table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td><div id="header">
				<ul id="Tabs">
					<li id="revEstHeaderTab" class="First Active"><a id="header_1" href="#" onclick="showRevEstHeaderTab();">Header</a></li>
					<li id="OrginialEstDtlsTab" class=""><a id="header_2" href="#" onclick="showOrginialEstDtlsTab();">Original Estimate Details</a></li>
					<li id="RevEstExtraItemsTab" class=""><a id="header_3" href="#" onclick="showExtraItemsTab();"><s:text name="revisionEstimate.extraItems" /></a></li>
					<li id="changeQtyDtlsTab" class=""><a id="header_4" href="#" onclick="showChangeQtyDtlsTab();"><s:text name="revisionEstimate.changeQuantity" /></a></li> 				
					<li id="overheadsTab" class="Last"><a id="header_5" href="#" onclick="showOverheadsTab();">Overhead Details</a></li>
 				</ul>
            </div></td>
          </tr>
      	<tr><td>&nbsp;</td></tr>
          <tr>
            <td>
            <div id="revEst_header">
 				<table width="100%" >
	 				<tr>
	                	<td colspan="4" class="headingwk"><div class="arrowiconwk"><img src="${pageContext.request.contextPath}/image/arrow.gif" /></div>
	                	<div class="headplacer"><s:text name="estimate.header" />:</div></td>
	                </tr>
	                
				 <tr>
						<td width="25%" class="whiteboxwk">Original <s:text name="estimate.search.estimateNo" />:</td> 
								<td width="25%" class="whitebox2wk" >
					<a href="#" id="estNumb" onclick="showEstimateDetails(<s:property  value="%{abstractEstimate.id}"/>)"><s:property  value="%{abstractEstimate.estimateNumber}"   /></a>
						</td>
						 
						<td width="25%" class="whiteboxwk">Revision  <s:text name="estimate.search.estimateNo" />:</td>
						<td width="25%" class="whitebox2wk">
								<s:if test="%{reList.size() != 0}">
									<s:iterator var="re" value="reList" status="status">
									<s:if test="id!=revEstimateId"> 
									<a href="#" id="reNumb" onclick="showRevisionEstimateDetails(<s:property  value="%{id}"/>)"><s:property  value="%{estimateNumber}"   /></a>
										<br>
									</s:if>
									</s:iterator> 
								</s:if>
						</td>
					</tr>
					<tr>
						<td width="25%" class="greyboxwk"> <s:text name="estimate.projectcode" />:</td>
						<td width="25%" class="greybox2wk"><s:property  value="%{abstractEstimate.projectCode.code}"   /></td>

						<td width="25%" class="greyboxwk"> <s:text name="estimate.estimatevalue" />:</td>
						<td width="25%" class="greybox2wk"><s:text name="contractor.format.number" ><s:param  value="%{revisionEstimatesValue}"   /></s:text></td>
					</tr>
					<tr>
						<td width="25%" class="whiteboxwk"> <s:text name="workOrder.originalWONumber" />:</td>
						<td width="25%" class="whitebox2wk"><s:property  value="%{workOrder.workOrderNumber}"   /></td>
						
						<td width="25%" class="whiteboxwk"><s:text name="workOrder.contractor" />:</td>
						<td width="25%" class="whitebox2wk"><s:property  value="%{workOrder.contractor.name}"   /></td>
					</tr>
					
					<tr> 
					 	<td width="25%" class="greyboxwk"> <s:text name="workOrder.CurWOValue" />:</td>
						<td width="25%" class="greybox2wk"><s:text name="contractor.format.number" ><s:param  value="%{revisionWOValue}"   /></s:text></td>
						<td width="25%" class="greyboxwk"></td>
						<td width="25%" class="greybox2wk"> </td>
					</tr>
					
				</table>
			
            </div>            
            </td> 
          </tr>   <!-- revEst_header End  -->   
           <tr>
            <td>
            <div id="originalEst_details" style="display:none;">
  				   
					<table border="0" width="100%" cellspacing="0" cellpadding="0">		
					<tr>
						<th class="pagetableth" width="3%">Sl No</th>
						<th class="pagetableth" width="5%">Estimate No</th>
						<th class="pagetableth" width="7%">Revision Type</th>
						<th class="pagetableth" width="6%">Code</th>
						<th class="pagetableth" width="25%">Description</th>
						<th class="pagetableth" width="10%">UOM</th>
						<th class="pagetableth" width="7%">Rate</th>
						<th class="pagetableth" width="10%">Estimated Quantity</th>
						<th class="pagetableth" width="10%">Estimated Amount</th>
						<th class="pagetableth" width="5%">Service Tax %</th>
						<th class="pagetableth" width="10%">Service Tax Amount</th>
						<th class="pagetableth" width="10%">Total</th>
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
					<td width="5%" ><div align="right"><s:property value="serviceTaxPerc"/></div></td>
					<td width="10%" ><div align="right"><s:text name="contractor.format.number" >
                                                    <s:param name="value" value="%{taxAmount.value}"/></s:text></div></td>
					<td width="10%" ><div align="right"><s:text name="contractor.format.number" >
                                                    <s:param name="value" value="%{amountIncludingTax.value}"/></s:text></div></td>
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
					<td width="5%" ><div align="right"><s:property value="serviceTaxPerc"/></div></td>
					<td width="10%" ><div align="right"><s:text name="contractor.format.number" >
                                                    <s:param name="value" value="%{taxAmount.value}"/></s:text></div></td>
					<td width="10%" ><div align="right"><s:text name="contractor.format.number" >
                                                    <s:param name="value" value="%{amountIncludingTax.value}"/></s:text></div></td>
				</tr>
				</s:else>
			</s:iterator>
			
			<tr>
					<td  colspan="7" class="whitebox4wk"></td>
					<td width="10%"  class="whitebox4wk"><div align="right"><b>Total</b></div></td>
					<td width="10%"  class="whitebox4wk">
						<div align="right"><b><s:text name="contractor.format.number" ><s:param name="value" value="%{originalTotalAmount}"/></s:text></b></div>
					</td>
					<td width="5%"  class="whitebox4wk"></td> 
					<td width="10%"  class="whitebox4wk">
						<div align="right"><b><s:text name="contractor.format.number" ><s:param name="value" value="%{originalTotalTax}"/></s:text></b></div>
					</td>
					<td width="10%" class="whitebox4wk">
						<div align="right"><b><s:text name="contractor.format.number" ><s:param name="value" value="%{originalWorkValueIncludingTax}"/></s:text></b></div>
					</td> 
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
          	<div id="revision_overheads" style="display:none;">
	       		<%@ include file="revisionEstimate-overheads.jsp"%>                
            </div>
            </td>
          </tr>        
          <tr>
            <td>
            <div id="re_extraItems" style="display:none;"> 
            	<%@ include file='revisionEstimate-sor.jsp'%>
            	<%@ include file='revisionEstimate-nonSor.jsp'%> 
           	<%@ include file="reExtraItem-measurementSheet.jsp"%>  
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
          
          
         <s:if test="%{mode != 'view' && sourcepage!='search'}" >
	 	 <tr> 
		    <td>
		    	<div id="manual_workflow">
         			<c:set var="approverHeadCSS" value="headingwk" scope="request" />
         			<c:set var="approverCSS" value="bluebox" scope="request" />
         			<s:hidden name="departmentName" id="departmentName" value="%{departmentName}"/>
					<%@ include file="/commons/commonWorkflow.jsp"%>
  				</div>
 		    </td>
            </tr>
          </s:if>         
          	
           
        </table>       
        
        <div class="rbbot2"><div></div></div>
      </div>     
	
</div>
  </div>
</div>

<div class="buttonholderwk">
	<s:if test="%{model.id==null || model.egwStatus.code=='NEW'}">
	 			<s:submit type="submit" cssClass="buttonfinal"
					value="Save" id="Save" name="Save"
					method="save"
					onclick="document.revisionEstimateForm.actionName.value='save';return validate('Save');" />
	 </s:if>
	<s:if test="%{abstractEstimate.isSpillOverWorks}">	
		<s:submit type="submit" cssClass="buttonfinal"
				value="Approve" id="Approve" name="Approve"
				method="save"
				onclick="document.revisionEstimateForm.actionName.value='Approve';return validate('Approve');" />
			<s:if test="%{model.egwStatus.code=='NEW'}">
				<s:submit type="submit" cssClass="buttonfinal"
						value="Cancel" id="Cancel" name="Cancel"
						method="save"
						onclick="document.revisionEstimateForm.actionName.value='Cancel';return validate('Cancel');" />
			
			</s:if>
	</s:if>
	<s:else>
	<s:if test="%{hasErrors() || sourcepage=='inbox' || model.currentState==null || model.currentState.value=='NEW' 
|| model.currentState.value=='REJECTED'}">
        	<s:iterator value="%{getValidActions()}" var="p">
			<!-- <s:submit type="submit" cssClass="buttonsubmit" value="%{p}" id="%{p}" name="%{p}" method="create" onclick="return validateUser('%{p}')"/>  -->
			 <s:submit type="submit" cssClass="buttonfinal" value="%{p}" id="%{p}" name="%{p}" method="save" onclick="document.revisionEstimateForm.actionName.value='%{p}';return validate('%{p}');"/>
				
		</s:iterator>
		</s:if>
	</s:else>
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
  		 var amountRule =  dom.get('amountRule').value;
  		 //var additionalRule =  dom.get('additionalRule').value;
  		 var additionalRuleValue =  dom.get('additionalRuleValue').value; 
  		 var pendingAction=document.getElementById('pendingActions').value;
       	 loadDesignationByDeptAndType('RevisionAbstractEstimate',dept,currentState,amountRule,additionalRuleValue,pendingAction);
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
	if(text!='Approve' && text!='Reject' && text!='Save'){
		if(!validateWorkFlowApprover(text))
			return false;
	}
	enableFields();
	return true;  
}

function disableForm(){
    <s:if test="%{(sourcepage=='search') || (sourcepage=='inbox' && model.currentState.value!='NEW' && model.currentState.value!='REJECTED')}" >
		toggleFields(false,['approverDepartment','approverDesignation','approverPositionId','approverComments']);
		for(i=0;i<document.revisionEstimateForm.elements.length;i++){
   			document.revisionEstimateForm.elements[i].disabled=true;
	  	}
		document.getElementById('re_extraItems').style.display="none";
		document.getElementById('baseSORTable').style.display="none"; 
		overheadsTable.removeListener('cellClickEvent');
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
document.getElementById("estimateValue").value=roundTo(eval(document.getElementById("grandTotal").innerHTML)+eval(document.getElementById("nonSorGrandTotal").innerHTML)+eval(document.getElementById("changeQuantityGrandTotal").innerHTML)+eval(document.getElementById("overHeadTotalAmnt").innerHTML));
	disableForm();
	if(document.getElementById('Approve')!=null)
		document.getElementById('Approve').style.display="none";
	document.revisionEstimateForm.closeButton.readonly=false;
	document.revisionEstimateForm.closeButton.disabled=false;
	document.revisionEstimateForm.docViewButton.readonly=false;
	document.revisionEstimateForm.docViewButton.disabled=false;
</s:if> 
<s:if test="%{sourcepage=='inbox'}">
	document.getElementById("estimateValue").value=roundTo(eval(document.getElementById("grandTotal").innerHTML)+eval(document.getElementById("nonSorGrandTotal").innerHTML)+eval(document.getElementById("changeQuantityGrandTotal").innerHTML)+eval(document.getElementById("overHeadTotalAmnt").innerHTML));
     <s:if test="%{model.currentState.value=='CREATED' || model.currentState.value=='TECH_SANCTIONED' || model.currentState.value=='FINANCIALLY_SANCTIONED'}">
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
		
		
		document.revisionEstimateForm.closeButton.readonly=false;
		document.revisionEstimateForm.closeButton.disabled=false;
		document.revisionEstimateForm.pdfButton.readonly=false;
		document.revisionEstimateForm.pdfButton.disabled=false;
		document.revisionEstimateForm.mSheetPdfButton.readonly=false;
	    document.revisionEstimateForm.mSheetPdfButton.disabled=false;	
		document.revisionEstimateForm.mvDocUploadButton.disabled=false;
		document.revisionEstimateForm.mvDocUploadButton.disabled=false;
		
		
	 </s:if>
	 <s:elseif test="%{model.currentState.value=='ADMIN_SANCTIONED' || model.currentState.value=='CANCELLED'|| model.currentState.value=='APPROVED'}">
		disableForm();
		document.revisionEstimateForm.closeButton.readonly=false;
		document.revisionEstimateForm.closeButton.disabled=false;
		document.revisionEstimateForm.pdfButton.readonly=false;
		document.revisionEstimateForm.pdfButton.disabled=false;	
		document.revisionEstimateForm.mSheetPdfButton.readonly=false;
	    document.revisionEstimateForm.mSheetPdfButton.disabled=false;	
		document.revisionEstimateForm.mvDocUploadButton.disabled=false;
		document.revisionEstimateForm.mvDocUploadButton.disabled=false;
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
	 <s:if test="%{model.id!=null && (model.currentState.value=='CREATED' || model.currentState.value=='TECH_SANCTIONED' || model.currentState.value=='FINANCIALLY_SANCTIONED' || model.currentState.value=='RESUBMITTED')}">
		document.getElementById('approverComments').readonly=false;	
 			document.getElementById('approverComments').disabled=false;	
	 </s:if>
</s:if>	
</script>
</body>

</html>
