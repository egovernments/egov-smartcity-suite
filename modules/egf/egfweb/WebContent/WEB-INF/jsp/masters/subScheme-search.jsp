<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-nested.tld" prefix="nested"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>

<html>  
<head>  
    <title><s:if test="showMode == 'edit'">
			<s:text name="subscheme.modify.title"/>	    	
		</s:if>
		<s:else> <s:text name="masters.subscheme.search.title"/> </s:else>
	</title>
</head>
	<body>  
		<jsp:include page="../budget/budgetHeader.jsp"/>
		<s:actionmessage theme="simple"/>
		
		<div class="formmainbox">
		 
		<div class="subheadnew"><s:if test="showMode == 'edit'">
			<s:text name="subscheme.modify.title"/>	    	
		</s:if>
		<s:else><s:text name="masters.subscheme.search.title"/>	 </s:else>
		</div>
		</div>
		<s:actionerror/>  
		<s:fielderror />
		<s:form name="subSchemeForm" action="subScheme" theme="simple">
		
			<s:hidden name="showMode"  />
			
			<table align="center" width="100%" cellpadding="0" cellspacing="0">
				<tr>
					<td class="greybox">&nbsp</td>
					<td class="greybox">&nbsp</td>
					<td class="greybox">&nbsp</td>
					<td class="greybox">&nbsp</td>
				</tr>
				
				<tr>
					<td class="bluebox">
						<s:text name="masters.subscheme.search.fund" />
					</td>
					<td class="bluebox">
						<s:select list="dropdownData.fundList" listKey="id"
							listValue="name" name="fundId" headerKey="0"
							headerValue="---- Choose ----" onchange="loadScheme(this)"></s:select>
						<egov:ajaxdropdown fields="['Text','Value']"
							url="voucher/common!ajaxLoadSchemes.action" dropdownId="schemeId"
							id="schemeId" />
					</td>
					<td class="bluebox">
						<s:text name="masters.subscheme.search.scheme" />
					</td>

					<td class="bluebox">
						<s:select name="schemeId" id="schemeId"
							list="dropdownData.schemeList" headerKey="-1"
							headerValue="---- Choose ----" listKey="id" listValue="name"
							onchange="loadSubScheme(this)" />
					</td>
					<egov:ajaxdropdown fields="['Text','Value']"
						url="voucher/common!ajaxLoadSubSchemes.action"
						dropdownId="subSchemeId" id="subSchemeId" />

				</tr>
				<tr>
					<td class="greybox">
						<s:text name="masters.subscheme.search" />
					</td>
					<td class="greybox">
						<s:select name="subScheme.id" id="subSchemeId"
							list="dropdownData.subSchemeList" headerKey="-1"
							headerValue="---- Choose ----" listKey="id" listValue="name" />
					</td>
					<td class="greybox">&nbsp</td>
					<td class="greybox">&nbsp</td>
				</tr>

			</table>
			<div class="buttonbottom">
				<s:submit method="search" value="Search" cssClass="buttonsubmit" />
				<s:submit method="beforeSearch" value="Cancel"
					cssClass="buttonsubmit" />
				<input type="submit" value="Close"
					onclick="javascript:window.close()" class="button" />
			</div>
			<br />
			<s:if test="%{subSchemeList.size!=0}">
				<table width="100%" border="0" align="center" cellpadding="0"
					cellspacing="0" class="tablebottom">
					<tr>
						<th class="bluebgheadtd">
							<s:text name="masters.subscheme.search.serial" />
						</th>
						<th class="bluebgheadtd">
							<s:text name="masters.subscheme.search.code" />
						</th>
						<th class="bluebgheadtd">
							<s:text name="masters.subscheme.search.name" />
						</th>
						<th class="bluebgheadtd">
							<s:text name="masters.subscheme.search.schemename" />
						</th>
						<th class="bluebgheadtd">
							<s:text name="masters.subscheme.search.fundname" />
						</th>
						<th class="bluebgheadtd">
							<s:text name="masters.subscheme.search.estimateamount" />
						</th>
						<th class="bluebgheadtd">
							<s:text name="masters.subscheme.search.isactive" />
						</th>

					</tr>
					
					<c:set var="trclass" value="greybox"/>
					<s:iterator var="sub" value="subSchemeList" status="s">
						<tr>
							<td class="<c:out value='${trclass}'/>">
								<s:property value="#s.index+1" />
							</td>
							<td class="<c:out value='${trclass}'/>">
								<a href="#" onclick="urlLoad('<s:property value="%{id}" />','<s:property value="%{showMode}" />');"
									id="sourceLink" /> <s:label value="%{code}" /> </a>
							</td>
							<td class="<c:out value='${trclass}'/>">
								<s:property value="name" />
							</td>
							<td class="<c:out value='${trclass}'/>">
								<s:property value="scheme.name" />
							</td>
							<td class="<c:out value='${trclass}'/>">
								<s:property value="scheme.fund.name" />
							</td>
							<td class="<c:out value='${trclass}'/>">
								<s:property value="initialEstimateAmount" />
							</td>
							<td class="<c:out value="${trclass}"/>" >
								<s:if test="%{isactive==true}">Active</s:if><s:else>Inactive</s:else>
							</td>
						</tr>
						<c:choose>
					        <c:when test="${trclass=='greybox'}"><c:set var="trclass" value="bluebox"/></c:when>
					        <c:when test="${trclass=='bluebox'}"><c:set var="trclass" value="greybox"/></c:when>
				        </c:choose>
					</s:iterator>

				</table>
			</s:if>


			<s:if test="%{subSchemeList.size==0}">
				<div id="msgdiv" style="display: block">
					<table align="center" class="tablebottom" width="80%">
						<tr>
							<th class="bluebgheadtd" colspan="7">
								No Records Found
							</td>
						</tr>
					</table>
				</div>

			</s:if>
			<br />

			<br />
			<br />

		</s:form>

		<script>
		
		function loadScheme(fund)
		{
		populateschemeId({fundId:fund.value})
		}
		
		function loadSubScheme(scheme)
		{
		populatesubSchemeId({schemeId:scheme.value})
		}
		
		function urlLoad(subSchemeId,showMode)
		{
		if(showMode=='edit')
			url="../masters/subScheme!viewSubScheme.action?id="+subSchemeId+"&showMode=edit";
		else
			url="../masters/subScheme!viewSubScheme.action?id="+subSchemeId;
		window.open(url,'subSchemeView','resizable=yes,scrollbars=yes,left=300,top=40, width=900, height=700');
		}
				
	
		</script>
	</body>  
</html>