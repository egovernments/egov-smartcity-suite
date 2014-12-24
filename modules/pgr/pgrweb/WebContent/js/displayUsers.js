var codes;
var oAC;

 function onBodyLoad()
 {
   loadEmpCodes();
 }


function displayUsers(deptObj)
{
		var bndryId=0;
		//Right now, there are only two dropdowns - Zone and Ward.
		//This code has to be made generic, if there are more dropdowns.
		if(document.getElementById('adminBndry1')!= null && document.getElementById('adminBndry1').value != 0)
			bndryId=document.getElementById('adminBndry1').value;
		if(document.getElementById('adminBndry2')!= null && document.getElementById('adminBndry2').value != 0)
			bndryId=document.getElementById('adminBndry2').value;
	if(deptObj != null)
	{
			loadEmpCodes(deptObj,bndryId);
		}

}
 function loadEmpCodes(deptObj,bndryId)
 {
		var deptId;
		deptId = deptObj.value;
 		var url="/pgr/citizen/getAllUsersForBndry.jsp";
		url=url+"?deptId="+deptId;
		url=url+"&bndryId="+bndryId;
		var req2 = initiateRequest();
		req2.open("GET", url, true);
	 req2.send(null);

		var userId = new Array();
		var userName = new Array();
		 req2.onreadystatechange = function()
		 {
				  if (req2.readyState == 4)
				  {
					  if (req2.status == 200)
					  {
						var response=req2.responseText;
						//alert(response);
						var values=response.split("!$");

						var result = values[0].split("^");
						userId = result[0].split("+");
						userName = result[1].split("+");
						for(var k = 1 ; k <= userId.length  ; k++)
						{
							if(document.getElementById('fwdRedressalOfficerID') != null && userId != "")
								document.getElementById('fwdRedressalOfficerID').options[k] = new Option(userName[k-1],userId[k-1]);
						}
					  }
				  }
		};

 }


function initiateRequest()
 {

        if (window.XMLHttpRequest) {

            return new XMLHttpRequest();

        } else if (window.ActiveXObject) {

            isIE = true;

            return new ActiveXObject("Microsoft.XMLHTTP");

        }

 }

function clearUsers()
{
		//alert("Inside ClearUsers");
		document.getElementById('fwdRedressalOfficerID').options.length = 0;
		document.getElementById('fwdRedressalOfficerID').options[0] = new Option(document.getElementById('Choose').getAttribute('value'),0,true,true);
}
function setUser(user)
{
document.getElementById("fwdRedressalOfficerID").value=user;
}

