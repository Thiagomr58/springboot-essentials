package br.com.devdojo.error;

/**
 * Created by Thiago Rodrigues on 28/11/2019
 */
public class CustomErrorType {

    private String errorMessage;

    public CustomErrorType(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
