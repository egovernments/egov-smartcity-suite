<%@ taglib uri="/WEB-INF/struts-tags.tld" prefix="s" %>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ include file="/includes/taglibs.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<title>Bid Details</title>


	<script>
	
	
	
		
	function createTenderResponse()
	{
	    var tableObj=document.getElementById('currentRowObject');
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
		if(selectedfile == "")
		{
		 	dom.get("searchTenderNotice_error").style.display = '';
			document.getElementById("searchTenderNotice_error").innerHTML = '<s:text name="tender.biddetail.validate" />';
			return false;
		}
		else
		{
		 	document.searchTenderForm.action="${pageContext.request.contextPath}/tenderresponse/tenderResponse!loadTenderNoticeToCreateResponse.action?tenderUnitId="+selectedfile;
		 	document.searchTenderForm.submit();
		}
	}
	
	


	</script>

</head>
<body>
<div class="errorstyle" id="searchTenderNotice_error" style="display:none;">
</div>
<s:if test="%{hasErrors()}">
		<div class="errorstyle" id="fieldError" >
			<s:actionerror />
		</div>
</s:if>
<s:form action="searchTenderNotice"  name="searchTenderForm" theme="simple" onKeyPress="return disableEnterKey(event);" onsubmit="showWaiting();" onclick="hideFieldError();">
	<div class="formheading"></div>
	<br/>
		<div class="formmainbox">
			<h1 class="subhead"><s:text name="tendernoticeno"/> <s:property value="tenderNoticeNumber"/> </h1>
		</div>	
	<div id="tableData">
        <s:if test="%{tenderUnits!=null && tenderUnits.size()!=0}">
		         <div id="displaytbl">	
          		     <display:table  name="tenderUnits" export="false" requestURI="" id="tNoticeId"  class="its" uid="currentRowObject"  >
          			   <div STYLE="display: table-header-group" align="center">
          			   		 <display:column  title=" Select "  style="width:10%" >
			 						<div align="center"><input type="radio" id="selectNotice" name="selectNotice" /></div>  			 						          
							</display:column>
							 <display:column class="hidden" headerClass="hidden"  media="html">
	 						 	<s:hidden id="noticeId" name="noticeId" value="%{#attr.currentRowObject.id}" />
	 						 </display:column>
	 						 <display:column title=" Tender Number " style="text-align:center;">
								<s:property value="%{#attr.currentRowObject.tenderableGroups.toArray()[0].number}" />
							 </display:column>
							 <display:column title=" Description " style="text-align:center;">
								<s:property value="%{#attr.currentRowObject.tenderGroupNarration}" />
							 </display:column>
							 <display:column title=" Estimated Cost " style="text-align:center;">
								<s:property value="%{#attr.currentRowObject.estimatedCost}" />
							 </display:column>
          			  </div>
          			 </display:table>
          		 </div>
          		 <div id="actionbuttons" align="center" class="buttonbottom"> 
					<input type="button" class="buttonsubmit" value=" Enter Bid Details " id="create" name="create" onclick="return createTenderResponse();"/>
					<input type="button" class="button" value=" Close " id="close" name="close" onclick=" window.close(); "/>
				</div>
         </s:if>
         
         <s:else>
         	<div  style="color:red;font:bold" align="center">
				<span> No Record Found !</span>
			</div>
         </s:else>
    </div>

 					
	</s:form>

</body>
</html>