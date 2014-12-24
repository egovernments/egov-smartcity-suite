<%@ taglib uri="/WEB-INF/struts-tags.tld" prefix="s" %>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ include file="/includes/taglibs.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Search Tender File</title>
	<script>
	
	function resetvalues()
	{
			document.getElementById("fromDate").value="";
			document.getElementById("toDate").value="";
			document.getElementById("departmentId").value="";
			document.getElementById("fileType").value="";
			document.getElementById("tenderFileNumber").value="";
			document.getElementById("status").value="";
			dom.get('searchTenderfile_error').style.display ='none';
			if(dom.get('fieldError')!=null)
				dom.get('fieldError').style.display ='none';
			if(dom.get('tableData')!=null)
				dom.get('tableData').style.display ='none';
			return false;
	}
	
	function validateForm()
	{
	   if(!checkStringMandatoryField('departmentId','Department','searchTenderfile_error'))
	    	return false;
	   else if(!checkStringMandatoryField('fileType','Tender File Type','searchTenderfile_error'))
	    	return false;
	   else if(document.getElementById('fromDate').value!="" && document.getElementById('toDate').value!=""
	            && compareDate(document.getElementById('fromDate').value,
				document.getElementById('toDate').value) < 0)
	   {
			dom.get("searchTenderfile_error").style.display = '';
			document.getElementById("searchTenderfile_error").innerHTML = '<s:text name="fromdate.todate.validate" />';
			document.getElementById('toDate').value = "";
			return false;
	    }
	    else
	     return true;
	       
	}
	
	function createTenderNotice()
	{	
	    var selectedfile="",fileType="";
		if(!checkStringMandatoryField('fileType','Tender File Type','searchTenderfile_error'))
	    	return false;
	    else
	       fileType=document.getElementById('fileType').value;
	    	
		var tableObj=document.getElementById('currentRowObject');
		var lastRow = tableObj.rows.length-1;
		
		if(lastRow==1){
			if(document.getElementById("selecttfile").checked)
				 selectedfile=document.getElementById("fileId").value;
		}
		else
		{
			for(i=0;i<lastRow;i++){
				if(document.forms[0].selecttfile[i].checked){
				 	selectedfile=document.forms[0].fileId[i].value;
				}
			}
	    }
		
		if(selectedfile == "")
		{
		 	dom.get("searchTenderfile_error").style.display = '';
			document.getElementById("searchTenderfile_error").innerHTML = '<s:text name="tenderfile.tendernotice.validate" />';
			return false;
		}
		else
		{
		 	document.searchTenderForm.action="${pageContext.request.contextPath}/tendernotice/tenderNotice!loadTenderFileToCreateNotice.action?tenderFileId="+selectedfile;
		 	document.searchTenderForm.submit();
		}
	}
	
	function openTenderFile(id)
	{
	  // alert("URL------>"+document.getElementById('linkSource').value);
	   document.searchTenderForm.action = document.getElementById('linkSource').value + id;         
       document.searchTenderForm.submit();
	   return false
	}
	
	</script>

</head>
<body>
<div class="errorstyle" id="searchTenderfile_error" style="display:none;">
</div>
<s:if test="%{hasErrors()}">
		<div class="errorstyle" id="fieldError">
			<s:actionerror />
		</div>
