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

$(document).ready(function () {
    $('#new-pass').popover({trigger: "focus", placement: "bottom"});

    $(document).on("keydown", disableRefresh);

    preventBack();

    $.fn.dataTable.moment('DD/MM/YYYY h:mm a');

    $('.page-container.horizontal-menu header.navbar .navbar-right > li, .page-container.horizontal-menu header.navbar .navbar-right > li ul li').hover(
        function () {
            $(this).children('ul').show();
        },
        function () {
            $(this).children('ul').removeAttr('style');
            $(this).children('ul').hide();
        });

    $('#feedback-form').on('submit', function (e) {
        e.preventDefault();
        $.ajax({
            url: 'home/feedback/sent',
            type: 'GET',
            data: {'subject': $("#subject").val(), 'message': $("#comment").val()},
            success: function (data) {
                bootbox.alert("Your feedback successfully submitted.");
            },
            error: function () {

            }, complete: function () {
                $('.add-feedback').modal('hide');
            }
        });

    });

    $('#password-form').on('submit', function (e) {
        e.preventDefault();
        $.ajax({
            url: 'home/password/update',
            type: 'GET',
            data: {
                'currentPwd': $("#old-pass").val(),
                'newPwd': $("#new-pass").val(),
                'retypeNewPwd': $("#retype-pass").val()
            },
            success: function (data) {
                var msg = "";
                if (data == "SUCCESS") {
                    $("#old-pass").val("");
                    $("#new-pass").val("");
                    $("#retype-pass").val("");
                    $('.change-password').modal('hide');
                    bootbox.alert("Your password has been updated.");
                    $('.pass-cancel').removeAttr('disabled');
                    $('#pass-alert').hide();
                } else if (data == "NEWPWD_UNMATCH") {
                    msg = "New password you have entered does not match with retyped password.";
                    $("#new-pass").val("");
                    $("#retype-pass").val("");
                    $('.change-password').modal('show');
                } else if (data == "CURRPWD_UNMATCH") {
                    msg = "Old password you have entered is incorrect.";
                    $("#old-pass").val("");
                    $('.change-password').modal('show');
                } else if (data == "NEWPWD_INVALID") {
                    msg = $('.password-error-msg').html();
                    $("#new-pass").val("");
                    $("#retype-pass").val("");
                    $('.change-password').modal('show');
                }
                $('.password-error').html(msg).show();

            },
            error: function () {
                bootbox.alert("Internal server error occurred, please try after sometime.");
            }
        });

    });

    worklist();

    $("#official_inbox").on('click', 'tbody tr td i.inbox-history', function (e) {
        $('.history-inbox').modal('show');
        historyTableContainer = $("#historyTable");
        historyTableContainer.DataTable({
            "sDom": "<'row'<'col-xs-12 hidden col-right'f>r>t<'row buttons-margin'<'col-md-6 col-xs-12'i>" +
            "<'col-md-3 col-xs-6'l><'col-md-3 col-xs-6 text-right'p>>",
            "aLengthMenu": [[10, 25, 50, -1], [10, 25, 50, "All"]],
            "autoWidth": false,
            "paging": false,
            "destroy": true,
            "aaSorting": [],
            "oLanguage": {
                "sInfo": ""
            },
            "ajax": {
                "url": "inbox/history?stateId=" + tableContainer1.dataTable().fnGetData($(this).parent().parent(), 7),
                "dataSrc": ""
            },
            "columns": [
                {"data": "date", "width": "20%"},
                {"data": "sender", "width": "15%"},
                {"data": "task", "width": "20%"},
                {"data": "status", "width": "20%"},
                {"data": "details", "width": "20%"},
                {"data": "id", "visible": false, "searchable": false},
                {"data": "link", "visible": false, "searchable": false}

            ]
        });

        e.stopPropagation();
    });

    $('.workspace').click(function () {
        $('.main-space').hide();
        $('.workspace').removeClass('active');
        clearnow();
        $('#' + $(this).attr('data-work')).find('input').val('');
        $(this).addClass('active');
        if ($(this).attr('data-work') == 'worklist') {
            focussedmenu = "worklist";
            worklist();
        } else if ($(this).attr('data-work') == 'drafts') {
            focussedmenu = "drafts";
            drafts();
        } else if ($(this).attr('data-work') == 'notifications') {
            focussedmenu = "notifications";
            notifications();
        }
        $('#' + $(this).attr('data-work')).show();
    });

    $('.search-table').keyup(function () {
        tableContainer1.dataTable().fnFilter(this.value);
    });

    $("#official_inbox").on('click', 'tbody tr', function (event) {
        if (tableContainer1.dataTable().fnGetData(this, 8) != undefined) {
            var windowObjectReference = window.open(tableContainer1.dataTable().fnGetData(this, 8),
                tableContainer1.dataTable().fnGetData(this, 7) + '', 'width=900, height=700, top=300, left=150,scrollbars=yes');
            openedWindows.push(windowObjectReference);
            windowObjectReference.focus();
        }
    });

    $("#official_drafts").on('click', 'tbody tr', function (event) {
        if (tableContainer1.dataTable().fnGetData(this, 7) != undefined) {
            var windowObjectReference = window.open(tableContainer1.dataTable().fnGetData(this, 7),
                tableContainer1.dataTable().fnGetData(this, 6) + '', 'width=900, height=700, top=300, left=150,scrollbars=yes');
            openedWindows.push(windowObjectReference);
            windowObjectReference.focus();
        }
    });

    $('.check-password').blur(function () {
        if (($('#new-pass').val() != "") && ($('#retype-pass').val() != "")) {
            if ($('#new-pass').val() !== $('#retype-pass').val()) {
                $('.password-error').show();
                $('#retype-pass').addClass('error');
            }
        }
    });

    $('#natureofwork').on('click', 'ul li a', function () {
        $('#natureofwork ul li').removeClass('active');
        $(this).parent().addClass('active');
        if ($('#natureofwork ul li a[data-now=Reset]').length == 0) {
            $('#natureofwork').append('<ul class="nav nav-pills" role="tablist"><li role="presentation"><a href="javascript:void(0)" data-now=Reset><span><i class="fa fa-refresh"></i></span>Reset / Clear</a></li></ul>');
        }

        var taskName, moduleName;

        if (!$(this).data('now')) {
            taskName = $(this).data('task') ? unescape($(this).data('task')) : "";
            moduleName = $(this).data('module') ? unescape($(this).data('module')) : "";
        }
        else
            taskName = unescape($(this).data('now'));

        now_json = [];
        refreshnow(taskName, moduleName);

        $('#inboxsearch').trigger('keyup');
    });

    //search menu item in tree
    $('.search_list').hide();//Initially hide search item section

    var menujson = [];
    var count = 0;
    var ind = 0;
    var offsetht = 0;
    var offsetbottomht = 0;
    var set = 0;

    $('#searchtree').on('keyup', function (e) {

        switch (e.keyCode) {

            case 38: /// up arrow
                e.preventDefault();
                count = (count - 1) > 0 ? (count - 1) : 0;
                ind = (count - 1) > 0 ? (count - 1) : 0;
                if (count > 0) {
                    $('.ullist li').removeClass('focus');
                    $('.ullist li').eq(ind).addClass('focus');
                    if ($('.ullist li.focus').offset().top <= 68) {
                        offsetbottomht = (($('.list').position().top + ($(window).height() - 63 - 48 - 29)) > 0) ? 0 : ($('.list').position().top + ($(window).height() - 63 - 48 - 29));
                        $('.list').animate({top: offsetbottomht}, 500);
                        set = 0;
                        offsetht = 0;
                    }
                }
                break;

            case 40: // down arrow
                e.preventDefault();
                // Store the reference to our top level link
                var link = $(this);

                // Find the ul li element that acts as the search item
                var dropdown = link.parent('.search').parent('.page-container.horizontal-menu').find('.ullist li');

                // If there is a UL available, place focus on the first focusable element within
                if (dropdown.length > 0) {
                    // Make sure to stop event bubbling
                    if (count >= dropdown.length) {
                        //console.log('Bottom failed');
                    } else {
                        count = count + 1;
                        ind = count - 1;
                        $('.ullist li').removeClass('focus');
                        $('.ullist li').eq(ind).addClass('focus');
                        if ($('.ullist li.focus').offset().top > ($(window).height() - 60)) {
                            set = $('.ullist li.focus').offset().top - 63 - 48;
                            offsetht += set;
                            $('.list').animate({top: -(offsetht) + 'px'}, 500);
                        }
                    }
                }
                break;

            case 13: /// enter key
                $('.ullist').find('li.focus a').click();
                break;

            case 27: /// Escape key
                $('#searchtree').val('');
                $('#searchtree').trigger('blur');
                clearsearchlist();
                break;

            default : //Logic for search menu tree
                menujson = [];
                count = 0;
                ind = 0;
                $('.list').css('top', '0px');

                if ($(this).val().length > 3) {

                    var result = getObject(menuItems, $(this).val());
                    $('.search_list .list ul').html('');

                    //Load dropdown values withrespect to json
                    if (result.length == 0) {
                        $('.search_list').hide();
                    } else {
                        searchlist_height = $(window).height() - 63 - 49 - 29;
                        $('.search_list').show();
                        $('.search_list').height(searchlist_height);
                        $.each(result, function (k, v) {
                            $('.search_list .list ul').append('<li><a href=' + v.link + ' class="open-popup" data-strwindname=' + v.id + '>' + v.name + '</a></li>');
                        });
                        if ($('.list').innerHeight() <= $('.search_list').innerHeight()) {
                            $('.search_list').css('overflow-y', 'hidden');
                        } else {
                            $('.search_list').css('overflow-y', 'scroll');
                        }
                    }
                } else {
                    //Menu JSON empty when seach key length less than 3
                    clearsearchlist();
                }
                break;

        }
    });

    $(document).on('focus', '#searchtree', function () {
        $('.searchicon').hide();
    }).on('blur', '#searchtree', function () {
        if ($(this).val().length > 0) {
            $('.searchicon').hide();
        } else {
            $('.searchicon').show();
        }
    });

    $('.searchicon').click(function () {
        $(this).hide();
        $('#searchtree').focus();
    });

    //prevent cursor from moving while using arrow keys
    $('input').bind('keydown', function (e) {
        if (e.keyCode == '38' || e.keyCode == '40') {
            e.preventDefault();
        }
    });

    function getObject(theObject, searchkey) {
        searchkey = searchkey.toLowerCase();
        var result = null;
        if (theObject instanceof Array) {
            for (var i = 0; i < theObject.length; i++) {
                result = getObject(theObject[i], searchkey);
            }
        }
        else {
            for (var prop in theObject) {
                if (prop == 'name') {
                    if (theObject[prop].toLowerCase().indexOf(searchkey) >= 0) {
                        if (theObject.link != 'javascript:void(0);' && theObject.icon != 'fa fa-times-circle remove-favourite') {
                            var obj = {};
                            obj['id'] = theObject.id;
                            obj['name'] = theObject.name;
                            obj['link'] = theObject.link;
                            menujson.push(obj);
                            return theObject;
                        }
                    }
                }
                if (theObject[prop] instanceof Object || theObject[prop] instanceof Array) {
                    result = getObject(theObject[prop], searchkey);
                }
            }
        }
        return menujson;
    }

    function clearsearchlist() {
        menujson = [];
        //Show No results in dropdown or hide it
        $('.search_list').hide();
        $('.list ul').html('');
        $('.list').css('top', '0px');
        offsetht = 0;
        offsetbottomht = 0;
    }


    $("#official_inbox, #official_drafts").on('click', 'tbody tr td span.details', function (e) {
        $(this).parent().html($(this).data('text'));
        e.stopPropagation();
        e.preventDefault();
    });

    $('#inboxsearch, #draftsearch').keyup(function (e) {
        if (e.keyCode == 27) { // escape key maps to keycode `27`
            $(this).val('');
            $('#' + $(this).attr('id')).trigger('keyup');
        }
    });

});

