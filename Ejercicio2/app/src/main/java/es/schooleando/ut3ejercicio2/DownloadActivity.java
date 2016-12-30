package es.schooleando.ut3ejercicio2;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class DownloadActivity extends AppCompatActivity {


    //Primero damos ****** PERMISOS ******* A LA APP para acceder a la red en el Manifest

    //Campos añadidos para comprobar los datos de la descarga
    private TextView tvConexion;

    private ImageView ivImagen;


    private Button botonDescargar;
    private Button botonCancelar;
    private TextView urlImagen;

    private String urlDescarga;

    private TextView tvComprobar;
    private TextView tvProgreso;

    private DownloadURLTask descargarImagenTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);


        tvConexion = (TextView) findViewById(R.id.tvConexion);
        ivImagen = (ImageView) findViewById(R.id.imageView);
        tvComprobar= (TextView) findViewById(R.id.tvComprobar);
        tvProgreso = (TextView) findViewById(R.id.tvProgreso);

        botonDescargar = (Button) findViewById(R.id.button);
        botonCancelar = (Button) findViewById(R.id.butCancelar);

        urlImagen = (TextView) findViewById(R.id.textURL);

        botonCancelar.setVisibility(View.INVISIBLE);
    }

    //Metodo onClick del boton de descarga
    public void descargarImagen(View view) {

        tvComprobar.setText("");
        tvProgreso.setText("Conectando...");

        urlDescarga = urlImagen.getText().toString();
//        urlDescarga = "http://www.fpmislata.com/joomla/images/cipfpmislata/logo.jpg";


        //Comprobamos si estamos conectado a la red
        if (estaConectado()){
            //Realizamos laoperacion de descarga en un AsynTask si estamos conectados
            descargarImagenTask = new DownloadURLTask(this);
            descargarImagenTask.execute(urlDescarga);
        } else {
            //Indicamos que no podemos realizar la descarga
            tvComprobar.setText("No podemos descargar la imagen");
        }
    }


    //Metodo para cancelar tarea
    public void cancelarTarea(View view) {

        descargarImagenTask.cancel(true);
    }



    //Metodo para comprobar la conexion
    protected Boolean estaConectado(){
        Boolean conectado = false;

    if (conectadoWifi()){
        tvConexion.setText("Estamos conectados a través del Wifi");
        conectado = true;
    }else if(conectadoDatos()){
        tvConexion.setText("Estamos conectado a través de Datos moviles");
        conectado = true;
    } else {
        tvConexion.setText("No tenemos conexion");
    }
        return conectado;
    }

    //Comprobamos si esta conectado por wifi
    protected Boolean conectadoWifi(){
        Boolean conectado = false;

        ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivity != null){
            NetworkInfo info = connectivity.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (info !=null){
                if (info.isConnected()){
                    conectado = true;
                }
            }
        }
        return conectado;
    }

    //Comprobamos si esta conectado por datos
    protected Boolean conectadoDatos(){
        Boolean conectado = false;
        ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivity != null){
            NetworkInfo info = connectivity.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (info !=null){
                if (info.isConnected()){
                    conectado = true;
                }
            }
        }
        return conectado;
    }

    //Introducimos NUEVA URL a través de un dialogo
    public void dialogoCambiar(View view) {

        // Rescatamos el layout creado para el prompt
        LayoutInflater li = LayoutInflater.from(this);
        View prompt = li.inflate(R.layout.dialogo_cambio_url, null);

        // Creamos un constructor de Alert Dialog y le añadimos nuestro layout al cuadro de dialogo
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(prompt);

        final EditText urlNueva = (EditText) prompt.findViewById(R.id.etUrlNueva);

        // Mostramos el mensaje del cuadro de dialogo
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Cambiar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // Rescatamos url del EditText y lo mostramos por pantalla
                        urlImagen.setText(urlNueva.getText());
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // Cancelamos el cuadro de dialogo
                        dialog.cancel();
                    }
                });

        // Creamos un AlertDialog y lo mostramos
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


}
