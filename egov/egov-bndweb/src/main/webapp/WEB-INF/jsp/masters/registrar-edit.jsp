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
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib prefix="sx" uri="/WEB-INF/struts-dojo-tags.tld"%>

<html>
<head>
<title>
         <s:if test="%{mode!='view'}">
			 <s:text name="modify.registrar"/>
		</s:if>
		<s:else>
	    	<s:text name="view.registrar"></s:text> 
	    </s:else>

</title>
     <sx:head/>
<SCRIPT type="text/javascript">
    
     var ESTABLISHMENTID = -1;
      var REGISTRATIONUNITID =-1;
      var REGISTRATIONNAME ;
       var ESTABLISHMENTNAME ;
    
	function init()
	{		
	  	if(document.getElementById('establishment').value==null || document.getElementById('establishment').value=="" )
			dom.get("hospitallist").style.display='none';		
		
		if(document.getElementById('mode').value=='view')
		{
			for(var i=0;i<document.forms[0].length;i++)
			{
				if( document.forms[0].elements[i].name!='close')
				document.forms[0].elements[i].disabled =true;
			}
		}
	}
	
	function enableHospital()
    {
    	var role = document.getElementById("role").options[document.getElementById("role").selectedIndex].text;
   	 	if((role != "" && role != null) && (role == "HospitalRegistrar" || role == "HospitalUser"))
     	{
      			
      		dom.get("hospitallist").style.display= 'block';
   		}
   		else
   		{
   		    	 	 dom.get("hospitallist").style.display='none';
   		}      
   		 
	}
      
	function populateRole()
    {
        
       	document.getElementById("role").value ="";
       	document.getElementById("establishment").value ="";
      	dom.get("hospitallist").style.display='none';
       	var userId = document.getElementById('userId').value; 
        populaterole({userId :  document.getElementById('userId').value});
    }
    
    
    	function validate()
	  {	 
	   var returnFlag = true;
		var role = document.getElementById("role").options[document.getElementById("role").selectedIndex].text;
   	  	if((role != "" && role != null) && (role == "HospitalRegistrar" || role == "HospitalUser"))
		{
			if(document.getElementById("establishment").value =="" ||document.getElementById("establishment").value == null)
			{
	 	   		dom.get("registrar_error").style.display = '';
	  	  		document.getElementById("registrar_error").innerHTML = '<s:text name="establishment.required" />';
        	    return false;
			}
	  	} 
	  	 returnFlag = compareWithOldValue();
	    return returnFlag;	    
	 }
	 
	 
	 function populateRegistrarDetails()
	 {
	 
	  var userId  = document.getElementById('userId').value;
	
	  var url = "${pageContext.request.contextPath}/common/ajaxCommon!populateRegistrarDetails.action?userId="+userId;
	  var req = initiateRequest();
	  	 	req.open("GET", url, false);
			req.send(null);
	  	 	if (req.readyState == 4)
	  		{
	  		 
	  		    if (req.status == 200)
	  		    {
	  		          
	  			    	var responseString =req.responseText;
	  			    	var result = responseString.split("+");
	  			    	
	  			    	if(result[0]!="" && result[0]!=0)
	  			    	{
	  			    	    document.getElementById("regUnitId").value = result[0];
	  			    	    REGISTRATIONUNITID = document.getElementById("regUnitId").value ;
	  			    	    REGISTRATIONNAME = document.getElementById("regUnitId").options[document.getElementById("regUnitId").selectedIndex].text;
	  			    	   
	  			    	    
	  			    	 }
	  			   
	  			    	if(result[1]!="" && result[1]!=0) 
	  			    	{
	  			    	   document.getElementById("establishment").value = result[1];
	  			    	   ESTABLISHMENTID =  document.getElementById("establishment").value ;
	  			    	   ESTABLISHMENTNAME = document.getElementById("establishment").options[document.getElementById("establishment").selectedIndex].text;
	  			    	}
	  			}
	  			
	  		}
      
    
	 }
	 
	  function compareWithOldValue()
	  {
	   
	    var changedString ="";
	    var flag = true ;
	    var role = document.getElementById("role").options[document.getElementById("role").selectedIndex].text;
         if(REGISTRATIONUNITID != -1 && REGISTRATIONUNITID != document.getElementById("regUnitId").value && document.getElementById("regUnitId").options[document.getElementById("regUnitId").selectedIndex].value != "" )
         {
           changedString = '<s:text name="registration.unit"/>'+" is changed from "+REGISTRATIONNAME+ " to "+document.getElementById("regUnitId").options[document.getElementById("regUnitId").selectedIndex].text+"'\n"; 
         }
         
         if((role != "" && role != null) && (role == "HospitalRegistrar" || role == "HospitalUser"))
         {
         if(ESTABLISHMENTID != -1 && ESTABLISHMENTID != document.getElementById("establishment").value &&  document.getElementById("establishment").options[document.getElementById("establishment").selectedIndex].value != "")
         {
        changedString = changedString+'<s:text name="hospital.name"/>'+" is changed from "+ESTABLISHMENTNAME+ " to "+document.getElementById("establishment").options[document.getElementById("establishment").selectedIndex].text+"'\n";   
         }
         }
          if(changedString!=""){
	  	          alert(changedString);
				flag = confirm("Do you want to Continue ?");
				  
			} 
	   return flag;
	  }
   function populateHospitalName()
    {
          var roleName = document.getElementById("role").options[document.getElementById("role").selectedIndex].text;
    	   populateestablishment({role:roleName,regUnit:document.getElementById('regUnitId').value});
    }
    </script>
