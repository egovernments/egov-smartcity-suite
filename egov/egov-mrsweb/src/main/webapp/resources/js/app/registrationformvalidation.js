/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2016>  eGovernments Foundation
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

$(document).ready( function () {
$('.mobileno-field').blur( function () {
	 var mobileno = $(this).val();
		if (mobileno.length < 10) {
			bootbox.alert("Please enter 10 digit mobile number");
			$(this).val('');
		}
	});



/*$('#select-venue').change(function(){
	if (this.value =='Residence')
	{
		$('#txt-venuelabel').hide();
		$('#txt-venue').hide();
		
		
	}
	else {
		$('#txt-venuelabel').show();
		$('#txt-venue').show();
		

}
})*/
$( "#select-venue" ).change(function() {
	var venue = $( "#select-venue option:selected" ).text();
	if(venue == 'Residence'){
		$('#txt-placeofmrg').val('');
		$('.toggle-madatory').find("span").removeClass( "mandatory" );
		$('.addremoverequired').removeAttr( "required" );
		$("#txt-placeofmrg").attr("disabled", "disabled");
	}else{
		$('.toggle-madatory').find("span").addClass( "mandatory" );
		$('.addremoverequired').attr( "required", "true" );
		$("#txt-placeofmrg").removeAttr("disabled", "disabled");
	}
	
});


$('#txt-dateOfMarriage').datepicker()
    .blur('changeDate', function(e) {
    	
    	var str=$('#txt-dateOfMarriage').val().toString(); 
    	
    	if(str.match(/(^(((0[1-9]|1[0-9]|2[0-8])[\/](0[1-9]|1[012]))|((29|30|31)[\/](0[13578]|1[02]))|((29|30)[\/](0[4,6,9]|11)))[\/](19|[2-9][0-9])\d\d$)|(^29[\/]02[\/](19|[2-9][0-9])(00|04|08|12|16|20|24|28|32|36|40|44|48|52|56|60|64|68|72|76|80|84|88|92|96)$)/g))
    	{
    		showMarriageFee();
    	}
    	else{
    		bootbox.alert('Invalid date!');
    	}
});

$('.month-field').blur( function () {
	var month = parseInt( $(this).val() );
	if (month != null && month != undefined && (month < 0 || month > 12)) {
		bootbox.alert("Invalid month(s)..!!");
		$(this).val('');
	}
})

$('.month-field').blur( function () {
	var month = parseInt( $(this).val() );
	if (month != null && month != undefined && (month < 0 || month > 12)) {
		bootbox.alert("Invalid month(s)..!!");
		$(this).val('');
	}
})

$('input[id$="email"]').blur(function() {
		var pattern = new RegExp("^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$");
		var email = $(this).val();
		if (!pattern.test(email) && $(this).val().length > 0) {
			var span = $(this).siblings('span'); 
	    	$(span).addClass('error-msg');
	    	$(span).text('Please enter valid email..!');
			$(this).show();
			$(this).val("");
		} else {
			var span = $(this).siblings('span'); 
			$(span).removeClass('error-msg');
	    	$(span).text('');
		}
	});

$('a[id^="signature"]').click( function () {
		var content = $( $(this).siblings('input[type="hidden"]') ).val();
		var value = "data:image/jpeg;base64," + content ;
		var link = document.createElement('a');
		link.href = toBinaryString(value);
		link.download = 'signature.jpg';
		link.click();		
})

	$('#select-registrationunit').change( function () {
		showRegistrationUnit();
	})
	
	
/*	function showMarriageFeeCriteria()
{
			$.ajax({
				type: "GET",
				url: "/mrs/registration/calculatemrfee",
				cache: true,
				dataType: "json",
				data:{
					'dateOfMarriage' : $('#txt-dateOfMarriage').val()
					}
			}).done(function(value) {
				$("#select-marriagefees option").prop('selected', false).filter(function() {
				    return $(this).text() == value;
				}).prop('selected', true);
			});
		}*/
	
function showMarriageFee()
{
			$.ajax({
				type: "GET",
				url: "/mrs/registration/calculatemarriagefee",
				cache: true,
				dataType: "json",
				data:{
					'dateOfMarriage' : $('#txt-dateOfMarriage').val()
					},
				success : function(value) {
						if(value==1){
							bootbox.alert("Application will not be accepted");
							$('#txt-dateOfMarriage').val('')
							$('#txt-feepaid').val('');
						}
						else
							$('#txt-feepaid').val(value);
							
					},
				error:function(value) {
							bootbox.alert("Application will not be accepted beyond 90 days from the date of marriage");
							$('#txt-dateOfMarriage').val('')
							$('#txt-feepaid').val('');
						}
					});
	
}
	/*function showFee()
	   {
	    if ($('#select-marriagefees').val() === '') {
	    	$('#txt-feepaid').val('');
				return;
			} else {
			
				$.ajax({
					type: "GET",
					url: "/mrs/registration/calculatemarriagefee",
					cache: true,
					dataType: "json",
					data:{
						'feeId' : $('#select-marriagefees').val()
						}
				}).done(function(value) {
										if (value == 0){
											$('#txt-feepaid').val(value);
											$('#txt-feepaid').attr('readonly', true);
										}
										else{
											$('#txt-feepaid').val(value);
										$('#txt-feepaid').attr('readonly', false);
				}
				
				});
			}
		
	   }
	*/
	function showRegistrationUnit()
	{
	 if ($('#select-registrationunit').val() === '') {
	 	$('#txt-zone').val('');
				return;
			} else {
			
				$.ajax({
					type: "GET",
					url: "/mrs/registration/getmrregistrationunitzone",
					cache: true,
					dataType: "json",
					data:{
						'registrationUnitId' : $('#select-registrationunit').val()
						}
				}).done(function(value) {
										/*if (value == 0)
											$('#ttxt-zone').val('');
										else*/
					console.log(value);
					
					$('#txt-zoneid').val(value.id);
											$('#txt-zone').val(value.name);
				
				});
			}
		
	}
	
var age_obj={'husband':21,'wife':18,'witness':18};

$('.age-field').blur( function () {
	if(!$.trim($(this).val())){
		return false
	}else{
		var age = parseInt( $(this).val() );
		var name = $(this).data('names');
		var age_condition;
		$.each(age_obj, function(key,value){
			if(key == name){
				age_condition = value;
				return false;
			}
		});
		if(age < age_condition){
			bootbox.alert(name+ "'s age should be atleast "+age_condition+" years");
			$(this).val('');
		}
	}
	});

})
	
	
