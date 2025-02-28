package com.avdei.spring1app.mapper;

import com.avdei.spring1app.dto.*;
import com.avdei.spring1app.model.Person;
import org.mapstruct.*;

@Mapper(
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public abstract class PersonMapper {

    public abstract void update(PersonUpdateDTO updateDTO, @MappingTarget Person person);
    public abstract Person map(PersonCreateDTO createDTO);
    public abstract PersonDTO map(Person person);
}
