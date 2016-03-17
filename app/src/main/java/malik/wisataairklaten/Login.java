package malik.wisataairklaten;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dd.processbutton.iml.ActionProcessButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mobsandgeeks.saripaar.Rule;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.mobsandgeeks.saripaar.annotation.TextRule;

import java.util.List;

import malik.wisataairklaten.api.DataAPI;
import malik.wisataairklaten.api.DataDeserializer;
import malik.wisataairklaten.api.VariableGlobal;
import malik.wisataairklaten.model.JSONData;
import malik.wisataairklaten.model.JSONPesan;
import malik.wisataairklaten.model.Review;
import malik.wisataairklaten.model.User;
import malik.wisataairklaten.view.ProgressGenerator;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;

/**
 * Created by Rizal Malik on 31/01/2016.
 */
public class Login extends AppCompatActivity implements ProgressGenerator.OnCompleteListener, View.OnClickListener, Validator.ValidationListener {
    ActionProcessButton btnLogin;
    TextView txtRegistrasi;
    TextInputLayout wrapperUsername, wrapperPassword;
    LinearLayout layoutLogin;
    Toolbar toolbar;
    ProgressGenerator progressGenerator;
    DataAPI api;
    Validator validator;
    SharedPreferences preferences;

    @TextRule(order = 1, minLength = 4, maxLength = 20, message = "Username Salah")
    EditText etUsername;

    @Password(order = 2, message = "Password Salah")
    EditText etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        // cek login status
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
//        boolean login = preferences.getBoolean("login", false);
//        if (login) finish();

        //init
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        btnLogin = (ActionProcessButton) findViewById(R.id.btnLogin);
        txtRegistrasi = (TextView) findViewById(R.id.txtRegistrasi);
        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        wrapperUsername = (TextInputLayout) findViewById(R.id.wrapperUsername);
        wrapperPassword = (TextInputLayout) findViewById(R.id.wrapperPassword);
        layoutLogin = (LinearLayout) findViewById(R.id.layout_login);

        //set View
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        btnLogin.setMode(ActionProcessButton.Mode.PROGRESS);

        // koneksi
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(List.class,
                        new DataDeserializer<List<Review>>())
                .registerTypeAdapter(JSONPesan.class, new DataDeserializer<JSONPesan>()).create();
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setConverter(new GsonConverter(gson))
                .setEndpoint(VariableGlobal.URL).build();
        api = restAdapter.create(DataAPI.class);

        //validation
        validator = new Validator(this);
        validator.setValidationListener(this);

        //listener
        btnLogin.setOnClickListener(this);
        txtRegistrasi.setOnClickListener(this);
        progressGenerator = new ProgressGenerator(this);
    }

    public void login() {
        setFormEnabled(false);

        api.login(etUsername.getText().toString(), etPassword.getText().toString(), new Callback<JSONData<User>>() {
            @Override
            public void success(JSONData<User> userJSONData, Response response) {
                setFormEnabled(true);

                if (userJSONData.getStatus() == 1) {
                    finish();
                    btnLogin.setProgress(100);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("login", true);
                    editor.putInt("id_user", userJSONData.getData().getId_user());
                    editor.putString("nama", userJSONData.getData().getNama());
                    editor.putString("username", userJSONData.getData().getUsername());
                    editor.putString("email", userJSONData.getData().getEmail());
                    editor.commit();

                } else {
                    Snackbar.make(layoutLogin, "Login Gagal", Snackbar.LENGTH_SHORT).show();
                    btnLogin.setProgress(-1);
                }
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                setFormEnabled(true);
                btnLogin.setProgress(-1);
                Snackbar.make(layoutLogin, "Koneksi Gagal", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    public void setFormEnabled(boolean enable) {
        etUsername.setEnabled(enable);
        etPassword.setEnabled(enable);
        btnLogin.setEnabled(enable);
    }

    public void resetError() {
        wrapperUsername.setErrorEnabled(false);
        wrapperPassword.setErrorEnabled(false);
    }

    @Override
    public void onComplete() {
        login();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogin:
                resetError();
                validator.validate();
                break;
            case R.id.txtRegistrasi:
                finish();
                Intent i = new Intent(this, Registrasi.class);
                startActivity(i);
                break;
        }
    }

    @Override
    public void onValidationSucceeded() {
        progressGenerator.start(btnLogin);
    }

    @Override
    public void onValidationFailed(View failedView, Rule<?> failedRule) {
        String message = failedRule.getFailureMessage();

        switch (failedView.getId()) {
            case R.id.etUsername:
                etUsername.requestFocus();
                wrapperUsername.setError(message);
                break;
            case R.id.etPassword:
                etPassword.requestFocus();
                wrapperPassword.setError(message);
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                onBackPressed();
            }
        }

        return true;
    }
}