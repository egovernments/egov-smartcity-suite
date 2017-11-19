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

//Initial view
var lastbtnId = "revenueOv";
var lastWinId = "overviewWin";
if(isdefault) {
	$("#deflt").addClass("active");
}

$(".menu-item").click(function(){
	var btnId = $(this).attr('id');
	var winId = $(this).data('win');
	if(lastbtnId === btnId) {
		return;
	} else { 
		$('#'+lastWinId).hide();
		lastWinId = winId;
		$('#'+lastWinId).show();
	}
	resetToggleBtns(winId);	
	lastWinId = winId;
	lastbtnId = btnId;
	var fn = window[$(this).data('fn')];
	if(typeof fn === "function") {	
		setTimeout(fn,10);
	}
});

function resetToggleBtns(win) {
	if(win == "performanceWin") {
		$(".performBtn").removeClass("btn-primary");
		$(".performBtn").addClass("btn-default");
		perfLastTglBtn = $('#barbtn');
		$("#barbtn").addClass("btn-primary");
	} 
}

//#############################**PERFORMANCE STARTS**#####################################//

var perfLastTglBtn = $('#barbtn');
$(".performBtn").on("click", function(){
	toggleBtnHandler($(this),perfLastTglBtn);
	perfLastTglBtn = $(this);
});

var table = null;
var anOpen = [];

function collectionEfficiency() {
	$("#page-top").mask('');
	$("#performanceGraph").empty();
	if($("#performanceGraph").highcharts()) {
		$("#performanceGraph").highcharts().destroy();
	}
	setTitle("Collection Eff.");
	performanceTabular();
	performanceBar();
} 

function performanceTabular() {
	$('#performanceTable').html( '<table width="100%" style="display:none" id="zonewisePerformanceTbl" class="display responsive no-wrap table table-striped table-bordered table-hover dataTable no-footer"  aria-describedby="dataTables-example_info">'+
			'<thead><tr><th rowspan="2">Zone</th><th colspan="2">Overall Performance</th><th colspan="4">Monthly Performance</th></tr>'+
			'<tr><th>Collection %age</th><th>Overall Rank</th><th>Amount Targeted (Crore)</th><th>Amount Collected (Crore)</th><th>Collection %age</th><th>Rank</th></tr></thead></table>' );
	 table = $('#zonewisePerformanceTbl').dataTable( {
    	processing: false,
        serverSide: true,
        sort:false,
        filter:false,
        responsive:true,
        paginate:false,
        ajax: "/ptis/dashboard/performance-tabular?rnd="+Math.random(),
        columns: [
            { data:"zone",render: function (oObj){
            	var nrmStr = oObj.replace(/\s+/g, '');
            	return oObj+"<span class=\"btn btn-primary btn-circle-xs btn-xs pull-right\" onclick=\"performanceTabularDrill(this,'"+oObj+"')\"><i id='btn"+nrmStr+"' class='fa fa-plus-circle'></i></span>";} 
            },
            { data:"collectionPerc" },
            { data:"overallrank" },
            { data:"amtTargeted" },
            { data:"amt_collectd" },
            { data:"percCollections" },
            { data:"rank" }
        ],
	 fnDrawCallback : function() {
		 $('#zonewisePerformanceTbl').show();
    	 $("#page-top").unmask();
    }
    } ); 
}