var response_json = [];
var counts = {};
var now_json = [];
var currentCondition;

Array.prototype.find = Array.prototype.find || function (callback) {
    for (var i = 0; i < this.length; i++) {
        if (callback(this[i], i)) {
            return this[i];
        }
    }
};

function clearnow() {
    $('#natureofwork').html('');
    response_json = [];
    counts = {};
    now_json = [];
    currentCondition = undefined;
}

//common ajax functions for worklist, drafts and notifications
function worklist() {
    tableContainer1 = $("#official_inbox");
    tableContainer1.DataTable({
        "sDom": "<'row'<'col-xs-12 hidden col-right'f>r>t<'row buttons-margin'<'col-md-5 col-xs-12'i><'col-md-3 col-xs-6'l><'col-md-4 col-xs-6 text-right'p>>",
        "aLengthMenu": [[10, 25, 50, -1], [10, 25, 50, "All"]],
        "bDestroy": true,
        "autoWidth": false,
        "aaSorting": [],
        "ajax": {
            "url": "inbox",
            "dataSrc": ""
        },
        "deferRender": true,
        "columns": [
            {"data": "date", "width": "15%"},
            {"data": "sender", "width": "15%"},
            {"data": "task", "width": "15%"},
            {"data": "status", "width": "20%"},
            {"data": "details", "width": "20%"},
            {"data": "elapsed", "className": "text-center", "width": "12%"},
            {
                "data": null,
                "width": "0%",
                "target": -1,
                "sortable": false,
                "defaultContent": '<i class="fa fa-history inbox-history history-size" class="tooltip-secondary" data-toggle="tooltip" title="History"></i>'
            },
            {"data": "id", "visible": false, "searchable": false},
            {"data": "link", "visible": false, "searchable": false}
        ],
        "columnDefs": [
            {
                "render": function (data, type, row) {
                    return type === 'display' && data.length > 75 ? data.substr(0, 75) + ' <span class="details" data-text="' + data + '"><button class="btn-xs" style="font-size:10px;">More <i class="fa fa-angle-double-right" aria-hidden="true"></i></button></span>' : data;
                },
                "targets": 4
            }
        ],
        "fnInitComplete": function (oSettings, json) {
            response_json = JSON.stringify(json);
            if (JSON.parse(response_json).length != 0) {

                var groupByModule = JSON.parse(response_json).reduce(function (obj, item) {
                    obj[item.moduleName] = obj[item.moduleName] || [];

                    var taskItem;
                    if (obj[item.moduleName].length > 0)
                        taskItem = obj[item.moduleName].find(
                            function (task) {
                                return Object.keys(task)[0] == item.task;
                            });

                    if (taskItem) {
                        taskItem[Object.keys(taskItem)[0]] += 1;
                    }
                    else {
                        var task = item.task;
                        var moduleValJson = {};
                        moduleValJson[task] = 1;
                        obj[item.moduleName].push(moduleValJson);
                    }

                    return obj;
                }, {});

                loadGroupMenusModuleWise(groupByModule);

            }
        }
    });

}

