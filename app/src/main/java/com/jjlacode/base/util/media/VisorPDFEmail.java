package com.jjlacode.base.util.media;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.jjlacode.base.util.android.AppActivity;
import com.jjlacode.base.util.android.FragmentBase;
import com.jjlacode.base.util.android.controls.EditMaterial;
import com.jjlacode.freelanceproject.R;

public class VisorPDFEmail extends FragmentBase {

    private EditMaterial emailDir;
    private EditMaterial asunto;
    private EditMaterial textoEmail;
    private Button enviaremail;
    private String uri;
    private String emailcli;
    private String asuntocli;
    private String textocli;

    @Override
    protected void setLayout() {

        layout = R.layout.fragment_visor_pdf_email;
    }

    @Override
    protected void setInicio() {

        emailDir = view.findViewById(R.id.etemailvisor);
        asunto = view.findViewById(R.id.etasuntovisor);
        textoEmail = view.findViewById(R.id.ettextovisor);
        enviaremail = view.findViewById(R.id.btnenviarvisor);

    }

    @Override
    public void onResume() {
        super.onResume();

        Bundle bundle = getArguments();

        if (bundle !=null) {

            emailcli = bundle.getString("email","");
            asuntocli = bundle.getString("asunto","");
            textocli = bundle.getString("texto","");
            uri = bundle.getString("path", "");
        }

        emailDir.setText(emailcli);
        asunto.setText(asuntocli);
        textoEmail.setText(textocli);

        enviaremail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AppActivity.enviarEmail(getContext(),emailDir.getText().toString(),
                        asunto.getText().toString(),textoEmail.getText().toString(),
                        uri);
            }
        });

    }
}
