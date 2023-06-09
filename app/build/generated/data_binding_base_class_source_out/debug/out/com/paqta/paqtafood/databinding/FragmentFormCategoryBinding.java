// Generated by view binder compiler. Do not edit!
package com.paqta.paqtafood.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.paqta.paqtafood.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class FragmentFormCategoryBinding implements ViewBinding {
  @NonNull
  private final FrameLayout rootView;

  @NonNull
  public final Button btnAdd;

  @NonNull
  public final Button btnDeleteImage;

  @NonNull
  public final Button btnDialogImage;

  @NonNull
  public final TextInputEditText edtTextDescripcion;

  @NonNull
  public final TextInputEditText edtTextNombre;

  @NonNull
  public final TextInputLayout filledTextField;

  @NonNull
  public final TextInputLayout filledTextField2;

  @NonNull
  public final ImageView imageCategory;

  private FragmentFormCategoryBinding(@NonNull FrameLayout rootView, @NonNull Button btnAdd,
      @NonNull Button btnDeleteImage, @NonNull Button btnDialogImage,
      @NonNull TextInputEditText edtTextDescripcion, @NonNull TextInputEditText edtTextNombre,
      @NonNull TextInputLayout filledTextField, @NonNull TextInputLayout filledTextField2,
      @NonNull ImageView imageCategory) {
    this.rootView = rootView;
    this.btnAdd = btnAdd;
    this.btnDeleteImage = btnDeleteImage;
    this.btnDialogImage = btnDialogImage;
    this.edtTextDescripcion = edtTextDescripcion;
    this.edtTextNombre = edtTextNombre;
    this.filledTextField = filledTextField;
    this.filledTextField2 = filledTextField2;
    this.imageCategory = imageCategory;
  }

  @Override
  @NonNull
  public FrameLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static FragmentFormCategoryBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FragmentFormCategoryBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.fragment_form_category, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FragmentFormCategoryBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.btn_add;
      Button btnAdd = ViewBindings.findChildViewById(rootView, id);
      if (btnAdd == null) {
        break missingId;
      }

      id = R.id.btnDeleteImage;
      Button btnDeleteImage = ViewBindings.findChildViewById(rootView, id);
      if (btnDeleteImage == null) {
        break missingId;
      }

      id = R.id.btnDialogImage;
      Button btnDialogImage = ViewBindings.findChildViewById(rootView, id);
      if (btnDialogImage == null) {
        break missingId;
      }

      id = R.id.edtTextDescripcion;
      TextInputEditText edtTextDescripcion = ViewBindings.findChildViewById(rootView, id);
      if (edtTextDescripcion == null) {
        break missingId;
      }

      id = R.id.edtTextNombre;
      TextInputEditText edtTextNombre = ViewBindings.findChildViewById(rootView, id);
      if (edtTextNombre == null) {
        break missingId;
      }

      id = R.id.filledTextField;
      TextInputLayout filledTextField = ViewBindings.findChildViewById(rootView, id);
      if (filledTextField == null) {
        break missingId;
      }

      id = R.id.filledTextField2;
      TextInputLayout filledTextField2 = ViewBindings.findChildViewById(rootView, id);
      if (filledTextField2 == null) {
        break missingId;
      }

      id = R.id.imageCategory;
      ImageView imageCategory = ViewBindings.findChildViewById(rootView, id);
      if (imageCategory == null) {
        break missingId;
      }

      return new FragmentFormCategoryBinding((FrameLayout) rootView, btnAdd, btnDeleteImage,
          btnDialogImage, edtTextDescripcion, edtTextNombre, filledTextField, filledTextField2,
          imageCategory);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
