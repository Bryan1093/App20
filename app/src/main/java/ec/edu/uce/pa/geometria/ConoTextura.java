package ec.edu.uce.pa.geometria;

import android.content.Context;
import android.opengl.GLES20;

import java.nio.FloatBuffer;

import ec.edu.uce.pa.R;
import ec.edu.uce.pa.utilidades.Funciones;

public class ConoTextura {

    private FloatBuffer bufferVertices, bufferColores, bufferTextura;
    private static int byteFlotante = 4;
    private final static int comPorVertice = 3, comPorColores = 4, comPorTextura=2;
    private int franjas;
    public int nVerticesGeneral, nColoresG;
    public float[] coloresAU, aux1;

    private float[] matrizProyeccion, matrizVista, matrizModelo;
    private Context contexto;

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

    public ConoTextura(int franjas, float radio, float altura, Context contexto, float[] matrizProyeccion,
                float[] matrizVista, float[] matrizModelo) {
        this.franjas = franjas;
        this.matrizVista = matrizVista;
        this.matrizModelo = matrizModelo;
        this.matrizProyeccion = matrizProyeccion;
        this.contexto = contexto;

        float[] vertices = new float[(franjas + 2) * 3];
        float[] texCoords = new float[(franjas + 2) * 2]; // Coordenadas de textura
        int vertexIndex = 0;
        int texCoordIndex = 0;

        // Vértice del vértice central
        vertices[vertexIndex++] = 0.0f;
        vertices[vertexIndex++] = 0.0f;
        vertices[vertexIndex++] = altura;

        // Coordenada de textura para el vértice central (punta del cono)
        texCoords[texCoordIndex++] = 0.5f; // u
        texCoords[texCoordIndex++] = 1.0f; // v

        // Vértices en la base del cono
        for (int i = 0; i <= franjas; ++i) {
            float theta = (float) (2.0 * Math.PI * (float) i / franjas);
            float u = (float) i / franjas; // Coordenada de textura U

            float x = (float) (radio * Math.cos(theta));
            float y = (float) (radio * Math.sin(theta));
            float z = 0.0f;

            vertices[vertexIndex++] = x;
            vertices[vertexIndex++] = y;
            vertices[vertexIndex++] = z;

            texCoords[texCoordIndex++] = u;
            texCoords[texCoordIndex++] = 0.0f; // v en la base del cono
        }

        nVerticesGeneral = vertices.length;

        aux1 = this.coloresAU(this.nVerticesGeneral / comPorVertice);
        this.nColoresG = aux1.length;

        bufferVertices=Funciones.generarFloatBuffer(vertices);
        bufferColores= Funciones.generarFloatBuffer(aux1);
        bufferTextura=Funciones.generarFloatBuffer(texCoords);
    }

    public void dibujar(GLES20 gl) {
        int vertexShader = 0;
        int fragmentShader = 0;

        String sourceVS = null;
        String sourceFS = null;

        sourceVS = Funciones.leerArchivo(R.raw.mvp_textura_vertex_shader, contexto);
        vertexShader = Funciones.crearShader(gl.GL_VERTEX_SHADER, sourceVS, gl);

        sourceFS = Funciones.leerArchivo(R.raw.textura_fragment_shader, contexto);
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

        bufferTextura.position(0);
        int idFragmentShader = gl.glGetAttribLocation(programa, "texturaVertex");
        gl.glVertexAttribPointer(idFragmentShader,
                comPorTextura,
                gl.GL_FLOAT,
                false,
                0,
                bufferTextura);
        gl.glEnableVertexAttribArray(idFragmentShader);

        // Obtener la ubicación del uniforme "matrizProjection, matrizView, matrizModel" en el shader
        int idPosMatrizProy = gl.glGetUniformLocation(programa, "matrizProjection");
        gl.glUniformMatrix4fv(idPosMatrizProy, 1, false, matrizProyeccion, 0);
        int idPosMatrizView = gl.glGetUniformLocation(programa, "matrizView");
        gl.glUniformMatrix4fv(idPosMatrizView, 1, false, matrizVista, 0);
        int idPosMatrizModel = gl.glGetUniformLocation(programa, "matrizModel");
        gl.glUniformMatrix4fv(idPosMatrizModel, 1, false, matrizModelo, 0);

        gl.glFrontFace(gl.GL_CW); //horario
        //gl.glDrawArrays(gl.GL_TRIANGLE_STRIP,0,franjas + 2);
        gl.glDrawArrays(gl.GL_TRIANGLE_FAN,0, franjas + 2);
        gl.glFrontFace(gl.GL_CCW); //antihorario
        gl.glDisableVertexAttribArray(idVertexShader);
        gl.glDisableVertexAttribArray(idFragmentShader);

        Funciones.liberarShader(programa, vertexShader, fragmentShader);
    }
}

