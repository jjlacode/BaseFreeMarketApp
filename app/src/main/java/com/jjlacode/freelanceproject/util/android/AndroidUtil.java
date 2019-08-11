package com.jjlacode.freelanceproject.util.android;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.LocaleList;
import android.speech.RecognizerIntent;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentActivity;

import com.jjlacode.freelanceproject.R;
import com.jjlacode.freelanceproject.util.JavaUtil;
import com.jjlacode.freelanceproject.util.android.controls.EditMaterial;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AndroidUtil extends AppCompatActivity {


    public static void reconocimientoVoz(FragmentActivity activity, int code){

        Intent intentActionRecognizeSpeech = new Intent(
                RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        // Configura el Lenguaje (Español-México)
        intentActionRecognizeSpeech.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
        intentActionRecognizeSpeech.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
        try {
            activity.startActivityForResult(intentActionRecognizeSpeech,
                    code);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(activity.getBaseContext(),
                    "Tú dispositivo no soporta el reconocimiento por voz",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public static String getSystemLocale() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return LocaleList.getDefault().get(0).getLanguage();
        } else {
            return Locale.getDefault().getLanguage();
        }
    }

    public static void ocultarTeclado(Context context, View v){

        // Ocultar teclado virtual
        InputMethodManager imm =
                (InputMethodManager) context.getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    public static void sinFoco(EditText editText){

        editText.setFocusable(false);
    }

    public static void foco(EditText editText){

        editText.requestFocus();
    }

    public static boolean validateEmail(CharSequence emailAddress) {
        return !TextUtils.isEmpty(emailAddress) && android.util.Patterns.EMAIL_ADDRESS.matcher(
                emailAddress).matches();
    }

    public static void notificacionSimple(Context contexto, int id, int iconId, int img,
                                          int color, String titulo, String contenido) {

        NotificationManager notifyMgr = (NotificationManager) contexto.getSystemService(NOTIFICATION_SERVICE);

        Notification.Builder builder =
                new Notification.Builder(contexto)
                        .setSmallIcon(iconId)
                        .setLargeIcon(BitmapFactory.decodeResource(contexto.getResources(),img))
                        .setContentTitle(titulo)
                        .setContentText(contenido)
                        .setColor(contexto.getResources().getColor(color));

        // Construir la notificación y emitirla
        notifyMgr.notify(id, builder.build());
    }

    public static void bars(Context contexto, ProgressBar bar, ProgressBar bar2, boolean inversa,
                            int valorMax, int valorAcept, int valorNotOk, double completada,
                            TextView lcompletada, TextView trek, int color_ok, int color_acept, int color_notok){

        if (bar2!=null) {
            if (completada > valorMax) {
                bar2.setVisibility(View.VISIBLE);
                bar2.setProgress((int) completada - valorMax - ((int) ((completada - valorMax) / 99)));
                bar2.setSecondaryProgress((int) completada - valorMax);
            } else {
                bar2.setVisibility(View.GONE);
            }
        }
        if (completada>0){
            bar.setVisibility(View.VISIBLE);
        }else{
            bar.setVisibility(View.GONE);
        }
        bar.setMax(valorMax);
        bar.setProgress((int)completada-((int)(completada/99)));
        bar.setSecondaryProgress((int)completada);

        if (inversa) {
            if (completada > valorAcept) {
                bar.setProgressDrawable(contexto.getResources().getDrawable(R.drawable.bar_ok, null));
                if (trek != null) {

                    trek.setTextColor(contexto.getResources().getColor(color_ok));

                }
            } else if (completada < valorAcept && completada > valorNotOk) {
                bar.setProgressDrawable(contexto.getResources().getDrawable(R.drawable.bar_acept, null));
                if (bar2 != null) {
                    bar2.setProgressDrawable(contexto.getResources().getDrawable(R.drawable.bar_acept, null));
                }
                if (trek != null) {

                    trek.setTextColor(contexto.getResources().getColor(color_acept));
                }
            } else {
                bar.setProgressDrawable(contexto.getResources().getDrawable(R.drawable.bar_notok, null));
                if (bar2 != null) {
                    bar2.setProgressDrawable(contexto.getResources().getDrawable(R.drawable.bar_notok, null));
                }
                if (trek != null) {

                    trek.setTextColor(contexto.getResources().getColor(color_notok));
                }
            }
        } else {
            if (completada < valorAcept) {
                bar.setProgressDrawable(contexto.getResources().getDrawable(R.drawable.bar_ok, null));
                if (trek != null) {

                    trek.setTextColor(contexto.getResources().getColor(color_ok));

                }
            } else if (completada > valorAcept && completada < valorNotOk) {
                bar.setProgressDrawable(contexto.getResources().getDrawable(R.drawable.bar_acept, null));
                if (bar2 != null) {
                    bar2.setProgressDrawable(contexto.getResources().getDrawable(R.drawable.bar_acept, null));
                }
                if (trek != null) {

                    trek.setTextColor(contexto.getResources().getColor(color_acept));
                }
            } else {
                bar.setProgressDrawable(contexto.getResources().getDrawable(R.drawable.bar_notok, null));
                if (bar2 != null) {
                    bar2.setProgressDrawable(contexto.getResources().getDrawable(R.drawable.bar_notok, null));
                }
                if (trek != null) {

                    trek.setTextColor(contexto.getResources().getColor(color_notok));
                }
            }
        }
        if (lcompletada!=null) {

            lcompletada.setText(String.format(Locale.getDefault(),
                    "%s %s", JavaUtil.getDecimales(completada), "% completa"));
        }
    }

    public static void bars(Context contexto, ProgressBar bar, ProgressBar bar2, boolean inversa,
                            int valorMax, int valorAcept, int valorNotOk, double completada,
                            EditMaterial lcompletada, TextView trek, int color_ok, int color_acept, int color_notok){

        if (bar2!=null) {
            if (completada > valorMax) {
                bar2.setVisibility(View.VISIBLE);
                bar2.setProgress((int) completada - valorMax - ((int) ((completada - valorMax) / 99)));
                bar2.setSecondaryProgress((int) completada - valorMax);
            } else {
                bar2.setVisibility(View.GONE);
            }
        }
        if (completada>0){
            bar.setVisibility(View.VISIBLE);
        }else{
            bar.setVisibility(View.GONE);
        }
        bar.setMax(valorMax);
        bar.setProgress((int)completada-((int)(completada/99)));
        bar.setSecondaryProgress((int)completada);

        if (inversa) {
            if (completada > valorAcept) {
                bar.setProgressDrawable(contexto.getResources().getDrawable(R.drawable.bar_ok, null));
                if (trek != null) {

                    trek.setTextColor(contexto.getResources().getColor(color_ok));

                }
            } else if (completada < valorAcept && completada > valorNotOk) {
                bar.setProgressDrawable(contexto.getResources().getDrawable(R.drawable.bar_acept, null));
                if (bar2 != null) {
                    bar2.setProgressDrawable(contexto.getResources().getDrawable(R.drawable.bar_acept, null));
                }
                if (trek != null) {

                    trek.setTextColor(contexto.getResources().getColor(color_acept));
                }
            } else {
                bar.setProgressDrawable(contexto.getResources().getDrawable(R.drawable.bar_notok, null));
                if (bar2 != null) {
                    bar2.setProgressDrawable(contexto.getResources().getDrawable(R.drawable.bar_notok, null));
                }
                if (trek != null) {

                    trek.setTextColor(contexto.getResources().getColor(color_notok));
                }
            }
        } else {
            if (completada < valorAcept) {
                bar.setProgressDrawable(contexto.getResources().getDrawable(R.drawable.bar_ok, null));
                if (trek != null) {

                    trek.setTextColor(contexto.getResources().getColor(color_ok));

                }
            } else if (completada > valorAcept && completada < valorNotOk) {
                bar.setProgressDrawable(contexto.getResources().getDrawable(R.drawable.bar_acept, null));
                if (bar2 != null) {
                    bar2.setProgressDrawable(contexto.getResources().getDrawable(R.drawable.bar_acept, null));
                }
                if (trek != null) {

                    trek.setTextColor(contexto.getResources().getColor(color_acept));
                }
            } else {
                bar.setProgressDrawable(contexto.getResources().getDrawable(R.drawable.bar_notok, null));
                if (bar2 != null) {
                    bar2.setProgressDrawable(contexto.getResources().getDrawable(R.drawable.bar_notok, null));
                }
                if (trek != null) {

                    trek.setTextColor(contexto.getResources().getColor(color_notok));
                }
            }
        }
        if (lcompletada!=null) {

            lcompletada.setText(String.format(Locale.getDefault(),
                    "%s %s", JavaUtil.getDecimales(completada), "% completa"));
        }
    }

    public static void barsCircCard(Context contexto, ProgressBar bar, ProgressBar bar2, boolean inversa,
                                    int valorMax, int valorAcept, int valorNotOk, double completada,
                                    TextView lcompletada, TextView trek, int color_ok, int color_acept, int color_notok,
                                CardView card, int color_ok_card, int color_acept_card, int color_notok_card){

        if (bar2!=null) {
            if (completada > valorMax) {
                bar2.setVisibility(View.VISIBLE);
                bar2.setProgress((int) completada - valorMax - ((int) ((completada - valorMax) / 99)));
                bar2.setSecondaryProgress((int) completada - valorMax);

            } else {
                bar2.setVisibility(View.GONE);
            }
        }
        if (completada>0){
            bar.setVisibility(View.VISIBLE);
        }else{
            bar.setVisibility(View.GONE);
        }
        bar.setMax(valorMax);
        bar.setProgress((int)completada-((int)(completada/99)));
        bar.setSecondaryProgress((int)completada);

        if (inversa) {
            if (completada > valorAcept) {
                bar.setProgressDrawable(contexto.getResources().getDrawable(R.drawable.bar_ok_circ, null));
                if (trek != null) {

                    trek.setTextColor(contexto.getResources().getColor(color_ok));
                }
                card.setCardBackgroundColor(contexto.getResources().getColor(color_ok_card));

            } else if (completada < valorAcept && completada > valorNotOk) {
                bar.setProgressDrawable(contexto.getResources().getDrawable(R.drawable.bar_acept_circ, null));
                if (bar2 != null) {
                    bar2.setProgressDrawable(contexto.getResources().getDrawable(R.drawable.bar_acept_circ, null));
                }
                if (trek != null) {

                    trek.setTextColor(contexto.getResources().getColor(color_acept));
                }
                card.setCardBackgroundColor(contexto.getResources().getColor(color_acept_card));

            } else {
                bar.setProgressDrawable(contexto.getResources().getDrawable(R.drawable.bar_notok_circ, null));
                if (bar2 != null) {
                    bar2.setProgressDrawable(contexto.getResources().getDrawable(R.drawable.bar_notok_circ, null));
                }
                if (trek != null) {

                    trek.setTextColor(contexto.getResources().getColor(color_notok));
                }
                card.setCardBackgroundColor(contexto.getResources().getColor(color_notok_card));

            }
        } else {
            if (completada < valorAcept) {
                bar.setProgressDrawable(contexto.getResources().getDrawable(R.drawable.bar_ok_circ, null));
                if (trek != null) {

                    trek.setTextColor(contexto.getResources().getColor(color_ok));
                }
                card.setCardBackgroundColor(contexto.getResources().getColor(color_ok_card));

            } else if (completada > valorAcept && completada < valorNotOk) {
                bar.setProgressDrawable(contexto.getResources().getDrawable(R.drawable.bar_acept_circ, null));
                if (bar2 != null) {
                    bar2.setProgressDrawable(contexto.getResources().getDrawable(R.drawable.bar_acept_circ, null));
                }
                if (trek != null) {

                    trek.setTextColor(contexto.getResources().getColor(color_acept));
                }
                card.setCardBackgroundColor(contexto.getResources().getColor(color_acept_card));

            } else {
                bar.setProgressDrawable(contexto.getResources().getDrawable(R.drawable.bar_notok_circ, null));
                if (bar2 != null) {
                    bar2.setProgressDrawable(contexto.getResources().getDrawable(R.drawable.bar_notok_circ, null));
                }
                if (trek != null) {

                    trek.setTextColor(contexto.getResources().getColor(color_notok));
                }
                card.setCardBackgroundColor(contexto.getResources().getColor(color_notok_card));

            }
        }
        if (lcompletada!=null) {

            lcompletada.setText(String.format(Locale.getDefault(),
                    "%s %s", JavaUtil.getDecimales(completada), "% completa"));
        }
    }

    public static void barsCirc(Context contexto, ProgressBar bar, ProgressBar bar2, boolean inversa, int valorMax, int valorAcept,
                            int valorNotOk, double completada, TextView lcompletada,
                            TextView trek, int color_ok, int color_acept, int color_notok){

        if (bar2!=null) {
            if (completada > valorMax) {
                bar2.setVisibility(View.VISIBLE);
                bar2.setProgress((int) completada - valorMax - ((int) ((completada - valorMax) / 99)));
                bar2.setSecondaryProgress((int) completada - valorMax);
            } else {
                bar2.setVisibility(View.GONE);
            }
        }
        if (completada>0){
            bar.setVisibility(View.VISIBLE);
        }else{
            bar.setVisibility(View.GONE);
        }
        bar.setMax(valorMax);
        bar.setProgress((int)completada-((int)(completada/99)));
        bar.setSecondaryProgress((int)completada);

        if (inversa) {
            if (completada > valorAcept) {
                bar.setProgressDrawable(contexto.getResources().getDrawable(R.drawable.bar_ok_circ, null));
                if (trek != null) {

                    trek.setTextColor(contexto.getResources().getColor(color_ok));
                }
            } else if (completada < valorAcept && completada > valorNotOk) {
                bar.setProgressDrawable(contexto.getResources().getDrawable(R.drawable.bar_acept_circ, null));
                if (bar2 != null) {
                    bar2.setProgressDrawable(contexto.getResources().getDrawable(R.drawable.bar_acept_circ, null));
                }
                if (trek != null) {

                    trek.setTextColor(contexto.getResources().getColor(color_acept));
                }
            } else {
                bar.setProgressDrawable(contexto.getResources().getDrawable(R.drawable.bar_notok_circ, null));
                if (bar2 != null) {
                    bar2.setProgressDrawable(contexto.getResources().getDrawable(R.drawable.bar_notok_circ, null));
                }
                if (trek != null) {

                    trek.setTextColor(contexto.getResources().getColor(color_notok));
                }
            }
        } else {
            if (completada < valorAcept) {
                bar.setProgressDrawable(contexto.getResources().getDrawable(R.drawable.bar_ok_circ, null));
                if (trek != null) {

                    trek.setTextColor(contexto.getResources().getColor(color_ok));
                }
            } else if (completada > valorAcept && completada < valorNotOk) {
                bar.setProgressDrawable(contexto.getResources().getDrawable(R.drawable.bar_acept_circ, null));
                if (bar2 != null) {
                    bar2.setProgressDrawable(contexto.getResources().getDrawable(R.drawable.bar_acept_circ, null));
                }
                if (trek != null) {

                    trek.setTextColor(contexto.getResources().getColor(color_acept));
                }
            } else {
                bar.setProgressDrawable(contexto.getResources().getDrawable(R.drawable.bar_notok_circ, null));
                if (bar2 != null) {
                    bar2.setProgressDrawable(contexto.getResources().getDrawable(R.drawable.bar_notok_circ, null));
                }
                if (trek != null) {

                    trek.setTextColor(contexto.getResources().getColor(color_notok));
                }
            }
        }
        if (lcompletada!=null) {

            lcompletada.setText(String.format(Locale.getDefault(),
                    "%s %s", JavaUtil.getDecimales(completada), "% completa"));
        }
    }

    public static void barsCard(Context contexto, ProgressBar bar, ProgressBar bar2, boolean inversa, int valorMax, int valorAcept,
                                int valorNotOk, double completada, TextView lcompletada,
                                TextView trek, int color_ok, int color_acept, int color_notok,
                                CardView card, int color_ok_card, int color_acept_card, int color_notok_card){

        if (bar2!=null) {
            if (completada > valorMax) {
                bar2.setVisibility(View.VISIBLE);
                bar2.setProgress((int) completada - valorMax - ((int) ((completada - valorMax) / 99)));
                bar2.setSecondaryProgress((int) completada - valorMax);

            } else {
                bar2.setVisibility(View.GONE);
            }
        }
        if (completada>0){
            bar.setVisibility(View.VISIBLE);
        }else{
            bar.setVisibility(View.GONE);
        }
        bar.setMax(valorMax);
        bar.setProgress((int)completada-((int)(completada/99)));
        bar.setSecondaryProgress((int)completada);

        if (inversa) {
            if (completada > valorAcept) {
                bar.setProgressDrawable(contexto.getResources().getDrawable(R.drawable.bar_ok, null));
                if (trek != null) {

                    trek.setTextColor(contexto.getResources().getColor(color_ok));
                }
                card.setCardBackgroundColor(contexto.getResources().getColor(color_ok_card));

            } else if (completada < valorAcept && completada > valorNotOk) {
                bar.setProgressDrawable(contexto.getResources().getDrawable(R.drawable.bar_acept, null));
                if (bar2 != null) {
                    bar2.setProgressDrawable(contexto.getResources().getDrawable(R.drawable.bar_acept, null));
                }
                if (trek != null) {

                    trek.setTextColor(contexto.getResources().getColor(color_acept));
                }
                card.setCardBackgroundColor(contexto.getResources().getColor(color_acept_card));

            } else {
                bar.setProgressDrawable(contexto.getResources().getDrawable(R.drawable.bar_notok, null));
                if (bar2 != null) {
                    bar2.setProgressDrawable(contexto.getResources().getDrawable(R.drawable.bar_notok, null));
                }
                if (trek != null) {

                    trek.setTextColor(contexto.getResources().getColor(color_notok));
                }
                card.setCardBackgroundColor(contexto.getResources().getColor(color_notok_card));

            }
        } else {
            if (completada < valorAcept) {
                bar.setProgressDrawable(contexto.getResources().getDrawable(R.drawable.bar_ok, null));
                if (trek != null) {

                    trek.setTextColor(contexto.getResources().getColor(color_ok));
                }
                card.setCardBackgroundColor(contexto.getResources().getColor(color_ok_card));

            } else if (completada > valorAcept && completada < valorNotOk) {
                bar.setProgressDrawable(contexto.getResources().getDrawable(R.drawable.bar_acept, null));
                if (bar2 != null) {
                    bar2.setProgressDrawable(contexto.getResources().getDrawable(R.drawable.bar_acept, null));
                }
                if (trek != null) {

                    trek.setTextColor(contexto.getResources().getColor(color_acept));
                }
                card.setCardBackgroundColor(contexto.getResources().getColor(color_acept_card));

            } else {
                bar.setProgressDrawable(contexto.getResources().getDrawable(R.drawable.bar_notok, null));
                if (bar2 != null) {
                    bar2.setProgressDrawable(contexto.getResources().getDrawable(R.drawable.bar_notok, null));
                }
                if (trek != null) {

                    trek.setTextColor(contexto.getResources().getColor(color_notok));
                }
                card.setCardBackgroundColor(contexto.getResources().getColor(color_notok_card));

            }
        }
        if (lcompletada!=null) {
            lcompletada.setText(String.format(Locale.getDefault(),
                    "%s %s", JavaUtil.getDecimales(completada), "% completa"));
        }
    }

    public static boolean validateCardNumber(String cardNumber) {

        if (TextUtils.isEmpty(cardNumber)) {
            return false;
        } else {
            cardNumber = cardNumber.replaceAll("\\D", "");
        }

        int len = cardNumber.length() - 1;
        if (len < 15) {
            return false;
        }
        int sum = 0;
        int value;
        StringBuilder sb = new StringBuilder(cardNumber.substring(0, len));
        for (int i = len - 1; i >= 0; i -= 2) {
            sb.replace(i, i + 1, String.valueOf((sb.charAt(i) - '0') * 2));
        }
        for (int i = 0; i < sb.length(); i++) {
            sum += sb.charAt(i) - '0';
        }
        if (sum % 10 == 0) {
            value = 0;
        } else {
            value = (sum / 10 + 1) * 10 - sum;
        }

        return value == cardNumber.charAt(len) - '0';
    }

    public static boolean validateCardExpireDate(String date) {
        if (date.length() == 5) {
            DateFormat df = new SimpleDateFormat("MM/yy", Locale.getDefault());
            try {
                Calendar checkDate = Calendar.getInstance();
                checkDate.setTime(df.parse(date));
                Calendar now = Calendar.getInstance();
                if (checkDate.get(Calendar.YEAR) < now.get(Calendar.YEAR)) {
                    return false;
                }
                if (checkDate.get(Calendar.YEAR) == now.get(Calendar.YEAR) && checkDate.get(Calendar.MONTH) >= now
                        .get(Calendar.MONTH)) {
                    return true;
                }
                if (checkDate.get(Calendar.YEAR) > now.get(Calendar.YEAR)) {
                    return true;
                }
                return false;
            } catch (ParseException e) {

            }
        }
        return false;
    }


    public static Uri getProviderUriFile(Context context, String path){

        return FileProvider.getUriForFile(context,"jjlacode.com.freelanceproject.provider",new File(path));
    }


    public static float getSizeTextProp(DisplayMetrics metrics) {

        float densidad = metrics.density;
        int ancho = (int) (metrics.widthPixels / densidad);
        int alto = (int) (metrics.heightPixels / densidad);
        int densidadDpi = (int) (metrics.densityDpi);

        return ((float) (ancho + alto + densidadDpi) / (100));
    }

    public static ColorStateList setColorStateListRatingBar(int colorActive, int colorDeactive) {

        if (colorActive == 0) {
            colorActive = R.color.colorPrimary;
        }
        if (colorDeactive == 0) {
            colorDeactive = R.color.colorPrimary;
        }

        ColorStateList stateList = new ColorStateList(
                new int[][]{
                        new int[]{android.R.attr.state_activated},
                        new int[]{-android.R.attr.state_activated},
                },
                new int[]{
                        colorActive,
                        colorDeactive
                }
        );

        return stateList;
    }
}
