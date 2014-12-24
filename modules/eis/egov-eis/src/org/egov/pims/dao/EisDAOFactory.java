package org.egov.pims.dao;



/**
 * Defines all DAOs and the concrete factories to get the conrecte DAOs.
 * <p>
 * Either use the <tt>DEFAULT</tt> to get the same concrete RNDDAOFactory
 * throughout your application, or a concrete factory by name, e.g.
 * <tt>RNDDAOFactory.HIBERNATE</tt> is a concrete <tt>RNDHibernateDAOFactory</tt>.
 * <p>
 * Implementation: If you write a new DAO, this class has to know about it.
 * If you add a new persistence mechanism, add an additional concrete factory
 * for it to the enumeration of factories.
 * <p>
 * It probably wouldn't be a bad idea to move the <tt>DEFAULT</tt> setting
 * into external configuration.
 *
 * @author christian.bauer@jboss.com
 */
public abstract class EisDAOFactory {

    private static final EisDAOFactory EJB3_PERSISTENCE = null;

    private static final EisDAOFactory HIBERNATE = new EisHibernateDAOFactory ();


    public static EisDAOFactory getDAOFactory()
    {
    	return HIBERNATE;
    }
    public abstract PersonalInformationDAO getPersonalInformationDAO();
    public abstract BankDetDAO getBankDetDAO();
    public abstract DeptTestsDAO getDeptTestsDAO();
    public abstract DetOfEnquiryOfficerDAO getDetOfEnquiryOfficerDAO();
    public abstract EduDetailsDAO getEduDetailsDAO();
    public abstract DisciplinaryPunishmentDAO getDisciplinaryPunishmentDAO();
    public abstract ImmovablePropDetailsDAO getImmovablePropDetailsDAO();
    public abstract LangKnownDAO getLangKnownDAO();
    public abstract LtcPirticularsDAO getLtcPirticularsDAO();
    public abstract MovablePropDetailsDAO getMovablePropDetailsDAO();
    public abstract NomimationPirticularsDAO getNomimationPirticularsDAO();
    public abstract ProbationDAO getProbationDAO();
    public abstract RegularisationDAO getRegularisationDAO();
    
    public abstract TecnicalQualificationDAO getTecnicalQualificationDAO();
    public abstract TrainingPirticularsDAO getTrainingPirticularsDAO();
    public abstract PersonAddressDAO getPersonAddressDAO();
    public abstract AssignmentPrdDAO getAssignmentPrdDAO();
    public abstract AssignmentDAO getAssignmentDAO();
	public abstract DisciplinaryPunishmentApprovalDAO getDisciplinaryPunishmentApprovalDAO() ;
	public abstract EmployeeDepartmentDAO getEmployeeDepartmentDAO() ;
	public abstract GradeMasterDao getEisGradeDAO();
	public abstract PayFixedInMasterDAO getPayFixedInMasterDAO();
}