function performanceTabularDrill(link,zoneName) {
	var tr = $(link).closest('tr');
	var nz = zoneName.replace(/\s+/g, '');
	var i = $.inArray( nz, anOpen );
	if ( i === -1 ) {
		$('#btn'+nz).removeClass("fa-plus-circle");
		$('#btn'+nz).addClass("fa-minus-circle");
		var content = '<table style="font-size:11px;display:none" width="100%" id="'+nz+'PerformanceTbl" class="display responsive no-wrap table table-striped table-bordered table-hover dataTable no-footer"  aria-describedby="dataTables-example_info">'+
		'<thead><tr><th rowspan="2">Ward</th><th colspan="2">Overall Performance</th><th colspan="4">Monthly Performance</th></tr>'+
		'<tr><th>Collection %age</th><th>Overall Rank</th><th>Amount Targeted (Lakh)</th><th>Amount Collected (Lakh)</th><th>Collection %age</th><th>Rank</th></tr></thead></table>';
	  	table.fnOpen(tr,content,'details');
	  	$('#zonewisePerformanceTbl').mask('');
	  	$('#'+nz+'PerformanceTbl').dataTable( {
	    	processing: false,
	        serverSide: true,
	        sort:false,
	        filter:false,
	        responsive:true,
	        paginate:false,
	        ajax: "/ptis/dashboard/performance-tabularDrill?zn="+zoneName+"&rnd="+Math.random(),
	        columns: [
	            { data:"ward"},
	            { data:"collectionPerc" },
	            { data:"overallrank" },
	            { data:"amtTargeted" },
	            { data:"amt_collectd" },
	            { data:"percCollections" },
	            { data:"rank" }
	        ],
	   	 	fnDrawCallback : function() {
	   	 		$('#'+nz+'PerformanceTbl').show();
	   	 		$("#zonewisePerformanceTbl").unmask();
	   	 	}
	    } );
	  	
	    anOpen.push(nz);
	} else {
		table.fnClose(tr);
	    anOpen.splice( i, 1);
	    $('#btn'+nz).addClass("fa-plus-circle");
		$('#btn'+nz).removeClass("fa-minus-circle");
			
	 }
}


function performanceBar() {
	$.ajax({url:"/ptis/dashboard/performance-bar?levelName=top",
		cache:false
	}).done(function(revenuebardata) {
		$('#performanceGraph').highcharts({
		   chart: {
		    		backgroundColor:null,
		            plotBackgroundColor: null,
		            plotBorderWidth: null,
		            plotShadow: false,
		            events: {
		               /* drilldown: function (e) {
		                	var chart = this;
		                	$.get("revenuedashboard.do?isAjax=true&data=performanceBar&zoneName=" + e.point.name+"&levelName="+e.point.levelName,function(data) {
		                		chart.addSeriesAsDrilldown(e.point, data);
		                    });
		                }    */           
		            }
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
		            text: ''
		        },
		        tooltip: {
                    shared: true,
                    valueSuffix:' %'
				},
		        legend: {
		        	enabled:true
		        },
		        xAxis: {
                    type: 'category'
                },
                yAxis: {
                    title: {
                        text: 'Collection %age'
                    }
                },
		        plotOptions: {
		        	series: {
                        borderWidth: 0,
                        minPointLength: 4,
                        dataLabels: {
                            enabled: false,
                            format: '{point.y}%',
                            color:'white'
                            	
                        },
                    }
		        },
		        series: [{
		            type: 'column',
		           // colorByPoint: true,
		            name: 'Overall',
		            data: revenuebardata.overallPerc
		        },{
		            type: 'column',
		         //   colorByPoint: true,
		            name: 'Monthly',
		            data: revenuebardata.monthlyPerc
		        }],
		        /*drilldown: {
		            series: [{name:"N13",id:"ovrallN13",data:[["N179",100.00]]}]
		        }*/
			});
		$("#page-top").unmask();
	});
}

//#############################**PERFORMANCE ENDS**#####################################//

//#############################**OVERVIEW STARTS**#####################################//

