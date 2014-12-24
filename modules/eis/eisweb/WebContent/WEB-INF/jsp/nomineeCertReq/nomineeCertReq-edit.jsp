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
	  <title>Modify Nominee Certification Required</title>
	  
	  <script language="JavaScript"  type="text/JavaScript">

		function onSubmit()
	  	{
			if(document.getElementById("certTypeIds").length!=0)
			{
				var certTypeIdsLength = document.getElementById("certTypeIds").length;			
				for (var i = 0; i<certTypeIdsLength; i++){				    
					document.getElementById("certTypeIds").options[i].selected=true;
				}
			}
			document.getElementById("relationType").disabled=false;
			
			return true;
	  	}
	  	
		function move(inputControl)
		{  
			var left = document.getElementById("certTypeIdLeft");  
			var right = document.getElementById("certTypeIds"); 
			var from, to;  
			var bAll = false;  
			switch (inputControl.value)  
			{  
				case '<':    
				from = right; 
				to = left;    
				break;  
				case '>':    
				from = left; 
				to = right;    
				break;  
			}  
			
			for (var i = from.length - 1; i >= 0; i--)  
			{    
				var o = from.options[i];    
				if (o.selected)    
				{      
					try      
					{        
						var clone = o.cloneNode(true);
						to.appendChild(clone);
					    //Standard method, fails in IE (6&7 at least)      
					}      
					catch (e)  
					{ 
					 to.add(o); 
					// IE only  
					} 
					from.remove(i);        
				}  
			}
		}
	</script>
   
  </head>
  
  <body>
  
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
								
								<s:push value="model">
									<tr>
										<td colspan="4" class="headingwk">
											<div class="arrowiconwk"><img src="../common/image/arrow.gif" /></div>
							              	<div class="headplacer"><s:text name="NomineeCertReq.Heading"/></div>
							        	</td>
							        </tr>
									
									<tr>
										<td class="whiteboxwk" ><span class="mandatory">*</span><s:text name="NomineeCertReq.RelationType"/> </td>
										<td class="whitebox2wk" colspan="3">
										<s:select name="relationType" id="relationType" list="dropdownData.eisRelationTypeList" listKey="id" 
										listValue="nomineeType" headerKey="-1" headerValue="----Select----"  disabled="true" value="%{relationType.id}"/> </td>
									</tr>
									
									<tr>
										<td  class="greyboxwk"><span class="mandatory">*</span><s:text name="NomineeCertReq.CertType"/> </td>
							   			<td class="greybox2wk">
											<s:select name="certTypeIdLeft" id="certTypeIdLeft" multiple="true" list="dropdownData.certReqListForLeftSelect" listKey="id" 
											listValue="type" value="%{certTypeIdLeft.id}" size="4" cssStyle="width:200px" />
										</td>
										<td align="center" class="greyboxwk" >				    		
											<input type="button" value='&gt;' style="width:50px" class="button" id="left" onclick="move(this);"/>
											<br>
											<s:textfield value="" cssStyle="width:50px;height:5px;border:0px" readonly="true" />
											<br>
											<input type="button" value='&lt' style="width:50px" class="button" id="right" onclick="move(this);"/>
										</td>
										<td class="greybox2wk">
											<s:select name="certTypeIds" id="certTypeIds" multiple="true" list="dropdownData.certReqListForRightSelect" listKey="id" 
											listValue="type"  size="4" cssStyle="width:200px" />
										</td>
								
									</tr>
								</s:push>
								  								
  								
  								
  								<tr>
						               	<td colspan="4" class="shadowwk"></td>
						            </tr>
									
									<tr>
									 	<td colspan="4" ><div align="right" class="mandatory">* Mandatory Fields</div></td>
									</tr>
						            
						           <tr>
								       <td align="center" colspan="2"> 
											<s:hidden  name="mode" value="Edit" />
									   </td>
						           </tr>
						      </tbody>
						</table>
					</div>
				</div>
			</div>
		</div>
		<div class="buttonholderwk">
			<s:submit method="edit" value="Update" cssClass="buttonfinal"/>
			<s:reset name="button" id="button" value="Cancel" onclick="resetForm()" cssClass="buttonfinal"/>
			<input type="button" value="Close" onclick="javascript:window.close()" class="buttonfinal"/> 
		</div>
	</s:form>
</body>
</html>	