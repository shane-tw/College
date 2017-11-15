package pm.shane.alexaclone;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */

    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle(R.string.sign_in);
        // Set up the login form.
        mEmailView = findViewById(R.id.email);

        mPasswordView = findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener((TextView textView, int id, KeyEvent keyEvent) -> {
            if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                attemptLogin();
                return true;
            }
            return false;
        });

        Button mEmailSignInButton = findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(view -> attemptLogin());

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    private void attemptLogin() {
        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean validationError = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            validationError = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            validationError = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            validationError = true;
        }

    //    if (validationError) {
    //        // focus the first form field with an error.
    //       focusView.requestFocus();
    //    } else {
            showProgress(true);

          /*
            RetrofitManager.getService().login(new Credentials(mEmailView.getText().toString(), mPasswordView.getText().toString(), "Patient"))
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<GenericResponse<User>>() {
                        @Override
                        public void onSubscribe(Disposable s) {}


                        @Override
                        public void onNext(GenericResponse<User> userResponse) {
                            Toast.makeText(LoginActivity.this, "Logged in successfully.", Toast.LENGTH_SHORT).show();
                            SessionManager.setLoggedInUser(userResponse.getData()); */
                            Intent myIntent = new Intent(LoginActivity.this, SettingsActivity.class);
                            LoginActivity.this.startActivity(myIntent);
                            finish();}
     /*                   }

                        @Override
                        public void onError(Throwable t) {
                            if (t instanceof HttpException) {
                                Response response = ((HttpException)t).response();
                                ResponseBody responseBody = response.errorBody();
                                if (responseBody == null) {
                                    return;
                                }
                                Converter<ResponseBody, GenericResponse<Void>> converter = RetrofitManager.getRetrofit().responseBodyConverter(GenericResponse.class, new Annotation[0]);
                                GenericResponse<Void> genericResponse;
                                try {
                                    genericResponse = converter.convert(responseBody);
                                    for (Error error : genericResponse.getErrors()) {
                                        Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                } catch (IOException | JsonSyntaxException e) {
                                    Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                            }
                            showProgress(false);
                        }

                        @Override
                        public void onComplete() {}
                    });
        }
   // }
*/
    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return true;// password.length() > 4;
    }

    private void showProgress(final boolean show) {
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
    }

}

