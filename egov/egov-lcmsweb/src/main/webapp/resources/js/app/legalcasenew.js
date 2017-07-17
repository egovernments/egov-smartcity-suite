/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
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
 */
$(document).ready(function(){
	
	var lcNumber=$('#lcNumber').val();
	
	var modeval=$('#mode').val();
	if(modeval=='edit')
	{
		if(lcNumber !='')
			$("#lcNumber").prop("disabled", true);
			$("#finwpYear").hide(); 
			/*$('#lcNumberType').prop("disabled", true);*/
	}
	/*if(lcNumberType !='' && lcNumberType== 'MANUAL')
		{
		
		 $(".show-ManualLcNumber").show(); 
		}
	else
		{
		 $(".show-ManualLcNumber").hide(); 
		}*/
	
    $("#petitionDetails tbody tr").each(function( index ) {
    	var $this = $(this);
        $this.find("select, button").prop("disabled", true);
    });
    $("#respondantDetails tbody tr").each(function( index ) {
    	var $this = $(this);
        $this.find("select, button").prop("disabled", true);
    });
    
    
    $(".btn-primary").click(function(event){
		
		var caseNumber =$('#caseNumber').val();
		var lcnumber=$('#lcNumber').val();
		/*var lcNumberType=$('#lcNumberType').val();*/
		var mode=$('#mode').val();
		if(mode=='create'){
		if(caseNumber !="" && caseNumber !=null && ($('#wpYear').val() ==null || $('#wpYear').val() =='') )
			{
			bootbox.alert("Select Case Number Year ");
			return false;
			}
		}
		
		/*if(lcNumberType =='MANUAL' && mode =='create'){
			if(lcnumber=="" ||  lcnumber ==null )
			{
				bootbox.alert("Please enter Legal Case Number");
				return false;
			}
			if( lcnumber !=null && $('#finwpYear').val() =='' || $('#finwpYear').val() ==null)
			{
				bootbox.alert("Select Legal Case Number Year ");
				return false;
			}
		}*/
		if(mode=='create'){
		  if($('#caseDate').val() != '' && $('#caseReceivingDate').val() != '' ){
				var start = $('#caseDate').val();
				var end = $('#caseReceivingDate').val();
				var stsplit = start.split("/");
					var ensplit = end.split("/");
					
					start = stsplit[1] + "/" + stsplit[0] + "/" + stsplit[2];
					end = ensplit[1] + "/" + ensplit[0] + "/" + ensplit[2];
					if(!validCaseRecievingAndFillingRange(start,end))
					{
						
					return false;
					}
			}
		  if($('#caseDate').val() != '' && $('#noticeDate').val() != '' ){
				var start = $('#caseDate').val();
				var end = $('#noticeDate').val();
				var stsplit = start.split("/");
					var ensplit = end.split("/");
					
					start = stsplit[1] + "/" + stsplit[0] + "/" + stsplit[2];
					end = ensplit[1] + "/" + ensplit[0] + "/" + ensplit[2];
					if(!validNoticeDateAndFillingRange(start,end))
					{
					return false;
					}
			}
		  if($('#caseDate').val() != '' && $('#caDueDate').val() != '' ){
				var start = $('#caseDate').val();
				var end = $('#caDueDate').val();
				var stsplit = start.split("/");
					var ensplit = end.split("/");
					
					start = stsplit[1] + "/" + stsplit[0] + "/" + stsplit[2];
					end = ensplit[1] + "/" + ensplit[0] + "/" + ensplit[2];
					if(!validCaDueDatendFillingRange(start,end))
					{
					return false;
					}
			}
		}
		  
		$('#newlegalcaseForm :not([type=submit])').prop('disabled',false);
		$(".btn-primary").prop('disabled',false);
		document.forms[0].submit;
			return true;
			event.preventDefault();
		
		
	});
    
    
    var assignPosition = new Bloodhound({
		datumTokenizer : function(datum) {
			return Bloodhound.tokenizers
					.whitespace(datum.value);
		},
		queryTokenizer : Bloodhound.tokenizers.whitespace,
		remote : {
			url : '/lcms/ajax/getposition', 
			replace : function(url, uriEncodedQuery) {
				return url + '?positionName=' + uriEncodedQuery;

			},
			filter : function(data) {
		
				return $.map(data, function(value, key) {
					
					return {
						name : value,
						value : key
					};
					
				});
			}
		}
	});
	
	assignPosition.initialize();
	var typeaheadobj = $('#positionName').typeahead({
		hint: false,
		highlight: false,
		minLength: 3
	},  {
		displayKey : 'name',
		source : assignPosition.ttAdapter()
	});
	
	typeaheadWithEventsHandling(typeaheadobj, '#positionId'); 
});
function validCaseRecievingAndFillingRange(start, end) {
    var startDate = Date.parse(start);
    var endDate = Date.parse(end);
	
    // Check the date range, 86400000 is the number of milliseconds in one day
    var difference = (endDate - startDate) / (86400000 * 7);
    if (difference < 0) {
    	bootbox.alert("Case Receiving Date should not be less than Case Filling Date");
		$('#end_date').val('');
		return false;
		} else {
		return true;
	}
    return true;
}

