<!--
	Program Name : cpf-CreateModify.jsp
	Author		: jagadeesan
	Created	on	: 09-10-2009
	Purpose 	: Contribution of Provident Fund
 -->
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ page language="java"%>
<%@ taglib uri="/tags/struts-bean" prefix="bean"%>
<%@ taglib uri="/tags/struts-html" prefix="html"%>
<%@ taglib uri="/tags/struts-logic" prefix="logic"%>
<%@ taglib uri="/tags/struts-nested" prefix="nested"%>
<%@ page import="java.util.*"%>

<html>  
	<head>  
	    <title>CPF Setup</title>
		
		
		<script type="text/JavaScript" src="${pageContext.request.contextPath}/javascript/calender.js"></script>
		<script language="JavaScript" src="${pageContext.request.contextPath}/dhtml.js" type="text/JavaScript"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/validations.js"	type="text/javascript"></SCRIPT>
		<script language="JavaScript" src="${pageContext.request.contextPath}/javascript/SASvalidation.js" type="text/JavaScript"></script>
		<script language="JavaScript" src="${pageContext.request.contextPath}/javascript/dateValidation.js" type="text/JavaScript"></script>
		<script language="JavaScript" src="${pageContext.request.contextPath}/script/jsCommonMethods.js" type="text/JavaScript"></script>

		<script type="text/javascript" src="${pageContext.request.contextPath}/commonyui/build/yahoo/yahoo.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/commonyui/build/dom/dom.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/commonyui/build/autocomplete/autocomplete-debug.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/commonyui/build/animation/animation.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/commonyui/build/event/event-debug.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/commonyui/build/animation/animation.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/commonjs/ajaxCommonFunctions.js"></Script>	

		
		<style type="text/css">
				#codescontainer {position:absolute;left:11em;width:9%}
				#codescontainer .yui-ac-content {position:absolute;width:80%;border:1px solid #404040;background:#fff;overflow:hidden;z-index:9050;}
				#codescontainer .yui-ac-shadow {position:absolute;margin:.3em;width:40%;background:#a0a0a0;z-index:9049;}
				#codescontainer ul {padding:5px 0;width:80%;}
				#codescontainer li {padding:0 5px;cursor:default;white-space:nowrap;}
				#codescontainer li.yui-ac-highlight {background:#ff0;}
				#codescontainer li.yui-ac-prehighlight {background:#FFFFCC;}
		</style>
		
		<script language="javascript">
		var expCodeObj=null;
		var yuiflag = new Array();
		
		function resetForm()
		{
			document.getElementById('msg').innerHTML ='';
		}
		
		function findPos(obj)
		{
			var curleft = curtop = 0;
			if (obj.offsetParent)
			{
				curleft = obj.offsetLeft;
				curtop = obj.offsetTop;
				while (obj = obj.offsetParent)
				{	//alert(obj.nodeName);
					curleft =curleft + obj.offsetLeft;
					curtop =curtop + obj.offsetTop; //alert(curtop);
				}
			}
			return [curleft,curtop];
		}
			
		function onloadDataForExpenses(type)
		{
			var url = "${pageContext.request.contextPath}/commons/process.jsp?type=getPFExpAccountCodes";
		    var req2 = initiateRequest();
		    req2.onreadystatechange = function()
		    {
		        if (req2.readyState == 4)
		        {
                  	if (req2.status == 200)
                  	{
	                  	var codes2=req2.responseText;
	                  	var a = codes2.split("^");
						var codes = a[0]; 
						acccodeArray=codes.split("+");
						expCodeObj = new YAHOO.widget.DS_JSArray(acccodeArray);
                  	}
              	}
		   };
		   req2.open("GET", url, true);
		   req2.send(null);
		}
			
		//based on key input displays the matching list of glcodes
		function autocompletecodeForExpenses(obj)
		{
			// set position of dropdown
			var src = obj;
			var target = document.getElementById('codescontainer');
			var posSrc=findPos(src);
			target.style.left=posSrc[0];
			target.style.top=posSrc[1]+5;
			target.style.width=450;
			var currRow=getRow(obj);
			var coaCodeObj = obj;
			if(yuiflag[currRow.rowIndex] == undefined)
  				{
				if(event.keyCode != 40 )
				{
					if(event.keyCode != 38 )
					{
						var oAutoComp = new YAHOO.widget.AutoComplete(coaCodeObj,'codescontainer', expCodeObj);
						oAutoComp.queryDelay = 0;
						oAutoComp.prehighlightClassName = "yui-ac-prehighlight";
						oAutoComp.useShadow = true;
					}
				}
				yuiflag[currRow.rowIndex] = 1;
			}
		}
		
		function fillNeibrAfterSplit(obj,neibrObjName,neibrObjId)
		 {
			if(obj.value=='')
			{
				document.getElementById(neibrObjName).value='';
				document.getElementById(neibrObjId).value='';
				return;
			}

		 	var currRow=getRow(obj);
		 	yuiflag[currRow.rowIndex] = undefined;
		 	neibrObj=getControlInBranch(currRow,neibrObjName);
		 	var temp = obj.value;
		 	temp = temp.split("`-`");
		 	obj.value=temp[0];
		 	if(temp.length>1)
		 	{
		 		document.getElementById(neibrObjName).value=temp[1];
		 		document.getElementById(neibrObjId).value=temp[2];
		 	}
		 	else
		 	{
		 		getIdAndNameAjax(neibrObjName,obj);
		 	}
		 }
		 
		 function getIdAndNameAjax(neibrObjName,neibrObj)
		 {
		 	var request = initiateRequest();
		 	var link = "${pageContext.request.contextPath}/providentfund/pfSetupAjax.jsp?fieldName="+neibrObjName+"&fieldValue="+neibrObj.value;
		 	request.onreadystatechange = function() 
			{
				if (request.readyState == 4) 
				{
					if (request.status == 200) 
					{
						var response=request.responseText;
						response = trimAll(response);
						if(response=='false')
						{
							//alert('Invalid code');
							document.getElementById(neibrObj.id).value='';
							document.getElementById('pfIntExpAccountName').value='';
							document.getElementById('pfIntExpAccount').value='';
							
						}
						else
						{
							response = response.split('-');
							document.getElementById('pfIntExpAccountName').value=response[1];
							document.getElementById('pfIntExpAccount').value=response[0];
						}							
					}
				}
			};
			request.open("GET", link, false);
			request.send(null);
		 }
		</script>
	</head>
	
	<body onload="onloadDataForExpenses()">  
		<s:form action="cpf" theme="simple" >  
			<center>
			
			<div class="navibarshadowwk"></div>
				<div class="formmainbox">
					<div class="insidecontent">
				  		<div class="rbroundbox2">
							<div class="rbtop2">
								<div>
								</div>
							</div>
							<div class="rbcontent2">
								<div class="datewk">	
								    <span class="bold">Today:</span> <egov:now/>
								</div>	
					
								<table width="95%" cellpadding="0" cellspacing="0" border="0" id="cpfheadertable">
									<span id="msg">
										<s:actionerror cssClass="mandatory"/>  
										<s:fielderror cssClass="mandatory"/>
										<s:actionmessage cssClass="actionmessage"/>
									</span>
									<%@ include file='cpf-form.jsp'%>
									
									<tr>
						               	<td colspan="4" class="shadowwk"></td>
						            </tr>
									
									<tr>
									 	<td colspan="2" ><div align="right" class="mandatory">* Mandatory Fields</div></td>
									</tr>
						            
						           <tr>
								       <td align="center" colspan="2"> 
											
									   </td>
						           </tr>
								</table>
							</div>
						</div>
					</div>
				</div>
				<div class="buttonholderwk">
					<s:submit method="create" value="Submit" cssClass="buttonfinal"/>
					<s:reset name="button" type="submit" id="button" value="Cancel" onclick="resetForm()" cssClass="buttonfinal"/>
					<input type="button" value="Close" onclick="javascript:window.close()" class="buttonfinal"/> 
				</div>
				<%@ include file='../common/payrollFooter.jsp'%>
			</center>
		</s:form>
	</body>
</html>