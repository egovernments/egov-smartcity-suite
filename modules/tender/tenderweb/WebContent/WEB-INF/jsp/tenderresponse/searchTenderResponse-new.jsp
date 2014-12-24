<%@ taglib uri="/WEB-INF/struts-tags.tld" prefix="s" %>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ include file="/includes/taglibs.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Search BID Response</title>
	<script>
	var isError=false;
	var responseId="";
	function resetvalues()
	{
			for ( var i = 0; i < document.forms[0].length; i++) {
				if (document.forms[0].elements[i].id != 'close' && document.forms[0].elements[i].id != 'reset'
				         && document.forms[0].elements[i].id != 'search')
					document.forms[0].elements[i].value = "";
			 }
			
			 if(dom.get("fieldError")!= null)
	   	    	dom.get("fieldError").style.display= 'none';
	   	     if(dom.get("searchTenderfile_error")!= null)
	   	    	dom.get("searchTenderfile_error").style.display= 'none';
	   	     if(dom.get("tableData")!= null)	
	   	    	dom.get("tableData").style.display= 'none';
	   	    	
			return false;
	}
	
	function validateForm()
	{
	   if(dom.get("fieldError")!=null)
	   	    dom.get("fieldError").style.display='none';
	   if(!checkStringMandatoryField('departmentId','Department','searchTenderfile_error'))
	    	return false;
	   if(!checkStringMandatoryField('fileType','Tender Notice Type','searchTenderfile_error'))
	    	return false;
	   if(document.getElementById('fromDate').value!="" && document.getElementById('toDate').value!=""
	            && compareDate(document.getElementById('fromDate').value,
				document.getElementById('toDate').value) < 0)
	   {
			dom.get("searchTenderfile_error").style.display = '';
			document.getElementById("searchTenderfile_error").innerHTML = '<s:text name="fromdate.todate.validate" />';
			document.getElementById('toDate').value = "";
			return false;
	    }
	   /* if(!checkStringMandatoryField('fileNumber','Tender File Number','searchTenderfile_error'))
	    	return false;
	    if(!checkStringMandatoryField('noticeNumber','Tender Notice Number','searchTenderfile_error'))
	    	return false; */
	     return true;
	       
	}
	
	function openTenderResponse(id,unitId)
	{
	   document.searchTenderForm.action="${pageContext.request.contextPath}/tenderresponse/tenderResponse!view.action?idTemp="+id;
	   document.searchTenderForm.submit();
	   return false
	}
	
	function populateGroupNumber(filetype)
	{
	   	populategroupNumber({fileType:filetype.value});
	   //	populateresponseNumber({fileType:filetype.value})
	}
	
	function populateResponses(elem)
	{
	    populateresponseNumber({groupNumber:elem.value})
	}
	
	function checkGroupNumber()
	{
	     if(!checkStringMandatoryField('groupNumber','Reference Number','searchTenderfile_error'))
	       return false;  
	}
	
	function checkTenderNoticeType()
	{
	     if(!checkStringMandatoryField('fileType','Tender Notice Type','searchTenderfile_error')){
	         return false; 
         } 
	}
	
	function setResponseId(obj,noticeId)
    {
          if(obj.checked)
            responseId=noticeId;
         
    }
    
    function captureNegotiationDetails()
	{
	  	if(responseId=="")
		{
			dom.get("searchTenderfile_error").style.display = '';
			document.getElementById("searchTenderfile_error").innerHTML = '<s:text name="tenderResponse.negotiation.validate" />';
			return false;
		}
		else
		{
		    document.searchTenderForm.action="${pageContext.request.contextPath}/tenderresponse/tenderNegotiation!load.action?responseId="+responseId;
		 	document.searchTenderForm.submit();
		 	return true;
		}
		
	}
	
	function callOnChange(obj)
	{
	   var currRow=getRow(obj); 
	   var objectFirstParam;
	   objectFirstParam = getControlInBranch(currRow,'idresponse');
	   if(obj.value=="ViewTenderResponse"){
	        //showSubmit();
	   	 	document.searchTenderForm.action="${pageContext.request.contextPath}/tenderresponse/tenderResponse!view.action?idTemp="+objectFirstParam.value;
	     	document.searchTenderForm.submit();
	   }
	   else if(obj.value=="CaptureNegotiation")
	   {
	   	  // showSubmit();
	       document.searchTenderForm.action="${pageContext.request.contextPath}/tenderresponse/tenderNegotiation!load.action?responseId="+objectFirstParam.value;
		   document.searchTenderForm.submit();
		   return true;
	   }
	   else if(obj.value=="GenerateNegotiationNotice")
	   {
	   	  // showSubmit();
	       document.searchTenderForm.action="${pageContext.request.contextPath}/tenderresponse/tenderNegotiation!print.action?responseId="+objectFirstParam.value;
		   document.searchTenderForm.submit();
		   return true;
	   }
	   else if(obj.value=="captureJustification")
	   {
	   	  // showSubmit();
	       document.searchTenderForm.action="${pageContext.request.contextPath}/tenderjustification/tenderJustification!newform.action?responseId="+objectFirstParam.value;
		   document.searchTenderForm.submit();
		   return true;
	   }
	   else if(obj.value=="GenerateJustificationNotice")
	   {
	   	  // showSubmit();
	       document.searchTenderForm.action="${pageContext.request.contextPath}/tenderjustification/tenderJustification!print.action?responseId="+objectFirstParam.value;
		   document.searchTenderForm.submit();
		   return true;
	   }
	   
	   else{
			dom.get("searchTenderfile_error").style.display = '';
			document.getElementById("searchTenderfile_error").innerHTML = 'There is no permission to do this action';
			return false;
		}	
	   return false
	} 
    
    function showSubmit()
    {
         document.getElementById('submitting').style.display = 'block';
    }
    
    function setbidresponseId(elem)
    {	
    	dom.get("bidResponseId").value = elem;
		checkWorkOrderOrRC(elem);
	}
	 function setbidResponseNumber(elem)
    {	
    	dom.get("bidResponseNumber").value = elem;
	}
	
	
    function checkWorkOrderOrRC(bidResponseId){
     makeJSONCall(["type","number"],'${pageContext.request.contextPath}/tenderresponse/ajaxTenderResponse!getApprovedEntity.action',{idTemp:bidResponseId,fileType:document.getElementById("fileType").value},bidresponseLoadHandler,bidresponseFailureHandler) ;
}

