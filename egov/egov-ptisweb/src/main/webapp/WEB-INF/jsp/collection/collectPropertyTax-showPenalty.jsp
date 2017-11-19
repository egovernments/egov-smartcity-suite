<%--
  ~    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) 2017  eGovernments Foundation
  ~
  ~     The updated version of eGov suite of products as by eGovernments Foundation
  ~     is available at http://www.egovernments.org
  ~
  ~     This program is free software: you can redistribute it and/or modify
  ~     it under the terms of the GNU General Public License as published by
  ~     the Free Software Foundation, either version 3 of the License, or
  ~     any later version.
  ~
  ~     This program is distributed in the hope that it will be useful,
  ~     but WITHOUT ANY WARRANTY; without even the implied warranty of
  ~     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  ~     GNU General Public License for more details.
  ~
  ~     You should have received a copy of the GNU General Public License
  ~     along with this program. If not, see http://www.gnu.org/licenses/ or
  ~     http://www.gnu.org/licenses/gpl.html .
  ~
  ~     In addition to the terms of the GPL license to be adhered to in using this
  ~     program, the following additional terms are to be complied with:
  ~
  ~         1) All versions of this program, verbatim or modified must carry this
  ~            Legal Notice.
  ~            Further, all user interfaces, including but not limited to citizen facing interfaces,
  ~            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
  ~            derived works should carry eGovernments Foundation logo on the top right corner.
  ~
  ~            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
  ~            For any further queries on attribution, including queries on brand guidelines,
  ~            please contact contact@egovernments.org
  ~
  ~         2) Any misrepresentation of the origin of the material is prohibited. It
  ~            is required that all modified versions of this material be marked in
  ~            reasonable ways as different from the original version.
  ~
  ~         3) This license does not grant any rights to any user of the program
  ~            with regards to rights under trademark law for use of the trade names
  ~            or trademarks of eGovernments Foundation.
  ~
  ~   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
  ~
  --%>

<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/includes/taglibs.jsp" %>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>

<head>
<title><s:text name="confirmPropPenalty"></s:text></title>
<script type="text/javascript">
	function checkIsPositiveNumber(obj, desc) {

		if(!isPositiveNumber(obj, desc)) {
			obj.value = "0";
			return false;
		}
		
		return true;
	}
</script>
</head>

<body>
	 <div align="left">
  		<s:actionerror/>
  	</div>
  	<s:if test="%{hasActionMessages()}">
  		<div id="actionMessages" class="messagestyle">
    	<s:actionmessage theme="simple"/>
    </div>
  	</s:if>
	<div class="formmainbox">
		<div class="formheading"></div>
		<div class="headingbg">
			<s:text name="confirmPropPenaltyAndRebate" />
		</div>
		<s:form theme="simple" action="collectPropertyTax" name="CollectPropertyTaxForm">
			<s:token />
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
				<span class="bold"  style="font-size: 14px"> <br> No Penalty. </span>
			</s:if>
			<s:else>
				<table width="100%" border="0" align="center" cellpadding="0"
					cellspacing="0" class="tablebottom">

					<tr>
						<th class="bluebgheadtd" width="10%"/>
						<th class="bluebgheadtd" width="10%" style="border-left: 1px solid #E9E9E9;">
							<s:text name="Installment" />
						</th>
						<th class="bluebgheadtd" width="10%" align="center">
							<s:text name="Tax" />
						</th>
						<th class="bluebgheadtd" width="10%" align="center">
							<s:text name="Balance" />
						</th>
						<th class="bluebgheadtd" width="10%" align="center">
							<s:text name="Penalty" />
						</th>
						<th class="bluebgheadtd" width="10%" align="center">
							<s:text name="PenaltyCollection" />
						</th>
						<th class="bluebgheadtd" width="10%" align="center">
							<s:text name="Rebate" />
						</th>
						<th class="bluebgheadtd" width="10%"/>
					</tr>
						<s:iterator value="(instTaxBeanList.size).{#this}" status="instTaxBean">
						<tr>
							<td class="blueborderfortd" width="10%"></td>
							<td class="blueborderfortd" style="border-left: 1px solid #E9E9E9;">
								<div align="center">
									<s:property
										value="%{instTaxBeanList[#instTaxBean.index].installmentStr}" />
									<s:hidden
										name="instTaxBeanList[%{#instTaxBean.index}].installmentId"
										value="%{instTaxBeanList[#instTaxBean.index].installmentId}" />
									<s:hidden
										name="instTaxBeanList[%{#instTaxBean.index}].installmentStr"
										value="%{instTaxBeanList[#instTaxBean.index].installmentStr}" />
								</div>
							</td>
							<td class="blueborderfortd">
								<div align="right">
									<s:text name="format.money">
										<s:param
											value="%{instTaxBeanList[#instTaxBean.index].instTaxAmt}" />
									</s:text>
									<s:hidden
										name="instTaxBeanList[%{#instTaxBean.index}].instTaxAmt"
										value="%{instTaxBeanList[#instTaxBean.index].instTaxAmt}" />
									<s:hidden
										name="instTaxBeanList[%{#instTaxBean.index}].instCollAmt"
										value="%{instTaxBeanList[#instTaxBean.index].instCollAmt}" />
								</div>
							</td>
							<td class="blueborderfortd">
								<div align="right">
									<s:text name="format.money">
										<s:param value="%{instTaxBeanList[#instTaxBean.index].instBalanceAmt}" />
									</s:text>
									<s:hidden
										name="instTaxBeanList[%{#instTaxBean.index}].instBalanceAmt"
										value="%{instTaxBeanList[#instTaxBean.index].instBalanceAmt}" />
								</div>
							</td>							
							<td class="blueborderfortd">
								<div align="right">
									<s:textfield
										name="instTaxBeanList[%{#instTaxBean.index}].instPenaltyAmt"
										maxlength="15" size="10" id="instPenaltyAmt"
										value="%{instTaxBeanList[#instTaxBean.index].instPenaltyAmt}" 
										onblur="trim(this,this.value); checkNumber(this); checkIsPositiveNumber(this, 'Penalty');" />
								</div>
							</td>
							<td class="blueborderfortd">
								<div align="right">
									<s:text name="format.money">
										<s:param
											value="%{instTaxBeanList[#instTaxBean.index].instPenaltyCollAmt}" />
									</s:text>
									<s:hidden
										name="instTaxBeanList[%{#instTaxBean.index}].instPenaltyCollAmt"
										value="%{instTaxBeanList[#instTaxBean.index].instPenaltyCollAmt}" />
								</div>
							</td>							
							<td class="blueborderfortd">
								<div align="right">
									<s:textfield
										name="instTaxBeanList[%{#instTaxBean.index}].instRebateAmt"
										maxlength="15" size="10" id="instRebateAmt"
										value="%{instTaxBeanList[#instTaxBean.index].instRebateAmt}" 
										onblur="trim(this,this.value); checkNumber(this); checkIsPositiveNumber(this, 'Rebate');" />
								</div>
							</td>
							<td class="blueborderfortd" width="10%"></td>
						</tr>
					</s:iterator>
				</table>
			</s:else>
			<s:hidden name="isPenaltyConfirmed" value="true" />
			<div class="buttonbottom" align="center">
			<s:submit value="Confirm" name="PayBill" id="PayBill" method="save" cssClass="buttonsubmit"/>
				<input type="button" name="button2" id="button2" value="Close"
					class="button" onclick="return confirmClose();" />
			</div>
		</s:form>
	</div>
</body>

</html>
