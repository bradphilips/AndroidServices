package services.common;

import android.content.Context;
import android.provider.Settings.Secure;

import com.google.inject.Inject;

public class BaseService {
    protected Context mContext;

    @Inject
    public BaseService(Context context) {
        mContext = context;
    }

    protected String getDeviceId() {
        return Secure.getString(mContext.getContentResolver(), Secure.ANDROID_ID);
    }

    protected String createResourcePath(String action, String... params) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < params.length; i += 2) {
            builder.append(String.format("%s=%s&", params[i], params[i + 1]));
        }

        String queryString = builder.substring(0, builder.length() - 1);
        return String.format("%s?%s", action, queryString);
    }
}