function loadGroupMenusModuleWise(moduleArray) {
    if (Object.keys(moduleArray).length > 1 ||
        (Object.keys(moduleArray).length == 1 && Object.keys(moduleArray[Object.keys(moduleArray)[0]]).length > 1)) {

        Object.keys(moduleArray)
            .sort()
            .forEach(function (moduleName, i) {
                var key = escape(moduleName);
                var count = 0;
                var taskItems = "";
                moduleArray[moduleName].map(function (item) {
                    count += item[Object.keys(item)];
                    taskItems = taskItems + '<li role="presentation"><a href="javascript:void(0)" data-module="' + escape(moduleName) + '" data-task="' + escape(Object.keys(item)[0]) + '"><span><i class="fa fa-tags"></i></span>' + Object.keys(item)[0] + ' <span class="badge">' + item[Object.keys(item)[0]] + '</span></a></li>';
                });

                var generatedHtml = '<div class="row"><div class="col-xs-3 col-sm-3 col-md-3"><ul class="nav nav-pills" role="tablist"><li role="presentation"><a href="javascript:void(0)" data-module="' + key + '"><span><i class="fa fa-circle"></i> </span>' + moduleName
                    + ' <span class="badge">' + count + '</span></a></li></ul></div> <div class="col-xs-9 col-sm-9 col-md-9"><ul style="border-bottom:1px solid #d4d4d4;margin-bottom:5px;" class="nav nav-pills" role="tablist">' + taskItems + '</ul></div></div>';
                $('#natureofwork').append(generatedHtml);
            });

        if (currentCondition) {
            var attrCondition = "";
            if (currentCondition.moduleName)
                attrCondition = '[data-module="' + escape(currentCondition.moduleName) + '"]';
            if (currentCondition.taskName)
                attrCondition += '[data-task="' + escape(currentCondition.taskName) + '"]';
            else
                attrCondition += ':not([data-task])';

            if (attrCondition)
                $('#natureofwork').find('li a' + attrCondition).parent().addClass('active');
            refreshnow(currentCondition.taskName, currentCondition.moduleName);
        }

    }


}

