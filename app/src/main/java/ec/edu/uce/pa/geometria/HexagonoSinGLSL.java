package ec.edu.uce.pa.geometria;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import ec.edu.uce.pa.utilidades.Funciones;

public class HexagonoSinGLSL {

    private FloatBuffer bufferVertices;
    private FloatBuffer bufferColores;
    private ByteBuffer bufferIndice;

    private static final int byteFlotante = 4;
    private static final int compPorVertice = 2;
    private static final int compPorColor = 4;

   public HexagonoSinGLSL(){

       float[]vertices = {
               0.0f,0.0f,
                0.6f, 1.0f,
              1.0f,0.0f,
               0.6f,-1.0f,
               -0.6f, -1.0f,
               -1.0f,0.0f,
               -0.6f, 1.0f

       };
       byte [] indices ={
               0,1,2,
               0,2,3,
               0,3,4,
               0,4,5,
               0,5,6,
               0,6,1

       };

       float[]colores = {
               1.0f,0.0f,0.0f,1.0f,
               1.0f,0.0f,0.0f,1.0f,
               1.0f,0.0f,0.0f,1.0f,
               1.0f,0.0f,0.0f,1.0f,
               1.0f,0.0f,0.0f,1.0f,
               1.0f,0.0f,0.0f,1.0f
       };

       bufferVertices = Funciones.generarFloatBuffer(vertices);
       bufferColores= Funciones.generarFloatBuffer(colores);

       bufferIndice=Funciones.generarByteBuffer(indices);


   }

    public void dibujar(GLES20 gl){
        int vertexShader = 0;
        int fragmentShader = 0;


        String source;
        StringBuilder sb = new StringBuilder();

        sb.append("attribute vec4 posVertexShader;");
        sb.append("\n");
        sb.append("void main() {");
        sb.append("\n");
        sb.append("gl_Position = posVertexShader;");
        sb.append("\n");
        sb.append("gl_PointSize = 80.0;");
        sb.append("\n");
        sb.append("}");


        // Compilar el vertexShader
        source=String.valueOf(sb);

        vertexShader = Funciones.crearShader(gl.GL_VERTEX_SHADER, source,gl);

        source = null;

        sb = new StringBuilder();
        sb.append("precision mediump float;");
        sb.append("\n");
        sb.append("uniform vec4 posColor;");
        sb.append("\n");
        sb.append("void main() {");
        sb.append("\n");
        sb.append("gl_FragColor = posColor;");
        sb.append("\n");
        sb.append("}");

        source=String.valueOf(sb);

        fragmentShader = Funciones.crearShader(gl.GL_FRAGMENT_SHADER,source,gl);

        int programa = Funciones.crearPrograma(vertexShader,fragmentShader,gl);

        gl.glUseProgram(programa);

        int idVertexShader = gl.glGetAttribLocation(programa,"posVertexShader");

        gl.glVertexAttribPointer(idVertexShader,
                                    compPorVertice,
                                     gl.GL_FLOAT,
                            false,
                                0,
                                     bufferVertices);

        gl.glEnableVertexAttribArray(idVertexShader);


        int idFragmentShader = gl.glGetUniformLocation(programa,"posColor");

        gl.glUniform4f(idFragmentShader,0.0f,1.0f,1.0f,1.0f);

        gl.glFrontFace(gl.GL_CW);
        gl.glDrawElements(gl.GL_TRIANGLE_FAN,3*6,gl.GL_UNSIGNED_BYTE, bufferIndice);
        gl.glFrontFace(gl.GL_CCW);


    }

}
