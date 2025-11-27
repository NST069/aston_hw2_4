package com.aston.user_service.mapper;

import com.aston.user_service.model.User;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserToUserDTOConverter implements Converter<User, UserDTO> {
    @Override
    public UserDTO convert(User source) {
        UserDTO userDTO = new UserDTO(source.getId(),
                source.getName(),
                source.getEmail(),
                source.getAge());
        return userDTO;
    }
}
