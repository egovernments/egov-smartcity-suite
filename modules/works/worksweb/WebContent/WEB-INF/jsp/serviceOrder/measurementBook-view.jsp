
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html><head>
<meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
<title> View Measurement Book </title>
</head>
<body   class="yui-skin-sam" onload="loadDropDownCodes();loadDropDownCodesFunction()"> 
<s:if test="%{hasErrors()}">
		<div class="errorstyle">
			<s:actionerror />
			<s:fielderror />
		</div>
	</s:if>
<div id="jserrorid" class="errorstyle" style="display:none" >
	<p class="error-block" id="lblError" ></p>
</div>
 <s:form action="measurementBook" theme="simple" name="mBookBillForm" >
<s:push value="model">

	    <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
            <td><div id="header">
				<ul id="Tabs">
					<li id="soDetailsTab" class="First Active"><a id="header_1" href="#" onclick="showSoDetailsTab();">Service Order Details</a></li>
					<li id="expenseBillTab" class="Last"><a id="header_2" href="#" onclick="showExpenseBillTab();">Create Expense Bill</a></li>
				</ul>
            </div></td>
          </tr>
      
            
          <tr>
            <td>
            <div id="so_details">
         			<jsp:include page="measurementBook-soDetls.jsp" />    
          			
            </div>            
            </td> 
          </tr>            
          <tr>
            <td>
            <div id="expensebill_details" style="display:none;"> 
            	<jsp:include page="measurementBook-bill.jsp" />    
            </div>
            </td>
          </tr>
         
	  </table><br><br>
	  <table width="100%">
							
							<tr>
								<td colspan="4">
									<div class="buttonholderwk">
									<s:iterator value="%{validButtons}" var="p">
										 <s:if test="%{description !='Cancel'}">
		 								 <s:submit type="submit" cssClass="buttonfinal" value="%{description}" id="%{name}" name="%{name}" method="create" onclick="var b= validateUser('%{name}','%{description}'); if(b) return validate(); else return b;"/>
		  								</s:if>
									</s:iterator>
										
										<input type="button" class="buttonfinal" value="CLOSE"
											id="closeButton" name="button" onclick="window.close();" />
									</div>
								</td>
							</tr>
							
				</table>
</s:push>
</s:form>
<script >
	function showSoDetailsTab(){
		document.getElementById('so_details').style.display='';
		document.getElementById('expensebill_details').style.display='none';
		setCSSClasses('expenseBillTab','Last');
		setCSSClasses('soDetailsTab','First Active');
	}
	function showExpenseBillTab(){
		document.getElementById('so_details').style.display='none';
		document.getElementById('expensebill_details').style.display='';
		setCSSClasses('soDetailsTab','First BeforeActive');
		setCSSClasses('expenseBillTab','Last Active ActiveLast');
	}
	function setCSSClasses(id,classes){
    		 document.getElementById(id).setAttribute('class',classes);
   		    document.getElementById(id).setAttribute('className',classes);
	}
for(var i=0;i<document.forms[0].length;i++){
		if(document.forms[0].elements[i].value !='CLOSE' ){
			document.forms[0].elements[i].disabled =true;
		}
	}
	
</script>
</body>

</html>