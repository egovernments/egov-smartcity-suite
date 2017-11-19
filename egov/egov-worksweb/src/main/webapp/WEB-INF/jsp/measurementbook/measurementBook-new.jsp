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
	<title><s:text name='measurementbook.title'/></title>
	<body id="home" onload="checkRCEst();populateDesignation();disableenable();disableEnableOther('nonTendered');disableEnableOther('lumpSum');calculateMBTotal();noBack();" onpageshow="if(event.persisted) noBack();" onunload="">
	<script src="<egov:url path='resources/js/works.js?${app_release_no}'/>"></script>

<script>
jQuery("#loadingMask").remove();
window.history.forward(1);
function noBack() {
	window.history.forward(); 
}
function checkRCEst(){
	if(dom.get("isRCEstimate").value=='yes'){
		document.getElementById('nonTenderedLumpSumpTab').style.visibility='hidden';
	}
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
		  test=actionNameVar;
			for(i=0;i<document.measurementBookForm.elements.length;i++){
	        	document.measurementBookForm.elements[i].disabled=false;
	        	document.measurementBookForm.elements[i].readonly=false;	       
			}
		}		

		function validateForBill(){

			if(test == 'cancel'){
	 			<s:if test="%{model !=null && (model.getEgBillregister()!=null && (model.getEgBillregister().getStatus()!=null && 
	 			   model.getEgBillregister().getStatus().getCode()!='CANCELLED'))}">
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

			var forms = document.getElementsByName('measurementBookForm');
			
			if(!validateHeaderBeforeSubmit(forms[0])){
				return false;
			}
			
			dom.get("mb_error").style.display='none'; 
            document.getElementById("mb_error").innerHTML='';  

            jQuery(".commontopyellowbg").prepend('<div id="loadingMask" style="display:none;overflow:none;scroll:none;" ><img src="/egi/images/bar_loader.gif"> <span id="message">Please wait....</span></div>')
    	    doLoadingMask();
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

function validate(text){
	if(!validateUser(text))
		return false;
	enableFields(text); 
	
	return true;
}

function showHeaderTab(){
	  document.getElementById('mbHeader_tab').style.display='';
	  setCSSClasses('headerTab','First Active');
	  setCSSClasses('tenderedDetailsTab','');
	  setCSSClasses('nonTenderedLumpSumpTab','Last');
	  hideTenderedDetailsTab();
	  hideNonTenderedLumpSumTab();
}

function hideHeaderTab(){
	  document.getElementById('mbHeader_tab').style.display='none';
}

function showTenderedDetailsTab(){
	  document.getElementById('mbTenderedDetails_tab').style.display='';	
	  document.getElementById('tenderedDetailsTab').setAttribute('class','Active');
	  document.getElementById('tenderedDetailsTab').setAttribute('className','Active');   
	  hideHeaderTab();
	  hideNonTenderedLumpSumTab();
	  setCSSClasses('headerTab','First BeforeActive');
	  setCSSClasses('tenderedDetailsTab','Active');
   	  setCSSClasses('nonTenderedLumpSumpTab','Last');
}

function hideTenderedDetailsTab(){
	  document.getElementById('mbTenderedDetails_tab').style.display='none';
}

