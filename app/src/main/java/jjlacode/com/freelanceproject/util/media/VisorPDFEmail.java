package jjlacode.com.freelanceproject.util.media;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.github.barteksc.pdfviewer.PDFView;

import java.io.File;

import jjlacode.com.freelanceproject.util.android.AppActivity;
import jjlacode.com.freelanceproject.util.android.FragmentBase;
import jjlacode.com.freelanceproject.util.android.controls.EditMaterial;

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

        layout = jjlacode.com.freelanceproject.R.layout.fragment_visor_pdf_email;
    }

    @Override
    protected void setInicio() {

        emailDir = view.findViewById(jjlacode.com.freelanceproject.R.id.etemailvisor);
        asunto = view.findViewById(jjlacode.com.freelanceproject.R.id.etasuntovisor);
        textoEmail = view.findViewById(jjlacode.com.freelanceproject.R.id.ettextovisor);
        enviaremail = view.findViewById(jjlacode.com.freelanceproject.R.id.btnenviarvisor);

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
