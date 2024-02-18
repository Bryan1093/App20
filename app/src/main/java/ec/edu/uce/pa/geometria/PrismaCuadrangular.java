package ec.edu.uce.pa.geometria;

import android.content.Context;
import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import ec.edu.uce.pa.R;
import ec.edu.uce.pa.utilidades.Funciones;

public class PrismaCuadrangular {
    private FloatBuffer bufferVertices;
    private  FloatBuffer bufferColores;

    private ByteBuffer bufferIndice;
    private  final static int byteFlotante = 4;
    private final static int comPorVertices = 3;
    private final static int compPorColores =4;

    private Context context;
    private float [] matrizProyeccion,matrizVista,matrizModelo;
    public int nVerticesGeneral, nColoresG;
    public float[] coloresAU, aux1;
    public float[] coloresAU(int n) {
        this.coloresAU = new float[n * 4];
        for (int i = 0; i < this.coloresAU.length; i++) {
            coloresAU[i] = 0.5f;
            coloresAU[i + 1] =1.0f;
            coloresAU[i + 2] =0.2f;
            coloresAU[i + 3] = 1.0f;
            i += 3;
        }
        return coloresAU;
    }

    public PrismaCuadrangular(Context context, float [] matrizProyeccion,float [] matrizVista,
                    float [] matrizModelo) {
        this.matrizVista = matrizVista;
        this.matrizModelo = matrizModelo;
        this.matrizProyeccion = matrizProyeccion;
        this.context = context;

        //necestiamos primero los vertices, para poder dibujar en OpenGl
        float[] vertices = {
                // FRONTAL
                -1.0f, -1.0f, 1.0f,  // 0
                1.0f, -1.0f,  1.0f,  // 1
                1.0f,  2.0f,  1.0f,  // 2
                -1.0f,  2.0f,  1.0f,  // 3

                // IZQUIERDA
                -1.0f, -1.0f,  1.0f,  //4
                -1.0f, -1.0f, -1.0f,  //5
                -1.0f,  2.0f, -1.0f,  //6
                -1.0f,  2.0f,  1.0f,  //7

                // TRASERA
                -1.0f, -1.0f, -1.0f,  //8
                1.0f, -1.0f, -1.0f,  //9
                1.0f,  2.0f, -1.0f,  //10
                -1.0f,  2.0f, -1.0f, //11

                // DERECHA
                1.0f, -1.0f,  1.0f,  // 16 //12
                1.0f, -1.0f, -1.0f,  // 17 //13
                1.0f,  2.0f, -1.0f,  // 18 //14
                1.0f,  2.0f,  1.0f,  // 19 //15

                // SUPERIOR
                -1.0f,  2.0f,  1.0f,  // 8 //16
                1.0f,  2.0f,  1.0f,  // 9 //17
                1.0f,  2.0f, -1.0f,  // 10 //18
                -1.0f,  2.0f, -1.0f,  // 11 //19

                // INFERIOR
                -1.0f, -1.0f,  1.0f,  // 12 //20
                1.0f, -1.0f,  1.0f,  // 13 //21
                1.0f, -1.0f, -1.0f,  // 14 //22
                -1.0f, -1.0f, -1.0f,  // 15 //23

        };
        /*float [] colores = {
//Frontal Azul
                0.0f, 0.0f, 1.0f, 1.0f,
                0.0f, 0.0f, 1.0f, 1.0f,
                0.0f, 0.0f, 1.0f, 1.0f,
                0.0f, 0.0f, 1.0f, 1.0f,
//Izquierda Celeste
                0.0f, 1.0f, 0.62f, 1.0f,
                0.0f, 1.0f, 0.62f, 1.0f,
                0.0f, 1.0f, 0.62f, 1.0f,
                0.0f, 1.0f, 0.62f, 1.0f,
//Trasera Blanco
                1.0f, 1.0f, 1.0f, 1.0f,
                1.0f, 1.0f, 1.0f, 1.0f,
                1.0f, 1.0f, 1.0f, 1.0f,
                1.0f, 1.0f, 1.0f, 1.0f,
//Derecha Verde
                0.0f, 0.5f, 0.25f, 1.0f,
                0.0f, 0.5f, 0.25f, 1.0f,
                0.0f, 0.5f, 0.25f, 1.0f,
                0.0f, 0.5f, 0.25f, 1.0f,
//Superior Cafe
                0.50f, 0.25f, 0.0f, 1.0f,
                0.50f, 0.25f, 0.0f, 1.0f,
                0.50f, 0.25f, 0.0f, 1.0f,
                0.50f, 0.25f, 0.0f, 1.0f,
//Inferior Rojo
                1.0f, 0.0f, 0.0f, 1.0f,
                1.0f, 0.0f, 0.0f, 1.0f,
                1.0f, 0.0f, 0.0f, 1.0f,
                1.0f, 0.0f, 0.0f, 1.0f
        };*/

        byte [] indices = {

                0, 2, 1,
                0, 3, 2,

                4,6,5,
                4,7,6,

                8,10,9,
                8,11,10,

                12,14,13,
                12,15,14,

                16,18,17,
                16,19,18,

                20,22,21,
                20,23,22,
        };

        nVerticesGeneral = vertices.length;
        aux1 = this.coloresAU(this.nVerticesGeneral / comPorVertices);
        this.nColoresG = aux1.length;

        bufferVertices= Funciones.generarFloatBuffer(vertices);
        bufferColores=Funciones.generarFloatBuffer(aux1);
        bufferIndice = Funciones.generarByteBuffer(indices);

    }
    public void dibujar(GLES20 gl){
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
                comPorVertices,
                gl.GL_FLOAT,
                false,
                0,
                bufferVertices);
        gl.glEnableVertexAttribArray(idVertexShader);

        int idFragmentShader = gl.glGetAttribLocation(programa,"colorVertex");

        //gl.glUniform4f(idFragmentShader,1.0f,0.6f,0.5f,1); // Color uniforme
        gl.glVertexAttribPointer(idFragmentShader, compPorColores, gl.GL_FLOAT,false,
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
//Dibujar el triangulo con draelements
        gl.glDrawElements(gl.GL_TRIANGLES,36,gl.GL_UNSIGNED_BYTE,bufferIndice);
        gl.glFrontFace(gl.GL_CCW);
        gl.glDisableVertexAttribArray(idVertexShader);
        gl.glDisableVertexAttribArray(idFragmentShader);

        Funciones.liberarShader(programa, vertexShader, fragmentShader);

    }

}