function overviewArea() {
	$("#page-top").mask('');
	if($("#overviewGraph").highcharts()) {
		$("#overviewGraph").highcharts().destroy();
		$("#overviewGraphCumilative").highcharts().destroy();
	}
	setTitle("Target vs Actual");
	$.ajax({url:"/ptis/dashboard/target-achieved",
		cache:false
	}).done(function(targetvsachieved) {
		$('#overviewGraph').highcharts({
			chart: {
	    		backgroundColor:null,
	            plotBackgroundColor: null,
	            borderWidth: 1,
	            borderColor:'#000',
	            marginBottom:100
			},
			title:{text:''},
	        subtitle:{
	        	text:'Monthly Collections: Targetted vs Actuals',
	        	y:-20,
	        	verticalAlign:'bottom'
	        },
	        exporting: {enabled: false},
	        credits : {enabled : false},
	        xAxis: [{
	            categories: ['Apr', 'May', 'Jun','Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec','Jan','Feb','Mar']
	        }],
	        yAxis: [{ // Primary yAxis
	            labels: {
	                format: '{value} cr',
	                style: {
	                    color: Highcharts.getOptions().colors[1]
	                }
	            },
	            title: {
	                text:'Monthly',
	                style:{
	                    fontSize: '15px'
	                },
	                verticalAlign:'bottom'
	            },
	            min:0
	        }],
	        tooltip: {
	            shared: true,
	            crosshairs: true
	        },
	        legend: {
	            layout: 'vertical',
	            align: 'left',
	            x: 100,
	            verticalAlign: 'top',
	            y: 0,
	            floating: true,
	            backgroundColor: (Highcharts.theme && Highcharts.theme.legendBackgroundColor) || '#FFFFFF'
	        },
	        series: [{
	            name: 'Targeted Collections',
	            type: 'area',
	            data: targetvsachieved.target,
	            tooltip: {
	                valueSuffix: 'cr'
	            },
	            color:Highcharts.getOptions().colors[1]
	        },
	        {
	            name: 'Actual Collections',
	            type: 'area',
	            yAxis: 0,
	            data: targetvsachieved.achieved,
	            tooltip: {
	                valueSuffix: ' cr'
	            },
	            color:Highcharts.getOptions().colors[0]

	        },
	        {
	            name: 'Last Year Collections',
	            type: 'area',
	            yAxis: 0,
	            data: targetvsachieved.lastAcheived,
	            tooltip: {
	                valueSuffix: ' cr'
	            },
	            color:Highcharts.getOptions().colors[2]

	        }
	        ]
	    });
		$('#overviewGraphCumilative').highcharts({
			chart: {
	    		backgroundColor:null,
	            plotBackgroundColor: null,
	            borderColor:'#000',
	            borderWidth: 1,
	            marginBottom:100
			},
			title:{text:''},
	        subtitle:{
	        	text:'Cumulative Collections till date: Targetted vs Actuals',
	        	y:-20,
	        	verticalAlign:'bottom'
	        },
	        exporting: {enabled: false},
	        credits : {enabled : false},
	        xAxis: [{
	            categories: ['Apr', 'May', 'Jun','Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec','Jan','Feb','Mar']
	        }],
	        yAxis: [{ // Primary yAxis
	            labels: {
	                format: '{value} cr',
	                style: {
	                    color: Highcharts.getOptions().colors[1]
	                }
	            },
	            title: {
	            	text:'Cumulative',
	            	style:{
	                    fontSize: '15px'
	                },
	                verticalAlign:'bottom'
	            },
	            min:0
	        }],
	        tooltip: {
	            shared: true,
	            crosshairs: true
	        },
	        legend: {
	            layout: 'vertical',
	            align: 'left',
	            x: 100,
	            verticalAlign: 'top',
	            y: 0,
	            floating: true,
	            backgroundColor: (Highcharts.theme && Highcharts.theme.legendBackgroundColor) || '#FFFFFF'
	        },
	        series: [{
	            name: 'Targeted Collections',
	            type: 'line',
	            data: targetvsachieved.cumilativetarget,
	            tooltip: {
	                valueSuffix: 'cr'
	            },
	            color:Highcharts.getOptions().colors[1]
	        },
	        {
	            name: 'Actual Collections',
	            type: 'line',
	            yAxis: 0,
	            data: targetvsachieved.cumilativeachieved,
	            tooltip: {
	                valueSuffix: ' cr'
	            },
	            color:Highcharts.getOptions().colors[0]

	        },
	        {
	            name: 'Last Year Collections',
	            type: 'line',
	            yAxis: 0,
	            data: targetvsachieved.lastcumilativeachieved,
	            tooltip: {
	                valueSuffix: ' cr'
	            },
	            color:Highcharts.getOptions().colors[2]

	        }
	        ]
	    });
		 $("#page-top").unmask();
	});
}

overviewArea();
//#############################**OVERVIEW ENDS**#####################################//


