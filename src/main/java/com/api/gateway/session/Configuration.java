package com.api.gateway.session;

import com.api.gateway.bind.GenericReferenceRegistry;
import com.api.gateway.bind.IGenericReference;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.rpc.service.GenericService;
import sun.net.www.content.text.Generic;

import java.util.HashMap;
import java.util.Map;

/**
 * @author root
 * @description 配置类 贯穿会话周期
 * @date 2023/11/19
 */
public class Configuration {

    private final GenericReferenceRegistry registry = new GenericReferenceRegistry(this);
    // RPC 应用服务配置项 api-gateway-test
    private final Map<String, ApplicationConfig> applicationConfigMap = new HashMap<>();
    // RPC 注册中心配置项 zookeeper://127.0.0.1:2181
    private final Map<String, RegistryConfig> registryConfigMap = new HashMap<>();

    // RPC 泛化服务配置项  // RPC 注册中心配置项 zookeeper://127.0.0.1:2181
    private final Map<String, ReferenceConfig<GenericService>> refenenceConfigMap = new HashMap<>();

    public Configuration(){
        ApplicationConfig applicationConfig = new ApplicationConfig();
        applicationConfig.setName("api-gateway-test");
        applicationConfig.setQosEnable(false);


        RegistryConfig registryConfig = new RegistryConfig();
        registryConfig.setAddress("zookeeper://127.0.0.1:2181");
        registryConfig.setRegister(false);

        ReferenceConfig<GenericService> referenceConfig = new ReferenceConfig<>();
        referenceConfig.setInterface("cn.bugstack.gateway.rpc.IActivityBooth");
        referenceConfig.setVersion("1.0.0");
        referenceConfig.setGeneric("true");

        applicationConfigMap.put("api-gateway-test",applicationConfig);

        registryConfigMap.put("api-gateway-test",registryConfig);

        refenenceConfigMap.put("cn.bugstack.gateway.rpc.IActivityBooth",referenceConfig);
    }

    public ApplicationConfig getApplicationConfig(String applicationName){
        return applicationConfigMap.get(applicationName);
    }

    public RegistryConfig getRegistryConfig(String applicationName){
        return registryConfigMap.get(applicationName);
    }
    public ReferenceConfig<GenericService> getReferenceConfig(String applicationName){
        return refenenceConfigMap.get(applicationName);
    }

    public void addGenericReference(String applicationName, String interfaceName, String version){
        registry.addGenericReference(applicationName, interfaceName, version);
    }

    public IGenericReference getGenericReference(String methodName){
        return registry.getGenericReference(methodName);
    }

}
