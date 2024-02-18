package ec.edu.uce.pa.geometria;

import android.content.Context;
import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import ec.edu.uce.pa.R;
import ec.edu.uce.pa.utilidades.Funciones;


public class Esfera {
    private FloatBuffer bufferVertices;
    private FloatBuffer bufferColores;
    private FloatBuffer bufferNormales;
    private FloatBuffer bufferTexturas;

    private final static int byteFlotante = 4;
    private final static int comPorVertices = 3;
    private final static int compPorTexturas = 2;
    private final static int STRIDE = (comPorVertices + compPorTexturas) * byteFlotante;
    private int franjas, cortes;
    private int[] arrayTexturas;
    private Context context;
    private float[] mProyeccion;
    private float[] mVista;
    private float[] mModelo;

    private ByteBuffer bufferIndice;

    //Necesitamos las franjas y cortes que vamos a dibujar
    public Esfera(int franjas, int cortes, float radio, float ejePolar, Context context, float[] mProyeccion, float[] mVista, float[] mModelo) {
        this.context = context;
        this.mProyeccion = mProyeccion;
        this.mVista = mVista;
        this.mModelo = mModelo;
        this.franjas = franjas;
        this.cortes = cortes;

        float[] vertices;
        float[] colores;
        float[] normales;

        float[] texturas;

        int iVertice = 0;
        int iColor = 0;
        int iNormal = 0;

        int iTextura = 0;

        //Tamaño de los vertices

        vertices = new float[3 * ((cortes * 2 + 2) * franjas)];
        colores = new float[4 * ((cortes * 2 + 2) * franjas)];
        normales = new float[3 * ((cortes * 2 + 2) * franjas)];

        texturas = new float[2 * ((cortes * 2 + 2) * franjas)];

        int i, j;

        // Bucle para construir las franjas de la esfera
        // Latitudes
        //Vamso recorriendo cada uno de las franjas
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
                //Empezamos con -2.0 porque estamos dibujando desde abajo hacia arriba
                float theta = (float) (-2.0f * Math.PI * j * (1.0 / (cortes - 1)));
                cosTheta = (float) Math.cos(theta);
                sinTheta = (float) Math.sin(theta);

                // Dibujar la esfera en duplas, pares de puntos
                vertices[iVertice + 0] = radio * cosPhi0 * cosTheta;          //x
                vertices[iVertice + 1] = radio * (sinPhi0 * ejePolar);    //y
                vertices[iVertice + 2] = radio * (cosPhi0 * sinTheta);        //z
//                vertices[iVertice+3] = 2.0f;

                vertices[iVertice + 3] = radio * cosPhi1 * cosTheta;          //x'
                vertices[iVertice + 4] = radio * (sinPhi1 * ejePolar);    //y'
                vertices[iVertice + 5] = radio * (cosPhi1 * sinTheta);        //z'
//                vertices[iVertice+6] = 2.0f;


                texturas[iTextura + 0] = j * (1.f / (franjas - 1));     //coordenada s franjas
                texturas[iTextura + 1] = -(i + 0) * (1.f / (cortes - 1));  //coordenada t cortes
                texturas[iTextura + 2] = j * (1.f / (franjas - 1));      //coordenada s
                texturas[iTextura + 3] = -(i + 1) * (1.f / (cortes - 1));    //t'


                colores[iColor + 0] = 0.25f;
                colores[iColor + 1] = 0.25f;
                colores[iColor + 2] = 0.0f;
                colores[iColor + 3] = 1.0f;

                colores[iColor + 4] = 1.0f;
                colores[iColor + 5] = 1.5f;
                colores[iColor + 6] = 0.5f;
                colores[iColor + 7] = 1.0f;

                //2 triangulos por 3 componentes

                iVertice += 2 * 3;
                iNormal += 2 * 3;
                iColor += 2 * 4;
                //dos coordenadas por dos puntos
                iTextura += 2 * 2;


            }

            //CREAR TRIANGULOS DEGENERADOS
            vertices[iVertice + 0] = vertices[iVertice + 3];
            vertices[iVertice + 3] = vertices[iVertice - 3];
            vertices[iVertice + 1] = vertices[iVertice + 4];
            vertices[iVertice + 4] = vertices[iVertice - 2];
            vertices[iVertice + 2] = vertices[iVertice + 5];
            vertices[iVertice + 5] = vertices[iVertice - 1];
        }

        bufferVertices = Funciones.generarFloatBuffer(vertices);
        bufferColores = Funciones.generarFloatBuffer(colores);
        bufferTexturas = Funciones.generarFloatBuffer(texturas);
    }

    public void dibujar(GLES20 gl) {
//        Configuracion Vertex Shader
        int vertexShader = 0;
        int fragmentShader = 0;
        String sourceVs = null;
        String sourceFs = null;

        sourceVs = Funciones.leerArchivo(R.raw.colr_vertex_shader, context);
        vertexShader = Funciones.crearShader(gl.GL_VERTEX_SHADER, sourceVs,gl);

        //        Configuracion Fragment Shader
        sourceFs = Funciones.leerArchivo(R.raw.color_fragment_shader, context);
        fragmentShader = Funciones.crearShader(gl.GL_FRAGMENT_SHADER, sourceFs, gl);

        int programa = Funciones.crearPrograma(vertexShader, fragmentShader,gl);
        gl.glUseProgram(programa);

//        11. Lectura de parametros desde el renderer
        int idVertexShader = gl.glGetAttribLocation(programa, "posVertexShader");
        gl.glVertexAttribPointer(idVertexShader,
                comPorVertices,
                gl.GL_FLOAT,
                false,
                0,
                bufferVertices);
        gl.glEnableVertexAttribArray(idVertexShader);

//        12. Lectura de parametros desde el renderer Fragment Shader

        int idFragmentShader = gl.glGetAttribLocation(programa, "colorVertex");
        gl.glVertexAttribPointer(idFragmentShader,
                4,
                gl.GL_FLOAT,
                false,
                0,
                bufferColores);
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

        gl.glDrawArrays(gl.GL_TRIANGLE_FAN, 0, franjas * cortes * 2);

        gl.glFrontFace(gl.GL_CCW);
        gl.glDisableVertexAttribArray(idVertexShader);
        gl.glDisableVertexAttribArray(idFragmentShader);

        Funciones.liberarShader(programa, vertexShader, fragmentShader);

    }



}