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

jQuery(document).ready(function() {
			jQuery('#view').click(function() {
						if (jQuery("#name").val() == '') {
							bootbox.alert("Apartment type Required");
							return false;
							}	else {
								var apartment = jQuery("#name").val();
								jQuery('#apartmentTypeForm').attr('method', 'get');
								jQuery('#apartmentTypeForm').attr('action','/ptis/apartment/view/'+ apartment);
								}
							});
					datefun();
					jQuery('#isResidential').change(function() {
						if ($(this).is(":checked")) {
							jQuery('#shopdetails').removeClass('hide');
							
							$("#shopTable").find("input[type='text']").attr("required","required" );
						}
						else{
							jQuery('#shopdetails').addClass('hide');
							$("#shopTable").find("input[type='text']").removeAttr("required");
							}
				    	});
					
					jQuery('#licenseStatus').change(function() {
						$(this).val($(this).is(":checked") ? true : false);
				    	});
					
				
				jQuery(document).on('click',"#add_row",function (){	
					
					var rowCount = $('#shopTable tbody tr').length;
					var valid = true;
					$('#shopTable tbody tr').each(function(index,value){
						$('#shopTable tbody tr:eq('+index+') td input[type="text"]').each(function(i,v){
							if(!$.trim($(v).val())){
								valid = false;
								bootbox.alert("Enter all values for existing rows!",function(){
									$(v).focus();
								});
								return false;
							}
						});
					});
					if(valid){
					var newRow = $('#shopTable tbody tr:first').clone();
					newRow.find("input").each(function(){
		    	        $(this).attr({
		    	        	'name': function(_, name) { return name.replace(/\[.\]/g, '['+ rowCount +']'); }
		    	        });
		    	    });
					$('#shopTable tbody').append(newRow);
					$('#shopTable tbody tr:last').find('input[type="text"]').val('');
					patternvalidation();
					datefun();
					}
				});								
				
				
				
				$('#shopTable tbody').on('click','tr td .delete_row',function(e){
					$(this).closest( 'tr').remove();
					$('#shopTable tbody tr').each(function(index,value){
						$(this).find("input").each(function(){
			    	        $(this).attr({
			    	        	'name': function(_, name) { return name.replace(/\[.\]/g, '['+ index +']'); }
			    	        });
			    	    });
					});
					
					$(this).closest( 'tr').remove();
					});				
				
				});		
			function datefun(){
				$(".licenseValidity").datepicker( {
				    format: "mm-yyyy",
				    viewMode: "months", 
				    minViewMode: "months"
				});
				}
					

				
				




