<%@ include file="/includes/taglibs.jsp" %>
<%@page import="java.util.*,org.egov.payroll.utils.*,java.text.SimpleDateFormat" %>


<html>

<head>


	<title>Search Payhead Rule</title>
	

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
	List payRuleList = PayrollManagersUtill.getPayheadService().getAllPayheadRule();
%>

<c:set var="payRuleList" value="<%= payRuleList %>" />
<script language="JavaScript"  type="text/JavaScript">

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
		if(document.getElementById("payheadRuleId").value==""){
			alert('Select Payhead Rule');
			document.getElementById("payheadRuleId").focus();
			return false;
		}
		document.forms[0].action ="${pageContext.request.contextPath}/payhead/payheadRule.do?submitType=viewPayheadRule";
	   	document.forms[0].submit();
	}	  
 	
</script>
</head>

<body  >


		
			<html:form   action="/payhead/payheadRule" >

			<table  width="95%" colspan="6" cellpadding ="0" cellspacing ="0" border = "0" id="searchPayheadruletable">
				<tr>
					<td colspan="4" class="headingwk">
						<div class="arrowiconwk"><img src="../common/image/arrow.gif" /></div>
                 		<div class="headplacer"><bean:message key="PayheadRuleSearchView"/></div>
                 	</td>
              	</tr>
				
				<tr>
					<td height="15" class="tablesubcaption" colspan="6" align="center"></td>
				</tr>
			
			    <tr>	 	 
			  		<td class="whiteboxwk"><span class="mandatory">*</span><bean:message key="PayheadRuleSearchName"/></td>
			  		<td class="whitebox2wk">
			  		<html:select styleClass="selectwk" property="payheadRuleId" styleId="payheadRuleId">
			  			<html:option value="">-----------Select-----------</html:option>
					    <c:forEach var="payheadRule" items="${payRuleList}" >
			   	 		    <html:option value="${payheadRule.id}">${payheadRule.description}</html:option>
					    </c:forEach>
					</html:select>	  
		  			</td>
			    </tr>	  	
			
				<tr>
	              <td colspan="2" class="shadowwk"></td>
	            </tr>
	            
	            <tr>
	                <td colspan="2"><div align="right" class="mandatory">* Mandatory Fields</div></td>
	            </tr>
					
		 		<tr>
					<td colspan="2" align="center">	    		
			    		<input type="button" name="view" value="View" onclick="return checkOnSubmit();" styleClass="buttonfinal">
					</td>    
			    </tr>
		  </table>
		</html:form>
		
</body>
</html>
