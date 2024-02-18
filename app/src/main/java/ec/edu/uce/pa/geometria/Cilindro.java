package ec.edu.uce.pa.geometria;

import android.content.Context;
import android.opengl.GLES20;

import java.nio.FloatBuffer;

import ec.edu.uce.pa.R;
import ec.edu.uce.pa.utilidades.Funciones;

public class Cilindro {

    private FloatBuffer bufferVertices, bufferColores;
    private static int byteFlotante = 4;
    private final static int comPorVertice = 3, comPorColores = 4;
    private int franjas, pilas;
    public int nVerticesGeneral, nColoresG;
    public float[] coloresAU, aux1;
    private float[] matrizProyeccion, matrizVista, matrizModelo;
    private Context contexto;

    public float[] coloresAU(int n) {
        this.coloresAU = new float[n * 4];
        for (int i = 0; i < this.coloresAU.length; i++) {
            coloresAU[i] = 1.0f;
            coloresAU[i + 1] =0.5f;
            coloresAU[i + 2] =1.0f;
            coloresAU[i + 3] = 0.0f;
            i += 3;
        }
        return coloresAU;
    }

    public Cilindro(int franjas, int pilas, float radius, float height, Context contexto,
                    float[] matrizProyeccion, float[] matrizVista, float[] matrizModelo) {
        this.franjas = franjas;
        this.pilas = pilas;
        this.matrizVista = matrizVista;
        this.matrizModelo = matrizModelo;
        this.matrizProyeccion = matrizProyeccion;
        this.contexto = contexto;

        float[] vertices = new float[(franjas + 1) * (pilas + 1) * 3];
        int index = 0;

        for (int i = 0; i <= pilas; ++i) {
            float stack = height * (-0.5f + (float) i / pilas);

            for (int j = 0; j <= franjas; ++j) {
                float theta = (float) (2.0 * Math.PI * (float) j / franjas);

                float x = (float) (radius * Math.cos(theta));
                float y = (float) (radius * Math.sin(theta));
                float z = stack;

                vertices[index++] = x;
                vertices[index++] = y;
                vertices[index++] = z;
            }
        }
        nVerticesGeneral = vertices.length;

        aux1 = this.coloresAU(this.nVerticesGeneral / comPorVertice);
        this.nColoresG = aux1.length;

        bufferVertices= Funciones.generarFloatBuffer(vertices);
        bufferColores=Funciones.generarFloatBuffer(aux1);
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
                comPorVertice,
                gl.GL_FLOAT,
                false,
                0,
                bufferVertices);
        gl.glEnableVertexAttribArray(idVertexShader);

        int idFragmentShader = gl.glGetAttribLocation(programa,"colorVertex");

        //gl.glUniform4f(idFragmentShader,1.0f,0.6f,0.5f,1); // Color uniforme
        gl.glVertexAttribPointer(idFragmentShader, comPorColores, gl.GL_FLOAT,false,
                0, bufferColores);
        gl.glEnableVertexAttribArray(idFragmentShader);

        // Obtener la ubicación del uniforme "matrizProjection, matrizView, matrizModel" en el shader
        int idPosMatrizProy = gl.glGetUniformLocation(programa, "matrizProjection");
        gl.glUniformMatrix4fv(idPosMatrizProy, 1, false, matrizProyeccion, 0);
        int idPosMatrizView = gl.glGetUniformLocation(programa, "matrizView");
        gl.glUniformMatrix4fv(idPosMatrizView, 1, false, matrizVista, 0);
        int idPosMatrizModel = gl.glGetUniformLocation(programa, "matrizModel");
        gl.glUniformMatrix4fv(idPosMatrizModel, 1, false, matrizModelo, 0);

        gl.glFrontFace(gl.GL_CW); //horario
        //gl.glDrawArrays(gl.GL_TRIANGLE_STRIP,0,(franjas + 1) * (pilas + 1));
        gl.glDrawArrays(gl.GL_TRIANGLE_FAN,0,(franjas + 1) * (pilas + 1));
        gl.glFrontFace(gl.GL_CCW); //antihorario
        gl.glDisableVertexAttribArray(idVertexShader);
        gl.glDisableVertexAttribArray(idFragmentShader);

        Funciones.liberarShader(programa, vertexShader, fragmentShader);
    }
}
