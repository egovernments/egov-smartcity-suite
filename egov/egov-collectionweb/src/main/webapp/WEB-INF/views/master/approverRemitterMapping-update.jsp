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
.error-area, 
.error-area li,
.error-area span {
	color: red;
	font-size: 16px;
	font-weight: bold;
	margin-top: 12px;
}
.error-area {
    margin: 12px;
    background: #ffe9e3;
    padding: 12px;
}
.error-msg {
	color: #cc0000;
}

</style>

<script>

    var originalValue = {
        approverId: '<c:out value="${approverRemitterMappingSpec.approverId}" />',
        remitterId: '<c:out value="${approverRemitterMappingSpec.remitterId}" />',
        isActive: '<c:out value="${approverRemitterMappingSpec.isActive}" />'
    };
	
    function resetValues() {
        jQuery.each(originalValue, function(k,v) {
            if(v)
                jQuery('#' + k).val(v);
            else
                jQuery('#' + k).val("");
        })
    }

    function checkForm(event) {
    	var missingFields = [];
        jQuery("select").each(function(idx, element) {
        	var $element = $(element);
        	var val = $element.val();
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
    	var mappingID = $('mapping-id').val();
    	$("the-form").attr("action", "edit/" + mappingID);
    }
	function onSubmitCreate(event) {
		checkForm(event);
    	$("the-form").attr("action", "create");
	}

</script>

<form:form id="the-form" modelAttribute="approverRemitterMappingSpec" method="post" role="form">

	<form:hidden id="mapping-id" path="id" value="${id}" />

    <div class="formmainbox">
        <div class="subheadnew">
             <c:if test='${mode == "MODIFY"}'>
                <spring:message code="form.title.approverRemitterMapping.modify" />
            </c:if>
            <c:if test='${mode != "MODIFY"}'>
                <spring:message code="form.title.approverRemitterMapping.create" />
            </c:if>
        </div>

        <c:if test="${not empty errors}">
            <ul class="error-area list-unstyled">
                <c:forEach items="${errors}" var="error">
                    <li>${error}</li>
                </c:forEach>
            </ul>
        </c:if>

        <form:errors element="div" cssClass="error-area add-margin error-msg"/>

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
                                <form:options items="${freeApproverList}" itemValue="id" itemLabel="name" />
                             </optgroup>
                             <optgroup label='<spring:message code="lbl.mapped.approver" />' disabled="true">
                                <form:options items="${activeMappedApproverList}" itemValue="id" itemLabel="name" />
                             </optgroup>
                        </form:select>
                        <form:errors path="approverIdList" cssClass="error-msg"/>
                    </c:if>

                    <c:if test='${mode == "MODIFY"}'>
                        <form:select path="approverId" value="${approverId}">
                            <form:option value="">
                                <spring:message code="lbl.select.option" />
                            </form:option>
                             <optgroup label='<spring:message code="lbl.current.approver" />'>
                                <form:option value="${approverRemitterMappingSpec.approverId}">
                                    <c:out value="${approverRemitterMappingSpec.approverName}" />
                                </form:option>
                             </optgroup>
                             <optgroup label='<spring:message code="lbl.free.approver" />'>
                                <form:options items="${freeApproverList}" itemValue="id" itemLabel="name" />
                             </optgroup>
                             <optgroup label='<spring:message code="lbl.mapped.approver" />' disabled="true">
                                <form:options items="${activeMappedApproverList}" itemValue="id" itemLabel="name" />
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
    </div> <%-- .formmainbox --%>

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