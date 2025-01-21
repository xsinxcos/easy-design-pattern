package com.zhuo.infra;

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

    private boolean notClass(Class<?> clazz){
        return clazz.isInterface() || clazz.isAnnotation() || clazz.isArray();
    }

    @Override
    public IStrategy getStrategyImpl(Class<?> istrategy, String key) {
        return strategyFFactory.get(istrategy.getName()).getStrategy(key);
    }
}
