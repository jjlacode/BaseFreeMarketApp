package com.codevsolution.base.module;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;

import com.codevsolution.base.R;
import com.codevsolution.base.android.FragmentBase;
import com.codevsolution.base.android.controls.EditMaterialLayout;
import com.codevsolution.base.android.controls.ViewGroupLayout;
import com.codevsolution.base.style.Estilos;
import com.codevsolution.base.time.TimeDateUtil;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnTapListener;
import com.github.barteksc.pdfviewer.util.FitPolicy;

import java.io.File;

public class PdfViewerModule extends BaseModule {

    private PDFView viewerPdf;
    private Button btnAntPage;
    private Button btnNextPage;
    private int pageNumber;
    private EditMaterialLayout etNumeroPag;
    private int alto;
    private long timestamp;
    private Callback callback;


    public PdfViewerModule(ViewGroup parent, Context context, Fragment fragment, int alto, String rutaPdf, Callback callback) {
        super(parent, context, fragment);
        this.alto = alto;
        this.callback = callback;
        init();
        if (rutaPdf != null) {
            cargarDocumento(rutaPdf);
        }
    }

    public PdfViewerModule(ViewGroup parent, Context context, Fragment fragment, int alto, String rutaPdf) {
        super(parent, context, fragment);
        this.alto = alto;
        init();
        if (rutaPdf != null) {
            cargarDocumento(rutaPdf);
        }
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    @Override
    public void init() {

        viewerPdf = (PDFView) vistaMain.addVista(new PDFView(context, null));
        Estilos.setLayoutParams(vistaMain.getViewGroup(), viewerPdf, ViewGroupLayout.MATCH_PARENT, alto);
        viewerPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (viewerPdf.getZoom() > 1) {
                    ((FragmentBase) fragmentParent).frameAnimationCuerpo.setActivo(false);
                    ((FragmentBase) fragmentParent).scrollDetalle.setScrollingEnabled(false);
                } else {
                    ((FragmentBase) fragmentParent).frameAnimationCuerpo.setActivo(true);
                    ((FragmentBase) fragmentParent).scrollDetalle.setScrollingEnabled(true);
                }

            }
        });

        ViewGroupLayout vistaPdf = new ViewGroupLayout(context, vistaMain.getViewGroup());
        vistaPdf.setOrientacion(LinearLayoutCompat.HORIZONTAL);
        btnAntPage = vistaPdf.addButtonSecondary(R.string.ant_page, 1);
        btnAntPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPage = viewerPdf.getCurrentPage();
                if (currentPage > 0) {
                    viewerPdf.jumpTo(currentPage - 1);
                    if (viewerPdf.getCurrentPage() == 0) {
                        btnAntPage.setVisibility(View.GONE);
                    } else {
                        btnAntPage.setVisibility(View.VISIBLE);
                    }
                    if (viewerPdf.getCurrentPage() == viewerPdf.getPageCount() - 1) {
                        btnNextPage.setVisibility(View.GONE);
                    } else {
                        btnNextPage.setVisibility(View.VISIBLE);
                    }
                }
                timestamp = TimeDateUtil.ahora();
            }
        });

        etNumeroPag = vistaPdf.addEditMaterialLayout(R.string.pagina, 1);
        etNumeroPag.setActivo(false);

        btnNextPage = vistaPdf.addButtonSecondary(R.string.next_page, 1);
        btnNextPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int totPages = viewerPdf.getPageCount();
                int currentPage = viewerPdf.getCurrentPage();

                if (currentPage < totPages) {
                    viewerPdf.jumpTo(currentPage + 1);
                    if (viewerPdf.getCurrentPage() == 0) {
                        btnAntPage.setVisibility(View.GONE);
                    } else {
                        btnAntPage.setVisibility(View.VISIBLE);
                    }
                    if (viewerPdf.getCurrentPage() == viewerPdf.getPageCount() - 1) {
                        btnNextPage.setVisibility(View.GONE);
                    } else {
                        btnNextPage.setVisibility(View.VISIBLE);
                    }
                }
                timestamp = TimeDateUtil.ahora();
            }
        });
    }

    public void cargarDocumento(String rutaPdf) {

        viewerPdf.fromFile(new File(rutaPdf)).enableSwipe(true)
                .swipeHorizontal(false)
                .enableDoubletap(true)
                .onTap(new OnTapListener() {
                    @Override
                    public boolean onTap(MotionEvent e) {
                        if (viewerPdf.getZoom() > 1) {
                            ((FragmentBase) fragmentParent).frameAnimationCuerpo.setActivo(false);
                            ((FragmentBase) fragmentParent).scrollDetalle.setScrollingEnabled(false);
                        } else {
                            ((FragmentBase) fragmentParent).frameAnimationCuerpo.setActivo(true);
                            ((FragmentBase) fragmentParent).scrollDetalle.setScrollingEnabled(true);
                        }
                        return false;
                    }
                })

                .onPageChange(new OnPageChangeListener() {
                    @Override
                    public void onPageChanged(int page, int pageCount) {
                        pageNumber = page;
                        etNumeroPag.setText((String.format("%s / %s", page + 1, pageCount)));
                        ((FragmentBase) fragmentParent).frameAnimationCuerpo.setActivo(true);
                        if (viewerPdf.getCurrentPage() == 0) {
                            btnAntPage.setVisibility(View.GONE);
                        } else {
                            btnAntPage.setVisibility(View.VISIBLE);
                        }
                        if (viewerPdf.getCurrentPage() == viewerPdf.getPageCount() - 1) {
                            btnNextPage.setVisibility(View.GONE);
                        } else {
                            btnNextPage.setVisibility(View.VISIBLE);
                        }
                    }
                })

                .defaultPage(pageNumber)
                .password(null)
                .enableAntialiasing(true) // improve rendering a little bit on low-res screens
                .pageFitPolicy(FitPolicy.BOTH)
                .load();

        timestamp = TimeDateUtil.ahora();

    }

    public interface Callback {

        void onPageChange(int page, int pageCount);

        boolean onTap(MotionEvent e);

        void onNextPage();

        void onPrevPage();

        void onClick();
    }
}
