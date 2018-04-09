//combined projection and view matrix
uniform mat4 u_projTrans;
uniform vec3 u_lightdir;

//"in" attributes from our SpriteBatch
attribute vec4 a_position;
attribute vec2 a_texCoord;
attribute vec4 a_color;
attribute vec3 a_normal;

//"out" varyings to our fragment shader
varying vec4 vColor;
varying vec2 vTexCoord;
 
void main() {
	vColor = vec4(a_normal, 1.0f);
	vColor = a_color;
	vTexCoord = a_texCoord + 0.0f*u_lightdir.x;
	// gl_Position = u_projTrans * vec4(a_position, 0.0, 1.0);
	gl_Position = u_projTrans * a_position;
}