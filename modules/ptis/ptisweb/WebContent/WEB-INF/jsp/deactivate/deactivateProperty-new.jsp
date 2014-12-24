<%@ page language="java" pageEncoding="UTF-8"%>
<%@include file="/includes/taglibs.jsp"%>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<html>
	<head>
		<title><s:text name="deactivate.prop"/></title>
		<script type="text/javascript" src="/javascript/calendar.js">	
		</script>
		<script type="text/javascript" src="/javascript/dateValidation.js">	
		</script>		
	</head>
	<body>
		<div align="left" class="mandatory">
			<s:actionerror />
		</div>
		<div class="errorstyle" id="property_error_area" style="display:none;"></div>
		<s:form method="post" theme="simple" name="deactivatePropertyForm"
			validate="true">
			<s:push value="model">
			<s:token />
			<div class="formmainbox">
			<center>
				<table border="0" width="100%" cellpadding="0" cellspacing="0">
					<tr>
						<td class="headingbg" width="25%">							
							<div class="formheading"></div>
							<div class="headingbg">&nbsp;</div>
						</td>
						<td class="headingbg" colspan="2">							
							<div class="formheading"></div>
							<div class="headingbg">							
							<s:text name="deactivate.prop"></s:text>											
							</div>
						</td>
						<td class="headingbg" width="25%">							
							<div class="formheading"></div>
							<div class="headingbg">&nbsp;</div>
						</td>
					</tr>
					<tr>
						<td class="greybox" width="25%">
							&nbsp;
						</td>
						<td class="greybox" colspan="2">
							&nbsp;&nbsp;&nbsp;
						</td>
						<td class="greybox" width="25%">
							&nbsp;
						</td>		
					</tr>
					<tr>
						<td class="bluebox" width="25%">
							&nbsp;
						</td>
						<td class="bluebox" >
							<s:text name="prop.Id" />
							:
						</td>

						<td class="bluebox">
							<b><s:property value="%{basicProp.upicNo}" /> </b>
						</td>
						<s:hidden name="propertyId" value="%{basicProp.upicNo}"></s:hidden>
						<s:hidden name="ownerName" value="%{ownerName}"></s:hidden>
						<s:hidden name="address" value="%{address}"></s:hidden>
						<td class="bluebox" width="25%">
							&nbsp;
						</td>			
					</tr>

					<tr>
						<td class="greybox" width="25%">
							&nbsp;
						</td>
						<td class="greybox" >
							<s:text name="HouseNo" />
							:
						</td>

						<td class="greybox">
							<b><s:property value="%{basicProp.address.houseNo}"
									default="N/A" /> </b>
						</td>
						<td class="greybox" width="25%">
							&nbsp;
						</td>			
					</tr>
					<tr>
						<td class="bluebox" width="25%">
							&nbsp;
						</td>
						<td class="bluebox" >
							<s:text name="OwnerName" />
							:
						</td>

						<td class="bluebox">
							<b><s:property value="%{ownerName}" default="N/A" /> </b>
						</td>
						<td class="bluebox" width="25%">
							&nbsp;
						</td>			
					</tr>
					<tr>
						<td class="greybox" width="25%">
							&nbsp;
						</td>
						<td class="greybox"  style="vertical-align: top">
							<s:text name="PropertyAddress" />
							:
						</td>

						<td class="greybox">
							<b><s:property value="%{address}" default="N/A" /> </b>
						</td>
						<td class="greybox" width="25%">
							&nbsp;
						</td>			
					</tr>
					<tr>
						<td class="bluebox" width="25%">
							&nbsp;
						</td>
						<td class="bluebox" >
							<s:text name="PropertyType" />
							:
						</td>

						<td class="bluebox">
							<b><s:property
									value="%{basicProp.property.propertyDetail.propertyTypeMaster.type}"
									default="N/A" /> </b>
						</td>
						<td class="bluebox" width="25%">
							&nbsp;
						</td>			
					</tr>
					<tr>
						<td class="greybox" width="25%">
							&nbsp;
						</td>
						<td class="greybox" >
							<s:text name="deactRsn" />
							<span class="mandatory">*</span> :
						</td>

						<td class="greybox">
							<b><s:select name="reason" list="dropdownData.Reason"
									headerKey="none" listKey="code" listValue="mutationName"
									headerValue="----Choose----" cssStyle="width:150px"></s:select>
							</b>
						</td>
						<td class="greybox" width="25%">
							&nbsp;
						</td>			
					</tr>
					<tr>
						<td class="bluebox" width="25%">
							&nbsp;
						</td>
						<td class="bluebox" >
							<s:text name="OrdNo" />
							:
						</td>
						<td class="bluebox">
							<s:textfield name="referenceNo"></s:textfield>
						</td>
						<td class="bluebox" width="25%">
							&nbsp;
						</td>			
					</tr>
					<tr>
						<td class="greybox" width="25%">
							&nbsp;
						</td>
						<td class="greybox" >
							<s:text name="OrdDate" />
							:
						</td>

						<td class="greybox" style="padding-right: 0; margin-right: 0">
							<s:date name="referenceDate" format="dd/MM/yyyy"
								var="refDate" />
							<s:textfield id="rDate" name="referenceDate"
								value="%{refDate}" size="10"></s:textfield>
								<a 
								href="javascript:show_calendar('deactivatePropertyForm.rDate')"><img
									src="../image/calendaricon.gif" style="border: none" /> </a>
						</td>

						<td class="greybox" width="25%">
							&nbsp;
						</td>
									
					</tr>
					<tr>
						<td class="bluebox" width="25%">
							&nbsp;
						</td>
						<td class="bluebox"  style="vertical-align: top">
							<s:text name="comment" />
							<span class="mandatory">*</span>:
						</td>
						<td class="bluebox">
							<b><s:textarea name="remarks" cols="18"></s:textarea> </b>
						</td>
						<td class="bluebox" width="25%">
							&nbsp;
						</td>			
					</tr>
					<tr>
		<td class="greybox2">&nbsp;</td>
		<td class="greybox">Upload Document</td>
		<td class="greybox"><input type="button" class="button"
			value="Upload Document" id="docUploadButton"
			onclick="showDocumentManager();" /> <s:hidden name="docNumber"
				id="docNumber" /></td>
		<td class="greybox" colspan="2">&nbsp;</td>
	</tr>
					<tr>
						<td class="greybox" width="25%">
							&nbsp;
						</td>
						<td class="greybox" colspan="2">
							&nbsp;&nbsp;&nbsp;
						</td>
						<td class="greybox" width="25%">
							&nbsp;
						</td>			
							
					</tr>
					<tr>
        				<%@ include file="../workflow/property-workflow.jsp" %>  
       			 	</tr>
       			 	<s:hidden name="modelId" id="modelId" value="%{modelId}"/>
				</table>
				<div class="buttonbottom" align="center">
					<s:submit cssClass="buttonsubmit" value="Forward" id="Deactivate:Forward" theme="simple" method="forward" onclick="setWorkFlowInfo(this);"/>
					<!-- <s:submit cssClass="buttonsubmit" value="Approve" id="Deactivate:Save" theme="simple" method="save" onclick="setWorkFlowInfo(this);"/> -->										
					<input type="button" class="button" value="Close" onclick="return confirmClose();">
				</div>								
			</center>
			</div>
			</s:push>
		</s:form>
		<script type="text/javascript">
	function showDocumentManager() {
			var docNum = document.getElementById("docNumber").value;
			var url;
			if (docNum == null || docNum == '' || docNum == 'To be assigned') {
				url = "/egi/docmgmt/basicDocumentManager.action?moduleName=ptis";
			} else {
				url = "/egi/docmgmt/basicDocumentManager!editDocument.action?docNumber="
						+ docNum + "&moduleName=ptis";
			}
			window.open(url, 'docupload', 'width=1000,height=400');
		}
		</script>
	</body>
</html>
