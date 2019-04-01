package jjlacode.com.freelanceproject.sqlite;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;

import jjlacode.com.androidutils.JavaUtil;
import jjlacode.com.androidutils.Modelo;

import static jjlacode.com.androidutils.AppActivity.getAppContext;
import static jjlacode.com.freelanceproject.sqlite.Contract.*;

public class QueryDB implements JavaUtil.Constantes {

    private static ContentResolver resolver = getAppContext().getContentResolver();


    public static ArrayList<Modelo> queryList(String[] campos, String seleccion, String orden) {


        ArrayList<Modelo> list = new ArrayList<>();

        Cursor reg = resolver.query(obtenerUriContenido(
                campos[1]), null, seleccion, null, orden);


        if (reg != null) {

            while (reg.moveToNext()) {

                String[] insert = new String[reg.getColumnCount()-1];

                for (int i = 0, x = 2,y = 4; i < reg.getColumnCount()-1; i++,x+=3,y+=3) {

                    switch (campos[y]){

                        case STRING:
                            insert[i] = reg.getString(reg.getColumnIndex(campos[x]));
                            break;
                        case INT:
                            insert[i] = String.valueOf(reg.getInt(reg.getColumnIndex(campos[x])));
                            break;
                        case LONG:
                            insert[i] = String.valueOf(reg.getLong(reg.getColumnIndex(campos[x])));
                            break;
                        case DOUBLE:
                            insert[i] = String.valueOf(reg.getDouble(reg.getColumnIndex(campos[x])));
                            break;
                        case FLOAT:
                            insert[i] = String.valueOf(reg.getFloat(reg.getColumnIndex(campos[x])));
                            break;
                        case SHORT:
                            insert[i] = String.valueOf(reg.getShort(reg.getColumnIndex(campos[x])));
                            break;
                        default:

                            insert[i] = reg.getString(reg.getColumnIndex(campos[x]));

                    }

                }
                System.out.println("insert = " + insert[0]);


                if (insert[0]!=null) {
                    Modelo modelo = new Modelo(campos, insert);
                    list.add(modelo);
                }
            }
        }
        reg.close();

        return list;
    }

    public static ArrayList<Modelo> queryList(String[] campos) {


        ArrayList<Modelo> list = new ArrayList<>();

        Cursor reg = resolver.query(obtenerUriContenido(
                campos[1]), null, null, null, null);

        System.out.println("regcolumn = " + reg.getColumnCount());

        if (reg != null) {

            while (reg.moveToNext()) {

                String[] insert = new String[reg.getColumnCount()-1];

                for (int i = 0, x = 2,y = 4; i < reg.getColumnCount()-1; i++,x+=3,y+=3) {

                    switch (campos[y]){

                        case STRING:
                            insert[i] = reg.getString(reg.getColumnIndex(campos[x]));
                            break;
                        case INT:
                            insert[i] = String.valueOf(reg.getInt(reg.getColumnIndex(campos[x])));
                            break;
                        case LONG:
                            insert[i] = String.valueOf(reg.getLong(reg.getColumnIndex(campos[x])));
                            break;
                        case DOUBLE:
                            insert[i] = String.valueOf(reg.getDouble(reg.getColumnIndex(campos[x])));
                            break;
                        case FLOAT:
                            insert[i] = String.valueOf(reg.getFloat(reg.getColumnIndex(campos[x])));
                            break;
                        case SHORT:
                            insert[i] = String.valueOf(reg.getShort(reg.getColumnIndex(campos[x])));
                            break;
                        default:

                            insert[i] = reg.getString(reg.getColumnIndex(campos[x]));

                    }

                }
                System.out.println("insert = " + insert[0]);


                if (insert[0]!=null) {
                    Modelo modelo = new Modelo(campos, insert);
                    list.add(modelo);
                }
            }
        }
        reg.close();

        return list;
    }

    public static Modelo queryObject(String[] campos, String id) {


        Modelo modelo = null;

        Cursor reg = resolver.query(crearUriTabla(id,
                campos[1]), null, null, null, null);


        while (reg.moveToNext()) {

            String[] insert = new String[reg.getColumnCount()-1];

            for (int i = 0, x = 2,y = 4; i < reg.getColumnCount()-1; i++,x+=3,y+=3) {

                switch (campos[y]){

                    case STRING:
                        insert[i] = reg.getString(reg.getColumnIndex(campos[x]));
                        break;
                    case INT:
                        insert[i] = String.valueOf(reg.getInt(reg.getColumnIndex(campos[x])));
                        break;
                    case LONG:
                        insert[i] = String.valueOf(reg.getLong(reg.getColumnIndex(campos[x])));
                        break;
                    case DOUBLE:
                        insert[i] = String.valueOf(reg.getDouble(reg.getColumnIndex(campos[x])));
                        break;
                    case FLOAT:
                        insert[i] = String.valueOf(reg.getFloat(reg.getColumnIndex(campos[x])));
                        break;
                    case SHORT:
                        insert[i] = String.valueOf(reg.getShort(reg.getColumnIndex(campos[x])));
                        break;
                    default:

                        insert[i] = reg.getString(reg.getColumnIndex(campos[x]));

                }

            }

            if (insert[0]!=null) {
                modelo = new Modelo(campos, insert);
            }
        }
        reg.close();

        return modelo;
    }

