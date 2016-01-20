/*#-------------------------------------------------------------------------------
# eGov suite of products aim to improve the internal efficiency,transparency, 
#    accountability and the service delivery of the government  organizations.
# 
#     Copyright (C) <2015>  eGovernments Foundation
# 
#     The updated version of eGov suite of products as by eGovernments Foundation 
#     is available at http://www.egovernments.org
# 
#     This program is free software: you can redistribute it and/or modify
#     it under the terms of the GNU General Public License as published by
#     the Free Software Foundation, either version 3 of the License, or
#     any later version.
# 
#     This program is distributed in the hope that it will be useful,
#     but WITHOUT ANY WARRANTY; without even the implied warranty of
#     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#     GNU General Public License for more details.
# 
#     You should have received a copy of the GNU General Public License
#     along with this program. If not, see http://www.gnu.org/licenses/ or 
#     http://www.gnu.org/licenses/gpl.html .
# 
#     In addition to the terms of the GPL license to be adhered to in using this
#     program, the following additional terms are to be complied with:
# 
# 	1) All versions of this program, verbatim or modified must carry this 
# 	   Legal Notice.
# 
# 	2) Any misrepresentation of the origin of the material is prohibited. It 
# 	   is required that all modified versions of this material be marked in 
# 	   reasonable ways as different from the original version.
# 
# 	3) This license does not grant any rights to any user of the program 
# 	   with regards to rights under trademark law for use of the trade names 
# 	   or trademarks of eGovernments Foundation.
# 
#   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
#-------------------------------------------------------------------------------*/
$(document).ready(function(){
	
	//var formdate=$('#formDate').val();
	  
	  $('#buttonid').click(function() {
		  if(!validateTapExecutionDate())
			{
			return false;
			
			}
		  else{
			  if($('#formDate').val() !=undefined)
		  $.ajax({
	            url: '/wtms/ajax-WaterRatescombination',
	            type: "GET",
	            data: {
			categoryType:'NON_METERED',
			waterSource:$('#waterSource').val(),
	            	usageType: $('#usageType').val(),
			pipeSize :$('#pipeSize').val(),
	            	
	            },
	            dataType : 'json',
	            success: function (response) {
	    			console.log("success"+response);
	    			//bootbox.alert("response"+response);
	    			if(response > 0){
	    				if(!overwritedonation(response))
	    				return false;
		    			}
	    			else{
	    				 document.forms[0].submit();
	    				 return true;
	    			}
	    		},error: function (response) {
	    			console.log("failed");
	    		}
	        });
		  }
		   
     });
});

function overwritedonation(res)
{
	var r=confirm("With entered combination and from same effective date monthly rent is present. Do you want to overwrite it?")
	if (r ==true){	
		console.log('came as true');
		document.forms[0].submit();
	}
	else
	{
		console.log('came as false');
	    //document.forms[0].reset();
	    return false;
	}
}
	function compareDate(dt1, dt2){			
	/*******		Return Values [0 if dt1=dt2], [1 if dt1<dt2],  [-1 if dt1>dt2]     *******/
		var d1, m1, y1, d2, m2, y2, ret;
		dt1 = dt1.split('/');
		dt2 = dt2.split('/');
		ret = (eval(dt2[2])>eval(dt1[2])) ? 1 : (eval(dt2[2])<eval(dt1[2])) ? -1 : (eval(dt2[1])>eval(dt1[1])) ? 1 : (eval(dt2[1])<eval(dt1[1])) ? -1 : (eval(dt2[0])>eval(dt1[0])) ? 1 : (eval(dt2[0])<eval(dt1[0])) ? -1 : 0 ;										
		return ret;
	}
	function getTodayDate()
	{
	var date;
	    var d = new Date();
	var curr_date = d.getDate();
	var curr_month = d.getMonth();
		curr_month++;
	var curr_year = d.getFullYear();
	    date=curr_date+"/"+curr_month+"/"+curr_year;
	    return date;
	}
	function validateTapExecutionDate() {
	var formdate= $('#formDate').val();
	var todaysDate=getTodayDate();
	if(compareDate(formdate,todaysDate) == 1  )
	{		
		bootbox.alert('Effective Date should not be less than todays date');
		obj.value="";
		return false;
		}
	else
		{
		return true;
		}
	}