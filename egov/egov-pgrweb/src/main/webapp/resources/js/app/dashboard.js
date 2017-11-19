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
var lastbtnId = isdefault ? "pgrPerf" : "topFiveCompTypeview"; 
var lastWinId = isdefault ? "performanceWin" : "topFiveCompTypeWin";
if(isdefault) {
	$("#deflt").addClass("active");
} else {
	$("#top5").addClass("active");
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

var sync = true;
$('.label-toggle-switch').on('switch-change', function (e, data) {
    sync = data.value;
});
function resetToggleBtns(win) {
	if(win == "performanceWin") {
		$(".performBtn").removeClass("btn-primary");
		$(".performBtn").addClass("btn-default");
		perfLastTglBtn = $('#barbtn');
		$("#barbtn").addClass("btn-primary");
	} else {
		$(".slaBtn").removeClass("btn-primary");
		$(".slaBtn").addClass("btn-default");
		lastSlaPiebtn = $('#slaPiebtn');
		$("#slaPiebtn").addClass("btn-primary");
	}
}

//#############################**PERFORMANCE STARTS**#####################################//

var perfLastTglBtn = $('#barbtn');
$(".performBtn").on("click", function(){
	toggleBtnHandler($(this),perfLastTglBtn);
	perfLastTglBtn = $(this);
});

var today = Date.today();
var twoWeeksBack = Date.today().add({ days: -14 });
var twoWeeksOneDayBack = Date.today().add({ days: -13 });
var twoWeeksBackFmt = twoWeeksBack.getDate()+"/"+Date.CultureInfo.abbreviatedMonthNames[twoWeeksBack.getMonth()]+"/"+twoWeeksBack.getFullYear();
var twoWeeksAfter1DayFmt = twoWeeksOneDayBack.getDate()+"/"+Date.CultureInfo.abbreviatedMonthNames[twoWeeksOneDayBack.getMonth()]+"/"+twoWeeksOneDayBack.getFullYear();
var todayFmt = today.getDate()+"/"+Date.CultureInfo.abbreviatedMonthNames[today.getMonth()]+"/"+today.getFullYear();

function redressalEfficiency() {
	$("#page-top").mask('');
	if($("#performanceGraph").highcharts()) {
		$("#performanceGraph").highcharts().destroy();
	}
	setTitle("Redressal Efficiency");
	performanceGIS();
}

function openCompPendency() {
	$("#page-top").mask('');
	if($("#slaGraph").highcharts()) {
		$("#slaGraph").highcharts().destroy();
	}
	setTitle("Complaint Pendency");
	slaPie();
	slaGIS();
}

//===============================PERFORMANCE GIS=======================================//
function performanceGIS() {
	
	 var gisOption = {
			zoom: 11,
			mapTypeId : google.maps.MapTypeId.TERRAIN, 
			streetViewControl: true,
			center: new google.maps.LatLng(citylat, citylng),
			panControl: true,
			zoomControl: true,
			//disableDefaultUI: true,
	        scrollwheel: true,
	        draggable: true,
	        //styles: mapStyle,
			mapTypeControlOptions: {
				style: google.maps.MapTypeControlStyle.DROPDOWN_MENU
			},
			zoomControlOptions: {
				style: google.maps.ZoomControlStyle.SMALL
			}
	 };
	
	var map = new google.maps.Map(document.getElementById("performanceGIS"), gisOption);
	var infoWin = new google.maps.InfoWindow();
	var chartWin = new google.maps.InfoWindow();
	$.ajax({url:"wardwise-performance",
		cache:false
	}).done(function(datas) {
		var length = datas[0].length;
		performanceTabular(datas[1]);
		performanceBar(datas[2]);
		$.each(datas[0], function(index,data) {
			setMarkers(data, length, index,map,infoWin,chartWin);
		});
		$("#page-top").unmask();
	});
	
	var setMarkers = function(data, length, index,map,infoWin,chartWin) {
		var contentDiv = '<div id="performancechart" style="height:350px;width:500px;white-space: nowrap;"></div>';
		var marker_image;
		var infoClass = "panel-red";
		if (index < 3) {
			marker_image = 'https://maps.google.com/mapfiles/ms/micons/green-dot.png';
			infoClass = "panel-green";
		} else if(index < length-3) {
	        marker_image = 'https://maps.google.com/mapfiles/ms/micons/yellow-dot.png';
	        infoClass = "panel-yellow";
	    } else {
	    	marker_image = 'https://maps.google.com/mapfiles/ms/micons/red-dot.png';
	    }
		var marker = new google.maps.Marker({
	        position: new google.maps.LatLng(data.lat,data.lng),
			color:"0000aa",
	        map: map,
	        animation: google.maps.Animation.DROP,
	        icon: marker_image,
	        title: data.zone

	      });
		google.maps.event.addListener(marker, 'click', (function(marker, index) {
	          return function() {
	        	  marker.setAnimation(google.maps.Animation.BOUNCE);
	        	  infoWin.setContent('<div class="panel '+infoClass+'"><div class="panel-heading" style="font-weight:bold">Ward : '+
	        			  data.zone+'</div><div class="panel-body" style="white-space:nowrap;color:#000;text-align:left;line-height:2;">'+
	        			  'Pending as on '+data.dateAsOn2WeekBack+':<b> '+data.noOfCompAsOnDate+'</b><br/>'+
	        			  'Received between '+data.dateAsOnDayAfter+'-'+data.dateAsOn+' :<b> '+data.noOfCompReceivedBtw+'</b><br/>'+
	        			  'Pending as on '+data.dateAsOn+' :<b> '+data.noOfCompPenAsonDate+'</b><br/>'+
	        			  'Disposal Percentage : <b>'+data.disposalPerc+'% </b><br/>'+
	        			  'Rank : <b>'+data.rank+'</b><br/>'+
	        			  '<!--<a href="#" id="performpgraph'+data.zoneId+'"><i class="fa fa-line-chart fa-fw"></i>Track Performance</a><br/>'+
	        			  '<a href="#" id="openbrkup'+data.zoneId+'"><i class="fa fa-pie-chart fa-fw"></i>Open Breakup</a><br/>'+
	        			  '<a href="#" id="regbrkup'+data.zoneId+'"><i class="fa fa-pie-chart fa-fw"></i>Filed Breakup</a>-->'+
	        			  '</div><div style="padding:5px;background-color:silver;color:white;font-size:11px;font-weight:bold">Till '+
	        			  data.dateAsOn+'</div></div>');
	        	  infoWin.open(map, marker);
	        	  stopAnimation(marker);
	          };
	    })(marker, index));
		var id = '#performpgraph'+data.zoneId;
	   $( "#performanceGIS").on( "click",id, function() {
		    infoWin.close();
      		chartWin.open(map, marker);
      		chartWin.setContent(contentDiv);
      		performanceGISTracker(data.zoneId,chartWin,infoWin,map,marker);
		});
	   var openbrkupid = '#openbrkup'+data.zoneId;
	   $( "#performanceGIS").on( "click",openbrkupid, function() {
		    infoWin.close();
      		chartWin.open(map, marker);
      		chartWin.setContent(contentDiv);
      		performanceGISOpenBreakup(data.zoneId,chartWin,infoWin,map,marker);
      	});
	   
	   var regbrkupid = '#regbrkup'+data.zoneId;
	   $( "#performanceGIS").on( "click",regbrkupid, function() {
		    infoWin.close();
      		chartWin.open(map, marker);
      		chartWin.setContent(contentDiv);
      		performanceGISRegBreakup(data.zoneId,chartWin,infoWin,map,marker);
		});
	};
}
//===============================PERFORMANCE GIS=======================================//



//===============================PERFORMANCE TABULAR=======================================//

function performanceTabular(datas) {
	$("#performanceTable").empty();
	$('#performanceTable').html( '<table width="100%" style="display:none" class="display responsive no-wrap table table-striped table-bordered table-hover dataTable no-footer" id="tabularPerformance" aria-describedby="dataTables-example_info">' );
	 $('#tabularPerformance').dataTable( {
    	//processing: false,
        //serverSide: false,
        sort:false,
        filter:false,
        responsive:true,
        paginate:false,
        data: datas.data,
        columns: [
            { "title": "Ward","data":"zone" },
            { "title": "Pending as on "+twoWeeksBackFmt,"data":"noOfCompAsOnDate" },
            { "title": "Registered between "+twoWeeksAfter1DayFmt+" - "+todayFmt,"data":"noOfCompReceivedBtw" },
            { "title": "Pending as on "+todayFmt,"data":"noOfCompPenAsonDate",render: function (oObj){
            	return oObj+"<span class=\"btn btn-primary btn-xs btn-circle-xs pull-left\" onclick=\"breakupTabularDrill(this)\"><i id='btn' class='fa fa-plus-circle'></i></span>";} 
            },
            { "title": "Disposal %","data":"disposalPerc" },
            { "title": "Rank","data":"rank" }
        ],
        fnDrawCallback : function() {
		 $("#tabularPerformance").show();
	 	}
    } );
	
}

var anOpen = [];
(function(H){
    H.wrap(H.Legend.prototype, 'positionItem', function(proceed, item){
        proceed.call(this, item);
        if(item.legendSymbol) {
            item.legendSymbol.translate(0, 2);
        }
        if(item.legendLine){
            item.legendLine.translate(0, 2);
        }
    });
    
})(Highcharts);
function breakupTabularDrill(link) {
	var tr = $(link).closest('tr');
	var td = $(link).closest('td');
	var clsBtn = $(link).children("i:first");
	$(link).next().show();
	var zoneName = tr.children('td:first').text();
	var zoneNo = zoneName.replace(/\s+/g, '');
	var i = $.inArray(zoneNo, anOpen );
	if ( i === -1 ) {
		clsBtn.removeClass("fa-plus-circle");
		clsBtn.addClass("fa-minus-circle");
		$("<div id='"+zoneNo+"tabularDrillGraph"+"' style='height:200px;width:200px;margin:0 auto;'></div>").insertAfter($(link));
		td.mask('');
		$.ajax({url:"ageing/"+zoneName,
			cache:false
		}).done(function(data) {
			$('#'+zoneNo+'tabularDrillGraph').highcharts({
				chart: {
					backgroundColor:null,
			        plotBackgroundColor: null,
			        plotBorderWidth: null,
			        plotShadow: false
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
			    	symbolHeight:5,
			        symbolWidth:5,
			        itemWidth: 80,
			        itemStyle: {
			            font: 'xx-small Verdana, sans-serif'
			        }
			    },
			    plotOptions: {
			        pie: {
			        	showInLegend: true,
			            allowPointSelect: true,
			            slicedOffset: 0,
			           // cursor: 'pointer',
			            dataLabels: {
			                enabled: true,
			                distance: -30,
							color:'white',
							style:{'fontSize':'smaller', 'textShadow':'none'},
			                formatter: function() {
			                	if (this.y != 0) {
			                        return this.y;
			                    } else {
			                        return null;
			                    }
			                }
			            }
			        },series: {
		                states: {
		                    hover: {
		                        enabled: false
		                    }
		                }
		            }
			    },
			    series: [{
		            type: 'pie',
		            name: 'Count Share',
		            data: data
		        }]
		    });
			td.unmask();
		});
	    anOpen.push(zoneNo);
	} else {
		anOpen.splice( i, 1);
		if($('#'+zoneNo+'tabularDrillGraph').highcharts())
		$('#'+zoneNo+'tabularDrillGraph').highcharts().destroy();
		$('#'+zoneNo+'tabularDrillGraph').remove();
		clsBtn.addClass("fa-plus-circle");
		clsBtn.removeClass("fa-minus-circle");
	}
}

//===============================PERFORMANCE TABULAR=======================================//



//===============================PERFORMANCE HIGHCHART=====================================//
var currentDate = Date.today();
var threeMonthsBack = Date.today().add({ months: -3 });
function performanceGISTracker(zoneId,chartWin,infoWin,map,marker) {
	$.ajax({url:"performanceGISTracker.do?zoneID="+zoneId,
		cache:false
	}).done(function(barData) {
	    $('#performancechart').highcharts({
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
	        	buttons: {
	                contextButton: {
	                    enabled: false
	                },
	                customButton: {
	                    text: '<< Back',
	                    symbolFill: '#A8BF77',
	                    align:'left',
	                    onclick: function() {
	                    	chartWin.close();
	                        infoWin.open(map,marker);
	                    }
	                }
	        	}
	        },
	        credits: {
	            enabled: false
	        },
	        title: {
	            text: 'Grievance History(Weekly)',
	            style:{
	                   fontSize: '15px'
                } 
	        },
	        subtitle:{
	        	text:"Last 3 Months"
	        },
	        tooltip: {
	        	shared:true
			},
	        legend: {
	        	enabled:true
	        },
	        xAxis: {
                type:'datetime',
                dateTimeLabelFormats: {
                    day: '%b %e',
                    week: '%b %e'
                }
            },
            yAxis: {
                title: {
                    text: 'No. of Complaints'
                }
            },
	        plotOptions: {
	        	series: {
                    borderWidth: 0,
                    minPointLength: 5,
                    dataLabels: {
                        enabled: false
                    }
                }
	        },
	        series: [{
	            type: 'column',
	            name: 'Filed',
	            data: barData.filedCnt,
	            pointStart: Date.UTC(threeMonthsBack.getFullYear(), threeMonthsBack.getMonth(), threeMonthsBack.getDate()),
                pointInterval: 7 * 24 * 3600 * 1000
	        },{
	            type: 'column',
	            name: 'Open',
	            data: barData.opnCnt,
	            pointStart: Date.UTC(threeMonthsBack.getFullYear(), threeMonthsBack.getMonth(), threeMonthsBack.getDate()),
                pointInterval: 7 * 24 * 3600 * 1000
	        },{
	            type: 'column',
	            name: 'Closed',
	            data: barData.closedCnt,
	            pointStart: Date.UTC(threeMonthsBack.getFullYear(), threeMonthsBack.getMonth(), threeMonthsBack.getDate()),
                pointInterval: 7 * 24 * 3600 * 1000
		    }]
	    });
	});
}	

