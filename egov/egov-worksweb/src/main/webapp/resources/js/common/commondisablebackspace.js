$('#new-pass').popover({
	trigger : "focus",
	placement : "bottom"
});
$(document).on("keydown", disableRefreshAndBack);

window.location.hash = "nbk";
window.location.hash = "Again-nbk";

window.onhashchange = function() {
	window.location.hash = "nbk";
}