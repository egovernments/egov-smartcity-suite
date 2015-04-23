package org.egov.eb.domain.master.entity;

import java.util.HashSet;
import java.util.Set;

import org.egov.infstr.models.BaseModel;
import org.egov.infstr.utils.HashCodeUtil;
import org.egov.pims.commons.Position;

public class TargetArea extends BaseModel{

	private String name;
	private String code;
	private boolean isActive;
	private Position position;
	private Set<TargetAreaMappings> targetAreaMappings = new HashSet<TargetAreaMappings>(0);

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public Set<TargetAreaMappings> getTargetAreaMappings() {
		return targetAreaMappings;
	}

	public void setTargetAreaMappings(Set<TargetAreaMappings> targetAreaMappings) {
		this.targetAreaMappings = targetAreaMappings;
	}

	@Override
	public int hashCode() {
		int seedValue = HashCodeUtil.SEED;

		seedValue = HashCodeUtil.hash(seedValue, this.code);
		seedValue = HashCodeUtil.hash(seedValue, this.isActive);
		seedValue = HashCodeUtil.hash(seedValue, this.name);
		seedValue = HashCodeUtil.hash(seedValue, this.position);
		
		return seedValue;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		
		if (obj == null)
			return false;
		
		if (getClass() != obj.getClass())
			return false;
		
		TargetArea other = (TargetArea) obj;
		
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		
		if (isActive != other.isActive)
			return false;
		
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		
		if (position == null) {
			if (other.position != null)
				return false;
		} else if (!position.equals(other.position))
			return false;
		
		return true;
	}
	
	@Override
	public String toString() {
		return new StringBuilder().append("TargetArea [")
				.append("id=").append(id)
				.append(", name=").append(name)
				.append(", code=").append(code)
				.append(", isActive").append(isActive)
				.append(", position").append(position)
				.append("]").toString();
	}
}

