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
    var applicationSource = $('#applicationSource').val();
    var isEmployee = $('#isEmployee').val();

    var agency = new Bloodhound({
        datumTokenizer: function (datum) {
            return Bloodhound.tokenizers.whitespace(datum.value);
        },
        queryTokenizer: Bloodhound.tokenizers.whitespace,
        remote: {
            url: '/adtax/agency/active-agencies?name=%QUERY',
            filter: function (data) {
                return $.map(data, function (ct) {
                    return {
                        name: ct.name,
                        value: ct.id
                    };
                });
            }
        }
    });

    agency.initialize(); // Instantiate the Typeahead UI

    /*$('.typeahead').typeahead({
          hint: true,
          highlight: true,
          minLength: 1
        }, {
        displayKey: 'name',
        source: agency.ttAdapter()
    }).on('typeahead:selected typeahead:autocompleted typeahead:matched', function(event, data){
        $("#agencyId").val(data.value);
   });*/
    if($('#nextAction').val() == "Commissioner approval pending"){
    	$('#taxAmount').attr("disabled", "disabled");
		$('#encroachmentFee').attr("disabled", "disabled");  	
    }
    var agency_typeahead = $('#agencyTypeAhead').typeahead({
        hint: true,
        highlight: true,
        minLength: 1
    }, {
        displayKey: 'name',
        source: agency.ttAdapter()
    });
    typeaheadWithEventsHandling(agency_typeahead, '#agencyId');


    $('.add-attachment').click(function () {
        console.log('came');
        $(this).parent().before('<div class="col-sm-3 add-margin"> <input type="file" class="form-control" required> </div>');
    });

    if (isEmployee == "true") {
        $('#measurement').change(function () {
            calculateTax();
        });
        $('#subCategory').change(function () {
            calculateTax();
        });
        $('#unitOfMeasure').change(function () {
            calculateTax();
        });
        $('#rateClass').change(function () {
            calculateTax();
        });
    }
    $('#propertyNumber').change(function () {
        callPropertyTaxRest();
    });

    $('#locality').change(function () {
        //	 alert('HI');

        var url;
        if (applicationSource == "online")
            url = "/egi/public/boundary/ajaxBoundary-blockByLocality";
        else
            url = "/egi/boundary/ajaxBoundary-blockByLocality";
        populateBoundaries(url);
    });
    $('#ward').change(function () {

        var url;
        if (applicationSource == "online")
            url = "/egi/public/boundary/ajaxBoundary-blockByWard.action";
        else
            url = "/egi/boundary/ajaxBoundary-blockByWard.action";


        populateBlock(url);
    });

    $('#category').change(function () {

        if (this.value === '') {
            return;
        } else {
            $.ajax({
                type: "GET",
                url: "/adtax/hoarding/subcategories",
                cache: true,
                dataType: "json",
                data: {'categoryId': this.value}
            }).done(function (value) {
                $('#subCategory option:gt(0)').remove();
                $.each(value, function (index, val) {
                    $('#subCategory').append($('<option>').text(val.description).attr('value', val.id));
                });
                if (subcategory !== '') {
                    $("select#subCategory").val(subcategory);
                    subcategory = '';
                }
            });
        }
    });

    $('#adminBoundryParent').change(function () {
        if (this.value === '') {
            return;
        } else {
            $.ajax({
                type: "GET",
                url: "/adtax/hoarding/child-boundaries",
                cache: true,
                dataType: "json",
                data: {'parentBoundaryId': this.value}
            }).done(function (value) {
                $('#ward option:gt(0)').remove();
                $.each(value, function (index, val) {
                    $('#ward').append($('<option>').text(val.name).attr('value', val.id));
                });
                if (ward !== '') {
                    $("select#ward").val(ward);
                    ward = '';
                }
            });
        }
    });

    $('#revenueBoundaryParent').change(function () {
        if (this.value === '') {
            return;
        } else {
            $.ajax({
                type: "GET",
                url: "/adtax/hoarding/child-boundaries",
                cache: true,
                dataType: "json",
                data: {'parentBoundaryId': this.value}
            }).done(function (value) {
                $('#locality option:gt(0)').remove();
                $.each(value, function (index, val) {
                    $('#locality').append($('<option>').text(val.name).attr('value', val.id));
                });
                if (locality !== '') {
                    $("select#locality").val(locality);
                    locality = '';
                }
            });
        }
    });

    $('input[type=file]').on('change.bs.fileinput', function (e) {
        EXIF.getData(e.target.files[0], function () {
            if (EXIF.getTag(this, "GPSLatitude")) {
                var imagelat = EXIF.getTag(this, "GPSLatitude");
                var imagelongt = EXIF.getTag(this, "GPSLongitude");
                var formatted_lat = format_lat_long(imagelat.toString());
                var formatted_long = format_lat_long(imagelongt.toString());
                var geocoder = new google.maps.Geocoder;
                geocoder.geocode({'location': {lat: formatted_lat, lng: formatted_long}}, function (results, status) {
                    if (status === 'OK') {
                        if (results[0]) {
                            $('#latitude').val(formatted_lat);
                            $('#longitude').val(formatted_long);
                        }
                    }
                });
            }
        });
    });

    function resetOnPropertyNumChange() {

        $('#ward').html("");
        $('#locality').val("");
        $('#block').html("");
        $('#street').html("");
        $('#address').val("");

    }


    function callPropertyTaxRest() {
        var propertyNo = jQuery("#propertyNumber").val();
        if (propertyNo != "" && propertyNo != null) {
            console.log(propertyNo);
            jQuery.ajax({
                //	url: "/ptis/rest/property/" + propertyNo,
                url: "/adtax/ajax-assessmentDetails",
                type: "GET",
                contentType: "application/json",
                dataType: "json",
                //data: JSON.stringify({"assessmentNoRequest":propertyNo}),
                data: {'assessmentNoRequest': propertyNo},
                success: function (data) {
                    if (data.errorDetails.errorCode != null && data.errorDetails.errorCode != '') {
                        alert(data.errorDetails.errorMessage);
                        $('#locality').val("");

                        document.getElementById("propertyNumber").value = "";
                        resetOnPropertyNumChange();
                    } else {
                        if (data.boundaryDetails != null) {

                            $('#ward').html("");
                            $('#block').html("");
                            $('#street').html("");
                            $('#address').val("");

                            $('#locality').val(data.boundaryDetails.localityId);
                            $('#ward').append("<option value='" + data.boundaryDetails.wardId + "'>" + data.boundaryDetails.wardName + "</option>");
                            $('#block').append("<option value='" + data.boundaryDetails.blockId + "'>" + data.boundaryDetails.blockName + "</option>");
                            if (data.boundaryDetails.streetId != null)
                                $('#street').append("<option value='" + data.boundaryDetails.streetId + "'>" + data.boundaryDetails.streetName + "</option>");

                            if (data.propertyAddress != null)
                                $('#address').val(data.propertyAddress);
                        }
                    }
                },
                error: function (e) {
                    console.log('error:' + e.message);
                    document.getElementById("propertyNumber").value = "";
                    resetOnPropertyNumChange();
                    alert("Error getting property details");
                }
            });
        } else {
            document.getElementById("propertyNumber").focus();
            resetOnPropertyNumChange();
        }
    }

    function populateBoundaries(url) {
        //alert('HI0000000000');
        console.log("came jursidiction" + $('#locality').val());
        $.ajax({
            type: "GET",
            url: url,
            cache: true,
            dataType: "json",
            data: {
                locality: $('#locality').val()
            }
        }).done(function (response) {

            $('#ward').html("");
            $('#block').html("");
            $('#street').html("");
            $.each(response.results.boundaries, function (j, boundary) {
                if (boundary.wardId) {
                    $('#ward').append("<option value='" + boundary.wardId + "'>" + boundary.wardName + "</option>");
                }
                $('#block').append("<option value='" + boundary.blockId + "'>" + boundary.blockName + "</option>");
            });
            $.each(response.results.streets, function (j, street) {
                $('#street').append("<option value='" + street.streetId + "'>" + street.streetName + "</option>");
            });
        })
            .fail(function (response1) {
                console.log("failed");
                $('#ward').html("");
                $('#block').html("");
                $('#street').html("");
                alert("No boundary details mapped for locality");
            });


    }


    function populateBlock(url) {
        $.ajax({
            type: "GET",
            url: url,
            cache: true,
            dataType: "json",
            data: {
                wardId: $('#ward').val()
            }
        }).done(function (response) {

            $('#block').html("");
            $.each(response, function (j, block) {
                $('#block').append("<option value='" + block.blockId + "'>" + block.blockName + "</option>");
            });
        })
            .fail(function (response1) {
                console.log("failed");
                $('#block').html("");
                alert("No block details mapped for ward");
            });

    }

    function format_lat_long(latorlong) {
        var loc_arry = latorlong.split(",");
        var degree = parseFloat(loc_arry[0]);
        var minutes = parseFloat(loc_arry[1]);
        var seconds = parseFloat(loc_arry[2]);
        var formatted = degree + ((minutes * 60) + seconds) / 3600;

        return formatted;
    }


    function calculateTax() {

        if ($('#rateClass').val() === '' || $('#unitOfMeasure').val() === '' || $('#subCategory').val() === '' || $('#measurement').val() === '') {
            return;
        } else {

            //	alert('All fields are entered');
            $.ajax({
                type: "GET",
                url: "/adtax/hoarding/calculateTaxAmount",
                cache: true,
                dataType: "json",
                data: {
                    'unitOfMeasureId': $('#unitOfMeasure').val(),
                    'measurement': $('#measurement').val(),
                    'subCategoryId': $('#subCategory').val(),
                    'rateClassId': $('#rateClass').val()
                }
            }).done(function (value) {
                if (value == 0)
                    $('#taxAmount').val('');
                else
                    $('#taxAmount').val(value);

            });
        }

    }

    $('#category').trigger('change');
    $('#locality').trigger('change');
    $('#revenueBoundryParent').trigger('change');

});

function DateValidation(startdate, enddate) {

	if (enddate <= startdate) {
		bootbox
				.alert("Permission end date should be greater than Permission Start Date");
		$('#permissionenddate').attr('style',
				"border-radius: 5px; border:#FF0000 1px solid;");
		return false;

	}else
		return true;

}


