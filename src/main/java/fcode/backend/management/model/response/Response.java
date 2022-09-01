package fcode.backend.management.model.response;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Response<E> {
    private int code;
    private String message;
    private E data;

    public Response() {
    }

    public Response(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public Response(int code, String message, E data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
}
