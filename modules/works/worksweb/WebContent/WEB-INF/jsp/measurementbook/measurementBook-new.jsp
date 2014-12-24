<%@ taglib prefix="egov" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator"%>
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>  
<html>
	<title><s:text name='measurementbook.title'/></title>
	<body id="home" onload="loadDefaults();loadDesignation();disableenable();hideDetailsTab();hideExtraItemsTab();">

	<script src="<egov:url path='js/works.js'/>"></script>
	<script src="<egov:url path='js/helper.js'/>"></script>
	<script>
	
	
function loadDefaults() {
	updateTotalValue();
	enablePartandReducedRate();
	enableNonSorPartandReducedRate();
 	enableSorPartandReducedRate();
 	enableEQPartandReducedRate();
}

function spillOverWorks(){
	<s:if test="%{isSpillOverWorks}">
		if(dom.get("manual_workflow")!=null){
			dom.get("manual_workflow").style.display='none';
		}
	</s:if>
}

function chkDate(obj){
var myDate=new Date();
var testdate=obj.value.split("/");
myDate.setFullYear(testdate[2],(parseInt(testdate[1])-1),testdate[0]);
var today = new Date();

if (myDate>today)
  {
dom.get("mb_error").style.display='';     
 document.getElementById("mb_error").innerHTML='Mbdetails Date should be current or previous date';
window.scroll(0,0);
return false;
  }
else
  {
  dom.get("mb_error").style.display='none';    
  return true;
  }
}

function trim(str) {
        return str.replace(/^\s+|\s+$/g,"");
}


function validateMBDetailsTable(table,column) {
	var records = table.getRecordSet();
	var tbLen = records.getLength();

  	for(var i=0;i<tbLen;i++){  
   		var record = records.getRecord(i);
   		var id=column+record.getId();
		var num= dom.get(id).value;
  		if(num=='' || trim(num)=='') {
  			dom.get('error'+id).style.display='';
  		}
  	}
}

function alphanumeric(alphane)
{
	var numaric = alphane;
	for(var j=0; j<numaric.length; j++)
		{
		  var alphaa = numaric.charAt(j);
		  var hh = alphaa.charCodeAt(0);
		  if((hh > 47 && hh<58) || (hh > 64 && hh<91) || (hh > 96 && hh<123))
		  {
		  }
		else	{
                       dom.get("mb_error").style.display='';     
	    			document.getElementById("mb_error").innerHTML='<s:text name="Order Number Should be Alphanumeric " />'; 
	    		window.scroll(0,0);
	    		return false;
		  }
 		}
  return true;
}
 		
 	

function validateOrderNumber(obj){
alphanumeric(obj.value);
}

		var test;
		function enableFields(actionNameVar){
  test=String(actionNameVar).toLowerCase();
			for(i=0;i<document.measurementBookForm.elements.length;i++){
	        	document.measurementBookForm.elements[i].disabled=false;
	        	document.measurementBookForm.elements[i].readonly=false;	       
			}
		}		

function validateForBill(){		
	clearMessage('mb_error')
	links=document.measurementBookForm.getElementsByTagName("span");
	errors=false;
	for(i=0;i<links.length;i++) {
        if(links[i].innerHTML=='&nbsp;x' && links[i].style.display!='none'){
            errors=true;
            break;
        }
    }
    
    if(errors) {
        dom.get("mb_error").style.display='';
    	document.getElementById("mb_error").innerHTML='<s:text name="mb.validate_x.message" />';
    	window.scroll(0,0);
    	return false;
    }
		
			if(test == 'cancel'){
	 			<s:if test="%{model !=null && (model.getMbBills()!=null && (model.getMbBills().getEgBillregister().getCurrentState()!=null && 
	 			model.getMbBills().getEgBillregister().getCurrentState().getValue()!=null && model.getMbBills().getEgBillregister().getCurrentState().getValue()!='CANCELLED'))}">
				dom.get("mb_error").style.display='';     
	    			document.getElementById("mb_error").innerHTML='<s:text name="measurementbook.cancel.failure" />';
	    			window.scroll(0,0);
	    			return false;
				</s:if>

			}
			estimate_id = document.getElementById('estimateId').value;
			if(estimate_id=='' || estimate_id=='-1'){
				dom.get("mb_error").style.display=''; 
            	document.getElementById("mb_error").innerHTML='<s:text name="mbheader.workOrderEstimate.null" />';  
				window.scroll(0,0);
				return false;
			}
			if(dom.get("mb_error").style.display==''){
				window.scroll(0,0);
				return false;   
			}
			dom.get("mb_error").style.display='none'; 
            document.getElementById("mb_error").innerHTML='';  
 }


