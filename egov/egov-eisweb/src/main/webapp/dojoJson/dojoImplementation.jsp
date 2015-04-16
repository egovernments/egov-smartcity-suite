
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

