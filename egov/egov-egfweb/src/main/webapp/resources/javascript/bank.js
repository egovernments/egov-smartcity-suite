/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *            Further, all user interfaces, including but not limited to citizen facing interfaces, 
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any 
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines, 
 *            please contact contact@egovernments.org
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 *
 */
var existingCode = '';
var existingName = '';

function checkUniqueBankCode(obj) {
	document.getElementById('codeuniquecode').style.display = 'none';
	var code = obj.value;
	if (code != "" && code != existingCode) {
		var url = "bank.action?model.code=" + code + "&rnd="+ Math.random() + "&mode=UNQ_CODE";
		var callback = {
				success : function(oResponse) {
					if (oResponse.responseText == "false") {
						document.getElementById('codeuniquecode').style.display = 'inline';
						obj.value = "";
					}
				},
				failure : function(oResponse) {
					bootbox.alert("Server error occurred");
				}
		};
		YAHOO.util.Connect.asyncRequest("GET", url, callback);
	}
}

function checkUniqueBankName(obj) {
	document.getElementById('nameuniquename').style.display = 'none';
	var name = obj.value;
	if (name !== "" && existingName != name) {
		var url = "bank.action?model.name=" + name + "&rnd="+ Math.random() + "&mode=UNQ_NAME";
		var callback = {
				success : function(oResponse) {
					if (oResponse.responseText == "false") {
						document.getElementById('nameuniquename').style.display = 'inline';
						obj.value = "";
					}
				},
				failure : function(oResponse) {
					bootbox.alert("Server error occurred");
				}
		};
		YAHOO.util.Connect.asyncRequest("GET", url, callback);
	}
}

function check_MICR(e) {
	var branchId = jQuery("#id").val();
	var value = jQuery(e.target).val();
	if (value != '') {
		jQuery.ajax({
			url : 'bankBranch.action?mode=CHECK_UNQ_MICR',
			data : {
				branchMICR : value,
				id : branchId
			},
			type : 'POST',
			async : false,
			datatype : 'text',
			success : function(data) {
				if (data == 'false') {
					bootbox.alert('MICR code already exist');
					jQuery(e.target).val("");
				}
			}
		});
	}
}

