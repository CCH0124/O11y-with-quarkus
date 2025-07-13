package org.cch;

import java.util.Map;
import java.util.Objects;

import org.eclipse.microprofile.jwt.Claim;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.logging.Logger;

import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.InternalServerErrorException;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

@RequestScoped
@Path("/")
public class TokenInfoResource {

    @Inject
    @Claim(value = "id")
    String userId;

    @Inject
    JsonWebToken jwt;

    @Inject
    Logger log;
 
    @GET
    @Path("/userinfo")
    @RolesAllowed({ "USER", "ADMIN" })
    @Produces(MediaType.APPLICATION_JSON)
    public Response helloRolesAllowed(@Context SecurityContext ctx) {
        String name;
        if (Objects.isNull(ctx.getUserPrincipal())) {
            name = "anonymous";
        } else if (!ctx.getUserPrincipal().getName().equals(jwt.getName())) {
            log.error("Principal and JsonWebToken names do not match");
            throw new InternalServerErrorException("Principal and JsonWebToken names do not match");
        } else {
            name = ctx.getUserPrincipal().getName();
        }
        return Response.ok(Map.of("id", userId, "name", name)).build();
    }
}