function disableFields(){
	for(i=0;i<document.measurementBookForm.elements.length;i++){
	   	document.measurementBookForm.elements[i].disabled=true;
		document.measurementBookForm.elements[i].readonly=true;
	}
}

function validateCancel() {
	var msg='<s:text name="measurementbook.cancel.confirm"/>';
	var estNo='<s:property value="model.mbRefNo"/>'; 
	if(!confirmCancel(msg,estNo)) {
		return false;
	}
	else {
		return true;
	}
}

function loadDesignationFromMatrix(){
  	
  		 //var dept=document.getElementById('approverDepartment').options[document.getElementById('approverDepartment').selectedIndex].text;
  		 var currentState = dom.get('currentState').value;
  		 var amountRuleValue =  dom.get('amountRule').value;
  		 var additionalRuleValue =  dom.get('additionalRuleValue').value;
  		 var pendingAction=document.getElementById('pendingActions').value;
  		 var dept=dom.get('departmentName').value;
  		 <s:if test="%{isSpillOverWorks}">
			dept='';
			additionalRuleValue='spillOverWorks';
		 </s:if>

  		 loadDesignationByDeptAndType('MBHeader',dept,currentState,amountRuleValue,additionalRuleValue,pendingAction); 
}

function populateApprover()
{
  getUsersByDesignationAndDept();
}


function validate(text){
/*	if(!validateUser(text))
	  return false; */
	if(!validateHeaderBeforeSubmit(document.measurementBookForm))
		return false;
	if(text!='Approve' && text!='Reject' ){
		if(!validateWorkFlowApprover(text))
		return false;
	}
	if(document.getElementById("actionName").value=='Cancel'){
	   if(!validateCancel())
	      return false;
	}
	enableFields(text);
	if(!validateMbEntryDate())
	  return false;
	return true;
}


function hideDetailsTab(){
  document.getElementById('mbDetailsTable').style.display='none';  
}

function showDetailsTab(){
  clearMessage('mb_error');
  document.getElementById('mb_details').style.display='';
  document.getElementById('mbDetailsTable').style.display='';
  document.getElementById('detailsTab').setAttribute('class','Active');
  document.getElementById('detailsTab').setAttribute('className','Active');  
   document.getElementById('baseSORTable').style.display='';
  document.getElementById('sorHeaderTable').style.display=''; 
  document.getElementById('sorTable').style.display='';
  document.getElementById('nonSorHeaderTable').style.display='';
  document.getElementById('nonSorTable').style.display='';
  hideHeaderTab();
  hideExtraItemsTab();
  hideChangeQuantityTab();
  setCSSClasses('detailsTab','Active');
  setCSSClasses('headerTab','First BeforeActive');
  setCSSClasses('extraItemsTab','');
  setCSSClasses('changeQuantityTab','Last');
}

function hideExtraItemsTab(){  
   document.getElementById('baseSORTable').style.display='none';
  document.getElementById('sorHeaderTable').style.display='none';
  document.getElementById('sorTable').style.display='none';
  document.getElementById('nonSorHeaderTable').style.display='none';
  document.getElementById('nonSorTable').style.display='none';
}

function showExtraItemsTab(){
  clearMessage('mb_error');
  document.getElementById('mb_extraItems').style.display='';
  document.getElementById('extraItemsTab').setAttribute('class','Active');
  document.getElementById('extraItemsTab').setAttribute('className','Active');  
   document.getElementById('baseSORTable').style.display='';
  document.getElementById('sorHeaderTable').style.display=''; 
  document.getElementById('sorTable').style.display='';
  document.getElementById('nonSorHeaderTable').style.display='';
  document.getElementById('nonSorTable').style.display='';
  hideHeaderTab();
  hideDetailsTab();
  hideChangeQuantityTab();
  setCSSClasses('headerTab','First');
  setCSSClasses('detailsTab','BeforeActive');
  setCSSClasses('extraItemsTab','Active');
  setCSSClasses('changeQuantityTab','Last');
}

