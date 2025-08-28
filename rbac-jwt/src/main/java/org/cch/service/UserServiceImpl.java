package org.cch.service;

import java.util.HashSet;
import java.util.Objects;
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
import org.mindrot.jbcrypt.BCrypt;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class UserServiceImpl implements UserService{

    @Inject
    Logger log;

    @Inject
    UserRepository userRepository;

    @Inject
    RoleRepository roleRepository;

    @Inject
    TokenServiceImpl tokenService;

    public String authenticate(final AuthenticationDTO authRequest)
            throws Exception {
        var user = userRepository.findByUsernameOptional(authRequest.username())
                .orElseThrow(AuthenticationUsernameException::new);

        if (Objects.isNull(user) || !BCrypt.checkpw(authRequest.password(), user.getPassword())) {
            throw new AuthenticationPasswordException();
        }
        
        var roles = user.getRoles().stream().map(x -> x.getName()).collect(Collectors.toSet());
        var tokenDto = new TokenDTO(user.getId().toString(), user.getUsername(), user.getEmail(), user.getBirthDate(), roles);
        return tokenService.generateToken(tokenDto);
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
        user.setPassword(BCrypt.hashpw(registerRequest.password(), BCrypt.gensalt()));
        user.setEmail(registerRequest.email());
        user.setBirthDate(registerRequest.birthDate());
        user.setRoles(roles);
        var isExist = userRepository.findByUsernameOptional(registerRequest.username());
        if (isExist.isPresent()) {
            throw new AuthenticationUsernameException();
        }
        userRepository.persist(user);
        log.infof("Insert User %s", user.getUsername());
    }
}
