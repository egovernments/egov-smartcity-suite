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

var getUrlParameter = function getUrlParameter(sParam) {
    var sPageURL = decodeURIComponent(window.location.search.substring(1)),
        sURLVariables = sPageURL.split('&'),
        sParameterName,
        i;

    for (i = 0; i < sURLVariables.length; i++) {
        sParameterName = sURLVariables[i].split('=');

        if (sParameterName[0] === sParam) {
            return sParameterName[1] === undefined ? true : sParameterName[1];
        }
    }
};

var report = getUrlParameter('report');

if(report != undefined){
	//Show secret key - Non public URL's
	$('.lock').show();
}else{
	//Hide secret key - Non public URL's
	$('.lock').hide();
}

var windowhref=window.location.href;

windowhref=windowhref.replace("http://", "");
windowhref=windowhref.replace("https://", "");

cityUrl=windowhref.split(".");

if(cityUrl.length > 1){
	var text = $('#header-text').data('append-text');
	$('#header-text').html(ucfirst(cityUrl[0])+" "+text);
	document.title = ucfirst(cityUrl[0])+" "+text;
}


function ucfirst (str) {
    return typeof str !="undefined"  ? (str += '', str[0].toUpperCase() + str.substr(1)) : '' ;
}

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

var reportarray=[];

$('.open-popup').each(function(){
	var report={report:$(this).text()+" - "+$(this).closest('div.panel').find('.panel-heading').html(), url:$(this).attr('href')};
	reportarray.push(report);
});


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