package com.zhuo.infra;

/**
 * 策略模式异常类
 *
 * @author : wzq
 * @since : 2025-01-21 13:39
 **/
public class StrategyException extends RuntimeException{
    public StrategyException(){
        super();
    }

    public StrategyException(String msg){
        super(msg);
    }
}
