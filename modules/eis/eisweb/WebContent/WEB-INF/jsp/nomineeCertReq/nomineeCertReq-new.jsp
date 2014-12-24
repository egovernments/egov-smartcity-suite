<!--
	Program Name : nomineeCertReq-new.jsp
	Author		: Jagadeesan M
	Created	on	: 17-03-2010
	Purpose 	: To create a nominee certification required.
 -->

<%@ page import="java.util.*"%>
<%@ include file="/includes/taglibs.jsp" %>
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<html>
  <head>
	  <title>Create Nominee Certification Required</title>
	  
	  <script type="text/javascript" src="<c:url value='/nomineeCertReq/nomineeCertReq.js'/>"></script>
	  <script type="text/javascript">
	  
	  function resetForm()
	  {
	  		if(document.getElementById("certTypeIds").length!=0)
			{
				var certTypeIdsLength = document.getElementById("certTypeIds").length;			
				for (var i = 0; i<certTypeIdsLength; i++){				    
					document.getElementById("certTypeIds").options[i].selected=true;
				}
				move(document.getElementById('right'));
			}
			
			document.getElementById('relationType').value ='-1';
			document.getElementById('msg').innerHTML ='';
	  }
	  	
	  </script>
   
  </head>
  
  <body onload="formOnLoad()">
  
   	<s:form action="nomineeCertReq" theme="simple" onsubmit="return onSubmit()">  
   		<s:token/>
  		
  		<div class="formmainbox">
			<div class="insidecontent">
		  		<div class="rbroundbox2">
					<div class="rbtop2">
						<div></div>
					</div>
			  		<div class="rbcontent2">
			  			<span id="msg"  style="height:1px">
							<s:actionerror cssStyle="font-size:10px;font-weight:bold;" cssClass="mandatory"/>  
							<s:fielderror cssStyle="font-size:10px;font-weight:bold;" cssClass="mandatory"/>
							<s:actionmessage cssClass="actionmessage"/>
						</span>

			  			<table width="96%" cellpadding ="0" cellspacing ="0" border = "0">
	  						<tbody>
								  								
  								<%@ include file='nomineeCertReq-form.jsp'%>
  								
  								    <tr>
						               	<td colspan="4" class="shadowwk"></td>
						            </tr>
									
									<tr>
									 	<td colspan="4" ><div align="right" class="mandatory">* Mandatory Fields</div></td>
									</tr>
						            
						           <tr>
								       <td align="center" colspan="2"> 
											<s:hidden  name="mode" value="Create" />
									   </td>
						           </tr>
						      </tbody>
						</table>
					</div>
				</div>
			</div>
		</div>
		<div class="buttonholderwk">
			<s:submit method="create" value="Save" cssClass="buttonfinal"/>
			<s:reset name="button" id="button" value="Cancel" onclick="resetForm()" cssClass="buttonfinal"/>
			<input type="button" value="Close" onclick="javascript:window.close()" class="buttonfinal"/> 
		</div>
	</s:form>
</body>
</html>	