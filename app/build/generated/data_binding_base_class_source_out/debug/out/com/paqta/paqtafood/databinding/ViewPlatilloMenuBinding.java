// Generated by view binder compiler. Do not edit!
package com.paqta.paqtafood.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.google.android.material.button.MaterialButton;
import com.paqta.paqtafood.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ViewPlatilloMenuBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final ConstraintLayout constraintLayout4;

  @NonNull
  public final ConstraintLayout constraintLayout5;

  @NonNull
  public final ImageView imagenPlatilloMenu;

  @NonNull
  public final TextView textNombrePlatilloMenu;

  @NonNull
  public final MaterialButton viewDetailPlatilloMenu;

  private ViewPlatilloMenuBinding(@NonNull ConstraintLayout rootView,
      @NonNull ConstraintLayout constraintLayout4, @NonNull ConstraintLayout constraintLayout5,
      @NonNull ImageView imagenPlatilloMenu, @NonNull TextView textNombrePlatilloMenu,
      @NonNull MaterialButton viewDetailPlatilloMenu) {
    this.rootView = rootView;
    this.constraintLayout4 = constraintLayout4;
    this.constraintLayout5 = constraintLayout5;
    this.imagenPlatilloMenu = imagenPlatilloMenu;
    this.textNombrePlatilloMenu = textNombrePlatilloMenu;
    this.viewDetailPlatilloMenu = viewDetailPlatilloMenu;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ViewPlatilloMenuBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ViewPlatilloMenuBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.view_platillo_menu, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ViewPlatilloMenuBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.constraintLayout4;
      ConstraintLayout constraintLayout4 = ViewBindings.findChildViewById(rootView, id);
      if (constraintLayout4 == null) {
        break missingId;
      }

      id = R.id.constraintLayout5;
      ConstraintLayout constraintLayout5 = ViewBindings.findChildViewById(rootView, id);
      if (constraintLayout5 == null) {
        break missingId;
      }

      id = R.id.imagenPlatilloMenu;
      ImageView imagenPlatilloMenu = ViewBindings.findChildViewById(rootView, id);
      if (imagenPlatilloMenu == null) {
        break missingId;
      }

      id = R.id.textNombrePlatilloMenu;
      TextView textNombrePlatilloMenu = ViewBindings.findChildViewById(rootView, id);
      if (textNombrePlatilloMenu == null) {
        break missingId;
      }

      id = R.id.viewDetailPlatilloMenu;
      MaterialButton viewDetailPlatilloMenu = ViewBindings.findChildViewById(rootView, id);
      if (viewDetailPlatilloMenu == null) {
        break missingId;
      }

      return new ViewPlatilloMenuBinding((ConstraintLayout) rootView, constraintLayout4,
          constraintLayout5, imagenPlatilloMenu, textNombrePlatilloMenu, viewDetailPlatilloMenu);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}