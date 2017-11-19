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

var today = Date.today();
var aWeekBack = Date.today().add({ days: -6 });
var aWeekBackFmt = aWeekBack.getDate()+"/"+Date.CultureInfo.abbreviatedMonthNames[aWeekBack.getMonth()];
var todayFmt = today.getDate()+"/"+Date.CultureInfo.abbreviatedMonthNames[today.getMonth()];
var aDayBefore = Date.today().add({ days: -1 });;
var sevenDaysBack = Date.today().add({ days: -7 });

$.ajax({url:"/pgr/dashboard/reg-resolution-trend",
	cache:false
}).done(function(pgrRegResTrendData) {
	$('#newtimeLineCompTrend').highcharts({
		chart : {
			borderRadius:'5px'
		},
	    title: {
	        text: ''
	    },
	    xAxis: {
	        type: 'datetime',
	        tickInterval: 24 * 3600 * 1000,
	        dateTimeLabelFormats: {
	            day: '%a-%e'
	        },
	        labels: {
	            rotation: -45,
	            style: {
	                fontSize: 'xx-small',
	                fontFamily: 'Verdana, sans-serif'
	            }
	        }
	    
	    },
	    yAxis: {
	        title: {
	        	text: 'No. of Complaints'
	        },
	        min:0,
			allowDecimals:false
	    },
	    tooltip: {
	        crosshairs: true,
	        shared: true
	    },
	    legend: {
	        enabled: true,
	        layout: 'vertical',
	        align: 'left',
	        verticalAlign: 'top',
	        x: 50,
	        y: 10,
	        symbolHeight:7,
	        symbolWidth:7,
	        floating: true,
	        borderWidth: 1,
	        itemStyle: {
	            font: 'xx-small Verdana, sans-serif'
	        }
	    },credits: {
	        enabled: false
	    },
	    exporting: { enabled: false },
	    plotOptions: {
	        area: {
	            fillColor: {
	                linearGradient: { x1: 0, y1: 0, x2: 0, y2: 1},
	                stops: [
	                    [0, Highcharts.getOptions().colors[0]],
	                    [1, Highcharts.Color(Highcharts.getOptions().colors[0]).setOpacity(0).get('rgba')]
	                ]
	            },
	            marker: {
	                radius: 2
	            },
	            lineWidth: 1,
	            states: {
	                hover: {
	                    lineWidth: 1
	                }
	            },
	            threshold: null
	        },
	        line:{        	 
	             marker: {
	                 radius: 2
	             },
	        	lineWidth: 1.5,
	            states: {
	                hover: {
	                    lineWidth: 1.5
	                }
	            }
	        }
	    },
	    
	    series: [{
	        name: 'Registered',
	        type: 'area',
	        pointInterval: 24 * 3600 * 1000,
	        pointStart: Date.UTC(aWeekBack.getFullYear(), aWeekBack.getMonth(), aWeekBack.getDate()),
	        yAxis: 0,
	        data: pgrRegResTrendData[0],
		    pointEnd: Date.UTC(today.getFullYear(), today.getMonth(), today.getDate())

	    }, {
	        name: 'Resolved',
	        type: 'line',
	        color:"#3C6EA1",
	        pointInterval: 24 * 3600 * 1000,
	        pointStart: Date.UTC(aWeekBack.getFullYear(), aWeekBack.getMonth(), aWeekBack.getDate()),
	        data: pgrRegResTrendData[1],
	        pointEnd: Date.UTC(today.getFullYear(), today.getMonth(), today.getDate())
	    }]
	});
});


//PGR aggregate line chart
$.ajax({url:"/pgr/dashboard/monthly-aggregate",
	cache:false
}).done(function(linedata) {
	$('#slaGraph').highcharts({
        chart: {
        	borderRadius:'5px'
        },
        title: {
            text: ''
        },
        exporting:{
        	enabled:false
        },
        colors: Highcharts.map(Highcharts.getOptions().colors, function(color) {
            return {
                radialGradient: { cx: 0.5, cy: 0.3, r: 0.7 },
                stops: [
                        [0, color],
                        [1, Highcharts.Color(color).brighten(-0.3).get('rgb')] // darken
                ]
            };
        }),
        credits:{
        	enabled:false
        },
        legend:{
        	enabled:false
        },
        xAxis: [{
            type : 'category',
            labels: {
                rotation: -45,
                style: {
                    fontSize: 'xx-small',
                    fontFamily: 'Verdana, sans-serif'
                }
            }
        }],
        yAxis: {
            plotLines: [{
                value: 0,
                width: 1,
                color: '#808080'
            }],
            title: {
            	text:'No.of Complaints',
                verticalAlign:'bottom'
            }
        },
        series: [{
            name: 'Complaint Count',
            type: 'line',
            data: linedata,
            tooltip: {
                valueSuffix: ''
            }
        }]
    });
});

