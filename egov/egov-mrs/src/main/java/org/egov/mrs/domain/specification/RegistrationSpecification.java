package org.egov.mrs.domain.specification;

import java.time.LocalDate;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

//import org.apache.poi.hssf.record.formula.functions.T;
import org.egov.mrs.domain.entity.MarriageRegistration;
import org.springframework.data.jpa.domain.Specification;

public class RegistrationSpecification {

   /* public static Specification<Registration> registrationNo() {
      return new Specification<Registration> {
        public Predicate toPredicate(Root<T> root, CriteriaQuery query, CriteriaBuilder cb) {
          return cb.equal(root.get(Registration.birthday), today);
        }
      };
    }

    public static Specification<Registration> dateOfMarriage() {
      return new Specification<Registration> {
        public Predicate toPredicate(Root<T> root, CriteriaQuery query, CriteriaBuilder cb) {
          return cb.lessThan(root.get(Registration.createdAt), new LocalDate.minusYears(2));
        }
      };
    }*/
  }
