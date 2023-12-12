package com.api.gateway.bind;

import com.api.gateway.session.Configuration;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.bootstrap.DubboBootstrap;
import org.apache.dubbo.config.utils.ReferenceConfigCache;
import org.apache.dubbo.rpc.service.GenericService;

import java.util.HashMap;
import java.util.Map;

/**
 * @author root
 * @description 泛化调用注册器
 * @date 2023/11/19
 */
public class GenericReferenceRegistry {
    private final Configuration configuration;

    private final Map<String,GenericReferenceProxyFactory> knowGenericReferenceFactory = new HashMap<>();

    public GenericReferenceRegistry(Configuration configuration) {
        this.configuration = configuration;
    }

    /**
     * 注册泛化调用服务接口方法
     * @param applicationName   服务：api-gateway-test
     * @param interfaceName     接口：cn.bugstack.gateway.rpc.IActivityBooth
     * @param methodName        方法：sayHi 全局唯一
     */
    public void addGenericReference(String applicationName, String interfaceName, String methodName) {

        ApplicationConfig applicationConfig = configuration.getApplicationConfig(applicationName);
        RegistryConfig registryConfig = configuration.getRegistryConfig(applicationName);
        ReferenceConfig<GenericService> reference = configuration.getReferenceConfig(interfaceName);

        DubboBootstrap dubboBootstrap = DubboBootstrap.getInstance();
        dubboBootstrap.application(applicationConfig).registry(registryConfig).reference(reference).start();

        ReferenceConfigCache cache = ReferenceConfigCache.getCache();
        GenericService genericService = cache.get(reference);
        knowGenericReferenceFactory.put(methodName,new GenericReferenceProxyFactory(genericService));
    }

    public IGenericReference getGenericReference(String methodName) {
        GenericReferenceProxyFactory genericReferenceProxyFactory = knowGenericReferenceFactory.get(methodName);
        if (genericReferenceProxyFactory==null) {
            throw new RuntimeException("Type " + methodName + " is not known to the GenericReferenceRegistry.");
        }
        return genericReferenceProxyFactory.newInstance(methodName);
    }
}
