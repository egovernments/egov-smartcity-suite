<%@ include file="/includes/taglibs.jsp"%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<html>
<head>
</head>
<body>
<div class="formmainbox">
			<div class="formheading"></div>
			<div class="headingbg">
				<s:text name="arrdndpen" />
			</div>
<table border="0" cellspacing="0" cellpadding="0" width="100%">
<s:form  name="transferform" theme="simple">
<tr>
						<td class="bluebox" width="15%"></td>
						<td class="bluebox">
							<s:text name="CurrentTax" />
							:
						</td>
						<td class="bluebox" colspan="3">
							<span class="bold">Rs. <s:property default="N/A"
									value="%{currDemand}" /> </span>
						</td>
					</tr>
					<tr>
						<td class="greybox" width="15%"></td>
						<td class="greybox">
							<s:text name="CurrentTaxDue" />
							:
						</td>
						<td class="greybox" colspan="3">
							<span class="bold">Rs. <s:property default="N/A"
									value="%{currDemandDue}" /> </span>
						</td>
					</tr>
					<tr>
						<td class="bluebox" width="15%"></td>
						<td class="bluebox">
							<s:text name="ArrearsDue" />
							:
						</td>
						<td class="bluebox" colspan="3">
							<span class="bold">Rs. <s:property default="N/A"
									value="%{arrDemand}" /> </span>
						</td>
					</tr>
					<div align="left" class="mandatory" style="font-size: 11px">
			* Arrears in demand must be cleared in order for the user to carry out this action.
					</div>
					
					</s:form>
</table>
<tr>
					<div class="buttonbottom" align="center">
						<td>
							<input type="button" value="Close" class="button" align="center"
								onClick="window.close()" />
						</td>
					</div>
					</tr>
</div>
</body>
</html>