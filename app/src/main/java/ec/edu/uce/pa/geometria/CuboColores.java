package ec.edu.uce.pa.geometria;

import android.content.Context;
import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import ec.edu.uce.pa.R;
import ec.edu.uce.pa.utilidades.Funciones;


public class CuboColores {
    private FloatBuffer bufferVertices;
    private FloatBuffer bufferColores;

    private final static int comPorVertices = 3;

    private Context context;
    private float[] mProyeccion;
    private float[] mVista;
    private float[] mModelo;

    private ByteBuffer bufferIndice;

    //Necesitamos las franjas y cortes que vamos a dibujar
    public CuboColores(Context context, float[] mProyeccion, float[] mVista, float[] mModelo) {
        this.context = context;
        this.mProyeccion = mProyeccion;
        this.mVista = mVista;
        this.mModelo = mModelo;


        float[] texturas;

        int iVertice = 0;
        int iColor = 0;
        int iNormal = 0;

        int iTextura = 0;

        //Tamaño de los vertices


        float[] vertices = {
                -0.5f, 0.5f, 0.5f, //0
                0.5f, 0.5f, 0.5f,  //1
                0.5f, -0.5f, 0.5f, //2
                -0.5f, -0.5f, 0.5f, //3
                -0.5f, 0.5f, 0.5f, //0
                0.5f, -0.5f, 0.5f, //2

                0.5f, 0.5f, -0.5f, //4
                -0.5f, 0.5f, -0.5f, //5
                -0.5f, -0.5f, -0.5f, //6
                0.5f, -0.5f, -0.5f, //7
                0.5f, 0.5f, -0.5f, //4
                -0.5f, -0.5f, -0.5f, //6


                -0.5f, 0.5f, -0.5f, //8
                0.5f, 0.5f, -0.5f, //9
                0.5f, 0.5f, 0.5f, //10
                -0.5f, 0.5f, 0.5f, //11
                -0.5f, 0.5f, -0.5f, //8
                0.5f, 0.5f, 0.5f, //10


                -0.5f, -0.5f, 0.5f, //12
                0.5f, -0.5f, 0.5f, //13
                0.5f, -0.5f, -0.5f,//14
                -0.5f, -0.5f, -0.5f,//15
                -0.5f, -0.5f, 0.5f, //12
                0.5f, -0.5f, -0.5f,//14


                0.5f, 0.5f, 0.5f,
                0.5f, 0.5f, -0.5f,
                0.5f, -0.5f, -0.5f,
                0.5f, -0.5f, 0.5f,
                0.5f, 0.5f, 0.5f,
                0.5f, -0.5f, -0.5f,


                -0.5f, 0.5f, -0.5f,
                -0.5f, 0.5f, 0.5f,
                -0.5f, -0.5f, 0.5f,
                -0.5f, -0.5f, -0.5f,
                -0.5f, 0.5f, -0.5f,
                -0.5f, -0.5f, 0.5f,

        };
        float[] colores = {
                // Front face
                1.0f, 0.0f, 0.0f, 1.0f, // Red
                1.0f, 0.0f, 0.0f, 1.0f, // Red
                1.0f, 0.0f, 0.0f, 1.0f, // Red
                1.0f, 0.0f, 0.0f, 1.0f, // Red
                1.0f, 0.0f, 0.0f, 1.0f, // Red
                1.0f, 0.0f, 0.0f, 1.0f, // Red
                // Back face
                0.0f, 1.0f, 0.0f, 1.0f, // Green
                0.0f, 1.0f, 0.0f, 1.0f, // Green
                0.0f, 1.0f, 0.0f, 1.0f, // Green
                0.0f, 1.0f, 0.0f, 1.0f, // Green
                0.0f, 1.0f, 0.0f, 1.0f, // Green
                0.0f, 1.0f, 0.0f, 1.0f, // Green
                // Top face
                0.0f, 0.0f, 1.0f, 1.0f, // Blue
                0.0f, 0.0f, 1.0f, 1.0f, // Blue
                0.0f, 0.0f, 1.0f, 1.0f, // Blue
                0.0f, 0.0f, 1.0f, 1.0f, // Blue
                0.0f, 0.0f, 1.0f, 1.0f, // Blue
                0.0f, 0.0f, 1.0f, 1.0f, // Blue
                // Bottom face
                1.0f, 1.0f, 0.0f, 1.0f, // Yellow
                1.0f, 1.0f, 0.0f, 1.0f, // Yellow
                1.0f, 1.0f, 0.0f, 1.0f, // Yellow
                1.0f, 1.0f, 0.0f, 1.0f, // Yellow
                1.0f, 1.0f, 0.0f, 1.0f, // Yellow
                1.0f, 1.0f, 0.0f, 1.0f, // Yellow
                // Right face
                1.0f, 0.0f, 1.0f, 1.0f, // Magenta
                1.0f, 0.0f, 1.0f, 1.0f, // Magenta
                1.0f, 0.0f, 1.0f, 1.0f, // Magenta
                1.0f, 0.0f, 1.0f, 1.0f, // Magenta
                1.0f, 0.0f, 1.0f, 1.0f, // Magenta
                1.0f, 0.0f, 1.0f, 1.0f, // Magenta
                // Left face
                0.0f, 1.0f, 1.0f, 1.0f, // Cyan
                0.0f, 1.0f, 1.0f, 1.0f, // Cyan
                0.0f, 1.0f, 1.0f, 1.0f, // Cyan
                0.0f, 1.0f, 1.0f, 1.0f, // Cyan
                0.0f, 1.0f, 1.0f, 1.0f, // Cyan
                0.0f, 1.0f, 1.0f, 1.0f, // Cyan
        };

        bufferVertices = Funciones.generarFloatBuffer(vertices);
        bufferColores = Funciones.generarFloatBuffer(colores);
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

        gl.glDrawArrays(gl.GL_TRIANGLES, 0, 36);

        gl.glFrontFace(gl.GL_CCW);
        gl.glDisableVertexAttribArray(idVertexShader);
        gl.glDisableVertexAttribArray(idFragmentShader);

        Funciones.liberarShader(programa, vertexShader, fragmentShader);

    }



}