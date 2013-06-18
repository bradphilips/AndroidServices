Android Services
=================

Simple common source library for android services incorporating several 'best practices' I have used in the past to create modular and re-usable mobile apps for such as servicing layers, model mapping, and view-logic separation.

#### How it Works: A simple login request

Given that the login takes a username and password and returns a user object I've split this example into 2 models and a simple service call.  The 2 models are as follows:

`LoginUser` - Username and password combination to call to the service layer.  Binds directly to the JSON payload depending on what media type you specify on the `ServiceTask`.

    public class LoginUser implements Serializable {
        @JsonProperty("username")
        private String mUsername;
        @JsonProperty("password")
        private String mPassword;

        public String getUsername() {
            return mUsername;
        }

        public void setUsername(String username) {
            mUsername = username;
        }

        public String getPassword() {
            return mPassword;
        }

        public void setPassword(String password) {
            mPassword = password;
        }
    }

`User` - The returned user model.  Binds directly from JSON response depending on what media type you specify on the `ServiceTask`.

    public class User implements Serializable {
        @JsonProperty("id")
        private int mId;
        @JsonProperty("username")
        private String mUserName;
        @JsonProperty("email")
        private String mEmail;

        public int getId() {
            return mId;
        }

        public String getEmail() {
            return mEmail;
        }

        public String getUserName() {
            return mUserName;
        }
    }
    
Then to call the service it's as simple as creating a service task and handling the response:
    
    public void loginUser(LoginUser user) {
        ServiceTask<User, LoginUser> loginUserTask = new ServiceTask<User, LoginUser>(mCallback,
                HttpMethod.POST,
                "/session",
                getContext());
        
        loginUserTask.execute();
    }
    
	private ServiceCallback<User> mCallback = new ServiceCallback<User>() {
        @Override
        public void didLoadResponse(User response, Error error) {
            if (error != null) {
                // Log and report...
            }
            // Do something with the User (bind to views, etc...)
        }
    };

You can also optionally hold on to the `AsyncTask` that is returned by the `execute()` method to manage cancel or anything an `AsyncTask` supports.

#### Configuration

The `ServiceTask` class creates it's url from combining the base url from the shared `ServiceConfiguration` by default.  The `ServiceConfiguration` object allows configuration per environment of your root service url.  You can also opt for a custom configuration on a per task basis by calling `setServiceConfiguration(ServiceConfiguration configuration)` based on your custom environments.

#### Dependencies

The library has the following dependencies:

* [Roboguice](https://code.google.com/p/roboguice/) - org.roboguice:roboguice:2.0
* [Jackson Parser](http://jackson.codehaus.org/) - org.codehaus.jackson:jackson-core-asl:1.9.12 and org.codehaus.jackson:jackson-mapper-asl:1.9.12
* [Spring REST Template for Android](http://www.springsource.org/spring-android) - org.springframework.android:spring-android-rest-template:1.0.1.RELEASE

Server
====================================

This application also has an accompanying localhost server that is written in Node to allow sandbox development and mock out service calls.  The only functions implemented now are login and logout.  The resources are as follows:

		POST 	/session - Login user { username:'brad', password: 'secure' }
		DELETE 	/session - Logout user

* [Node](http://nodejs.org/) - Node is an evented server side technology built in javascript (using Google's V8 engine).  This is a very fast lightweight server-side technology and is rapidly becoming very popular.

**Setup**

1.  Get and install [nvm (Node Version Manager)](https://github.com/creationix/nvm) or [nvm (Node Version Manager) for Windows](https://github.com/hakobera/nvmw.git).  Windows requires python.
2.  Run `nvm install v0.10.11`. Should work in pretty much any node version (disclaimer: not guaranteed).
3.  In the base of the `Server` directory run `npm install`
4.  Run `node app.js`
5.  Can use [Advanced Rest Client](https://chrome.google.com/webstore/detail/advanced-rest-client/hgmloofddffdnphfgcellkdfbfbjeloo?hl=en-US) to test.