function showNonTenderedLumpSumTab(){
	    hideTenderedDetailsTab();
	    hideHeaderTab();
	    document.getElementById('mbNonTenderedLumpsum_tab').style.display='';
	    setCSSClasses('headerTab','First');
	    setCSSClasses('tenderedDetailsTab','BeforeActive');
	    setCSSClasses('nonTenderedLumpSumpTab','Last Active ActiveLast');
}
function setCSSClasses(id,classes){
	    document.getElementById(id).setAttribute('class',classes);
	    document.getElementById(id).setAttribute('className',classes);

}
function hideNonTenderedLumpSumTab(){
	  document.getElementById('mbNonTenderedLumpsum_tab').style.display='none';
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
		<s:token/>
			<div class="errorstyle" id="mb_error" style="display:none;"></div>
			<s:push value="model">
				<div class="navibarshadowwk">
				</div>
				<div class="formmainbox">
					<div class="insidecontent">
						<div class="rbroundbox2">
							<div class="rbtop2"><div></div></div>
							<div class="rbcontent2">
								<!--<div class="datewk">
									<span class="bold">Today</span>
									<egov:now />
								</div>-->
								<s:hidden name="id" />
								<s:hidden name="model.documentNumber" id="docNumber" />
								<s:hidden name="isRCEstimate" id="isRCEstimate" value="%{isRCEstimate}" />
								<table width="100%" border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td><div id="header">
										<ul id="Tabs">
											<li id="headerTab" class="First Active"><a id="header_1" href="#" onclick="showHeaderTab();"><s:text name="measurementbook.header" /></a></li>
											<li id="tenderedDetailsTab" class="Befor"><a id="header_2" href="#" onclick="showTenderedDetailsTab();"><s:text name="measurementbook.tendered" /></a></li>
											<li id="nonTenderedLumpSumpTab" class="Last"><a id="header_3" href="#" onclick="showNonTenderedLumpSumTab();"><s:text name="measurementbook.non.tendered" /></a></li>
										</ul>
								        </div>
								    </td>
								</tr>
								<tr><td>&nbsp;</td></tr>
								<tr>
						            <td>
						            <div id="mbHeader_tab" >
						                 <%@ include file="measurementBook-header.jsp"%>                
						            </div>
						            </td>
						        </tr>
						        <tr>
						            <td>
						            <div id="mbTenderedDetails_tab" style="display:none;">
						                 <%@ include file="measurementBook-details.jsp"%>                
						            </div>
						            </td>
						        </tr>
						        <tr>
						            <td>
						            <div id="mbNonTenderedLumpsum_tab" style="display:none;">
						                 <%@ include file="measurementBook-nonTenderedAndLumpSumItems.jsp"%>                
						            </div>
						            </td>
						        </tr>
					            <tr>
						            <td>
							            <table width="100%" align="center" border="0" cellspacing="0" cellpadding="0">
								            <tr>
												<td  class="whiteboxwk"><s:text name="measurementbook.amount.total" />:</td>
								                <td  class="whitebox2wk">
								                	<input name="mbAmount"   id="mbAmount" class="selectamountwk" readonly="true" align="right" />
								                </td>
								                
								            </tr>
								         </table>
							        </td>
					            </tr>						        
							</div>
							<div id="manual_workflow">
						         <%@ include file="workflowApproval.jsp"%>   
						    </div>
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

					<s:if test="%{((sourcepage=='inbox' || model.egwStatus==null || model.egwStatus.code=='NEW'  
						|| model.egwStatus.code=='REJECTED') && mode !='view' || hasErrors() || hasActionMessages())}">
						
						<s:iterator value="%{validActions}">
							<s:if test="%{description!=''}">
								<s:if test="%{name != null && name == 'reject'}">
							
									<s:submit type="submit" cssClass="buttonfinal"
										value="%{description}" id="%{name}" name="%{name}"
										method="%{name}"
										onclick="document.measurementBookForm.actionName.value='%{name}';return validate('%{name}');" />
								</s:if>
								<s:elseif test="%{description == 'CANCEL' && model.id != null}">
							
									<s:submit type="submit" cssClass="buttonfinal" value="%{description}" id="%{name}" name="%{name}" 
									 method="cancel" onclick="enableFields('%{name}');document.measurementBookForm.actionName.value='%{name}';return validateCancel();"/>
	  							</s:elseif>
								<s:else>
							
									<s:submit type="submit" cssClass="buttonfinal"
										value="%{description}" id="%{name}" name="%{name}"
										method="save"
										onclick="document.measurementBookForm.actionName.value='%{name}';return validate('%{name}'); validateForm();" />
								</s:else>
							</s:if>
						</s:iterator>
					</s:if>
					<s:if test="%{mode!='search'}">
						<input type="button" class="buttonfinal" value="CLOSE" id="closeButton" name="button" onclick="confirmClose('<s:text name='measurementbook.close.confirm'/>');" />
					</s:if>
					<s:else>
						<input type="button" class="buttonfinal" value="CLOSE" id="closeButton" name="button" onclick="window.close();" />					
					</s:else>
						<s:if test="%{model.id!=null}">
							<input type="button" onclick="window.open('${pageContext.request.contextPath}/measurementbook/measurementBookPDF.action?measurementBookId=<s:property value='%{model.id}'/>');" class="buttonpdf" value="VIEW PDF" id="pdfButton" name="pdfButton" />
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
		
		<!-- for enable or disabling functionalities based on status -->
		<script>
		function disableForm(){
		    <s:if test="%{(sourcepage=='search' || mode=='search' || mode=='view') || (sourcepage=='inbox' && model.egwStatus.code!='NEW' && model.egwStatus.code!='REJECTED')}" >
		        for(i=0;i<document.measurementBookForm.elements.length;i++){
					document.measurementBookForm.elements[i].disabled=true;
					document.measurementBookForm.elements[i].readonly=true;
				} 
				mbDataTable.removeListener('cellClickEvent');
				mbNTenderedDataTable.removeListener('cellClickEvent');
				mbLSDataTable.removeListener('cellClickEvent');
				links=document.measurementBookForm.getElementsByTagName("a");
				for(i=0;i<links.length;i++){
					if(links[i].id=='header_1' || links[i].id=='header_2' || links[i].id=='header_3')
						continue;
					else	  
						links[i].onclick=function(){return false;};
				}
		    </s:if>
	    }
		<s:if test="%{model.egwStatus != null && model.egwStatus.code=='CANCELLED'}">
			clearCumulativeDataIfCancelled();
			clearCumulativeDataIfCancelledNTLS();	
		</s:if>
		<s:if test="%{(sourcepage=='search' || mode=='search' || mode=='view')}">
			disableForm();
			document.measurementBookForm.closeButton.readonly=false;
			document.measurementBookForm.closeButton.disabled=false;
			document.measurementBookForm.mbDocViewButton.disabled=false;
			document.measurementBookForm.pdfButton.readonly=false;
			document.measurementBookForm.pdfButton.disabled=false;	
			document.measurementBookForm.mbDocViewButton.disabled=false;
			hideElements(['workflowDetials']);
		</s:if> 
		<s:if test="%{sourcepage=='inbox'}">
		     <s:if test="%{model.egwStatus!=null 
			     && (model.egwStatus.code=='CREATED' || model.egwStatus.code=='CHECKED' || model.egwStatus.code=='RESUBMITTED')}">
		     	disableForm();
				document.measurementBookForm.approval.readonly=false;
				document.measurementBookForm.approval.disabled=false;
				document.measurementBookForm.reject.readonly=false;
				document.measurementBookForm.reject.disabled=false;
				document.measurementBookForm.closeButton.readonly=false;
				document.measurementBookForm.closeButton.disabled=false;
				document.measurementBookForm.pdfButton.readonly=false;
				document.measurementBookForm.pdfButton.disabled=false;
				document.measurementBookForm.mvDocUploadButton.disabled=false;
				document.measurementBookForm.mvDocUploadButton.disabled=false;
				
				
			 </s:if>
			 <s:elseif test="%{model.egwStatus!=null 
				 && (model.egwStatus.code=='APPROVED' || model.egwStatus.code=='CANCELLED')}">
				disableForm();
				document.measurementBookForm.closeButton.readonly=false;
				document.measurementBookForm.closeButton.disabled=false;
				document.measurementBookForm.pdfButton.readonly=false;
				document.measurementBookForm.pdfButton.disabled=false;	
				document.measurementBookForm.mvDocUploadButton.disabled=false;
				document.measurementBookForm.mvDocUploadButton.disabled=false;
			 </s:elseif>
			 <s:if test="%{model.currentState.value!='END' || hasErrors()}">
		  		document.measurementBookForm.departmentid.readonly=false;
				document.measurementBookForm.departmentid.disabled=false;
				document.measurementBookForm.designationId.readonly=false;
				document.measurementBookForm.designationId.disabled=false;
				document.measurementBookForm.approverUserId.readonly=false;
				document.measurementBookForm.approverUserId.disabled=false;
		  	</s:if>
			 <s:if test="%{model.id!=null 
				 && (model.egwStatus!=null && (model.egwStatus.code=='CREATED' || model.egwStatus.code=='CHECKED' || model.egwStatus.code=='RESUBMITTED'))}">
				document.getElementById('approverCommentsRow').style.display='';
			 	document.getElementById('approverComments').readonly=false;	
	     		document.getElementById('approverComments').disabled=false;	
			 </s:if>
			  <s:if test="%{model.id!=null && model.currentState.nextAction=='Pending for Approval'}">
			 	hideElements(['workflowDetials']);
	      		showElements(['approverCommentsRow']);
	      	 </s:if>
		</s:if>
		<s:if test="%{sourcepage=='inbox' && mBWorkflowModifyDesignation!=null && model.currentState.owner.deptDesigId.desigId.designationName==mBWorkflowModifyDesignation}">
			var records = mbDataTable.getRecordSet();
			for(var i=0;i<records.getLength();i++){
				var record = mbDataTable.getRecord(i);								
		        var column = mbDataTable.getColumn('quantity');
		        dom.get(column.getKey()+record.getId()).readonly=false;
				dom.get(column.getKey()+record.getId()).disabled=false;
				var orderNumbercolumn = mbDataTable.getColumn('orderNumber');
				dom.get(orderNumbercolumn.getKey()+record.getId()).readonly=false;
				dom.get(orderNumbercolumn.getKey()+record.getId()).disabled=false;
				
				var mbdetailsDatecolumn = mbDataTable.getColumn('mbdetailsDate');
				dom.get(mbdetailsDatecolumn.getKey()+record.getId()).readonly=false;
				dom.get(mbdetailsDatecolumn.getKey()+record.getId()).disabled=false;
			}
			if(mbNTenderedDataTable!=null && mbNTenderedDataTable.getRecordSet()!=null )
			{
				records = mbNTenderedDataTable.getRecordSet();
				if(records.getLength()>0)
				{
					for(var i=0;i<records.getLength();i++){
						var record = mbNTenderedDataTable.getRecord(i);								
				        var column = mbNTenderedDataTable.getColumn('quantity');
				        dom.get(column.getKey()+record.getId()).readonly=false;
						dom.get(column.getKey()+record.getId()).disabled=false;
						var orderNumbercolumn = mbNTenderedDataTable.getColumn('orderNumber');
						dom.get(orderNumbercolumn.getKey()+record.getId()).readonly=false;
						dom.get(orderNumbercolumn.getKey()+record.getId()).disabled=false;
						
						var mbdetailsDatecolumn = mbNTenderedDataTable.getColumn('mbdetailsDate');
						dom.get(mbdetailsDatecolumn.getKey()+record.getId()).readonly=false;
						dom.get(mbdetailsDatecolumn.getKey()+record.getId()).disabled=false;
					}		
				}	
			}
			if(mbLSDataTable!=null && mbLSDataTable.getRecordSet()!=null )
			{
				records = mbLSDataTable.getRecordSet();
				if(records.getLength()>0)
				{
					for(var i=0;i<records.getLength();i++){
						var record = mbLSDataTable.getRecord(i);								
				        var column = mbLSDataTable.getColumn('quantity');
				        dom.get(column.getKey()+record.getId()).readonly=false;
						dom.get(column.getKey()+record.getId()).disabled=false;
						var orderNumbercolumn = mbLSDataTable.getColumn('orderNumber');
						dom.get(orderNumbercolumn.getKey()+record.getId()).readonly=false;
						dom.get(orderNumbercolumn.getKey()+record.getId()).disabled=false;
						
						var mbdetailsDatecolumn = mbLSDataTable.getColumn('mbdetailsDate');
						dom.get(mbdetailsDatecolumn.getKey()+record.getId()).readonly=false;
						dom.get(mbdetailsDatecolumn.getKey()+record.getId()).disabled=false;
					}		
				}	
			}
		</s:if>
	</script>
	</body>
</html>
