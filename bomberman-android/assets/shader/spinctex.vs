attribute vec4 a_position;
attribute vec3 a_normal;
attribute vec2 a_texCoord;

uniform float ambience;
uniform mat4 u_projTrans;
uniform vec3 u_lightdir;
varying float intensity;
varying vec2 texCoords;

void main() {
vec3 n = normalize(a_normal);
vec3 l = normalize(u_lightdir);
intensity = max(dot(n, l), ambience);
texCoords = a_texCoord;
gl_Position = u_projTrans * a_position;
}