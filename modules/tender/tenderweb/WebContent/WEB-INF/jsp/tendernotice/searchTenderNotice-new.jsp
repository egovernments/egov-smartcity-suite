<%@ taglib uri="/WEB-INF/struts-tags.tld" prefix="s" %>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ include file="/includes/taglibs.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<title>Search Tender Notice</title>

  <script type="text/javascript" src="<c:url value='/common/js/jquery-1.7.2.min.js'/>"></script>
	<script>
	 
	var noticeUnitId="";
	var isError=false;
	function resetvalues()
	{
			document.getElementById("fromDate").value="";
			document.getElementById("toDate").value="";
			document.getElementById("departmentId").value="";
			document.getElementById("fileType").value="";
			document.getElementById("tenderNoticeNumber").value="";
			document.getElementById("filetypeNumber").value="";
			changenumber();
			dom.get('searchTenderNotice_error').style.display ='none';
			if(dom.get('fieldError')!=null)
				dom.get('fieldError').style.display ='none';
			if(dom.get('tableData')!=null)
				dom.get('tableData').style.display ='none';
			noticeUnitId="";
			return false;
	}
	
	function validateForm()
	{
	
	    if(!checkStringMandatoryField('fileType','Tender File Type','searchTenderNotice_error'))
	    	return false;
	   else if(document.getElementById('fromDate').value!="" && document.getElementById('toDate').value!=""
	            && compareDate(document.getElementById('fromDate').value,
				document.getElementById('toDate').value) < 0)
	   {
			dom.get("searchTenderNotice_error").style.display = '';
			document.getElementById("searchTenderNotice_error").innerHTML = '<s:text name="fromdate.todate.validate" />';
			document.getElementById('toDate').value = "";
			return false;
	    }
	    else
	     return true;
	       
	}
	
	
	function openTenderNotice(id)
	{
	   document.searchTenderForm.action="${pageContext.request.contextPath}/tendernotice/tenderNotice!view.action?idTemp="+id+"&mode="+"view";
	   document.searchTenderForm.submit();
	   return false
	}
	
	function generateComparisionSheetReport()
	{
		if(noticeUnitId=="")
			{
				dom.get("searchTenderNotice_error").style.display = '';
				document.getElementById("searchTenderNotice_error").innerHTML = '<s:text name="tendernotice.biddetail.validate" />';
				return false;
			}
			else
			{
			    document.searchTenderForm.action="${pageContext.request.contextPath}/reports/comparisionReport!generateComparisionReport.action?tenderUnitId="+noticeUnitId;
			 	document.searchTenderForm.submit();
			 	return true;
			}
	}
	function createTenderResponse()
	{
	  /*  var tableObj=document.getElementById('currentRowObject');
		var lastRow = tableObj.rows.length-1;
		var selectedfile="";
		if(lastRow==1){
			if(document.getElementById("selectNotice").checked)
				 selectedfile=document.getElementById("noticeId").value;
		}
		else
		{
			for(i=0;i<lastRow;i++){
				if(document.forms[0].selectNotice[i].checked){
				 	selectedfile=document.forms[0].noticeId[i].value;
				}
			}
	    }
		//alert("selectedfile------>"+selectedfile);
		if(selectedfile == "")
		{
		 	dom.get("searchTenderNotice_error").style.display = '';
			document.getElementById("searchTenderNotice_error").innerHTML = '<s:text name="tendernotice.biddetail.validate" />';
			return false;
		}
		else
		{
		 	document.searchTenderForm.action="${pageContext.request.contextPath}/tendernotice/searchTenderNotice!loadTenderUnits.action?notice="+selectedfile;
		 	document.searchTenderForm.submit();
		}*/
		
		if(noticeUnitId=="")
		{
			dom.get("searchTenderNotice_error").style.display = '';
			document.getElementById("searchTenderNotice_error").innerHTML = '<s:text name="tendernotice.biddetail.validate" />';
			return false;
		}
		else
		{
		    document.searchTenderForm.action="${pageContext.request.contextPath}/tenderresponse/tenderResponse!loadTenderNoticeToCreateResponse.action?tenderUnitId="+noticeUnitId;
		 	document.searchTenderForm.submit();
		 	return true;
		}
		
	}
	
	function addRow(obj)
	{
		var curRow=getRow(obj).rowIndex;
		var curRow1=eval(curRow-1)/2;
		var rowId="noticeUnitDetails"+curRow1;
	   if(document.getElementById(rowId).style.display=='none')
	    	document.getElementById(rowId).style.display='';
	    else
	    	document.getElementById(rowId).style.display='none';
	}

    function setNoticeId(obj,noticeId)
    {
          if(obj.checked)
            noticeUnitId=noticeId;
         
    }

	function setTenderNoticeId(elem)
    {	
    	dom.get("tenderNoticeId").value = elem;
		checkBidResponse(elem);
	}
	 function setTenderNoticeNumber(elem)
    {	
    	dom.get("tenderNoticeNumber").value = elem;
	}
	
    function checkBidResponse(tenderNoticeId){
     makeJSONCall(["bidNo"],'${pageContext.request.contextPath}/tendernotice/ajaxTenderNotice!getApprovedBidResponse.action',{noticeId:tenderNoticeId},myNoticeSuccessHandler,myNoticeFailureHandler) ;
}

