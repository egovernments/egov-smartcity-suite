<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
  "http://www.w3.org/TR/html4/loose.dtd">  
  <style type="text/css">

#yui-dt0-bodytable,#yui-dt1-bodytable,#yui-dt2-bodytable {
	Width: 100%;
}

</style>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() +"/build/button/assets/button.css" %>" type="text/css">
<script src="<egov:url path='js/assetRevaluation.js'/>"></script>	

<html>
	<head> <title>- <s:text name="page.title.assset.revaluation" /> - View</title></head> 
	<body id="home" >

		<s:if test="%{hasErrors()}">
			<div class="errorstyle">
				<s:actionerror />
				<s:fielderror />
			</div>
		</s:if>
		<s:if test="%{hasActionMessages()}">
			<font  style='color: green ; font-weight:bold '> 
     					<s:actionmessage/>
   				</font>
		</s:if>
		
<s:form action="assetRevaluationActivity" theme="simple" name="assetRevalActForm">
			<div class="errorstyle" id="asset_error" style="display:none;"></div>
			<s:push value="model">
				<div class="navibarshadowwk">
				</div>
				<div class="formmainbox">
					<div class="insidecontent">
						<div class="rbroundbox2">
							<div class="rbtop2"><div></div>
							</div>
							<div class="rbcontent2">
								<div class="datewk">
									<span class="bold">Today</span>
									<egov:now />
								</div>
								
								<jsp:include page="assetRevaluationActivity-form.jsp" />			
		<div id="wfHistoryDiv">
			<s:if test="%{assetRevaluation.state.id!=null}">
			  	<c:import url="/WEB-INF/jsp/workflow/workflowHistory.jsp" context="/egi">
			        <c:param name="stateId" value="${assetRevaluation.state.id}"></c:param>
			    </c:import>
			 </s:if>
		 
	    
	</div>
			<table align="center">
			
				<tr>
					<td >
						<div class="buttonholderwk">
						<input type="button" class="buttonfinal" value="CLOSE" id="closeButton" name="button" onclick="window.close();" />
						</div>
					</td>
				</tr>
			</table>
			
			</div>
			<div class="rbbot2">
			<div></div>
			</div>
		</div>
	</div>
</div>		
</s:push>
</s:form>
<script>

	for(var i=0;i<document.forms[0].length;i++)
	{
		
		if(document.forms[0].elements[i].value != 'CLOSE'){
			document.forms[0].elements[i].disabled =true;
		}						
	}	


</script>
</body>
</html>