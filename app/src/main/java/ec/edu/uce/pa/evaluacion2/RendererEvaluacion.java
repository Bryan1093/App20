package ec.edu.uce.pa.evaluacion2;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import ec.edu.uce.pa.R;
import ec.edu.uce.pa.utilidades.Funciones;

public class RendererEvaluacion implements GLSurfaceView.Renderer {

    private Cubo cubo;
    private Plano plano,plano1,manillas,manillas1;
    private PlanoTextura planoTextura,planoTv;
    private Astro astro;
    private Cilindro cilindro;


    private Context context;
    private int [] arrayTextura = new int[3];
    private static float[] stackModelo = new float[16];
    private static float[] stackVista = new float[16];
    private static float[] stackProyeccion = new float[16];
    private float[] matrizProyeccion = new float[16];
    private float[] matrizModelo = new float[16];
    private float[] matrizVista = new float[16];
    private float[] matrizTemp = new float[16];
    private float relacionAspecto, rotacion = 0.0f;

    private float[] colorAzul = {0.0f,0.0f,1.0f,1.0f};
    private float[] colorRojo = {1.0f,0.0f,0.0f,1.0f};
    private float[] colorVerde = {0.0f,1.0f,0.0f,1.0f};
    private float[] colorCafe = {0.4f, 0.2f, 0.0f, 0.5f};

    private float[] colorNegro = {0.0f, 0.00f, 0.0f, 1.0f};

    public RendererEvaluacion(Context contexto) {
        this.context = contexto;

        plano = new Plano(contexto,matrizProyeccion,matrizVista,matrizModelo,colorVerde);
        plano1 = new Plano(contexto,matrizProyeccion,matrizVista,matrizModelo,colorAzul);
        planoTextura = new PlanoTextura(context, matrizProyeccion, matrizVista, matrizModelo);
        cubo=new Cubo(context, matrizProyeccion, matrizVista, matrizModelo,colorCafe);
        planoTv = new PlanoTextura(context, matrizProyeccion, matrizVista, matrizModelo);
        astro = new Astro(30, 30, 1.0f, 1.0f,contexto,matrizProyeccion,matrizVista,matrizModelo);
        cilindro= new Cilindro(20, 20, 1.3f, 0.2f, contexto, matrizProyeccion,
                matrizVista,matrizModelo);
        manillas = new Plano(contexto,matrizProyeccion,matrizVista,matrizModelo,colorNegro);
        manillas1 = new Plano(contexto,matrizProyeccion,matrizVista,matrizModelo,colorNegro);

    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig eglConfig) {
        gl.glClearColor(0.2f, 0.2f, 0.2f, 1.0f);
        gl.glEnable(gl.GL_DEPTH_TEST);
        arrayTextura= Funciones.habilitarTexturas(new GLES20(),3);


        Funciones.cargarImagenesTexturas(new GLES20(),context,
                R.drawable.tierra,0,arrayTextura);
        Funciones.cargarImagenesTexturas(new GLES20(),context,
                R.drawable.partidotennis,1,arrayTextura);
        Funciones.cargarImagenesTexturas(new GLES20(),context,
                R.drawable.piso,2,arrayTextura);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int ancho, int alto) {
        gl.glViewport(0, 0, ancho, alto);
        relacionAspecto = (float) ancho / (float) alto;
        invocarFrustrum();
        invocarMatrices();



        Matrix.setLookAtM(matrizVista, 0,
                1, 2, 2,
                0, 0, 0,
                0, 1, 0);

    }

