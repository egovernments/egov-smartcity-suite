<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator"%>
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>  
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags" %>


<html>  
<head>  
    <title><s:text name='rateContract.title'/></title>  
</head>  
<body>
<script src="<egov:url path='js/works.js'/>"></script>

<script>
window.onload = function(){
	setCurrentdate();
	doInit();
	getInit();
	roundOffMaxMinAmount();
}

function roundOffMaxMinAmount()
{
	if(document.getElementById("indentAmount").value!="")
	{
	document.rateContractForm.indentAmount.value=roundTo(document.rateContractForm.indentAmount.value);
	}
}

function openViewBudget(){
var functionid  = document.rateContractForm.function.value;
var fundid  = document.rateContractForm.fund.value;
var budgetheadid  = document.rateContractForm.budgetGroup.value;
var startDate = formatDate6(document.rateContractForm.startDate.value);
var deptid = document.rateContractForm.department.value;

	if(fundid=='-1')
	{
	     dom.get("rateContract_error").style.display='';
         dom.get("rateContract_error").innerHTML='<s:text name="rateContract.fund.null" />';
	     document.rateContract_error.fund.focus();
	}
	else
	{
		if(functionid=='-1')
		{
			dom.get("rateContract_error").style.display='';
         	dom.get("rateContract_error").innerHTML='<s:text name="rateContract.function.null" />';
			document.rateContract_error.function.focus();
		}
		else
		{
			if(budgetheadid=='-1')
			{
				dom.get("rateContract_error").style.display='';
         		dom.get("rateContract_error").innerHTML='<s:text name="rateContract.budgetGroup.null" />';
				document.rateContract_error.budgetGroup.focus();
			}
			else
			{
				if(deptid=='-1')
				{
					dom.get("rateContract_error").style.display='';
         			dom.get("rateContract_error").innerHTML='<s:text name="rateContract.dept.null" />';
					document.rateContract_error.budgetGroup.focus();
				}
				else
				{
					if(startDate=="")
					{
						dom.get("rateContract_error").style.display='';
         				dom.get("rateContract_error").innerHTML='<s:text name="rateContract.validity.null" />';
						document.rateContract_error.budgetGroup.focus();
					}
					else
					{
						dom.get("rateContract_error").style.display='none';
						window.open("${pageContext.request.contextPath}/rateContract/ajaxIndentRateContract!showBudgetDetails.action?functionid="+functionid+"&fundid="+fundid+"&deptid="+deptid+"&startDate="+startDate+"&budgetheadid="+budgetheadid,'viewbudget',"width=600,height=300,resizable=0,left=250,top=250");
					}
				}
			}
		}
	}
}


function setupBudgetGroups(elem){
	var id=elem.options[elem.selectedIndex].value;
	var date=document.rateContractForm.indentDate.value;
	if(id != -1 )
	{
    	populatebudgetGroup({functionId:id,indentDate:date});
    }
    else
    {
    	document.getElementById("budgetGroup").value=-1;
    }
}

