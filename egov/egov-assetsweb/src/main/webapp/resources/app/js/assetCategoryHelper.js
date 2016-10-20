function getFormData($form) {
	var unindexed_array = $form.serializeArray();
	var indexed_array = {};

	$.map(unindexed_array, function(n, i) {
		indexed_array[n['name']] = n['value'];
	});

	return indexed_array;
}

function callAjaxSearch() {
	drillDowntableContainer = jQuery("#resultTable");
	jQuery('.report-section').removeClass('display-hide');
	reportdatatable = drillDowntableContainer
	.dataTable({
		ajax : {
			url : "/egassets/assetcategory/ajaxsearch/"
				+ $('#mode').val(),
				type : "POST",
				"data" : getFormData(jQuery('form'))
		},
		"fnRowCallback" : function(row, data, index) {
			$(row).on(
					'click',
					function() {
						console.log(data.id);
						window.open('/egassets/assetcategory/'
								+ $('#mode').val() + '/' + data.id, '',
						'width=800, height=600, scrollbars=yes');
					});
		},
		"bDestroy" : true,
		"sDom" : "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-xs-3'i><'col-xs-3 col-right'l><'col-xs-3 col-right'<'export-data'T>><'col-xs-3 text-right'p>>",
		"aLengthMenu" : [ [ 10, 25, 50, -1 ], [ 10, 25, 50, "All" ] ],
		"oTableTools" : {
			"sSwfPath" : "../../../../../../egi/resources/global/swf/copy_csv_xls_pdf.swf",
			"aButtons" : [ "xls", "pdf", "print" ]
		},
		aaSorting : [],
		columns : [ {
			"data" : "code",
			"sClass" : "text-left"
		}, {
			"data" : "name",
			"sClass" : "text-left"
		}, {
			"data" : "assetType",
			"sClass" : "text-left"
		}, {
			"data" : "parent",
			"sClass" : "text-left"
		}, {
			"data" : "uom",
			"sClass" : "text-left"
		}]
	});
}

$(document).ready(function() {

	$('#parent').change(function() {
		var parentId = jQuery('#parent').val();
		console.log(parentId);
		if(parentId === '')
		{
			$('#assetAccountCode').val("");
			$('#accDepAccountCode').val("");
			$('#revAccountCode').val("");
			$('#depExpAccountCode').val("");
			$('#uom').val("");
		}
		else
		{
			$.ajax({
				url: '/egassets/assetcategory/getParentAccounts/'+ parentId,
				type: "GET",
				dataType: "json", 
				success: function(response) {
					var stringArray = response.split(",");
					$.each(stringArray, function(index, value) {
						var array = value.split(":");
						if(array[1] !== "")
							$('#'+array[0]).val(array[1]);
					});
				},
				error: function(response){
					console.log("Failed");
				}
			});
		}
	});

	$('input[type="text"]').keypress(function(e) {
		if (e.keyCode == '13') { 
			e.preventDefault(); 
		} 
	});

	$('#btnsearch').click(function(e) {
		callAjaxSearch();
	});

	$("#addrow").click(function(event) {
		addRowAssetCategory();
	});

});

function replaceAll(str, find, replace) {
  return str.replace(new RegExp(find, 'g'), replace);
}


function addRowAssetCategory()
{
	var rowCount = $('#result tr').length;
	/* if(!checkforNonEmptyPrevRow()) return false;*/
	var content = $('#resultrow0').html();
	resultContent = content.replace(/0/g, rowCount - 1);
	$(resultContent).find("input").val("");
	$(resultContent).find("select").val("");
	$textArea = $(resultContent).find("textarea");
	var textAreaValueWithOuterHtml=$textArea[0].outerHTML;
	$textArea.html("");
	var replaceTextAreaContent=$textArea[0].outerHTML;
	resultContent = resultContent.replace(textAreaValueWithOuterHtml,replaceTextAreaContent); /*replaceAll(resultContent, '/'+ textAreaValueWithOuterHtml+'/g', replaceTextAreaContent);*/	
	resultContent = replaceAll(resultContent, /selected="selected"/g, "");
	$row=$("<tr>" + resultContent + "</tr>");
	$row.find("input[type='text']").val("");
	$row.find("input[name$='id']").val("");
	$row.find('input[type="checkbox"]').each(function(){
		$(this).prop('checked', false);
	});
	$('#result > tbody:last').append($row);	
	patternvalidation();
}

function deleteThisRow(obj) {

	//This is to show loading effect till the row is deleted.
	

	var idx=$(obj).data('idx');
	var categoryPropertyId = $('input[name="categoryProperties['+ $(obj).data('idx') +'].id"]').val();
	var tbl = document.getElementById('result');
	var rows=tbl.rows.length;
	
	//included th also so 1+1
	if(rows === 2)
	{
		alert("Can't delete!");
		return false;
	}
	
	
	if(categoryPropertyId)
	{
		$.ajax({
			url: '/egassets/assetcategory/categoryproperty/delete?categoryPropertyId=' +categoryPropertyId,
			type: "GET",
			beforesend:function()
			{
				$('.loader-class').modal('show', {backdrop: 'static'});
			},
			success: function(response) {
				tbl.deleteRow((idx+1));
				regenerateTable();
				$('.loader-class').modal('hide');
			},
			error: function(response){
				console.log("Failed");
				$('.loader-class').modal('hide');
			}
		});
	}
	else
	{
		tbl.deleteRow((idx+1));
		regenerateTable();
	}

}

function regenerateTable()
{
	//starting index for table fields
	var idx=0;

	jQuery("#result tbody tr").each(function() {
		jQuery(this).find("input, select, button").each(function() {
			var customAttrs={};
			if($(this).attr('id'))
			{
				console.log('coming inside!');
				customAttrs['id']=function(_,id){
					return id.replace(/\[.\]/g, '['+ idx +']'); 
				};
			}
			if($(this).attr('name'))
			{
				customAttrs['name']=function(_,id){
					return id.replace(/\[.\]/g, '['+ idx +']'); 
				};
			}
			if($(this).attr('data-idx'))
			{
				customAttrs['data-idx']=function(_,dataIdx){
					return idx;
				};
			}		
			jQuery(this).attr(customAttrs);

		});
		idx++;
	});
}