function drafts() {
    tableContainer1 = $("#official_drafts");
    tableContainer1.DataTable({
        "sDom": "<'row'<'col-xs-12 hidden col-right'f>r>t<'row buttons-margin'<'col-md-5 col-xs-12'i>" +
        "<'col-md-3 col-xs-6'l><'col-md-4 col-xs-6 text-right'p>>",
        "aLengthMenu": [[10, 25, 50, -1], [10, 25, 50, "All"]],
        "bDestroy": true,
        "aaSorting": [],
        "autoWidth": false,
        "ajax": {
            "url": "inbox/draft",
            "dataSrc": ""
        },
        "deferRender": true,
        "columns": [
            {"data": "date"},
            {"data": "sender"},
            {"data": "task"},
            {"data": "status"},
            {"data": "details", "width": "20%"},
            {"data": "elapsed"},
            {"data": "id", "visible": false, "searchable": false},
            {"data": "link", "visible": false, "searchable": false}
        ],
        "columnDefs": [
            {
                "render": function (data, type, row) {
                    return type === 'display' && data.length > 75 ? data.substr(0, 75) +
                        ' <span class="details" data-text="' + data + '"><button class="btn-xs" style="font-size:10px;">More ' +
                        '<i class="fa fa-angle-double-right" aria-hidden="true"></i></button></span>' : data;
                },
                "targets": 4
            }
        ]
    });
}

