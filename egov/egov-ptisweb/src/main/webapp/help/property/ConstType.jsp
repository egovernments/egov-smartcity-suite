#-------------------------------------------------------------------------------
# /**
#  * eGov suite of products aim to improve the internal efficiency,transparency, 
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
#  */
#-------------------------------------------------------------------------------
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