    public static Modelo queryObject(String[] campos, Uri uri) {

        Modelo modelo = null;

        String id = obtenerIdTabla(uri);

        Cursor reg = resolver.query(crearUriTabla(id,
                campos[1]), null, null, null, null);


        while (reg.moveToNext()) {

            String[] insert = new String[reg.getColumnCount()-1];

            for (int i = 0, x = 2,y = 4; i < reg.getColumnCount()-1; i++,x+=3,y+=3) {

                switch (campos[y]){

                    case STRING:
                        insert[i] = reg.getString(reg.getColumnIndex(campos[x]));
                        break;
                    case INT:
                        insert[i] = String.valueOf(reg.getInt(reg.getColumnIndex(campos[x])));
                        break;
                    case LONG:
                        insert[i] = String.valueOf(reg.getLong(reg.getColumnIndex(campos[x])));
                        break;
                    case DOUBLE:
                        insert[i] = String.valueOf(reg.getDouble(reg.getColumnIndex(campos[x])));
                        break;
                    case FLOAT:
                        insert[i] = String.valueOf(reg.getFloat(reg.getColumnIndex(campos[x])));
                        break;
                    case SHORT:
                        insert[i] = String.valueOf(reg.getShort(reg.getColumnIndex(campos[x])));
                        break;
                    default:

                        insert[i] = reg.getString(reg.getColumnIndex(campos[x]));

                }

            }

            if (insert[0]!=null) {
                modelo = new Modelo(campos, insert);
            }
        }
        reg.close();

        return modelo;
    }

    public static ArrayList<Modelo> queryListCampo(String[] campos, String campo, String valor, String orden) {

        String seleccion = campo+" = '"+valor+"'";

        ArrayList<Modelo> list = new ArrayList<>();

        Cursor reg = resolver.query(obtenerUriContenido(
                campos[1]), null, seleccion, null, orden);

        while (reg.moveToNext()) {

            String[] insert = new String[reg.getColumnCount()-1];

            for (int i = 0, x = 2,y = 4; i < reg.getColumnCount()-1; i++,x+=3,y+=3) {

                switch (campos[y]){

                    case STRING:
                        insert[i] = reg.getString(reg.getColumnIndex(campos[x]));
                        break;
                    case INT:
                        insert[i] = String.valueOf(reg.getInt(reg.getColumnIndex(campos[x])));
                        break;
                    case LONG:
                        insert[i] = String.valueOf(reg.getLong(reg.getColumnIndex(campos[x])));
                        break;
                    case DOUBLE:
                        insert[i] = String.valueOf(reg.getDouble(reg.getColumnIndex(campos[x])));
                        break;
                    case FLOAT:
                        insert[i] = String.valueOf(reg.getFloat(reg.getColumnIndex(campos[x])));
                        break;
                    case SHORT:
                        insert[i] = String.valueOf(reg.getShort(reg.getColumnIndex(campos[x])));
                        break;
                    default:

                        insert[i] = reg.getString(reg.getColumnIndex(campos[x]));

                }

            }

            if (insert[0]!=null) {
                Modelo modelo = new Modelo(campos, insert);
                list.add(modelo);
            }
        }
        reg.close();

        return list;
    }

    public static ArrayList<Modelo> queryListDetalle(String[] campos, String id, String tablaCab, String seleccion, String orden) {

        ArrayList<Modelo> list = new ArrayList<>();

        Cursor reg = resolver.query(crearUriTablaDetalleId(id,
                campos[1], tablaCab),null, seleccion, null, orden);


        while (reg.moveToNext()) {

            String[] insert = new String[reg.getColumnCount()-1];

            for (int i = 0, x = 2,y = 4; i < reg.getColumnCount()-1; i++,x+=3,y+=3) {

                switch (campos[y]){

                    case STRING:
                        insert[i] = reg.getString(reg.getColumnIndex(campos[x]));
                        break;
                    case INT:
                        insert[i] = String.valueOf(reg.getInt(reg.getColumnIndex(campos[x])));
                        break;
                    case LONG:
                        insert[i] = String.valueOf(reg.getLong(reg.getColumnIndex(campos[x])));
                        break;
                    case DOUBLE:
                        insert[i] = String.valueOf(reg.getDouble(reg.getColumnIndex(campos[x])));
                        break;
                    case FLOAT:
                        insert[i] = String.valueOf(reg.getFloat(reg.getColumnIndex(campos[x])));
                        break;
                    case SHORT:
                        insert[i] = String.valueOf(reg.getShort(reg.getColumnIndex(campos[x])));
                        break;
                    default:

                        insert[i] = reg.getString(reg.getColumnIndex(campos[x]));

                }

            }
            System.out.println("insert = " + insert[0]);
            System.out.println("regcolumn = " + reg.getColumnCount());

            if (insert[0]!=null) {
                Modelo modelo = new Modelo(campos, insert);
                list.add(modelo);
            }
        }
        reg.close();

        return list;
    }

    public static ArrayList<Modelo> queryListDetalle(String[] campos, String id, String tablaCab) {

        ArrayList<Modelo> list = new ArrayList<>();

        Cursor reg = resolver.query(crearUriTablaDetalleId(id,
                campos[1], tablaCab),null, null, null, null);


        while (reg.moveToNext()) {

            String[] insert = new String[reg.getColumnCount()-1];

            for (int i = 0, x = 2,y = 4; i < reg.getColumnCount()-1; i++,x+=3,y+=3) {

                switch (campos[y]){

                    case STRING:
                        insert[i] = reg.getString(reg.getColumnIndex(campos[x]));
                        break;
                    case INT:
                        insert[i] = String.valueOf(reg.getInt(reg.getColumnIndex(campos[x])));
                        break;
                    case LONG:
                        insert[i] = String.valueOf(reg.getLong(reg.getColumnIndex(campos[x])));
                        break;
                    case DOUBLE:
                        insert[i] = String.valueOf(reg.getDouble(reg.getColumnIndex(campos[x])));
                        break;
                    case FLOAT:
                        insert[i] = String.valueOf(reg.getFloat(reg.getColumnIndex(campos[x])));
                        break;
                    case SHORT:
                        insert[i] = String.valueOf(reg.getShort(reg.getColumnIndex(campos[x])));
                        break;
                    default:

                        insert[i] = reg.getString(reg.getColumnIndex(campos[x]));

                }

            }

            if (insert[0]!=null) {
                Modelo modelo = new Modelo(campos, insert);
                list.add(modelo);
            }
        }
        reg.close();

        return list;
    }

