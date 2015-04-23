<%@ include file="/includes/taglibs.jsp"%>

<html>

<head>
<title><s:text name="propertyArrears"></s:text></title>
<link href="<c:url value='/css/propertytax.css'/>" rel="stylesheet"
	type="text/css" />
<link href="<c:url value='/css/commonegov.css'/>" rel="stylesheet"
	type="text/css" />
<script type="text/javascript"
	src="<c:url value='/javascript/validations.js'/>"></script>
</head>

<body>
	<div class="formmainbox">
		<div class="formheading"></div>
		<div class="headingbg">
			<s:text name="propertyArrears" />
		</div>
		<s:form theme="simple">
			<br />
			<table width="300px" border="0" align="center" cellspacing="0"
				cellpadding="0">
				<tr>
					<td class="bluebox" width="180px" colspan="2">
						<div align="center">
							<s:text name="prop.Id" /> : 
							<span class="bold">
								<s:property value="%{propertyId}" />
							</span>
						</div>
					</td>
				</tr>
			</table>
			<s:if test="%{propertyArrearsList.isEmpty()}">
				<span class="bold"  style="font-size: 14px"> <br> <s:text name="noPropArr"/></span>
			</s:if>
			<s:else>
				<table width="300px" border="0" align="center" cellpadding="0"
					cellspacing="0" class="tablebottom">

					<tr>
						<th class="bluebgheadtd" width="50%">
							<s:text name="Installment" />
						</th>
						<th class="bluebgheadtd" width="50%" align="center" colspan="3">
							<s:text name="Tax" />
						</th>
					</tr>
					<s:iterator value="propertyArrearsList" var="arrears">
						<tr>
							<td class="blueborderfortd">
								<div align="center">
									<s:property value="year" />
								</div>
							</td>
							<td class="blueborderfortd">
								<div align="right">
									<s:property value="taxAmount" />
								</div>
							</td>
						</tr>
					</s:iterator>
				</table>
			</s:else>
			
			<s:if test="%{propReceiptList.isEmpty()}">
				<span class="bold"  style="font-size: 14px"> <br><s:text name="noPropReceipt"/></span>
			</s:if>
			<s:else>
				<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="tablebottom">
					<tr>
						<td colspan="6">
							<div class="headingsmallbg">
								<s:text name="propRcptDet" />
							</div>
						</td>
					</tr>
					<tr>
						<th class="bluebgheadtd" colspan="1" rowspan="2">
							<s:text name="bookNo" />
						</th>
						<th class="bluebgheadtd" align="center" colspan="1" rowspan="2">
							<s:text name="receiptNo" />
						</th>
						<th class="bluebgheadtd" align="center" colspan="1" rowspan="2">
							<s:text name="receiptDate" />
						</th>
						<th class="bluebgheadtd" align="center" colspan="2">
							<s:text name="period" />
						</th>
						<th class="bluebgheadtd" align="center" colspan="1" rowspan="2">
							<s:text name="receiptAmt" />
						</th>
					</tr>
					<tr>
						<th class="bluebgheadtd" align="center" colspan="1">
							<s:text name="fromDate" />
						</th>
						<th class="bluebgheadtd" align="center" colspan="1">
							<s:text name="toDate" />
						</th>
					</tr>
					<s:iterator value="propReceiptList" var="receipt">
						<tr>
							<td class="blueborderfortd" colspan="1">
								<div align="center">
									<s:property value="bookNumber" />
								</div>
							</td>
							<td class="blueborderfortd" colspan="1">
								<div align="center">
									<s:property value="receiptNumber" />
								</div>
							</td>
							<td class="blueborderfortd" colspan="1">
								<div align="center">
									<s:property value="receiptDate" />
								</div>
							</td>
							<td class="blueborderfortd" colspan="1">
								<div align="center">
									<s:property value="fromDate" />
								</div>
							</td>
							<td class="blueborderfortd" colspan="1">
								<div align="center">
									<s:property value="toDate" />
								</div>
							</td>
							<td class="blueborderfortd" colspan="1">
								<div align="right">
									<s:property value="receiptAmount" />
								</div>
							</td>
						</tr>
					</s:iterator>
				</table>
			</s:else>
			
			<div class="buttonbottom" align="center">
				<input type="button" name="button2" id="button2" value="Close"
					class="button" onclick="return confirmClose();" />
			</div>
		</s:form>
	</div>
</body>

</html>