package org.cch;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

import org.cch.dto.AuthenticationDTO;
import org.cch.dto.RegisterDTO;
import org.cch.entity.ERole;
import org.cch.request.AuthenticationRequest;
import org.cch.request.RegisterRequest;
import org.cch.response.AuthenticationResponse;
import org.cch.service.UserService;
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

    @PermitAll
    @POST
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(@RequestBody AuthenticationRequest authRequest) throws Exception {
        var authDto = new AuthenticationDTO(authRequest);
        final String token = userService.authenticate(authDto);
        return Response.ok(new AuthenticationResponse(token)).build();
    }

    @PermitAll
    @POST
    @Path("/register")
    @Produces(MediaType.APPLICATION_JSON)
    public Response register(@RequestBody RegisterRequest registerRequest, @Context UriInfo uriInfo) throws Exception {
        var registerDto = new RegisterDTO(registerRequest);
        userService.register(registerDto);
        URI uri = uriInfo.getAbsolutePathBuilder().build();
        return Response.created(uri).build();
    }
}
