package com.lambdus.emailengine.persistence;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;


public class SessionFactoryUtil {
        
        

         private static final SessionFactory sessionFactory = buildSessionFactory();

         private static SessionFactory buildSessionFactory() {
                try {
                    
                        //sessionFactory = new Configuration().configure().buildSessionFactory();
                SessionFactory sessionFactory = new Configuration()
                .addClass(TemplatePersist.class)
                .setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect")
                .setProperty("hibernate.connection.datasource", "java:jboss/datasources/MySqlDS")
                .setProperty("hibernate.current_session_context_class", "thread")
                .buildSessionFactory();
                        
                        return sessionFactory;
                    
                } catch (Throwable ex) {
                    
                    System.err.println("Initial SessionFactory creation failed." + ex);
                    throw new ExceptionInInitializerError(ex);
                }
            }

            public static SessionFactory getSessionFactory() {
                return sessionFactory;
            }

}