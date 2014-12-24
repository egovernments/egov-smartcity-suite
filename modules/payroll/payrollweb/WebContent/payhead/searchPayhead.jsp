<%@ include file="/includes/taglibs.jsp" %>
<%@page import="java.util.*" %>


<html>

<head>


	<title>Payhead</title>
	

	<style type="text/css">
		#payheadContainer {position:absolute;left:11em;width:9%}
		#payheadContainer .yui-ac-content {position:absolute;width:80%;border:1px solid #404040;background:#fff;overflow:hidden;z-index:9050;}
		#payheadContainer .yui-ac-shadow {position:absolute;margin:.3em;width:40%;background:#a0a0a0;z-index:9049;}
		#payheadContainer ul {padding:5px 0;width:80%;}
		#payheadContainer li {padding:0 5px;cursor:default;white-space:nowrap;}
		#payheadContainer li.yui-ac-highlight {background:#ff0;}
		#payheadContainer li.yui-ac-prehighlight {background:#FFFFCC;}
</style>

<%
	String mode = (String)request.getAttribute("mode");
	System.out.println("mode--"+mode);
	
				request.getSession().removeAttribute("id");	
				request.getSession().removeAttribute("salarycodeList");	
				request.getSession().removeAttribute("wfActionList");	
				request.getSession().removeAttribute("ruleList");	
			
%>


<script language="JavaScript"  type="text/JavaScript">
    
	var payheadArray = new Array();
	var payheadObj;
	var yuiflag = new Array();
  
   function initiateRequest(){
		if(window.XMLHttRequest){
			return new XMLHttpRequest();
		}
		else if(window.ActiveXObject){
			return new ActiveXObject("Microsoft.XMLHTTP");
		}
	}
	
	function loadAllPayheads(){
		var action = "getallPayheads";
        var url = "<%=request.getContextPath()%>"+"/commons/process.jsp?type=" +action;
      	var req = initiateRequest();
      	req.onreadystatechange = function(){
	      if (req.readyState == 4){
	            if (req.status == 200){
                   	var payheads=req.responseText
                   	var a = payheads.split("^");
                   	var codes = a[0];
					payheadArray=codes.split("+");
					payheadObj = new YAHOO.widget.DS_JSArray(payheadArray);
	           }
	       }
        };
	   req.open("GET", url, true);
	   req.send(null);
  	}
  	
  	function autocompletePayhead(){
	 	var coaCodeObj = document.getElementById("name");
		if(yuiflag[0] == undefined){
			if(event.keyCode != 40 ){
				if(event.keyCode != 38 ){
					var oAutoComp = new YAHOO.widget.AutoComplete(coaCodeObj,'payheadContainer',payheadObj);
					oAutoComp.queryDelay = 0;
					oAutoComp.prehighlightClassName = "yui-ac-prehighlight";
					oAutoComp.useShadow = true;
					oAutoComp.maxResultsDisplayed = 100;
				}
			}
		yuiflag[0] = 1;
		}
	}
	
	function setPayheadAfterSplit(obj){
  		var temp = obj.value;
  		var temp1 = temp.split("`-`");
  		this.value = temp1[0];
  	}

  	function setPayheadFlagUndefined(){
 		yuiflag[0] = undefined;
	}
 function checkOnSubmit()
  {
	var Id= document.payheadForm.name.value;
	
		if(document.payheadForm.name.value==""){
			alert('<bean:message key="alertPayhead"/>');
			document.payheadForm.name.focus();
			return false;
		}
		}
	function validate()
	{
	
	
	var head= document.payheadForm.name.value;
	
		if(document.payheadForm.name.value==""){
			alert('<bean:message key="alertPayhead"/>');
			document.payheadForm.name.focus();
			return false;
		}
	<%

	if("create".equals(mode)){ 
	%>
			var mode="<%=mode%>";
			
		 document.payheadForm.action ="${pageContext.request.contextPath}/payhead/payheadRule.do?submitType=beforModifyPayheadRule&head="+head +"&mode="+mode;
		 //document..submit();
		 document.payheadForm.submit();
		<%}
		 %>
		<%if("viewRule".equals(mode)){ %>
		var mode="<%=mode%>";
		
		 document.payheadForm.action ="${pageContext.request.contextPath}/payhead/payheadRule.do?submitType=viewPayheadRule&head="+head +"&mode="+mode;
		 //document..submit();
		 document.payheadForm.submit();
		<%}%>	
		
	}	  
  
 		
 	
</script>
</head>

<body  >

	  
<html:form  action="/payhead/searchPayhead">





	<table  width="95%" cellpadding ="0" cellspacing ="0" border = "0" id="employee">
		<tr>
	<%if("view".equals(mode)){ %>	
	    <td colspan="4" class="headingwk"><div class="arrowiconwk"><img src="../common/image/arrow.gif" /></div>
                  <div class="headplacer">View Payhead </div></td>
	<%} %>    
	<%if("modify".equals(mode)){ %>
	    <td colspan="4" class="headingwk"><div class="arrowiconwk"><img src="../common/image/arrow.gif" /></div>
                  <div class="headplacer">Modify Payhead </div></td>
	<%} %>    
	    </tr>
		
	
	     <tr>	 	 
	  		<td class="whiteboxwk"><span class="mandatory">*</span>Payhead Name</td>
	  		<td class="whitebox2wk">
	  		<html:select styleClass="selectwk" property="name" >
	  		<html:option value="">-----------Select-----------</html:option>
			   <c:forEach var="salcodeObj" items="${salarycodes}" >
	   	 		<html:option value="${salcodeObj.head}">${salcodeObj.head}</html:option>
			   </c:forEach>
			</html:select>	  
  			</td>
	    </tr>	  	
	    
	</table> 

<br>
<table align='center' id="table2">	
 		<tr>
 			<td class="labelcell"></td>
 		<%if("view".equals(mode)){ %>	
	    	<td >	    		
	    		
	    		<html:submit property="action" value="View" onclick="return checkOnSubmit();" styleClass="buttonfinal"/>
			</td>    
		<%} %>	
		<%if("modify".equals(mode)){ %>
			<td >
				<html:submit property="action" value="Modify" onclick="return checkOnSubmit();" styleClass="buttonfinal"/>
				
			</td>
		<%} %>
		<%if("create".equals(mode)){ %>	
	    	<td >	    		
	    		<input type="button" property="action" value="Update" onclick="validate();" class="buttonfinal"/>
			</td>    
		<%} %>
		<%if("viewRule".equals(mode)){ %>	
	    	<td >	    		
	    		<input type="button" property="action" value="ViewRule" onclick="validate();" class="buttonfinal"/>
			</td>    
		<%} %>		
	    </tr>	
	   
              

</table>	
</html:form>

<tr align="right">
                <td colspan="4"><div align="right" class="mandatory">* Mandatory Fields</div></td>
              </tr>
</tr>
            </table></td>
          </tr>
       

</body>
</html>
