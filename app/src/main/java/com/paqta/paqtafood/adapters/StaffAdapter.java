package com.paqta.paqtafood.adapters;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.paqta.paqtafood.R;
import com.paqta.paqtafood.api.Apis;
import com.paqta.paqtafood.api.UserAPI;
import com.paqta.paqtafood.model.User;
import com.paqta.paqtafood.ui.admin.staff.components.FormStaffFragment;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StaffAdapter extends FirestoreRecyclerAdapter<User, StaffAdapter.ViewHolder> {
    UserAPI userService = Apis.getUserService();
    Activity activity;
    FragmentManager fm;

    public StaffAdapter(@NonNull FirestoreRecyclerOptions<User> options, FragmentActivity activity, FragmentManager supportFragmentManager) {
        super(options);
        this.activity = activity;
        this.fm = supportFragmentManager;
    }

    @Override
    protected void onBindViewHolder(@NonNull StaffAdapter.ViewHolder holder, int position, @NonNull User model) {
        DocumentSnapshot documentSnapshot = getSnapshots().getSnapshot(holder.getBindingAdapterPosition());
        final String id = documentSnapshot.getId();

        holder.nameStaff.setText(model.getUsername());
        holder.rolStaff.setText(model.getRol());

        Glide.with(holder.imageStaff.getContext())
                .load(model.getImagen())
                .placeholder(R.drawable.baseline_person_24)
                .into(holder.imageStaff);

        holder.btnDetail.setOnClickListener(v -> irAlDetalle(id));
        holder.btnDelete.setOnClickListener(v -> {
            if (model.isDisabled()) {
                eliminarUser(id);
            } else {
                inhabilitarUser(id);
            }
        });

        holder.btnEnableUser.setOnClickListener(v -> habilitarUser(id));
        if (model.isDisabled()) {
            holder.btnEnableUser.setVisibility(View.VISIBLE);
        } else {
            holder.btnEnableUser.setVisibility(View.GONE);
        }
    }

    private void irAlDetalle(String id) {
        FormStaffFragment fragment = new FormStaffFragment();

        Bundle bundle = new Bundle();
        bundle.putString("idStaff", id);

        fragment.setArguments(bundle);

        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void inhabilitarUser(String id) {
        Call<Boolean> call = userService.inhabilitarUsuario(id);

        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(activity, "Usuario Inhabilitado", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Toast.makeText(activity, "Error al inhabilitar el usuario", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void habilitarUser(String id) {
        Call<Boolean> call = userService.habilitarUsuario(id);

        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(activity, "Usuario habilitado", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Toast.makeText(activity, "Error al habilitar al usuario", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void eliminarUser(String id) {
        Call<Boolean> call = userService.eliminarUsuario(id);

        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful()) {
                    StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                    StorageReference imageRef = storageReference.child("usuarios/*user" + id);

                    imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(activity, "Imagen del usuario eliminada", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(activity, "Error al eliminar la imagen", Toast.LENGTH_SHORT).show();
                        }
                    });

                    Toast.makeText(activity, "Usuario eliminado", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Toast.makeText(activity, "Error al eliminar el usuario", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @NonNull
    @Override
    public StaffAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_staff_single, parent, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageStaff;
        TextView nameStaff, rolStaff;
        MaterialButton btnDetail, btnDelete, btnEnableUser;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageStaff = itemView.findViewById(R.id.imagenStaff);
            nameStaff = itemView.findViewById(R.id.textViewNameStaff);
            rolStaff = itemView.findViewById(R.id.textViewRolStaff);
            btnDetail = itemView.findViewById(R.id.btnDetailStaff);
            btnDelete = itemView.findViewById(R.id.btnDeleteStaff);
            btnEnableUser = itemView.findViewById(R.id.btnHabilitarUser);
        }


    }
}
