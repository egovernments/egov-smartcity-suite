package org.egov.eb.domain.master.entity;

import org.egov.infstr.models.BaseModel;
import org.egov.infstr.utils.HashCodeUtil;
import org.egov.infra.admin.master.entity.Boundary;
//TODO Should Extend baseModel remove isactive field
public class TargetAreaMappings extends BaseModel{

	private Boundary boundary;
	private TargetArea area;


	public Boundary getBoundary() {
		return boundary;
	}

	public void setBoundary(Boundary boundary) {
		this.boundary = boundary;
	}

	public TargetArea getArea() {
		return area;
	}

	public void setArea(TargetArea area) {
		this.area = area;
	}
	
	@Override
	public int hashCode() {
		int seedValue = HashCodeUtil.SEED;

		seedValue = HashCodeUtil.hash(seedValue, this.area);
		seedValue = HashCodeUtil.hash(seedValue, this.boundary);
		
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
		
		TargetAreaMappings other = (TargetAreaMappings) obj;
		
		if (area == null) {
			if (other.area != null)
				return false;
		} else if (!area.equals(other.area))
			return false;
		
		if (boundary == null) {
			if (other.boundary != null)
				return false;
		} else if (!boundary.equals(other.boundary))
			return false;
		
		return true;
	}

	public String toString() {
		return new StringBuilder()
			.append("TargetAreaMappings [")
			.append("id=").append(id)
			.append(", area=").append(area)
			.append(", boundary=").append(boundary)
			.append("]").toString();
	}

}
