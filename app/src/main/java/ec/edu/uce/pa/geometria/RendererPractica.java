package ec.edu.uce.pa.geometria;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class RendererPractica implements GLSurfaceView.Renderer {
    private Cilindro cilindro;
    private CilindroTextura cilindroTextura;
    private ConoColor conoColor;
    private Cono cono;
    private ConoTextura conoTextura;
    private CuboColores cuboColores;
    private Cubo cubo;
    private Cuadrado cuadrado;
    private EsferaColor elipsoide, esfera;
    private Piramide piramide;
    private Plano plano;
    private PrismaCuadrangular prismaCuadrangular;
    private PrismaTriangular prismaTriangular;
    private Dona dona;
    private Context context;
    private int [] arrayTextura = new int[1];
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

    public RendererPractica(Context contexto) {
        this.context = contexto;
        cilindro= new Cilindro(20, 20, 1, 2, contexto, matrizProyeccion,
                matrizVista,matrizModelo);
        cilindroTextura= new CilindroTextura(20, 20, 1, 2, contexto,
                matrizProyeccion, matrizVista,matrizModelo);
        conoColor=new ConoColor(20,1,2,contexto,matrizProyeccion,matrizVista,matrizModelo);
        cono=new Cono(20,1,2,contexto,matrizProyeccion,matrizVista,matrizModelo,colorAzul);
        conoTextura=new ConoTextura(20,1,2,contexto,matrizProyeccion,matrizVista,
                matrizModelo);
        piramide= new Piramide(context,matrizProyeccion,matrizVista,matrizModelo);
        elipsoide=new EsferaColor(20, 20, 1, 2.5f, contexto,
                matrizProyeccion, matrizVista, matrizModelo);
        esfera=new EsferaColor(20, 20, 1, 1, contexto,
                matrizProyeccion, matrizVista, matrizModelo);
        cuboColores=new CuboColores(context, matrizProyeccion, matrizVista, matrizModelo);
        cubo=new Cubo(context, matrizProyeccion, matrizVista, matrizModelo,colorRojo);
        cuadrado = new Cuadrado(contexto,matrizProyeccion,matrizVista,matrizModelo);
        prismaCuadrangular= new PrismaCuadrangular(context,matrizProyeccion,matrizVista,matrizModelo);
        prismaTriangular=new PrismaTriangular(context,matrizProyeccion,matrizVista,matrizModelo);
        plano = new Plano(contexto,matrizProyeccion,matrizVista,matrizModelo,colorVerde);
        dona = new Dona(20, 20, 1.0f, 0.3f, contexto, matrizProyeccion, matrizVista, matrizModelo,colorVerde);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig eglConfig) {
        gl.glClearColor(0.2f, 0.2f, 0.2f, 1.0f);
        gl.glEnable(gl.GL_DEPTH_TEST);


    }

    @Override
    public void onSurfaceChanged(GL10 gl, int ancho, int alto) {
        gl.glViewport(0, 0, ancho, alto);
        relacionAspecto = (float) ancho / (float) alto;
        invocarFrustrum();
        invocarMatrices();
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        gl.glClear(gl.GL_COLOR_BUFFER_BIT | gl.GL_DEPTH_BUFFER_BIT);
        Matrix.setIdentityM(matrizModelo, 0);

        transladar(0,0,-2);
        pushMatrix();
        rotar(1,1,1,rotacion);
            escalar(0.5f,0.5f,0.5f);
        cubo.dibujar(new GLES20());
        popMatrix();

        //cilindro.dibujar(new GLES20());
        //cilindroTextura.dibujar(new GLES20());
        //conoTextura.dibujar(new GLES20());
        pushMatrix();
            transladar(0,2,0);
            rotar(-1,1,-1,rotacion);
            escalar(0.5f,0.5f,0.5f);
            plano.dibujar(new GLES20());
        popMatrix();
//        pushMatrix();
//            transladar(0,4,0);
//            rotar(-1,1,-1,rotacion);
//            escalar(0.5f,0.5f,0.5f);
//            piramide.dibujar(new GLES20());
//        popMatrix();
//        pushMatrix();
//            transladar(0,-2,0);
//            rotar(-1,1,-1,rotacion);
//            cuadrado.dibujar(new GLES20());
//        popMatrix();
//        elipsoide.dibujar(new GLES20());
//        esfera.dibujar(new GLES20());
//        prismaTriangular.dibujar(new GLES20());
//        prismaCuadrangular.dibujar(new GLES20());


        //cilindro.dibujar(new GLES20());               //FUNCIONA
        //cono.dibujar(new GLES20());                   //FUNCIONA
        //piramide.dibujar(new GLES20());               //FUNCIONA
        //cilindroTextura.dibujar(new GLES20());        //FUNCIONA
        //conoTextura.dibujar(new GLES20());            //FUNCIONA
        //elipsoide.dibujar(new GLES20());              //FUNCIONA
        //esfera.dibujar(new GLES20());                 //FUNCIONA
        //cubo.dibujar(new GLES20());
        //cuboColor.dibujar(new GLES20());
        //cuadrado.dibujar(new GLES20());               //FUNCIONA
        //prismaTriangular.dibujar(new GLES20());       //FUNCIONA
        //prismaCuadrangular.dibujar(new GLES20());     //FUNCIONA
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
