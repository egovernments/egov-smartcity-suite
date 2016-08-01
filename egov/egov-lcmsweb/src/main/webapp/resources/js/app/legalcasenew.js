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
	$(".show-ManualLcNumber").hide(); 
	
    $("#petitionDetails tbody tr").each(function( index ) {
    	var $this = $(this);
        $this.find("select, button").prop("disabled", true);
    });
    $("#respodantDetails tbody tr").each(function( index ) {
    	var $this = $(this);
        $this.find("select, button").prop("disabled", true);
    });
    
    
    $(".btn-primary").click(function(event){
		
		var caseNumber =$('#caseNumber').val();
		var lcnumber=$('#lcNumber').val();
		var lcNumberType=$('#lcNumberType').val();
		var mode=$('#mode').val();
		
		if(mode=='create')
		if(caseNumber !="" && caseNumber !=null && ($('#wpYear').val() ==null || $('#wpYear').val() =='') )
			{
			bootbox.alert("Select Case Number Year ");
			return false;
			}
		
		if(lcNumberType =='MANUAL'){
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
		}
			document.forms[0].submit;
			return true;
			event.preventDefault();
		
		
	});
    
	
});


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
			
			nextIdx=(lastRow-1);
			var dd=nextIdx-1;
			alert("dd= "+ '[' + dd + ']');
			jQuery(rowObj).find("input, select").each(
					function() {

					jQuery(this).attr({
								'id' : function(_, id) {
									return id.replace('[' + dd + ']', '['
											+ nextIdx + ']');
								},
								'name' : function(_, name) {
									return name.replace('[' + dd + ']', '['
											+ nextIdx + ']');
								}
					});  
		   });

		   tbody.appendChild(rowObj);
		   
}

function addResRow()
{     
	var index=document.getElementById('respodantDetails').rows.length-1;
	    	var tableObj=document.getElementById('respodantDetails');
			var tbody=tableObj.tBodies[0];
			var lastRow = tableObj.rows.length;
			var rowObj = tableObj.rows[1].cloneNode(true);
			
			nextIdx=(lastRow-1);
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
		
		return true;
	}
});

function onChangeofPetitioncheck()
{
	 $("#petitionDetails tbody tr").each(function( index ) {
		 var $this = $(this);
	        
	        if ( $('#activeid').val() == "true") {
	        	$this.find("select, button").prop("disabled", false);
	        }
	        if ( !($('#activeid').val()) == "false") {
	        	$this.find("select, button").prop("disabled", true);
	        }

	    });	
}
function onChangeofRespodantcheck()
{
	 $("#respodantDetails tbody tr").each(function( index ) {
		 var $this = $(this);
	        
	        if ( $('#activeid').val() == "true") {
	        	$this.find("select, button").prop("disabled", false);
	        }
	        if ( !($('#activeid').val()) == "false") {
	        	$this.find("select, button").prop("disabled", true);
	        }

	    });	
}

$(document).on('click',"#res_delete_row",function (){
	var table = document.getElementById('respodantDetails');
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
		
		jQuery("#respodantDetails tr:eq(1) td span[alt='AddF']").show();
		//starting index for table fields
		var idx=0;
		
		//regenerate index existing inputs in table row
		jQuery("#respodantDetails tr:not(:first)").each(function() {
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
		
		return true;
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