function validNoticeDateAndFillingRange(start, end) {
    var startDate = Date.parse(start);
    var endDate = Date.parse(end);
	
    // Check the date range, 86400000 is the number of milliseconds in one day
    var difference = (endDate - startDate) / (86400000 * 7);
    if (difference < 0) {
    	bootbox.alert("Notice Date should not be less than Case Filling Date");
		$('#end_date').val('');
		return false;
		} else {
		return true;
	}
    return true;
}

function validCaDueDatendFillingRange(start, end) {
    var startDate = Date.parse(start);
    var endDate = Date.parse(end);
	
    // Check the date range, 86400000 is the number of milliseconds in one day
    var difference = (endDate - startDate) / (86400000 * 7);
    if (difference < 0) {
    	bootbox.alert("Counter Affidavit Due Date should not be less than Case Filling Date");
		$('#end_date').val('');
		return false;
		} else {
		return true;
	}
    return true;
}
function checkLCType()
{
	 if($('#lcNumberType').val() == "MANUAL")
{
		 $(".show-ManualLcNumber").show(); 

}
else
{
	$(".show-ManualLcNumber").hide(); 
}
document.getElementById("lcNumber").value="";	
document.getElementById("finwpYear").value="";
 
}
	
function addPetRow()
{     
			var tableObj=document.getElementById('petitionDetails');
			var tbody=tableObj.tBodies[0];
			var lastRow = tableObj.rows.length;
			var rowObj = tableObj.rows[1].cloneNode(true);
			 var phoneno = /^\d{10}$/;  
			nextIdx=(lastRow-1);
			var currentROwIndex=nextIdx-1;
			jQuery(rowObj).find("input, select").each(
					function() {
					
					jQuery(this).attr({
								'id' : function(_, id) {
									return id.replace('[0]', '['
											+ nextIdx + ']');
								},
								'name' : function(_, name) {
									return name.replace('[0]', '['
											+ nextIdx + ']');
									
								}
					});  
		   });
			tbody.appendChild(rowObj);
			
			 $('#petitionDetails tbody tr:last').find('input').val('');
			 generateSno(".petitionDetails");
		   
}

function generateSno(tablenameclass)
{
	$(tablenameclass+'.spansno').each(function(idx){
		$(this).html(""+(idx+1));
	});
}

function addResRow()
{     
	var index=document.getElementById('respondantDetails').rows.length-1;
	    	var tableObj=document.getElementById('respondantDetails');
			var tbody=tableObj.tBodies[0];
			var lastRow = tableObj.rows.length;
			var rowObj = tableObj.rows[1].cloneNode(true);
			
			nextIdx=(lastRow-1);
			var currentROwIndex=nextIdx-1;
			jQuery(rowObj).find("input, select").each(
					function() {
					
					jQuery(this).attr({
								'id' : function(_, id) {
									return id.replace('[0]', '['
											+ nextIdx + ']');
								},
								'name' : function(_, name) {
									return name.replace('[0]', '['
											+ nextIdx + ']');
									
								}
					});  
		   });


		   tbody.appendChild(rowObj);
		   $('#respondantDetails tbody tr:last').find('input').val('');
		   generateSno(".respondantDetails");
		
 }
function addPetEditRow()
{     
			var tableObj=document.getElementById('petitionDetails');
			var tbody=tableObj.tBodies[0];
			var lastRow = tableObj.rows.length;
			var rowObj = tableObj.rows[1].cloneNode(true);
			
			nextIdx=(lastRow-1);
			var currentROwIndex=nextIdx-1;
			jQuery(rowObj).find("input, select").each(
					function() {
					
					jQuery(this).attr({
								'id' : function(_, id) {
									return id.replace('['+ currentROwIndex +']', '['
											+ nextIdx + ']');
								},
								'name' : function(_, name) {
									return name.replace('[' + currentROwIndex + ']', '['
											+ nextIdx + ']');
									
								}
					});  
		   });
			tbody.appendChild(rowObj);
		   
			generateSno(".petitionDetails");
			
}

