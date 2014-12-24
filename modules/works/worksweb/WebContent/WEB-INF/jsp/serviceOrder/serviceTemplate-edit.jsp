<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ page language="java"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
	<title>Modify Service Template</title>
</head>

<body class="yui-skin-sam">
	<s:if test="%{hasErrors()}">
		<div class="errorstyle">
			<s:actionerror />
			<s:fielderror />
		</div>
	</s:if>
<div id="jserrorid" class="errorstyle" style="display:none" >
	<p class="error-block" id="lblError" ></p>
</div>
	<s:if test="%{hasActionMessages()}">
		<div class="messagestyle">
			<s:actionmessage theme="simple" />
		</div>
	</s:if>

	<s:form action="serviceTemplate" theme="simple"name="serviceTemplateForm">
		<s:push value="model">
		
			<table id="formTable" width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td>
						&nbsp;
					</td>
				</tr>
				<tr>
					<td>
						<table  width="100%" border="0"
							cellspacing="0" cellpadding="0">
							<tr>
								<td colspan="4" class="headingwk">
									<div class="arrowiconwk">
										<img
											src="${pageContext.request.contextPath}/image/arrow.gif" />
									</div>
									<div class="headplacer" align="left">
										Template Details
									</div>
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<table width="100%">
					<tr>
						<td width="25%" class="whiteboxwk"> Template Code <span class="mandatory">*</span></td>
						<td width="25%" class="whitebox2wk"><s:textfield id="templateCode" name="templateCode"></s:textfield> </td>						<script> dom.get('templateCode').readOnly = true;</script>
						<td width="25%" class="whiteboxwk"> Template Name <span class="mandatory">*</span></td>
						<td width="25%" class="whitebox2wk"> <s:textfield id="templateName" name="templateName"></s:textfield></td>
					</tr>
					<tr>
						<td width="25%" class="greyboxwk"> Template Description <span class="mandatory">*</span></td>
						<td width="25%" class="greybox2wk"><s:textfield id="templateDesc" name="templateDesc"> </s:textfield> </td>
						<td width="25%" class="greyboxwk"> Is Active <span class="mandatory">*</span></td>
						<td width="25%" class="greybox2wk"><s:checkbox name="isActive" id="isActive" ></s:checkbox> </td>
					</tr>
				</table>
				</table>
				<br><br>
				 <div class="rbroundbox2">
				<jsp:include page='templateActivity.jsp' />
				<s:hidden name="templateId" id="templateId" value="%{id}"/> 
					
				</div>
			<br>
			<table width="100%">
							
							<tr>
								<td colspan="4">
									<div class="buttonholderwk">
									<s:submit type="submit" id="save" value="SAVE" cssClass="buttonfinal" method="edit" />
										
										<input type="button" class="buttonfinal" value="CLOSE"
											id="closeButton" name="button" onclick="window.close();" />
									</div>
								</td>
							</tr>
							
				</table>
				</s:push>
	</s:form>

<script>
	

	for(var i=0;i<document.forms[0].length;i++){
		if(document.forms[0].elements[i].name !='isActive' && document.forms[0].elements[i].value !='SAVE' 
			&& document.forms[0].elements[i].value !='CLOSE' && document.forms[0].elements[i].name !='templateId'){
			document.forms[0].elements[i].disabled =true;
		}
	}

</script>
</body>
</html>