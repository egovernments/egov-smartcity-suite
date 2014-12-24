package org.egov.pims.dao;
import org.apache.log4j.Logger;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.pims.model.Assignment;
import org.egov.pims.model.AssignmentPrd;
import org.egov.pims.model.BankDet;

import org.egov.pims.model.DetOfEnquiryOfficer;
import org.egov.pims.model.DisciplinaryPunishment;
import org.egov.pims.model.DisciplinaryPunishmentApproval;
import org.egov.pims.model.EduDetails;
import org.egov.pims.model.EmployeeDepartment;
import org.egov.pims.model.GradeMaster;
import org.egov.pims.model.ImmovablePropDetails;
import org.egov.pims.model.LangKnown;
import org.egov.pims.model.LtcPirticulars;
import org.egov.pims.model.MovablePropDetails;
import org.egov.pims.model.NomimationPirticulars;
import org.egov.pims.model.PayFixedInMaster;
import org.egov.pims.model.PersonAddress;
import org.egov.pims.model.PersonalInformation;
import org.egov.pims.model.Probation;
import org.egov.pims.model.Regularisation;

import org.egov.pims.model.TecnicalQualification;
import org.egov.pims.model.TrainingPirticulars;
import org.hibernate.Session;


/**
 * Returns Hibernate-specific instances of DAOs.
 * <p>
 * One of the responsiblities of the factory is to inject a Hibernate Session
 * into the DAOs. You can customize the getCurrentSession() method if you
 * are not using the default strategy, which simply delegates to
 * <tt>HibernateUtil.getCurrentSession()</tt>, and also starts a transaction
 * lazily, if none exists already for the current thread or current EJB.
 * <p>
 * If for a particular DAO there is no additional non-CRUD functionality, we use
 * an inner class to implement the interface in a generic way. This allows clean
 * refactoring later on, should the interface implement business data access
 * methods at some later time. Then, we would externalize the implementation into
 * its own first-level class. We can't use anonymous inner classes for this trick
 * because they can't extend or implement an interface and they can't include
 * constructors.
 *
 * @author christian.bauer@jboss.com
 */
public class EisHibernateDAOFactory extends EisDAOFactory
{
	protected Session getCurrentSession()
    {
        return HibernateUtil.getCurrentSession();
    }

    public  PersonAddressDAO getPersonAddressDAO()
    {
		return new PersonAddressHibernateDAO(PersonAddress.class, getCurrentSession());
	}
    public PersonalInformationDAO getPersonalInformationDAO()
	{
    	return new PersonalInformationHibernateDAO(PersonalInformation.class, getCurrentSession());
	}
	public BankDetDAO getBankDetDAO()
	{
	    	return new BankDetHibernateDAO(BankDet.class, getCurrentSession());
	}

	public DeptTestsDAO getDeptTestsDAO()
	{
	    	return new DeptTestsHibernateDAO(BankDet.class, getCurrentSession());
	}
	
	public  AssignmentDAO getAssignmentDAO()
	{
			return new AssignmentHibernateDAO(Assignment.class, getCurrentSession());
	}
	public  AssignmentPrdDAO getAssignmentPrdDAO()
	{
		return new AssignmentPrdHibernateDAO(AssignmentPrd.class, getCurrentSession());
	}
	public DetOfEnquiryOfficerDAO getDetOfEnquiryOfficerDAO()
	{
	    	return new DetOfEnquiryOfficerHibernateDAO(DetOfEnquiryOfficer.class, getCurrentSession());
	}
	public EduDetailsDAO getEduDetailsDAO()
	{
	    	return new EduDetailsHibernateDAO(EduDetails.class, getCurrentSession());
	}
	public  DisciplinaryPunishmentDAO  getDisciplinaryPunishmentDAO()
	{
		   return new DisciplinaryPunishmentHibernateDAO(DisciplinaryPunishment.class, getCurrentSession());

	}
	public ImmovablePropDetailsDAO getImmovablePropDetailsDAO()
	{
		    return new ImmovablePropDetailsHibernateDAO(ImmovablePropDetails.class, getCurrentSession());
	}
	public LangKnownDAO getLangKnownDAO()
	{
			    return new LangKnownHibernateDAO(LangKnown.class, getCurrentSession());
	}
	public LtcPirticularsDAO getLtcPirticularsDAO()
	{
				return new LtcPirticularsHibernateDAO(LtcPirticulars.class, getCurrentSession());
	}
	public MovablePropDetailsDAO getMovablePropDetailsDAO()
	{
			return new MovablePropDetailsHibernateDAO(MovablePropDetails.class, getCurrentSession());
	}
	public NomimationPirticularsDAO getNomimationPirticularsDAO()
		{
				return new NomimationPirticularsHibernateDAO(NomimationPirticulars.class, getCurrentSession());
	}
	public ProbationDAO getProbationDAO()
	{
					return new ProbationHibernateDAO(Probation.class, getCurrentSession());
	}
	public RegularisationDAO getRegularisationDAO()
	{
						return new RegularisationHibernateDAO(Regularisation.class, getCurrentSession());
	}



public TecnicalQualificationDAO getTecnicalQualificationDAO()
		{
							return new TecnicalQualificationHibernateDAO(TecnicalQualification.class, getCurrentSession());
	}
public TrainingPirticularsDAO getTrainingPirticularsDAO()
		{
							return new TrainingPirticularsHibernateDAO(TrainingPirticulars.class, getCurrentSession());
	}

public  DisciplinaryPunishmentApprovalDAO getDisciplinaryPunishmentApprovalDAO() 
{
	return new DisciplinaryPunishmentApprovalHibernateDAO(DisciplinaryPunishmentApproval.class, getCurrentSession());
}
public  EmployeeDepartmentDAO getEmployeeDepartmentDAO() 
{
	return new EmployeeDepartmentHibernateDAO(EmployeeDepartment.class, getCurrentSession());
}



public GradeMasterDao getEisGradeDAO() {
	
	return new GradeMasterHibernateDAO(GradeMaster.class,getCurrentSession());
}
public PayFixedInMasterDAO getPayFixedInMasterDAO() {
	
	return new PayFixedInMasterHibernateDAO(PayFixedInMaster.class,getCurrentSession());
}
}
