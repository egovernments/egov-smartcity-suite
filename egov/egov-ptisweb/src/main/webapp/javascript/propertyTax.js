// JavaScript Document

function MM_openBrWindow(theURL,winName,features) { //v2.0
  window.open(theURL,winName,features);
}

function MM_goToURL() { //v3.0
  var i, args=MM_goToURL.arguments; document.MM_returnValue = false;
  for (i=0; i<(args.length-1); i+=2) eval(args[i]+".location='"+args[i+1]+"'");
}
function MM_showHideLayers() { //v9.0
  var i,p,v,obj,args=MM_showHideLayers.arguments;
  for (i=0; i<(args.length-2); i+=3) 
  with (document) if (getElementById && ((obj=getElementById(args[i]))!=null)) { v=args[i+2];
    if (obj.style) { obj=obj.style; v=(v=='show')?'visible':(v=='hide')?'hidden':v; }
    obj.visibility=v; }
}
function MM_callJS(jsStr) { //v2.0
  return eval(jsStr)
}
function show(x)
{
if(document.getElementById(x).style.display=='none')
document.getElementById(x).style.display='';
else
document.getElementById(x).style.display='none';
}


/* This javascript is used for auto tab feature. Automatically sets focus to the next form element when the current form element's 
maxlength has been reached. The user, then, does not have to manually click in or tab to the next field. Very easy to change for different
 size fields.  */ 
function autoTab(input,len, e) {
	var isNN = (navigator.appName.indexOf("Netscape")!=-1);
  var keyCode = (isNN) ? e.which : e.keyCode; 
  var filter = (isNN) ? [0,8,9] : [0,8,9,16,17,18,37,38,39,40,46];
  if(input.value.length >= len && !containsElement(filter,keyCode)) {
    input.value = input.value.slice(0, len);
    input.form[(getIndex(input)+1) % input.form.length].focus();
  }

  function containsElement(arr, ele) {
    var found = false, index = 0;
    while(!found && index < arr.length)
    if(arr[index] == ele)
    found = true;
    else
    index++;
    return found;
  }

  function getIndex(input) {
    var index = -1, i = 0, found = false;
    while (i < input.form.length && index == -1)
    if (input.form[i] == input)index = i;
    else i++;
    return index;
  }
  return true;
}

function populatePartNumbers(wardDropdownId) {
	populatepartNumbers({
		wardId : document.getElementById(wardDropdownId).value
	});			
}

function paintAlternateColorForRows() {
   jQuery("document").ready(function() {
   		jQuery("tr:even > td").addClass("greybox");
   		jQuery("tr:odd > td").addClass("bluebox");
   	});
}

function checkHouseNoStartsWithNo(field) {
	var pattern = /^[1-9].*$/; 
	
	if (!pattern.test(field.value)) {
		alert('House number should start with a number');
		field.value="";
		field.focus();
		return false;
	}
	
	return true;
}