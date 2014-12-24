<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ include file="/includes/taglibs.jsp" %>

<html>
<title>
<s:if test="%{mode=='view'}">
		 <s:text name="View Tender Notice"/>
	</s:if>
	<s:else>
	   <s:text name="createTenderNoticeTitle"/>
	</s:else>
</title>
<head>
	<jsp:include page='/WEB-INF/jsp/tendernotice/tenderNotice.jsp'/> 

	<script>
	function validateNumericValue(obj,field) {
			dom.get("tenderNotice_error").style.display = 'none';
			checkUptoTwoDecimalPlace(obj,"tenderNotice_error",field);
		}
		
		
		function openTenderfile()
		{
		  // alert("URL------>"+document.getElementById('linkSource').value);
		 	var id= document.getElementById('tenderfileid').value;    
		 	   
		 var  link = document.getElementById('linkSource').value; 
		  
		   window.open(link+id,"simple","height=650,width=900,scrollbars=yes,left=20,top=20,status=yes");        
	     
		 
		}
			
	function init()
	{
		var mode=document.getElementById('mode').value;
		
		if(mode=='view')
		{
					
				for(var i=0;i<document.forms[0].length;i++)
				{
					if( document.forms[0].elements[i].name!='close' && document.forms[0].elements[i].id!='print')
					{
					document.forms[0].elements[i].disabled =true;
					}
				}
		
		}
		if(mode=='notmodify')
		{
				for(var i=0;i<document.forms[0].length;i++)
				{
					if( document.forms[0].elements[i].name!='close' && document.forms[0].elements[i].id!='approve' &&
					     document.forms[0].elements[i].id!='reject' && document.forms[0].elements[i].id!='saveAndClose' && document.forms[0].elements[i].id!='comments' )
				
					{
					document.forms[0].elements[i].disabled =true;
					}
				}
				document.getElementById('noticeDate').disabled =true;
	
		}
		if(mode=='modify')
		{
		
		document.getElementById('noticeDate').disabled =true;
		
		
		 }
	}	
		
	function validateApprover(obj)
	{
		
		 if(document.getElementById('workFlowType').value=='reject'){ 		  
	  		  	if(document.getElementById('comments').value==""){
	  		  		alert(" Please enter Comments for rejection")
	  		  		 return false; 
	  		    	}
	  		 }
	  	/*if(document.getElementById('workFlowType').value=='approve' && document.getElementById('approverName').value=="-1")
			{
				alert("Select Approver");
				return false;
			} */
			if(document.getElementById('workFlowType').value=='savesubmit' && document.getElementById('approverName').value=="-1")
			{
				alert("Select Approver");
				return false;
			}	 
			return true;
	}	
	function validate(obj)
	{
		document.getElementById('workFlowType').value=obj;
		
		/* if(!validateLines())
		  return false; */
		  
		 
		if(!validateApprover(obj))
		  return false;
		
	    if(!checkStringMandatoryField('noticeDate','Notice Date','tenderNotice_error'))
		    	return false;
		 var autogenNumber=document.getElementById('autoGenerateNumberFlag').value;
		 //alert(autogenNumber);
		 if(autogenNumber){   	
		if(!checkStringMandatoryField('number','Tender Notice Number','tenderNotice_error'))
		    	return false;
		    	}
		 return true;  	
	}	  
	function refreshInbox()
	  {
	
	  if(opener.top.document.getElementById('inboxframe')!=null)
	  {
	  	if(opener.top.document.getElementById('inboxframe').contentWindow.name!=null && opener.top.document.getElementById('inboxframe').contentWindow.name=="inboxframe")
	  		{ 
	  		 opener.top.document.getElementById('inboxframe').contentWindow.egovInbox.refresh();
	  		}
	  	}
	 }
	  function checkuniqueness()
	  {
	  		
				dom.get("tenderNotice_error").style.display = 'none';
				document.getElementById('noticeNumberUnique').style.display = 'none';
				var noticeNumber = document.getElementById('number').value;
				var id = document.getElementById('id').value;
				populatenoticeNumberUnique( {tenderNoticeNumber : noticeNumber,id : id});
		
		}	
		
		
		function trimString(str) 
	{ 
	     var str1 = ''; 
	     var i = 0; 
	     while ( i != str.length) 
	     { 
	         if(str.charAt(i) != ' ') str1 = str1 + str.charAt(i); i++; 
	     }
	     var retval = IsNumeric(str1); 
	     if(retval == false) 
	         return -100; 
	     else 
	         return str1; 
	}
		
		
		function trimAllSpace(str) 
	{ 
	    var str1 = ''; 
	    var i = 0; 
	    while(i != str.length) 
	    { 
	        if(str.charAt(i) != ' ') 
	            str1 = str1 + str.charAt(i); 
	            i ++; 
	    } 
	  
	    return str1; 
	}
		
		
		function  validatetime(obj)
	{
		
	  var strval = obj.value;
	 // alert(strval.length);
	  var strval1;  
	  if(strval.length > 0){
		  if(strval.length < 6 )
		  {
		   alert("Invalid . Time format should be HH:MM AM/PM.");
		   obj.value="";
		   return false;
		  }
		
		  if(strval.length > 8)
		  {
		   alert("Invalid . Time format should be HH:MM AM/PM.");
		   obj.value="";
		   return false;
		  }
		
		  strval = trimAllSpace(strval); 
	
		  if(strval.charAt(strval.length - 1) != "M" && strval.charAt(
		      strval.length - 1) != "m")
		  {
		   alert("Invalid time. Time shoule be end with AM or PM.");
		   obj.value="";
		   return false;
		  }
		  else if(strval.charAt(strval.length - 2) != 'A' && strval.charAt(
		      strval.length - 2) != 'a' && strval.charAt(
		      strval.length - 2) != 'p' && strval.charAt(strval.length - 2) != 'P')
		  {
		   alert("Invalid time. Time shoule be end with AM or PM.");
		   obj.value="";
		   return false;	   
	  	 }
	
		 
		  strval1 =  strval.substring(0,strval.length - 2);
		  strval1 = strval1 + ' ' + strval.substring(strval.length - 2,strval.length)
		  
		  strval = strval1;
		      
		  var pos1 = strval.indexOf(':');
		  obj.value = strval;
		 
		  if(pos1 < 0 )
		  {
		   alert("invlalid time. A color(:) is missing between hour and minute.");
		   obj.value="";
		   return false;
		  }
		  else if(pos1 > 2 || pos1 < 1)
		  {
		   alert("invalid time. Time format should be HH:MM AM/PM.");
		   obj.value="";
		   return false;
		  }
		  
		  //Checking hours
		  var horval =  trimString(strval.substring(0,pos1));
		 
		  if(horval == -100)
		  {
		   alert("Invalid time. Hour should contain only integer value (0-11).");
		   obj.value="";
		   return false;
		  }
		      
		  if(horval > 12)
		  {
		   alert("invalid time. Hour can not be greater that 12.");
		   obj.value="";
		   return false;
		  }
		  else if(horval < 0)
		  {
		   alert("Invalid time. Hour can not be hours less than 0.");
		   obj.value="";
		   return false;
		  }
		  //Completes checking hours.
		  
		  //Checking minutes.
		  var minval =  trimString(strval.substring(pos1+1,pos1 + 3));
		  
		  if(minval == -100)
		  {
		   alert("Invalid time. Minute should have only integer value (0-59).");
		   obj.value="";
		   return false;
		  }
		    
		  if(minval > 59)
		  {
		     alert("Invalid time. Minute can not be more than 59.");
		     obj.value="";
		     return false;
		  }   
		  else if(minval < 0)
		  {
		   alert("Invalid time. Minute can not be less than 0.");
		   obj.value="";
		   return false;
		  }
		   
		  //Checking minutes completed.
		  
		  //Checking one space after the mintues 
		  minpos = pos1 + minval.length + 1;
		  if(strval.charAt(minpos) != ' ')
		  {
		   alert("Invalid time. Space missing after minute. Time should have HH:MM AM/PM format.");
		   obj.value="";
		   return false;
		  }
	 }
	  return true;
	}
	
	  	
	</script>
