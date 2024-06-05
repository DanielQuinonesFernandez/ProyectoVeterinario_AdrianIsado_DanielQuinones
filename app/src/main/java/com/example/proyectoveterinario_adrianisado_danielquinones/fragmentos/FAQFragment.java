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
        listDataHeader.add(getString(R.string.c_mo_puedo_reservar_una_cita_para_mi_mascota));
        listDataHeader.add(getString(R.string.puedo_mantener_un_registro_de_la_salud_de_mis_mascotas));
        listDataHeader.add(getString(R.string.qu_servicios_veterinarios_est_n_disponibles_en_la_aplicaci_n));
        listDataHeader.add(getString(R.string.c_mo_puedo_acceder_al_historial_de_visitas_veterinarias));
        listDataHeader.add(getString(R.string.la_aplicaci_n_env_a_recordatorios_para_las_citas));
        listDataHeader.add(getString(R.string.puedo_contactar_a_mi_veterinario_a_trav_s_de_la_aplicaci_n));
        listDataHeader.add(getString(R.string.c_mo_actualizo_la_informaci_n_de_mi_mascota));
        listDataHeader.add(getString(R.string.la_aplicaci_n_es_compatible_con_m_ltiples_mascotas));
        listDataHeader.add(getString(R.string.qu_debo_hacer_si_olvido_mi_contrase_a));
        listDataHeader.add(getString(R.string.c_mo_aseguran_la_privacidad_de_la_informaci_n_de_mi_mascota));

        // Adding child data (Respuestas)
        List<String> answer1 = new ArrayList<>();
        answer1.add(getString(R.string.para_reservar_una_cita_para_su_mascota_simplemente_navegue_a_la_secci_n_de_citas_seleccione_el_tipo_de_servicio_que_necesita_y_elija_una_fecha_y_hora_disponibles));

        List<String> answer2 = new ArrayList<>();
        answer2.add(getString(R.string.s_puede_mantener_un_registro_detallado_de_la_salud_de_sus_mascotas_incluyendo_vacunas_tratamientos_y_notas_del_veterinario));

        List<String> answer3 = new ArrayList<>();
        answer3.add(getString(R.string.la_aplicaci_n_ofrece_una_variedad_de_servicios_incluyendo_reservas_de_citas_historial_m_dico_recordatorios_de_citas_y_acceso_a_consultas_veterinarias_en_l_nea));

        List<String> answer4 = new ArrayList<>();
        answer4.add(getString(R.string.puede_acceder_al_historial_de_visitas_veterinarias_desde_el_men_desplazable_de_la_aplicaci_n_que_se_encuentra_en_las_3_rayas_horizontales_de_arriba_a_la_izquierda));

        List<String> answer5 = new ArrayList<>();
        answer5.add(getString(R.string.s_la_aplicaci_n_env_a_recordatorios_autom_ticos_para_las_citas_programadas_para_que_nunca_las_olvide));

        List<String> answer6 = new ArrayList<>();
        answer6.add(getString(R.string.no_para_contactar_con_su_verinario_puede_llamar_al_n_mero_de_tel_fono_del_veterinario_o_presentarse_presencialmente_all));

        List<String> answer7 = new ArrayList<>();
        answer7.add(getString(R.string.a_n_no_existe_la_posibilidad_de_actualizar_la_informaci_n_de_su_mascota_desde_la_aplicaci_n_aseg_rate_de_escribir_correctamente_la_informaci_n));

        List<String> answer8 = new ArrayList<>();
        answer8.add(getString(R.string.s_la_aplicaci_n_es_compatible_con_m_ltiples_mascotas_permiti_ndole_gestionar_la_salud_y_las_citas_de_cada_una_por_separado));

        List<String> answer9 = new ArrayList<>();
        answer9.add(getString(R.string.si_olvida_su_contrase_a_puede_utilizar_la_opci_n_olvid_mi_contrase_a_en_la_pantalla_de_inicio_de_sesi_n_para_restablecerla));

        List<String> answer10 = new ArrayList<>();
        answer10.add(getString(R.string.la_privacidad_de_la_informaci_n_de_su_mascota_es_muy_importante_utilizamos_medidas_de_seguridad_avanzadas_para_proteger_todos_los_datos_personales_y_de_salud));

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