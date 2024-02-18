package ec.edu.uce.pa.geometria;

import android.content.Context;
import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import ec.edu.uce.pa.R;
import ec.edu.uce.pa.utilidades.Funciones;

public class Dona {
    private FloatBuffer bufferVertices;
    private FloatBuffer bufferColores;
    private FloatBuffer bufferNormales;
    private FloatBuffer bufferTexturas;

    private final static int byteFlotante = 4;
    private final static int comPorVertices = 3;
    private final static int compPorTexturas = 2;
    private int franjas, cortes;
    private int[] arrayTexturas;
    private Context context;
    private float[] mProyeccion;
    private float[] mVista;
    private float[] mModelo;
    private float[] colorDona;

    private ByteBuffer bufferIndice;

    // Necesitamos las franjas y cortes que vamos a dibujar
    public Dona(int franjas, int cortes, float radioMayor, float radioMenor, Context context, float[] mProyeccion,
                float[] mVista, float[] mModelo, float[] colorDona) {
        this.context = context;
        this.mProyeccion = mProyeccion;
        this.mVista = mVista;
        this.mModelo = mModelo;
        this.franjas = franjas;
        this.cortes = cortes;
        this.colorDona = colorDona;

        float[] vertices;
        float[] colores;
        float[] normales;

        float[] texturas;

        int iVertice = 0;
        int iColor = 0;
        int iNormal = 0;

        int iTextura = 0;

        // Tamaño de los vertices
        vertices = new float[3 * ((cortes * 2 + 2) * franjas)];
        colores = new float[4 * ((cortes * 2 + 2) * franjas)];
        normales = new float[3 * ((cortes * 2 + 2) * franjas)];
        texturas = new float[2 * ((cortes * 2 + 2) * franjas)];

        int i, j;

        // Bucle para construir las franjas de la dona
        for (i = 0; i < franjas; i++) {
            float phi0 = (float) (2.0 * Math.PI * (float) i / franjas);
            float cosPhi0 = (float) Math.cos(phi0);
            float sinPhi0 = (float) Math.sin(phi0);

            float phi1 = (float) (2.0 * Math.PI * (float) (i + 1) / franjas);
            float cosPhi1 = (float) Math.cos(phi1);
            float sinPhi1 = (float) Math.sin(phi1);

            for (j = 0; j < cortes; j++) {
                float theta = (float) (2.0 * Math.PI * (float) j / cortes);
                float cosTheta = (float) Math.cos(theta);
                float sinTheta = (float) Math.sin(theta);

                // Punto en el círculo mayor (radioMayor)
                vertices[iVertice + 0] = (radioMayor + radioMenor * cosPhi0) * cosTheta;
                vertices[iVertice + 1] = (radioMayor + radioMenor * cosPhi0) * sinTheta;
                vertices[iVertice + 2] = radioMenor * sinPhi0;

                // Punto en el círculo menor (radioMenor)
                vertices[iVertice + 3] = (radioMayor + radioMenor * cosPhi1) * cosTheta;
                vertices[iVertice + 4] = (radioMayor + radioMenor * cosPhi1) * sinTheta;
                vertices[iVertice + 5] = radioMenor * sinPhi1;

                // Coordenadas de textura
                texturas[iTextura + 0] = (float) j / cortes;
                texturas[iTextura + 1] = (float) i / franjas;
                texturas[iTextura + 2] = (float) j / cortes;
                texturas[iTextura + 3] = (float) (i + 1) / franjas;

                iVertice += 2 * 3;
                iNormal += 2 * 3;
                iColor += 2 * 4;
                iTextura += 2 * 2;
            }
        }

        bufferVertices = Funciones.generarFloatBuffer(vertices);
        bufferColores = Funciones.generarFloatBuffer(colores);
        bufferTexturas = Funciones.generarFloatBuffer(texturas);
    }

    public void dibujar(GLES20 gl) {
        // Configuración Vertex Shader
        int vertexShader = 0;
        int fragmentShader = 0;
        String sourceVs = null;
        String sourceFs = null;

        sourceVs = Funciones.leerArchivo(R.raw.colr_vertex_shader, context);
        vertexShader = Funciones.crearShader(gl.GL_VERTEX_SHADER, sourceVs, gl);

        // Configuración Fragment Shader
        sourceFs = Funciones.leerArchivo(R.raw.color_fragment_shader, context);
        fragmentShader = Funciones.crearShader(gl.GL_FRAGMENT_SHADER, sourceFs, gl);

        int programa = Funciones.crearPrograma(vertexShader, fragmentShader, gl);
        gl.glUseProgram(programa);

        int idVertexShader = gl.glGetAttribLocation(programa, "posVertexShader");
        gl.glVertexAttribPointer(idVertexShader,
                comPorVertices,
                gl.GL_FLOAT,
                false,
                0,
                bufferVertices);
        gl.glEnableVertexAttribArray(idVertexShader);

        int idFragmentShader = gl.glGetAttribLocation(programa, "colorVertex");
        gl.glVertexAttrib4fv(idFragmentShader, colorDona, 0);
        gl.glEnableVertexAttribArray(idFragmentShader);

        int idPosMatrixProy = gl.glGetUniformLocation(programa,
                "matrizProjection");
        gl.glUniformMatrix4fv(idPosMatrixProy, 1,
                false, mProyeccion, 0);

        int idPosMatrixview = gl.glGetUniformLocation(programa,
                "matrizView");
        gl.glUniformMatrix4fv(idPosMatrixview, 1,
                false, mVista, 0);

        int idPosMatrixModel = gl.glGetUniformLocation(programa,
                "matrizModel");
        gl.glUniformMatrix4fv(idPosMatrixModel, 1,
                false, mModelo, 0);

        gl.glFrontFace(gl.GL_CW);

        gl.glDrawArrays(gl.GL_TRIANGLE_STRIP, 0, franjas * cortes * 2);

        gl.glFrontFace(gl.GL_CCW);
        gl.glDisableVertexAttribArray(idVertexShader);
        gl.glDisableVertexAttribArray(idFragmentShader);

        Funciones.liberarShader(programa, vertexShader, fragmentShader);
        }
}