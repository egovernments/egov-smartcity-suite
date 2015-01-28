<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<!DOCTYPE html>
<html>
	<head>
	    <meta charset="utf-8">
	    <meta http-equiv="X-UA-Compatible" content="IE=edge">
	    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
	    <meta name="description" content="eGov ERP System" />
	    <meta name="author" content="eGovernments Foundation" />
	
	    <title><tiles:insertAttribute name="title"/></title>
	
	    <link rel="stylesheet" href="<c:url value='/resources/theme/js/jquery-ui/css/no-theme/jquery-ui.css'/>">
	    <link rel="stylesheet" href="<c:url value='/resources/theme/css/font-icons/entypo/css/entypo.css'/>">
	    <link rel="stylesheet" href="<c:url value='/resources/theme/css/bootstrap/bootstrap.css'/>">
	    <link rel="stylesheet" href="<c:url value='/resources/theme/css/neon/neon-core.css'/>">
	    <link rel="stylesheet" href="<c:url value='/resources/theme/css/neon/neon-theme.css'/>">
	    <link rel="stylesheet" href="<c:url value='/resources/theme/css/neon/neon-forms.css'/>">
	    <link rel="stylesheet" href="<c:url value='/resources/theme/css/egov/custom.css'/>">
	
	    <script src="<c:url value='/resources/theme/js/jquery/jquery.js'/>"></script>
	    <script src="<c:url value='/resources/pgr/js/complaint-type.js'/>"></script>
	
	    <!--[if lt IE 9]><script src="<c:url value='/resources/theme/js/ie8-responsive-file-warning.js'/>"></script><![endif]-->
	
	    <!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
	    <!--[if lt IE 9]>
	    <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
	    <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
	    <![endif]-->
	
	</head>
    <body class="page-body">
        <div class="page-container horizontal-menu">
            <tiles:insertAttribute name="header"/>
                <div class="main-content">
                    <tiles:insertAttribute name="body"/>
                    <tiles:insertAttribute name="footer"/>
                </div>

        </div>
        <!-- Bottom scripts (common) -->
        <script src="<c:url value='/resources/theme/js/gsap/main-gsap.js'/>"></script>
        <script src="<c:url value='/resources/theme/js/jquery-ui/js/jquery-ui.js'/>"></script>
        <script src="<c:url value='/resources/theme/js/bootstrap/bootstrap.js'/>"></script>
        <script src="<c:url value='/resources/theme/js/neon/joinable.js'/>"></script>
        <script src="<c:url value='/resources/theme/js/neon/resizeable.js'/>"></script>
        <script src="<c:url value='/resources/theme/js/neon/neon-api.js'/>"></script>
        <script src="<c:url value='/resources/theme/js/jquery/jquery.inputmask.bundle.min.js'/>"></script>
        <script src="<c:url value='/resources/theme/js/neon/neon-custom.js'/>"></script>
        <script src="<c:url value='/resources/theme/js/jquery/jquery.validate.min.js'/>"></script>
    </body>
</html>