function notifications() {
    tableContainer1 = $("#official_notify");
    tableContainer1.DataTable({
        "sDom": "<'row'<'col-xs-12 hidden col-right'f>r>t<'row buttons-margin'<'col-md-5 col-xs-12'i>" +
        "<'col-md-3 col-xs-6'l><'col-md-4 col-xs-6 text-right'p>>",
        "aLengthMenu": [[10, 25, 50, -1], [10, 25, 50, "All"]],
        "bDestroy": true,
        /* Disable initial sort */
        "aaSorting": [],
        "autoWidth": false,
        "deferRender": true
    });
}

function worklistwrtnow(json) {
    tableContainer1 = $("#official_inbox");
    tableContainer1.DataTable({
        "sDom": "<'row'<'col-xs-12 hidden col-right'f>r>t<'row buttons-margin'<'col-md-5 col-xs-12'i>" +
        "<'col-md-3 col-xs-6'l><'col-md-4 col-xs-6 text-right'p>>",
        "aLengthMenu": [[10, 25, 50, -1], [10, 25, 50, "All"]],
        "bDestroy": true,
        "aaSorting": [],
        "autoWidth": false,
        "data": json,
        "columns": [
            {"data": "date", "width": "15%"},
            {"data": "sender", "width": "15%"},
            {"data": "task", "width": "15%"},
            {"data": "status", "width": "20%"},
            {"data": "details", "width": "20%"},
            {"data": "elapsed", "className": "text-center", "width": "12%"},
            {
                "data": null,
                "width": "0%",
                "target": -1,
                "sortable": false,
                "defaultContent": '<i class="fa fa-history inbox-history history-size" class="tooltip-secondary" data-toggle="tooltip" title="History"></i>'
            },
            {"data": "id", "visible": false, "searchable": false},
            {"data": "link", "visible": false, "searchable": false}
        ],
        "columnDefs": [
            {
                "render": function (data, type, row) {
                    return type === 'display' && data.length > 75 ? data.substr(0, 75) +
                        ' <span class="details" data-text="' + data + '"><button class="btn-xs" style="font-size:10px;">More ' +
                        '<i class="fa fa-angle-double-right" aria-hidden="true"></i></button></span>' : data;
                },
                "targets": 4
            }
        ]
    });
}

function refreshnow(taskName, moduleName) {
    if (taskName != 'Reset' && taskName != undefined) {
        now_json = JSON.parse(response_json).filter(function (task) {
            return (taskName ? task.task == taskName : true) && (moduleName ? task.moduleName == moduleName : true);
        });
        worklistwrtnow(now_json);
        currentCondition = {taskName: taskName, moduleName: moduleName};
    } else {
        currentCondition = undefined;
        $('#natureofwork ul li a[data-now="Reset"]').parent().parent().remove();
        worklistwrtnow(JSON.parse(response_json));
    }
}

function inboxloadmethod() {
    clearnow();
    if (focussedmenu == 'worklist') {
        worklist();
    } else if (focussedmenu == 'drafts') {
        drafts();
    } else if (focussedmenu == 'notifications') {
        notifications();
    }
}