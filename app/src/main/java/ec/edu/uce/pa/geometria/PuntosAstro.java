package ec.edu.uce.pa.geometria;

import static android.opengl.GLES20.GL_FRAGMENT_SHADER;
import static android.opengl.GLES20.GL_VERTEX_SHADER;

import android.opengl.GLES20;
import android.util.Log;

import java.nio.FloatBuffer;

import ec.edu.uce.pa.utilidades.Funciones;

public class PuntosAstro {

    private FloatBuffer bufferVertices;
    private FloatBuffer bufferColores;

    private static final int byteFlotante = 4;
    private static final int compPorVertice = 2;
    private static final int compPorColor = 4;
    private static final int numPuntos = 200;

   public PuntosAstro(){

       float[] vertices = generateCircleVertices(10f, numPuntos);
       float[]colores = {
               1.0f,0.0f,0.0f,1.0f,
               1.0f,0.0f,0.0f,1.0f,
               1.0f,0.0f,0.0f,1.0f,
               1.0f,0.0f,0.0f,1.0f
       };

       bufferVertices = Funciones.generarFloatBuffer(vertices);
       bufferColores= Funciones.generarFloatBuffer(colores);

   }
    private float[] generateCircleVertices(float radius, int numPoints) {
        float[] vertices = new float[numPoints * 2];

        double angleIncrement = 2 * Math.PI / numPoints;

        double angle = 0;

        for (int i = 0; i < numPoints; i++) {
            vertices[i * 2] = (float) (radius * Math.cos(angle));
            vertices[i * 2 + 1] = (float) (radius * Math.sin(angle));
            angle += angleIncrement;
        }
        return vertices;
    }


    public void dibujar(GLES20 gl){

        //Configuracion Vertex Shader
        //Crear vertexShader

        int [] infoCompiler= new  int[1];

        String source;
        StringBuilder sb = new StringBuilder();

        int vertexShader = gl.glCreateShader(GL_VERTEX_SHADER);

        if (vertexShader ==0){
            Log.w("Vertex Shader", "Error al crear vertex shader"); // mandamos a consola el error
            return;
        }
        //4 Crear  source del vertex shader (crear la intrucción )
        sb.append("attribute vec4 posVertexShader;");
        sb.append("\n");
        sb.append("void main() {");
        sb.append("\n");
        sb.append("gl_Position = posVertexShader;");
        sb.append("\n");
        sb.append("gl_PointSize = 80.0;");
        sb.append("\n");
        sb.append("}");
        sb.append("\n");

        // Compilar el vertexShader
        source=String.valueOf(sb);

        gl.glShaderSource(vertexShader,source);
        GLES20.glCompileShader(vertexShader);
        GLES20.glGetShaderiv(vertexShader, gl.GL_COMPILE_STATUS, infoCompiler,0);
        if (infoCompiler[0] == 0){
            gl.glDeleteShader(vertexShader);
            Log.w("Vertex Shader", "Error al compilar ek vertexShader"); // mandamos a consola el error
            return;
        }

        /////////////////////////////////////////////////////////////////////////////////////
        //FranmenShader

        source = null;
        sb = new StringBuilder();
        int fragmentShader = gl.glCreateShader(GL_FRAGMENT_SHADER);

        if (fragmentShader == 0){
            Log.w("Fragmen Shader", "Error al crear fragmen shader"); // mandamos a consola el error
            return;
        }

        //2. Crear source del fragmen shader
        sb.append("precision mediump float;");
        sb.append("\n");
        sb.append("uniform vec4 posColor;");
        sb.append("\n");
        sb.append("void main() {");
        sb.append("\n");
        sb.append("gl_FragColor = posColor;");
        sb.append("\n");
        sb.append("}");
        sb.append("\n");

        source=String.valueOf(sb);
        // 3 Compilar el fragmen Shader
        gl.glShaderSource(fragmentShader,source);
        GLES20.glCompileShader(fragmentShader);
        GLES20.glGetShaderiv(fragmentShader, gl.GL_COMPILE_STATUS, infoCompiler,0);
        if (infoCompiler[0] == 0){
            gl.glDeleteShader(fragmentShader);
            Log.w("Fragment Shader", "Error al compilar ek fragmentShader"); // mandamos a consola el error
            return;
        }

        //////////////////////////////////////////
        //7 Crear Programa
        int [] infoLink = new int[1];
        int programa = gl.glCreateProgram();
        if (programa == 0 ) {
            Log.w("Programa", "Error al crear el programa "); // mandamos a consola el error
            return;
        }
        // 8 Attach shaders al programa
        gl.glAttachShader(programa,vertexShader);
        gl.glAttachShader(programa,fragmentShader);

        // 9 Vincular programa
        gl.glLinkProgram(programa);
        gl.glGetProgramiv(programa, gl.GL_LINK_STATUS, infoLink,0);

        if (infoLink[0] == 0){
            gl.glDeleteShader(programa);
            Log.w("Programa", "Error al vincular programa"); // mandamos a consola el error
            return;
        }
        // 10 usar programa para el proceso de renderización
        gl.glUseProgram(programa);

        // 11 Lectura de parametros desd el render
        int idVertexShader = gl.glGetAttribLocation(programa,"posVertexShader");
        gl.glVertexAttribPointer(idVertexShader,compPorVertice, gl.GL_FLOAT,false,0,bufferVertices);
        gl.glEnableVertexAttribArray(idVertexShader);

        // 11 Lectura de parametros desdesd e render frahme

        gl.glGetUniformLocation(programa, "posColor");
        int idFragmentShader = gl.glGetUniformLocation(programa,"posColor");

        gl.glUniform4f(idFragmentShader,1.0f,0.0f,0.0f,1.0f);



        gl.glFrontFace(gl.GL_CW);
        gl.glDrawArrays(gl.GL_LINE_LOOP,0,4);
        gl.glFrontFace(gl.GL_CCW);




    }

}
