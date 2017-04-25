/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *  
 *  Copyright (C) 2017  eGovernments Foundation
 *  
 *  The updated version of eGov suite of products as by eGovernments Foundation
 *  is available at http://www.egovernments.org
 *  
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see http://www.gnu.org/licenses/ or
 *  http://www.gnu.org/licenses/gpl.html .
 *  
 *  In addition to the terms of the GPL license to be adhered to in using this
 *  program, the following additional terms are to be complied with:
 *  
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 *  
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *  
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *  
 *  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
/*
$.fn.pageMe = function(opts){
    var $this = this,
        defaults = {
            perPage: 7,
            showPrevNext: false,
            hidePageNumbers: false
        },
        settings = $.extend(defaults, opts);

    var listElement = $this;
    var perPage = settings.perPage;
    var children = listElement.children();
    var pager = $('.pager');

    if (typeof settings.childSelector!="undefined") {
        children = listElement.find(settings.childSelector);
    }

    if (typeof settings.pagerSelector!="undefined") {
        pager = $(settings.pagerSelector);
    }

    var numItems = children.size();
    var numPages = Math.ceil(numItems/perPage);

    pager.data("curr",0);

    if (settings.showPrevNext){
        $('<li><a href="#" class="prev_link">«</a></li>').appendTo(pager);
    }

    var curr = 0;
    while(numPages > curr && (settings.hidePageNumbers==false)){
        $('<li><a href="#" class="page_link">'+(curr+1)+'</a></li>').appendTo(pager);
        curr++;
    }

    if (settings.showPrevNext){
        $('<li><a href="#" class="next_link">»</a></li>').appendTo(pager);
    }

    pager.find('.page_link:first').addClass('active');
    pager.find('.prev_link').hide();
    if (numPages<=1) {
        pager.find('.next_link').hide();
    }
    pager.children().eq(1).addClass("active");

    children.hide();
    children.slice(0, perPage).show();

    pager.find('li .page_link').click(function(){
        var clickedPage = $(this).html().valueOf()-1;
        goTo(clickedPage,perPage);
        return false;
    });
    pager.find('li .prev_link').click(function(){
        previous();
        return false;
    });
    pager.find('li .next_link').click(function(){
        next();
        return false;
    });

    function previous(){
        var goToPage = parseInt(pager.data("curr")) - 1;
        goTo(goToPage);
    }

    function next(){
        goToPage = parseInt(pager.data("curr")) + 1;
        goTo(goToPage);
    }

    function goTo(page){
        var startAt = page * perPage,
            endOn = startAt + perPage;

        children.css('display','none').slice(startAt, endOn).show();

        if (page>=1) {
            pager.find('.prev_link').show();
        }
        else {
            pager.find('.prev_link').hide();
        }

        if (page<(numPages-1)) {
            pager.find('.next_link').show();
        }
        else {
            pager.find('.next_link').hide();
        }

        pager.data("curr",page);
        pager.children().removeClass("active");
        pager.children().eq(page+1).addClass("active");

    }
};
*/
function openTradeLicense(obj) {
    window.open("/tl/public/viewtradelicense/viewTradeLicense-view.action?id="
        + $(obj).data('eleval'), '',
        'scrollbars=yes,width=1000,height=700,status=yes');
}

function populateData(reponsedata) {
    $('.report-section').removeClass('display-hide');
    $('#report-footer').show();
    var tbl = $("#tbldemandgenerate");
    var reportdatatable = tbl.dataTable({
            dom : "<'row'<'col-xs-4 pull-right'f>r>t<'row add-margin'<'col-md-3 col-xs-6'i><'col-md-2 col-xs-6'l><'col-md-3 col-xs-6 text-right'B><'col-md-4 col-xs-6 text-right'p>>",
            "autoWidth" : false,
            "bDestroy" : true,
            responsive : true,
            destroy : true,
            'aaData' : reponsedata,
            buttons : [  {
                extend : 'pdf',
                title : 'Demand Generation Log',
                filename : 'Demand Generation Log',
                orientation : 'portrait',
                pageSize:'A4',
                exportOptions : {
                    columns : ':visible'
                }
            }, {
                extend : 'excel',
                filename : 'Demand Generation Log',
                exportOptions : {
                    columns : ':visible'
                }
            }, {
                extend : 'print',
                title : 'Demand Generation Log',
                filename : 'Demand Generation Log',
                exportOptions : {
                    columns : ':visible'
                }
            } ],
            columns : [
                {
                    "data" : function(row, type, set, meta) {
                        return {
                            name : row.licensenumber,
                            id : row.licenseid
                        };
                    },
                    "render" : function(data, type, row) {
                        return '<a href="javascript:void(0);" onclick="openTradeLicense(this);" data-hiddenele="id" data-eleval="'
                            + data.id + '">' + data.name + '</a>';
                    },
                    "sTitle" : "License No."
                }, {
                    "data" : "detail",
                    "sTitle" : "Details"
                }, {
                    "data" : "status",
                    "sTitle" : "Status"
                }]});
    $('.loader-class').modal('hide');
}

$(document).ready(function(){
    $('#regenbtn').click(function() {
		$('#generatedemand').attr('method', 'post');
		$('#generatedemand').attr('action', '/tl/demand/regenerate');
	});
    $('#genmissingbtn').click(function() {
        $('#generatedemand').attr('method', 'post');
        $('#generatedemand').attr('action', '/tl/demand/generatemissing');
    });
    $(".alert-success").fadeTo(5000, 5000).slideUp(500, function(){
        $(".alert-success").alert('close');
    });
});

