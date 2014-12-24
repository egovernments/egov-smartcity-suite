<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<tr>
			<td class="bluebox">Voucher Sub-Type</td>
			<td class="bluebox">
				<select name="vType" id="vType" onchange="onChangeVSubType()">
					<option value="-1">------Choose------</option>
					<option value="JVGeneral">General</option>
					<option value="Works">Works</option>
					<option value="Purchase">Purchase</option>
					<option value="Fixed Asset">Fixed Asset</option>
					<option value="Salary">Salary</option>
					<option value="Expense">Expense</option>
				</select>
			</td>
			<td class="bluebox"><s:text name="jv.partyBillNum" /> </td>
			<td class="bluebox"><s:textfield name="voucherTypeBean.partyBillNum" id="voucherTypeBean.partyBillNum" value="%{voucherTypeBean.partyBillNum}"/> 				<td>
		</tr>
	<tr>
		 <td class="greybox"><s:text name="jv.partyName" /><div id="partyNameDivId" style="display:none"><span class="mandatory">*</span></div></td>	
		 <td class="greybox"><s:textfield name="voucherTypeBean.partyName" id="voucherTypeBean.partyName" value="%{voucherTypeBean.partyName}"/></td>
		 <td class="greybox"><s:text name="jv.partyBillDate" /></td>	
		 <td class="greybox"><s:date name="voucherTypeBean.partyBillDate" id="partyBillDateId" format="dd/MM/yyyy"/>
			<s:textfield name="voucherTypeBean.partyBillDate" id="partyBillDate" value="%{partyBillDateId}"  maxlength="10" size="15" onkeyup="DateFormat(this,this.value,event,false,'3')"/>
			<a href="javascript:show_calendar('jvcreateform.partyBillDate',null,null,'DD/MM/YYYY');" style="text-decoration:none">&nbsp;<img tabIndex=-1 src="${pageContext.request.contextPath}/image/calendaricon.gif" border="0"/></a>(dd/mm/yyyy)</td> 				
	</tr>
       <tr>
		 <td class="bluebox"><s:text name="jv.billNum" /></td>	
		 <td class="bluebox"><s:textfield name="voucherTypeBean.billNum" id="voucherTypeBean.billNum" value="%{voucherTypeBean.billNum}"/></td>
		 <td class="bluebox"><s:text name="jv.billDate" /></td>	
		<td class="bluebox"><s:date name="voucherTypeBean.billDate" id="billDateId" format="dd/MM/yyyy"/>
			<s:textfield name="voucherTypeBean.billDate" id="billDate" value="%{billDateId}"  maxlength="10" size="15" onkeyup="DateFormat(this,this.value,event,false,'3')"/>
			<a href="javascript:show_calendar('jvcreateform.billDate',null,null,'DD/MM/YYYY');" style="text-decoration:none">&nbsp;<img tabIndex=-1 src="${pageContext.request.contextPath}/image/calendaricon.gif" border="0"/></a>(dd/mm/yyyy)</td> 				
	</tr>
<input type="hidden" id="voucherTypeBean.voucherSubType" name="voucherTypeBean.voucherSubType" value="${voucherTypeBean.voucherSubType}"/>

<script>
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
</script>
