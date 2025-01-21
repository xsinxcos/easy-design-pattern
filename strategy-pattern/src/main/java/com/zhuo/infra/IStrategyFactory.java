package com.zhuo.infra;

/**
 * 策略接口
 *
 * @author : wzq
 * @since : 2025-01-21 11:14
 **/
public interface IStrategyFactory {
    IStrategy getStrategy(String key);

    void register(String key, IStrategy... strategy);
}
