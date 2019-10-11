<%--
  ~    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) 2018  eGovernments Foundation
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
  ~            Further, all user interfaces, including but not limited to citizen facing interfaces,
  ~            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
  ~            derived works should carry eGovernments Foundation logo on the top right corner.
  ~
  ~            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
  ~            For any further queries on attribution, including queries on brand guidelines,
  ~            please contact contact@egovernments.org
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
  ~
  --%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn" %>

<script src="<cdn:url value='/resources/global/js/bootstrap/bootbox.min.js' context='/egi'/>"></script>

<style>
td.bluebox {
    padding: 10px;
}
table {
    max-width: 960px;
    margin: 0 auto;
    border: 0;
}
#the-form {
    width: 850px;
    margin: 0 auto;
}
#approverIdList { height: initial; }
.mandatorycoll {
	font-weight: normal;
    color: #cc0000;
    font-size: 11px;
}

.error-area {
    margin: 12px;
    font-size: 11px;
    color: #F00;
    text-align: center;
    padding: 10px;
    background-color: #FFFFEE;
    font-family: semibold;
    border: 1px solid #F00;
}
.error-msg {
	color: #cc0000;
}

</style>

<script>

    var originalValue = {
        approverId: '<c:out value="${mapspec.approverId}" />',
        remitterId: '<c:out value="${mapspec.remitterId}" />',
        isActive: '<c:out value="${mapspec.isActive}" />',
        approverIdList: ''
    };
	
    function resetValues() {
        jQuery.each(originalValue, function(k,v) {
            if(v)
                jQuery('#' + k).val(v);
            else
                jQuery('#' + k).val('');
        })
    }

    function checkForm(event) {
    	var missingFields = [];
        jQuery("select").each(function(idx, element) {
        	var $element = $(element);
        	var val = $element.val();
        	if(element.disabled) {
        	    return;
        	}
        	if(!val || val.length === 0 || (jQuery.isArray(val) && val.indexOf("") !== -1)) {
        		missingFields.push($element.parent().prev().text());
        	}
        });
        if(missingFields.length > 0) {
            event.preventDefault();
            var missingFieldLbl = "";
            for(var i = 0; i < missingFields.length; i++){
                if(i > 0){
                    missingFieldLbl += ", ";
                }
                missingFieldLbl += missingFields[i];
            }
        	bootbox.alert("Please select " + missingFieldLbl);
        }
    }
    function onSubmitUpdate(event) {
    	checkForm(event);
    }
	function onSubmitCreate(event) {
		checkForm(event);
    	$("#the-form").attr("action", "create");
	}

</script>

<form:form id="the-form" modelAttribute="mapspec" method="post" role="form">

    <div class="formmainbox">
        <div class="subheadnew">
             <c:if test='${mode == "MODIFY"}'>
                <spring:message code="form.title.approverRemitterMapping.modify" />
            </c:if>
            <c:if test='${mode != "MODIFY"}'>
                <spring:message code="form.title.approverRemitterMapping.create" />
            </c:if>
        </div>

        <spring:bind path="mapspec">
            <c:if test="${status.error == true}">
                <div class="error-area">
                    <ul>
                        <form:errors element="li" />
                    </ul>
                </div>
            </c:if>
        </spring:bind>

        <table width="100%">
            <tr>
                <td class="bluebox">&nbsp;</td>
                <td class="bluebox"><spring:message code="lbl.approver" /><span class="mandatory"></span></td>
                <td class="bluebox">
                    <c:if test='${mode == "CREATE"}'>
                        <form:select path="approverIdList" value="${approverId}" multiple="true" size="10">
                            <form:option value="">
                                <spring:message code="lbl.select" />
                            </form:option>
                             <optgroup label='<spring:message code="lbl.free.approver" />'>
                                <form:options items="${unmappedApproverList}" itemValue="id" itemLabel="name" />
                             </optgroup>
                             <optgroup label='<spring:message code="lbl.mapped.approver" />' disabled="true">
                                <form:options items="${activeApproverList}" itemValue="id" itemLabel="name" />
                             </optgroup>
                        </form:select>
                        <form:errors path="approverIdList" cssClass="error-msg"/>
                    </c:if>

                    <c:if test='${mode == "MODIFY"}'>
                    	<%-- <form:hidden path="mappingId" value="${id}" /> --%>
                    	<input type="hidden" name="id" value="${mapspec.id}" />
                    	<form:hidden path="approverId" value="${approverId}" />
                        <form:select path="approverId" value="${approverId}" disabled="true">
                            <form:option value="">
                                <spring:message code="lbl.select.option" />
                            </form:option>
                             <optgroup label='<spring:message code="lbl.current.approver" />'>
                                <form:option value="${mapspec.approverId}">
                                    <c:out value="${mapspec.approverName}" />
                                </form:option>
                             </optgroup>
                             <optgroup label='<spring:message code="lbl.free.approver" />'>
                                <form:options items="${unmappedApproverList}" itemValue="id" itemLabel="name" />
                             </optgroup>
                             <optgroup label='<spring:message code="lbl.mapped.approver" />' disabled="true">
                                <form:options items="${activeApproverList}" itemValue="id" itemLabel="name" />
                             </optgroup>
                        </form:select>
                        <form:errors path="approverId" cssClass="error-msg"/>
                    </c:if>
                </td>
                <td class="bluebox"><spring:message code="lbl.remitter" /><span class="mandatory"></span></td>
                <td class="bluebox">
                    <form:select path="remitterId" value="${remitterId}">
                        <form:option value="">
                            <spring:message code="lbl.select.option" />
                        </form:option>
                        <form:options items="${remitterList}" itemValue="id" itemLabel="name" />
                    </form:select>
                    <form:errors path="remitterId" cssClass="error-msg"/>

                </td>
            </tr>
            <tr>
                <td class="bluebox">&nbsp;</td>
                <td class="bluebox"><spring:message code="lbl.status" /> <span class="mandatory"></span></td>
                <td class="bluebox" colspan="3">
                    <form:select path="isActive" value="${isActive}">
                        <form:option value="">
                            <spring:message code="lbl.select.option" />
                        </form:option>
                        <form:option value="true">
                            <spring:message code="lbl.active" />
                        </form:option>
                        <form:option value="false">
                            <spring:message code="lbl.inactive" />
                        </form:option>
                    </form:select>
                    <form:errors path="isActive" cssClass="error-msg"/>
                    
                </td>
            </tr>
        </table>
        <div align="left" class="mandatorycoll">
            &nbsp;&nbsp;&nbsp;
            <spring:message code="lbl.common.mandatoryfields" />
        </div>
        <br />
    </div> 

    <div class="buttonbottom">
    	<c:if test='${mode == "MODIFY"}'>
    		<input type="submit" class="buttonsubmit"
	            onclick="return onSubmitUpdate(event);"
	            value='<spring:message code="lbl.update.mapping" />' />
    	</c:if>
    	<c:if test='${mode == "CREATE"}'>
    		<input type="submit" class="buttonsubmit"
	            onclick="return onSubmitCreate(event);"
	            value='<spring:message code="lbl.create.mapping" />' />
    	</c:if>
        
        <input type="button" class="button" value='<spring:message code="lbl.reset" />'
            id="resetbutton" name="clear" onclick="resetValues();">
        <input name="close" type="button" class="button"
            onclick="window.close()" value='<spring:message code="lbl.close" />' />
    </div>
</form:form>