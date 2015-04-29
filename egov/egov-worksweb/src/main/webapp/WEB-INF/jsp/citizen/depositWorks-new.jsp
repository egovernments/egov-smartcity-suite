<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags" %>
<html>
	<head>
		<title>
			<s:text name="depositworks.roadcut.title" />
		</title>
	</head>	
		<script src="<egov:url path='js/works.js'/>"></script>
		<script src="<egov:url path='js/helper.js'/>"></script>
	<script>
	var rowId='';
	var wardId='';
	var areaId='';
	var localityId='';
	var streetId='';
	var agencyId;
	var dwCategory='';
	window.history.forward(1);
	function noBack() {
		window.history.forward(); 
	}
	function load()
	{	   
		hideColumn('Add');
		hideColumn('Delete');
		var showScheme=false;
		var typeOfCutStr = "<s:property value='applicationRequest.depositWorksType.name' />";
		<s:iterator value="schemeBasedDWTypeList" var="dwTypeList">
			if (typeOfCutStr=="<s:property value='#dwTypeList' />")
			{
				showScheme=true;
			}
		</s:iterator>  
		if(showScheme)
		{
			document.getElementById("isSchemeBasedTextTD").style.visibility='visible';
			document.getElementById("isSchemeBasedTD").style.visibility='visible';
		}
		var emergencyCutRadioStatus = false;
		if(document.getElementById("depositWorksCategoryEMERGENCYCUT") != null)
			emergencyCutRadioStatus = document.getElementById("depositWorksCategoryEMERGENCYCUT").checked;
		if(emergencyCutRadioStatus) {
			document.getElementById("purposeOfCutLabelTD").style.visibility='visible';
			document.getElementById("purposeOfCutFieldTD").style.visibility='visible';
			if(document.getElementById("isSchemeBasedRadiotrue")!=null) {
				document.getElementById("isSchemeBasedRadiotrue").disabled=true;
			}
		}
		var bpaCutRadioStatus = false;
		if(document.getElementById("depositWorksCategoryBPA") != null)
			bpaCutRadioStatus = document.getElementById("depositWorksCategoryBPA").checked;
		if(bpaCutRadioStatus) {
			document.getElementById("newOrgznServiceDeptTR").style.display='none';
			document.getElementById("mirrorApplicantOrgznServDeptTR").style.display='none';
			document.getElementById("serviceDeptTR").style.display='';
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
		<s:if test="%{applicationRequest.applicant.name!=null}">
			document.getElementById('applicantName').value='<s:property value="%{applicationRequest.applicant.name}" />';
			document.getElementById('applicantName').readOnly = true;
		</s:if>
		<s:else>
			document.getElementById("serviceDeptTR").style.display='';
			document.getElementById("serviceDept").disabled="true";
		</s:else>
		<s:if test="%{applicationRequest.id==null}">
			setTypeOfCut('<s:property value = "%{applicationRequest.applicant.organization.id}" />', false);
			
		</s:if>
		
		appConfOrgn1 = '<s:property value='bpaOrganizations[0]' />';
		appConfOrgn2 = '<s:property value='bpaOrganizations[1]' />';
		organizationName = '<s:property value='applicationRequest.applicant.organization.name'/>';
		
		if(organizationName!=appConfOrgn1 && organizationName!=appConfOrgn2){
				jQuery(document).ready( jQuery('#depositWorksCategoryBPA').hide());
		 		jQuery(document).ready( jQuery('label[for="depositWorksCategoryBPA"]').hide());
		}		
		toggleOrgnAndServiceDept();
		
		if(document.getElementById("depositWorksCategory")!=null)
			toggleShowBPADetails(document.getElementById("depositWorksCategory"));
		var individualRadioStatus = false;
		<s:if test="%{applicationRequest.applicant.name!=null}">
			individualRadioStatus = true;
		</s:if>
		jQuery.noConflict();  
		if(individualRadioStatus)
			{
			 jQuery(document).ready( jQuery('#depositWorksCategoryREPAIRSANDMAINTENANCE').hide());
			 jQuery(document).ready( jQuery('label[for="depositWorksCategoryREPAIRSANDMAINTENANCE"]').hide());
			 jQuery(document).ready( jQuery('#depositWorksCategoryEMERGENCYCUT').hide());
			 jQuery(document).ready( jQuery('label[for="depositWorksCategoryEMERGENCYCUT"]').hide());
			}
	}  

	function enableOrdisableElements()
	{
		<s:if test="%{mode=='view'}">
			disableElements();
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
		<s:if test="%{mode=='UpdateRoadCutDate'}">
			enableRCApprovalLetterDates();
		</s:if>
	}

	function disableElements(){
		
		var links=document.getElementById("startDatelnk"); 
		links.onclick=function(){return false;};

		links=document.getElementById("endDatelnk"); 
		links.onclick=function(){return false;};
		
        for(var i=0;i<document.forms[0].length;i++) {
      		document.forms[0].elements[i].disabled =true;
      	}
      	document.depositWorksForm.closeButton.readonly=false;
		document.depositWorksForm.closeButton.disabled=false;
		if(document.depositWorksForm.docViewButton!=null){
			document.depositWorksForm.docViewButton.readonly=false;
			document.depositWorksForm.docViewButton.disabled=false;
		}
	    if(document.depositWorksForm.saveButton!=null)
      		$('saveButton').hide();
	    hideColumn('Add');	   
	    hideColumn('Delete');
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
		document.depositWorksForm.areDrawingsAttached.readonly = false;
		document.depositWorksForm.areDrawingsAttached.disabled = false;
		$('saveButton').show();
		$('docUploadButton').hide();
	
		document.depositWorksForm.saveButton.readonly = false;
		document.depositWorksForm.saveButton.disabled = false;
		document.depositWorksForm.emergencyCutDocUploadButton.readonly = false;
		document.depositWorksForm.emergencyCutDocUploadButton.disabled = false;
	}

	function enableModifiableElementsForBPACut(){
		disableElements();
		document.getElementById("purposeOfCutLabelTD").style.visibility='visible';
		document.getElementById("purposeOfCutFieldTD").style.visibility='visible';
    	document.getElementById('purposeOfRoadCut').disabled = false;
		var records= jurisdictionDetailsTable.getRecordSet();
		for(i=0;i<records.getLength();i++)
    	{
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
		$('saveButton').show();
		document.depositWorksForm.saveButton.readonly = false;
		document.depositWorksForm.saveButton.disabled = false;
		document.depositWorksForm.docUploadButton.readonly = false;
		document.depositWorksForm.docUploadButton.disabled = false;
	}
	
	function enableRCApprovalLetterDates(){
		showUpdateRoadCutDateTab();
        for(var i=0;i<document.forms[0].length;i++) {
      		document.forms[0].elements[i].disabled =true;
      	}
        var links; 

		links=document.getElementById("startDatelnk"); 
		links.onclick=function(){return true;};
		
		links=document.getElementById("endDatelnk"); 
		links.onclick=function(){return true;};
		
		document.depositWorksForm.rcStartDate.disabled =false;
	    document.depositWorksForm.rcStartDate.readonly=false;
	    document.depositWorksForm.rcEndDate.disabled =false;
	    document.depositWorksForm.rcEndDate.readonly=false;
		document.depositWorksForm.closeButton.readonly=false;
		document.depositWorksForm.closeButton.disabled=false;
		document.depositWorksForm.submitButton.readonly=false;
		document.depositWorksForm.submitButton.disabled=false;	
	    hideColumn('Add');	   
	    hideColumn('Delete');
	}
	
	function enableModifiableElements(){
		var domElement;
		var links;
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
        $('saveButton').show();
	    <s:if test="%{applicationRequest.isSchemeBased!=null && applicationRequest.isSchemeBased==true}" >
		    showColumn('Add');	   
		    showColumn('Delete');
	    </s:if>
	    <s:else>
		    hideColumn('Add');	   
		    hideColumn('Delete');
	    </s:else>
	}
	
	function validateInput()
	{
		<s:if test="%{mode!='UpdateRoadCutDate'}">
			var individualRadioStatus = false;
			var bpaCheckboxStatus = document.getElementById("depositWorksCategoryBPA").checked;
			var repairAndMentainanceRadioStatus = false;
			var emergencyCutRadioStatus = false;
			var bpaCutRadioStatus = false;
			if(document.getElementById("depositWorksCategoryREPAIRSANDMAINTENANCE") !=null )
				repairAndMentainanceRadioStatus = document.getElementById("depositWorksCategoryREPAIRSANDMAINTENANCE").checked;
			
			if(document.getElementById("depositWorksCategoryEMERGENCYCUT") != null)
				emergencyCutRadioStatus = document.getElementById("depositWorksCategoryEMERGENCYCUT").checked;
			
			if(document.getElementById("depositWorksCategoryBPA") != null)
				bpaCutRadioStatus = document.getElementById("depositWorksCategoryBPA").checked;

			<s:if test="%{applicationRequest.applicant.name!=null}">
				individualRadioStatus = true;
			</s:if>
			dom.get("citizenPortal_error").style.display="block";
			if(bpaCheckboxStatus && individualRadioStatus)
			{
				if(document.getElementById('rcDtlsOrganization').value==-1)
				{
					document.getElementById("citizenPortal_error").innerHTML='<s:text name="depositworks.select.organization.in.dtls" />';
					return false;							
				}
				if(document.getElementById('rcDtlsServiceDept').value==-1)
				{
					document.getElementById("citizenPortal_error").innerHTML='<s:text name="depositworks.select.service.dept.in.details" />';
					return false;							
				}	
			}
			if(document.getElementById('typeOfCut').value==-1)
			{
				document.getElementById("citizenPortal_error").innerHTML='<s:text name="depositworks.roadcut.enter.typeofcut" />';
				return false;
			}
			if(document.getElementById('applicantName').value=='')
			{
				document.getElementById("citizenPortal_error").innerHTML='<s:text name="depositworks.roadcut.enter.applicant.name" />';
				return false;
			}
			if((emergencyCutRadioStatus || bpaCutRadioStatus) && document.getElementById('purposeOfRoadCut').value== -1) {
				document.getElementById("citizenPortal_error").innerHTML='<s:text name="dw.roadcut.select.purposeofcut" />';
				return false;
			}
        	else if(!(emergencyCutRadioStatus || bpaCutRadioStatus) && document.getElementById('purposeOfRoadCut') != null) {
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
						document.getElementById("citizenPortal_error").innerHTML='<s:text name="depositworks.roadcut.enter.schemename" />';
						return false;
					}	
					if(document.getElementById("applicationRequest.schemeDetails").value=="")
					{
						document.getElementById("citizenPortal_error").innerHTML='<s:text name="depositworks.roadcut.enter.schemedetails" />';
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
						document.getElementById("citizenPortal_error").innerHTML='<s:text name="depositworks.roadcut.delete.multiplerows" />';
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
						document.getElementById("citizenPortal_error").innerHTML='<s:text name="depositworks.roadcut.delete.multiplerows" />';
						return false;
					}	
				}		
			}
			if(individualRadioStatus)
			{
				if(repairAndMentainanceRadioStatus){
					dom.get("citizenPortal_error").style.display='';
					document.getElementById("citizenPortal_error").innerHTML='<s:text name="depositworks.roadcut.validate.repairandmentainance"/>';
					return false;
				}
				else if(emergencyCutRadioStatus){
					dom.get("citizenPortal_error").style.display='';
					document.getElementById("citizenPortal_error").innerHTML='<s:text name="depositworks.roadcut.validate.emergencyCut"/>';
					return false;
				}
			}   
			<s:if test="%{mode=='edit' && applicationRequest.depositWorksCategory==@org.egov.works.models.depositWorks.DepositWorksCategory@EMERGENCYCUT}">
				dom.get("modifyMode").value="afterModify";
				dom.get("docNum").value=document.getElementById("docNumber").value;
				var cbox = document.getElementById("areDrawingsAttached");
				if(!cbox.checked){
					document.getElementById("citizenPortal_error").innerHTML='<s:text name="dw.emergencyCut.upload.drawing.checkbox.validate" />';
					return false;
				}
			</s:if>
			var records= jurisdictionDetailsTable.getRecordSet();
			var i;
			var recordId1;
            for(i=0;i<records.getLength();i++)
            {
            	recordId1=records.getRecord(i).getId();
            	if(document.getElementById("depositWorksCategoryBPA")!=null && document.getElementById("depositWorksCategoryBPA").checked==true) {
                	if(dom.get("BPANumber"+recordId1).value=="") {
                		document.getElementById("citizenPortal_error").innerHTML='<s:text name="depositworks.roadcut.null.bpanumber" />';
						return false;
                    }
            	}
            	if(dom.get("LocationName"+recordId1).value=="")
                {
            		document.getElementById("citizenPortal_error").innerHTML='<s:text name="depositworks.roadcut.null.roadname" />';
					return false;
                }
            	if(dom.get("Zone"+recordId1).value==-1)
                {
            		document.getElementById("citizenPortal_error").innerHTML='<s:text name="depositworks.roadcut.null.zone" />';
					return false;
                }
            	if(dom.get("Ward"+recordId1).value==-1)
                {
            		document.getElementById("citizenPortal_error").innerHTML='<s:text name="depositworks.roadcut.enter.ward" />';
					return false;
                }
            	if(dom.get("Area"+recordId1).value==-1)
                {
            		document.getElementById("citizenPortal_error").innerHTML='<s:text name="depositworks.roadcut.enter.area" />';
					return false;
                }	
            	if(dom.get("Locality"+recordId1).value==-1)
                {
            		document.getElementById("citizenPortal_error").innerHTML='<s:text name="depositworks.roadcut.enter.locality" />';
					return false;
                }
            	if(dom.get("Street"+recordId1).value==-1)
                {
            		document.getElementById("citizenPortal_error").innerHTML='<s:text name="depositworks.roadcut.enter.street" />';
					return false;
                }
            	if(dom.get("Length"+recordId1).value=="")
                {
            		document.getElementById("citizenPortal_error").innerHTML='<s:text name="depositworks.roadcut.enter.length" />';
					return false;
                }
            	if(dom.get("Breadth"+recordId1).value=="")
                {
            		document.getElementById("citizenPortal_error").innerHTML='<s:text name="depositworks.roadcut.enter.breadth" />';
					return false;
                }
            	if(emergencyCutRadioStatus || bpaCutRadioStatus) {
                	if(dom.get("DepthDropDown"+recordId1).value == -1) {
						document.getElementById("citizenPortal_error").innerHTML='<s:text name="dw.roadcut.select.depthofcut" />';
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
          </s:if>
          <s:if test="%{mode=='UpdateRoadCutDate' }">
    		if(dom.get('rcStartDate').value=='' && dom.get('rcEndDate').value=='') 
    		{
    			dom.get("citizenPortal_error").style.display='';
    			document.getElementById("citizenPortal_error").innerHTML='<s:text name="depositworks.roadcut.validate.Dates" />';
    			dom.get("rcStartDate").focus(); 
    			return false;
    		}
    		if(dom.get('rcStartDate').value=='' && dom.get('rcEndDate').value!='') 
    		{
    			dom.get("citizenPortal_error").style.display='';
    			document.getElementById("citizenPortal_error").innerHTML='<s:text name="depositworks.roadcut.validate.startdate" />';
    			dom.get("rcStartDate").focus(); 
    			return false;
    		}
    		if(dom.get('rcStartDate').value!='' && dom.get('rcEndDate').value!='')
    		{
    			if(compareDate(dom.get('rcStartDate').value,dom.get('rcEndDate').value) == -1 ){
    				dom.get("citizenPortal_error").style.display='';
    				document.getElementById("citizenPortal_error").innerHTML='<s:text name="depositworks.roadcut.greaterthan.endDate.startdate" />';
    				dom.get("rcEndDate").focus(); 
    				return false;
    			}
    		} 

    		for(var i=0;i<document.forms[0].length;i++) {
          		document.forms[0].elements[i].disabled =false; 
          	}
    	</s:if>
    	if(bpaCutRadioStatus){
			var doc = dom.get('docNumber').value;
			if(doc==''){
				document.getElementById("citizenPortal_error").innerHTML='<s:text name="dw.bpa.document.upload.mandatory" />';
				return false;
			}
		}
	    dom.get("citizenPortal_error").style.display='none'; 
	  	dom.get("citizenPortal_error").innerHTML='';
	  	<s:if test="%{mode=='edit'}"> 
	        for(var i=0;i<document.forms[0].length;i++) {
	      		document.forms[0].elements[i].disabled =false;
	      	}
	    </s:if>
	    enableBoundaryDetailsForBPARoadCut();
	    document.getElementById("typeOfCut").disabled=false;
	    if(!(emergencyCutRadioStatus || bpaCutRadioStatus) && document.getElementById('purposeOfRoadCut') != null) {
    		document.getElementById('purposeOfRoadCut').disabled = true;
		}
		return true;
	}

	function setCSSClasses(id,classes){
	    document.getElementById(id).setAttribute('class',classes);
	    document.getElementById(id).setAttribute('className',classes);

	}
	function showRequestDetailsTab(){
		  document.getElementById('requestDetails_div').style.display='';
		  setCSSClasses('requestDetailsTab','First Active');
		  if(document.getElementById('updateRoadCutDateTab')!=null) {
	 		setCSSClasses('updateRoadCutDateTab','Last');
	 		hideUpdateRoadCutDateTab();
	 	  }
	}
	function showUpdateRoadCutDateTab(){
		$('roadCut_div').show();
		document.getElementById('requestDetails_div').style.display='none';
		hideRequestDetailsTab();
	 	setCSSClasses('requestDetailsTab','First');
	    setCSSClasses('updateRoadCutDateTab','Last Active ActiveLast');
	}
	function hideRequestDetailsTab(){
		  document.getElementById('requestDetails_div').style.display='none';
	}
	function hideUpdateRoadCutDateTab(){
		  document.getElementById('roadCut_div').style.display='none';
	}
	var DETAILS_ELEMENT_PREFIX;
	<s:if test="%{mode=='edit' && applicationRequest.depositWorksCategory==@org.egov.works.models.depositWorks.DepositWorksCategory@BPA && applicationRequest.applicant.organization != null}">
		DETAILS_ELEMENT_PREFIX ='roadCutDetList';
	</s:if>
	<s:else>
		DETAILS_ELEMENT_PREFIX ='applicationRequest.roadCutDetailsList';
	</s:else>
		function createDetailsTableHiddenIdFormatter(suffix){
			var textboxFormatter = function(el, oRecord, oColumn, oData) {
				var value = (YAHOO.lang.isValue(oData))?oData:"";
				var fieldName = DETAILS_ELEMENT_PREFIX+"[" + oRecord.getCount() + "]." + suffix;
				markup="<input type='hidden' id='"+oColumn.getKey()+oRecord.getId()+"'  value='"+value+"' class='selectmultilinewk' name='"+fieldName+"'  />";
				el.innerHTML = markup;
			};
			return textboxFormatter;
		}
		var dataTableHiddenId = createDetailsTableHiddenIdFormatter('id');
		
	function createDetailsTableTextAreaFormatter(suffix){
			var textboxFormatter = function(el, oRecord, oColumn, oData) {
				var value = (YAHOO.lang.isValue(oData))?oData:"";
				var fieldName = DETAILS_ELEMENT_PREFIX+"[" + oRecord.getCount() + "]." + suffix;
				if(value!=null)
					markup="<textarea rows='2' cols='50' id='"+oColumn.getKey()+oRecord.getId()+"'   style=width:160px  class='selectmultilinewk'  name='"+fieldName+"' >"+value+"</textarea>";
				else
					markup="<textarea rows='2' cols='50' id='"+oColumn.getKey()+oRecord.getId()+"'   style=width:160px  class='selectmultilinewk'  name='"+fieldName+"' ></textarea>";
				el.innerHTML = markup;
			};
			return textboxFormatter;
		}
		function createDetailsTableTBNumberFormatter( suffix){
			var textboxFormatter = function(el, oRecord, oColumn, oData) {
				var fieldName = DETAILS_ELEMENT_PREFIX+"[" + oRecord.getCount() + "]." + suffix;
				var value = (YAHOO.lang.isValue(oData))?oData:"";
				markup="<input type='text' id='"+oColumn.getKey()+oRecord.getId()+"' value='"+value+"' class='selectmultilinewk'  name='"+fieldName+"' onblur='validateNumber(this)' />";
				el.innerHTML = markup;
			};
			return textboxFormatter;
		}
		var dataTableTBRoadName = createDetailsTableTextAreaFormatter('locationName');
		var dataTableTBRemarks = createDetailsTableTextAreaFormatter('remarks');
		var dataTableTBLength = createDetailsTableTBNumberFormatter('roadLength');
		var dataTableTBBreadth = createDetailsTableTBNumberFormatter('roadBreadth');
		var dataTableTBDepth = createDetailsTableTBNumberFormatter('roadDepth');
		var dataTableTBRemarksEmergency = createDetailsTableTextAreaFormatter('remarksEmergency');
		
	    function createDetailsTableZoneDropDown(suffix){
	    	return function(el, oRecord, oColumn, oData) {
	    		var fieldName = DETAILS_ELEMENT_PREFIX+"[" + oRecord.getCount() + "]." + suffix;
	    		var element="<select  id='"+oColumn.getKey()+oRecord.getId()+"' name='"+fieldName+"'  style=width:90px  onchange='resetDropDowns(this,4);'  >";
	    		element=element+"<option value=-1 selected='selected' >Choose</option>  ";
	    		<s:iterator value="dropdownData.zoneList" status="stat">
	    			var name='<s:property value="name"/> - <s:property value="parent.name"/>';
	    			var id1='<s:property value="id" />';
	    			element=element+" <option value="+id1 +" > "+ name+" </option>  ";
	    		</s:iterator>
	    		element=element+" </select>";
	    		el.innerHTML =element ;
	    		};
	    }
	    var dataTableDDZone = createDetailsTableZoneDropDown('zone.id');
	    
	    function createEmptyDropDown(suffix){
	    	return function(el, oRecord, oColumn, oData) {
				var onchangeParam;
	            if(suffix=='ward.id')
	        		onchangeParam='resetDropDowns(this,3);';
	        	if(suffix=='area.id')
	         		onchangeParam='resetDropDowns(this,2);';
	         	if(suffix=='locality.id')
	             	onchangeParam='resetDropDowns(this,1);';                	
	                		
	    		var fieldName = DETAILS_ELEMENT_PREFIX+"[" + oRecord.getCount() + "]." + suffix;
	    		var element="<select  id='"+oColumn.getKey()+oRecord.getId()+"' name='"+fieldName+"'  style=width:170px  onchange='"+onchangeParam+"'  ><option value=-1 selected='selected' > --- Choose --- </option>  ";
	    		if(suffix=='ward.id')
	    			element="<select  id='"+oColumn.getKey()+oRecord.getId()+"' name='"+fieldName+"'  style=width:90px  onchange='"+onchangeParam+"'  ><option value=-1 selected='selected' >Choose</option>  ";
	    		if(suffix=='street.id')
	    			element="<select  id='"+oColumn.getKey()+oRecord.getId()+"' name='"+fieldName+"'  style=width:170px  ><option value=-1 selected='selected' > --- Choose --- </option>  ";
	    		element=element+" </select>";
	    		el.innerHTML =element ;
	    		};
	    }
	    var dataTableDDWard = createEmptyDropDown('ward.id');
	    var dataTableDDArea = createEmptyDropDown('area.id');
	    var dataTableDDLocality = createEmptyDropDown('locality.id');
	    var dataTableDDStreet = createEmptyDropDown('street.id');
	    
	    function resetDropDowns(obj,level)
	    {
	        var objName = obj.name;
	        var rowIndex = getRowIndexWithRowName(objName);
	        var namePrefix = DETAILS_ELEMENT_PREFIX + "[" + rowIndex + "]." ;
	        var dropdownObj;
	        var i;
	        if(level>=1)
	        {
	        	dropdownObj = document.getElementsByName(namePrefix+"street.id")[0];
	        	for(i=dropdownObj.options.length-1;i>=1;i--)
	        	{
	        		dropdownObj.remove(i);
	        	}
	        }
	        if(level>=2)
	        {
	        	dropdownObj = document.getElementsByName(namePrefix+"locality.id")[0];
	        	for(i=dropdownObj.options.length-1;i>=1;i--)
	        	{
	        		dropdownObj.remove(i);
	        	}
	        }
	        if(level>=3)
	        {
	        	dropdownObj = document.getElementsByName(namePrefix+"area.id")[0];
	        	for(i=dropdownObj.options.length-1;i>=1;i--)
	        	{
	        		dropdownObj.remove(i);
	        	}
	        }
	        if(level>=4)
	        {
	        	dropdownObj = document.getElementsByName(namePrefix+"ward.id")[0];
	        	for(i=dropdownObj.options.length-1;i>=1;i--)
	        	{
	        		dropdownObj.remove(i);
	        	}
	        }
	        if(level==4)
	        	ajaxLoadDropdown(obj,"populateWard","zoneId","zone","ward");
	        if(level==3)
	        	ajaxLoadDropdown(obj,"populateArea","wardId","ward","area");
	        if(level==2)
	        	ajaxLoadDropdown(obj,"populateLocality","areaId","area","locality");
	        if(level==1)
	        	ajaxLoadDropdown(obj,"populateStreets","locationId","locality","street");
	        
	    }
	    function getRowIndexWithRowName(name)
	    {
	        var temp = name.split("[");
	        var rowIndex = temp[1].split("]")[0];
			return rowIndex;
	    }
	    function escapeSpecialChars(str) {
	    	str1 = str.replace(/\'/g, "\\'");
	    	str2 = str1.replace(/\"/g, '&quot;');
	    	str3 = str2.replace(/\r\n/g, "&#13;");
	    	return str3;
	    }  
	    function getIndexOfRowBasedOnRecordId(recordId)
	    {
	    	var records= jurisdictionDetailsTable.getRecordSet();
			var i;
			var recordId1;
	        for(i=0;i<records.getLength();i++)
	        {
	        	recordId1=records.getRecord(i).getId();
	        	if(recordId==recordId1)
	            	return i;
	        }
	        return 0;	
	    }

	    function onclickOfBPASearch(recordId)
	    {
	        
	        var index = getIndexOfRowBasedOnRecordId(recordId);
	         <s:if test="%{id == null || (mode=='edit' && applicationRequest.depositWorksCategory==@org.egov.works.models.depositWorks.DepositWorksCategory@BPA && applicationRequest.applicant.organization != null)}">
	            if(document.getElementById("bpaNumberSpanId"+recordId)){
	                window.open("${pageContext.request.contextPath}/../bpa/search/search!searchApplForm.action?rowId="+index,"","height=600,width=1200,scrollbars=yes,left=0,top=0,status=yes");
	            }
	        </s:if>
	        <s:elseif test="%{id != null}">
	            showBPAViewPage(dom.get("BPANumber"+recordId).value);
	        </s:elseif>
	    }
	    function createBPANumberTextboxFormatter(size,maxlength){
	        var textboxFormatter = function(el, oRecord, oColumn, oData) {
	            var bpaNumber = (YAHOO.lang.isValue(oData))?oData:"";
	            var id=oColumn.getKey()+oRecord.getId();
	            var onclickCallForAnchor = "showBPAViewPageById('"+id+"')";
	            var spanId="bpaNumberSpanId"+oRecord.getId()
	            var fieldName=DETAILS_ELEMENT_PREFIX+"[" + oRecord.getCount() + "].bpaNumber";
	            markup="<input type='hidden' id='"+id+"' name='"+fieldName+"' value='"+bpaNumber+"' size='"+size+"' maxlength='"+maxlength+"' class='selectwk' /><a href='#' onclick="+onclickCallForAnchor+" ><span id='"+spanId+"'>"+bpaNumber+"</span></a>";
	            var markupForSearchImage='';
	            var onclickCallForImage = "onclickOfBPASearch('"+oRecord.getId()+"')";
	            <s:if test="%{id == null || (mode=='edit' && applicationRequest.depositWorksCategory==@org.egov.works.models.depositWorks.DepositWorksCategory@BPA && applicationRequest.applicant.organization != null)}">
	                var imageURL="${pageContext.request.contextPath}/image/magnifier.png";
	                markupForSearchImage='&nbsp;&nbsp;&nbsp;&nbsp;<a href="#"><img src="'+imageURL+'" height=16  width=16 border="0" alt="Search" onclick='+onclickCallForImage+' align="absmiddle"></a>';
	            </s:if>
	            el.innerHTML = markup+markupForSearchImage;
	            }
	            return textboxFormatter;
	    }
	    
	    var bpaNumberTextboxFormatter = createBPANumberTextboxFormatter(15,50);

	    function showBPAViewPage(bpaNumber) {
    		window.open("/bpa/register/registerBpa!viewRegForm.action?planSubmissionNum="+bpaNumber,"","height=600,width=1200,scrollbars=yes,left=0,top=0,status=yes");
        }

        function showBPAViewPageById(objId) {
            var bpaNumber = document.getElementById(objId).value;
            window.open("/bpa/register/registerBpa!viewRegForm.action?planSubmissionNum="+bpaNumber,"","height=600,width=1200,scrollbars=yes,left=0,top=0,status=yes");
        }

        function createDepthOfCutDropDown(suffix){
            return function(el, oRecord, oColumn, oData) {
                var fieldName = DETAILS_ELEMENT_PREFIX+"[" + oRecord.getCount() + "]." + suffix;
                var element="<select  id='"+oColumn.getKey()+oRecord.getId()+"' name='"+fieldName+"'  style='width:70px' >";
                element=element+"<option value=-1 selected='selected' >Choose</option>  ";
                <s:iterator value="dropdownData.depthOfCutList" status="stat">
                    var name='<s:property value="label"/>';
                    var id1='<s:property value="value" />';
                    element=element+" <option value="+id1 +" > "+ name+" </option>  ";
                </s:iterator>
                element=element+" </select>";
                el.innerHTML =element ;
                };
        }
        var dataTableDDDepthOfCut = createDepthOfCutDropDown('roadDepthEmergency');
          	     
		var jurisdictionDetailsTable;
		var makeJurisdictionDetailsTable = function() {
			var jurisDetailsColumns = [
				{key:"SlNo", label:'SNo', width:15,sortable:false, resizeable:false},
				{key:"Id",hidden:true, formatter:dataTableHiddenId,sortable:false, resizeable:false},
				{key:"BPANumber",hidden:true,label:'BPA Number<span class="mandatory">*</span>',  width:100, formatter:bpaNumberTextboxFormatter,sortable:false, resizeable:false},		                   		
				{key:"LocationName",label:'Address/Description<span class="mandatory">*</span>',  width:170, formatter:dataTableTBRoadName,sortable:false, resizeable:false},		                   		 
				{key:"Zone", label:'Zone<span class="mandatory">*</span>', width:90,formatter:dataTableDDZone,resizeable:true},		
				{key:"Ward", label:'Ward<span class="mandatory">*</span>', width:90, formatter:dataTableDDWard,resizeable:true},
				{key:"Area", label:'Area<span class="mandatory">*</span>', width:170, formatter:dataTableDDArea,resizeable:true},
				{key:"Locality", label:'Locality<span class="mandatory">*</span>', width:170, formatter:dataTableDDLocality,resizeable:true},
				{key:"Street", label:'Street<span class="mandatory">*</span>', width:170, formatter:dataTableDDStreet,resizeable:true},			
				{key:"Length",label:'Cut Length<span class="mandatory">*</span><br />(meters)', width:60, formatter:dataTableTBLength,sortable:false, resizeable:false},
				{key:"Breadth",label:'Cut Breadth<span class="mandatory">*</span><br />(meters)', width:60, formatter:dataTableTBBreadth,sortable:false, resizeable:false},
				{key:"Depth",label:'Cut Depth<br />(feet)', width:60, formatter:dataTableTBDepth,sortable:false, resizeable:false},
				{key:"Remarks",label:'Purpose of Cut', width:170, formatter:dataTableTBRemarks,sortable:false, resizeable:false},
				{key:"DepthDropDown",hidden:true,label:'Depth of Cut<span class="mandatory">*</span><br />(meters)', width:70, formatter:dataTableDDDepthOfCut,resizeable:true},
	            {key:"RemarksEmergency",hidden:true,label:'Remarks', width:170, formatter:dataTableTBRemarksEmergency,sortable:false, resizeable:false},
				{key:'Add',label:'Add',formatter:createAddImageFormatter("${pageContext.request.contextPath}")},
				{key:'Delete',label:'Delete', width:22,formatter:createDeleteImageFormatter("${pageContext.request.contextPath}")}  
			];
			var jurisdictionDS = new YAHOO.util.DataSource(); 
			jurisdictionDetailsTable = new YAHOO.widget.DataTable("jurisdictionDetailsTableDiv",jurisDetailsColumns, jurisdictionDS);	 
			jurisdictionDetailsTable.on('cellClickEvent',function (oArgs) {
				var target = oArgs.target;
				var record = this.getRecord(target);
				var column = this.getColumn(target);
				if (column.key == 'Add') { 
					jurisdictionDetailsTable.addRow({SlNo:jurisdictionDetailsTable.getRecordSet().getLength()+1});
				}

				if (column.key == 'Delete') {  	
					if(this.getRecordSet().getLength()>1){	
						this.deleteRow(record);
						var allRecords=this.getRecordSet();
						var i;
						for(i=0;i<allRecords.getLength();i++){
							this.updateCell(this.getRecord(i),this.getColumn('SlNo'),""+(i+1)); 
						}
					}
					else
					{
						alert("This row can not be deleted");
					}
				}        
			});
			
			<s:if test="%{applicationRequest.roadCutDetailsList==null || applicationRequest.roadCutDetailsList.size()==0}" >
				jurisdictionDetailsTable.addRow({SlNo:jurisdictionDetailsTable.getRecordSet().getLength()+1});
			</s:if>	
		};
		var selectedDropdownObjectName;
		var suffixToBeReplaced;
		var suffixToReplaceWith;
		
		function ajaxLoadDropdown(obj, urlMethod, paramPassed, paramSuffixToBeReplaced, paramSuffixToReplaceWith){
			var paramValue=obj.value;
			selectedDropdownObjectName=obj.name;
			suffixToBeReplaced = paramSuffixToBeReplaced;
			suffixToReplaceWith = paramSuffixToReplaceWith;
			var url = '../citizen/ajaxDepositWorks!'+urlMethod+'.action?'+paramPassed+'='+paramValue;
			YAHOO.util.Connect.asyncRequest('POST', url, ajaxResultList, null);
		
		}
	
		var ajaxResultList={
				success: function(o) {
					if(o.responseText!="")
					{
						var docs=o.responseText;               
						res=docs.split("$");
						var dropdownName=selectedDropdownObjectName;
						dropdownName=dropdownName.replace(suffixToBeReplaced,suffixToReplaceWith);
						var x=document.getElementsByName(dropdownName)[0];
						x.length=0;
						x.options[0]=new Option("---Choose---","-1");  
						var j=0;         
						for(var i=0;i<res.length-1;i++)
						{
							var idandnum=res[i].split('~');
							x.options[++j]=new Option(idandnum[0],idandnum[1]);
					    }                     
					}
				},                                         
				failure: function(o) {
					alert('Cannot fetch zone list');
				}
			};
		function validateNumber(obj)
		{
			var text = obj.value;
			if(text=='')
				return;
			if(isNaN(text))
			{
				alert('<s:text name="depositworks.roadcut.invalid.cutdetails" />');
				obj.value="";
				return;
			}
			if(text<=0)
			{
				alert('<s:text name="depositworks.roadcut.negative.cutdetails" />');
				obj.value='';
				return;
			}
			if(text.replace("+","~").search("~")!=-1)
			{
				alert('<s:text name="depositworks.roadcut.plus.notallowed" />');
				obj.value='';
				return;
			}
		}
		function hideColumn(colKey)
		{
			jurisdictionDetailsTable.hideColumn(colKey);
		}
		function showColumn(colKey)
		{
			jurisdictionDetailsTable.showColumn(colKey);
		}
		function showOrHideAdd(obj)
		{
			if(obj.value=='true')
			{
				jurisdictionDetailsTable.showColumn("Add");
				jurisdictionDetailsTable.showColumn("Delete");
				document.getElementById("schemenameTextTD").style.visibility='visible';
				document.getElementById("schemenameTD").style.visibility='visible';
				document.getElementById("schemedetailsTextTD").style.visibility='visible';
				document.getElementById("schemedetailsTD").style.visibility='visible';
			}
			else
			{
				jurisdictionDetailsTable.hideColumn("Add");
				jurisdictionDetailsTable.hideColumn("Delete");
					
				//RESET ELEMENTS
				document.getElementById("applicationRequest.schemeName").value="";
				document.getElementById("applicationRequest.schemeDetails").value="";
	
				//HIDE ELEMENTS
				document.getElementById("schemenameTextTD").style.visibility='hidden';
				document.getElementById("schemenameTD").style.visibility='hidden';
				document.getElementById("schemedetailsTextTD").style.visibility='hidden';
				document.getElementById("schemedetailsTD").style.visibility='hidden';
			}	
		}
		function showOrHideSchemeDetails(obj)
		{
			var objText = obj[obj.selectedIndex].text;
			var showScheme=false;
			<s:iterator value="schemeBasedDWTypeList" var="dwTypeList">
				if (objText=="<s:property value='#dwTypeList' />")
				{
					showScheme=true;
				}
			</s:iterator>
			if(showScheme)
			{
				document.getElementById("isSchemeBasedTextTD").style.visibility='visible';
				document.getElementById("isSchemeBasedTD").style.visibility='visible';
			}
			else
			{
				jurisdictionDetailsTable.hideColumn("Add");
				// RESET VALUES
				document.getElementById("isSchemeBasedRadiofalse").checked="checked";
				document.getElementById("applicationRequest.schemeName").value="";
				document.getElementById("applicationRequest.schemeDetails").value="";
	
				// HIDE THE ELEMENTS
				document.getElementById("isSchemeBasedTextTD").style.visibility='hidden';
				document.getElementById("isSchemeBasedTD").style.visibility='hidden';
				document.getElementById("schemenameTextTD").style.visibility='hidden';
				document.getElementById("schemenameTD").style.visibility='hidden';
				document.getElementById("schemedetailsTextTD").style.visibility='hidden';
				document.getElementById("schemedetailsTD").style.visibility='hidden';
			}		
		}

		function viewDocument(){
			  viewDocumentManager(dom.get("docNumber").value); 
		}

		function toggleOrgnAndServiceDept()
		{
			var individualRadioStatus = false;
			var isBPA = false; 
			<s:if test="%{applicationRequest.applicant.name!=null}">
				individualRadioStatus = true;
			</s:if>
			isBPA = document.getElementById("depositWorksCategoryBPA").checked;
			if(isBPA) {
				if(individualRadioStatus)
				{
					document.getElementById("newOrgznServiceDeptTR").style.display='none';
					document.getElementById("serviceDeptTR").style.display='none';
				}		
				else
				{
					document.getElementById("serviceDeptTR").style.display='';
					document.getElementById("newOrgznServiceDeptTR").style.display='none';
					document.getElementById("mirrorApplicantOrgznServDeptTR").style.display='none';
				}		
			}
			else {
				if(individualRadioStatus)
				{
					document.getElementById("newOrgznServiceDeptTR").style.display='none';
					document.getElementById("serviceDeptTR").style.display='none';
				}		
				else
				{
					document.getElementById("serviceDeptTR").style.display='';
					document.getElementById("newOrgznServiceDeptTR").style.display='none';
				}
			}
			showOrHideSchemeDetails(document.getElementById("typeOfCut"));
		}

		function toggleShowBPADetails(obj) {
			var individualRadioStatus = false;
			<s:if test="%{applicationRequest.applicant.name!=null}">
				individualRadioStatus = true;
			</s:if>
			document.getElementById("mirrorApplicantOrgznServDeptTR").style.display='none';

			agencyId = '<s:property value = "%{applicationRequest.applicant.organization.id}" />';
			jurisdictionDetailsTable.showColumn("Depth");
			jurisdictionDetailsTable.showColumn("Remarks"); 
			jurisdictionDetailsTable.hideColumn("DepthDropDown");
		    jurisdictionDetailsTable.hideColumn("RemarksEmergency");
			document.getElementById("purposeOfCutLabelTD").style.visibility='hidden';
			document.getElementById("purposeOfCutFieldTD").style.visibility='hidden';
			if(document.getElementById("isSchemeBasedRadiotrue")!=null) {
				document.getElementById("isSchemeBasedRadiotrue").disabled=false;
			}
			
			if(obj.value == 'BPA') {
				jurisdictionDetailsTable.showColumn("BPANumber");
				if(individualRadioStatus)
				{
					document.getElementById("newOrgznServiceDeptTR").style.display='none';
					document.getElementById("serviceDeptTR").style.display='none';
				}		
				else
				{
					document.getElementById("serviceDeptTR").style.display='';
					document.getElementById("newOrgznServiceDeptTR").style.display='none';
				}
				document.getElementById("purposeOfCutLabelTD").style.visibility='visible';
				document.getElementById("purposeOfCutFieldTD").style.visibility='visible';
		    	document.getElementById('purposeOfRoadCut').disabled = false;	
				jurisdictionDetailsTable.hideColumn("Depth");
		    	jurisdictionDetailsTable.hideColumn("Remarks");
		    	jurisdictionDetailsTable.showColumn("DepthDropDown");
		    	jurisdictionDetailsTable.showColumn("RemarksEmergency");   
		    	if(document.getElementById("isSchemeBasedRadiotrue")!=null) {
					document.getElementById("isSchemeBasedRadiotrue").disabled=true;
					document.getElementById("isSchemeBasedRadiofalse").checked=true;
				 	document.getElementById("schemenameTextTD").style.visibility='hidden';
		            document.getElementById("schemenameTD").style.visibility='hidden';
		            document.getElementById("schemedetailsTextTD").style.visibility='hidden';
		            document.getElementById("schemedetailsTD").style.visibility='hidden';
				}
				
		    	<s:if test="%{(applicationRequest.id==null)}">
					document.getElementById("typeOfCut").disabled=false;
					dwCategory = 'BPA';
					prepareTypeOfCutDropDown(agencyId,dwCategory);
				</s:if>	
			}
			else if(obj.value == 'EMERGENCYCUT') {
			    	jurisdictionDetailsTable.hideColumn("BPANumber");
			    	jurisdictionDetailsTable.hideColumn("Depth");
			    	jurisdictionDetailsTable.hideColumn("Remarks");
			    	jurisdictionDetailsTable.showColumn("DepthDropDown");
			    	jurisdictionDetailsTable.showColumn("RemarksEmergency");   
			    	document.getElementById("purposeOfCutLabelTD").style.visibility='visible';
					document.getElementById("purposeOfCutFieldTD").style.visibility='visible';
			    	document.getElementById('purposeOfRoadCut').disabled = false;	
					if(document.getElementById("isSchemeBasedRadiotrue")!=null) {
						document.getElementById("isSchemeBasedRadiotrue").disabled=true;
						document.getElementById("isSchemeBasedRadiofalse").checked=true;
					 	document.getElementById("schemenameTextTD").style.visibility='hidden';
			            document.getElementById("schemenameTD").style.visibility='hidden';
			            document.getElementById("schemedetailsTextTD").style.visibility='hidden';
			            document.getElementById("schemedetailsTD").style.visibility='hidden';
					}
			        if(individualRadioStatus)
			        {
			        	document.getElementById("newOrgznServiceDeptTR").style.display='none';
			            document.getElementById("serviceDeptTR").style.display='none';
			        }        
			        else
			        {
			        	document.getElementById("serviceDeptTR").style.display='';
			            document.getElementById("newOrgznServiceDeptTR").style.display='none';
			        } 
			        <s:if test="%{(applicationRequest.id==null)}">
				        dwCategory='';
				        prepareTypeOfCutDropDown(agencyId,dwCategory);
				    </s:if>	    
		    }
			else {
				jurisdictionDetailsTable.hideColumn("BPANumber");
				if(individualRadioStatus)
				{
					document.getElementById("newOrgznServiceDeptTR").style.display='none';
					document.getElementById("serviceDeptTR").style.display='none';
				}		
				else
				{
					document.getElementById("serviceDeptTR").style.display='';
					document.getElementById("newOrgznServiceDeptTR").style.display='none';
				}
			 	<s:if test="%{(applicationRequest.id==null)}">
					dwCategory='';
					prepareTypeOfCutDropDown(agencyId,dwCategory);
				 </s:if>
			}
			
		}
		
		function prepareTypeOfCutDropDown(agencyId,dwCategory)
		{
			makeJSONCall(["Value","Text"],'${pageContext.request.contextPath}/depositWorks/ajaxDepositWorks!populateTypeOfCut.action',{organizationId:agencyId,dwType:dwCategory},typeOfCutDropDownSuccess,typeOfCutDropDownFailure) ;
		}

		typeOfCutDropDownSuccess = function(req,res){
			results=res.results;
			var opt;

			typeOfCutDropdown=dom.get("typeOfCut");
			document.getElementById("typeOfCut").options.length = 0;
			for(i=0;i<results.length;i++){
				typeOfCutDropdown.options[i]=new Option(results[i].Text,results[i].Value);
			}
			if(results.length==1){
				document.getElementById("typeOfCut").disabled=true;
			}
			if(dwCategory!='BPA'){
				setTypeOfCut(agencyId,false);
			}
		}
		
		typeOfCutDropDownFailure= function(){
		  	dom.get("citizenPortal_error").style.display='';
			document.getElementById("citizenPortal_error").innerHTML='<s:text name="dw.bpa.typeOfCut.ajax.failure" />';
		}
		
		function update(elemValue) {	
			if(elemValue!="" || elemValue!=null) {
				var a = elemValue.split("`~`");
				//alert(a);
				var records= jurisdictionDetailsTable.getRecordSet();
				rowId=a[0];
				var bpaNumber=a[1];
				var zoneId=a[2];
				wardId=a[3];
				areaId=a[4];
				localityId=a[5];
				streetId=a[6];
				dom.get("BPANumber"+records.getRecord(getNumber(rowId)).getId()).value=bpaNumber;
				document.getElementById("bpaNumberSpanId"+records.getRecord(getNumber(rowId)).getId()).innerHTML=bpaNumber;
				if(zoneId !=null && zoneId != '') {
					dom.get("Zone"+records.getRecord(getNumber(rowId)).getId()).value=zoneId;
					dom.get("Zone"+records.getRecord(getNumber(rowId)).getId()).disabled=true;
					ajaxLoadDropdownForBPA(dom.get("Zone"+records.getRecord(getNumber(rowId)).getId()),"populateWard","zoneId","zone","ward");
				}
				if (wardId == null || wardId == '') {
					dom.get("Ward"+records.getRecord(getNumber(rowId)).getId()).disabled=false;
				}
				if (areaId == null || areaId == '') {
					dom.get("Area"+records.getRecord(getNumber(rowId)).getId()).disabled=false;
				}
				if (localityId == null || localityId == '') {
					dom.get("Locality"+records.getRecord(getNumber(rowId)).getId()).disabled=false;
				}
				if (streetId == null || streetId == '') {
					dom.get("Street"+records.getRecord(getNumber(rowId)).getId()).disabled=false;
				}
			}
		}

		function ajaxLoadDropdownForBPA(obj, urlMethod, paramPassed, paramSuffixToBeReplaced, paramSuffixToReplaceWith){
			var paramValue=obj.value;
			selectedDropdownObjectName=obj.name;
			suffixToBeReplaced = paramSuffixToBeReplaced;
			suffixToReplaceWith = paramSuffixToReplaceWith;
			var url = '../citizen/ajaxDepositWorks!'+urlMethod+'.action?'+paramPassed+'='+paramValue;
			YAHOO.util.Connect.asyncRequest('POST', url, ajaxResultListBPA, null);

		}

		var ajaxResultListBPA={
				success: function(o) {
					if(o.responseText!="")
					{
						var docs=o.responseText;               
						res=docs.split("$");
						var dropdownName=selectedDropdownObjectName;
						dropdownName=dropdownName.replace(suffixToBeReplaced,suffixToReplaceWith);
						var x=document.getElementsByName(dropdownName)[0];
						x.length=0;
						x.options[0]=new Option("---Choose---","-1");  
						var j=0;         
						for(var i=0;i<res.length-1;i++)
						{
							var idandnum=res[i].split('~');
							x.options[++j]=new Option(idandnum[0],idandnum[1]);
					    }			  
						populateBoundaryDataFromBPA();                  
					}
				},                                         
				failure: function(o) {
					alert('Cannot fetch boundary list');
				}
			};

		function populateBoundaryDataFromBPA() {
			var records= jurisdictionDetailsTable.getRecordSet();
			if(wardId !=null && wardId != '' && suffixToReplaceWith == 'ward') {
				dom.get("Ward"+records.getRecord(getNumber(rowId)).getId()).value=wardId;
				dom.get("Ward"+records.getRecord(getNumber(rowId)).getId()).disabled=true;
				ajaxLoadDropdownForBPA(dom.get("Ward"+records.getRecord(getNumber(rowId)).getId()),"populateArea","wardId","ward","area");
			} 
			else if(areaId !=null && areaId != '' && suffixToReplaceWith == 'area') {
				dom.get("Area"+records.getRecord(getNumber(rowId)).getId()).value=areaId;
				dom.get("Area"+records.getRecord(getNumber(rowId)).getId()).disabled=true;
				ajaxLoadDropdownForBPA(dom.get("Area"+records.getRecord(getNumber(rowId)).getId()),"populateLocality","areaId","area","locality");
			} 
			else if(localityId !=null && localityId != '' && suffixToReplaceWith == 'locality') {
				dom.get("Locality"+records.getRecord(getNumber(rowId)).getId()).disabled=true;
				dom.get("Locality"+records.getRecord(getNumber(rowId)).getId()).value=localityId;
				ajaxLoadDropdownForBPA(dom.get("Locality"+records.getRecord(getNumber(rowId)).getId()),"populateStreets","locationId","locality","street")
			}
			else if(streetId !=null && streetId != '' && suffixToReplaceWith == 'street') {
				dom.get("Street"+records.getRecord(getNumber(rowId)).getId()).value=streetId;
				dom.get("Street"+records.getRecord(getNumber(rowId)).getId()).disabled=true;
			}
		}

		function disableBoundaryDetailsForBPARoadCut() {
			for(var i=0;i<document.forms[0].length;i++) {    
				domElement = document.forms[0].elements[i];
				if((domElement.name.indexOf("zone.id") != -1) ||(domElement.name.indexOf("ward.id") != -1) || (domElement.name.indexOf("area.id") != -1)  )	{
					domElement.disabled =true;
				}
				if((domElement.name.indexOf("locality.id") != -1) ||(domElement.name.indexOf("street.id") != -1)) {
					domElement.disabled =true;
				}
			}
		}

		function enableBoundaryDetailsForBPARoadCut() {
			for(var i=0;i<document.forms[0].length;i++) {    
				domElement = document.forms[0].elements[i];
				if((domElement.name.indexOf("zone.id") != -1) ||(domElement.name.indexOf("ward.id") != -1) || (domElement.name.indexOf("area.id") != -1)  )	{
					domElement.disabled =false;
				}
				if((domElement.name.indexOf("locality.id") != -1) ||(domElement.name.indexOf("street.id") != -1)) {
					domElement.disabled =false;
				}
			}
		}

		function defaultDepthOfCut(obj) {
			records = jurisdictionDetailsTable.getRecordSet();
		    for(i=0;i < records.getLength();i++) {
		    	recordId1=records.getRecord(i).getId();
		    	if(dom.get("DepthDropDown"+recordId1) != null) {
		    		if(obj.value=='<s:property value="@org.egov.works.models.depositWorks.PurposeOfRoadCut@SERVICE"/>') {
			     		dom.get("DepthDropDown"+recordId1).value = '<s:property value="@org.egov.works.utils.DepositWorksConstants@DEPTH_OF_CUT_TWO"/>';
			     		dom.get("DepthDropDown"+recordId1).disabled=true;
		    		}
		    		else { 
		    			dom.get("DepthDropDown"+recordId1).value = -1;
			     		dom.get("DepthDropDown"+recordId1).disabled=false;
		        	}
		    	}        	
		    }
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
	<body onload="load();enableOrdisableElements();noBack();" onpageshow="if(event.persisted) noBack();" onunload="">
	<s:if test="%{hasErrors()}">
        <div id="errorstyle" class="errorstyle" >
          <s:actionerror/>
          <s:fielderror/>
        </div>
    </s:if>
	<div class="errorstyle" id="citizenPortal_error" style="display: none;"></div>
		<s:form name="depositWorksForm" action="depositWorks" theme="simple">
		<s:hidden name="appDetailsId" id="appDetailsId" />
		<s:hidden name="mode" id="mode" />
		<s:hidden name="applicationRequest.documentNumber" id="docNumber" />
		<s:hidden name="modifyMode" id="modifyMode" value="%{modifyMode}" />
		<s:hidden name="originalDocsCount" id="originalDocsCount" value="%{originalDocsCount}" />
		<s:hidden name="docNum" id="docNum" value="%{docNum}" />
		<s:hidden name="sourcepage" id="sourcepage" />
		<s:if test="%{mode!='view'}"> 
			<s:token />
		</s:if>
		<s:hidden id="citizenId" name="citizenId" value="%{citizenId}"/>
			<s:push value="model">
				<div class="formmainbox">
				<div class="insidecontent">
				<div class="rbroundbox2">
				<div class="rbtop2"><div></div></div>
				<div class="rbcontent2">
				<div class="estimateno">Application Request No: <s:if test="%{applicationRequest.applicationNo==null || applicationRequest.applicationNo==''}">&lt; Not Assigned &gt;</s:if><s:property value="applicationRequest.applicationNo" /></div>
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						&nbsp;
					</tr>
					<tr>
						<td><div id="header">
								<ul id="Tabs">
									<li id="requestDetailsTab" class="First Active"><a id="header_1" href="#" onclick="showRequestDetailsTab();"><s:text name="depositworks.roadcut.details" /></a></li>
									<s:if test="%{mode=='UpdateRoadCutDate' || ((model.applicationRequest.workEndDate!=null || model.applicationRequest.workStartDate!=null ) && mode=='view')}">
										<li id="updateRoadCutDateTab" class="Last"><a id="header_7" href="#" onclick="showUpdateRoadCutDateTab();"><s:text name="depositworks.updateRoadCutDate.title" /></a></li>
									</s:if>
								</ul>
						</div></td>
				    </tr> 
					<tr>
				    	<td>
				    		<div id="requestDetails_div">
								<%@ include file="depositWorks-roadCutDetails.jsp"%>
							</div>	
						</td>		
				    </tr>
				     <tr>
				       	 <td>
							<div id="roadCut_div" style="display:none;"> 
								<%@ include file="depositWorks-roadCutDate.jsp"%>
							</div>
				       	</td>
				     </tr>
				</table>
				<br/><br/>				
				<s:if test="%{mode=='edit' && sourcepage=='search' && applicationRequest.depositWorksCategory==@org.egov.works.models.depositWorks.DepositWorksCategory@EMERGENCYCUT}">
				    <tr>
				    	<span class="mandatory">*</span><s:text name="dw.emergencyCut.upload.drawing" />
				    	<s:checkbox name="areDrawingsAttached" id="areDrawingsAttached" value="%{areDrawingsAttached}"  onclick="checkDrawingsAttached(this);" />
				    </tr>
				</s:if>
				<div class="buttonholdersearch" align = "center">
					<s:if test="%{mode=='edit'}">
			      		<s:submit value="UPDATE" cssClass="buttonfinal" onclick="return validateInput();" id="saveButton" method="update" name="button" />
			      	</s:if>
			      	<s:elseif test="%{mode=='UpdateRoadCutDate'}">
						<s:submit cssClass="buttonfinal" onclick='return validateInput()' value="Submit" id="submitButton" method="updateRoadCutDate" />
					</s:elseif>
			      	<s:else>
			      		<s:submit value="APPLY" cssClass="buttonfinal" onclick="return validateInput();" id="saveButton" method="save" name="button" />
			      	</s:else>	
			      	<input type="button" class="buttonfinal" value="CLOSE" id="closeButton" name="closeButton" onclick="window.close();"/>
			      	<s:if test="%{mode != 'UpdateRoadCutDate' && mode != 'view'}">
			      		<input type="submit" class="buttonadd" value="Upload Document" id="docUploadButton" onclick="showDocumentManager();return false;" />
			      	</s:if>
			      	<s:if test="%{mode=='view' && sourcepage=='search'}">
							<input type="submit" class="buttonadd" value="View Document" id="docViewButton" onclick="viewDocument();return false;" />
					</s:if>
					<s:if test="%{mode=='edit' && sourcepage=='search' && applicationRequest.depositWorksCategory==@org.egov.works.models.depositWorks.DepositWorksCategory@EMERGENCYCUT}">
  							<input type="submit" class="buttonadd" value="View Document" id="docViewButton" onclick="viewDocument();return false;" />
  							<input type="submit" class="buttonadd" value="Upload Drawings" id="emergencyCutDocUploadButton" onclick="showDocumentManager();return false;" />
					</s:if>
						
				</div>
				</div>
				</div>
				</div>
				</div>
				</s:push>
			</s:form>
	</body>
</html>
