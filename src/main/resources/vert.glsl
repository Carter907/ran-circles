#version 330

in vec3 pos;
uniform vec3 transl;
uniform float radius;
void main() {

    gl_Position = vec4(pos.x*radius + transl.x, pos.y*radius + transl.y, pos.z*radius + transl.z, 1.0);
}
