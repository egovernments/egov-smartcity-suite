<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ include file="/includes/taglibs.jsp"%>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<html>
	<head>
		<title><s:text name="BigBuildingRec.title"/></title>
		<script type="text/javascript">
		</script>
		<sx:head/>
	</head>
	<body>
		<div align="left">
  			<s:actionerror/>
  		</div>
		<s:form action="bigBuildingRecoveryReport" theme="simple" validate="true">
		<div class="formmainbox">
			<div class="formheading"></div>
			<div class="headingbg"><s:text name="BigBuildingRec.title"/></div>
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td class="greybox">&nbsp;</td>
					<td class="greybox"><s:text name="fromDate"/><span class="mandatory1">*</span> :</td>
					<td class="greybox">
						<s:textfield name="fromDate" id="fromDate" maxlength="10"
							onkeyup="DateFormat(this,this.value,event,false,'3')" size="10" onblur="validateDateFormat(this);"/>
					</td>
					<td class="greybox">&nbsp;</td>
				</tr>
				<tr>
					<td class="bluebox">&nbsp;</td>
					<td class="bluebox"><s:text name="toDate"/><span class="mandatory1">*</span> :</td>
					<td class="bluebox">
						<s:textfield name="toDate" id="toDate" maxlength="10"
							onkeyup="DateFormat(this,this.value,event,false,'3')" size="10" onblur="validateDateFormat(this);"/>
					</td>
					<td class="bluebox" colspan="2">&nbsp;</td>
				</tr>
	</table>
		<tr>
        	<font size="2"><div align="left" class="mandatory"><s:text name="mandtryFlds"/></div></font>
        </tr>
	</div>
	<div class="buttonbottom" align="center">
		<tr>
		 <td><s:submit name="button32" type="submit" cssClass="buttonsubmit" id="button32" value="Search" method="generateBigBldgRecStmt"/></td>
		 <td><input type="button" name="button2" id="button2" value="Close" class="button" onclick="window.close();"/></td>
		</tr>
	</div>
	</s:form>
			
	</body>
</html>