function performanceGISOpenBreakup(zoneId,chartWin,infoWin,map,marker) {
	$.ajax({url:"performanceGISOpenBreakup.do?zoneID="+zoneId,
		cache:false
	}).done(function(openbrkupPiedata) {
		 $('#performancechart').highcharts({
		        chart: {
		            plotBackgroundColor: null,
		            plotBorderWidth: 0,
		            plotShadow: false
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
		        exporting: {
		        	buttons: {
		                contextButton: {
		                    enabled: false
		                },
		                customButton: {
		                    text: '<< Back',
		                    symbolFill: '#A8BF77',
		                    align:'left',
		                    onclick: function() {
		                    	chartWin.close();
		                        infoWin.open(map,marker);
		                    }
		                }
		        	}
		        },
		        credits : {
				  enabled : false
				},
		        title: {
		            text: 'Open Complaint Typewise',
		            style:{
		                   fontSize: '15px'
		                }  
		        },
		        subtitle:{
		        	text:"Last 3 Months"
		        },
		        tooltip: {
		            pointFormat: 'Complaints :{point.y} Nos'
		        },
		        legend: {
		        	layout: 'vertical',
					align: 'right',
					verticalAlign: 'middle',
					borderWidth: 0,
					margin:0,
					symbolHeight:5,
			        symbolWidth:5,
			        itemStyle: {
			            font: 'smaller Verdana, sans-serif'
			        }
		        },
		        plotOptions: {
		            pie: {
		            	showInLegend: true,
		                allowPointSelect: true,
		                slicedOffset: 0,
		                //cursor: 'pointer',
		                dataLabels: {
		                    enabled: false,
		                   // format: '<b>{point.name}</b>: {point.percentage:.1f} %',
		                    style: {
		                        color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black'
		                    }
		                }
		            },series: {
		                states: {
		                    hover: {
		                        enabled: false
		                    }
		                }
		            }
		        },
		        series: [{
		        	type: 'pie',
		            name: 'Complaint Groups',
		            data: openbrkupPiedata
		        }]
		    });
	});
}

function performanceGISRegBreakup(zoneId,chartWin,infoWin,map,marker) {
	$.ajax({url:"performanceGISRegBreakup.do?zoneID="+zoneId,
		cache:false
	}).done(function(regdbrkupPiedata) {
		 $('#performancechart').highcharts({
		        chart: {
		            plotBackgroundColor: null,
		            plotBorderWidth: 0,
		            plotShadow: false
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
		        exporting: { 
		        	buttons: {
		                contextButton: {
		                    enabled: false
		                },
		                customButton: {
		                    text: '<< Back',
		                    symbolFill: '#A8BF77',
		                    align:'left',
		                    onclick: function() {
		                    	chartWin.close();
		                        infoWin.open(map,marker);
		                    }
		                }
		        	}
		        },
		        credits : {
				  enabled : false
				},
		        title: {
		            text: 'Filed Complaint Typewise',
		            style:{
		                 fontSize: '15px'
		            }  
		        },
		        subtitle:{
		        	text:"Last 3 Months"
		        },
		        tooltip: {
		            pointFormat: 'Complaints :{point.y} Nos'
		        },
		        legend: {
		        	layout: 'vertical',
					align: 'right',
					verticalAlign: 'middle',
					borderWidth: 0,
					margin:0,
					symbolHeight:5,
			        symbolWidth:5,
			        itemStyle: {
			            font: 'smaller Verdana, sans-serif'
			        }
				},
		        plotOptions: {
		            pie: {
		            	showInLegend: true,
		                allowPointSelect: true,
		                slicedOffset: 0,
		                //cursor: 'pointer',
		                dataLabels: {
		                    enabled: false,
		                   // format: '<b>{point.name}</b>: {point.percentage:.1f} %',
		                    style: {
		                        color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black'
		                    }
		                }
		            },series: {
		                states: {
		                    hover: {
		                        enabled: false
		                    }
		                }
		            }
		        },
		        series: [{
		        	type: 'pie',
		            name: 'Complaint Groups',
		            data: regdbrkupPiedata
		        }]
		    });
	});
}


/*
 * Performance Bar Wardwise drilldown, disabled since no zone
 * 
 * function performanceBarDrilldown(zoneName) {
	$("#performanceGraph").parent().mask('');
	$("#performanceGraph").empty();
	if($("#performanceGraph").highcharts()) {
		$("#performanceGraph").highcharts().destroy();
	}
	$.ajax({url:"tabularDrill.do?zn="+zoneName,
		cache:false
	}).done(function(data) {
		$('#performanceGraph').highcharts({
			chart: {
				backgroundColor:null,
		        plotBackgroundColor: null,
		        plotBorderWidth: null,
		        plotShadow: false
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
		    				performanceBar();
		    			}
		    		}
		    	}
		    },
		    credits : {enabled : false},
		    title:{
				text:zoneName
		        },
		    tooltip:{
				enabled:false
		    },
		    legend :{
		    	enabled:true
		    },
		    plotOptions: {
		        pie: {
		        	showInLegend: true,
		            allowPointSelect: true,
		            //cursor: 'pointer',
		            slicedOffset: 0,
		            dataLabels: {
		                enabled: true,
		                distance: -18,
						color:'white',
						style:{'fontSize':'small'},
		                formatter: function() {
		                	if (this.y != 0) {
		                        return this.y;
		                    } else {
		                        return null;
		                    }
		                }
		            }
		        },series: {
	                states: {
	                    hover: {
	                        enabled: false
	                    }
	                }
	            }
		    },
		    series: [{
	            type: 'pie',
	            name: 'Count Share',
	            data: data
	        }]
	    });
		$("#performanceGraph").parent().unmask();
	});	
}*/

function performanceBar(data) {
	var drawPerformanceBar = function (bardata) {
		$('#performanceGraph').highcharts({
		    chart: {
		    		backgroundColor:null,
		            plotBackgroundColor: null,
		            plotBorderWidth: null,
		            plotShadow: false
		        },
		        colors: Highcharts.map(rankColor, function(color) {
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
		        	valueSuffix:'%'
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
                        text: 'Disposal %age'
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
                        stacking: 'normal',
                        cursor: 'pointer'/*
                        commenting since no drilldown from ward
                        ,
                        point: {
                            events: {
                                click: function () {
                                	var zoneName = this.name;
                                	$("#performanceGraph").highcharts().destroy();
                                	performanceBarDrilldown(zoneName);
                                	
                                }
                            }
                        }*/
                    }
		        },
		        series: [{
		            type: 'column',
		            colorByPoint: true,
		            name: 'Redressal Efficiency',
		            data: bardata
		        }]
		    });
	};
	
	if (data) {
		drawPerformanceBar(data);
	} else {
		$.ajax({url:"wardwise-performance?ctype=bar",
			cache:false
		}).done(function(bardata) {
			drawPerformanceBar(bardata);
			$("#page-top").unmask();
			
		});
	}
}



//===============================PERFORMANCE HIGHCHART=====================================//

//#############################**PERFORMANCE ENDS**#####################################//


//#############################**SLA STARTS**#####################################//
var lastSlaPiebtn = $('#slaPiebtn');
$(".slaBtn").on("click", function(){
	toggleBtnHandler($(this),lastSlaPiebtn);
	lastSlaPiebtn = $(this);
});

function slaPie() {
	$.ajax({url:"sla/pie",
		cache:false
	}).done(function(piedata) {
		$('#slaGraph').highcharts({
		    chart: {
		        plotBackgroundColor: null,
		        plotBorderWidth: 0,
		        plotShadow: false,
		        backgroundColor:null
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
				enabled:true
		    },
		    legend :{
		    	enabled:true
		    },
		    plotOptions: {
		        pie: {
		        	showInLegend: true,
		            allowPointSelect: true,
		            //cursor: 'pointer',
		            slicedOffset: 0,
		            dataLabels: {
		                enabled: true,
		                distance: -80,
						color:'white',
						style:{'fontSize':'smaller', 'textShadow':'none'},
		                formatter: function() {
		                    return this.y;
		                }
		            }
		        },series: {
	                states: {
	                    hover: {
	                        enabled: false
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
}

function slaGIS() {
	var gisOption = {
			zoom: 11,
			mapTypeId : google.maps.MapTypeId.TERRAIN, 
			streetViewControl: true,
			center: new google.maps.LatLng(citylat, citylng),
			panControl: true,
			zoomControl: true,
			//disableDefaultUI: true,
	        scrollwheel: true,
	        draggable: true,
	        //styles: mapStyle,
			mapTypeControlOptions: {
				style: google.maps.MapTypeControlStyle.DROPDOWN_MENU
			},
			zoomControlOptions: {
				style: google.maps.ZoomControlStyle.SMALL
			}
	 };
	var map = new google.maps.Map(document.getElementById("slaGIS"), gisOption);
	var infoWin = new google.maps.InfoWindow();
	var chartWin = new google.maps.InfoWindow();
	var contentDiv = '<div id="slaPerformance" style="height:350px;width:500px;white-space: nowrap;"></div>';
	chartWin.setContent(contentDiv);	
	$.ajax({url:"sla/gis?rn="+Math.random(),
		cache:false
	}).done(function(data) {
		var length = data.length;
		$.each(data, function(index,data) {
			setMarkers(data, length, index,map,infoWin,chartWin);
		});
		$("#page-top").unmask();
	});
	
	var setMarkers = function(data, length, index,map,infoWin,chartWin) {
		var marker_image;
		var infoClass = "panel-green"; 
		if (index < 3) {
			marker_image = 'https://maps.google.com/mapfiles/ms/micons/red-dot.png';
	        infoClass = "panel-red";	        
		} else if(index < length-3) {
	        marker_image = 'https://maps.google.com/mapfiles/ms/micons/yellow-dot.png';
	        infoClass = "panel-yellow";
	    } else {
	    	marker_image = 'https://maps.google.com/mapfiles/ms/micons/green-dot.png';
	    }
		var marker = new google.maps.Marker({
	        position: new google.maps.LatLng(data.lat,data.lng),
			color:"0000aa",
	        map: map,
	        animation: google.maps.Animation.DROP,
	        icon: marker_image,
	        title: data.zone

	      });
		
		google.maps.event.addListener(marker, 'click', (function(marker, index) {
	          return function() {
	        	  marker.setAnimation(google.maps.Animation.BOUNCE);
	        	  infoWin.setContent('<div class="panel '+infoClass+'"><div class="panel-heading" style="font-weight:bold">Ward : '+
	        			  data.zone+'</div><div class="panel-body" style="white-space:nowrap;color:#000;text-align:left;line-height:2;">Total Complaints : '+
	        			  data.regComp+'<br/>Total Open : '+data.openComp+'<br/>Open From 90 Days: '+data.open90Comp+'<br/>Open : '+data.pecentage+'%<br/>'+
	        			  '<!--<a href="#" id="slapgraph'+data.zoneID+'"><i class="fa fa-line-chart fa-fw"></i>Track Performance</a><br/>'+
	        			  '<a href="#" id="slapbrkup'+data.zoneID+'"><i class="fa fa-pie-chart fa-fw"></i>Open Breakup</a><br/>-->'+
	        			  '</div><div style="padding:5px;background-color:silver;color:white;font-size:11px;font-weight:bold">Since '+
	        			  data.startDt+' - '+data.endDt+'</div></div>');
	        	  infoWin.open(map, marker);
	        	  stopAnimation(marker);
	          };
	    })(marker, index));
		   var id = '#slapgraph'+data.zoneID;
		   $( "#slaGIS" ).on( "click",id, function() {
			    infoWin.close();
	      		chartWin.open(map, marker);
	      		chartWin.setContent(contentDiv);
	      		slaGISTrend(data.zoneID,chartWin,infoWin,map,marker);
			});
		   
		   var brkupid = '#slapbrkup'+data.zoneID;
		   $( "#slaGIS" ).on( "click",brkupid, function() {
			    infoWin.close();
	      		chartWin.open(map, marker);
	      		chartWin.setContent(contentDiv);
	      		slaGISBreakUp(data.zoneID,chartWin,infoWin,map,marker);
			});
		
	};
}

function slaGISTrend(zoneId,chartWin,infoWin,map,marker) {
	$.ajax({url:"slaGISTrend.do?zoneId="+zoneId,
		cache:false
	}).done(function(linedata) {
	    $('#slaPerformance').highcharts({
	    chart: {
            type: 'line'
        },
        exporting: {
        	buttons: {
                contextButton: {
                    enabled: false
                },
                customButton: {
                    text: '<< Back',
                    symbolFill: '#A8BF77',
                    align:'left',
                    onclick: function() {
                    	chartWin.close();
                        infoWin.open(map,marker);
                    }
                }
        	}
        },
        credits : {
		  enabled : false
		},
        title: {
            text: 'Open Complaints Trend',
            style:{
                fontSize: '15px'
           }  
        },
        subtitle:{
        	text:'Last 3 Months'
        },
        xAxis: {  
            tickInterval:  7 * 24 * 3600 * 1000,
	        type: 'datetime',
            startOnTick: true,
            startOfWeek:0,
            labels: {
	            format: '{value:%d/%m/%Y}',
	            rotation: -45,
	            y: 30,
	            align: 'center'
	        } 
	    },
        yAxis: {
        	 title: {
	                text: 'No of Complaints'
	            },
	            min:0,
    			allowDecimals:false
        },
        plotOptions: {
            series: {
                pointStart: linedata.startDate,
                pointInterval: 7 * 24 * 3600 * 1000
            }
        }, 
        series: [
	        {
	            name: 'Open Complaints',
	            data: linedata.opnCnt
	        }
        ]
    });
});};

function slaGISBreakUp(zoneId,chartWin,infoWin,map,marker) {
	$.ajax({url:"slaGISBreakUp.do?zoneID="+zoneId,
		cache:false
	}).done(function(brkupPiedata) {
		 $('#slaPerformance').highcharts({
		        chart: {
		            plotBackgroundColor: null,
		            plotBorderWidth: 0,
		            plotShadow: false
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
		        exporting: {
		        	buttons: {
		                contextButton: {
		                    enabled: false
		                },
		                customButton: {
		                    text: '<< Back',
		                    symbolFill: '#A8BF77',
		                    align:'left',
		                    onclick: function() {
		                    	chartWin.close();
		                        infoWin.open(map,marker);
		                    }
		                }
		        	}
		        },
		        credits : {
				  enabled : false
				},
		        title: {
		            text: 'Open Complaint Typewise'
		        },
		        subtitle:{
		        	text:'Last 3 Months'
		        },
		        legend :{
			    	enabled:true,
		        	layout: 'vertical',
					align: 'right',
					verticalAlign: 'middle',
					borderWidth: 0,
					margin:0,
					symbolHeight:5,
			        symbolWidth:5,
			        itemStyle: {
			            font: 'smaller Verdana, sans-serif'
			        }
		        },
		        tooltip: {
		            pointFormat: 'Complaints :{point.y} Nos'
		        },
		        plotOptions: {
		            pie: {
		            	showInLegend: true,
		                allowPointSelect: true,
		                //cursor: 'pointer',
		                slicedOffset: 0,
		                dataLabels: {
		                    enabled: false,
		                   // format: '<b>{point.name}</b>: {point.percentage:.1f} %',
		                    style: {
		                        color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black'
		                    }
		                }
		            },series: {
		                states: {
		                    hover: {
		                        enabled: false
		                    }
		                }
		            }
		        },
		        series: [{
		        	type: 'pie',
		            name: 'Complaint Types',
		            data: brkupPiedata
		        }]
		    });
	});
}

//#############################**SLA ENDS**#####################################//

//#############################**OVERVIEW STARTS**#####################################//
function compTypeDistribution() {
	$("#page-top").mask('');
	setTitle("Type Distribution");
	overviewPie();
}

var gisPieData = null;
var lastSelSlice = '';
function overviewPie() {
	$.ajax({url:"typewise-aggregate",
		cache:true
	}).done(function(piedata) {
		gisPieData = piedata;
		lastSelSlice = piedata[0].name;
		$.ajax({url:"wardwise-complaint-by-type/"+piedata[0].ctId,
			cache:true,
			data: {color: "#5B94CB"}
		}).done(function(gisData) {
			overviewGis(gisData);
		});
	});
}

function overviewGis(gisData){	
	var gisOption = {
			zoom: 15,
			minZoom:11,
			mapTypeId : google.maps.MapTypeId.TERRAIN, 
			streetViewControl: true,
			center: new google.maps.LatLng(citylat, citylng),
			panControl: true,
			zoomControl: true,
			//disableDefaultUI: true,
	        scrollwheel: true,
	        draggable: true,
	        //styles: mapStyle,
			mapTypeControlOptions: {
				style: google.maps.MapTypeControlStyle.DROPDOWN_MENU
			},
			zoomControlOptions: {
				style: google.maps.ZoomControlStyle.SMALL
			}
	 };
	var map = new google.maps.Map(document.getElementById("overviewGIS"), gisOption);
	var infoWin = new google.maps.InfoWindow();
	var wardArray = new Array();
	$.each(gisData, function(index,data) {
		wardArray[data.wardName.toString()] = {"color":data.color,"name":data.wardName,"value":data.count}; 
	});
	var bounds = new google.maps.LatLngBounds();
	var drawPolygonColor = function (doc){
	    var geoXmlDoc = doc[0];
	    $.each(geoXmlDoc.gpolygons, function(index,gpolygon) {
	    	var wardData = wardArray[geoXmlDoc.placemarks[index].name];
	    	if(wardData != null) {
				gpolygon.setOptions({fillColor: wardData.color, strokeColor: "black", fillOpacity: 0.7});
				google.maps.event.addListener(gpolygon,"click",function(event) {
					map.fitBounds(gpolygon.bounds);
					infoWin.setContent("<div class='panel panel-heading' style=';white-space: nowrap;'>" +
							"<b>Ward Name : </b>"+wardData.name+"<br/>" +
							"<b>No. of Complaints : </b>"+wardData.value+"</div>"); 
					infoWin.setPosition(event.latLng);
					infoWin.open(map);
			    });
				bounds.union(gpolygon.bounds);				
			} else {
				google.maps.event.addListener(gpolygon,"mouseout",function(event) {
					infoWin.close();
			    });
				gpolygon.setOptions({fillColor: "green", strokeColor: "gray", fillOpacity: 0.3});
			}
	    	
		});
	    map.fitBounds(bounds);
	    map.controls[google.maps.ControlPosition.RIGHT_TOP].push(createPieInGmap());
	    $("#page-top").unmask();
	};

	var geoXml=new geoXML3.parser({map: map, singleInfoWindow: true,suppressInfoWindows: true,afterParse:drawPolygonColor});
	geoXml.parse("/egi/downloadfile/gis");
}

function createPieInGmap() {
	var gisPie = document.createElement('div');
	$(gisPie).attr('id','gisPie');
	$(gisPie).css('height','400px');
	$(gisPie).css('width','300px');
	$(gisPie).css('z-index','1');
	$('#overviewGIS').append(gisPie);
	$('#gisPie').highcharts({
	    chart: {
	    		backgroundColor:null,
	            plotBackgroundColor: null,
	            plotBorderWidth: null,
	            plotShadow: false,
	            events:{
	            	load: function () {
	                    var data = this.series[0].data,
	                        dLen = data.length,
	                        i = 0;

	                    while (dLen > i) {
	                        var point = data[i];
	                        if(point.name == lastSelSlice) {
	                        	data[i].update({sliced:true,selected: true});
	                        }
	                        i++;
	                    }

	                }
	            }
	        },
	        colors: Highcharts.map(["#5B94CB","#938250","#6AC657","#4F54B8","#B15D16","#f9f107"], function(color) {
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
	            text: '',
	            style:{
	            	textTransform:'uppercase'
	            }
	        },
	        tooltip: {
	            pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
	        },
	        legend: {
	        	enabled:true,
	        	layout: 'vertical',
				verticalAlign: 'bottom',
				borderWidth: 0,
				margin:0,
				symbolHeight:5,
		        symbolWidth:5,
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
						style:{'fontSize':'smaller', 'textShadow':'none'},
		                formatter: function() {
		                	var width = $(window).width(), height = $(window).height();
		                	if (width >= 768) {
		                		return this.percentage.toFixed(1) + '%';
		                	} else {
		                		return null;
		                	}
		                }
		            },
		            point : {
		            	events : {
		            		click:function() {
		            			if(this.ctId != 0) {
		            				$("#page-top").mask('');
		            				var name = this.name;
		            				var color = this.color.stops[0][1];
		            				lastSelSlice = name;
			            			$.ajax({url:"wardwise-complaint-by-type/"+this.ctId,
			            				cache:true,
			            				data: { color: color }
			            			}).done(function(gisData) {
			            				overviewGis(gisData);
			            			});
		            			}
		            			
		            		}
		            	}
		            }
	            }
	        },
	        series: [{
	            type: 'pie',
	            name: 'Complaint Type Share',
	            data: gisPieData
	        }]
	    });
	
	return gisPie;
}

function gisSearch() {
	$("#page-top").mask('');
	setTitle("GIS Analysis");
	var gisOption = {
			zoom: 11,
			mapTypeId : google.maps.MapTypeId.TERRAIN, 
			streetViewControl: true,
			center: new google.maps.LatLng(citylat, citylng),
			panControl: true,
			zoomControl: true,
			//disableDefaultUI: true,
	        scrollwheel: true,
	        draggable: true,
	        //styles: mapStyle,
			mapTypeControlOptions: {
				style: google.maps.MapTypeControlStyle.DROPDOWN_MENU
			},
			zoomControlOptions: {
				style: google.maps.ZoomControlStyle.SMALL
			}
	 };
	var infoWin = new google.maps.InfoWindow();
	var chartWin = new google.maps.InfoWindow();
	var map = new google.maps.Map(document.getElementById('searchGIS'), gisOption);
	$("#fromDate").datepicker({ dateFormat: "dd/mm/yy"});
	$("#toDate").datepicker({ dateFormat: "dd/mm/yy"});
	$("#search").click(function() {
		$("#page-top").mask('');
		for (var i = 0; i < markers.length; i++) {
		    markers[i].setMap(null);
		}
		$.post( "searchGis.do",$("#gmapsearchform").serialize(), function( data ) {
			$.each(data, function(index,data) {
				setMarkers(data, index,map,infoWin,chartWin);
			});
			$("#page-top").unmask();			
		});
	});
	
	var setMarkers = function (data,index,map,infoWin,chartWin) {    	
		//var contentDiv = '<div id="chartcontainer" style="height:350px;width:550px;white-space: nowrap;"></div>';
		var marker_image;
		var infoClass = "alert-success";    
		if (data.compCount <= 10)
	        marker_image = 'https://maps.google.com/mapfiles/ms/icons/green-dot.png';
	    else if(data.compCount > 10 && data.compCount <= 50) {
	        marker_image = 'https://maps.google.com/mapfiles/ms/icons/pink-dot.png';
	        infoClass = "alert-warning";
	    } else {
	        marker_image = 'https://maps.google.com/mapfiles/ms/icons/red-dot.png';
	        infoClass = "alert-danger";
	    }
		
		var marker = new google.maps.Marker({
	        position: new google.maps.LatLng(data.lat,data.lng),
			color:"0000aa",
	        map: map,
	        animation: google.maps.Animation.DROP,
	        icon: marker_image,
	        title: data.boundary+ " (" + data.compCount + " Complaints)"

	      });
		markers.push(marker);  
		google.maps.event.addListener(marker, 'click', (function(marker, index) {
	          return function() {
	        	  infoWin.setContent('<div class="alert '+infoClass+'" style="color:#000;text-align:left;line-height:2;white-space:nowrap;"> Ward : '+data.boundary+'<br/>Total Complaints : '+data.compCount+'</div>');
	        	  infoWin.open(map, marker);
	          }
	    })(marker, index));
	      	
	    /*google.maps.event.addListener(marker, 'click', (function(marker, index) {
				return function() {
					infoWin.close();
					marker.setAnimation(google.maps.Animation.BOUNCE);
					chartWin.open(map, marker);
					chartWin.setContent(contentDiv);
					var boundary = data.boundary;
					$.ajax({	url:"pgrdashboard.do?isAjax=true&data=gmapchartdata&wardId="+data.boundaryId,
							cache:true
						}).done(function(data) {
							googleChart(data,boundary);
						});
					stopAnimation(marker);
					return false;
				};
	    })(marker, index));*/
	}
	$("#page-top").unmask();
}
$(".ovrviewBkBtn").on("click",function(){
	$(this).addClass('hidden');
	$("#ovrViewHd").addClass("hidden");
	var fn = window[$(this).data('fn')];
	if(typeof fn === "function") {	
		setTimeout(fn,10);
	}
});

//#############################**OVERVIEW STARTS**#####################################//


function topFiveCompType() {
	$.ajax({url:"top-complaints",
		cache:false
	}).done(function(piedata) {
	$("#page-top").mask('');
	if($("#topFiveCompTypeGraph").highcharts()) {
		$("#topFiveCompTypeGraph").highcharts().destroy();
	}
	setTitle("Top 5 Complaint Types");
	$('#topFiveCompTypeGraph').highcharts({
        chart: {
            type: 'area',
        	backgroundColor:null,
            plotBackgroundColor: null,
            plotBorderWidth: null,
            plotShadow: false
        },
        title: {
            text: ''
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
        xAxis: [{
            categories: piedata.year
        }],
        tooltip: {
            shared: true,
            crosshairs: true
        },
        plotOptions: {
            area: {
                stacking: 'normal',
                lineColor: '#666666',
                lineWidth: 1,
                marker: {
                    lineWidth: 1,
                    lineColor: '#666666'
                }
            }
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
        series: piedata.series
      });
    $("#page-top").unmask();
	
});
}
if(isdefault) {
	redressalEfficiency();
} else {
	topFiveCompType();
}

function wardwiseAnalysis() {
	setTitle("GIS Analysis");
	$("#page-top").mask('');
	var gisOption = {
			zoom: 11,
			minZoom:11,
			mapTypeId : google.maps.MapTypeId.TERRAIN, 
			streetViewControl: false,
			center: new google.maps.LatLng(citylat, citylng),
			panControl: false,
			zoomControl: true,
			//disableDefaultUI: true,
	        scrollwheel: true,
	        draggable: true,
	        //styles: mapStyle,
			mapTypeControlOptions: {
				style: google.maps.MapTypeControlStyle.DROPDOWN_MENU
			},
			zoomControlOptions: {
				style: google.maps.ZoomControlStyle.SMALL
			}
	 };
	
	$.ajax({url:"gis-analysis",
		cache:false
	}).done(function(analysisData) {
		
		console.log(JSON.stringify(analysisData));
		
		drawGisAnalysis("wardwiseAnalysis1",analysisData.registered,gisOption);
		//drawGisAnalysis("wardwiseAnalysis2",analysisData.redressed,gisOption);
		//drawGisAnalysis("wardwiseAnalysis3",analysisData.complaintPerProperty,gisOption);
		
		/*$("#top1").text(analysisData.top1[0].compType);
		drawGisAnalysis("topGis1",analysisData.top1,gisOption);
		
		$("#top2").text(analysisData.top2[0].compType);
		drawGisAnalysis("topGis2",analysisData.top2,gisOption);
		
		$("#top3").text(analysisData.top3[0].compType);
		drawGisAnalysis("topGis3",analysisData.top3,gisOption);*/
		$("#page-top").unmask();
	});
	
}
var maps = new Array();
function drawGisAnalysis(winId, gisData,gisOption) {
	var map = new google.maps.Map(document.getElementById(winId), gisOption);
	maps.push(map);
	var infoWin = new google.maps.InfoWindow();
	var wardArray = new Array();
	$.each(gisData, function(index,data) {
		wardArray[data.wardId.toString()] = {"color":data.color,"name":data.wardName,"value":data.count}; 
	});
	var bounds = new google.maps.LatLngBounds();
	var drawPolygonColor = function (doc){
	    var geoXmlDoc = doc[0];
	    $.each(geoXmlDoc.gpolygons, function(index,gpolygon) {
	    	var wardData = wardArray[geoXmlDoc.placemarks[index].name];
	    	if(wardData != null) {
				gpolygon.setOptions({fillColor: wardData.color, strokeColor: "black", fillOpacity: 0.7});
				google.maps.event.addListener(gpolygon,"click",function(event) {
					if(sync) { 
						$.each(maps, function(index,gismap) {
							 gismap.fitBounds(gpolygon.bounds);
						 });
					} else {
						map.fitBounds(gpolygon.bounds);
					}
					 infoWin.setContent("<div class='panel panel-heading' style=';white-space: nowrap;'>" +
								"<b>Ward Name : </b>"+wardData.name+"<br/>" +
								"<b>No. of Complaints : </b>"+wardData.value+"</div>"); 
					infoWin.setPosition(event.latLng);
					infoWin.open(map);
			    });
				
				google.maps.event.addListener(map, 'bounds_changed', function(event) {
					if(sync) { 	
						$.each(maps, function(index,gismap) {
				    		gismap.setZoom(map.getZoom());
						 });
					}
				});
				
				google.maps.event.addListener(map, 'dragend', function(event){
					if(sync) { 
						$.each(maps, function(index,gismap) {
							gismap.setCenter(map.getCenter());
						});
					}
				});
				bounds.union(gpolygon.bounds);
			} else {
				google.maps.event.addListener(gpolygon,"mouseout",function(event) {
					infoWin.close();
			    });
				gpolygon.setOptions({fillColor: "whitesmoke", strokeColor: "gray", fillOpacity: 0.3});
			}
		});
	    map.fitBounds(bounds);	   
	};

	var geoXml=new geoXML3.parser({map: map, singleInfoWindow: true,suppressInfoWindows: true,afterParse:drawPolygonColor});
	geoXml.parse("/egi/downloadfile/gis");
}