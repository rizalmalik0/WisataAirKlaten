package malik.wisataairklaten;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.dd.processbutton.iml.ActionProcessButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mobsandgeeks.saripaar.Rule;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Checked;
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.Email;
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
public class Registrasi extends AppCompatActivity implements ProgressGenerator.OnCompleteListener, View.OnClickListener, Validator.ValidationListener, View.OnFocusChangeListener {
    Toolbar toolbar;
    ActionProcessButton btnRegistrasi;
    ProgressBar pbUsername;
    TextInputLayout wrapperUsername, wrapperPassword, wrapperNama, wrapperKonfPassword, wrapperEmail;
    ProgressGenerator progressGenerator;
    DataAPI api;
    Validator validator;
    SharedPreferences preferences;
    LinearLayout layoutRegistrasi;

    boolean userValid = false;

    @TextRule(order = 1, minLength = 5, maxLength = 20, message = "Masukkan Username (min 5)")
    EditText etUsername;
    @TextRule(order = 2, minLength = 5, maxLength = 20, message = "Masukkan Nama")

    EditText etNama;
    @Email(order = 3, message = "Masukkan valid Email")
    EditText etEmail;
    @Password(order = 4, message = "Masukkan Password (min 6)")
    @TextRule(order = 5, minLength = 6, maxLength = 20, message = "Masukkan Password (min 6)")
    EditText etPassword;
    @ConfirmPassword(order = 6, message = "Password tidak cocok")
    EditText etKonfPassword;
    @Checked(order = 7, message = "Anda harus setuju dengan syarat dan ketentuan")
    CheckBox cbSyarat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registrasi);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        // init
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        pbUsername = (ProgressBar) findViewById(R.id.pbUsername);
        btnRegistrasi = (ActionProcessButton) findViewById(R.id.btnRegistrasi);
        etUsername = (EditText) findViewById(R.id.etUsername);
        etNama = (EditText) findViewById(R.id.etNamaLengkap);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etKonfPassword = (EditText) findViewById(R.id.etKonfPassword);
        wrapperUsername = (TextInputLayout) findViewById(R.id.wrapperUsername);
        wrapperNama = (TextInputLayout) findViewById(R.id.wrapperNama);
        wrapperEmail = (TextInputLayout) findViewById(R.id.wrapperEmail);
        wrapperPassword = (TextInputLayout) findViewById(R.id.wrapperPassword);
        wrapperKonfPassword = (TextInputLayout) findViewById(R.id.wrapperKonfPassword);
        cbSyarat = (CheckBox) findViewById(R.id.cbSyarat);
        layoutRegistrasi = (LinearLayout) findViewById(R.id.layout_registrasi);

        //set view
        btnRegistrasi.setMode(ActionProcessButton.Mode.PROGRESS);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // koneksi
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(List.class,
                        new DataDeserializer<List<Review>>())
                .registerTypeAdapter(JSONPesan.class, new DataDeserializer<JSONPesan>()).create();
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setConverter(new GsonConverter(gson))
                .setEndpoint(VariableGlobal.URL).build();
        api = restAdapter.create(DataAPI.class);

        // validation
        validator = new Validator(this);
        validator.setValidationListener(this);

        // listener
        progressGenerator = new ProgressGenerator(this);
        btnRegistrasi.setOnClickListener(this);
        etUsername.setOnFocusChangeListener(this);
    }

    public void cekUsername() {
        userValid = false;

        if (etUsername.getText().toString().trim().matches("^[a-z0-9_]{5,15}$")) {
            wrapperUsername.setErrorEnabled(false);
            pbUsername.setVisibility(View.VISIBLE);

            api.getStatusUsername(etUsername.getText().toString(), new Callback<JSONPesan>() {
                @Override
                public void success(JSONPesan jsonPesan, Response response) {
                    pbUsername.setVisibility(View.GONE);

                    if (jsonPesan.getStatus() == 1) {
                        wrapperUsername.setError("Username sudah ada");
                    } else {
                        wrapperUsername.setErrorEnabled(false);
                        userValid = true;
                    }
                }

                @Override
                public void failure(RetrofitError retrofitError) {
                    pbUsername.setVisibility(View.GONE);
                    Toast.makeText(Registrasi.this, "Koneksi Gagal. Cek Koneksi Internet Anda", Toast.LENGTH_SHORT).show();
                }
            });
        } else if (etUsername.getText().length() < 5) {
            wrapperUsername.setError("Masukkan Username (min 5)");
        } else {
            wrapperUsername.setError("Username hanya dapat mengandung huruf, angka, dan '_'");
        }
    }

    public void registrasi() {
        setFormEnabled(false);

        String username = etUsername.getText().toString();
        String nama = etNama.getText().toString();
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        api.registrasi(username, nama, email, password, new Callback<JSONData<User>>() {
            @Override
            public void success(JSONData<User> userJSONData, Response response) {
                setFormEnabled(true);

                if (userJSONData.getStatus() == 1) {
                    finish();
                    btnRegistrasi.setProgress(100);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("login", true);
                    editor.putInt("id_user", userJSONData.getData().getId_user());
                    editor.putString("nama", userJSONData.getData().getNama());
                    editor.putString("username", userJSONData.getData().getUsername());
                    editor.commit();
                } else {
                    Toast.makeText(Registrasi.this, "Registrasi Gagal", Toast.LENGTH_SHORT).show();
                    btnRegistrasi.setProgress(-1);
                }
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                setFormEnabled(true);
                btnRegistrasi.setProgress(-1);
                Toast.makeText(Registrasi.this, "Koneksi Gagal " + retrofitError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setFormEnabled(boolean enable) {
        etUsername.setEnabled(enable);
        etPassword.setEnabled(enable);
        etNama.setEnabled(enable);
        etKonfPassword.setEnabled(enable);
        etEmail.setEnabled(enable);
        btnRegistrasi.setEnabled(enable);
    }

    public void resetError() {
        wrapperUsername.setErrorEnabled(false);
        wrapperPassword.setErrorEnabled(false);
        wrapperKonfPassword.setErrorEnabled(false);
        wrapperEmail.setErrorEnabled(false);
        wrapperNama.setErrorEnabled(false);
    }

    @Override
    public void onComplete() {
        registrasi();
    }

    @Override
    public void onClick(View v) {
        resetError();
        validator.validate();
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus) {
            cekUsername();
        }
    }

    @Override
    public void onValidationSucceeded() {
        if (userValid) {
            progressGenerator.start(btnRegistrasi);
        } else {
            etUsername.requestFocus();
            wrapperUsername.setError("Cek Valid Username");
        }
    }

    @Override
    public void onValidationFailed(View failedView, Rule<?> failedRule) {
        String message = failedRule.getFailureMessage();

        switch (failedView.getId()) {
            case R.id.etUsername:
                etUsername.requestFocus();
                wrapperUsername.setError(message);
                break;
            case R.id.etNamaLengkap:
                etNama.requestFocus();
                wrapperNama.setError(message);
                break;
            case R.id.etEmail:
                etEmail.requestFocus();
                wrapperEmail.setError(message);
                break;
            case R.id.etPassword:
                etPassword.requestFocus();
                wrapperPassword.setError(message);
                break;
            case R.id.etKonfPassword:
                etKonfPassword.requestFocus();
                wrapperKonfPassword.setError(message);
                break;
            default:
                failedView.requestFocus();
                Snackbar
                        .make(layoutRegistrasi, "Anda harus setuju dengan syarat dan ketentuan", Snackbar.LENGTH_SHORT).show();
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