</head>
<body onload="init();refreshInbox();" >
	<div class="errorstyle" id="tenderNotice_error" style="display: none;"></div>
		<div class="errorstyle" style="display:none" id="noticeNumberUnique" >
			<s:text name="noticeNumber.alreadyExist"/>
		</div>
		<s:if test="%{hasErrors()}">
			<div class="errorstyle">
				<s:actionerror />
				<s:fielderror />
			</div>
		</s:if>
		<s:if test="%{hasActionMessages()}">
			<div class="errorstyle">
				<s:actionmessage />
			</div>
		</s:if>
<s:form action="tenderNotice" name="tendernoticeform" theme="simple" onkeypress="return disableEnterKey(event);" onsubmit="enablingFields();">
<s:token name="%{tokenName()}"/>

<s:push value="model">

<s:hidden id="tenderFileId" name="tenderFileId" value="%{tenderFileId}" />
<s:hidden id="state" name="state" value="%{state.id}" />
<s:hidden id="createdDate" name="createdDate" value="%{createdDate}"/>
<s:hidden id="createdBy" name="createdBy" value="%{createdBy.id}"/>
<s:hidden id="modifiedBy" name="modifiedBy" value="%{modifiedBy.id}"/>
<s:hidden id="id" name="id" value="%{id}" />
<s:hidden id="idTemp" name="idTemp" value="%{id}" />
<s:hidden id="tenderFileType" name="tenderFileType" value="%{tenderFileType.id}" />
<s:hidden id="workFlowType" name="workFlowType"/>
<s:hidden id="autoGenerateNumberFlag" name="autoGenerateNumberFlag" value="%{autoGenerateNumberFlag}"/>
<s:hidden id="status" name="status" value="%{status.id}" />
<s:hidden id="combineTenderableGroups" name="combineTenderableGroups" value="%{combineTenderableGroups}" />
<s:hidden id="linkSource" name="linkSource" value="%{linkSource}" />
<s:hidden id="tenderfileid" name="tenderfileid" value="%{tenderFile.id}" />
<s:iterator value="tenderableGroupsList" status="row">     
 	<s:hidden id="tenderableGroupsList%{#row.index}.number" name="tenderableGroupsList[%{#row.index}].number" value="%{number}" />
  	<s:hidden id="tenderableGroupsList%{#row.index}.tenderableGroupType" name="tenderableGroupsList[%{#row.index}].tenderableGroupType" value="%{tenderableGroupType}" />
  	
 	<s:hidden id="tenderableGroupsList%{#row.index}.id" name="tenderableGroupsList[%{#row.index}].id" value="%{id}" />
  	<s:hidden id="tenderableGroupsList%{#row.index}.description" name="tenderableGroupsList[%{#row.index}].description" value="%{description}" />
  	
 	<s:hidden id="tenderableGroupsList%{#row.index}.estimatedCost" name="tenderableGroupsList[%{#row.index}].estimatedCost" value="%{estimatedCost}" />
  	<s:hidden id="tenderableGroupsList%{#row.index}.tenderUnit" name="tenderableGroupsList[%{#row.index}].tenderUnit.id" value="%{tenderUnit.id}" />
    <s:hidden id="tenderableGroupsList%{#row.index}.tenderUnitRefNumber" name="tenderableGroupsList[%{#row_status.index}].tenderUnitRefNumber" value="%{tenderUnitRefNumber}" />
		   
 </s:iterator> 
 