    public static Modelo queryObjectDetalle(String[] campos, String id, String secuencia) {


        Modelo modelo = null;

        Cursor reg = resolver.query(crearUriTablaDetalle(id,secuencia,
                campos[1]), null, null, null, null);


        while (reg.moveToNext()) {

            String[] insert = new String[reg.getColumnCount()-1];

            for (int i = 0, x = 2,y = 4; i < reg.getColumnCount()-1; i++,x+=3,y+=3) {

                switch (campos[y]){

                    case STRING:
                        insert[i] = reg.getString(reg.getColumnIndex(campos[x]));
                        break;
                    case INT:
                        insert[i] = String.valueOf(reg.getInt(reg.getColumnIndex(campos[x])));
                        break;
                    case LONG:
                        insert[i] = String.valueOf(reg.getLong(reg.getColumnIndex(campos[x])));
                        break;
                    case DOUBLE:
                        insert[i] = String.valueOf(reg.getDouble(reg.getColumnIndex(campos[x])));
                        break;
                    case FLOAT:
                        insert[i] = String.valueOf(reg.getFloat(reg.getColumnIndex(campos[x])));
                        break;
                    case SHORT:
                        insert[i] = String.valueOf(reg.getShort(reg.getColumnIndex(campos[x])));
                        break;
                    default:

                        insert[i] = reg.getString(reg.getColumnIndex(campos[x]));

                }

            }
            System.out.println("insert = " + insert[0]);
            System.out.println("regcolumn = " + reg.getColumnCount());

            if (insert[0]!=null) {
                modelo = new Modelo(campos, insert);
            }
        }
        reg.close();

        return modelo;
    }

    public static Modelo queryObjectDetalle(String[] campos, Uri uri) {


        String[] ids = Contract.obtenerIdTablaDetalle(uri);

        Modelo modelo = null;

        Cursor reg = resolver.query(crearUriTablaDetalle(ids[0],ids[1],
                campos[1]), null, null, null, null);


        while (reg.moveToNext()) {

            String[] insert = new String[reg.getColumnCount()-1];

            for (int i = 0, x = 2,y = 4; i < reg.getColumnCount()-1; i++,x+=3,y+=3) {

                switch (campos[y]){

                    case STRING:
                        insert[i] = reg.getString(reg.getColumnIndex(campos[x]));
                        break;
                    case INT:
                        insert[i] = String.valueOf(reg.getInt(reg.getColumnIndex(campos[x])));
                        break;
                    case LONG:
                        insert[i] = String.valueOf(reg.getLong(reg.getColumnIndex(campos[x])));
                        break;
                    case DOUBLE:
                        insert[i] = String.valueOf(reg.getDouble(reg.getColumnIndex(campos[x])));
                        break;
                    case FLOAT:
                        insert[i] = String.valueOf(reg.getFloat(reg.getColumnIndex(campos[x])));
                        break;
                    case SHORT:
                        insert[i] = String.valueOf(reg.getShort(reg.getColumnIndex(campos[x])));
                        break;
                    default:

                        insert[i] = reg.getString(reg.getColumnIndex(campos[x]));

                }

            }
            System.out.println("insert = " + insert[0]);
            System.out.println("regcolumn = " + reg.getColumnCount());

            if (insert[0]!=null) {
                modelo = new Modelo(campos, insert);
            }
        }
        reg.close();

        return modelo;
    }

    public static ArrayList<Modelo> queryList
            (String[] campos, String campo, String valor, String valor2, int flag, String orden) {


        ArrayList<Modelo> list = new ArrayList<>();

        String seleccion = null;

        switch (flag){

            case IGUAL:
                seleccion = campo+" = '"+valor+"'";
                break;
            case DIFERENTE:
                seleccion = campo+" <> '"+valor+"'";
                break;
            case ENTRE:
                seleccion = campo+" BETWEEN '"+valor+"' AND '"+valor2+"'";
                break;
            case MAYOR:
                seleccion = campo+" > '"+valor+"'";
                break;
            case MAYORIGUAL:
                seleccion = campo+" >= '"+valor+"'";
                break;
            case MENOR:
                seleccion = campo+" < '"+valor+"'";
                break;
            case MENORIGUAL:
                seleccion = campo+" <= '"+valor+"'";

        }

        Cursor reg = resolver.query(obtenerUriContenido(
                campos[1]), null, seleccion, null, orden);


        if (reg != null) {

            while (reg.moveToNext()) {

                String[] insert = new String[reg.getColumnCount()-1];

                for (int i = 0, x = 2,y = 4; i < reg.getColumnCount()-1; i++,x+=3,y+=3) {

                    switch (campos[y]){

                        case STRING:
                            insert[i] = reg.getString(reg.getColumnIndex(campos[x]));
                            break;
                        case INT:
                            insert[i] = String.valueOf(reg.getInt(reg.getColumnIndex(campos[x])));
                            break;
                        case LONG:
                            insert[i] = String.valueOf(reg.getLong(reg.getColumnIndex(campos[x])));
                            break;
                        case DOUBLE:
                            insert[i] = String.valueOf(reg.getDouble(reg.getColumnIndex(campos[x])));
                            break;
                        case FLOAT:
                            insert[i] = String.valueOf(reg.getFloat(reg.getColumnIndex(campos[x])));
                            break;
                        case SHORT:
                            insert[i] = String.valueOf(reg.getShort(reg.getColumnIndex(campos[x])));
                            break;
                        default:

                            insert[i] = reg.getString(reg.getColumnIndex(campos[x]));

                    }

                }
                System.out.println("insert = " + insert[0]);
                System.out.println("regcolumn = " + reg.getColumnCount());

                if (insert[0]!=null) {
                    Modelo modelo = new Modelo(campos, insert);
                    list.add(modelo);
                }
            }
        }
        reg.close();

        return list;
    }

