package com.netflix.ndbench.core.config;

import com.google.common.collect.Lists;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.netflix.archaius.guice.ArchaiusModule;
import com.netflix.ndbench.api.plugin.annotations.NdBenchClientPluginGuiceModule;
import com.netflix.ndbench.core.defaultimpl.NdBenchClientModule;
import com.netflix.ndbench.core.defaultimpl.NdBenchGuiceModule;
import org.reflections.Reflections;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class GuiceInjectorProvider {
    private static final org.slf4j.Logger Logger = LoggerFactory.getLogger(GuiceInjectorProvider.class);

    private List<Module> getModuleList(AbstractModule... modules) {
        List<Module> moduleList = Lists.newArrayList();

        // Add default list of modules
        moduleList.add(new NdBenchGuiceModule());
        moduleList.add(new NdBenchClientModule());
        moduleList.add(new ArchaiusModule()); //Archaius-2

        // Add any additional caller specified modules
        moduleList.addAll(Arrays.asList(modules));
        return moduleList;
    }


    /**
     * Creates an injector using modules obtained from the following sources:  (1) the hard coded list of modules
     * specified in the {@link GuiceInjectorProvider #getModulesList()} method of this class,  (2)  the  'modules'
     * list passed as the first and only argument to this method
     *
     * @param modules - any additional Guice binding modules which will supplement the list of  those added by default
     */
    public Injector getInjector(AbstractModule ... modules) {
        List<Module> moduleList = getModuleList(modules);
        Injector injector = Guice.createInjector(moduleList);
        injector.getInstance(IConfiguration.class).initialize();
        return injector;
    }
}
