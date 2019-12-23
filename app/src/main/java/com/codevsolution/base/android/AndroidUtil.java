package com.codevsolution.base.android;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
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

import com.codevsolution.base.android.controls.EditMaterial;
import com.codevsolution.base.javautil.JavaUtil;
import com.codevsolution.base.style.Estilos;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static com.codevsolution.base.javautil.JavaUtil.Constantes.NULL;
import static com.codevsolution.base.logica.InteractorBase.Constantes.USERID;
import static com.codevsolution.base.logica.InteractorBase.Constantes.USERIDCODE;

public class AndroidUtil extends AppCompatActivity {


    public static void reconocimientoVoz(FragmentActivity activity, int code) {

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

    public static boolean isPackageExisted(Context context, String targetPackage) {
        List<ApplicationInfo> packages;
        PackageManager pm;

        pm = context.getPackageManager();
        packages = pm.getInstalledApplications(0);
        for (ApplicationInfo packageInfo : packages) {
            if (packageInfo.packageName.equals(targetPackage))
                return true;
        }
        return false;
    }

    public static String getSystemLocale() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return LocaleList.getDefault().get(0).getLanguage();
        } else {
            return Locale.getDefault().getLanguage();
        }
    }

    public static String getMetodo() {
        return Thread.currentThread().getStackTrace()[3].getMethodName();
    }

    public static boolean nn(Object object) {
        return object != null;
    }

    public static boolean nnn(String string) {
        return string != null && !string.equals(NULL);
    }

    public static void ocultarTeclado(Context context, View v) {

        // Ocultar teclado virtual
        InputMethodManager imm =
                (InputMethodManager) context.getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    public static int getId(Context context, String nombre, String tipo) {
        return context.getResources()
                .getIdentifier(nombre, tipo, context.getPackageName());
    }

    public static void sinFoco(EditText editText) {

        editText.setFocusable(false);
    }

    public static void foco(EditText editText) {

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
                        .setLargeIcon(BitmapFactory.decodeResource(contexto.getResources(), img))
                        .setContentTitle(titulo)
                        .setContentText(contenido)
                        .setColor(contexto.getResources().getColor(color));

        // Construir la notificación y emitirla
        notifyMgr.notify(id, builder.build());
    }

    public static void bars(Context contexto, ProgressBar bar, ProgressBar bar2, boolean inversa,
                            int valorMax, int valorAcept, int valorNotOk, double completada,
                            TextView lcompletada, TextView trek, int color_ok, int color_acept, int color_notok) {

        if (bar2 != null) {
            if (completada > valorMax) {
                bar2.setVisibility(View.VISIBLE);
                bar2.setProgress((int) completada - valorMax - ((int) ((completada - valorMax) / 99)));
                bar2.setSecondaryProgress((int) completada - valorMax);
            } else {
                bar2.setVisibility(View.GONE);
            }
        }
        if (completada > 0) {
            bar.setVisibility(View.VISIBLE);
        } else {
            bar.setVisibility(View.GONE);
        }
        bar.setMax(valorMax);
        bar.setProgress((int) completada - ((int) (completada / 99)));
        bar.setSecondaryProgress((int) completada);

        if (inversa) {
            if (completada > valorAcept) {
                bar.setProgressDrawable(Estilos.getDrawable(contexto, "bar_ok"));
                if (trek != null) {

                    trek.setTextColor(contexto.getResources().getColor(color_ok));

                }
            } else if (completada < valorAcept && completada > valorNotOk) {
                bar.setProgressDrawable(Estilos.getDrawable(contexto, "bar_acept"));
                if (bar2 != null) {
                    bar2.setProgressDrawable(Estilos.getDrawable(contexto, "bar_acept"));
                }
                if (trek != null) {

                    trek.setTextColor(contexto.getResources().getColor(color_acept));
                }
            } else {
                bar.setProgressDrawable(Estilos.getDrawable(contexto, "bar_notok"));
                if (bar2 != null) {
                    bar2.setProgressDrawable(Estilos.getDrawable(contexto, "bar_notok"));
                }
                if (trek != null) {

                    trek.setTextColor(contexto.getResources().getColor(color_notok));
                }
            }
        } else {
            if (completada < valorAcept) {
                bar.setProgressDrawable(Estilos.getDrawable(contexto, "bar_ok"));
                if (trek != null) {

                    trek.setTextColor(contexto.getResources().getColor(color_ok));

                }
            } else if (completada > valorAcept && completada < valorNotOk) {
                bar.setProgressDrawable(Estilos.getDrawable(contexto, "bar_acept"));
                if (bar2 != null) {
                    bar2.setProgressDrawable(Estilos.getDrawable(contexto, "bar_acept"));
                }
                if (trek != null) {

                    trek.setTextColor(contexto.getResources().getColor(color_acept));
                }
            } else {
                bar.setProgressDrawable(Estilos.getDrawable(contexto, "bar_notok"));
                if (bar2 != null) {
                    bar2.setProgressDrawable(Estilos.getDrawable(contexto, "bar_notok"));
                }
                if (trek != null) {

                    trek.setTextColor(contexto.getResources().getColor(color_notok));
                }
            }
        }
        if (lcompletada != null) {

            lcompletada.setText(String.format(Locale.getDefault(),
                    "%s %s", JavaUtil.getDecimales(completada), "% completa"));
        }
    }

    public static void bars(Context contexto, ProgressBar bar, ProgressBar bar2, boolean inversa,
                            int valorMax, int valorAcept, int valorNotOk, double completada,
                            EditMaterial lcompletada, TextView trek, int color_ok, int color_acept, int color_notok) {

        if (bar2 != null) {
            if (completada > valorMax) {
                bar2.setVisibility(View.VISIBLE);
                bar2.setProgress((int) completada - valorMax - ((int) ((completada - valorMax) / 99)));
                bar2.setSecondaryProgress((int) completada - valorMax);
            } else {
                bar2.setVisibility(View.GONE);
            }
        }
        if (completada > 0) {
            bar.setVisibility(View.VISIBLE);
        } else {
            bar.setVisibility(View.GONE);
        }
        bar.setMax(valorMax);
        bar.setProgress((int) completada - ((int) (completada / 99)));
        bar.setSecondaryProgress((int) completada);

        if (inversa) {
            if (completada > valorAcept) {
                bar.setProgressDrawable(Estilos.getDrawable(contexto, "bar_ok"));
                if (trek != null) {

                    trek.setTextColor(contexto.getResources().getColor(color_ok));

                }
            } else if (completada < valorAcept && completada > valorNotOk) {
                bar.setProgressDrawable(Estilos.getDrawable(contexto, "bar_acept"));
                if (bar2 != null) {
                    bar2.setProgressDrawable(Estilos.getDrawable(contexto, "bar_acept"));
                }
                if (trek != null) {

                    trek.setTextColor(contexto.getResources().getColor(color_acept));
                }
            } else {
                bar.setProgressDrawable(Estilos.getDrawable(contexto, "bar_notok"));
                if (bar2 != null) {
                    bar2.setProgressDrawable(Estilos.getDrawable(contexto, "bar_notok"));
                }
                if (trek != null) {

                    trek.setTextColor(contexto.getResources().getColor(color_notok));
                }
            }
        } else {
            if (completada < valorAcept) {
                bar.setProgressDrawable(Estilos.getDrawable(contexto, "bar_ok"));
                if (trek != null) {

                    trek.setTextColor(contexto.getResources().getColor(color_ok));

                }
            } else if (completada > valorAcept && completada < valorNotOk) {
                bar.setProgressDrawable(Estilos.getDrawable(contexto, "bar_acept"));
                if (bar2 != null) {
                    bar2.setProgressDrawable(Estilos.getDrawable(contexto, "bar_acept"));
                }
                if (trek != null) {

                    trek.setTextColor(contexto.getResources().getColor(color_acept));
                }
            } else {
                bar.setProgressDrawable(Estilos.getDrawable(contexto, "bar_notok"));
                if (bar2 != null) {
                    bar2.setProgressDrawable(Estilos.getDrawable(contexto, "bar_notok"));
                }
                if (trek != null) {

                    trek.setTextColor(contexto.getResources().getColor(color_notok));
                }
            }
        }
        if (lcompletada != null) {

            lcompletada.setText(String.format(Locale.getDefault(),
                    "%s %s", JavaUtil.getDecimales(completada), "% completa"));
        }
    }

    public static void barsCircCard(Context contexto, ProgressBar bar, ProgressBar bar2, boolean inversa,
                                    int valorMax, int valorAcept, int valorNotOk, double completada,
                                    TextView lcompletada, TextView trek, int color_ok, int color_acept, int color_notok,
                                    CardView card, int color_ok_card, int color_acept_card, int color_notok_card) {

        if (bar2 != null) {
            if (completada > valorMax) {
                bar2.setVisibility(View.VISIBLE);
                bar2.setProgress((int) completada - valorMax - ((int) ((completada - valorMax) / 99)));
                bar2.setSecondaryProgress((int) completada - valorMax);

            } else {
                bar2.setVisibility(View.GONE);
            }
        }
        if (completada > 0) {
            bar.setVisibility(View.VISIBLE);
        } else {
            bar.setVisibility(View.GONE);
        }
        bar.setMax(valorMax);
        bar.setProgress((int) completada - ((int) (completada / 99)));
        bar.setSecondaryProgress((int) completada);

        if (inversa) {
            if (completada > valorAcept) {
                bar.setProgressDrawable(Estilos.getDrawable(contexto, "bar_ok_circ"));
                if (trek != null) {

                    trek.setTextColor(contexto.getResources().getColor(color_ok));
                }
                card.setCardBackgroundColor(contexto.getResources().getColor(color_ok_card));

            } else if (completada < valorAcept && completada > valorNotOk) {
                bar.setProgressDrawable(Estilos.getDrawable(contexto, "bar_acept_circ"));
                if (bar2 != null) {
                    bar2.setProgressDrawable(Estilos.getDrawable(contexto, "bar_acept_circ"));
                }
                if (trek != null) {

                    trek.setTextColor(contexto.getResources().getColor(color_acept));
                }
                card.setCardBackgroundColor(contexto.getResources().getColor(color_acept_card));

            } else {
                bar.setProgressDrawable(Estilos.getDrawable(contexto, "bar_notok_circ"));
                if (bar2 != null) {
                    bar2.setProgressDrawable(Estilos.getDrawable(contexto, "bar_notok_circ"));
                }
                if (trek != null) {

                    trek.setTextColor(contexto.getResources().getColor(color_notok));
                }
                card.setCardBackgroundColor(contexto.getResources().getColor(color_notok_card));

            }
        } else {
            if (completada < valorAcept) {
                bar.setProgressDrawable(Estilos.getDrawable(contexto, "bar_ok_circ"));
                if (trek != null) {

                    trek.setTextColor(contexto.getResources().getColor(color_ok));
                }
                card.setCardBackgroundColor(contexto.getResources().getColor(color_ok_card));

            } else if (completada > valorAcept && completada < valorNotOk) {
                bar.setProgressDrawable(Estilos.getDrawable(contexto, "bar_acept_circ"));
                if (bar2 != null) {
                    bar2.setProgressDrawable(Estilos.getDrawable(contexto, "bar_acept_circ"));
                }
                if (trek != null) {

                    trek.setTextColor(contexto.getResources().getColor(color_acept));
                }
                card.setCardBackgroundColor(contexto.getResources().getColor(color_acept_card));

            } else {
                bar.setProgressDrawable(Estilos.getDrawable(contexto, "bar_notok_circ"));
                if (bar2 != null) {
                    bar2.setProgressDrawable(Estilos.getDrawable(contexto, "bar_notok_circ"));
                }
                if (trek != null) {

                    trek.setTextColor(contexto.getResources().getColor(color_notok));
                }
                card.setCardBackgroundColor(contexto.getResources().getColor(color_notok_card));

            }
        }
        if (lcompletada != null) {

            lcompletada.setText(String.format(Locale.getDefault(),
                    "%s %s", JavaUtil.getDecimales(completada), "% completa"));
        }
    }

    public static void barsCirc(Context contexto, ProgressBar bar, ProgressBar bar2, boolean inversa, int valorMax, int valorAcept,
                                int valorNotOk, double completada, TextView lcompletada,
                                TextView trek, int color_ok, int color_acept, int color_notok) {

        if (bar2 != null) {
            if (completada > valorMax) {
                bar2.setVisibility(View.VISIBLE);
                bar2.setProgress((int) completada - valorMax - ((int) ((completada - valorMax) / 99)));
                bar2.setSecondaryProgress((int) completada - valorMax);
            } else {
                bar2.setVisibility(View.GONE);
            }
        }
        if (completada > 0) {
            bar.setVisibility(View.VISIBLE);
        } else {
            bar.setVisibility(View.GONE);
        }
        bar.setMax(valorMax);
        bar.setProgress((int) completada - ((int) (completada / 99)));
        bar.setSecondaryProgress((int) completada);

        if (inversa) {
            if (completada > valorAcept) {
                bar.setProgressDrawable(Estilos.getDrawable(contexto, "bar_ok_circ"));
                if (trek != null) {

                    trek.setTextColor(contexto.getResources().getColor(color_ok));
                }
            } else if (completada < valorAcept && completada > valorNotOk) {
                bar.setProgressDrawable(Estilos.getDrawable(contexto, "bar_acept_circ"));
                if (bar2 != null) {
                    bar2.setProgressDrawable(Estilos.getDrawable(contexto, "bar_acept_circ"));
                }
                if (trek != null) {

                    trek.setTextColor(contexto.getResources().getColor(color_acept));
                }
            } else {
                bar.setProgressDrawable(Estilos.getDrawable(contexto, "bar_notok_circ"));
                if (bar2 != null) {
                    bar2.setProgressDrawable(Estilos.getDrawable(contexto, "bar_notok_circ"));
                }
                if (trek != null) {

                    trek.setTextColor(contexto.getResources().getColor(color_notok));
                }
            }
        } else {
            if (completada < valorAcept) {
                bar.setProgressDrawable(Estilos.getDrawable(contexto, "bar_ok_circ"));
                if (trek != null) {

                    trek.setTextColor(contexto.getResources().getColor(color_ok));
                }
            } else if (completada > valorAcept && completada < valorNotOk) {
                bar.setProgressDrawable(Estilos.getDrawable(contexto, "bar_acept_circ"));
                if (bar2 != null) {
                    bar2.setProgressDrawable(Estilos.getDrawable(contexto, "bar_acept_circ"));
                }
                if (trek != null) {

                    trek.setTextColor(contexto.getResources().getColor(color_acept));
                }
            } else {
                bar.setProgressDrawable(Estilos.getDrawable(contexto, "bar_notok_circ"));
                if (bar2 != null) {
                    bar2.setProgressDrawable(Estilos.getDrawable(contexto, "bar_notok_circ"));
                }
                if (trek != null) {

                    trek.setTextColor(contexto.getResources().getColor(color_notok));
                }
            }
        }
        if (lcompletada != null) {

            lcompletada.setText(String.format(Locale.getDefault(),
                    "%s %s", JavaUtil.getDecimales(completada), "% completa"));
        }
    }

    public static void barsCard(Context contexto, ProgressBar bar, ProgressBar bar2, boolean inversa, int valorMax, int valorAcept,
                                int valorNotOk, double completada, TextView lcompletada,
                                TextView trek, int color_ok, int color_acept, int color_notok,
                                CardView card, int color_ok_card, int color_acept_card, int color_notok_card) {

        if (bar2 != null) {
            if (completada > valorMax) {
                bar2.setVisibility(View.VISIBLE);
                bar2.setProgress((int) completada - valorMax - ((int) ((completada - valorMax) / 99)));
                bar2.setSecondaryProgress((int) completada - valorMax);

            } else {
                bar2.setVisibility(View.GONE);
            }
        }
        if (completada > 0) {
            bar.setVisibility(View.VISIBLE);
        } else {
            bar.setVisibility(View.GONE);
        }
        bar.setMax(valorMax);
        bar.setProgress((int) completada - ((int) (completada / 99)));
        bar.setSecondaryProgress((int) completada);

        if (inversa) {
            if (completada > valorAcept) {
                bar.setProgressDrawable(Estilos.getDrawable(contexto, "bar_ok"));
                if (trek != null) {

                    trek.setTextColor(contexto.getResources().getColor(color_ok));
                }
                card.setCardBackgroundColor(contexto.getResources().getColor(color_ok_card));

            } else if (completada < valorAcept && completada > valorNotOk) {
                bar.setProgressDrawable(Estilos.getDrawable(contexto, "bar_acept"));
                if (bar2 != null) {
                    bar2.setProgressDrawable(Estilos.getDrawable(contexto, "bar_acept"));
                }
                if (trek != null) {

                    trek.setTextColor(contexto.getResources().getColor(color_acept));
                }
                card.setCardBackgroundColor(contexto.getResources().getColor(color_acept_card));

            } else {
                bar.setProgressDrawable(Estilos.getDrawable(contexto, "bar_notok"));
                if (bar2 != null) {
                    bar2.setProgressDrawable(Estilos.getDrawable(contexto, "bar_notok"));
                }
                if (trek != null) {

                    trek.setTextColor(contexto.getResources().getColor(color_notok));
                }
                card.setCardBackgroundColor(contexto.getResources().getColor(color_notok_card));

            }
        } else {
            if (completada < valorAcept) {
                bar.setProgressDrawable(Estilos.getDrawable(contexto, "bar_ok"));
                if (trek != null) {

                    trek.setTextColor(contexto.getResources().getColor(color_ok));
                }
                card.setCardBackgroundColor(contexto.getResources().getColor(color_ok_card));

            } else if (completada > valorAcept && completada < valorNotOk) {
                bar.setProgressDrawable(Estilos.getDrawable(contexto, "bar_acept"));
                if (bar2 != null) {
                    bar2.setProgressDrawable(Estilos.getDrawable(contexto, "bar_acept"));
                }
                if (trek != null) {

                    trek.setTextColor(contexto.getResources().getColor(color_acept));
                }
                card.setCardBackgroundColor(contexto.getResources().getColor(color_acept_card));

            } else {
                bar.setProgressDrawable(Estilos.getDrawable(contexto, "bar_notok"));
                if (bar2 != null) {
                    bar2.setProgressDrawable(Estilos.getDrawable(contexto, "bar_notok"));
                }
                if (trek != null) {

                    trek.setTextColor(contexto.getResources().getColor(color_notok));
                }
                card.setCardBackgroundColor(contexto.getResources().getColor(color_notok_card));

            }
        }
        if (lcompletada != null) {
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
                return checkDate.get(Calendar.YEAR) > now.get(Calendar.YEAR);
            } catch (ParseException e) {

            }
        }
        return false;
    }


    public static Uri getProviderUriFile(Context context, String path) {

        return FileProvider.getUriForFile(context, "jjlacode.com.freelanceproject.provider", new File(path));
    }


    public static float getSizeTextProp(DisplayMetrics metrics) {

        float densidad = metrics.density;
        int ancho = (int) (metrics.widthPixels / densidad);
        int alto = (int) (metrics.heightPixels / densidad);
        int densidadDpi = metrics.densityDpi;

        return ((float) (ancho + alto + densidadDpi) / (100));
    }

    public static ColorStateList setColorStateListRatingBar(int colorActive, int colorDeactive) {

        if (colorActive == 0) {
            colorActive = Estilos.colorPrimary;
        }
        if (colorDeactive == 0) {
            colorDeactive = Estilos.colorPrimary;
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

    public static SharedPreferences openSharePreference(Context contexto, String sharePreference) {

        String idUser = AndroidUtil.getSharePreferenceMaster(contexto, USERID, USERIDCODE, NULL);
        if (!sharePreference.equals(USERID)) {
            sharePreference += idUser;
        }
        return contexto.getSharedPreferences(sharePreference, MODE_PRIVATE);
    }

    public static void setSharePreference(Context contexto, String sharePreference, String key, String valor) {

        String idUser = AndroidUtil.getSharePreferenceMaster(contexto, USERID, USERIDCODE, NULL);
        if (!sharePreference.equals(USERID)) {
            sharePreference += idUser;
        }
        SharedPreferences sP = contexto.getSharedPreferences(sharePreference, MODE_PRIVATE);
        SharedPreferences.Editor editor = sP.edit();
        editor.putString(key, valor);
        editor.apply();

    }

    public static void setSharePreferenceMaster(Context contexto, String sharePreference, String key, String valor) {

        SharedPreferences sP = contexto.getSharedPreferences(sharePreference, MODE_PRIVATE);
        SharedPreferences.Editor editor = sP.edit();
        editor.putString(key, valor);
        editor.apply();

    }



    public static void setSharePreference(Context contexto, String sharePreference, String key, int valor) {

        String idUser = AndroidUtil.getSharePreferenceMaster(contexto, USERID, USERIDCODE, NULL);
        if (!sharePreference.equals(USERID)) {
            sharePreference += idUser;
        }
        SharedPreferences sP = contexto.getSharedPreferences(sharePreference, MODE_PRIVATE);
        SharedPreferences.Editor editor = sP.edit();
        editor.putInt(key, valor);
        editor.apply();

    }

    public static void setSharePreference(Context contexto, String sharePreference, String key, long valor) {

        String idUser = AndroidUtil.getSharePreferenceMaster(contexto, USERID, USERIDCODE, NULL);
        if (!sharePreference.equals(USERID)) {
            sharePreference += idUser;
        }
        SharedPreferences sP = contexto.getSharedPreferences(sharePreference, MODE_PRIVATE);
        SharedPreferences.Editor editor = sP.edit();
        editor.putLong(key, valor);
        editor.apply();

    }

    public static void setSharePreference(Context contexto, String sharePreference, String key, boolean valor) {

        String idUser = AndroidUtil.getSharePreferenceMaster(contexto, USERID, USERIDCODE, NULL);
        if (!sharePreference.equals(USERID)) {
            sharePreference += idUser;
        }
        SharedPreferences sP = contexto.getSharedPreferences(sharePreference, MODE_PRIVATE);
        SharedPreferences.Editor editor = sP.edit();
        editor.putBoolean(key, valor);
        editor.apply();

    }

    public static void setSharePreference(Context contexto, String sharePreference, String key, float valor) {

        String idUser = AndroidUtil.getSharePreferenceMaster(contexto, USERID, USERIDCODE, NULL);
        if (!sharePreference.equals(USERID)) {
            sharePreference += idUser;
        }
        SharedPreferences sP = contexto.getSharedPreferences(sharePreference, MODE_PRIVATE);
        SharedPreferences.Editor editor = sP.edit();
        editor.putFloat(key, valor);
        editor.apply();

    }

    public static String getSharePreference(Context contexto, String sharePreference, String key, String defecto) {

        try {
            String idUser = AndroidUtil.getSharePreferenceMaster(contexto, USERID, USERIDCODE, NULL);
            if (!sharePreference.equals(USERID)) {
                sharePreference += idUser;
            }
            SharedPreferences sP = contexto.getSharedPreferences(sharePreference, MODE_PRIVATE);
            return sP.getString(key, defecto);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public static String getSharePreferenceMaster(Context contexto, String sharePreference, String key, String defecto) {

        try {
            SharedPreferences sP = contexto.getSharedPreferences(sharePreference, MODE_PRIVATE);
            return sP.getString(key, defecto);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public static int getSharePreference(Context contexto, String sharePreference, String key, int defecto) {

        String idUser = AndroidUtil.getSharePreferenceMaster(contexto, USERID, USERIDCODE, NULL);
        if (!sharePreference.equals(USERID)) {
            sharePreference += idUser;
        }
        SharedPreferences sP = contexto.getSharedPreferences(sharePreference, MODE_PRIVATE);

        return sP.getInt(key, defecto);

    }

    public static long getSharePreference(Context contexto, String sharePreference, String key, long defecto) {

        String idUser = AndroidUtil.getSharePreferenceMaster(contexto, USERID, USERIDCODE, NULL);
        if (!sharePreference.equals(USERID)) {
            sharePreference += idUser;
        }
        SharedPreferences sP = contexto.getSharedPreferences(sharePreference, MODE_PRIVATE);

        return sP.getLong(key, defecto);

    }

    public static boolean getSharePreference(Context contexto, String sharePreference, String key, boolean defecto) {

        String idUser = AndroidUtil.getSharePreferenceMaster(contexto, USERID, USERIDCODE, NULL);
        if (!sharePreference.equals(USERID)) {
            sharePreference += idUser;
        }
        SharedPreferences sP = contexto.getSharedPreferences(sharePreference, MODE_PRIVATE);

        return sP.getBoolean(key, defecto);

    }

    public static float getSharePreference(Context contexto, String sharePreference, String key, float defecto) {

        String idUser = AndroidUtil.getSharePreferenceMaster(contexto, USERID, USERIDCODE, NULL);
        if (!sharePreference.equals(USERID)) {
            sharePreference += idUser;
        }
        SharedPreferences sP = contexto.getSharedPreferences(sharePreference, MODE_PRIVATE);

        return sP.getFloat(key, defecto);

    }
}
