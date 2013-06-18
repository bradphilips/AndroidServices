package services.common.models;

import java.io.Serializable;
import java.security.InvalidParameterException;

public class ServiceConfiguration implements Serializable {
    public static final String ENVIRONMENT_TEST = "TEST";
    public static final String ENVIRONMENT_PRODUCTION = "PRODUCTION";
    /**
     *
     */
    private static final long serialVersionUID = -2378164386450288593L;
    private static final String mTestUrl = "http://localhost:3000";
    private static final String mProductionUrl = "http://localhost:3000";
    private static ServiceConfiguration mSharedInstance;
    private String mEnvironment;

    private ServiceConfiguration() {
        setServiceEnvironment(ENVIRONMENT_TEST); // Default to test...
        mSharedInstance = this;
    }

    public static ServiceConfiguration sharedInstance() {
        if (mSharedInstance == null) {
            mSharedInstance = new ServiceConfiguration();
        }
        return mSharedInstance;
    }

    public String getBaseUrl() {
        if (getServiceEnvironment().equals(ENVIRONMENT_TEST)) return mTestUrl;
        if (getServiceEnvironment().equals(ENVIRONMENT_PRODUCTION)) return mProductionUrl;

        return null;
    }

    public String getServiceEnvironment() {
        return mEnvironment;
    }

    public void setServiceEnvironment(String environment) {
        if (environment.equals(ENVIRONMENT_TEST) || environment.equals(ENVIRONMENT_PRODUCTION)) {
            mEnvironment = environment;
            return;
        }

        throw new InvalidParameterException(String.format("%s is not a valid environment.", environment));
    }
}
