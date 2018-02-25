attribute vec3 a_position;
attribute vec3 a_normal;
attribute vec4 a_color;
 
uniform mat4 u_worldTrans;
uniform mat4 u_projViewTrans;

uniform vec4 u_cameraPosition;
 
uniform mat3 u_normalMatrix;
varying vec3 v_normal;

void main() {
	vec3 pos = vec3(u_worldTrans * vec4(a_position, 1.0));
	//vec3 normal = normalize(vec3(u_worldTrans * vec4(a_normal, 0.0)));
    vec3 normal = normalize(u_normalMatrix * a_normal);

    gl_Position = u_projViewTrans * u_worldTrans * vec4(a_position, 1.0);

    v_normal = normal;
}
