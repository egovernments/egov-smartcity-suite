
	var responsiveHelper;
	var breakpointDefinition = {
		tablet: 1024,
		phone : 480
	};
	var tableContainer;
	
	jQuery(document).ready(function($)
	{
		
		$(".opencomplaint").click(function(e){
			e.preventDefault(); // this will prevent the browser to redirect to the href
			// if js is disabled nothing should change and the link will work normally
			var url = $(this).attr('href');
			var windowName = $(this).attr('class');
			window.open(url, windowName, "height=600,width=1000,scrollbars=yes");
		});
		
		tableContainer = $("#table-1");
		
		$('#searchtable').keyup(function(){
			tableContainer.fnFilter( $(this).val() ).draw();;
		});
		
		tableContainer.dataTable({
			"sPaginationType": "bootstrap",
			"aLengthMenu": [[10, 25, 50, -1], [10, 25, 50, "All"]],
			"bStateSave": true,
			
			
			// Responsive Settings
			bAutoWidth     : false,
			fnPreDrawCallback: function () {
				// Initialize the responsive datatables helper once.
				if (!responsiveHelper) {
					responsiveHelper = new ResponsiveDatatablesHelper(tableContainer, breakpointDefinition);
				}
			},
			fnRowCallback  : function (nRow, aData, iDisplayIndex, iDisplayIndexFull) {
				responsiveHelper.createExpandIcon(nRow);
			},
			fnDrawCallback : function (oSettings) {
				responsiveHelper.respond();
			}
		});
		
		$(".dataTables_wrapper select").select2({
			minimumResultsForSearch: -1
		});
		
		$('#searchtable').val($('.dataTables_filter input').val());
		
	});
