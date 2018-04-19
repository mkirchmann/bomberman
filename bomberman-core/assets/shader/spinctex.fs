
uniform sampler2D u_texture;
varying lowp float intensity;
varying highp vec2 texCoords;

void main() {
highp vec4 tc=texture2D(u_texture, vec2(texCoords.x, 1.0-texCoords.y));
gl_FragColor =  vec4(intensity*tc.r,intensity*tc.g,intensity*tc.b,tc.a);
}