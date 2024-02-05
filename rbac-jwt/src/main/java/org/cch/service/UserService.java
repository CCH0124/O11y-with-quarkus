package org.cch.service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.cch.dto.AuthenticationDTO;
import org.cch.dto.RegisterDTO;
import org.cch.dto.TokenDTO;
import org.cch.entity.ERole;
import org.cch.entity.Role;
import org.cch.entity.User;
import org.cch.exception.AuthenticationPasswordException;
import org.cch.exception.AuthenticationRoleException;
import org.cch.exception.AuthenticationUsernameException;
import org.cch.repository.RoleRepository;
import org.cch.repository.UserRepository;
import org.jboss.logging.Logger;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class UserService {

    @Inject
    Logger log;

    @Inject
    UserRepository userRepository;

    @Inject
    RoleRepository roleRepository;

    @Inject
    TokenService tokenService;

    public String authenticate(final AuthenticationDTO authRequest)
            throws Exception {
        var user = userRepository.findByUsernameOptional(authRequest.username())
                .orElseThrow(AuthenticationUsernameException::new);
        // if (user.password.equals(cryptoService.encrypt(authRequest.getPassword()))){
        //     var roles = user.roles.stream().map(x -> x.name).collect(Collectors.toSet());
        //     return Token.generateToken(user.username, user.account, user.email, user.id.toString(), roles);
        // }
        
        if (user.getPassword().equals(authRequest.password())){
            var roles = user.getRoles().stream().map(x -> x.getName()).collect(Collectors.toSet());
            var tokenDto = new TokenDTO(user.getId(), user.getUsername(), user.getEmail(), user.getBirthDate(), roles);
            return tokenService.generateToken(tokenDto);
        }
        throw new AuthenticationPasswordException();
    }

    @Transactional
    public void register(final RegisterDTO registerRequest) throws AuthenticationUsernameException, AuthenticationRoleException {
        log.debugf("roles: %s", registerRequest.roles().toString());
        Set<Role> roles = new HashSet<>();
        for (ERole role : registerRequest.roles()) {
            log.infof("Converting role name from string to ERole: %s", role.name().toString());
            var r = roleRepository.findByNameOptional(role).orElseThrow(AuthenticationRoleException::new);
            roles.add(r);
        }
        var user = new User();
        user.setUsername(registerRequest.username());
        user.setPassword(registerRequest.password());
        user.setEmail(registerRequest.email());
        user.setBirthDate(registerRequest.birthDate());
        user.setRoles(roles);
        userRepository.findByUsernameOptional(registerRequest.username()).orElseThrow(AuthenticationUsernameException::new);
        // user.password = cryptoService.encrypt(user.password);
        userRepository.persist(user);
        log.infof("Insert User %s", user.getUsername());
    }
}
