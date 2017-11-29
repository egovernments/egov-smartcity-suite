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


<%@ include file="/includes/taglibs.jsp"%>
<%@ page language="java"%>
<script>
var path="${pageContext.request.contextPath}";
var earningsList="earningsList";
var deductionsList="deductionsList";
function fillNeighbourAfterSplitFunction(obj,prefix)
{
	var temp = obj.value;
	temp = temp.split("`-`");
	if(temp.length>1)
	{ 
		obj.value=temp[0];
		var currRow=getRowIndex(obj);
		document.getElementById(prefix+'['+currRow+'].functionid').value=temp[1];
	}
}

var validationCallback = {
	success: function(o) {
		var res= o.responseText;
		res = res.split('~');
		if(res.length>2)
		{
			document.getElementById('subledgerList['+parseInt(res[0])+']'+'.detailKeyId').value=res[1];
			document.getElementById('subledgerList['+parseInt(res[0])+']'+'.detailKey').value=res[2];
		}
		else
		{
			bootbox.alert('Enter valid Code');
			document.getElementById('subledgerList['+parseInt(res[0])+']'+'.detailKeyId').value='';
			document.getElementById('subledgerList['+parseInt(res[0])+']'+'.detailKey').value='';
			return;
		}
    },
    failure: function(o) {
    	bootbox.alert('failure');
    }
}
function validateDetailCode(obj){
	var index = getRowIndex(obj);
	var element = document.getElementById('subledgerList['+index+']'+'.detailType.id');
	var detailtypeid = element.options[element.selectedIndex].value;
	var url = path+'/voucher/common!ajaxValidateDetailCode.action?code='+obj.value+'&detailtypeid='+detailtypeid+'&index='+index;
	var transaction = YAHOO.util.Connect.asyncRequest('POST', url, validationCallback, null);
}


var callback = {
success: function(o) {
		var detailType= o.responseText;
		var detailRecord = detailType.split('#');
		var eachItem;
		var obj = null;
		for(var i=0;i<detailRecord.length;i++){
			eachItem =detailRecord[i].split('~');
			if(obj==null){
				obj = document.getElementById('subledgerList['+parseInt(eachItem[0])+']'+'.detailType.id');
			}
			if(obj!=null){
			    var optionEl = document.createElement("option");
                optionEl.value = eachItem[2];
                optionEl.innerHTML = eachItem[1];
				optionEl = obj.appendChild(optionEl);
				document.getElementById('subledgerList['+parseInt(eachItem[0])+']'+'.detailKey').value = eachItem[1];
			}
		} 
    },
    failure: function(o) {
    	bootbox.alert('failure');
    }
}

function wait(msecs){
	var start = new Date().getTime();
	var cur = start
	while(cur - start < msecs)
	{
	cur = new Date().getTime();
	}
} 

var codeObject = [];
var detailCodeCallback = {
success: function(o) {
		var result = o.responseText;
		r = result.split(",")
		codeObject = new YAHOO.widget.DS_JSArray(r);
    },
    failure: function(o) {
    	bootbox.alert('failure');
    }
}

	function autoCompleteSubledgerCode(element,myEvent){
		//bootbox.alert(codeObject)
		var yuiflag = new Array();
		var src = element;	
		var target = document.getElementById('detailcodescontainer');	
		var posSrc=findPos(src); 
		target.style.left=posSrc[0];	                   
		target.style.top=posSrc[1]+22;      
		target.style.width=450;	
		      		
		var coaCodeObj=element;
		var  currRow=getRowIndex(element);
		//40 --> Down arrow, 38 --> Up arrow
		if(yuiflag[currRow] == undefined){
			var key = window.event ? window.event.keyCode : myEvent.charCode;  
			if(key != 40 )
			{
				if(key != 38 )
				{
					var oAutoComp = new YAHOO.widget.AutoComplete(coaCodeObj,'detailcodescontainer', codeObject);
					oAutoComp.queryDelay = 0;
					oAutoComp.prehighlightClassName = "yui-ac-prehighlight";
					oAutoComp.useShadow = true;
					oAutoComp.maxResultsDisplayed = 15;
					oAutoComp.useIFrame = true;
				}
			}
			yuiflag[currRow] = 1;
		}	
	}

		var onDropDownChangeEvent = function(obj,index) { 
				var subledgerid=document.getElementById('subledgerList['+index+'].glcode.id');
				var accountCode = subledgerid.options[subledgerid.selectedIndex].text;
				var url = path+'/voucher/common!getDetailType.action?accountCode='+accountCode+'&index='+index;
				var transaction = YAHOO.util.Connect.asyncRequest('POST', url, callback, null);
		};

		function getDetailCode(element,myEvent) {
				index = element.id.split('[')[1].split(']')[0]
				var detailtypeid=document.getElementById('subledgerList['+index+'].detailType.id')
				var id = detailtypeid.options[detailtypeid.selectedIndex].value;
				var url = path+'/voucher/common!ajaxGetDetailCode.action?detailtypeid='+id+'&index='+index;
				var transaction = YAHOO.util.Connect.asyncRequest('POST', url, detailCodeCallback, null);
		}

