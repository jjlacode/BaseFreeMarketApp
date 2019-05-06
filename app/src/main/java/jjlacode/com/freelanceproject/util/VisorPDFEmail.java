package jjlacode.com.freelanceproject.util;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.github.barteksc.pdfviewer.PDFView;

import java.io.File;

public class VisorPDFEmail extends FragmentBase {

    private PDFView VisorPDF;
    private File archivoPDF;
    private EditText emailDir;
    private EditText asunto;
    private EditText textoEmail;
    private Button enviaremail;
    private String uri;
    private String emailcli;
    private String asuntocli;
    private String textocli;
    private Bundle bundle;

    @Override
    protected void setLayout() {

        layout = jjlacode.com.freelanceproject.R.layout.fragment_visor_pdf_email;
    }

    @Override
    protected void setInicio() {

        VisorPDF = (PDFView) view.findViewById(jjlacode.com.freelanceproject.R.id.pdfVisoremail);
        emailDir = view.findViewById(jjlacode.com.freelanceproject.R.id.etemailvisor);
        asunto = view.findViewById(jjlacode.com.freelanceproject.R.id.etasuntovisor);
        textoEmail = view.findViewById(jjlacode.com.freelanceproject.R.id.ettextovisor);
        enviaremail = view.findViewById(jjlacode.com.freelanceproject.R.id.btnenviarvisor);

    }

    @Override
    public void onResume() {
        super.onResume();

        bundle = getArguments();

        if (bundle!=null) {

            emailcli = bundle.getString("email","");
            asuntocli = bundle.getString("asunto","");
            textocli = bundle.getString("texto","");
            uri = bundle.getString("uri", "");
            archivoPDF = new File(bundle.getString("path", ""));
        }

        if (bundle != null) {

            VisorPDF.fromFile(archivoPDF)
                    .enableSwipe(true)         //Deslizar página
                    .swipeHorizontal(false)    //Deslizamiento vertical de páginas
                    .enableDoubletap(true)     //Hago zoom con doble click
                    .enableAntialiasing(true)  //Mejor visualización
                    .load();
        }

        enviaremail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AppActivity.enviarEmail(emailDir.getText().toString(),
                        asunto.getText().toString(),textoEmail.getText().toString(),
                        Uri.parse(uri));
            }
        });

    }
}