myNoticeSuccessHandler = function(req,res){
  results=res.results;
  var bidNo=''
  if(results=='') {
  	dom.get("searchTenderNotice_error").style.display='none';
  	document.getElementById("searchTenderNotice_error").innerHTML='';
	isError=false;
  }
  else{
  		if(results != '') {
  			for(var i=0; i<results.length;i++) {
  				if(bidNo!='')
  					bidNo=bidNo+', BidNo#:'+results[i].bidNo;
  				else
					bidNo=results[i].bidNo;
				}
  			}
  	
		if(bidNo != ''){
			dom.get("searchTenderNotice_error").style.display='';
			document.getElementById("searchTenderNotice_error").innerHTML='<s:text name="cancelTenderNotice.bidResponse.created.message"/>'+bidNo;
			isError=true;
	  		window.scroll(0,0);
			return false;
		}	
	}
}
  	
myNoticeFailureHandler= function(){
	document.getElementById("searchTenderNotice_error").style.display='';
	document.getElementById("searchTenderNotice_error").innerHTML='Unable to get Bid Responses for Tender Notice';
}


	function cancelTendrNotice(){
		if(dom.get('tenderNoticeId').value=='') {
			clearMessage('searchTenderNotice_error');
			dom.get("searchTenderNotice_error").style.display='';
			document.getElementById("searchTenderNotice_error").innerHTML='<s:text name="tenderNotice.cancel.not.selected" />';
			isError=true
			window.scroll(0,0);
		
	  	}
	  	if(!isError && validateCancel()) {
	  	document.searchTenderForm.action="${pageContext.request.contextPath}/tendernotice/tenderNotice!cancelApprovedTenderNotice.action?noticeId="+dom.get('tenderNoticeId').value;
		document.searchTenderForm.submit();
    	}
	}	

	function validateCancel() {
		var msg='<s:text name="tenderNotice.cancel.confirm"/>';
		var bidNo=dom.get("tenderNoticeNumber").value;
		if(!confirm(msg+": "+bidNo+" ?")) {
			return false;
		}
		else {
			 return true;
	 	}
	}

	jQuery.noConflict();
//	jQuery(window).bind('load',changenumber());

	 jQuery(document).ready(function () {
		 changenumber();
		 jQuery('#fileType').bind('change',function(){
			 changenumber();

		 });	
		});
			
	
			      			  
			

function changenumber(){
	 
	 var newText = jQuery('#fileType option:selected').text();
	
	 if(newText!=""&&newText!='---------choose---------')
     jQuery('#filetypeno').text(newText+' Number');	    
	 else
		 jQuery('#filetypeno').text('FileType Number');	    
		
}
	</script>

</head>
<body>
<div name="errorstyle" class="errorstyle" id="searchTenderNotice_error" style="display:none;">
</div>
<s:if test="%{hasErrors()}">
		<div class="errorstyle" id="fieldError" >
			<s:actionerror />
		</div>
