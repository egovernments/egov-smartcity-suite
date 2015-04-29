<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags" %>
<html>
	<head>
		<meta http-equiv="X-UA-Compatible" content="IE=8" />
		<title>
			<s:text name="depositworks.roadcut.title" />
		</title>
	</head>	
		<script src="<egov:url path='js/works.js'/>"></script>
		<script src="<egov:url path='js/helper.js'/>"></script>
		<script type="text/javascript"> 
			function load() {	
				var bpaCutRadioStatus = false;
				if(document.getElementById("depositWorksCategoryBPA") != null) {
					bpaCutRadioStatus = document.getElementById("depositWorksCategoryBPA").checked;
				}
				var emergencyCutRadioStatus = false;
				if(document.getElementById("depositWorksCategoryEMERGENCYCUT") != null) {
					emergencyCutRadioStatus = document.getElementById("depositWorksCategoryEMERGENCYCUT").checked;
				}
				var individualRadioStatus = document.getElementById("individualOrOrgRadiotrue").checked;

				if(!(individualRadioStatus && bpaCutRadioStatus)) {
					hideColumn('Add');
					hideColumn('Delete');
					var showScheme=false;
					var typeOfCutStr = "<s:property value='applicationRequest.depositWorksType.name' />";
					<s:iterator value="schemeBasedDWTypeList" var="dwTypeList">
						if (typeOfCutStr=="<s:property value='#dwTypeList' />") {
							showScheme=true;
						}
					</s:iterator>  
					if(showScheme) {
						document.getElementById("isSchemeBasedTextTD").style.visibility='visible';
						document.getElementById("isSchemeBasedTD").style.visibility='visible';
					}
				
					if(emergencyCutRadioStatus) {
						document.getElementById("purposeOfCutLabelTD").style.visibility='visible';
						document.getElementById("purposeOfCutFieldTD").style.visibility='visible';
						if(document.getElementById("isSchemeBasedRadiotrue")!=null) {
							document.getElementById("isSchemeBasedRadiotrue").disabled=true;
						}
					}
					<s:if test="%{applicationRequest.isSchemeBased==true}">
						showColumn('Add');	   
				    	showColumn('Delete');
						document.getElementById("isSchemeBasedTextTD").style.visibility='visible';
						document.getElementById("isSchemeBasedTD").style.visibility='visible';
						document.getElementById("schemenameTextTD").style.visibility='visible';
						document.getElementById("schemenameTD").style.visibility='visible';
						document.getElementById("schemedetailsTextTD").style.visibility='visible';
						document.getElementById("schemedetailsTD").style.visibility='visible';
					</s:if>
				
					<s:if test="%{depositCode!=null}">
						document.getElementById("depCodeTable").style.display='block';
			  			document.roadCutForm.depCode.checked=true;
			 			document.roadCutForm.depCode.value=true;
			 			document.roadCutForm.depCode.disabled =true;
				    	document.roadCutForm.codeName.disabled =true;
				    	document.roadCutForm.description.disabled =true;
				    	document.roadCutForm.fundId.disabled =true;
				    	document.roadCutForm.financialYearId.disabled =true;
				    	document.roadCutForm.fundSourceId.disabled =true;
					</s:if>
					document.roadCutForm.estimatedCost.value=roundTo(document.roadCutForm.estimatedCost.value);
					//Show or hide individual or organization 
					document.getElementById('organizationElement').style.display='none';
					document.getElementById('organizationLabel').style.display='none';
					document.getElementById('individualElement').style.display='';
					document.getElementById('individualLabel').style.display='';
					var applicantName = '<s:property value="%{model.applicationRequest.applicant.name}" />';
					var orgznId ='<s:property value="%{model.applicationRequest.applicant.organization.id}" />';
					if(applicantName!='' && orgznId=='') {
						document.getElementById('organizationElement').style.display='none';
						document.getElementById('organizationLabel').style.display='none';
						document.getElementById('individualElement').style.display='';
						document.getElementById('individualLabel').style.display='';
						document.getElementById("serviceDeptTR").style.display='none';
						document.getElementById("newOrgznServiceDeptTR").style.display='';						
					}	
					if(applicantName=='' && orgznId!='') {
						document.getElementById('individualElement').style.display='none';
						document.getElementById('individualLabel').style.display='none';
						document.getElementById('organizationElement').style.display='';
						document.getElementById('organizationLabel').style.display='';
						document.getElementById("serviceDeptTR").style.display='';
						document.getElementById("newOrgznServiceDeptTR").style.display='none';
						document.getElementById("serviceDept").disabled="true";					
					}
					<s:if test="%{applicationRequest.id==null}">
						setTypeOfCut('<s:property value = "%{applicationRequest.applicant.organization.id}" />', false);
					</s:if>	
					toggleOrgnAndServiceDept();		
					if(document.getElementById("depositWorksCategory")!=null) {
						toggleShowBPADetails(document.getElementById("depositWorksCategory"));
					} 
		
					jQuery.noConflict();
					<s:if test="applicationRequest.depositWorksCategory==@org.egov.works.models.depositWorks.DepositWorksCategory@BPA">
						jQuery("#depCodeCheck").hide();
					</s:if>
				}		
				if(individualRadioStatus) {
					jQuery(document).ready( jQuery('#depositWorksCategoryREPAIRSANDMAINTENANCE').hide());
					jQuery(document).ready( jQuery('label[for="depositWorksCategoryREPAIRSANDMAINTENANCE"]').hide());
					jQuery(document).ready( jQuery('#depositWorksCategoryEMERGENCYCUT').hide());
					jQuery(document).ready( jQuery('label[for="depositWorksCategoryEMERGENCYCUT"]').hide());
				}

				var hideForward = <s:property value="%{forwardNotApplicable}"/>;
				if(hideForward==true && document.getElementById('Forward')!=null && document.getElementById('Reject')!=null) {
					jQuery("#Forward").hide();
					jQuery("#Reject").hide();
				}
				if(hideForward==true && document.getElementById('Approve')!=null) {
					jQuery("#Approve").hide();
				}
				if(dom.get("citizenId").value != '' && !individualRadioStatus) {
					appConfOrgn1 = '<s:property value='bpaOrganizations[0]' />';
					appConfOrgn2 = '<s:property value='bpaOrganizations[1]' />';
					organizationName = '<s:property value='applicationRequest.applicant.organization.name'/>';
					if(organizationName!=appConfOrgn1 && organizationName!=appConfOrgn2){
						jQuery(document).ready( jQuery('#depositWorksCategoryBPA').hide());
					 	jQuery(document).ready( jQuery('label[for="depositWorksCategoryBPA"]').hide());
					}
				}
					
				if(bpaCutRadioStatus && !individualRadioStatus) {
					document.getElementById("purposeOfCutLabelTD").style.visibility='visible';
					document.getElementById("purposeOfCutFieldTD").style.visibility='visible';
					if(document.getElementById("isSchemeBasedRadiotrue")!=null) {
						document.getElementById("isSchemeBasedRadiotrue").disabled=true;
					}
					document.getElementById("newOrgznServiceDeptTR").style.display='none';
					document.getElementById("mirrorApplicantOrgznServDeptTR").style.display='none';
					document.getElementById("serviceDeptTR").style.display='';
				}
				if(individualRadioStatus){
					if(!bpaCutRadioStatus){
						dom.get("roadCutDetails_div").style.display='block';
						dom.get("newBPADetails_div").style.display='none';
						dom.get("bpaButtons_div").style.display='none';
					} else {
						dom.get("roadCutDetails_div").style.display='none';
						dom.get("newBPADetails_div").style.display='block';
						dom.get("bpaButtons_div").style.display='block';
						dom.get("normalbuttons_div").style.display='none';
						<s:if test="%{mode=='view'}">
							if(document.roadCutForm.bpaCloseButton!=null){
								document.roadCutForm.bpaCloseButton.readonly=false;
								document.roadCutForm.bpaCloseButton.disabled=false;
							}
							if(document.roadCutForm.bpaDocViewButton!=null){
						    	document.roadCutForm.bpaDocViewButton.readonly=false; 
								document.roadCutForm.bpaDocViewButton.disabled=false; 
					    	}
						</s:if>
						loadBPA();
						enableOrDisableElementsBPA();
					}
					<s:if test="%{applicationRequest.id==null}">
					if(dom.get("citizenId").value == ''){
						<s:iterator value="serviceConnectionTypes" status="rowstatus">
							serviceConnections[<s:property value="%{#rowstatus.index}"/>] = '<s:property value="id"/>';
						</s:iterator>
					}
					</s:if>
				}
			}

			function toggleOrgnAndServiceDept() {
				var isBPA = false; 
				var individualRadioStatus = document.getElementById("individualOrOrgRadiotrue").checked;
				isBPA = document.getElementById("depositWorksCategoryBPA").checked;
				if(isBPA)	{
					if(individualRadioStatus) {
						document.getElementById("newOrgznServiceDeptTR").style.display='';
						document.getElementById("serviceDeptTR").style.display='none';
					} else {
						document.getElementById("serviceDeptTR").style.display='none';
						document.getElementById("newOrgznServiceDeptTR").style.display='none';
						document.getElementById("mirrorApplicantOrgznServDeptTR").style.display='';
					}		
				} else {
					if(individualRadioStatus) {
						document.getElementById("newOrgznServiceDeptTR").style.display='none';
						document.getElementById("serviceDeptTR").style.display='none';
					} else {
						document.getElementById("serviceDeptTR").style.display='';
						document.getElementById("newOrgznServiceDeptTR").style.display='none';
					}
				}
				showOrHideSchemeDetails(document.getElementById("typeOfCut"));
			}
			
			function enableOrdisableElements()
			{
				<s:if test="%{mode=='view'}">
					disableElements();
				</s:if>
				<s:if test="%{mode=='rejectForResubmission'}">
					disableElements();
					enableRemarks();
				</s:if>
				<s:if test="%{mode=='edit'}">
					enableModifiableElements();
				</s:if>
				<s:if test="%{mode=='edit' && applicationRequest.depositWorksCategory==@org.egov.works.models.depositWorks.DepositWorksCategory@EMERGENCYCUT}">
					enableModifiableElementsForEmergencyCut();
				</s:if>
				<s:if test="%{mode=='edit' && applicationRequest.depositWorksCategory==@org.egov.works.models.depositWorks.DepositWorksCategory@BPA && applicationRequest.applicant.organization != null}">
					enableModifiableElementsForBPACut();
				</s:if>
				<s:if test="%{sourcepage=='genFeasibilityReport' || sourcepage=='inbox'}">
					enableFeasibleReportElements();
				</s:if>
				<s:if test="%{mode=='UpdateRoadCutDate'}">
					enableRCApprovalLetterDates();
				</s:if>
				<s:if test="%{mode=='UpdateRoadCutRestorationDate'}">
					enableRCRestorationDates();
				</s:if>
				//For registered user disable applicant name
				<s:if test="%{mode=='registeredUser' && model.applicationRequest.applicantName!=null}">
						document.getElementById('applicantName').value='<s:property value="%{model.applicationRequest.applicantName}" />';
						document.getElementById('applicantName').readOnly = true;
				</s:if>
				//Disable applicant name in edit mode for individual
				<s:if test="%{mode=='edit' && sourcepage=='search' && model.applicationRequest.applicant.name!=null}">
					document.getElementById('applicantName').readOnly = true;
				</s:if>
			}
			
			function disableElements(){
				var links=document.getElementById("aprdDatelnk"); 
				links.onclick=function(){return false;};
				
				links=document.getElementById("fbDatelnk"); 
				links.onclick=function(){return false;};

				links=document.getElementById("startDatelnk"); 
				links.onclick=function(){return false;};

				links=document.getElementById("endDatelnk"); 
				links.onclick=function(){return false;};

				links=document.getElementById("restorationStartDatelnk"); 
				links.onclick=function(){return false;};

				links=document.getElementById("restorationEndDatelnk"); 
				links.onclick=function(){return false;};
				
		        for(var i=0;i<document.forms[0].length;i++) {
		      		document.forms[0].elements[i].disabled =true;
		      	}
		      	document.roadCutForm.closeButton.readonly=false;
				document.roadCutForm.closeButton.disabled=false;

				if(document.roadCutForm.rejectButton!=null) {
					document.roadCutForm.rejectButton.readonly=false; 
					document.roadCutForm.rejectButton.disabled=false;		
				}
				
				//Enable Document View
				if(document.roadCutForm.docViewButton!=null){
				    document.roadCutForm.docViewButton.readonly=false; 
					document.roadCutForm.docViewButton.disabled=false; 
			    }

				if(document.roadCutForm.fbRepDocViewButton!=null){
				    document.roadCutForm.fbRepDocViewButton.readonly=false; 
					document.roadCutForm.fbRepDocViewButton.disabled=false; 
			    }
			    
				if(document.roadCutForm.history!=null){
				    document.roadCutForm.history.readonly=false; 
					document.roadCutForm.history.disabled=false; 
			    }
				//Enable buttons for BPA
				if(document.roadCutForm.bpaCloseButton!=null){
					document.roadCutForm.bpaCloseButton.readonly=false;
					document.roadCutForm.bpaCloseButton.disabled=false;
				}
				if(document.roadCutForm.bpaDocViewButton!=null){
				    document.roadCutForm.bpaDocViewButton.readonly=false; 
					document.roadCutForm.bpaDocViewButton.disabled=false; 
			    }
			    
			    if(document.roadCutForm.docUploadButton!=null)
			    {
			    	$('docUploadButton').hide();
			    }
		      	$('clearButton').hide();
		      	$('submitButton').hide();
			    hideColumn('Add');	   
			    hideColumn('Delete');

			    <s:if test="%{mode=='view' && model.egwStatus.code=='CANCELLED' && model.rejectionType!=null}">
		    		document.getElementById('RejectRemarks').style.display='block';  
		    	</s:if>
		    	<s:if test="%{mode=='rejectForResubmission'}">
		    		$('bpaSubmitButton').hide();
		    		$('bpaClearButton').hide();
		    		if(document.roadCutForm.bpaRejectButton!=null){
						document.roadCutForm.bpaRejectButton.readonly=false;
						document.roadCutForm.bpaRejectButton.disabled=false;
					}
		    	</s:if>
			    
			}

			function enableModifiableElementsForBPACut(){
				disableElements();
				document.getElementById("purposeOfCutLabelTD").style.visibility='visible';
				document.getElementById("purposeOfCutFieldTD").style.visibility='visible';
		    	document.getElementById('purposeOfRoadCut').disabled = false;
				var records= jurisdictionDetailsTable.getRecordSet();
				for(i=0;i<records.getLength();i++) {
					indexRow = records.getRecord(i).getId();
					dom.get("LocationName"+indexRow).readonly=false;
					dom.get("LocationName"+indexRow).disabled=false;
					<s:if test="%{!disableZone}">
						dom.get("Zone"+indexRow).disabled=false;
						dom.get("Zone"+indexRow).readonly=false;
					</s:if>
					<s:if test="%{!disableWard}">
						dom.get("Ward"+indexRow).disabled=false;
						dom.get("Ward"+indexRow).readonly=false;
					</s:if>
					<s:if test="%{!disableArea}">
						dom.get("Area"+indexRow).disabled=false;
						dom.get("Area"+indexRow).readonly=false;
					</s:if>
					<s:if test="%{!disableLocality}">
						dom.get("Locality"+indexRow).disabled=false;
						dom.get("Locality"+indexRow).readonly=false;
					</s:if>
					<s:if test="%{!disableStreet}">
						dom.get("Street"+indexRow).disabled=false;
						dom.get("Street"+indexRow).readonly=false;
					</s:if>
					dom.get("Length"+indexRow).disabled=false;
					dom.get("Length"+indexRow).readonly=false;
					dom.get("Breadth"+indexRow).disabled=false;
					dom.get("Breadth"+indexRow).readonly=false;
					if (document.getElementById('purposeOfRoadCut') != null && document.getElementById('purposeOfRoadCut').value != '<s:property value="@org.egov.works.models.depositWorks.PurposeOfRoadCut@SERVICE"/>') {
						dom.get("DepthDropDown"+indexRow).disabled=false;
						dom.get("DepthDropDown"+indexRow).readonly=false;
					}
					dom.get("RemarksEmergency"+indexRow).disabled=false;
					dom.get("RemarksEmergency"+indexRow).readonly=false;
		    	}
				$('submitButton').show();
				$('docUploadButton').show();
				$('docUploadButton').disabled = false;
				document.roadCutForm.submitButton.readonly = false;
				document.roadCutForm.submitButton.disabled = false;
			}
			
			function enableModifiableElements(){
					var domElement;
					var links=document.getElementById("aprdDatelnk"); 
					links.onclick=function(){return false;};
			        for(var i=0;i<document.forms[0].length;i++) {
				        
			        	domElement = document.forms[0].elements[i];
			        	if((domElement.name.indexOf("zone.id") != -1) ||(domElement.name.indexOf("ward.id") != -1) || (domElement.name.indexOf("area.id") != -1) || (domElement.name.indexOf("applicationRequest.depositWorksCategory") != -1) )
			        	{
			        		domElement.disabled =true;
			        	}
			        	if((domElement.name.indexOf("locality.id") != -1) ||(domElement.name.indexOf("street.id") != -1) || (domElement.name.indexOf("applicationRequest.schemeName") != -1)  )
			        	{
			        		domElement.disabled =true;
			        	}
			        	if((domElement.name.indexOf("applicationRequest.schemeDetails") != -1) ||(domElement.name.indexOf("applicationRequest.isSchemeBased") != -1))
			        	{
			        		domElement.disabled =true;
			        	}
			        	if((domElement.name.indexOf("applicationRequest.depositWorksType.id") != -1) ||(domElement.name.indexOf("applicationRequest.applicationDate") != -1))
			        	{
			        		domElement.disabled =true;
			        	}
			      	}
			      	$('clearButton').hide();
				    <s:if test="%{applicationRequest.isSchemeBased!=null && applicationRequest.isSchemeBased==true}" >
					    showColumn('Add');	   
					    showColumn('Delete');
				    </s:if>
				    <s:else>
					    hideColumn('Add');	   
					    hideColumn('Delete');
				    </s:else>
			}
			
			function enableModifiableElementsForEmergencyCut(){
				disableElements();
				var records= jurisdictionDetailsTable.getRecordSet();
				for(i=0;i<records.getLength();i++)
		    	{
					indexRow = records.getRecord(i).getId();
					dom.get("RemarksEmergency"+records.getRecord(i).getId()).readonly=false;
					dom.get("RemarksEmergency"+records.getRecord(i).getId()).disabled=false;
		    	}
				document.roadCutForm.areDrawingsAttached.readonly = false;
				document.roadCutForm.areDrawingsAttached.disabled = false;
				$('submitButton').show();
				$('history').hide();
				$('fbRepDocViewButton').hide();
				document.roadCutForm.submitButton.readonly = false;
				document.roadCutForm.submitButton.disabled = false;
				document.roadCutForm.emergencyCutDocUploadButton.readonly = false;
				document.roadCutForm.emergencyCutDocUploadButton.disabled = false;
		}

			function enableFeasibleReportElements(){
				showFeasibilityTab();
		        for(var i=0;i<document.forms[0].length;i++) {
		      		document.forms[0].elements[i].disabled =true;
		      	}
		        var links=document.getElementById("aprdDatelnk"); 
				links.onclick=function(){return false;};

				links=document.getElementById("fbDatelnk"); 
				links.onclick=function(){return false;};

				links=document.getElementById("startDatelnk"); 
				links.onclick=function(){return false;};

				links=document.getElementById("endDatelnk"); 
				links.onclick=function(){return false;};
		        
		        toggleFields(true,['approverDepartment','approverDesignation','approverPositionId','approverComments']);
		        <s:if test="%{sourcepage=='genFeasibilityReport' || (sourcepage=='inbox' && (model.egwStatus.code=='NEW' ||  model.egwStatus.code=='REJECTED'))}">
					if(document.roadCutForm.fbRepDocUploadButton!=null){
						document.roadCutForm.fbRepDocUploadButton.readonly=false; 
						document.roadCutForm.fbRepDocUploadButton.disabled=false;
					}

				    document.roadCutForm.recommendation.disabled =false;
				    document.roadCutForm.recommendation.readonly=false;
				    document.roadCutForm.estimatedCost.disabled =false;
				    document.roadCutForm.estimatedCost.readonly=false;
				    document.roadCutForm.roadLastLaidDate.disabled =false;
				    document.roadCutForm.roadLastLaidDate.readonly=false;
				   
				    var links=document.getElementById("fbDatelnk"); 
					links.onclick=function(){return true;};
					
				    <s:if test="%{depositCode==null}">
					    document.roadCutForm.depCode.disabled =false;
					    document.roadCutForm.codeName.disabled =false;
					    document.roadCutForm.description.disabled =false;
					    document.roadCutForm.fundId.disabled =false;
					    document.roadCutForm.financialYearId.disabled =false;
					    document.roadCutForm.fundSourceId.disabled =false;
				    </s:if> 
				 </s:if>
				 if(dom.get('Approve')!=null)
					dom.get('Approve').disabled=false;
				 if(document.getElementById('Forward')!=null)
					 document.getElementById('Forward').disabled=false;
				 if(dom.get('Save')!=null)
					dom.get('Save').disabled=false;
				 if(dom.get('Reject')!=null)
					dom.get('Reject').disabled=false;
				 if(dom.get('Cancel')!=null)
					dom.get('Cancel').disabled=false; 
				 document.roadCutForm.closeButton.readonly=false; 
				 document.roadCutForm.closeButton.disabled=false;
				 document.roadCutForm.docViewButton.readonly=false;
				 document.roadCutForm.docViewButton.disabled=false;

				 <s:if test="%{sourcepage=='inbox' && model.egwStatus.code=='REJECTED'}">
			    	document.getElementById('RejectRemarks').style.display='block'; 
			    	if(dom.get("rejectionType").value=='Reject permanently'){
			    		 if(document.getElementById('Forward')!=null)
							 document.getElementById('Forward').style.display='none';
			    		 document.roadCutForm.recommendation.disabled =true;
					     document.roadCutForm.recommendation.readonly=true; 
					     document.roadCutForm.estimatedCost.disabled =true;
					     document.roadCutForm.estimatedCost.readonly=true; 
					     document.roadCutForm.roadLastLaidDate.disabled =true;
					     document.roadCutForm.roadLastLaidDate.readonly=true; 
					     var links=document.getElementById("fbDatelnk"); 
							links.onclick=function(){return false;}; 
				    }
			    </s:if>
			    <s:if test="%{egwStatus!=null && egwStatus.code!='NEW' && egwStatus.code!='REJECTED'}">
				    if(document.roadCutForm.fbRepDocViewButton!=null){
					    document.roadCutForm.fbRepDocViewButton.readonly=false; 
						document.roadCutForm.fbRepDocViewButton.disabled=false; 
				    }
			    </s:if> 
			    if(document.roadCutForm.history!=null){
				    document.roadCutForm.history.readonly=false; 
					document.roadCutForm.history.disabled=false; 
			    }
			    hideColumn('Add');	   
			    hideColumn('Delete');
			}

			function enableRCRestorationDates(){
				showUpdateRoadCutDateTab();
				//Show the Restoration dates Div
				document.getElementById("restorationDates_div").style.display="block";
		        for(var i=0;i<document.forms[0].length;i++) {
		      		document.forms[0].elements[i].disabled =true;
		      	}
		        var links=document.getElementById("aprdDatelnk"); 
				links.onclick=function(){return false;};

				links=document.getElementById("fbDatelnk"); 
				links.onclick=function(){return false;};

				links=document.getElementById("startDatelnk"); 
				links.onclick=function(){return false;};
				
				links=document.getElementById("endDatelnk"); 
				links.onclick=function(){return false;};
				
				document.roadCutForm.rcRestorationStartDate.disabled =false;
			    document.roadCutForm.rcRestorationStartDate.readonly=false;
			    document.roadCutForm.rcRestorationEndDate.disabled =false;
			    document.roadCutForm.rcRestorationEndDate.readonly=false;				

				document.roadCutForm.closeButton.readonly=false;
				document.roadCutForm.closeButton.disabled=false;
				document.roadCutForm.submitButton.readonly=false;
				document.roadCutForm.submitButton.disabled=false;	

				<s:if test="%{applicationRequest.depositWorksCategory==@org.egov.works.models.depositWorks.DepositWorksCategory@BPA}">
					document.roadCutForm.bpaCloseButton.readonly=false;
					document.roadCutForm.bpaCloseButton.disabled=false;
					document.roadCutForm.bpaUpdateRoadCutDatesButton.readonly=false;
					document.roadCutForm.bpaUpdateRoadCutDatesButton.disabled=false;	
					$('bpaSubmitButton').hide();
				</s:if>	
				
		      	$('docUploadButton').hide();
		      	$('clearButton').hide();
			    hideColumn('Add');	   
			    hideColumn('Delete');
				
			}

			function enableRCApprovalLetterDates(){
				showUpdateRoadCutDateTab();
		        for(var i=0;i<document.forms[0].length;i++) {
		      		document.forms[0].elements[i].disabled =true;
		      	}
		        var links=document.getElementById("aprdDatelnk"); 
				links.onclick=function(){return false;};

				links=document.getElementById("fbDatelnk"); 
				links.onclick=function(){return false;};

				links=document.getElementById("startDatelnk"); 
				links.onclick=function(){return true;};
				
				links=document.getElementById("endDatelnk"); 
				links.onclick=function(){return true;};
				
				document.roadCutForm.rcStartDate.disabled =false;
			    document.roadCutForm.rcStartDate.readonly=false;
			    document.roadCutForm.rcEndDate.disabled =false;
			    document.roadCutForm.rcEndDate.readonly=false;
				document.roadCutForm.closeButton.readonly=false;
				document.roadCutForm.closeButton.disabled=false;
				document.roadCutForm.submitButton.readonly=false;
				document.roadCutForm.submitButton.disabled=false;	

				<s:if test="%{applicationRequest.depositWorksCategory==@org.egov.works.models.depositWorks.DepositWorksCategory@BPA}">
					document.roadCutForm.bpaCloseButton.readonly=false;
					document.roadCutForm.bpaCloseButton.disabled=false;
					document.roadCutForm.bpaUpdateRoadCutDatesButton.readonly=false;
					document.roadCutForm.bpaUpdateRoadCutDatesButton.disabled=false;	
					$('bpaSubmitButton').hide();
				</s:if>	
				
				$('docUploadButton').hide();
		      	$('clearButton').hide();
			    hideColumn('Add');	   
			    hideColumn('Delete');
			}
			function hideFeasReportEmergencyCut(){
				<s:if test="%{(mode=='view' || mode=='UpdateRoadCutDate') && applicationRequest.depositWorksCategory==@org.egov.works.models.depositWorks.DepositWorksCategory@EMERGENCYCUT}">
					document.getElementById('history').style.visibility='hidden';
					document.getElementById('fbRepDocViewButton').style.visibility='hidden';
				</s:if> 
			}
		</script>
	<body onload="load();hideFeasReportEmergencyCut();enableOrdisableElements();noBack();" onpageshow="if(event.persisted) noBack();" onunload="">
		<script >
		//TODO - Check for Apply Road Cut and Generate Feasibility report, check for portal screen also
		window.history.forward(1);
		function noBack() {
			window.history.forward(); 
		}
			function setCSSClasses(id,classes){
			    document.getElementById(id).setAttribute('class',classes);
			    document.getElementById(id).setAttribute('className',classes);
			}
			
			function showApplicantDetailsTab(){
				  document.getElementById('applicant_div').style.display='';
				  setCSSClasses('applicantDetailsTab','First Active');
				  setCSSClasses('requestDetailsTab','');
				  if(document.getElementById('feasibilityTab')!=null) {
					  setCSSClasses('feasibilityTab','');
					  hideFeasibilityTab();
				  }
				  hideRequestDetailsTab();
				  if(document.getElementById('updateRoadCutDateTab')!=null) {
				    	hideRoadCutTab();
				    	setCSSClasses('updateRoadCutDateTab','Last');
				   }
			}
			
			function showRequestDetailsTab(){
				$('requestDetails_div').show();
				document.getElementById('applicant_div').style.display='none';
				hideApplicantDetailsTab();
			 	setCSSClasses('applicantDetailsTab','First BeforeActive');
			    setCSSClasses('requestDetailsTab','Active');
			    if(document.getElementById('feasibilityTab')!=null) {
					hideFeasibilityTab();
			    	setCSSClasses('feasibilityTab','');
			    }
			    if(document.getElementById('updateRoadCutDateTab')!=null) {
			    	hideRoadCutTab();
			    	setCSSClasses('updateRoadCutDateTab','Last');
			    }
			}
			    
			
			function showFeasibilityTab(){
				$('feasibility_div').show();
				document.getElementById('applicant_div').style.display='none'; 
				hideApplicantDetailsTab();
				hideRequestDetailsTab();
			 	setCSSClasses('applicantDetailsTab','First');
			 	setCSSClasses('requestDetailsTab','BeforeActive');
			 	setCSSClasses('feasibilityTab','Active');
			 	if(document.getElementById('updateRoadCutDateTab')!=null) {
			 		setCSSClasses('updateRoadCutDateTab','Last');
				   	hideRoadCutTab();
			 	}
			}

			function showUpdateRoadCutDateTab(){
				$('roadCut_div').show();
				document.getElementById('applicant_div').style.display='none'; 
				hideApplicantDetailsTab();
				hideRequestDetailsTab();
			 	setCSSClasses('applicantDetailsTab','First');
			 	setCSSClasses('requestDetailsTab','');
			    if(document.getElementById('feasibilityTab')!=null) {
			    	setCSSClasses('feasibilityTab','BeforeActive');
			    	hideFeasibilityTab();
			 	} 	
			    setCSSClasses('updateRoadCutDateTab','Last Active ActiveLast');
			}
			
			function hideApplicantDetailsTab(){
				  document.getElementById('applicant_div').style.display='none';
			}
			function hideRequestDetailsTab(){
				  document.getElementById('requestDetails_div').style.display='none';
			}
			function hideFeasibilityTab(){
				  document.getElementById('feasibility_div').style.display='none';
			}
			function hideRoadCutTab(){
				  document.getElementById('roadCut_div').style.display='none';
			}

			function validateApplicantDetails(){
				
				if(document.getElementById('citizenEMail').value=="")
				{
					document.getElementById("roadcut_error").innerHTML='<s:text name="depositworks.roadcut.enter.email" />';
					return false;
				}
				if(document.getElementById('citizenMobileNo').value=="")
				{
					document.getElementById("roadcut_error").innerHTML='<s:text name="depositworks.roadcut.enter.mobileno" />';
					return false;
				}
				if(document.getElementById('citizenHouseNo').value=="")
				{
					document.getElementById("roadcut_error").innerHTML='<s:text name="depositworks.roadcut.enter.houseno" />';
					return false;
				}
				if(document.getElementById('citizenAdr1').value=="")
				{
					document.getElementById("roadcut_error").innerHTML='<s:text name="depositworks.roadcut.enter.streetaddr" />';
					return false;
				}
				if(document.getElementById('citizenLocality').value=="")
				{
					document.getElementById("roadcut_error").innerHTML='<s:text name="depositworks.roadcut.enter.locality" />';
					return false;	
				}
				if(document.getElementById('citizenPincode').value=="")
				{
					document.getElementById("roadcut_error").innerHTML='<s:text name="depositworks.roadcut.enter.pincode" />';
					return false;
				}
				return true;
			}
			
			function validateInput()
			{
					dom.get("roadcut_error").style.display="block";
					var individualRadioStatus = document.getElementById("individualOrOrgRadiotrue").checked;
					var organizationRadioStatus = document.getElementById("individualOrOrgRadiofalse").checked;
					var bpaCheckboxStatus = document.getElementById("depositWorksCategoryBPA").checked;
					var repairAndMentainanceRadioStatus = false;
					var emergencyCutRadioStatus = false;
					var bpaCutRadioStatus = false;
					if(document.getElementById("depositWorksCategoryREPAIRSANDMAINTENANCE") != null)
						repairAndMentainanceRadioStatus = document.getElementById("depositWorksCategoryREPAIRSANDMAINTENANCE").checked;

					if(document.getElementById("depositWorksCategoryEMERGENCYCUT") != null)
						emergencyCutRadioStatus = document.getElementById("depositWorksCategoryEMERGENCYCUT").checked;

					if(document.getElementById("depositWorksCategoryBPA") != null)
						bpaCutRadioStatus = document.getElementById("depositWorksCategoryBPA").checked;
				<s:if test="%{mode!='UpdateRoadCutDate' && mode!='UpdateRoadCutRestorationDate'}">
					<s:if test="%{mode=='edit' && applicationRequest.depositWorksCategory==@org.egov.works.models.depositWorks.DepositWorksCategory@EMERGENCYCUT}">
						dom.get("modifyMode").value="afterModify";
						dom.get("docNum").value=document.getElementById("docNumber").value;
						var cbox = document.getElementById("areDrawingsAttached");
						if(!cbox.checked){
							document.getElementById("roadcut_error").innerHTML='<s:text name="dw.emergencyCut.upload.drawing.checkbox.validate" />';
							return false;
						}
					</s:if>
									
					if(individualRadioStatus)  
					{        
						if(document.getElementById('citizenName').value=="")
						{
							document.getElementById("roadcut_error").innerHTML='<s:text name="depositworks.roadcut.enter.name" />';
							return false;
						}
					}	
					if(organizationRadioStatus)
					{
						if(document.getElementById('organizationTypes').value==-1)
						{
							document.getElementById("roadcut_error").innerHTML='<s:text name="depositworks.roadcut.enter.organization" />';
							return false;
						}
						if(document.getElementById('servicedept').value==-1)
						{
							document.getElementById("roadcut_error").innerHTML='<s:text name="depositworks.roadcut.enter.service.dept" />';
							return false;
						}
					}
					if(bpaCheckboxStatus && individualRadioStatus)
					{
						if(document.getElementById('rcDtlsOrganization').value==-1)
						{
							document.getElementById("roadcut_error").innerHTML='<s:text name="depositworks.select.organization.in.dtls" />';
							return false;							
						}
						if(document.getElementById('rcDtlsServiceDept').value==-1)
						{
							document.getElementById("roadcut_error").innerHTML='<s:text name="depositworks.select.service.dept.in.details" />';
							return false;							
						}	
					}

					if(individualRadioStatus)
					{
						if(repairAndMentainanceRadioStatus){
							dom.get("roadcut_error").style.display='';
							document.getElementById("roadcut_error").innerHTML='<s:text name="depositworks.roadcut.validate.repairandmentainance"/>';
							return false;   
						}
						else if(emergencyCutRadioStatus){
							dom.get("roadcut_error").style.display='';
							document.getElementById("roadcut_error").innerHTML='<s:text name="depositworks.roadcut.validate.emergencyCut"/>';
							return false;   
						}
					}   

					if(!validateApplicantDetails()){
						return false;
					}
					
					if(document.getElementById('typeOfCut').value==-1)
					{
						document.getElementById("roadcut_error").innerHTML='<s:text name="depositworks.roadcut.enter.typeofcut" />';
						return false;
					}
					if(document.getElementById('appRequestDate').value=='')
					{
						document.getElementById("roadcut_error").innerHTML='<s:text name="depositworks.roadcut.enter.date" />';
						return false;
					}
					if(document.getElementById('applicantName').value=='')
					{
						document.getElementById("roadcut_error").innerHTML='<s:text name="depositworks.roadcut.enter.applicant.name" />';
						return false;
					}
					if( (emergencyCutRadioStatus || (bpaCutRadioStatus && !individualRadioStatus)) && document.getElementById('purposeOfRoadCut').value== '') {
						document.getElementById("roadcut_error").innerHTML='<s:text name="dw.roadcut.select.purposeofcut" />';
						return false;
					}
                	else if(!(emergencyCutRadioStatus || (bpaCutRadioStatus && !individualRadioStatus)) && document.getElementById('purposeOfRoadCut') != null) {
                		document.getElementById('purposeOfRoadCut').disabled = true;
					}
					if(document.getElementById('typeOfCut').value!=-1)
					{
						var typeOfCutDD = document.getElementById('typeOfCut');
						var objText = typeOfCutDD[typeOfCutDD.selectedIndex].text;
						var showScheme=false;
						<s:iterator value="schemeBasedDWTypeList" var="dwTypeList">
							if (objText=="<s:property value='#dwTypeList' />")
							{
								showScheme=true;
							}
						</s:iterator>
						var radioTrue =  document.getElementById('isSchemeBasedRadiotrue').checked;
						var radioFalse =  document.getElementById('isSchemeBasedRadiofalse').checked;
						
						if(showScheme && radioTrue )
						{
							if(document.getElementById("applicationRequest.schemeName").value=="")
							{
								document.getElementById("roadcut_error").innerHTML='<s:text name="depositworks.roadcut.enter.schemename" />';
								return false;
							}	
							if(document.getElementById("applicationRequest.schemeDetails").value=="")
							{
								document.getElementById("roadcut_error").innerHTML='<s:text name="depositworks.roadcut.enter.schemedetails" />';
								return false;	
							}
						}
						
						if(showScheme && radioFalse)
						{
							// SET THE SCHEME DETAILS TO EMPTY
							document.getElementById("applicationRequest.schemeDetails").value="";
							document.getElementById("applicationRequest.schemeName").value="";
							if(jurisdictionDetailsTable.getRecordSet().getLength()>1)
							{
								document.getElementById("roadcut_error").innerHTML='<s:text name="depositworks.roadcut.delete.multiplerows" />';
								return false;
							}
						}
						if(showScheme==false)
						{
							document.getElementById('isSchemeBasedRadiofalse').checked="checked";
							document.getElementById("applicationRequest.schemeDetails").value="";
							document.getElementById("applicationRequest.schemeName").value="";
							if(jurisdictionDetailsTable.getRecordSet().getLength()>1)
							{
								document.getElementById("roadcut_error").innerHTML='<s:text name="depositworks.roadcut.delete.multiplerows" />';
								return false;
							}	
						}		
					}
					var records= jurisdictionDetailsTable.getRecordSet();
					var i;
					var recordId1;
	                for(i=0;i<records.getLength();i++)
	                {
	                	recordId1=records.getRecord(i).getId();
	                	if(document.getElementById("depositWorksCategoryBPA")!=null && document.getElementById("depositWorksCategoryBPA").checked==true) {
		                	if(dom.get("BPANumber"+recordId1).value=="") {
		                		document.getElementById("roadcut_error").innerHTML='<s:text name="depositworks.roadcut.null.bpanumber" />';
								return false;
		                    }
	                	}
	                	if(dom.get("LocationName"+recordId1).value=="")
	                    {
	                		document.getElementById("roadcut_error").innerHTML='<s:text name="depositworks.roadcut.null.roadname" />';
							return false;
	                    }
	                	if(dom.get("Zone"+recordId1).value==-1)
	                    {
	                		document.getElementById("roadcut_error").innerHTML='<s:text name="depositworks.roadcut.null.zone" />';
							return false;
	                    }
	                	if(dom.get("Ward"+recordId1).value==-1)
	                    {
	                		document.getElementById("roadcut_error").innerHTML='<s:text name="depositworks.roadcut.enter.ward" />';
							return false;
	                    }
	                	if(dom.get("Area"+recordId1).value==-1)
	                    {
	                		document.getElementById("roadcut_error").innerHTML='<s:text name="depositworks.roadcut.enter.area" />';
							return false;
	                    }	
	                	if(dom.get("Locality"+recordId1).value==-1)
	                    {
	                		document.getElementById("roadcut_error").innerHTML='<s:text name="depositworks.roadcut.enter.locality" />';
							return false;
	                    }
	                	if(dom.get("Street"+recordId1).value==-1)
	                    {
	                		document.getElementById("roadcut_error").innerHTML='<s:text name="depositworks.roadcut.enter.street" />';
							return false;
	                    }
	                	if(dom.get("Length"+recordId1).value=="")
	                    {
	                		document.getElementById("roadcut_error").innerHTML='<s:text name="depositworks.roadcut.enter.length" />';
							return false;
	                    }
	                	if(dom.get("Breadth"+recordId1).value=="")
	                    {
	                		document.getElementById("roadcut_error").innerHTML='<s:text name="depositworks.roadcut.enter.breadth" />';
							return false;
	                    }
	                	if(emergencyCutRadioStatus || (bpaCutRadioStatus && !individualRadioStatus)) {
    	                	if(dom.get("DepthDropDown"+recordId1).value == -1) {
								document.getElementById("roadcut_error").innerHTML='<s:text name="dw.roadcut.select.depthofcut" />';
								return false;
							}								
		                	else {
		                		dom.get("Depth"+recordId1).value = dom.get("DepthDropDown"+recordId1).value;
			                }
    	                	if(dom.get("RemarksEmergency"+recordId1).value != "") {
    	                		dom.get("Remarks"+recordId1).value = dom.get("RemarksEmergency"+recordId1).value;
							}
	                	}
	                }
	                if((bpaCutRadioStatus && !individualRadioStatus)){
	    				var doc = dom.get('docNumber').value;
	    				if(doc==''){
	    					document.getElementById("roadcut_error").innerHTML='<s:text name="dw.bpa.document.upload.mandatory" />';
	    					return false;
	    				}
	    			}
			</s:if>
			<s:if test="%{mode=='UpdateRoadCutDate' }">
				if(!validateRoadCutDates(bpaCutRadioStatus)){
					return false;
				}

					for(var i=0;i<document.forms[0].length;i++) {
			      		document.forms[0].elements[i].disabled =false; 
			      	}
			</s:if>
			
			<s:if test="%{mode=='UpdateRoadCutRestorationDate'}">
				if(!validateRestorationDates()){
					return false;
				}
		
				for(var i=0;i<document.forms[0].length;i++) {
		      		document.forms[0].elements[i].disabled =false; 
		      	}
			</s:if>
			
				<s:if test="%{mode=='edit'}"> 
	                for(var i=0;i<document.forms[0].length;i++) {
			      		document.forms[0].elements[i].disabled =false;
			      	}
			    </s:if>
			    dom.get("roadcut_error").style.display='none'; 
			  	dom.get("roadcut_error").innerHTML='';
			  	enableBoundaryDetailsForBPARoadCut();
			  	document.getElementById("typeOfCut").disabled=false;
				return true;
			}

			function validateRoadCutDates(bpaCutRadioStatus){
				if(dom.get('rcStartDate').value=='' && dom.get('rcEndDate').value=='')
				{
					showMessage('roadcut_error','<s:text name="depositworks.roadcut.validate.Dates" />');
					dom.get("rcStartDate").focus(); 
					return false;
				}
				if(dom.get('rcStartDate').value=='' && dom.get('rcEndDate').value!='')
				{
					showMessage('roadcut_error','<s:text name="depositworks.roadcut.validate.startdate" />');
					dom.get("rcStartDate").focus(); 
					return false;
				}
				if(dom.get('rcStartDate').value!='' && dom.get('rcEndDate').value!='')
				{
					if(compareDate(dom.get('rcStartDate').value,dom.get('rcEndDate').value) == -1 ){
						showMessage('roadcut_error','<s:text name="depositworks.roadcut.greaterthan.endDate.startdate" />');
						dom.get("rcEndDate").focus(); 
						return false;
					}
				} 
				if(bpaCutRadioStatus){
					var workCommDate =  '<s:date name="workCommencedDate" format="dd/MM/yyyy"/>'; 
					if(compareDate(dom.get('rcStartDate').value,workCommDate) == 1 ){
						var errMsg = '<s:text name="dw.bpa.roadCut.startDate.lesserthan.workCommenced.date.msg1" />'+' '+workCommDate+'<s:text name="dw.bpa.roadCut.startDate.lesserthan.workCommenced.date.msg2" />';
						showMessage('roadcut_error',errMsg);
						dom.get("rcStartDate").focus(); 
						return false;
					}
				}
				return true;
			}

			function validateRestorationDates(){
				if(dom.get('rcRestorationStartDate').value!='' || dom.get('rcRestorationEndDate').value!='')
				{
					if(dom.get('rcStartDate').value=='' || dom.get('rcEndDate').value=='')
					{
						showMessage('roadcut_error','<s:text name="depositworks.roadcut.restorationdates.without.cutdates" />');
						return false;
					}
				}	
				if(dom.get('rcRestorationStartDate').value=='' && dom.get('rcRestorationEndDate').value=='')
				{
					showMessage('roadcut_error','<s:text name="depositworks.roadcut.validate.restoration.Dates" />');
					dom.get("rcRestorationStartDate").focus(); 
					return false;
				}
				if(dom.get('rcRestorationStartDate').value=='' && dom.get('rcRestorationEndDate').value!='')
				{
					showMessage('roadcut_error','<s:text name="depositworks.roadcut.validate.restoration.startdate" />');
					dom.get("rcRestorationStartDate").focus(); 
					return false;
				}
				if(dom.get('rcRestorationStartDate').value!='' && dom.get('rcRestorationEndDate').value!='')
				{
					if(compareDate(dom.get('rcRestorationStartDate').value,dom.get('rcRestorationEndDate').value) == -1 ){
						showMessage('roadcut_error','<s:text name="depositworks.roadcut.restoration.greaterthan.endDate.startdate" />');
						dom.get("rcRestorationEndDate").focus(); 
						return false;
					}
				}
				var currentDate=getCurrentDate();
		  		var restorStartDate=dom.get("rcRestorationStartDate").value;
		  		var restorEndDate=dom.get("rcRestorationEndDate").value;
				if(compareDate(restorStartDate,currentDate) == -1){
					showMessage('roadcut_error','<s:text name="depositworks.roadcut.restoration.startdate.error"/>');
					return false;
				}
				if(compareDate(restorEndDate,currentDate) == -1){
					showMessage('roadcut_error','<s:text name="depositworks.roadcut.restoration.enddate.error"/>');
					return false;
				} 

				if(compareDate(dom.get('rcEndDate').value,dom.get('rcRestorationStartDate').value) == -1 ){
					showMessage('roadcut_error','<s:text name="depositworks.roadcut.restoration.greaterthan.roadcutdate" />');
					dom.get("rcRestorationStartDate").focus(); 
					return false;
				}
				return true;
			}
			
			function validateRemarks() {
				if (dom.get('resubmitRemarks').value == null || dom.get('resubmitRemarks').value == "") {
					dom.get('roadcut_error').style.display = '';
					dom.get('roadcut_error').innerHTML = '<s:text name="dw.bpacut.rejection.resubmit.error.msg"/>';
					return false;
				} else {
					dom.get('roadcut_error').innerHTML = '';
					dom.get('roadcut_error').style.display = 'none';
					var msg = '<s:text name="dw.bpacut.rejection.resubmit.confirm.msg"/>';
					if(!confirmReject(msg)) {
						return false;
					}
					else {
						return true;
					}
				}
			}

			function enableRemarks() {
				document.forms[0].appDetailsId.disabled = false;
				document.forms[0].mode.disabled = false;
				document.forms[0].sourcepage.disabled = false;
				dom.get('resubmitRemarks').disabled = false;
			}
			
			function validateDataBeforeSubmit(text) {
				if(!validateFeasibilityBeforeSubmit()) 
					return false;

				if(document.getElementById("actionName").value=='Cancel'){
					if(!validateCancel()){
					return false;
					}
				}
				if(document.getElementById("actionName").value=='Forward'){
					<s:if test="%{applicationRequest.depositWorksCategory==@org.egov.works.models.depositWorks.DepositWorksCategory@OTHERS || applicationRequest.depositWorksCategory==@org.egov.works.models.depositWorks.DepositWorksCategory@REPAIRSANDMAINTENANCE}">
						if(!(document.roadCutForm.depCode.checked==true)){
							dom.get("roadcut_error").style.display='';
							document.getElementById("roadcut_error").innerHTML='<s:text name="dw.feasibilityReport.validate.depCode"/>';
							return false;
						}	
					</s:if>
				}

				if(document.getElementById("actionName").value=='Reject'){
					 document.getElementById('RejectRemarks').style.display='block'; 
					 document.roadCutForm.rejectionType.disabled =false;
					 document.roadCutForm.rejectionRemarks.disabled =false;

					 if(dom.get("rejectionType").value=='0' && (dom.get("rejectionRemarks").value=='' || dom.get("rejectionRemarks").value==null)){
							dom.get("roadcut_error").style.display='';
							document.getElementById("roadcut_error").innerHTML='<s:text name="dw.feasibilityReport.validate.rejectionDetails"/>';
							dom.get("rejectionType").focus();
							return false;
						 }

					 if(dom.get("rejectionType").value=='0'){
						dom.get("roadcut_error").style.display='';
						document.getElementById("roadcut_error").innerHTML='<s:text name="dw.feasibilityReport.validate.rejectionType"/>';
						dom.get("rejectionType").focus();
						return false; 
					 }
					 
					 if(dom.get("rejectionRemarks").value=='' || dom.get("rejectionRemarks").value==null){
						dom.get("roadcut_error").style.display='';
						document.getElementById("roadcut_error").innerHTML='<s:text name="dw.feasibilityReport.validate.rejectionRemarks"/>';
						dom.get("rejectionRemarks").focus();
						return false;
					 }
				}

				if(document.getElementById("actionName").value!='Reject' && document.getElementById("actionName").value!='Cancel'){
						if(dom.get("rejectionType").value!='0'){
							dom.get("rejectionType").value='0';
						 }
						if(dom.get("rejectionRemarks").value!='' || dom.get("rejectionRemarks").value!=null){
							dom.get("rejectionRemarks").value='';
						 }
				}  
				
				if(text!='Approve' && text!='Reject' ){
					if(!validateWorkFlowApprover(text))
						return false;
				}

				// Enable Fields				
				for(var i=0;i<document.forms[0].length;i++) {
		      		document.forms[0].elements[i].disabled =false; 
		      	}
				var emergencyCutRadioStatus = false;
				var bpaCutRadioStatus = false;
				var individualRadioStatus = document.getElementById("individualOrOrgRadiotrue").checked;
				if(document.getElementById("depositWorksCategoryEMERGENCYCUT") != null)
					emergencyCutRadioStatus = document.getElementById("depositWorksCategoryEMERGENCYCUT").checked;
				if(document.getElementById("depositWorksCategoryBPA") != null)
					bpaCutRadioStatus = document.getElementById("depositWorksCategoryBPA").checked;
				
				if(!(emergencyCutRadioStatus || (bpaCutRadioStatus && !individualRadioStatus)) && document.getElementById('purposeOfRoadCut') != null) {
            		document.getElementById('purposeOfRoadCut').disabled = true;
				}
		      	return true;
			}

			function validateCancel() {
				var msg='<s:text name="dw.feasibilityReport.cancel.confirm"/>';
				var applNo='<s:property value="applicationRequest.applicationNo"/>';
				if(!confirmCancel(msg,applNo)) {
					return false;
				}
				else {
					return true;
				}
			}

			function loadDesignationFromMatrix(){ 
				var dept=dom.get('departmentName').value;
		  		var currentState = dom.get('currentState').value;
		  		var amountRule =  dom.get('amountRule').value;
				var additionalRuleValue =  dom.get('additionalRuleValue').value; 
				var pendingAction=document.getElementById('pendingActions').value;
				loadDesignationByDeptAndType('ApplicationDetails',dept,currentState,amountRule,additionalRuleValue,pendingAction); 
			}

			function populateApprover()
			{
			  getUsersByDesignationAndDept();
			}		

			function viewDocument(){
				  viewDocumentManager(dom.get("docNumber").value); 
			}
			function viewFeasibilityRepDocument(){
				  viewDocumentManager(dom.get("feasibilityRepDocNumber").value); 
			}

			function checkDrawingsAttached(obj){ 
				   if(obj.checked){
					   	document.getElementById('areDrawingsAttached').value= true;
					 	document.getElementById('areDrawingsAttached').checked=true;
					}
					else if(!obj.checked){
						document.getElementById('areDrawingsAttached').value=false;
						document.getElementById('areDrawingsAttached').checked=false;
					}
			}
			
			</script>
		<s:if test="%{hasErrors()}"> 
	        <div class="errorstyle">
	          <s:actionerror/>
	          <s:fielderror/>
	        </div>
	    </s:if>
    
		<s:if test="%{hasActionMessages()}">
	        <div class="messagestyle">
	        	<s:actionmessage theme="simple"/>
	        </div>
	    </s:if>
    	<div class="errorstyle" id="roadcut_error" style="display: none;"></div>
		<s:form action="roadCut" name="roadCutForm" onSubmit="return validateInput()" theme="simple">
		<s:if test="%{mode!='view'}"> 
			<s:token id="token"/>

		</s:if>
   
		<s:push value="model">
 		<s:hidden name="applicationRequest.documentNumber" id="docNumber" />
		<s:hidden name="applicationDetails.documentNumber" id="feasibilityRepDocNumber" />  
		
			<s:hidden name="id" id="id" />
			<input type="hidden" name="actionName" id="actionName"/>
			<s:hidden name="appDetailsIdForModify" id="appDetailsIdForModify" />
			<s:hidden name="appDetailsId" id="appDetailsId" />
			<s:hidden name="applicationRequest.applicant.id" id="citizenId" value="%{applicationRequest.applicant.id}"/>
			<s:hidden name="citizenDetailsId" value="%{applicationRequest.applicant.id}"/>
			<s:hidden name="mode" id="mode" />
			<s:hidden name="sourcepage" id="sourcepage" />
			<s:hidden name="forwardNotApplicable" id="forwardNotApplicable" value="%{forwardNotApplicable}" />
			<s:hidden name="modifyMode" id="modifyMode" value="%{modifyMode}" />
			<s:hidden name="originalDocsCount" id="originalDocsCount" value="%{originalDocsCount}" />
			<s:hidden name="docNum" id="docNum" value="%{docNum}" />
			<s:hidden id="additionalRuleValue" name="additionalRuleValue" value="%{additionalRuleValue}" />
			<div class="formmainbox">
			<div class="insidecontent">
  			<div class="rbroundbox2">
 			<div class="rbtop2"><div></div></div>
 			<div class="rbcontent2">

	      <div class="estimateno">
			Application Request No: <s:if test="%{applicationRequest.applicationNo==null || applicationRequest.applicationNo==''}">&lt; Not Assigned &gt;</s:if><s:property value="applicationRequest.applicationNo" />
		  </div>
		  <s:if test="%{depositCode!=null}">
 				<div class="estimateno" style="text-align: right">Deposit Code: <s:property value="depositCode.code" /></div> 
	       </s:if>
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						&nbsp;
					</tr>
					<tr>
						<td><div id="header">
								<ul id="Tabs">
									<li id="applicantDetailsTab" class="First Active"><a id="header_1" href="#" onclick="showApplicantDetailsTab();"><s:text name="depositworks.roadcut.applicant.details" /></a></li>
									<li id="requestDetailsTab" class="Befor"><a id="header_2" href="#" onclick="showRequestDetailsTab();"><s:text name="depositworks.roadcut.details" /></a></li>
									<s:if test="%{sourcepage=='genFeasibilityReport' || sourcepage=='inbox' || (model.egwStatus!=null && (model.applicationRequest.depositWorksCategory!=@org.egov.works.models.depositWorks.DepositWorksCategory@EMERGENCYCUT && model.applicationRequest.depositWorksCategory!=@org.egov.works.models.depositWorks.DepositWorksCategory@BPA) && sourcepage=='search' && mode=='view') || ((mode=='UpdateRoadCutDate' || mode=='UpdateRoadCutRestorationDate') && (model.applicationRequest.depositWorksCategory!=@org.egov.works.models.depositWorks.DepositWorksCategory@EMERGENCYCUT && model.applicationRequest.depositWorksCategory!=@org.egov.works.models.depositWorks.DepositWorksCategory@BPA))}">
										<li id="feasibilityTab" class="Befor"><a id="header_4" href="#" onclick="showFeasibilityTab();"><s:text name="depositworks.feasibilityRep.title" /></a></li>  
									</s:if>
									<s:if test="%{mode=='UpdateRoadCutDate' || mode=='UpdateRoadCutRestorationDate' || ((model.applicationRequest.workEndDate!=null || model.applicationRequest.workStartDate!=null ) && sourcepage=='search' && mode=='view')}">
										<li id="updateRoadCutDateTab" class="Last"><a id="header_7" href="#" onclick="showUpdateRoadCutDateTab();"><s:text name="depositworks.updateRoadCutDate.title" /></a></li>  
									</s:if>
								</ul>
						</div></td>
				    </tr>
					<tr> 
				    	<td>
				    		<div id="applicant_div">
								<%@ include file="dw-applicantDetails.jsp"%>  
							</div>	
						</td>		
				    </tr>
				     <tr>
				       	 <td>
							<div id="requestDetails_div" style="display:none;">
								<%@ include file="roadCut-categories.jsp"%> 
								<div id="roadCutDetails_div" >
									<%@ include file="roadCut-details.jsp"%> 
								</div>
								<div id="newBPADetails_div" style="display:none;">
					 				<%@ include file="roadCut-bpa.jsp"%>	
								</div>
									
							</div>
				       	</td>
				     </tr>
				      <tr>
				       	 <td>
							<div id="feasibility_div" style="display:none;">
								<%@ include file="feasibilityReport.jsp"%>
							</div>
				       	</td>
				     </tr>
				     <tr>
				       	 <td>
							<div id="roadCut_div" style="display:none;">
								<%@ include file="roadCutDate.jsp"%>
							</div>
				       	</td>
				     </tr>
				     
				     <s:if test="%{sourcepage=='genFeasibilityReport' || sourcepage=='inbox'}">
				     <tr>
						<td class="shadowwk" colspan="4"> </td>
					 </tr>
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
				    
				    <tr>
				    <table width="100%" border="0" cellspacing="0" cellpadding="0" align="left" id="RejectRemarks" style="display: none;">
						<br> 
						<tr>
							  <td colspan="2">
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span class="mandatory">*</span><s:text name="dw.feasibilityReport.reject.type" />:
							 </td>
							 <td colspan="2">
								&nbsp;&nbsp;<s:select id="rejectionType" name="rejectionType" cssClass="selectwk" list="#{'0':'----Choose----','Reject permanently':'Reject permanently','Reject with a option to resubmit':'Reject with a option to resubmit'}" />
							</td>	
							
							 <td colspan="2">
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span class="mandatory">*</span><s:text name="dw.feasibilityReport.reject.remarks" />:
							 </td>
							<td colspan="3">
								<s:textarea name="rejectionRemarks" cols="40" rows="3" cssClass="selectwk" id="rejectionRemarks" value="%{rejectionRemarks}"/>
							</td>
						</tr>
					</table>
				    </tr>
				    <tr>
				    	<td colspan="4">
				    		<s:if test="%{mode == 'rejectForResubmission'}">
				    		<table width="100%" border="0" cellspacing="0" cellpadding="0" align="left" >
							    <tr>
							    	<td class="greyboxwk"><span class="mandatory">*</span><s:text name="dw.bpacut.rejection.remarks"/>:</td>
									<td class="greybox2wk"><s:textarea name="rejectionRemarks" cols="40" rows="3" cssClass="selectwk" id="resubmitRemarks"
														value="%{rejectionRemarks}" />
									</td>
									<td colspan="2" class="greybox2wk">&nbsp;</td>
							    </tr>
						    </table>
				    		</s:if>
				    	</td>
				    </tr>
				    <s:if test="%{mode=='edit' && sourcepage=='search' && applicationRequest.depositWorksCategory==@org.egov.works.models.depositWorks.DepositWorksCategory@EMERGENCYCUT}">
				    <tr>
				    	<span class="mandatory">*</span><s:text name="dw.emergencyCut.upload.drawing" />
				    	<s:checkbox name="areDrawingsAttached" id="areDrawingsAttached" value="%{areDrawingsAttached}"  onclick="checkDrawingsAttached(this);" />
				    </tr>
				    </s:if>
				     <tr>
			            <td><div align="right" class="mandatory" style="font-size:11px;padding-right:20px;">* <s:text name="message.mandatory" /></div></td>
			          </tr>
				</table>
				</div>
			<div class="rbbot2"><div></div></div>
			</div>
			</div>
	
				<s:if test="%{sourcepage!='genFeasibilityReport' && sourcepage!='inbox'}"> 
				     <div class="buttonholderwk" id="buttons">
				     	<div id="normalbuttons_div" style="display:block;">
				     	<s:if test="%{mode=='edit'}">
							<s:submit cssClass="buttonfinal" onclick='return validateInput()' value="Submit" id="submitButton" method="update" />
						</s:if>
						<s:elseif test="%{mode=='UpdateRoadCutDate' || mode=='UpdateRoadCutRestorationDate'}">
							<s:submit cssClass="buttonfinal" onclick='return validateInput()' value="Submit" id="submitButton" method="updateRoadCutDate" />
						</s:elseif>
						<s:else>
							<s:submit cssClass="buttonfinal" onclick='return validateInput()' value="Submit" id="submitButton" method="save" />
						</s:else>
						<input type="button" class="buttonfinal" value="CLEAR" id="clearButton" name="clearButton" onclick="window.open('${pageContext.request.contextPath}/depositWorks/roadCut!newform.action','_self');"/>
				     	<input type="button" class="buttonfinal" value="CLOSE" id="closeButton" name="closeButton" onclick="window.close();"/>
				     	
						
						<s:if test="%{mode=='rejectForResubmission' && sourcepage=='search'}">
							<s:submit cssClass="buttonfinal" value="REJECT" id="rejectButton" method="rejectForResubmit" onclick="return validateRemarks();" />
						</s:if>
					 	<s:if test="%{mode=='view' || mode=='rejectForResubmission' && sourcepage=='search'}">
							<input type="submit" class="buttonadd" value="View Document" id="docViewButton" onclick="viewDocument();return false;" />
						</s:if>
						<s:elseif test="%{applicationRequest.depositWorksCategory!=@org.egov.works.models.depositWorks.DepositWorksCategory@EMERGENCYCUT}">
							<input type="submit" class="buttonadd" value="Upload Document" id="docUploadButton" onclick="showUploadDoc();return false;" />
  						</s:elseif>
  						<s:if test="%{mode=='edit' && sourcepage=='search' && applicationRequest.depositWorksCategory==@org.egov.works.models.depositWorks.DepositWorksCategory@EMERGENCYCUT}">
  							<input type="submit" class="buttonadd" value="View Document" id="docViewButton" onclick="viewDocument();return false;" />
  							<input type="submit" class="buttonadd" value="Upload Drawings" id="emergencyCutDocUploadButton" onclick="showUploadDoc();return false;" />
  						</s:if>
  						<s:if test="%{sourcepage=='search' && egwStatus!=null && egwStatus.code!='NEW'}">
								<input type="button" onclick="window.open('${pageContext.request.contextPath}/estimate/abstractEstimate!workflowHistory.action?stateValue=<s:property value="state.id"/>', '', 'height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');"
								class="buttonfinal" value=" Workflow History" id="history" name="History" />
						</s:if>
  						<s:if test="%{egwStatus!=null && egwStatus.code!='NEW' && egwStatus.code!='REJECTED'}">  
							  	<input type="submit" class="buttonadd" value="View Estimate for claim charges" id="fbRepDocViewButton" onclick="viewFeasibilityRepDocument();return false;" /> 
						</s:if>
						</div> 
						<div id="bpaButtons_div" style="display:none;">
							<s:submit cssClass="buttonfinal" onclick='return validateInputBPA()' value="Submit" id="bpaSubmitButton"  method="saveBPADetails" />
							<s:if test="%{mode=='rejectForResubmission' && sourcepage=='search'}">
								<s:submit cssClass="buttonfinal" value="REJECT" id="bpaRejectButton" method="rejectForResubmit" onclick="return validateRemarks();" />
							</s:if>
							<s:elseif test="%{mode=='UpdateRoadCutDate' || mode=='UpdateRoadCutRestorationDate'}">
								<s:submit cssClass="buttonfinal" onclick='return validateInput()' value="Submit" id="bpaUpdateRoadCutDatesButton" method="updateRoadCutDate" />
							</s:elseif>
							<s:if test="%{mode!='view'}">
								<input type="button" class="buttonfinal" value="CLEAR" id="bpaClearButton" name="clearButton" onclick="window.open('${pageContext.request.contextPath}/depositWorks/roadCut!newform.action','_self');"/>
				     		</s:if>
				     		<input type="button" class="buttonfinal" value="CLOSE" id="bpaCloseButton" name="bpaCloseButton" onclick="window.close();"/>
				     		<s:if test="%{mode=='view' || mode=='rejectForResubmission' && sourcepage=='search'}">
								<input type="submit" class="buttonadd" value="View Document" id="bpaDocViewButton" onclick="viewDocument();return false;" />
							</s:if>
							<s:else>
								<input type="submit" class="buttonadd" value="Upload Document" id="docUploadButton" onclick="showUploadDoc();return false;" />
	  						</s:else>
						</div>
				     </div>
				 </s:if>
				 <s:elseif test="%{sourcepage=='genFeasibilityReport' || sourcepage=='inbox'}">
						<div class="buttonholderwk" id="buttons">
						<s:if test="%{(sourcepage=='inbox' || model.egwStatus==null || model.egwStatus.code=='NEW'  
						|| model.egwStatus.code=='REJECTED') && mode !='view' && mode !='search' || hasErrors() || hasActionMessages()}"> 
							<s:if test="%{egwStatus==null || egwStatus.code=='NEW'}">
	 							<s:submit type="submit" cssClass="buttonfinal"
									value="Save" id="Save" name="Save"
									method="saveFeasibilityReport"
									onclick="document.roadCutForm.actionName.value='Save';return validateDataBeforeSubmit('Save');" />
	 						</s:if>
	 						
							<s:iterator value="%{getValidActions()}" var="name">
								<s:if test="%{name!=''}">
									<s:submit type="submit" cssClass="buttonfinal"
										value="%{name}" id="%{name}" name="%{name}"
										method="saveFeasibilityReport"
										onclick="document.roadCutForm.actionName.value='%{name}';return validateDataBeforeSubmit('%{name}');" />
								</s:if>
							</s:iterator>
							
							</s:if>
							<s:if test="%{((sourcepage=='inbox' && egwStatus!=null && egwStatus.code!='NEW') || (sourcepage=='search' && egwStatus!=null && egwStatus.code!='NEW'))}">
								<input type="button" onclick="window.open('${pageContext.request.contextPath}/estimate/abstractEstimate!workflowHistory.action?stateValue=<s:property value="state.id"/>', '', 'height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');"
								class="buttonfinal" value="Workflow History" id="history" name="History" />
							</s:if>
							<s:if test="%{id==null}">
								<input type="button" class="buttonfinal" value="CLEAR" id="clearButton" name="clearButton" onclick="window.open('${pageContext.request.contextPath}/depositWorks/roadCut!view.action?sourcepage=genFeasibilityReport&appDetailsId='+'<s:property value='appDetailsId' />','_self');"/>
							</s:if>
				     		<input type="button" class="buttonfinal" value="CLOSE" id="closeButton" name="closeButton" onclick="window.close();"/>
							
							<input type="submit" class="buttonadd" value="View Document" id="docViewButton" onclick="viewDocument();return false;" />
							
							<s:if test="%{egwStatus!=null && egwStatus.code!='NEW' && egwStatus.code!='REJECTED'}">  
							  	<input type="submit" class="buttonadd" value="View Estimate for claim charges" id="fbRepDocViewButton" onclick="viewFeasibilityRepDocument();return false;" /> 
							 </s:if> 
							 <s:else> 
								<input type="submit" class="buttonadd" value="Attach Estimate for claim charges" id="fbRepDocUploadButton" onclick="showWarning(this);return false;" />
									<div class="mandatory" align="right" style="font-size:11px;padding-right:20px;">
									# The document attached will be shared with the citizen for claim charges split up
									</div>
									<span class='warning' id="docUploadWarning"></span>
						 	</s:else>
						</div> 
				 </s:elseif>
				</div>
			</s:push>
		</s:form>
	</body>
</html>
 