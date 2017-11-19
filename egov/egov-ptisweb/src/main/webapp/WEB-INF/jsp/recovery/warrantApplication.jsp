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

<%@ taglib prefix="s" uri="/WEB-INF/taglib/struts-tags.tld" %>

<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<div id="errMsg">
	</div>
	<tr>
		<td colspan="5">
			<div class="headingsmallbg"> 
				<s:text name="warrantApp.title"></s:text>
			</div>
		</td>
	</tr>
	<s:hidden name="warrant.recovery.id" value="%{id}"></s:hidden>
             <tr>
		<td colspan="5">
			 <table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr><td class="bluebox" width="25%"><s:text name="warrantFee" />:<span class="mandatory1">*</span></td>
			    <td class="bluebox" width="25%">
			   
				<s:textfield name="warrant.warrantFees[0].amount" id="warrentFee" value="%{warrentFee}"   onkeyup="validateDecimal(this);" onblur="validateDigitsAndDecimal(this);totalFees();" maxlength="11" cssStyle="text-align:right;"/>
				<s:hidden name="warrant.warrantFees[0].demandReason.id" value="%{getDemandReason('WARRANT_FEE')}" ></s:hidden>
				</td>
<td class="bluebox" width="25%"><s:text name="noticeFee" />:<span class="mandatory1">*</span></td>
			    <td class="bluebox" width="25%">
				<s:textfield name="warrant.warrantFees[1].amount"  id="noticeFee" value="%{noticeFee}" onkeyup="validateDecimal(this);" onblur="validateDigitsAndDecimal(this);totalFees()" maxlength="11" cssStyle="text-align:right;"/>
				<s:hidden name="warrant.warrantFees[1].demandReason.id" value="%{getDemandReason('NOTICE_FEE')}"></s:hidden>
				</td>
				</tr>
              <tr>
<td class="bluebox" width="25%"><s:text name="courtFee" />:</td>
			    <td class="bluebox" width="25%">
				<s:textfield name="warrant.warrantFees[2].amount"  id="courtFee" value="%{courtFee}"  onkeyup="validateDecimal(this);" onblur="validateDigitsAndDecimal(this);totalFees()" maxlength="11" cssStyle="text-align:right;"/>
				<s:hidden name="warrant.warrantFees[2].demandReason.id" value="%{getDemandReason('COURT_FEE')}"></s:hidden>
				</td>
            
<td class="bluebox" width="25%"><s:text name="totalFee" />:<span class="mandatory1">*</span></td>
			    <td class="bluebox" width="25%">
			    
			    <div id="ttlFee"> </div>
				<s:property value="%{ttlFee}" />
				</td>
			</tr>
			<tr>
			<td class="bluebox" width="25%"><s:text name="remarks"/></td>
			<td class="bluebox" width="25%"><s:textarea name="recovery.warrant.remarks" id="warratRemarks" cols="40" rows="2"  onblur="checkLength1024(this)" ></s:textarea></td>
			<td class="bluebox" width="25%"></td>
			<td class="bluebox" width="25%"></td>
			</tr>
			
		</table>
		</td>
	</tr>


</table>
<script>
</script>