</s:if>
<s:form action="searchTenderNotice"  name="searchTenderForm" theme="simple" onKeyPress="return disableEnterKey(event);" onsubmit="showWaiting();" onclick="hideFieldError();">
	<div class="formheading"></div>
		<div class="formmainbox"></div>	
		<s:hidden id="searchMode" name="searchMode" />
	
		<table width="100%" border="0" cellspacing="0" cellpadding="2">
			<s:hidden id="sourcepage" name="sourcepage" />
			<s:hidden id="tenderNoticeId" name="tenderNoticeId" />
			<s:hidden id="tenderNoticeNumber"/>
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
				<td class="greybox" width="10%"><s:text name="departmentlbl" /></td>
			    <td class="greybox" width="26%">
			    	<s:select name="departmentId" id="departmentId"  list="dropdownData.departmentIdList" listKey="id" listValue="deptName" value="%{departmentId}" headerKey="" headerValue="-------------choose----------------" />
			    </td>
				<td class="greybox" width="10%"><s:text name="tenderfiletype" /><span class="mandatory">*</span></td>
				<td class="greybox" width="26%">
					<s:select name="fileType" id="fileType" list="dropdownData.tenderFileTypeList" listKey="id" listValue="description" value="%{fileType}" headerKey="" headerValue="---------choose---------" onblur="checkStringMandatoryField('fileType','Tender File Type','searchTenderNotice_error');"/>
				</td>
				<td class="greybox" width="22%">&nbsp;</td>
			</tr>
			
			<tr>
				<td class="bluebox" width="5%">&nbsp;</td>
				<td class="bluebox" width="10%"><s:text name="tendernoticeno" /></td>
			    <td class="bluebox" width="26%"><s:textfield name="tenderNoticeNumber" id="tenderNoticeNumber" value="%{tenderNoticeNumber}"/></td>
				<td class="bluebox" width="10%"><s:label id="filetypeno" value="FileType Number" /></td>
				<td class="bluebox" width="26%"><s:textfield name="filetypeNumber" id="filetypeNumber" /></td>
				<td class="bluebox" width="22%">&nbsp;</td>
			</tr>
		</table>
		<s:hidden name="status" id="status" value="%{status}"/>
		<div id="actionbuttons" align="center" class="buttonbottom"> 

	<s:submit type="submit" cssClass="buttonsubmit" value="Search" id="Search" name="Search"  method="searchTenderNotice" onclick="return validateForm();"/>	
	<input type="button" name="reset" id="reset" class="button" value="Reset" onclick= "resetvalues();" />
	<input type="button" name="close" id="close" class="button" value="Close" onclick=" window.close();" />
	</div>
	<div id="loading"  style="display:none;color:red;font:bold" align="center">
				<span>Processing , Please wait...</span>
	</div>
	<br>
	<s:if test="%{mode=='new'}">
	<div id="tableData" align="center">
		<s:if test="%{searchMode == null || searchMode == ''}">
		
          		<s:if test="%{paginatedList!=null && paginatedList.getFullListSize()!=0}">
          		    <display:table  name="paginatedList" export="false" requestURI="" id="tNoticeId"  class="its" uid="currentRowObject"  >
          			   <div STYLE="display: table-header-group" align="center">
          			   	<s:if test="%{sourcepage=='cancelTenderNotice'}">
							<display:column headerClass="pagetableth" class="pagetabletd" title="Select" titleKey="column.title.select" style="width:4%;text-align:center">						
								<input name="radio" type="radio" id="radio"
								value="<s:property value='%{#attr.currentRowObject.id}'/>"
								onClick='setTenderNoticeId("<s:property value='%{#attr.currentRowObject.id}'/>");setTenderNoticeNumber("<s:property value='%{#attr.currentRowObject.number}'/>");' />
						</display:column>
						</s:if>
          			   		 <display:column  title=" Srl No "  style="width:7%;text-align:center;" >
			 						<s:property value="%{#attr.currentRowObject_rowNum}"/>	 						          
							</display:column>
							<display:column title="Date" style="text-align:center;" property="noticeDate" format="{0,date,dd-MM-yyyy}"/>	
          			 	  
          			    	<display:column title="Tender Notice Number" style="text-align:center;" >
	          			    	<a href="#" onclick="openTenderNotice('${currentRowObject.id}')"> ${currentRowObject.number}</a>
          			    	</display:column>	
          			    	<display:column title="Department" style="text-align:center;" property="department.deptName"/>
          			    	<display:column title="Status" style="text-align:center;" property="status.description"/>
          			        <display:column title="Owner" style="text-align:center;" >
          			        <s:property value="%{getUsertName(#attr.currentRowObject.state.owner.id)}" />
							</display:column>
				
          			    	
          			   </div>
          			 </display:table>
          				 <s:if test="%{sourcepage.equals('cancelTenderNotice')}"> 
											<br/>
												<P align="left">
													<b><span class="mandatory">*</span><s:text name="cancel.remarks" />:</b>&nbsp;&nbsp;<s:select id="cancelRemarks" name="cancelRemarks" cssClass="selectwk" list="#{'DATA ENTRY MISTAKE':'DATA ENTRY MISTAKE','OTHER':'OTHER'}" />
												</P>
												<P align="center">
													<input type="button" class="buttonsubmit"
													value="Cancel Tender Notice" id="addButton"
													name="cancelTenderNotice" onclick="cancelTendrNotice();"
												align="center" />
												</P>
										</s:if>	
         		</s:if>
         		<s:else>
             		<div  style="color:red;font:bold" align="center">
						<span> No Record Found !</span>
					</div>
         		</s:else>
         </s:if>
        <s:else>
           <s:if test="%{noticeList!=null && !noticeList.isEmpty()}">
       
         <table id="noticeDetails" width="80%" border="0" cellspacing="0" cellpadding="0" class="tablebottom">
	      <tr>
			<th  class="bluebgheadtd" width="3%"><div align="center">Sl No</div></th>
		    <th  class="bluebgheadtd" width="8%"><div align="center">Date</div></th>
		    <th  class="bluebgheadtd" width="8%"><div align="center">Tender Notice Number </div></th>
		    <th  class="bluebgheadtd" width="2%"><div align="center"> Department</div></th>
		    <th  class="bluebgheadtd" width="2%"><div align="center"> Click </div></th>
		 </tr>
		  <s:iterator value="noticeList" status="row_status">
		  <tr>
		  		<td  class="bluebox" width="3%" rowspan="1"><div align="center"><s:property value="%{#row_status.count}" /></div></td>
		  	    <td class="bluebox" width="8%">
		    		<div align="center">
		    			<s:date name="noticeDate" format="dd/MM/yyyy" var="noticeDateTemp"  />
		    			<s:property value="noticeDateTemp" />
		    		</div>
		        </td>
		       <td  class="bluebox" width="8%"><div align="center"><a href="#" onclick="openTenderNotice('${id}');"><s:property value="number" default="NA"/></a></div></td>
		       <td  class="bluebox" width="8%"><div align="center"><s:property value="department.deptName" /></div></td>
		       <td  class="bluebox" width="2%"  rowspan="1" ><div align="center"><a href="#"><img src="${pageContext.request.contextPath}/common/image/addrow.gif" height="16"  width="16" border="2" alt="Add" onclick="addRow(this);"></a></div></td>
		    </tr>
		   
		    <tr>
		      <td colspan="5">
			    <table id="noticeUnitDetails<s:property value="%{#row_status.index}" />" width="100%" border="0" cellspacing="0" cellpadding="0" class="tablebottom" style="display:none;">
				      
      <td  width="5%" class="greybox" >&nbsp;</td>
				            <s:if test="%{tenderFileTypeName=='Works estimate'}">
				            <td  width="30%" class="greybox" ><div align="center"><h1 class="tablesubhead">Estimate Number</h1></div></td>
				            </s:if>
				            <s:else>
				            <td  width="30%" class="greybox" ><div align="center"><h1 class="tablesubhead">Tender Number</h1></div></td>
				            </s:else>
				     	 	
				    	 	<td  width="20%" class="greybox" ><div align="center"><h1 class="tablesubhead">Description</h1></div></td>
				    		<td  width="20%" class="greybox" ><div align="center"><h1 class="tablesubhead">Estimated Cost</h1></div></td>
				    		<td  width="5%" class="greybox" ><div align="center"><h1 class="tablesubhead">Select</h1></div></td>
				    		 
				    	 </tr>
				    	 <s:iterator value="tenderUnitToCreateResponse"  status="unit_status">
				    	 <tr>
				    	    <td  width="5%" class="greybox" >&nbsp;</td>
				    		<td  class="greybox"><div align="center"><s:property value="tenderUnitNumber" default="NA" /></div></td>
				  			<td  class="greybox"><div align="center"><s:property value="tenderGroupNarration" default="NA"/></div></td>
				  			<td  class="greybox"><div align="center"><s:property value="estimatedCost" default="NA"/></div></td>
				  			<td  class="greybox"><div align="center"> <input type="radio" id="selectUnit" name="selectUnit" onclick="setNoticeId(this,'${id}');"/></div></td>
				  			  
				    	 </tr>
				    	
				    	 </s:iterator>
				     </table>
				 </td>
			</tr>
		  </s:iterator>
	  </table>
	 
	  <div id="actionbuttons" align="center" class="buttonbottom"> 
		<s:if test="%{searchMode != null && searchMode == 'report'}">
							<input type="button" class="buttonsubmit" value="Generate Report" id="report" name="report" onclick="return generateComparisionSheetReport();"/>
	   </s:if>        
         <s:else>		<input type="button" class="buttonsubmit" value="Capture Bid Details" id="create" name="create" onclick="return createTenderResponse();"/>
		  </s:else>
	  </div>
	</s:if>
         
         <s:else>
         	<div  style="color:red;font:bold" align="center">
				<span> No Record Found !</span>
			</div> 
         </s:else>
       </s:else>
    </div>
    </s:if>
  					
	</s:form>

</body>
</html>