function validateRateContractFormAndSubmit(){
		clearMessage('rateContract_error');
		var err = false;
		var actionName=document.rateContractForm.actionName.value;
		if(actionName=='Reject'){
	 		if(dom.get('approverComments').value==''){
				dom.get("rateContract_error").style.display='';
	 			dom.get("rateContract_error").innerHTML='<s:text name="rateContract.remark.null" /><br>'; 
        		err = true;
	 		}
	 	}	
		if(!validateDateFormat(document.rateContractForm.indentDate))
		{
			dom.get("rateContract_error").style.display='';
			document.getElementById("rateContract_error").innerHTML+='<s:text name="rateContract.date.invalid" /><br>';
			err = true;
		}
	 	
	 	if(document.rateContractForm.indentDate != "")
	 	{
	 		if(checkdate())
	 		{
	 			err = true;
	 		}
	 	}
		
	 	if(document.getElementById("indentDate").value=="")
		{
			dom.get("rateContract_error").style.display='';
			document.getElementById("rateContract_error").innerHTML+='<s:text name="rateContract.date.invalid" /><br>';
		  	err = true;
		}
		
		if(dom.get("preparedBy").value==-1){
			dom.get("rateContract_error").style.display='';
	  	 	dom.get("rateContract_error").innerHTML='<s:text name="rateContract.preparedBy.is.null"/><br>'; 
     		err = true;
	  	}
	  	
		if(document.getElementById("department").value==-1)
		{
			dom.get("rateContract_error").style.display='';
			document.getElementById("rateContract_error").innerHTML+='<s:text name="rateContract.dept.null" /><br>';
		  	err = true;
		}
		
		if(document.getElementById("contractorGrade").value==-1)
		{
			dom.get("rateContract_error").style.display='';
		  	document.getElementById("rateContract_error").innerHTML+='<s:text name="rateContract.contractorGrade.null" /><br>';
		  	err = true;
		}
		
		if(document.getElementById("boundary").value==-1)
		{
			dom.get("rateContract_error").style.display='';
		  	document.getElementById("rateContract_error").innerHTML+='<s:text name="rateContract.zone.null" /><br>';
		  	err = true;
		}
	
		if(document.getElementById("indentType").value == -1)
		{
			dom.get("rateContract_error").style.display='';
			document.getElementById("rateContract_error").innerHTML+='<s:text name="rateContract.type.null" /><br>';
		  	err = true;
		}
			
		if(document.getElementById("indentType").value == "Amount")
		{	
			if(document.getElementById("fund").value==-1)
			{
				dom.get("rateContract_error").style.display='';
		  		document.getElementById("rateContract_error").innerHTML+='<s:text name="rateContract.fund.null" /><br>';
		  		err = true;
			}
		
			if(document.getElementById("function").value==-1)
			{
				dom.get("rateContract_error").style.display='';
		  		document.getElementById("rateContract_error").innerHTML+='<s:text name="rateContract.function.null" /><br>';
		  		err = true;
			}
		
			if(!validateNumber(document.rateContractForm.indentAmount))
			{
		  		dom.get("rateContract_error").style.display='';
		  		document.getElementById("rateContract_error").innerHTML+='<s:text name="rateContract.amount.value.invalid" /><br>';
		  		document.getElementById("rateContract_error").innerHTML+='<s:text name="rateContract.amount.value.negetive" /><br>';
		  		err = true;
		  	}
		  	
			if(document.getElementById("indentAmount").value== "" || roundTo(document.getElementById("indentAmount").value)==0.00)
			{
				dom.get("rateContract_error").style.display='';
		  		document.getElementById("rateContract_error").innerHTML+='<s:text name="rateContract.amount.null" /><br>';
		  		err = true;
			}
		
			if(document.getElementById("budgetGroup").value==-1)
			{
				dom.get("rateContract_error").style.display='';
		  		document.getElementById("rateContract_error").innerHTML+='<s:text name="rateContract.budgetGroup.null" /><br>';
		  		err = true;
			}
		}
		
		if(document.getElementById("startDate").value=="")
		{
			dom.get("rateContract_error").style.display='';
			document.getElementById("rateContract_error").innerHTML+='<s:text name="rateContract.startDate.invalid" /><br>';
		  	err = true;
		}
		
		if(document.getElementById("endDate").value=="")
		{
			dom.get("rateContract_error").style.display='';
			document.getElementById("rateContract_error").innerHTML+='<s:text name="rateContract.endDate.invalid" /><br>';
		  	err = true;
		}
		
		if(!validateDateFormat(document.rateContractForm.startDate) || !validateDateFormat(document.rateContractForm.endDate))
		{
			dom.get("rateContract_error").style.display='';
			document.getElementById("rateContract_error").innerHTML+='<s:text name="rateContract.date.invalid" /><br>';
			err = true;
		}
		
		if(formatDate6(document.rateContractForm.startDate.value)!="" && formatDate6(document.rateContractForm.endDate.value) != "")
		{
		 	if(checkDates())
		 	{
		 		err = true;
		 	}
		}
	
		if(sorDataTable.getRecordSet().getLength() == 0 && nonSorDataTable.getRecordSet().getLength() == 0)
		{
			
			dom.get("rateContract_error").style.display='';
			document.getElementById("rateContract_error").innerHTML+='<s:text name="rateContract.sor.null" /><br>';
			err = true;
		}
		
		if(nonSorDataTable.getRecordSet().getLength() > 0)
		{
			allRecords = nonSorDataTable.getRecordSet();
			for(i=0;i<allRecords.getLength();i++)
			{
				if(validateNonSORDescriptionOnSubmit(allRecords.getRecord(i)) || validateNonSorUomOnSubmit(allRecords.getRecord(i)))
				{	
					err = true;
				}
			}
		}
			
	if(!err)
	{
		mode=dom.get('mode').value;
		if(mode=='edit')
    	{
    		if(validateCancel())
    		{
    			document.rateContractForm.action='${pageContext.request.contextPath}/rateContract/indentRateContract!save.action';
    			document.rateContractForm.submit();
    		}
    	}
   	}
   	return err;
}

