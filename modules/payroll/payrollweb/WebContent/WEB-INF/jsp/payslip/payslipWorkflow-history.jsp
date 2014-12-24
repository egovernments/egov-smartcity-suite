<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 

<table width="100%" cellpadding ="0" cellspacing ="0" border = "0">
<tbody>
  <tr>&nbsp;
   <td colspan="20" class="headingwk"><div class="arrowiconwk"><img src="${pageContext.request.contextPath}/common/image/arrow.gif" /></div>
   <p><div class="headplacer">Workflow History</div></p></td><td></td> 
  </tr>
   </tbody>
</table>

<s:if test="%{stateValue!=null}">
	 <c:import url="/WEB-INF/jsp/workflow/workflowHistory.jsp" context="/egi">
	<c:param name="stateId" value="${stateValue}"></c:param>
	</c:import>
</s:if>