function createDropdownFormatterForAccountCode(prefix){
    return function(el, oRecord, oColumn, oData) {
        var selectedValue = (lang.isValue(oData)) ? oData : oRecord.getData(oColumn.field),
            options = (lang.isArray(oColumn.dropdownOptions)) ?
                oColumn.dropdownOptions : null,
            selectEl,
            collection = el.getElementsByTagName("select");
        if(collection.length === 0) {
            selectEl = document.createElement("select");
            selectEl.className = YAHOO.widget.DataTable.CLASS_DROPDOWN;
            index = subledgerTable.getRecordIndex(oRecord);
            selectEl.name = prefix+'['+index+'].'+oColumn.getKey();
			selectEl.id = prefix+'['+index+'].'+oColumn.getKey();
            selectEl = el.appendChild(selectEl);
            YAHOO.util.Event.addListener(selectEl,"change",onDropDownChangeEvent,index,this);
        }

        selectEl = collection[0];

        if(selectEl) {
            selectEl.innerHTML = "";
            if(options) {
                for(var i=0; i<options.length; i++) {
                    var option = options[i];
                    var optionEl = document.createElement("option");
                    optionEl.value = (lang.isValue(option.value)) ?
                            option.value : option;
                    optionEl.innerHTML = (lang.isValue(option.text)) ?
                            option.text : (lang.isValue(option.label)) ? option.label : option;
                    optionEl = selectEl.appendChild(optionEl);
                    if (optionEl.value == selectedValue) {
                        optionEl.selected = true;
                    }
                }
            }
            else {
                selectEl.innerHTML = "<option selected value=\"" + selectedValue + "\">" + selectedValue + "</option>";
            }
        }
        else {
            el.innerHTML = lang.isValue(oData) ? oData : "";
        }
    }
}

		function computeSubledgersTotalCrAmount(target){
			if(isNaN(parseInt(target.value))){
				bootbox.alert("Please enter a valid amount")
				target.value = 0;
			}
			element = document.getElementById('totalSubLedgerCreditamount');
			if(element.value == ""){
				element.value = 0;
			}
			element.value = parseInt(element.value) + parseInt(target.value);
		}
		
		function computeSubledgersTotalDrAmount(target){
			if(isNaN(parseInt(target.value))){
				bootbox.alert("Please enter a valid amount")
				target.value = 0;
			}
			element = document.getElementById('totalSubLedgerDebitamount');
			if(element.value == ""){
				element.value = 0;
			}
			element.value = parseInt(element.value) + parseInt(target.value);
		}
		function computeEarningsTotalAmount(target){
			if(isNaN(parseInt(target.value))){
				bootbox.alert("Please enter a valid amount")
				target.value = 0;
			}
			element = document.getElementById('netPayList[0].creditamount');
			if(element.value == ""){
				element.value = 0;
			}
			element.value = parseInt(element.value) + parseInt(target.value);
			total = document.getElementById('totaldramount');
			total.value =  parseInt(total.value) + parseInt(target.value);
		}
		
		function computeDeductionsTotalAmount(target){
			if(isNaN(parseInt(target.value))){
				bootbox.alert("Please enter a valid amount")
				target.value = 0;
			}
			element = document.getElementById('netPayList[0].creditamount');
			if(element.value == ""){
				element.value = 0;
			}
			element.value = parseInt(element.value) - parseInt(target.value);
			total = document.getElementById('totalcramount');
			total.value =  parseInt(total.value) + parseInt(target.value);
		}

		function getReadOnlyTextFieldFormatter(tableName,prefix,suffix,width){
		    return function(el, oRecord, oColumn, oData) {
				var value = (YAHOO.lang.isValue(oData))?oData:0;
				table = eval(tableName);
				el.innerHTML = "<input type='text' id='"+prefix+"["+table.getRecordIndex(oRecord)+"]"+suffix+"' name='"+prefix+"["+table.getRecordIndex(oRecord)+"]"+suffix+"' value='"+value+"' style='text-align:left;width:"+width+"px;' readOnly='readonly'/>";
			}
		}

		function getTextFieldFormatter(tableName,prefix,suffix,width){
		    return function(el, oRecord, oColumn, oData) {
				var value = (YAHOO.lang.isValue(oData))?oData:0;
				table = eval(tableName);
				el.innerHTML = "<input type='text' id='"+prefix+"["+table.getRecordIndex(oRecord)+"]"+suffix+"' name='"+prefix+"["+table.getRecordIndex(oRecord)+"]"+suffix+"' value='"+value+"' style='text-align:left;width:"+width+"px;'/>";
			}
		}
		function getAmountFieldFormatter(tableName,prefix,suffix,width,functionName){
		    return function(el, oRecord, oColumn, oData) {
				var value = (YAHOO.lang.isValue(oData))?oData:0;
				table = eval(tableName);
				el.innerHTML = "<input type='text' id='"+prefix+"["+table.getRecordIndex(oRecord)+"]"+suffix+"' name='"+prefix+"["+table.getRecordIndex(oRecord)+"]"+suffix+"' value='"+value+"' style='text-align:right;width:"+width+"px;' onBlur='"+functionName+"'/>";
			}
		}

		function createTextFieldFormatterForCode(tableName,prefix,suffix,onclickfunction,onblurfunction){
		    return function(el, oRecord, oColumn, oData) {
		    	table = eval(tableName);
				el.innerHTML = "<input type='text' id='"+prefix+"["+table.getRecordIndex(oRecord)+"]"+suffix+"' name='"+prefix+"["+table.getRecordIndex(oRecord)+"]"+suffix+"' class='yui-ac-input' onkeyup='"+onclickfunction+"' onblur='"+onblurfunction+"'/>";
			}
		}

	var glcodeOptions=[{label:"--- Select ---", value:"0"}];
	<s:iterator value="dropdownData.glcodeList">
	    glcodeOptions.push({label:'<s:property value="glcode"/>', value:'<s:property value="id"/>'})
	</s:iterator>
	var detailtypeOptions=[{label:"--- Select ---", value:"0"}];
	<s:iterator value="dropdownData.detailTypeList">
	    detailtypeOptions.push({label:'<s:property value="name"/>', value:'<s:property value="id"/>'})
	</s:iterator>
	
		var EARNINGS_SUBLEDGER_TABLENAME="subledgerTable";
		var makeSubledgerTable = function() {
		var subledgerColumns = [ 
			{key:"SlNo",label:'Sl No',width:30},
			{key:"glcode",hidden:true,width:90, formatter:createSLTextFieldFormatterJV(EARNINGS_SUBLEDGER_TABLENAME,".subledgerCode","hidden")},
			{key:"glcode.id",label:'Account Code <span class="mandatory">*</span>',width:100, formatter:createDropdownFormatterForAccountCode("subledgerList","loaddropdown(this)"),  dropdownOptions:glcodeOptions},
			{key:"detailTypeName",hidden:true,width:90, formatter:createSLTextFieldFormatterJV(EARNINGS_SUBLEDGER_TABLENAME,".detailTypeName","hidden")},
			{key:"detailType.id",label:'Type <span class="mandatory">*</span>',width:90, formatter:createDropdownFormatterForAccountCode("subledgerList"),dropdownOptions:detailtypeOptions},
			{key:"detailCode",label:'Code <span class="mandatory">*</span>',width:90, formatter:createTextFieldFormatterForCode(EARNINGS_SUBLEDGER_TABLENAME,"subledgerList",".detailCode","getDetailCode(this,event);autoCompleteSubledgerCode(this,event);","validateDetailCode(this)")},
			{key:"detailKeyId",hidden:true,width:100, formatter:getReadOnlyTextFieldFormatter(EARNINGS_SUBLEDGER_TABLENAME,"subledgerList",".detailKeyId","99")},
			{key:"detailKey",label:'Name',width:180, formatter:getReadOnlyTextFieldFormatter(EARNINGS_SUBLEDGER_TABLENAME,"subledgerList",".detailKey","179")},
			{key:"debitamount",label:'Debit Amount',width:130, formatter:getAmountFieldFormatter(EARNINGS_SUBLEDGER_TABLENAME,"subledgerList",".debitAmount","129","computeSubledgersTotalDrAmount(this)")},
			{key:"creditamount",label:'Credit Amount',width:130, formatter:getAmountFieldFormatter(EARNINGS_SUBLEDGER_TABLENAME,"subledgerList",".creditAmount","129","computeSubledgersTotalCrAmount(this)")},
			{key:'Add',label:'Add',formatter:createAddImageFormatter("${pageContext.request.contextPath}")},
			{key:'Delete',label:'Delete',formatter:createDeleteImageFormatter("${pageContext.request.contextPath}")}
		];
	    var subledgerDS = new YAHOO.util.DataSource(); 
		subledgerTable = new YAHOO.widget.DataTable("subledgerTable",subledgerColumns, subledgerDS);
		subledgerTable.on('cellClickEvent',function (oArgs) {
			var target = oArgs.target;
			var record = this.getRecord(target);
			var column = this.getColumn(target);
			if (column.key == 'Add') { 
				subledgerTable.addRow({SlNo:subledgerTable.getRecordSet().getLength()+1});
			}
			if (column.key == 'Delete') { 	
				if(this.getRecordSet().getLength()>1){			
					this.deleteRow(record);
					allRecords=this.getRecordSet();
					for(var i=0;i<allRecords.getLength();i++){
						this.updateCell(this.getRecord(i),this.getColumn('SlNo'),""+(i+1));
					}
				}
				else{
					bootbox.alert("This row can not be deleted");
				}
			}
		});
		<s:iterator value="subledgerList" status="stat">
				subledgerTable.addRow({SlNo:subledgerTable.getRecordSet().getLength()+1,
					"glcode":'<s:property value="subledgerCode"/>',
					"glcode.id":'<s:property value="glcode.id"/>',
					"accountDetailTypeId.id":'<s:property value="accountDetailTypeId"/>',
					"detailTypeName":'<s:property value="detailTypeName"/>',
					"detailCode":'<s:property value="detailCode"/>',
					"accountDetailKeyId":'<s:property value="accountDetailKeyId"/>',
					"narration":'<s:property value="narration"/>',
					"debitAmount":'<s:property value="%{debitAmount}"/>',
					"creditAmount":'<s:property value="%{creditAmount}"/>'
				});
			</s:iterator>
		var tfoot = subledgerTable.getTbodyEl().parentNode.createTFoot();
		var tr = tfoot.insertRow(-1);
		var th = tr.appendChild(document.createElement('th'));
		th.colSpan = 8;
		th.innerHTML = 'Total&nbsp;&nbsp;&nbsp;';
		th.align='right';
		var td = tr.insertCell(-1);
		td.width="90"
		td.align="right"
		td.innerHTML="<input type='text' style='text-align:right;width:130px;'  id='totalSubLedgerDebitamount' name='totalSubLedgerDebitamount' value='0' readonly='true' tabindex='-1'/>";
		var td = tr.insertCell(-1);
		td.width="90"
		td.align="right"
		td.innerHTML="<input type='text' style='text-align:right;width:130px;'  id='totalSubLedgerCreditamount' name='totalSubLedgerCreditamount' value='0' readonly='true' tabindex='-1'/>";
	}
	