function loadDesignationFromMatrix(){
		var value=document.getElementById("department").value;
		if(value=="-1"){
  			alert("Please select department for Indent Rate Contract");
	 		document.getElementById("approverDepartment").value=-1;
			return false;
		}
		// var dept=document.getElementById('approverDepartment').options[document.getElementById('approverDepartment').selectedIndex].text;
		 var dept=document.getElementById('department').options[document.getElementById('department').selectedIndex].text;
  		 var currentState = dom.get('currentState').value;
  		 var amountRuleValue =  dom.get('amountRule').value;
  		 var additionalRuleValue =  dom.get('additionalRule').value;
  		 var pendingAction=document.getElementById('pendingActions').value;
  		 
  		 loadDesignationByDeptAndType('Indent',dept,currentState,amountRuleValue,additionalRuleValue,pendingAction); 
}

function populateApprover()
{
  getUsersByDesignationAndDept();
}

function checkdate(){
	indentDate = document.getElementById("indentDate").value;
	date = formatDate6("<%=new java.text.SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date())%>");
	if(compareDate(formatDate6(date),formatDate6(indentDate)) == 1 )
	{
		dom.get("rateContract_error").style.display='';
		document.getElementById("rateContract_error").innerHTML+='<s:text name="rateContract.date.validate" /><br>';
		return true;
	}
	/*This can be used to check the date is in current financial year
	var tMonth=date.substr(date.length-7,2);
	if(tMonth<4)
		var fiscalYearIndentDate = "01/04/"+(date.substr(date.length-4,4)-1);
	else
		var fiscalYearIndentDate="01/04/"+date.substr(date.length-4,4);
		if(compareDate(fiscalYearIndentDate,indentDate) == -1 )
		{ 
		   	dom.get("rateContract_error").style.display='';
			document.getElementById("rateContract_error").innerHTML+='<s:text name="rateContract.indentdate.fiscalYear.validate" /><br>';
			return true;
		}*/
}

function checkDates(){
	sdate = document.getElementById("startDate").value;
	edate = document.getElementById("endDate").value;
	if(compareDate(formatDate6(sdate),formatDate6(edate)) == -1)
	{
		dom.get("rateContract_error").style.display='';
		document.getElementById("rateContract_error").innerHTML+='<s:text name="rateContract.startDate.validate" /><br>';
		return true;
	}
			
	var tMonth=edate.substr(edate.length-7,2);
	if(tMonth<4)
		var fiscalYearStartDate="01/04/"+(edate.substr(edate.length-4,4)-1);
	else
		var fiscalYearStartDate="01/04/"+edate.substr(edate.length-4,4);
	if(compareDate(fiscalYearStartDate,sdate) == -1  && document.getElementById("indentType").value!="Item" )
	{
		dom.get("rateContract_error").style.display='';
		document.getElementById("rateContract_error").innerHTML+='<s:text name="rateContract.date.fiscalYear.validate" /><br>';
		return true;
	}
}

