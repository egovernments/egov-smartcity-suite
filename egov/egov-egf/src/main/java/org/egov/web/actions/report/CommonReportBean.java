package org.egov.web.actions.report;

import java.math.BigDecimal;

public class CommonReportBean {

	private Boolean isMajor;
	private String accCode;
	private String name;
	private String deptName;
	private Integer slNo;
	public Integer getSlNo() {
		return slNo;
	}
	public void setSlNo(Integer slNo) {
		this.slNo = slNo;
	}
	//Name of the schedule in schedule mapping
	private String schedule;
	private Long FIEscheduleId;
	private BigDecimal balance;
	private BigDecimal computedBalance;
	public CommonReportBean(String accCode, String name, BigDecimal beSum,
			BigDecimal reSum, BigDecimal beAppSum, BigDecimal reAppSum,
			BigDecimal amountSum) {
		this.accCode=accCode;
		this.name=name;
		this.beAmount=beSum;
		this.reAmount=reSum;
		this.beAppAmount=beAppSum;
		this.reAppAmount=reAppSum;
		this.amount=amountSum;
		isMajor=false;
	}
	public CommonReportBean(String accCode, String name, BigDecimal beSum,
			BigDecimal reSum, BigDecimal beAppSum, BigDecimal reAppSum,
			BigDecimal amountSum, BigDecimal pyAmountSum) {
		this.accCode=accCode;
		this.name=name;
		this.beAmount=beSum;
		this.reAmount=reSum;
		this.beAppAmount=beAppSum;
		this.reAppAmount=reAppSum;
		this.amount=amountSum;
		this.pyAmount=pyAmountSum;
		isMajor=false;
	}
	public CommonReportBean() {
		super();
	}
	public String getName() { 
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
	public Long getFIEscheduleId() {
		return FIEscheduleId;
	}
	public void setFIEscheduleId(Long escheduleId) {
		FIEscheduleId = escheduleId;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getSchedule() {
		return schedule;
	}
	public void setSchedule(String schedule) {
		this.schedule = schedule;
	}
	public BigDecimal getBeAmount() {
		if(beAmount==null)
			return BigDecimal.ZERO;
		else
		return beAmount;
	}
	public void setBeAmount(BigDecimal beAmount) {
		this.beAmount = beAmount;
	}
	public BigDecimal getReAmount() {
		if(reAmount==null)
			return BigDecimal.ZERO;
		else
			return reAmount;
	}
	public void setReAmount(BigDecimal reAmount) {
		this.reAmount = reAmount;
	}
	public BigDecimal getReAppAmount() {
		if(reAppAmount==null)
			return BigDecimal.ZERO;
		else
		return reAppAmount;
	}
	public void setReAppAmount(BigDecimal reAppAmount) {
		this.reAppAmount = reAppAmount;
	}
	//Used for GL amount
	private BigDecimal amount;
	private BigDecimal pyAmount;
	private BigDecimal beAmount;
	private BigDecimal reAmount;
	private BigDecimal beAppAmount;
	public BigDecimal getBeAppAmount() {
		if(beAppAmount==null)
			return BigDecimal.ZERO;
		else
		return beAppAmount;
	}
	public void setBeAppAmount(BigDecimal beAppAmount) {
		this.beAppAmount = beAppAmount;
	}
	private BigDecimal reAppAmount;



	public Boolean getIsMajor() {
		return isMajor;
	}
	public void setIsMajor(Boolean isMajor) {  
		this.isMajor = isMajor;
	}

	public String getAccCode() {
		return accCode;
	}
	public void setAccCode(String accCode) {
		this.accCode = accCode;
	}
	public BigDecimal getAmount() {
		if(amount==null)
			return BigDecimal.ZERO;
		else
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		
		this.amount = amount;
	}
	public boolean isZero()
	{
		if(  ( beAmount==null || beAmount.compareTo(BigDecimal.ZERO)==0) 
	   	   && (reAmount==null || reAmount.compareTo(BigDecimal.ZERO)==0)
	   	&& (beAppAmount==null || beAppAmount.compareTo(BigDecimal.ZERO)==0)
		&& (reAppAmount==null || reAppAmount.compareTo(BigDecimal.ZERO)==0)
		&&  (    amount==null || amount.compareTo(BigDecimal.ZERO)==0)
          )
			return true;
		else 
			return false;
	}
	public String toString()
	{
		return ""+isMajor+"\t"+deptName+"\t"+accCode+"\t"+name+"\t"+beAmount+"\t"+beAppAmount+"\t"+reAmount+"\t"+reAppAmount+"\t"+amount+"\t"+getComputedBalance();
	}
	public BigDecimal getComputedBalance()
	{
		
		if(this.reAmount!=null && this.reAmount.compareTo(BigDecimal.ZERO)!=0 )
		{
			computedBalance=reAmount;
			if(reAppAmount!=null)
				computedBalance=computedBalance.add(reAppAmount);
			if(amount!=null)
				computedBalance=computedBalance.subtract(amount);
		}else if(beAmount!=null && beAmount.compareTo(BigDecimal.ZERO)!=0 )
		{
			computedBalance=beAmount;
			if(beAppAmount!=null)
				computedBalance=computedBalance.add(beAppAmount);
			if(amount!=null)
				computedBalance=computedBalance.subtract(amount);
		}else
		{   computedBalance=BigDecimal.ZERO;
		     if(amount!=null)
			computedBalance=computedBalance.subtract(amount);
		}   
		
		return computedBalance;
	}
	public boolean isZeroForIncome() {
		if(  ( beAmount==null || beAmount.compareTo(BigDecimal.ZERO)==0) &&  ( amount==null || amount.compareTo(BigDecimal.ZERO)==0)  )
			return true;
		else 
			return false;
		
	}
	public BigDecimal getPyAmount() {
		if(this.pyAmount==null)
			return BigDecimal.ZERO;
		else
			return this.pyAmount;
	}
	public void setPyAmount(BigDecimal pyAmount) {
		this.pyAmount = pyAmount;
	}
}