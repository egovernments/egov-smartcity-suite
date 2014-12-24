
<%
String cityurl=(String)session.getAttribute("cityurl");
String cityname=(String)session.getAttribute("cityname");
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
         <a id="orgID" name="orgID"href="//www.egovernments.org"><img src="<c:url value='/images/egov-logo.gif'/>"  width="97" height="97"  id="logoID"> </a></td>
<td  height="102" width="1"> </td>
    </tr>
	
</table>



		</center>
	</div></div></div>
   </tr></td>
   </table>
  
   	<br><br>
<Center>