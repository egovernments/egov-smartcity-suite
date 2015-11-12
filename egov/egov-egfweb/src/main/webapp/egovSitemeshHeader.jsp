<!--  #-------------------------------------------------------------------------------
# eGov suite of products aim to improve the internal efficiency,transparency, 
#      accountability and the service delivery of the government  organizations.
#   
#       Copyright (C) <2015>  eGovernments Foundation
#   
#       The updated version of eGov suite of products as by eGovernments Foundation 
#       is available at http://www.egovernments.org
#   
#       This program is free software: you can redistribute it and/or modify
#       it under the terms of the GNU General Public License as published by
#       the Free Software Foundation, either version 3 of the License, or
#       any later version.
#   
#       This program is distributed in the hope that it will be useful,
#       but WITHOUT ANY WARRANTY; without even the implied warranty of
#       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#       GNU General Public License for more details.
#   
#       You should have received a copy of the GNU General Public License
#       along with this program. If not, see http://www.gnu.org/licenses/ or 
#       http://www.gnu.org/licenses/gpl.html .
#   
#       In addition to the terms of the GPL license to be adhered to in using this
#       program, the following additional terms are to be complied with:
#   
#   	1) All versions of this program, verbatim or modified must carry this 
#   	   Legal Notice.
#   
#   	2) Any misrepresentation of the origin of the material is prohibited. It 
#   	   is required that all modified versions of this material be marked in 
#   	   reasonable ways as different from the original version.
#   
#   	3) This license does not grant any rights to any user of the program 
#   	   with regards to rights under trademark law for use of the trade names 
#   	   or trademarks of eGovernments Foundation.
#   
#     In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
#-------------------------------------------------------------------------------  -->
<%
String cityurl=(String)session.getAttribute("cityurl");
String cityname=(String)session.getAttribute("citymunicipalityname");
String citynamelocal=(String)session.getAttribute("citynamelocal");
if(cityurl==null)
cityurl="http://www."+cityname+".gov.in";


System.out.println("Hoooooooooooooooooooooooo");
System.out.println("cityurl"+cityurl);
System.out.println("cityname"+cityname);
System.out.println("citynamelocal"+citynamelocal);

%>
   
  

<table align="center" id="getAllDetails" name="getAllDetails" >
	    	<tr><td>
		<div id="main"><div id="m2"><div id="m3"> 
		<center>
		  
		<table align="center"  class="tableStyle" 	><!--class="tableStyle"-->
		          
		<tr><td colspan=4>&nbsp;</td></tr>
	
  
	<tr>
      <td  width="2"> </td>
      <td vAlign="center"  align="left" width="107">
          <p align="center">
          <a  id="imaID" name="imaID" href="<%=cityurl%>">
          <img src="<c:url value='../images/<%=cityname%>-logo.gif'/>" width="97" height="97" >
          </a>
      </td>
      <td  width="520" id="cityID" align="center">
        <font size="6" id="cityFontID" >City Administration </font>
      </td>
      <td align="middle"  width="124">
         <a id="orgID" name="orgID"href="//www.egovernments.org"><img src="<c:url value='/egi/resources/erp2/images/egov-logo.gif'/>"  width="97" height="97"  id="logoID"> </a></td>
<td  height="102" width="1"> </td>
    </tr>
	
</table>



		</center>
	</div></div></div>
   </tr></td>
   </table>
  
   	<br><br>
<Center>
