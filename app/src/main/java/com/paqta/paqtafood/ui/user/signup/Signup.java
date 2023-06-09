package com.paqta.paqtafood.ui.user.signup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.util.Patterns;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.paqta.paqtafood.R;
import com.paqta.paqtafood.api.Apis;
import com.paqta.paqtafood.model.User;
import com.paqta.paqtafood.ui.user.login.Login;
import com.paqta.paqtafood.api.UserAPI;
import com.paqta.paqtafood.utils.ChangeColorBar;

import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Optional;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Signup extends AppCompatActivity {

    TextInputEditText txtRegUser, txtRegPassword, txtRegConfirmPassword, txtRegEmail;
    ImageView imgViewSignup;
    TextView nuevoUsuario, labelRegistro;
    MaterialButton btnRegister;
    ImageView imageView;
    ChangeColorBar changeColorBar = new ChangeColorBar();
    FirebaseFirestore mFirestore;
    FirebaseAuth mAuth;
    UserAPI userService = Apis.getUserService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        txtRegUser = findViewById(R.id.txtRegisterUser);
        txtRegPassword = findViewById(R.id.txtRegisterPassword);
        txtRegConfirmPassword = findViewById(R.id.txtRegisterConfirmPassword);
        txtRegEmail = findViewById(R.id.txtRegisterEmail);

        btnRegister = findViewById(R.id.btnRegister);
        imageView = findViewById(R.id.imgBackLogin);

        imgViewSignup = findViewById(R.id.imageLogoSignup);
        labelRegistro = findViewById(R.id.tvwSignup);
        nuevoUsuario = findViewById(R.id.tvwSignupToLogin);


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate(v);
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transitionBack();
            }
        });

        nuevoUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transitionBack();
            }
        });

        changeColorBar.window = getWindow();
        changeColorBar.cambiarColor("#151C48", "#151C48");
    }

    public void validate(View v) {
        String username = txtRegUser.getText().toString().trim();
        String email = txtRegEmail.getText().toString().trim();
        String password = txtRegPassword.getText().toString().trim();
        String confirmPassword = txtRegConfirmPassword.getText().toString().trim();

        if (username.isEmpty()) {
            txtRegUser.setError("Falta el usuario");
        } else {
            txtRegUser.setError(null);
        }

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            txtRegEmail.setError("Correo Invalido");
            return;
        } else {
            txtRegEmail.setError(null);
        }

        if (password.isEmpty() || password.length() < 6) {
            txtRegPassword.setError("Se necesitan más de 6 caracteres");
            return;
        } else if (!Pattern.compile("[0-9]").matcher(password).find()) {
            txtRegPassword.setError("Al menos un numero");
            return;
        } else {
            txtRegPassword.setError(null);
        }

        if (!confirmPassword.equals(confirmPassword)) {
            txtRegConfirmPassword.setError("Deben ser iguales");
            return;
        } else {
            registrarV2(username, email, password);
        }

    }

    public void registrarV2(String username, String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = mAuth.getCurrentUser();
                    guardarUserDB(username, user, password);
                    Intent intent = new Intent(Signup.this, Login.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(Signup.this, "Fallo en autenticación", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void guardarUserDB(String username, FirebaseUser user, String password) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", user.getUid());
        map.put("username", Optional.ofNullable(user.getDisplayName()).orElse(username));
        map.put("email", user.getEmail());
        map.put("imagen", Optional.ofNullable(user.getPhotoUrl()).orElse(Uri.parse("")));
        map.put("phone", Optional.ofNullable(user.getPhoneNumber()).orElse(""));
        map.put("password", sha256(password));
        map.put("rol", "Usuario");
        map.put("disabled", false);
        map.put("created_at", Timestamp.now());

        mFirestore.collection("usuarios")
                .document(user.getUid())
                .set(map)
                .addOnSuccessListener(this, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(Signup.this, "Registrado con exito", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Signup.this, "Error al registrar", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    public void registrar(View v, String username, String email, String password) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("username", username);
        map.put("email", email);
        map.put("password", password);

        Call<User> call = userService.registrarUsuario(map);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    Snackbar.make(v, "Registrado con éxito", Snackbar.LENGTH_SHORT).show();
                    Intent intent = new Intent(Signup.this, Login.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Snackbar.make(v, "Error: " + t.getMessage(), Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Hasheo de contraseña
     *
     * @param base, base de hasheo
     * @return Regresa un string con la contraseña hasheada
     */
    public static String sha256(String base) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(base.getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer();

            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void onBackPressed() {
        transitionBack();
    }

    public void transitionBack() {
        Intent intent = new Intent(Signup.this, Login.class);

        Pair[] pairs = new Pair[5];
        pairs[0] = new Pair<View, String>(imgViewSignup, "logoImageTrans");
        pairs[1] = new Pair<View, String>(labelRegistro, "textTrans");
        pairs[2] = new Pair<View, String>(txtRegEmail, "emailInputTextTrans");
        pairs[3] = new Pair<View, String>(txtRegPassword, "passwordInputTextTrans");
        pairs[4] = new Pair<View, String>(btnRegister, "buttonSignInTrans");

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Signup.this, pairs);
        startActivity(intent, options.toBundle());
    }
}