//###########################** COLLECTIONS VS PAYMENT MODE STARTS**#######################//
function collectionsPaymentMode() {
	$("#page-top").mask('');
	if($("#collectionsPaymentGraph").highcharts()) {
		$("#collectionsPaymentGraph").highcharts().destroy();
	}
	setTitle("Collection Mode");
	$.ajax({url:"/ptis/dashboard/collections-paymentMode",
		cache:false
	}).done(function(collPaybardata) {
		$('#collectionsPaymentGraph').highcharts({
		   chart: {
		    		backgroundColor:null,
		            plotBackgroundColor: null,
		            plotBorderWidth: null,
		            plotShadow: false,
		            events: {
		            }
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
		            text: ''
		        },
		        tooltip: {
                    shared: true,
                    valueSuffix:' %'
				},
		        legend: {
		        	enabled:true
		        },
		        xAxis: {
                    type: 'category'
                },
                yAxis: {
                    title: {
                        text: 'Value in Rs'
                    }
                },
		        plotOptions: {
		        	series: {
                        borderWidth: 0,
                        minPointLength: 4,
                        dataLabels: {
                            enabled: false,
                            format: '{point.y}%',
                            color:'white'
                            	
                        },
                    }
		        },
		        series: [{
		            type: 'column',
		            name: 'Value in Rs. ',
		        	data: collPaybardata.overallColl
		        },{
		            type: 'column',
		            name: 'Number of Payments',
		            data: collPaybardata.totalTransPerc
		        }],
			});
		$("#page-top").unmask();
	});
}
//##########################** COLLECTIONS VS PAYMENT MODE ENDS **#######################//

//###########################** COVERAGE EFFICIENCY STARTS **#######################//

function coverageEfficiency() {
	$("#page-top").mask('');
	if($("#coverageEfficiencyGraph").highcharts()) {
		$("#coverageEfficiencyGraph").highcharts().destroy();
	}
	$("#coverageEfficiencyGraph").empty();
	setTitle("Zonal Coverage Eff.");
	$.ajax({url:"/ptis/dashboard/coverage-efficiency",
		cache:false
	}).done(function(coverageEfficiencydata) {
		$('#coverageEfficiencyGraph').highcharts({
		   chart: {
		    		backgroundColor:null,
		            plotBackgroundColor: null,
		            plotBorderWidth: null,
		            plotShadow: false,
		            events: {
		            }
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
		            text: ''
		        },
		        tooltip: {
                    shared: true,
                    crosshairs: true
				},
		        legend: {
		        	enabled:false
		        },
		        xAxis: {
                    type: 'category'
                },
                tooltip: {
                    headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
                    pointFormat: '<tr><td style="color:{series.color};padding:0">No. Of Tax Paid Properties: </td>' +
                        '<td style="padding:0"><b>{point.noOfTaxProps} </b></td></tr>'  +
                        '<tr><td style="color:{series.color};padding:0">Total No. Of Properties: </td>' +
                        '<td style="padding:0"><b>{point.noOfProps} </b></td></tr>' +
                        '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
                        '<td style="padding:0"><b>{point.y} </b></td></tr>',
                    footerFormat: '</table>',
                    shared: true,
                    useHTML: true
                },
		        plotOptions: {
		        	series: {
                        borderWidth: 0,
                        minPointLength: 4,
                        dataLabels: {
                            enabled: false,
                            format: '{point.y}',
                            color:'white'
                        },
                        stacking: 'normal',
                        cursor: 'pointer',
                      	point: {
                          events: {
                              click: function () {
                              	var zoneName = this.name;
                              	$("#coverageEfficiencyGraph").highcharts().destroy();
                              	coverageEfficiencyDrilldown(zoneName);
                              	
                              }
                          }
                       }
                    }
		        },
		        series: [{
		            type: 'column',
		            colorByPoint: true,
		            name: 'Coverage Efficiency',
		            data: coverageEfficiencydata.overallCoverage
		        }]
			});
		$("#page-top").unmask();
	});
}



