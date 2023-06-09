package com.paqta.paqtafood.ui.admin.staff.components;

import static android.app.Activity.RESULT_OK;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.paqta.paqtafood.R;
import com.paqta.paqtafood.api.Apis;
import com.paqta.paqtafood.api.UserAPI;
import com.paqta.paqtafood.model.User;
import com.paqta.paqtafood.ui.admin.staff.StaffFragment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FormStaffFragment extends Fragment {

    String idStaff, storagePath = "usuarios/*", prefijo = "user";
    ImageView userFoto;
    private FirebaseFirestore mFirestore;
    private FirebaseStorage mStorage;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    Button btnAdd, btnDialogImage;
    TextInputEditText edtTxtUsername, edtTxtPassword, edtTxtEmail;
    AutoCompleteTextView rolAutoComplete;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_GALLERY = 2;
    private Uri imageUrl;
    private Bitmap imageCamera;

    private UserAPI userService = Apis.getUserService();
    Boolean isLoginWithGoogle = false;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            idStaff = getArguments().getString("idStaff");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_form_staff, container, false);

        mFirestore = FirebaseFirestore.getInstance();
        mStorage = FirebaseStorage.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        edtTxtUsername = root.findViewById(R.id.edtTextUsername);
        edtTxtEmail = root.findViewById(R.id.edtTextEmail);
        edtTxtPassword = root.findViewById(R.id.edtTextPassword);
        rolAutoComplete = root.findViewById(R.id.cmbRol);

        userFoto = root.findViewById(R.id.imageStaff);

        btnAdd = root.findViewById(R.id.btnAdd);
        btnDialogImage = root.findViewById(R.id.btnDialogImage);

        btnDialogImage.setOnClickListener(v-> mostrarDialog());


        initialize();
        procesarFormulario();
        setupDropdown();
        return root;
    }

    private void initialize() {
        if (idStaff != null) {
            Call<Boolean> call = userService.isLoggedInWithGoogle(idStaff);
            call.enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    if (response.isSuccessful()) {
                        isLoginWithGoogle = response.body();
                        if (Boolean.TRUE.equals(isLoginWithGoogle)) {
                            edtTxtPassword.setEnabled(false);
                        }
                    }
                }

                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {
                    Toast.makeText(getActivity(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_GALLERY) {
                imageUrl = data.getData();
                Glide.with(getView()).load(imageUrl).into(userFoto);
            }
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                imageCamera = (Bitmap) data.getExtras().get("data");
                imageUrl = bitmapToUri(imageCamera);
                Glide.with(getView()).load(imageUrl).into(userFoto);
            }
        }
    }

    private void setupDropdown() {
        String[] roles = getResources().getStringArray(R.array.roles_array);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, roles);
        rolAutoComplete.setAdapter(adapter);

    }

    private void procesarFormulario(){
        if (idStaff == null || idStaff.isEmpty()) {
            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String username = edtTxtUsername.getText().toString().trim();
                    String password = edtTxtPassword.getText().toString().trim();
                    String rol = rolAutoComplete.getText().toString().trim();
                    String email = edtTxtEmail.getText().toString().trim();

                    if (validar(username, rol, password, email)) {
                        postStaff(username, rol, password, email);
                    }
                }
            });
        } else {
            getStaff();
            btnAdd.setText("Actualizar");
            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String username = edtTxtUsername.getText().toString().trim();
                    String password = edtTxtPassword.getText().toString().trim();
                    String rol = rolAutoComplete.getText().toString().trim();
                    String email = edtTxtEmail.getText().toString().trim();

                    if (validar(username, rol, password, email)) {
                        updateStaff(username, rol, password, email);
                    }
                }
            });
        }
    }

    private void updateStaff(String username, String rol, String password, String email) {

        HashMap<String, Object> map = new HashMap<>();
        map.put("username", username);
        map.put("rol", rol);
        map.put("password", password);
        map.put("email", email);

        Call<User> call = userService.editarUsuario(idStaff, map);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    User user = response.body();
                    DocumentReference documentReference = mFirestore.collection("usuarios").document(user.getId());
                    if (imageUrl != null) {
                        subirImagen(documentReference);
                    }
                    Toast.makeText(getContext(), "El staff se modifico exitosamente", Toast.LENGTH_SHORT).show();
                    replaceFragment(new StaffFragment());
                }

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void postStaff(String username, String rol, String password, String email) {
        HashMap<String, Object> usuario = new HashMap<>();
        usuario.put("username", username);
        usuario.put("password", password);
        usuario.put("email", email);
        usuario.put("rol", rol);

        Call<User> call = userService.registrarUsuario(usuario);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    User user = response.body();
                    DocumentReference documentReference = mFirestore.collection("usuarios").document(user.getId());
                    subirImagen(documentReference);
                    replaceFragment(new StaffFragment());
                    Toast.makeText(getActivity(), "Usuario registrado", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getActivity(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getStaff() {
        Call<User> call = userService.obtenerUsuarioPorId(idStaff);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    User user = response.body();
                    if (user != null) {
                        Glide.with(getView()).load(user.getImagen()).into(userFoto);
                        Log.d("usuario", user.getCreated_at().toString());
                        edtTxtUsername.setText(user.getUsername());
                        edtTxtEmail.setText(user.getEmail());
                        //edtTxtPassword.setText(user.getPassword());
                        //Establece el rol en el cmb y lo vuelve a adaptar
                        rolAutoComplete.setText(user.getRol());
                        String[] roles = getResources().getStringArray(R.array.roles_array);
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, roles);
                        rolAutoComplete.setAdapter(adapter);
                    } else {
                        Toast.makeText(getActivity(), "El usuario no existe", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Error en la respuesta del servidor", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getActivity(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validar(String username, String rol, String password, String email) {
        Drawable currentDrawable = userFoto.getDrawable();
        Drawable defaultDrawable = getResources().getDrawable(R.drawable.image_icon_124);

        if (currentDrawable == null) {
            Toast.makeText(getContext(), "Selecciona una imagen", Toast.LENGTH_SHORT).show();
            return false;
        }

        Bitmap currentBitmap = ((BitmapDrawable) currentDrawable).getBitmap();
        Bitmap defaultBitmap = ((BitmapDrawable) defaultDrawable).getBitmap();

        if (username.isEmpty() || rol.isEmpty() || email.isEmpty()) {
            Toast.makeText(getContext(), "Ingresar los datos", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!isLoginWithGoogle) {
            if (password.isEmpty()) {
                Toast.makeText(getContext(), "Ingresar la contraseña", Toast.LENGTH_SHORT).show();
                return false;
            } else if (password.length() < 6) {
                Toast.makeText(getContext(), "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        if (currentBitmap.equals(defaultBitmap)) {
            Toast.makeText(getContext(), "Selecciona una imagen diferente", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void mostrarDialog() {
        new MaterialAlertDialogBuilder(getContext())
                .setTitle("Seleccione una opcion")
                .setMessage("Puede seleccionar la imagen de su galeria o si quiere puede tomar una foto con la camara")
                .setPositiveButton("Galeria", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        openImageGallery();
                    }
                })
                // Se cambio el orden solamente por estetica ;)
                .setNegativeButton("Camara", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        openCamera();
                    }
                })
                .setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    /**
     * Se encarga de subir la imagen a FirebaseStorage con la referencia del documento que se envia por parametro
     *
     * @param documentReference
     */
    private void subirImagen(DocumentReference documentReference) {
        String ruta_storage_foto = storagePath + "" + prefijo + "" + documentReference.getId();
        StorageReference imageRef = mStorage.getReference().child(ruta_storage_foto);

        UploadTask uploadTask = imageRef.putFile(imageUrl);
        uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String imageUrl = uri.toString();

                            documentReference.update("imagen", imageUrl)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(getContext(), "El documento se subió con su imagen exitosamente", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getContext(), "Error al subir el documento con su imagen", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    });
                }
            }
        });
    }

    /**
     * Abre la galeria de imagenes
     */
    private void openImageGallery() {
        permisosCamara();
        Intent galeria = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galeria.setType("image/*");
        startActivityForResult(galeria, REQUEST_IMAGE_GALLERY);
    }

    /**
     * Abre la camara del celular
     */
    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
    }

    /**
     * Se piden los permisos para poder usar la camara del usuario
     */
    private void permisosCamara() {
        if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            // El permiso de la cámara está concedido, puedes abrir la cámara
        } else {
            // El permiso de la cámara no está concedido, solicita el permiso al usuario
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.CAMERA}, REQUEST_IMAGE_CAPTURE);
        }
    }

    /**
     * Remplaza el anterior fragment con otro
     *
     * @param fragment
     */
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    /**
     * Convierte un Bitmap a una URI
     *
     * @param bitmap
     * @return
     */
    private Uri bitmapToUri(Bitmap bitmap) {
        String nombreArchivo = "temp_image";
        File archivoTemporal = new File(getActivity().getCacheDir(), nombreArchivo + ".png");
        try (OutputStream os = new FileOutputStream(archivoTemporal)) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return FileProvider.getUriForFile(getContext(), getActivity().getPackageName() + ".fileprovider", archivoTemporal);
    }

}