bidresponseLoadHandler = function(req,res){
  var id = document.searchTenderForm.bidResponseId.value;
  if(res.results[0].type=='') {
  	dom.get("searchTenderfile_error").style.display='none';
  	document.getElementById("searchTenderfile_error").innerHTML='';
	isError=false;
  }
  else {
	  if(res.results[0].type!='' && res.results[0].type=='WorkOrder') {
	  document.getElementById("searchTenderfile_error").style.display='';
	  document.getElementById("searchTenderfile_error").innerHTML='<s:text name="cancelBidResponse.workorder.created.message"/>'+res.results[0].number;
	  isError=true;
	  window.scroll(0,0);
	  }
	  if(res.results[0].type!='' && res.results[0].type=='RateContract') {
	   document.getElementById("searchTenderfile_error").style.display='';
	   document.getElementById("searchTenderfile_error").innerHTML='<s:text name="cancelBidResponse.ratecontract.created.message"/>'+res.results[0].number;
	   isError=true;
	   window.scroll(0,0);
	  }
  } 
}
  	
bidresponseFailureHandler= function(){
	document.getElementById("searchTenderfile_error").style.display='';
	document.getElementById("searchTenderfile_error").innerHTML='Unable to get Work Orders for Bid Response';
}

function cancelBidResponses(){
	if(dom.get('idresponse').value=='') {
		clearMessage('searchTenderfile_error');
		dom.get("searchTenderfile_error").style.display='';
		document.getElementById("searchTenderfile_error").innerHTML='<s:text name="bidresponse.cancel.not.selected" />';
		isError=true
		window.scroll(0,0);
		
	  }
	  if(!isError && validateCancel()) {
	  	document.searchTenderForm.action="${pageContext.request.contextPath}/tenderresponse/tenderResponse!cancelApprovedBidresponse.action?responseId="+dom.get('idresponse').value+"&cancelRemarks="+dom.get("cancelRemarks").value;
		document.searchTenderForm.submit();
    }
}	

function validateCancel() {
	var msg='<s:text name="bidResponse.cancel.confirm"/>';
	var bidNo=dom.get("bidResponseNumber").value;
	if(!confirm(msg+": "+bidNo+" ?")) {
		return false;
	}
	else {
		return true;
	}
}

	</script>

</head>
<body>
<div name="errorstyle" class="errorstyle" id="searchTenderfile_error" style="display:none;">
</div>
<s:if test="%{hasErrors()}">
		<div class="errorstyle" id="fieldError">
			<s:actionerror />
		</div>