</script>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td width="9%" class="bluebox">&nbsp;</td>
		<td width="18%" class="bluebox"><s:text name="billDate" /></>:<span
			class="mandatory">*</span></td>
		<td width="23%" class="bluebox"><input type="text"
			name="model.billdate" value='<s:property value="model.billdate"/>'
			id="billdate" onkeyup="DateFormat(this,this.value,event,false,'3')" />
			<a href="javascript:show_calendar('salaryBill.billdate');"
			style="text-decoration: none">&nbsp;<img tabIndex="-1"
				src="/egi/resources/erp2/images/calendaricon.gif" border="0" /></A>(dd/mm/yyyy)</td>
		</td>
		<td width="17%" class="bluebox">&nbsp;</td>
		<td width="33%" class="bluebox"></td>
	</tr>
	<tr>
		<td class="greybox">&nbsp;</td>
		<td class="greybox">For the Month of:<span class="mandatory">*</span></td>
		<td class="greybox"><select name="billregistermis.month"
			id="month" value='<s:property value="billregistermis.month"/>'>
				<option value="-1" selected="selected">------Choose-----</option>
				<option value="1">January</option>
				<option value="2">February</option>
				<option value="3">March</option>
				<option value="4">April</option>
				<option value="5">May</option>
				<option value="6">June</option>
				<option value="7">July</option>
				<option value="8">August</option>
				<option value="9">September</option>
				<option value="10">October</option>
				<option value="11">November</option>
				<option value="12">December</option>
		</select></td>
		<td class="greybox"><s:text name="financialyear" />:<span
			class="mandatory">*</span></td>
		<td class="greybox"><s:select
				name="billregistermis.financialyear.id" id="financialyear"
				list="dropdownData.financialYearList" listKey="id"
				listValue="finYearRange" headerKey="-1" headerValue="----Choose----"
				value="%{billregistermis.financialyear.id}" /></td>
	</tr>
	<tr>
		<td class="bluebox">&nbsp;</td>
		<td class="bluebox"><s:text name="department" />:<span
			class="mandatory">*</span></td>
		<td class="bluebox"><s:select
				name="billregistermis.egDepartment.id" id="department"
				list="dropdownData.departmentList" listKey="id" listValue="deptName"
				headerKey="-1" headerValue="----Choose----"
				value="billregistermis.egDepartment.id" /></td>
		<td class="bluebox"><s:text name="field" />:</td>
		<td class="bluebox"><s:select name="billregistermis.fieldid.id"
				id="fieldid" list="dropdownData.fieldList" listKey="id"
				listValue="name" headerKey="-1" headerValue="----Choose----"
				value="billregistermis.fieldid.id" /></td>
	</tr>
	<tr>
		<td class="greybox">&nbsp;</td>
		<td class="greybox"><s:text name="functionary" />:</td>
		<td class="greybox"><s:select
				name="billregistermis.functionaryid.id" id="functionaryid"
				list="dropdownData.functionaryList" listKey="id" listValue="name"
				headerKey="-1" headerValue="----Choose----"
				value="billregistermis.functionaryid.id" /></td>
		<td class="greybox">&nbsp;</td>
		<td class="greybox">&nbsp;</td>
	</tr>
	<tr>
		<td class="bluebox">&nbsp;</td>
		<td class="bluebox">&nbsp;</td>
		<td class="bluebox"></td>
		<td class="bluebox">&nbsp;</td>
		<td class="bluebox">&nbsp;</td>
	</tr>
