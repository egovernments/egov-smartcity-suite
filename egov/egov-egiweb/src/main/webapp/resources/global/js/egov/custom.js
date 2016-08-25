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
$(document).ready(function()
{
	// jQuery plugin to prevent double submission of forms
	jQuery.fn.preventDoubleSubmission = function() {
	  $(this).on('submit',function(e){
	    var $form = $(this);
	 
	    if ($form.data('submitted') === true) {
	      // Previously submitted - don't submit again
	      e.preventDefault();
	    } else {
	      // Mark it so that the next submit can be ignored
	      $form.data('submitted', true);
	    }
	  });
	 
	  // Keep chainability
	  return this;
	};
	
	$('form').preventDoubleSubmission();
	
	$(".is_valid_number").on("input", function(){
        var regexp = /[^0-9]/g;
		if($(this).val().match(regexp)){
			$(this).val( $(this).val().replace(regexp,'') );
		}
	});
	$(".is_valid_alphabetWithsplchar").on("input", function(){
		var regexp = /[^A-Z_-]*$/g;
		if($(this).val().match(regexp)){
			$(this).val( $(this).val().replace(regexp,'') );
		}
	});
	$(".is_valid_alphabet").on("input", function(){
		var regexp = /[^a-zA-Z ]/g;
		if($(this).val().match(regexp)){
			$(this).val( $(this).val().replace(regexp,'') );
		}
	});
	
	$(".is_valid_alphaNumWithsplchar").on("input", function(){
		var regexp =  /[^a-zA-Z0-9_@./#&+-]*$/;
		if($(this).val().match(regexp)){
			$(this).val( $(this).val().replace(regexp,'') );
		}
	});
	
	$(".is_valid_alphanumeric").on("input", function(){
		var regexp = /[^a-zA-Z _0-9]/g;
		if($(this).val().match(regexp)){
			$(this).val( $(this).val().replace(regexp,'') );
		}
	});
	$(".is_valid_letters_space_hyphen_underscore").on("input", function(){
        var regexp = /[^a-zA-Z _0-9_-]/g;
		if($(this).val().match(regexp)){
			$(this).val( $(this).val().replace(regexp,'') );
		}
	});
	try { $('.twitter-typeahead').css('display','block'); } catch(e){}
	
	try { $(":input").inputmask(); }catch(e){}
	
	try { 
		$(".datepicker").datepicker({
			format: "dd/mm/yyyy",
			autoclose: true 
		}); 

		var d = new Date();
		var currDate = d.getDate();
		var currMonth = d.getMonth();
		var currYear = d.getFullYear();
		var startDate = new Date(currYear,currMonth,currDate);
		$('.today').datepicker('setDate',startDate);

		}catch(e){
		//console.warn("No Date Picker");
	}
	
	try { 
		$('[data-toggle="tooltip"]').tooltip({
			'placement': 'bottom'
		});
		}catch(e){
		//console.warn("No tooltip");
	}
	
	$("a.open-popup").click(function(e) {
		window.open(this.href, ''+$(this).attr('data-strwindname')+'', 'width=900, height=700, top=300, left=260,scrollbars=yes'); 
		return false;
	});
	
	$(document).on('click', 'a.open-popup', function(e) {
		window.open(this.href, ''+$(this).attr('data-strwindname')+'', 'width=900, height=700, top=300, left=260,scrollbars=yes'); 
		return false;
	});
	
	$("form.form-horizontal[data-ajaxsubmit!='true']").submit(function( event ) {
		$('.loader-class').modal('show', {backdrop: 'static'});
	});
	
	//fade out success message
	$(".alert-success").fadeTo(2000, 500).slideUp(500, function(){
       		$(".alert-success").alert('close');
    	});

	var elements = document.querySelectorAll('input,select,textarea');

	for(var i = 0; i<elements.length; i++){
	    elements[i].addEventListener('invalid', function () {
    	off = (elements[0].offsetTop + 50);
    	$('html, body').animate({scrollTop: off }, 0);
	    });
	}
	
	try{
		jQuery.extend(jQuery.validator.messages, {
			required: "Required"
		});
	}catch(e){
		//console.warn("No validation involved");
	}

	$('form[data-ajaxsubmit="true"]').submit(function(e){
		
		var formData = new FormData($(this)[0]);
		var formAction = $(this).attr('action');

	    $.ajax({
	        url: 'https://api.github.com/users/mralexgray/repos',//formAction,
	        type: 'POST',
	        data: formData,
	        beforeSend: function() {
	        	//remove all existing alert messages
	        	$('.alert').remove();
	            //show loader
	        	$('.loader-class').modal('show', {backdrop: 'static'});
	        },
	        complete: function(){
	        	//callback function calling
	        	window["callBackAjax"]();
	        	//hide loader
	        	$('.loader-class').modal('hide');
	        	//scroll page to top
	        	pageScrollTop();
	        },
	        success: function (data) {
	            //append server response html text to current page
	        	$('.main-content').prepend(data);
	        },
	        error: function (xhr, ajaxOptions, thrownError) {
	        	//generic error message with error code
	            var errormsg = 'Error '+xhr.status+' '+ thrownError +'. please, try again!'
	            //add error alert in current page  
	            $('.main-content').prepend('<div id="notifyerror" class="alert alert-danger" role="alert"> <div> <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span> <span class="sr-only">Error:</span> '+ errormsg +' </div> ');	            
	        },
	        cache: false,
	        contentType: false,
	        processData: false
	    });

	    return false;
		
	});
	
});

function pageScrollTop()
{
    var body = $("html, body");
    body.stop().animate({scrollTop:0}, '500', 'swing', function() { 
       //bootbox.alert("Finished animating");
    });
}

function DateValidation(start , end){
    if (start != "" && end != "") {
		var stsplit = start.split("/");
		var ensplit = end.split("/");
		
		start = stsplit[1] + "/" + stsplit[0] + "/" + stsplit[2];
		end = ensplit[1] + "/" + ensplit[0] + "/" + ensplit[2];
		
		return ValidRange(start, end);
	}else{
		return true;
	}
}

function ValidRange(start, end) {
	var retvalue = false;
    var startDate = Date.parse(start);
    var endDate = Date.parse(end);
	
    // Check the date range, 86400000 is the number of milliseconds in one day
    var difference = (endDate - startDate) / (86400000 * 7);
    if (difference < 0) {
		bootbox.alert("Start date must come before the end date.");
		} else {
		retvalue = true;
	}
    return retvalue;
}

//Typeahead event handling
$.fn.getCursorPosition = function() {
    var el = $(this).get(0);
    var pos = 0;
    var posEnd = 0;
    if('selectionStart' in el) {
        pos = el.selectionStart;
        posEnd = el.selectionEnd;
    } else if('selection' in document) {
        el.focus();
        var Sel = document.selection.createRange();
        var SelLength = document.selection.createRange().text.length;
        Sel.moveStart('character', -el.value.length);
        pos = Sel.text.length - SelLength;
        posEnd = Sel.text.length;
    }
    return [pos, posEnd];
};

function typeaheadWithEventsHandling(typeaheadobj, hiddeneleid)
{
	  typeaheadobj.on('typeahead:selected', function(event, data){
		//setting hidden value
		$(hiddeneleid).val(data.value);    
	    }).on('keydown', this, function (event) {
	    	var e = event;
	    	
	    	var position = $(this).getCursorPosition();
	        var deleted = '';
	        var val = $(this).val();
	        if (e.which == 8) {
	            if (position[0] == position[1]) {
	                if (position[0] == 0)
	                    deleted = '';
	                else
	                    deleted = val.substr(position[0] - 1, 1);
	            }
	            else {
	                deleted = val.substring(position[0], position[1]);
	            }
	        }
	        else if (e.which == 46) {
	            var val = $(this).val();
	            if (position[0] == position[1]) {
	                
	                if (position[0] === val.length)
	                    deleted = '';
	                else
	                    deleted = val.substr(position[0], 1);
	            }
	            else {
	                deleted = val.substring(position[0], position[1]);
	            }
	        }
	        
	        if(deleted){ 
	        	$(hiddeneleid).val(''); 
        	}

        }).on('keypress', this, function (event) {
        	//getting charcode by independent browser
        	var evt = (evt) ? evt : event;
        	var charCode = (evt.which) ? evt.which : 
                ((evt.charCode) ? evt.charCode : 
                  ((evt.keyCode) ? evt.keyCode : 0));
        	//only characters keys condition
	    	if((charCode >= 32 && charCode <= 127)){
	    		//clearing input hidden value on keyup
	    	    $(hiddeneleid).val('');
	    	    $(dependentfield).empty();
	    	}
        }).on('focusout', this, function (event) { 
    	    //focus out clear textbox, when no values selected from suggestion list
    	    if(!$(hiddeneleid).val())
    	    {	
    	    	$(this).typeahead('val', '');
    	    }
       });
}

function typeaheadWithEventsHandling(typeaheadobj, hiddeneleid, dependentfield)
{
	  typeaheadobj.on('typeahead:selected', function(event, data){
		//setting hidden value
		$(hiddeneleid).val(data.value);    
	    }).on('keydown', this, function (event) {
	    	var e = event;
	    	
	    	var position = $(this).getCursorPosition();
	        var deleted = '';
	        var val = $(this).val();
	        if (e.which == 8) {
	            if (position[0] == position[1]) {
	                if (position[0] == 0)
	                    deleted = '';
	                else
	                    deleted = val.substr(position[0] - 1, 1);
	            }
	            else {
	                deleted = val.substring(position[0], position[1]);
	            }
	        }
	        else if (e.which == 46) {
	            var val = $(this).val();
	            if (position[0] == position[1]) {
	                
	                if (position[0] === val.length)
	                    deleted = '';
	                else
	                    deleted = val.substr(position[0], 1);
	            }
	            else {
	                deleted = val.substring(position[0], position[1]);
	            }
	        }
	        
	        if(deleted){ 
	        	$(hiddeneleid).val(''); 
	        	cleardependentfield(dependentfield);
        	}

        }).on('keypress', this, function (event) {
        	//getting charcode by independent browser
        	var evt = (evt) ? evt : event;
        	var charCode = (evt.which) ? evt.which : 
                ((evt.charCode) ? evt.charCode : 
                  ((evt.keyCode) ? evt.keyCode : 0));
        	//only characters keys condition
	    	if((charCode >= 32 && charCode <= 127)){
	    		//clearing input hidden value on keyup
	    	    $(hiddeneleid).val('');
	    	    cleardependentfield(dependentfield);
	    	}
        }).on('focusout', this, function (event) { 
    	    //focus out clear textbox, when no values selected from suggestion list
    	    if(!$(hiddeneleid).val())
    	    {	
    	    	$(this).typeahead('val', '');
    	    	cleardependentfield(dependentfield);
    	    }
       });
}

function cleardependentfield(dependentfield){
	console.log($(dependentfield).prop("type"));
	if($(dependentfield).prop("type") == 'select-one' || $(dependentfield).prop("type") == 'select-multiple'){
		$(dependentfield).empty();
	}else if($(dependentfield).prop("type") == 'text' || $(dependentfield).prop("type") == 'textarea'){
		$(dependentfield).val('');
	}
}

function disableRefreshAndBack(e) {
	var key = (e.which || e.keyCode);
	if (key == 116 || (key == 8 && !$(':focus').length))//F5 and Backspace
		e.preventDefault();
	if (e.ctrlKey)
		if (key == 82)
			e.preventDefault();
}
