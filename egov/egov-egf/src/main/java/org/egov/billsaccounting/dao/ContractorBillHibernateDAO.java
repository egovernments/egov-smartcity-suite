/*******************************************************************************
 * eGov suite of products aim to improve the internal efficiency,transparency, 
 *    accountability and the service delivery of the government  organizations.
 * 
 *     Copyright (C) <2015>  eGovernments Foundation
 * 
 *     The updated version of eGov suite of products as by eGovernments Foundation 
 *     is available at http://www.egovernments.org
 * 
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 * 
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or 
 *     http://www.gnu.org/licenses/gpl.html .
 * 
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 * 
 * 	1) All versions of this program, verbatim or modified must carry this 
 * 	   Legal Notice.
 * 
 * 	2) Any misrepresentation of the origin of the material is prohibited. It 
 * 	   is required that all modified versions of this material be marked in 
 * 	   reasonable ways as different from the original version.
 * 
 * 	3) This license does not grant any rights to any user of the program 
 * 	   with regards to rights under trademark law for use of the trade names 
 * 	   or trademarks of eGovernments Foundation.
 * 
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 ******************************************************************************/
/*
 * Created on Jan 31, 2007
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.egov.billsaccounting.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.egov.commons.Fundsource;
import org.egov.commons.Relation;
import org.egov.infstr.dao.GenericHibernateDAO;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.infra.admin.master.entity.Department;
import org.egov.billsaccounting.model.Contractorbilldetail;
import org.egov.billsaccounting.model.Worksdetail;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session; 
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Tilak
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
@Transactional(readOnly=true)
public class ContractorBillHibernateDAO extends GenericHibernateDAO
{

	public ContractorBillHibernateDAO(Class persistentClass, Session session)
	{
			super(persistentClass, session);
	}
    
    private static final Logger LOGGER = Logger.getLogger(ContractorBillHibernateDAO.class);
    
    public List<Contractorbilldetail> getBillsDetailFilterBy(String billNo,Date billDate,ArrayList<Integer> statusId,String wdCode)
    {
        Query qry = null;
        StringBuffer qryStr = new StringBuffer();
        List<Contractorbilldetail> billDetailList=null;
        qryStr.append("select distinct cb From org.egov.billsaccounting.model.Contractorbilldetail cb where cb.voucherHeaderId in ( select vh.id from CVoucherHeader vh where vh.status = 0)");
        //This fix is for Phoenix Migration.
        //qry =HibernateUtil.getCurrentSession().createQuery(qryStr.toString()) ;
                    
        if(statusId!=null)
        {
            qryStr.append(" and (cb.egwStatus.id in (:statusId) )");
          //This fix is for Phoenix Migration.
            //qry =HibernateUtil.getCurrentSession().createQuery(qryStr.toString());
        }
        
        if(billNo!=null && !billNo.equals(""))
        {
            qryStr.append(" and (upper(cb.billnumber) like :billnumber)");
            //qry =HibernateUtil.getCurrentSession().createQuery(qryStr.toString()) ;
        }
       
        if(billDate!=null)
        {
            qryStr.append(" and (cb.billdate = :billdate)");
          //This fix is for Phoenix Migration.
            //qry =HibernateUtil.getCurrentSession().createQuery(qryStr.toString()) ;
        }
        if(wdCode != null && !wdCode.equals(""))
        {
            qryStr.append(" and (upper(cb.worksdetail.code) like :wdCode)");
          //This fix is for Phoenix Migration.
            
        }    
        
        if(statusId!=null)
            qry.setParameterList("statusId",statusId);
        if(billNo!=null && !billNo.equals(""))
            qry.setString("billnumber","%"+billNo.toUpperCase().trim()+"%");        
        if(billDate!=null)
            qry.setDate("billdate",billDate);
        if(wdCode != null && !wdCode.equals(""))
          	 qry.setString("wdCode","%"+wdCode.toUpperCase().trim()+"%");
        if(LOGGER.isInfoEnabled())     LOGGER.info("qryStr "+qryStr.toString());
        billDetailList=qry.list();
        return billDetailList;
    }
    
    public List<Contractorbilldetail> getFinalBillsDetailFilterBy(String billNo,Date billDate,String billType, ArrayList<Integer> statusId)
    {
        Query qry  = null;
        StringBuffer qryStr = new StringBuffer();
        List<Contractorbilldetail> finalBillDetailList=null;
        qryStr.append("select distinct cb From org.egov.billsaccounting.model.Contractorbilldetail cb, Worksdetail wd where cb.worksdetail=wd.id and wd.statusid!=8");
      //This fix is for Phoenix Migration.
        //qry =HibernateUtil.getCurrentSession().createQuery(qryStr.toString()) ;
        
        if(billNo!=null && !billNo.equals(""))
        {
            qryStr.append(" and (upper(cb.billnumber) like :billnumber)");
          //This fix is for Phoenix Migration.
            //qry =HibernateUtil.getCurrentSession().createQuery(qryStr.toString()) ;
        }
       
        if(billDate!=null)
        {
            qryStr.append(" and (cb.billdate = :billdate)");
          //This fix is for Phoenix Migration.
            //qry =HibernateUtil.getCurrentSession().createQuery(qryStr.toString()) ;
        }            
        if(statusId!=null)
        {
            qryStr.append(" and (cb.egwStatus.id in (:statusId) )");
          //This fix is for Phoenix Migration.
            //qry =HibernateUtil.getCurrentSession().createQuery(qryStr.toString());
        }
        
        if(billType!=null && !billType.equals(""))
        {
            qryStr.append(" and cb.billtype =:billType)");
          //This fix is for Phoenix Migration.
            qry =HibernateUtil.getCurrentSession().createQuery(qryStr.toString()) ;
        }
       
        
        
        if(statusId!=null)
            qry.setParameterList("statusId",statusId);
        if(billType!=null && !billType.equals(""))
            qry.setString("billType",billType);
        if(billNo!=null && !billNo.equals(""))
            qry.setString("billnumber","%"+billNo.toUpperCase().trim()+"%");
        if(billDate!=null)
            qry.setDate("billdate",billDate);
        if(LOGGER.isInfoEnabled())     LOGGER.info("qryStr "+qryStr.toString());
        finalBillDetailList=qry.list();
        return finalBillDetailList;
    }
    
   /* public List getAllActiveBills()
    {
        Query qry =HibernateUtil.getCurrentSession().createQuery("from org.egov.billsaccounting.model.Contractorbilldetail cb where cb.voucherHeaderId in(select vh.id from org.egov.commons.CVoucherHeader vh where vh.status=0)");
        //qry.setString("workOrderId",workOrderId);
        return qry.list();
    }*/
    
    public List<Contractorbilldetail> getMaterialAdjAmtFilterBy()
    {
        Query qry =HibernateUtil.getCurrentSession().createQuery(" from org.egov.billsaccounting.model.Contractorbilldetail cb where cb.voucherHeaderId in(select vh.id from org.egov.commons.CVoucherHeader vh where vh.status=0)");
        //qry.setString("workOrderId",workOrderId);
        return qry.list();
    }
    
    public Contractorbilldetail getContractorbilldetailById(Integer conBillId)
    {
    	Query qry =HibernateUtil.getCurrentSession().createQuery("from org.egov.billsaccounting.model.Contractorbilldetail cb where cb.voucherHeaderId in(select vh.id from org.egov.commons.CVoucherHeader vh where vh.status=0) and cb.id=:conBillId");
        qry.setInteger("conBillId",conBillId);
        return (Contractorbilldetail)qry.uniqueResult();
    }
    


    public List<Contractorbilldetail> getActiveBillByWorksdetail(Worksdetail worksdetail)
    {
        Query qry =HibernateUtil.getCurrentSession().createQuery("from Contractorbilldetail cb where cb.voucherHeaderId in(select vh.id from org.egov.commons.CVoucherHeader vh where vh.status=0) and cb.worksdetail = :worksdetail");
        qry.setEntity("worksdetail", worksdetail);
        return qry.list();
    }
    
    public List<Contractorbilldetail> getMaterialAdjAmtFilterBy(Worksdetail worksdetail)
    {
        Query qry =HibernateUtil.getCurrentSession().createQuery(" from org.egov.billsaccounting.model.Contractorbilldetail cb where cb.voucherHeaderId in(select vh.id from org.egov.commons.CVoucherHeader vh where vh.status=0) and cb.worksdetail=:worksdetail");
        qry.setEntity("worksdetail",worksdetail);
        return qry.list();
    }
    
    public List<Contractorbilldetail> getContractorBillDetailFilterBy(Integer fundId, Fundsource fundSource, Date vhFromDate, Date vhToDate, Relation relation, String vhNoFrom, String vhNoTo,Department dept,String functionary)
    {
    	Query qry;
        StringBuffer qryStr = new StringBuffer();
        List<Contractorbilldetail> contBillList=null;
        qryStr.append(" from org.egov.billsaccounting.model.Contractorbilldetail cb where cb.voucherHeaderId in(select vh.id from org.egov.commons.CVoucherHeader vh where vh.status=0 "
        		+" AND cb.passedamount>((cb.paidamount)+(cb.tdsamount)+(cb.advadjamt)+(cb.otherrecoveries))");    											 
    											       
        if(fundId!=null)
        {
            qryStr.append(" and vh.fundId=:fundId");           
        } 	
        
        if(vhFromDate!=null)
        {
            qryStr.append(" and vh.voucherDate >=:vhFromDate");
        }
        if(vhToDate!=null)
        {
            qryStr.append(" and vh.voucherDate <=:vhToDate");
        }
        if(vhNoFrom!=null && vhNoTo!=null)
        {
            qryStr.append(" and vh.voucherNumber between :vhNoFrom and :vhNoTo");
        } 
        else if(vhNoFrom!=null)
        {
            qryStr.append(" and vh.voucherNumber >=:vhNoFrom");
        }
    	else if(vhNoTo!=null)
    	{
            qryStr.append(" and vh.voucherNumber <=:vhNoTo");
        }
        qryStr.append(" )");

    	if(fundSource!=null)
        {
            qryStr.append(" and cb.worksdetail.fundsource=:fundSource");           
        } 
    	if(relation!=null)
        { 
            if(relation.getBankname().equals("RTGS")) // for RTGS bills
            {
            	qryStr.append(" and trim(cb.relation.bankname) is not null and trim(cb.relation.bankaccount) is not null and trim(cb.relation.panno) is not null and trim(cb.relation.ifsccode) is not null  ");
            }
            else
            	qryStr.append(" and cb.relation=:relation");
        }
    	if(dept!=null)
    	{
    		qryStr.append(" and cb.egBillregister.egBillregistermis.egDepartment=:dept");
    	}
    	if(functionary!=null)
    	{
    		qryStr.append(" and cb.egBillregister.egBillregistermis.functionaryid=:functionary");
    	}
    		
        qry =HibernateUtil.getCurrentSession().createQuery(qryStr.toString()) ;
        if(fundId!=null)
        	qry.setInteger("fundId",fundId);       
        if(vhFromDate!=null)
        	qry.setDate("vhFromDate", vhFromDate);
        if(vhToDate!=null)
        	qry.setDate("vhToDate", vhToDate);
        if(fundSource!=null)
        	qry.setEntity("fundSource",fundSource);
        if(relation!=null)
        {
        	if(!relation.getBankname().equals("RTGS"))
        		qry.setEntity("relation",relation);
        }
        if(vhNoFrom!=null && vhNoTo!=null)
        {
        	qry.setString("vhNoFrom", vhNoFrom);
        	qry.setString("vhNoTo", vhNoTo);
        }
    	else if(vhNoFrom!=null)
    		qry.setString("vhNoFrom", vhNoFrom);
    	else if(vhNoTo!=null)
    		qry.setString("vhNoTo", vhNoTo);
        if(dept!=null)
     	{
     		qry.setEntity("dept", dept);
     	}
     	if(functionary!=null)
     	{
     		qry.setBigDecimal("functionary", new BigDecimal(functionary));
     	}
    	contBillList=qry.list();
        return contBillList;
    }    
    
}