function coverageEfficiencyDrilldown(zoneName) {
	$("#page-top").mask('');
	$("#coverageEfficiencyGraph").empty();
	if($("#coverageEfficiencyGraph").highcharts()) {
		$("#coverageEfficiencyGraph").highcharts().destroy();
	}
	setTitle("Ward Coverage Eff.");
	$.ajax({url:"/ptis/dashboard/coverage-efficiency-ward/"+zoneName,
		cache:false
	}).done(function(data) {
		$('#coverageEfficiencyGraph').highcharts({
			chart: {
	    		backgroundColor:null,
	            plotBackgroundColor: null,
	            plotBorderWidth: null,
	            plotShadow: false,
	            events: {
	            }
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
	            text: ''
	        },
	        tooltip: {
              shared: true,
              crosshairs: true
			},
	        legend: {
	        	enabled:true
	        },
	        xAxis: {
              type: 'category'
          },
          tooltip: {
              headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
              pointFormat: '<tr><td style="color:{series.color};padding:0">No. Of Tax Paid Properties: </td>' +
                  '<td style="padding:0"><b>{point.noOfTaxProps} </b></td></tr>'  +
                  '<tr><td style="color:{series.color};padding:0">Total No. Of Properties: </td>' +
                  '<td style="padding:0"><b>{point.noOfProps} </b></td></tr>' +
                  '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
                  '<td style="padding:0"><b>{point.y} </b></td></tr>',
              footerFormat: '</table>',
              shared: true,
              useHTML: true
          },
	        plotOptions: {
	        	series: {
                  borderWidth: 0,
                  minPointLength: 4,
                  dataLabels: {
                      enabled: false,
                      format: '{point.y}',
                      color:'white'
                  }
	        	}
	        },
		    exporting: {
		    	buttons: {
		    		contextButton: {
		    			enabled: false
		    		},
		    		customButton: {
		    			text: '<< Back',
		    			verticalAlign:'middle',
		    			align:'left',
		    			symbolFill: '#A8BF77',
		    			onclick: function() {
		    				coverageEfficiency();
		    			}
		    		}
		    	}
		    },
		    series: [{
		    	type: 'column',
		    	colorByPoint: true,
		        name: 'Coverage Efficiency',
	            data: data.overallCoverage
	        }]
	    });
		$("#page-top").unmask();
	});	
}

//###########################** COVERAGE EFFICIENCY ENDS **#######################//

//###########################** ZONE ANALYSIS STARTS **#######################//
function zonewiseAnalysis() {
	setTitle("Zone Analysis");
	google.maps.visualRefresh = true;    
	//Demand per property
	zonedemandPerProperty();
	//Collection per Property
    zonecollectionPerProperty();
	//Balance per Property
    zonebalancePerProperty();
	//No of properties per sq km
    zonenoOfPropPerSqKm();
    //Coverage Efficiency
    zoneCoverageEfficiency();
    //Revenue Per Capita
    //zonerevenuePerCapita();
    //collection Efficiency
    zoneCollectionEfficiency();
}

function zonenoOfPropPerSqKm() {

    var mapDiv = document.getElementById('zonewiseAnalysis4');
    //mapDiv.style.width = isMobile ? '100%' : '500px';
    //mapDiv.style.height = '500px';
    var map = new google.maps.Map(mapDiv, {
      center: new google.maps.LatLng(13.0589382, 80.209274),
      zoom: 11,
      mapTypeId: google.maps.MapTypeId.TERRAIN,
      //disableDefaultUI: true,
      scrollwheel: true,
      draggable: true,
      //styles: mapStyle
    });

    layer = new google.maps.FusionTablesLayer({
      map: map,
      heatmap: { enabled: false },
      query: {
        select: "col8\x3e\x3e0",
        from: "1EzHiEjvyJiluQXCTDK5_CAsfKmo214o3BWj_SiDx",
        where: ""
      },
      options: {
        styleId: 2,
        templateId: 2
      }
    });
}


function zonebalancePerProperty() {
	var mapDiv = document.getElementById('zonewiseAnalysis3');
    //mapDiv.style.width = isMobile ? '100%' : '500px';
    //mapDiv.style.height = '500px';
    var map = new google.maps.Map(mapDiv, {
      center: new google.maps.LatLng(13.0589382, 80.209274),
      zoom: 11,
      mapTypeId: google.maps.MapTypeId.TERRAIN,
      //disableDefaultUI: true,
      scrollwheel: true,
      draggable: true,
      //styles: mapStyle
    });

    layer = new google.maps.FusionTablesLayer({
      map: map,
      heatmap: { enabled: false },
      query: {
        select: "col8\x3e\x3e0",
        from: "1QL9ortCLmiH4U3BLurKvojjSVzVigvrDunLfu5Yb",
        where: ""
      },
      options: {
        styleId: 2,
        templateId: 2
      }
    });
}

