package ec.edu.uce.pa.geometria;

import android.content.Context;
import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import ec.edu.uce.pa.R;
import ec.edu.uce.pa.utilidades.Funciones;


public class CuboTextura {
    private FloatBuffer bufferVertices;
    private FloatBuffer bufferColores;
    private FloatBuffer bufferTexturas;

    private final static int comPorVertices = 3;
    private final static int compPorTextura = 2;
    private Context contexto;
    private float[] matrizProyeccion,matrizVista, matrizModelo;

    private ByteBuffer bufferIndice;

    //Necesitamos las franjas y cortes que vamos a dibujar
    public CuboTextura(Context contexto, float[] matrizProyeccion, float[] matrizVista, float[] matrizModelo) {
        this.matrizVista = matrizVista;
        this.matrizModelo = matrizModelo;
        this.matrizProyeccion = matrizProyeccion;
        this.contexto = contexto;

        float cuboVertices[] = {
                // cara frontal
                -1.0f,  1.0f, -1.0f,  // arriba-izquierda
                1.0f,  1.0f, -1.0f,  // arriba-derecha
                1.0f, -1.0f, -1.0f,  // abajo-derecha
                -1.0f, -1.0f, -1.0f,  // abajo-izquierda
                // cara trasera
                1.0f,  1.0f,  1.0f,  // arriba-izquierda
                -1.0f,  1.0f,  1.0f,  // arriba-derecha
                -1.0f, -1.0f,  1.0f,  // abajo-derecha
                1.0f, -1.0f,  1.0f,  // abajo-izquierda
                // cara superior
                -1.0f,  1.0f,  1.0f,  // arriba-izquierda
                1.0f,  1.0f,  1.0f,  // arriba-derecha
                1.0f,  1.0f, -1.0f,  // abajo-derecha
                -1.0f,  1.0f, -1.0f,  // abajo-izquierda
                // cara inferior
                -1.0f, -1.0f, -1.0f,  // arriba-izquierda
                1.0f, -1.0f, -1.0f,  // arriba-derecha
                1.0f, -1.0f,  1.0f,  // abajo-derecha
                -1.0f, -1.0f,  1.0f,  // abajo-izquierda
                // cara derecha
                1.0f,  1.0f, -1.0f,  // arriba-izquierda
                1.0f,  1.0f,  1.0f,  // arriba-derecha
                1.0f, -1.0f,  1.0f,  // abajo-derecha
                1.0f, -1.0f, -1.0f,  // abajo-izquierda
                // cara izquierda
                -1.0f,  1.0f,  1.0f,  // arriba-izquierda
                -1.0f,  1.0f, -1.0f,  // arriba-derecha
                -1.0f, -1.0f, -1.0f,  // abajo-derecha
                -1.0f, -1.0f,  1.0f   // abajo-izquierda
        };

        float cuboTexura[] = {
                // cara frontal
                1.0f, 0.0f,  // arriba-izquierda
                0.0f, 0.0f,  // arriba-derecha
                0.0f, 1.0f,  // abajo-derecha
                1.0f, 1.0f,  // abajo-izquierda
                // cara trasera
                0.0f, 0.0f,  // arriba-izquierda
                1.0f, 0.0f,  // arriba-derecha
                1.0f, 1.0f,  // abajo-derecha
                0.0f, 1.0f,  // abajo-izquierda
                // cara superior
                0.0f, 1.0f,  // arriba-izquierda
                1.0f, 1.0f,  // arriba-derecha
                1.0f, 0.0f,  // abajo-derecha
                0.0f, 0.0f,  // abajo-izquierda
                // cara inferior
                1.0f, 1.0f,  // arriba-izquierda
                0.0f, 1.0f,  // arriba-derecha
                0.0f, 0.0f,  // abajo-derecha
                1.0f, 0.0f,  // abajo-izquierda
                // cara derecha
                0.0f, 0.0f,  // arriba-izquierda
                1.0f, 0.0f,  // arriba-derecha
                1.0f, 1.0f,  // abajo-derecha
                0.0f, 1.0f,  // abajo-izquierda
                // cara izquierda
                1.0f, 0.0f,  // arriba-izquierda
                0.0f, 0.0f,  // arriba-derecha
                0.0f, 1.0f,  // abajo-derecha
                1.0f, 1.0f   // abajo-izquierda
        };


        bufferVertices = Funciones.generarFloatBuffer(cuboVertices);
        bufferTexturas = Funciones.generarFloatBuffer(cuboTexura);
    }

    public void dibujar(GLES20 gl) {
//        Configuracion Vertex Shader
        int vertexShader = 0;
        int fragmentShader = 0;
        String sourceVs = null;
        String sourceFs = null;

        sourceVs = Funciones.leerArchivo(R.raw.textura_vertex_shader, contexto);
        vertexShader = Funciones.crearShader(gl.GL_VERTEX_SHADER, sourceVs,gl);

        //        Configuracion Fragment Shader
        sourceFs = Funciones.leerArchivo(R.raw.textura_fragment_shader, contexto);
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

        gl.glDrawArrays(gl.GL_TRIANGLES, 0, 36);

        gl.glFrontFace(gl.GL_CCW);
        gl.glDisableVertexAttribArray(idVertexShader);
        gl.glDisableVertexAttribArray(idFragmentShader);

        Funciones.liberarShader(programa, vertexShader, fragmentShader);

    }



}