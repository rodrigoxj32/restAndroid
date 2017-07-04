package com.example.rodrigo.rest;

import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.EditText;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.os.StrictMode;
import android.annotation.SuppressLint;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;





public class MainActivity extends AppCompatActivity {

    //controlServicio helper;
    EditText idLibro;
    EditText nomLibro;
    EditText desLibro;
    EditText cantLibro;
    ImageView imagen;
    Button guardar;
    Conexion conn;
    private final String USER_AGENT = "Mozilla/5.0";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        //codigo de conexion
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        conn=new Conexion();
        //helper = new controlP3SR11038(this);
        idLibro = (EditText) findViewById(R.id.idLibro);
        nomLibro= (EditText) findViewById(R.id.nombreLibro);
        desLibro= (EditText) findViewById(R.id.descripcionLibro);
        cantLibro= (EditText) findViewById(R.id.cantidadLibro);
        guardar = (Button)findViewById(R.id.guardar);
        imagen=(ImageView) findViewById(R.id.imagenLibro);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //consultar LIBROS POR GET METODO INDEX DE LARAVEL
    public void consultarLibroWeb(View v) {

        List<Libro> libros;
        int idlibro = Integer.parseInt(idLibro.getText().toString());
        String url = "";
        url += conn.getURLLocal() + "documento";
        System.out.println(url);
        libros = ControlServicio.obtenerLibros(url, this);
        System.out.println("objeto libto");
        System.out.println(libros);

        for (Libro c: libros){
            if(c.getId() == idlibro){
                nomLibro.setText(c.getNombre());
                desLibro.setText(c.getDescripcion());
                cantLibro.setText(String.valueOf( c.getCantidad()));


                byte[] decodedByte = Base64.decode(c.getImagen(), 0);

                imagen.setImageBitmap(BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length));


                System.out.println("nombre del libro: "+c.getNombre());
                break;
            }
        }
    }


/*GUARDA POR METODO POST SE CONECTA AL METODO STORE DE LARAVEL*/
    public void guardarHTTP(View v){

    System.out.println("entro al metodo");
// Creando objetos de conexion
        HttpClient cliente = new DefaultHttpClient();

        HttpPost httpPost = new HttpPost("http://192.168.1.4:8000/documento");

        httpPost.setHeader("content-type", "application/json");
        //httpPost.setHeader("Content-Type","application/x-www-form-urlencoded");

        System.out.println("antes del try");

        try{
            System.out.println("crean el objeto json");
            JSONObject dato = new JSONObject();

            System.out.println("insertando los datos al json");
            dato.put("nombre", nomLibro.getText().toString());
            dato.put("descripcion", desLibro.getText().toString());
            dato.put("cantidad", cantLibro.getText().toString());


            System.out.println("creando la entidad a enviar");
            StringEntity entity = new StringEntity(dato.toString());
            httpPost.setEntity(entity);

            System.out.println("ejecutando el httpResponse");
            HttpResponse resp = cliente.execute(httpPost);

            StatusLine estado = resp.getStatusLine();
            int codigoEstado = estado.getStatusCode();


            String resultado = EntityUtils.toString(resp.getEntity());
            System.out.println("respuesta dl http "+resp);
            System.out.println("codigo del estado: "+codigoEstado);
        }catch (Exception e){
            System.out.println("entro en el catch");
        }

    }






}//final de la clase main
