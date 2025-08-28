package org.cch.service;

import org.cch.dto.AuthenticationDTO;
import org.cch.dto.RegisterDTO;
import org.cch.exception.AuthenticationRoleException;
import org.cch.exception.AuthenticationUsernameException;

public interface UserService {
    String authenticate(final AuthenticationDTO authRequest) throws Exception;
    void register(final RegisterDTO registerRequest) throws AuthenticationUsernameException, AuthenticationRoleException;
}
