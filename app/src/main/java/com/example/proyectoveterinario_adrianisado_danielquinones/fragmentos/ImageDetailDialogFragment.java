package com.example.proyectoveterinario_adrianisado_danielquinones.fragmentos;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.proyectoveterinario_adrianisado_danielquinones.R;

import java.util.Objects;

public class ImageDetailDialogFragment extends DialogFragment {

    private Bitmap imageBitmap;

    public ImageDetailDialogFragment() {
        // Constructor vacío requerido para DialogFragment
    }

    // Método estático para crear una nueva instancia del fragmento de diálogo
    public static ImageDetailDialogFragment newInstance(Bitmap imageBitmap) {
        ImageDetailDialogFragment fragment = new ImageDetailDialogFragment();
        fragment.imageBitmap = imageBitmap;
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Crear un diálogo personalizado sin título
        Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_image_detail, container, false);

        ImageView imageView = view.findViewById(R.id.imageViewDetail);
        imageView.setImageBitmap(imageBitmap);

        ImageButton cerrarDialog = view.findViewById(R.id.btnClose);
        cerrarDialog.setOnClickListener(v -> dismiss());

        return view;
    }
}
