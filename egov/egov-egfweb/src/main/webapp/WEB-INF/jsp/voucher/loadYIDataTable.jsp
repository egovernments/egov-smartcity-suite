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
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<script>

		path="${pageContext.request.contextPath}";
		var totaldbamt=0,totalcramt=0;
		
		   
		
		var makeVoucherDetailTable = function() {
			var OneFunctionCenter= <s:property value="isRestrictedtoOneFunctionCenter"/>; 
		//	bootbox.alert("HHiii"+OneFunctionCenter+"<<<>>>>>") ;                             
				<s:if test='%{isRestrictedtoOneFunctionCenter == true}'>                                                   
				//bootbox.alert(OneFunctionCenter);                  
				var voucherDetailColumns = [                                       
				{key:"functionid",hidden:true, formatter:createTextFieldFormatterJV(VOUCHERDETAILLIST,".functionIdDetail","hidden")},         
				{key:"function",label:'Function Name',hidden:true, formatter:createTextFieldFormatterForFunctionJV(VOUCHERDETAILLIST,".functionDetail","hidden")},                            
				{key:"glcodeid",hidden:true,formatter:createTextFieldFormatterJV(VOUCHERDETAILLIST,".glcodeIdDetail","hidden")},
				{key:"glcode",label:'Account Code <span class="mandatory1">*</span>', formatter:createTextFieldFormatterJV(VOUCHERDETAILLIST,".glcodeDetail","text")},
				{key:"accounthead", label:'Account Head',formatter:createLongTextFieldFormatterJV(VOUCHERDETAILLIST,".accounthead")},				
				{key:"debitamount",label:'Debit Amount', formatter:createAmountFieldFormatterJV(VOUCHERDETAILLIST,".debitAmountDetail","updateDebitAmountJV()")}, 
				{key:"creditamount",label:'Credit Amount', formatter:createAmountFieldFormatterJV(VOUCHERDETAILLIST,".creditAmountDetail","updateCreditAmountJV()")},
				{key:'Add',label:'Add',formatter:createAddImageFormatter("${pageContext.request.contextPath}")},
				{key:'Delete',label:'Delete',formatter:createDeleteImageFormatter("${pageContext.request.contextPath}")}
			];
				</s:if>
				<s:else>
			var voucherDetailColumns = [   
 				{key:"glcodeid",hidden:true, formatter:createTextFieldFormatterJV(VOUCHERDETAILLIST,".glcodeIdDetail","hidden")},
 				{key:"glcode",label:'Account Code <span class="mandatory1">*</span>', formatter:createTextFieldFormatterJV(VOUCHERDETAILLIST,".glcodeDetail","text")},
 				{key:"accounthead", label:'Account Head',formatter:createLongTextFieldFormatterJV(VOUCHERDETAILLIST,".accounthead")},				
 				{key:"debitamount",label:'Debit Amount', formatter:createAmountFieldFormatterJV(VOUCHERDETAILLIST,".debitAmountDetail","updateDebitAmountJV()")}, 
 				{key:"creditamount",label:'Credit Amount', formatter:createAmountFieldFormatterJV(VOUCHERDETAILLIST,".creditAmountDetail","updateCreditAmountJV()")},
 				{key:'Add',label:'Add',formatter:createAddImageFormatter("${pageContext.request.contextPath}")},
 				{key:'Delete',label:'Delete',formatter:createDeleteImageFormatter("${pageContext.request.contextPath}")}
 			];	</s:else>			
		        
	    var voucherDetailDS = new YAHOO.util.DataSource(); 
		billDetailsTable = new YAHOO.widget.DataTable("billDetailTable",voucherDetailColumns, voucherDetailDS);
		billDetailsTable.on('cellClickEvent',function (oArgs) {
			var target = oArgs.target;
			var record = this.getRecord(target);
			var column = this.getColumn(target);
			if (column.key == 'Add') { 
				billDetailsTable.addRow({SlNo:billDetailsTable.getRecordSet().getLength()+1});
				updateAccountTableIndex();
			}
			if (column.key == 'Delete') { 	
				if(this.getRecordSet().getLength()>1){			
					this.deleteRow(record);
					allRecords=this.getRecordSet();
					for(var i=0;i<allRecords.getLength();i++){
						this.updateCell(this.getRecord(i),this.getColumn('SlNo'),""+(i+1));
					}
					updateDebitAmountJV();updateCreditAmountJV();
					check();
					loadSlFunction();
				}
				else{
					bootbox.alert("This row can not be deleted");
				}
			}
			
			        
		});
		<s:iterator value="billDetailslist" status="stat">
			<s:if test='%{isRestrictedtoOneFunctionCenter == true}'>
				billDetailsTable.addRow({SlNo:billDetailsTable.getRecordSet().getLength()+1,
					"functionid":'<s:property value="functionIdDetail"/>',
					"function":'<s:property value="functionDetail"/>',
					"glcodeid":'<s:property value="glcodeIdDetail"/>',
					"glcode":'<s:property value="glcodeDetail"/>',
					"accounthead":'<s:property value="accounthead"/>',
					"debitamount":'<s:property value="%{debitAmountDetail}"/>',
					"creditamount":'<s:property value="%{creditAmountDetail}"/>'
				});
			</s:if>
			<s:else>
				billDetailsTable.addRow({SlNo:billDetailsTable.getRecordSet().getLength()+1,
					"glcodeid":'<s:property value="glcodeIdDetail"/>',
					"glcode":'<s:property value="glcodeDetail"/>',
					"accounthead":'<s:property value="accounthead"/>',
					"debitamount":'<s:property value="%{debitAmountDetail}"/>',
					"creditamount":'<s:property value="%{creditAmountDetail}"/>'
				});
			</s:else>
				var index = '<s:property value="#stat.index"/>';
				<s:if test='%{isRestrictedtoOneFunctionCenter == true}'>
				updateGridPJV('functionIdDetail',index,'<s:property value="functionIdDetail"/>');
				updateGridPJV('functionDetail',index,'<s:property value="functionDetail"/>');
				</s:if>
				updateGridPJV('glcodeIdDetail',index,'<s:property value="glcodeIdDetail"/>');
				updateGridPJV('glcodeDetail',index,'<s:property value="glcodeDetail"/>');
				updateGridPJV('accounthead',index,'<s:property value="accounthead"/>');
				updateGridPJV('debitAmountDetail',index,'<s:property value="debitAmountDetail"/>');
				updateGridPJV('creditAmountDetail',index,'<s:property value="creditAmountDetail"/>');
				totaldbamt = totaldbamt+parseFloat('<s:property value="debitAmountDetail"/>');
				totalcramt = totalcramt+parseFloat('<s:property value="creditAmountDetail"/>');
				updateAccountTableIndex();	
			</s:iterator>
				
	
		var tfoot = billDetailsTable.getTbodyEl().parentNode.createTFoot();
		var tr = tfoot.insertRow(-1);
		var th = tr.appendChild(document.createElement('th'));
		<s:if test='%{isRestrictedtoOneFunctionCenter == true}'>
		th.colSpan = 5;
		</s:if>
		<s:else>
		th.colSpan = 4;
		</s:else>
		
		th.innerHTML = 'Total&nbsp;&nbsp;&nbsp;';
		th.align='right';
		th.style.borderTop = "1px solid #84B1AD";
		var td = tr.insertCell(-1);
		td.innerHTML="<input type='text' style='text-align:right;width:100px;'  id='totaldbamount' name='totaldbamount' readonly='true' tabindex='-1'/>";
		td.style.borderTop = "1px solid #84B1AD";
		var td = tr.insertCell(-1);
		td.align="right";
		td.style.borderTop = "1px solid #84B1AD";
		td.innerHTML="<input type='text' style='text-align:right;width:100px;'  id='totalcramount' name='totalcramount' readonly='true' tabindex='-1'/>";
		document.getElementById('totaldbamount').value=totaldbamt;
		document.getElementById('totalcramount').value=totalcramt;
		var td = tr.insertCell(-1);
		td.style.borderTop = "1px solid #84B1AD";
		var td = tr.insertCell(-1);
		td.style.borderTop = "1px solid #84B1AD";
	}         
	var glcodeOptions=[{label:"---Select---", value:"0"}];
	<s:iterator value="dropdownData.glcodeList">
	    glcodeOptions.push({label:'<s:property value="glcode"/>', value:'<s:property value="id"/>'})
	</s:iterator>
	var detailtypeOptions=[{label:"---Select---", value:"0"}];
	<s:iterator value="dropdownData.detailTypeList">
	    detailtypeOptions.push({label:'<s:property value="name"/>', value:'<s:property value="id"/>'})
	</s:iterator>
	var funcOptions=[{label:"---Select---", value:"0"}];
	var OneFunctionCenter= <s:property value="isRestrictedtoOneFunctionCenter"/>;       
	var makeSubLedgerTable = function() {
		//var OneFunctionCenter= <s:property value="isRestrictedtoOneFunctionCenter"/>;  
		//bootbox.alert("OneFunctionCenter"+OneFunctionCenter);        
		<s:if test='%{isRestrictedtoOneFunctionCenter == true}'>                                    
		var subledgerColumns = [                         
		    {key:"functionDetail",label:'Function Name',hidden:true, formatter:createSLDropdownFormatterFuncJV(SUBLEDGERLIST,".functionDetail"),dropdownOptions:funcOptions},
			{key:"glcode",hidden:true, formatter:createSLTextFieldFormatterJV(SUBLEDGERLIST,".subledgerCode","hidden")},
			{key:"glcode.id",label:'Account Code <span class="mandatory1">*</span>', formatter:createDropdownFormatterJV(SUBLEDGERLIST,"loaddropdown(this)"),  dropdownOptions:glcodeOptions},
			{key:"detailTypeName",hidden:true, formatter:createSLTextFieldFormatterJV(SUBLEDGERLIST,".detailTypeName","hidden")},
			{key:"detailType.id",label:'Type <span class="mandatory1">*</span>', formatter:createDropdownFormatterJV1(SUBLEDGERLIST),dropdownOptions:detailtypeOptions},
			{key:"detailCode",label:'Code <span class="mandatory1">*</span>', formatter:createSLDetailCodeTextFieldFormatterJV(SUBLEDGERLIST,".detailCode","splitEntitiesDetailCode(this)", ".search", "openSearchWindowFromJV(this)")},
			{key:"detailKeyId",hidden:true, formatter:createSLHiddenFieldFormatterJV(SUBLEDGERLIST,".detailKeyId")},
			{key:"detailKey",label:'Name', formatter:createSLLongTextFieldFormatterJV(SUBLEDGERLIST,".detailKey","")},
			{key:"amount",label:'Amount', formatter:createSLAmountFieldFormatterJV(SUBLEDGERLIST,".amount")},
			{key:'Add',label:'Add',formatter:createAddImageFormatter("${pageContext.request.contextPath}")},
			{key:'Delete',label:'Delete',formatter:createDeleteImageFormatter("${pageContext.request.contextPath}")}
		];   
		</s:if>
		<s:else>    
		var subledgerColumns = [      
		{key:"glcode",hidden:true, formatter:createSLTextFieldFormatterJV(SUBLEDGERLIST,".subledgerCode","hidden")},
		{key:"glcode.id",label:'Account Code <span class="mandatory1">*</span>', formatter:createDropdownFormatterJV(SUBLEDGERLIST,"loaddropdown(this)"),  dropdownOptions:glcodeOptions},
		{key:"detailTypeName",hidden:true, formatter:createSLTextFieldFormatterJV(SUBLEDGERLIST,".detailTypeName","hidden")},
		{key:"detailType.id",label:'Type <span class="mandatory1">*</span>', formatter:createDropdownFormatterJV1(SUBLEDGERLIST),dropdownOptions:detailtypeOptions},
		{key:"detailCode",label:'Code <span class="mandatory1">*</span>', formatter:createSLDetailCodeTextFieldFormatterJV(SUBLEDGERLIST,".detailCode","splitEntitiesDetailCode(this)", ".search", "openSearchWindowFromJV(this)")},
		{key:"detailKeyId",hidden:true, formatter:createSLHiddenFieldFormatterJV(SUBLEDGERLIST,".detailKeyId")},
		{key:"detailKey",label:'Name', formatter:createSLLongTextFieldFormatterJV(SUBLEDGERLIST,".detailKey","")},
		{key:"amount",label:'Amount', formatter:createSLAmountFieldFormatterJV(SUBLEDGERLIST,".amount")},
		{key:'Add',label:'Add',formatter:createAddImageFormatter("${pageContext.request.contextPath}")},
		{key:'Delete',label:'Delete',formatter:createDeleteImageFormatter("${pageContext.request.contextPath}")}
		];              
	</s:else>            
		
	    var subledgerDS = new YAHOO.util.DataSource(); 
		subLedgersTable = new YAHOO.widget.DataTable("subLedgerTable",subledgerColumns, subledgerDS);
		subLedgersTable.on('cellClickEvent',function (oArgs) {
			var target = oArgs.target;
			var record = this.getRecord(target);
			var column = this.getColumn(target);
			if (column.key == 'Add') { 
				subLedgersTable.addRow({SlNo:subLedgersTable.getRecordSet().getLength()+1});
				updateSLTableIndex();
				check();
				loadSlFunction();
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
		<s:iterator value="subLedgerlist" status="stat">
			<s:if test='%{isRestrictedtoOneFunctionCenter == true}'>  
				subLedgersTable.addRow({SlNo:subLedgersTable.getRecordSet().getLength()+1,
					"functionDetail":'<s:property value="functionDetail"/>',
					"glcode":'<s:property value="subledgerCode"/>',
					"glcode.id":'<s:property value="glcode.id"/>',
					"detailType.id":'<s:property value="detailType.id"/>',
					"detailTypeName":'<s:property value="detailTypeName" />',
					"detailCode":'<s:property value="detailCode"/>',
					"detailKeyId":'<s:property value="detailKeyId"/>',
					"detailKey":'<s:property value="detailKeyEscSpecChar" />',
					"debitAmount":'<s:property value="%{debitAmount}"/>',
					"creditAmount":'<s:property value="%{creditAmount}"/>'
				});
			</s:if>
			<s:else>    
				subLedgersTable.addRow({SlNo:subLedgersTable.getRecordSet().getLength()+1,
					"glcode":'<s:property value="subledgerCode"/>',
					"glcode.id":'<s:property value="glcode.id"/>',
					"detailType.id":'<s:property value="detailType.id"/>',
					"detailTypeName":'<s:property value="detailTypeName" />',
					"detailCode":'<s:property value="detailCode"/>',
					"detailKeyId":'<s:property value="detailKeyId"/>',
					"detailKey":'<s:property value="detailKeyEscSpecChar" />',
					"debitAmount":'<s:property value="%{debitAmount}"/>',
					"creditAmount":'<s:property value="%{creditAmount}"/>'
				});
			</s:else>    
				var index = '<s:property value="#stat.index"/>';
				<s:if test='%{isRestrictedtoOneFunctionCenter == true}'>  
				updateGridSLDropdownPJV('functionDetail',index,'<s:property value="functionDetail"/>');
				</s:if>
				updateSLGridPJV('subledgerCode',index,'<s:property value="subledgerCode"/>');
				updateSLGridPJV('detailTypeName',index,'<s:property value="detailTypeName"/>');
				updateSLGridPJV('detailCode',index,'<s:property value="detailCode"/>');
				updateSLGridPJV('detailKeyId',index,'<s:property value="detailKeyId"/>');
				updateSLGridPJV('detailKey',index,'<s:property value="detailKeyEscSpecChar" />');
				updateSLGridPJV('amount',index,'<s:property value="amount"/>');
				updateSLTableIndex();
			</s:iterator>
		
	}
	



// logic to re-populate the account code in the account code drop down in the subledger table data grid.
// basicaly required when validation fails.
function populateslDropDown(){
	var accountCodes=new Array();
	for(var i=0;i<billDetailTableIndex+1;i++){
	if(null != document.getElementById('billDetailslist['+i+'].glcodeDetail')){
		accountCodes[i] = document.getElementById('billDetailslist['+i+'].glcodeDetail').value;
	}
	}
	var url = path+'/voucher/common-getDetailCode.action?accountCodes='+accountCodes;
	var transaction = YAHOO.util.Connect.asyncRequest('POST', url, callbackJV, null);
}
var callbackJV = {
success: function(o) {
		var test= o.responseText;
		test = test.split('~');
		slAccountCodes.length=0;
		for (var j=0; j<slDetailTableIndex;j++ )
		{
			var d=document.getElementById('subLedgerlist['+j+'].glcode.id');
			if(null != d && test.length >1 && d.value ==0 )
			{
				
				d.options.length=((test.length)/2)+1;
				for (var i=1; i<((test.length)/2)+1;i++ )
				{
					d.options[i].text=test[i*2-2];
					d.options[i].value=test[i*2 -1];
					slAccountCodes.push(test[i*2 -1]);
				}
			} 
			if(test.length<2)
			{
				var d = document.getElementById('subLedgerlist['+j+'].glcode.id');
				if(d)
				{
				d.options.length=1;
				d.options[0].text='---Select---';
				d.options[0].value=0;
				}
			}
		}
		
			// logic to re-populate the account code in the account code drop down in the subledger table data grid.
			// basicaly required when validation fails.
			<s:iterator value="subLedgerlist" status="stat">
				if('<s:property value="glcode.id"/>' !="" || '<s:property value="glcode.id"/>' !=0){
					var index = '<s:property value="#stat.index"/>';
					updateGridSLDropdownGL('glcode.id',index,'<s:property value="glcode.id"/>');
				}
				
			</s:iterator>
	loadSlFunction();// load the functions in the SL grid after validation fails.
    },
    failure: function(o) {
    	bootbox.alert('failure');
    }
}
function updateGridSLDropdownGL(field,index,value){

	document.getElementById('subLedgerlist['+index+'].'+field).value=value;
	loadDetailType(index);
}

var loadDetailType = function(index) { 
		var subledgerid=document.getElementById('subLedgerlist['+index+'].glcode.id');
		var accountCode = subledgerid.options[subledgerid.selectedIndex].text;
		document.getElementById('subLedgerlist['+index+'].subledgerCode').value =accountCode;
		var url = path+'/voucher/common-getDetailType.action?accountCode='+accountCode+'&index='+index;
		var transaction = YAHOO.util.Connect.asyncRequest('POST', url, postType, null);
};
var postType = {
success: function(o) {
		var detailType= o.responseText;
		var detailRecord = detailType.split('#');
		var eachItem;
		var obj;
		for(var i=0;i<detailRecord.length;i++)
		{
			eachItem =detailRecord[i].split('~');
			if(obj==null)
			{
				obj = document.getElementById('subLedgerlist['+parseInt(eachItem[0])+']'+'.detailType.id');
				if(obj!=null)
					obj.options.length=detailRecord.length+1;
			}
			if(obj!=null)
			{
				obj.options[i+1].text=eachItem[1];
				obj.options[i+1].value=eachItem[2];
				document.getElementById('subLedgerlist['+parseInt(eachItem[0])+']'+'.detailTypeName').value = eachItem[1];
			}
			
			if(eachItem.length==1) // for deselect the subledger code
			{
				var d = document.getElementById('subLedgerlist['+i+'].detailType.id');
				d.options.length=1;
				d.options[0].text='---Select---';
				d.options[0].value=0;
			}
		} 
			<s:iterator value="subLedgerlist" status="stat">
			if('<s:property value="detailType.id"/>' !="" || '<s:property value="detailType.id"/>' !=0){
				var index = '<s:property value="#stat.index"/>';
				updateSLDetailDropdown('detailType.id',index,'<s:property value="detailType.id"/>');
				<s:if test='%{isRestrictedtoOneFunctionCenter == true}'>  
				updateGridSLDropdownPJV('functionDetail',index,'<s:property value="functionDetail"/>');
				</s:if>
			}
				
			</s:iterator>
		
		
    },
    failure: function(o) {
    	bootbox.alert('failure');
    }
}

function updateSLDetailDropdown(field,index,value){
	document.getElementById('subLedgerlist['+index+'].'+field).value=value;
}




	</script>

<style type="text/css">
#codescontainer {
	position: absolute;
	left: 11em;
	width: 9%;
	text-align: left;
}

#codescontainer .yui-ac-content {
	position: absolute;
	width: 600px;
	border: 1px solid #404040;
	background: #fff;
	overflow: hidden;
	z-index: 9050;
}

#codescontainer .yui-ac-shadow {
	position: absolute;
	margin: .3em;
	width: 300px;
	background: #a0a0a0;
	z-index: 9049;
}

#codescontainer ul {
	padding: 5px 0;
	width: 100%;
}

#codescontainer li {
	padding: 0 5px;
	cursor: default;
	white-space: nowrap;
}

#codescontainer li.yui-ac-highlight {
	background: #ff0;
}

#codescontainer li.yui-ac-prehighlight {
	background: #FFFFCC;
}

.yui-skin-sam tr.yui-dt-odd {
	background-color: #FFF;
}
</style>
