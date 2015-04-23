/*
 * Created on Feb 16, 2007
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.egov.billsaccounting.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.egov.billsaccounting.model.Worksdetail;
import org.egov.commons.Fund;
import org.egov.infra.admin.master.entity.User;
import org.egov.infstr.dao.GenericHibernateDAO;
import org.egov.infstr.utils.HibernateUtil;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.type.StringType;

/**
 * @author Mani
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class WorksDetailHibernateDAO extends GenericHibernateDAO
{

	public WorksDetailHibernateDAO(Class persistentClass, Session session)
	{
			super(persistentClass, session);
	}
	 private final static Logger LOGGER = Logger.getLogger(WorksDetailHibernateDAO.class);

	public Worksdetail findWorksDetailByCode(String worksDetailCode)
	{
		Query qry =HibernateUtil.getCurrentSession().createQuery("from Worksdetail wd where upper(wd.code) =:code");
		qry.setString("code", worksDetailCode.toUpperCase().trim());
		return (Worksdetail)qry.uniqueResult();
	}
	public List<Worksdetail> findAllWorksDetail()
	{
		Query qry =HibernateUtil.getCurrentSession().createQuery("from Worksdetail wd order by code");
		return qry.list();
	}
	/**
	 *
	 * @param workCode
	 * @param estimatePreparedBy
	 * @param estimatePreparedOn
	 * @param statusId
	 * @return list of Worksdetail filtered by optional conditions
	 */
	public List<Worksdetail> getWorksDetailFilterBy(String workCode,User estimatePreparedBy,Date estimatePreparedOn,ArrayList<Integer> statusId)
	{
		Query qry;
		StringBuffer qryStr = new StringBuffer();
		List<Worksdetail> worksDetailList=null;
		qryStr.append("select distinct wd From Worksdetail wd inner join fetch wd.egwWorksMises ewm "
				+"  where (wd.relation is null or wd.relation in ( select wd1.relation From Worksdetail wd1 inner join wd1.relation r inner join r.relationtype rt where rt.id=2 ))");
		qry =HibernateUtil.getCurrentSession().createQuery(qryStr.toString()) ;
		if(statusId!=null)
	    {
	    	qryStr.append(" and (wd.statusid in (:statusId) )");
	    	qry =HibernateUtil.getCurrentSession().createQuery(qryStr.toString());
	    }
		if(workCode!=null && !workCode.equals(""))
	    {
	    	qryStr.append(" and (upper(wd.code) like :code)");
	    	qry =HibernateUtil.getCurrentSession().createQuery(qryStr.toString()) ;
	    }
	    if(estimatePreparedBy!=null)
	    {
	    	qryStr.append(" and (ewm.esimatepreparedby = :estimatePreparedBy)");
	    	qry =HibernateUtil.getCurrentSession().createQuery(qryStr.toString()) ;
	    }
	    if(estimatePreparedOn!=null)
	    {
	    	qryStr.append(" and (ewm.estimatepreparedon = :estimatePreparedOn)");
	    	qry =HibernateUtil.getCurrentSession().createQuery(qryStr.toString()) ;
	    }
	    if(statusId!=null)
	    	qry.setParameterList("statusId",statusId);
	    if(workCode!=null && !workCode.equals(""))
	       	qry.setString("code","%"+workCode.toUpperCase().trim()+"%");
	    if(estimatePreparedBy!=null)
	      	qry.setEntity("estimatePreparedBy",estimatePreparedBy);
	    if(estimatePreparedOn!=null)
	      	qry.setDate("estimatePreparedOn",estimatePreparedOn);
	    if(LOGGER.isDebugEnabled())     LOGGER.debug("qryStr "+qryStr.toString());
	    worksDetailList=qry.list();
        return worksDetailList;
	}

    public List<Worksdetail> getWorksDetailFilterBy(String billType, String workCode,User estimatePreparedBy,Date estimatePreparedOn,ArrayList<Integer> statusId)
    {
        Query qry;
        StringBuffer qryStr = new StringBuffer();
        List<Worksdetail> worksDetailList=null;
        qryStr.append("select distinct wd From Worksdetail wd inner join fetch wd.egwWorksMises ewm "
                +"  where (wd.relation is null or wd.relation in ( select wd1.relation From Worksdetail wd1 inner join wd1.relation r inner join r.relationtype rt where rt.id=2 ))");
        qry =HibernateUtil.getCurrentSession().createQuery(qryStr.toString()) ;

        if(statusId!=null)
        {
            qryStr.append(" and (wd.statusid in (:statusId) )");
            qry =HibernateUtil.getCurrentSession().createQuery(qryStr.toString());
        }
        if(workCode!=null && !workCode.equals(""))
        {
            qryStr.append(" and (upper(wd.code) like :code)");
            qry =HibernateUtil.getCurrentSession().createQuery(qryStr.toString()) ;
        }
        if(estimatePreparedBy!=null)
        {
            qryStr.append(" and (ewm.esimatepreparedby = :estimatePreparedBy)");
            qry =HibernateUtil.getCurrentSession().createQuery(qryStr.toString()) ;
        }
        if(estimatePreparedOn!=null)
        {
            qryStr.append(" and (ewm.estimatepreparedon = :estimatePreparedOn)");
            qry =HibernateUtil.getCurrentSession().createQuery(qryStr.toString()) ;
        }
        if(statusId!=null)
            qry.setParameterList("statusId",statusId);
        if(workCode!=null && !workCode.equals(""))
            qry.setString("code","%"+workCode.toUpperCase().trim()+"%");
        if(estimatePreparedBy!=null)
            qry.setEntity("estimatePreparedBy",estimatePreparedBy);
        if(estimatePreparedOn!=null)
            qry.setDate("estimatePreparedOn",estimatePreparedOn);
        if(LOGGER.isDebugEnabled())     LOGGER.debug("qryStr "+qryStr.toString());
        worksDetailList=qry.list();
        return worksDetailList;
    }

     public Worksdetail findByWorkOrderId(String workOrderId)
        {
            Query qry =HibernateUtil.getCurrentSession().createQuery("from Worksdetail wd where wd.id =:id");
            qry.setString("id",workOrderId);
            return (Worksdetail)qry.uniqueResult();
        }

     public List findAllCheckedWorksDetail()
     {
         Query qry =HibernateUtil.getCurrentSession().createQuery("from Worksdetail wd where wd.id in(select mb.worksdetail from EgwMBHeader mb where mb.egwStatus=11 and mb.voucherHeaderId is null)  and wd.id not in (select cb.worksdetail From Contractorbilldetail cb where cb.billtype='Final') order by upper(wd.name)");
         return qry.list();
     }
    /* public List findAllCheckedWorksDetail()
     {
         Query qry =HibernateUtil.getCurrentSession().createQuery("from Worksdetail wd where statusid=11 order by upper(name)");
         return qry.list();
     }*/

     public List<Worksdetail> getRevisionWOFilterBy(String revisionNo,Date revisionDate,String originalWOCode,ArrayList<Integer> statusId)
 	{
 		Query qry;
 		StringBuffer qryStr = new StringBuffer();
 		List<Worksdetail> worksDetailList=null;
 		qryStr.append("select distinct wd From Worksdetail wd where wd.parentid is not null ");
 		qry =HibernateUtil.getCurrentSession().createQuery(qryStr.toString()) ;
 		if(statusId!=null)
 	    {
 	    	qryStr.append(" and (wd.statusid in (:statusId) )");
 	    	qry =HibernateUtil.getCurrentSession().createQuery(qryStr.toString());
 	    }
 		if(revisionNo!=null && !revisionNo.equals(""))
 	    {
 	    	qryStr.append(" and (upper(wd.code) like :code)");
 	    	qry =HibernateUtil.getCurrentSession().createQuery(qryStr.toString()) ;
 	    }
 		if(originalWOCode!=null && !originalWOCode.equals(""))
 	    {
 	    	qryStr.append(" and (upper(wd.parentid.code) like :originalWOCode)");
 	    	qry =HibernateUtil.getCurrentSession().createQuery(qryStr.toString()) ;
 	    }

 	    if(revisionDate!=null)
 	    {
 	    	qryStr.append(" and (wd.orderdate = :orderdate)");
 	    	qry =HibernateUtil.getCurrentSession().createQuery(qryStr.toString()) ;
 	    }
 	    if(statusId!=null)
 	    	qry.setParameterList("statusId",statusId);
 	    if(revisionNo!=null && !revisionNo.equals(""))
 	       	qry.setString("code","%"+revisionNo.toUpperCase().trim()+"%");
 	   if(originalWOCode!=null && !originalWOCode.equals(""))
	       	qry.setString("originalWOCode","%"+originalWOCode.toUpperCase().trim()+"%");
 	    if(revisionDate!=null)
 	      	qry.setDate("orderdate",revisionDate);
 	    if(LOGGER.isDebugEnabled())     LOGGER.debug("qryStr "+qryStr.toString());
 	    worksDetailList=qry.list();
         return worksDetailList;
 	}

     public List<Worksdetail> getWOForRevisionFilterBy(String workCode,User estimatePreparedBy,Date estimatePreparedOn,ArrayList<Integer> statusId)
 	{
 		Query qry;
 		StringBuffer qryStr = new StringBuffer();
 		List<Worksdetail> worksDetailList=null;
 		qryStr.append("select distinct wd From Worksdetail wd inner join fetch wd.egwWorksMises ewm "
 				+"  where (wd.relation is null or wd.relation in ( select wd1.relation From Worksdetail wd1 inner join wd1.relation r inner join r.relationtype rt where rt.id=2 )) and wd.id not in (select cb.worksdetail From Contractorbilldetail cb where cb.billtype='Final') and wd.parentid is null ");
 		qry =HibernateUtil.getCurrentSession().createQuery(qryStr.toString()) ;
 		if(statusId!=null)
 	    {
 	    	qryStr.append(" and (wd.statusid in (:statusId) )");
 	    	qry =HibernateUtil.getCurrentSession().createQuery(qryStr.toString());
 	    }
 		if(workCode!=null && !workCode.equals(""))
 	    {
 	    	qryStr.append(" and (upper(wd.code) like :code)");
 	    	qry =HibernateUtil.getCurrentSession().createQuery(qryStr.toString()) ;
 	    }
 	    if(estimatePreparedBy!=null)
 	    {
 	    	qryStr.append(" and (ewm.esimatepreparedby = :estimatePreparedBy)");
 	    	qry =HibernateUtil.getCurrentSession().createQuery(qryStr.toString()) ;
 	    }
 	    if(estimatePreparedOn!=null)
 	    {
 	    	qryStr.append(" and (ewm.estimatepreparedon = :estimatePreparedOn)");
 	    	qry =HibernateUtil.getCurrentSession().createQuery(qryStr.toString()) ;
 	    }
 	    if(statusId!=null)
 	    	qry.setParameterList("statusId",statusId);
 	    if(workCode!=null && !workCode.equals(""))
 	       	qry.setString("code","%"+workCode.toUpperCase().trim()+"%");
 	    if(estimatePreparedBy!=null)
 	      	qry.setEntity("estimatePreparedBy",estimatePreparedBy);
 	    if(estimatePreparedOn!=null)
 	      	qry.setDate("estimatePreparedOn",estimatePreparedOn);
 	    if(LOGGER.isDebugEnabled())     LOGGER.debug("qryStr "+qryStr.toString());
 	    worksDetailList=qry.list();
         return worksDetailList;
 	}

 	public List<Worksdetail> getRivisionWOByParentId(Worksdetail worksDetail)
 	{
 		Query qry =HibernateUtil.getCurrentSession().createQuery("from Worksdetail wd where wd.parentid =:parentId order by id");
 		qry.setEntity("parentId", worksDetail);
 		return qry.list();
 	}

 	public List<Worksdetail> getWorksDetailFilterBy(Fund fund,Date fromDate,Date toDate,ArrayList<Integer> statusId)
	{
 		Query qry;
		StringBuffer qryStr = new StringBuffer();
		List<Worksdetail> worksDetailList=null;
		qryStr.append("select distinct wd From Worksdetail wd inner join fetch wd.egwWorksMises ewm "
				+"  where (wd.relation is null or wd.relation in ( select wd1.relation From Worksdetail wd1 inner join wd1.relation r inner join r.relationtype rt where rt.id=2 ))");
		qry =HibernateUtil.getCurrentSession().createQuery(qryStr.toString()) ;

		if(statusId!=null)
 	    {
 	    	qryStr.append(" and (wd.statusid in (:statusId) )");
 	    	qry =HibernateUtil.getCurrentSession().createQuery(qryStr.toString());
 	    }
		if(fund!=null)
	    {
	    	qryStr.append(" and (ewm.fund =:fund)");
	    	qry =HibernateUtil.getCurrentSession().createQuery(qryStr.toString()) ;
	    }
		if(fromDate!=null)
	    {
	    	qryStr.append(" and (wd.orderdate >=:fromDate)");
	    	qry =HibernateUtil.getCurrentSession().createQuery(qryStr.toString()) ;
	    }
	    if(toDate!=null)
	    {
	    	qryStr.append(" and (wd.orderdate <=:toDate)");
	    	qry =HibernateUtil.getCurrentSession().createQuery(qryStr.toString()) ;
	    }

	    qryStr.append(" order by orderdate");
	    qry =HibernateUtil.getCurrentSession().createQuery(qryStr.toString()) ;

	    if(statusId!=null)
 	    	qry.setParameterList("statusId",statusId);
	    if(fund!=null)
	       	qry.setEntity("fund",fund);
	    if(fromDate!=null)
	      	qry.setDate("fromDate",fromDate);
	    if(toDate!=null)
	      	qry.setDate("toDate",toDate);
	    worksDetailList=qry.list();
        return worksDetailList;
	}
	public List getAllTds(Worksdetail wd)
	{

	Query qry;

	String qryStr="Select t.id from Tds t  where t.egPartytype.id:=relationType";
	qry =HibernateUtil.getCurrentSession().createQuery(qryStr);
	qry.setEntity("relationType",wd.getRelation().getRelationtype());
	List tdsList=qry.list();
	return tdsList;
	}
	public List getTotalBillValue(Worksdetail wd)
	{
		Query qry;
		String billRegqry="SELECT SUM(reg.passedamount) FROM  EgBillregister reg WHERE reg.worksdetailId=? AND  reg.billstatus='PENDING'";

		qry =HibernateUtil.getCurrentSession().createQuery(billRegqry);
		qry.setParameter(0, wd.getId().toString(),StringType.INSTANCE);
			return	qry.list();

	}

	public List	getTotalAdvAdj(Worksdetail wd)
	{
		Query qry;
		String billRegqry="SELECT SUM(reg.advanceadjusted) FROM  EgBillregister reg WHERE reg.worksdetailId=? AND  reg.billstatus='PENDING'";

		qry =HibernateUtil.getCurrentSession().createQuery(billRegqry);
		qry.setParameter(0, wd.getId().toString(),StringType.INSTANCE);
			return	qry.list();

	}


}
