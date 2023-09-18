package eu.zderadicka.mbs3.rest.error;

import eu.zderadicka.mbs3.rest.error.ErrorResponse.Code;
import eu.zderadicka.mbs3.rest.error.Exceptions.InvalidEntityId;
import io.quarkus.logging.Log;
import io.quarkus.rest.data.panache.RestDataPanacheException;
import jakarta.persistence.OptimisticLockException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

public class ErrorMappers {
    @Provider
    public static class OptimisticLockErrorMapper implements ExceptionMapper<OptimisticLockException> {

        @Override
        public Response toResponse(OptimisticLockException exception) {
            return Response.status(Response.Status.CONFLICT).entity(new ErrorResponse(Code.CONCURRENT_MODIFICATION))
                    .build();

        }

    }

    @Provider
    public static class InvalidEntityIdErrorMapper implements ExceptionMapper<InvalidEntityId> {

        @Override
        public Response toResponse(InvalidEntityId exception) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ErrorResponse(Code.INVALID_ENTITY_ID))
                    .build();
        }

    }

    @Provider
    public static class PanacheDataResourceErrorMapper implements ExceptionMapper<RestDataPanacheException> {

        @Override
        public Response toResponse(RestDataPanacheException exception) {
            var cause = exception.getCause();

            if (cause instanceof OptimisticLockException) {
                return buildResponse(Response.Status.CONFLICT, Code.CONCURRENT_MODIFICATION);
            } else {
                Log.error("Panache Data Resource Error", exception);
                return buildResponse(Response.Status.INTERNAL_SERVER_ERROR, Code.GENERAL_ERROR);

            }

        }

    }

    private static Response buildResponse(Response.Status status, Code code) {
        return Response.status(status).entity(new ErrorResponse(code)).build();
    }
}
