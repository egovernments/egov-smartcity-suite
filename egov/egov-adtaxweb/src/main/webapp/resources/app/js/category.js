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
jQuery(document).ready(function($) {
	
	$("#searcheditbutton").click(function() {
		var action = '/adtax/category/updateCategory/' + $('#categorydesc').val();
		$('#categoryForm').attr('method', 'get');
		$('#categoryForm').attr('action', action);
	});
	
	$("#buttonView").click(function() {
		var action = '/adtax/category/success/' + $('#categorydesc').val();
		$('#categoryForm').attr('method', 'get');
		$('#categoryForm').attr('action', action);
	});
	
	$("#subcateditbutton").click(function() {
		var action = '/adtax/subcategory/updateSubCategory/' + $('#subcategorydesc').val();
		$('#subcategoryform').attr('method', 'get');
		$('#subcategoryform').attr('action', action);
	});
	
	$("#subcateditbuttonview").click(function() {
		var action = '/adtax/subcategory/success/' + $('#subcategorydesc').val();
		$('#subcategoryform').attr('method', 'get');
		$('#subcategoryform').attr('action', action);
	});
	
	var prevdatatable;
	$('#subcatsubmit').click(function(e){
		if($('#subcategoryform').valid())
		{
			oTable= $('#adtax_searchsubcategory');
			if(prevdatatable)
			{
				prevdatatable.fnClearTable();
				$('#adtax_searchsubcategory thead tr').remove();
			}
				prevdatatable = oTable.dataTable({
					"sDom": "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-md-5 col-xs-12'i><'col-md-3 col-xs-6'l><'col-md-3 col-xs-6'l><'col-md-1 col-xs-2 text-right'p>>",
					"aLengthMenu": [[10, 25, 50, -1], [10, 25, 50, "All"]],
					"autoWidth": false,
					"bDestroy": true,
					"ajax": "/adtax/subcategory/searchSubCategory?"+$("#subcategoryform").serialize(),
					"columns" : [
					              { "data" : "category", "title":"Category"},
								  { "data" : "description", "title": "SubCategory"},
								  { "data" : "code", "title": "Code"},
								  { 
								  "data" : function(row, type, set, meta) {
										return {
											id : row.id,
										};
									},
								  "render" : function(data, type, row, meta)  
								  {
										return '<span class="add-padding"><i class="fa fa-edit history-size" onclick="subCategoryEdit('+data.id+');" class="tooltip-secondary" data-toggle="tooltip" title="Edit"></i></span><span class="add-padding"><i class="fa fa-eye history-size" class="tooltip-secondary" onclick="subCategoryView('+data.id+');" data-toggle="tooltip" title="View"></i></span>';
									   },
									  
								  },
					]
			});
			e.stopPropagation();
			
		}
		e.preventDefault();
	});
	
	$('#categories').change(function(){
		$.ajax({
			url: "/adtax/hoarding/getsubcategories-by-category",    
			type: "GET",
			data: {
				categoryId : $('#categories').val()   
			},
			dataType: "json",
			success: function (response) {
				console.log("success"+response);
				$('#subcategories').empty();
				$('#subcategories').append($("<option value=''>Select from below</option>"));
				$.each(response, function(index, value) {
					$('#subcategories').append($('<option>').text(value.description).attr('value', value.id));
				});
				
			}, 
			error: function (response) {
				console.log("failed");
			}
		});
	});
	
});

function subCategoryEdit(id)
{
	window.open("/adtax/subcategory/update/"+id,'_blank', "width=800, height=700, scrollbars=yes")
}

function subCategoryView(id)
{
	window.open("/adtax/subcategory/success/"+id, '_blank', 'width=900, height=700, top=300, left=150,scrollbars=yes')
}

