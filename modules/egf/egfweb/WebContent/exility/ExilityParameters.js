//<script language=javascript>
// parameters to be set for a gievn installation for Exility


var urlStr = document.location.href;
//alert("urlStr = " + urlStr);
urlLen = urlStr.length;


if(urlStr.indexOf("http://") > -1)
		{
			urlStr = urlStr.substring(urlStr.indexOf("http://")+"http://".length,urlLen);
			//alert("urlStr AGAIN" + urlStr);
			urlLenHTTP = urlStr.indexOf("http://");
			//alert("urlLenHTTP : " + urlLenHTTP);
			urlLenHTTP = urlStr.indexOf("/");
			
			urlStr = urlStr.substring(urlStr.indexOf("/")+"/".length,urlLen);
			urlStr = urlStr.substring(urlStr.indexOf("/")+"/".length,urlLen);
			//alert("urlStr finally : " + urlStr);
		}

var totOcc = 0;
var urlLn=0;
urlLn=urlStr.indexOf("?");
if (urlLn==-1) urlLn=urlStr.length;
//alert("&:"+urlLn);
for (var p=1;p<urlLn;p++)
{
	var x=urlStr.charAt(p);
	if (x == "/")
	{
		totOcc++;
	}	
}
//alert(totOcc);


var exilParms = new Object(); 
var pathResol = "../";
for (var i=1;i<totOcc;i++ )
{
	pathResol = pathResol+ pathResol;
}
//alert(pathResol);

//All Descriotion Service will be requested to this URL
exilParms.descServiceName	= pathResol+"GetDescription.jsp";
//All List Service will be requested to this URL
exilParms.listServiceName	=  pathResol+"GetList.jsp";
//All List Service will be requested to this URL
exilParms.treeServiceName	=  pathResol+"GetTree.jsp";

// If this is set to non-null string, all update services will be
// sent to this with ?serviceID=xxxxxx. Else the serviceID is assumed to be the URL
exilParms.updateServiceName	=  pathResol+"SubmitData.jsp"; 
// similar meaning foor Data service
exilParms.dataServiceName	=  pathResol+"GetData.jsp";
exilParms.minDate = new Date("January 01, 1900"); 
exilParms.maxDate = new Date("January 01, 2100");
exilParms.verbose = false;
//value to be sent to server when a check box is checked/unchecked
exilParms.checkedValue = 1;
exilParms.uncheckedValue = 0;

//</script>