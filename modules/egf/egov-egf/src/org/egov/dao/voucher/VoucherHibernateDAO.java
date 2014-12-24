/**
 * 
 */
package org.egov.dao.voucher;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.egov.exceptions.EGOVException;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.commons.Accountdetailtype;
import org.egov.commons.CGeneralLedger;
import org.egov.commons.CGeneralLedgerDetail;
import org.egov.commons.CVoucherHeader;
import org.egov.commons.VoucherDetail;
import org.egov.commons.utils.EntityType;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.commons.dao.GenericHibernateDaoFactory;
import org.egov.infstr.config.AppConfigValues;
import org.egov.infstr.services.PersistenceService;
import org.egov.utils.Constants;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * @author Manoranjan
 *
 */
public class VoucherHibernateDAO extends PersistenceService<CVoucherHeader, Long> {
	
	private static final Logger	LOGGER	= Logger.getLogger(VoucherHibernateDAO.class);
	private PersistenceService<VoucherDetail, Integer> vdPersitSer;
	private GenericHibernateDaoFactory genericDao;
	public List<CVoucherHeader> getVoucherList(final CVoucherHeader voucherHeader,
			final Map<String, Object> searchFilterMap) throws EGOVException,ParseException{
		
		StringBuffer sql= new StringBuffer(500);
		sql.append(" and vh.type='Journal Voucher' ");
		sql.append(" and vh.isConfirmed != 1 ");
		if(null != voucherHeader.getVoucherNumber() && StringUtils.isNotEmpty(voucherHeader.getVoucherNumber())){
			
		 sql.append(" and vh.voucherNumber like '%").append(voucherHeader.getVoucherNumber()).append("%'");
		}	
		if (null != searchFilterMap.get(Constants.VOUCHERDATEFROM) && StringUtils.isNotEmpty
				(searchFilterMap.get(Constants.VOUCHERDATEFROM).toString())) {
			sql.append(" and vh.voucherDate>='").append(Constants.DDMMYYYYFORMAT1.format(Constants.DDMMYYYYFORMAT2.
					parse(searchFilterMap.get(Constants.VOUCHERDATEFROM).toString()))).append("'");
		}
		if (null != searchFilterMap.get(Constants.VOUCHERDATETO)  && StringUtils.isNotEmpty
				(searchFilterMap.get(Constants.VOUCHERDATETO).toString())) {
			sql.append(" and vh.voucherDate<='").append(Constants.DDMMYYYYFORMAT1.format(Constants.DDMMYYYYFORMAT2.
					parse(searchFilterMap.get(Constants.VOUCHERDATETO).toString()))).append("'");
		}
		
		if(null != voucherHeader.getFundId()){
			 sql.append(" and vh.fundId=").append(voucherHeader.getFundId().getId());
		}
		if(null != voucherHeader.getFundsourceId()){
			sql.append(" and vh.fundsourceId=" ).append(voucherHeader.getFundsourceId().getId());
		}
		
		if(null != voucherHeader.getVouchermis().getDepartmentid()){
			sql.append(" and vh.vouchermis.departmentid=").append(voucherHeader.getVouchermis().getDepartmentid().getId());
		}
			
		if(voucherHeader.getVouchermis().getSchemeid()!=null){
			sql.append(" and vh.vouchermis.schemeid=").append(voucherHeader.getVouchermis().getSchemeid().getId());
		}
			
		if(null != voucherHeader.getVouchermis().getSubschemeid())
			sql.append(" and vh.vouchermis.subschemeid=").append(voucherHeader.getVouchermis().getSubschemeid().getId());
		if(null != voucherHeader.getVouchermis().getFunctionary()){
			sql.append(" and vh.vouchermis.functionary=").append(voucherHeader.getVouchermis().getFunctionary().getId());
			
		}
		if(null != voucherHeader.getVouchermis().getDivisionid()){
			 sql.append(" and vh.vouchermis.divisionid=").append(voucherHeader.getVouchermis().getDivisionid().getId());
		}
			
		LOGGER.debug("sql===================="+ sql.toString());
		final List<AppConfigValues> appList = genericDao.getAppConfigValuesDAO().getConfigValuesByModuleAndKey("finance","statusexcludeReport");
		final String statusExclude = appList.get(0).getValue();
		
		List<CVoucherHeader> list = (List<CVoucherHeader>)findAllBy(" from CVoucherHeader vh where vh.status not in ("+statusExclude+") "+sql.toString()+" order by vh.cgn,vh.voucherNumber,vh.voucherDate ");
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public CVoucherHeader getVoucherHeaderById(final Long voucherId){
		
		LOGGER.debug("VoucherHibernateDAO | getVoucherHeaderById | Start ");
		List<CVoucherHeader> vhList = getSession().createCriteria(CVoucherHeader.class).
		add(Restrictions.eq("id", voucherId)).list();
		LOGGER.debug("numer of voucher with voucherheaderid "+voucherId+"="+vhList.size());
		return vhList.get(0);
	}
	@SuppressWarnings("unchecked")
	public List<CGeneralLedger> getGLInfo(final Long voucherId){
		LOGGER.debug("VoucherHibernateDAO | getGLInfo | Start ");
		return getSession().createCriteria(CGeneralLedger.class).createCriteria("voucherHeaderId").add(Restrictions.eq("id", voucherId)).list();
		
	}
	
	@SuppressWarnings("unchecked")
	public List<CGeneralLedgerDetail> getGeneralledgerdetail(final Integer gledgerId){
	
		Criteria criteria = getSession().createCriteria(CGeneralLedgerDetail.class);
		criteria.add(Restrictions.eq("generalLedgerId", gledgerId));
		return criteria.list();
		
	}
	public Accountdetailtype getAccountDetailById(final Integer accDetailTypeId){
		
		Criteria criteria = getSession().createCriteria(Accountdetailtype.class);
		criteria.add(Restrictions.eq("id", accDetailTypeId));
		return (Accountdetailtype)criteria.list().get(0);
		
	}
	public EntityType getEntityInfo(final Integer detailKeyId,final Integer detailtypeId) throws ValidationException
	{	
		LOGGER.debug("VoucherHibernateDAO | getDetailCodeName | start");
		EntityType entity = null;
		try {
				Accountdetailtype accountdetailtype= getAccountDetailById(detailtypeId);
				Class<?> service = Class.forName(accountdetailtype.getFullQualifiedName());
				// getting the entity type service.
				String detailTypeName = service.getSimpleName();
				String detailTypeService =  detailTypeName.substring(0,1).toLowerCase()+detailTypeName.substring(1)+"Service";
				LOGGER.debug("VoucherHibernateDAO | detailtype service name = "+ detailTypeService);
				WebApplicationContext wac= WebApplicationContextUtils.getWebApplicationContext(ServletActionContext.getServletContext());
				PersistenceService entityPersistenceService=(PersistenceService)wac.getBean(detailTypeService);
				String dataType = "";
				// required to know data type of the id of the detail  type object.
				java.lang.reflect.Method method = service.getMethod("getId");
				dataType = method.getReturnType().getSimpleName();
				if ( dataType.equals("Long") ){
					entity=(EntityType)entityPersistenceService.findById(Long.valueOf(detailKeyId.toString()),false);
				}else{
					entity=(EntityType)entityPersistenceService.findById(detailKeyId,false);
				}
		} catch (Exception e) {
			 List<ValidationError> errors=new ArrayList<ValidationError>();
			 errors.add(new ValidationError("exp",e.getMessage()));
			 throw new ValidationException(errors);
		}
		LOGGER.debug("VoucherHibernateDAO | getDetailCodeName | End");
		return entity;
		
	}
	public VoucherDetail postInVoucherDetail(VoucherDetail model){
		
		try {
			vdPersitSer.persist(model);
		} catch (HibernateException e) {
			throw new HibernateException("exception voucherHibDao | postInVoucherDetail"+e);
		}
		return model;
	}
	public void deleteVoucherDetailByVHId(final Object voucherHeaderId){
		
		try {
			Query qry = getSession().createQuery("delete from VoucherDetail where voucherHeaderId.id=:vhid");
			qry.setLong("vhid", Long.parseLong(voucherHeaderId.toString()));
			qry.executeUpdate();
			
		} catch (HibernateException e) {
			throw new HibernateException("exception in voucherHibDao while deleting from voucher detail"+e);
		}catch (EGOVRuntimeException e) {
			throw new EGOVRuntimeException("exception in voucherHibDao while deleting from voucher detail"+e);
		}
		
	}
	@SuppressWarnings("unchecked")
	public void deleteGLDetailByVHId(final Object voucherHeaderId){
		
		try {
			/**
			 *  Deleting record from general ledger detail.
			 */
			List<CGeneralLedger> glList = getGLInfo(Long.parseLong(voucherHeaderId.toString()));
			for (CGeneralLedger generalLedger : glList) {
				List<CGeneralLedgerDetail> glDetailList =(List<CGeneralLedgerDetail>) getSession().
				createCriteria(CGeneralLedgerDetail.class).add(Restrictions.eq("generalLedgerId", Integer.valueOf(generalLedger.getId().toString()))).list();
				for (CGeneralLedgerDetail generalLedgerDetail : glDetailList) {
					Query qry = getSession().createQuery("delete from EgRemittanceGldtl where generalledgerdetail.id=:gldetailId");
					qry.setInteger("gldetailId", Integer.valueOf(generalLedgerDetail.getId().toString()));
					qry.executeUpdate();
				}
				Query qry = getSession().createQuery("delete from CGeneralLedgerDetail where generalLedgerId=:glId");
				qry.setInteger("glId", Integer.valueOf(generalLedger.getId().toString()));
				qry.executeUpdate();
			}
			/**
			 *  Deleting record from general ledger .
			 */
			Query qry = getSession().createQuery("delete from CGeneralLedger where voucherHeaderId.id=:vhid");
			qry.setInteger("vhid", Integer.valueOf(voucherHeaderId.toString()));
			qry.executeUpdate();
			
		} catch (HibernateException e) {
			throw new HibernateException("exception in voucherHibDao while deleting from general ledger"+e);
		}catch (EGOVRuntimeException e) {
			throw new EGOVRuntimeException("exception in voucherHibDao while deleting from general ledger"+e);
		}
		
	}
	public PersistenceService<VoucherDetail, Integer> getVdPersitSer() {
		return vdPersitSer;
	}
	public void setVdPersitSer(
			PersistenceService<VoucherDetail, Integer> vdPersitSer) {
		this.vdPersitSer = vdPersitSer;
	}

	public GenericHibernateDaoFactory getGenericDao() {
		return genericDao;
	}

	public void setGenericDao(GenericHibernateDaoFactory genericDao) {
		this.genericDao = genericDao;
	}
	
}
