package services.common;

import android.content.Context;

import com.google.inject.Inject;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

public class HttpRequestFactory extends HttpComponentsClientHttpRequestFactory {

    private PersistentCookieStore mCookieStore;

    @Inject
    public HttpRequestFactory(Context context) {
        mCookieStore = new PersistentCookieStore(context.getApplicationContext());
    }

    @Override
    public HttpClient getHttpClient() {
        DefaultHttpClient client = (DefaultHttpClient) super.getHttpClient();
        client.setCookieStore(mCookieStore);
        return client;
    }
}