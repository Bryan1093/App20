package ec.edu.uce.pa.evaluacion2;

import android.content.Context;
import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import ec.edu.uce.pa.R;
import ec.edu.uce.pa.utilidades.Funciones;

public class PlanoTextura {
    private FloatBuffer bufferVertices;
    private FloatBuffer bufferTexturas;
    private ByteBuffer bufferIndice;
    private final static int compPorVertice = 2;
    private final static int compPorTextura = 2;
    private float[] matrizProyeccion,matrizVista, matrizModelo;

    private Context contexto;

    public PlanoTextura(Context contexto, float[] matrizProyeccion, float[] matrizVista, float[] matrizModelo ) { // el render se encargara de manejar la matriz
        this.matrizVista = matrizVista;
        this.matrizModelo = matrizModelo;
        this.matrizProyeccion = matrizProyeccion;
        this.contexto = contexto;
        float[] vertices ={//TEXTURA
                1,1,//0
                1,-1,//1
                -1,-1,//2
                -1,1//3
        };
        float[] texturas ={
                1,0,
                1,1,
                0,1,
                0,0
        };
        byte[] indices = {
                0,1,2,
                0,2,3
        };

        bufferVertices = Funciones.generarFloatBuffer(vertices);
        bufferTexturas = Funciones.generarFloatBuffer(texturas);
        bufferIndice = Funciones.generarByteBuffer(indices);

    }

    public void dibujar(GLES20 gl) {
        //1.Crear vertex Shader
        String sourceVS = Funciones.leerArchivo(R.raw.textura_vertex_shader, contexto);
        int vertexShader = Funciones.crearShader(gl.GL_VERTEX_SHADER, sourceVS, gl);

        String sourceFS = Funciones.leerArchivo(R.raw.textura_fragment_shader, contexto);
        int fragmentShader = Funciones.crearShader(gl.GL_FRAGMENT_SHADER, sourceFS, gl);

        int programa = Funciones.crearPrograma(vertexShader, fragmentShader, gl);
        gl.glUseProgram(programa);



        bufferVertices.position(0);
        //11. Lectura de parámetros desde el renderer vertexShader
        int idVertexShader = gl.glGetAttribLocation(programa, "posVertexShader");
        gl.glVertexAttribPointer(idVertexShader,
                compPorVertice, gl.GL_FLOAT,
                false, 0, bufferVertices);
        gl.glEnableVertexAttribArray(idVertexShader);

        //12. Lectura de parámetros desde el renderer FragmentShader
        bufferVertices.position(4);
        int idFragmentShader = gl.glGetAttribLocation(programa, "texturaVertex");
        gl.glVertexAttribPointer(idFragmentShader,
                compPorTextura, gl.GL_FLOAT,
                false, 0, bufferTexturas);
        gl.glEnableVertexAttribArray(idFragmentShader);

        //MVP
        int idPosMatrizProy = gl.glGetUniformLocation(programa, "matrizProjection");
        gl.glUniformMatrix4fv(idPosMatrizProy, 1, false, matrizProyeccion, 0);

        int idPosMatrizView = gl.glGetUniformLocation(programa, "matrizView");
        gl.glUniformMatrix4fv(idPosMatrizView, 1, false, matrizVista, 0);

        int idPosMatrizModel = gl.glGetUniformLocation(programa, "matrizModel");
        gl.glUniformMatrix4fv(idPosMatrizModel, 1, false, matrizModelo, 0);



        gl.glFrontFace(gl.GL_CW);
        gl.glDrawElements(gl.GL_TRIANGLES,6,gl.GL_UNSIGNED_BYTE, bufferIndice); //DRAW ELEMENTS;


        gl.glDisableVertexAttribArray(idVertexShader);
        gl.glDisableVertexAttribArray(idFragmentShader);
        Funciones.liberarShader(programa, vertexShader, fragmentShader);
    }
}
