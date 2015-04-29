<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>
<script src="<egov:url path='js/works.js'/>"></script>
<script>
function toggleShowBPADetails(obj) {
    var individualRadioStatus = document.getElementById("individualOrOrgRadiotrue").checked;
    document.getElementById("mirrorApplicantOrgznServDeptTR").style.display='none';

	var orgId;
    if(!individualRadioStatus){
	    <s:if test="%{applicationRequest.applicant.organization!=null}" >
	    	agencyId = '<s:property value = "%{applicationRequest.applicant.organization.id}" />';
		</s:if>
		<s:else>
			agencyId = document.getElementById("organizationTypes").value;
		</s:else>
    }
    if(obj.value!='BPA' && !individualRadioStatus){
		jurisdictionDetailsTable.showColumn("Depth");
		jurisdictionDetailsTable.showColumn("Remarks");
		jurisdictionDetailsTable.hideColumn("DepthDropDown");
	   	jurisdictionDetailsTable.hideColumn("RemarksEmergency");
		document.getElementById("purposeOfCutLabelTD").style.visibility='hidden';
		document.getElementById("purposeOfCutFieldTD").style.visibility='hidden';
		if(document.getElementById("isSchemeBasedRadiotrue")!=null) {
			document.getElementById("isSchemeBasedRadiotrue").disabled=false;
		}
    }
    if(obj.value == 'BPA') {
            
        if(individualRadioStatus)
        {
            
        	dom.get("roadCutDetails_div").style.display='none';
			dom.get("newBPADetails_div").style.display='block';
			dom.get("bpaButtons_div").style.display='block';
			dom.get("normalbuttons_div").style.display='none';
		
        }        
        else
        {
        	jurisdictionDetailsTable.showColumn("BPANumber"); 
            document.getElementById("serviceDeptTR").style.display='';
            document.getElementById("newOrgznServiceDeptTR").style.display='none';
            document.getElementById("mirrorApplicantOrgznServDeptTR").style.display='none';

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
    		<s:if test="%{(applicationRequest.id==null)}">
    			document.getElementById("typeOfCut").disabled=false;
    			dwCategory = 'BPA';
    			prepareTypeOfCutDropDown(agencyId,dwCategory);
    		</s:if>
    	}
    }
    else if(obj.value == 'EMERGENCYCUT') {
    	dom.get("roadCutDetails_div").style.display='block';
		dom.get("newBPADetails_div").style.display='none';
    	jurisdictionDetailsTable.hideColumn("BPANumber");
    	jurisdictionDetailsTable.hideColumn("Depth");
    	jurisdictionDetailsTable.hideColumn("Remarks");
    	jurisdictionDetailsTable.showColumn("DepthDropDown");
    	jurisdictionDetailsTable.showColumn("RemarksEmergency");   
    	document.getElementById("purposeOfCutLabelTD").style.visibility='visible';
		document.getElementById("purposeOfCutFieldTD").style.visibility='visible';
    	document.getElementById('purposeOfRoadCut').disabled = false;	
		dom.get("bpaButtons_div").style.display='none';
		dom.get("normalbuttons_div").style.display='block';
		
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
            <s:if test="%{(applicationRequest.id==null)}">
	    		dwCategory = '';    
	            prepareTypeOfCutDropDown(agencyId,dwCategory);
	        </s:if>
        }
        
    }
    else {
    	dom.get("roadCutDetails_div").style.display='block';
		dom.get("newBPADetails_div").style.display='none';
		dom.get("bpaButtons_div").style.display='none';
		dom.get("normalbuttons_div").style.display='block';
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
            <s:if test="%{(applicationRequest.id==null)}">
	          	dwCategory = '';
	            prepareTypeOfCutDropDown(agencyId,dwCategory);
	        </s:if>
        }
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
  	dom.get("roadcut_error").style.display='';
	document.getElementById("roadcut_error").innerHTML='<s:text name="dw.bpa.typeOfCut.ajax.failure" />';
}
</script>

<table width="100%" cellspacing="0" cellpadding="0" border="0" >
    <tr>
        <td colspan="6" class="headingwk">
            <div class="arrowiconwk">
                <img src="${pageContext.request.contextPath}/image/arrow.gif" />
            </div>
            <div class="headplacer"><s:text name="depositworks.roadcut.app.request.details" /> </div>
        </td>
    </tr>
    <tr>
        <td class="greyboxwk">&nbsp;</td>
        <td class="greybox2wk"><s:radio name="applicationRequest.depositWorksCategory" id="depositWorksCategory" list="%{depositWorksCategoryMap}" onchange="toggleShowBPADetails(this)" /></td>
        <td class="greyboxwk"></td>
        <td class="greybox2wk"></td>
        <td class="greyboxwk"></td>
        <td class="greybox2wk"></td>
    </tr>
</table>