function hideChangeQuantityTab(){  
	document.getElementById('extraItemsHeaderTable').style.display='none';
	document.getElementById('itemsTable').style.display='none';
}

function showChangeQuantityTab() {
	clearMessage('mb_error');
	document.getElementById('mb_changeQuantity').style.display='';
	document.getElementById('changeQuantityTab').setAttribute('class','Active');
	document.getElementById('changeQuantityTab').setAttribute('className','Active');  
	document.getElementById('extraItemsHeaderTable').style.display='';
	document.getElementById('itemsTable').style.display='';
  	
  	hideHeaderTab();
  	hideDetailsTab();
  	hideExtraItemsTab()
 	setCSSClasses('headerTab','First');
  	setCSSClasses('detailsTab','');
  	setCSSClasses('extraItemsTab','BeforeActive');
  	setCSSClasses('changeQuantityTab','Last Active ActiveLast');
}

function showHeaderTab(){
	document.getElementById('mb_header').style.display='';
 	setCSSClasses('extraItemsTab','');
  setCSSClasses('detailsTab','');
  setCSSClasses('headerTab','First Active'); 
 	setCSSClasses('changeQuantityTab','Last');
  hideDetailsTab();
  hideExtraItemsTab();
  	hideChangeQuantityTab();
}

function hideHeaderTab(){ 
  document.getElementById('mb_header').style.display='none';
}

function setCSSClasses(id,classes){
    document.getElementById(id).setAttribute('class',classes);
    document.getElementById(id).setAttribute('className',classes);
}

