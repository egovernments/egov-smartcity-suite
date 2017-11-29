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

jQuery('#btnsearch').click(function(e) {

	callAjaxSearch();
});



function getFormData($form){
	var unindexed_array = $form.serializeArray();
	var indexed_array = {};

	$.map(unindexed_array, function(n, i){
		indexed_array[n['name']] = n['value'];
	});

	return indexed_array;
}

function validateFields(){
		var tbl=document.getElementById("fiscalPeriodTable");
		var lastRow = (tbl.rows.length)-1;
		var startingDate=getControlInBranch(tbl.rows[1],'startDate').value;
		var finYearStartDate=document.getElementById("startingDate").value;
		var lastRowFiscalName = getControlInBranch(tbl.rows[lastRow],'name').value;
		var lastRowEndDate=getControlInBranch(tbl.rows[lastRow],'endDate').value;
		var finYearEndDate=document.getElementById("endingDate").value;
		
		if(startingDate!=finYearStartDate){
			bootbox.alert('Enter valid Start date');
			getControlInBranch(tbl.rows[1],'startDate').value='';
			getControlInBranch(tbl.rows[1],'startDate').focus();
			return false;
		}
		if(lastRowEndDate!=finYearEndDate)
		{
			bootbox.alert('Enter valid End date');
			getControlInBranch(tbl.rows[lastRow],'endDate').value='';
			getControlInBranch(tbl.rows[lastRow],'endDate').focus();
			return false;
		}
		if(lastRowFiscalName==""){
			bootbox.alert('Enter Fiscal Period Name');
			getControlInBranch(tbl.rows[1],'name').value='';
			getControlInBranch(tbl.rows[1],'name').focus();
			return false;
		}
	    var previousRow = lastRow - 1;
	    if(previousRow>0){
			var lastRowStartDate=getControlInBranch(tbl.rows[lastRow],'startDate').value;
		    var previousEndDate=getControlInBranch(tbl.rows[previousRow],'endDate').value;
		    if( compareDate(formatDate6(previousEndDate),formatDate6(lastRowStartDate)) == -1 )
			{
			     bootbox.alert('Enter valid Start Date');
				 getControlInBranch(tbl.rows[lastRow],'startDate').value='';
				 getControlInBranch(tbl.rows[lastRow],'startDate').focus();
				 return false;
			}
	
	    }
	
	 return true;

}



function addRow1() 
{
	var table = document.getElementById('fiscalPeriodTable');

	if(!checkforNonEmptyPrevRow())
		return false;


	var rowCount = table.rows.length;
	var row = table.insertRow(rowCount);
	var counts = rowCount - 1;
	var newRow = document.createElement("tr");

	var newCol = document.createElement("td");
	newRow.appendChild(newCol);

	var cell1 = row.insertCell(0);
	var fiscalName = document.createElement("input");
	var att = document.createAttribute("class");
	fiscalName.setAttributeNode(att); 
	fiscalName.setAttribute("class","form-control text-right patternvalidation");
	fiscalName.type = "text";
	fiscalName.setAttribute("required", "required");
	fiscalName.setAttribute("id","name");
	fiscalName.setAttribute("maxlength", "10");
	fiscalName.name = "cFiscalPeriod[" + counts + "].name";
	cell1.appendChild(fiscalName);

	var newCol = document.createElement("td");
	newRow.appendChild(newCol);
	var cell2 = row.insertCell(1);
	var fiscalDate = document.createElement("input");
	fiscalDate.setAttribute("class","form-control datepicker");
	fiscalDate.setAttribute("id","startDate");
	fiscalDate.type = "text";
	fiscalDate.setAttribute("required", "required");
	fiscalDate.className = "form-control datepicker";
	fiscalDate.setAttribute("maxlength", "10");
	fiscalDate.setAttribute("data-inputmask","'mask': 'd/M/y'");
	fiscalDate.name = "cFiscalPeriod[" + counts + "].startingDate";

	
	cell2.appendChild(fiscalDate);

	var newCol = document.createElement("td");
	newRow.appendChild(newCol);
	var cell3 = row.insertCell(2);
	var att = document.createAttribute("class");
	att.value="form-control datepicker";
	var fiscalDate = document.createElement("input");
	fiscalDate.setAttribute("class","form-control datepicker");
	fiscalDate.setAttribute("id","endDate");
	fiscalDate.type = "text";
	fiscalDate.setAttribute("required", "required");
	
	fiscalDate.setAttribute("maxlength", "10");
	fiscalDate.setAttribute("data-inputmask","'mask': 'd/M/y'");
	
	fiscalDate.name = "cFiscalPeriod[" + counts + "].endingDate";
	cell3.appendChild(fiscalDate);

	var newCol = document.createElement("td");
	newRow.appendChild(newCol);
	var cell4 = row.insertCell(3);

	var addButton = document.createElement("input");
	addButton.type = "button";
	addButton.setAttribute("class", "btn btn-primary");
	addButton.setAttribute("onclick", "return addRow1();");
	addButton.setAttribute("value", "Add");
	cell4.appendChild(addButton);

	var x = document.createElement("LABEL");
	var t = document.createTextNode(" ");
	cell4.appendChild(t);

	var hiddenId = document.createElement("input");
	hiddenId.type = "hidden";
	hiddenId.id = "cFiscalPeriod[" + counts + "].id";
	hiddenId.setAttribute("value", "${cFiscalPeriod[" + counts + "].id}");
	cell4.appendChild(hiddenId);
	
	jQuery(".datepicker").datepicker({
		format: "dd/mm/yyyy",
		autoclose:true
	}); 

}

