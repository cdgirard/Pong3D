#ifdef GL_ES
#define LOWP lowp
#define MED mediump
#define HIGH highp
precision mediump float;
#else
#define MED
#define LOWP
#define HIGH
#endif

uniform float u_opacity;
varying float v_opacity;

attribute vec3 a_position;
attribute vec2 a_texCoord0;
attribute vec3 a_normal;


uniform mat4 u_projViewTrans;
uniform mat4 u_worldTrans;
uniform mat3 u_normalMatrix;

varying vec2 v_texCoords0;
varying float v_intensity;

void main()
{	
	// Vertex position after transformation
    vec4 pos = u_worldTrans * vec4(a_position, 1.0);
    gl_Position = u_projViewTrans * pos;
    
    v_texCoords0 = a_texCoord0;
    
	v_opacity = u_opacity;

    // Just add some basic self shadow
    vec3 normal = normalize(u_normalMatrix * a_normal);    
	v_intensity=1.0;
}
