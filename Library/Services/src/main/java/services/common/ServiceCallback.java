package services.common;

public interface ServiceCallback<T> {
    public void didLoadResponse(T response, Error error);
}
