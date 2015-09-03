/*******************************************************************************
 * eGov suite of products aim to improve the internal efficiency,transparency, 
 *     accountability and the service delivery of the government  organizations.
 *  
 *      Copyright (C) <2015>  eGovernments Foundation
 *  
 *      The updated version of eGov suite of products as by eGovernments Foundation 
 *      is available at http://www.egovernments.org
 *  
 *      This program is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      any later version.
 *  
 *      This program is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU General Public License for more details.
 *  
 *      You should have received a copy of the GNU General Public License
 *      along with this program. If not, see http://www.gnu.org/licenses/ or 
 *      http://www.gnu.org/licenses/gpl.html .
 *  
 *      In addition to the terms of the GPL license to be adhered to in using this
 *      program, the following additional terms are to be complied with:
 *  
 *  	1) All versions of this program, verbatim or modified must carry this 
 *  	   Legal Notice.
 *  
 *  	2) Any misrepresentation of the origin of the material is prohibited. It 
 *  	   is required that all modified versions of this material be marked in 
 *  	   reasonable ways as different from the original version.
 *  
 *  	3) This license does not grant any rights to any user of the program 
 *  	   with regards to rights under trademark law for use of the trade names 
 *  	   or trademarks of eGovernments Foundation.
 *  
 *    In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 ******************************************************************************/
package org.egov.tl.domain.web;

import java.lang.reflect.Field;

public class ToString {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			boolean x;
			String pack="org.egov.tl.domain.entity.";
			String name="Licensee";
			Class<?> l = Class.forName(pack+name);
			Field[] declaredFields = l.getDeclaredFields();
			StringBuilder strBuilder = new StringBuilder();
			strBuilder.append("StringBuilder str=new StringBuilder();\n")
			.append("str.append(\"").append(name).append("={\");");
					
			for (Field f : declaredFields) {
			
				if(f.getName().equalsIgnoreCase("LOGGER"))
					continue;
				//System.out.println(f.getName()+""+f.getClass()+""+f.getName());
				if (!f.getType().getSimpleName().equalsIgnoreCase("boolean") 
				&& !f.getType().getSimpleName().equalsIgnoreCase("int")
				&& !f.getType().getSimpleName().equalsIgnoreCase("long")
				&& !f.getType().getSimpleName().equalsIgnoreCase("double")
				) {
				
					// strBuilder.append(f==null?"null":"hi");
					 strBuilder.append("str.append(\"  ").append(f.getName()).append("=\")")
					 .append(".append(").append(f.getName()).append("==null?").append("\"null\":")
					 .append(f.getName()).append(".toString());	");
				} else {
					strBuilder.append("str.append(\"  ").append(f.getName()).append("=\")").append(".append(").append(f.getName()).append(");    ");
				}
			}
			strBuilder.append("str.append(\"}\");\n return str.toString();");
			System.out.println(strBuilder.toString());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