function zoneCoverageEfficiency() {
	  var mapDiv = document.getElementById('zonewiseAnalysis5');
	    //mapDiv.style.height = '500px';
	    var map = new google.maps.Map(mapDiv, {
	    	center: new google.maps.LatLng(13.0589382, 80.209274),
	    	 zoom: 11,
	         mapTypeId: google.maps.MapTypeId.TERRAIN,
	         //disableDefaultUI: true,
	         scrollwheel: true,
	         draggable: true,
	         //styles: mapStyle
	    });

	    layer = new google.maps.FusionTablesLayer({
	      map: map,
	      heatmap: { enabled: false },
	      query: {
	        select: "col8\x3e\x3e0",
	        from: "1I7eF-7GgtM8WU939Pv_bSwi-JPW9F1DpSZNyFtl_",
	        where: ""
	      },
	      options: {
	        styleId: 2,
	        templateId: 2
	      }
	    });
}

function zonedemandPerProperty() {
	 var mapDiv = document.getElementById('zonewiseAnalysis1');
	    //mapDiv.style.height = '500px';
	    var map = new google.maps.Map(mapDiv, {
	    	center: new google.maps.LatLng(13.0589382, 80.209274),
	    	 zoom: 11,
	         mapTypeId: google.maps.MapTypeId.TERRAIN,
	         //disableDefaultUI: true,
	         scrollwheel: true,
	         draggable: true,
	         //styles: mapStyle
	    });

	    layer = new google.maps.FusionTablesLayer({
	      map: map,
	      heatmap: { enabled: false },
	      query: {
	        select: "col8\x3e\x3e0",
	        from: "1-p9aBZ2jqYrHd6N06QK-OkjY-OKBSmK_YAONSqPU",
	        where: ""
	      },
	      options: {
	        styleId: 2,
	        templateId: 2
	      }
	    });

}

function zonecollectionPerProperty() {
	 var mapDiv = document.getElementById('zonewiseAnalysis2');
	 var map = new google.maps.Map(mapDiv, {
	    	center: new google.maps.LatLng(13.0589382, 80.209274),
	    	 zoom: 11,
	         mapTypeId: google.maps.MapTypeId.TERRAIN,
	         //disableDefaultUI: true,
	         scrollwheel: true,
	         draggable: true,
	         //styles: mapStyle
	    });

	    layer = new google.maps.FusionTablesLayer({
	      map: map,
	      heatmap: { enabled: false },
	      query: {
	        select: "col8\x3e\x3e0",
	        from: "1l7EzoivO4J0YQ0tC-nFgGD7lJwwZMw7C6N51fjIi",
	        where: ""
	      },
	      options: {
	        styleId: 2,
	        templateId: 2
	      }
	    });
}

/*function zonerevenuePerCapita() {
	 var mapDiv = document.getElementById('zonewiseAnalysis6');
	 var map = new google.maps.Map(mapDiv, {
	    	center: new google.maps.LatLng(13.0589382, 80.209274),
	    	 zoom: 11,
	         mapTypeId: google.maps.MapTypeId.TERRAIN,
	         //disableDefaultUI: true,
	         scrollwheel: true,
	         draggable: true,
	         //styles: mapStyle
	    });
	    layer = new google.maps.FusionTablesLayer({
	      map: map,
	      heatmap: { enabled: false },
	      query: {
	        select: "col8\x3e\x3e0",
	        from: "1NkrRAVHrlm8k4ZfZlL4d2tTNXRK1bvT87d69chCQ",

	        where: ""
	      },
	      options: {
	        styleId: 2,
	        templateId: 2
	      }
	    });
}
*/

