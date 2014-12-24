<script>
/**
* function that Toggles the left frame.
* Dependency on eGov.jsp having a frameset called mainFrameset with cols = 200,*
* showMenu.gif and hideMenu.gif should be available under img
**/
  function frameToggle() {
       var mfr = window.top.document.getElementById('mainFrameset');
       if (mfr == null)
  	return;
       if (mfr.cols == "200,*") {
       	 mfr.cols="0,*";
       	 window.document.getElementById("menuToggle").src = "${pageContext.request.contextPath}/img/showMenu.gif";
       }
       else {
       	 mfr.cols="200,*";
       	 window.document.getElementById("menuToggle").src = "${pageContext.request.contextPath}/img/hideMenu.gif";
       }
   }

   

</script>

<div align="center">
<table align='center'>
<tr>
<td style="buffer:0pt; padding:0pt; margin:0pt">
<div id="paymain"><div id="paym2"><div id="m3">
<table cellSpacing="0"   border="0"  cellpadding="0">
  <tr>
      <td  align="left"  colSpan="5" height=1o>
		<table height=10>
			<%@ include file="/staff/egovImgandUrl.jsp" %>
		</table>
     </td>
    </tr>
</table>
</div></div></div>
</td>
</tr>
</table>
</div>
<div id="m3noBorder">

       

<table align="center" height="15" cellSpacing=0 cellPadding=0 width="100%" summary="" border=0>
  <tbody>
  <tr>
  	<td height=5 width="30%" align="left">
  		<img id="menuToggle" src="${pageContext.request.contextPath}/img/hideMenu.gif" alt="Click to toggle left menu bar" onClick="frameToggle();" />
  	</td>
        <td height=5 width="70%" align="right">
        
	<%
	   	if (request.getUserPrincipal() == null) //user has logged in
	   	{
	   	
        %>
          <a id="loginhref" name="loginhref" href="${pageContext.request.contextPath}/login/securityLogin.jsp"><font id="welcome_font">Log In</font></a>
          <%
    	}
    	else
    	
    	{
          %>
		   
         <font id="welcome_font">Logged in as:<%=EgovUtils.getPrincipalName(request.getUserPrincipal().getName()) %><span id="headerusername">
                     &nbsp;</span></font>
         &nbsp;&nbsp;<a id="loginhref" name="loginhref" href="#" onclick="javascript:top.location='/egi/logout.do'"><font id="welcome_font">Logout</font></a>
          <%
    	}

          %>
    </td></tr>
  </tbody>
</table>
</div>
<Center>
