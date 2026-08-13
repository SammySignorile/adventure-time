package org.example.adventuretime.mapper;

import org.example.adventuretime.bean.UserBean;
import org.example.adventuretime.model.User;

public final class UserMapper {

    private UserMapper() {
    }

    public static UserBean toBean(User user) {
        return new UserBean(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getPoints(),
                user.getEmail(),
                user.getRole()
        );
    }
}
