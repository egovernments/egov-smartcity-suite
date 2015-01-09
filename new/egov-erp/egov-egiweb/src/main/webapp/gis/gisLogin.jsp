<%@ include file="/includes/taglibs.jsp"%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<html>
	<head>
		<title>GIS MODULE SEARCH</title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link rel="stylesheet" type="text/css"
			href="<c:url value='/css/ccMenu.css'/>" />
		<link rel="stylesheet" type="text/css"
			href="<%=request.getContextPath() + "/css/egov.css"%>">
		<link href="common/css/portal.css" rel="stylesheet" type="text/css" />
		<link href="common/css/commonegovportal.css" rel="stylesheet"
			type="text/css" />

		<script type="text/javascript">
	function show()
	{

	var url=top.location;
	var arrParamValues=getURLParameters(url);

	 var mgsession='<%=request.getParameter("SESSION")%>';
	 var mode= arrParamValues[1];
	 if(mode=='ADDGISMARKER')
	 {
	window.location = "/egi/gis/gisAddMarker.jsp?SESSION="+mgsession+"&DomainName="+arrParamValues[0];
	 }
	else if(mode=='VIEWGISMARKER')
	 {setTimeout("viewMarker()", 10000);

	}
	else if(mode=='PTIS')
	 {
	window.location = "/ptis/search/gisSearchProperty!gisSearchForm.action?SESSION="+mgsession;
	}
	}

function viewMarker()
{var mgsession='<%=request.getParameter("SESSION")%>';
var url=top.location;
	var arrParamValues=getURLParameters(url);
parent.parent.formFrame.Submit("/mapguide/"+arrParamValues[0]+"/showMarker.jsp?SESSION="+mgsession+"&DomainName="+
arrParamValues[0]+"&resultString="+arrParamValues[2],null,"scriptFrame");
}

function getURLParameters(url) 
    {
        var sURL = url.toString();
        
        if (sURL.indexOf("?") > 0)
        {
            var arrParams = sURL.split("?");
                
            var arrURLParams = arrParams[1].split("&");
           
           var arrParamNames = new Array(arrURLParams.length);
           var arrParamValues = new Array(arrURLParams.length);
           
           var i = 0;
           for (i=0;i<arrURLParams.length;i++)
           {
               var sParam =  arrURLParams[i].split("=");
               arrParamNames[i] = sParam[0];
               if (sParam[1] != "")
                   arrParamValues[i] = unescape(sParam[1]);
               else
                   arrParamValues[i] = "No Value";
           }
           
         //  for (i=0;i<arrURLParams.length;i++)
         //  {
        //      alert(arrParamNames[i]+" = "+ arrParamValues[i]);
          // }
	return arrParamValues;
       }
       else
       {
           alert("No parameters.");
       }

   }
</script>
	</head>
	<body onLoad="show();">
		<div class="formmainbox">

			<table width="320px" border="0">
				<s:form action="gisSelect" name="selectModule" theme="simple">
				<div class="headingbg"
							style="font-weight: bold; font-size: 13px; text-align: center;">
					<center>
							View Marker
					</center>
					</div>
								
				</s:form>
			</table>
			</div>
	</body>
</html>
