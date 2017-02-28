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
$(document).ready(function () {
    $('#categories').change(function () {
        var val = $(this).val();
        if (val !== '') {
            var results = [];
            $.ajax({
                type: "GET",
                url: '/tl/licensesubcategory/by-category',
                dataType: "json",
                data: {categoryId: val},
                success: function (data) {
                    $.each(data, function (i) {
                        var obj = {};
                        obj['id'] = data[i]['code'];
                        obj['text'] = data[i]['name'];
                        results.push(obj);
                    });
                    select2initialize($("#subCategory"), results, false);
                },
                error: function () {
                    bootbox.alert('something went wrong on server');
                }
            });
        }
    });


    $('#subcat').on('change', 'select', function () {
        var obj = $("[id$='feeType']:visible");
        for (var i = 0; i < $("[id$='feeType']:visible").length - 1; i++) {
            for (var j = i + 1; j <= $("[id$='feeType']:visible").length - 1; j++) {
                if (obj[i].options[obj[i].selectedIndex].text == obj[j].options[obj[j].selectedIndex].text) {
                    var selected = obj[j];
                    bootbox.alert('The Fee Type  ' + selected.options[selected.selectedIndex].text + ' is already selected', function () {
                        $(selected).val('');
                    });
                }
            }
        }
    });

    $('#addrow').click(function () {
        var count = $("#subcat tbody  tr").length - 1;
        var $tableBody = $('#subcat').find("tbody"),
            $trLast = $tableBody.find("tr:last"),
            $trNew = $trLast.clone();
        if (!$.trim($trLast.find('select.feeType').val()) || !$.trim($trLast.find('select.rateType').val()) || !$.trim($trLast.find('select.uom').val())) {
            bootbox.alert('All values are mandatory !');
        } else {
            count++;
            $trNew.find("select").each(function () {
                $(this).attr({
                    'name': function (_, name) {
                        return name.replace(/\[.\]/g, '[' + count + ']');
                    },
                    'id': function (_, id) {
                        return id.replace(/\[.\]/g, '[' + count + ']');
                    }
                });
            });
            $trLast.after($trNew);
            $trNew.show().find('select').val('').removeAttr('disabled').addClass('dynamicInput');
            $trNew.find('input.markedForRemoval').val('false');
        }
    });

    $(document).on('click', '#deleterow', function () {
        var length = $('#subcat').find("tbody tr:visible").length;
        if (length == 1) {
            bootbox.alert('First row cannot be deleted!');
        } else {
            if ($(this).data('func')) {
                deleteandreplaceindexintable($(this));
            } else {
                if ($(this).closest('tr').find('select').hasClass('dynamicInput')) {
                    deleteandreplaceindexintable($(this));
                }
                else {
                    $(this).closest('tr').hide().find('input.markedForRemoval').val('true');
                }

            }

        }

    });

    $('#btnsubmit').click(function (e) {
        if ($('form').valid()) {

        } else {
            e.preventDefault();
        }
    })

});

function deleteandreplaceindexintable(obj) {
    obj.closest('tr').remove();
    var idx = 0;
    //regenerate index existing inputs in table row
    $("#subcat tbody tr").each(function () {
        $(this).find("select").each(function () {
            $(this).attr({
                'id': function (_, id) {
                    return id.replace(/\[.\]/g, '[' + idx + ']');
                },
                'name': function (_, name) {
                    return name.replace(/\[.\]/g, '[' + idx + ']');
                }
            });
        });
        idx++;
    });
}
function redirect(href) {
    window.location.href = href;
} 
