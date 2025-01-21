package com.zhuo.infra;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 策略工厂
 *
 * @author : wzq
 * @since : 2025-01-21 11:27
 **/
public class StrategyFactory implements IStrategyFactory{
    private final Map<String, IStrategy> strategyMap = new ConcurrentHashMap<>();
    @Override
    public IStrategy getStrategy(String key) {
        return strategyMap.get(key);
    }

    @Override
    public void register(String key, IStrategy... strategy) {
        for (IStrategy iStrategy : strategy) {
            strategyMap.put(key, iStrategy);
        }
    }

}
