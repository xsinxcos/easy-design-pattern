package com.zhuo.infra.strategy.pattern;


import com.zhuo.infra.IStrategy;
import com.zhuo.infra.StrategyFacade;

/**
 * @author : wzq
 * @since : 2025-01-21 11:38
 **/
public class Test {
    public static void main(String[] args) {
        StrategyFacade facade = new StrategyFacade();
        facade.registerIStrategy(Auth.class)
                .registerIStrategyImpl(PsAuth.class , "ps");
        Auth ps = (Auth) facade.getStrategyImpl(Auth.class, "ps");
        ps.auth();
    }

    interface Auth extends IStrategy {
        void auth();
    }

    public static class PsAuth implements Auth {

        @Override
        public void auth() {
            System.out.println("this way");
        }
    }


}
