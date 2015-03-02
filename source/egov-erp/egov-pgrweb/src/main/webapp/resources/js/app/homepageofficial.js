$(document).ready(function()
{
	$('#container1').highcharts({
        chart: {
            plotBackgroundColor: null,
            plotBorderWidth: null,
            plotShadow: false
		},
        title: {
            text: 'By Task Type'
		},
        tooltip: {
            pointFormat: '{series.name}: <b>{point.y:.0f}</b>'
		},
        plotOptions: {
            pie: {
                allowPointSelect: true,
                cursor: 'pointer',
                dataLabels: {
                    enabled: true,
                    format: '<b>{point.name}</b>: {point.y:.0f}',
                    style: {
                        color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black'
					}
				}
			}
		},
        series: [{
            type: 'pie',
            name: 'Pending Tasks',
            data: [
			['Create Property',   45],
			['Expense Bill',       12],
			{
				name: 'Modify Property',
				y: 6,
				sliced: true,
				selected: true
			},
			['Expense',   14],
			['Travel',     23],
            ]
		}]
	});
	
	$('#container2').highcharts({
        chart: {
            plotBackgroundColor: null,
            plotBorderWidth: null,
            plotShadow: false
		},
        title: {
            text: 'By No. of days'
		},
        tooltip: {
            pointFormat: '<b>{point.extraValue}</b>'
		},
        plotOptions: {
            pie: {
                allowPointSelect: true,
                cursor: 'pointer',
                dataLabels: {
                    enabled: true,
                    format: '{point.extraValue}',
                    style: {
                        color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black'
					}
				}
			}
		},
        series: [{
            type: 'pie',
            name: 'days',
            data:
			[{
				name: 'Range',
                y: 23.0,
                extraValue: 'Less than 3 days'
			},
			{
				name: 'Range',
                y: 12.0,
                extraValue: '3 - 7 days'
			},
			{
				name: 'Range',
                y: 7.0,
                extraValue: '7 - 15 days'
			},
			{
				name: 'Range',
                y: 59.0,
                extraValue: '> 15 days'
			}]
		}]
	});
	
	
	tableContainer1 = $("#official_inbox"); 
	
	tableContainer1.dataTable({
		"sPaginationType": "bootstrap",
		"sDom": "<'row'<'col-xs-12 hidden col-right'f>r>t<'row add-border'<'col-md-6 col-xs-12'i><'col-md-3 col-xs-6'l><'col-md-3 col-xs-6 text-right'p>>",
		"aLengthMenu": [[10, 25, 50, -1], [10, 25, 50, "All"]],
		"columns": [
		{ "width": "15%" },
		{ "width": "20%" },
		{ "width": "20%" },
		{ "width": "20%" },
		{ "width": "25%" }
		],
	});
	
	$('#inboxsearch').keyup(function(){
		tableContainer1.fnFilter(this.value);
	});
});