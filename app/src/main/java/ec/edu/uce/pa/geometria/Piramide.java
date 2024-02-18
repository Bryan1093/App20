package ec.edu.uce.pa.geometria;

import android.content.Context;
import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import ec.edu.uce.pa.R;
import ec.edu.uce.pa.utilidades.Funciones;

public class Piramide {
    private FloatBuffer bufferVertices, bufferColores;
    private ByteBuffer bufferIndices;
    private final static int byteFlotante = 4, compVertices = 3, compColores = 4;
    private Context context;
    private float [] matrizProyeccion,matrizVista,matrizModelo;
    public int nVerticesGeneral, nColoresG;
    public float[] coloresAU, aux1;
    public float[] coloresAU(int n) {
        this.coloresAU = new float[n * 4];
        for (int i = 0; i < this.coloresAU.length; i++) {
            coloresAU[i] = 0.0f;
            coloresAU[i + 1] =0.5f;
            coloresAU[i + 2] =1.0f;
            coloresAU[i + 3] = 1.0f;
            i += 3;
        }
        return coloresAU;
    }

    public Piramide(Context context, float [] matrizProyeccion,float [] matrizVista,
                    float [] matrizModelo) {
        this.matrizVista = matrizVista;
        this.matrizModelo = matrizModelo;
        this.matrizProyeccion = matrizProyeccion;
        this.context = context;

        float[] vertices = {
                0.0f, 1.0f, 0.0f,  // 0
                1.0f, -1.0f, 1.0f, //1
                -1.0f, -1.0f, 1.0f,// 2
                -1.0f, -1.0f, -1.0f,// 3
                1.0f, -1.0f, -1.0f  //4
        };

        byte[] indices = {
                0, 1, 2,
                0, 2, 3,
                0, 3, 4,
                0, 4, 1,
                4, 1, 2,
                4, 2, 3
        };

        nVerticesGeneral = vertices.length;

        aux1 = this.coloresAU(this.nVerticesGeneral / compVertices);
        this.nColoresG = aux1.length;

        bufferVertices= Funciones.generarFloatBuffer(vertices);
        bufferColores=Funciones.generarFloatBuffer(aux1);
        bufferIndices = Funciones.generarByteBuffer(indices);
    }

    public void dibujar(GLES20 gl) {
        int vertexShader = 0;
        int fragmentShader = 0;

        String sourceVS = null;
        String sourceFS = null;

        sourceVS = Funciones.leerArchivo(R.raw.mvp_color_vertex_shader, context);
        vertexShader = Funciones.crearShader(gl.GL_VERTEX_SHADER, sourceVS, gl);

        sourceFS = Funciones.leerArchivo(R.raw.color_fragment_shader, context);
        fragmentShader = Funciones.crearShader(gl.GL_FRAGMENT_SHADER, sourceFS, gl);

        int programa = Funciones.crearPrograma(vertexShader, fragmentShader, gl);
        gl.glUseProgram(programa);

        bufferVertices.position(0);
        int idVertexShader = gl.glGetAttribLocation(programa, "posVertexShader");
        gl.glVertexAttribPointer(idVertexShader,
                compVertices,
                gl.GL_FLOAT,
                false,
                0,
                bufferVertices);
        gl.glEnableVertexAttribArray(idVertexShader);

        int idFragmentShader = gl.glGetAttribLocation(programa,"colorVertex");

        //gl.glUniform4f(idFragmentShader,1.0f,0.6f,0.5f,1); // Color uniforme
        gl.glVertexAttribPointer(idFragmentShader, compColores, gl.GL_FLOAT,false,
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
        gl.glDrawElements(GL10.GL_TRIANGLES, 18, GL10.GL_UNSIGNED_BYTE, bufferIndices);
        gl.glFrontFace(gl.GL_CCW);
        gl.glDisableVertexAttribArray(idVertexShader);
        gl.glDisableVertexAttribArray(idFragmentShader);

        Funciones.liberarShader(programa, vertexShader, fragmentShader);
    }
}
