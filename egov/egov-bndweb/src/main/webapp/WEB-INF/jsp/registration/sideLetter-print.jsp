#-------------------------------------------------------------------------------
# /*******************************************************************************
#  *    eGov suite of products aim to improve the internal efficiency,transparency,
#  *    accountability and the service delivery of the government  organizations.
#  *
#  *     Copyright (C) <2015>  eGovernments Foundation
#  *
#  *     The updated version of eGov suite of products as by eGovernments Foundation
#  *     is available at http://www.egovernments.org
#  *
#  *     This program is free software: you can redistribute it and/or modify
#  *     it under the terms of the GNU General Public License as published by
#  *     the Free Software Foundation, either version 3 of the License, or
#  *     any later version.
#  *
#  *     This program is distributed in the hope that it will be useful,
#  *     but WITHOUT ANY WARRANTY; without even the implied warranty of
#  *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#  *     GNU General Public License for more details.
#  *
#  *     You should have received a copy of the GNU General Public License
#  *     along with this program. If not, see http://www.gnu.org/licenses/ or
#  *     http://www.gnu.org/licenses/gpl.html .
#  *
#  *     In addition to the terms of the GPL license to be adhered to in using this
#  *     program, the following additional terms are to be complied with:
#  *
#  * 	1) All versions of this program, verbatim or modified must carry this
#  * 	   Legal Notice.
#  *
#  * 	2) Any misrepresentation of the origin of the material is prohibited. It
#  * 	   is required that all modified versions of this material be marked in
#  * 	   reasonable ways as different from the original version.
#  *
#  * 	3) This license does not grant any rights to any user of the program
#  * 	   with regards to rights under trademark law for use of the trade names
#  * 	   or trademarks of eGovernments Foundation.
#  *
#  *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
#  ******************************************************************************/
#-------------------------------------------------------------------------------
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@taglib uri="http://displaytag.sf.net" prefix="display"%>


<html>

	<head>
