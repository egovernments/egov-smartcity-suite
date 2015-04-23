
grep -rl 'extends AbstractStatelessSessionBean' ./ | xargs sed -i 's/extends AbstractStatelessSessionBean/ /g'


grep -rl 'public void ejbCreate() throws CreateException {}' ./ | xargs sed -i 's/public void ejbCreate() throws CreateException {}/ /g'

grep -rl 'org.egov.EGOVException' ./ | xargs sed -i 's/org.egov.EGOVException/org.egov.exceptions.EGOVException/g'

grep -rl 'org.egov.EGOVRuntimeException' ./ | xargs sed -i 's/org.egov.EGOVRuntimeException/org.egov.exceptions.EGOVRuntimeException/g'


grep -rl 'org.egov.lib.rjbac.dept.DepartmentImpl' ./ | xargs sed -i 's/org.egov.lib.rjbac.dept.DepartmentImpl/org.egov.infra.admin.master.entity.Department/g'

grep -rl 'DepartmentImpl' ./ | xargs sed -i 's/DepartmentImpl/Department/g'


grep -rl 'org.egov.lib.admbndry.BoundaryImpl' ./ | xargs sed -i 's/org.egov.lib.admbndry.BoundaryImpl/org.egov.infra.admin.master.entity.Boundary/g'

grep -rl 'BoundaryImpl' ./ | xargs sed -i 's/BoundaryImpl/Boundary/g'


grep -rl 'org.egov.infstr.models.StateAware' ./ | xargs sed -i 's/org.egov.infstr.models.StateAware/org.egov.infra.workflow.entity.StateAware/g'



grep -rl 'org.egov.lib.admbndry.BoundaryTypeImpl' ./ | xargs sed -i 's/org.egov.lib.admbndry.BoundaryTypeImpl/org.egov.infra.admin.master.entity.BoundaryType/g'

grep -rl 'BoundaryTypeImpl' ./ | xargs sed -i 's/BoundaryTypeImpl/BoundaryType/g'

grep -rl 'BillsAccountingManager' ./ | xargs sed -i 's/BillsAccountingManager/BillsAccountingService/g'

grep -rl 'billsAccountingManager' ./ | xargs sed -i 's/billsAccountingManager/billsAccountingService/g'

grep -rl 'EisCommonsManager' ./ | xargs sed -i 's/EisCommonsManager/EisCommonsService/g'

grep -rl 'eisCommonsManager' ./ | xargs sed -i 's/eisCommonsManager/eisCommonsService/g'

grep -rl 'CommonsManager' ./ | xargs sed -i 's/CommonsManager/CommonsService/g'

grep -rl 'commonsManager' ./ | xargs sed -i 's/commonsManager/commonsService/g'

grep -rl 'import org.egov.commons.service.CommonsServiceHome;' ./ | xargs sed -i 's/import org.egov.commons.service.CommonsServiceHome;/ /g'

grep -rl 'import org.egov.lib.rjbac.user.UserImpl;' ./ | xargs sed -i 's/import org.egov.lib.rjbac.user.UserImpl;/import org.egov.infra.admin.master.entity.User;/g'


grep -rl 'UserImpl' ./ | xargs sed -i 's/UserImpl/User/g'


grep -rl 'import org.hibernate.Hibernate;' ./ | xargs sed -i 's/import org.hibernate.Hibernate;/import org.hibernate.type.*;/g'


grep -rl 'Hibernate.STRING' ./ | xargs sed -i 's/Hibernate.STRING/StringType.INSTANCE/g'

grep -rl 'Hibernate.BOOLEAN' ./ | xargs sed -i 's/Hibernate.BOOLEAN/BooleanType.INSTANCE/g'

grep -rl 'Hibernate.LONG' ./ | xargs sed -i 's/Hibernate.LONG/LongType.INSTANCE/g'

grep -rl 'Hibernate.BIG_DECIMAL' ./ | xargs sed -i 's/Hibernate.BIG_DECIMAL/BigDecimalType.INSTANCE/g'


grep -rl 'import org.egov.infstr.utils.ejb.support.AbstractStatelessSessionBean;' ./ | xargs sed -i 's/import org.egov.infstr.utils.ejb.support.AbstractStatelessSessionBean;/ /g'

grep -rl 'CommonsServiceBean' ./ | xargs sed -i 's/CommonsServiceBean/CommonsService/g'

grep -rl 'import org.egov.lib.rjbac.dept.ejb.api.DepartmentManager;' ./ | xargs sed -i 's/import org.egov.lib.rjbac.dept.ejb.api.DepartmentManager;/import org.egov.infra.admin.master.service.DepartmentService;/g'


grep -rl 'DepartmentManager' ./ | xargs sed -i 's/DepartmentManager/DepartmentService/g'


grep -rl 'import org.egov.lib.admbndry.ejb.api.BoundaryManager;' ./ | xargs sed -i 's/import org.egov.lib.admbndry.ejb.api.BoundaryManager;/import org.egov.infra.admin.master.service.BoundaryService;/g'


grep -rl 'BoundaryManager' ./ | xargs sed -i 's/BoundaryManager/BoundaryService/g'

grep -rl 'SalaryBillManager' ./ | xargs sed -i 's/SalaryBillManager/SalaryBillService/g'


grep -rl 'import org.egov.lib.admbndry.CityWebsite;' ./ | xargs sed -i 's/import org.egov.lib.admbndry.CityWebsite;/import org.egov.infra.admin.master.entity.CityWebsite;/g'
grep -rl 'import org.egov.lib.admbndry.CityWebsite;
' ./ | xargs sed -i 's/import org.egov.lib.admbndry.CityWebsite;
/import org.egov.infra.admin.master.entity.CityWebsite;/g'







grep -rl 'BoundaryImpl' ./ | xargs sed -i 's/BoundaryImpl/Boundary/g'




import javax.ejb.CreateException;






