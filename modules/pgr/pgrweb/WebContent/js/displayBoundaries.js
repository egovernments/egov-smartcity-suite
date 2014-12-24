function initiateRequest()
 {

        if (window.XMLHttpRequest) {

            return new XMLHttpRequest();

        } else if (window.ActiveXObject) {

            isIE = true;

            return new ActiveXObject("Microsoft.XMLHTTP");

        }

 }
function displayBoundaries()
{
		//alert("Inside displayBoundaries");	

		var request = initiateRequest();
		if (request==null)
		{
			alert ("Your browser does not support AJAX!");
			return;
		}
		var url="/pgr/citizen/getAllBoundaries.jsp";	
		request.onreadystatechange=stateChanged;
		request.open("GET",url,true);
		request.send(null);

		var bndryId = new Array();
		var bndryName = new Array();

		function stateChanged()
		{
			if (request.readyState==4)
			{
				 if (request.status == 200)
				 {
					var response=request.responseText;
					//alert(response);
					var values=response.split("!$");
					var result = values[0].split("^");
					bndryId = result[0].split("+");
					bndryName = result[1].split("+");									
					for(var k = 1 ; k <= bndryId.length  ; k++)
					{	
						if(document.forms[0].adminBndry1 != null)
							document.forms[0].adminBndry1.options[k] = new Option(bndryName[k-1],bndryId[k-1]);						
					}
				 }
			}
		}  	
}
