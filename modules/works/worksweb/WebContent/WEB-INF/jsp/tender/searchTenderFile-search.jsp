<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>

<html>
<head>
	<title><s:text name='page.title.search.tenderfile' /></title>
	<script src="<egov:url path='js/works.js'/>"></script>
	<script src="<egov:url path='js/helper.js'/>"></script>
	<script type="text/javascript">

	function validateSearch()
	{
		
		/*
		if(dom.get("pullDataForeTenderingFlag").checked && dom.get('noticeNumber').value=="")
		{
			dom.get("searchTenderFile_error").innerHTML='Please Select Tender Notice Number'; 
	        dom.get("searchTenderFile_error").style.display='';
			return false;
		}
		else
		{
			
		}
		*/
		
		if(dom.get('status').value==-1 && dom.get('noticeNumber').value=="" && dom.get('fileNum').value=="" && dom.get('estimateId').value=="-1")
		{
			dom.get("searchTenderFile_error").innerHTML='Please Select at least one criteria'; 
	        dom.get("searchTenderFile_error").style.display='';
			return false;
		}	
	    else{
			 dom.get("searchTenderFile_error").style.display='none';
			 dom.get("searchTenderFile_error").innerHTML=''; 
		 }
		
		return true;
	}	
	
	
	
	</script>
</head>

<body>
 <s:if test="%{hasErrors()}">
        <div id="errorstyle" class="errorstyle" >
          <s:actionerror/>
          <s:fielderror/>
        </div>
    </s:if>
<div class="errorstyle" id="searchTenderFile_error" style="display: none;"></div>

<s:form name="searchTenderFileForm" action="searchTenderFile" theme="simple" >
	<s:hidden id="mode" name="mode" value="%{mode}"/>
	<div class="formmainbox"></div>
	<div class="insidecontent">
	<div class="rbroundbox2">
	<div class="rbtop2"><div></div></div>
	<div class="rbcontent2">
		<table width="100%" border="0" cellspacing="0" cellpadding="0">          
          <tr>
            <td>&nbsp;</td>
          </tr>
        
		<tr>
	        	<td colspan="4" class="headingwk"><div class="arrowiconwk"><img src="${pageContext.request.contextPath}/image/arrow.gif" /></div>
	            <div class="headplacer"><s:text name="page.subheader.search.Criteria" /></div></td>
	           
	        </tr>
			<tr>
			<td colspan="4" ><%@ include file='searchTenderFileForWO.jsp'%></td>
			<script>
 					document.forms[0].tenderForEst.checked=true;
			</script>
			</tr>
			<tr>
				<td colspan="4" >	
					<%@ include file='searchTender.jsp'%>		
				</td>
			</tr>
		
	</table>
   <%@ include file='searchTenderFile-searchResults.jsp'%>
   </div><!-- end of rbroundbox2 -->
   <div class="rbbot2"><div></div></div>
   </div><!-- end of insidecontent -->
   </div><!-- end of formmainbox -->
</s:form>
</body>
</html>