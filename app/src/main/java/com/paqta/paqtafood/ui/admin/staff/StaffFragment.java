package com.paqta.paqtafood.ui.admin.staff;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.paqta.paqtafood.R;
import com.paqta.paqtafood.adapters.PlatilloAdapter;
import com.paqta.paqtafood.adapters.StaffAdapter;
import com.paqta.paqtafood.api.Apis;
import com.paqta.paqtafood.api.UserAPI;
import com.paqta.paqtafood.model.Producto;
import com.paqta.paqtafood.model.User;
import com.paqta.paqtafood.ui.admin.staff.components.FormStaffFragment;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StaffFragment extends Fragment {
    RecyclerView rycStaff;
    Button btnAdd, btnSeeStaffDisable;
    private FirebaseFirestore mFirestore;
    StaffAdapter mAdapter;
    private boolean mostrarTodoElPersonal = true;
    Query query;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_staff, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mFirestore = FirebaseFirestore.getInstance();

        rycStaff = view.findViewById(R.id.rycViewStaff);
        btnAdd = view.findViewById(R.id.btnAddStaff);
        btnSeeStaffDisable = view.findViewById(R.id.btnSeeDisableStaff);

        btnAdd.setOnClickListener(v -> replaceFragment(new FormStaffFragment()));

        query = mFirestore.collection("usuarios");

        btnSeeStaffDisable.setOnClickListener(v -> {
            if (mostrarTodoElPersonal) {
                setUpRecyclerView(query.whereEqualTo("disabled", true));
                btnSeeStaffDisable.setText("Ver Personal Habilitado");
                mAdapter.startListening();
            } else {
                setUpRecyclerView(query.whereEqualTo("disabled", false));
                btnSeeStaffDisable.setText("Ver Personal inhabilitado");
                mAdapter.startListening();
            }
            mostrarTodoElPersonal = !mostrarTodoElPersonal;
        });

        setUpRecyclerView(query.whereEqualTo("disabled", false));
    }

    @Override
    public void onStart() {
        super.onStart();
        mAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        mAdapter.stopListening();
    }

    private void setUpRecyclerView(Query query) {

        rycStaff.setLayoutManager(new LinearLayoutManager(getContext()));

        FirestoreRecyclerOptions<User> firestoreRecyclerOptions =
                new FirestoreRecyclerOptions.Builder<User>().setQuery(query, User.class).build();

        mAdapter = new StaffAdapter(firestoreRecyclerOptions, getActivity(), getActivity().getSupportFragmentManager());
        mAdapter.notifyDataSetChanged();
        rycStaff.setAdapter(mAdapter);


    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

}