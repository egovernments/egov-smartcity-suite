<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn" %>

<script src="<cdn:url value='/resources/global/js/bootstrap/bootbox.min.js' context='/egi'/>"></script>

<style>
section {
	text-align: center;
}
.error-area, 
.error-area li{
	color: red;
	font-size: 16px;
	font-weight: bold;
	margin-top: 12px;
}
.error-area {
	margin-bottom: 24px;
}

.error-area {
    margin: 12px;
    background: #ffe9e3;
    padding: 12px;
}

#success-msg {
	color: green;
	font-size: 16px;
	font-weight: bold;
	margin-top: 24px;
	padding: 12px;
}
</style>


<section>
    <c:if test='${not empty errors}'>
        <ul class="error-area list-unstyled">
            <c:forEach items="${errors}" var="error">
                <li>${error}</li>
            </c:forEach>
        </ul>
    </c:if>

    <c:if test='${not empty successMsg}'>
        <div id="success-msg">
            <c:out value="${successMsg}" />
        </div>
    </c:if>
    <div class="buttonbottom">
        <input name="close" type="button" class="button"
            onclick="window.close();" value='<spring:message code="lbl.close" />' />
    </div>
</section>