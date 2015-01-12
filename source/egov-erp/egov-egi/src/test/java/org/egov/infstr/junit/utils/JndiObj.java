package org.egov.infstr.junit.utils;

public class JndiObj {
	private String jndiName;
	private String hibFactName;
	private String dataSetPath;
	private String schema;
	private String contextDir;

	public String getJndiName() {
		return this.jndiName;
	}

	public void setJndiName(final String jndiName) {
		this.jndiName = jndiName;
	}

	public String getHibFactName() {
		return this.hibFactName;
	}

	public void setHibFactName(final String hibFactName) {
		this.hibFactName = hibFactName;
	}

	public String getDataSetPath() {
		return this.dataSetPath;
	}

	public void setDataSetPath(final String dataSetPath) {
		this.dataSetPath = dataSetPath;
	}

	public String getSchema() {
		return this.schema;
	}

	public void setSchema(final String schema) {
		this.schema = schema;
	}

	public String getContextDir() {
		return this.contextDir;
	}

	public void setContextDir(final String contextDir) {
		this.contextDir = contextDir;
	}

}