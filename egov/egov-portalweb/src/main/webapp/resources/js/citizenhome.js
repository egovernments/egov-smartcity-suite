jQuery(document).ready(function($)
{
	var unreadMessageCount = "${unreadMessageCount}";
	document.getElementById("unreadMessageCount").innerHTML=unreadMessageCount;
	//unreadMessageCount.initialize();
});

function refreshInbox(obj){
	alert('hi');
	$.ajax({
		url: "/portal/home/refreshInbox",
		type : "GET",
		data: {
			citizenInboxId : obj
		},
		success : function(response) {
            document.getElementById("unreadMessageCount").innerHTML=response;
        }
    });
}