</script>
		<s:if test="%{hasErrors()}">
			<div class="errorstyle">
				<s:actionerror />
				<s:fielderror />
			</div>
		</s:if>
		<s:if test="%{hasActionMessages()}">
			<div class="messagestyle">
				<s:property value="%{model.mbRefNo}"/> &nbsp; <s:actionmessage theme="simple" />
			</div>
		</s:if>
		<s:form action="measurementBook" theme="simple" id="measurementBookForm" name="measurementBookForm" onsubmit="return validateForBill(this);">
			<div class="errorstyle" id="mb_error" style="display:none;"></div>
			<s:token/>
			<s:push value="model">
				<div class="navibarshadowwk">
				</div>
				<div class="formmainbox">
					<div class="insidecontent">
						<div class="rbroundbox2">
							<div class="rbtop2"><div></div></div>
							<div class="rbcontent2">
								
								<s:hidden name="id" id="id" />
								<s:hidden id="additionalRuleValue" name="additionalRuleValue" value="%{additionalRuleValue}" />
								<s:hidden name="model.documentNumber" id="docNumber" />
								<table width="100%" border="0" cellspacing="0" cellpadding="0">
							          <tr>
							            <td><div id="header">
											<ul id="Tabs">
												<li id="headerTab" class="First Active"><a id="header_1" href="#" onclick="showHeaderTab();">Header</a></li>
									 			<li id="detailsTab" class=""><a id="header_2" href="#" onclick="showDetailsTab();">MB Details</a></li>											
									 			<li id="extraItemsTab" class="" style="display:none" ><a id="header_3" href="#" onclick="showExtraItemsTab();"><s:text name="revisionEstimate.extraItems" /></a></li>
									 			<li id="changeQuantityTab" class="Last" style="display:none"><a id="header_4" href="#" onclick="showChangeQuantityTab();"><s:text name="revisionEstimate.changeQuantity" /></a></li>
											</ul>
							            </div></td>
							          </tr>
							      	<tr><td>&nbsp;</td></tr>
							          <tr>
							            <td>
							            <div id="mb_header">
								<s:hidden name="departmentName" id="departmentName" value="%{departmentName}"/>
								<%@ include file='measurementBook-header.jsp'%>
							            </div>            
							            </td> 
							          </tr>            
							          <tr>
							            <td>
							            <div id="mb_details" style="display:none;"> 
								<%@ include file='measurementBook-details.jsp'%>
								<%@ include file='measurementBook-mSheet.jsp'%>
							</div>
							            </td>
							          </tr>
							          <tr>
							            <td>
							            <div id="mb_extraItems" style="display:none;"> 
							            	<%@ include file='mb-sor.jsp'%>
							            	<%@ include file='mb-nonSor.jsp'%>
							            	<%@ include file='mbExtraItem-mSheet.jsp'%>
							            </div>
							            </td>
							          </tr>
							          <tr>
							            <td>
							            <div id="mb_changeQuantity" style="display:none;"> 
							            	<%@ include file='mb-changeQuantity.jsp'%>
							            	<%@ include file='mbChangeQuantity-mSheet.jsp'%>
							            </div>
							            </td>
							          </tr>
							          							          
							          <tr>
								            <td><table width="100%" align="center" border="0" cellspacing="0" cellpadding="0">
								            <s:hidden id="isTenderPercentageType" name="isTenderPercentageType" />
											<s:hidden id="mbPercentagelevel" name="mbPercentagelevel" />
						
								            <s:if test='%{mbPercentagelevel=="TotalMBValue" && isTenderPercentageType==true}'>
								           	<tr>
									            <td width="17%" class="whiteboxwk"><s:text name="mb.tenderpercentagerate" />:</td>
								                <td width="17%" class="whitebox2wk"><s:select id="sign" name="sign" list="{'+','-'}" value="%{sign}" disabled="true"/><s:textfield id="tenderPercentage" name="tenderPercentage" value="%{tenderPercentage.abs()}" maxlength="9" size="10" readonly="true"/>&nbsp;<s:text name="mb.percentagesymbol" />
								              	</td>
									            <td class="whiteboxwk">&nbsp;</td>
									            <td class="whiteboxwk">&nbsp;</td>
								            </tr>
								            </s:if>
								           	<tr>
									            <td width="17%" class="whiteboxwk"><s:text name="mb.netamount" />:</td>
								                <td width="17%" class="whitebox2wk"><s:textfield name="mbValue" value="%{mbValue}"  id="mbValue" cssClass="selectamountwk" readonly="true" align="right" />
								              	<s:if test='%{mbPercentagelevel=="TotalMBValue" && isTenderPercentageType==true}'>(Rate applied on Total + Extra Items Total)</s:if>
								              	</td>   
									            <td class="whiteboxwk">&nbsp;</td>
									            <td class="whiteboxwk">&nbsp;</td>
								            </tr>
								            </table>
								            </td>
								      </tr>
							     </table>
								
								
								
							</div>
 	 						<s:if test="%{sourcepage!='search' and mode!='search'}" >
							<div id="manual_workflow">
				         			<c:set var="approverHeadCSS" value="headingwk" scope="request" />
				         			<c:set var="approverCSS" value="bluebox" scope="request" />
									<%@ include file="/commons/commonWorkflow.jsp"%>
						    </div>
         					</s:if> 
						    <tr>
				    			<td colspan="4"><div align="right" class="mandatory">* <s:text name="message.mandatory" /></div></td>
							</tr>		
							<div class="rbbot2"><div></div>
							</div>
						</div>						 
					</div>
				</div>
				<div class="buttonholderwk" id="divButRow1" name="divButRow1">
					<input type="hidden" name="actionName" id="actionName" />
	
					<!-- Action buttons have to displayed only if the page is directed from the inbox -->
	
	<s:if test="%{(sourcepage=='inbox' || model.currentState==null || model.currentState.value=='NEW' || model.currentState.value=='REJECTED') && mode !='view' || hasErrors() || hasActionMessages()}">

		 	<s:if test="%{model.id==null || model.currentState.value=='NEW'}">
 				<s:submit type="submit" cssClass="buttonfinal"
						value="Save" id="Save" name="Save"
						method="save"
						onclick="enableFields('Save');document.measurementBookForm.actionName.value='Save';checkActivity();return validate('Save');" />
 			</s:if>
 			<s:iterator value="%{getValidActions()}" var="name">
				<s:if test="%{name!=''}">
					<s:submit type="submit" cssClass="buttonfinal"
						value="%{name}" id="%{name}" name="%{name}"
						method="save"
							onclick="document.measurementBookForm.actionName.value='%{name}';checkActivity();return validate('%{name}');" />
				</s:if>
			</s:iterator>  
		

