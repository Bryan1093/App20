package ec.edu.uce.pa.geometria;

import android.content.Context;
import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import ec.edu.uce.pa.R;
import ec.edu.uce.pa.utilidades.Funciones;

public class Plano {

    private FloatBuffer bufferVertices, bufferColores;
    private ByteBuffer bufferIndice;
    private static final int byteFlotante = 4;
    private static final int compPorVertice = 2;
    private static final int compPorColor = 4;
    private final static int STRIDE = (compPorVertice + compPorColor) * byteFlotante;
    private int[] arrayTexturas;
    private float[] matrizProyeccion, matrizVista, matrizModelo;
    private Context contexto;
    public int nVerticesGeneral, nColoresG;
    public float[] coloresAU, aux1;

    public float[] coloresAU(int n) {
        this.coloresAU = new float[n * 4];
        for (int i = 0; i < this.coloresAU.length; i++) {
            coloresAU[i] = aux1[0];
            coloresAU[i + 1] = aux1[1];
            coloresAU[i + 2] = aux1[2];
            coloresAU[i + 3] = aux1[3];
            i += 3;
        }
        return coloresAU;
    }

    public Plano(Context contexto, float[] matrizProyeccion, float[] matrizVista,
                 float[] matrizModelo, float[] colorPlano) {
        aux1 = colorPlano;
        this.nColoresG = aux1.length;
        this.matrizProyeccion = matrizProyeccion;
        this.matrizVista = matrizVista;
        this.matrizModelo = matrizModelo;
        this.contexto = contexto;

        float[] vertices = {
                -1, -1,
                1, -1,
                -1, 1,
                1, 1
        };

        byte[] indices = {
                0, 2, 3,
                0, 3, 1
        };

        nVerticesGeneral = vertices.length;

        aux1 = this.coloresAU(this.nVerticesGeneral / compPorVertice);
        this.nColoresG = aux1.length;

        bufferVertices = Funciones.generarFloatBuffer(vertices);
        bufferColores = Funciones.generarFloatBuffer(aux1);
        bufferIndice = Funciones.generarByteBuffer(indices);
    }

    public void dibujar(GLES20 gl) {
        int vertexShader = 0;
        int fragmentShader = 0;

        String sourceVS = null;
        String sourceFS = null;

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

        int idFragmentShader = gl.glGetAttribLocation(programa, "colorVertex");

        gl.glVertexAttribPointer(idFragmentShader, compPorColor, gl.GL_FLOAT, false,
                0, bufferColores);
        gl.glEnableVertexAttribArray(idFragmentShader);

        int idPosMatrizProy = gl.glGetUniformLocation(programa, "matrizProjection");
        gl.glUniformMatrix4fv(idPosMatrizProy, 1, false, matrizProyeccion, 0);
        int idPosMatrizView = gl.glGetUniformLocation(programa, "matrizView");
        gl.glUniformMatrix4fv(idPosMatrizView, 1, false, matrizVista, 0);
        int idPosMatrizModel = gl.glGetUniformLocation(programa, "matrizModel");
        gl.glUniformMatrix4fv(idPosMatrizModel, 1, false, matrizModelo, 0);

        gl.glFrontFace(gl.GL_CW);
        gl.glDrawElements(gl.GL_TRIANGLES, 6, gl.GL_UNSIGNED_BYTE, bufferIndice);
        gl.glFrontFace(gl.GL_CCW);
        gl.glDisableVertexAttribArray(idVertexShader);
        gl.glDisableVertexAttribArray(idFragmentShader);

        Funciones.liberarShader(programa, vertexShader, fragmentShader);
    }
}