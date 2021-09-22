package edu.miu.cs.appointmentsystem.integration.aop;

import java.time.LocalDateTime;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.miu.cs.appointmentsystem.domain.User;
import edu.miu.cs.appointmentsystem.domain.base.BaseEntity;
import edu.miu.cs.appointmentsystem.services.UserService;

@Aspect
@Component
public class EntityBaseAdvice {
    @Autowired
    private UserService userService;

    @Around("execution(* edu.miu.cs.appointmentsystem.repositories..*(..))")
    public Object updateCreatedAndUpdatedByAndDateOnReposotries(ProceedingJoinPoint joinpoint) throws Throwable {
        return updateEntityBaseObject(joinpoint);
    }

    @Around("execution(* edu.miu.cs.appointmentsystem.domain.Reservation..*(..))")
    public Object updateCreatedAndUpdatedByAndDateOnDomainClasses(ProceedingJoinPoint joinpoint) throws Throwable {
        return updateEntityBaseObject(joinpoint);
    }

    private Object updateEntityBaseObject(ProceedingJoinPoint joinpoint) throws Throwable {
        System.out.println("AOP Execution started -> edu.miu.cs.appointmentsystem.ao.EntityBaseAdvice");
        User user = null;
        int index = 0;
        for (Object item : joinpoint.getArgs()) {
            if (item instanceof BaseEntity && !(item instanceof User)) {
                if (user == null) {
                    user = userService.getCurrentUser();
                }
                BaseEntity dto = (BaseEntity) item;
                if (dto.getId() == null || dto.getId() <= 0) {
                    dto.setCreatedBy(user);
                    dto.setCreatedDate(LocalDateTime.now());
                    dto.setTimestamp(LocalDateTime.now());
                    System.out.println(
                            "AOP Execution Created by And Creation Date -> edu.miu.cs.appointmentsystem.ao.EntityBaseAdvice");
                } else {
                    dto.setUpdatedBy(user);
                    dto.setUpdatedDate(LocalDateTime.now());
                    dto.setTimestamp(LocalDateTime.now());
                    System.out.println(
                            "AOP Execution Updated by And Updated Date -> edu.miu.cs.appointmentsystem.ao.EntityBaseAdvice");
                }
                joinpoint.getArgs()[index] = dto;
            }
            index++;
        }
        Object result = joinpoint.proceed();
        System.out.println("AOP Execution Ended -> edu.miu.cs.appointmentsystem.ao.EntityBaseAdvice");
        return result;
    }

}
