package jjlacode.com.freelanceproject.util.android;

public class copy {

    /*

    //TAREAS ASYNTASK CON DIFERENTES PARAMETROS------------------------------------------------

    public static class Tareafechas extends AsyncTask<Void, Float, Integer> {

            @Override
            protected Integer doInBackground(Void... voids) {

                recalcularFechas();
                return null;
            }

            protected void onPreExecute() {
            }

            protected void onProgressUpdate(Float... valores) {
            }

            protected void onPostExecute(Integer bytes) {
            }
        }

        public static class TareaTipoCliente extends AsyncTask<String, Float, Integer> {

            @Override
            protected Integer doInBackground(String... strings) {

                calcularTipoCliente(strings[0]);
                return null;
            }

            protected void onPreExecute() {
            }

            protected void onProgressUpdate(Float... valores) {
            }

            protected void onPostExecute(Integer bytes) {
            }
        }



    }

    //APPACTIVITY ----------------------------------------------------------------------------

    public static class AppActivity extends Application {

        private static Context context;

        public void onCreate() {
            super.onCreate();
            AppActivity.context = getApplicationContext();
        }

        public static Context getAppContext() {
            return AppActivity.context;
        }

        public static void hacerLlamada(Context context, String phoneNo){

            if(!TextUtils.isEmpty(phoneNo)) {
                String dial = "tel:" + phoneNo;
                context.startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse(dial)));
            }else {
                Toast.makeText(context, "El numero no es valido", Toast.LENGTH_SHORT).show();
            }
        }




    }

    //APPFRAGMENTS------------------------------------------------------------------------------

    public static class AppFragments extends Fragment {

        private static Context context;



        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            AppFragments.context = getContext();

            if (getArguments() != null) {
            }
        }

        public static Context getAppContext() {
            return AppFragments.context;
        }

        public static void cambiarFragment(Fragment myFragment, int layout){

            Fragment fragment = new Fragment();

            FragmentManager fragmentManager = fragment.getFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(layout,myFragment);
            transaction.commit();

        }
    }

    //ADAPTADOR RV BASE-----------------------------------------------------------------------

    public class Adaptador extends RecyclerView.Adapter<Adaptador.ProyectoViewHolder>
        implements View.OnClickListener, Contract.Tablas {


    private ArrayList<Modelo> listaProyecto;
    private String actual;

    private View.OnClickListener listener;

    public Adaptador(ArrayList<Modelo> listaProyecto, String actual) {
        this.listaProyecto = listaProyecto;
        this.actual = actual;
    }

    @NonNull
    @Override
    public ProyectoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_proyecto,null,false);

        view.setOnClickListener(this);

        return new ProyectoViewHolder(view);
    }

    public void setOnClickListener(View.OnClickListener listener) {

        this.listener = listener;
    }

    @Override
    public void onBindViewHolder(@NonNull Adaptador.ProyectoViewHolder proyectoViewHolder, int position) {


        proyectoViewHolder.nombreProyecto.setText(listaProyecto.get(position).getCampos(PROYECTO_NOMBRE));
        proyectoViewHolder.descripcionProyecto.setText(listaProyecto.get(position).getCampos(PROYECTO_DESCRIPCION));
        proyectoViewHolder.clienteProyecto.setText(listaProyecto.get(position).getCampos(PROYECTO_CLIENTE_NOMBRE));
        proyectoViewHolder.estadoProyecto.setText(listaProyecto.get(position).getCampos(PROYECTO_DESCRIPCION_ESTADO));
        if (actual.equals(PROYECTO)){

            proyectoViewHolder.progressBarProyecto.setProgress(Integer.parseInt(listaProyecto.get(position).getCampos(PROYECTO_TOTCOMPLETADO)));

            long retraso = Long.parseLong(listaProyecto.get(position).getCampos(PROYECTO_RETRASO));
            if (retraso > 3 * Common.DIASLONG){proyectoViewHolder.imagenEstado.setImageResource(R.drawable.alert_box_r);}
            else if (retraso > Common.DIASLONG){proyectoViewHolder.imagenEstado.setImageResource(R.drawable.alert_box_a);}
            else {proyectoViewHolder.imagenEstado.setImageResource(R.drawable.alert_box_v);}

        }else{

            proyectoViewHolder.progressBarProyecto.setVisibility(View.GONE);
            proyectoViewHolder.imagenEstado.setVisibility(View.GONE);

        }

        if (listaProyecto.get(position).getCampos(PROYECTO_RUTAFOTO)!=null) {
            proyectoViewHolder.imagenProyecto.setImageURI(Uri.parse(listaProyecto.get(position).getCampos(PROYECTO_RUTAFOTO)));
        }
        int peso = Integer.parseInt(listaProyecto.get(position).getCampos(PROYECTO_CLIENTE_PESOTIPOCLI));

        if (peso>6){proyectoViewHolder.imagenCliente.setImageResource(R.drawable.clientev);}
        else if (peso>3){proyectoViewHolder.imagenCliente.setImageResource(R.drawable.clientea);}
        else if (peso>0){proyectoViewHolder.imagenCliente.setImageResource(R.drawable.clienter);}
        else {proyectoViewHolder.imagenCliente.setImageResource(R.drawable.cliente);}



    }

    @Override
    public int getItemCount() {
        if (listaProyecto==null){
            return 0;
        }
        return listaProyecto.sizeLista();
    }

    @Override
    public void onClick(View v) {

        if (listener!= null){

            listener.onClick(v);


        }

    }

    public class ProyectoViewHolder extends RecyclerView.ViewHolderAdapter {

        ImageView imagenProyecto, imagenEstado, imagenCliente;
        TextView nombreProyecto,descripcionProyecto,clienteProyecto, estadoProyecto;
        ProgressBar progressBarProyecto;

        public ProyectoViewHolder(@NonNull View itemView) {
            super(itemView);

            imagenProyecto = itemView.findViewById(R.id.imglistaproyectos);
            imagenCliente = itemView.findViewById(R.id.imgclientelistaproyectos);
            imagenEstado = itemView.findViewById(R.id.imgestadolistaproyectos);
            nombreProyecto = itemView.findViewById(R.id.tvnombrelistaproyectos);
            descripcionProyecto = itemView.findViewById(R.id.tvdesclistaproyectos);
            clienteProyecto = itemView.findViewById(R.id.tvnombreclientelistaproyectos);
            estadoProyecto = itemView.findViewById(R.id.tvestadolistaproyectos);
            progressBarProyecto = itemView.findViewById(R.id.progressBarlistaproyectos);

        }
    }
}
    //CONTRACT BASE ---------------------------------------------------------------------------

    public class Contract implements Utilidades.Constantes {

    public static final String AUTORIDAD_CONTENIDO =
            "jjlacode.com.proyecto";

    public static final Uri URI_BASE = Uri.parse("content://" + AUTORIDAD_CONTENIDO);

    public static final String BASE_CONTENIDOS = "proyecto.";

    public static final String TIPO_CONTENIDO = "vnd.android.cursor.dir/vnd."
            + BASE_CONTENIDOS;

    public static final String TIPO_CONTENIDO_ITEM = "vnd.android.cursor.item/vnd."
            + BASE_CONTENIDOS;

    public interface Tablas {

        //TABLAS-----------------

        String TABLA_TABLAS = "tablas";
        String TABLA_PROYECTO = "proyecto";

        //COLUMNAS---------------

        String PROYECTO_ID_PROYECTO = "id_proyecto";

        String TABLAS_ID_TABLA = "id_tabla";
        String TABLAS_TABLA = "tablaModelo";
        String TABLAS_CAMPO = "campo";
        String TABLAS_PARAMETROS = "parametros";


        //REFERENCIAS------------

        String ID_PROYECTO = String.format("REFERENCES %s(%s) ON DELETE CASCADE",
                TABLA_PROYECTO, PROYECTO_ID_PROYECTO);


        //CAMPOS------------------

        String[] CAMPOS_PROYECTO = {"47", TABLA_PROYECTO,
                PROYECTO_ID_PROYECTO, "TEXT NON NULL UNIQUE",STRING,
                PROYECTO_ID_CLIENTE, String.format("TEXT NON NULL %s", ID_CLIENTE),STRING,

        };


    }

        public static final String PARAMETRO_FILTRO = "filtro";
        public static final String FILTRO_TOTAL = "total";
        public static final String FILTRO_FECHA = "fecha";

        public static ArrayList<String[]> obtenerListaCampos(){

            ArrayList<String[]> listaCampos = new ArrayList<>();
            listaCampos.addModelo(Tablas.CAMPOS_PROYECTO);

            return listaCampos;
        }

        public static Uri obtenerUriContenido(String tablaModelo){

            return URI_BASE.buildUpon().appendPath(tablaModelo).build();
        }

        public static Uri crearUriTabla(String id, String tablaModelo){

            Uri URI_CONTENIDO = obtenerUriContenido(tablaModelo);

            return URI_CONTENIDO.buildUpon().appendPath(id).build();
        }

        public static Uri crearUriTablaDetalle(String id, String secuencia, String tablaModelo) {
            // Uri de la forma 'gasto/:id#:secuencia'
            Uri URI_CONTENIDO = obtenerUriContenido(tablaModelo);
            return URI_CONTENIDO.buildUpon()
                    .appendPath(String.format("%s#%s", id, secuencia))
                    .build();
        }

        public static Uri crearUriTablaDetalle(String id, int secuencia, String tablaModelo) {
            // Uri de la forma 'gasto/:id#:secuencia'
            Uri URI_CONTENIDO = obtenerUriContenido(tablaModelo);
            return URI_CONTENIDO.buildUpon()
                    .appendPath(String.format("%s#%s", id, String.valueOf(secuencia)))
                    .build();
        }

        public static Uri crearUriTablaDetalleId(String id, String tablaModelo, String tablaCab){

            Uri URI_CONTENIDO = obtenerUriContenido(tablaCab);
            return URI_CONTENIDO.buildUpon().appendPath(id).appendPath(tablaModelo).build();

        }

        public static String obtenerIdTablaDetalleId(Uri uri) {
            return uri.getPathSegments().get(1);
        }

        public static String[] obtenerIdTablaDetalle(Uri uri) {
            return uri.getLastPathSegment().split("#");
        }

        public static String generarIdTabla(String tablaModelo){
            return tablaModelo + UUID.randomUUID().toString();
        }

        public static String obtenerIdTabla(Uri uri){
            return uri.getLastPathSegment();
        }

        public static boolean tieneFiltro(Uri uri){
            return uri != null && uri.getQueryParameter(PARAMETRO_FILTRO) != null;
        }


    public static String generarMime(String id) {
        if (id != null) {
            return TIPO_CONTENIDO + id;
        } else {
            return null;
        }
    }

    public static String generarMimeItem(String id) {
        if (id != null) {
            return TIPO_CONTENIDO_ITEM + id;
        } else {
            return null;
        }
    }

    private Contract() {
    }
}

    //QUERYDB BASE------------------------------------------------------------------------------

public class QueryDB implements Utilidades.Constantes {

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
                    list.addModelo(modelo);
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
                    list.addModelo(modelo);
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
                list.addModelo(modelo);
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
                list.addModelo(modelo);
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
                list.addModelo(modelo);
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
                    list.addModelo(modelo);
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
                    list.addModelo(modelo);
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
                    list.addModelo(modelo);
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
                    list.addModelo(modelo);
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
                    list.addModelo(modelo);
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
                    list.addModelo(modelo);
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
                    list.addModelo(modelo);
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

    public static int updateRegistro(String tablaModelo,String id,ContentValues valores){

        return resolver.update(Contract.crearUriTabla(id, tablaModelo)
                , valores, null, null);

    }

    public static int updateRegistro(Uri uri,ContentValues valores){

        return resolver.update(uri, valores, null, null);

    }

    public static int updateRegistro(Uri uri,ContentValues valores, String seleccion){

        return resolver.update(uri, valores, seleccion, null);

    }

    public static int updateRegistroDetalle(String tablaModelo,String id, String secuencia,ContentValues valores){

        return resolver.update(Contract.crearUriTablaDetalle(id,secuencia, tablaModelo)
                , valores, null, null);

    }

    public static int updateRegistroDetalle(String tablaModelo,String id, int secuencia,ContentValues valores){

        return resolver.update(Contract.crearUriTablaDetalle(id,secuencia, tablaModelo)
                , valores, null, null);

    }

    public static int updateRegistrosDetalle(String tablaModelo,String id, String tablaCab, ContentValues valores,String seleccion){

        return resolver.update(Contract.crearUriTablaDetalleId(id,tablaModelo,tablaCab)
                , valores, seleccion, null);

    }

    public static int updateRegistros(String tablaModelo,ContentValues valores,String seleccion){

        return resolver.update(Contract.obtenerUriContenido(tablaModelo)
                , valores, seleccion, null);

    }

    public static int updateRegistros
            (String tablaModelo,ContentValues valores,String campo, String valor, String valor2,  int flag){

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
        return resolver.update(Contract.obtenerUriContenido(tablaModelo)
                , valores, seleccion, null);

    }

    public static int updateRegistros
            (String tablaModelo,ContentValues valores,String campo, int valor, int valor2,  int flag){

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
        return resolver.update(Contract.obtenerUriContenido(tablaModelo)
                , valores, seleccion, null);

    }

    public static int updateRegistros
            (String tablaModelo,ContentValues valores,String campo, long valor, long valor2,  int flag){

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
        return resolver.update(Contract.obtenerUriContenido(tablaModelo)
                , valores, seleccion, null);

    }

    public static int updateRegistros
            (String tablaModelo,ContentValues valores,String campo, double valor, double valor2,  int flag){

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
        return resolver.update(Contract.obtenerUriContenido(tablaModelo)
                , valores, seleccion, null);

    }

    public static int updateRegistros
            (String tablaModelo,ContentValues valores,String campo, float valor, float valor2,  int flag){

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
        return resolver.update(Contract.obtenerUriContenido(tablaModelo)
                , valores, seleccion, null);

    }

    public static int updateRegistros
            (String tablaModelo,ContentValues valores,String campo, short valor, short valor2,  int flag){

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
        return resolver.update(Contract.obtenerUriContenido(tablaModelo)
                , valores, seleccion, null);

    }

    public static int deleteRegistro(String tablaModelo,String id){

        return resolver.delete(Contract.crearUriTabla(id, tablaModelo)
                , null, null);

    }

    public static int deleteRegistrosDetalle(String tablaModelo,String id){

        return resolver.delete(Contract.crearUriTabla(id, tablaModelo)
                , null, null);

    }

    public static int deleteRegistroDetalle(String tablaModelo,String id, int secuencia){

        return resolver.delete(Contract.crearUriTablaDetalle(id,secuencia, tablaModelo)
                , null, null);

    }

    public static int deleteRegistroDetalle(String tablaModelo,String id, String secuencia){

        return resolver.delete(Contract.crearUriTablaDetalle(id,secuencia, tablaModelo)
                , null, null);

    }

    public static int deteteRegistros(String tablaModelo,String seleccion){

        return resolver.delete(Contract.obtenerUriContenido(tablaModelo)
                ,  seleccion, null);

    }

    public static int deleteRegistros
            (String tablaModelo,String campo, String valor, String valor2,  int flag){

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
        return resolver.delete(Contract.obtenerUriContenido(tablaModelo)
                , seleccion, null);

    }

    public static Uri insertRegistro(String tablaModelo,ContentValues valores){

        return resolver.insert(Contract.obtenerUriContenido(tablaModelo), valores);

    }

    public static String idInsertRegistro(String tablaModelo,ContentValues valores){

        Uri uri = resolver.insert(Contract.obtenerUriContenido(tablaModelo), valores);

        return Contract.obtenerIdTabla(uri);

    }

    public static Uri insertRegistroDetalle(String[] campos, String id, String tablaCab, ContentValues valores){

        ArrayList<Modelo> lista = QueryDB.queryListDetalle(campos,id,tablaCab,null,null);

        int secuencia = 0;

        if (lista!=null && lista.sizeLista()>0) {
            secuencia = lista.sizeLista() + 1;
        }else{
            secuencia = 1;
        }
        System.out.println("secuencia = " + secuencia);

        QueryDB.putDato(valores,campos,"secuencia",secuencia);

        return resolver.insert(Contract.crearUriTablaDetalle(id,secuencia,campos[1]), valores);

    }

}

    //DATABASE -------------------------------------------------------------------------------------------

    public class DataBase extends SQLiteOpenHelper
        implements Common.Constantes, Contract.Tablas, Common.Estados, Common.TiposEstados {

    private static final String NOMBRE_BASE_DATOS = "freelanceproject.db";

    private static final int VERSION_ACTUAL = 1;

    private final Context contexto;

    public DataBase(Context contexto) {
        super(contexto, NOMBRE_BASE_DATOS, null, VERSION_ACTUAL);
        this.contexto = contexto;
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                db.setForeignKeyConstraintsEnabled(true);
            } else {
                db.execSQL("PRAGMA foreign_keys=ON");
            }
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(String.format("CAMPO_CREATEREG TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "%s TEXT NOT NULL UNIQUE," +
                        "%s TEXT NOT NULL," +
                        "%s TEXT NOT NULL," +
                        "%s TEXT"+
                        ")",
                TABLA_TABLAS, BaseColumns._ID,
                TABLAS_ID_TABLA,
                TABLAS_TABLA,
                TABLAS_CAMPO,
                TABLAS_PARAMETROS

        ));

        Log.d("db", "Creada tablaModelo tablaModelo");
        cargarDatosTabla(db);

        String[] proyeccion = {TABLAS_TABLA};
        Cursor cursor = db.query(true, TABLA_TABLAS, proyeccion,null,null,
                null,null,null,null);

        StringBuilder insert = null;

        while (cursor.moveToNext()){

            String tbl = cursor.getString(cursor.getColumnIndex(TABLAS_TABLA));
            String seleccion = TABLAS_TABLA + " = '" + tbl+"'";

            insert = new StringBuilder(String.format("CAMPO_CREATEREG TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT,", tbl, BaseColumns._ID));

            Cursor campos = db.query(TABLA_TABLAS,null,seleccion,
                    null,null,null,null);

            while (campos.moveToNext()){

                String nomCampo = campos.getString(campos.getColumnIndex(TABLAS_CAMPO));
                String parametrosCampo = campos.getString(campos.getColumnIndex(TABLAS_PARAMETROS));

                if (campos.isLast()) {
                    insert.append(nomCampo).append(" ").append(parametrosCampo).append(")");
                }else{
                    insert.append(nomCampo).append(" ").append(parametrosCampo).append(",");
                }
            }

            campos.close();
            db.execSQL(insert.toString());
            Log.d("db", "Creada tablaModelo "+tbl);

        }
        cursor.close();

        cargarDatosDefecto(db);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        String[] proyeccion = {TABLAS_TABLA};
        Cursor cursor = db.query(true, TABLA_TABLAS, proyeccion,null,null,
                null,null,null,null);

        if (cursor.moveToNext()) {

            db.execSQL("DROP TABLE IF EXISTS " + TABLAS_TABLA);

        }

        cursor.close();
        onCreate(db);

    }

    private void cargarDatosDefecto(SQLiteDatabase db) {


        //long i = 0;

        //ContentValues valores = new ContentValues();
        //valores.put(TIPOCLIENTE_DESCRIPCION, PRINCIPAL);
        //valores.put(TIPOCLIENTE_PESO, 15);
        //valores.put(TIPOCLIENTE_ID_TIPOCLIENTE, TABLA_TIPOCLIENTE + UUID.randomUUID().toString());
        //i = db.insertOrThrow(TABLA_TIPOCLIENTE, null, valores);
        //if (i > 0) {
        //    Log.d("datos_iniciales", "Insertados datos defecto TiposCliente");
        //    i = 0;
        //}
    }

    private void cargarDatosTabla(SQLiteDatabase db) {

        ArrayList<String[]> listaCampos = Contract.obtenerListaCampos();

        for (String[] campos : listaCampos) {

            construirTabla(db,campos);
        }

    }

    private void construirTabla(SQLiteDatabase db, String[] args){

        ContentValues valores = new ContentValues();

        int fin = Integer.parseInt(args[0]);

        for (int i = 2;i<fin;i+=3){

            valores.put(TABLAS_TABLA, args[1]);
            valores.put(TABLAS_CAMPO, args[i]);
            valores.put(TABLAS_PARAMETROS, args[i+1]);
            valores.put(TABLAS_ID_TABLA, TABLA_TABLAS + UUID.randomUUID().toString());
            long res = db.insertOrThrow(TABLA_TABLAS, null, valores);
            if (res > 0) {
                Log.d("datos_iniciales", "Insertado " + args[i]);
            } else {
                Log.d("datos_iniciales", "Error al Insertar " + args[i]);
            }

        }
    }

}

     */
    /*

    //PROVIDER ------------------------------------------------------------------------------------------

    public class ProviderFreelanceProject extends ContentProvider implements Tablas{

    private DataBase bd;

    private ContentResolver resolver;

    public static final UriMatcher uriMatcher;

    // Casos
    public static final int PROYECTO = 101;
    public static final int PROYECTO_ID = 102;
    public static final int PROYECTO_ID_PARTIDA = 103;
    public static final int PROYECTO_ID_GASTO = 104;

    public static final int PARTIDA = 105;
    public static final int PARTIDA_ID = 106;



    public static final String AUTORIDAD = "jjlacode.com.proyecto";

    static {

        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        //uriMatcher.addURI(AUTORIDAD, TABLA_PROYECTO, PROYECTO);
        //uriMatcher.addURI(AUTORIDAD, TABLA_PROYECTO+"/*", PROYECTO_ID);


}

    private static final String PROYECTO_JOIN_CLIENTE_Y_ESTADO= "proyecto " +
            "INNER JOIN cliente " +
            "ON proyecto.id_cliente = cliente.id_cliente " +
            "INNER JOIN estado " +
            "ON proyecto.id_estado = estado.id_estado";

    private final String proyProyecto = String.format("%s.*,%s,%s,%s,%s",

            TABLA_PROYECTO,
            CLIENTE_NOMBRE,
            CLIENTE_PESOTIPOCLI,
            ESTADO_DESCRIPCION,
            ESTADO_TIPOESTADO);



    public ProviderFreelanceProject() {

    }



    @Override
    public boolean onCreate() {
        bd = new DataBase(getContext());
        resolver = getContext().getContentResolver();
        return true;
    }


    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case PROYECTO:
                return generarMime(TABLA_PROYECTO);
            case PROYECTO_ID:
                return generarMimeItem(TABLA_PROYECTO);

            default:
                throw new UnsupportedOperationException("Uri desconocida =>" + uri);
        }
    }

    private ContentValues matcherUri(Uri uri){

        String tablaModelo = null;
        String idTabla = null;
        String setTablas = null;
        String proyeccion = null;
        boolean esDetalle= false;
        boolean esId = false;

        ContentValues values = new ContentValues();

        switch (uriMatcher.match(uri)) {

            case PROYECTO:
                // Generar Pk
                tablaModelo = TABLA_PROYECTO;
                idTabla = PROYECTO_ID_PROYECTO;
                setTablas = PROYECTO_JOIN_CLIENTE_Y_ESTADO;
                proyeccion = proyProyecto;
                esId = false;
                esDetalle = false;
                break;
            case PROYECTO_ID:
                tablaModelo = TABLA_PROYECTO;
                idTabla = PROYECTO_ID_PROYECTO;
                setTablas = PROYECTO_JOIN_CLIENTE_Y_ESTADO;
                proyeccion = proyProyecto;
                esId = true;
                esDetalle = false;
                break;


            case PROYECTO_ID_PARTIDA:
                tablaModelo = TABLA_PARTIDA;
                setTablas = PARTIDAS_JOIN_ESTADO_JOIN_PROYECTO;
                proyeccion = proyPartida;
                idTabla = PARTIDA_ID_PROYECTO;
                esId = false;
                esDetalle = true;

                break;

            case PARTIDA:
                tablaModelo = TABLA_PARTIDA;
                setTablas = PARTIDAS_JOIN_ESTADO_JOIN_PROYECTO;
                proyeccion = proyPartida;
                idTabla = PARTIDA_ID_PROYECTO;
                esId = false;
                esDetalle = false;

                break;
            case PARTIDA_ID:

                tablaModelo = TABLA_PARTIDA;
                setTablas = PARTIDAS_JOIN_ESTADO_JOIN_PROYECTO;
                proyeccion = proyPartida;
                idTabla = PARTIDA_ID_PROYECTO;
                esDetalle = true;
                esId = true;
                break;



        }

        values.put("tablaModelo",tablaModelo);
        values.put("idTabla",idTabla);
        values.put("proyeccion", proyeccion);
        values.put("setTablas", setTablas);
        values.put("esDetalle", esDetalle);
        values.put("esId", esId);

        return values;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        SQLiteDatabase db = bd.getWritableDatabase();

        ContentValues valores = matcherUri(uri);

        String secuencia= values.getAsString("secuencia");
        String tablaModelo = valores.getAsString("tablaModelo");
        String idTabla = valores.getAsString("idTabla");
        String id = generarIdTabla(tablaModelo);

        System.out.println("valores = " + valores);


        if (tablaModelo!=null){
            if (secuencia == null) {
                values.put(idTabla, id);
            }
            db.insertOrThrow(tablaModelo, null, values);
            notificarCambio(uri);
            if (secuencia != null) {
                id= values.getAsString(idTabla);
                return crearUriTablaDetalle(id, secuencia, tablaModelo);
            }else {
                return crearUriTabla(id, tablaModelo);
            }
        } else{
            throw new UnsupportedOperationException("Uri no soportada");
        }
    }


    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // Obtener base de datos
        SQLiteDatabase db = bd.getReadableDatabase();

        Cursor c;

        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();

        ContentValues valores = matcherUri(uri);

        String proy = valores.getAsString("proyeccion");
        String[] proyeccion = new String[] {proy};//proy.split(",");
        String setTablas = valores.getAsString("setTablas");
        String idTabla = valores.getAsString("idTabla");
        String[] ids = null;
        boolean esId = valores.getAsBoolean("esId");
        boolean esDetalle = valores.getAsBoolean("esDetalle");
        String tablaModelo = valores.getAsString("tablaModelo");

        if (selection == null) {

            if (esDetalle && esId) {

                ids = obtenerIdTablaDetalle(uri);
                String id = ids[0];
                String secuencia = ids[1];
                selection = tablaModelo + "." +idTabla + " = '" + id + "' AND " +
                        "secuencia = '" + secuencia + "'";

            } else if (esDetalle) {

                String id = Contract.obtenerIdTablaDetalleId(uri);
                selection = tablaModelo + "." + idTabla + " = '" + id + "'";

            } else if (esId) {

                String id = Contract.obtenerIdTabla(uri);
                selection = idTabla + " = '" + id + "'";

            }
        }


        if (setTablas!=null) {
            builder.setTables(setTablas);
            c = builder.query(db, proyeccion, selection,
                    selectionArgs, null, null, sortOrder);
            c.setNotificationUri(resolver, uri);

            return c;

        }else{

            throw new UnsupportedOperationException("Uri no soportada");
        }

    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {

        SQLiteDatabase db = bd.getWritableDatabase();

        ContentValues valores = matcherUri(uri);

        String tablaModelo = valores.getAsString("tablaModelo");
        String idTabla = valores.getAsString("idTabla");
        boolean esId = valores.getAsBoolean("esId");
        boolean esDetalle = valores.getAsBoolean("esDetalle");
        String id = null;
        String[] ids = null;
        String secuencia = null;
        String seleccion = null;

        if (selection == null) {
            if (!esDetalle) {
                id = obtenerIdTabla(uri);
                seleccion = idTabla + " = '" + id + "'";

            } else if (esDetalle && !esId){

                id = obtenerIdTablaDetalleId(uri);
                seleccion = tablaModelo+"."+idTabla + " = '" + id + "'";

            }else {
                ids = obtenerIdTablaDetalle(uri);
                id = ids[0];
                secuencia = ids[1];
                seleccion = idTabla + " = '" + id + "' AND " +
                        "secuencia = '" + secuencia + "'";
            }
        }else{
            seleccion = selection;
        }

        if (tablaModelo!=null) {

            notificarCambio(uri);

            return db.update(tablaModelo, values,
                    seleccion ,
                    selectionArgs);

        }else {

            throw new UnsupportedOperationException("Uri no soportada");

        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        SQLiteDatabase db = bd.getWritableDatabase();

        ContentValues valores = matcherUri(uri);

        String tablaModelo = valores.getAsString("tablaModelo");
        String idTabla = valores.getAsString("idTabla");
        boolean esId = valores.getAsBoolean("esId");
        boolean esDetalle = valores.getAsBoolean("esDetalle");
        String id = null;
        String[] ids = null;
        String secuencia = null;
        String seleccion = null;

        if (selection == null) {

            if (!esDetalle) {
                id = obtenerIdTabla(uri);
                seleccion = idTabla + " = '" + id + "'";

            } else if (esDetalle && !esId) {

                id = obtenerIdTablaDetalleId(uri);
                seleccion = tablaModelo + "." + idTabla + " = '" + id + "'";

            }else {
                ids = obtenerIdTablaDetalle(uri);
                id = ids[0];
                secuencia = ids[1];
                seleccion = idTabla + " = '" + id + "' AND " +
                        "secuencia = '" + secuencia + "'";
            }
        }else{
            seleccion = selection;
        }

        if (tablaModelo!=null) {
            notificarCambio(uri);
            return db.delete(tablaModelo,seleccion,
                    selectionArgs);
        }else{

            throw new UnsupportedOperationException("Uri no soportada");
        }
    }

    private void notificarCambio(Uri uri) {
        resolver.notifyChange(uri, null);
    }

    private String construirFiltro(String filtro) {

        String sentencia = null;

        switch (filtro) {
            case FILTRO_CLIENTE:
                sentencia = "cliente.nombres";
                break;
            case FILTRO_FECHA:
                sentencia = "proyecto.fecha";
                break;
        }

        return sentencia;
    }

}

    // SPLASH ACTIVITY---------------------------------------------------------------------------

    public class SplashActivity extends AppCompatActivity implements Contract.Tablas {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (!comprobarInicio()){

                    SharedPreferences preferences=getSharedPreferences("preferencias", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor=preferences.edit();

                    try {
                        ContentValues valores = new ContentValues();

                        QueryDB.putDato(valoresPer,CAMPOS_PERFIL,PERFIL_NOMBRE,"Defecto");
                        QueryDB.putDato(valoresPer,CAMPOS_PERFIL,PERFIL_BENEFICIO,10);
                        System.out.println("Contract.Tablas.obtenerUriContenido(Contract.Tabla.PERFIL) = "
                                + Contract.obtenerUriContenido(TABLA_PERFIL));
                        Uri reg = QueryDB.insertRegistro(TABLA_PERFIL,valoresPer);
                        System.out.println(reg);


                    }catch (Exception e){

                        Toast.makeText(getApplicationContext(),"Error al crear base de datos",Toast.LENGTH_LONG).show();
                        System.out.println("error al crear base");
                        getApplicationContext().deleteDatabase("freelanceproject.db");
                        if (preferences.contains("perfil_activo")) {

                            editor.removeModelo("perfil_activo");
                            editor.apply();
                        }
                        finish();
                    }

                    try{

                        editor.putString("perfil_activo", "Defecto");
                        editor.apply();

                    }catch (Exception e){

                        if (preferences.contains("perfil_activo")) {

                            editor.removeModelo("perfil_activo");
                            editor.apply();

                        }
                        getApplicationContext().deleteDatabase("freelanceproject.db");
                        System.out.println("error al crear base");
                        finish();

                    }
                    Common.perfila="Defecto";
                }



                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        },2000);
    }

    private Boolean comprobarInicio() {

        SharedPreferences preferences=getSharedPreferences("preferencias", Context.MODE_PRIVATE);

        if (getDatabasePath("freelanceproject.db")!=null && preferences.contains("perfil_activo")){

            Common.perfila = preferences.getString("perfil_activo","Defecto");

            Log.d("inicio", "Inicio correcto");


            return true;
        }

        if (preferences.contains("perfil_activo")){

            SharedPreferences.Editor editor=preferences.edit();
            editor.removeModelo("perfil_activo");
            editor.apply();

            Log.d("inicio", "borrado perfil setActivo de preferencias");
        }
        if (getDatabasePath("freelanceproject.db")!=null){

            deleteDatabase("freelanceproject.db");

            Log.d("inicio", "borrada freelanceproject.db");
        }

        return false;
    }




}


*/
}
