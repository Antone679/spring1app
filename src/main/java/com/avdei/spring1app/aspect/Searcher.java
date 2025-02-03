package com.avdei.spring1app.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class Searcher {

    @Pointcut("within(com.avdei.spring1app.controller.AuthenticationController)")
    public void authControllerPointCut(){}

    @Pointcut("execution(public * com.avdei.spring1app.service.PeopleService.getPersonByUserName(..))")
    public void getPersonPointCut() {}
}
