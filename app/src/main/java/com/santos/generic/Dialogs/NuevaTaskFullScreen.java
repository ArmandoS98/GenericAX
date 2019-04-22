package com.santos.generic.Dialogs;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import com.santos.firestoremeth.Models.Notas;
import com.santos.generic.Interfaz.IDatos;
import com.santos.generic.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class NuevaTaskFullScreen extends DialogFragment implements View.OnClickListener {

    private EditText mEditTextTitulo;
    private EditText mEditTextContent;
    private TextView mTextViewFecha;

    private IDatos iDatos;
    private Toolbar toolbar;

    private String fecha_final;

    private static final String CERO = "0";
    private static final String BARRA = "/";

    public static NuevaTaskFullScreen newInstance(Notas notas) {
        NuevaTaskFullScreen dialog = new NuevaTaskFullScreen();

        Bundle args = new Bundle();
        args.putParcelable("notas", notas);
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
        setHasOptionsMenu(true);
        //notas = getArguments().getParcelable("notas");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_fullscreen_nueva_task, container, false);

        toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_close_black_24dp);
        toolbar.setNavigationOnClickListener(v -> dismiss());
        toolbar.setTitle(R.string.nueva_tarea_tittle);

        //mTextViewSave = view.findViewById(R.id.save);

        mEditTextTitulo = view.findViewById(R.id.et_titulo);
        mEditTextContent = view.findViewById(R.id.et_descripcion);
        mTextViewFecha = view.findViewById(R.id.tv_view_fecha);
        view.findViewById(R.id.linLayout1).setOnClickListener(this::onClick);

        //mTextViewSave.setOnClickListener(this);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolbar.inflateMenu(R.menu.nueva_tarea);
        toolbar.setOnMenuItemClickListener(item -> {
            String title = mEditTextTitulo.getText().toString();
            String content = mEditTextContent.getText().toString();
            if (!title.equals("") || !fecha_final.equals("") || !content.equals("")) {
                iDatos.onNuevaTarea(title, content, fecha_final);
                getDialog().dismiss();
            } else {
                Toast.makeText(getActivity(), "Todos los campos son necesarios!", Toast.LENGTH_SHORT).show();
            }
            dismiss();
            return true;
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        iDatos = (IDatos) getActivity();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.linLayout1:
                final Calendar c = Calendar.getInstance();
                int mHour = c.get(Calendar.HOUR_OF_DAY);
                int mMinute = c.get(Calendar.MINUTE);


                final int mes = c.get(Calendar.MONTH);
                final int dia = c.get(Calendar.DAY_OF_MONTH);
                final int anio = c.get(Calendar.YEAR);


                /*TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                        (view, hourOfDay, minute) -> {
                            from_time.setText(String.format("%02d:%02d", hourOfDay, minute));
                            semana.setHora_de(String.format("%02d:%02d", hourOfDay, minute));
                        }, mHour, mMinute, true);
                timePickerDialog.setTitle(R.string.choose_time);
                timePickerDialog.show();*/

                DatePickerDialog recogerFecha = new DatePickerDialog(getContext(), (view, year, month, dayOfMonth) -> {
                    final int mesActual = month + 1;
                    String diaFormateado = (dayOfMonth < 10) ? CERO + String.valueOf(dayOfMonth) : String.valueOf(dayOfMonth);
                    String mesFormateado = (mesActual < 10) ? CERO + String.valueOf(mesActual) : String.valueOf(mesActual);
                    SimpleDateFormat sdf = new SimpleDateFormat("MMM dd");
                    //"2018-07-31T09:27:37Z"
                    fecha_final = year + "-" + mesFormateado + "-" + diaFormateado + "T09:27:37Z";
                    String date = sdf.format(getDateFromString(fecha_final));

                    mTextViewFecha.setText(date);
                }, anio, mes, dia);
                //Muestro el widget
                recogerFecha.show();
                break;
            /*case R.id.save: {

                String title = mEditTextTitulo.getText().toString();
                String content = mEditTextContent.getText().toString();

                if (!title.equals("")) {
                    iDatos.onNuevoCuestionario(title, content);
                    getDialog().dismiss();
                } else {
                    Toast.makeText(getActivity(), "Enter a title", Toast.LENGTH_SHORT).show();
                }
                break;
            }*/

        }
    }

    static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

    public Date getDateFromString(String datetoSaved) {

        try {
            Date date = format.parse(datetoSaved);
            return date;
        } catch (ParseException e) {
            return null;
        }

    }

}
