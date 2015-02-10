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
public class CategoryMaster implements GenericMaster
{
	public Integer id;
			public String name;
			public java.util.Date fromDate;
			public java.util.Date toDate;
			private Long fileId;
			
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
			public java.util.Date getFromDate() {
				return fromDate;
			}
			public void setFromDate(java.util.Date fromDate) {
				this.fromDate = fromDate;
		}

		public java.util.Date getToDate() {
					return toDate;
				}
				public void setToDate(java.util.Date toDate) {
					this.toDate = toDate;
		}
				public Long getFileId() {
					return fileId;
				}
				public void setFileId(Long fileId) {
					this.fileId = fileId;
				}
				

}
