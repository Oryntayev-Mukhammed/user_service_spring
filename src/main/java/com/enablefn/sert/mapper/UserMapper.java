package com.enablefn.sert.mapper;

import com.enablefn.sert.dto.UserDTO;
import com.enablefn.sert.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO userToUserDTO(User user);
    User userDTOToUser(UserDTO userDTO);
}
