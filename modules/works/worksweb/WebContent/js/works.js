document.writeln('<link rel="stylesheet" href="http://code.jquery.com/ui/1.9.2/themes/base/jquery-ui.css" />');
document.writeln('<script src="http://code.jquery.com/jquery-1.8.3.js" ></sc'+'ript>');
document.writeln('<script src="http://code.jquery.com/ui/1.9.2/jquery-ui.js"></sc'+'ript>');

function ChangeColor(tableRow, highLight)
{
	if (highLight)
	{
	  tableRow.style.backgroundColor = '#dcfac9';
	}
	else
	{
	  tableRow.style.backgroundColor = 'white';
	}
}

var horizontal_offset="9px" //horizontal offset of hint box from anchor link
var vertical_offset="0" //horizontal offset of hint box from anchor link. No need to change.
var ie=document.all
var ns6=document.getElementById&&!document.all


function getposOffset(what, offsettype){
var totaloffset=(offsettype=="left")? what.offsetLeft : what.offsetTop;
var parentEl=what.offsetParent;
while (parentEl!=null){
totaloffset=(offsettype=="left")? totaloffset+parentEl.offsetLeft : totaloffset+parentEl.offsetTop;
parentEl=parentEl.offsetParent;
}
return totaloffset;
}

function iecompattest(){
return (document.compatMode && document.compatMode!="BackCompat")? document.documentElement : document.body
}

function clearbrowseredge(obj, whichedge){
var edgeoffset=(whichedge=="rightedge")? parseInt(horizontal_offset)*-1 : parseInt(vertical_offset)*-1
if (whichedge=="rightedge"){
var windowedge=ie && !window.opera? iecompattest().scrollLeft+iecompattest().clientWidth-30 : window.pageXOffset+window.innerWidth-40
dropmenuobj.contentmeasure=dropmenuobj.offsetWidth
if (windowedge-dropmenuobj.x < dropmenuobj.contentmeasure)
edgeoffset=dropmenuobj.contentmeasure+obj.offsetWidth+parseInt(horizontal_offset)
}
else{
var windowedge=ie && !window.opera? iecompattest().scrollTop+iecompattest().clientHeight-15 : window.pageYOffset+window.innerHeight-18
dropmenuobj.contentmeasure=dropmenuobj.offsetHeight
if (windowedge-dropmenuobj.y < dropmenuobj.contentmeasure)
edgeoffset=dropmenuobj.contentmeasure-obj.offsetHeight
}
return edgeoffset
}

function showhint(menucontents, obj, e, tipwidth){
if ((ie||ns6) && document.getElementById("hintbox")){
dropmenuobj=document.getElementById("hintbox")
dropmenuobj.innerHTML=menucontents
dropmenuobj.style.left=dropmenuobj.style.top=-500
if (tipwidth!=""){
dropmenuobj.widthobj=dropmenuobj.style
dropmenuobj.widthobj.width=tipwidth
}
dropmenuobj.x=getposOffset(obj, "left")
dropmenuobj.y=getposOffset(obj, "top")
dropmenuobj.style.left=dropmenuobj.x-clearbrowseredge(obj, "rightedge")+obj.offsetWidth+"px"
dropmenuobj.style.top=dropmenuobj.y-clearbrowseredge(obj, "bottomedge")+"px"
dropmenuobj.style.visibility="visible"
obj.onmouseout=hidetip
}
}

function hidetip(e){
dropmenuobj.style.visibility="hidden"
dropmenuobj.style.left="-500px"
}

function createhintbox(){
var divblock=document.createElement("div")
divblock.setAttribute("id", "hintbox")
document.body.appendChild(divblock)
}

if (window.addEventListener)
window.addEventListener("load", createhintbox, false)
else if (window.attachEvent)
window.attachEvent("onload", createhintbox)
else if (document.getElementById)
window.onload=createhintbox

//Document Upload Starts
function showDocumentManager()
{
    var v= dom.get("docNumber").value;
    var url;
    if(v==null||v==''||v=='To be assigned')
    {
      url="/egi/docmgmt/basicDocumentManager.action?moduleName=Works";
    }
    else
    {
      url = "/egi/docmgmt/basicDocumentManager!editDocument.action?docNumber="+v+"&moduleName=Works";
    }
    var wdth = 1000;
    var hght = 400;
    window.open(url,'docupload','width='+wdth+',height='+hght);
}

function viewDocumentManager()
{
   var v= dom.get("docNumber").value;
   if(v!='') {
   var url= "/egi/docmgmt/basicDocumentManager!viewDocument.action?docNumber="+v+"&moduleName=Works";
   var wdth = 1000;
    var hght = 400;
    window.open(url,'docupload','width='+wdth+',height='+hght);
}
   else { 
		alert("No Documents Found");
		return;
	}
}

