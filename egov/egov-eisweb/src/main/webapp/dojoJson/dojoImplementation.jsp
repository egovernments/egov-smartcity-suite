<!-- #-------------------------------------------------------------------------------
# eGov suite of products aim to improve the internal efficiency,transparency, 
#    accountability and the service delivery of the government  organizations.
# 
#     Copyright (C) <2015>  eGovernments Foundation
# 
#     The updated version of eGov suite of products as by eGovernments Foundation 
#     is available at http://www.egovernments.org
# 
#     This program is free software: you can redistribute it and/or modify
#     it under the terms of the GNU General Public License as published by
#     the Free Software Foundation, either version 3 of the License, or
#     any later version.
# 
#     This program is distributed in the hope that it will be useful,
#     but WITHOUT ANY WARRANTY; without even the implied warranty of
#     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#     GNU General Public License for more details.
# 
#     You should have received a copy of the GNU General Public License
#     along with this program. If not, see http://www.gnu.org/licenses/ or 
#     http://www.gnu.org/licenses/gpl.html .
# 
#     In addition to the terms of the GPL license to be adhered to in using this
#     program, the following additional terms are to be complied with:
# 
# 	1) All versions of this program, verbatim or modified must carry this 
# 	   Legal Notice.
# 
# 	2) Any misrepresentation of the origin of the material is prohibited. It 
# 	   is required that all modified versions of this material be marked in 
# 	   reasonable ways as different from the original version.
# 
# 	3) This license does not grant any rights to any user of the program 
# 	   with regards to rights under trademark law for use of the trade names 
# 	   or trademarks of eGovernments Foundation.
# 
#   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
#----------------------------------------------------------------------------- -->
<html>
<body>
<head>
<title>Dojo Implementation </title>

<script language="javascript" src="${pageContext.request.contextPath}/dojoJson/js/dojo/dojo.js"></script>
<script type="text/javascript">
   // dojo.hostenv.setModulePrefix('utils', 'utils');
    dojo.widget.manager.registerWidgetPackage('utils');
    dojo.require("utils.AutoComplete");


</script>

<style type="text/css">
.choices {
    background-color: lavender;
    color: black;
    border: solid #000000;
    border-width: 0px 1px 1px 1px;
    font: 10px arial;
    display: none;
    position: static !important;

}

.selected {
    background-color: #222222;
    color: white;
    border: solid #CCCCCC;
    border-width: 0px 1px 1px 1px;
    font: 10px arial;
    width: 100%;
}
</style>
</head>

<body>
<h1>enter the letter </h1>
<p>
Start typing the name of position:
</p>
<form  id="testForm">
<input type="hidden" style="width: 300px" name="beginsWith" id="beginsWith"/>
<input type="hidden" style="width: 300px" name="desId" id="desId" />
<input type="hidden" style="width: 300px" name="deptId" id="deptId" />
<input type="hidden" style="width: 300px" name="jurId" id="jurId" />
<input type="hidden" style="width: 300px" name="roleId" id="roleId" />
<input type="text" style="width: 300px" name="pos" id="pos"/>
<input type="button" value="Enter" onclick="alert('example for dojo auto complete');"/>
<dojo:AutoComplete formId="testForm"
		   textboxId="pos"		   
		   action="${pageContext.request.contextPath}/dojoJson/positions.jsp"/>


</form>
</body>
</html>