function setCurrentdate(){
	var indentDate=document.getElementById("indentDate").value;
	var startDate=document.getElementById("startDate").value;
	var date = '<%=new java.text.SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date())%>';
	if(indentDate=='' && startDate=='') 
	{
		document.getElementById("indentDate").value=date;
		document.getElementById("startDate").value=date;
	}
	else 
	{	
		document.getElementById("indentDate").value=indentDate;
		document.getElementById("startDate").value=startDate;
	}
}

function doInit()
{	//populateDesignation();
	mode=dom.get('mode').value;
	indentStatus=dom.get('indentStatus').value;
	sourcepage=dom.get('sourcepage').value;
	element = document.getElementById("amount");
	element.style.display = 'none';
	if(mode=='view' || mode=='edit')
	{
		if(document.rateContractForm.indentType.value == "Amount")
		{
			element.style.display = 'block';
		}
	}
	if(mode=='edit' && document.rateContractForm.indentType.value=="Item")
	{
		document.rateContractForm.indentAmount.value="";
		document.rateContractForm.fund.value="-1";
    	document.rateContractForm.function.value="-1";
    	document.rateContractForm.budgetGroup.value="-1";
	}
	if((indentStatus!='' && indentStatus!='NEW' && indentStatus != 'REJECTED' && mode !='edit') || mode=='view'){
		disableSelect();
		enableButtons();
		<s:if test="%{sourcepage=='inbox' && model.egwStatus!=null}">
			<s:if test="%{model.egwStatus.code=='CREATED' || model.egwStatus.code=='RESUBMITTED'}">
				enableButtons();
		</s:if>
		</s:if>
		<s:if test="%{(sourcepage=='inbox' && model.egwStatus!=null) && model.egwStatus.code=='CHECKED'}">
				enableButtons();
		</s:if>
		<s:else>
			if(sourcepage=='inbox'){
				enableButtons();
			}
		</s:else>
		
	}
	else{
		enableFields();
	}
//	<s:if test="%{(sourcepage=='inbox' || model.egwStatus.code =='NEW')}">
	//	showElements(['approverCommentsRow']);
	//	dom.get('approverComments').disabled=false;
	//</s:if>
//	<s:if test="%{(sourcepage=='inbox' && model.currentState!=null) && model.currentState.nextAction=='Pending for Approval'}">  
//		hideElements(['workflowDetials']);
//	</s:if>					
	<s:if test="%{sourcepage=='search' || (sourcepage=='inbox' && (egwStatus.code=='APPROVED' || egwStatus.code=='CANCELLED'))}">
		dom.get('docViewButton').disabled=false;
	</s:if>
	<s:else>
		dom.get('worksDocUploadButton').disabled=false;
	</s:else>
}

function getInit() 
{
	var id = document.getElementById("indentType").value;
	if(id == "Amount")
	{
		element = document.getElementById("amount");
		element.style.display = 'block';
	}
	else if(id == "Item")
	{
		element = document.getElementById("amount");
		element.style.display = 'none';
	}
	else
	{
		element = document.getElementById("amount");
		element.style.display = 'none';
	}
}

function validateCancel() {
	<s:if test="%{mode=='edit'}">
		var msg='<s:text name="rateContract.template.modify.confirm"/>';
	</s:if>
	<s:else>
		var msg='<s:text name="indent.cancel.confirm"/>';
	</s:else>
	var estNo='<s:property value="model.indentNumber"/>'; 
	if(!confirmCancel(msg,estNo)) {
		return false;
	}
	else {
		return true;
	}
}

function validateForm(){
	if(validateRateContractFormAndSubmit()){
		return false;
	}
	return true;
}

