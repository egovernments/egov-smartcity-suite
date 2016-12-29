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
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn" %>
<div class="col-md-12">
<div class="form-group view-content header-color hidden-xs">
	<div class="col-sm-1"><s:text name="doctable.sno" /></div>
    <div class="col-sm-3"><s:text name="doctable.docname" /></div>
    <div class="col-sm-4"><s:text name="doctable.attach.doc" /></div>
    <div class="col-sm-3"><s:text name="license.remarks" /></div>
</div>
<s:iterator value="documentTypes" status="stat" var="documentType">
	<div class="form-group">
    	<div class="col-sm-1">
            <s:property value="#stat.index + 1"/>
        </div>
        <div class="col-sm-3">
        	<span class="docname"><s:property value="name" /></span>
            <s:if test="mandatory">
                <span class="mandatory"></span>
            </s:if>
            <s:hidden name="documents[%{#stat.index}].type.id" value="%{id}"/>
            <s:hidden name="documents[%{#stat.index}].type.name" value="%{name}"/>
		</div>
       	<div class="col-sm-4">
            <s:file name="documents[%{#stat.index}].uploads" id="uploadFile%{#stat.index}" value="%{documents[#stat.index].uploads}" cssClass="file-ellipsis upload-file"/>
            <script>
                
                <c:if test="${mandatory && empty documents[stat.index].files}">
                    jQuery('#uploadFile${stat.index}').attr('required', true);
                </c:if>
            </script>
            <s:iterator value="%{documents[#stat.index].files}">
                <a href="javascript:viewDocument('<s:property value="fileStoreId"/>')">
                    <s:property value="%{fileName}"/>
                </a>
            </s:iterator>
			<form:errors path="documents[%{#stat.index}].files" cssClass="add-margin error-msg" />
       	</div>
        <div class="col-sm-3">
            <s:textarea name="documents[%{#stat.index}].description" cssClass="form-control" value="%{documents[#stat.index].description}"/>
        </div>
	</div>
</s:iterator>
</div>

<script src="<cdn:url  value='/resources/js/app/documentupload.js?rnd=${app_release_no}'/>"></script>