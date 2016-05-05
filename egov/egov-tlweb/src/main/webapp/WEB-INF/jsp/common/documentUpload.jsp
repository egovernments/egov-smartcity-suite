<%--
  ~ eGov suite of products aim to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) <2015>  eGovernments Foundation
  ~
  ~     The updated version of eGov suite of products as by eGovernments Foundation
  ~     is available at http://www.egovernments.org
  ~
  ~     This program is free software: you can redistribute it and/or modify
  ~     it under the terms of the GNU General Public License as published by
  ~     the Free Software Foundation, either version 3 of the License, or
  ~     any later version.
  ~
  ~     This program is distributed in the hope that it will be useful,
  ~     but WITHOUT ANY WARRANTY; without even the implied warranty of
  ~     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  ~     GNU General Public License for more details.
  ~
  ~     You should have received a copy of the GNU General Public License
  ~     along with this program. If not, see http://www.gnu.org/licenses/ or
  ~     http://www.gnu.org/licenses/gpl.html .
  ~
  ~     In addition to the terms of the GPL license to be adhered to in using this
  ~     program, the following additional terms are to be complied with:
  ~
  ~         1) All versions of this program, verbatim or modified must carry this
  ~            Legal Notice.
  ~
  ~         2) Any misrepresentation of the origin of the material is prohibited. It
  ~            is required that all modified versions of this material be marked in
  ~            reasonable ways as different from the original version.
  ~
  ~         3) This license does not grant any rights to any user of the program
  ~            with regards to rights under trademark law for use of the trade names
  ~            or trademarks of eGovernments Foundation.
  ~
  ~   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
  --%>

<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/includes/taglibs.jsp"%>
<script>
	function viewDocument(fileStoreId) {
		var sUrl = "/egi/downloadfile?fileStoreId="+fileStoreId+"&moduleName=EGTL";
		window.open(sUrl,"window",'scrollbars=yes,resizable=no,height=400,width=400,status=yes');	
	}
	
	// checklist should be checked before attaching document
	function verifyChecklist(obj){
		var rowobj=getRow(obj);
		var tbl = document.getElementById('docAttachmentTab');
		var checkListval=getControlInBranch(tbl.rows[rowobj.rowIndex],'checklist').checked;
		if(checkListval!=true){
			bootbox.alert("Please Check the Check List before attaching.");
			return false; 
		} 
	}
	
	// Clear attached document on unselection of checklist
	function checkFileAttachment(obj){
		var rowobj=getRow(obj);
		var tbl = document.getElementById('docAttachmentTab');
		var checkListval=getControlInBranch(tbl.rows[rowobj.rowIndex],'checklist').checked;
		if(checkListval!=true && getControlInBranch(tbl.rows[rowobj.rowIndex],'uploadFile').value!=''){
			var r = confirm("Unselecting Check List will clear the Document attached!");
			if(r==true)
				getControlInBranch(tbl.rows[rowobj.rowIndex],'uploadFile').value='';
			else{
				getControlInBranch(tbl.rows[rowobj.rowIndex],'checklist').checked=true;
			}
		} 
	}  
</script>
<div class="panel-heading custom_form_panel_heading">
    <div class="panel-title">Enclosed Documents</div>
</div>
<div class="form-group col-sm-12 view-content header-color hidden-xs">
	<div class="col-sm-1 text-center"><s:text name="doctable.sno" /></div>
    <div class="col-sm-5 text-center"><s:text name="doctable.docname" /></div>
    <div class="col-sm-3 text-center"><s:text name="doctable.checklist"/></div>
    <div class="col-sm-3 text-center"><s:text name="doctable.attach.doc" /></div>	
</div>
<table class="table" id="docAttachmentTab">  
<s:iterator value="documentTypes" status="status" var="documentType">
	<div class="form-group">
    	<div class="col-sm-1 text-center"><s:property value="#status.index + 1"/></div>
        <div class="col-sm-5 text-center">
        	<span class="docname"><s:property value="name" /></span><s:if test="mandatory"><span class="mandatory"></span></s:if>
			<s:hidden name="documents[%{#status.index}].type.id" value="%{id}"/>
		</div>
       	<div class="col-sm-3 text-center">
       		<s:if test="mandatory">
       			<s:checkbox name="documents[%{#status.index}].enclosed" id="checklist" onclick="checkFileAttachment(this);" required="true"/>
       		</s:if>
       		<s:else>
       			<s:checkbox name="documents[%{#status.index}].enclosed" id="checklist" onclick="checkFileAttachment(this);" />
       		</s:else>
       	</div>
       	<div class="col-sm-3 text-center">
       		<s:if test="%{documents.isEmpty()}">
       			<s:if test="mandatory">
					<s:file name="documents[%{#status.index}].uploads" id="uploadFile" value="%{documents[#status.index].uploads}" onclick="return verifyChecklist(this);" cssClass="file-ellipsis upload-file" required="true"/>
				</s:if>
				<s:else>
					<s:file name="documents[%{#status.index}].uploads" id="uploadFile" value="%{documents[#status.index].uploads}" onclick="return verifyChecklist(this);" cssClass="file-ellipsis upload-file"/>
				</s:else>
			</s:if>
			<s:elseif test="%{documents[#status.index].files.isEmpty()}">
				<s:hidden name="documents[%{#status.index}].id"/>
				<s:if test="mandatory">
					<s:file name="documents[%{#status.index}].uploads" id="uploadFile" value="%{documents[#status.index].uploads}" onclick="return verifyChecklist(this);" cssClass="file-ellipsis upload-file" required="true"/>
				</s:if>
				<s:else>
					<s:file name="documents[%{#status.index}].uploads" id="uploadFile" value="%{documents[#status.index].uploads}" onclick="return verifyChecklist(this);" cssClass="file-ellipsis upload-file"/>
				</s:else>
			</s:elseif>
			<s:else>
				<s:iterator value="%{documents[#status.index].files}">
					<s:hidden name="documents[%{#status.index}].id"/>
					<a href="javascript:viewDocument('<s:property value="fileStoreId"/>')"> 
 						<s:property value="%{fileName}"/>
					</a> 
				</s:iterator>	
			</s:else>
			<form:errors path="documents[%{#status.index}].files" cssClass="add-margin error-msg" />
			<%-- <div class="add-margin error-msg text-left" ><font size="2"><s:text name="lbl.mesg.document"/></font></div> --%>
       	</div>
   	</div>
</s:iterator>
</table>

<script src="<c:url value='/resources/js/app/documentupload.js?rnd=${app_release_no}'/>"></script> 