$.ajax({url:"/pgr/dashboard/typewise-aggregate",
	cache:true
}).done(function(piedata) {
	
	$('#overviewGraph').highcharts({
	    chart: {
	    	borderRadius:'5px'
	        },
	        /*colors: Highcharts.map(Highcharts.getOptions().colors, function(color) {
	            return {
	                radialGradient: { cx: 0.5, cy: 0.3, r: 0.7 },
	                stops: [
	                        [0, color],
	                        [1, Highcharts.Color(color).brighten(-0.3).get('rgb')] // darken
	                ]
	            };
	        }),*/
	        exporting: { enabled: false },
	        credits: {
	            enabled: false
	        },
	        title: {
	            text: 'Complaint type Breakup'
	        },
	        legend:{
	            symbolHeight:7,
	            symbolWidth:7,
	            itemStyle: {
	                font: 'smaller Verdana, sans-serif'
	            }
	        },
	        tooltip: {
	            pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
	        },
	        plotOptions: {
	            pie: {
	            	showInLegend: false,
		            allowPointSelect: true,
		            cursor: 'pointer',
		            dataLabels: {
		                enabled: true,
		                distance: -30,
						color:'white',
						style:{'fontSize':'xx-small', "textShadow": "none"},
		                formatter: function() {
		                    return this.percentage.toFixed(1)+'%';
		                }
		            }
	            }
	        },
	        series: [{
	            type: 'pie',
	            name: 'Complaint Type Share',
	            data: piedata
	        }]
	    });
});

// Revenue
$.ajax({
	url : '/ptis/dashboard/revenueTrendForTheWeek',
	cache : false
}).done(
		function(revenueTrendForWeek) {
			$('#newtimeLineRevenueTrend').highcharts(
					{
						chart : {
							borderRadius : '5px'
						},
						colors : Highcharts.map(Highcharts.getOptions().colors,
								function(color) {
									return {
										radialGradient : {
											cx : 0.5,
											cy : 0.3,
											r : 0.7
										},
										stops : [
												[ 0, color ],
												[
														1,
														Highcharts.Color(color)
																.brighten(-0.3)
																.get('rgb') ] // darken
										]
									};
								}),
						exporting : {
							enabled : false
						},
						credits : {
							enabled : false
						},
						title : {
							text : ''
						},
						tooltip : {
							enabled : true,
							formatter : function() {
								return this.point.tooltipFormat + '<br/>'
										+ this.series.name + ': <b>' + this.y
										+ ' cr </b>';
							}
						},
						legend : {
							enabled : false
						},
						xAxis : {
							type : 'category',
							labels : {
								rotation : -45,
								style : {
									fontSize : 'xx-small',
									fontFamily : 'Verdana, sans-serif'
								}
							},
							min : 0,
							allowDecimals : false

						},
						yAxis : {
							title : {
								text : 'Amount Collected (Crores)'
							}
						},
						plotOptions : {
							series : {
								borderWidth : 0,
								minPointLength : 4,
								dataLabels : {
									enabled : false,
									format : '{point.y}',
									color : 'white'
								},
								stacking : 'normal',
								cursor : 'pointer'
							}
						},
						series : [ {
							type : 'column',
							colorByPoint : true,
							name : ' Collection Amount',
							data : revenueTrendForWeek,
						} ]
					});
		});