function enableButtons(){
		if(dom.get('approverDepartment')!=null){
			dom.get('approverDepartment').disabled=false;
		}
		if(dom.get('approverDesignation')!=null){
			dom.get('approverDesignation').disabled=false;
		}
		if(dom.get('approverPositionId')!=null){
			dom.get('approverPositionId').disabled=false;
		}
		if(dom.get('approverComments')!=null){
			dom.get('approverComments').disabled=false;
		}
		if(dom.get('Approve')!=null){
			dom.get('Approve').disabled=false;
		}
		if(dom.get('Reject')!=null){
			dom.get('Reject').disabled=false;
		}
		if(dom.get('Cancel')!=null){
			dom.get('Cancel').disabled=false;
		}
		if(dom.get('docViewButton')!=null){
			dom.get('docViewButton').disabled=false;
		}
		if(dom.get('worksDocUploadButton')!=null){
			dom.get('worksDocUploadButton').disabled=false;
		}
		if(dom.get('closeButton')!=null){
			dom.get('closeButton').disabled=false;
		}
		if(dom.get('pdfButton')!=null){
			dom.get('pdfButton').disabled=false;
		}
		if(dom.get('Forward')!=null){
			dom.get('Forward').disabled=false;
		}
}

function validate(text){
	if(document.getElementById("actionName").value=='Cancel'){
		if(!validateCancel()){
			return false;
		}
	}
	if(validateRateContractFormAndSubmit()){
		return false;
	}
	if(text!='Approve' && text!='Reject' ){
		if(!validateWorkFlowApprover(text))
		return false;
	}
	enableFields(); 
	return true;
}

function enableFields(){
   	for(i=0;i<document.rateContractForm.elements.length;i++){
    document.rateContractForm.elements[i].disabled=false;
  }
}
function disableSelect(){
   	for(i=0;i<document.rateContractForm.elements.length;i++){
    document.rateContractForm.elements[i].disabled=true;
	}
	nonSorDataTable.removeListener('cellClickEvent');
	sorDataTable.removeListener('cellClickEvent');
	document.getElementById("addnonSorRowBttn").style.display="none";
}
</script>
<div class="errorstyle" id="rateContract_error" style="display:none;"></div>
    <s:if test="%{hasErrors()}">
        <div id="errorstyle" class="errorstyle" >
          <s:actionerror/>
          <s:fielderror/>
        </div>
    </s:if>
    <s:if test="%{hasActionMessages()}">
        <div class="messagestyle">
        	 <s:actionmessage theme="simple"/>
        </div>
    </s:if>
<s:form theme="simple" name="rateContractForm" >
<s:token/> 
<s:push value="model">
<s:if test="%{model.id!=null}">
	<s:hidden name="id" value="%{id}" id="id"/>
    <s:hidden name="mode" value="%{mode}" id="mode"/>
    <s:hidden name="indentStatus" value="%{model.egwStatus.code}" id="indentStatus"/>
    <s:hidden name="sourcepage" value="%{sourcepage}" id="sourcepage"/>
    
</s:if> 
<s:else>
    <s:hidden name="id" value="%{null}" id="mode" />
    <s:hidden name="indentStatus" value="%{null}" id="indentStatus"/>
    <s:hidden name="sourcepage" value="%{null}" id="sourcepage"/>
</s:else>
	<s:hidden name="documentNumber" id="docNumber" />
