package com.avdei.spring1app.mapper;

import com.avdei.spring1app.dto.*;
import com.avdei.spring1app.model.Person;
import org.mapstruct.*;

@Mapper(
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class PersonMapper {

    public abstract void update(PersonUpdateDTO updateDTO, @MappingTarget Person person);
    public abstract Person map(PersonCreateDTO createDTO);
    public abstract PersonDTO map(Person person);
}