<title>Print Side Letter</title>
<link rel="stylesheet" href="<c:url value="common/css/bnd.css"/>" type="text/css" media="screen" />




 <STYLE type="text/css">
 .main{
  border:1px solid black;
  font-size:12px;
  font-family:arial;
  width:100%;
  background-color: blue;
  background-repeat: no-repeat;
  background-position: center;
  background-position:right top;
  /*background-color:#b0c4de;
  background-repeat:repeat-x;*/
  
  }
 .nmcbd-form-main
     {
      font-family:arial;
     }
     
     .nmcbd-form-title
     {
      font-size:20px;
       line-height:1.3em;
       
     }
       
  .nmcbd-header{
  	
  	line-height:1.3em;
  	font-size:16px;
  	padding-bottom:1px;
  		font-family:arial;
  }
     .maintable{
  	padding:1px;
  	line-height:1.5em;
  		
  }
   .maintable td{padding:1px;}
   .nmcbd-paragraph{
    	line-height:1.5em;
    	font-size:16px;
    	text-align:justify;
    	
  }
  
  .name {
  	font-family: "Times New Roman", Times, serif;
  	font-size:14px;
  	font-weight: bold;
  	color:#000;
          padding-right: 10px;
  	padding-left: 10px;
  	padding-bottom: 0px;
  	/* text-decoration: underline; */
  	border-bottom:1px dotted #6F6F6F;
         max-height:10px;
  		
  }  
  .FormEng
  {
  font-family:arial;

  }
  </STYLE>

   <SCRIPT language="javascript" type="text/javascript">
	function dothis()
	 {
	      window.moveTo(0,0);
	      window.resizeTo(screen.availWidth,screen.availHeight);

	  }    
	  function hidePrintButton(){
		
		document.getElementById("printbutton").style.display='none';
		window.print();
	}
 </SCRIPT>
	</head>

	<body bgcolor="#FFFFFF" onload="dothis()">
	<s:form theme="css_xhtml" action="sideLetter" name="sideLetterForm" >
	<s:push value="model">	
		<table align='center' id="table2">
			<tr>
				<td>

					<!-- Body Begins -->

					<DIV id="paymain">
						<DIV id="paym2">
							<DIV id="m3">
								<div align="center">
									<table align="center" border="0" cellpadding="0"
										cellspacing="0" class="nmcbd-form-main">
										<tr >
										
										      
											<th class="nmcbd-header" >
												<span class="nmcbd-form-title"><s:text
														name="GovNameLoc" /> </span>
												<br />
												<span class="nmcbd-form-title"> <s:text
														name="nagpurMuncipalOfficeHeader" /> </span>
												<br />
												<span class="nmcbd-form-title"> <s:text
														name="DeptLoc" />/<s:text name="DeptEng" /> <br /> </span>
												<span class="nmcbd-header"><s:text
														name="officeTelDetailForSideLetter" /> <br /> </span>

											</th>
										</tr>
										
									</table>

									<table width="90%" border="0" cellpadding="0" cellspacing="0"
										align="center" class="maintable">
										<tr>&nbsp;</td>
										</tr>
										<tr>&nbsp;</td>
										</tr>
										<tr>    
										<s:hidden id="sideLetterType" name="sideLetterType" value="%{sideLetterType}" />
										 
										
											<td colspan="4">
												<table width="80%" border="0">
													<tr>
														<td width="50%" colspan="2" align="left">
															<span class="FormEng"><s:text name="RefNo" />
																&nbsp;&nbsp;&nbsp; <s:property value="%{referenceNumber}"/></span>
														</td>
														<td width="50%" colspan="2" align="right">
															&nbsp;&nbsp;Date: &nbsp; <s:date format="dd/MM/yyyy" name="dateOfissue" />
														</td>
													</tr>
												</table>
											</td>
										</tr>
										<tr>
											<td>
												
												<br>
												<span class="FormEng"><s:text name="to" /> </span>
											</td>
										</tr>
										<tr>
									
											<td>
												<span class="FormEng">
													&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp; <s:property value="%{citizenName}"/>          <br>
													<s:if
														test="%{sideLetterType=='withoutregistrationdetails' }">
														
	    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<s:property value="%{applicantRelationType.desc}" />&nbsp; <s:property value="%{applicantName}"/> <br>
													</s:if> &nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; &nbsp;
														<s:property value="%{applicantAddress}" /> </span>
											</td>
										</tr>

										<tr>
											<td>
												
												<br>
												<span class="FormEng"><s:text
														name="sideLettersubject" /> <br> </span>
											</td>
										</tr>
										<tr>
											<td>
												<span class="FormEng"> <s:text
														name="sideLetterreference" /><s:date format="dd/MM/yyyy" name="applicationDate" />  <br> </span>
											</td>
										</tr>
										<tr>
											<td><br>
												<span class="FormEng"> <s:text
														name="sideLetterReqHeader" /> <br> </span>
											</td>
										</tr>

										<tr>
											<td>
												<br>
												<span class="nmcbd-paragraph"><span class="FormEng">
												<s:if
														test="%{sideLetterType=='withoutregistrationdetails'}">
														
														&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<s:text name="sideLetterFirstPara" /> 
 										</s:if>
 									<s:else>
 									&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;With
														the reference to your application as above regarding entry
														of name &nbsp;  <span class="name"> <s:property value="%{citizenName}"/>   
																
													</span>
													
													
													&nbsp; I have to inform you that birth Certificate issued under
														section 12/17 of Registration of Birth & Death Act 1969
														bearing Registration Number <span class="name"> <s:property value="%{birthReportId.registrationNo}"/>
																
													</span>
													
													
													 here in date of Birth is mentioned as <span class="name"><s:property value="%{formattedDateOfEvent}" />
																
													</span>
													&nbsp; which is in respect of Birth of<span class="name"><s:property value="%{citizenNameAsPerRecord}"/></span>
													&nbsp; only <span class="FormEng"> . So for as
														inclusion of name in our office record is concerned,it is
														not possible in view of notification issued by the
														Government.</span> 
 									
 									</s:else> 
 										
														
 	
 						      
 								 <br>
														<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<s:text name="sideLetterSecondPara" />
														<br>
														<br>
														&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<s:text name="sideLetterLastPara" />
														<br>
														<br>
														<br> 
														</span>
														</span>
											</td>

										</tr>

									</table>
									</DIV>
								</DIV>
							</DIV>
						</DIV>
				</td>
			</tr>
			
			<br><br>        
    <tr>		
             
     <td colspan="4">
     		<table width="90%" border="0">
     			<tr>
			<td width="20%" colspan="2" align="left" ><span class="FormEng">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<s:text name="sLOfficeSeal"/></span></td>
			<td width="40%" colspan="2" >&nbsp;&nbsp;</td>
			<td width="30%" colspan="1" align="center"><span class="FormEng"><s:text name="yourFaithfully"/><br><br><br><s:text name="sLStatOfficer"/></span><br>
  	<s:text name="sLBDDept"/><br></td>
			
			</tr>
		</table>
	</td>
     </tr>
		</table>
		</div></div></div></div>
</td></tr>

</table>


<div id="printbutton" class="buttonbottom" align="center">
<table>
<tr >
	<td>   
	 <input type="button" class="button"   id="printButton" name="printButton"  value="Print This Page" onclick="hidePrintButton();" /> 
	</td>              
</tr> 
</table>
</div> 





		</s:push>
</s:form>
	</body>
</html>
