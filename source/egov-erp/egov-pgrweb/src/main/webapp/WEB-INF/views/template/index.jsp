<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>


<html>

<head>
    <meta charset="utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>

    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no"/>
    <meta name="description" content="Neon Admin Panel" />
    <meta name="author" content="" />

    <title>eGov Urban Portal</title>

    <link rel="stylesheet" href="<c:url value='/resources/theme/js/jquery-ui/css/no-theme/jquery-ui-1.10.3.custom.min.css'/>"/>
    <link rel="stylesheet" href="<c:url value='/resources/theme/css/font-icons/entypo/css/entypo.css'/>"/>
    <link rel="stylesheet" href="http://fonts.googleapis.com/css?family=Noto+Sans:400,700,400italic"/>
    <link rel="stylesheet" href="<c:url value='/resources/theme/css/bootstrap.css'/>"/>
    <link rel="stylesheet" href="<c:url value='/resources/theme/css/neon-core.css'/>"/>
    <link rel="stylesheet" href="<c:url value='/resources/theme/css/neon-theme.css'/>"/>
    <link rel="stylesheet" href="<c:url value='/resources/theme/css/neon-forms.css'/>"/>
    <link rel="stylesheet" href="<c:url value='/resources/theme/css/custom.css'/>"/>

    <script src="<c:url value='/resources/theme/js/jquery-1.11.0.min.js'/>"></script>


    <!--[if lt IE 9]><script src="<c:url value='/resources/theme/js/ie8-responsive-file-warning.js'/>"></script><![endif]-->

    <!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
    <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->

    <title><tiles:insertAttribute name="title"/></title>
</head>

    <body class="page-body">
        <div class="page-container horizontal-menu">
            <tiles:insertAttribute name="header"/>
                <div class="main-content">
                    <tiles:insertAttribute name="body"/>
                    <tiles:insertAttribute name="footer"/>
                </div>

        </div>
    </body>

    <link rel="stylesheet" href="<c:url value='/resources/theme/js/select2/select2-bootstrap.css'/>" />
    <link rel="stylesheet" href="<c:url value='/resources/theme/js/select2/select2.css'/>"/>
    <link rel="stylesheet" href="<c:url value='/resources/theme/js/selectboxit/jquery.selectBoxIt.css'/>"/>

    <!-- Bottom scripts (common) -->
    <script src="<c:url value='/resources/theme/js/gsap/main-gsap.js'/>"></script>
    <script src="<c:url value='/resources/theme/js/jquery-ui/js/jquery-ui-1.10.3.minimal.min.js'/>"></script>
    <script src="<c:url value='/resources/theme/js/bootstrap.js'/>"></script>
    <script src="<c:url value='/resources/theme/js/joinable.js'/>"></script>
    <script src="<c:url value='/resources/theme/js/resizeable.js'/>"></script>
    <script src="<c:url value='/resources/theme/js/neon-api.js'/>"></script>


    <script src="<c:url value='/resources/theme/js/jquery.inputmask.bundle.min.js'/>"></script>
    <script src="<c:url value='/resources/theme/js/select2/select2.min.js'/>"></script>
    <script src="<c:url value='/resources/theme/js/selectboxit/jquery.selectBoxIt.min.js'/>"></script>

    <!-- JavaScripts initializations and stuff -->
    <script src="<c:url value='/resources/theme/js/neon-custom.js'/>"></script>
    <script src="<c:url value='/resources/theme/js/jquery.validate.min.js'/>"></script>

</html>