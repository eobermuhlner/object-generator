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

#if defined(specularTextureFlag) || defined(specularColorFlag)
#define specularFlag
#endif

#if defined(specularFlag) || defined(fogFlag) || defined(normalTextureFlag)
#define cameraPositionFlag
#endif

#ifdef normalFlag
varying vec3 v_normal;
#endif //normalFlag

#if defined(tangentFlag) || defined(normalTextureFlag)
varying vec3 v_tangent;
#endif

#if defined(binormalFlag) || defined(normalTextureFlag)
varying vec3 v_binormal;
#endif

#if defined(colorFlag)
varying vec4 v_color;
#endif

#ifdef blendedFlag
varying float v_opacity;
#ifdef alphaTestFlag
varying float v_alphaTest;
#endif //alphaTestFlag
#endif //blendedFlag

#if defined(diffuseTextureFlag) || defined(specularTextureFlag) || defined(emissiveTextureFlag) || defined(normalTextureFlag) || defined(ambientTextureFlag)
#define textureFlag
#endif

#ifdef diffuseTextureFlag
varying MED vec2 v_diffuseUV;
#endif

#ifdef specularTextureFlag
varying MED vec2 v_specularUV;
#endif

#ifdef ambientTextureFlag
varying MED vec2 v_ambientUV;
#define separateAmbientFlag
#endif

#ifdef emissiveTextureFlag
varying MED vec2 v_emissiveUV;
#endif

#ifdef normalTextureFlag
varying MED vec2 v_normalUV;
#define fragmentLightingFlag
#endif

#ifdef diffuseColorFlag
uniform vec4 u_diffuseColor;
#endif

#ifdef diffuseTextureFlag
uniform sampler2D u_diffuseTexture;
#endif

#ifdef specularColorFlag
uniform vec4 u_specularColor;
#endif

#ifdef specularTextureFlag
uniform sampler2D u_specularTexture;
#endif

#ifdef normalTextureFlag
uniform sampler2D u_normalTexture;
#endif

#ifdef ambientTextureFlag
uniform sampler2D u_ambientTexture;
#endif

#ifdef emissiveTextureFlag
uniform sampler2D u_emissiveTexture;
#endif

#ifdef emissiveColorFlag
uniform vec4 u_emissiveColor;
#endif

