attribute vec4 posVertexShader;
attribute vec3 colorVertex;
varying vec4 fragColorVertex;

uniform mat4 matrizModel; // Matriz de transformación del modelo
uniform mat4 matrizView; // Matriz de vista (cámara)
uniform mat4 matrizProjection; // Matriz de proyección

void main(){

    mat4 mvp = matrizProjection * matrizView * matrizModel;

    // Transforma la posición del vértice utilizando la matriz MVP
    gl_Position = mvp * vec4(posVertexShader);

    // Pasa el color del vértice al fragment shader
    fragColorVertex = vec4(colorVertex,1.0);
}