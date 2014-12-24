// JavaScript Document
function waterMarkTextIn(obj, value) {
	if (obj.value == value) {
		obj.value = "";
		obj.style.color = "";
	}
}
function waterMarkTextOut(obj, value) {
	if (obj.value == "") {
		obj.value = value;
		obj.style.color = "darkgray";
	}
}
function waterMarkInitialize(obj, value) {
	obj.value = value;
	obj.style.color = "darkgray";
}