    public static ArrayList<Modelo> queryList
            (String[] campos, String campo, int valor, int valor2, int flag, String orden) {


        ArrayList<Modelo> list = new ArrayList<>();

        String seleccion = null;

        switch (flag){

            case IGUAL:
                seleccion = campo+" = '"+valor+"'";
                break;
            case DIFERENTE:
                seleccion = campo+" <> '"+valor+"'";
                break;
            case ENTRE:
                seleccion = campo+" BETWEEN '"+valor+"' AND '"+valor2+"'";
                break;
            case MAYOR:
                seleccion = campo+" > '"+valor+"'";
                break;
            case MAYORIGUAL:
                seleccion = campo+" >= '"+valor+"'";
                break;
            case MENOR:
                seleccion = campo+" < '"+valor+"'";
                break;
            case MENORIGUAL:
                seleccion = campo+" <= '"+valor+"'";

        }

        Cursor reg = resolver.query(obtenerUriContenido(
                campos[1]), null, seleccion, null, orden);


        if (reg != null) {

            while (reg.moveToNext()) {

                String[] insert = new String[reg.getColumnCount()-1];

                for (int i = 0, x = 2,y = 4; i < reg.getColumnCount()-1; i++,x+=3,y+=3) {

                    switch (campos[y]){

                        case STRING:
                            insert[i] = reg.getString(reg.getColumnIndex(campos[x]));
                            break;
                        case INT:
                            insert[i] = String.valueOf(reg.getInt(reg.getColumnIndex(campos[x])));
                            break;
                        case LONG:
                            insert[i] = String.valueOf(reg.getLong(reg.getColumnIndex(campos[x])));
                            break;
                        case DOUBLE:
                            insert[i] = String.valueOf(reg.getDouble(reg.getColumnIndex(campos[x])));
                            break;
                        case FLOAT:
                            insert[i] = String.valueOf(reg.getFloat(reg.getColumnIndex(campos[x])));
                            break;
                        case SHORT:
                            insert[i] = String.valueOf(reg.getShort(reg.getColumnIndex(campos[x])));
                            break;
                        default:

                            insert[i] = reg.getString(reg.getColumnIndex(campos[x]));

                    }

                }
                System.out.println("insert = " + insert[0]);
                System.out.println("regcolumn = " + reg.getColumnCount());

                if (insert[0]!=null) {
                    Modelo modelo = new Modelo(campos, insert);
                    list.add(modelo);
                }
            }
        }
        reg.close();

        return list;
    }

    public static ArrayList<Modelo> queryList
            (String[] campos, String campo, double valor, double valor2, int flag, String orden) {


        ArrayList<Modelo> list = new ArrayList<>();

        String seleccion = null;

        switch (flag){

            case IGUAL:
                seleccion = campo+" = '"+valor+"'";
                break;
            case DIFERENTE:
                seleccion = campo+" <> '"+valor+"'";
                break;
            case ENTRE:
                seleccion = campo+" BETWEEN '"+valor+"' AND '"+valor2+"'";
                break;
            case MAYOR:
                seleccion = campo+" > '"+valor+"'";
                break;
            case MAYORIGUAL:
                seleccion = campo+" >= '"+valor+"'";
                break;
            case MENOR:
                seleccion = campo+" < '"+valor+"'";
                break;
            case MENORIGUAL:
                seleccion = campo+" <= '"+valor+"'";

        }

        Cursor reg = resolver.query(obtenerUriContenido(
                campos[1]), null, seleccion, null, orden);


        if (reg != null) {

            while (reg.moveToNext()) {

                String[] insert = new String[reg.getColumnCount()-1];

                for (int i = 0, x = 2,y = 4; i < reg.getColumnCount()-1; i++,x+=3,y+=3) {

                    switch (campos[y]){

                        case STRING:
                            insert[i] = reg.getString(reg.getColumnIndex(campos[x]));
                            break;
                        case INT:
                            insert[i] = String.valueOf(reg.getInt(reg.getColumnIndex(campos[x])));
                            break;
                        case LONG:
                            insert[i] = String.valueOf(reg.getLong(reg.getColumnIndex(campos[x])));
                            break;
                        case DOUBLE:
                            insert[i] = String.valueOf(reg.getDouble(reg.getColumnIndex(campos[x])));
                            break;
                        case FLOAT:
                            insert[i] = String.valueOf(reg.getFloat(reg.getColumnIndex(campos[x])));
                            break;
                        case SHORT:
                            insert[i] = String.valueOf(reg.getShort(reg.getColumnIndex(campos[x])));
                            break;
                        default:

                            insert[i] = reg.getString(reg.getColumnIndex(campos[x]));

                    }

                }
                System.out.println("insert = " + insert[0]);
                System.out.println("regcolumn = " + reg.getColumnCount());

                if (insert[0]!=null) {
                    Modelo modelo = new Modelo(campos, insert);
                    list.add(modelo);
                }
            }
        }
        reg.close();

        return list;
    }

    public static void putDato(ContentValues valores,String[] campos, String campo, String valor){

        for (int i = 0; i < campos.length; i++) {

            if (campos[i].equals(campo)){

                switch (campos[i+2]){

                    case STRING:
                        valores.put(campo,valor);
                        break;
                    case INT:
                        valores.put(campo, Integer.parseInt(valor));
                        break;
                    case LONG:
                        valores.put(campo, Long.parseLong(valor));
                        break;
                    case DOUBLE:
                        valores.put(campo, Double.parseDouble(valor));
                        break;
                    case FLOAT:
                        valores.put(campo,Float.parseFloat(valor));
                        break;
                    case SHORT:
                        valores.put(campo,Short.parseShort(valor));
                        break;
                        default:
                            valores.put(campo,valor);
                }
            }
        }
    }

