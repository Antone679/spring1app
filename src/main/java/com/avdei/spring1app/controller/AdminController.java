package com.avdei.spring1app.controller;

import com.avdei.spring1app.dto.PersonDTO;
import com.avdei.spring1app.dto.PersonUpdateDTO;
import com.avdei.spring1app.dto.TaskDTO;
import com.avdei.spring1app.dto.TaskUpdateDTO;
import com.avdei.spring1app.mapper.PersonMapper;
import com.avdei.spring1app.model.Person;
import com.avdei.spring1app.model.Role;
import com.avdei.spring1app.model.Status;
import com.avdei.spring1app.model.Task;
import com.avdei.spring1app.service.PeopleService;
import com.avdei.spring1app.util.CurrentUserUtil;
import com.avdei.spring1app.util.DurationConverter;
import com.avdei.spring1app.validator.PersonCreateValidator;
import com.avdei.spring1app.validator.PersonUpdateValidator;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.http.converter.json.GsonBuilderUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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
    private final PersonUpdateValidator personUpdateValidator;

    public AdminController(PeopleService peopleService, PersonMapper personMapper, PersonUpdateValidator personUpdateValidator) {
        this.peopleService = peopleService;
        this.personMapper = personMapper;
        this.personUpdateValidator = personUpdateValidator;
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
    @DeleteMapping("{id}")
    public String deleteUser(@PathVariable int id){
        peopleService.deletePerson(id);
        log.info("User has been removed successfully");
        return "redirect:/admin";
    }

    @GetMapping("/edit/{id}")
    public String editUser(@PathVariable int id, Model model) {
        model.addAttribute("personDTO", peopleService.getPersonDTOById(id).get());
        log.info("Template with the User has been sent successfully");
        return "admin/edit";
    }


    @PatchMapping("/{id}")
    public String updateUser(@PathVariable int id, @ModelAttribute("personDTO") PersonUpdateDTO personUpdateDTO,
                             BindingResult bindingResult){
        personUpdateValidator.validate(personUpdateDTO, bindingResult);

        if (bindingResult.hasErrors()) {
            return "admin/edit";
        }
        peopleService.updatePerson(id, personUpdateDTO);
        log.info("User has been updated successfully");

        return "redirect:/admin";
    }

    @ModelAttribute
    private void addCommonAttributes(Model model) {
        model.addAttribute("roles", Role.values());
    }

    private static void setParameters(int page, Integer size, String sortBy, String sortDirection, Model model, Page<PersonDTO> personsDTO) {
        model.addAttribute("personsDTO", personsDTO);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDirection", sortDirection);
        model.addAttribute("size", size);
        model.addAttribute("currentPage", page);
    }

}
