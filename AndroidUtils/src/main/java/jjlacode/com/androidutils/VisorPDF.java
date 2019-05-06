package jjlacode.com.androidutils;

import android.net.Uri;
import android.view.View;
import android.widget.Button;
import com.github.barteksc.pdfviewer.PDFView;
import java.io.File;

public class VisorPDF extends FragmentBase {

    private PDFView VisorPDF;
    private Button compartir;
    private File archivoPDF;
    private String uri;

    @Override
    protected void setLayout() {

        layout = R.layout.fragment_visor_pdf;
    }

    @Override
    protected void setInicio() {

        VisorPDF = view.findViewById(R.id.pdfVisor);
        compartir = view.findViewById(R.id.btncompartirvisor);

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

    }
}
