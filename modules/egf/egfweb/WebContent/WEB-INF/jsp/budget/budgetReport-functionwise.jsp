<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<html>  
<head>  
    <title>Budget Report - Functionwise</title>
</head> 
	<body>  
		<s:form action="budgetReport" theme="simple" >  
			<jsp:include page="../budget/budgetHeader.jsp">
        		<jsp:param name="heading" value="Budget Report - Functionwise" />
			</jsp:include>
			<div class="formmainbox"><div class="subheadnew">Budget Report - Functionwise</div>
				<%@include file="budgetReport-form.jsp" %>
				<div class="buttonbottom" style="padding-bottom:10px;">
					<s:submit value="Submit" method="getFunctionwiseReport" cssClass="buttonsubmit" onclick="return validateFinYear()"/>
					<input type="submit" value="Close" onclick="javascript:window.close()" class="button"/>     
				</div>
			</div>
		</s:form>  
	</body>  
</html>