</table>

</table>
<table width="100%" border="0" cellpadding="0" cellspacing="0"
	class="tablebottom">
	<tr>
		<th colspan="8"><div class="subheadsmallnew">
				<s:text name="bill.earnings" />
			</div></th>
	</tr>
	<tr>
		<th class="bluebgheadtd" width="7%">SL No</th>
		<th class="bluebgheadtd" width="20%">Function</th>
		<th class="bluebgheadtd" width="15%">Head</th>
		<th class="bluebgheadtd" width="15%">Account Code</th>
		<th class="bluebgheadtd" width="50%">Account Description</th>
		<th class="bluebgheadtd" width="50%">Debit Amount</th>
	</tr>
	<s:iterator value="earningsList" status="stat" var="a">
		<tr>
			<td class="blueborderfortd"><div align="center">
					<input name="textfield" type="text"
						value='<s:property value="#stat.index+1"/>' size="3" />
				</div></td>
			<td class="blueborderfortd"><div align="center">
					<input value='<s:property value="#a.functionid"/>'
						name='earningsList[<s:property value="#stat.index"/>].functionid'
						type="hidden" size="1"
						id='earningsList[<s:property value="#stat.index"/>].functionid' />
					<input type="text" size="28"
						name='earningsList[<s:property value="#stat.index"/>].functionDetail'
						id='earningsList[<s:property value="#stat.index"/>].functionDetail'
						class="yui-ac-input"
						onkeyup='autocompletecodeFunction(this,event)' autocomplete='off'
						onblur='fillNeighbourAfterSplitFunction(this,"earningsList")' />
				</div></td>
			<td class="blueborderfortd"><div align="center">
					<input value='<s:property value="coaIdAndHead[#a.glcodeid]"/>'
						type="text" size="28"
						id='earningsList[<s:property value="#stat.index"/>].narration'
						readonly="readonly" />
				</div></td>
			<td class="blueborderfortd"><input
				name='earningsList[<s:property value="#stat.index"/>].glcodeid'
				type="hidden" size="1"
				id='earningsList[<s:property value="#stat.index"/>].glcodeid'
				value='<s:property value="#a.glcodeid"/>' /> <input type="text"
				size="20"
				id='earningsList[<s:property value="#stat.index"/>].glcode'
				value='<s:property value="coaAndIds[#a.glcodeid].glcode"/>'
				readonly="readonly" /></td>
			<td class="blueborderfortd"><input
				value='<s:property value="coaAndIds[#a.glcodeid].name"/>'
				name='earningsList[<s:property value="#stat.index"/>].narration'
				type="text"
				id='earningsList[<s:property value="#stat.index"/>].narration'
				size="25" readonly="readonly" /></td>
			<td class="blueborderfortd"><div align="center">
					<input
						name='earningsList[<s:property value="#stat.index"/>].debitamount'
						type="text" class="amount" size="25"
						id='earningsList[<s:property value="#stat.index"/>].debitamount'
						onblur="computeEarningsTotalAmount(this)"
						value='<s:property value="earningsList[#stat.index].debitamount"/>' />
				</div></td>
		</tr>
	</s:iterator>
	<tr>
		<td colspan="5" class="blueborderfortd1"><div align="right"
				class="bold">Total Earnings:</div></td>
		<td class="blueborderfortd1"><div align="center">
				<input name="textfield8" type="text" class="amount" size="25"
					id='totaldramount' name='totaldramount' value='0'
					readonly="readonly" />
			</div></td>
	</tr>
