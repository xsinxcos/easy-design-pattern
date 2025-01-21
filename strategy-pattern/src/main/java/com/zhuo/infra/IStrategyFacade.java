package com.zhuo.infra;

import java.util.List;

/**
 * 对外暴露接口
 *
 * @author : wzq
 * @since : 2025-01-21 11:49
 **/
public interface IStrategyFacade {
    IStrategyFacade registerIStrategy(Class<?> istrategy);

    IStrategyFacade registerIStrategyImpl(Class<?> istrategyImpl, String key);

    IStrategyFacade registerIStrategyImpl(Class<?> istrategyImpl, String key, List<Class<?>> constructClazz ,List<Object> constructData);

    IStrategy getStrategyImpl(Class<?> istrategy, String key);
}
