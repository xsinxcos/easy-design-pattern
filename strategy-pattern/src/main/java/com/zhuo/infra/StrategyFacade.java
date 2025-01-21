package com.zhuo.infra;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 对外暴露api
 *
 * @author : wzq
 * @since : 2025-01-21 11:42
 **/
public class StrategyFacade implements IStrategyFacade {
    private final StrategyFFactory strategyFFactory = new StrategyFFactory();

    @Override
    public IStrategyFacade registerIStrategy(Class<?> istrategy) {
        if (!istrategy.isInterface()) {
            throw new StrategyException(istrategy.getName() + " not interface");
        }
        if (!IStrategy.class.isAssignableFrom(istrategy)) {
            throw new StrategyException(istrategy.getName() + " not to extend interface IStrategy");
        }
        strategyFFactory.register(istrategy.getName(), new StrategyFactory());
        return this;
    }

    @Override
    public IStrategyFacade registerIStrategyImpl(Class<?> istrategyImpl, String key) {
        if (notClass(istrategyImpl)) {
            throw new StrategyException(istrategyImpl.getName() + " not class(impl)");
        }
        Class<?>[] interfaces = istrategyImpl.getInterfaces();
        try {
            IStrategy iStrategy = (IStrategy) istrategyImpl.newInstance();
            for (Class<?> anInterface : interfaces) {
                if (strategyFFactory.contain(anInterface.getName())) {
                    IStrategyFactory iStrategyFactory = strategyFFactory.get(anInterface.getName());
                    iStrategyFactory.register(key, iStrategy);
                }
            }
        } catch (InstantiationException | IllegalAccessException e) {
            throw new StrategyException(istrategyImpl.getName() + ": fail to instantiate");
        }
        return this;
    }

    @Override
    public IStrategyFacade registerIStrategyImpl(Class<?> istrategyImpl, String key, List<Object> constructData) {
        try {
            List<Class<?>> clazzs = new ArrayList<>();
            constructData.forEach(data -> clazzs.add(data.getClass()));
            Constructor<?> constructor = findConstructor(istrategyImpl, clazzs.toArray(new Class<?>[0]));
            Optional.ofNullable(constructor).orElseThrow(() -> new StrategyException(istrategyImpl.getName() + " this constructor not exist"));
            IStrategy instance = (IStrategy) constructor.newInstance(constructData.toArray(new Object[0]));
            Class<?>[] interfaces = istrategyImpl.getInterfaces();
            for (Class<?> anInterface : interfaces) {
                if (strategyFFactory.contain(anInterface.getName())) {
                    IStrategyFactory iStrategyFactory = strategyFFactory.get(anInterface.getName());
                    iStrategyFactory.register(key, instance);
                }
            }
        } catch (InvocationTargetException | IllegalAccessException |
                 InstantiationException e) {
            throw new StrategyException(e.getMessage());
        }
        return this;
    }

    private boolean notClass(Class<?> clazz) {
        return clazz.isInterface() || clazz.isAnnotation() || clazz.isArray();
    }

    @Override
    public IStrategy getStrategyImpl(Class<?> istrategy, String key) {
        return strategyFFactory.get(istrategy.getName()).getStrategy(key);
    }

    public static Constructor<?> findConstructor(Class<?> clazz, Class<?>... parameterTypes) {
        // 遍历所有构造函数
        for (Constructor<?> constructor : clazz.getConstructors()) {
            Class<?>[] constructorParameterTypes = constructor.getParameterTypes();
            // 判断参数数量是否匹配
            if (constructorParameterTypes.length == parameterTypes.length) {
                boolean match = true;
                for (int i = 0; i < parameterTypes.length; i++) {
                    // 检查每个参数类型是否兼容（子类可以匹配父类）
                    if (!constructorParameterTypes[i].isAssignableFrom(parameterTypes[i])) {
                        match = false;
                        break;
                    }
                }
                if (match) {
                    return constructor;
                }
            }
        }
        return null;
    }
}
