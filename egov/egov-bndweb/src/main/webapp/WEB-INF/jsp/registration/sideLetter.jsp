#-------------------------------------------------------------------------------
# /*******************************************************************************
#  *    eGov suite of products aim to improve the internal efficiency,transparency,
#  *    accountability and the service delivery of the government  organizations.
#  *
#  *     Copyright (C) <2015>  eGovernments Foundation
#  *
#  *     The updated version of eGov suite of products as by eGovernments Foundation
#  *     is available at http://www.egovernments.org
#  *
#  *     This program is free software: you can redistribute it and/or modify
#  *     it under the terms of the GNU General Public License as published by
#  *     the Free Software Foundation, either version 3 of the License, or
#  *     any later version.
#  *
#  *     This program is distributed in the hope that it will be useful,
#  *     but WITHOUT ANY WARRANTY; without even the implied warranty of
#  *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#  *     GNU General Public License for more details.
#  *
#  *     You should have received a copy of the GNU General Public License
#  *     along with this program. If not, see http://www.gnu.org/licenses/ or
#  *     http://www.gnu.org/licenses/gpl.html .
#  *
#  *     In addition to the terms of the GPL license to be adhered to in using this
#  *     program, the following additional terms are to be complied with:
#  *
#  * 	1) All versions of this program, verbatim or modified must carry this
#  * 	   Legal Notice.
#  *
#  * 	2) Any misrepresentation of the origin of the material is prohibited. It
#  * 	   is required that all modified versions of this material be marked in
#  * 	   reasonable ways as different from the original version.
#  *
#  * 	3) This license does not grant any rights to any user of the program
#  * 	   with regards to rights under trademark law for use of the trade names
#  * 	   or trademarks of eGovernments Foundation.
#  *
#  *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
#  ******************************************************************************/
#-------------------------------------------------------------------------------
 <%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
 
<script>

	function bodyonload(){
	    if(populate("mode").value=="view")
	     {
	   		for (var i =0; i < document.forms[0].length;i++) {
				if (document.forms[0].elements[i].name != 'close' && document.forms[0].elements[i].id != 'Printsideletterwithregistrationdetails' && document.forms[0].elements[i].id != 'Printsideletterwithoutregistrationdetails')
					document.forms[0].elements[i].disabled = true;
					
			}
	     }
	
	} 
	
	function populate(objName)
  {
     return document.getElementById(objName);
  }
  
	
	
	function validateForm(obj)
	{
	 return true;
	 
	}	
	
	
		function validateNames(obj)
	{	
		
	   if(obj.value)
	        warn(citizenlbl+" "+'<s:text name="firstName.required"/>');
	        return false;
	  
	    return true;
	}
	
	  function warn(msg)
	  {		 
	      dom.get("nonavailableform_error").style.display = '';
	      populate("nonavailableform_error").innerHTML = msg;
	      return false;		
	  }
	  	



	function checkSpecialCharacters(obj)
	{
		var iChars = "!@$%^*+=[']\\';{}|\"<>?#&(),";
		for (var i = 0; i < obj.value.length; i++)
		{
			if (iChars.indexOf(obj.value.charAt(i)) != -1)
			{
			   warn("Special characters are not allowed for Names")
			   obj.value="";
				return false;
			}					
	    }
	  		
		return true;
	}
	

function validateDate(){


	
		var applicationDate = populate("applicationDate").value;	
		if (applicationDate!="" )
		{
		    var date;
  	    var d = new Date();
		var curr_date = d.getDate();
		var curr_month = d.getMonth();
			curr_month++;
		var curr_year = d.getFullYear();
  	    date=curr_date+"/"+curr_month+"/"+curr_year; 	    
  	     if(compareDate(applicationDate,date)<0)
  	    {
  	    	
  	    	 warn("Application Date should be less than or equal to todays date");
  	    	 populate("applicationDate").value="";
  	    	 return false;
  	    }
		}
		return true;

}

 function printSideLetter(obj)
 {
  var temp =  populate('id').value;
 document.sideLetterForm.action="${pageContext.request.contextPath}/registration/sideLetter!printSideLetter.action?idTemp="+temp +"&sideLetterType="+obj;
  
 }
 
 
</script>