<s:iterator value="tenderEntitiesList" status="rows">    
 	<s:hidden id="tenderEntitiesList%{#rows.index}.description"  name="tenderEntitiesList[%{#rows.index}].description" value="%{description}" />
 	<s:hidden id="tenderEntitiesList%{#rows.index}.id"  name="tenderEntitiesList[%{#rows.index}].id" value="%{id}" />
 	
 	<s:hidden id="tenderEntitiesList%{#rows.index}.name"  name="tenderEntitiesList[%{#rows.index}].name" value="%{name}" />
 	<s:hidden id="tenderEntitiesList%{#rows.index}.number"  name="tenderEntitiesList[%{#rows.index}].number" value="%{number}" />
 	<s:hidden id="tenderEntitiesList%{#rows.index}.requestedByDate"  name="tenderEntitiesList[%{#rows.index}].requestedByDate" value="%{requestedByDate}" />
 	<s:hidden id="tenderEntitiesList%{#rows.index}.requestedQty"  name="tenderEntitiesList[%{#rows.index}].requestedQty" value="%{requestedQty}" />
 	<s:hidden id="tenderEntitiesList%{#rows.index}.requestedValue"  name="tenderEntitiesList[%{#rows.index}].requestedValue" value="%{requestedValue}" />
 	<s:hidden id="tenderEntitiesList%{#rows.index}.requestedUOM"  name="tenderEntitiesList[%{#rows.index}].requestedUOM.id" value="%{requestedUOM.id}" />
	<s:hidden id="tenderEntitiesList%{#rows.index}.tenderableType"  name="tenderEntitiesList[%{#rows.index}].tenderableType" value="%{tenderableType}" />
 	<s:hidden id="tenderEntitiesList%{#rows.index}.tenderUnit"  name="tenderEntitiesList[%{#rows.index}].tenderUnit.id" value="%{tenderUnit.id}" />
 	<s:hidden id="tenderEntitiesList%{#rows.index}.tempEntityGroupName"  name="tenderEntitiesList[%{#rows.index}].tempEntityGroupName" value="%{tempEntityGroupName}" />
 	<s:hidden id="tenderEntitiesList%{#rows.index}.indexNo"  name="tenderEntitiesList[%{#rows.index}].indexNo" value="%{indexNo}" />
 	 	

 </s:iterator> 
 
 <div class="formmainbox">
 		  <table width="100%" border="0" cellspacing="0" cellpadding="2">
		      	
		      	<s:hidden id="mode" name="mode" value="%{mode}"/>
				<tr>
					<td width="12%" class="bluebox">&nbsp;</td>
					<td width="14%" class="bluebox"><s:text name="tenderNoticeDate"/><span class="mandatory">*</span> </td>
					<td  width="28%" class="bluebox">
								 <s:hidden name="paramDate" id="paramDate" value="%{noticeDateTemp}"/>	  
								<s:date name="noticeDate" format="dd/MM/yyyy" var="noticeDateTemp"/>
								<s:textfield name="noticeDate" id="noticeDate"
									value="%{noticeDateTemp}" maxlength="20"
									onkeyup="DateFormat(this,this.value,event,false,'3')"
									onblur="validateDateFormat(this);" />
								<s:if test="%{mode=='create'}">
									<a href="javascript:show_calendar('forms[0].noticeDate');"
										onmouseover="window.status='Date Picker';return true;"
										onmouseout="window.status='';return true; "><img
											src="${pageContext.request.contextPath}/common/image/calendaricon.gif"
											border="0" />
									</a>
								</s:if>		
								
						
							
								
									
					</td>
					 <s:if test="%{id!=null}">
						<td class="bluebox"><s:text name="tenderNoticeNumber"/>  </td>
						<td class="bluebox"><s:textfield name="number" id="number" value="%{number}" readonly="true"/></td>
					</s:if>
					<s:else>
						<s:if test="%{autoGenerateNumberFlag==false}">  
							<td class="bluebox"><s:text name="tenderNoticeNumber"/> <span class="mandatory">*</span></td>
							<td class="bluebox">
								<s:textfield name="number" id="number" value="%{number}"   onblur="checkuniqueness();"/>
							<egov:uniquecheck id="noticeNumberUnique" fieldtoreset="number" fields="['Value']" url='tendernotice/ajaxTenderNotice!tenderNoticeNumberUniqueCheck.action' />				
			
							</td>
						</td>
						</s:if>	
						<s:else>	
						<td class="bluebox">&nbsp;</td>
						<td class="bluebox">&nbsp;</td>
						</s:else>
					</s:else>
					
				</tr>
				<tr>
					<td class="greybox">&nbsp;</td>
					<td class="greybox"><s:text name="tenderFileNumber"/></td>
					<td class="greybox"><a href="javascript:openTenderfile();"/><s:property value="%{tenderFile.fileNumber}"/></a>	</td>
					<s:hidden  readonly="true" id="tenderFileRefNumber" name="tenderFileRefNumber" value="%{tenderFileRefNumber}"/>
						<td class="greybox"><s:text name="department"/></td>
					<td class="greybox"><s:select id="department" name="department"  list="dropdownData.departmentIdList" headerKey="-1" disabled="true" headerValue="------Choose------" value="%{department.id}"  listKey="id" listValue="deptName"/>
	   	 			</td>
					
				</tr>	
				
			<!-- 	<tr>
				
			 	<td class="bluebox">&nbsp;</td>
				<td class="bluebox"><s:text name="TenderFile No:"/></td>
				<td class="bluebox"><a href="javascript:openTenderfile();"/><s:property value="%{tenderFile.fileNumber}"/></a>											
				</td>	
				<td class="bluebox">&nbsp;</td>		
				<td class="bluebox">&nbsp;</td>			
				</tr>  -->
			 </table>
		</div>
		<div id="noticeDetail">
		<div class="blueshadow"></div>
	
		<h1 class="subhead"> <s:text name="tenderNoticeDetail"/> </h1>
		<div align="center" style="width:1280px;overflow:auto">
	<table id="tenderNoticeDetailTable" width="100%" border="0" cellspacing="0" cellpadding="1">
	     <tr>
			<th  class="bluebgheadtd" width=""><div align="center"><s:text name="serialNumber"/></div></th>
		    <th  class="bluebgheadtd" width="" ><div align="center"> 
		    <s:if test="%{tenderFileType.groupType=='ESTIMATE' }" >
		    	<s:text name="estimateNumber"/>
		  	  </s:if>
		  	  <s:elseif   test="%{tenderFileType.groupType=='RATECONTRACT_INDENT' || tenderFileType.groupType=='ITEM_INDENT'|| tenderFileType.groupType=='WORKS_RC_INDENT'}" >
		    	<s:text name="indentNumber"/>
		  	  </s:elseif >
		    <s:else> <s:text name="referenceNumber"/>
		    </s:else>
		    </div></th>
		   
		    <th  class="bluebgheadtd" width=""><div align="center"> 
		     <s:if test="%{tenderFileType.groupType=='ESTIMATE'}" >
		    	<s:text name="worksDescription"/>
		  	  </s:if>
		  	    <s:elseif   test="%{tenderFileType.groupType=='WORKS_RC_INDENT'}" >
		    	<s:text name="Tender Description"/>
		  	  </s:elseif >
		  	  <s:elseif   test="%{tenderFileType.groupType=='RATECONTRACT_INDENT' || tenderFileType.groupType=='ITEM_INDENT'}" >
		    	<s:text name="narration"/>
		  	  </s:elseif >
		    <s:else> <s:text name="description"/>
		    </s:else>
		    </div></th>
		    
		    <th  class="bluebgheadtd" width="">
		    <div align="center">
		    <s:if test="%{tenderFileType.bidderType=='Supplier'}" >
		    	<s:text name="estimateCost"/>
		  	  </s:if>
		  	  <s:elseif   test="%{tenderFileType.groupType=='ESTIMATE'}" >
		    	<s:text name="estimateAmount"/>
		  	  </s:elseif >
		  	  <s:elseif   test="%{tenderFileType.groupType=='WORKS_RC_INDENT'}" >
		    	<s:text name="indentAmount"/>
		  	  </s:elseif >
		    <s:else> 
		        <s:text name="ratePerSqFt"/>
		    </s:else>
		    </div>
		    </th>
		    <th  class="bluebgheadtd" width=""><div align="center"><s:text name="emdAmount"/></div></th>
		    <th  class="bluebgheadtd" width=""><div align="center"><s:text name="timeLimit"/></div></th>
		    <th  class="bluebgheadtd" width=""><div align="center"><s:text name="bankCostofTender"/></div></th>
		    <th  class="bluebgheadtd" width=""><div align="center">
		     <s:if test="%{tenderFileType.groupType=='ESTIMATE' || tenderFileType.groupType=='WORKS_RC_INDENT'}" >
		    	<s:text name="contractorClass"/>
		  	  </s:if>
		  	  <s:elseif   test="%{tenderFileType.groupType=='RATECONTRACT_INDENT' || tenderFileType.groupType=='ITEM_INDENT'}" >
		    	<s:text name="supplierGrade"/>
		  	  </s:elseif >
		    <s:else> <s:text name="supplierGrade"/>
		    </s:else>
		    </div></th>
		    
		    <th  class="bluebgheadtd" width="" ><div align="center"><s:text name="tenderSaleDate"/> <span class="mandatory">*</span></div></th>
		    <th  class="bluebgheadtd" width="" ><div align="center"><s:text name="tenderSaleEndDate"/></div></th>
		    <th  class="bluebgheadtd" width=""><div align="center"><s:text name="tenderSubmissionDate"/> <span class="mandatory">*</span></div></th>
		    <th  class="bluebgheadtd" width="" ><div align="center"><s:text name="submissiontime"/></div></th>
		    <th  class="bluebgheadtd" width=""><div align="center"><s:text name="bidMeetingDate"/> <span class="mandatory">*</span></div></th>
		    <th  class="bluebgheadtd" width=""><div align="center"><s:text name="openingtimeofEtender"/> <span class="mandatory">*</span></div></th>
		    <th  class="bluebgheadtd" width="" ><div align="center"><s:text name="openingtime"/></div></th>
		 </tr>  
		  <s:iterator value="tenderUnitDetailsList" status="row_status">
		   <tr>
		  	<td  class="blueborderfortd"><s:textfield name="tenderUnitDetailsList[%{#row_status.index}].srlNo" id="srlNo" readonly="true"  class="blueborderfortd" value="%{#row_status.count}" cssStyle="text-align:center" size="6"/></td>
		         
		     <td class="blueborderWithBlckBckGrdfortd"> 
		    	<div align="center"> 
		    		<s:textfield name="tenderUnitDetailsList[%{#row_status.index}].tenderGroupRefNumber" id="tenderGroupRefNumber"  value="%{tenderGroupRefNumber}" maxlength="64" readonly="true" class="blueborderfortd"  size="30"/>
		    	</div>
		    </td>           
		    <td  class="blueborderWithBlckBckGrdfortd"><s:textarea name="tenderUnitDetailsList[%{#row_status.index}].tenderGroupNarration"  id="tenderGroupNarration" cols="15" rows="2" class="blueborderfortd"  /></td>
		     <td  class="blueborderWithBlckBckGrdfortd"><s:textfield name="tenderUnitDetailsList[%{#row_status.index}].estimatedCost" id="estimatedCost" readonly="true" maxlength="10" class="blueborderfortd"   cssStyle="text-align:right" /></td>
		  	
		   <td  class="blueborderfortd"><s:textfield name="tenderUnitDetailsList[%{#row_status.index}].emd" id="emd" maxlength="12" class="blueborderfortd" cssStyle="text-align:right" size="15" onblur="validateNumericValue(this,'Emd');"/></td>
		   <td  class="blueborderfortd"><s:textfield name="tenderUnitDetailsList[%{#row_status.index}].timeLimit" id="timeLimit" maxlength="10" class="blueborderfortd"  cssStyle="text-align:right" size="8"/></td>
		   <td  class="blueborderfortd"><s:textfield name="tenderUnitDetailsList[%{#row_status.index}].formCost" id="formCost" maxlength="10" class="blueborderfortd"  cssStyle="text-align:right" size="8" onblur="validateNumericValue(this,'Cost of blank tender');"/></td>
	<!--  <td  class="blueborderfortd">
		  
		  <select id="contractordropdown" name="tenderUnitDetailsList[<c:out value='${row_status.index}'/>].classOfContractor.id" >
		  	<option value="-1">------Choose--------</option>
		   <s:iterator value="contractorGradeList" var="contlist">
		  	<option  value="${contlist.id}"><c:out value="${contlist.grade}"/></option>
		   </s:iterator>
		   </select>
		   </td>   
		  	--> 
		 <td  class="blueborderfortd"><s:select id="classOfContractor" name="tenderUnitDetailsList[%{#row_status.index}].classOfContractor.id"  list="contractorGradeList"  headerKey="-1" headerValue="------Choose------" value="%{classOfContractor.id}" listKey="id" listValue='grade+"-"+description'/>
	   		</td> 
		   <td  class="blueborderfortd">
		   <s:date name="dateofSale" format="dd/MM/yyyy" var="dateofSaleTemp" />
			<s:textfield id="dateofSale%{#row_status.index}" name="tenderUnitDetailsList[%{#row_status.index}].dateofSale" 	value="%{dateofSaleTemp}"  maxlength="20" onkeyup="DateFormat(this,this.value,event,false,'3')" class="blueborderfortd" cssStyle="text-align:right"
									onblur="validateDateFormat(this);" /> 
									<a href="javascript:show_calendar('forms[0].dateofSale<s:property value="%{#row_status.index}"/>');" onmouseover="window.status='Date Picker';return true;"
					onmouseout="window.status='';return true;">
			<s:if test="%{mode!='view' && mode!='notmodify'}">
								
			<img src="${pageContext.request.contextPath}/common/image/calendaricon.gif" alt="Date" width="18" height="18"
			border="0" align="absmiddle" /></a>
		       			
			</s:if>
									
		   </td>
		   				
			
		
			   <td  class="blueborderfortd">
		   <s:date name="enddateofSale" format="dd/MM/yyyy" var="enddateofSaleTemp" />
			<s:textfield id="enddateofSale%{#row_status.index}" name="tenderUnitDetailsList[%{#row_status.index}].enddateofSale" 	value="%{enddateofSaleTemp}"  maxlength="20" onkeyup="DateFormat(this,this.value,event,false,'3')" class="blueborderfortd" cssStyle="text-align:right"
									onblur="validateDateFormat(this);" /> 
									<a href="javascript:show_calendar('forms[0].enddateofSale<s:property value="%{#row_status.index}"/>');" onmouseover="window.status='Date Picker';return true;"
					onmouseout="window.status='';return true;">
			<s:if test="%{mode!='view' && mode!='notmodify'}">
								
			<img src="${pageContext.request.contextPath}/common/image/calendaricon.gif" alt="Date" width="18" height="18"
			border="0" align="absmiddle" /></a>
			</s:if>
									
		   </td>
		   
			
		
		   <td  class="blueborderfortd">
		  		   			
		  		   	 <s:date name="dateofSubmission" format="dd/MM/yyyy" var="dateofSubmissionTemp" />
		   		 	<s:textfield name="tenderUnitDetailsList[%{#row_status.index}].dateofSubmission" id="dateofSubmission%{#row_status.index}"	 class="blueborderfortd"  cssStyle="text-align:right" value="%{dateofSubmissionTemp}" maxlength="10"
									onkeyup="DateFormat(this,this.value,event,false,'3')"
									onblur="validateDateFormat(this);" />
									<a href="javascript:show_calendar('forms[0].dateofSubmission<s:property value="%{#row_status.index}"/>');" onmouseover="window.status='Date Picker';return true;"
					onmouseout="window.status='';return true;">
				<s:if test="%{mode!='view' && mode!='notmodify'}">
			<img src="${pageContext.request.contextPath}/common/image/calendaricon.gif" alt="Date" width="18" height="18"
			border="0" align="absmiddle" /></a></s:if>
		   </td>
		   <td  class="blueborderfortd"><s:textfield name="tenderUnitDetailsList[%{#row_status.index}].submissionTime" id="submissionTime" value="%{submissionTime}"  size="10"  onblur="validatetime(this);"/>
			</td>
		   <td  class="blueborderfortd">
		    <s:date name="bidMeetingDate" format="dd/MM/yyyy" var="bidMeetingDateTemp" />
								<s:textfield name="tenderUnitDetailsList[%{#row_status.index}].bidMeetingDate" id="bidMeetingDate%{#row_status.index}"	class="blueborderfortd"  cssStyle="text-align:right" value="%{bidMeetingDateTemp}" maxlength="10"
									onkeyup="DateFormat(this,this.value,event,false,'3')"
									onblur="validateDateFormat(this);" />
									<a href="javascript:show_calendar('forms[0].bidMeetingDate<s:property value="%{#row_status.index}"/>');" onmouseover="window.status='Date Picker';return true;"
					onmouseout="window.status='';return true;">
			<s:if test="%{mode!='view' && mode!='notmodify'}">
			<img src="${pageContext.request.contextPath}/common/image/calendaricon.gif" alt="Date" width="18" height="18"
			border="0" align="absmiddle" /></a></s:if>
		   </td>
		   <td  class="blueborderfortd">
		    <s:date name="dateOfOpeningOfEtender" format="dd/MM/yyyy" var="dateOfOpeningOfEtenderTemp" />
								<s:textfield name="tenderUnitDetailsList[%{#row_status.index}].dateOfOpeningOfEtender" id="dateOfOpeningOfEtender%{#row_status.index}"	class="blueborderfortd"  cssStyle="text-align:right" value="%{dateOfOpeningOfEtenderTemp}" maxlength="10"
									onkeyup="DateFormat(this,this.value,event,false,'3')"
									onblur="validateDateFormat(this);" />
							<a href="javascript:show_calendar('forms[0].dateOfOpeningOfEtender<s:property value="%{#row_status.index}"/>');" onmouseover="window.status='Date Picker';return true;"
							onmouseout="window.status='';return true;">
							<s:if test="%{mode!='view' && mode!='notmodify'}">
			<img src="${pageContext.request.contextPath}/common/image/calendaricon.gif" alt="Date" width="18" height="18"
							border="0" align="absmiddle" /></a></s:if>
		   </td>
		    <s:hidden id="id" name="tenderUnitDetailsList[%{#row_status.index}].id" value="%{id}" />
		 <td class="blueborderfortd" width=""><s:textfield name="tenderUnitDetailsList[%{#row_status.index}].openTime" id="openTime" value="%{openTime}" size="10" onblur="validatetime(this);"/>
			
			</td></td>
		    </tr> 
		       
		   </s:iterator>
   	</table>
   </div>
    	<div align="center">
	 	<s:if test="%{mode!='view'}">
	 		  
					<s:if test="%{wfStatus!='APPROVED'}">	
					<s:if test="%{wfStatus!='CLOSE'}">
							<jsp:include page='/WEB-INF/jsp/common/approverInfo.jsp'/>
					</s:if>
				
		</s:if>						 
				 
					 	 <s:if test="%{mode!='create'}">
							 <table width="100%" border="0" cellspacing="0" cellpadding="0" >
								   <tr>
						      			<td class="greybox" width="12%">&nbsp;</td>	  
								  		<td class="greybox" width="14%">Comments:</td>
								  		<td class="greybox" colspan="3"  width="33%"> <s:textarea rows="2" cols="50"  id="comments" name="comments" value="%{comments}"/> </td>
								  		<td class="greybox" width="13%">&nbsp;</td>	
								  		<td class="greybox" width="33%">&nbsp;</td>	  
								 </tr>	
							 </table>
						 </s:if>	
		  </s:if>
		  <div class="blueshadow"></div> 
		  <br/>
		<table>
			<tr>
		<s:if test="%{mode=='create'}">	
		
			<td ><s:submit  cssClass="buttonsubmit"  id="saveAndClose" name="saveAndClose" method="create" value="Save"  onclick="return validate('save');"/></td> 
	  		<td ><s:submit  cssClass="buttonsubmit"  id="saveAndSubmit" name="saveAndSubmit"  method="create"  value="Save & Submit"  onclick="return validate('savesubmit');"/></td> 
	  		</td> 
		</s:if>
			<s:if test="%{mode =='modify' || mode == 'notmodify' }">
				<s:if test="%{wfStatus !='END'}">
			 			 <td><s:submit  cssClass="buttonsubmit" id="saveAndClose" name="saveAndClose" method="create" value="Save" onclick="return validate('save');"/></td>			
					<s:if test="%{wfStatus=='APPROVED'}">
					<td>	<s:submit type="submit" cssClass="buttonsubmit" value="Approve" id="approve" name="approve" method="create" onclick="return validate('approve');"/>	</td>	
					</s:if>
					<s:else>
					<td>	<s:submit type="submit" cssClass="buttonsubmit" value="Save&Submit" id="savesubmit" name="savesubmit" method="create" onclick="return validate('savesubmit');"/></td>
					</s:else>
					
			  		 <td><s:submit  cssClass="buttonsubmit" id="reject" name="reject" value="Reject"  method="create" onclick="return validate('reject');"/>
			  		 	
			  		 </td>
	  		 </s:if>
	  	</s:if>
	  	<s:if test="%{mode=='view'}">
	  		<td>
	  			<s:submit cssClass="buttonsubmit" id="print" name="print"  value="Print" method="print" /></td>
	  		</td>
	  	</s:if>
		<td><input type="button" name="close" id="close" class="button" value="Close"   onclick="window.close();"></td>
		</tr>	
	 </table>
	</div> 
	
	
	
	
	</s:push>	
	</s:form>
	</body>
	</html>