package ramstalk.co.jp.project.ramstalk.co.jp.project.activity.Activity;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import ramstalk.co.jp.project.R;
import ramstalk.co.jp.project.ramstalk.co.jp.project.activity.Cons.CommonConst;
import ramstalk.co.jp.project.ramstalk.co.jp.project.activity.Entity.User;
import ramstalk.co.jp.project.ramstalk.co.jp.project.activity.Interface.ApiService;
import ramstalk.co.jp.project.ramstalk.co.jp.project.activity.Manager.ApiManager;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.text.TextUtils.isEmpty;

public class RegistUserActivity extends AppCompatActivity {
    private String TAG = RegistUserActivity.class.getSimpleName();
    private EditText mEditTextUserId, mEditTextPassword, mEditTextPasswordConfirmation, mEditTextEmail;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor sharedPreferencesEditor;
    private View focusView;
    private String userId;
    private String password;
    private String passwordConfirmation;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist_user);
        sharedPreferences = getApplicationContext().getSharedPreferences(CommonConst.FileName.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        sharedPreferencesEditor = sharedPreferences.edit();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        TextView mTextSignIn = (TextView)findViewById(R.id.login_singin_link);
        mTextSignIn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                proceedToActivity(LoginActivity.class);
            }
        });

        Button mRegistButton = (Button)findViewById(R.id.buttonRegist);
        mRegistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (setUIComponent()) {//データチェック1
                    try {
                        registUser(userId, email, password, passwordConfirmation);
                    }catch (Exception e) {
                        Log.e(TAG, e.getMessage());
                    }
                }
            }
        });
    }

    private boolean setUIComponent() {
        mEditTextUserId = (EditText)findViewById(R.id.editTextUserId);
        mEditTextPassword = (EditText)findViewById(R.id.editTextPassword);
        mEditTextPasswordConfirmation = (EditText)findViewById(R.id.editTextPasswordConfirm);
        mEditTextEmail = (EditText)findViewById(R.id.editTextEmail);

        userId = mEditTextUserId.getText().toString();
        password = mEditTextPassword.getText().toString();
        passwordConfirmation = mEditTextPasswordConfirmation.getText().toString();
        email = mEditTextEmail.getText().toString();
        return isDataValid(userId,email,password, passwordConfirmation);
    }

    private void registUser(final String userId, final String email, final String password, String passwordConfirmation) throws Exception {
        HashMap<String, User> userMap = new HashMap<String, User>();
        final ApiService apiService = ApiManager.getApiService();
        userMap.put("user", new User(userId, email, password, passwordConfirmation));
        Observable<User> user = apiService.registerUser(userMap);
        user.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<User>() {
                    @Override
                    public void onCompleted() {
                        Observable<User> user = apiService.authenticate(email, password);
                        user.subscribeOn(Schedulers.newThread())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Observer<User>() {
                                    @Override
                                    public void onCompleted() {
                                        Log.d(TAG, "Login Successful");
                                        finish();
                                        proceedToActivity(TimeLineActivity.class);
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        String message = getString(R.string.login_failed);
                                        finish();
                                        startActivity(getIntent());
                                        Toast.makeText(RegistUserActivity.this, message, Toast.LENGTH_LONG).show();
                                        Log.e(TAG, "ERROR : " + e.toString());
                                    }

                                    @Override
                                    public void onNext(User user) {
                                        sharedPreferencesEditor.putString("auth_token", user.getAuthToken());
                                        sharedPreferencesEditor.putString("user_id", user.getId());
                                        sharedPreferencesEditor.apply();
                                    }
                                });
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "ERROR: " + e.toString());
                        if(e.getMessage().equalsIgnoreCase("422")) { // validation error
                            String message = getString(R.string.error_422);
                            Toast.makeText(RegistUserActivity.this, message, Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onNext(User user) {
                    }
                });
    }

    private Boolean isDataValid(String userId,String email,String password,String passwordConfirm){
        return isUserIdValid(userId) && isEmailValid(email) && isPasswordValid(password, passwordConfirm);
    }
    private boolean isPasswordValid(String password, String passwordConfirm){
        if(password.trim().length() <= 3){
            mEditTextPassword.setError(getString(R.string.error_invalid_password));
            focusView = mEditTextPassword;
            return false;
        }
        if(!password.equals(passwordConfirm.trim())){
            mEditTextPassword.setError(getString(R.string.error_invalid_passwordConfirm_not_match));
            focusView = mEditTextPassword;
            return false;
        }
        return true;
    }

    private boolean isUserIdValid(String userId){
        if(isEmpty(userId.trim())){
            mEditTextUserId.setError(getString(R.string.error_invalid_userId));
            focusView = mEditTextUserId;
            return false;
        }
        return true;
    }

    private boolean isEmailValid(String email){
        if(isEmpty(email) || !email.contains("@")){
            mEditTextEmail.setError(getString(R.string.error_invalid_email));
            focusView = mEditTextEmail;
            return false;
        }
        return true;
    }

    private void proceedToActivity(Class activity) {
        Intent intent = new Intent(this, activity);
        Log.i(TAG, "The next activity is: " + activity);
        startActivity(intent);
    }
}