function compareDate(dt1, dt2){			
	/*******		Return Values [0 if dt1=dt2], [1 if dt1<dt2],  [-1 if dt1>dt2]     *******/
	var d1, m1, y1, d2, m2, y2, ret;
	dt1 = dt1.split('/');
	dt2 = dt2.split('/');
	ret = (eval(dt2[2])>eval(dt1[2])) ? 1 : (eval(dt2[2])<eval(dt1[2])) ? -1 : (eval(dt2[1])>eval(dt1[1])) ? 1 : (eval(dt2[1])<eval(dt1[1])) ? -1 : (eval(dt2[0])>eval(dt1[0])) ? 1 : (eval(dt2[0])<eval(dt1[0])) ? -1 : 0 ;										
	return ret;
}

function validateStartDate() {
	var startDate = document.getElementById('startingDate').value;
	var finYearStartDate = document.getElementById('finYearStartDate').value;
	var currDate = new Date();
	var currentDate = currDate.getDate() + "/" + (currDate.getMonth()+1) + "/" + currDate.getFullYear() ;
	/*To check whether Start Date is Greater than End Date*/
	if(startDate!=finYearStartDate){
		if( compareDate(formatDate6(finYearStartDate),formatDate6(startDate)) == -1 )
		{
			bootbox.alert('Enter valid Start Date');
			document.getElementById('endingDate').value='';
			document.getElementById('endingDate').focus();
			return false;
		}
	}
	return true;
}

function validateEndDate() {
	var strtDate = document.getElementById('startingDate').value;
	var endDate = document.getElementById('endingDate').value;
	var currDate = new Date();
	var currentDate = currDate.getDate() + "/" + (currDate.getMonth()+1) + "/" + currDate.getFullYear() ;
	/*To check whether Start Date is Greater than End Date*/
	if( compareDate(formatDate6(strtDate),formatDate6(endDate)) == -1 )
	{
		bootbox.alert('Start Date cannot be greater than End Date');
		document.getElementById('endingDate').value='';
		document.getElementById('endingDate').focus();
		return false;
	}
}

function formatDate6(dt){
	if(dt==null || dt==''  || dt=="" )return '';
	var array = dt.split("/");
	var mon=array[1];
	var day=array[0];
	var year=array[2].substring(0,4);			
	dt = day+"/"+mon+"/"+year;			
	return dt;	
}

function getControlInBranch(tableobj, columnName) {
	if (!tableobj || !(tableobj.getAttribute)) {
		return null;
	}
	// check if the object itself has the name
	if (tableobj.getAttribute("id") == columnName) {
		return tableobj;
	}

	// try its children
	var children = tableobj.childNodes;
	var child;
	if (children && children.length > 0) {
		for (var i = 0; i < children.length; i++) {
			child = this.getControlInBranch(children[i], columnName);
			if (child) {
				return child;
			}
		}
	}
	return null;
}

function validateFiscalEndDate() {

	var endDate = document.getElementById('endingDate').value;

	if( endDate == '' )
	{
		bootbox.alert('Enter Ending Date');
		document.getElementById('endDate').value='';
		document.getElementById('endDate').focus();
		return false;
	}

	//var fiscalEndDate= document.getElementById('endingDate').value
}

function callAjaxSearch() {
	drillDowntableContainer = jQuery("#resultTable");		
	jQuery('.report-section').removeClass('display-hide');
	reportdatatable = drillDowntableContainer
	.dataTable({
		ajax : {
			url : "/EGF/cfinancialyear/ajaxsearch/"+$('#mode').val(),      
			type: "POST",
			"data":  getFormData(jQuery('form'))
		},
		"fnRowCallback": function (row, data, index) {
			$(row).on('click', function() {
				console.log(data.id);
				window.open('/EGF/cfinancialyear/'+ $('#mode').val() +'/'+data.id,'','width=800, height=600');
			});
		},
		"bDestroy" : true,
		"sDom" : "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-xs-3'i><'col-xs-3 col-right'l><'col-xs-3 col-right'<'export-data'T>><'col-xs-3 text-right'p>>",
		"aLengthMenu" : [ [ 10, 25, 50, -1 ], [ 10, 25, 50, "All" ] ],
		"oTableTools" : {
			"sSwfPath" : "../../../../../../egi/resources/global/swf/copy_csv_xls_pdf.swf",
			"aButtons" : [ "xls", "pdf", "print" ]
		},
		aaSorting: [],				
		columns : [ { 
			"data" : "finYearRange", "sClass" : "text-left"} ,{ 
				"data" : "startingDate", "sClass" : "text-left"} ,{ 
					"data" : "endingDate", "sClass" : "text-left"} ,{ 
							"data" : "isActiveForPosting", "sClass" : "text-left"} ,{ 
								"data" : "isClosed", "sClass" : "text-left"} ,{ 
									"data" : "transferClosingBalance", "sClass" : "text-left"}]				
	});
}