    public static ArrayList<Modelo> queryList
            (String[] campos, String campo, long valor, long valor2, int flag, String orden) {


        ArrayList<Modelo> list = new ArrayList<>();

        String seleccion = null;

        switch (flag){

            case IGUAL:
                seleccion = campo+" = '"+valor+"'";
                break;
            case DIFERENTE:
                seleccion = campo+" <> '"+valor+"'";
                break;
            case ENTRE:
                seleccion = campo+" BETWEEN '"+valor+"' AND '"+valor2+"'";
                break;
            case MAYOR:
                seleccion = campo+" > '"+valor+"'";
                break;
            case MAYORIGUAL:
                seleccion = campo+" >= '"+valor+"'";
                break;
            case MENOR:
                seleccion = campo+" < '"+valor+"'";
                break;
            case MENORIGUAL:
                seleccion = campo+" <= '"+valor+"'";

        }

        Cursor reg = resolver.query(obtenerUriContenido(
                campos[1]), null, seleccion, null, orden);


        if (reg != null) {

            while (reg.moveToNext()) {

                String[] insert = new String[reg.getColumnCount()-1];

                for (int i = 0, x = 2,y = 4; i < reg.getColumnCount()-1; i++,x+=3,y+=3) {

                    switch (campos[y]){

                        case STRING:
                            insert[i] = reg.getString(reg.getColumnIndex(campos[x]));
                            break;
                        case INT:
                            insert[i] = String.valueOf(reg.getInt(reg.getColumnIndex(campos[x])));
                            break;
                        case LONG:
                            insert[i] = String.valueOf(reg.getLong(reg.getColumnIndex(campos[x])));
                            break;
                        case DOUBLE:
                            insert[i] = String.valueOf(reg.getDouble(reg.getColumnIndex(campos[x])));
                            break;
                        case FLOAT:
                            insert[i] = String.valueOf(reg.getFloat(reg.getColumnIndex(campos[x])));
                            break;
                        case SHORT:
                            insert[i] = String.valueOf(reg.getShort(reg.getColumnIndex(campos[x])));
                            break;
                        default:

                            insert[i] = reg.getString(reg.getColumnIndex(campos[x]));

                    }

                }
                System.out.println("insert = " + insert[0]);
                System.out.println("regcolumn = " + reg.getColumnCount());

                if (insert[0]!=null) {
                    Modelo modelo = new Modelo(campos, insert);
                    list.add(modelo);
                }
            }
        }
        reg.close();

        return list;
    }

    public static ArrayList<Modelo> queryList
            (String[] campos, String campo, float valor, float valor2, int flag, String orden) {


        ArrayList<Modelo> list = new ArrayList<>();

        String seleccion = null;

        switch (flag){

            case IGUAL:
                seleccion = campo+" = '"+valor+"'";
                break;
            case DIFERENTE:
                seleccion = campo+" <> '"+valor+"'";
                break;
            case ENTRE:
                seleccion = campo+" BETWEEN '"+valor+"' AND '"+valor2+"'";
                break;
            case MAYOR:
                seleccion = campo+" > '"+valor+"'";
                break;
            case MAYORIGUAL:
                seleccion = campo+" >= '"+valor+"'";
                break;
            case MENOR:
                seleccion = campo+" < '"+valor+"'";
                break;
            case MENORIGUAL:
                seleccion = campo+" <= '"+valor+"'";

        }

        Cursor reg = resolver.query(obtenerUriContenido(
                campos[1]), null, seleccion, null, orden);


        if (reg != null) {

            while (reg.moveToNext()) {

                String[] insert = new String[reg.getColumnCount()-1];

                for (int i = 0, x = 2,y = 4; i < reg.getColumnCount()-1; i++,x+=3,y+=3) {

                    switch (campos[y]){

                        case STRING:
                            insert[i] = reg.getString(reg.getColumnIndex(campos[x]));
                            break;
                        case INT:
                            insert[i] = String.valueOf(reg.getInt(reg.getColumnIndex(campos[x])));
                            break;
                        case LONG:
                            insert[i] = String.valueOf(reg.getLong(reg.getColumnIndex(campos[x])));
                            break;
                        case DOUBLE:
                            insert[i] = String.valueOf(reg.getDouble(reg.getColumnIndex(campos[x])));
                            break;
                        case FLOAT:
                            insert[i] = String.valueOf(reg.getFloat(reg.getColumnIndex(campos[x])));
                            break;
                        case SHORT:
                            insert[i] = String.valueOf(reg.getShort(reg.getColumnIndex(campos[x])));
                            break;
                        default:

                            insert[i] = reg.getString(reg.getColumnIndex(campos[x]));

                    }

                }
                System.out.println("insert = " + insert[0]);
                System.out.println("regcolumn = " + reg.getColumnCount());

                if (insert[0]!=null) {
                    Modelo modelo = new Modelo(campos, insert);
                    list.add(modelo);
                }
            }
        }
        reg.close();

        return list;
    }

