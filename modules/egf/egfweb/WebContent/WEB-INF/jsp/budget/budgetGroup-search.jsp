<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-nested.tld" prefix="nested"%>
<html>  
<head>  
    <title><s:text name="budgetgroup.search"/></title>
</head>
	<body>  
		<s:form action="budgetGroup" theme="simple" >
			<jsp:include page="budgetHeader.jsp"/>
			<div class="formmainbox"><div class="subheadnew"><s:text name="budgetgroup.search"/></div>
			<table align="center" width="100%">
				<tr>
					<td class="bluebox" width="15%"/>
					<td class="bluebox" width="10%"><s:text name="budgetgroup.groupname"/> </td>
					<td class="bluebox"><s:textfield name="name" id="name" value="%{budgetGroup.name}" maxlength="50" size="60"/></td>
				</tr>
				<s:hidden name="mode" value="%{mode}" id="mode"/>  
				<br/>
			</table>
			<div class="buttonbottom">
				<s:submit method="list" value="Search" cssClass="buttonsubmit" />
				<input type="submit" value="Close" onclick="javascript:window.close()" class="button"/>
			</div>
			<div id="listid" style="display:block">
				<div  style="float:left; width:100%;">
					<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="tablebottom">
			        <tr>  
			            <th class="bluebgheadtdnew"><s:text name="name"/></th>  
			            <th class="bluebgheadtdnew"><s:text name="budgetgroup.budgetingtype"/></th>
			            <th class="bluebgheadtdnew"><s:text name="budgetgroup.accounttype"/></th>  
			            <th class="bluebgheadtdnew"><s:text name="budgetgroup.majorcode"/></th>
			            <th class="bluebgheadtdnew"><s:text name="budgetgroup.mincode"/></th>
			            <th class="bluebgheadtdnew"><s:text name="budgetgroup.maxcode"/></th>  
			            <th class="bluebgheadtdnew"><s:text name="isactive"/></th>
			        </tr>  
				    <s:iterator var="p" value="budgetGroupList" status="1">  
				    <tr>  
						<td class="blueborderfortdnew">  
				            <a href="#" onclick="javascript:window.open('budgetGroup!edit.action?id=<s:property value='%{id}'/>&mode=<s:property value='%{mode}'/>','Search','resizable=yes,scrollbars=yes,left=300,top=40, width=900, height=700')"><s:property value="%{name}" /> </a> 
				        </td>
				        <td class="blueborderfortdnew">  
				            <s:property value="%{budgetingType}" />  
				        </td>
				        <td class="blueborderfortdnew">  
				            <s:property value="%{accountType}" />  
				        </td>
				        <td class="blueborderfortdnew">  
				            <s:property value="%{majorCode.glcode}" />  
				        </td>
				        <td class="blueborderfortdnew">  
				            <s:property value="%{minCode.glcode}" />  
				        </td>
				        <td class="blueborderfortdnew">  
				            <s:property value="%{maxCode.glcode}" />  
				        </td>
				        <td class="blueborderfortdnew">  
				            <s:if test="isActive ==true"><s:text name="yes"/></s:if><s:else><s:text name="no"/></s:else>
				        </td>
				    </tr>  
				    </s:iterator>
				    <s:hidden name="targetvalue" value="%{target}" id="targetvalue"/>  
					</table>  
				</div>
			</div>
			<br/>
			<div id="msgdiv" style="display:none">
				<table align="center" width="100%">
					<tr><td class="bluebgheadtd" colspan="7"><s:text name="no.data.found"/></td></tr>
				</table>
			</div>
			<br/>
			<br/>
		</s:form>  
		<script>
		//alert('hi=='+dom.get('target'));
		if(dom.get('targetvalue').value=='NONE')
		{
			dom.get('listid').style.display='none';
		}	
		else if(dom.get('targetvalue').value=='EMPTY')
		{
			dom.get('listid').style.display='none';
			dom.get('msgdiv').style.display='block';
		}
		</script>
	</body>  
</html>
