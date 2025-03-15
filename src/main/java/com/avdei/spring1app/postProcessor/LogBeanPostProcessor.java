package com.avdei.spring1app.postProcessor;

import com.avdei.spring1app.validator.PersonCreateValidator;
import com.avdei.spring1app.validator.TaskValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class LogBeanPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof PersonCreateValidator){
            PersonCreateValidator personValidator = (PersonCreateValidator) bean;
            log.warn("{} bean is being initialized", personValidator.getClass().getSimpleName());
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof TaskValidator){
            TaskValidator taskValidator = (TaskValidator) bean;
            log.warn("{} bean is being initialized", taskValidator.getClass().getSimpleName());
        }
        return bean;
    }
}
