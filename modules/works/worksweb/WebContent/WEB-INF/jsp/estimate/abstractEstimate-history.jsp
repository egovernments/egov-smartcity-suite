<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<s:if test="%{stateValue!=null}">
     <c:import url="/WEB-INF/jsp/workflow/workflowHistory.jsp" context="/egi">
     <c:param name="stateId" value="${stateValue}"></c:param>
</c:import>
 </s:if>