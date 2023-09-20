package eu.zderadicka.mbs3;

import org.eclipse.microprofile.jwt.JsonWebToken;

import io.smallrye.jwt.build.Jwt;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.SecurityContext;

@Path("/api/v1/authenticator")

public class Authenticator {

    @RequestScoped
    @Inject
    JsonWebToken jwt;

    @GET
    @Path("/test")
    @Produces(MediaType.TEXT_PLAIN)
    @PermitAll
    public String test(@Context SecurityContext ctx) {
        return extractSecurityInfo(ctx);
    }

    @GET
    @Path("/test-secured")
    @RolesAllowed({ "user", "admin" })
    @Produces(MediaType.TEXT_PLAIN)
    public String testSecured(@Context SecurityContext ctx) {
        return "SECURED\n" + extractSecurityInfo(ctx);
    }

    private String extractSecurityInfo(SecurityContext ctx) {

        String name = ctx.getUserPrincipal() != null ? ctx.getUserPrincipal().getName() : "anonymous";

        return String.format("User Name: %s\n" +
                "JWT name: %s\n" +
                "is HTTPS: %s\n" +
                "authScheme: %s\n" +
                "JWT claims: %s\n" +
                "Whole JWT: %s\n",
                name, jwt.getName(), ctx.isSecure(), ctx.getAuthenticationScheme(), jwt.getClaimNames(), jwt);

    }

    @POST
    @Path("/generate")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public String generate(@Valid TokenInfo tokenInfo) {

        var token = Jwt.issuer(TokenInfo.issuer)
                .upn(tokenInfo.upn)
                .groups(tokenInfo.groups);
        if (tokenInfo.validSeconds != null) {
            token.expiresIn(tokenInfo.validSeconds);
        }

        return token.sign();

    }

}