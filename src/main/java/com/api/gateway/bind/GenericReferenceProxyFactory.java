package com.api.gateway.bind;

import net.sf.cglib.core.Signature;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.InterfaceMaker;
import org.apache.dubbo.rpc.service.GenericService;
import org.objectweb.asm.Type;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author root
 * @description
 * @date 2023/11/19
 */
public class GenericReferenceProxyFactory {

    private final GenericService genericService;

    private final Map<String, IGenericReference> genericReferenceCache = new HashMap<>();

    public GenericReferenceProxyFactory(GenericService genericService) {
        this.genericService = genericService;
    }

    public IGenericReference newInstance(String methodName) {
        //computeIfAbsent 向map中添加新值并返回支持lambda表示式
        return genericReferenceCache.computeIfAbsent(methodName, k -> {
            GenericReferenceProxy proxy = new GenericReferenceProxy(genericService, methodName);
            InterfaceMaker interfaceMaker = new InterfaceMaker();
            //创建一个接口 入参和出参都是String类型 也就是json
            interfaceMaker.add(new Signature(methodName, Type.getType(String.class), new Type[]{Type.getType(String.class)}), null);
            Class interfaceClass = interfaceMaker.create();

            Enhancer enhancer = new Enhancer();
            enhancer.setSuperclass(Object.class);
            // IGenericReference 统一泛化调用接口
            // interfaceClass    根据泛化调用注册信息创建的接口，建立 http -> rpc 关联
            enhancer.setInterfaces(new Class[]{IGenericReference.class, interfaceClass});
            enhancer.setCallback(proxy);
            return (IGenericReference) enhancer.create();
        });
    }

}
