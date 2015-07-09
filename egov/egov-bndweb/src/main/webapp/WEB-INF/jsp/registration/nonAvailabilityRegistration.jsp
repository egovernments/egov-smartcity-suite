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
				if (document.forms[0].elements[i].name != 'close')
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
	    var curr_date = new Date();
	    var curr_year = curr_date.getFullYear(); 	    
		   if(document.getElementById('yearOfEvent')!=null){
		  var selectedYear=document.getElementById('yearOfEvent').value;
		
		if(selectedYear!="" && selectedYear!=null){
			if((selectedYear<1900) || (selectedYear>curr_year))
			{		
				warn('Not A Valid Year!! \nPlease Enter the year between 1900 And '+ curr_year);
				document.getElementById('yearOfEvent').value="";
				document.getElementById('yearOfEvent').focus();
				return false;
			 } 
         }
      }
      
      if(document.getElementById('no_Of_copies')!=null){
      if(document.getElementById('no_Of_copies').value>10){
      document.getElementById('no_Of_copies').value="";
	  document.getElementById('no_Of_copies').focus();
      warn('No of Copies cannot be greater than 10');
      return false;
      }else
      return true;
      }
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
	  	
   function checkdecimalval(obj)
   {
   populate("totalFee").value="";
	var objt = obj;
	var amt = obj.value;
	if(amt != null && amt != "")
	{
		if(amt < 0 )
		{
			warnwarn("Please enter positive value for the certificate fee");
			return false;

		}
		if(isNaN(amt))
		{
			warn("Please enter a numeric value for the certificate fee");
			return false;

		}
		calculateFee();
	     return true;
	}
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
	
	function validateyear(obj)
	{
	   if(isNaN(obj.value)){
			warn("Enter a valid year in YYYY format");
			 obj.value="";
			return false;
	     }
     
	    var iChars = "!@$%^*+=[']\\';{}|\"<>?#&(),.";
		for (var i = 0; i < obj.value.length; i++)
		{
			if (iChars.indexOf(obj.value.charAt(i)) != -1)
			{
			   warn("Enter a valid year in YYYY format")
			   obj.value="";
				return false;
			}					
	     }
	   	   	   
	  	if(obj.value.length!=4)	
	  	{
	  	warn("Enter a valid year in YYYY format");
		 obj.value="";
		return false;
		}
		
		return true;
	}
	
	
	function validatenoofcopy(obj){
	populate("totalFee").value="";
	  if(isNaN(obj.value)){
	  warn("Enter a valid number for number of copies");
			 obj.value="";
			return false;
	  }
	if (obj.value == Math.round(obj.value)){
	calculateFee();
	return true;
	}
	else
	{
	warn("Enter a valid number for number of copies");
	 obj.value="";
	return false;
	}
	}

	function calculateFee(){
	var total=0;
	var noofcopy=populate("no_Of_copies").value;
	var cost=populate("cost").value;
	if(noofcopy!=""&&cost!=""){
	total=noofcopy*cost;
	populate("totalFee").value=total;
	}
	
	}


</script>
