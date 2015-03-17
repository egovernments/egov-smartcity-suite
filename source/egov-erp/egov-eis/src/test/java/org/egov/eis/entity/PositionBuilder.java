/**
 * 
 */
package org.egov.eis.entity;

import java.lang.reflect.Field;

import org.egov.pims.commons.Position;

/**
 * @author Vaibhav.K
 *
 */
public class PositionBuilder {
	
	private final Position position;
	
	public PositionBuilder() {
		position = new Position();
	}

	public PositionBuilder withName(String name) {
		position.setName(name);
		return this;
	}
	
	public Position build() {
		return position;
	}
	
	public PositionBuilder withId(Integer id){
		 position.setId(id);
	        return this;
	    }
}
