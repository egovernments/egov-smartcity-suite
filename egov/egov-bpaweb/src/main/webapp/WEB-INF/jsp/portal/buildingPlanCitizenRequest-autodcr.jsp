<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/includes/taglibs.jsp"%>
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>Citizen Page
		</title>
 <sj:head jqueryui="true" jquerytheme="redmond" loadAtOnce="true"/>
	</head>

<script language="javascript" type="text/javascript">
jQuery.noConflict();

jQuery(document).ready(function() {
jQuery('#buttonClose').button();
jQuery('#Close').button();

jQuery('input[type="button"]').click(function(){
if(jQuery(this).val()=='Select'){
       jQuery('input[type="button"]').each(function(){
        jQuery(this).val('Select');      
          jQuery(this).removeClass('greenbutton');
           jQuery(this).addClass('redbutton');
       })
        jQuery(this).removeClass('redbutton');
        jQuery(this).val('Selected');
        jQuery(this).addClass('greenbutton');       
        var selectedautodcrnum  = jQuery(this).closest('tr').find("input[id='autoDcrNum']").attr("value");
         jQuery('#autoDcrNumtemp').val(selectedautodcrnum)
        }else{
           jQuery('#autoDcrNumtemp').val('')
        jQuery(this).addClass('redbutton');
         jQuery(this).val('Select');
       jQuery(this).removeClass('greenbutton');
        
        }
});




});

function createNewRecord()
		{
		
		var autoDcrNum = document.getElementById("autoDcrNumtemp").value;
		if(autoDcrNum==""){
            alert("Please select an autoDcr")
		return false;
		}else{
		  var servType=document.getElementById("serviceType").value;
		  var servRegId=  document.getElementById("serviceRegId").value;
		  var reqId= document.getElementById("requestID").value;
		  document.location.href="${pageContext.request.contextPath}/portal/citizenRegisterBpa!newCitizenForm.action?serviceType="+servType +"&serviceRegId="+servRegId +"&requestID="+reqId+"&autoDcrNum="+autoDcrNum;
		 loadingMask();
		}
		}
function viewRegisterBpa(registrationId){
	document.location.href="${pageContext.request.contextPath}/register/registerBpa!viewForm.action?registrationId="+registrationId;		
 }
		 
