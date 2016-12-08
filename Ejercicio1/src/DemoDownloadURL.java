import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class DemoDownloadURL {
	public static void main(String[] args) {
		
		String nombre = "imagenDescarga.jpeg";
        File file = new File (nombre);
		
		// 1. pedimos una URL por línea de comandos
		//String urlStringPrueba = "http://www.fpmislata.com/joomla/images/cipfpmislata/logo.jpg";
		String urlStringPrueba = "http://lorempixel.com/200/300/";
				
		
		try {
			// 2. creamos el objeto URL
            URL url = new URL(urlStringPrueba);
            // 3. Obtenemos un objeto HttpURLConnection. openConnection 
            HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
            // 4. configuramos la conexión al método GET. setRequestMethod
            	//El metodo por defecto es GET, por lo que no haria falta configurar
            conexion.setRequestMethod("GET");
            // 5. Nos conectamos. connect
            conexion.connect();
            // 6. Obtenemos y imprimimos el código de respuesta. getResponseCode
            // 7. Obtenemos y imprimimos el tamaño del recurso. ContentLength
            int codRespuesta = conexion.getResponseCode();
            int tamañoRecurso = conexion.getContentLength();
            String tipo = conexion.getContentType();

            System.out.println("Codigo Respuesta: " + codRespuesta
                    + "\nTamaño del recurso: " + tamañoRecurso + ""
                    + "\nTipo de recurso: " + tipo);

            // 8. Guardamos el stream a un fichero con el nombre del recurso
    		//    en caso de que el código sea correcto.
            if (tipo.startsWith("image/")) {

                InputStream in = conexion.getInputStream();
                byte[] buffer = new byte[4096];
                int n = -1;
                OutputStream out = new FileOutputStream(file);
                //Mientras el resultado de lectura del buffer sea distinto a -1
                while ((n=in.read(buffer))!=-1) {                    
                    //Escribimos en el archivo de destino creado
                    out.write(buffer, 0, n);
                    
                }
                in.close();
                out.close();
                System.out.println("Se ha guardado el archivo correctamente");
                System.out.println("El archivo se llama: "+file.getName());
                System.out.println("Se encuentra en la ruta: "+file.getAbsolutePath());
                
            }else{
            	// 9. Damos un error en caso de que el código sea incorrecto (BufferedInputStream -> FileOutputStream)
                System.out.println("El codigo de archivo (image/) no es correcto");
            }

        } catch (MalformedURLException ex) {
            System.out.println("Error, protocolo desconocido ");
            System.out.println(ex.getMessage());
        } catch (IOException ex) {
        	System.out.println(ex.getMessage());
        }
    }	

}