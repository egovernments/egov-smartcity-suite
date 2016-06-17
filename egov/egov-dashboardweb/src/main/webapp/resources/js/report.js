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

/* $.extend($.expr[":"], {
				"containsIN": function(elem, i, match, array) {
					return (elem.textContent || elem.innerText || "").toLowerCase().indexOf((match[3] || "").toLowerCase()) >= 0;
				 }
			}); */
		
$('.accordion').click(function(){
	if($(this).data('collapse') == 'more'){
		$(this).data('collapse','less');
		$(this).html('Less <i class="fa fa-angle-up" aria-hidden="true"></i>');
		$(this).closest('.panel-body').find('li.report-li').removeClass('hide');
	}else if($(this).data('collapse') == 'less'){
		$(this).data('collapse','more');
		$(this).html('More <i class="fa fa-angle-down" aria-hidden="true"></i>');
		$(this).closest('.panel-body').find('li.report-li:gt(1)').addClass('hide');
	}
});

$('.expand').click(function(){
	$('li').removeClass('hide');
	$('.panel-body').find('span a').data('collapse','less').html('Less <i class="fa fa-angle-up" aria-hidden="true"></i>');
});

$('.collapsable').click(function(){
	$('.panel-body').find('li.report-li:gt(1)').addClass('hide');
	$('.panel-body').find('span a').data('collapse','more').html('More <i class="fa fa-angle-down" aria-hidden="true"></i>');
});

/* $( ".search" ).keyup(function() {
	$( ".report-li a").css( "color", "#337ab7" );
	var key = $(this).val();
	//alert( "Handler for .keyup() called.:" + $(this).val());
	if(!key){
		//empty - collapse
		$('.panel-body').find('li.report-li:gt(1)').addClass('hide');
		$('.panel-body').find('span a').data('collapse','more').html('More <i class="fa fa-angle-down" aria-hidden="true"></i>');
	}else{
		//Non-empty - Expand
		$('li').removeClass('hide');
		$('.panel-body').find('span a').data('collapse','less').html('Less <i class="fa fa-angle-up" aria-hidden="true"></i>');
		//search in li a tag
		$( ".report-li a:containsIN('"+key+"')" ).css( "color", "red" );
	}
}); */

var reportarray = [{"report":"Grievance Type Wise Report", "url":"/pgr/report/complaintTypeReport"},
                   {"report":"Ageing Report - Department wise", "url":"/pgr/report/ageingReportByDept"},
                   {"report":"Ageing Report - Boundary wise", "url":"/pgr/report/ageingReportByBoundary"},
                   {"report":"Status Drill Down Report - Department wise", "url":"/pgr/report/drillDownReportByBoundary"},
                   {"report":"Status Drill Down Report - Boundary wise", "url":"/pgr/report/drillDownReportByDept"},
                   {"report":"Collection summary report", "url":"/collection/reports/collectionSummary-criteria.action#no-back-button"},
                   {"report":"Remittance voucher report", "url":"/collection/reports/remittanceVoucherReport-criteria.action#no-back-button"},
                   {"report":"Receipt register report", "url":"/collection/reports/receiptRegisterReport-criteria.action#no-back-button"},
                   {"report":"Work Progress Register", "url":"/egworks/reports/workprogressregister/searchform"},
                   {"report":"Estimate Abstract Report By Department", "url":"/egworks/reports/estimateabstractreport/departmentwise-searchform"},
                   {"report":"Estimate Abstract Report By Type Of Work", "url":"/egworks/reports/estimateabstractreport/typeofworkwise-searchform"},
                   {"report":"Revenue ward wise collection report", "url":"/ptis/reports/collectionSummaryReport-wardWise.action"},
                   {"report":"Defaulters Report", "url":"/ptis/reports/defaultersReport-search.action#no-back-button"},
                   {"report":"DCB Report", "url":"/ptis/reports/dCBReport-search.action#no-back-button"},
                   {"report":"Base Register", "url":"/ptis/report/baseRegister"},
                   {"report":"Daily collection report", "url":"/ptis/report/dailyCollection"},
                   {"report":"Arrear Register report", "url":"/ptis/reports/arrearRegisterReport-index.action#no-back-button"},
                   {"report":"Advertisement Collection Report", "url":"/adtax/reports/search-for-dcbreport"},
                   {"report":"Agency wise Collection Report ", "url":"/adtax/reports/search-dcbreport"},
                   {"report":"DCB Report by Trade", "url":"/tl/tlreports/dCBReport/licenseNumberWise#no-back"},
                   {"report":"View/Search Trade licenses", "url":"/tl/search/searchTrade-newForm.action#no-back"},
                   {"report":"DCB Report revenue ward wise", "url":"/wtms/reports/dCBReport/wardWise"},
                   {"report":"Defaulters Report", "url":"/wtms/report/defaultersWTReport/search"},
                   {"report":"Daily collection report", "url":"/wtms/report/dailyWTCollectionReport/search/"},
                   {"report":"DCB Report locality wise", "url":"/wtms/reports/dCBReport/localityWise"},
                   {"report":"Number of connections", "url":"/wtms/reports/coonectionReport/wardWise"},
                   {"report":"Base Register report", "url":"/wtms/report/baseRegister"}
	       			];

$( ".search" ).keyup(function() {
	var key = $(this).val();
	$('.search-ul').empty();
	searchlink = '';
	if(key){
		var result = getObject(reportarray, key);
		if(result.length!=0){
			$('.search-ul').show().append(result);
			//console.log($('.search-ul').height());
			if($('.search-ul').height() >= '200'){
				//console.log('came to set fixed height');
				$('.search-ul').css('height','200');
			}else{
				//console.log('came to set height auto');
				$('.search-ul').css('height','auto');
			}
		}else{
			//console.log('No result set');
			$('.search-ul').empty().hide();
		}
	}else{
		//console.log('Empty Input');
		$('.search-ul').empty().hide();
	}
});

var searchlink = '';

function getObject(theObject, searchkey) {
	searchkey = searchkey.toLowerCase();
    var result = null;
    if(theObject instanceof Array) {
        for(var i = 0; i < theObject.length; i++) {
            result = getObject(theObject[i], searchkey);
        }
    }
    else
    {
        for(var prop in theObject) {
            //console.log(prop + ': ' + theObject[prop]);
            if(prop == 'report') {
                if (theObject[prop].toLowerCase().indexOf(searchkey) >= 0){
                	//console.log(theObject[prop]+'<--->'+theObject.url);
                	searchlink+='<li><a href="'+theObject.url+'" class="open-popup" data-strwindname="'+theObject[prop]+'">'+theObject[prop]+'</a></li>';
                	return theObject;	
                }
            }
            if(theObject[prop] instanceof Object || theObject[prop] instanceof Array){
	            //console.log('came for inner object iteration');
            	result = getObject(theObject[prop], searchkey);
            }
        }
    }
    return searchlink;
}