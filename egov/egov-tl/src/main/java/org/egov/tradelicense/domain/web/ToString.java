package org.egov.tradelicense.domain.web;

import java.lang.reflect.Field;

public class ToString {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			boolean x;
			String pack="org.egov.license.domain.entity.";
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
