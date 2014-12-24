<%@ include file="/includes/taglibs.jsp" %>
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="egovtags"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>

<html>
<s:if test="%{sourcepage=='incomeTaxDeduction'}">
	<title><s:text name='page.title.incometax'/></title>
</s:if>
<s:if test="%{sourcepage=='profTaxDeduction'}">
    <title><s:text name='page.title.professionaltax'/></title>
</s:if>
<s:if test="%{sourcepage=='dcpsDeduction'}">
    <title><s:text name='page.title.dcpsDeduction'/></title>
</s:if>
<s:if test="%{sourcepage=='GISDeduction'}">
    <title><s:text name='page.title.gisdeduction'/></title>
</s:if>
<s:if test="%{sourcepage=='LICDeduction'}">
    <title><s:text name='page.title.licdeduction'/></title>
</s:if>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>

<script type="text/javascript">

function setMonthStr(){
	var monthIndex=document.getElementById("month").selectedIndex;
	document.getElementById("monthStr").value=document.getElementById("month").options[monthIndex].text;
	}

</script>

<body>
	<div class="errorstyle" id="searchDeduciton_error" style="display: none;"></div>
	<s:if test="%{hasErrors()}">
		<div class="errorstyle">
			<s:actionerror />
			<s:fielderror />
		</div>
	</s:if>
	<s:form name="deductionReport" action="deductionReport" theme="simple">
		<s:hidden name="sourcepage" id="sourcepage" value="%{sourcepage}" />
		<s:hidden name="grandTotal" id="grandTotal" value="%{grandTotal}" />
			<table width="100%" align="center" cellpadding="0" cellspacing="0" border="0" id="deductionReport">
				<tr>
						<td class="headingwk" colspan="4">
							<div class="arrowiconwk">
							  <img src="../common/image/arrow.gif" />
							</div>
							<s:if test="%{sourcepage=='incomeTaxDeduction'}">
							   <div class="headplacer"> <s:text name="report.common.search" /></div>
							</s:if>
                            <s:elseif test="%{sourcepage=='profTaxDeduction'}">
                               <div class="headplacer"> <s:text name="report.common.search" /></div>
                            </s:elseif>
                            <s:elseif test="%{sourcepage=='dcpsDeduction'}">
                               <div class="headplacer"> <s:text name="report.common.search" /></div>
                            </s:elseif>
                            <s:if test="%{sourcepage=='GISDeduction'}">
                               <div class="headplacer"> <s:text name="page.title.gisdeduction" /></div>
                            </s:if>
                            <s:if test="%{sourcepage=='LICDeduction'}">
                               <div class="headplacer"> <s:text name="report.common.search" /></div>
                            </s:if>
						</td>
				</tr>
					<%@ include file="commonSearchReport.jsp" %>
						<s:if test="%{sourcepage=='incomeTaxDeduction'}">
						  	<br/>
						  	<%@ include file="incomeTaxDeduction-list.jsp" %>
						 	<br/>
						</s:if>
                        <s:if test="%{sourcepage=='profTaxDeduction'}">
                              <br/>
                              <%@ include file="professionalTaxDeduction-list.jsp" %>
                             <br/>
                        </s:if>
                        <s:if test="%{sourcepage=='dcpsDeduction'}">
                              <br/>
                              <%@ include file="dcpsDeduction-list.jsp" %>
                             <br/>
                        </s:if>
                        <s:if test="%{sourcepage=='GISDeduction'}">
                              <br/>
                              <%@ include file="gisDeduction-list.jsp" %>
                             <br/>
                        </s:if>
                        <s:if test="%{sourcepage=='LICDeduction'}">
							<%@ include file="licDeduction-actions.jsp" %>
                       </s:if>
			</table>
	</s:form>
</body>
</html>
