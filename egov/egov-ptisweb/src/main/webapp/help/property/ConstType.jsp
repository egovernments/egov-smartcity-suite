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
	   <tr><th>SI No</th><th>Type</th><th>Description</th><th>Factor</th><tr>
		<tr><td>1</td><td>I-A Posh - A</td><td>Building of about 100 Years life. Posh Construction First Class Pucca building of RCC structure, wall of table moulded brick in cement mortar, cement plaster on both sides, first class wood-work and tiled flooring</td><td>1.25</td><tr>
		<tr><td>2</td><td>I-A - B</td><td>Building of about 100 Years life. First Class Pucca building of RCC structure, wall of table moulded brick in cement mortar, cement plaster on both sides, first class wood-work and tiled flooring</td><td>1</td><tr>
		<tr><td>3</td><td>I-B – C</td><td>Building of about 50 Years life.Other specification similar to I A but thin walls and slightly inferior specifications</td><td>0.75</td><tr>
		<tr><td>4</td><td>II – D</td><td>Building of about 30 Years life. Plinth of stone masonary in cement mortar The walls of 2nd class Table Moulded brick in cement mortar. Cement plaster on both sides or on one side with Mangalore tiles.</td><td>0.6</td><tr>
		<tr><td>5</td><td>III – E</td><td>Building of about 25 Years life.Plinth of stone masonary in cement mortar or Table Moulded brick in cement mortar Walls of K.B. (Khanjar Bricks) in clay with cement plaster inside and cement or lime pointing. Floor & roof, Mud floor or Bagra or Country tiles</td><td>0.45</td><tr>
		<tr><td>6</td><td>IV – F</td><td>Building of about 20 Years life. Plinth of stone masonary in cement or lime mortar. Mud walls, Mud plaster on both sides. Floor is of mud and roof of country tiles or sheet roofing</td><td>0.3</td><tr>
		<tr><td>7</td><td>V – G</td><td>Building of about 15 Years life. Plinth of Mud, walls, tatta or sind roof - country tiles and grass with mud floor</td><td>0.15</td><tr>
	</table> 
  </body>
</html>
