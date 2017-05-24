$(document).ready(function(){
	$('.password-error').hide();
	$('.totalServicesAppliedHide').hide();
	$('.totalServicesCompletedHide').hide();
	$('#servicesCmpletedDiv').attr('style', 'opacity: 0.7;cursor: pointer');
	$('#totalServicesAppliedDiv').attr('style', 'opacity: 0.7;cursor: pointer');
	$('#totalServicesAppliedDiv').click(function() {
		$('.servicesUnderScrutinyHide').hide();
		$('.totalServicesCompletedHide').hide();
		$('.totalServicesAppliedHide').show();
		$('#totalServicesAppliedDiv').attr('style', 'opacity: 1;cursor: pointer');
		$('#servicesUnderScrutinyDiv').attr('style', 'opacity: 0.7;cursor: pointer');
		$('#servicesCmpletedDiv').attr('style', 'opacity: 0.7;cursor: pointer');
	});
	
	$('#servicesUnderScrutinyDiv').click(function() {
		$('.totalServicesAppliedHide').hide();
		$('.totalServicesCompletedHide').hide();
		$('.servicesUnderScrutinyHide').show();
		$('#servicesUnderScrutinyDiv').attr('style', 'opacity: 1;cursor: pointer');
		$('#servicesCmpletedDiv').attr('style', 'opacity: 0.7;cursor: pointer');
		$('#totalServicesAppliedDiv').attr('style', 'opacity: 0.7;cursor: pointer');
	});
	
	$('#servicesCmpletedDiv').click(function() {
		$('.totalServicesAppliedHide').hide();
		$('.servicesUnderScrutinyHide').hide();
		$('.totalServicesCompletedHide').show();
		$('#servicesCmpletedDiv').attr('style', 'opacity: 1;cursor: pointer');
		$('#totalServicesAppliedDiv').attr('style', 'opacity: 0.7;cursor: pointer');
		$('#servicesUnderScrutinyDiv').attr('style', 'opacity: 0.7;cursor: pointer');

	});
	
  var module;

  $('.services .content').matchHeight();
  
  leftmenuheight();
  rightcontentheight();

  //alert(matchRuleShort("bird123", "bird*"))

  $('#search').keyup(function(e){
    var rule = '*'+$(this).val()+'*';
    if(e.keyCode == 8){
      $('[data-services="'+module+'"]').show();
    }
    $(".services-item .services:visible").each(function(){
      var testStr = $(this).find('.content').html().toLowerCase();
      console.log(testStr, rule, matchRuleShort(testStr, rule))
      if(matchRuleShort(testStr, rule))
        $(this).show();
      else
        $(this).hide();
    });
  });

  $('.modules-li').click(function(){
    $('#search').val('');
    $('.modules-li').removeClass('active');
    $(this).addClass('active');
    module = $(this).data('module');
    $('.services-item .services').hide();
    $('#showServiceGroup').hide();
    if(module == 'home'){
      $('.inbox-modules').show();
      $('.action-bar').addClass('hide');
      $('#showServiceGroup').show();
    }
    else{
      $('.inbox-modules').hide();
      $('[data-services="'+module+'"]').show();
      $('.action-bar').removeClass('hide');
    }
  })

  $( window ).resize(function() {
    leftmenuheight();
    rightcontentheight();
  });

  $('table tbody tr').click(function(){
    $('#myModal').modal('show');
  });
  

  $('.checkpassword').blur(function(){
  	if(($('#new-pass').val()!="") && ($('#retype-pass').val()!=""))
  	{
  		if ($('#new-pass').val() === $('#retype-pass').val()) {
  			
  			}else{
  			$('.password-error').show();
  			$('#retype-pass').addClass('error');
  		}
  	}
  });

  $('#passwordForm').on('submit', function(e){
         e.preventDefault();
         $.ajax({
             url: '/egi/home/password/update',
             type: 'GET',
             data: {'currentPwd': $("#old-pass").val(), 'newPwd':$("#new-pass").val(),'retypeNewPwd':$("#retype-pass").val()},
             success: function(data) {
             	var msg = "";
             	if (data == "SUCCESS") {
             		msg = "Your password has been updated."
             	} else if (data == "NEWPWD_UNMATCH") {
             		msg = "New password you have entered does not match with retyped password."
             		$('#retype-pass').val('');
             		$('#new-pass').val('');
             		$('#old-pass').val('');
             		$('.password-error').hide();
             	} else if (data == "CURRPWD_UNMATCH") {
             		msg = "Old password you have entered is incorrect."
             		$('#retype-pass').val('');
             		$('#new-pass').val('');
             		$('#old-pass').val('');
             		$('.password-error').hide();
             	} 
             	bootbox.alert(msg);
             },
             error: function() {
             	bootbox.alert("Internal server error occurred, please try after sometime.");
             }, complete : function() {
             	$('.change-password, .loader-class').modal('hide');
             }
     }); 
  });
  
  $('#serviceGroup').change(function(){
	  var selected = $(this).val();
	  var total = $( "#totalServicesAppliedSize" ).html().trim();
	  var length = document.getElementsByClassName($(this).val()).length / 2;
	  if($(this).val() == "") {
		  $('.showAll').show();
		  $( "#totalServicesAppliedSize" ).html($( "#tabelPortal tbody.totalServicesAppliedHide tr.showAll" ).length);
		  $( "#totalServicesCompletedSize" ).html($( "#tabelPortal tbody.totalServicesCompletedHide tr.showAll" ).length);
		  $( "#totalServicesPendingSize" ).html($( "#tabelPortal tbody.servicesUnderScrutinyHide tr.showAll" ).length);
		  var showAllClass ="#tabelPortal tbody.servicesUnderScrutinyHide tr.showAll td:first-child";
		  generateSno(showAllClass);

	  } else {
		  $('.showAll').hide();
		  $('.'+$(this).val()).show();
		  $( "#totalServicesAppliedSize" ).html($( "#tabelPortal tbody.totalServicesAppliedHide tr."+$(this).val() ).length);
		  $( "#totalServicesCompletedSize" ).html($( "#tabelPortal tbody.totalServicesCompletedHide tr."+$(this).val() ).length);
		  $( "#totalServicesPendingSize" ).html($( "#tabelPortal tbody.servicesUnderScrutinyHide tr."+$(this).val() ).length);
		  
		  var servicesUnderScrutinyHideClass ="#tabelPortal tbody.servicesUnderScrutinyHide tr."+ selected + " td:first-child";
		  var totalServicesAppliedHideClass="#tabelPortal tbody.totalServicesAppliedHide tr."+ selected + " td:first-child";
		  var totalServicesCompletedHideClass="#tabelPortal tbody.totalServicesCompletedHide tr."+ selected + " td:first-child";
		  generateSno(servicesUnderScrutinyHideClass);
		  generateSno(totalServicesAppliedHideClass);
		  generateSno(totalServicesCompletedHideClass);
	  }
  });
  
  
  

});

function leftmenuheight(){
  //console.log($( window ).height(), $('.modules-ul').height());
  $('.left-menu,.modules-ul').css({
    height:$( window ).height(),
    overflow : 'auto'
  });
}

function rightcontentheight(){
  //console.log($( window ).height(), $('.right-content').height());
  $('.right-content').css({
    height:$( window ).height(),
    overflow : 'auto'
  })
}

//Short code
function matchRuleShort(str, rule) {
  return new RegExp("^" + rule.toLowerCase().split("*").join(".*") + "$").test(str.toLowerCase());
}

var url;
function openPopUp(url) {
	window.open(url, '', 'height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
}

function generateSno(className)
{
	var idx=1;
	$(className).each(function(){
		$(this).text(idx);
		idx++;
	});
}