package org.egov.tradelicense.utils;

public class LicenseChecklistHelper {

	private Integer id;
	private String name;
	private  String val;
	private String checked;
    public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getVal() {
		return val;
	}
	public void setVal(String val) {
		this.val = val;
	}
	public String getChecked() {
		return checked;
	}
	public void setChecked(String checked) {
		this.checked = checked;
	}
	public LicenseChecklistHelper(String name, String val, String checked) {
		super();
		this.name = name;
		this.val = val;
		this.checked = checked;
	}
}
