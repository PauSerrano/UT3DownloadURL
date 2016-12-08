package es.schooleando.ut3ejercicio2;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by ruben on 18/11/16.
 */

public class DownloadURLTask extends AsyncTask<String, Integer, Bitmap> {


    private Activity actividad;

    private ProgressBar barraProgreso;
    private TextView tvProgreso;
    private ImageView imageView;


    //++++++++++++++ variables para los campos de comprobacion
    private TextView tvComprobar;
    private int codRespuesta;
    private int tamañoRecurso;
    private String tipo;
    //++++++++++++++


    private Bitmap imagenBmpDescargada;
    //private int tamaño;

    //Realizamos un ******* CONSTRUCTOR ******* de la clase donde le pasamos la activity para así poser referenciar los controles
    public DownloadURLTask(Activity actividad) {
        this.actividad = actividad;

        //Referenciamos los controles que vamos a utilizar de la Activity
        tvComprobar = (TextView) actividad.findViewById(R.id.tvComprobar);
        imageView = (ImageView) actividad.findViewById(R.id.imageView);
    }

    @Override
    protected void onPreExecute() {

        //Declaramos la barra de progreso
        barraProgreso = (ProgressBar) actividad.findViewById(R.id.progressBar);
        barraProgreso.setMax(100);
        barraProgreso.setProgress(0);
        tvProgreso = (TextView) actividad.findViewById(R.id.tvProgreso);
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        barraProgreso.setProgress(values[0]);
        tvProgreso.setText(String.valueOf(values[0]) + " %");

    }

    @Override
    protected Bitmap doInBackground(String... urls) {

        //Realizamos la ****** DESCARGA ******
        try {
            URL url = new URL (urls[0]);
            HttpURLConnection conexion = (HttpURLConnection) url.openConnection();

            //Realizamos una peticion previa para obtener el tamaño del archivo
            conexion.setRequestMethod("HEAD");
            conexion.connect();

            //++++++++++++++ variables para los campos de comprobacion
            codRespuesta = conexion.getResponseCode();
            tamañoRecurso = conexion.getContentLength();
            tipo = conexion.getContentType();
            //++++++++++++++

            URL url1 = new URL (urls[0]);
            HttpURLConnection conexion1 = (HttpURLConnection) url1.openConnection();

            //Realizamos la peticion de la descarga de la imagen
            conexion1.setRequestMethod("GET");
            conexion1.connect();
            tipo = conexion1.getContentType();
            //tamañoRecurso = conexion1.getContentLength();

            if (tipo.startsWith("image/")){
                InputStream is = conexion1.getInputStream();
                ByteArrayOutputStream bos = new ByteArrayOutputStream();

                byte[] buffer = new byte[1024];
                int n = 0;
                int total = 0;
                //Mientras el resultado de lectura del buffer sea distinto a -1
                while ((n=is.read(buffer))!=-1) {
                    //Escribimos los bytes
                    bos.write(buffer,0,n);

                    //porcentaje y progress bar
                    total += n;
                    int porc = (total*100)/ tamañoRecurso;
                    SystemClock.sleep(50);

                    publishProgress(porc);
                }

                //cerramos los Streams
                bos.close();
                is.close();

                byte[] arrayImagen = bos.toByteArray();
                imagenBmpDescargada = BitmapFactory.decodeByteArray(arrayImagen,0,arrayImagen.length) ;
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return imagenBmpDescargada;
    }


    @Override
    protected void onPostExecute(Bitmap result) {


        tvComprobar.setText("Info Imagen: Codidigo Respuesta:" + String.valueOf(codRespuesta)+" - Tamaño: "+String.valueOf(tamañoRecurso)+" - Tipo: "+tipo);

        imageView.setImageBitmap(result);

        barraProgreso.setProgress(0);
        tvProgreso.setText( "Completado");


    }



    /*private Bitmap descarcarImagen (String direccionImagen){
        URL imagenURL = null;
        Bitmap imagenBmp = null;

        try {
            imagenURL = new URL (direccionImagen);
            HttpURLConnection conexion = (HttpURLConnection) imagenURL.openConnection();
            conexion.connect();
            imagenBmp = BitmapFactory.decodeStream(conexion.getInputStream());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return imagenBmp;
    }*/
}
