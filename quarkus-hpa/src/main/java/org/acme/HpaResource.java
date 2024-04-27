package org.acme;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;

import java.util.Arrays;
import java.util.HashMap;

@Path("/")
public class HpaResource {
    @Inject
    Logger log;

    @GET
    @Path("fill/{index}")
    @Produces(MediaType.TEXT_PLAIN)
    public String fill(@PathParam("index") String index) {
        HashMap<String, String> mem = new HashMap<>();
        char[] chars = new char[2 * 1024 * 1024];
        Arrays.fill(chars, 'f');
        mem.put(Math.random() + "", new String(chars));
        log.infof("Added %s MB", index);
        return "Added " + index + "MB \n";
    }

    @GET
    @Path("prime/{number}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response isPrime(@PathParam("number") String number) {
        var num = Integer.parseInt(number);
        return Response.ok(fib(num)).build();

    }

    public static long fib(int num) {
        if (num <= 1) {
            return num;
        } else {
            return fib(num - 1) + fib(num -2);
        }
    }

}
