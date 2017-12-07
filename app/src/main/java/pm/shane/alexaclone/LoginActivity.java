package pm.shane.alexaclone;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.lang.annotation.Annotation;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import pm.shane.alexaclone.activities.DashboardActivity;
import pm.shane.alexaclone.api.Credentials;
import pm.shane.alexaclone.api.RetrofitManager;
import pm.shane.alexaclone.api.response.Error;
import pm.shane.alexaclone.api.response.GenericResponse;
import pm.shane.alexaclone.api.response.data.Patient;
import retrofit2.Converter;
import retrofit2.HttpException;
import retrofit2.Response;

/**
 * Created by Shane.
 */

public class LoginActivity extends AppCompatActivity {

    private EditText mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle(R.string.sign_in);

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

        Intent myIntent = new Intent(LoginActivity.this, DashboardActivity.class);
        LoginActivity.this.startActivity(myIntent);
        finish();

        /*
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        showProgress(true);

        RetrofitManager.getService().login(new Credentials(mEmailView.getText().toString(), mPasswordView.getText().toString(), "Patient"))
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Observer<GenericResponse<Patient>>() {
                @Override
                public void onSubscribe(Disposable s) {}

                @Override
                public void onNext(GenericResponse<Patient> userResponse) {
                    Toast.makeText(LoginActivity.this, "Logged in successfully.", Toast.LENGTH_SHORT).show();
                    SessionManager.setLoggedInUser(userResponse.getData());
                    Intent myIntent = new Intent(LoginActivity.this, DashboardActivity.class);
                    LoginActivity.this.startActivity(myIntent);
                    finish();
                }

                @Override
                public void onError(Throwable t) {
                    if (!(t instanceof HttpException)) {
                        Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                        showProgress(false);
                        return;
                    }
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
                    showProgress(false);
                }

                @Override
                public void onComplete() {}
            });

            */
        }

    private void showProgress(final boolean show) {
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
    }

}

