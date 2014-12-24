// JavaScript Document

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




Array.prototype.contains = function (element) 
			{
				for (var i = 0; i < this.length; i++) {
				if (this[i] == element) {
				return true;
				}
				}
				return false;
			}
function checkduplicate(tableObj,obj,value,fieldNameDisplay,index,ifId,typeOfFeild)
{
        
        //value-field value
        //obj - field's obj
        //tableObj - table name
        //fieldNameDisplay - pass the name in which u want in alert
        //index-exact row count
        //ifId - Yes - can be used for dropDown,AutoComplete and No can be used for Text field
        //typeOfFeild - text will reset the value to empty String and otherValue will reset the value to 0 (for DropDown)
        //value check for empty string and if DropDown 0 and -1
     if(trimAll(obj.value)!='' && obj.value!='0' && obj.value!='-1')
	{
	   
        var indexObj = index;
      
    	var scripts = new Array();
   		var tbl= document.getElementById(tableObj);
   		var rowLength= tbl.rows.length;
		var row=getRow(obj);
		var firstName = getControlInBranch(row,obj.name);
		var firstNameVal = firstName.value;
		 
		 var rowIndex = row.rowIndex-indexObj;
		  var rowExactlength = rowLength-indexObj;
		 
		for(i=0;i<rowExactlength;i++)
    	{
    	   
    	   if(i!=(rowIndex))
    	     {
    	        if(ifId=='Yes')
    	        {
    	        	scripts.push(document.getElementsByName(obj.name)[i].value);
    	        }
    	        else
    	        {
    	            var value = document.getElementsByName(obj.name)[i].value;
    	            scripts.push(trimAll(value.toLowerCase().replace(/\s/g,"")));
    	        }
    	        
    	     }
    	  
    	     
    	}
		
		
		
		if(scripts.contains(firstNameVal))
		{
		    
		    if(typeOfFeild=='text')
		    {
		      alert('Please enter the unique '+fieldNameDisplay);
		      firstName.value = '';
		    }
		    else
		    {
		        alert('Please select the unique '+fieldNameDisplay);
				firstName.value = "0";
			}
			firstName.focus();
			return false;
		}
		}
}
function checkForPct(obj){
		var objt = obj;
	    var amt = obj.value;
	    if(amt != null && amt != "")
	    {
	      if(amt < 0 || amt >100)
	        {
	            alert("Please enter value (0-100) for the percentage");
	            obj.value="";
	            objt.focus();
	            return false;
	
	        }
	        if(isNaN(amt))
	        {
	            alert("Please enter a numeric value for the percentage");
	            obj.value="";
	            objt.focus();
	            return false;	
	        }	           
	    }
	}


function waterMarkTextIn(styleId,value)
{
	var txt=document.getElementById(styleId).value;
	if(txt==value)
	{
		document.getElementById(styleId).value='';
		document.getElementById(styleId).style.color='';
	}
}

function waterMarkTextOut(styleId,value)
{
	var txt=document.getElementById(styleId).value;
	if(txt=='')
	{
		document.getElementById(styleId).value=value;
		document.getElementById(styleId).style.color='DarkGray';
	}
}

function waterMarkInitialize(styleId,value)
{
	document.getElementById(styleId).value=value;
	document.getElementById(styleId).style.color='DarkGray';
}