    public static ArrayList<Modelo> queryList
            (String[] campos, String campo, short valor, short valor2, int flag, String orden) {


        ArrayList<Modelo> list = new ArrayList<>();

        String seleccion = null;

        switch (flag){

            case IGUAL:
                seleccion = campo+" = '"+valor+"'";
                break;
            case DIFERENTE:
                seleccion = campo+" <> '"+valor+"'";
                break;
            case ENTRE:
                seleccion = campo+" BETWEEN '"+valor+"' AND '"+valor2+"'";
                break;
            case MAYOR:
                seleccion = campo+" > '"+valor+"'";
                break;
            case MAYORIGUAL:
                seleccion = campo+" >= '"+valor+"'";
                break;
            case MENOR:
                seleccion = campo+" < '"+valor+"'";
                break;
            case MENORIGUAL:
                seleccion = campo+" <= '"+valor+"'";

        }

        Cursor reg = resolver.query(obtenerUriContenido(
                campos[1]), null, seleccion, null, orden);


        if (reg != null) {

            while (reg.moveToNext()) {

                String[] insert = new String[reg.getColumnCount()-1];

                for (int i = 0, x = 2,y = 4; i < reg.getColumnCount()-1; i++,x+=3,y+=3) {

                    switch (campos[y]){

                        case STRING:
                            insert[i] = reg.getString(reg.getColumnIndex(campos[x]));
                            break;
                        case INT:
                            insert[i] = String.valueOf(reg.getInt(reg.getColumnIndex(campos[x])));
                            break;
                        case LONG:
                            insert[i] = String.valueOf(reg.getLong(reg.getColumnIndex(campos[x])));
                            break;
                        case DOUBLE:
                            insert[i] = String.valueOf(reg.getDouble(reg.getColumnIndex(campos[x])));
                            break;
                        case FLOAT:
                            insert[i] = String.valueOf(reg.getFloat(reg.getColumnIndex(campos[x])));
                            break;
                        case SHORT:
                            insert[i] = String.valueOf(reg.getShort(reg.getColumnIndex(campos[x])));
                            break;
                        default:

                            insert[i] = reg.getString(reg.getColumnIndex(campos[x]));

                    }

                }
                System.out.println("insert = " + insert[0]);
                System.out.println("regcolumn = " + reg.getColumnCount());

                if (insert[0]!=null) {
                    Modelo modelo = new Modelo(campos, insert);
                    list.add(modelo);
                }
            }
        }
        reg.close();

        return list;
    }

    public static ArrayList<Modelo> queryList
            (String[] campos, String seleccion, String campoOrden, int flagOrden) {


        ArrayList<Modelo> list = new ArrayList<>();

        String orden = null;

        switch (flagOrden){

            case ASCENDENTE:
                orden = campoOrden+" ASC";
                break;
            case DESCENDENTE:
                orden = campoOrden+" DESC";

        }

        Cursor reg = resolver.query(obtenerUriContenido(
                campos[1]), null, seleccion, null, orden);


        if (reg != null) {

            while (reg.moveToNext()) {

                String[] insert = new String[reg.getColumnCount()-1];

                for (int i = 0, x = 2,y = 4; i < reg.getColumnCount()-1; i++,x+=3,y+=3) {

                    switch (campos[y]){

                        case STRING:
                            insert[i] = reg.getString(reg.getColumnIndex(campos[x]));
                            break;
                        case INT:
                            insert[i] = String.valueOf(reg.getInt(reg.getColumnIndex(campos[x])));
                            break;
                        case LONG:
                            insert[i] = String.valueOf(reg.getLong(reg.getColumnIndex(campos[x])));
                            break;
                        case DOUBLE:
                            insert[i] = String.valueOf(reg.getDouble(reg.getColumnIndex(campos[x])));
                            break;
                        case FLOAT:
                            insert[i] = String.valueOf(reg.getFloat(reg.getColumnIndex(campos[x])));
                            break;
                        case SHORT:
                            insert[i] = String.valueOf(reg.getShort(reg.getColumnIndex(campos[x])));
                            break;
                        default:

                            insert[i] = reg.getString(reg.getColumnIndex(campos[x]));

                    }

                }
                System.out.println("insert = " + insert[0]);
                System.out.println("regcolumn = " + reg.getColumnCount());

                if (insert[0]!=null) {
                    Modelo modelo = new Modelo(campos, insert);
                    list.add(modelo);
                }
            }
        }
        reg.close();

        return list;
    }

    public static void putDato(ContentValues valores,String[] campos, String campo, int valor){

        for (int i = 0; i < campos.length; i++) {

            if (campos[i].equals(campo)){

                switch (campos[i+2]){

                    case STRING:
                        valores.put(campo,String.valueOf(valor));
                        break;
                    case INT:
                        valores.put(campo, valor);
                        break;
                    case LONG:
                        valores.put(campo, Long.parseLong(String.valueOf(valor)));
                        break;
                    case DOUBLE:
                        valores.put(campo, Double.parseDouble(String.valueOf(valor)));
                        break;
                    case FLOAT:
                        valores.put(campo,Float.parseFloat(String.valueOf(valor)));
                        break;
                    case SHORT:
                        valores.put(campo,Short.parseShort(String.valueOf(valor)));
                        break;
                    default:
                        valores.put(campo,valor);
                }
            }
        }
    }

    public static void putDato(ContentValues valores,String[] campos, String campo, long valor){

        for (int i = 0; i < campos.length; i++) {

            if (campos[i].equals(campo)){

                switch (campos[i+2]){

                    case STRING:
                        valores.put(campo,String.valueOf(valor));
                        break;
                    case INT:
                        valores.put(campo, Integer.parseInt(String.valueOf(valor)));
                        break;
                    case LONG:
                        valores.put(campo, valor);
                        break;
                    case DOUBLE:
                        valores.put(campo, Double.parseDouble(String.valueOf(valor)));
                        break;
                    case FLOAT:
                        valores.put(campo,Float.parseFloat(String.valueOf(valor)));
                        break;
                    case SHORT:
                        valores.put(campo,Short.parseShort(String.valueOf(valor)));
                        break;
                    default:
                        valores.put(campo,valor);
                }
            }
        }
    }

    public static void putDato(ContentValues valores,String[] campos, String campo, double valor){

        for (int i = 0; i < campos.length; i++) {

            if (campos[i].equals(campo)){

                switch (campos[i+2]){

                    case STRING:
                        valores.put(campo,String.valueOf(valor));
                        break;
                    case INT:
                        valores.put(campo, Integer.parseInt(String.valueOf(valor)));
                        break;
                    case LONG:
                        valores.put(campo, Long.parseLong(String.valueOf(valor)));
                        break;
                    case DOUBLE:
                        valores.put(campo, valor);
                        break;
                    case FLOAT:
                        valores.put(campo,Float.parseFloat(String.valueOf(valor)));
                        break;
                    case SHORT:
                        valores.put(campo,Short.parseShort(String.valueOf(valor)));
                        break;
                    default:
                        valores.put(campo,valor);
                }
            }
        }
    }

