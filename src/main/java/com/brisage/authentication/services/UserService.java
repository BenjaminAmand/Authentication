package com.brisage.authentication.services;

import com.brisage.authentication.dtos.RegisterDTO;
import com.brisage.authentication.entity.User;

public interface UserService {

    User findByEmail(String email);

    String register(RegisterDTO registerDto);
}