function check_BankAccounts(branchId) {
	var response;
	jQuery.ajax({
		url : 'bankBranch.action?mode=CHECK_BANK_ACC',
		data : {
			id : branchId
		},
		type : 'POST',
		async : false,
		datatype : 'text',
		success : function(data) {
			if (data == 'true') {
				response  = true;
			}else{
				response  = false;
			}

		}
	});
	return response;
}
function initializeGrid() {
	jQuery("#listsg11")
	.jqGrid(
			{
				caption : "Branch Details",
				url : 'bankBranch.action?mode=LIST_BRANCH&bankId='+ jQuery("#bank_id").val(),
				editurl : 'bankBranch.action?mode=CRUD&bankId='+ jQuery("#bank_id").val(),
				datatype : "json",
				height : 300,
				width : 800,
				hiddengrid : true,
				colNames : [ 'Srl No', 'Branch Name', 'Branch Code','MICR', 'Address', 'Contact Person', 'Phone Number', 'Narration', 'Active' ],
				colModel : [ {name : 'id',index : 'id',key : true,hidden : true,width : 55,	editable : true,editoptions : {readonly : true, size : 10}}, 
				             {name : 'branchname', index : 'branchname',align:'center', width : 90, editable : true, editoptions : {size : 25},editrules : {required : true},formoptions: { elmprefix: "<span class='mandatory1'>*</span>"}}, 
				             {name : 'branchcode', index : 'branchcode',align:'center', width : 90, editable : true, editoptions : {size : 25}, editrules : {required : true},formoptions: { elmprefix: "<span class='mandatory1'>*</span>"}}, 
				             {name : 'branchMICR', index : 'branchMICR',align:'center', width : 90, editable : true, searchoptions: { sopt: ['eq','ne','lt','le','gt','ge', 'in', 'ni'] },editoptions : {size : 25, dataEvents : [ {type : 'blur', fn : check_MICR} ]}}, 
				             {name : 'branchaddress1', index : 'branchaddress1',align:'center', width : 100, sortable : false, editable : true, edittype : "textarea", editoptions : {rows : "2", cols : "20"}, editrules : {required : true},formoptions: { elmprefix: "<span class='mandatory1'>*</span>"}},
				             {name : 'contactperson', index : 'contactperson',align:'center', width : 80, editable : true, editoptions : {size : 25}}, 
				             {name : 'branchphone', index : 'branchphone',align:'center', width : 80, editable : true, editoptions : {size : 25}}, 
				             {name : 'narration', index : 'narration',align:'center', width : 80, sortable : false, editable : true, edittype : "textarea", editoptions : {rows : "2", cols : "20"}}, 
				             {name : 'isActive', index : 'isActive', align:'center',width : 80, editable : true, edittype : "checkbox",searchoptions: { sopt: ['eq','ne']}, editoptions : { value : "Y:N"}} 
				             ],
				             rowNum : 20,
				             rowList : [ 20, 30, 40, 50 ],
				             pager : '#pagersg11',
				             sortname : 'id',
				             viewrecords : true,
				             sortorder : "desc",
				             multiselect : false,
				             subGrid : true,
				             subGridRowExpanded : function(subgrid_id, row_id) {
				            	 var subgrid_table_id, pager_id;
				            	 subgrid_table_id = subgrid_id + "_t";
				            	 pager_id = "p_" + subgrid_table_id;
				            	 jQuery("#" + subgrid_id).html("<table id='"+ subgrid_table_id+ "' class='scroll'></table><div id='"+ pager_id+ "' class='scroll'></div>");
				            	 jQuery("#" + subgrid_table_id)
				            	 .jqGrid(
				            			 {
				            				 caption : "Account Details",
				            				 url : 'bankAccount.action?mode=LIST_BRANCH_ACC&q=2&bankBranchId='+ row_id,
				            				 editurl : 'bankAccount.action?mode=CRUD&bankBranchId='+ row_id,
				            				 colNames : [ 'ID', 'Account No', 'Fund', 'Account Type', 'Description', 'Pay To', 'Usage Type', 'Active' , 'GlCode'],
				            				 colModel : [{name : 'id', index : 'id', key : true, hidden : true, width : 55, editable : true, editoptions : {readonly : true, size : 10}},
				            				             {name : 'accountnumber', index : 'accountnumber',align:'center', width : 80, key : true, editable : true,searchoptions: { sopt: ['eq','ne','lt','le','gt','ge', 'in', 'ni'] }, editoptions : {size : 25}, editrules : { required : true},formoptions: { elmprefix: "<span class='mandatory1'>*</span>"}},
				            				             {name : 'fundname', index : 'fundname',align:'center', width : 130, editable : true, edittype : "select", editoptions : {value : fundJson}, editrules : { required : true},formoptions: { elmprefix: "<span class='mandatory1'>*</span>"}},
				            				             {name : 'accounttype', index : 'accounttype', width : 70, editable :true, edittype : "select", editoptions : {value : accTypeJson},formoptions: { elmprefix: "<span class='mandatory1'>*</span>"}},
				            				             {name : 'narration', index : 'narration',align:'center', width : 70, editable : true, edittype : "textarea", editoptions : { rows : "2", cols : "20" } },
				            				             {name : 'payto', index : 'payto',align:'center', width : 70, editable : true, editoptions : {size : 25}},
				            				             {name : 'typename', index : 'typename',align:'center', width : 70, editable : true, edittype : "select", editoptions : {value : bankAccTypeJson}, editrules : { required : true},formoptions: { elmprefix: "<span class='mandatory1'>*</span>"}},
				            				             {name : 'active', index : 'active',align:'center', width : 70, editable : true, edittype : "checkbox",searchoptions: { sopt: ['eq','ne']}, editoptions : { value : "Y:N"}},
				            				             {name : 'glcode', index : 'glcode', align:'center',key : true, width : 60, editable : true,searchoptions: { sopt: ['eq','ne','lt','le','gt','ge', 'in', 'ni'] }, editoptions : { size : 20},formoptions: { elmprefix: "<span class='mandatory1'>*</span>"}}
				            				             ],
				            				             datatype : "json",
				            				             rowNum : 20,
				            				             width : 700,
				            				             pager : pager_id,
				            				             multiselect : false,
				            				             viewrecords : true,
				            				             sortname : 'id',
				            				             sortorder : "asc",
				            				             height : '100%'
				            			 });
				            	 jQuery("#" + subgrid_table_id).jqGrid('navGrid',"#" + pager_id, 
				            			 {edit : true, add : true, del : false},
				            			 {
				            				 closeAfterEdit:true,
				            				 resize : true,
				            				 editCaption: "Edit Bank Account",
				            				 bSubmit: "Save Bank Account",
				            				 beforeShowForm:function(response,data){
				            					 jQuery("#accounttype").prop('disabled',true);
				            					 jQuery("#glcode").prop('disabled',true);
				            					 jQuery("#glcode").prop('hidden',false);
				            					 jQuery("#tr_glcode").prop('hidden',false);
				            					 jQuery("#accounttype").prop('hidden',false);
				            					 jQuery("#tr_accounttype").prop('hidden',false);
				            				 },
				            				 afterSubmit: function(response,data){
				            					 return afterSubmit(response.responseText,data,"Bank Account Updated ");
				            				 }
				            			 },
				            			 {
				            				 closeAfterAdd:true,
				            				 resize : true,
				            				 addCaption: "Add Bank Account",
				            				 bSubmit: "Save Bank Account",
				            				 beforeShowForm:function(response,data){
				            					 jQuery("#accounttype").prop('disabled',false);
				            					 jQuery("#glcode").prop('disabled',false);
				            					 if(autoBankGLCode == 'true'){
				            						 jQuery("#glcode").prop('hidden',true);
				            						 jQuery("#tr_glcode").prop('hidden',true);
				            						 jQuery("#" + subgrid_table_id).jqGrid('setColProp', 'accounttype', {editrules: {required: true}});
				            					 }
				            					 else{
				            						 jQuery("#accounttype").prop('hidden',true);
				            						 jQuery("#tr_accounttype").prop('hidden',true);
				            						 jQuery("#" + subgrid_table_id).jqGrid('setColProp', 'glcode', {editrules: {required: true}});
				            					 }
				            				 },
				            				 errorTextFormat: function (response) {
				            					 return '<span class="ui-icon ui-icon-alert" ' +
				            					 'style="float:left; margin-right:.3em;"></span>' + response.responseText;
				            				 },
				            				 afterSubmit: function(response,data){
				            					 return afterSubmit(response.responseText,data,"Bank Account Added ");
				            				 }
				            			 },{
				            				 caption: "Remove Bank Account",
				            				 msg: "Remove the selected Bank Account ?"
				            			 });
				             },
				             subGridRowColapsed : function(subgrid_id, row_id) {
				            	 // this function is called before removing the data
				            	 // var subgrid_table_id;
				            	 // subgrid_table_id = subgrid_id+"_t";
				            	 // jQuery("#"+subgrid_table_id).remove();
				             }
			});
	jQuery("#listsg11").jqGrid('navGrid', '#pagersg11', 
			{add : true,edit : true, del : false},
			{
				closeAfterEdit:true,
				checkOnUpdate:true,
				checkOnSubmit:true,					
				bSubmit: "Save Bank Branch",
				editCaption: "Edit Bank Branch",
				resize : true,
				afterSubmit: function(response,data){
					return afterSubmit(response.responseText,data,"Bank Branch Updated ");
				}				
			},
			{
				closeAfterAdd:true,
				checkOnUpdate:true,
				bSubmit: "Save Bank Branch",
				addCaption: "Add Bank Branch",
				resize : true,
				afterSubmit: function(response,data){
					return afterSubmit(response.responseText,data,"Bank Branch Added ");
				}
			},{
				caption: "Remove Bank Branch",
				msg: "Remove the selected Bank Branch ?",
				beforeSubmit : function(data, formid) { 
					if(check_BankAccounts(data)){
						return[false,"Should not delete branch!!!"];
					}
					else{
						return[true,"Deleted Bank branch successfully"];
					}


				} 
			},{multipleSearch:true});
}