</s:if>
<s:form action="searchTenderFile"  name="searchTenderForm" theme="simple" onKeyPress="return disableEnterKey(event);" onsubmit="showWaiting();" >
	<div class="formheading"></div>
		<div class="formmainbox"></div>	
		<s:hidden id="linkSource" name="linkSource" value="%{linkSource}" />	
		<table width="100%" border="0" cellspacing="0" cellpadding="2">

			<tr>
				<td class="bluebox" width="20%">&nbsp;</td>
				<td class="bluebox" width="10%"><s:text name="fromDatelbl" /></td>
			    <td class="bluebox" width="26%">
			    	<s:date name="fromDate" format="dd/MM/yyyy" var="fromDateTemp" />
			    	<s:textfield name="fromDate" id="fromDate" value="%{fromDateTemp}" onkeyup="DateFormat(this,this.value,event,false,'3')" onblur="validateDateFormat(this);" />
			    	<a href="javascript:show_calendar('forms[0].fromDate');" >
							<img src="${pageContext.request.contextPath}/common/image/calendaricon.gif"	border="0" align="absmiddle"/></a>
				</td>
				<td class="bluebox" width="10%"><s:text name="todatelbl" /></td>
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
				<td class="greybox" width="5%">&nbsp;</td>
				<td class="greybox" width="10%"><s:text name="departmentlbl" /><span class="mandatory">*</span></td>
			    <td class="greybox" width="26%">
			    	<s:select name="departmentId" id="departmentId"  list="dropdownData.departmentIdList" listKey="id" listValue="deptName" value="%{departmentId}" headerKey="" headerValue="-------------choose----------------" onblur="checkStringMandatoryField('departmentId','Department','searchTenderfile_error');"/>
			    </td>
				<td class="greybox" width="10%"><s:text name="tenderfiletype" /><span class="mandatory">*</span></td>
				<td class="greybox" width="26%">
					<s:select name="fileType" id="fileType" list="dropdownData.tenderFileTypeList" listKey="fileType" listValue="description" value="%{fileType}" headerKey="" headerValue="---------choose---------" onblur="checkStringMandatoryField('fileType','Tender File Type','searchTenderfile_error');"/>
				</td>
				<td class="greybox" width="22%">&nbsp;</td>
			</tr>
			
			<tr>
				<td class="bluebox" width="5%">&nbsp;</td>
				<td class="bluebox" width="10%"><s:text name="tenderfileno" /></td>
			    <td class="bluebox" width="26%"><s:textfield name="tenderFileNumber" id="tenderFileNumber" value="%{tenderFileNumber}"/></td>
				<td class="bluebox" width="10%">&nbsp;</td>
				<td class="bluebox" width="26%"><s:hidden name="status" id="status" value="%{status}"/></td>
				<td class="bluebox" width="22%">&nbsp;</td>
			</tr>
		</table>
		
		<div id="actionbuttons" align="center" class="buttonbottom"> 

	<s:submit type="submit" cssClass="buttonsubmit" value="Search" id="Search" name="Search"  method="searchTenderFile" onclick="return validateForm();"/>	
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
          		     <display:table  name="paginatedList" export="false" requestURI="" id="tFileId"  class="its" uid="currentRowObject"  >
          			   <div STYLE="display: table-header-group" align="center">
          			   		<display:column  title=" Select "  style="width:10%" >
			 						<div align="center"><input type="radio" id="selecttfile" name="selecttfile" /></div>  			 						          
							</display:column>
							<display:column class="hidden" headerClass="hidden"  media="html">
	 						 	<s:hidden id="fileId" name="fileId" value="%{#attr.currentRowObject.id}" />
	 						 </display:column>
          			 	   	<display:column title="Date" style="text-align:center;" property="tenderDate" format="{0,date,dd-MM-yyyy}"/>	
          			    	<display:column title="Tender File Number" style="text-align:center;" >
	          			    	<a href="#" onclick="openTenderFile('${currentRowObject.id}')">
 						 			 ${currentRowObject.fileNumber}
 							 	</a>
          			    	</display:column>	
          			    	<display:column title="Description" style="text-align:center;" property="description"/>	
          			    	<display:column title="Department" style="text-align:center;" property="department.deptName"/>	
          			    	<display:column title="Status" style="text-align:center;" property="status.code"/>		 	      
          			   </div>
          			 </display:table>
          		 </div>
          		 <div id="actionbuttons" align="center" class="buttonbottom"> 
					<input type="button" class="buttonsubmit" value="Create Tender Notice" id="create" name="create" onclick="return createTenderNotice();"/>
				</div>
         </s:if>
         <s:else>
         	<div  style="color:red;font:bold" align="center">
				<span> No Record Found !</span>
			</div>
         </s:else>
    </div>
    </s:if>
 					
	</s:form>

</body>
</html>