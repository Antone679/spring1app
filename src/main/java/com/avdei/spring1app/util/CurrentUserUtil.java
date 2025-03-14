package com.avdei.spring1app.util;

import com.avdei.spring1app.model.Person;
import com.avdei.spring1app.security.PersonDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class CurrentUserUtil {

    public static Person getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()){
            PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
            return personDetails.getPerson();
        }
        return null;
    }

}
