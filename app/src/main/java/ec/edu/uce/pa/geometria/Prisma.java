package ec.edu.uce.pa.geometria;

import android.content.Context;
import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import ec.edu.uce.pa.R;
import ec.edu.uce.pa.utilidades.Funciones;


public class Prisma {
    private FloatBuffer bufferVertices;
    private FloatBuffer bufferColores;
    private FloatBuffer bufferNormales;
    private FloatBuffer bufferTexturas;

    private final static int byteFlotante = 4;
    private final static int comPorVertices = 3;
    private final static int compPorTexturas = 2;
    private final static int STRIDE = (comPorVertices + compPorTexturas) * byteFlotante;
    private int[] arrayTexturas;
    private Context context;
    private float[] mProyeccion;
    private float[] mVista;
    private float[] vertices;
    private float[] colores;

    private float[] mModelo;

    private static int numVertices;
    private static int numIndices;

    private ByteBuffer bufferIndice;

    //Necesitamos las franjas y cortes que vamos a dibujar
    public Prisma(float width, float height, int numSides,Context context, float[] mProyeccion, float[] mVista, float[] mModelo) {
        this.context = context;
        this.mProyeccion = mProyeccion;
        this.mVista = mVista;
        this.mModelo = mModelo;



        numVertices = numSides * 2 + 2;  // Incluye los vértices de las tapas
        numIndices = numSides * 12;  // Incluye los índices de las tapas y los lados

        vertices = generateVertices(width, height, numSides);

        colores = generateVertices(width, height, numSides);

        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        bufferVertices = vbb.asFloatBuffer();
        bufferVertices.put(vertices);
        bufferVertices.position(0);

        ByteBuffer vcb = ByteBuffer.allocateDirect(colores.length * 4);
        vcb.order(ByteOrder.nativeOrder());
        bufferColores = vcb.asFloatBuffer();
        bufferColores.put(colores);
        bufferColores.position(0);


    }


    private float[] generateVertices(float width, float height, int numSides) {
        float[] vertices = new float[numVertices * 3];
        float angleStep = (float) (2.0 * Math.PI / numSides);

        // Generar vértices de las tapas
        vertices[0] = 0.0f;  // Vértice central de la tapa inferior
        vertices[1] = -height / 2.0f;
        vertices[2] = 0.0f;
        vertices[numVertices * 3 - 3] = 0.0f;  // Vértice central de la tapa superior
        vertices[numVertices * 3 - 2] = height / 2.0f;
        vertices[numVertices * 3 - 1] = 0.0f;

        for (int i = 0; i < numSides; i++) {
            float angle = i * angleStep;
            float x = (float) (width * Math.cos(angle));
            float y = height / 2.0f;
            float z = (float) (width * Math.sin(angle));

            // Vértices superiores
            vertices[i * 3 + 3] = x;
            vertices[i * 3 + 4] = y;
            vertices[i * 3 + 5] = z;

            // Vértices inferiores
            vertices[(i + numSides) * 3 + 3] = x;
            vertices[(i + numSides) * 3 + 4] = -y;
            vertices[(i + numSides) * 3 + 5] = z;
        }

        return vertices;
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

        gl.glDrawArrays(gl.GL_TRIANGLE_STRIP, 0, numVertices-1);

        gl.glFrontFace(gl.GL_CCW);
        gl.glDisableVertexAttribArray(idVertexShader);
        gl.glDisableVertexAttribArray(idFragmentShader);

        Funciones.liberarShader(programa, vertexShader, fragmentShader);

    }



}