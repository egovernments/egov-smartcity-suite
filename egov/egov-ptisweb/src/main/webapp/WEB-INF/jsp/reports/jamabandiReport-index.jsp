<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ include file="/includes/taglibs.jsp"%>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<html>
	<head>
		<title><s:text name="Jamabandi.title"/></title>
		<script type="text/javascript">
			function populateWard() {
				populatewardId( {
					zoneId : document.getElementById("zoneId").value
				});
			}		
		</script>
		<sx:head/>
	</head>
	<body onload="loadOnStartUp();">
		<div align="left">
  			<s:actionerror/>
  		</div>
		<s:form action="jamabandiReport" theme="simple" validate="true">
		<div class="formmainbox">
			<div class="formheading"></div>
			<div class="headingbg"><s:text name="Jamabandi.title"/></div>
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td>&nbsp;</td>
					<td><s:text name="Zone"/><span class="mandatory1">*</span> :</td>
					<td>
						<s:select name="zoneId" id="zoneId" list="dropdownData.Zone"
							listKey="id" listValue="name" headerKey="-1"
							headerValue="%{getText('default.select')}" value="%{zoneId}"
							onchange="populateWard()" />
						<egov:ajaxdropdown id="wardId" fields="['Text','Value']"
							dropdownId="wardId" url="common/ajaxCommon!wardByZone.action" />
					</td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td>&nbsp;</td>
					<td><s:text name="Ward"/><span class="mandatory1">*</span> :</td>
					<td><s:select name="wardId" id="wardId" list="dropdownData.wardList"
							listKey="id" listValue="name" headerKey="-1"
							headerValue="%{getText('default.select')}" value="%{wardId}"  
							onchange="return populatePartNumbers(this.id);" />
					</td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td>&nbsp;</td>
					<%@ include file="../common/partnumber.jsp" %>
					<td>&nbsp;</td>	
				</tr> 
				<tr>
					<td colspan="4">&nbsp;</td>
				</tr>
				
	</table>
		<tr>
        	<font size="2"><div align="left" class="mandatory"><s:text name="mandtryFlds"/></div></font>
        </tr>
	</div>
	<div class="buttonbottom" align="center">
		<tr>
		 <td><s:submit name="button32" type="submit" cssClass="buttonsubmit" id="button32" value="Search" method="generateJamabandi"/></td>
		 <td><input type="button" name="button2" id="button2" value="Close" class="button" onclick="window.close();"/></td>
		</tr>
	</div>
	</s:form>
	<script type="text/javascript">
  		paintAlternateColorForRows();
  	</script>
	</body>
</html>