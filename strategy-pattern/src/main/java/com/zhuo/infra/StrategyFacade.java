package com.zhuo.infra;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * 对外暴露api
 *
 * @author : wzq
 * @since : 2025-01-21 11:42
 **/
public class StrategyFacade implements IStrategyFacade{
    private final StrategyFFactory strategyFFactory = new StrategyFFactory();

    @Override
    public IStrategyFacade registerIStrategy(Class<?> istrategy) {
        if (istrategy.isInterface()){
            strategyFFactory.register(istrategy.getName(), new StrategyFactory());
            return this;
        }
        throw new StrategyException(istrategy.getName() + " not interface");
    }

    @Override
    public IStrategyFacade registerIStrategyImpl(Class<?> istrategyImpl, String key) {
        if(notClass(istrategyImpl)){
            throw new StrategyException(istrategyImpl.getName() + " not class(impl)");
        }
        Class<?>[] interfaces = istrategyImpl.getInterfaces();
        try {
            IStrategy iStrategy = (IStrategy) istrategyImpl.newInstance();
            for (Class<?> anInterface : interfaces) {
                if(strategyFFactory.contain(anInterface.getName())){
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
    public IStrategyFacade registerIStrategyImpl(Class<?> istrategyImpl, String key, List<Class<?>> constructClazz, List<Object> constructData) {
        if (constructData.size() != constructClazz.size()){
            throw new StrategyException(istrategyImpl.getName() + ": constructData.size not equal constructClazz.size");
        }
        try {
            Constructor<?> constructor = istrategyImpl.getConstructor(constructClazz.toArray(new Class<?>[0]));
            IStrategy instance = (IStrategy)constructor.newInstance(constructData.toArray(new Object[0]));
            Class<?>[] interfaces = istrategyImpl.getInterfaces();
            for (Class<?> anInterface : interfaces) {
                if(strategyFFactory.contain(anInterface.getName())){
                    IStrategyFactory iStrategyFactory = strategyFFactory.get(anInterface.getName());
                    iStrategyFactory.register(key, instance);
                }
            }
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException | InstantiationException e) {
            throw new StrategyException(e.getMessage());
        }
        return null;
    }

    private boolean notClass(Class<?> clazz){
        return clazz.isInterface() || clazz.isAnnotation() || clazz.isArray();
    }

    @Override
    public IStrategy getStrategyImpl(Class<?> istrategy, String key) {
        return strategyFFactory.get(istrategy.getName()).getStrategy(key);
    }
}
