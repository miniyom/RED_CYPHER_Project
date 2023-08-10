package com.cyphers.game.RecordSearch.cyphers.annotation;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class RequiredAspect {

//    @Before("execution(* *(.., @com.cyphers.game.RecordSearch.cyphers.annotation.Required (*), ..))")
//    public void before(JoinPoint joinPoint) {
//        for (Object arg : joinPoint.getArgs()) {
//            if (arg == null || (arg instanceof String && ((String) arg).isEmpty())) {
//                throw new IllegalArgumentException("필수 파라미터가 빠져있습니다.");
//            }
//        }
//    }

}