</table>
<br />
<div id="codescontainer"></div>

</table>
<table width="100%" border="0" cellpadding="0" cellspacing="0"
	class="tablebottom">
	<tr>
		<th colspan="8"><div class="subheadsmallnew">
				<s:text name="bill.deductions" />
			</div></th>
	</tr>
	<tr>
		<th class="bluebgheadtd" width="7%">SL No</th>
		<th class="bluebgheadtd" width="20%">Function</th>
		<th class="bluebgheadtd" width="15%">Head</th>
		<th class="bluebgheadtd" width="15%">Account Code</th>
		<th class="bluebgheadtd" width="50%">Account Description</th>
		<th class="bluebgheadtd" width="50%">Credit Amount</th>
	</tr>
	<s:iterator value="deductionsList" status="stat" var="a">
		<tr>
			<td class="blueborderfortd"><div align="center">
					<input name="textfield" type="text"
						value='<s:property value="#stat.index+1"/>' size="3" />
				</div></td>
			<td class="blueborderfortd"><div align="center">
					<input value='<s:property value="functionid"/>'
						name='deductionsList[<s:property value="#stat.index"/>].functionid'
						type="hidden" size="1"
						id='deductionsList[<s:property value="#stat.index"/>].functionid' />
					<input type="text" size="28"
						name='deductionsList[<s:property value="#stat.index"/>].functionDetail'
						id='deductionsList[<s:property value="#stat.index"/>].functionDetail'
						class="yui-ac-input"
						onkeyup='autocompletecodeFunction(this,event)' autocomplete='off'
						onblur='fillNeighbourAfterSplitFunction(this,"deductionsList")' />
				</div></td>
			<td class="blueborderfortd"><div align="center">
					<input value='<s:property value="coaIdAndHead[#a.glcodeid]"/>'
						type="text" size="28"
						id='deductionsList[<s:property value="#stat.index"/>].narration'
						readonly="readonly" />
				</div></td>
			<td class="blueborderfortd"><input
				name='deductionsList[<s:property value="#stat.index"/>].glcodeid'
				type="hidden" size="1"
				id='deductionsList[<s:property value="#stat.index"/>].glcodeid'
				value='<s:property value="#a.glcodeid"/>' /> <input type="text"
				size="20"
				id='deductionsList[<s:property value="#stat.index"/>].glcode'
				value='<s:property value="coaAndIds[#a.glcodeid].glcode"/>'
				readonly="readonly" /></td>
			<td class="blueborderfortd"><input
				value='<s:property value="coaAndIds[#a.glcodeid].name"/>'
				name='deductionsList[<s:property value="#stat.index"/>].narration'
				type="text"
				id='deductionsList[<s:property value="#stat.index"/>].narration'
				size="25" readonly="readonly" /></td>
			<td class="blueborderfortd"><div align="center">
					<input
						name='deductionsList[<s:property value="#stat.index"/>].creditamount'
						type="text" class="amount" size="25"
						id='deductionsList[<s:property value="#stat.index"/>].creditamount'
						onblur="computeDeductionsTotalAmount(this)"
						value='<s:property value="deductionsList[#stat.index].creditamount"/>' />
				</div></td>
		</tr>
	</s:iterator>
	<tr>
		<td colspan="5" class="blueborderfortd1"><div align="right"
				class="bold">Total Deductions:</div></td>
		<td class="blueborderfortd1"><div align="center">
				<input name="textfield8" type="text" class="amount" size="25"
					id='totalcramount' name='totalcramount' value='0'
					readonly="readonly" />
			</div></td>
	</tr>
