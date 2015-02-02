<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<header class="navbar navbar-fixed-top">
    <div class="navbar-inner">
        <div class="navbar-brand">
            <a href="<c:url value='/index'/>">
                <img src="<c:url value='/resources/global/images/logo@2x.png'/>"/>
            </a>
        </div>
        <h2 class="horizontal-page-title">
            <c:set var="titleKey">
                <tiles:getAsString name="page-title"/>
            </c:set>
			<spring:message code="${titleKey}"/>
        </h2>
    </div>
</header>