    public static void putDato(ContentValues valores,String[] campos, String campo, float valor){

        for (int i = 0; i < campos.length; i++) {

            if (campos[i].equals(campo)){

                switch (campos[i+2]){

                    case STRING:
                        valores.put(campo,String.valueOf(valor));
                        break;
                    case INT:
                        valores.put(campo, Integer.parseInt(String.valueOf(valor)));
                        break;
                    case LONG:
                        valores.put(campo, Long.parseLong(String.valueOf(valor)));
                        break;
                    case DOUBLE:
                        valores.put(campo, Double.parseDouble(String.valueOf(valor)));
                        break;
                    case FLOAT:
                        valores.put(campo, valor);
                        break;
                    case SHORT:
                        valores.put(campo,Short.parseShort(String.valueOf(valor)));
                        break;
                    default:
                        valores.put(campo,valor);
                }
            }
        }
    }

    public static void putDato(ContentValues valores,String[] campos, String campo, short valor){

        for (int i = 0; i < campos.length; i++) {

            if (campos[i].equals(campo)){

                switch (campos[i+2]){

                    case STRING:
                        valores.put(campo,String.valueOf(valor));
                        break;
                    case INT:
                        valores.put(campo, Integer.parseInt(String.valueOf(valor)));
                        break;
                    case LONG:
                        valores.put(campo, Long.parseLong(String.valueOf(valor)));
                        break;
                    case DOUBLE:
                        valores.put(campo, Double.parseDouble(String.valueOf(valor)));
                        break;
                    case FLOAT:
                        valores.put(campo,Float.parseFloat(String.valueOf(valor)));
                        break;
                    case SHORT:
                        valores.put(campo, valor);
                        break;
                    default:
                        valores.put(campo,valor);
                }
            }
        }
    }

    public static int updateRegistro(String tabla,String id,ContentValues valores){

        return resolver.update(Contract.crearUriTabla(id, tabla)
                , valores, null, null);

    }

    public static int updateRegistro(Uri uri,ContentValues valores){

        return resolver.update(uri, valores, null, null);

    }

    public static int updateRegistro(Uri uri,ContentValues valores, String seleccion){

        return resolver.update(uri, valores, seleccion, null);

    }

    public static int updateRegistroDetalle(String tabla,String id, String secuencia,ContentValues valores){

        return resolver.update(Contract.crearUriTablaDetalle(id,secuencia, tabla)
                , valores, null, null);

    }

    public static int updateRegistroDetalle(String tabla,String id, int secuencia,ContentValues valores){

        return resolver.update(Contract.crearUriTablaDetalle(id,secuencia, tabla)
                , valores, null, null);

    }

    public static int updateRegistrosDetalle(String tabla,String id, String tablaCab, ContentValues valores,String seleccion){

        return resolver.update(Contract.crearUriTablaDetalleId(id,tabla,tablaCab)
                , valores, seleccion, null);

    }

    public static int updateRegistros(String tabla,ContentValues valores,String seleccion){

        return resolver.update(Contract.obtenerUriContenido(tabla)
                , valores, seleccion, null);

    }

    public static int updateRegistros
            (String tabla,ContentValues valores,String campo, String valor, String valor2,  int flag){

        String seleccion = null;

        switch (flag){

            case IGUAL:
                seleccion = campo+" = '"+valor+"'";
                break;
            case DIFERENTE:
                seleccion = campo+" <> '"+valor+"'";
                break;
            case ENTRE:
                seleccion = campo+" BETWEEN '"+valor+"' AND '"+valor2+"'";
                break;
            case MAYOR:
                seleccion = campo+" > '"+valor+"'";
                break;
            case MAYORIGUAL:
                seleccion = campo+" >= '"+valor+"'";
                break;
            case MENOR:
                seleccion = campo+" < '"+valor+"'";
                break;
            case MENORIGUAL:
                seleccion = campo+" <= '"+valor+"'";

        }
        return resolver.update(Contract.obtenerUriContenido(tabla)
                , valores, seleccion, null);

    }

    public static int updateRegistros
            (String tabla,ContentValues valores,String campo, int valor, int valor2,  int flag){

        String seleccion = null;

        switch (flag){

            case IGUAL:
                seleccion = campo+" = '"+valor+"'";
                break;
            case DIFERENTE:
                seleccion = campo+" <> '"+valor+"'";
                break;
            case ENTRE:
                seleccion = campo+" BETWEEN '"+valor+"' AND '"+valor2+"'";
                break;
            case MAYOR:
                seleccion = campo+" > '"+valor+"'";
                break;
            case MAYORIGUAL:
                seleccion = campo+" >= '"+valor+"'";
                break;
            case MENOR:
                seleccion = campo+" < '"+valor+"'";
                break;
            case MENORIGUAL:
                seleccion = campo+" <= '"+valor+"'";

        }
        return resolver.update(Contract.obtenerUriContenido(tabla)
                , valores, seleccion, null);

    }

    public static int updateRegistros
            (String tabla,ContentValues valores,String campo, long valor, long valor2,  int flag){

        String seleccion = null;

        switch (flag){

            case IGUAL:
                seleccion = campo+" = '"+valor+"'";
                break;
            case DIFERENTE:
                seleccion = campo+" <> '"+valor+"'";
                break;
            case ENTRE:
                seleccion = campo+" BETWEEN '"+valor+"' AND '"+valor2+"'";
                break;
            case MAYOR:
                seleccion = campo+" > '"+valor+"'";
                break;
            case MAYORIGUAL:
                seleccion = campo+" >= '"+valor+"'";
                break;
            case MENOR:
                seleccion = campo+" < '"+valor+"'";
                break;
            case MENORIGUAL:
                seleccion = campo+" <= '"+valor+"'";

        }
        return resolver.update(Contract.obtenerUriContenido(tabla)
                , valores, seleccion, null);

    }

