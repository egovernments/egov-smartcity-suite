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


<%@ taglib prefix="s" uri="/WEB-INF/tags/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<tr>
	<td style="width: 5%"></td>
	<td class="bluebox">Voucher Sub-Type<span class="mandatory1">*</span></td>
	<td class="bluebox"><select name="vType" id="vType"
		onchange="onChangeVSubType()">
			<option value="-1">------Choose------</option>
			<option value="JVGeneral">General</option>
			<option value="Works">Works</option>
			<option value="Purchase">Purchase</option>
			<option value="Fixed Asset">Fixed Asset</option>
			<!-- <option value="Salary">Salary</option> -->
			<option value="Expense">Expense</option>
			<!-- <option value="Pension">Pension</option> -->
	</select></td>
	<td class="greybox"><s:text name="jv.partyName" />
		<div id="partyNameDivId" style="display: none">
			<span class="mandatory1">*</span>
		</div></td>
	<td class="bluebox"><s:textfield name="voucherTypeBean.partyName"
			id="voucherTypeBean.partyName" onblur="isSpecialChar(this)"
			value="%{voucherTypeBean.partyName}" /></td>
</tr>
<tr>
	<td style="width: 5%"></td>
	<td class="bluebox"><s:text name="jv.partyBillNum" /></td>
	<td class="bluebox"><s:textfield
			name="voucherTypeBean.partyBillNum" id="voucherTypeBean.partyBillNum"
			value="%{voucherTypeBean.partyBillNum}" /></td>
	<td class="bluebox"><s:text name="jv.billNum" /></td>
	<td class="bluebox"><s:textfield name="voucherTypeBean.billNum"
			id="voucherTypeBean.billNum" value="%{voucherTypeBean.billNum}" /></td>
</tr>
<tr>
	<td style="width: 5%"></td>
	<td class="greybox"><s:text name="jv.partyBillDate" /></td>
	<td class="bluebox"><s:date name="voucherTypeBean.partyBillDate"
			var="partyBillDateId" format="dd/MM/yyyy" /> <s:textfield
			name="voucherTypeBean.partyBillDate" id="partyBillDate"
			value="%{partyBillDateId}" maxlength="10" size="15"
			onkeyup="DateFormat(this,this.value,event,false,'3')" /> <a
		href="javascript:show_calendar('jvcreateform.partyBillDate',null,null,'DD/MM/YYYY');"
		style="text-decoration: none">&nbsp;<img tabIndex=-1
			src="/egi/resources/erp2/images/calendaricon.gif" border="0" /></a>(dd/mm/yyyy)</td>
	<td class="bluebox"><s:text name="jv.billDate" /></td>
	<td class="bluebox"><s:date name="voucherTypeBean.billDate"
			var="billDateId" format="dd/MM/yyyy" /> <s:textfield
			name="voucherTypeBean.billDate" id="billDate" value="%{billDateId}"
			maxlength="10" size="15"
			onkeyup="DateFormat(this,this.value,event,false,'3')" /> <a
		href="javascript:show_calendar('jvcreateform.billDate',null,null,'DD/MM/YYYY');"
		style="text-decoration: none">&nbsp;<img tabIndex=-1
			src="/egi/resources/erp2/images/calendaricon.gif" border="0" /></a>(dd/mm/yyyy)</td>
</tr>
<input type="hidden" id="voucherTypeBean.voucherSubType"
	name="voucherTypeBean.voucherSubType"
	value="${voucherTypeBean.voucherSubType}" />

<script type="text/javascript">
function onChangeVSubType(){
	
	var vType = document.getElementById('vType').value;
	if(vType == 'JVGeneral' || vType == '-1'){
		vsubTypeValue = "Journal-JVGeneral";
	}else if(vType == 'Works'){
		vsubTypeValue = "worksjv-Contractor Journal";
	}else if(vType == 'Purchase'){
		vsubTypeValue = "purchasejv-Supplier Journal";
	}else if(vType == 'Salary'){
		vsubTypeValue = "salaryjv-Salary Journal";
	}else if(vType == 'Expense'){
		vsubTypeValue = "contingentjv-Expense Journal";
	}else if(vType == 'Fixed Asset'){
		vsubTypeValue = "fixedassetjv-Supplier Journal";
	}else if(vType == 'Pension'){
	vsubTypeValue = "pensionjv-Pension Journal";  
	}
	var array = vsubTypeValue.split("-");
	document.getElementById('voucherTypeBean.voucherNumType').value = array[0];
	document.getElementById('voucherTypeBean.voucherName').value = array[1];
	document.getElementById('voucherTypeBean.voucherSubType').value = vType;
	
	if(vType == 'JVGeneral'){
		document.getElementById('voucherTypeBean.partyBillNum').value="";
		document.getElementById('voucherTypeBean.partyName').value="";
		document.getElementById('partyBillDate').value="";
		document.getElementById('voucherTypeBean.billNum').value="";
		document.getElementById('billDate').value="";		
		
		document.getElementById('voucherTypeBean.partyBillNum').readOnly=true;
		document.getElementById('voucherTypeBean.partyName').readOnly=true;
		document.getElementById('partyBillDate').readOnly=true;
		document.getElementById('voucherTypeBean.billNum').readOnly=true;
		document.getElementById('billDate').readOnly=true;
	}
	else{
		document.getElementById('voucherTypeBean.partyBillNum').readOnly=false;
		document.getElementById('voucherTypeBean.partyName').readOnly=false;
		document.getElementById('partyBillDate').readOnly=false;
		document.getElementById('voucherTypeBean.billNum').readOnly=false;
		document.getElementById('billDate').readOnly=false;
	}
	
	if(vType == 'JVGeneral' || vType == '-1') {
		document.getElementById('partyNameDivId').style.display='none';
	} else {
		document.getElementById('partyNameDivId').style.display='inline';
	}
	
}    

function isSpecialChar(Obj)
{
  return true;
  }
</script>
