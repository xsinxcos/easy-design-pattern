package com.zhuo.infra;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 策略工厂
 *
 * @author : wzq
 * @since : 2025-01-21 11:12
 **/
public class StrategyFFactory {
    private final Map<String, IStrategyFactory> strategyMap = new ConcurrentHashMap<>();

    public void register(String key ,IStrategyFactory strategyFactory){
        strategyMap.put(key, strategyFactory);
    }

    public IStrategyFactory get(String key){
        return strategyMap.get(key);
    }

    public boolean contain(String key){
        return strategyMap.containsKey(key);
    }
}
