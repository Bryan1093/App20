precision mediump float;
varying vec3 texCoords;

uniform samplerCube cubemapTexture;

void main() {
    gl_FragColor = textureCube(cubemapTexture, texCoords);
}
