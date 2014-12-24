<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%> 
<script>
var isError=false;
function checkTenderNotice(elem){
	clearMessage('tenderfilesearcherror');
	var fileId = elem.value;

	var myTenderNoticeSuccessHandler = function(req,res) {
                dom.get("tenderfilesearcherror").style.display='none';
                dom.get("tenderfilesearcherror").innerHTML='';
				if(res.results[0].noticeNumber==''){
					dom.get("fileId").value=elem.value;
					isError=false;
				}
				else{
					dom.get("tenderfilesearcherror").style.display='';
					document.getElementById("tenderfilesearcherror").innerHTML='<s:text name="cancel.tenderfile.tendernotice.created.message"/>'+' '+res.results[0].noticeNumber+'<br/>'
					window.scroll(0,0);
					isError=true;
				}
            };
            
	var myTenderNoticeFailureHandler = function() {
	            dom.get("tenderfilesearcherror").style.display='';
	            document.getElementById("tenderfilesearcherror").innerHTML='<s:text name="tender.notice.check.fail"/>';
	            isError=true;
	            window.scroll(0,0);
	        };
	makeJSONCall(["noticeNumber"],'${pageContext.request.contextPath}/tender/ajaxTenderFile!checkTenderNotice.action',{tenderFileId:fileId},myTenderNoticeSuccessHandler,myTenderNoticeFailureHandler) ;
}

function setTenderFileId(elem){
	dom.get('fileId').value = elem.value;
	checkTenderNotice(elem);
}

function cancelTenderFile() {
	if(dom.get('fileId').value=='') {
		clearMessage('tenderfilesearcherror');
    	dom.get("tenderfilesearcherror").style.display='';
		document.getElementById("tenderfilesearcherror").innerHTML+='<s:text name="tenderfile.cancel.select.null" /><br>';
		isError=true
		window.scroll(0,0);
	}

    if(!isError) {
    	document.tenderFileSearchForm.action='${pageContext.request.contextPath}/tender/tenderFile!cancelApprovedTenderFile.action';
		dom.get("egwStatus").disabled=false;
    	document.tenderFileSearchForm.submit();
    }
}