// Revenue Annual Targeted vs Actual
$.ajax({
	url : "/ptis/dashboard/revenueTrend",
	cache : false
}).done(
		function(revenueTrend) {
			$('#timeLineRevenueTrend').highcharts(
					{
						chart : {
							borderRadius : '5px'
						},
						colors : Highcharts.map(Highcharts.getOptions().colors,
								function(color) {
									return {
										radialGradient : {
											cx : 0.5,
											cy : 0.3,
											r : 0.7
										},
										stops : [
												[ 0, color ],
												[
														1,
														Highcharts.Color(color)
																.brighten(-0.3)
																.get('rgb') ] // darken
										]
									};
								}),
						exporting : {
							enabled : false
						},
						credits : {
							enabled : false
						},
						title : {
							text : ''
						},
						tooltip : {
							shared : true,
							valueSuffix : ' cr'
						},
						legend : {
							layout : 'vertical',
							align : 'left',
							verticalAlign : 'top',
							x : 50,
							y : 10,
							symbolHeight : 7,
							symbolWidth : 7,
							floating : true,
							borderWidth : 1,
							itemStyle : {
								font : 'xx-small Verdana, sans-serif'
							}
						},
						xAxis : {
							type : 'category',
							labels : {
								rotation : -45
							}
						},
						yAxis : {
							title : {
								text : 'Annual Collections',
								style : {
									fontSize : '15px'
								},
								verticalAlign : 'bottom'
							},
							labels : {
								format : '{value} cr',
								style : {
									color : Highcharts.getOptions().colors[1]
								}
							},
							min : 0
						},
						plotOptions : {
							column : {
								grouping : false,
								borderWidth : 0,
								minPointLength : 4,
								dataLabels : {
									enabled : false,
									format : '{point.y}%',
									color : 'white'

								},
							}
						},
						series : [ {
							type : 'column',
							// colorByPoint: true,
							name : 'Targeted Collections',
							data : revenueTrend.targeted
						}, {
							type : 'column',
							// colorByPoint: true,
							name : 'Collections',
							data : revenueTrend.actual
						} ]
					});
		});

$.ajax({
	url : "/ptis/dashboard/target-achieved",
	cache : false
}).done(
		function(targetvsachieved) {
			$('#overviewGraphCumilative').highcharts(
					{
						chart : {
							borderRadius : '5px'
						},
						title : {
							text : ''
						},
						subtitle : {
							text : ''
						},
						exporting : {
							enabled : false
						},
						credits : {
							enabled : false
						},
						xAxis : [ {
							categories : [ 'Apr', 'May', 'Jun', 'Jul', 'Aug',
									'Sep', 'Oct', 'Nov', 'Dec', 'Jan', 'Feb',
									'Mar' ]
						} ],
						yAxis : [ { // Primary yAxis
							labels : {
								format : '{value} cr',
								style : {
									color : Highcharts.getOptions().colors[1]
								}
							},
							title : {
								text : 'Cumulative',
								style : {
									fontSize : '15px'
								},
								verticalAlign : 'bottom'
							},
							min : 0
						} ],
						tooltip : {
							shared : true,
							crosshairs : true
						},
						legend : {
							layout : 'vertical',
							align : 'left',
							verticalAlign : 'top',
							x : 50,
							y : 10,
							symbolHeight : 7,
							symbolWidth : 7,
							floating : true,
							borderWidth : 1,
							itemStyle : {
								font : 'xx-small Verdana, sans-serif'
							}
						},
						series : [ {
							name : 'Targeted Collections',
							type : 'line',
							data : targetvsachieved.cumilativetarget,
							tooltip : {
								valueSuffix : 'cr'
							},
							color : Highcharts.getOptions().colors[1]
						}, {
							name : 'Actual Collections',
							type : 'line',
							yAxis : 0,
							data : targetvsachieved.cumilativeachieved,
							tooltip : {
								valueSuffix : ' cr'
							},
							color : Highcharts.getOptions().colors[0]

						}, {
							name : 'Last Year Collections',
							type : 'line',
							yAxis : 0,
							data : targetvsachieved.lastcumilativeachieved,
							tooltip : {
								valueSuffix : ' cr'
							},
							color : Highcharts.getOptions().colors[2]

						} ]
					});
		});


