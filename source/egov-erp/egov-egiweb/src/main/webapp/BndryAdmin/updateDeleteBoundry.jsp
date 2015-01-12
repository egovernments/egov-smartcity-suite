<%@page import="java.util.ArrayList,
                java.util.List,
                java.util.Iterator,
                org.egov.infstr.utils.EGovConfig,
                org.egov.lib.admbndry.Boundary,
                org.egov.lib.admbndry.CityWebsite" %>
<%@ include file="/includes/taglibs.jsp" %>
<%
    if(request.getParameter("myBoundaryId") != null) {
        session.setAttribute("myBoundaryId",request.getParameter("myBoundaryId"));
    }
    int isActive=0;
    String operation= (String)request.getSession().getAttribute("operation");
    String name= (String)request.getSession().getAttribute("name");
    List cityList =null;
    CityWebsite cityWebsite=null;
    Boundary Obj = (Boundary)session.getAttribute("bndry");
    session.setAttribute("operation",operation);
    String mandatoryFields = "boundaryNum";

    if (request.getSession().getAttribute("cityList")!=null) {
        cityList = (ArrayList) request.getSession().getAttribute("cityList");
        for(Iterator itr =cityList.iterator();itr.hasNext();) {
            cityWebsite = (CityWebsite)itr.next();
            String url = cityWebsite.getCityBaseURL();
            isActive= cityWebsite.getIsActive().intValue();
        }
    }