</table>
<br />


<div class="subheadsmallnew" align="center">
	<s:text name="bill.subledger" />
</div>
<div class="yui-skin-sam" align="center">
	<div id="subledgerTable"></div>
</div>
<script>
			makeSubledgerTable();
			<s:if test="%{subledgerList.size() == 0 && getActionErrors().size()==0 && getFieldErrors().size()==0}">
				subledgerTable.addRow({SlNo:subledgerTable.getRecordSet().getLength()+1});
			</s:if>
		</script>
<div id="detailcodescontainer"></div>
<br />
<br />

</table>
<table width="100%" border="0" cellpadding="0" cellspacing="0"
	class="tablebottom">
	<tr>
		<th colspan="8"><div class="subheadsmallnew">
				<s:text name="bill.netpay" />
			</div></th>
	</tr>
	<tr>
		<th class="bluebgheadtd" width="15%">Head</th>
		<th class="bluebgheadtd" width="15%">Account Code</th>
		<th class="bluebgheadtd" width="50%">Account Description</th>
		<th class="bluebgheadtd" width="50%">Total Amount</th>
	</tr>
	<s:iterator value="netPayList" status="stat" var="a">
		<tr>
			<td class="blueborderfortd"><div align="center">
					<input value='<s:property value="coaIdAndHead[#a.glcodeid]"/>'
						type="text" size="28"
						id='netPayList[<s:property value="#stat.index"/>].narration'
						readonly="readonly" />
				</div></td>
			<td class="blueborderfortd"><input
				name='netPayList[<s:property value="#stat.index"/>].glcodeid'
				type="hidden" size="1"
				id='netPayList[<s:property value="#stat.index"/>].glcodeid'
				value='<s:property value="#a.glcodeid"/>' /> <input type="text"
				size="30" id='netPayList[<s:property value="#stat.index"/>].glcode'
				value='<s:property value="coaAndIds[#a.glcodeid].glcode"/>'
				readonly="readonly" /></td>
			<td class="blueborderfortd"><input
				value='<s:property value="coaAndIds[#a.glcodeid].name"/>'
				name='netPayList[<s:property value="#stat.index"/>].narration'
				type="text"
				id='netPayList[<s:property value="#stat.index"/>].narration'
				size="35" readonly="readonly" /></td>
			<td class="blueborderfortd"><div align="center">
					<input
						name='netPayList[<s:property value="#stat.index"/>].creditamount'
						type="text" class="amount" size="25"
						id='netPayList[<s:property value="#stat.index"/>].creditamount'
						readonly="readonly" value="0" />
				</div></td>
		</tr>
	</s:iterator>
