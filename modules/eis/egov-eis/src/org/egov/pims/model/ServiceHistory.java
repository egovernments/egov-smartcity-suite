package org.egov.pims.model;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;

public class ServiceHistory implements Serializable,Comparable
{
   private Integer idService;
   private Date commentDate;
   private String comments;
   private String reason;
   private String orderNo;
   private Long docNo;
   private String payScale;
   private PersonalInformation employeeId;
   
   public ServiceHistory()
   {
	   
   }
   
   public ServiceHistory(PersonalInformation employeeId,Date commentDate,String comments,String reason,String serviceOrderNo,String serviceDocNo, String payScale)
   {
	   this.commentDate=commentDate;
	   this.comments=comments;
	   this.reason=reason;
	   this.orderNo=serviceOrderNo;
	   this.docNo=serviceDocNo.equals("")? this.docNo : Long.valueOf(serviceDocNo);
	   this.payScale=payScale;
   }
   
   public ServiceHistory(Integer idService)
   {
	   this.idService=idService;
   }
   
  
   
public Integer getIdService() {
	return idService;
}
public void setIdService(Integer idService) {
	this.idService = idService;
}
public Date getCommentDate() {
	return commentDate;
}
public void setCommentDate(Date commentDate) {
	this.commentDate = commentDate;
}
public String getComments() {
	return comments;
}
public void setComments(String comments) {
	this.comments = comments;
}
public String getReason() {
	return reason;
}
public void setReason(String reason) {
	this.reason = reason;
}

public PersonalInformation getEmployeeId() {
	return employeeId;
}

public void setEmployeeId(PersonalInformation employeeId) {
	this.employeeId = employeeId;
}

public static Comparator commentDateComparator = new Comparator() {
	public int compare(Object commentDate, Object anotherCommentDate) {
		Date commentDate1 = ((ServiceHistory)commentDate).getCommentDate();
		Date commentDate2 = ((ServiceHistory)anotherCommentDate).getCommentDate();
		return commentDate1.compareTo(commentDate2);
	}
};

public int compareTo(Object anotherCommentDate) throws ClassCastException {
    if (!(anotherCommentDate instanceof ServiceHistory))
      throw new ClassCastException("A ServiceHistory object expected.");
    Integer serviceId = ((ServiceHistory) anotherCommentDate).getIdService(); 
    return this.idService.compareTo(serviceId);  
  }

public String getOrderNo() {
	return orderNo;
}

public Long getDocNo() {
	return docNo;
}

public void setOrderNo(String orderNo) {
	this.orderNo = orderNo;
}

public void setDocNo(Long docNo) {
	this.docNo = docNo;
}

public String getPayScale() {
	return payScale;
}

public void setPayScale(String payScale) {
	this.payScale = payScale;
}


   
}