function zoneCollectionEfficiency() {
	 var mapDiv = document.getElementById('zonewiseAnalysis6');
	 var map = new google.maps.Map(mapDiv, {
	    	center: new google.maps.LatLng(13.0589382, 80.209274),
	    	 zoom: 11,
	         mapTypeId: google.maps.MapTypeId.TERRAIN,
	         //disableDefaultUI: true,
	         scrollwheel: true,
	         draggable: true,
	         //styles: mapStyle
	    });
	    layer = new google.maps.FusionTablesLayer({
	      map: map,
	      heatmap: { enabled: false },
	      query: {
	        select: "col8\x3e\x3e0",
	        from: "1SCEywYLBXxTRqaZLJIqiTISfMnygJio6rMqd6E9s",

	        where: ""
	      },
	      options: {
	        styleId: 2,
	        templateId: 2
	      }
	    });
}


//###########################** ZONE ANALYSIS ENDS **#######################//

//###########################** WARD ANALYSIS STARTS **#######################//
function wardwiseAnalysis() {
	setTitle("Ward Analysis");
	google.maps.visualRefresh = true;    
	//Demand per property
	demandPerProperty();
	//Collection per Property
    collectionPerProperty();
	//Balance per Property
    balancePerProperty();
	//No of properties per sq km
    noOfPropPerSqKm();
    //Citizen per Property
   // citizenPerProperty();
    //Revenue Per Capita
  //  revenuePerCapita();
    //Ward coverage efficiency
    wardCoverageEfficiency();
    //Collection percentage
    collectionPerc();
    
}

function noOfPropPerSqKm() {

    var mapDiv = document.getElementById('wardwiseAnalysis4');
    //mapDiv.style.width = isMobile ? '100%' : '500px';
    //mapDiv.style.height = '500px';
    var map = new google.maps.Map(mapDiv, {
      center: new google.maps.LatLng(13.0589382, 80.209274),
      zoom: 11,
      mapTypeId: google.maps.MapTypeId.TERRAIN,
      //disableDefaultUI: true,
      scrollwheel: true,
      draggable: true,
      //styles: mapStyle
    });

    layer = new google.maps.FusionTablesLayer({
      map: map,
      heatmap: { enabled: false },
      query: {
        select: "col10\x3e\x3e0",
        from: "1mYZCiqrwcza5JnrLH-caLdiXcADaW_TLiSEEY2C1",
        where: ""
      },
      options: {
        styleId: 2,
        templateId: 2
      }
    });
}


function balancePerProperty() {
	var mapDiv = document.getElementById('wardwiseAnalysis3');
    //mapDiv.style.width = isMobile ? '100%' : '500px';
    //mapDiv.style.height = '500px';
    var map = new google.maps.Map(mapDiv, {
      center: new google.maps.LatLng(13.0589382, 80.209274),
      zoom: 11,
      mapTypeId: google.maps.MapTypeId.TERRAIN,
      //disableDefaultUI: true,
      scrollwheel: true,
      draggable: true,
      //styles: mapStyle
    });

    layer = new google.maps.FusionTablesLayer({
      map: map,
      heatmap: { enabled: false },
      query: {
        select: "col10\x3e\x3e0",
        from: "1UVdf7OeZoj5YOxerBInKyCu0dh3KzRzjZtS9BnXC",
        where: ""
      },
      options: {
        styleId: 2,
        templateId: 2
      }
    });
}

/*function citizenPerProperty() {
	  var mapDiv = document.getElementById('wardwiseAnalysis5');
	    //mapDiv.style.height = '500px';
	    var map = new google.maps.Map(mapDiv, {
	    	center: new google.maps.LatLng(13.0589382, 80.209274),
	    	 zoom: 11,
	         mapTypeId: google.maps.MapTypeId.TERRAIN,
	         //disableDefaultUI: true,
	         scrollwheel: true,
	         draggable: true,
	         //styles: mapStyle
	    });

	    layer = new google.maps.FusionTablesLayer({
	      map: map,
	      heatmap: { enabled: false },
	      query: {
	        select: "col10\x3e\x3e0",
	        from: "15mO2XVUMjmAL9scded6yW4Ar8Naiu-r54ay91P7R",
	        where: ""
	      },
	      options: {
	        styleId: 2,
	        templateId: 2
	      }
	    });
}*/

