package eu.zderadicka.mbs3.rest.error;

public class ErrorResponse {
    public static enum Code {
        CONCURRENT_MODIFICATION("Entity was modified by other user"),
        INVALID_ENTITY_ID("ID is null or different between path param and entity"),
        GENERAL_ERROR("General server error");

        private String msg;

        Code(String msg) {
            this.msg = msg;
        }

    }

    public String code;
    public String message;

    ErrorResponse(Code code) {
        this.code = code.name();
        this.message = code.msg;
    }

}
