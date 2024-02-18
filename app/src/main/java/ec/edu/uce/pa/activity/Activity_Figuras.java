package ec.edu.uce.pa.activity;


import android.opengl.GLSurfaceView;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import ec.edu.uce.pa.evaluacion2.RendererEvaluacion;

public class Activity_Figuras extends AppCompatActivity {
    private GLSurfaceView view;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        view = new GLSurfaceView(this);
        view.setEGLContextClientVersion(2);//OpenGL 2.0

        //SELECCIONAR RENDERER
        //view.setRenderer(new RenderPunto());
        //view.setRenderer(new RenderHexagono(getApplicationContext()));
        //view.setRenderer(new RenderAstroTextura(getApplicationContext()));
        //view.setRenderer(new RenderEstrellasFondo());
        //view.setRenderer(new RenderHexagonoTextura(getApplicationContext()));
        //view.setRenderer(new RendererAstro(getApplicationContext()));
        //view.setRenderer(new RenderFondoTextura(getApplicationContext()));
        //view.setRenderer(new RenderHexagonoProyFP(getApplicationContext()));
        //view.setRenderer(new RenderCubo(getApplicationContext()));
        view.setRenderer(new RendererEvaluacion(getApplicationContext()));
        //view.setRenderer(new RenderPractica(getApplicationContext()));
        //view.setRenderer(new RenderCubeMap());
        setContentView(view);
    }
}