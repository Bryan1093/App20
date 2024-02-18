precision mediump float;

uniform vec4 colorUniform; // Declarar una variable uniform para el color

void main() {
    gl_FragColor = colorUniform; // Aplicar el color uniforme al fragmento
}