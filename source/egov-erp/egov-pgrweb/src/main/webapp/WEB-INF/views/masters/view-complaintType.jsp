<%@ page contentType="text/html" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<c:url var="firstUrl" value="/view-complaintType/1" />
<c:url var="lastUrl" value="/view-complaintType/${complaintTypePage.totalPages}" />
<c:url var="prevUrl" value="/view-complaintType/${currentIndex - 1}" />
<c:url var="nextUrl" value="/view-complaintType/${currentIndex + 1}" />
<body>
	<form:form class="form-horizontal form-groups-bordered">
		<%-- <div>
			<ul>
				<c:choose>
					<c:when test="${currentIndex == 1}">
						<a href="#">&lt;&lt;</a>
						<a href="#">&lt;</a>
					</c:when>
					<c:otherwise>
						<a href="${firstUrl}">&lt;&lt;</a>
						<a href="${prevUrl}">&lt;</a>
					</c:otherwise>
				</c:choose>
				<c:forEach var="i" begin="${beginIndex}" end="${endIndex}">
					<c:url var="pageUrl" value="/view-complaintType/${i}" />
					<c:choose>
						<c:when test="${i == currentIndex}">
							<a href="${pageUrl}"><c:out value="${i}" /></a>
						</c:when>
						<c:otherwise>
							<a href="${pageUrl}"><c:out value="${i}" /></a>
						</c:otherwise>
					</c:choose>
				</c:forEach>
				<c:choose>
					<c:when test="${currentIndex == complaintTypePage.totalPages}">
						<a href="#">&gt;</a>
						<a href="#">&gt;&gt;</a>
					</c:when>
					<c:otherwise>
						<a href="${nextUrl}">&gt;</a>
						<a href="${lastUrl}">&gt;&gt;</a>
					</c:otherwise>
				</c:choose>
			</ul>
		</div> --%>
		<div 	class="panel-title,add-margin">
			<display:table name="list" uid="currentRowObject" pagesize="2"
				sort="list" cellpadding="0" cellspacing="0" export="false"
				style="background-color:#e8edf1;width:98%;height:140px;padding:0px;margin:10 0 0 5px;"
				requestURI="">
				<display:caption style="width:98%;height:20px;padding:0px;margin:10 0 0 5px;">
				<strong>Total Complaint Types found</strong></display:caption>
				<display:column style="width:5%" title="Sl No"
					class="panel panel-primary,panel-heading,panel-title">
					<c:out value="${currentRowObject_rowNum+ (page-1)*pageSize}" />
				</display:column>
				<display:column property="name"	class="panel panel-primary,panel-heading,panel-title" title="Name" />
				<display:column property="department.deptName"	class="panel panel-primary,panel-heading,panel-title"
					title="Department" />
				<display:column property="locationRequired" class="panel panel-primary,panel-heading,panel-title"
					title="Location Required" />
			</display:table>
		</div>
	</form:form>
</body>
</html>