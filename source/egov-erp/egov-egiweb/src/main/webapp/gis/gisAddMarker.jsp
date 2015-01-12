<%@ include file="/includes/taglibs.jsp"%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<html>
<head>
    <title>Digitize Polygon</title>
    <script type="text/javascript">
        function DigitizePoint() {
 	  var mgsession='<%=request.getParameter("SESSION")%>';
          ShowResults("");
          parent.parent.mapFrame.DigitizePoint(OnPointDigitized);
        }

	function clearAll()
	{
	 var mgsession='<%=request.getParameter("SESSION")%>';
	 var cityName='<%=request.getParameter("DomainName")%>';
	 parent.parent.formFrame.Submit("/mapguide/"+cityName+"/clearGisMarker.jsp?SESSION="+mgsession+"&DomainName="+cityName,null,"scriptFrame");
	 DigitizePoint();
	}
        function OnPointDigitized(point) {
 	 var mgsession='<%=request.getParameter("SESSION")%>';
 	 var cityName='<%=request.getParameter("DomainName")%>';
 	 parent.parent.formFrame.Submit("/mapguide/"+cityName+"/showMarker.jsp?SESSION="+mgsession+"&DomainName="+cityName+"&resultString="+point.Y	+"^"+point.X+"@",null,"scriptFrame");
         ShowResults(point.X,point.Y);
        }
        
        function ShowResults(x,y)  {
           document.getElementById("lat").value = y;
           document.getElementById("lon").value = x;
        }
	
	function post_value(){
	if(document.getElementById('lat').value!="" && document.getElementById('lon').value!=""){
		top.parent.opener.document.getElementById('lat').value = document.getElementById('lat').value;
		top.parent.opener.document.getElementById('lon').value = document.getElementById('lon').value;
		top.parent.close();
	}else{
	alert("Please Add a Marker on map");
	}
	}

    </script>
 
</head>
<body onload="DigitizePoint();" >
<s:form  name="markerForm" theme="simple">
<tr><td>
    <input type="button" value="Add/Clear Marker" onclick="clearAll();" /></td>
    </tr>
<tr><td>
    <input type="hidden" id="lat" name="lat" value="" /></td><td>
    <input type="hidden" id="lon" name="lon" value="" /></td></tr>
<tr><td>
<input type="button" value='Submit' onclick="post_value();">
</td></tr>
</s:form>
</body>
</html>
