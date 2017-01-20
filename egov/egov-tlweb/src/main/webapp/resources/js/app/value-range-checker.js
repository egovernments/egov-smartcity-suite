/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *  
 *  Copyright (C) 2017  eGovernments Foundation
 *  
 *  The updated version of eGov suite of products as by eGovernments Foundation
 *  is available at http://www.egovernments.org
 *  
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see http://www.gnu.org/licenses/ or
 *  http://www.gnu.org/licenses/gpl.html .
 *  
 *  In addition to the terms of the GPL license to be adhered to in using this
 *  program, the following additional terms are to be complied with:
 *  
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 *  
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *  
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *  
 *  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

$(document).ready(function() {
	
	$(document).on('blur','.fromto tbody tr td input.fromvalue,.fromto tbody tr td input.tovalue',function(e){
		console.log('Blurred:'+$(this).data('fromto'));
		var obj = $(this);
		if($(this).data('fromto') == 'from'){
			//From range validation
			if($.trim(obj.val())){
				var current_fromval = parseInt(obj.val());
				var next_toval = parseInt(obj.closest('tr').find('input.tovalue').val());
				if(current_fromval >= next_toval){
					bootbox.alert("\"From Range\" value should be lesser than \"To Range\" value!", function(){
						obj.focus();
					});
					obj.val('');
				}else if(obj.closest('tr').index() != 0){
					var prev_toval = parseInt(obj.closest('tr').prev('tr').find('input.tovalue').val());
					if(current_fromval < prev_toval || current_fromval > prev_toval){
						bootbox.alert("\"From Range\" value should be same as previous row \"To Range\" value!", function(){
							obj.focus();
						});
						obj.val('');
					}
				}
			}
		}else if($(this).data('fromto') == 'to'){
			//To range validation
			if($.trim(obj.val())){
				var current_toval = parseInt(obj.val());
				var prev_fromval = parseInt(obj.closest('tr').find('input.fromvalue').val());
				var next_fromval = parseInt(obj.closest('tr').next('tr').find('input.fromvalue').val());
				if(current_toval <= prev_fromval){
					console.log(current_toval+'<-->'+prev_fromval);
					bootbox.alert("\"To Range\" value should be greater than \"From Range\" value!", function(){
						obj.focus();
					});
					obj.val('');
				}else if(current_toval < next_fromval || current_toval > next_fromval){
					obj.closest('tr').next('tr').find('input.fromvalue').val(current_toval);
					if(parseInt(obj.closest('tr').next('tr').find('input.fromvalue').val()) >= parseInt(obj.closest('tr').next('tr').find('input.tovalue').val())){
						bootbox.alert("\"To Range\" value should be greater than \"From Range\" value!", function(){
							obj.closest('tr').next('tr').find('input.tovalue').focus();
						});
						obj.closest('tr').next('tr').find('input.tovalue').val('')
					}
				}
			}
		}
		
	});
	
	$(document).on('focus','.fromto tbody tr td input.fromvalue,.fromto tbody tr td input.tovalue',function(e){
		console.log('Focussed:'+$(this).data('fromto'));
		e.preventDefault();
		e.stopPropagation();
	});
		
});
