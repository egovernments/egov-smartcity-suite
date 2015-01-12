  //Program Name : balloons.js
  //Purpose : To add balloons on specified boundary.
  
   if(GBrowserIsCompatible())
   {
	   balloonMarkersArr =[];
	   balloonMarkersTalukArr =[];
	   function loadBiharDarbhangaTalukBalloonsNREGA()
	   {
		   for(var i=0;i<18;i++)
		   {
			  if(i==0)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=r81C2vlQQu4Udn46gP46bDQ&oid=5&output=image";
				point = new GLatLng(26.386997224900842,85.7380027098672);
				createMarkerTaluk(point,"Jale",districtChartURL,i);
			  }
			  else if(i==1)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=r81C2vlQQu4Udn46gP46bDQ&oid=6&output=image";
				point = new GLatLng(26.265741505165224,85.73809154047852);
				createMarkerTaluk(point,"Singhwara ",districtChartURL,i);
			  }
			  else if(i==2)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=r81C2vlQQu4Udn46gP46bDQ&oid=7&output=image";
				point = new GLatLng(26.323589500292414,85.79963206083438);
				createMarkerTaluk(point,"Keotiranway",districtChartURL,i);
			  }
			  else if(i==3)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=r81C2vlQQu4Udn46gP46bDQ&oid=8&output=image";
				point = new GLatLng(26.21371110885456,85.80872303137886);
				createMarkerTaluk(point,"Darbhanga",districtChartURL,i);
			  }
			  else if(i==4)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=r81C2vlQQu4Udn46gP46bDQ&oid=9&output=image";
				point = new GLatLng(26.28131069328013,85.96043466570202);
				createMarkerTaluk(point,"Manigachhi",districtChartURL,i);
			  }
			  else if(i==5)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=r81C2vlQQu4Udn46gP46bDQ&oid=10&output=image";
				point = new GLatLng(26.17298339544972,86.22311093219525);
				createMarkerTaluk(point,"Tardih",districtChartURL,i);
			  }
			  else if(i==6)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=r81C2vlQQu4Udn46gP46bDQ&oid=11&output=image";
				point = new GLatLng(26.06756565789112,86.09052204057787);
				createMarkerTaluk(point,"Alinagar",districtChartURL,i);
			  }
			  else if(i==7)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=r81C2vlQQu4Udn46gP46bDQ&oid=12&output=image";
				point = new GLatLng(26.135953315542512,86.00136379917818);
				createMarkerTaluk(point,"Benipur",districtChartURL,i);
			  }
			  else if(i==8)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=r81C2vlQQu4Udn46gP46bDQ&oid=13&output=image";
				point = new GLatLng(26.184029456384494,85.90329570326954);
				createMarkerTaluk(point,"Bahadurpur",districtChartURL,i);
			  }
			  else if(i==9)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=r81C2vlQQu4Udn46gP46bDQ&oid=14&output=image";
				point = new GLatLng(26.08800606598414,85.78561735497173);
				createMarkerTaluk(point,"Hanuman Nagar",districtChartURL,i);
			  }
			  else if(i==10)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=r81C2vlQQu4Udn46gP46bDQ&oid=15&output=image";
				point = new GLatLng(26.053409551305517,85.90667845153995);
				createMarkerTaluk(point,"Hayaghat",districtChartURL,i);
			  }
			  else if(i==11)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=r81C2vlQQu4Udn46gP46bDQ&oid=16&output=image";
				point = new GLatLng( 25.977890406062187,85.99236472398172);
				createMarkerTaluk(point,"Baheri",districtChartURL,i);
			  }
			  else if(i==12)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=r81C2vlQQu4Udn46gP46bDQ&oid=17&output=image";
				point = new GLatLng(25.97955680485015,86.1079986580477);
				createMarkerTaluk(point,"Biraul",districtChartURL,i);
			  }
			  else if(i==13)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=r81C2vlQQu4Udn46gP46bDQ&oid=18&output=image";
				point = new GLatLng(26.158849111826306,86.12636504696609);
				createMarkerTaluk(point,"Ghanshyampur",districtChartURL,i);
			  }
			  else if(i==14)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=r81C2vlQQu4Udn46gP46bDQ&oid=19&output=image";
				point = new GLatLng(26.09299756637272,86.25128192487105);
				createMarkerTaluk(point,"Kiratpur",districtChartURL,i);
			  }
			  else if(i==15)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=r81C2vlQQu4Udn46gP46bDQ&oid=20&output=image";
				point = new GLatLng(25.944183065290936,86.18759474329111);
				createMarkerTaluk(point,"Gora Bauram",districtChartURL,i);
			  }
			  else if(i==16)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=r81C2vlQQu4Udn46gP46bDQ&oid=21&output=image";
				point = new GLatLng(25.996778927343343,86.27598074016329);
				createMarkerTaluk(point,"Kusheshwar Asthan",districtChartURL,i);
			  }
			  else if(i==17)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=r81C2vlQQu4Udn46gP46bDQ&oid=22&output=image";
				point = new GLatLng(25.97626795471254,86.34123498643);
				createMarkerTaluk(point,"Kusheshwar Asthan (East)",districtChartURL,i);
			  }
		   }
	   }
	   
	   
	   function loadKarnatakaDistBalloonsNREGA()
	   {
		   for(var i=0;i<28;i++)
		   {
			  if(i==0)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=rdpYareG7wOsHbMP8s9Z28w&oid=25&output=image";
				point = new GLatLng(16.219999999999999,75.629999999999995);
				createMarker(point,"Bagalkote",districtChartURL,i);
			  }
			  else if(i==1)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=rdpYareG7wOsHbMP8s9Z28w&oid=26&output=image";
				point = new GLatLng(12.76,77.540000000000006);
				createMarker(point,"Bangalore ",districtChartURL,i);
			  }
			  else if(i==2)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=rdpYareG7wOsHbMP8s9Z28w&oid=27&output=image";
				point = new GLatLng(13.039999999999999,77.420000000000002);
				createMarker(point,"Bangalore Rural",districtChartURL,i);
			  }
			  else if(i==3)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=rdpYareG7wOsHbMP8s9Z28w&oid=6&output=image";
				point = new GLatLng(16.120000000000001,74.819999999999993);
				createMarker(point,"Belgaum",districtChartURL,i);
			  }
			  else if(i==4)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=rdpYareG7wOsHbMP8s9Z28w&oid=28&output=image";
				point = new GLatLng(15.109999999999999,76.530000000000001);
				createMarker(point,"Bellary",districtChartURL,i);
			  }
			  else if(i==5)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=rdpYareG7wOsHbMP8s9Z28w&oid=9&output=image";
				point = new GLatLng(17.949999999999999,77.230000000000004);
				createMarker(point,"Bidar",districtChartURL,i);
			  }
			  else if(i==6)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=rdpYareG7wOsHbMP8s9Z28w&oid=29&output=image";
				point = new GLatLng(16.789999999999999,75.950000000000003);
				createMarker(point,"Bijapur",districtChartURL,i);
			  }
			  else if(i==7)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=rdpYareG7wOsHbMP8s9Z28w&oid=30&output=image";
				point = new GLatLng(11.960000000000001,77.099999999999994);
				createMarker(point,"Chamaraja Nagara",districtChartURL,i);
			  }
			  //else if(i==9)
			  //{
				//districtChartURL ="http://spreadsheets.google.com/pub?key=rdpYareG7wOsHbMP8s9Z28w&oid=31&output=image";
				//point = new GLatLng(14.45372,75.91893);
				//createMarker(point,"Chikkaballapura",districtChartURL,i);
			  //}
			  else if(i==8)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=rdpYareG7wOsHbMP8s9Z28w&oid=32&output=image";
				point = new GLatLng(13.449999999999999,75.689999999999998);
				createMarker(point,"Chikmagalur",districtChartURL,i);
			  }
			  else if(i==9)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=rdpYareG7wOsHbMP8s9Z28w&oid=33&output=image";
				point = new GLatLng(14.17,76.519999999999996);
				createMarker(point,"Chitradurga",districtChartURL,i);
			  }
			  else if(i==10)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=rdpYareG7wOsHbMP8s9Z28w&oid=34&output=image";
				point = new GLatLng(12.84,75.269999999999996);
				createMarker(point,"Dakshina Kannada",districtChartURL,i);
			  }
			  else if(i==11)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=rdpYareG7wOsHbMP8s9Z28w&oid=7&output=image";
				point = new GLatLng( 14.41,75.950000000000003);
				createMarker(point,"Davanagere",districtChartURL,i);
			  }
			  else if(i==12)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=rdpYareG7wOsHbMP8s9Z28w&oid=35&output=image";
				point = new GLatLng(15.279999999999999,75.099999999999994);
				createMarker(point,"Dharwar",districtChartURL,i);
			  }
			  else if(i==13)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=rdpYareG7wOsHbMP8s9Z28w&oid=36&output=image";
				point = new GLatLng(15.449999999999999,75.609999999999999);
				createMarker(point,"Gadag",districtChartURL,i);
			  }
			  else if(i==14)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=rdpYareG7wOsHbMP8s9Z28w&oid=8&output=image";
				point = new GLatLng(17.050000000000001,76.879999999999995);
				createMarker(point,"Gulbarga",districtChartURL,i);
			  }
			  else if(i==15)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=rdpYareG7wOsHbMP8s9Z28w&oid=37&output=image";
				point = new GLatLng(13,76.109999999999999);
				createMarker(point,"Hassan",districtChartURL,i);
			  }
			  else if(i==16)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=rdpYareG7wOsHbMP8s9Z28w&oid=38&output=image";
				point = new GLatLng(14.699999999999999,75.450000000000003);
				createMarker(point,"Haveri",districtChartURL,i);
			  }
			  else if(i==17)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=rdpYareG7wOsHbMP8s9Z28w&oid=39&output=image";
				point = new GLatLng(12.32,75.799999999999997);
				createMarker(point,"Kodagu",districtChartURL,i);
			  }
			  else if(i==18)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=rdpYareG7wOsHbMP8s9Z28w&oid=10&output=image";
				point = new GLatLng(13.369999999999999,78.019999999999996);
				createMarker(point,"Kolar",districtChartURL,i);
			  }
			  else if(i==19)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=rdpYareG7wOsHbMP8s9Z28w&oid=40&output=image";
				point = new GLatLng(15.56,76.219999999999999);
				createMarker(point,"Koppal",districtChartURL,i);
			  }
			  else if(i==20)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=rdpYareG7wOsHbMP8s9Z28w&oid=41&output=image";
				point = new GLatLng(12.609999999999999,76.799999999999997);
				createMarker(point,"Mandya",districtChartURL,i);
			  }
			  else if(i==21)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=rdpYareG7wOsHbMP8s9Z28w&oid=11&output=image";
				point = new GLatLng(12.210000000000001,76.439999999999998);
				createMarker(point,"Mysore",districtChartURL,i);
			  }
			  else if(i==22)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=rdpYareG7wOsHbMP8s9Z28w&oid=12&output=image";
				point = new GLatLng( 16.09,76.890000000000001);
				createMarker(point,"Raichur",districtChartURL,i);
			  }
			  else if(i==23)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=rdpYareG7wOsHbMP8s9Z28w&oid=42&output=image";
				point = new GLatLng(12.71454,77.276718);
				createMarker(point,"Ramanagara",districtChartURL,i);
			  }
			  else if(i==24)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=rdpYareG7wOsHbMP8s9Z28w&oid=13&output=image";
				point = new GLatLng(14.039999999999999,75.219999999999999);
				createMarker(point,"Shimoga",districtChartURL,i);
			  }
			  else if(i==25)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=rdpYareG7wOsHbMP8s9Z28w&oid=14&output=image";
				point = new GLatLng(13.43,76.900000000000006);
				createMarker(point,"Tumkur",districtChartURL,i);
			  }
			  else if(i==26)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=rdpYareG7wOsHbMP8s9Z28w&oid=15&output=image";
				point = new GLatLng(13.470000000000001,74.879999999999995);
				createMarker(point,"Udupi",districtChartURL,i);
			  }
			  else if(i==27)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=rdpYareG7wOsHbMP8s9Z28w&oid=43&output=image";
				point = new GLatLng(14.789999999999999,74.620000000000005);
				createMarker(point,"Uttara Kannada",districtChartURL,i);
			  }
		  
		  }
	   }
	   
	   function loadBiharDistBalloonsNREGA()
	   {
		   for(var i=0;i<37;i++)
		   {
			  if(i==0)//
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=rP6HRPFPs2PWDRV1I5dRQYA&oid=3&output=image";
				point = new GLatLng(26.1343946,87.4628736);
				createMarker(point,"Araria",districtChartURL,i);
			  }
			  else if(i==1)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=rP6HRPFPs2PWDRV1I5dRQYA&oid=5&output=image";
				point = new GLatLng(24.74839, 84.376663);
				createMarker(point,"Aurangabad",districtChartURL,i);
			  }
			  else if(i==2)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=rP6HRPFPs2PWDRV1I5dRQYA&oid=6&output=image";
				point = new GLatLng(24.87892,86.914803);
				createMarker(point,"Banka",districtChartURL,i);
			  }
			  else if(i==3)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=rP6HRPFPs2PWDRV1I5dRQYA&oid=7&output=image";
				point = new GLatLng(25.41301,86.124077);
				createMarker(point,"Begusarai",districtChartURL,i);
			  }
			  else if(i==4)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=rP6HRPFPs2PWDRV1I5dRQYA&oid=8&output=image";
				point = new GLatLng(25.24003,86.984512);
				createMarker(point,"Bhagalpur",districtChartURL,i);
			  }
			  else if(i==5)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=rP6HRPFPs2PWDRV1I5dRQYA&oid=9&output=image";
				point = new GLatLng(25.466155,84.5222189);
				createMarker(point,"Bhojpur",districtChartURL,i);
			  }
			  else if(i==6)
			  {

				districtChartURL ="http://spreadsheets.google.com/pub?key=rP6HRPFPs2PWDRV1I5dRQYA&oid=10&output=image";
				point = new GLatLng(25.56024,83.982384);
				createMarker(point,"Buxur",districtChartURL,i);
			  }
			  else if(i==7)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=rP6HRPFPs2PWDRV1I5dRQYA&oid=11&output=image";
				point = new GLatLng(26.149179, 85.890587);
				createMarker(point,"Darbhanga",districtChartURL,i);
			  }
			  else if(i==8)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=rP6HRPFPs2PWDRV1I5dRQYA&oid=12&output=image";
				point = new GLatLng(24.791121,84.996353);
				createMarker(point,"Gaya",districtChartURL,i);
			  }
			  //else if(i==9)
			  //{
				//districtChartURL ="http://spreadsheets.google.com/pub?key=rP6HRPFPs2PWDRV1I5dRQYA&oid=4&output=image";
				//point = new GLatLng(36.666667, 70.91667);
				//createMarker(point,"Arwal",districtChartURL,i);
			  //}
			  else if(i==9)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=rP6HRPFPs2PWDRV1I5dRQYA&oid=13&output=image";
				point = new GLatLng(26.440000000000001, 84.379999999999995);
				createMarker(point,"Gopalganj",districtChartURL,i);
			  }
			  else if(i==10)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=rP6HRPFPs2PWDRV1I5dRQYA&oid=22&output=image";
				point = new GLatLng(24.780000000000001, 86.299999999999997);
				createMarker(point,"Jamui",districtChartURL,i);
			  }
			  else if(i==11)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=rP6HRPFPs2PWDRV1I5dRQYA&oid=23&output=image";
				point = new GLatLng(25.149999999999999, 84.890000000000001);
				createMarker(point,"Jehanabad",districtChartURL,i);
			  }
              else if(i==12)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=rP6HRPFPs2PWDRV1I5dRQYA&oid=24&output=image";
				point = new GLatLng(24.989999999999998, 83.609999999999999);
				createMarker(point,"Kaimur (Bhabua)",districtChartURL,i);
			  }
              else if(i==13)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=rP6HRPFPs2PWDRV1I5dRQYA&oid=25&output=image";
				point = new GLatLng(25.539999999999999, 87.650000000000006);
				createMarker(point,"Katihar",districtChartURL,i);
			  }
			  else if(i==14)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=rP6HRPFPs2PWDRV1I5dRQYA&oid=26&output=image";
				point = new GLatLng(25.510000000000002, 86.599999999999994);
				createMarker(point,"Khagaria",districtChartURL,i);
			  }
			  else if(i==15)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=rP6HRPFPs2PWDRV1I5dRQYA&oid=27&output=image";
				point = new GLatLng(26.106235, 87.956631);
				createMarker(point,"Kishanganj",districtChartURL,i);
			  }
			  else if(i==16)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=rP6HRPFPs2PWDRV1I5dRQYA&oid=28&output=image";
				point = new GLatLng(25.171709, 86.103889);
				createMarker(point,"Lakhisarai",districtChartURL,i);
			  }
			 else if(i==17)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=rP6HRPFPs2PWDRV1I5dRQYA&oid=29&output=image";
				point = new GLatLng(25.82, 86.900000000000006);
				createMarker(point,"Madhepura",districtChartURL,i);
			  }
			else if(i==18)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=rP6HRPFPs2PWDRV1I5dRQYA&oid=30&output=image";
				point = new GLatLng(26.399999999999999, 86.219999999999999);
				createMarker(point,"Madhubani",districtChartURL,i);
			  }
			else if(i==19)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=rP6HRPFPs2PWDRV1I5dRQYA&oid=31&output=image";
				point = new GLatLng(25.210000000000001, 86.530000000000001);
				createMarker(point,"Munger",districtChartURL,i);
			  }
              else if(i==20)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=rP6HRPFPs2PWDRV1I5dRQYA&oid=32&output=image";
				point = new GLatLng(26.149999999999999, 85.329999999999998);
				createMarker(point,"Muzaffarpur",districtChartURL,i);
			  }
              else if(i==21)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=rP6HRPFPs2PWDRV1I5dRQYA&oid=33&output=image";
				point = new GLatLng(25.219999999999999, 85.459999999999994);
				createMarker(point,"Nalanda",districtChartURL,i);
			  }
			  else if(i==22)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=rP6HRPFPs2PWDRV1I5dRQYA&oid=34&output=image";
				point = new GLatLng(24.809999999999999, 85.590000000000003);
				createMarker(point,"Nawada",districtChartURL,i);
			  }
			else if(i==23)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=rP6HRPFPs2PWDRV1I5dRQYA&oid=35&output=image";
				point = new GLatLng(27.079999999999998, 84.340000000000003);
				createMarker(point,"Pashchim Champaran",districtChartURL,i);
			  }
			  else if(i==24)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=rP6HRPFPs2PWDRV1I5dRQYA&oid=36&output=image";
				point = new GLatLng(25.449999999999999, 85.219999999999999);
				createMarker(point,"Patna",districtChartURL,i);
			  }
			 else if(i==25)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=rP6HRPFPs2PWDRV1I5dRQYA&oid=37&output=image";
				point = new GLatLng(26.6098139, 84.8567932);
				createMarker(point,"Purba Champaran",districtChartURL,i);
			  }
			 else if(i==26)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=rP6HRPFPs2PWDRV1I5dRQYA&oid=38&output=image";
				point = new GLatLng(25.77618, 87.466103);
				createMarker(point,"Purnia",districtChartURL,i);
			  }
			else if(i==27)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=rP6HRPFPs2PWDRV1I5dRQYA&oid=39&output=image";
				point = new GLatLng(24.6834395, 83.8473015);
				createMarker(point,"Rohtas",districtChartURL,i);
			  }
			else if(i==28)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=rP6HRPFPs2PWDRV1I5dRQYA&oid=40&output=image";
				point = new GLatLng(25.869841, 86.592918);
				createMarker(point,"Saharsa",districtChartURL,i);
			  }
			else if(i==29)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=rP6HRPFPs2PWDRV1I5dRQYA&oid=41&output=image";
				point = new GLatLng(25.853809, 85.779114);
				createMarker(point,"Samastipur",districtChartURL,i);
			  }
			  else if(i==30)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=rP6HRPFPs2PWDRV1I5dRQYA&oid=42&output=image";
				point = new GLatLng(25.899999999999999, 84.829999999999998);
				createMarker(point,"Saran",districtChartURL,i);
			  }
			else if(i==31)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=rP6HRPFPs2PWDRV1I5dRQYA&oid=43&output=image";
				point = new GLatLng(25.142491, 85.85173);
				createMarker(point,"Sheikhpura",districtChartURL,i);
			  }
			  else if(i==32)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=rP6HRPFPs2PWDRV1I5dRQYA&oid=44&output=image";
				point = new GLatLng(26.51667, 85.3);
				createMarker(point,"Sheohar",districtChartURL,i);
			  }
			else if(i==33)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=rP6HRPFPs2PWDRV1I5dRQYA&oid=45&output=image";
				point = new GLatLng(26.59807, 85.495323);
				createMarker(point,"Sitamarhi",districtChartURL,i);
			  }
             else if(i==34)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=rP6HRPFPs2PWDRV1I5dRQYA&oid=47&output=image";
				point = new GLatLng(26.116461, 86.590317);
				createMarker(point,"Supaul",districtChartURL,i);
			  }
			else if(i==35)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=rP6HRPFPs2PWDRV1I5dRQYA&oid=46&output=image";
				point = new GLatLng(26.16, 84.390000000000001);
				createMarker(point,"Siwan",districtChartURL,i);
			  }
			  else if(i==36)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=rP6HRPFPs2PWDRV1I5dRQYA&oid=48&output=image";
				point = new GLatLng(25.6838206, 85.354965);
				createMarker(point,"Vaishali",districtChartURL,i);
		      }
			}
	   }
       
	   function loadKarnatakaDistBalloonsIAY()
	   {
		   for(var i=0;i<28;i++)
		   {
			  if(i==0)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=rtw_lzB1QmpMJNfyDoRVl7Q&oid=1&output=image";
				point = new GLatLng(16.185011,75.696243);
				createMarker(point,"Bagalkot",districtChartURL,i);
			  }
			  else if(i==1)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=rtw_lzB1QmpMJNfyDoRVl7Q&oid=2&output=image";
				point = new GLatLng(14.22209,76.40036);
				createMarker(point,"Chitradurga",districtChartURL,i);
			  }
			  else if(i==2)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=rtw_lzB1QmpMJNfyDoRVl7Q&oid=4&output=image";
				point = new GLatLng(12.971606,77.594376);
				createMarker(point,"Bangalore Urban",districtChartURL,i);
			  }
			  else if(i==3)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=rtw_lzB1QmpMJNfyDoRVl7Q&oid=5&output=image";
				point = new GLatLng(15.13854,76.918732);
				createMarker(point,"Bellary",districtChartURL,i);
			  }
			  else if(i==4)
			  {

				districtChartURL ="http://spreadsheets.google.com/pub?key=rtw_lzB1QmpMJNfyDoRVl7Q&oid=6&output=image";
				point = new GLatLng(17.90737,77.527138);
				createMarker(point,"Bidar",districtChartURL,i);
			  }
			  else if(i==5)
			  {

				districtChartURL ="http://spreadsheets.google.com/pub?key=rtw_lzB1QmpMJNfyDoRVl7Q&oid=7&output=image";
				point = new GLatLng(12.8437814,75.2479061);
				createMarker(point,"Dakshina Kannada",districtChartURL,i);
			  }
			  else if(i==6)
			  {

				districtChartURL ="http://spreadsheets.google.com/pub?key=rtw_lzB1QmpMJNfyDoRVl7Q&oid=8&output=image";
				point = new GLatLng(12.3374942,75.8069082);
				createMarker(point,"Kodagu",districtChartURL,i);
			  }
			  else if(i==7)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=rtw_lzB1QmpMJNfyDoRVl7Q&oid=9&output=image";
				point = new GLatLng(15.33957,76.153347);
				createMarker(point,"Koppal",districtChartURL,i);
			  }
			  else if(i==8)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=rtw_lzB1QmpMJNfyDoRVl7Q&oid=10&output=image";
				point = new GLatLng(13.338581,77.101219);
				createMarker(point,"Tumkur",districtChartURL,i);
			  }
			  else if(i==9)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=rtw_lzB1QmpMJNfyDoRVl7Q&oid=11&output=image";
				point = new GLatLng(13.33232,74.746048);
				createMarker(point,"Udupi",districtChartURL,i);
			  }
              else if(i==10)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=rtw_lzB1QmpMJNfyDoRVl7Q&oid=13&output=image";
				point = new GLatLng(13.039999999999999,77.420000000000002);
				createMarker(point,"Bangalore Rural",districtChartURL,i);
			  }
			 else if(i==11)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=rtw_lzB1QmpMJNfyDoRVl7Q&oid=14&output=image";
				point = new GLatLng(15.85036,74.504669);
				createMarker(point,"Belgaum",districtChartURL,i);
			  }
              else if(i==12)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=rtw_lzB1QmpMJNfyDoRVl7Q&oid=15&output=image";
				point = new GLatLng(16.82402,75.71534);
				createMarker(point,"Bijapur",districtChartURL,i);
			  }
			  else if(i==13)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=rtw_lzB1QmpMJNfyDoRVl7Q&oid=16&output=image";
				point = new GLatLng(11.92,76.95);
				createMarker(point,"chamaraja nagara",districtChartURL,i);
			  }
              else if(i==14)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=rtw_lzB1QmpMJNfyDoRVl7Q&oid=18&output=image";
				point = new GLatLng(13.31232,75.770889);
				createMarker(point,"Chikmagalur",districtChartURL,i);
			  }
              else if(i==15)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=rtw_lzB1QmpMJNfyDoRVl7Q&oid=19&output=image";
				point = new GLatLng(15.463846,75.0026972);
				createMarker(point,"Dharwad",districtChartURL,i);
			  }
               else if(i==16)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=rtw_lzB1QmpMJNfyDoRVl7Q&oid=20&output=image";
				point = new GLatLng(14.41,75.950000000000003);
				createMarker(point,"Davanagere",districtChartURL,i);
			  }
              else if(i==17)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=rtw_lzB1QmpMJNfyDoRVl7Q&oid=21&output=image";
				point = new GLatLng(15.449999999999999,75.609999999999999);
				createMarker(point,"Gadag",districtChartURL,i);
			  }
			else if(i==18)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=rtw_lzB1QmpMJNfyDoRVl7Q&oid=22&output=image";
				point = new GLatLng(17.332939,76.830452);
				createMarker(point,"Gulbarga",districtChartURL,i);
			  }
              else if(i==19)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=rtw_lzB1QmpMJNfyDoRVl7Q&oid=23&output=image";
				point = new GLatLng(13.0072,76.094543);
				createMarker(point,"Hassan",districtChartURL,i);
			  }
              else if(i==20)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=rtw_lzB1QmpMJNfyDoRVl7Q&oid=24&output=image";
				point = new GLatLng(14.699999999999999,75.450000000000003);
				createMarker(point,"Haveri",districtChartURL,i);
			  }
               else if(i==21)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=rtw_lzB1QmpMJNfyDoRVl7Q&oid=25&output=image";
				point = new GLatLng(13.1363,78.136391);
				createMarker(point,"Kolar",districtChartURL,i);
			  }
               else if(i==22)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=rtw_lzB1QmpMJNfyDoRVl7Q&oid=26&output=image";
				point = new GLatLng(12.52276,76.897133);
				createMarker(point,"Mandya",districtChartURL,i);
			  }
               else if(i==23)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=rtw_lzB1QmpMJNfyDoRVl7Q&oid=27&output=image";
				point = new GLatLng(12.3033,76.645866);
				createMarker(point,"Mysore",districtChartURL,i);
			  }
              else if(i==24)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=rtw_lzB1QmpMJNfyDoRVl7Q&oid=28&output=image";
				point = new GLatLng(16.20702,77.354362);
				createMarker(point,"Raichur",districtChartURL,i);
			  }
              else if(i==25)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=rtw_lzB1QmpMJNfyDoRVl7Q&oid=29&output=image";
				point = new GLatLng(12.71454,77.276718);
				createMarker(point,"Ramanagara",districtChartURL,i);
			  }
              else if(i==26)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=rtw_lzB1QmpMJNfyDoRVl7Q&oid=30&output=image";
				point = new GLatLng(13.92225,75.570374);
				createMarker(point,"Shimoga",districtChartURL,i);
			  }
              else if(i==27)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=rtw_lzB1QmpMJNfyDoRVl7Q&oid=31&output=image";
				point = new GLatLng(14.789999999999999,74.620000000000005);
				createMarker(point,"Uttara Kannada",districtChartURL,i);
			  }
              //else if(i==28)
			  //{
				//districtChartURL ="http://spreadsheets.google.com/pub?key=rtw_lzB1QmpMJNfyDoRVl7Q&oid=17&output=image";
				//point = new GLatLng(13.4239948,77.7309012);
				//createMarker(point,"chik ballapur",districtChartURL,i);
			  //}
		  }
	   }

       function loadBiharDistBalloonsIAY()
	   {
		   for(var i=0;i<37;i++)
		   {
			  if(i==0)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=rXZuJdw2tyHb3hI8w_z5QIw&oid=1&output=image";
				point = new GLatLng(26.1343946,87.4628736);
				createMarker(point,"Araria",districtChartURL,i);
			  }
			  else if(i==1)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=rXZuJdw2tyHb3hI8w_z5QIw&oid=3&output=image";
				point = new GLatLng(24.74839, 84.376663);
				createMarker(point,"Aurangabad",districtChartURL,i);
			  }
			  else if(i==2)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=rXZuJdw2tyHb3hI8w_z5QIw&oid=4&output=image";
				point = new GLatLng(24.87892,86.914803);
				createMarker(point,"Banka",districtChartURL,i);
			  }
			  else if(i==3)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=rXZuJdw2tyHb3hI8w_z5QIw&oid=5&output=image";
				point = new GLatLng(25.41301,86.124077);
				createMarker(point,"Begusarai",districtChartURL,i);
			  }
			  else if(i==4)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=rXZuJdw2tyHb3hI8w_z5QIw&oid=6&output=image";
				point = new GLatLng(25.24003,86.984512);
				createMarker(point,"Bhagalpur",districtChartURL,i);
			  }
			  else if(i==5)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=rXZuJdw2tyHb3hI8w_z5QIw&oid=7&output=image";
				point = new GLatLng(25.466155,84.5222189);
				createMarker(point,"Bhojpur",districtChartURL,i);
			  }
			  else if(i==6)
			  {

				districtChartURL ="http://spreadsheets.google.com/pub?key=rXZuJdw2tyHb3hI8w_z5QIw&oid=8&output=image";
				point = new GLatLng(25.56024,83.982384);
				createMarker(point,"Buxur",districtChartURL,i);
			  }
			  else if(i==7)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=rXZuJdw2tyHb3hI8w_z5QIw&oid=9&output=image";
				point = new GLatLng(26.149179, 85.890587);
				createMarker(point,"Darbhanga",districtChartURL,i);
			  }
			  else if(i==8)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=rXZuJdw2tyHb3hI8w_z5QIw&oid=10&output=image";
				point = new GLatLng(24.791121,84.996353);
				createMarker(point,"Gaya",districtChartURL,i);
			  }
			  //else if(i==9)
			  //{
				//districtChartURL ="http://spreadsheets.google.com/pub?key=rXZuJdw2tyHb3hI8w_z5QIw&oid=2&output=image";
				//point = new GLatLng(36.666667, 70.91667);
				//createMarker(point,"Arwal",districtChartURL,i);
			  //}
			  else if(i==9)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=rXZuJdw2tyHb3hI8w_z5QIw&oid=11&output=image";
				point = new GLatLng(26.440000000000001, 84.379999999999995);
				createMarker(point,"Gopalganj",districtChartURL,i);
			  }
			  else if(i==10)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=rXZuJdw2tyHb3hI8w_z5QIw&oid=12&output=image";
				point = new GLatLng(24.780000000000001, 86.299999999999997);
				createMarker(point,"Jamui",districtChartURL,i);
			  }
			  else if(i==11)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=rXZuJdw2tyHb3hI8w_z5QIw&oid=13&output=image";
				point = new GLatLng(25.149999999999999, 84.890000000000001);
				createMarker(point,"Jehanabad",districtChartURL,i);
			  }
              else if(i==12)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=rXZuJdw2tyHb3hI8w_z5QIw&oid=14&output=image";
				point = new GLatLng(24.989999999999998, 83.609999999999999);
				createMarker(point,"Kaimur (Bhabua)",districtChartURL,i);
			  }
              else if(i==13)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=rXZuJdw2tyHb3hI8w_z5QIw&oid=15&output=image";
				point = new GLatLng(25.539999999999999, 87.650000000000006);
				createMarker(point,"Katihar",districtChartURL,i);
			  }
			  else if(i==14)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=rXZuJdw2tyHb3hI8w_z5QIw&oid=16&output=image";
				point = new GLatLng(25.510000000000002, 86.599999999999994);
				createMarker(point,"Khagaria",districtChartURL,i);
			  }
			  else if(i==15)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=rXZuJdw2tyHb3hI8w_z5QIw&oid=17&output=image";
				point = new GLatLng(26.106235, 87.956631);
				createMarker(point,"Kishanganj",districtChartURL,i);
			  }
			  else if(i==16)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=rXZuJdw2tyHb3hI8w_z5QIw&oid=18&output=image";
				point = new GLatLng(25.171709, 86.103889);
				createMarker(point,"Lakhisarai",districtChartURL,i);
			  }
			 else if(i==17)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=rXZuJdw2tyHb3hI8w_z5QIw&oid=19&output=image";
				point = new GLatLng(25.82, 86.900000000000006);
				createMarker(point,"Madhepura",districtChartURL,i);
			  }
			else if(i==18)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=rXZuJdw2tyHb3hI8w_z5QIw&oid=20&output=image";
				point = new GLatLng(26.399999999999999, 86.219999999999999);
				createMarker(point,"Madhubani",districtChartURL,i);
			  }
			else if(i==19)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=rXZuJdw2tyHb3hI8w_z5QIw&oid=21&output=image";
				point = new GLatLng(25.210000000000001, 86.530000000000001);
				createMarker(point,"Munger",districtChartURL,i);
			  }
              else if(i==20)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=rXZuJdw2tyHb3hI8w_z5QIw&oid=22&output=image";
				point = new GLatLng(26.149999999999999, 85.329999999999998);
				createMarker(point,"Muzaffarpur",districtChartURL,i);
			  }
              else if(i==21)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=rXZuJdw2tyHb3hI8w_z5QIw&oid=23&output=image";
				point = new GLatLng(25.219999999999999, 85.459999999999994);
				createMarker(point,"Nalanda",districtChartURL,i);
			  }
			  else if(i==22)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=rXZuJdw2tyHb3hI8w_z5QIw&oid=24&output=image";
				point = new GLatLng(24.809999999999999, 85.590000000000003);
				createMarker(point,"Nawada",districtChartURL,i);
			  }
			else if(i==23)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=rXZuJdw2tyHb3hI8w_z5QIw&oid=25&output=image";
				point = new GLatLng(27.079999999999998, 84.340000000000003);
				createMarker(point,"Pashchim Champaran",districtChartURL,i);
			  }
			  else if(i==24)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=rXZuJdw2tyHb3hI8w_z5QIw&oid=26&output=image";
				point = new GLatLng(25.449999999999999, 85.219999999999999);
				createMarker(point,"Patna",districtChartURL,i);
			  }
			 else if(i==25)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=rXZuJdw2tyHb3hI8w_z5QIw&oid=27&output=image";
				point = new GLatLng(26.6098139, 84.8567932);
				createMarker(point,"Purba Champaran",districtChartURL,i);
			  }
			 else if(i==26)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=rXZuJdw2tyHb3hI8w_z5QIw&oid=28&output=image";
				point = new GLatLng(25.77618, 87.466103);
				createMarker(point,"Purnia",districtChartURL,i);
			  }
			else if(i==27)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=rXZuJdw2tyHb3hI8w_z5QIw&oid=29&output=image";
				point = new GLatLng(24.6834395, 83.8473015);
				createMarker(point,"Rohtas",districtChartURL,i);
			  }
			else if(i==28)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=rXZuJdw2tyHb3hI8w_z5QIw&oid=30&output=image";
				point = new GLatLng(25.869841, 86.592918);
				createMarker(point,"Saharsa",districtChartURL,i);
			  }
			else if(i==29)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=rXZuJdw2tyHb3hI8w_z5QIw&oid=31&output=image";
				point = new GLatLng(25.853809, 85.779114);
				createMarker(point,"Samastipur",districtChartURL,i);
			  }
			  else if(i==30)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=rXZuJdw2tyHb3hI8w_z5QIw&oid=32&output=image";
				point = new GLatLng(25.899999999999999, 84.829999999999998);
				createMarker(point,"Saran",districtChartURL,i);
			  }
			else if(i==31)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=rXZuJdw2tyHb3hI8w_z5QIw&oid=33&output=image";
				point = new GLatLng(25.142491, 85.85173);
				createMarker(point,"Sheikhpura",districtChartURL,i);
			  }
			  else if(i==32)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=rXZuJdw2tyHb3hI8w_z5QIw&oid=34&output=image";
				point = new GLatLng(26.51667, 85.3);
				createMarker(point,"Sheohar",districtChartURL,i);
			  }
			else if(i==33)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=rXZuJdw2tyHb3hI8w_z5QIw&oid=35&output=image";
				point = new GLatLng(26.59807, 85.495323);
				createMarker(point,"Sitamarhi",districtChartURL,i);
			  }
             else if(i==34)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=rXZuJdw2tyHb3hI8w_z5QIw&oid=36&output=image";
				point = new GLatLng(26.116461, 86.590317);
				createMarker(point,"Supaul",districtChartURL,i);
			  }
			  else if(i==35)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=rXZuJdw2tyHb3hI8w_z5QIw&oid=37&output=image";
				point = new GLatLng(26.16, 84.390000000000001);
				createMarker(point,"Siwan",districtChartURL,i);
			  }
			  else if(i==36)
			  {
				districtChartURL ="http://spreadsheets.google.com/pub?key=rXZuJdw2tyHb3hI8w_z5QIw&oid=38&output=image";
				point = new GLatLng(25.6838206, 85.354965);
				createMarker(point,"Vaishali",districtChartURL,i);
			  }

		  }
	   }
	   // Create a  marker.   
	   function createMarker(point,name,url,cnt)
	   {
		  var tinyIcon = new GIcon(G_DEFAULT_ICON);
		  //tinyIcon.image = "http://labs.google.com/ridefinder/images/mm_20_red.png";
		  tinyIcon.iconSize = new GSize(12, 30);
		  tinyIcon.shadowSize = new GSize(22, 30);
		  tinyIcon.iconAnchor = new GPoint(6, 30);
		  tinyIcon.infoWindowAnchor = new GPoint(5, 1);
		  // Set up our GMarkerOptions object
		  var markerOptions = { icon:tinyIcon };
		  var marker = new GMarker(point,markerOptions);
		  GEvent.addListener(marker, "click", function() {marker.openInfoWindowHtml('<iframe frameborder="0" marginheight="0" marginwidth="0" scrolling="no" width="255" height="210" src="'+url+'"> </iframe>'); });
		  map.addOverlay(marker);
		  balloonMarkersArr[cnt] = marker;
	   }

	   function createMarkerTaluk(point,name,url,cnt)
	   {
		  var tinyBlueIcon = new GIcon(G_DEFAULT_ICON);
		  tinyBlueIcon.image = "http://www.google.com/intl/en_us/mapfiles/ms/micons/blue-dot.png";
		  tinyBlueIcon.iconSize = new GSize(15, 30);
		  tinyBlueIcon.shadowSize = new GSize(22, 30);
		  tinyBlueIcon.iconAnchor = new GPoint(6, 30);
		  tinyBlueIcon.infoWindowAnchor = new GPoint(5, 1);
		  // Set up our GMarkerOptions object
		  var markerOptions = { icon:tinyBlueIcon };
		  var markerTaluk = new GMarker(point,markerOptions);
		  GEvent.addListener(markerTaluk, "click", function() {markerTaluk.openInfoWindowHtml('<iframe frameborder="0" marginheight="0" marginwidth="0" scrolling="no" width="250" height="200" src="'+url+'"> </iframe>'); });
		  map.addOverlay(markerTaluk);
		  balloonMarkersTalukArr[cnt] = markerTaluk;
	   }

	   function removeAllBalloonMarkers()
	   {
			//alert(balloonMarkersArr.length);
			for(var d=0;d<balloonMarkersArr.length;d++)
			{
				if(balloonMarkersArr[d]!=null)
					map.removeOverlay(balloonMarkersArr[d]);
			}
	   }
	   function removeAllBalloonMarkersTaluk()
	   {
			//alert(balloonMarkersTalukArr.length);
			for(var t=0;t<balloonMarkersTalukArr.length;t++)
			{
				if(balloonMarkersTalukArr[t]!=null)
					map.removeOverlay(balloonMarkersTalukArr[t]);
			}
	   }
   }
   else
   {
       alert("Sorry, the Google Maps API is not compatible with this browser");
   }