<div class="formmainbox">
	<div class="insidecontent">
  		<div class="rbroundbox2">
		<div class="rbtop2"><div></div></div>  
     	<div class="rbcontent2">  
     	
     	<div class="datewk">
 		<div class="estimateno">Indent Rate Contract No: <s:if test="%{not model.indentNumber}">&lt; Not Assigned &gt;</s:if><s:property value="model.indentNumber" /></div>
	  </div>
	  	<table width="100%" border="0" cellspacing="0" cellpadding="0">
	  	<tr>
				<td colspan="6">&nbsp;</td>
		</tr>
		
          	<tr>
            	<td>
					<%@ include file='itemRateContractIndent.jsp'%>
				</td>
		  	</tr>	

		  	<tr>
		  		<td>&nbsp;</td>
		  	</tr>
		  
		  	<tr>
          	  	<td>
            		<%@ include file='itemRateContract-sor.jsp'%>
			  	</td>
		  	</tr>	
		  	<tr>
		  	  	<td>&nbsp;</td>
		  	</tr>
		  
		  	<tr>
            	<td>
            		<%@ include file='itemRateContract-nonSor.jsp'%>
				</td>
		  	</tr>
		  			 <tr><td>&nbsp;</td></tr>
	 	 <s:if test="%{mode != 'view'}" >
	 	 <tr> 
		    <td>
		    	<div id="manual_workflow">
         			<c:set var="approverHeadCSS" value="headingwk" scope="request" />
         			<c:set var="approverCSS" value="bluebox" scope="request" />
					<%@ include file="/commons/commonWorkflow.jsp"%>
  				</div>
 		    </td>
            </tr>
          </s:if>  	
			<s:hidden name="scriptName" id="scriptName" value="Indent"></s:hidden>
		<tr>
			<td>
				<div id="mandatary" align="right" class="mandatory" style="font-size: 11px; padding-right: 20px;">*
					<s:text name="message.mandatory" />
				</div>
			</td>
		</tr>	
		</table>
		 <div class="rbbot2"><div></div></div>
		</div>
		</div>
	</div>
</div>
<div class="buttonholderwk">	
	  <p>
   <input type="hidden" name="actionName" id="actionName"/>
   			<s:if test="%{mode!='edit'}"> 
					<s:if test="%{(sourcepage=='inbox' || model.egwStatus==null || model.egwStatus.code=='NEW'  
						|| model.egwStatus.code=='REJECTED') && mode !='view' || hasErrors() || hasActionMessages()}">
 						<s:if test="%{model.id==null || model.egwStatus.code=='NEW'}">
 							<s:submit type="submit" cssClass="buttonfinal"
								value="Save" id="Save" name="Save"
								method="save"
								onclick="document.rateContractForm.actionName.value='Save';return validateForm();" />
 						</s:if>
 						<s:iterator value="%{getValidActions()}" var="name">
							<s:if test="%{name!=''}">
									<s:submit type="submit" cssClass="buttonfinal"
										value="%{name}" id="%{name}" name="%{name}"
										method="save"
										onclick="document.rateContractForm.actionName.value='%{name}';return validate('%{name}');" />
							</s:if>
						</s:iterator>  
					</s:if>
	  		</s:if>
	   
		<s:if test="%{model.id!=null && (mode=='view' || mode=='edit' || sourcepage=='inbox' || model.egwStatus!=null)}">
			<input type="button" onclick="window.open('${pageContext.request.contextPath}/rateContract/indentRateContract!viewIndentRateContractPDF.action?id=<s:property value='%{model.id}'/>');" class="buttonpdf" value="VIEW PDF" id="pdfButton" name="pdfButton" />
		</s:if>
		<s:if test="%{model.id==null}" >
			<input type="button" class="buttonfinal" value="Clear" id="button" name="clear" onclick="window.open('${pageContext.request.contextPath}/rateContract/indentRateContract!newform.action','_self');">&nbsp;
		</s:if>
			<input type="button" class="buttonfinal" value="Close" id="closeButton" name="closeButton" onclick="window.close();" />
		<s:if test="%{sourcepage=='search' || (sourcepage=='inbox' && (egwStatus.code=='APPROVED' || egwStatus.code=='CANCELLED'))}">
  			<input type="submit" class="buttonadd" value="View Document" id="docViewButton" onclick="viewDocumentManager(dom.get('docNumber').value);return false;" />
  		</s:if>
  		<s:else>
			<input type="submit" class="buttonadd" value="Upload Document" id="worksDocUploadButton" onclick="showDocumentManager();return false;" />
  		</s:else>
	  </p>	
</div>
</s:push>
</s:form>
</body>
</html>    