 /*
 * Created on Dec 4, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.egov.pims.empLeave.model;


/**
 * @author deepak
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TypeOfLeaveMaster implements java.io.Serializable
{
	public Integer id;
			public String name;
			public Character accumulate = new Character('0');
			public Character payElegible = new Character('0');
			public Character isHalfDay = new Character('0');
			public Character isEncashable = new Character('0');
			
			
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
							public Character getAccumulate() {
					return accumulate;
				}
				public void setAccumulate(Character accumulate) {
					this.accumulate = accumulate;
				}
				public Character getPayElegible() {
					return payElegible;
				}
				public void setPayElegible(Character payElegible) {
					this.payElegible = payElegible;
				}
				public Character getIsHalfDay() {
					return isHalfDay;
				}
				public void setIsHalfDay(Character isHalfDay) {
					this.isHalfDay = isHalfDay;
				}
				public Character getIsEncashable() {
					return isEncashable;
				}
				public void setIsEncashable(Character isEncashable) {
					this.isEncashable = isEncashable;
				}
				

}
