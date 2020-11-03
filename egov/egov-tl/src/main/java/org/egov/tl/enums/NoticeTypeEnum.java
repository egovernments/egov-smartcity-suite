package org.egov.tl.enums;

public enum NoticeTypeEnum {
	GENERATEBULKDEMANDNOTICE("Generate Bulk Demand Notice"), REJECTIONNOTICE("Rejection Notice");
	
	private String noticeType; 
	  
    public String getAction() 
    { 
        return this.noticeType; 
    } 
  
    private NoticeTypeEnum(String noticeType) 
    { 
        this.noticeType = noticeType; 
    }
    
    public String toString() {
    	return String.valueOf(noticeType);
    }
}