<!-- 	<s:iterator value="%{validActions}">
							<s:if test="%{description!=''}">
								<s:if test="%{name != null && name == 'reject'}">
					<s:submit type="submit" cssClass="buttonfinal"	value="%{description}" id="%{name}" name="%{name}" 	method="%{name}" onclick="document.measurementBookForm.actionName.value='%{name}';return validate('%{name}');" />
								</s:if>
								<s:elseif test="%{description == 'CANCEL' && model.id != null}">
					<s:submit type="submit" cssClass="buttonfinal" value="%{description}" id="%{name}" name="%{name}"  method="cancel" onclick="enableFields('%{name}');document.measurementBookForm.actionName.value='%{name}';return validateCancel();"/>
	  							</s:elseif>
								<s:else>
					<s:submit type="submit" cssClass="buttonfinal" value="%{description}" id="%{name}" name="%{name}" method="save"  onclick="document.measurementBookForm.actionName.value='%{name}';checkActivity();return validate('%{name}');validateForm();" />
								</s:else>
							</s:if>
		</s:iterator>  -->	
					</s:if>
					
					<input type="button" class="buttonfinal" value="CLOSE" id="closeButton" name="button" onclick="confirmClose('<s:text name='measurementbook.close.confirm'/>');" />
						<s:if test="%{model.id!=null}">
							<input type="button" onclick="window.open('${pageContext.request.contextPath}/measurementbook/measurementBookPDF.action?measurementBookId=<s:property value='%{model.id}'/>');" class="buttonpdf" value="VIEW PDF" id="pdfButton" name="pdfButton" />
			<input type="button" onclick="window.open('${pageContext.request.contextPath}/measurementbook/measurementBook!viewMBMeasurementSheetPdf.action?id=<s:property value='%{model.id}'/>');" class="buttonpdf" value="VIEW MEASUREMENT SHEET PDF" id="mSheetPdfButton" name="mSheetPdfButton"/>
						</s:if>
					 <s:if test="%{mode =='view' ||  sourcepage=='search' || mode=='search'}">  	
  						<input type="submit" class="buttonadd" value="View Document" id="mbDocViewButton" onclick="viewDocumentManager(dom.get('docNumber').value);return false;" />
  					</s:if>
  					<s:else>
						<input type="submit" class="buttonadd" value="Upload Document" id="mvDocUploadButton" onclick="showDocumentManager();return false;" />
  					</s:else>
				</div>
			</s:push>
		</s:form>
		
		<!-- for enable or disabling functionalities based on states -->
		<script>
		function disableForm(){
		    <s:if test="%{(sourcepage=='search' || mode=='search' || mode=='view') || (sourcepage=='inbox' && model.currentState.value!='NEW' && model.currentState.value!='REJECTED')}" >
		        for(i=0;i<document.measurementBookForm.elements.length;i++){
			        if(document.measurementBookForm.elements[i]!='mbMSheetDtls'){
					document.measurementBookForm.elements[i].disabled=true;
					document.measurementBookForm.elements[i].readonly=true;
					}
				} 
				//nonSorDataTable.removeListener('cellClickEvent');
        		//sorDataTable.removeListener('cellClickEvent');    
        		//extraItemsDataTable.removeListener('cellClickEvent'); 
				links=document.measurementBookForm.getElementsByTagName("a");  
				for(i=0;i<links.length;i++){    
					if(links[i].id.indexOf("header_")!=0)
					links[i].onclick=function(){return false;};
				}
		    </s:if>
	    }
		<s:if test="%{model.currentState.value=='CANCELLED'}">
			clearCumulativeDataIfCancelled();	
		</s:if>
		<s:if test="%{(sourcepage=='search' || mode=='search' || mode=='view')}">
			disableForm();
			document.measurementBookForm.closeButton.readonly=false;
			document.measurementBookForm.closeButton.disabled=false;
			document.measurementBookForm.mbDocViewButton.disabled=false;
			document.measurementBookForm.pdfButton.readonly=false;
			document.measurementBookForm.pdfButton.disabled=false;	
			document.measurementBookForm.mSheetPdfButton.readonly=false;
			document.measurementBookForm.mSheetPdfButton.disabled=false;	
			document.measurementBookForm.mbDocViewButton.disabled=false;
			//hideElements(['workflowDetials']);
		</s:if> 
		<s:if test="%{sourcepage=='inbox'}">
		     <s:if test="%{model.currentState.value=='CREATED' || model.currentState.value=='CHECKED' || model.currentState.value=='RESUBMITTED'}">
		     	disableForm();
				if(document.getElementById("Approve")!=null){
					document.measurementBookForm.Approve.disabled=false;
				}
				if(document.getElementById("approverPositionId")!=null){
					document.measurementBookForm.approverPositionId.disabled=false;
				}
				if(document.getElementById("approverDesignation")!=null){
					document.measurementBookForm.approverDesignation.disabled=false;
				}
				if(document.getElementById("approverDepartment")!=null){
					document.measurementBookForm.approverDepartment.disabled=false;
				}
				if(document.getElementById("Reject")!=null){
					document.measurementBookForm.Reject.disabled=false;
				}
				if(document.getElementById("Forward")!=null){
					document.measurementBookForm.Forward.disabled=false;
				}
				
				if(document.getElementById("approverComments")!=null){
					document.measurementBookForm.approverComments.disabled=false;
				}
				
				
				document.measurementBookForm.closeButton.readonly=false;
				document.measurementBookForm.closeButton.disabled=false;
				document.measurementBookForm.pdfButton.readonly=false;
				document.measurementBookForm.pdfButton.disabled=false;
				document.measurementBookForm.mSheetPdfButton.readonly=false;
			    document.measurementBookForm.mSheetPdfButton.disabled=false;	
				document.measurementBookForm.mvDocUploadButton.disabled=false;
				document.measurementBookForm.mvDocUploadButton.disabled=false;
				
				
			 </s:if>
			 <s:elseif test="%{model.currentState.value=='APPROVED' || model.currentState.value=='CANCELLED'}">
				disableForm();
				document.measurementBookForm.closeButton.readonly=false;
				document.measurementBookForm.closeButton.disabled=false;
				document.measurementBookForm.pdfButton.readonly=false;
				document.measurementBookForm.pdfButton.disabled=false;	
				document.measurementBookForm.mSheetPdfButton.readonly=false;
			    document.measurementBookForm.mSheetPdfButton.disabled=false;	
				document.measurementBookForm.mvDocUploadButton.disabled=false;
				document.measurementBookForm.mvDocUploadButton.disabled=false;
			 </s:elseif>
			 <s:if test="%{model.currentState.value!='END' || hasErrors()}">
		  		if(document.getElementById("approverPositionId")!=null){
			  		document.measurementBookForm.approverPositionId.readonly=false;
					document.measurementBookForm.approverPositionId.disabled=false;
					document.measurementBookForm.approverDesignation.readonly=false;
					document.measurementBookForm.approverDesignation.disabled=false;
					document.measurementBookForm.approverDepartment.readonly=false;
					document.measurementBookForm.approverDepartment.disabled=false;
				}
		  	</s:if>
			 <s:if test="%{model.id!=null && (model.currentState.value=='CREATED' || model.currentState.value=='CHECKED' || model.currentState.value=='RESUBMITTED')}">
				//document.getElementById('approverCommentsRow').style.display='';
			 	document.getElementById('approverComments').readonly=false;	
	     		document.getElementById('approverComments').disabled=false;	
			 </s:if>
		/*	  <s:if test="%{model.id!=null && model.currentState.nextAction=='Pending for Approval'}">
			 	hideElements(['workflowDetials']);
	      		showElements(['approverCommentsRow']);
	      	 </s:if>*/
		</s:if>	
		<s:if test="%{sourcepage=='inbox' && mBWorkflowModifyDesignation!=null && model.currentState.owner.desigId.designationName==mBWorkflowModifyDesignation}">
			<s:iterator id="mbIterator" value="mbDetails" status="row_status">
				var record = mbDataTable.getRecord(parseInt('<s:property value="#row_status.index"/>'));								
		        var column = mbDataTable.getColumn('quantity');
				dom.get(column.getKey()+record.getId()).readonly=false;
				dom.get(column.getKey()+record.getId()).disabled=false;	
				
			    var orderNumbercolumn = mbDataTable.getColumn('orderNumber');
				dom.get(orderNumbercolumn.getKey()+record.getId()).readonly=false;
				dom.get(orderNumbercolumn.getKey()+record.getId()).disabled=false;
				
				var mbdetailsDatecolumn = mbDataTable.getColumn('mbdetailsDate');
				dom.get(mbdetailsDatecolumn.getKey()+record.getId()).readonly=false;
				dom.get(mbdetailsDatecolumn.getKey()+record.getId()).disabled=false;								
			</s:iterator>
		</s:if>
	</script>
	</body>
</html>
