<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ page language="java"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>

<%@ page import =  "ChartDirector.*"%>
<html>

<head>
	<title><s:text name="header.pieChart" /></title>

</head>

<body class="yui-skin-sam">
	<s:if test="%{hasErrors()}">
		<div class="errorstyle">
			<s:actionerror />
			<s:fielderror />
		</div>
	</s:if>
	<s:form theme="simple" name="groupWiseBoundaryLevelReportForm" action="groupWiseBoundaryLevelReport" method="post">
	
			
				 
				<s:if test="%{ null!=complaintGroups && complaintGroups.length >0 }">
						 <img src="/pgr/common/jsp/getchart.jsp?<s:property value="%{chartURL}"/>" usemap="#map1" border="0">

          				  <!-- the following will display tool tip if we move cursor on image    -->
          				
           				 <map name="map1"> <s:property value="%{imageMap}" escape="#{<}"/> </map>
				
				</s:if>
				<s:else>
						No complaints registered
				</s:else>
			
			
				
				
	</s:form>
	


</body>
</html>