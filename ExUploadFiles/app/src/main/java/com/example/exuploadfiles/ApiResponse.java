package com.example.exuploadfiles;

import java.util.List;

public class ApiResponse {
    private String success;
    private String message;
    private List<Data> result;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Data> getResult() {
        return result;
    }

    public void setResult(List<Data> result) {
        this.result = result;
    }

    public ApiResponse(String success, String message, List<Data> result) {
        this.success = success;
        this.message = message;
        this.result = result;
    }
}