function viewDocumentManager(docNumber)
{
   if(docNumber!='') {
		var url= "/egi/docmgmt/basicDocumentManager!viewDocument.action?docNumber="+docNumber+"&moduleName=Works";
	   var wdth = 1000;
	   var hght = 400;
	   window.open(url,'docupload','width='+wdth+',height='+hght);
   }
   else { 
		alert("No Documents Found");
		return;
	}
}

function setupDocNumberBeforeSave()
{
	   var v= dom.get("docNumber").value;
       if(v=='To be assigned')
       {
          dom.get("docNumber").value='';
       }
}
// Document Upload Ends

function confirmCancel(msg,objectNo) { 
	var ans=confirm(msg+": "+objectNo+" ?");
	if(ans) {
		return true;
	}
	else {
		return false;		
	}
}

function confirmCancel(msg) { 
	var ans=confirm(msg+"?");
	if(ans) {
		return true;
	}
	else {
		return false;		
	}
}

function confirmClose(msg) {  
	var ans=confirm(msg);	
	if(ans) {
		window.close();
	}
	else {
		return false;		
	}
}


function init()
{
	
	jQuery.noConflict();
     (function( jQuery ) {
        jQuery.widget( "ui.combobox", {
            _create: function() {
                var input,
                    that = this,
                    select = this.element.hide(),
                    selected = select.children( ":selected" ),
                    value = selected.val() ? selected.text() : "",
                    wrapper = this.wrapper = jQuery( "<span>" )
                        .addClass( "ui-combobox" )
                        .insertAfter( select );
 
                    function removeIfInvalid(element) {
                    	var value = jQuery( element ).val(),
                    	matcher = new RegExp( "^" + jQuery.ui.autocomplete.escapeRegex( value ) + "$", "i" ),
                    	valid = false;
                    	select.children( "option" ).each(function() {
                    		if ( jQuery( this ).text().match( matcher ) ) {
                    			this.selected = valid = true;
                    			return false;
                    		}
                    	});
                    	if ( !valid ) {
                    	// remove invalid value, as it didn't match anything
                    		jQuery( element )
                    		.val( "" )
                    		.attr( "title", value + "" );
                    		select.val( "" );
                    		input.data( "autocomplete" ).term = "";
                    		return false;
                    	}
                    }
                    input = jQuery( "<input>" )
                    .appendTo( wrapper )
                    .val( value )
                    .attr( "title", "" )
                    .addClass( "ui-state-default ui-combobox-input" )
                    .autocomplete({
                        delay: 0,
                        minLength: 0,
                        
                        source: function( request, response ) {
                            var matcher = new RegExp( jQuery.ui.autocomplete.escapeRegex(request.term), "i" );
                            response( select.children( "option" ).map(function() {
                                var text = jQuery( this ).text();
                                if ( this.value && ( !request.term || matcher.test(text) ) )
                                    return {
                                        label: text.replace(
                                            new RegExp(
                                                "(?![^&;]+;)(?!<[^<>]*)(" +
                                                jQuery.ui.autocomplete.escapeRegex(request.term) +
                                                ")(?![^<>]*>)(?![^&;]+;)", "gi"
                                            ), "<strong>$1</strong>" ),
                                        value: text,
                                        option: this
                                    };
                            }) );
                        },
                        select: function( event, ui ) {
                            ui.item.option.selected = true;
                            that._trigger( "selected", event, {
                                item: ui.item.option
                            });
                        },
                        change: function( event, ui ) {
                        	if ( !ui.item )
                        	return removeIfInvalid( this );
                        	}
                        	
                    })
                    .addClass( "ui-widget ui-widget-content ui-corner-left" );
 
                input.data( "autocomplete" )._renderItem = function( ul, item ) {
                    return jQuery( "<li>" )
                        .data( "item.autocomplete", item )
                        .append( "<a>" + item.label + "</a>" )
                        .appendTo( ul );
                };
 
                jQuery( "<a>" )
                    .attr( "tabIndex", -1 )                   
                    .appendTo( wrapper )
                    .button({
                        icons: {
                            primary: "ui-icon-triangle-1-s"
                        },
                        text: false
                    })
                    .removeClass( "ui-corner-all" )
                    .addClass( "ui-corner-right ui-combobox-toggle" )
                    .click(function() {
                        // close if already visible
                        if ( input.autocomplete( "widget" ).is( ":visible" ) ) {
                            input.autocomplete( "close" );
                            removeIfInvalid( input );
                            return;
                        }
 
                        jQuery( this ).blur(); 
                        // pass empty string as value to search for, displaying all results
                        input.autocomplete( "search", "" );
                        input.focus();
                    });
            },
             destroy: function() {
                this.wrapper.remove();
                this.element.show();
                jQuery.Widget.prototype.destroy.call( this );
            }
        });
    })( jQuery );
 
    jQuery(function() {
        jQuery( "#contractorId" ).combobox();
        jQuery( "#toggle" ).click(function() {
       	jQuery( "#contractorId" ).toggle();
     });
   });
}

