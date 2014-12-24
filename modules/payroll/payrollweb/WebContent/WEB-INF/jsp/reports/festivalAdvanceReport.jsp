<%@ include file="/includes/taglibs.jsp" %>
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="egovtags"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>

<html>
<title><s:text name="page.title.festivalAdvance" /></title>
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
	<div class="errorstyle" id="searchFestivalAdvance_error" style="display: none;"></div>
	<s:if test="%{hasErrors()}">
		<div class="errorstyle">
			<s:actionerror />
			<s:fielderror />
		</div>
	</s:if>
	<s:form name="festivalAdvanceReport" action="festivalAdvanceReport" theme="simple">
		<table width="100%" align="center" cellpadding="0" cellspacing="0" border="0" id="festivalAdvanceReport">		
			<tr>
				<td class="headingwk" colspan="4">
					<div class="arrowiconwk">
					  <img src="../common/image/arrow.gif" />
					</div>
					   <div class="headplacer"> <s:text name="report.common.search" /></div>
				</td>
			</tr>
			<%@ include file="commonSearchReport.jsp" %>
		  	<br/>
		  	<%@ include file="festivalAdvance-list.jsp" %>
		 	<br/>  
	   	 </table>
	</s:form>
</body>
</html>