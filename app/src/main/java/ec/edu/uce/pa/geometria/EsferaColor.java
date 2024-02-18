package ec.edu.uce.pa.geometria;

import android.content.Context;
import android.opengl.GLES20;

import java.nio.FloatBuffer;

import ec.edu.uce.pa.R;
import ec.edu.uce.pa.utilidades.Funciones;

public class EsferaColor {

    private FloatBuffer bufferVertices, bufferNormales, bufferColores;
    private static final int byteFlotante = 4;
    private static final int compPorVertice = 3;
    private static final int compPorColor = 3;
    private final static int STRIDE = (compPorVertice + compPorColor) * byteFlotante;

    private int cortes, franjas;
    private float[] matrizProyeccion, matrizVista, matrizModelo;
    private Context contexto;

    public EsferaColor(int franjas, int cortes, float radio, float ejePolar, Context contexto,
                       float[] matrizProyeccion, float[] matrizVista, float[] matrizModelo) {
        this.cortes = cortes;
        this.franjas = franjas;
        this.matrizVista = matrizVista;
        this.matrizModelo = matrizModelo;
        this.matrizProyeccion = matrizProyeccion;
        this.contexto = contexto;

        float[] vertices;
        float[] normales;
        float[] colores;

        int iVertice = 0;
        int iNormal = 0;
        int iColor =0;

        vertices = new float[3 * ((cortes * 2 + 2) * franjas)];
        colores = new float[3 * ((cortes * 2 + 2) * franjas)];
        normales = new float[3 * ((cortes * 2 + 2) * franjas)];

        int i, j;

        // Bucle para construir las franjas de la esfera
        // Latitudes
        for (i = 0; i < franjas; i++) {
            //empieza en -90 grados (-1.57 radianes) incrementa hasta +90 grados  (o +1.57 radianes)
            //Phi   --> angulo de latitud
            //Theta --> angulo de longitud

            //Valor del angulo para el primer cìrculo
            float phi0 = (float) Math.PI * ((i + 0) * (1.0f / (franjas)) - 0.5f);
            float cosPhi0 = (float) Math.cos(phi0);
            float sinPhi0 = (float) Math.sin(phi0);

            //Valor del angulo para el segundo cìrculo
            float phi1 = (float) Math.PI * ((i + 1) * (1.0f / (franjas)) - 0.5f);
            float cosPhi1 = (float) Math.cos(phi1);
            float sinPhi1 = (float) Math.sin(phi1);

            float cosTheta, sinTheta;
            //Bucle para construir los cortes de la esfera
            //Longitudes
            for (j = 0; j < cortes; j++) {
                float theta = (float) (-2.0f * Math.PI * j * (1.0 / (cortes - 1)));
                cosTheta = (float) Math.cos(theta);
                sinTheta = (float) Math.sin(theta);

                // Dibujar la esfera en duplas, pares de puntos
                vertices[iVertice + 0] = radio * cosPhi0 * cosTheta;          //x
                vertices[iVertice + 1] = radio * (sinPhi0 * ejePolar);    //y
                vertices[iVertice + 2] = (radio * (cosPhi0 * sinTheta));        //z

                vertices[iVertice + 3] = radio * cosPhi1 * cosTheta;          //x'
                vertices[iVertice + 4] = radio * (sinPhi1 * ejePolar);    //y'
                vertices[iVertice + 5] = (radio * (cosPhi1 * sinTheta));        //z'

                normales[iNormal + 0] = cosPhi0 * cosTheta;          //x
                normales[iNormal + 1] = (sinPhi0);    //y
                normales[iNormal + 2] = (cosPhi0 * sinTheta);        //z

                normales[iNormal + 3] = cosPhi1 * cosTheta;          //x'
                normales[iNormal + 4] = (sinPhi1);    //y'
                normales[iNormal + 5] = (cosPhi1 * sinTheta);        //z'

                colores[iColor+0] = 1.0f;
                colores[iColor+1] = 1.0f;
                colores[iColor+2] = 0.0f;
                colores[iColor+3] = 1.0f;

                colores[iColor+4] = 0.3f;
                colores[iColor+5] = 0.5f;
                //colores[iColor+6] = 0.0f;
                //colores[iColor+7] = 1.0f;

                iVertice += 2 * 3;
                iNormal += 2 * 3;
                iColor += 2 * 3;
            }

            vertices[iVertice + 0] = vertices[iVertice + 3];
            vertices[iVertice + 3] = vertices[iVertice - 3];
            vertices[iVertice + 1] = vertices[iVertice + 4];
            vertices[iVertice + 4] = vertices[iVertice - 2];
            vertices[iVertice + 2] = vertices[iVertice + 5];
            vertices[iVertice + 5] = vertices[iVertice - 1];
        }
        bufferVertices = Funciones.generarFloatBuffer(vertices);
        bufferNormales = Funciones.generarFloatBuffer(normales);
        bufferColores = Funciones.generarFloatBuffer(colores);
    }

    public void dibujar(GLES20 gl) {
        int vertexShader;
        int fragmentShader;

        String sourceVS;
        String sourceFS;

        sourceVS = Funciones.leerArchivo(R.raw.mvp_color_vertex_shader, contexto);
        vertexShader = Funciones.crearShader(gl.GL_VERTEX_SHADER, sourceVS, gl);

        sourceFS = Funciones.leerArchivo(R.raw.color_fragment_shader, contexto);
        fragmentShader = Funciones.crearShader(gl.GL_FRAGMENT_SHADER, sourceFS, gl);

        int programa = Funciones.crearPrograma(vertexShader, fragmentShader, gl);
        gl.glUseProgram(programa);

        bufferVertices.position(0);
        int idVertexShader = gl.glGetAttribLocation(programa, "posVertexShader");
        gl.glVertexAttribPointer(idVertexShader,
                compPorVertice,
                gl.GL_FLOAT,
                false,
                0,
                bufferVertices);
        gl.glEnableVertexAttribArray(idVertexShader);

        int idFragmentShader = gl.glGetAttribLocation(programa,"colorVertex");

        //gl.glUniform4f(idFragmentShader,1.0f,0.6f,0.5f,1); // Color uniforme
        gl.glVertexAttribPointer(idFragmentShader, compPorColor, gl.GL_FLOAT,false,
                0, bufferColores);
        gl.glEnableVertexAttribArray(idFragmentShader);

        // Obtener la ubicación del uniforme "matrizProjection, matrizView, matrizModel" en el shader
        int idPosMatrizProy = gl.glGetUniformLocation(programa, "matrizProjection");
        gl.glUniformMatrix4fv(idPosMatrizProy, 1, false, matrizProyeccion, 0);
        int idPosMatrizView = gl.glGetUniformLocation(programa, "matrizView");
        gl.glUniformMatrix4fv(idPosMatrizView, 1, false, matrizVista, 0);
        int idPosMatrizModel = gl.glGetUniformLocation(programa, "matrizModel");
        gl.glUniformMatrix4fv(idPosMatrizModel, 1, false, matrizModelo, 0);

        gl.glFrontFace(gl.GL_CW);
        gl.glDrawArrays(gl.GL_TRIANGLE_STRIP, 0, franjas * cortes * 2);
        gl.glFrontFace(gl.GL_CCW);
        gl.glDisableVertexAttribArray(idVertexShader);
        gl.glDisableVertexAttribArray(idFragmentShader);

        Funciones.liberarShader(programa, vertexShader, fragmentShader);
    }

}