    @Override
    public void onDrawFrame(GL10 gl) {
        gl.glClear(gl.GL_COLOR_BUFFER_BIT | gl.GL_DEPTH_BUFFER_BIT);
        Matrix.setIdentityM(matrizModelo, 0);


        pushMatrix();
        transladar(0.2f,1f,0.4f);
        rotar(0,1,0,rotacion);
        escalar(0.3f,0.3f,0.3f );
        gl.glBindTexture(GLES20.GL_TEXTURE_2D, 1);
        astro.dibujar(new GLES20());
        popMatrix();

        pushMatrix();
        transladar(0.0f,1.8f,1.3f);
        rotar(0,1,0,90);
        escalar(0.5f,0.3f,0.5f);
        gl.glBindTexture(GLES20.GL_TEXTURE_2D, 2);
        planoTv.dibujar(new GLES20());
        popMatrix();

        transladar(0.2f,1,-0.7f);
        pushMatrix();
        transladar(-1f,0,1);
        rotar(0,1,0,90);
           // escalar(0.5f,0.5f,0.5f);
        plano.dibujar(new GLES20());
        popMatrix();

        pushMatrix();
        transladar(0f,0,0);
        rotar(0,1,0,0);
        //escalar(0.5f,0.5f,0.5f);
        plano1.dibujar(new GLES20());
        popMatrix();

        pushMatrix();
        transladar(0.0f,-1,1);
        rotar(1,0,0,-90);
        //escalar(0.5f,0.5f,0.5f);
        gl.glBindTexture(GLES20.GL_TEXTURE_2D, 3);
        planoTextura.dibujar(new GLES20());
        popMatrix();

        pushMatrix();
        transladar(0.1f,-0.4f,1.3f);
        //rotar(1,0,0,0);
        escalar(0.5f,0.5f,0.5f);
        cubo.dibujar(new GLES20());
        popMatrix();

        pushMatrix();
        transladar(0.4f,1f,1.2f);
        rotar(0,2,0,0);
        escalar(0.1f,0.1f,0.1f);
        cilindro.dibujar(new GLES20());
        popMatrix();

        pushMatrix();
        transladar(0.4f,1.05f,1.3f);
        rotar(0,1,0,180);
        escalar(0.05f,0.005f,0.5f);
        manillas.dibujar(new GLES20());
        popMatrix();

        pushMatrix();
        transladar(0.47f,1.08f,1.3f);
        rotar(0,1,0,90);
        escalar(0.06f,0.008f,0.5f);
        manillas1.dibujar(new GLES20());
        popMatrix();

        rotacion += 2.5f;
    }
    private void invocarFrustrum() {
        Matrix.frustumM(matrizProyeccion, 0, -relacionAspecto, relacionAspecto,
                -1, 1, 1, 30);

        Matrix.setLookAtM(matrizVista, 0,
                0, 0, 2,
                0, 0, 0,
                0, 1, 0);

        Matrix.multiplyMM(matrizTemp, 0, matrizProyeccion, 0, matrizVista, 0);
        System.arraycopy(matrizTemp, 0, matrizProyeccion, 0, matrizTemp.length);
    }

    private void invocarMatrices() {
        Matrix.setIdentityM(matrizModelo, 0);
        Matrix.multiplyMM(matrizTemp, 0, matrizProyeccion, 0, matrizModelo, 0);
        System.arraycopy(matrizTemp, 0, matrizProyeccion, 0, matrizTemp.length);
    }

    private void resetearMatrices() {
        invocarFrustrum();
        invocarMatrices();
        Matrix.setIdentityM(matrizModelo, 0);
    }


    public void pushMatrix() {
        System.arraycopy(matrizModelo, 0, stackModelo, 0, stackModelo.length);
        System.arraycopy(matrizVista, 0, stackVista, 0, stackVista.length);
        System.arraycopy(matrizProyeccion, 0, stackProyeccion, 0, stackProyeccion.length);
    }

    public void popMatrix() {
        System.arraycopy(stackModelo, 0, matrizModelo, 0, matrizModelo.length);
        System.arraycopy(stackVista, 0, matrizVista, 0, matrizVista.length);
        System.arraycopy(stackProyeccion, 0, matrizProyeccion, 0, matrizProyeccion.length);
    }

    private void rotar(float x, float y, float z, float anguloRot) {
        Matrix.rotateM(matrizModelo, 0, anguloRot, x, y, z);
    }

    private void escalar(float x, float y, float z) {
        Matrix.scaleM(matrizModelo, 0, x, y, z);
    }

    private void transladar(float x, float y, float z) {
        Matrix.translateM(matrizModelo, 0, x, y, z);
    }

}
