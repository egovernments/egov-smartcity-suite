/*-------------------------------------------------------------------------------
# eGov suite of products aim to improve the internal efficiency,transparency,
# accountability and the service delivery of the government  organizations.
# 
# Copyright (C) <2015>  eGovernments Foundation
# 
# The updated version of eGov suite of products as by eGovernments Foundation
# is available at http://www.egovernments.org
# 
# This program is free software: you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation, either version 3 of the License, or
# any later version.
# 
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
# 
# You should have received a copy of the GNU General Public License
# along with this program. If not, see http://www.gnu.org/licenses/ or
# http://www.gnu.org/licenses/gpl.html .
# 
# In addition to the terms of the GPL license to be adhered to in using this
# program, the following additional terms are to be complied with:
# 
# 1) All versions of this program, verbatim or modified must carry this
#    Legal Notice.
# 
# 2) Any misrepresentation of the origin of the material is prohibited. It
#    is required that all modified versions of this material be marked in
#    reasonable ways as different from the original version.
# 
# 3) This license does not grant any rights to any user of the program
#    with regards to rights under trademark law for use of the trade names
#    or trademarks of eGovernments Foundation.
# 
# In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
#-------------------------------------------------------------------------------*/
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