function gotoPage(obj){
	var currRow=getRow(obj);
	
	var id = getControlInBranch(currRow,'tenderId');
    var tenderFileStateId=getControlInBranch(currRow,'tenderFileStateId');	
	var docNumber = getControlInBranch(currRow,'docNumber');
	var showActions = getControlInBranch(currRow,'showActions');
	if(showActions[1]!=null && obj.value==showActions[1].value){	
		window.open("${pageContext.request.contextPath}/tender/tenderFile!edit.action?tenderFileId="+id.value+"&sourcepage=search",'', 'height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
	}
	if(showActions[2]!=null && obj.value==showActions[2].value){	
	
	window.open("${pageContext.request.contextPath}/tender/tenderFile!viewTenderFilePdf.action?tenderFileId="+id.value+
		"&sourcepage=search",'', 'height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');	
	}
	if(showActions[3]!=null && obj.value==showActions[3].value){
		window.open("${pageContext.request.contextPath}/tender/tenderFile!workflowHistory.action?stateValue="+
		tenderFileStateId.value,'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
	}
	if(showActions[4]!=null && obj.value==showActions[4].value){ 
		viewDocumentManager(docNumber.value);return false;
	}
}
</script>

<div>
     <s:if test="%{searchResult.fullListSize != 0}">
	     <s:if test="%{sourcepage=='cancelTenderFile'}">
    	 	<s:hidden name="fileId" id="fileId" value=""/>
    	 </s:if>
	     <display:table name="searchResult" pagesize="30"
			uid="currentRow" cellpadding="0" cellspacing="0"
			requestURI=""
			style="border:1px;width:100%;empty-cells:show;border-collapse:collapse;">
	        <s:if test="%{sourcepage=='cancelTenderFile'}">
		        <display:column headerClass="pagetableth" class="pagetabletd" title="Select" style="width:2%;" titleKey="column.title.select">
					<input name="radio" type="radio" id="radio"
						value="<s:property value='%{#attr.currentRow.id}'/>"
						onClick="setTenderFileId(this);" />
				</display:column>
	        </s:if>
	        <display:column headerClass="pagetableth"
			   class="pagetabletd" title="Sl. No."
			   titleKey="column.title.SLNo"
			   style="width:3%;text-align:left" >
			     <s:property value="#attr.currentRow_rowNum + (page-1)*pageSize"/>
			</display:column>
	        
            <display:column headerClass="pagetableth"
			   class="pagetabletd" title="Tender File Date"
			   titleKey="tender.file.search.column.date"
			   style="width:6%;text-align:left" >
		          <s:date name="#attr.currentRow.fileDate" format="dd/MM/yyyy" />
		    </display:column>

            <display:column headerClass="pagetableth"
			   class="pagetabletd" title="Department"
			   titleKey="tender.file.search.column.department"
			   style="width:10%;text-align:left" >
				   <s:property value="%{#attr.currentRow.department.deptName}" />
			</display:column>

	        <display:column headerClass="pagetableth"
		       class="pagetabletd" title="Tender File Number"
			   titleKey="tender.file.search.column.fileNumber"
			   style="width:8%;text-align:left">
 				<s:property  value='%{#attr.currentRow.fileNumber}' />
 				<s:hidden name="tenderId" id="tenderFileId" value="%{#attr.currentRow.id}" />
				<s:hidden name="docNumber" id="docNumber" value="%{#attr.currentRow.documentNumber}" />
            </display:column>
                   
            <display:column headerClass="pagetableth"
			   class="pagetabletd" title="Tender File Name"
			   titleKey="tender.file.search.column.fileName"
			   style="width:12%;text-align:left" >
			    <s:property  value='%{#attr.currentRow.fileName}' />
		    </display:column>

			<display:column headerClass="pagetableth" class="pagetabletd"
				title="Owner" titleKey="tender.file.search.owner"
				style="width:10%;text-align:left">
				<s:property value="#attr.currentRow.owner" />
			</display:column>

			<display:column headerClass="pagetableth"
			   class="pagetabletd" title="Status"
			   titleKey="tender.file.search.label.status"
			   style="width:6%;text-align:left" >
					<s:property value="%{#attr.currentRow.egwStatus.code}" />
					<s:hidden name="tenderFileStateId" id="tenderFileStateId" value="%{#attr.currentRow.state.id}" />
			</display:column>
			<s:if test="%{sourcepage!='cancelTenderFile'}">
	            <display:column headerClass="pagetableth"
					class="pagetabletd" title="Actions"
					titleKey="tender.File.search.actions"
					style="width:6%;text-align:left">
						<s:select theme="simple"
							list="%{#attr.currentRow.tenderFileActions}"
							name="showActions" id="showActions"
							headerValue="%{getText('default.dropdown.select')}"
							headerKey="-1" onchange="gotoPage(this);">
						</s:select>
				</display:column>  
		   </s:if>                                     
	   </display:table> 
		<s:if test="%{sourcepage=='cancelTenderFile'}">
			 <P align="left">
				<span class="mandatory">*</span><s:text name="cancel.remarks" />:
					<s:select id="cancelRemarks" name="cancelRemarks" cssClass="selectwk" list="#{'DATA ENTRY MISTAKE':'DATA ENTRY MISTAKE','OTHER':'OTHER'}" />
			</P>
			<P align="center">
				<input type="button" class="buttonadd"
					value="Cancel Tender File" id="addButton"
					name="cancelTF" onclick="cancelTenderFile();"
					align="center" />
			</P>
		</s:if>
	 </s:if> 
	 <s:elseif test="%{searchResult.fullListSize == 0}">
		  <div>
			<table width="100%" border="0" cellpadding="0" 	cellspacing="0">
				<tr>
				   <td align="center">
					 <font color="red"><s:text name="search.result.no.recorod" /></font>
				   </td>
			    </tr>
			</table>
		  </div>
	</s:elseif>   
 </div>
<script>
<s:if test="%{sourcepage=='cancelTenderFile'}">
	dom.get("egwStatus").disabled=true;
</s:if>
</script>