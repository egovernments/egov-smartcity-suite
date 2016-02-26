/**
 * eGov suite of products aim to improve the internal efficiency,transparency, 
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation 
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or 
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

	1) All versions of this program, verbatim or modified must carry this 
	   Legal Notice.

	2) Any misrepresentation of the origin of the material is prohibited. It 
	   is required that all modified versions of this material be marked in 
	   reasonable ways as different from the original version.

	3) This license does not grant any rights to any user of the program 
	   with regards to rights under trademark law for use of the trade names 
	   or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.commons;

import java.util.Date;

import org.hibernate.search.annotations.Field;

public class CFunction {

	private Long id = null;
	private String name;
	private String code;
	private String type;
	private int level;
	private Long parentId;
	private Boolean isActive;
	private Date created;
	private Date lastModified;
	private String modifiedBy;
	private Boolean isNotLeaf;
	private String funcNameActual;
	private CFunction function;
	/**
	 * @return Returns the id.
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id The id to set.
	 */
	public void setId(Long id) {
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

	/**
	 * @return Returns the code.
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code The code to set.
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return Returns the created.
	 */
	public Date getCreated() {
		return created;
	}

	/**
	 * @param created The created to set.
	 */
	public void setCreated(Date created) {
		this.created = created;
	}

	/**
	 * @return Returns the isActive.
	 */
	public boolean isIsActive() {
		return this.isActive;
	}
	/**
	 * @param isActive The isActive to set.
	 */
	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
	}
	
	/**
	 * @return Returns the lastModified.
	 */
	public Date getLastModified() {
		return lastModified;
	}

	/**
	 * @param lastModified The lastModified to set.
	 */
	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}

	/**
	 * @return Returns the lLevel.
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * @param level The lLevel to set.
	 */
	public void setLevel(int level) {
		this.level = level;
	}

	/**
	 * @return Returns the modifiedBy.
	 */
	public String getModifiedBy() {
		return modifiedBy;
	}

	/**
	 * @param modifiedBy The modifiedBy to set.
	 */
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	/**
	 * @return Returns the parentId.
	 */
	public Long getParentId() {
		return parentId;
	}

	/**
	 * @param parentId The parentId to set.
	 */
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	/**
	 * @return Returns the type.
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type The type to set.
	 */
	public void setType(String type) {
		this.type = type;
	}
	public String getFuncNameActual() {
		return funcNameActual;
	}
	
	public void setFuncNameActual(String funcNameActual) {
		this.funcNameActual = funcNameActual;
	}
	/**
	 * @return the function
	 */
	public CFunction getFunction() {
		return function;
	}
	/**
	 * @param function the function to set
	 */
	public void setFunction(CFunction function) {
		this.function = function;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public Boolean getIsNotLeaf() {
		return isNotLeaf;
	}

	public void setIsNotLeaf(Boolean isNotLeaf) {
		this.isNotLeaf = isNotLeaf;
	}
}
