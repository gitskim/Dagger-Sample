package com.test.xyz.daggersample1.di;

import com.test.xyz.daggersample1.di.module.CommonModule;
import com.test.xyz.daggersample1.service.api.HelloService;
import com.test.xyz.daggersample1.service.impl.HelloServiceDebugManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(includes = CommonModule.class)
public class ServiceModule {

    @Provides
    @Singleton
    HelloService provideHelloService() {
        return new HelloServiceDebugManager();
    }

}
