//SpriteBatch will use texture unit 0
uniform sampler2D u_texture;

//"in" varyings from our vertex shader
varying lowp vec4 vColor;
varying highp vec2 vTexCoord;
uniform lowp float u_alpha;

void main() {
	//sample the texture
	lowp vec4 texColor = texture2D(u_texture, vTexCoord);
	
	//invert the red, green and blue channels
	texColor.a = texColor.a*u_alpha;
	//final color
	gl_FragColor = texColor;
}