%>
<c:set var="Obj" value="<%=Obj%>" scope="page" />
<html>
    <head>
        <title><%=operation.replace(operation.charAt(0),Character.toTitleCase(operation.charAt(0))) %> Boundary</title>
        <script type="text/javascript"> 
        
        function checkDuplicate() {
        
            if(document.getElementById("cityBaseURL").value == "") {
                alert("Please enter City Base URL");
                return false;
            } 
            var allRows = document.getElementsByName('cityBaseURL'); 
            for (var i=0;i<allRows.length;i++) {
                if (allRows[i].value == "") {
                    alert("Please enter City Base URL");
                    return false;
                } else {
                    for (var  j = allRows.length-1 ; j > 0 ; j--) {
                        if (j == i) {
                            break;
                        } else if (allRows[i].value == allRows[j].value) {
                            alert("City Base URL can not be duplicated");
                            return false;
                        }
                    }
                }
            }
            return true;
        }
        
        function addURL() {
    
            if (checkDuplicate()) { 
                var tbl     = document.getElementById('reason_table');
                var tbody   = tbl.tBodies[0];
                var lastRow = tbl.rows.length;
                var rowObj   = document.getElementById('reasonrow').cloneNode(true);
                var x = 0;
                while (true) {
                    if(rowObj.cells[1].childNodes[x].type == 'text' ) {
                        rowObj.cells[1].childNodes[x].value = "";
                        break;
                    }
                    x = x+1;
                }
                tbody.appendChild(rowObj);
            }           
        }
        
        function deleteURL(obj) {
            var tbl=document.getElementById('reason_table');
            var rowNumber=getRow(obj).rowIndex;
            var rowo=tbl.rows.length;
            if(rowo <= 1) {
                alert("Sorry, This City Base URL row can not be deleted");
                return false;
            } else {
                tbl.deleteRow(rowNumber)
                return true;
            }
        }
        
        var isUniqueBoundaryName = "";
        function checkUniqueForBoundaryName(boundaryId,bndryName) {
        var type='checkUniqueForBoundaryName';
        var url = "<c:url value='/commons/Process.jsp?type="+type+"&boundaryId="+boundaryId+"&bndryName="+bndryName+"'/> ";
        var req2 = initiateRequest();
        req2.open("GET", url, false);
        req2.send(null);
        if (req2.status == 200) {
            var result=req2.responseText;
            result = result.split("/");
            if(result[0]!= null && result[0]!= "") {
                isUniqueBoundaryName = result[0];
            }
        }
        
        return isUniqueBoundaryName;            
    }
    
    function checkUnique() {
        var boundaryId = "${Obj.boundaryType.id}";
        var bndryName = document.getElementById('name').value;  
        var checking  = checkUniqueForBoundaryName(boundaryId,bndryName);   
        if(checking != 'false' && "${Obj.name}" != bndryName){
            document.getElementById('uniqueBndry').style.display="block";
            return false;
        } else {
            document.getElementById('uniqueBndry').style.display="none";
            return true;
        }   
    }
    
    var isUniqueBoundaryNumber = "";
    function checkUniqueForBoundaryNum(boundaryId,bndryNum) {
        var type = 'checkUniqueForBoundaryNum';
        var url = "<c:url value='/commons/Process.jsp?type="+type+"&boundaryId="+boundaryId+"&bndryNum="+bndryNum+"'/> ";
        var req2 = initiateRequest();
        req2.open("GET", url, false);
        req2.send(null);
        if (req2.status == 200) {
            var result=req2.responseText;
            result = result.split("/");
            if(result[0]!= null && result[0]!= "") {
                isUniqueBoundaryNumber = result[0];
            }
        }
    
        return isUniqueBoundaryNumber;          
    }
    
    function checkUniqueforBndryNum() {
        var boundaryId="${Obj.boundaryType.id}";
        var bndryNum= document.getElementById('boundaryNum').value;         
        var checking=checkUniqueForBoundaryNum(boundaryId,bndryNum);
        if(checking != 'false' && "${Obj.boundaryNum}" != bndryNum) {
            document.getElementById('uniqueBndryNum').style.display="block";
            return false;
        } else {
            document.getElementById('uniqueBndryNum').style.display="none";
            return true;
        }
    }
    
    function checkFdateTdate(fromDate,toDate) {
        //ENTERED DATE FORMAT MM/DD/YYYY
        if(fromDate.substr(6,4) > toDate.substr(6,4)) {
            return false;
        } else if(fromDate.substr(6,4) == toDate.substr(6,4)) {
            if(fromDate.substr(3,2) > toDate.substr(3,2)) {
                return false;
            } else if(fromDate.substr(3,2) == toDate.substr(3,2)) {
                if(fromDate.substr(0,2) > toDate.substr(0,2)) {
                    return false;
                } else {
                    return true;
                }
            } else {
                return true;
            }
        } else {
            return true;
        }
    }
        
    function trimText1(obj,value) {
        
        value = value;
        if(value!=undefined) {
            while (value.charAt(value.length-1) == " ") {
                value = value.substring(0,value.length-1);
            }
            while(value.substring(0,1) == " ") {
                value = value.substring(1,value.length);
            }
            obj.value = value;
        }
        return value ;
    }
    
    function bodyonload() {
        if (document.getElementById("isActiveValue")) {
            var isactive = "<%= isActive%>";
            if (isactive == 1) {
                document.getElementById("isActiveValue").checked=true;
            }
        }
    }

    function checkNumber(eve) {
        var key = window.event ? window.event.keyCode : eve.keyCode ;
        if(key<48 || key >58) {
            try { window.event.returnValue = 0; } catch (e) {eve.preventDefault();}
        }
    }
    
    function validate() {
        if('<%=operation%>' == "delete") {
            document.forms[0].submit();         
        }
        if(document.getElementById("isActiveValue")) {
            if(document.getElementById("isActiveValue").checked) {
                document.getElementById('isActive').value = "1";
             } else {
                 document.getElementById('isActive').value = "0";
             }  
        }
        if(document.getElementById('name').value == ""){
            alert("Please enter the Boundary Name");
            return false;       
        }
        
        if(checkUnique()==false) {          
            return false;
        }
        
        if(document.getElementById('boundaryNum').value == ""){
            <%if("boundaryNum".equals(mandatoryFields)) {%>
                alert("Please enter the Boundary Number");
                return false;           
            <%}%>
        }
        
        if(isNaN(document.getElementById('boundaryNum').value) || document.getElementById('boundaryNum').value == 0 ){  
            alert("Please enter a Boundary Number greater than Zero");
            return false;
        }
        
        if(checkUniqueforBndryNum() == false) {
            return false;
        }
        
        if(document.getElementById('fromDate').value=="") {
           alert("Please enter a From Date");
           document.getElementById('fromDate').focus();
           return false;
        }

        if(document.getElementById('fromDate').value != "" && document.getElementById('toDate').value != "") {
            rTF = checkFdateTdate(document.getElementById('fromDate').value,document.getElementById('toDate').value);
            if(!rTF) {
                alert('From Date should be less than or equal to To Date');
                document.getElementById('fromDate').value = "";
                return false;
            }
        }       
		 <%if(cityList != null && cityList.size()!=0) {%>
        if(document.getElementById('cityLogo').value == ""){
            alert("Please enter a City Logo name");
            return false;
        }
        
        if(document.getElementById('cityBaseURL').value == "") {
            alert("Please enter a City Base URL");
            return false;
        } 
        <%}%>  
        document.forms[0].submit();
    }
    </script>
    </head> 
    <body onload ="bodyonload()">
        <html:form action="/Boundry" >
            <table align="center"   width="400" style="padding-left:25px">
            <%if (operation.equals("edit")) {%>             
                    <tr>
                        <td class="tableheader" align="center" width="100%" colspan="3" height="23">
                            <b>Update Boundary</b>
                        </td>
                    </tr>
                    <tr>
                        <td colspan=4>&nbsp;</td>
                    </tr>               
                    <tr>
                        <td class="labelcell">
                            <p align="left">Boundary Name<font class="ErrorText">*</font>&nbsp;&nbsp;</p>
                        </td>
                        <td class="labelcell" align="left" width="50%" height="31" colspan="2">
                            <input type="text" name="name" id="name" value="${Obj.name}" onblur="checkUnique();trim(this,this.value);">
                        </td>
                    </tr>
                    <tr>
                        <td class="errorcell" colspan="3">
                            <span style="display:none" id="uniqueBndry"><b><font color="red">&nbsp;&nbsp;&nbsp;Error : Boundary Name already exists!!!</font></b></span>
                        </td>
                    </tr>
                    <tr>
                        <td class="labelcell" >
                            <p align="left">Boundary Name Local</p>
                        </td>
                        <td class="labelcell" align="left" width="50%" height="31" colspan="2">
                            <input type="text" name="cityNameLocal" id="cityNameLocal" value="${Obj.bndryNameLocal}"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="labelcell">
                            <p align="left">Boundary Number
                                <%if("boundaryNum".equals(mandatoryFields)){%>  <font class="ErrorText">*</font>&nbsp;&nbsp;<%}%>
                            </p>
                        </td>
                        <td  class="labelcell" align="left" width="50%" height="31" colspan="2">
                            <html:text property="boundaryNum" styleId="boundaryNum" onkeypress="checkNumber(event)" onblur="checkUniqueforBndryNum();trim(this,this.value);"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="errorcell" colspan="3">
                            <span style="display:none" id="uniqueBndryNum"><b><font color="red">&nbsp;&nbsp;&nbsp;&nbsp;Error : Boundary Number Already Exists!!!</font></b></span>
                        </td>
                    </tr>
                    <tr>
                        <td class="labelcell" width="50%">
                            <p align="left">From Date(dd/mm/yyyy)<font class="ErrorText">*</font></p>
                        </td>
                        <td class="labelcell" align="left" width="50%" height="31" colspan="2">
                            <html:text property="fromDate" styleId="fromDate" onfocus="javascript:vDateType='3';" onkeyup="DateFormat(this,this.value,event,false,'3');" onblur="return validateDateFormat(this);"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="labelcell" width="50%">
                            <p align="left">To Date(dd/mm/yyyy)</p>
                        </td>
                        <td class="labelcell" align="left" width="50%" height="31" colspan="2">
                            <html:text property="toDate" styleId="toDate" onfocus="javascript:vDateType='3';" onkeyup="DateFormat(this,this.value,event,false,'3');" onblur="return validateDateFormat(this);"/>
                        </td>
                    </tr>                   
                    <%
                        if(cityList != null && cityList.size()!=0) {
                    %>
                    <tr>
                        <td class="labelcell">
                            <p align="left">City Logo<font class="ErrorText">*</font>
                            </p>
                        </td>
                        <td class="labelcell" align="left" width="50%" height="31">
                            <input type="text" name="cityLogo" id="cityLogo" value="<%=((CityWebsite)cityList.get(0)).getLogo() %>">
                        </td>
                    </tr>
                    <tr>
                        <td class="labelcell" >
                            <p align="left">Is Active</p>
                        </td>
                        <td  align="left" width="50%" height="31" colspan="2">
                            <input type="checkbox" name="isActiveValue"  id="isActiveValue" value="ON"  >
                            <input type="hidden" name="isActive" id="isActive">
                        </td>
                    </tr>
                    <tr>
                        <td colspan="3" align="left" class="labelcell">
                            <input type=button  name ="addRow" value="Add Row" onclick="addURL();"/>
                        </td>                       
                    <tr>
                    
                    <tr>
                        <td colspan="3">
                            <table   width="400"  id="reason_table" align="center">
                            <%
                               
                             String url= null;
                             for(Iterator itr =cityList.iterator();itr.hasNext();) {
                                cityWebsite = (CityWebsite)itr.next();
                                url = cityWebsite.getCityBaseURL();
                            %>
                                <tr  id="reasonrow">
                                    <td class="labelcell" vAlign="bottom" align="left" width="23%" height="20"> City Base URL</td>
                                    <td class="labelcell" vAlign="bottom" align="left" width="23%" height="20">
                                        <html:text property="cityBaseURL" styleId="cityBaseURL" value="<%= url%>" size="19"  onchange="return trimText1(this,this.value);"/>
                                    </td>
                                    <td class="labelcell" vAlign="bottom" align="left" width="23%" height="20">
                                        <p align="center">&nbsp;<input type=button  name ="delRow" value="Delete Row" onClick="deleteURL(this)"/>
                                    </td>
                                </tr>
                            <% }%>
                             </table>
                        </td>
                     </tr>
                    <% }%>
                    <% } else if(operation.equals("delete")) { %>
                    <tr>
                        <td class="tableheader" align="center" width="400" height="30" colspan="4">
                            <b>Delete Boundary</b>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="4">&nbsp;</td>
                    </tr>
                    <tr>
                        <td style="font-size:12px"  align="center"  colspan="4" height="26">            
                        <% if(name!=null && !name.trim().equalsIgnoreCase("null")) {%>
                            <b>Deleting this Boundary will delete all the dependant boundaries, Are you sure you want to Delete   ---  <font class="ErrorText" size = "3"><%=name%></font> ---  Boundary</b>
                        <%} %>
                        </td>
                    </tr>           
                    <%}%>
                    <tr>
                        <td><br/></td>
                    </tr>
                    <tr height="23px">
                        <td class="button2"  align="center" colspan="3" align="right"  >
                            <input type="button"  value="Save" onclick="validate()"/>
                            <input type="button" value="Back" onclick = "window.location ='BndryAdmin/boundarySearch.jsp'"/>
                        </td>
                    </tr>
                </table>                
        </html:form>
    </body>
</html>