#ifdef lightingFlag
    #ifdef fragmentLightingFlag
        varying vec4 v_pos;

        #ifdef ambientLightFlag
        uniform vec3 u_ambientLight;
        #endif // ambientLightFlag

        #ifdef ambientCubemapFlag
        uniform vec3 u_ambientCubemap[6];
        #endif // ambientCubemapFlag

        #ifdef sphericalHarmonicsFlag
        uniform vec3 u_sphericalHarmonics[9];
        #endif //sphericalHarmonicsFlag

        #ifdef cameraPositionFlag
        uniform vec4 u_cameraPosition;
        #endif // cameraPositionFlag

        #if numDirectionalLights > 0
        struct DirectionalLight
        {
            vec3 color;
            vec3 direction;
        };
        uniform DirectionalLight u_dirLights[numDirectionalLights];
        #endif // numDirectionalLights

        #if numPointLights > 0
        struct PointLight
        {
            vec3 color;
            vec3 position;
        };
        uniform PointLight u_pointLights[numPointLights];
        #endif // numPointLights

        #if	defined(ambientLightFlag) || defined(ambientCubemapFlag) || defined(sphericalHarmonicsFlag)
        #define ambientFlag
        #endif //ambientFlag

        #ifdef shadowMapFlag
        uniform mat4 u_shadowMapProjViewTrans;
        varying vec3 v_shadowMapUv;
        #define separateAmbientFlag
        #endif //shadowMapFlag

        #ifdef shininessFlag
        uniform float u_shininess;
        #else
        const float u_shininess = 20.0;
        #endif // shininessFlag

    #else // ! fragmentLightingFlag
        varying vec3 v_lightDiffuse;

        #if	defined(ambientLightFlag) || defined(ambientCubemapFlag) || defined(sphericalHarmonicsFlag)
        #define ambientFlag
        #endif //ambientFlag

        #ifdef specularFlag
        varying vec3 v_lightSpecular;
        #endif //specularFlag

        #ifdef shadowMapFlag
        uniform sampler2D u_shadowTexture;
        uniform float u_shadowPCFOffset;
        varying vec3 v_shadowMapUv;
        #define separateAmbientFlag

        float getShadowness(vec2 offset)
        {
            const vec4 bitShifts = vec4(1.0, 1.0 / 255.0, 1.0 / 65025.0, 1.0 / 16581375.0);
            return step(v_shadowMapUv.z, dot(texture2D(u_shadowTexture, v_shadowMapUv.xy + offset), bitShifts));//+(1.0/255.0));
        }

        float getShadow()
        {
            return (//getShadowness(vec2(0,0)) +
                    getShadowness(vec2(u_shadowPCFOffset, u_shadowPCFOffset)) +
                    getShadowness(vec2(-u_shadowPCFOffset, u_shadowPCFOffset)) +
                    getShadowness(vec2(u_shadowPCFOffset, -u_shadowPCFOffset)) +
                    getShadowness(vec2(-u_shadowPCFOffset, -u_shadowPCFOffset))) * 0.25;
        }
        #endif //shadowMapFlag

        #if defined(ambientFlag) && defined(separateAmbientFlag)
        varying vec3 v_ambientLight;
        #endif //separateAmbientFlag
    #endif // ! fragmentLightingFlag

#endif //lightingFlag

#ifdef fogFlag
uniform vec4 u_fogColor;
varying float v_fog;
#endif // fogFlag

