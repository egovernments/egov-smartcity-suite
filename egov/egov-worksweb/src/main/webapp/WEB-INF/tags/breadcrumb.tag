<%@ tag body-content="empty"  isELIgnored="false" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="now" value="<%=new java.util.Date()%>" />
<div class='commontopyellowbg'>Works Management System</div>
<div class='commontopbluebg'><div class='commontopdate'>
Today is: <span class='bold' style='color:black'><fmt:formatDate value="${now}" pattern="dd/MM/yyyy"/></span>
</div>
Welcome <span class='bold' style='color:#cccccc'>${username}</span></div>
<div class='commontopbreadc' id='breadcrumb'>&nbsp;</div>

	
 