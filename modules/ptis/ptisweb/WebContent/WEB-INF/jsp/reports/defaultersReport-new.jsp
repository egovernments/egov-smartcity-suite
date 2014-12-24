<%@ include file="/includes/taglibs.jsp" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title><s:text name="defaultersListReport.title"/></title>
	<script type="text/javascript">
	
		function populateWard() {
			populatereportWards({
				zoneId : document.getElementById("reportZones").value
			});			
		}
	
	</script>
</head>
<body>
	<div align="left">
  		<s:actionerror/>
  	</div>

  	<s:form name="DefaultersListForm" action="defaultersReport" theme="simple">
		<div class="formmainbox">
  			<div class="formheading"></div>
			<div class="headingbg"><s:text name="defaultersListReport.title"/></div>
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td class="greybox" colspan="4" width="100%">
						&nbsp; &nbsp;
					</td>					
				</tr>
				<tr>
					<td class="greybox" width="45%">
							&nbsp; &nbsp;
					</td>
					<td class="greybox" width="5%" style="text-align: left">
						&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
						&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
						<s:text name="Zone" /><span class="mandatory"> * </span> :
					</td>  
					<td class="greybox" width="25%" style="text-align: left">
						&nbsp; &nbsp; &nbsp; &nbsp;
						<s:select id="reportZones" headerKey="-1"
							headerValue="%{getText('default.select')}" name="zoneId"
							value="%{zoneId}" list="ZoneBndryMap" cssClass="selectnew"
							onchange="return populateWard();" style="width: 100px"/>
					</td>
					<td class="greybox" width="25%">
							&nbsp; &nbsp;
					</td>					
				</tr>
				<tr>
					<td class="bluebox" width="45%">
							&nbsp; &nbsp;
					</td>					
					<td class="bluebox" width="5%" style="text-align: left">
						&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
						&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
						<s:text name="Ward" /> : 
					</td>
					<td class="bluebox" width="25%" style="text-align: left">
						<egov:ajaxdropdown id="wardAjaxDropdown" fields="['Text','Value']"
							dropdownId="reportWards" url="common/ajaxCommon!wardByZone.action" />
						&nbsp; &nbsp; &nbsp; &nbsp;
						<s:select id="reportWards" headerKey="-1"
							headerValue="%{getText('default.select')}"
							list="dropdownData.Wards" cssClass="selectnew" name="wardId"
							value="%{wardId}" style="width: 100px"/> 
					</td>
					<td class="bluebox" width="25%">
							&nbsp; &nbsp;
					</td>
				</tr>				
				<tr>
					<td class="greybox" width="45%">
							&nbsp; &nbsp;
					</td>					
					<td class="greybox" width="5%" style="text-align: left">
						&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
						&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
						<s:text name="outstandingAmount" /><span class="mandatory"> * </span> : 
					</td>
					<td class="greybox" width="25%" style="text-align: left">
						&nbsp; &nbsp; &nbsp; &nbsp;
						<s:select id="outstandingAmount" headerKey="-1"
							headerValue="%{getText('default.select')}" name="amountRange"
							value="%{amountRange}"
							list="@org.egov.ptis.actions.common.CommonServices@outstandingAmountRanges"
							cssClass="selectnew" style="width: 100px" />
					</td>					
					<td class="greybox" width="25%">
							&nbsp; &nbsp;
					</td>
				</tr>
				<tr>
					<td class="greybox" colspan="4" width="100%">
					</td>					
				</tr>
			</table>
			<div class="buttonbottom" align="center">
					<s:submit name="btnSubmitReport" id="btnSubmitReport" method="generateReport" cssClass="buttonsubmit" />
					&nbsp; &nbsp;
					<input type="button" name="ptReportClose" id="ptReportClose" value="Close" class="button" onclick="return confirmClose();">
			</div>			
		</div>
	</s:form>
</body>
</html>