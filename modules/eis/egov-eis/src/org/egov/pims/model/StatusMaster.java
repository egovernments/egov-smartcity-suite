/*
 * Created on Dec 4, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.egov.pims.model;

/**
 * @author deepak
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class StatusMaster implements java.io.Serializable
{
	public Integer id;
			public String name;
			/**
			 * @return Returns the id.
			 */
			public Integer getId() {
				return id;
			}
			/**
			 * @param id The id to set.
			 */
			public void setId(Integer id) {
				this.id = id;
			}
			/**
			 * @return Returns the name.
			 */
			public String getName() {
				return name;
			}
			/**
			 * @param name The name to set.
			 */
			public void setName(String name) {
				this.name = name;
			}

}
