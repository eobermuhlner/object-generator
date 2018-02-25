#ifdef GL_ES 
precision mediump float;
#endif
 
varying vec3 v_normal;
 
void main() {
    gl_FragColor.rgb = v_normal;
    gl_FragColor.a = 1.0;
}
