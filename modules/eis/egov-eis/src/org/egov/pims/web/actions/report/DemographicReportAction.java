package org.egov.pims.web.actions.report;

import java.util.Date;
import java.util.List;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.DateUtils;
import org.egov.web.actions.BaseFormAction;
import org.hibernate.Query;
@ParentPackage("egov")
public class DemographicReportAction extends BaseFormAction {

	private static final long serialVersionUID = -6302418267197623055L;
	private PersistenceService persistenceService;
	List<Object[]> genderList;
	List<Object[]> religionList;
	List<Object[]> ageRangeList;
	List<Object[]> communityList;
	Integer typeId;
	private transient Date givenDate;
	@Override
	public Object getModel() {
		return null;
	}
	@Override
	public void prepare()
	{
		
	}
	
	private List getAgeRangeList(Date date)
	{		
		String ageQuery="SELECT  CASE " +
		"WHEN  trunc( months_between( '"+DateUtils.getFormattedDate(date, "dd-MMM-yyyy")+"',date_of_birth ) /12 )  between 18 and 25 THEN '18-25' " +
		"WHEN  trunc( months_between( '"+DateUtils.getFormattedDate(date, "dd-MMM-yyyy")+"',date_of_birth ) /12 )  between 25 and 30  THEN '25-30' " +
		"WHEN  trunc( months_between( '"+DateUtils.getFormattedDate(date, "dd-MMM-yyyy")+"',date_of_birth ) /12 )  between 30 and 40  THEN '30-40' " +
		"WHEN  trunc( months_between( '"+DateUtils.getFormattedDate(date, "dd-MMM-yyyy")+"',date_of_birth ) /12 )  between 40 and 50  THEN '40-50' " +
		"WHEN  trunc( months_between( '"+DateUtils.getFormattedDate(date, "dd-MMM-yyyy")+"',date_of_birth ) /12 )  between 50 and 60  THEN '50-60' " +
		"WHEN  trunc( months_between( '"+DateUtils.getFormattedDate(date, "dd-MMM-yyyy")+"',date_of_birth ) /12 )  > 60  THEN '60+' " +
		"END AS age, " +
		"COUNT(*) AS count," +
		"ROUND( (COUNT(distinct emp.id)/(" +
		"select COUNT(distinct emp.id) from EG_EMPLOYEE emp , EG_EIS_EMPLOYEEINFO info  where emp.id=info.id and '" +
		DateUtils.getFormattedDate(date, "dd-MMM-yyyy")+"' between info.from_date and info.to_date or(from_date<='"+DateUtils.getFormattedDate(date, "dd-MMM-yyyy")+"' and to_date is null) and info.is_primary='Y'" +
		") * 100),2) as percnt " +
		"FROM  EG_EMPLOYEE emp inner join EG_EIS_EMPLOYEEINFO info  on emp.id=info.id and " +
		"'"+DateUtils.getFormattedDate(date, "dd-MMM-yyyy")+"' between info.from_date and info.to_date or(from_date<='"+DateUtils.getFormattedDate(date, "dd-MMM-yyyy")+"' and to_date is null) and info.is_primary='Y'" +
		"where  trunc( months_between( '"+DateUtils.getFormattedDate(date, "dd-MMM-yyyy")+"',date_of_birth ) /12 )>=18 " +
		"GROUP BY  CASE " +
		"WHEN  trunc( months_between( '"+DateUtils.getFormattedDate(date, "dd-MMM-yyyy")+"',date_of_birth ) /12 )  between 18 and 25 THEN '18-25'  " +
		"WHEN  trunc( months_between( '"+DateUtils.getFormattedDate(date, "dd-MMM-yyyy")+"',date_of_birth ) /12 )  between 25 and 30  THEN '25-30' " +
		"WHEN  trunc( months_between( '"+DateUtils.getFormattedDate(date, "dd-MMM-yyyy")+"',date_of_birth ) /12 )  between 30 and 40  THEN '30-40' " +
		"WHEN  trunc( months_between( '"+DateUtils.getFormattedDate(date, "dd-MMM-yyyy")+"',date_of_birth ) /12 )  between 40 and 50  THEN '40-50' " +
		"WHEN  trunc( months_between( '"+DateUtils.getFormattedDate(date, "dd-MMM-yyyy")+"',date_of_birth ) /12 )  between 50 and 60  THEN '50-60' " +
		"WHEN  trunc( months_between( '"+DateUtils.getFormattedDate(date, "dd-MMM-yyyy")+"',date_of_birth ) /12 )  > 60  THEN '60+' " +
		"END  " +
		"order by 2";
		Query qry = persistenceService.getSession().createSQLQuery(ageQuery);		
		ageRangeList=qry.list();
		return ageRangeList;
	}
	public String  getDemoGraphicInfo() 
	{
		Date date = new Date();
		if(getGivenDate() != null)
			date = getGivenDate();
		if(getTypeId()==null || getTypeId()==-1)
		{
		genderList=persistenceService.
		getSession().getNamedQuery("GENDER_DEMOGRAPHIC").setDate("date", date).list();
		religionList=	persistenceService.
		getSession().getNamedQuery("RELIGION_DEMOGRAPHIC").setDate("date", date).list();	
		ageRangeList=getAgeRangeList(date);
		communityList=	persistenceService.
				getSession().getNamedQuery("COMMUNITY_DEMOGRAPHIC").setDate("date", date).list();
		}
		else if(getTypeId()==1)
		{
			genderList=persistenceService.
			getSession().getNamedQuery("GENDER_DEMOGRAPHIC").setDate("date", date).list();
		}
		else if(getTypeId()==2)
		{
			ageRangeList=getAgeRangeList(date);
		}
		else if(getTypeId()==3)
		{
			religionList=	persistenceService.
			getSession().getNamedQuery("RELIGION_DEMOGRAPHIC").setDate("date", date).list();
		}
		else if(getTypeId()==4)
		{
			communityList=	persistenceService.
			getSession().getNamedQuery("COMMUNITY_DEMOGRAPHIC").setDate("date", date).list();
		}
		return SUCCESS;
		
	}
	public void setPersistenceService(PersistenceService persistenceService) {
		this.persistenceService = persistenceService;
	}
	public void setGenderList(List<Object[]> genderList) {
		this.genderList = genderList;
	}
	public void setReligionList(List<Object[]> religionList) {
		this.religionList = religionList;
	}
	public void setAgeRangeList(List<Object[]> ageRangeList) {
		this.ageRangeList = ageRangeList;
	}
	public List<Object[]> getGenderList() {
		return genderList;
	}
	public List<Object[]> getReligionList() {
		return religionList;
	}
	public List<Object[]> getAgeRangeList() {
		return ageRangeList;
	}
	public Integer getTypeId() {
		return typeId;
	}
	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}
	public Date getGivenDate() {
		return givenDate;
	}
	public void setGivenDate(Date givenDate) {
		this.givenDate = givenDate;
	}
	public List<Object[]> getCommunityList() {
		return communityList;
	}
	public void setCommunityList(List<Object[]> communityList) {
		this.communityList = communityList;
	}
	
	
	

}
