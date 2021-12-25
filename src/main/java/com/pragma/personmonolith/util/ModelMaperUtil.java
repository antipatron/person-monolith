package com.pragma.personmonolith.util;

import com.pragma.personmonolith.dto.PersonDto;
import com.pragma.personmonolith.dto.PersonImageDto;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.Mapping;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public abstract class ModelMaperUtil {
    private static ModelMapper modelMapper = new ModelMapper();

    static {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }

    public static ModelMapper getMapperPersonaImageDto2PersonDto(){
        modelMapper.typeMap(PersonImageDto.class, PersonDto.class).addMappings(mapper->{
            mapper.map(PersonImageDto::getPersonId,PersonDto::setId);
            mapper.map(PersonImageDto::getIdentificationTypeId,PersonDto::setIdentificationTypeId);
        });

        return modelMapper;
    }

    public static void getPropertyOfMapperPersonaImageDto2PersonDto(){
        TypeMap<PersonImageDto, PersonDto> tm = modelMapper.getTypeMap(PersonImageDto.class, PersonDto.class);
        List<Mapping> list = tm.getMappings();
        for (Mapping m : list)
        {
            System.out.println("Prop: "+m);
        }
    }


}
