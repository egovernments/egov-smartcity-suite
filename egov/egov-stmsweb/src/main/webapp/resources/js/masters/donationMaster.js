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
$(document).ready(function() {
	$('#propertyType option').each(function() {            
		var $this = $(this);
		$this.text($this.text().replace(/_/g, ' '));
		if ($this.text() == "MIXED")
			$(this).remove();
	});
	
	$( "#donationMasterTable tr .delete-button").hide();
	$( "#donationMasterTable tr:last .delete-button").show();
	
	var isSubmitForm=false;

	$("#submitformvalue").click(function() {
		
		if(isSubmitForm){
			return true;
		}
		
		if ($("#donationRatesSearchForm").valid())
		{
		    if(!validateEffectiveDate())
			{
			  return false;
			}
		    else if(!fromDateAndNoOfClosetsAndCombinationValidation()){
		    	return false;
		    }
		    return true;
		    
		}
		else{
			return false;
		}
		
	});
	
	
	function checkUniqueNumberOfClosets(){
		var donationCollection=[];
		var isValidation=true;
		
		$('.donationRatesNoOfClosets').each(function(i,obj){
			if($(this).val() != ""){
				if($(this).val()==="0" ){
					var textbox=$(this);
					bootbox.alert('Number of closets should be more than 0',function(){
					setTimeout(function(){ textbox.focus(); }, 400);
					});
					isValidation=false;
					return false;
				}
				else{
				
						if(i==0){
							donationCollection.push($(this).val());
						}
						else
						{
							if(donationCollection.indexOf($(this).val())===-1)
							{
								donationCollection.push($(this).val());
							}
							else
							{
								isValidation=false;
								var textfield=$(this);
								bootbox.alert('Entered Number Of Closets '+$(this).val()+' is a duplicate value. Please enter different value.', function(){
									
									setTimeout(function(){ textfield.focus(); }, 400);
								});
								return false;
							}
						}
				}
			}
		});
		return isValidation;
	}
	
	function fromDateAndNoOfClosetsAndCombinationValidation(){
		$.ajax({
			url:'/stms/masters/fromDateValidationWithActiveRecord',
		
		type:"GET",
		data :{
			propertyType : $('#propertyType').val(),
			fromDate : $('#effectiveDate').val(),
		},
		dataType : 'json',
		success: function(response){
			console.log(" response -> "+response);
			if(response!="true"){
				bootbox.alert(" The effecive from date should not be less than "+response);
				return false;
			}
			else{
				 if(!checkUniqueNumberOfClosets()){
				    	
			    		return false;
			    	}	
				 else{
					 if(!donationMasterCombination()){
					    	return false;
					    }
					 else{
					 isSubmitForm=true;
						$('#submitformvalue').trigger('click');
					 }
				 }
			}
		},
		error: function (response) {
			console.log("failed");
		}
		
		});
		
	}
	
	
	function donationMasterCombination() {
		$.ajax({
			url : '/stms/masters/ajaxexistingdonationvalidate',
				type : "GET",
				data : {
					propertyType : $('#propertyType').val(),
					fromDate : $('#effectiveDate').val(),
					},
					dataType : 'json',
					success : function(response) {
					console.log("success"+ response);
					if (response > 0) {
					if (!overwriteDonationMasterRate(response))
						return false;
					} else {
						isSubmitForm=true;
						$('#submitformvalue').trigger('click');
					}
				},
				error : function(response) {
					console.log("failed");
				}
			});
	}
	
	function overwriteDonationMasterRate(res) {
		bootbox.confirm(" With entered combination donation rates are present. Do you want to overwrite it?",function(result){
			if(result){
				isSubmitForm=true;
				$('#submitformvalue').trigger('click');
		    }
		});
		return false;
	}
	
	function validateEffectiveDate() {
		var fromdate = $('#effectiveDate').val();
		var todaysDate = getTodayDate();
		if (compareDate(fromdate, todaysDate) == 1) {
			bootbox.alert("The effective from date should not be less than today's date");
			$(this).val("");
			return false;
		} else {
			return true;
		}
	}
	
	function clearField() {
		input.value = "";
	};

	function validateAmmount() {
		var val = $('#amount').val();
		if (val < 1) {
			bootbox.alert($("#err-validate-amount").text());
			return false;
		} else
			return true;
	}
	
	
	$('#amount').keyup(function(e) {       // validate two decimal points
		var regex = /^\d+(\.\d{0,2})?$/g;
		if (!regex.test(this.value)) {
			$(this).val($(this).getNum());
		}
	});

	jQuery.fn.getNum = function() {
		var val = $.trim($(this).val());
		if (val.indexOf(',') > -1) {
			val = val.replace(',', '.');
		}
		var num = parseFloat(val);
		var num = num.toFixed(2);
		if (isNaN(num)) {
			num = '';
		}
		return num;
	}
	
	
	
	$('#effectiveDate').datepicker('setEndDate', $('#effectiveEndDate').val());
	
	
	
	$('#tblBody tr').each(function() {                            
		var $this = $(this).find('#propertyType');
		$this.text($this.text().replace(/_/g, ' '));
		var $this = $(this).find('#active');
		$this.text($this.text().toUpperCase());
	});
	
	$('#propertyType option').each(function(){
		var $this=$(this);
		$this.text($this.text().replace(/_/g,' '));
		if($this.text()=="MIXED")
			$(this).remove();
	});
	
	var datatbl = $('#donation_master_search');
	$('#search').click(function(e){
		datatbl.dataTable({
			"ajax": {url:"/stms/masters/search-donation-master?"+$("#donationMasterViewForm").serialize(),
				type:"GET"
			},
			"sDom": "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-md-6 col-xs-12'i><'col-xs-12 col-md-3 col-right' <'export-data'T>><'col-md-3 col-xs-6 text-right' p>>",
			"aLengthMenu": [[10, 25, 50, -1], [10, 25, 50, "All"]],
			"bDestroy": true,
			"autoWidth": false,
			"oTableTools": {
				"sSwfPath" : "../../../../../../egi/resources/global/swf/copy_csv_xls_pdf.swf",
				"aButtons": [
								{
								"sExtends": "xls",
								"mColumns": [1,2,3]
								},
								{
								"sExtends": "pdf",
								"mColumns": [1,2,3]
								},
								{
								"sExtends": "print"
								}]
			},
			"columns" : [
			            
						 { "title" : "S.No"},
						 { "data" : "id", "visible": false},
						 { "data" : "propertyType", "title":"Property Type"},
						 { "data" : "size", "title": "Total Number Of Closets"},
						 { "data" : "fromDate", "title": "Effective From Date"},
						 { "data" : "isActive",
						   "title": "Connection Status",
							"render" : function(data, type, row, meta){
								return (data?"ACTIVE":"INACTIVE");
							}	 
						 
						 },
						 { "data" : "modifiedDate", "title": "ModifiedDate"},
						 { "data" : "amount", "title": "Donation Amount", "className" : "text-right", "visible":false},
						 { "data" : "","title":"Actions", 
						   "render" : function(data, type, row, meta){
							     var editAction = '<span class="add-padding"><i class="fa fa-edit history-size" class="tooltip-secondary" data-toggle="tooltip" title="Edit"></i></span>';
							     var viewAction = '<span class="add-padding"><i class="fa fa-eye history-size" class="tooltip-secondary" data-toggle="tooltip" title="View"></i></span>';
								 return ((row.isActive && row.isEditable)?editAction+viewAction:viewAction);
							}
						 }
						
						 ],
						 "fnRowCallback" : function(nRow, aData, iDisplayIndex){ $("td:first", nRow).html(iDisplayIndex +1); return nRow; },
						});
						e.stopPropagation();
	});
	
	
	$("#propertyType").change(function(){
		$.ajax({
			url:"/stms/masters/fromDate-by-propertyType",
			type:"GET",
			data:{
				propertyType : $('#propertyType').val()
			},
			dataType: "json",
			success:function(response){
				console.log("success"+response);
				$('#effectiveFromDate').empty();
				$('#effectiveFromDate').append($("<option value=''>Select from below</option>"));
				$.each(response,function(index, value){
					console.log("index "+index + "    value "+value);
					var date = new Date(value);
					var day = date.getDate();
					var month = date.getMonth() + 1;
					if(day < 10 ){
						day = '0'+day;
					}
					if(month < 10 ){
						month = '0'+month;
					}
					date = day+ '/' + month + '/' +  date.getFullYear();
					$('#effectiveFromDate').append($('<option>').text(date).attr('value', date));
				});
			},
			error: function (response) {
				console.log("failed");
			}
			
		});
	});
	
	var datatbl = $('#donation_master_search');
	$("#donation_master_search").on('click','tbody tr td i.fa-eye',function(e) {
		var donationMasterId = datatbl.fnGetData($(this).parent().parent().parent(),1);
		window.open("donationView/"+donationMasterId, ''+donationMasterId+'', 'width=900, height=700, top=300, left=150,scrollbars=yes')
	});
	
	$("#donation_master_search").on('click','tbody tr td i.fa-edit',function(e) {
		var donationMasterId = datatbl.fnGetData($(this).parent().parent().parent(),1);
		window.open("donationUpdate/"+donationMasterId, ''+donationMasterId+'', 'width=900, height=700, top=300, left=150,scrollbars=yes')
	});
	
	$(".btn-addRow").click(function(){
		if($('#donationRatesSearchForm').valid()){
			if(checkUniqueNumberOfClosets()){
				var currentIndex=$("#donationMasterTable tr").length;
				addRowToTable(currentIndex);
			}
		}
	});
	
	function addRowToTable(currentIndex){
		$("#donationMasterTable tr:last .delete-button").hide();
		$("#donationMasterTable tbody")
		.append(
				'<tr> <td> <input id="id'+(currentIndex)+'" type="hidden"> <input type="text" class="form-control patternvalidation donationRatesNoOfClosets" maxlength="8" style="text-align: left; font-size: 12px;" id="donationMasterNoOfClosets'+(currentIndex - 1)+'" name="donationDetail[' + (currentIndex -1) + '].noOfClosets" data-pattern="number" required="required"/></td><td><input class="form-control patternvalidation donationRatesAmount" data-pattern="decimalvalue" maxlength="8" style="text-align: right; font-size: 12px;" id="donationMasterAmount'+(currentIndex - 1)+'" name="donationDetail[' + (currentIndex-1) + '].amount" type="text" required="required"/></td>  <td> <button type="button" onclick="deleteRow(this)" id="Add" class="btn btn-primary display-hide delete-button">Delete Row </button> </td></tr>');
		patternvalidation(); 
		$("#donationMasterTable tr:last .delete-button").show();
	}
	
	$(".btn-addNewRow").click(function(){
		if($('#donationMasterUpdateform').valid()){
			
			if(checkUniqueNumberOfClosets())
			{
				var currentIndex=$("#donationMasterViewTable tr").length;
				addNewRowToTable(currentIndex);
			}
		}
	});
	
	function addNewRowToTable(currentIndex){
		$("#donationMasterViewTable tr:last .delete-button").hide();
		$("#donationMasterViewTable tbody")
		.append(
				'<tr> <td> <input id="id'+(currentIndex)+'" type="hidden"> <input type="text" class="form-control patternvalidation donationRatesNoOfClosets" maxlength="8" style="text-align: center; font-size: 12px;" id="donationMasterNoOfClosets'+(currentIndex - 1)+'" name="donationDetail[' + (currentIndex -1) + '].noOfClosets" data-pattern="number" required="required"/></td><td><input class="form-control patternvalidation donationRatesAmount" data-pattern="decimalvalue" maxlength="8" style="text-align: right; font-size: 12px;" id="donationMasterAmount'+(currentIndex - 1)+'" name="donationDetail[' + (currentIndex-1) + '].amount" type="text" required="required"/></td>  <td> <button type="button" onclick="deleteCurrentRow(this)" id="Add" class="btn btn-primary display-hide delete-button">Delete Row </button> </td></tr>');
		patternvalidation(); 
		$("#donationMasterViewTable tr:last .delete-button").show();
	}
	
	$("#submitDonationValues").click(function(){
		if($('#donationMasterUpdateform').valid()){
			return checkUniqueNumberOfClosets();
		}
		return false;
	});
	
});





function deleteRow(obj){
	var curRow=obj.parentNode.parentNode.rowIndex;
		if(curRow!=1){
		document.getElementById("donationMasterTable").deleteRow(curRow);
				if(curRow==2){
					$("#donationMasterTable tr:last .delete-button").hide();
				}
				else{
				$("#donationMasterTable tr:last .delete-button").show();
				}
				return true;
		}
	}



function deleteCurrentRow(obj){

		var curRow=obj.parentNode.parentNode.rowIndex;
		if(curRow!=1){
		document.getElementById("donationMasterViewTable").deleteRow(curRow);
				if(curRow==2){
					$("#donationMasterViewTable tr:last .delete-button").hide();
				}
				else{
				$("#donationMasterViewTable tr:last .delete-button").show();
				}
				return true;
		}
	}

