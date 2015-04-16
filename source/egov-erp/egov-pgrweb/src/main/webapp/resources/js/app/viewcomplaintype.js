$(document)
		.ready(
				function() {
					$('#view-complaint-type')
							.dataTable(
									{
										processing : true,
										serverSide : true,
										sort : false,
										filter : true,
										responsive : true,
										ajax : "/pgr/complainttype/ajax/result",
										"aLengthMenu" : [ [ 10, 25, 50, -1 ],
												[ 10, 25, 50, "All" ] ],
										"sDom" : "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-md-6 col-xs-12'i><'col-md-3 col-xs-6'l><'col-md-3 col-xs-6 text-right'p>>",
										columns : [ {
											"mData" : "name",
											"sTitle" : "Name",
										}, {
											"mData" : "code",
											"sTitle" : "Code"
										}, {
											"mData" : "department",
											"sTitle" : "Department"
										}, {
											"mData" : "locationRequired",
											"sTitle" : "Is Location Required"
										}, {
											"mData" : "isActive",
											"sTitle" : "Is Active"
										} ],
									});

					$('#view-complaint-type tbody').on(
							'click',
							'tr',
							function() {
								if ($(this).hasClass('apply-background')) {
									$(this).removeClass('apply-background');
								} else {
									$('#view-complaint-type tbody tr')
											.removeClass('apply-background');
									$(this).addClass('apply-background');
								}

							});

				});