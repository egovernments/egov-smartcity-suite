<%@ include file="/includes/taglibs.jsp"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="org.egov.ptis.constants.PropertyTaxConstants" %>


<html>
	<head>
		<title><s:text name="objectionView.det"></s:text></title>
		<link href="<c:url value='/css/headertab.css'/>" rel="stylesheet" type="text/css" />
	</head>
	<body class="yui-skin-sam">
	<s:push value="model">
			<jsp:include page="objectionDetailsCommonView.jsp"/>
	  <div class="buttonbottom" align="center">
	  	<table>
		<tr>
					<s:if test="egwStatus.code.equalsIgnoreCase(@org.egov.ptis.constants.PropertyTaxConstants@OBJECTION_ACCEPTED)">
					<td>
					<input type="button" name="memoButton" id="memoButton" value="Generate Memo" class="button" 
					onclick="window.open('${pageContext.request.contextPath}/objection/memoGeneration!print.action?objection.id=<s:property value="%{id}"/>','dataitem','resizable=yes,scrollbars=yes,height=700,width=800,status=yes');"/>
					</s:if>
					</td>
					<td><input type="button" name="printButton" id="printButton" value="Print" class="button" onclick="window.print();"/></td>
		    		<td><input type="button" name="closeButton" id="closeButton" value="Close" class="button" onclick="window.close();"/></td>
		</tr>             
		</table></div>    
	</s:push>  
</body>
</html>