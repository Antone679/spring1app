package com.avdei.spring1app.service;

import com.avdei.spring1app.dto.PersonCreateDTO;
import com.avdei.spring1app.dto.PersonDTO;
import com.avdei.spring1app.dto.PersonUpdateDTO;
import com.avdei.spring1app.mapper.PersonMapper;
import com.avdei.spring1app.model.Comment;
import com.avdei.spring1app.model.Person;
import com.avdei.spring1app.model.Role;
import com.avdei.spring1app.repository.CommentRepository;
import com.avdei.spring1app.repository.PeopleRepository;
import com.avdei.spring1app.util.CurrentUserUtil;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Getter
@Setter
@Transactional(readOnly = true)
public class PeopleService {

    private final PeopleRepository peopleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final PersonMapper personMapper;
    private final CommentRepository commentRepository;
    final MailSenderService mailSenderService;
    final MessageSource messageSource;

    @Autowired
    public PeopleService(PeopleRepository peopleRepository, BCryptPasswordEncoder bCryptPasswordEncoder,
                         PersonMapper personMapper, CommentRepository commentRepository, MailSenderService mailSenderService, MessageSource messageSource) {
        this.peopleRepository = peopleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.personMapper = personMapper;
        this.commentRepository = commentRepository;
        this.mailSenderService = mailSenderService;
        this.messageSource = messageSource;
    }

    public Optional<Person> getPersonByUserName(String username) {
        return peopleRepository.findByUserName(username);
    }

    public Optional<Person> getPersonByEmail(String email) {
        return peopleRepository.findByEmail(email);
    }

    public Optional<Person> getPersonById(int id) {
        return peopleRepository.findById(id);
    }

    public Optional<PersonUpdateDTO> getPersonDTOById(int id) {
        return Optional.of(personMapper.mapToUpdateDTO(peopleRepository.findById(id).orElseThrow()));
    }

    public Page<PersonDTO> getAllUsers(int page, int size, String sortBy,
                                    String sortDirection) {
        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Person> persons = peopleRepository.findAll(pageable);
        Integer currentUserId = CurrentUserUtil.getCurrentUser().getId();

        List<PersonDTO> personDTOList = persons.getContent()
                .stream()
                .map(personMapper::map)
                .filter(person -> !person.getId().equals(currentUserId))
                .toList();

        long totalElements = persons.getTotalElements();

        return new PageImpl<>(personDTOList, pageable, totalElements);

    }

    @Transactional
    public void savePerson(PersonCreateDTO personCreateDTO) {
        Person person = personMapper.map(personCreateDTO);
        String normalizedEmail = person.getEmail().trim().toLowerCase();
        person.setEmail(normalizedEmail);
        person.setPassword(bCryptPasswordEncoder.encode(person.getPassword()));
        person.setRole(Role.USER);
        peopleRepository.save(person);

        String message = messageSource.getMessage("reg.mail.body", null,
                LocaleContextHolder.getLocale());

        String body = String.format(message, personCreateDTO.getUserName(),
                personCreateDTO.getPassword());

        System.out.println(messageSource.getMessage("reg.mail.subject",
                null, LocaleContextHolder.getLocale()));
        System.out.println(body);

        mailSenderService.sendEmail(personCreateDTO.getEmail(),
                messageSource.getMessage("reg.mail.subject",
                        null, LocaleContextHolder.getLocale()),
                body);
    }
    @Transactional
    public void updatePerson(int id, PersonUpdateDTO personUpdateDTO) {

        Person person = peopleRepository.findById(id).get();
        personMapper.update(personUpdateDTO, person);
        String normalizedEmail = person.getEmail().trim().toLowerCase();
        person.setEmail(normalizedEmail);
        person.setPassword(bCryptPasswordEncoder.encode(person.getPassword()));
        peopleRepository.save(person);
    }

    @Transactional
    public void deletePerson(int id) {
        List<Comment> comments = commentRepository.findByAuthorId(id);
        for (Comment comment : comments) {
            comment.setAuthor(null);
        }
        commentRepository.saveAll(comments);
        peopleRepository.deleteById(id);

    }

}
