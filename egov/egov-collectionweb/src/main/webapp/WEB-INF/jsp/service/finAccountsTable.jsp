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

<%@ taglib prefix="s" uri="/WEB-INF/taglib/struts-tags.tld"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/autocomplete-debug.js?rnd=${app_release_no}"></script>  
<script>

		path="${pageContext.request.contextPath}";
		
		var makeAccountsDetailTable = function() {
		var accountColumns = [ 
			{key:"glcodeid",hidden:true, formatter:createTextFieldFormatter(ACCOUNTDETAILSLIST,".glCodeId.id","hidden")},
			{key:"accounthead", label:'Account Head <span class="mandatory1">*</span>',formatter:createLongTextFieldFormatter(ACCOUNTDETAILSLIST,".glCodeId.name")},
			{key:"glcode",label:'Account Code <span class="mandatory1">*</span>', formatter:createTextFieldFormatter(ACCOUNTDETAILSLIST,".glCodeId.glcode","text")},				
			{key:"amount",label:'Amount <span class="mandatory1">*</span>', formatter:createAmountFieldFormatter(ACCOUNTDETAILSLIST,".amount")}, 
			{key:'Add',label:'Add',formatter:createAddImageFormatter("${pageContext.request.contextPath}")},
			{key:'Delete',label:'Delete',formatter:createDeleteImageFormatter("${pageContext.request.contextPath}")}
		];
	    var accountDataSource = new YAHOO.util.DataSource(); 
		var accountsDetailTable = new YAHOO.widget.DataTable("accountsDetailTable",accountColumns, accountDataSource);
		accountsDetailTable.on('cellClickEvent',function (oArgs) {
			var target = oArgs.target;
			var record = this.getRecord(target);
			var column = this.getColumn(target);
			if (column.key == 'Add') { 
				accountsDetailTable.addRow({SlNo:accountsDetailTable.getRecordSet().getLength()+1});
				updateAccountTableIndex();
			}
			if (column.key == 'Delete') { 	
				if(this.getRecordSet().getLength()>1){			
					this.deleteRow(record);
					allRecords=this.getRecordSet();
					for(var i=0;i<allRecords.getLength();i++){
						this.updateCell(this.getRecord(i),this.getColumn('SlNo'),""+(i+1));
					}
					loadSLAccountCode();
				}
				else{
					bootbox.alert("This row can not be deleted");
				}
			}	        
		});

		if(jQuery('#isviewmode'))
		{
			if(jQuery('#isviewmode').val()==="true")
			{
				accountsDetailTable.hideColumn('Add');
				accountsDetailTable.hideColumn('Delete');
			}
		}
		
		<s:iterator value="accountDetails" status="stat">
				accountsDetailTable.addRow({SlNo:accountsDetailTable.getRecordSet().getLength()+1,
					"glcodeid":'<s:property value="glCodeId.id"/>',
					"glcode":'<s:property value="glCodeId.glcode"/>',
					"accounthead":'<s:property value="glCodeId.name"/>',
					"amount":'<s:property value="%{amount}"/>'
					
				});
				var index = '<s:property value="#stat.index"/>';
				updateGridAccounts('glCodeId.id',index,'<s:property value="glCodeId.id"/>');
				updateGridAccounts('glCodeId.glcode',index,'<s:property value="glCodeId.glcode"/>');
				updateGridAccounts('glCodeId.name',index,'<s:property value="glCodeId.name"/>');
				updateGridAccounts('amount',index,'<s:property value="amount"/>');
				updateAccountTableIndex();	
			</s:iterator>

	}
	
	
	var glcodeOptions=[{label:"---Select---", value:"0"}];
	var detailtypeOptions=[{label:"---Select---", value:"0"}];
	var makeSubLedgerTable = function() {
		var subledgerColumns = [ 
	
			{key:"glcode",hidden:true, formatter:createSLTextFieldFormatter(SUBLEDGERLIST,".serviceAccountDetail.glCodeId.glcode","hidden")},
			{key:"serviceAccountDetail.glCodeId.id",label:'Account Code', formatter:createDropdownFormatter(SUBLEDGERLIST,"loaddropdown(this)"),  dropdownOptions:glcodeOptions},
			{key:"detailType.name",hidden:true, formatter:createSLTextFieldFormatter(SUBLEDGERLIST,".detailType.name","hidden")},
			{key:"detailType.id",label:'Type', formatter:createDropdownFormatter1(SUBLEDGERLIST),dropdownOptions:detailtypeOptions},
			{key:"detailCode",label:'Code', formatter:createSLDetailCodeTextFieldFormatter(SUBLEDGERLIST,".detailCode","splitEntitiesDetailCode(this)")},
			{key:"detailKeyId",hidden:true, formatter:createSLHiddenFieldFormatter(SUBLEDGERLIST,".detailKeyId")},
			{key:"detailKey",label:'Name', formatter:createSLLongTextFieldFormatter(SUBLEDGERLIST,".detailKey","")},
			{key:"amount",label:'Amount', formatter:createSLAmountFieldFormatter(SUBLEDGERLIST,".amount")},
			{key:'Add',label:'Add',formatter:createAddImageFormatter("${pageContext.request.contextPath}")},
			{key:'Delete',label:'Delete',formatter:createDeleteImageFormatter("${pageContext.request.contextPath}")}
		];
	    var subledgerDS = new YAHOO.util.DataSource(); 
		subLedgersTable = new YAHOO.widget.DataTable("subLedgerTable",subledgerColumns, subledgerDS);
		subLedgersTable.on('cellClickEvent',function (oArgs) {
			var target = oArgs.target;
			var record = this.getRecord(target);
			var column = this.getColumn(target);
			if (column.key == 'Add') { 
				subLedgersTable.addRow({SlNo:subLedgersTable.getRecordSet().getLength()+1});
				updateSLTableIndex();
				loadSLAccountCode();
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
		
		if(jQuery('#isviewmode'))
		{
			if(jQuery('#isviewmode').val()==="true")
			{
				subLedgersTable.hideColumn('Add');
				subLedgersTable.hideColumn('Delete');
			}
		}
		<s:iterator value="subledgerDetails" status="stat">
				subLedgersTable.addRow({SlNo:subLedgersTable.getRecordSet().getLength()+1,
					"glcode":'<s:property value=".serviceAccountDetail.glCodeId.glcode"/>',
					"serviceAccountDetail.glCodeId.id":'<s:property value="serviceAccountDetail.glCodeId.id"/>',
					"detailType.id":'<s:property value="detailType.id"/>',
					"detailType.name":'<s:property value="detailType.name" />',
					"detailCode":'<s:property value="detailCode"/>',
					"detailKeyId":'<s:property value="detailKeyId"/>',
					"detailKey":'<s:property value="detailKey" />',
					"amount":'<s:property value="%{amount}"/>'
					
				});
				var index = '<s:property value="#stat.index"/>';
				updateSLGrid('serviceAccountDetail.glCodeId.glcode',index,'<s:property value="serviceAccountDetail.glCodeId.glcode"/>');
				updateSLGrid('serviceAccountDetail.glCodeId.id',index,'<s:property value="serviceAccountDetail.glCodeId.id"/>');
				 setTimeout(function(){
					 updateSLDetailDropdown('detailType.id',index,'<s:property value="detailType.id"/>');
					 }, 1000);
				updateSLGrid('detailType.name',index,'<s:property value="detailType.name"/>');
				updateSLGrid('detailCode',index,'<s:property value="detailCode"/>');
				updateSLGrid('detailKeyId',index,'<s:property value="detailKeyId"/>');
				updateSLGrid('detailKey',index,'<s:property value="detailKey" />');
				updateSLGrid('amount',index,'<s:property value="amount"/>');
				updateSLTableIndex();
			</s:iterator>
		
	}

var slAccountCodes = new Array();
// logic to re-populate the account code in the account code drop down in the subledger table data grid.
// basicaly required when validation fails.
function loadGridOnValidationFail(){
	var accountCodes=new Array();
	for(var i=0;i<accountTableIndex+1;i++){
	if(null != document.getElementById('accountDetails['+i+'].glCodeId.glcode')){
		accountCodes[i] = document.getElementById('accountDetails['+i+'].glCodeId.glcode').value;
	}
	}
	var url = path+'/receipts/ajaxReceiptCreate-getDetailCode.action?accountCodes='+accountCodes;
	var transaction = YAHOO.util.Connect.asyncRequest('POST', url, callbackSLAccCode, null);
}
var callbackSLAccCode = {
success: function(o) {
		var test= o.responseText;
		test = test.split('~');
		slAccountCodes.length=0;
		for (var j=0; j<subledgerTableIndex;j++ )
		{
			var d=document.getElementById('subledgerDetails['+j+'].serviceAccountDetail.glCodeId.id');
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
				var d = document.getElementById('subledgerDetails['+j+'].serviceAccountDetail.glCodeId.id');
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
			<s:iterator value="subledgerDetails" status="stat">
				if('<s:property value="serviceAccountDetail.glCodeId.id"/>' !="" || '<s:property value="serviceAccountDetail.glCodeId.id"/>' !=0){
					var index = '<s:property value="#stat.index"/>';
					updateGridSLDropdownGL('serviceAccountDetail.glCodeId.id',index,'<s:property value="serviceAccountDetail.glCodeId.id"/>');
				}
				
			</s:iterator>
    },
    failure: function(o) {
    	bootbox.alert('failure');
    }
}
function updateGridSLDropdownGL(field,index,value){
	var element = document.getElementById('subledgerDetails['+index+'].'+field)
	if(value != "" ){
	element.options.length=2;
	element.options[1].value=value;
	element.options[1].selected = true;
	}
	loadDetailType(index);
}

var loadDetailType = function(index) { 
		var subledgerid=document.getElementById('subledgerDetails['+index+'].serviceAccountDetail.glCodeId.id');
		var accountCode = subledgerid.options[subledgerid.selectedIndex].text.trim();
		document.getElementById('subledgerDetails['+index+'].serviceAccountDetail.glCodeId.glcode').value =accountCode;
		var url = path+'/receipts/ajaxReceiptCreate-getDetailTypeForService.action?accountCode='+accountCode+'&index='+index;
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
				obj = document.getElementById('subledgerDetails['+parseInt(eachItem[0])+']'+'.detailType.id');
				if(obj!=null)
					obj.options.length=detailRecord.length+1; 	
			}
			if(obj!=null)
			{
				obj.options[i+1].text=eachItem[1];     
				obj.options[i+1].value=eachItem[2];
				document.getElementById('subledgerDetails['+parseInt(eachItem[0])+']'+'.detailType.name').value = eachItem[1];
			}
			
			if(eachItem.length==1) // for deselect the subledger code
			{
				var d = document.getElementById('subledgerDetails['+i+'].detailType.id');
				d.options.length=1;
				d.options[0].text='---Select---';
				d.options[0].value=0;
			}
		} 	
			<s:iterator value="subledgerDetails" status="stat">
			if('<s:property value="detailType.id"/>' !="" || '<s:property value="detailType.id"/>' !=0){
				var index = '<s:property value="#stat.index"/>';
				updateSLDetailDropdown('detailType.id',index,'<s:property value="detailType.id"/>');
			}
			</s:iterator>
    },
    failure: function(o) {
    	bootbox.alert('failure');
    }
}

function updateSLDetailDropdown(field,index,value){
	var element = document.getElementById(SUBLEDGERLIST+'['+index+'].'+field);
	if(value != "" ){
		for(var k=0;k<3;k++){
			if(element.options[k].value.trim() == value.trim()){
				element.options[k].selected=true;
			}
		}
	}
}

</script>

<style type="text/css">
	#codescontainer {position:absolute;left:11em;width:9%;text-align: left;}
	#codescontainer .yui-ac-content {position:absolute;width:600px;border:1px solid #404040;background:#fff;overflow:hidden;z-index:9050;}
	#codescontainer .yui-ac-shadow {position:absolute;margin:.3em;width:300px;background:#a0a0a0;z-index:9049;}
	#codescontainer ul {padding:5px 0;width:100%;}
	#codescontainer li {padding:0 5px;cursor:default;white-space:nowrap;}
	#codescontainer li.yui-ac-highlight {background:#ff0;}
	#codescontainer li.yui-ac-prehighlight {background:#FFFFCC;}
	#subledgercodescontainer {position:absolute;left:11em;width:9%;text-align: left;}
    #subledgercodescontainer .yui-ac-content {position:absolute;width:350px;border:1px solid #404040;background:#fff;overflow:hidden;z-index:9050;}
    #subledgercodescontainer .yui-ac-shadow {position:absolute;margin:.3em;width:300px;background:#a0a0a0;z-index:9049;}
    #subledgercodescontainer ul {padding:5px 0;width:100%;}
    #subledgercodescontainer li {padding:0 5px;cursor:default;white-space:nowrap;}
    #subledgercodescontainer li.yui-ac-highlight {background:#ff0;}
    #subledgercodescontainer li.yui-ac-prehighlight {background:#FFFFCC;}
.yui-skin-sam tr.yui-dt-odd{background-color:#F5F5F5;}

    .yui-dt-liner input
    {
       width:100%;
    }
</style>
