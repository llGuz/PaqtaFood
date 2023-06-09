// Generated by view binder compiler. Do not edit!
package com.paqta.paqtafood.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.google.android.material.card.MaterialCardView;
import com.paqta.paqtafood.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ViewDrinkMenuBinding implements ViewBinding {
  @NonNull
  private final MaterialCardView rootView;

  @NonNull
  public final MaterialCardView cardDrink;

  @NonNull
  public final TextView descripcionDrink;

  @NonNull
  public final ImageView imagenDrink;

  @NonNull
  public final TextView precioDrink;

  @NonNull
  public final TextView tituloDrink;

  private ViewDrinkMenuBinding(@NonNull MaterialCardView rootView,
      @NonNull MaterialCardView cardDrink, @NonNull TextView descripcionDrink,
      @NonNull ImageView imagenDrink, @NonNull TextView precioDrink,
      @NonNull TextView tituloDrink) {
    this.rootView = rootView;
    this.cardDrink = cardDrink;
    this.descripcionDrink = descripcionDrink;
    this.imagenDrink = imagenDrink;
    this.precioDrink = precioDrink;
    this.tituloDrink = tituloDrink;
  }

  @Override
  @NonNull
  public MaterialCardView getRoot() {
    return rootView;
  }

  @NonNull
  public static ViewDrinkMenuBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ViewDrinkMenuBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.view_drink_menu, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ViewDrinkMenuBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      MaterialCardView cardDrink = (MaterialCardView) rootView;

      id = R.id.descripcionDrink;
      TextView descripcionDrink = ViewBindings.findChildViewById(rootView, id);
      if (descripcionDrink == null) {
        break missingId;
      }

      id = R.id.imagenDrink;
      ImageView imagenDrink = ViewBindings.findChildViewById(rootView, id);
      if (imagenDrink == null) {
        break missingId;
      }

      id = R.id.precioDrink;
      TextView precioDrink = ViewBindings.findChildViewById(rootView, id);
      if (precioDrink == null) {
        break missingId;
      }

      id = R.id.tituloDrink;
      TextView tituloDrink = ViewBindings.findChildViewById(rootView, id);
      if (tituloDrink == null) {
        break missingId;
      }

      return new ViewDrinkMenuBinding((MaterialCardView) rootView, cardDrink, descripcionDrink,
          imagenDrink, precioDrink, tituloDrink);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
