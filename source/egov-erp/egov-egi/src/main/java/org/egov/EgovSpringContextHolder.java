package org.egov;

import org.hibernate.SessionFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

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
    public static SessionFactory sessionFactory() {
        if (beanFactory != null) {
            return (SessionFactory) beanFactory.getBean("sessionFactory");
        }
        return null;
    }
}
