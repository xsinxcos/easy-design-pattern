package com.zhuo.infra.strategy.pattern;


import com.zhuo.infra.IStrategy;
import com.zhuo.infra.StrategyFacade;

import java.util.*;

/**
 * @author : wzq
 * @since : 2025-01-21 11:38
 **/
public class Test {
    public static void main(String[] args) {
        StrategyFacade facade = new StrategyFacade();
        facade.registerIStrategy(Auth.class);

        facade.registerIStrategyImpl(PsAuth.class , "ps");
        Auth ps = (Auth) facade.getStrategyImpl(Auth.class, "ps");
        ps.auth();

        List<Class<?>> clazz = new ArrayList<>();
        clazz.add(Map.class);
        List<Object> objects = new ArrayList<>();
        objects.add(new HashMap<>());
        facade.registerIStrategyImpl(AAAuth.class, "aa" ,clazz, objects);
        Auth aa = (Auth) facade.getStrategyImpl(Auth.class, "aa");
        aa.auth();
    }

    interface Auth extends IStrategy {
        void auth();
    }

    public static class PsAuth implements Auth {

        @Override
        public void auth() {
            System.out.println("this PsAuth");
        }
    }

    public static class AAAuth implements Auth {
        private Map<String, String> map;

        public AAAuth(Map<String, String> map) {
            this.map = map;
        }

        @Override
        public void auth() {
            System.out.println("this AAAuth");
        }
    }

}
