package com.aston.user_service.mapper;

import com.aston.user_service.model.User;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserDTOToUserConverter implements Converter<UserDTO, User> {
    @Override
    public User convert(UserDTO source) {
        User user = new User();
        if (source.id() > 0) user.setId(source.id());
        user.setName(source.name());
        user.setEmail(source.email());
        user.setAge(source.age());

        return user;
    }
}