function addResEditRow()
{     
	var index=document.getElementById('respondantDetails').rows.length-1;
	    	var tableObj=document.getElementById('respondantDetails');
			var tbody=tableObj.tBodies[0];
			var lastRow = tableObj.rows.length;
			var rowObj = tableObj.rows[1].cloneNode(true);
			
			nextIdx=(lastRow-1);
			var currentROwIndex=nextIdx;
			nextIdx=nextIdx+1;
			jQuery(rowObj).find("input, select").each(
					function() {
					
					jQuery(this).attr({
								'id' : function(_, id) {
									return id.replace('['+ currentROwIndex +']', '['
											+ nextIdx + ']');
								},
								'name' : function(_, name) {
									return name.replace('[' + currentROwIndex + ']', '['
											+ nextIdx + ']');
									
								}
					});  
		   });

			validatePhone(contactNumber);
		   tbody.appendChild(rowObj);
		   generateSno(".respondantDetails");
		
 }
$(document).on('click',"#pet_delete_row",function (){
	var table = document.getElementById('petitionDetails');
    var rowCount = table.rows.length;
    var counts = rowCount - 1;
    var k = 2;
    var m;
    if(counts==1)
	{
		bootbox.alert("This Row cannot be deleted");
		return false;
	}else{	

		$(this).closest('tr').remove();		
		
		jQuery("#petitionDetails tr:eq(1) td span[alt='AddF']").show();
		//starting index for table fields
		var idx=0;
		
		//regenerate index existing inputs in table row
		jQuery("#petitionDetails tr:not(:first)").each(function() {
			jQuery(this).find("input, select").each(function() {
			   jQuery(this).attr({
			      'id': function(_, id) {  
			    	  return id.replace(/\[.\]/g, '['+ idx +']'); 
			       },
			      'name': function(_, name) {
			    	  return name.replace(/\[.\]/g, '['+ idx +']'); 
			      },
			   });
			  });
			
			idx++;
		});
		
		generateSno(".petitionDetails");
		
		return true;
	}
});



function onChangeofPetitioncheck(obj)
{
		if ( $(obj).is(':checked')) {
	    	console.log('Checkbox checked');
	    	$(obj).closest('tr').find("select").removeAttr("disabled");
	    }else{
	    	console.log('Checkbox not checked');
	    	$(obj).closest('tr').find("select").attr("disabled", "disabled");
	    }
		
}

$(document).on('click',"#res_delete_row",function (){
	var table = document.getElementById('respondantDetails');
    var rowCount = table.rows.length;
    var counts = rowCount - 1;
    var j = 2;
    var i;
    if(counts==1)
	{
		bootbox.alert("This Row cannot be deleted");
		return false;
	}else{	

		$(this).closest('tr').remove();		
		
		jQuery("#respondantDetails tr:eq(1) td span[alt='AddF']").show();
		//starting index for table fields
		var idx=0;
		
		//regenerate index existing inputs in table row
		jQuery("#respondantDetails tr:not(:first)").each(function() {
			jQuery(this).find("input, select").each(function() {
			   jQuery(this).attr({
			      'id': function(_, id) {  
			    	  return id.replace(/\[.\]/g, '['+ idx +']'); 
			       },
			      'name': function(_, name) {
			    	  return name.replace(/\[.\]/g, '['+ idx +']'); 
			      },
			   });
			   
			  
			   
		    });
			idx++;
		});
		
		generateSno(".respondantDetails");
		
		return true;
	}
});

$(document).on('keyup','.validateZero', function(){
	  var valid = /^[1-9],,()OR?$/.test(this.value),
	  val = this.value;
	  
	  if(!valid){
	    console.log("Invalid input!");
	    this.value = val.substring(0, val.length - 1);
	   }
	});

$('#btnclose').click(function(){
	bootbox.confirm({
	    message: 'Information entered in this screen will be lost if you close this page ? Please confirm if you want to close. ',
	    buttons: {
	        'cancel': {
	            label: 'No',
	            className: 'btn-default pull-right'
	        },
	        'confirm': {
	            label: 'Yes',
	            className: 'btn-danger pull-right'
	        }
	    },
	    callback: function(result) {
	        if (result) {
	             window.close();
	        }
	    }
	});
	
});
