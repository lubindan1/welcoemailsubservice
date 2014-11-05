package com.welcohealth.email.service;


import java.util.Set;
import java.util.HashSet;

import javax.ws.rs.core.Application;

public class JaxRsInitializer extends Application {

        private Set<Object> singletons = new HashSet<Object>();
        private Set<Class<?>> empty = new HashSet<Class<?>>();
        public JaxRsInitializer(){
             singletons.add(new UserRestDefinition());
        }
        @Override
        public Set<Class<?>> getClasses() {
             return empty;
        }
        @Override
        public Set<Object> getSingletons() {
             return singletons;
        }
}