</head>
<body onload="init();">
<div class="errorstyle" id="registrar_error" style="display: none;">
	</div>
	
	
	<s:if test="%{hasErrors()}">
		<div class="errorstyle">
			<s:actionerror />	
			<s:fielderror />
		</div>
	</s:if>
	<s:if test="%{hasActionMessages()}">
		<div class="errorstyle">
			<s:actionmessage />
		</div>
	</s:if>
 <s:form action="registrar" theme="css_xhtml" name="registrar" validate="true" >
 <div class="formheading"/></div>
   <s:token/>
    
    <s:hidden id="mode" name="mode" value="%{mode}"></s:hidden>
    	
     <table width="100%" cellpadding="0" cellspacing="0" border="0">
       
      
      
        <tr>
           <td class="greybox" width="35%" > &nbsp;</td>
           <td class="greybox" width="15%">
               <s:text name="user.name"/>
               <span class="mandatory">* </span>
           </td>
            	<td class="greybox">
				<s:select name="userId"
					id="userId" list="dropdownData.userNameList"
					value="%{userId.id}" listKey="id" listValue="userName"
					headerKey="" headerValue="---choose---" onchange="populateRole(); populateRegistrarDetails();"/>
					<egov:ajaxdropdown id="role"
					fields="['Text','Value']" dropdownId="role"
					url="common/ajaxCommon!getRoleNamesByUserID.action"   />	
				
			</td>	
			
			
         </tr>
            <s:if test="%{mode!='view'}">
         <tr>
		           <td class="bluebox" width="35%" > &nbsp;</td>
		           <td class="bluebox" width="15%">
		               <s:text name="role"/>
		               <span class="mandatory">* </span>
		           </td>
		           <td class="bluebox">
						<s:select name="role"
							id="role" list="dropdownData.roleList"
							value="%{role.id}" listKey="id" listValue="roleName"
							headerKey="" headerValue="---choose---"  onchange="populateHospitalName(); enableHospital();"/>		
						<egov:ajaxdropdown id="establishment" fields="['Text','Value']" dropdownId="establishment" url="common/ajaxCommon!getHospitalNameByRole.action"  />				
				   </td>
	           	</tr>
	           	</s:if>
	          <s:elseif test="%{mode=='view'}">
			<tr>
             	<div id="rolelistdiv">
		        <table width="40%" cellpadding="0" cellspacing="0" border="0" align="center">
		        	<tr>
						<th  class="bluebgheadtd" width="3%"><div align="center"> Srl.No.</div></th>
						<th  class="bluebgheadtd" width="5%"><div align="center"> Role </div></th>
					   
				    </tr>
				    
		        	<s:iterator id="roleList" status="row_status" value="roleList">
		        	<tr>
		        		<td class="blueborderfortd" width="3%"><s:property value="%{#row_status.count}"  /></td>			    
					 	<td class="blueborderfortd" width="5%"><s:property value="%{roleName}" /></td>					 				 	
		        	</tr>
		        	</s:iterator>        
        		</table>
        		</div>
        	</tr>
           </s:elseif>  
           </table>
            	<table width="100%" cellpadding="0" cellspacing="0" border="0">  	
            <tr>
           <td class="greybox" width="35%" > &nbsp;</td>
           <td class="greybox" width="15%">
               <s:text name="registration.unit"/>
               <span class="mandatory">* </span>
           </td>       
           <td class="greybox">
				<s:select name="regUnitId"
					id="regUnitId" list="dropdownData.registrationUnitList"
					value="%{regUnitId.id}" listKey="id" listValue="regUnitDesc"
					headerKey="" headerValue="---choose---" />
			</td>					
        </tr>
           
           
         </table>
         
          
         <div id="hospitallist">
        	<table width="100%" cellpadding="0" cellspacing="0" border="0">
           		<tr>
           			<td class="bluebox" width="35%" > &nbsp;</td>
           			<td class="bluebox" width="15%">
	               		<s:text name="hospital.name"/>
	               		<span class="mandatory">* </span>
           			</td>
            		<td class="bluebox">
						<s:select name="establishment"
							id="establishment" list="dropdownData.establishmentList"
							value="%{establishment.id}" listKey="id" listValue="name"
							headerKey="" headerValue="---choose---" />
           			</td>
           		</tr>
			</table>
		</div> 
		
		<div class="buttonbottom" align="center">
			<table>
				<tr>
					<s:if test="%{mode!='view'}">
						<td>
							<s:submit cssClass="buttonsubmit" id="save" name="save" value="Save"  method="edit" onclick="return validate();" />
						</td>
					</s:if>	
				    <td><input type="button" class="button" id="close" name="close" value="Close" onclick="window.close();" ></td>					
				</tr>
			</table>
		</div>
		  
       <br>
			
		<div align="center">
				<font color="red"><s:text name="warning.lbl" /> </font>
		</div>
       
</s:form>		  
</body>

</html>
