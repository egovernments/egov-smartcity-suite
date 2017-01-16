$('#new-pass').popover({
	trigger : "focus",
	placement : "bottom"
});

$(document).on("keydown", function(event) {
	event.altKey.returnValue = false;
	disableRefreshAndBack();
});

window.location.hash = "nbk";
window.location.hash = "Again-nbk";

window.onhashchange = function() {
	window.location.hash = "nbk";
}
