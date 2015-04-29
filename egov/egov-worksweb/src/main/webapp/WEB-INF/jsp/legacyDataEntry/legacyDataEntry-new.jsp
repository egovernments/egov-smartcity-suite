<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags" %>
<html>
<head><title><s:text name="lde.title" /></title></head>	
<body>
<script src="<egov:url path='js/works.js'/>"></script>
<script src="<egov:url path='js/helper.js'/>"></script>	
<script >

var wpLastOfflineLastStatusDate = "";
var wpLastOfflineLastStatus = "";

var tnLastOfflineLastStatusDate = "";
var tnLastOfflineLastStatus = "";

function setCSSClasses(id,classes){
    document.getElementById(id).setAttribute('class',classes);
    document.getElementById(id).setAttribute('className',classes);
}

function showWPDetailsTab(){
	  document.getElementById('wpDetails_div').style.display='';
	  setCSSClasses('wpDetailsTab','First Active');
	  setCSSClasses('tnDetailsTab','');
	  setCSSClasses('woDetailsTab','Last');
	  hideWODetailsTab();
	  hideTNDetailsTab();
}

function showTNDetailsTab(){
	if(!validateWPBeforeSubmit())
		return false;	
	populateDataToTNTab();
	document.getElementById('tnDetails_div').style.display='';
	document.getElementById('wpDetails_div').style.display='none';
	hideWPDetailsTab();
 	setCSSClasses('wpDetailsTab','First BeforeActive');
    setCSSClasses('tnDetailsTab','Active');
	hideWODetailsTab();
   	setCSSClasses('woDetailsTab','Last');
}

function populateDataToTNTab() {
	 document.forms[0].wpAmount.value = document.forms[0].totalAmount.value;
	 var tbl = dom.get('wpOfflineStatusTable');
     var lastRow = tbl.rows.length;
     if(lastRow>3) {
        for(var i=2;i<lastRow;i++) {   
        	 var wpOfflineStatusName = getControlInBranch(tbl.rows[i],'wpOfflineStatusName').value;
        	 var wpOfflineStatusDate = getControlInBranch(tbl.rows[i],'wpOfflineStatusDate').value;
        	 if(wpOfflineStatusName != '' && wpOfflineStatusDate != '' && wpOfflineStatusName == '<s:property value="%{tenderDateStatus}" />')
        		 document.forms[0].tenderDate.value = wpOfflineStatusDate;
        	 if(wpOfflineStatusName != '' && wpOfflineStatusDate != '' && wpOfflineStatusName == '<s:property value="%{wpLastOfflineStatus}" />') {	 
        		 wpLastOfflineLastStatusDate = wpOfflineStatusDate;
        		 wpLastOfflineLastStatus = wpOfflineStatusName;
        	 }
        }
     }  
}

function showWODetailsTab(){
	if(!validateWPBeforeSubmit())
		return false;	
	if(!validateTNBeforeSubmit())
		return false;	
	populateDataToWOTab();
	$('woDetails_div').show();
	document.getElementById('wpDetails_div').style.display='none'; 
	hideWPDetailsTab();
	hideTNDetailsTab();
 	setCSSClasses('wpDetailsTab','First');
 	setCSSClasses('tnDetailsTab','BeforeActive');
 	setCSSClasses('woDetailsTab','Last Active ActiveLast');
}

function populateDataToWOTab() {
	document.getElementById('contractorName').value = document.forms[0].contractorSearch.value;

	if(document.legacyDataEntryForm.tenderType.value==dom.get("tenderType")[1].value) {
		document.getElementById('wpAmountRow').style.display=''; 
		if(document.forms[0].estimateAmount.value == '' || document.forms[0].estimateAmount.value == 0.0) 
			document.forms[0].estimateAmount.value = roundTo(document.forms[0].wpAmount.value);
		if(document.forms[0].tenderpercentage.value == '' || document.forms[0].tenderpercentage.value == 0.0) 
			document.forms[0].tenderpercentage.value = roundTo(document.forms[0].percNegotiatedAmountRate.value);

	}
	
	if(document.forms[0].workOrderAmount.value == '' || document.forms[0].workOrderAmount.value == 0.0) 
		document.forms[0].workOrderAmount.value = roundTo(document.forms[0].percNegotiatedAmount.value);
	
	if(document.forms[0].securityDeposit.value == '' || document.forms[0].securityDeposit.value ==0.0)
		document.forms[0].securityDeposit.value = roundTo(('<s:property value="%{securityDepositConfValue}" />'/100)*document.getElementById('workOrderAmount').value)
	if(document.forms[0].labourWelfareFund.value == '' || document.forms[0].labourWelfareFund.value ==0.0) 
		document.forms[0].labourWelfareFund.value = roundTo(('<s:property value="%{labourWelfareFundConfValue}" />'/100)*document.getElementById('workOrderAmount').value)
	
	 var tbl = dom.get('tnOfflineStatusTable');
    var lastRow = tbl.rows.length;
    if(lastRow>3) {
       for(var i=2;i<lastRow;i++) {   
       	 var tnOfflineStatusName = getControlInBranch(tbl.rows[i],'tnOfflineStatusName').value;
       	 var tnOfflineStatusDate = getControlInBranch(tbl.rows[i],'tnOfflineStatusDate').value;
       	 if(tnOfflineStatusName != '' && tnOfflineStatusDate != '' && tnOfflineStatusName == '<s:property value="%{tnLastOfflineStatus}" />') {	 
       		 tnLastOfflineLastStatusDate = tnOfflineStatusDate;
       		 tnLastOfflineLastStatus = tnOfflineStatusName;
       	 }
       }
    }  
}