</s:if>
<s:form action="searchTenderResponse"  name="searchTenderForm" theme="simple" onKeyPress="return disableEnterKey(event);" onsubmit="showWaiting();" >
	<div class="formheading"></div>
		<s:hidden id="sourcepage" name="sourcepage" />
		<s:hidden id="bidResponseId" name="bidResponseId" />
		<s:hidden id="bidResponseNumber"/>
		<div class="formmainbox"></div>	
		<s:hidden id="linkSource" name="linkSource" value="%{linkSource}" />	
		<table width="100%" border="0" cellspacing="0" cellpadding="2">

			<tr>
				<td class="bluebox" width="18%">&nbsp;</td>
				<td class="bluebox" width="12%"><s:text name="fromDatelbl" /></td>
			    <td class="bluebox" width="26%">
			    	<s:date name="fromDate" format="dd/MM/yyyy" var="fromDateTemp" />
			    	<s:textfield name="fromDate" id="fromDate" value="%{fromDateTemp}" onkeyup="DateFormat(this,this.value,event,false,'3')" onblur="validateDateFormat(this);" />
			    	<a href="javascript:show_calendar('forms[0].fromDate');" >
							<img src="${pageContext.request.contextPath}/common/image/calendaricon.gif"	border="0" align="absmiddle"/></a>
				</td>
				<td class="bluebox" width="12%"><s:text name="todatelbl" /></td>
				<td class="bluebox" width="26%">
					<s:date name="toDate" format="dd/MM/yyyy" var="toDateTemp" />
					<s:textfield name="toDate" id="toDate" value="%{toDateTemp}" onkeyup="DateFormat(this,this.value,event,false,'3')" onblur="validateDateFormat(this);" />
			    	<a href="javascript:show_calendar('forms[0].toDate');" >
							<img src="${pageContext.request.contextPath}/common/image/calendaricon.gif"	border="0" align="absmiddle"/>
					</a>
				</td>
				<td class="bluebox" width="22%">&nbsp;</td>
			</tr>
			<tr>
				<td class="greybox" >&nbsp;</td>
				<td class="greybox" ><s:text name="departmentlbl" /><span class="mandatory">*</span></td>
			    <td class="greybox" >
			    	<s:select name="departmentId" id="departmentId"  list="dropdownData.departmentIdList" listKey="id" listValue="deptName" value="%{departmentId}" headerKey="" headerValue="----------------choose----------------" />
			    </td>
				<td class="greybox" ><s:text name="tendernoticetype" /><span class="mandatory">*</span></td>
				<td class="greybox" >
					<s:select name="fileType" id="fileType" list="dropdownData.tenderFileTypeList" listKey="id" listValue="description" value="%{fileType}" headerKey="" headerValue="-------------choose---------------" />
					<!--<egov:ajaxdropdown id="groupNumber" fields="['Text','Value']" dropdownId='groupNumber' url='common/ajaxCommon!populateGroups.action'/>-->
				</td>
				<td class="greybox" >&nbsp;</td>
			</tr>
			
			<tr>
				<td class="bluebox" >&nbsp;</td>
				<td class="bluebox" ><s:text name="tenderfileno" /></td>
			    <td class="bluebox" ><s:textfield name="fileNumber" id="fileNumber" /></td>
				<td class="bluebox" ><s:text name="tendernoticeno" /></td>
				<td class="bluebox" ><s:textfield name="noticeNumber" id="noticeNumber" /></td>
				<td class="bluebox" >&nbsp;</td>
			</tr>
			
			<tr>
				<td class="greybox" >&nbsp;</td>
				<td class="greybox" ><s:text name="groupno" /></td>
			    <td class="greybox" >
			        <s:textfield id="groupNumber" name="groupNumber"  />
			    	<!--<s:select name="groupNumber" id="groupNumber" list="dropdownData.groupNumberList" headerKey="" headerValue="-------------Choose-------------" listKey="number" listValue="number" onchange="populateResponses(this);" />-->
			    </td>
				<td class="greybox" ><s:text name="responseNo" /></td>
				<td class="greybox" >
				     <s:textfield id="responseNumber" name="responseNumber" />
					<!--<s:select name="responseNumber" id="responseNumber" list="dropdownData.responseNumberList" listKey="number" listValue="number" headerKey="" headerValue="-------------Choose-------------" />
					<egov:ajaxdropdown id="responseNumber" fields="['Text','Value']" dropdownId='responseNumber' url='common/ajaxCommon!populateResponse.action'/>-->
				</td>
				<td class="greybox" >&nbsp;</td>
			</tr>
			
			<tr>
				<td class="bluebox" >&nbsp;</td>
				<td class="bluebox" ><s:text name="user" /></td>
			    <td class="bluebox" >
			    	<s:select name="userId" id="userId" list="dropdownData.userList" listKey="id" listValue="userName" headerKey="" headerValue="----------Choose--------"/></td>
				<td class="bluebox" >&nbsp;</td>
				<td class="bluebox" >&nbsp;</td>
				<td class="bluebox" >&nbsp;</td>
			</tr>
		</table>
		
		<div id="actionbuttons" align="center" class="buttonbottom"> 
			<s:submit type="submit" cssClass="buttonsubmit" value="Submit" id="search" name="search"  method="searchTenderResponse" onclick=" return validateForm();" />	
			<input type="button" name="reset" id="reset" class="button" value="Reset" onclick= "resetvalues();" />
			<input type="button" name="close" id="close" class="button" value="Close" onclick=" window.close();" />
		</div>
		<div id="loading"  style="display:none;color:red;font:bold" align="center">
				<span>Processing , Please wait...</span>
		</div>
		<br>
		<s:if test="%{mode=='new'}">
		<div id="tableData">
			<s:if test="%{paginatedList!=null && paginatedList.getFullListSize()!=0}">
		         <div id="displaytbl">	
          		     <display:table  name="paginatedList" export="false" requestURI="" id="tNoticeId"  class="its" uid="currentRowObject"  >
          			   <div STYLE="display: table-header-group" align="center">
          			   <s:if test="%{sourcepage=='cancelBidResponse'}">
						<display:column headerClass="pagetableth" class="pagetabletd" title="Select" titleKey="column.title.select" style="width:4%;text-align:center">						
								<input name="radio" type="radio" id="radio"
								value="<s:property value='%{#attr.currentRowObject.id}'/>"
								onClick='setbidresponseId("<s:property value='%{#attr.currentRowObject.response.id}'/>");setbidResponseNumber("<s:property value='%{#attr.currentRowObject.response.number}'/>");' />
						</display:column>
						</s:if>
					   		<display:column  title=" Srl No "  style="width:7%;text-align:center;" >
			 						<s:property value="%{#attr.currentRowObject_rowNum}"/>		 						          
							</display:column>
          			 	   	<display:column title="Date" style="text-align:center;" property="response.responseDate" format="{0,date,dd-MM-yyyy}"/>	
          			    	<display:column title="Tender Notice Number" style="text-align:center;"  property="response.tenderUnit.tenderNotice.number" />
          			    	<display:column title="Department" style="text-align:center;" property="response.tenderUnit.tenderNotice.department.deptName"/>
          			    	<display:column title="Bid Response Number" style="text-align:center;" >
          			    	<s:hidden id="idresponse" name="idresponse" value="%{#attr.currentRowObject.response.id}" />
          			    		<a href="#" onclick="openTenderResponse('<s:property value="%{#attr.currentRowObject.response.id}"/>','<s:property value="%{#attr.currentRowObject.response.tenderUnit.id}"/>')">
 						 			<s:property value="%{#attr.currentRowObject.response.number}"/>
 							 	</a>
          			    	</display:column>
          			    	<display:column title="Reference Number" style="text-align:center;" >
          			    		<s:property value="%{#attr.currentRowObject.response.tenderUnit.tenderUnitNumber}" />
          			    	</display:column>
          			    	<display:column title="Tender File Number" style="text-align:center;" >
          			    		<s:property value="%{#attr.currentRowObject.response.tenderUnit.tenderNotice.tenderFileRefNumber}" />
          			    	</display:column>
          			    	<display:column title="Created By" style="text-align:center;" property="response.createdBy.userName" />
          			    	<display:column title="Status" style="text-align:center;" property="response.status.code" />
          			    	 
          			    	 <display:column title="Owner" style="text-align:center;" >
          			        <s:property value="%{getUsertName(#attr.currentRowObject.response.state.owner.id)}" />
							</display:column>
          			    	 
          			    	
          			      
          			    	 
          			    	 <display:column media="html" class="blueborderfortd" title="Action" style="width:12%;text-align:center;" >
								<s:select  id="workflowActions" name="workflowActions" list="#attr.currentRowObject.availableStatus"  headerValue="----------Choose-------" headerKey="0" onchange="callOnChange(this);"> </s:select>
							 </display:column>
					   </div>
					</display:table>
					<s:if test="%{sourcepage.equals('cancelBidResponse')}"> 
											<br/>
												<P align="left">
													<b><span class="mandatory">*</span><s:text name="cancel.remarks" />:</b>&nbsp;&nbsp;<s:select id="cancelRemarks" name="cancelRemarks" cssClass="selectwk" list="#{'DATA ENTRY MISTAKE':'DATA ENTRY MISTAKE','OTHER':'OTHER'}" />
												</P>
												<P align="center">
													<input type="button" class="buttonsubmit"
													value="Cancel BID Response" id="addButton"
													name="cancelBidResp" onclick="cancelBidResponses();"
												align="center" />
												</P>
										</s:if>	
          		 </div>
          		
         </s:if>
         <s:else>
         	 <div  style="color:red;font:bold" align="center">
				<span> No Record Found !</span>
			</div>
         </s:else>
    </div>
     <div id="submitting"  class="loading" style="display:none;width: 700; height: 700;" align="left">
           <span>Processing , Please wait...</span>
    </div>	
    </s:if>
 			
	</s:form>

</body>
</html>