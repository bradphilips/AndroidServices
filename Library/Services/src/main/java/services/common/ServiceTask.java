package services.common;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.springframework.http.ContentCodingType;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;

import services.common.models.ServiceConfiguration;

public class ServiceTask<T, TBody> extends AsyncTask<Void, Void, ResponseWrapper<T>> {
    private static final String TAG = ServiceTask.class.getName();
    private static final String USER_PREFERENCES = "user_preferences";
    private static final String COOKIES_KEY = "user_cookie";
    private static RestTemplate mRestClient;
    private ServiceCallback<T> mCallback;
    private String mPath;
    private HttpMethod mMethod;
    private TBody mBody;
    private Class<T> mResponseType;
    private Context mContext;

    private ServiceConfiguration mServiceConfiguration;

    public ServiceTask(ServiceCallback<T> callback, HttpMethod method, String path, Class<T> responseType, Context context) {
        mCallback = callback;
        mMethod = method;
        mPath = path;
        mResponseType = responseType;
        mContext = context;

        if (mRestClient == null) {
            mRestClient = new RestTemplate();
            mRestClient.setRequestFactory(new HttpRequestFactory(mContext));
            mRestClient.getMessageConverters().add(new MappingJacksonHttpMessageConverter());
        }
    }

    @Override
    protected ResponseWrapper<T> doInBackground(Void... params) {
        ServiceConfiguration config = ServiceConfiguration.sharedInstance();
        String url = String.format("%s%s", config.getBaseUrl(), mPath);
        HttpEntity<TBody> requestBody = createRequest();

        Log.d(TAG, String.format("Sending request to: %s", url));
        Log.d(TAG, String.format("Entity: %s", requestBody));
        T serviceResponse = null;

        ResponseWrapper<T> wrapper;
        try {
            ResponseEntity<T> response = mRestClient.exchange(url, mMethod, requestBody, mResponseType);
            serviceResponse = response.getBody();

            wrapper = new ResponseWrapper<T>(serviceResponse, null);
        } catch (Exception e) {
            Error error = new Error("We're sorry.  Something went wrong with your request.  Please try again later.");
            wrapper = new ResponseWrapper<T>(null, error);
        }

        return wrapper;
    }

    private HttpEntity<TBody> createRequest() {
        ArrayList<MediaType> accept = new ArrayList<MediaType>();
        accept.add(MediaType.APPLICATION_JSON);

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(accept);
        headers.setAcceptEncoding(ContentCodingType.GZIP);
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new HttpEntity<TBody>(getBody(), headers);
    }

    @Override
    protected void onPostExecute(ResponseWrapper<T> result) {
        mCallback.didLoadResponse(result.getResponse(), result.getError());
    }

    public TBody getBody() {
        return mBody;
    }

    public void setBody(TBody mBody) {
        this.mBody = mBody;
    }

    public ServiceConfiguration getServiceConfiguration() {
        return mServiceConfiguration == null ? ServiceConfiguration.sharedInstance() : mServiceConfiguration;
    }

    public void setServiceConfiguration(ServiceConfiguration serviceConfiguration) {
        mServiceConfiguration = serviceConfiguration;
    }
}
