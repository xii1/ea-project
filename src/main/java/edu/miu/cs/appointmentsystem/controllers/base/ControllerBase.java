package edu.miu.cs.appointmentsystem.controllers.base;

import java.util.List;

import edu.miu.cs.appointmentsystem.controllers.responses.AppResponse;

public abstract class ControllerBase {

    public <T> AppResponse<T> CreateResponse(T t) {
        return new AppResponse<T>(t);
    }

    public <T> AppResponse<T> CreateResponse(boolean isSuccess, T data, List<String> errors) {
        return new AppResponse<T>(isSuccess, data, errors);
    }
}
