package org.egov.tradelicense.domain.entity;

import org.egov.infstr.commons.Module;
import org.egov.infstr.models.BaseModel;
/**
 * 
 * @author mani
 * will hold the each license module types
 * eg: 1.TradeLicense
 *     2.Hospital License
 *     3.Hawker
 */
public class LicenseType extends BaseModel {
	private static final long serialVersionUID = 1L;
	private String name;
	private Module module;

	public Module getModule() {
		return module;
	}

	public void setModule(Module module) {
		this.module = module;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/* (non-Javadoc)
	 * @see org.egov.infstr.models.BaseModel#toString()
	 */
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append("LicenseType= { ");
		str.append("serialVersionUID=").append(serialVersionUID);
		str.append("name=").append(name == null ? "null" : name.toString());
		str.append("module=").append(module == null ? "null" : module.toString());
		str.append("}");
		return str.toString();		
	}
}
