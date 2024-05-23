package com.example.proyectoveterinario_adrianisado_danielquinones.fragmentos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.proyectoveterinario_adrianisado_danielquinones.R;
import com.example.proyectoveterinario_adrianisado_danielquinones.adaptadores.AdaptadorListaFAQ;
import com.example.proyectoveterinario_adrianisado_danielquinones.databinding.FragmentFaqBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FAQFragment extends Fragment {

    private FragmentFaqBinding binding;
    private AdaptadorListaFAQ listAdapter;
    private ExpandableListView expListView;
    private List<String> listDataHeader;
    private HashMap<String, List<String>> listDataChild;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFaqBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Get the ExpandableListView
        expListView = root.findViewById(R.id.expandableListviewFAQ);

        // Preparing list data
        rellenarListaDePreguntas();

        listAdapter = new AdaptadorListaFAQ(getContext(), listDataHeader, listDataChild);

        // Setting list adapter
        expListView.setAdapter(listAdapter);

        return root;
    }

    private void rellenarListaDePreguntas() {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();

        // Adding header data (Preguntas)
        listDataHeader.add("¿Cómo puedo reservar una cita para mi mascota?");
        listDataHeader.add("¿Puedo mantener un registro de la salud de mis mascotas?");
        listDataHeader.add("¿Qué servicios veterinarios están disponibles en la aplicación?");
        listDataHeader.add("¿Cómo puedo acceder al historial de visitas veterinarias?");
        listDataHeader.add("¿La aplicación envía recordatorios para las citas?");
        listDataHeader.add("¿Puedo contactar a mi veterinario a través de la aplicación?");
        listDataHeader.add("¿Cómo actualizo la información de mi mascota?");
        listDataHeader.add("¿La aplicación es compatible con múltiples mascotas?");
        listDataHeader.add("¿Qué debo hacer si olvido mi contraseña?");
        listDataHeader.add("¿Cómo aseguran la privacidad de la información de mi mascota?");

        // Adding child data (Respuestas)
        List<String> answer1 = new ArrayList<>();
        answer1.add("Para reservar una cita para su mascota, simplemente navegue a la sección de citas, seleccione el tipo de servicio que necesita y elija una fecha y hora disponibles.");

        List<String> answer2 = new ArrayList<>();
        answer2.add("Sí, puede mantener un registro detallado de la salud de sus mascotas, incluyendo vacunas, tratamientos y notas del veterinario.");

        List<String> answer3 = new ArrayList<>();
        answer3.add("La aplicación ofrece una variedad de servicios, incluyendo reservas de citas, historial médico, recordatorios de citas y acceso a consultas veterinarias en línea.");

        List<String> answer4 = new ArrayList<>();
        answer4.add("Puede acceder al historial de visitas veterinarias desde el menú desplazable de la aplicación que se encuentra en las 3 rayas horizontales de arriba a la izquierda.");

        List<String> answer5 = new ArrayList<>();
        answer5.add("Sí, la aplicación envía recordatorios automáticos para las citas programadas para que nunca las olvide.");

        List<String> answer6 = new ArrayList<>();
        answer6.add("No, para contactar con su verinario puede llamar al número de teléfono del veterinario o presentarse presencialmente allí");

        List<String> answer7 = new ArrayList<>();
        answer7.add("Aún no existe la posibilidad de actualizar la información de su mascota desde la aplicación. ¡Asegúrate de escribir correctamente la información!");

        List<String> answer8 = new ArrayList<>();
        answer8.add("Sí, la aplicación es compatible con múltiples mascotas, permitiéndole gestionar la salud y las citas de cada una por separado.");

        List<String> answer9 = new ArrayList<>();
        answer9.add("Si olvida su contraseña, puede utilizar la opción 'Olvidé mi contraseña' en la pantalla de inicio de sesión para restablecerla.");

        List<String> answer10 = new ArrayList<>();
        answer10.add("La privacidad de la información de su mascota es muy importante. Utilizamos medidas de seguridad avanzadas para proteger todos los datos personales y de salud.");

        // Linking child data to header data
        listDataChild.put(listDataHeader.get(0), answer1);
        listDataChild.put(listDataHeader.get(1), answer2);
        listDataChild.put(listDataHeader.get(2), answer3);
        listDataChild.put(listDataHeader.get(3), answer4);
        listDataChild.put(listDataHeader.get(4), answer5);
        listDataChild.put(listDataHeader.get(5), answer6);
        listDataChild.put(listDataHeader.get(6), answer7);
        listDataChild.put(listDataHeader.get(7), answer8);
        listDataChild.put(listDataHeader.get(8), answer9);
        listDataChild.put(listDataHeader.get(9), answer10);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}