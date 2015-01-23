<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>

<header class="navbar navbar-fixed-top">

    <div class="navbar-inner">

        <!-- logo -->
        <div class="navbar-brand">
            <a href="<c:url value='/index'/>">
                <img src="<c:url value='/resources/theme/images/logo@2x.png'/>"/>
            </a>
        </div>

        <h2 class="horizontal-page-title">
            <%--<tiles:insertAttribute name="page-title"/>--%>
            Add Complaint Type
        </h2>

    </div>

</header>