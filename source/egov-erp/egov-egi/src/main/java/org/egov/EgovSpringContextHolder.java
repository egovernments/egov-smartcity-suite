package org.egov;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

/**
 * Do not use this unless you are not able to use spring for injecting
 * appropriate beans. This has been introduced only to deal with the
 * legacy we carried by using HibernateUtil static references across
 * domain entities and JSPs which is non trivial to inject via spring.
 */
@Deprecated
public class EgovSpringContextHolder implements BeanFactoryAware {

    private static BeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    /**
     * Use spring to inject sessionFactory instead of static method of this class.
     */
    @Deprecated
    public static EntityManagerFactory sessionFactory() {
        if (beanFactory != null) {
            return (EntityManagerFactory) beanFactory.getBean("entityManagerFactory");
        }
        return null;
    }
}
