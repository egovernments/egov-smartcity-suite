jQuery('#searchApproval').click(function(e) {
	var financialYearId = $('#financialYear').val();
	if (financialYearId) {
		search();
	}
});

function getFormData($form) {
	var unindexed_array = $form.serializeArray();
	var indexed_array = {};

	$.map(unindexed_array, function(n, i) {
		indexed_array[n['name']] = n['value'];
	});

	return indexed_array;
}

jQuery('#approve,#reject').click(
		function(e) {
			var chkArray = [];
			var button = $(this).attr("id");
			$("#checkBoxList:checked").each(function() {
				chkArray.push($(this).val());
			});
			var checked = isChecked(chkArray);
			if (checked != false) {
				var selected;
				selected = chkArray.join(',');
				var comments = $('#comments').val();
				$.ajax({
					type : "POST",
					url : "/EGF/budgetapproval/"+button+ "?checkedArray="
							+ selected.toString() + "&comments=" + comments,
					success : function(response) {
						console.log("success" + response);
						window.location.href = "/EGF/budgetapproval/success"
								+ "?message=" + response
					},
					error : function(response) {
						console.log("failed");
					}
				});
			}
		});

function search() {
	drillDowntableContainer = jQuery("#resultTable");
	jQuery('.report-section').removeClass('display-hide');
	reportdatatable = drillDowntableContainer
			.dataTable({
				ajax : {
					url : "/EGF/budgetapproval/search",
					type : "POST",
					"data" : getFormData(jQuery('form'))
				},
				"bDestroy" : true,
				"autoWidth" : false,
				"sDom" : "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-xs-3'i><'col-xs-3 col-right'l><'col-xs-3 col-right'><'col-xs-3 text-right'p>>",
				"aLengthMenu" : [ [ 10, 25, 50, -1 ], [ 10, 25, 50, "All" ] ],
				"fnRowCallback" : function(row, data, index) {
					$('td:eq(0)', row).html(
							'<input type="checkbox" name="selectCheckbox" id="checkBoxList"value="'
									+ data.id + '"/>');
					if(index == 0) {
						var approvedBudgetCount = "Number of budgets already approved - " + data.approvedBudget;
						var verifiedBudgetCount = "Number of budgets in progress/verified - " + data.verifiedBudget;
				        var notInitiatedCount = "Number of budgets not yet initiated - " + data.notInitiated;
						$('#approvedBudget').html(approvedBudgetCount);
						$('#verifiedBudget').html(verifiedBudgetCount);
						$('#notInitiated').html(notInitiatedCount);
					}
					return row;
				},
				aaSorting : [],
				columns : [ {
					"data" : ""
				}, {
					"data" : "department",
					"sClass" : "text-left"
				}, {
					"data" : "referenceBudget",
					"sClass" : "text-left"
				}, {
					"data" : "parent",
					"sClass" : "text-left"
				}, {
					"data" : "reAmount",
					"sClass" : "text-left"
				}, {
					"data" : "beAmount",
					"sClass" : "text-left"
				},
				{
					"data" : "count",
					"sClass" : "text-left"
				}]
			});
}

function isChecked(chkArray) {
	var checked = $(chkArray).length;
	if (!checked) {
		alert("Please check at least one checkbox");
		return false;
	}
}