</script>		
	<body>
		<s:form name="buildingPlanCitizenRequest" theme="simple">
			<s:if test="%{hasErrors()}">
				<div class="errorstyle">
					<s:actionerror />
					<s:fielderror />
				</div>
			</s:if>
			<s:if test="%{hasActionMessages()}">
				<div class="errorstyle">
					<s:actionmessage />
					</div>
			</s:if>
			
	<s:hidden id="serviceType" name="serviceType" value="%{serviceType}"/>
	<s:hidden id="serviceRegId" name="serviceRegId" value="%{serviceRegId}"/>
	<s:hidden id="requestID" name="requestID" value="%{requestID}"/>
	<s:hidden id="autoDcrNumtemp" name="autoDcrNumtemp" value="%{autoDcrNum}"/>
	<div align="center" id="autoDcrWithRegistrationdiv">
	  		<s:if test="%{regnAutoDcrList.size!=0}">
					   <h1 class="subhead" ><s:text name=" Already used auto Dcr details "/></h1>
			
				         <div id="displaytbl">	
          		     	 <display:table  name="regnAutoDcrList" export="false" requestURI="" id="regListid"  class="its" uid="currentRowObject" >
          			 	 <div STYLE="display: table-header-group" align="center">
          			 	  
 						 	<display:column title=" Sl No" style="text-align:center;"  >
 						 	 		<s:property value="#attr.currentRowObject_rowNum"/>
 						 	</display:column>
						
							<display:column class="hidden" headerClass="hidden"  media="html">
 						 		<s:hidden id="registrationId" name="registrationId" value="%{#attr.currentRowObject.id}" />
							</display:column>
							
							<display:column title="Plan Submission Number " style="text-align:center;" >	
 						 	<a href="#" onclick="viewRegisterBpa('${currentRowObject.registration.id}')">
 						 		 ${currentRowObject.registration.planSubmissionNum}
 						 	</a>
 						 	</display:column>	
 						 	<display:column title="Auto Dcr Number " style="text-align:center;" property="autoDcrNum" />	 
 						 	<display:column  title="Plan Submission Date" style="width:6%;text-align:left" >
								<s:date name="#attr.currentRowObject.registration.planSubmissionDate" format="dd/MM/yyyy" />
							</display:column>
 						 	
							<display:column title="Service Type" style="text-align:center;" property="registration.serviceType.description" />								
						
							<display:column title="Applicant Name " style="text-align:center;" property="registration.owner.firstName" />	 
							
							<display:column title="Applicant Address " style="text-align:center;" property="registration.bpaOwnerAddress" />	 								
							
							<display:column title="Current Owner" style="text-align:center;" >
          			        	<s:property value="%{getUsertName(#attr.currentRowObject.registration.state.owner.id)}" />
							</display:column>
						    <display:column title="Status " style="text-align:center;" property="registration.egwStatus.code" />	 
										 						 
						</div>
						</display:table>
					</div>
			</s:if>
     </div>

	<div align="center" id="autoDcrDiv">
	
	  		<s:if test="%{autoDcrList.size!=0}">
					   <h1 class="subhead" ><s:text name=" Auto Dcr details "/></h1>
					    <div align="center">
				 <table id="feehead" width="80%" border="0" cellspacing="0" cellpadding="0" class="tablebottom">
				  		 <tr>
							<th  class="bluebgheadtd" width="3%"><div align="center">Sl No</div></th>
						    <th  class="bluebgheadtd" width="8%"><div align="center">Auto Dcr Number</div></th>
						    <th  class="bluebgheadtd" width="8%"><div align="center">Applicant Name</div></th>
						    <th  class="bluebgheadtd" width="8%"><div align="center">Address</div></th>
						    <th  class="bluebgheadtd" width="8%"><div align="center">Plot Number</div></th>
						     <th  class="bluebgheadtd" width="8%"><div align="center">Survey Number</div></th>
						      <th  class="bluebgheadtd" width="8%"><div align="center">Village Name</div></th>
						       <th  class="bluebgheadtd" width="8%"><div align="center">Plot Area</div></th>
						         <th  class="bluebgheadtd" width="8%"><div align="center"></div></th>
						    </tr>
					  <s:iterator value="autoDcrList" status="row_status">
		    			<tr>		
							   <td  class="blueborderfortd"><div align="center"><s:text name="#row_status.count" /></div></td>
							   <s:hidden id="autoDcrNum" name="autoDcrNum" value="%{autoDcrNum}" />
							     <td class="blueborderfortd"><div align="center"><s:property value="%{autoDcrNum}"/>	</td>
		       					<td class="blueborderfortd"><div align="center"><s:property value="%{applicant_name}"/>	</td>
		       					<td class="blueborderfortd"><div align="center"><s:property value="%{address}"/>	</td>
		       					
		       					<td class="blueborderfortd"><div align="center"><s:property value="%{plotno}"/>	</td>
		       					<td class="blueborderfortd"><div align="center"><s:property value="%{surveyno}"/>	</td>
		       					<td class="blueborderfortd"><div align="center"><s:property value="%{village}"/>	</td>
		       					<td class="blueborderfortd"><div align="center"><s:property value="%{plotarea}"/>	</td>
		       					<td class="blueborderfortd"><div align="center"><input class ="redbutton"  type ="button" id="check" value ="Select" /></td>
						  </tr>
				          </s:iterator> 
				          </table>
				     </div> 
     		</s:if>
     		<s:else>
     			<h1 class="subhead" ><s:text name="Auto Dcr details are not available with the registered mobile number. "/></h1>
	 		 </s:else>
     		
     </div>
	
			
			<div class="buttonbottom">
			<s:if test="%{autoDcrList.size!=0}">
				<a href="#"   class="button"
					id="buttonClose" onclick="createNewRecord();" >Proceed</a>
					</s:if>
				<a href="#"   class="button"
					id="Close" onclick="window.close();" >Close</a>

			</div>
		</s:form>
	</body>
</html>