    public static int updateRegistros
            (String tabla,ContentValues valores,String campo, double valor, double valor2,  int flag){

        String seleccion = null;

        switch (flag){

            case IGUAL:
                seleccion = campo+" = '"+valor+"'";
                break;
            case DIFERENTE:
                seleccion = campo+" <> '"+valor+"'";
                break;
            case ENTRE:
                seleccion = campo+" BETWEEN '"+valor+"' AND '"+valor2+"'";
                break;
            case MAYOR:
                seleccion = campo+" > '"+valor+"'";
                break;
            case MAYORIGUAL:
                seleccion = campo+" >= '"+valor+"'";
                break;
            case MENOR:
                seleccion = campo+" < '"+valor+"'";
                break;
            case MENORIGUAL:
                seleccion = campo+" <= '"+valor+"'";

        }
        return resolver.update(Contract.obtenerUriContenido(tabla)
                , valores, seleccion, null);

    }

    public static int updateRegistros
            (String tabla,ContentValues valores,String campo, float valor, float valor2,  int flag){

        String seleccion = null;

        switch (flag){

            case IGUAL:
                seleccion = campo+" = '"+valor+"'";
                break;
            case DIFERENTE:
                seleccion = campo+" <> '"+valor+"'";
                break;
            case ENTRE:
                seleccion = campo+" BETWEEN '"+valor+"' AND '"+valor2+"'";
                break;
            case MAYOR:
                seleccion = campo+" > '"+valor+"'";
                break;
            case MAYORIGUAL:
                seleccion = campo+" >= '"+valor+"'";
                break;
            case MENOR:
                seleccion = campo+" < '"+valor+"'";
                break;
            case MENORIGUAL:
                seleccion = campo+" <= '"+valor+"'";

        }
        return resolver.update(Contract.obtenerUriContenido(tabla)
                , valores, seleccion, null);

    }

    public static int updateRegistros
            (String tabla,ContentValues valores,String campo, short valor, short valor2,  int flag){

        String seleccion = null;

        switch (flag){

            case IGUAL:
                seleccion = campo+" = '"+valor+"'";
                break;
            case DIFERENTE:
                seleccion = campo+" <> '"+valor+"'";
                break;
            case ENTRE:
                seleccion = campo+" BETWEEN '"+valor+"' AND '"+valor2+"'";
                break;
            case MAYOR:
                seleccion = campo+" > '"+valor+"'";
                break;
            case MAYORIGUAL:
                seleccion = campo+" >= '"+valor+"'";
                break;
            case MENOR:
                seleccion = campo+" < '"+valor+"'";
                break;
            case MENORIGUAL:
                seleccion = campo+" <= '"+valor+"'";

        }
        return resolver.update(Contract.obtenerUriContenido(tabla)
                , valores, seleccion, null);

    }

    public static int deleteRegistro(String tabla,String id){

        return resolver.delete(Contract.crearUriTabla(id, tabla)
                , null, null);

    }

    public static int deleteRegistrosDetalle(String tabla,String id){

        return resolver.delete(Contract.crearUriTabla(id, tabla)
                , null, null);

    }

    public static int deleteRegistroDetalle(String tabla,String id, int secuencia){

        return resolver.delete(Contract.crearUriTablaDetalle(id,secuencia, tabla)
                , null, null);

    }

    public static int deleteRegistroDetalle(String tabla,String id, String secuencia){

        return resolver.delete(Contract.crearUriTablaDetalle(id,secuencia, tabla)
                , null, null);

    }

    public static int deteteRegistros(String tabla,String seleccion){

        return resolver.delete(Contract.obtenerUriContenido(tabla)
                ,  seleccion, null);

    }

    public static int deleteRegistros
            (String tabla,String campo, String valor, String valor2,  int flag){

        String seleccion = null;

        switch (flag){

            case IGUAL:
                seleccion = campo+" = '"+valor+"'";
                break;
            case DIFERENTE:
                seleccion = campo+" <> '"+valor+"'";
                break;
            case ENTRE:
                seleccion = campo+" BETWEEN '"+valor+"' AND '"+valor2+"'";
                break;
            case MAYOR:
                seleccion = campo+" > '"+valor+"'";
                break;
            case MAYORIGUAL:
                seleccion = campo+" >= '"+valor+"'";
                break;
            case MENOR:
                seleccion = campo+" < '"+valor+"'";
                break;
            case MENORIGUAL:
                seleccion = campo+" <= '"+valor+"'";

        }
        return resolver.delete(Contract.obtenerUriContenido(tabla)
                , seleccion, null);

    }

    public static Uri insertRegistro(String tabla,ContentValues valores){

        return resolver.insert(Contract.obtenerUriContenido(tabla), valores);

    }

    public static String idInsertRegistro(String tabla,ContentValues valores){

        Uri uri = resolver.insert(Contract.obtenerUriContenido(tabla), valores);

        return Contract.obtenerIdTabla(uri);

    }

    public static Uri insertRegistroDetalle(String[] campos, String id, String tablaCab, ContentValues valores){

        ArrayList<Modelo> lista = QueryDB.queryListDetalle(campos,id,tablaCab,null,null);

        int secuencia = 0;

        if (lista!=null && lista.size()>0) {
            secuencia = lista.size() + 1;
        }else{
            secuencia = 1;
        }
        System.out.println("secuencia = " + secuencia);

        QueryDB.putDato(valores,campos,"secuencia",secuencia);

        return resolver.insert(Contract.crearUriTablaDetalle(id,secuencia,campos[1]), valores);

    }

}   