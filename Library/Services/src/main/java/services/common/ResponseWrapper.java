package services.common;

public class ResponseWrapper<T> {
    private T mResponse;
    private Error mError;

    public ResponseWrapper(T response, Error error) {
        mResponse = response;
        mError = error;
    }

    public T getResponse() {
        return mResponse;
    }

    public Error getError() {
        return mError;
    }
}