function wardCoverageEfficiency() {
	  var mapDiv = document.getElementById('wardwiseAnalysis5');
	    //mapDiv.style.height = '500px';
	    var map = new google.maps.Map(mapDiv, {
	    	center: new google.maps.LatLng(13.0589382, 80.209274),
	    	 zoom: 11,
	         mapTypeId: google.maps.MapTypeId.TERRAIN,
	         //disableDefaultUI: true,
	         scrollwheel: true,
	         draggable: true,
	         //styles: mapStyle
	    });

	    layer = new google.maps.FusionTablesLayer({
	      map: map,
	      heatmap: { enabled: false },
	      query: {
	        select: "col10\x3e\x3e0",
	        from: "1P5FeRLZ81v4MpsNXu0s5bZS5a5skZ2CzwBaThJH0",
	        where: ""
	      },
	      options: {
	        styleId: 2,
	        templateId: 2
	      }
	    });
}

function demandPerProperty() {
	 var mapDiv = document.getElementById('wardwiseAnalysis1');
	    //mapDiv.style.height = '500px';
	    var map = new google.maps.Map(mapDiv, {
	    	center: new google.maps.LatLng(13.0589382, 80.209274),
	    	 zoom: 11,
	         mapTypeId: google.maps.MapTypeId.TERRAIN,
	         //disableDefaultUI: true,
	         scrollwheel: true,
	         draggable: true,
	         //styles: mapStyle
	    });

	    layer = new google.maps.FusionTablesLayer({
	      map: map,
	      heatmap: { enabled: false },
	      query: {
	        select: "col10\x3e\x3e0",
	        from: "1YwM47-QPgceFfcisnHOsDep112s7p2T7wsb50WA7",
	        where: ""
	      },
	      options: {
	        styleId: 2,
	        templateId: 2
	      }
	    });

}

function collectionPerProperty() {
	 var mapDiv = document.getElementById('wardwiseAnalysis2');
	 var map = new google.maps.Map(mapDiv, {
	    	center: new google.maps.LatLng(13.0589382, 80.209274),
	    	 zoom: 11,
	         mapTypeId: google.maps.MapTypeId.TERRAIN,
	         //disableDefaultUI: true,
	         scrollwheel: true,
	         draggable: true,
	         //styles: mapStyle
	    });

	    layer = new google.maps.FusionTablesLayer({
	      map: map,
	      heatmap: { enabled: false },
	      query: {
	        select: "col10\x3e\x3e0",
	        from: "1vK4a-3G1nXFGFjDBlDfoRvWDN4G7HZr3cfja1lh6",
	        where: ""
	      },
	      options: {
	        styleId: 2,
	        templateId: 2
	      }
	    });
}

/*function revenuePerCapita() {
	 var mapDiv = document.getElementById('wardwiseAnalysis6');
	 var map = new google.maps.Map(mapDiv, {
	    	center: new google.maps.LatLng(13.0589382, 80.209274),
	    	 zoom: 11,
	         mapTypeId: google.maps.MapTypeId.TERRAIN,
	         //disableDefaultUI: true,
	         scrollwheel: true,
	         draggable: true,
	         //styles: mapStyle
	    });
	    layer = new google.maps.FusionTablesLayer({
	      map: map,
	      heatmap: { enabled: false },
	      query: {
	        select: "col10\x3e\x3e0",
	        from: "1AmgwcbeM8ZAkiiioZB9hZWmeMjH8XUSl4ZPwRsIN",

	        where: ""
	      },
	      options: {
	        styleId: 2,
	        templateId: 2
	      }
	    });
}*/

function collectionPerc() {
	 var mapDiv = document.getElementById('wardwiseAnalysis6');
	 var map = new google.maps.Map(mapDiv, {
	    	center: new google.maps.LatLng(13.0589382, 80.209274),
	    	 zoom: 11,
	         mapTypeId: google.maps.MapTypeId.TERRAIN,
	         //disableDefaultUI: true,
	         scrollwheel: true,
	         draggable: true,
	         //styles: mapStyle
	    });
	    layer = new google.maps.FusionTablesLayer({
	      map: map,
	      heatmap: { enabled: false },
	      query: {
	        select: "col10\x3e\x3e0",
	        from: "1BstSRxzFRteTh-kPnkWZmqI6XpDUo91W_Pmeb3XT",

	        where: ""
	      },
	      options: {
	        styleId: 2,
	        templateId: 2
	      }
	    });
}

//###########################** WARD ANALYSIS ENDS **#######################//