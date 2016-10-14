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


var count = $("#subcat tbody  tr").length - 1;

$('#addrow').click(function(){
	var $tableBody = $('#subcat').find("tbody"),
    $trLast = $tableBody.find("tr:last"),
    $trNew = $trLast.clone();
	if( !$.trim($trLast.find('select.feeType').val()) || !$.trim($trLast.find('select.rateType').val()) || !$.trim($trLast.find('select.uom').val()))
	{
		bootbox.alert('All Values are mandatory !');
	}else{
		count++;
		$trNew.find("select").each(function(){
	        $(this).attr({
	        	'name': function(_, name) { return name.replace(/\[.\]/g, '['+ count +']'); } ,
	        	'id': function(_, id) { return id.replace(/\[.\]/g, '['+ count +']'); }
	        });
	    });
		$trLast.after($trNew);
		$trNew.show().find('select').val('').removeAttr('disabled').addClass('dynamicInput');
        $trNew.find('select.markedForRemoval').val('false');
	}
	});

$(document).on('click','#deleterow',function(){
	var length = $('#subcat').find("tbody tr:visible").length;
	if(length == 1){
		bootbox.alert('First row cannot be deleted!');
	}else{
        if($(this).data('func')){
            $(this).closest('tr').remove();
            var idx=0;
            //regenerate index existing inputs in table row
            jQuery("#subcat tbody tr").each(function() {
             jQuery(this).find("input").each(function() {
             jQuery(this).attr({
             'id': function(_, id) {
             return id.replace(/\[.\]/g, '['+ idx +']');
             },
             'name': function(_, name) {
             return name.replace(/\[.\]/g, '['+ idx +']');
             }
             });
             });

             idx++;
             });
        } else {
            if($(this).closest('tr').find('select').hasClass('dynamicInput')){
            	console.log('Dynamic Row deleted');
            	$(this).closest('tr').remove();
                var idx=0;
                //regenerate index existing inputs in table row
                jQuery("#subcat tbody tr").each(function() {
                 jQuery(this).find("input").each(function() {
                 jQuery(this).attr({
                 'id': function(_, id) {
                 return id.replace(/\[.\]/g, '['+ idx +']');
                 },
                 'name': function(_, name) {
                 return name.replace(/\[.\]/g, '['+ idx +']');
                 }
                 });
                 });

                 idx++;
                 });
            }
            else{
            	console.log('Existing Row deleted');
            	$(this).closest('tr').find('input[type=hidden]').val('true');
            	$(this).closest('tr').hide();
            }
            
        }
		
	}
		
});

$('#categories').change(function(){
	$.ajax({
		url: "/tl/licensesubcategory/getsubcategories-by-category",    
		type: "GET",
		data: {
			categoryId : $('#categories').val()   
		},
		dataType: "json",
		success: function (response) {
			console.log("success"+response);
			$('#licenseSubCategories').empty();
			$('#licenseSubCategories').append($("<option value=''>Select from below</option>"));
			$.each(response, function(index, value) {
				$('#licenseSubCategories').append($('<option>').text(value.name).attr('value', value.code));
			});
			
		}, 
		error: function (response) {
			console.log("failed");
		}
	
	});
});

$('#subcat').on('change', 'select', function(){
    var obj = $("[id$='feeType']");
    for (var i=0 ;i < $("[id$='feeType']").length - 1; i++) {
    	for (var j=i+1 ;j <= $("[id$='feeType']").length - 1; j++) {
    		if(obj[i].options[obj[i].selectedIndex].text == obj[j].options[obj[j].selectedIndex].text){
    			var selected = obj[j];
    			bootbox.alert('The Fee Type  '+selected.options[selected.selectedIndex].text+' is already selected',function(){ $(selected).val(''); }); 	
    		}
    	}
    }
});