package org.cch;

import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.cch.dto.AuthenticationDTO;
import org.cch.dto.RegisterDTO;
import org.cch.request.AuthenticationRequest;
import org.cch.request.RegisterRequest;
import org.cch.response.AuthenticationResponse;
import org.cch.service.UserService;
import org.eclipse.microprofile.config.inject.ConfigProperties;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;

import java.net.URI;



@Path("/api")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {
    @Inject
    UserService userService;

    @Inject
    JsonWebToken jwt;

    @ConfigProperty(name = "smallrye.jwt.new-token.lifespan")
    Long tokenExpireTime;
    
    @PermitAll
    @POST
    @Path("/login")
    public Response login(@RequestBody final AuthenticationRequest authRequest) {
        var authDto = new AuthenticationDTO(authRequest);
        String token;
        try {
            token = userService.authenticate(authDto);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid credentials").build();
        }
        return Response.ok(new AuthenticationResponse(token, String.valueOf(tokenExpireTime))).build();
    }

    @PermitAll
    @POST
    @Path("/register")
    public Response register(@RequestBody RegisterRequest registerRequest, @Context UriInfo uriInfo) throws Exception {
        var registerDto = new RegisterDTO(registerRequest);
        userService.register(registerDto);
        URI uri = uriInfo.getAbsolutePathBuilder().build();
        return Response.created(uri).build();
    }
}
