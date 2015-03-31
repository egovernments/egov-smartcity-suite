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
	    try {
                final Field idField = position.getClass().getSuperclass().getDeclaredField("id");
                idField.setAccessible(true);
                idField.set(position, id);
            } catch (final Exception e) {
                throw new RuntimeException(e);
            }
            return this;
	}
}
