package com.paqta.paqtafood.api;

import com.paqta.paqtafood.model.User;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UserAPI {

    public static final String PREFIX = "api/usuarios";

    @GET(PREFIX + "/list")
    Call<List<User>> listar();

    @GET(PREFIX + "/disabled")
    Call<List<User>> listarDisabled();

    @GET(PREFIX + "/{id}/googleProvider")
    Call<Boolean> isLoggedInWithGoogle(@Path("id") String id);

    @GET(PREFIX + "/{id}/get")
    Call<User> obtenerUsuarioPorId(@Path("id") String id);

    @POST(PREFIX + "/add")
    Call<User> registrarUsuario(@Body HashMap<String, Object> user);

    @PUT(PREFIX + "/{id}/update")
    Call<User> editarUsuario(@Path("id") String id, @Body HashMap<String, Object> user);

    @PUT(PREFIX + "/{id}/disable")
    Call<Boolean> inhabilitarUsuario(@Path("id") String id);

    @PUT(PREFIX + "/{id}/enable")
    Call<Boolean> habilitarUsuario(@Path("id") String id);

    @DELETE(PREFIX + "/{id}/delete")
    Call<Boolean> eliminarUsuario(@Path("id") String id);

}