void main() {
	#if defined(normalFlag)
        #if defined(normalTextureFlag)
    		vec3 normal = normalize(v_normal);
            vec3 tangent = normalize(v_tangent);
            vec3 binormal = normalize(v_binormal);

            vec3 normalDeviation = texture2D(u_normalTexture, v_normalUV).xyz;
            normalDeviation = normalize(normalDeviation * 2.0 - 1.0); // consider supporting different formats of normal texture
            normal = normalize((tangent * normalDeviation.x) + (binormal * normalDeviation.y) + (normal * normalDeviation.z));
        #else // ! normalTextureFlag
    		vec3 normal = normalize(v_normal);
        #endif // ! normalTextureFlag
	#endif // normalFlag

	#if defined(diffuseTextureFlag) && defined(diffuseColorFlag) && defined(colorFlag)
		vec4 diffuse = texture2D(u_diffuseTexture, v_diffuseUV) * u_diffuseColor * v_color;
	#elif defined(diffuseTextureFlag) && defined(diffuseColorFlag)
		vec4 diffuse = texture2D(u_diffuseTexture, v_diffuseUV) * u_diffuseColor;
	#elif defined(diffuseTextureFlag) && defined(colorFlag)
		vec4 diffuse = texture2D(u_diffuseTexture, v_diffuseUV) * v_color;
	#elif defined(diffuseTextureFlag)
		vec4 diffuse = texture2D(u_diffuseTexture, v_diffuseUV);
	#elif defined(diffuseColorFlag) && defined(colorFlag)
		vec4 diffuse = u_diffuseColor * v_color;
	#elif defined(diffuseColorFlag)
		vec4 diffuse = u_diffuseColor;
	#elif defined(colorFlag)
		vec4 diffuse = v_color;
	#else
		vec4 diffuse = vec4(1.0);
	#endif

	#if defined(emissiveTextureFlag) && defined(emissiveColorFlag)
		vec4 emissive = texture2D(u_emissiveTexture, v_emissiveUV) * u_emissiveColor;
	#elif defined(emissiveTextureFlag)
		vec4 emissive = texture2D(u_emissiveTexture, v_emissiveUV);
	#elif defined(emissiveColorFlag)
		vec4 emissive = u_emissiveColor;
	#else
		vec4 emissive = vec4(0.0);
	#endif

    #if defined(ambientFlag) && defined(separateAmbientFlag) && !defined(fragmentLightingFlag)
        vec3 ambientLight = v_ambientLight;
    #elif defined(ambientLightFlag)
        vec3 ambientLight = u_ambientLight;
    #else
        vec3 ambientLight = vec3(0.0);
    #endif

	#if (defined(lightingFlag))
	    #if (defined(fragmentLightingFlag))
	        vec4 pos = v_pos;

            #ifdef ambientCubemapFlag
                vec3 squaredNormal = normal * normal;
                vec3 isPositive  = step(0.0, normal);
                ambientLight += squaredNormal.x * mix(u_ambientCubemap[0], u_ambientCubemap[1], isPositive.x) +
                        squaredNormal.y * mix(u_ambientCubemap[2], u_ambientCubemap[3], isPositive.y) +
                        squaredNormal.z * mix(u_ambientCubemap[4], u_ambientCubemap[5], isPositive.z);
            #endif // ambientCubemapFlag

            #ifdef sphericalHarmonicsFlag
                ambientLight += u_sphericalHarmonics[0];
                ambientLight += u_sphericalHarmonics[1] * normal.x;
                ambientLight += u_sphericalHarmonics[2] * normal.y;
                ambientLight += u_sphericalHarmonics[3] * normal.z;
                ambientLight += u_sphericalHarmonics[4] * (normal.x * normal.z);
                ambientLight += u_sphericalHarmonics[5] * (normal.z * normal.y);
                ambientLight += u_sphericalHarmonics[6] * (normal.y * normal.x);
                ambientLight += u_sphericalHarmonics[7] * (3.0 * normal.z * normal.z - 1.0);
                ambientLight += u_sphericalHarmonics[8] * (normal.x * normal.x - normal.y * normal.y);
            #endif // sphericalHarmonicsFlag

            #ifdef ambientTextureFlag
                vec3 ambientLightFactor = texture2D(u_ambientTexture, v_ambientUV).rbg;
                ambientLight *= ambientLightFactor;
            #endif

            #ifdef ambientFlag
                #ifdef separateAmbientFlag
                    vec3 lightDiffuse = vec3(0.0);
                #else
                    vec3 lightDiffuse = ambientLight;
                #endif //separateAmbientFlag
            #else
                vec3 lightDiffuse = vec3(0.0);
            #endif //ambientFlag


            #ifdef specularFlag
                vec3 lightSpecular = vec3(0.0);
                vec3 viewVec = normalize(u_cameraPosition.xyz - pos.xyz);
            #endif // specularFlag

            #if (numDirectionalLights > 0) && defined(normalFlag)
                for (int i = 0; i < numDirectionalLights; i++) {
                    vec3 lightDir = -u_dirLights[i].direction;
                    float NdotL = clamp(dot(normal, lightDir), 0.0, 1.0);
                    vec3 value = u_dirLights[i].color * NdotL;
                    lightDiffuse += value;
                    #ifdef specularFlag
                        float halfDotView = max(0.0, dot(normal, normalize(lightDir + viewVec)));
                        lightSpecular += value * pow(halfDotView, u_shininess);
                    #endif // specularFlag
                }
            #endif // numDirectionalLights

            #if (numPointLights > 0) && defined(normalFlag)
                for (int i = 0; i < numPointLights; i++) {
                    vec3 lightDir = u_pointLights[i].position - pos.xyz;
                    float dist2 = dot(lightDir, lightDir);
                    lightDir *= inversesqrt(dist2);
                    float NdotL = clamp(dot(normal, lightDir), 0.0, 1.0);
                    vec3 value = u_pointLights[i].color * (NdotL / (1.0 + dist2));
                    lightDiffuse += value;
                    #ifdef specularFlag
                        float halfDotView = max(0.0, dot(normal, normalize(lightDir + viewVec)));
                        lightSpecular += value * pow(halfDotView, u_shininess);
                    #endif // specularFlag
                }
            #endif // numPointLights
	    #else // ! fragmentLightingFlag
            #ifdef ambientTextureFlag
                vec3 ambientLightFactor = texture2D(u_ambientTexture, v_ambientUV).rbg;
                ambientLight *= ambientLightFactor;
            #endif

	        vec3 lightDiffuse = v_lightDiffuse;
            #ifdef specularFlag
    	        vec3 lightSpecular = v_lightSpecular;
            #endif // specularFlag
	    #endif // ! fragmentLightingFlag

        #if (!defined(specularFlag))
            #if defined(ambientFlag) && defined(separateAmbientFlag)
                #ifdef shadowMapFlag
                    gl_FragColor.rgb = (diffuse.rgb * (ambientLight + getShadow() * lightDiffuse)) + emissive.rgb;
                    //gl_FragColor.rgb = texture2D(u_shadowTexture, v_shadowMapUv.xy);
                #else
                    gl_FragColor.rgb = (diffuse.rgb * (ambientLight + lightDiffuse)) + emissive.rgb;
                #endif //shadowMapFlag
            #else
                #ifdef shadowMapFlag
                    gl_FragColor.rgb = getShadow() * (diffuse.rgb * lightDiffuse) + emissive.rgb;
                #else
                    gl_FragColor.rgb = (diffuse.rgb * lightDiffuse) + emissive.rgb;
                #endif //shadowMapFlag
            #endif
        #else // specularFlag
            #if defined(specularTextureFlag) && defined(specularColorFlag)
                vec3 specular = texture2D(u_specularTexture, v_specularUV).rgb * u_specularColor.rgb * lightSpecular;
            #elif defined(specularTextureFlag)
                vec3 specular = texture2D(u_specularTexture, v_specularUV).rgb * lightSpecular;
            #elif defined(specularColorFlag)
                vec3 specular = u_specularColor.rgb * lightSpecular;
            #else
                vec3 specular = lightSpecular;
            #endif

            #if defined(ambientFlag) && defined(separateAmbientFlag)
                #ifdef shadowMapFlag
                gl_FragColor.rgb = (diffuse.rgb * (getShadow() * lightDiffuse + ambientLight)) + specular + emissive.rgb;
                    //gl_FragColor.rgb = texture2D(u_shadowTexture, v_shadowMapUv.xy);
                #else
                    gl_FragColor.rgb = (diffuse.rgb * (lightDiffuse + ambientLight)) + specular + emissive.rgb;
                #endif //shadowMapFlag
            #else
                #ifdef shadowMapFlag
                    gl_FragColor.rgb = getShadow() * ((diffuse.rgb * lightDiffuse) + specular) + emissive.rgb;
                #else
                    gl_FragColor.rgb = (diffuse.rgb * lightDiffuse) + specular + emissive.rgb;
                #endif //shadowMapFlag
            #endif
        #endif // specularFlag
    #else // ! lightingFlag
		gl_FragColor.rgb = diffuse.rgb + emissive.rgb;
    #endif // ! lightingFlag

	#ifdef fogFlag
		gl_FragColor.rgb = mix(gl_FragColor.rgb, u_fogColor.rgb, v_fog);
	#endif // end fogFlag

        #if defined(normalTextureFlag)
        //gl_FragColor.rgb = normalize(v_tangent); // FIXME DEBUG
		#endif

	#ifdef blendedFlag
		gl_FragColor.a = diffuse.a * v_opacity;
		#ifdef alphaTestFlag
			if (gl_FragColor.a <= v_alphaTest)
				discard;
		#endif
	#else
		gl_FragColor.a = 1.0;
	#endif

}
