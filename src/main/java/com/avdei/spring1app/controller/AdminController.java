package com.avdei.spring1app.controller;

import com.avdei.spring1app.dto.PersonDTO;
import com.avdei.spring1app.dto.TaskDTO;
import com.avdei.spring1app.mapper.PersonMapper;
import com.avdei.spring1app.model.Person;
import com.avdei.spring1app.model.Task;
import com.avdei.spring1app.service.PeopleService;
import com.avdei.spring1app.util.CurrentUserUtil;
import com.avdei.spring1app.util.DurationConverter;
import com.avdei.spring1app.validator.PersonValidator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@Controller
@Slf4j
@Getter
@Setter
@RequestMapping("/admin")
public class AdminController {

    private final PeopleService peopleService;
    private final PersonMapper personMapper;
    private final PersonValidator personValidator;

    public AdminController(PeopleService peopleService, PersonMapper personMapper, PersonValidator personValidator) {
        this.peopleService = peopleService;
        this.personMapper = personMapper;
        this.personValidator = personValidator;
    }
    @GetMapping
    public String getUsers(@RequestParam(defaultValue = "0") int page,
                           @RequestParam(defaultValue = "10") Integer size,
                           @RequestParam(defaultValue = "id") String sortBy,
                           @RequestParam(defaultValue = "asc") String sortDirection,
                           Model model) {

        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Person> persons = peopleService.getAllUsers(pageable);
        Integer currentUserId = CurrentUserUtil.getCurrentUser().getId();

        List<PersonDTO> personDTOList = persons.getContent()
                .stream()
                .map(personMapper::map)
                .filter(person -> !person.getId().equals(currentUserId))
                .toList();

        Page<PersonDTO> personsDTO = new PageImpl<>(personDTOList, pageable, persons.getTotalElements() - 1);

        setParameters(page, size, sortBy, sortDirection, model, personsDTO);

        log.info("Users returned successfully");
        return "admin/users";

    }

    private static void setParameters(int page, Integer size, String sortBy, String sortDirection, Model model, Page<PersonDTO> personsDTO) {
        model.addAttribute("personsDTO", personsDTO);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDirection", sortDirection);
        model.addAttribute("size", size);
        model.addAttribute("currentPage", page);
    }

}
