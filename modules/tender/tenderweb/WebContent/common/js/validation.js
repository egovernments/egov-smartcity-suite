       
   var numb = '0123456789';
   var lwr = 'abcdefghijklmnopqrstuvwxyz';
   var upr = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ';

   function isValid(parm,val) 
   {
           if (parm == "") return false;
           for (i=0; i<parm.length; i++) 
           {
           if (val.indexOf(parm.charAt(i),0) == -1) return false;
           }
           return true;
   }
 
   function isNum(parm) {return isValid(parm,numb);}
   function isLower(parm) {return isValid(parm,lwr);}
   function isUpper(parm) {return isValid(parm,upr);}
   function isAlpha(parm) {return isValid(parm,lwr+upr);}
   function isAlphanum(parm) {return isValid(parm,lwr+upr+numb);}
           
           
   function IsNumeric(sText)
   {
           var ValidChars = "0123456789.";
           var IsNumber=true;
           var Char;
 
           for (i = 0; i < sText.length && IsNumber == true; i++) 
           { 
           Char = sText.charAt(i); 
           if (ValidChars.indexOf(Char) == -1) 
           {
           IsNumber = false;
           }
           }
           return IsNumber;
   }
           
   function validateNumeric(id,label)
   {
           var element=document.getElementById(id).value; 
           
           if(element!="")
           {
           if (!IsNumeric(element))
           {
           alert(label+" is a Numeric Field");
           document.getElementById(id).value="";
           document.getElementById(id).focus();
           return false;
           }
           else return true;
           }
   }
           
   function validateAlpha(id,label)
   {
           var element=document.getElementById(id).value;
           
           if (element!="")
           {
           if(!isAlpha(element))
           {
           alert(label+" can contain only letters");
           document.getElementById(id).value="";
           document.getElementById(id).focus();
           return;
           }
           }
   }
           
   function validateAlphaNum(id,label)
   {
           var element=document.getElementById(id).value;
           
           if (element!="")
           {
           if(!isAlphanum(element))
           {
           alert(label+" can contain only letters & numbers");
           document.getElementById(id).value="";
           document.getElementById(id).focus();
           return;
           }
           }
   }
   function trimAll(strValue )
     {
         var objRegExp = /^(\s*)$/;
         // alert("strValue"+strValue);
   
         //check for all spaces
         if(objRegExp.test(strValue))
         {
            strValue = strValue.replace(objRegExp, '');
           // alert("strValue-------"+strValue);
            if( strValue.length == 0)
               return strValue;
         }
   
         //check for leading & trailing spaces
         objRegExp = /^(\s*)([\W\w]*)(\b\s*$)/;
         if(objRegExp.test(strValue))
         {
            //remove leading and trailing whitespace characters
            strValue = strValue.replace(objRegExp, '$2');
         }
         return strValue;
 }
 function refreshInbox()
  {

  if(opener.top.document.getElementById('inboxframe')!=null)
  {
  	if(opener.top.document.getElementById('inboxframe').contentWindow.name!=null && opener.top.document.getElementById('inboxframe').contentWindow.name=="inboxframe")
  		{ 
  		 opener.top.document.getElementById('inboxframe').contentWindow.egovInbox.refresh();
  		}
  	}
 }


 
	function disableEnterKey(e)
	{
	     var key;

	     if(window.event)
	          key = window.event.keyCode;     //IE
	     else
	          key = e.which;     //firefox

	     if(key == 13)
	          return false;
	     else
	          return true;
	}