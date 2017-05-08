$(document).ready(function(){

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
    if(module == 'inbox'){
      $('.inbox-modules').show();
      $('.action-bar').addClass('hide');
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
  return new RegExp("^" + rule.split("*").join(".*") + "$").test(str);
}