function hideWPDetailsTab(){
	  document.getElementById('wpDetails_div').style.display='none';
}
function hideTNDetailsTab(){
	  document.getElementById('tnDetails_div').style.display='none'; 
}
function hideWODetailsTab(){
	  document.getElementById('woDetails_div').style.display='none';
}

function validateInput() {

	//temporary validation message till we implement save with all 3 tabs. Needs to be removed when we do specific validation and integration validation for all 3 tabs
	if(!validateWPBeforeSubmit())
		return false;
	if(!validateTNBeforeSubmit(legacyDataEntryForm))
		return false;
	showMessage('legacyDataEntry_error','<s:text name="lde.validate.mandatory" />');
	return false;
	
	// Enable Fields				
	for(var i=0;i<document.forms[0].length;i++) {
   		document.forms[0].elements[i].disabled =false; 
   	}

    //return true;
}		


function enableSelect(){
   	for(i=0;i<document.legacyDataEntryForm.elements.length;i++){
    document.legacyDataEntryForm.elements[i].disabled=false;
	}
}

function disableSelect(){
   	for(i=0;i<document.legacyDataEntryForm.elements.length;i++){
    document.legacyDataEntryForm.elements[i].disabled=false;
	}
}

function disableLinks() {
	var links=document.legacyDataEntryForm.getElementsByTagName("a");
	for(i=0;i<links.length;i++){
  			links[i].onclick=function(){return false;};
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
<div class="errorstyle" id="legacyDataEntry_error" style="display: none;"></div>
<s:form action="legacyDataEntry" name="legacyDataEntryForm" onSubmit="return validateInput()" theme="simple">
	<s:if test="%{mode!='view'}"> 
		<s:token />
	</s:if>
	 
	<s:hidden name="mode" id="mode" />
	<s:hidden name="sourcepage" id="sourcepage" />	
	<div class="formmainbox">
	<div class="insidecontent">
	<div class="rbroundbox2">
	<div class="rbtop2"><div></div></div>
	<div class="rbcontent2">
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				&nbsp;
			</tr>
			<tr>
				<td><div id="header">
						<ul id="Tabs">
							<li id="wpDetailsTab" class="First Active"><a id="header_1" href="#" onclick="showWPDetailsTab();"><s:text name="lde.wp.details" /></a></li>
							<li id="tnDetailsTab" class="Befor"><a id="header_2" href="#" onclick="showTNDetailsTab();"><s:text name="lde.tn.details" /></a></li>
							<li id="woDetailsTab" class="Last"><a id="header_3" href="#" onclick="showWODetailsTab();"><s:text name="lde.wo.details" /></a></li>  
						</ul>
					</div>
				</td>
		    </tr>
			<tr> 
		    	<td>
		    		<div id="wpDetails_div">
						<%@ include file="legacyDataEntry-worksPackage.jsp"%>
					</div>	
				</td>		
		    </tr>
		     <tr>
		       	 <td>
					<div id="tnDetails_div" style="display:none;">
					<%@ include file="legacyDataEntry-negotiationDetails.jsp"%>						
					</div>
		       	</td>
		     </tr>
		      <tr>
		       	 <td>
					<div id="woDetails_div" style="display:none;">
					<%@ include file="legacyDataEntry-workOrder.jsp"%>	
					</div>
		       	</td>
		     </tr>		   
		     
		     <tr>
				<td class="shadowwk" colspan="4"> </td>
			 </tr>
		    		    
		     <tr>
	            <td><div align="right" class="mandatory" style="font-size:11px;padding-right:20px;">* <s:text name="message.mandatory" /></div></td>
	          </tr>
		</table>
		</div>
	<div class="rbbot2"><div></div></div>
	</div>
	</div> 
     <div class="buttonholderwk" id="buttons">
     <s:if test="%{mode!='edit'}">
			<s:submit cssClass="buttonfinal" onclick='return validateInput()' value="Save" id="submitButton" method="save" />
		</s:if>
     	<input type="button" class="buttonfinal" value="CLEAR" id="clearButton" name="clearButton" onclick="window.open('${pageContext.request.contextPath}/legacyDataEntry/legacyDataEntry!newform.action','_self');"/>
     	<input type="button" class="buttonfinal" value="CLOSE" id="closeButton" name="closeButton" onclick="window.close();"/>     
     </div>
		
	</div>
	</s:form>
</body>
</html>