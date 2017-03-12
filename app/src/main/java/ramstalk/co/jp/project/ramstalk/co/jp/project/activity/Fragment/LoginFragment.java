//package ramstalk.co.jp.project.ramstalk.co.jp.project.activity.Fragment;
//
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.Toast;
//
//import com.facebook.CallbackManager;
//import com.facebook.FacebookCallback;
//import com.facebook.FacebookException;
//import com.facebook.FacebookSdk;
//import com.facebook.GraphRequest;
//import com.facebook.GraphResponse;
//import com.facebook.appevents.AppEventsLogger;
//import com.facebook.login.LoginResult;
//import com.facebook.login.widget.LoginButton;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.Arrays;
//
//import ramstalk.co.jp.project.R;
//import ramstalk.co.jp.project.ramstalk.co.jp.project.activity.Activity.MapsActivity;
//import ramstalk.co.jp.project.ramstalk.co.jp.project.activity.Cons.CommonConst;
//import ramstalk.co.jp.project.ramstalk.co.jp.project.activity.Http.AsyncLogin;
//import ramstalk.co.jp.project.ramstalk.co.jp.project.activity.Http.AsyncResponseJsonObject;
//
//public class LoginFragment extends Fragment implements AsyncResponseJsonObject {
//    private static final String TAG = CommonConst.ActivityName.TAG_LOGIN_FB_FRAGMENT;
//    private SharedPreferences sharedPreferences;
//    private SharedPreferences.Editor sharedPreferencesEditor;
//    private CallbackManager callbackManager;
//    private Button mLoginButton;
//    private LoginButton mLoginButtonFacebook;
//    private AsyncLogin mAuthTask;
//
//    public LoginFragment() {
//        // Required empty public constructor
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
//        AppEventsLogger.activateApp(getActivity().getApplication());
//        callbackManager = CallbackManager.Factory.create();
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        View view = inflater.inflate(R.layout.fragment_login, container, false);
//        mLoginButtonFacebook = (LoginButton) view.findViewById(R.id.button_login_facebook);
//        mLoginButtonFacebook.setReadPermissions(Arrays.asList(
//                "public_profile", "email", "user_birthday", "user_friends"));
//        mLoginButtonFacebook.setFragment(this);
//        mLoginButtonFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
//            @Override
//            public void onSuccess(final LoginResult loginResult) {
//                GraphRequest request = GraphRequest.newMeRequest(
//                        loginResult.getAccessToken(),
//                        new GraphRequest.GraphJSONObjectCallback() {
//                            String email;
//                            String name;
//                            String userAccessToken;
//                            String gender;
//                            String birthday;
//                            @Override
//                            public void onCompleted(JSONObject object, GraphResponse response) {
//                                try {
//                                    email = object.getString("email");
//                                    userAccessToken = loginResult.getAccessToken().getToken();
//                                    Log.i(TAG, "The user information retrival successful.");
//                                } catch (JSONException e) {
//                                    Log.e(TAG, "The user information retrieval from FB failed");
//                                    Log.e(TAG, "The exception message is : " + e.getCause());
//                                    e.printStackTrace();
//                                }
//                                // TODO: 2017/01/22  Add login logic
//                                Log.i(TAG, "Login logic starts.");
////                                if(!userWithTheSameEmailExists) {
////                                    Log.i(TAG, "A user information does not exist in DB.");
////                                    Log.i(TAG, "Start inserting the user information in DB.");
////                                    // insert a new record in the table
////                                } else {
////                                    Log.i(TAG, "The user exists in DB.");
////                                    Log.i(TAG, "Start comparing the token in DB with the one just acquired");
////                                    mAuthTask = new AsyncLogin(LoginFragment.this, email, null, userAccessToken, false);
////                                    mAuthTask.execute();
////                                }
//                            }
//                        });
//                Bundle parameters = new Bundle();
//                parameters.putString("fields", "id,name,gender,birthday,email");
//                request.setParameters(parameters);
//                request.executeAsync();
//                getActivity().finish();
//            }
//
//            @Override
//            public void onCancel() {
//                Log.i(TAG, "The user information request to FB has been canceled.");
//                Toast.makeText(getActivity().getApplicationContext(), "The login action was cancelled.", Toast.LENGTH_LONG).show();
//            }
//
//            @Override
//            public void onError(FacebookException e) {
//                Log.i(TAG, "The user information request to FB has been failed.");
//                Log.i(TAG, "The exception message is : " + e.getCause());
//                Toast.makeText(getActivity().getApplicationContext(), "The login attempt failed. Please check your registration information on FB.", Toast.LENGTH_LONG).show();
//            }
//        });
//        return view;
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        callbackManager.onActivityResult(requestCode, resultCode, data);
//    }
//
//    @Override
//    public void processFinish(JSONObject output) {
//        getActivity().finish();
//        if(output == null) {
//            // go back to the login screen with the message saying:
//            // "the combination of email and password does not match any records"
//            Log.e(TAG, "Null output is returned.");
//            String message = getString(R.string.login_failed);
//            startActivity(getActivity().getIntent());
//            Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
//        } else {
//            String email = null;
//            try {
//                email = output.getString("email");
//            } catch(JSONException e) {
//                Log.e(TAG, "JSON Exception happens: " + e.getCause());
//            }
//            Log.i(TAG, "The next activity is: MapsActivity.class" );
//            Intent intent = new Intent(getActivity(), MapsActivity.class);
//            startActivity(intent);
//        }
//    }
//}
