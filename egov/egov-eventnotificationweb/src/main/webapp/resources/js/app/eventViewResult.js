$(document).ready(function(){
	$(".btn-primary").click(function(event){
		window.open("/eventnotification/event/update/"+$("#eventId").val(),"_blank", "width=800, height=700, scrollbars=yes");
	});
});