package ec.edu.uce.pa.geometria;

import android.content.Context;
import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import ec.edu.uce.pa.R;
import ec.edu.uce.pa.utilidades.Funciones;

public class HexagonoTextura {
    private FloatBuffer bufferVertices;
    private ByteBuffer bufferIndice;
    private final static int byteFlotante = 4;
    private final static int compPorVertice = 4;
    private final static int compPorTextura = 2;
    private final static int STRIDE = (compPorVertice + compPorTextura) * byteFlotante;
    private float[] matrizProyeccion;

    private Context contexto;

    public HexagonoTextura(Context contexto, float[] matrizProyeccion) { // el render se encargara de manejar la matriz
        this.matrizProyeccion = matrizProyeccion;
        this.contexto = contexto;
        float varStride = 3.0f; /// profundidad de la componente varStride

        float[] vertices = {
                // X    Y    Z      W    R     G    B
                -0.6f, 1.0f, 0.0f, varStride, 0.3f, 0.0f,    //0
                -0.6f, -1.0f, 0.0f, varStride, 0.3f, 1.0f,    //1
                -1.0f, 0.0f, 0.0f, varStride, 0.0f, 0.5f,     //2

                -0.6f, 1.0f, 0.0f, varStride, 0.3f, 0.0f,     //3
                0.6f, 1.0f, 0.0f, varStride, 0.6f, 0.0f,     //4
                0.6f, -1.0f, 0.0f, varStride, 0.6f, 1.0f,     //5
                -0.6f, -1.0f, 0.0f, varStride, 0.3f, 1.0f,     //6

                0.6f, 1.0f, 0.0f, varStride, 0.6f, 0.0f,     //4
                1.0f, 0.0f, 0.0f, varStride, 1.0f, 0.5f,     //2
                0.6f, -1.0f, 0.0f, varStride, 0.6f, 1.0f,     //5
        };
        byte[] indices = {
                0, 1, 2,
                3, 4, 5,
                3, 5, 6,
                7, 8, 9
        };

        bufferVertices = Funciones.generarFloatBuffer(vertices);
        bufferIndice = Funciones.generarByteBuffer(indices);

    }

    public void dibujar(GLES20 gl) {
        //1.Crear vertex Shader
        int vertexShader = 0;
        int fragmentShader = 0;

        String sourceVS = null;
        String sourceFS = null;

        sourceVS = Funciones.leerArchivo(R.raw.textura_vertex_shader, contexto);
        vertexShader = Funciones.crearShader(gl.GL_VERTEX_SHADER, sourceVS, gl);


        sourceFS = Funciones.leerArchivo(R.raw.textura_fragment_shader, contexto);
        fragmentShader = Funciones.crearShader(gl.GL_FRAGMENT_SHADER, sourceFS, gl);

        //7. Crear programa
        int programa = Funciones.crearPrograma(vertexShader, fragmentShader, gl);

        //10. Usar programa en el proceso de renderizacion
        gl.glUseProgram(programa);
        bufferVertices.position(0);
        //11. Lectura de parámetros desde el renderer vertexShader
        int idVertexShader = gl.glGetAttribLocation(programa, "posVertexShader");
        gl.glVertexAttribPointer(idVertexShader,
                compPorVertice, gl.GL_FLOAT,
                false, STRIDE, bufferVertices);
        gl.glEnableVertexAttribArray(idVertexShader);
        bufferVertices.position(4);
        //12. Lectura de parámetros desde el renderer FragmentShader
        int idFragmentShader = gl.glGetAttribLocation(programa, "texturaVertex");
        gl.glVertexAttribPointer(idFragmentShader,
                compPorTextura, gl.GL_FLOAT,
                false, STRIDE, bufferVertices);
        gl.glEnableVertexAttribArray(idFragmentShader);

        //Definimos la matriz de proyeccion
        int idPosMatrizProy = gl.glGetUniformLocation(programa, "matrizProyeccion");
        gl.glUniformMatrix4fv(idPosMatrizProy, 1, false, matrizProyeccion, 0);

        gl.glFrontFace(gl.GL_CW);

        bufferIndice.position(0);
        gl.glDrawElements(gl.GL_TRIANGLE_FAN, 3, gl.GL_UNSIGNED_BYTE, bufferIndice);
        bufferIndice.position(3);
        gl.glDrawElements(gl.GL_TRIANGLE_FAN, 3 * 2, gl.GL_UNSIGNED_BYTE, bufferIndice);
        bufferIndice.position(9);
        gl.glDrawElements(gl.GL_TRIANGLE_FAN, 3, gl.GL_UNSIGNED_BYTE, bufferIndice);


        gl.glFrontFace(gl.GL_CW);
        gl.glDisableVertexAttribArray(idVertexShader);
        gl.glDisableVertexAttribArray(idFragmentShader);

        Funciones.liberarShader(programa, vertexShader, fragmentShader);
    }
}

