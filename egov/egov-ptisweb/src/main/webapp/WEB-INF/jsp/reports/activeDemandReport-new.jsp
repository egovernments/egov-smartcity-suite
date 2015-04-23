<%@ include file="/includes/taglibs.jsp" %>

<html>
<head>
	<title><s:text name="activeDemandReport.title"/></title>
	<script type="text/javascript">
		jQuery.noConflict();
		jQuery(function() {
			jQuery("#asOnDate").datepicker(
				{
					maxDate: new Date(),
					dateFormat: 'dd/mm/yy'
				}
			);
		});

		function generateWardReport() {
		}
	</script>
</head>
<body>
	<div align="left">
  		<s:actionerror/>
  	</div>
  	<s:form name="ActiveDemandReportForm" action="activeDemandReport" theme="simple">
		<div class="formmainbox">
  			<div class="formheading"></div>
			<div class="headingbg">
				<s:text name="title.nmc" /><br>
	  			<s:text name="title.pt" /><br><br>
				<s:date name="asOnDate" format="dd MMMMM yyyy" var="asOnDateString" />
				<s:if test="#asOnDateString == null">
					<s:text name="activeDemandReport.title"/>
				</s:if> 				
				<s:else>
					<s:text name="activeDemandReport.title"/> as on <s:property value="%{asOnDateString}" />
				</s:else>
			</div>
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td class="bluebox" width="5%">
						&nbsp;
					</td>
					<td class="bluebox">
						<s:text name="PropertyType" /><span class="mandatory"> * </span> : 
					</td>
					<td class="bluebox">
						<s:select list="dropdownData.propTypes" name="propertyTypes" value="%{propertyTypes}"
							headerKey="-1" listKey="id" listValue="type"
						    id="propType" multiple="true"/>
					</td>
					<td class="bluebox"> 
						<s:text name="asOnDate" /> : 
					</td>
					<td class="bluebox">
						<s:textfield id="asOnDate" name="asOnDate" value="%{asOnDate}" readonly="true"/>
					</td>					
				</tr>
				<tr>
					<td class="greybox" width="5%">
						&nbsp;
					</td>
					<td class="greybox" colspan="2">
						<s:text name="include.obj.properties" /> 
						<s:checkbox name="objPropsIncluded" value="%{objPropsIncluded}" id="objPropsIncluded"/>
					</td>
					<td class="greybox" colspan="3">
						&nbsp;
					</td>
				</tr>		
			</table>
			<div class="buttonbottom" align="center">
					<s:submit name="btnSubmitReport" id="btnSubmitReport" method="search" cssClass="buttonsubmit" />
					<input type="button" name="ptReportClose" id="ptReportClose" value="Close" class="button" onclick="return confirmClose();">
			</div>			
		</div>
		<s:if test="%{resultPage}">
			<%@ include file="activeDemandReportResults.jsp"%> 
		</s:if>
	</s:form>
</body>
</html>
