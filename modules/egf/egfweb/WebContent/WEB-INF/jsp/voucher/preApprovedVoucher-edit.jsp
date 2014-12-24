<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<s:if test="%{methodName=='save'}">
	<%@ include file='preApprovedVoucher-billview.jsp'%>
</s:if>
<s:elseif test="%{methodName=='update'}">
	<%@ include file='preApprovedVoucher-voucherview.jsp'%>
</s:elseif>
<s:else>
	<%@ include file='preApprovedVoucher-voucheredit.jsp'%>
</s:else>

