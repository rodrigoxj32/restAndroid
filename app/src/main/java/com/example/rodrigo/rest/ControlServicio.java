package com.example.rodrigo.rest;

/**
 * Created by rodrigo on 04-12-17.
 */

import android.content.Context;
import android.database.CharArrayBuffer;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import android.content.ContentResolver;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.apache.http.HttpEntity;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class ControlServicio {


    public static String obtenerRespuestaPeticion(String url, Context ctx) {
        String respuesta = " ";
// Estableciendo tiempo de espera del servicio
        HttpParams parametros = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(parametros, 3000);
        HttpConnectionParams.setSoTimeout(parametros, 5000);
// Creando objetos de conexion
        HttpClient cliente = new DefaultHttpClient(parametros);
        HttpGet httpGet = new HttpGet(url);
        try {
            HttpResponse httpRespuesta = cliente.execute(httpGet);
            StatusLine estado = httpRespuesta.getStatusLine();
            int codigoEstado = estado.getStatusCode();
            System.out.println("Codigo de estado:" +codigoEstado);
            if (codigoEstado == 200) {
                HttpEntity entidad = httpRespuesta.getEntity();
                respuesta = EntityUtils.toString(entidad);
            }
        } catch (Exception e) {
            Toast.makeText(ctx, "Error en la conexion", Toast.LENGTH_LONG).show();
// Desplegando el error en el LogCat
            Log.v("Error de Conexion", e.toString());
        }
        return respuesta;
    }


    public static List<Libro> obtenerLibros(String peticion, Context ctx) {
        String json = obtenerRespuestaPeticion(peticion, ctx);
        List<Libro> listaLibro = new ArrayList<Libro>();
        try {
            JSONArray librosJSON = new JSONArray(json);
            for (int i = 0; i < librosJSON.length(); i++) {
                JSONObject obj = librosJSON.getJSONObject(i);
                Libro libro = new Libro();
                libro.setId(obj.getInt("id"));
                libro.setNombre(obj.getString("nombre"));
                libro.setDescripcion(obj.getString("descripcion"));
                libro.setCantidad(obj.getInt("cantidad"));
                libro.setImagen(obj.getString("imagen"));
                listaLibro.add(libro);
            }
            return listaLibro;
        } catch (Exception e) {
            Toast.makeText(ctx, "Error en parseo de JSON", Toast.LENGTH_LONG).show();
            return null;
        }


    }

}
