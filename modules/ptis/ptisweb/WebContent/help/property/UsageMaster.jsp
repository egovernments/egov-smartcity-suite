<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/includes/taglibs.jsp" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <style type="text/css">
    	td, th {font-size: 12px}
    	td.custWidth { width:300px}
    	table, td, th {border: 1px  solid black}
    	td {			
			font-size: 11px;
			padding-top: 3px;
			padding-bottom: 3px;
			padding-right: 5px;
			padding-left: 5px;
			border-right-color: #DFDFDF;
			border-bottom-color: #DFDFDF;
			color: #000000;
			text-align: left;
			font-family: Verdana, Arial, Helvetica, sans-serif;
		}
		
		th{
			height: 28px;
			border-right-width: 1px;
			border-bottom-width: 1px;
			border-right-style: solid;
			border-bottom-style: solid;
			border-right-color: #DFDFDF;
			border-bottom-color: #DFDFDF;
			font-family: Verdana, Arial, Helvetica, sans-serif;
			font-size: 10px;
			font-weight: bold;
			color: #333333;
			padding-right: 2px;
			padding-left: 2px;
			background-color: #FBEBD7;
			}
    </style>
  </head>
  
  <body>
    <table class="custStyle" cellpadding="0" cellspacing="0">
	    <tr><th>SI No</th><th>Usage Name</th><th>Description</th><th>Factor</th><th>Property Type</th></tr>
		<tr><td>1</td><td>A1</td><td>Starred Hotels, starred restaurants/ Bars</td><td>4</td><td>Non-residential</td></tr>
		<tr><td>2</td><td>A2</td><td>Entertainment and recreational clubs/ centres, Multiplexes, Cinema theatres, Entertainment parks and similar facilities</td><td>4</td><td>Non-residential</td></tr>
		<tr><td>3</td><td>A3</td><td>All Banks and Financial institutions</td><td>4</td><td>Non-residential</td></tr>
		<tr><td>4</td><td>A4</td><td>Towers and Hoardings</td><td>4</td><td>Non-residential</td></tr>
		<tr><td>5</td><td>A5</td><td>Marriage Halls</td><td>4</td><td>Non-residential</td></tr>
		<tr><td>6</td><td>A6</td><td>Petrol Pumps</td><td>4</td><td>Non-residential</td></tr>
		<tr><td>7</td><td>B1</td><td>Commercial establishments like shops, business centres, unstarred hotels, unstarred restaurants/ bars</td><td>3</td><td>Non-residential</td></tr>
		<tr><td>8</td><td>B2</td><td>Health care institutions like hospitals, nursing homes, clinics, diagnostic centres, dispensaris, health and fitness centres</td><td>3</td><td>Non-residential</td></tr>
		<tr><td>9</td><td>C1</td><td>Educational institutions like Colleges, schools, hostels, Libraries, Museums, Sport infrastructure, Auditorium of educational institutions</td><td>2</td><td>Residential</td></tr>
		<tr><td>10</td><td>C2</td><td>Administrative / Public purpse buildings of Government, Semi-Government, Public Sector Undertaking (excluding Banks and financial institutions)</td><td>2</td><td>Non-residential</td></tr>
		<tr><td>11</td><td>C3</td><td>Sports Complexes, Stadiums, Auditorium</td><td>2</td><td>Non-residential</td></tr>
		<tr><td>12</td><td>C4</td><td>Cottage and small scale industries</td><td>2</td><td>Non-residential</td></tr>
		<tr><td>13</td><td>C5</td><td>Open spaces  and lawns for marriage other functions, open air restaurant, exhibitions</td><td>2</td><td>Open Plot</td></tr>
		<tr><td>14</td><td>D1</td><td>Residential houses</td><td>1</td><td>Residential</td></tr>
		<tr><td>15</td><td>D2</td><td>IT buildings</td><td>1</td><td>Residential</td></tr>
		<tr><td>16</td><td>D3</td><td>Sports ground (default)</td><td>1</td><td>Open Plot</td></tr>
		<tr><td>17</td><td>E1</td><td>Religious properties and places of worship like Temples, Mosques, Churches, Synagogues, Durgahs, Agiaries, Jain Temples, Gurudwara, Buddh Vihar, Prayer Halls</td><td>0.75</td><td>Residential</td></tr>
		<tr><td>18</td><td>E2</td><td>Dharamshala, Musafirkhana, Orphanages, Asylums, Beggar's Home, Andhalaya, Remand Home, Schools and hostels for physically challenged</td><td>0.75</td><td>Residential</td></tr>
		<tr><td>19</td><td>E3</td><td>Places of cremation/ burning ghar/ burial ground</td><td>0.75</td><td>Residential</td></tr>
		<tr><td>20</td><td>E4</td><td>Religious and charitable purpose</td><td>0.75</td><td>Open Plot</td></tr>
	    
	</table> 
  </body>
</html>