/*$('#newtimeLineRevenueTrend').highcharts({
	chart : {
		borderRadius:'5px'
	},
    title: {
        text: ''
    },
    xAxis: {
        type: 'datetime',
        tickInterval: 24 * 3600 * 1000,
        dateTimeLabelFormats: {
            day: '%a-%e'
        },
        labels: {
        rotation: -45,
        style: {
            fontSize: 'xx-small',
            fontFamily: 'Verdana, sans-serif'
        }
    }
    
    },
    yAxis: {
        title: {
        	text: 'Amount Collected (Crores)'
        },
    	labels: {
        	format: '{value}'
    	},
    	min:0

    },
    tooltip: {
        crosshairs: true,
        valueSuffix:' cr'
    },
    legend: {
        enabled: false
    },credits: {
        enabled: false
    },
    exporting: { enabled: false },
    plotOptions: {
        area: {
            fillColor: {
                linearGradient: { x1: 0, y1: 0, x2: 0, y2: 1},
                stops: [
                    [0, Highcharts.getOptions().colors[0]],
                    [1, Highcharts.Color(Highcharts.getOptions().colors[0]).setOpacity(0).get('rgba')]
                ]
            },
            marker: {
                radius: 2
            },
            lineWidth: 1,
            states: {
                hover: {
                    lineWidth: 1
                }
            },
            threshold: null
        }
    },

    series: [{
        type: 'area',
        name: ' Collection Amount',
        pointInterval: 24 * 3600 * 1000,
        pointStart: Date.UTC(sevenDaysBack.getFullYear(), sevenDaysBack.getMonth(), sevenDaysBack.getDate()),
        data: revenueTrend,
        pointEnd: Date.UTC(aDayBefore.getFullYear(), aDayBefore.getMonth(), aDayBefore.getDate())
    }]
});

$('#newtimeLinePaymentTrend').highcharts({
	chart : {
		borderRadius:'5px'
	},
    title: {
        text: ''
    },
    xAxis: {
        type: 'datetime',
        tickInterval: 24 * 3600 * 1000,
        dateTimeLabelFormats: {
            day: '%a-%e'
        },
        labels: {
            rotation: -45,
            style: {
                fontSize: 'xx-small',
                fontFamily: 'Verdana, sans-serif'
            }
        }
    },
    yAxis: {
        title: {
        	text: 'Amount Paid (Crores)'
        },
    	labels: {
        	format: '{value}'
    	},
    	min:0

    },
    tooltip: {
        crosshairs: true,
        valueSuffix:' cr'
    },
    legend: {
        enabled: false
    },credits: {
        enabled: false
    },
    exporting: { enabled: false },
    plotOptions: {
        area: {
            fillColor: {
                linearGradient: { x1: 0, y1: 0, x2: 0, y2: 1},
                stops: [
                    [0, Highcharts.getOptions().colors[0]],
                    [1, Highcharts.Color(Highcharts.getOptions().colors[0]).setOpacity(0).get('rgba')]
                ]
            },
            marker: {
                radius: 2
            },
            lineWidth: 1,
            states: {
                hover: {
                    lineWidth: 1
                }
            },
            threshold: null
        }
    },

    series: [{
        type: 'area',
        name: 'Payment Amount',
        pointInterval: 24 * 3600 * 1000,
        pointStart: Date.UTC(aWeekBack.getFullYear(), aWeekBack.getMonth(), aWeekBack.getDate()),
        data: paymentTrendForWeek,
        pointEnd: Date.UTC(today.getFullYear(), today.getMonth(), today.getDate())
    }]
});

//Expenditure
$('#timeLinePaymentTrend').highcharts({
    chart: {
    		borderRadius:'5px'
        },
        colors: Highcharts.map(Highcharts.getOptions().colors, function(color) {
	        return {
	            radialGradient: { cx: 0.5, cy: 0.3, r: 0.7 },
	            stops: [
	                    [0, color],
	                    [1, Highcharts.Color(color).brighten(-0.3).get('rgb')] // darken
	            ]
	        };
	    }),
        exporting:{
        	enabled:false
        },
        credits: {
            enabled: false
        },
        title: {
            text:''
        },
        tooltip: {
        	enabled:true,
        	formatter: function() {
                return  this.point.tooltipFormat +'<br/>' + this.series.name +': <b>' + this.y + ' cr </b>';
            }
		},
        legend: {
        	enabled:false
        },
        xAxis: {
            type: 'category',
            labels: {
            	rotation: -45,
                style: {
                    fontSize: 'xx-small',
                    fontFamily: 'Verdana, sans-serif'
                }
            },
            min:0,
			allowDecimals:false
            
        },
        yAxis: {
            title: {
                text: 'Amount Paid (Crores)'
            }
        },
        plotOptions: {
        	column: {
                stacking: 'normal',
                borderWidth: 0,
                minPointLength: 4,
                dataLabels: {
                    enabled: false,
                    format: '{point.y}',
                    color:'white'
                },
                stacking: 'normal',
                cursor: 'pointer'
            }
        },
        series: [{
            type: 'column',
            colorByPoint: true,
            name: 'Payment Amount',
            data: paymentTrend
        }]
    });

	/*$.ajax({url:"pgrdashboard/slaPie.do",
		cache:false
	}).done(function(piedata) {
		$('#slaGraph').highcharts({
		    chart: {
		    	borderRadius:'5px'
		    },
		    colors: Highcharts.map(Highcharts.getOptions().colors, function(color) {
		        return {
		            radialGradient: { cx: 0.5, cy: 0.3, r: 0.7 },
		            stops: [
		                    [0, color],
		                    [1, Highcharts.Color(color).brighten(-0.3).get('rgb')] // darken
		            ]
		        };
		    }),
		    exporting: {enabled: false},
		    credits : {enabled : false},
		    title:{
				text:''
		        },
		    tooltip:{
				enabled:false
		    },
		    legend :{
		    	enabled:true,
		        symbolHeight:7,
		        symbolWidth:7,
		        itemStyle: {
		            font: 'smaller Verdana, sans-serif'
		        }

		    },
		    plotOptions: {
		        pie: {
		        	showInLegend: true,
		            allowPointSelect: true,
		            cursor: 'pointer',
		            dataLabels: {
		                enabled: true,
		                distance: -30,
						color:'white',
						style:{'fontSize':'xx-small'},
		                formatter: function() {
		                    return this.y;
		                }
		            }
		        }
		    },
		    series: [{
		    	type: 'pie',
		        name: 'Complaints',
		        data: piedata
		    }]
		});
	});

	$.ajax({url:"expendashboard/performanceLine.do",
		cache:false
	}).done(function(linedata) {
		$('#performanceLineGraph').highcharts({
	        chart: {
	        	borderRadius:'5px'
	        },
	        title: {
	            text: ''
	        },
	        exporting:{
	        	enabled:false
	        },
	        colors: Highcharts.map(Highcharts.getOptions().colors, function(color) {
	            return {
	                radialGradient: { cx: 0.5, cy: 0.3, r: 0.7 },
	                stops: [
	                        [0, color],
	                        [1, Highcharts.Color(color).brighten(-0.3).get('rgb')] // darken
	                ]
	            };
	        }),
	        credits:{
	        	enabled:false
	        },
	        legend:{
	        	enabled:false
	        },
	        xAxis: [{
	            categories: ['Budget Allocated', 'Estimated', 'Work Completed', 'Payments Released'],
	            labels: {
                    rotation: -45,
                    style:{
    	            	fontSize:'smaller'
    	            }
                }
	        }],
	        credits:{
	        	text:'* Work Completed and Payment Released includes spill over estimate data',
	        	style:{
	        		color:'red',
	        		textDecoration:'none'
	        	},
	        	href:null
	        },
	        yAxis: [{ // Primary yAxis
	            labels: {
	                format: '{value} cr',
	                style: {
	                    color: Highcharts.getOptions().colors[1]
	                }
	            },
	            title: {
	            	text:'Amount',
	            	style:{
	                    fontSize: '15px'
	                },
	                verticalAlign:'bottom'
	            },
	            min:0
	        }],
	        series: [{
	            name: 'Value',
	            type: 'line',
	            data: linedata.value,
	            tooltip: {
	                valueSuffix: 'cr'
	            }
	        }]
	    });
	});
  	
	
	
	//Expenditure New Chart expense amount
	
	$('#timeLinePaymentTrend').highcharts({
		    chart: {
		    	borderRadius:'5px'
		        },
		        colors: Highcharts.map(Highcharts.getOptions().colors, function(color) {
		            return {
		                radialGradient: { cx: 0.5, cy: 0.3, r: 0.7 },
		                stops: [
		                        [0, color],
		                        [1, Highcharts.Color(color).brighten(-0.3).get('rgb')] // darken
		                ]
		            };
		        }),
		        exporting: { enabled: false },
		        credits: {
		            enabled: false
		        },
		        title: {
		            text: 'Expenditure Breakup',
		            style:{
		                   fontSize: '15px'
		                }  
		        },
		        legend:{
		            symbolHeight:7,
		            symbolWidth:7,
		            itemStyle: {
		                font: 'smaller Verdana, sans-serif'
		            }
		        },
		        tooltip: {
		            pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b><br/>Amount: {point.amt} cr'
		        },
		        plotOptions: {
		            pie: {
		            	showInLegend: true,
			            allowPointSelect: true,
			            cursor: 'pointer',
			            dataLabels: {
			                enabled: true,
			                distance: -30,
							color:'white',
							style:{'fontSize':'xx-small'},
			                formatter: function() {
			                    return this.percentage.toFixed(1)+'%';
			                }
			            }
		            }
		        },
		        series: [{
		            type: 'pie',
		            name: 'Perc',
		            data: paymentTrend
		        }]
		    });
	
	*/
	
  	