function viewGrid()
{
	jQuery("#listsg11")
	.jqGrid(
			{
				caption : "Branch Details",
				url : 'bankBranch.action?mode=LIST_BRANCH&bankId='+ jQuery("#bank_id").val(),
				datatype : "json",
				height : 300,
				width : 800,
				hiddengrid : true,
				colNames : [ 'Srl No', 'Branch Name', 'Branch Code','MICR', 'Address', 'Contact Person', 'Phone Number', 'Narration', 'Active' ],
				colModel : [ {name : 'id',index : 'id',key : true,hidden : true,width : 55,	editable : true,editoptions : {readonly : true, size : 10}}, 
				             {name : 'branchname', index : 'branchname',align:'center', width : 90, editable : true, editoptions : {size : 25},editrules : {required : true},formoptions: { elmprefix: "<span class='mandatory1'>*</span>"}}, 
				             {name : 'branchcode', index : 'branchcode',align:'center', width : 90, editable : true, editoptions : {size : 25}, editrules : {required : true},formoptions: { elmprefix: "<span class='mandatory1'>*</span>"}}, 
				             {name : 'branchMICR', index : 'branchMICR',align:'center', width : 90, editable : true, searchoptions: { sopt: ['eq','ne','lt','le','gt','ge', 'in', 'ni'] },editoptions : {size : 25, dataEvents : [ {type : 'blur', fn : check_MICR} ]}}, 
				             {name : 'branchaddress1', index : 'branchaddress1',align:'center', width : 100, sortable : false, editable : true, edittype : "textarea", editoptions : {rows : "2", cols : "20"}, editrules : {required : true},formoptions: { elmprefix: "<span class='mandatory1'>*</span>"}},
				             {name : 'contactperson', index : 'contactperson',align:'center', width : 80, editable : true, editoptions : {size : 25}}, 
				             {name : 'branchphone', index : 'branchphone',align:'center', width : 80, editable : true, editoptions : {size : 25}}, 
				             {name : 'narration', index : 'narration',align:'center', width : 80, sortable : false, editable : true, edittype : "textarea", editoptions : {rows : "2", cols : "20"}}, 
				             {name : 'isActive', index : 'isActive', align:'center',width : 80, editable : true, edittype : "checkbox",searchoptions: { sopt: ['eq','ne']}, editoptions : { value : "Y:N"}} 
				             ],
				             rowNum : 20,
				             rowList : [ 20, 30, 40, 50 ],
				             pager : '#pagersg11',
				             sortname : 'id',
				             viewrecords : true,
				             sortorder : "desc",
				             multiselect : false,
				             subGrid : true,
				             subGridRowExpanded : function(subgrid_id, row_id) {
				            	 var subgrid_table_id, pager_id;
				            	 subgrid_table_id = subgrid_id + "_t";
				            	 pager_id = "p_" + subgrid_table_id;
				            	 jQuery("#" + subgrid_id).html("<table id='"+ subgrid_table_id+ "' class='scroll'></table><div id='"+ pager_id+ "' class='scroll'></div>");
				            	 jQuery("#" + subgrid_table_id)
				            	 .jqGrid(
				            			 {
				            				 caption : "Account Details",
				            				 url : 'bankAccount.action?mode=LIST_BRANCH_ACC&q=2&bankBranchId='+ row_id,
				            				 colNames : [ 'ID', 'Account No', 'Fund', 'Account Type', 'Description', 'Pay To', 'Usage Type', 'Active' , 'GlCode'],
				            				 colModel : [{name : 'id', index : 'id', key : true, hidden : true, width : 55, editable : true, editoptions : {readonly : true, size : 10}},
				            				             {name : 'accountnumber', index : 'accountnumber',align:'center', width : 80, key : true, editable : true,searchoptions: { sopt: ['eq','ne','lt','le','gt','ge', 'in', 'ni'] }, editoptions : {size : 25}, editrules : { required : true},formoptions: { elmprefix: "<span class='mandatory1'>*</span>"}},
				            				             {name : 'fundname', index : 'fundname',align:'center', width : 130, editable : true, edittype : "select", editoptions : {value : fundJson}, editrules : { required : true},formoptions: { elmprefix: "<span class='mandatory1'>*</span>"}},
				            				             {name : 'accounttype', index : 'accounttype', width : 70, editable :true, edittype : "select", editoptions : {value : accTypeJson},formoptions: { elmprefix: "<span class='mandatory1'>*</span>"}},
				            				             {name : 'narration', index : 'narration',align:'center', width : 70, editable : true, edittype : "textarea", editoptions : { rows : "2", cols : "20" } },
				            				             {name : 'payto', index : 'payto',align:'center', width : 70, editable : true, editoptions : {size : 25}},
				            				             {name : 'typename', index : 'typename',align:'center', width : 70, editable : true, edittype : "select", editoptions : {value : bankAccTypeJson}, editrules : { required : true},formoptions: { elmprefix: "<span class='mandatory1'>*</span>"}},
				            				             {name : 'active', index : 'active',align:'center', width : 70, editable : true, edittype : "checkbox",searchoptions: { sopt: ['eq','ne']}, editoptions : { value : "Y:N"}},
				            				             {name : 'glcode', index : 'glcode', align:'center',key : true, width : 60, editable : true,searchoptions: { sopt: ['eq','ne','lt','le','gt','ge', 'in', 'ni'] }, editoptions : { size : 20},formoptions: { elmprefix: "<span class='mandatory1'>*</span>"}}
				            				             ],
				            				             datatype : "json",
				            				             rowNum : 20,
				            				             width : 700,
				            				             pager : pager_id,
				            				             multiselect : false,
				            				             viewrecords : true,
				            				             sortname : 'id',
				            				             sortorder : "asc",
				            				             height : '100%'
				            			 });}
				            	 });
	
}
function showMessage (msg) {
	jQuery.jgrid.info_dialog("Info","<div class=\"ui-state-highlight\" style=\"padding:5px;\">"+msg+"!</div>");
	jQuery("#info_dialog").delay(3000).fadeOut();
	jQuery(".ui-widget-overlay").hide();
}

function afterSubmit(reply,data, action) {
	var isSuccess = false;
	var message='';
	if (reply == "success") {
		showMessage(action+"successfully.");
		isSuccess = true;
	} else {
		message = action+"failed.";
	}
	return [isSuccess,message,data.id];
}