package jjlacode.com.freelanceproject.util;

import android.net.Uri;
import android.view.View;
import android.widget.Button;

import com.github.barteksc.pdfviewer.PDFView;

import java.io.File;

import jjlacode.com.freelanceproject.R;
import jjlacode.com.freelanceproject.ui.FragmentCRUDProyecto;

import static jjlacode.com.freelanceproject.util.JavaUtil.Constantes.ORIGEN;
import static jjlacode.com.freelanceproject.util.JavaUtil.Constantes.SUBTITULO;

public class VisorPDF extends FragmentBase {

    private PDFView VisorPDF;
    private Button compartir;
    private Button volver;
    private File archivoPDF;
    private String uri;

    @Override
    protected void setLayout() {

        layout = jjlacode.com.freelanceproject.R.layout.fragment_visor_pdf;
    }

    @Override
    protected void setInicio() {

        VisorPDF = view.findViewById(jjlacode.com.freelanceproject.R.id.pdfVisor);
        compartir = view.findViewById(jjlacode.com.freelanceproject.R.id.btncompartirvisor);
        volver = view.findViewById(R.id.btnvolvervisor);

    }

    @Override
    public void onResume() {
        super.onResume();

        bundle = getArguments();
        if (bundle!=null) {

            uri = bundle.getString("uri","");
            archivoPDF = new File(bundle.getString("path", ""));
        }

        compartir.setVisibility(View.GONE);

        if (bundle != null) {

            if (uri!="") {
                compartir.setVisibility(View.VISIBLE);
                compartir.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AppActivity.compartirPdf(Uri.parse(uri));
                    }
                });
            }

            VisorPDF.fromFile(archivoPDF)
                    .enableSwipe(true)         //Deslizar página
                    .swipeHorizontal(false)    //Deslizamiento vertical de páginas
                    .enableDoubletap(true)     //Hago zoom con doble click
                    .enableAntialiasing(true)  //Mejor visualización
                    .load();
        }

        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bundle.putString(ORIGEN, bundle.getString(SUBTITULO));
                bundle.putString(SUBTITULO,CommonPry.setNamefdef());
                icFragmentos.enviarBundleAFragment(bundle,new FragmentCRUDProyecto());
            }
        });


    }
}
