<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>  
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<html>
  <head>
      <title><s:text name='project.closure.title'/></title>
  </head>

	<script src="<egov:url path='js/works.js'/>"></script>
	<script src="<egov:url path='js/helper.js'/>"></script>
	
  	<script type="text/javascript">
  		var warnings=new Array();
		warnings['improperProjectCodeSelection']='<s:text name="estimate.depositCode.warning.improperProjectCodeSelection"/>';


		function validateSearch(){
			clearMessage('projectClosureError');
			document.getElementById("projectClosureError").style.display='none';
			if(dom.get("projectCodeId").value=='' || dom.get("projectCodeSearch").value=='')
			{
				dom.get("projectCodeId").value='';
				dom.get("projectCodeSearch").value='';
			}		
			if(dom.get("depositCodeId").value=='' || dom.get("depositCodeSearch").value=='')
			{
				dom.get("depositCodeId").value='';
				dom.get("depositCodeSearch").value='';
			}
			if(dom.get("estimateId").value=='' || dom.get("estimateNumberSearch").value=='')
			{
				dom.get("estimateId").value='';
				dom.get("estimateNumberSearch").value='';
			}
			if((dom.get("projectCodeId").value=='' || dom.get("projectCodeSearch").value=='')
				&& (dom.get("depositCodeId").value=='' || dom.get("depositCodeSearch").value=='')
				&& (dom.get("estimateId").value=='' || dom.get("estimateNumberSearch").value==''))
			{
				document.getElementById("projectClosureError").style.display='';
        		document.getElementById("projectClosureError").innerHTML='<s:text name="project.closure.enter.search.filter" />';
				return false;
			}
			return true;
		}
		function gotoProjectDetailsPage(obj){
			if(obj!=null && obj.value!='')
				window.open("${pageContext.request.contextPath}/depositWorks/projectClosure!viewProjectDetails.action?viewEstimateId="+obj.value,'_self');
		}
		
        var projectCodeSearchSelectionHandler = function(sType, arguments){ 
        	var oData = arguments[2];
            dom.get("projectCodeId").value = oData[1];
        };

        var depositCodeSearchSelectionHandler = function(sType, arguments){ 
        	var oData = arguments[2];
            dom.get("depositCodeId").value = oData[1];
        };

        var estimateNumberSearchSelectionHandler = function(sType, arguments){ 
        	var oData = arguments[2];
        	dom.get("estimateId").value = oData[1];
        };
        
	var projectCodeSelectionEnforceHandler = function(sType, arguments){
	    warn('improperProjectCodeSelection');
	};

	function roundAmount(str){
		if(str!=null && str!='')
		{
			return roundTo(eval(str));	
		}	
	}
		
  </script> 	
   <body >
      	<s:if test="%{hasErrors()}">
        	<div class="errorstyle" >
          		<s:actionerror/>
          		<s:fielderror/>
        	</div>
    	</s:if>    

	<s:form theme="simple" name="projectClosureForm" id="projectClosureForm"> 
  	 	<input type="hidden" name="isEnableSelect" id="isEnableSelect"/>
    		<div id="projectClosureError" class="errorstyle" style="display:none;"></div>
 
    		<div class="formmainbox">
 			<div class="insidecontent"> 
 				<div class="rbroundbox2">
 					<div class="rbtop2"><div></div></div>
 					<div class="rbcontent2">
 						<table width="100%" border="0" cellspacing="0" cellpadding="0"> 
							<tr>
								<td colspan="4">
									<table width="100%" border="0" cellspacing="0" cellpadding="0">
										<tr>
											<td class="headingwk" align="left">
												<div class="arrowiconwk">
													<img src="${pageContext.request.contextPath}/image/arrow.gif" />
												</div>
												<div class="headerplacer">
													<s:text name='title.search.criteria' />
												</div>
											</td>
										</tr>
									</table>
								</td>
							</tr>
 							<tr>
								<td class="whiteboxwk"><s:text name="project.closure.project.code" />:</td>
           						<td class="whitebox2wk">
               						<div class="yui-skin-sam">
               							<div id="projectCodeSearch_autocomplete">
               								<div>
												<s:textfield id="projectCodeSearch" name="searchProjectCode" value="%{searchProjectCode}" disabled="%{isEnableSelect}" class="selectwk" /><s:hidden id="projectCodeId" name="projectCodeId" value="%{projectCodeId}"/>
											</div>
               								<span id="projectCodeSearchResults"></span>
               							</div>
               						</div>
               						<egov:autocomplete name="projectCodeSearch" width="20" field="projectCodeSearch" url="ajaxProjectClosure!searchProjectCode.action?" queryQuestionMark="false" results="projectCodeSearchResults" handler="projectCodeSearchSelectionHandler" forceSelectionHandler="projectCodeSelectionEnforceHandler"/> 
               						<span class='warning' id="improperProjectCodeSelectionWarning"></span> 
           						</td> 
	        					<td class="whiteboxwk" ><s:text name="estimate.number" />:</td>
	        					<td class="whitebox2wk" >
	        						<div class="yui-skin-sam">
	        							<div id="estimateNumberSearch_autocomplete">
                							<div>
	        									<s:textfield id="estimateNumberSearch" name="estimateNumber" value="%{estimateNumber}" cssClass="selectwk" /><s:hidden id="estimateId" name="estimateId" value="%{estimateId}"/>
	        								</div>
	        								<span id="estimateNumberSearchResults"></span>
	        							</div>		
	        						</div>
	        						<egov:autocomplete name="estimateNumberSearch" width="20" field="estimateNumberSearch" url="ajaxProjectClosure!searchEstimateNumber.action?" queryQuestionMark="false" results="estimateNumberSearchResults" handler="estimateNumberSearchSelectionHandler" />
		         				</td>
		    				</tr>
	    					<tr>
								<td class="greyboxwk"><s:text name="depositworks.deposit.code" />:</td>
           						<td class="greybox2wk">
               						<div class="yui-skin-sam">
               							<div id="depositCodeSearch_autocomplete">
               								<div>
												<s:textfield id="depositCodeSearch" name="searchDepositCode" value="%{searchDepositCode}" disabled="%{isEnableSelect}" class="selectwk" /><s:hidden id="depositCodeId" name="depositCodeId" value="%{depositCodeId}"/>
											</div>
               								<span id="depositCodeSearchResults"></span>
               							</div>
               						</div>
               						<egov:autocomplete name="depositCodeSearch" width="20" field="depositCodeSearch" url="ajaxProjectClosure!searchDepositCode.action?" queryQuestionMark="false" results="depositCodeSearchResults" handler="depositCodeSearchSelectionHandler" /> 
           						</td>
           						<td class="greyboxwk"></td>
           						<td class="greybox2wk"></td>
            				</tr>
            				<tr><td class="whiteboxwk" colspan="4"></td></tr>
 						</table>
 					</div>
 	
 	 	
 					<div class="buttonholderwk" id="slCodeButtons">
						<s:submit cssClass="buttonadd" value="SEARCH" id="searchButton" name="searchButton" method="searchProjectDetails" onclick="return validateSearch();"/>
	  					&nbsp;
	  					<input type="button" class="buttonfinal" value="CLEAR" id="clearButton" name="clearButton" onclick="window.open('${pageContext.request.contextPath}/depositWorks/projectClosure!changeStatus.action','_self');"/>
	  					&nbsp;
	 					<input type="button" class="buttonfinal" value="CLOSE" id="closeButton" name="closeButton" onclick="window.close();"/>
    				</div>
					<br/>
 					<div class="rbcontent2">
 						<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td  class="headingwk" align="left">
									<div class="arrowiconwk">
										<img src="${pageContext.request.contextPath}/image/arrow.gif" />
									</div>
									<div class="headerplacer">
										<s:text name='search.result' />
									</div>
								</td>
							</tr>
 						<s:if test="%{estimateList.size!=0}" >
								<tr align="center">
								<display:table name="estimateList" pagesize="30"
									uid="currentRow" cellpadding="0" cellspacing="0"
									requestURI=""
									style="border:1px;width:100%;empty-cells:show;border-collapse:collapse;">
									
									<display:column headerClass="pagetableth"
										class="pagetabletd" title="Sl.No"
										titleKey="column.title.SLNo"
										style="width:2%;text-align:right" >
											<s:property value="#attr.currentRow_rowNum"/>
									</display:column>
									
									<display:column headerClass="pagetableth"
										class="pagetabletd" title="Estimate Number"
										titleKey="estimate.number"
										style="width:10%;text-align:left" property="estimateNumber" />
				
									<display:column headerClass="pagetableth"
										class="pagetabletd" title="Project Code"
										titleKey="project.closure.project.code"
										style="width:10%;text-align:left" property="projectCode.code" />
				
									<display:column headerClass="pagetableth"
										class="pagetabletd" title="Estimate Amount"
										titleKey="estimate.amount"
										style="width:10%;text-align:right"  >
										<s:text name="contractor.format.number" >
												<s:param name="rate" value="#attr.currentRow.totalAmount.value"/>
										</s:text>
									</display:column>	
										
									<display:column headerClass="pagetableth"
										class="pagetabletd" title="Deposit Code"
										titleKey="depositworks.deposit.code"
										style="width:10%;text-align:left" property="depositCode.code" />	
			
									<display:column headerClass="pagetableth" class="pagetabletd"
										title="Select" titleKey="column.title.select"
										style="width:2%;text-align:left">
										<input name="radio" type="radio" id="radio"
											value="<s:property value='%{#attr.currentRow.id}'/>"
											onClick="gotoProjectDetailsPage(this)" />
									</display:column>
								</display:table>
								</tr>
						</s:if>
						<s:else>
						<div>
							<table width="100%" border="0" cellpadding="0"
								cellspacing="0">
								<tr>
									<td align="center">
										<font color="red">No record Found.</font>
									</td>
								</tr>
							</table>
						</div>
						</s:else>
						</table>	
					</div>
				</div>
			</div>
		</div>
   	</s:form>
  </body>
</html>
