attribute vec4 position;
varying vec3 texCoords;

uniform mat4 projection;
uniform mat4 view;

void main() {
    texCoords = position.xyz;
    gl_Position = projection * view * position;
}