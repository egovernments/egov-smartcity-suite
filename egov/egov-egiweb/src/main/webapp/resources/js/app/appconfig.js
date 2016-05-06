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
	
	// Instantiate the Bloodhound suggestion engine
	/*var complaintlocation = new Bloodhound({
		datumTokenizer: function (datum) {
			return Bloodhound.tokenizers.whitespace(datum.value);
		},
		queryTokenizer: Bloodhound.tokenizers.whitespace,
		remote: {
			url: 'modules?moduleName=%QUERY',
			filter: function (data) {
				// Map the remote source JSON array to a JavaScript object array
				return $.map(data, function (cl) {
					return {
						name: cl.name,
						value: cl.id
					};
				});
			}
		}
	});
	
	// Initialize the Bloodhound suggestion engine
	complaintlocation.initialize();
	
	// Instantiate the Typeahead UI
	$('#module').typeahead({
		  hint: true,
		  highlight: true,
		  minLength: 3
		}, {
		displayKey: 'name',
		source: complaintlocation.ttAdapter()
	}).on('typeahead:selected', function(event, data){    
		$("#module").val(data.name);    
		$("#moduleid").val(data.value);    
    });
	*/
	$('#appModuleName').change(function(){
		$.ajax({
			url: "/egi/appConfig/ajax-appConfigpopulate",     
			type: "GET",
			data: {
				appModuleName : $('#appModuleName').val()   
			},
			dataType: "json",
			success: function (response) {
				console.log("success"+response);
				$('#appKeyName').empty();
				$('#appKeyName').append($("<option value=''>Select</option>"));
				$.each(response, function(index, value) {
					$('#appKeyName').append($('<option>').text(value.keyName).attr('value', value.id));
				});
				
			}, 
			error: function (response) {
				console.log("failed");
			}
		});
	});
	
});



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

function validateDate(obj)
{

	var todaysDate=getTodayDate();
	document.getElementById("egi_error_area").style.display='none';
	if(compareDate(obj.value,todaysDate) == 1)
	{		
		 var color = "#FF0000";
		document.getElementById("egi_error_area").style.display = '';
		document.getElementById("egi_error_area").innerHTML = '<font color="+color+">Effective Date should not be less than todays date</font>';
		obj.value="";
		return false;
		}
}

	function checkSplCharIncludingFewSplchar(obj)
	{
		var iChars = ",";
		document.getElementById("egi_error_area").style.display='none';
		for (var i = 0; i < obj.value.length; i++)
		{
			if (iChars.indexOf(obj.value.charAt(i)) != -1)
			{
				
				 var color = "#FF0000";
				document.getElementById("egi_error_area").style.display = '';
				document.getElementById("egi_error_area").innerHTML = '<font color="+color+">Special characters comma(,) is not allowed so add New Row for Second Value</font>';
				obj.value="";
				return false;
			}
		}
		return true;
	}
