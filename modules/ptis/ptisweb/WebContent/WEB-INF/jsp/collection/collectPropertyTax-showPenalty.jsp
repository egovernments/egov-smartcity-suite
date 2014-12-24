<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/includes/taglibs.jsp" %>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>

<head>
<title><s:text name="confirmPropPenalty"></s:text></title>
<script type="text/javascript">
</script>
</head>

<body>
	<div class="formmainbox">
		<div class="formheading"></div>
		<div class="headingbg">
			<s:text name="confirmPropPenalty" />
		</div>
		<s:form theme="simple" action="collectPropertyTax" name="CollectPropertyTaxForm">
			<table width="100%" border="0" align="center" cellspacing="0" cellpadding="0">
				<tr>
					<td class="bluebox" width="25%"></td>				
					<td class="bluebox" width="25%" colspan="2">
							<s:text name="prop.Id" /> : 
							<span class="bold">
								<s:property value="%{propertyId}" />
								<s:hidden name="propertyId" value="%{propertyId}" />
							</span>
							<s:hidden name="levyPenalty" value="true" />
					</td>
				</tr>
			</table>
			<s:if test="%{instTaxBeanList.isEmpty()}">
				<span class="bold"  style="font-size: 14px"> <br> The property doesn't have arrears </span>
			</s:if>
			<s:else>
				<table width="100%" border="0" align="center" cellpadding="0"
					cellspacing="0" class="tablebottom">

					<tr>
						<th class="bluebgheadtd" width="25%"/>
						<th class="bluebgheadtd" width="15%">
							<s:text name="Installment" />
						</th>
						<th class="bluebgheadtd" width="15%" align="center">
							<s:text name="Tax" />
						</th>
						<th class="bluebgheadtd" width="15%" align="center">
							<s:text name="Penalty" />
						</th>
						<th class="bluebgheadtd" width="25%"/>
					</tr>
						<s:iterator value="(instTaxBeanList.size).{#this}" status="instTaxBean">
						<tr>
							<td class="blueborderfortd" width="25%"></td>
							<td class="blueborderfortd">
								<div align="center">
									<s:property value="%{instTaxBeanList[#instTaxBean.index].installmentStr}" />
									<s:hidden name="instTaxBeanList[%{#instTaxBean.index}].installmentId" value="%{instTaxBeanList[#instTaxBean.index].installmentId}" />
									<s:hidden name="instTaxBeanList[%{#instTaxBean.index}].installmentStr" value="%{instTaxBeanList[#instTaxBean.index].installmentStr}" />
								</div>
							</td>
							<td class="blueborderfortd">
								<div align="right">
									<s:property value="%{instTaxBeanList[#instTaxBean.index].instTaxAmt}" />
									<s:hidden name="instTaxBeanList[%{#instTaxBean.index}].instTaxAmt" value="%{instTaxBeanList[#instTaxBean.index].instTaxAmt}" />
									<s:hidden name="instTaxBeanList[%{#instTaxBean.index}].instCollAmt" value="%{instTaxBeanList[#instTaxBean.index].instCollAmt}" />
								</div>
							</td>
							<td class="blueborderfortd">
								<div align="right">
									<s:textfield name="instTaxBeanList[%{#instTaxBean.index}].instPenaltyAmt" maxlength="15" size="10" id="instPenaltyAmt"	value="%{instTaxBeanList[#instTaxBean.index].instPenaltyAmt}" />
								</div>
							</td>
							<td class="blueborderfortd" width="25%"></td>
						</tr>
					</s:iterator>
				</table>
			</s:else>
			<div class="buttonbottom" align="center">
			<s:submit value="Confirm Penalty" name="PayBill" id="PayBill" method="generatePropertyTaxBill" cssClass="buttonsubmit"/>
				<input type="button" name="button2" id="button2" value="Close"
					class="button" onclick="return confirmClose();" />
			</div>
		</s:form>
	</div>
</body>

</html>
