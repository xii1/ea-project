package edu.miu.cs.appointmentsystem.controllers.responses;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AppResponse<T> {
    private boolean isSuccess;
    private T Data;
    private List<String> errors;

    public AppResponse(boolean isSuccess, T data, List<String> errors) {

        this.isSuccess = isSuccess;
        this.Data = data;
        this.errors = errors;
    }

    public AppResponse(T data) {
        this.isSuccess = true;
        this.Data = data;
        this.errors = null;
    }
}