</table>
<br />

<div class="subheadsmallnew"></div>
<div align="left" class="mandatory">* Mandatory Fields</div>
</div>

<script>
function validate(){
	if(document.getElementById('billdate').value == ""){
		bootbox.alert("Please enter Bill Date")
		return false;
	}
	if(document.getElementById('month').value == -1){
		bootbox.alert("Please select Month")
		return false;
	}
	if(document.getElementById('financialyear').value == -1){
		bootbox.alert("Please select Financial Year")
		return false;
	}
	if(document.getElementById('department').value == -1){
		bootbox.alert("Please select Department")
		return false;
	}
	var isSubLederAmountValid = validateSubledgerAmounts();
	return isSubLederAmountValid;
}

function validateSubledgerAmounts(){
	var map = {};
	var subLedgerMap = {};
	var amount = 0;
	<s:iterator value="earningsList" status="stat" var="a">
		map[document.getElementById('earningsList[<s:property value="#stat.index"/>].glcode').value] = document.getElementById('earningsList[<s:property value="#stat.index"/>].debitamount').value;
	</s:iterator>	
	<s:iterator value="deductionsList" status="stat" var="a">
		map[document.getElementById('deductionsList[<s:property value="#stat.index"/>].glcode').value] = document.getElementById('deductionsList[<s:property value="#stat.index"/>].creditamount').value;
	</s:iterator>
	for(j=0;j<subledgerTable.getRecordSet().getLength();j++){
		element = document.getElementById("subledgerList["+j+"].glcode.id");
		amount = subLedgerMap[element.options[element.selectedIndex].value] == null?0:subLedgerMap[element.options[element.selectedIndex].value];
		if(amount == 0){
			subLedgerMap[element.options[element.selectedIndex].text] = parseInt(document.getElementById("subledgerList["+j+"].debitAmount").value) + parseInt(document.getElementById("subledgerList["+j+"].creditAmount").value);
		}else{
			subLedgerMap[element.options[element.selectedIndex].text] = amount + parseInt(document.getElementById("subledgerList["+j+"].debitAmount").value) + parseInt(document.getElementById("subledgerList["+j+"].creditAmount").value);
		}
	}

	for (var key in subLedgerMap) {
		value = parseInt(subLedgerMap[key])-parseInt(map[key]);
		if(value==0){
			return true;
		}else{
			bootbox.alert("The sum of the subledger amount and the account code amount should match");
			return false;
		}
	}
	return true;
}
</script>
