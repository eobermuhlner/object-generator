#define pbrFlag

#if defined(diffuseTextureFlag) || defined(specularTextureFlag) || defined(normalTextureFlag) || defined(emissiveTextureFlag)
#define textureFlag
#endif

#if defined(specularTextureFlag) || defined(specularColorFlag)
#define specularFlag
#endif

#if defined(specularFlag) || defined(fogFlag) || defined(normalTextureFlag)
#define cameraPositionFlag
#endif

#if defined(normalTextureFlag) || (defined(pbrFlag) && defined(specularTextureFlag))
#define fragmentLightingFlag
#endif


attribute vec3 a_position;
uniform mat4 u_projViewTrans;

#if defined(colorFlag)
varying vec4 v_color;
attribute vec4 a_color;
#endif // colorFlag

#ifdef normalFlag
attribute vec3 a_normal;
uniform mat3 u_normalMatrix;
varying vec3 v_normal;
#endif // normalFlag

#ifdef tangentFlag
attribute vec3 a_tangent;
varying vec3 v_tangent;
#endif

#ifdef binormalFlag
attribute vec3 a_binormal;
varying vec3 v_binormal;
#endif

#ifdef textureFlag
attribute vec2 a_texCoord0;
#endif // textureFlag

#ifdef diffuseTextureFlag
uniform vec4 u_diffuseUVTransform;
varying vec2 v_diffuseUV;
#endif

#ifdef emissiveTextureFlag
uniform vec4 u_emissiveUVTransform;
varying vec2 v_emissiveUV;
#endif

#ifdef specularTextureFlag
uniform vec4 u_specularUVTransform;
varying vec2 v_specularUV;
#ifdef pbrFlag
#define separateAmbientFlag
#endif // pbrFlag
#endif

#ifdef normalTextureFlag
uniform vec4 u_normalUVTransform;
varying vec2 v_normalUV;
#endif // normalTextureFlag

#ifdef normalTextureFlag
    #ifndef tangentFlag
    varying vec3 v_tangent;
    #endif // ! tangentFlag
    #ifndef binormalFlag
    varying vec3 v_binormal;
    #endif // ! binormalFlag
#endif // normalTextureFlag

#ifdef specularColorFlag
uniform vec4 u_specularColor;
#endif

#ifdef fragmentLightingFlag
 	vec3 biggestAngle(const in vec3 base, const in vec3 v1, const in vec3 v2) {
		vec3 c1 = cross(base, v1);
		vec3 c2 = cross(base, v2);
		return (dot(c2, c2) > dot(c1, c1)) ? c2 : c1;
	}
#endif // fragmentLightingFlag

#ifdef boneWeight0Flag
#define boneWeightsFlag
attribute vec2 a_boneWeight0;
#endif //boneWeight0Flag

#ifdef boneWeight1Flag
#ifndef boneWeightsFlag
#define boneWeightsFlag
#endif
attribute vec2 a_boneWeight1;
#endif //boneWeight1Flag

#ifdef boneWeight2Flag
#ifndef boneWeightsFlag
#define boneWeightsFlag
#endif
attribute vec2 a_boneWeight2;
#endif //boneWeight2Flag

#ifdef boneWeight3Flag
#ifndef boneWeightsFlag
#define boneWeightsFlag
#endif
attribute vec2 a_boneWeight3;
#endif //boneWeight3Flag

#ifdef boneWeight4Flag
#ifndef boneWeightsFlag
#define boneWeightsFlag
#endif
attribute vec2 a_boneWeight4;
#endif //boneWeight4Flag

#ifdef boneWeight5Flag
#ifndef boneWeightsFlag
#define boneWeightsFlag
#endif
attribute vec2 a_boneWeight5;
#endif //boneWeight5Flag

#ifdef boneWeight6Flag
#ifndef boneWeightsFlag
#define boneWeightsFlag
#endif
attribute vec2 a_boneWeight6;
#endif //boneWeight6Flag

#ifdef boneWeight7Flag
#ifndef boneWeightsFlag
#define boneWeightsFlag
#endif
attribute vec2 a_boneWeight7;
#endif //boneWeight7Flag

#if defined(numBones) && defined(boneWeightsFlag)
#if (numBones > 0)
#define skinningFlag
#endif
#endif

uniform mat4 u_worldTrans;

#if defined(numBones)
#if numBones > 0
uniform mat4 u_bones[numBones];
#endif //numBones
#endif

#ifdef shininessFlag
uniform float u_shininess;
#else
#ifdef pbrFlag
const float u_shininess = 0.2;
#else
const float u_shininess = 20.0;
#endif
#endif // shininessFlag

#ifdef blendedFlag
uniform float u_opacity;
varying float v_opacity;

#ifdef alphaTestFlag
uniform float u_alphaTest;
varying float v_alphaTest;
#endif //alphaTestFlag
#endif // blendedFlag

#ifdef cameraPositionFlag
uniform vec4 u_cameraPosition;
#endif // cameraPositionFlag

#ifdef fogFlag
varying float v_fog;
#endif // fogFlag

#ifdef lightingFlag
    #ifdef fragmentLightingFlag
        varying vec4 v_pos;
    #else // !fragmentLightingFlag
        varying vec3 v_lightDiffuse;

        #ifdef ambientLightFlag
        uniform vec3 u_ambientLight;
        #endif // ambientLightFlag

        #ifdef ambientCubemapFlag
        uniform vec3 u_ambientCubemap[6];
        #endif // ambientCubemapFlag

        #ifdef sphericalHarmonicsFlag
        uniform vec3 u_sphericalHarmonics[9];
        #endif //sphericalHarmonicsFlag


        #ifdef specularFlag
        varying vec3 v_lightSpecular;
        #endif // specularFlag


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

        #if defined(ambientFlag) && defined(separateAmbientFlag)
        varying vec3 v_ambientLight;
        #endif //separateAmbientFlag
    #endif // !fragmentLightingFlag
#endif // lightingFlag

void main() {
	#ifdef diffuseTextureFlag
		v_diffuseUV = u_diffuseUVTransform.xy + a_texCoord0 * u_diffuseUVTransform.zw;
	#endif //diffuseTextureFlag

	#ifdef emissiveTextureFlag
		v_emissiveUV = u_emissiveUVTransform.xy + a_texCoord0 * u_emissiveUVTransform.zw;
	#endif //emissiveTextureFlag

	#ifdef specularTextureFlag
		v_specularUV = u_specularUVTransform.xy + a_texCoord0 * u_specularUVTransform.zw;
	#endif //specularTextureFlag

    #ifdef normalTextureFlag
        v_normalUV = u_normalUVTransform.xy + a_texCoord0 * u_normalUVTransform.zw;
    #endif // textureFlag

	#if defined(colorFlag)
		v_color = a_color;
	#endif // colorFlag

	#ifdef blendedFlag
		v_opacity = u_opacity;
		#ifdef alphaTestFlag
			v_alphaTest = u_alphaTest;
		#endif //alphaTestFlag
	#endif // blendedFlag

	#ifdef skinningFlag
		mat4 skinning = mat4(0.0);
		#ifdef boneWeight0Flag
			skinning += (a_boneWeight0.y) * u_bones[int(a_boneWeight0.x)];
		#endif //boneWeight0Flag
		#ifdef boneWeight1Flag
			skinning += (a_boneWeight1.y) * u_bones[int(a_boneWeight1.x)];
		#endif //boneWeight1Flag
		#ifdef boneWeight2Flag
			skinning += (a_boneWeight2.y) * u_bones[int(a_boneWeight2.x)];
		#endif //boneWeight2Flag
		#ifdef boneWeight3Flag
			skinning += (a_boneWeight3.y) * u_bones[int(a_boneWeight3.x)];
		#endif //boneWeight3Flag
		#ifdef boneWeight4Flag
			skinning += (a_boneWeight4.y) * u_bones[int(a_boneWeight4.x)];
		#endif //boneWeight4Flag
		#ifdef boneWeight5Flag
			skinning += (a_boneWeight5.y) * u_bones[int(a_boneWeight5.x)];
		#endif //boneWeight5Flag
		#ifdef boneWeight6Flag
			skinning += (a_boneWeight6.y) * u_bones[int(a_boneWeight6.x)];
		#endif //boneWeight6Flag
		#ifdef boneWeight7Flag
			skinning += (a_boneWeight7.y) * u_bones[int(a_boneWeight7.x)];
		#endif //boneWeight7Flag
	#endif //skinningFlag

	#ifdef skinningFlag
		vec4 pos = u_worldTrans * skinning * vec4(a_position, 1.0);
	#else
		vec4 pos = u_worldTrans * vec4(a_position, 1.0);
	#endif

	gl_Position = u_projViewTrans * pos;

	#ifdef shadowMapFlag
		vec4 spos = u_shadowMapProjViewTrans * pos;
		v_shadowMapUv.xyz = (spos.xyz / spos.w) * 0.5 + 0.5;
		v_shadowMapUv.z = min(v_shadowMapUv.z, 0.998);
	#endif //shadowMapFlag

	#if defined(normalFlag)
		#if defined(skinningFlag)
			vec3 normal = normalize((u_worldTrans * skinning * vec4(a_normal, 0.0)).xyz);
		#else
			vec3 normal = normalize(u_normalMatrix * a_normal);
		#endif
		v_normal = normal;
	#endif // normalFlag

    #ifdef fogFlag
        vec3 flen = u_cameraPosition.xyz - pos.xyz;
        float fog = dot(flen, flen) * u_cameraPosition.w;
        v_fog = min(fog, 1.0);
    #endif

	#ifdef lightingFlag
        #ifdef fragmentLightingFlag
            v_pos = pos;

            #if defined(normalFlag) && defined(binormalFlag) && defined(tangentFlag)
                v_tangent = a_tangent;
                v_binormal = a_binormal;
            #elif defined(normalFlag) && defined(binormalFlag)
                v_tangent = normalize(cross(v_normal, v_binormal));
            #elif defined(normalFlag) && defined(tangentFlag)
                v_binormal = normalize(cross(v_normal, v_tangent));
            #elif defined(binormalFlag) && defined(tangentFlag)
                v_normal = normalize(cross(v_binormal, v_tangent));
            #elif defined(normalFlag) || defined(binormalFlag) || defined(tangentFlag)
                #if defined(normalFlag)
                    v_binormal = normalize(cross(v_normal, biggestAngle(v_normal, vec3(1.0, 0.0, 0.0), vec3(0.0, 1.0, 0.0))));
                    v_tangent = normalize(cross(v_normal, v_binormal));
                #elif defined(binormalFlag)
                    v_tangent = normalize(cross(v_binormal, biggestAngle(v_binormal, vec3(0.0, 0.0, 1.0), vec3(0.0, 1.0, 0.0))));
                    v_normal = normalize(cross(v_binormal, v_tangent));
                #elif defined(tangentFlag)
                    v_binormal = normalize(cross(v_tangent, biggestAngle(v_binormal, vec3(0.0, 0.0, 1.0), vec3(0.0, 1.0, 0.0))));
                    v_normal = normalize(cross(v_tangent, v_binormal));
                #endif
            #endif

        #else // ! fragmentLightingFlag
            #if	defined(ambientLightFlag)
                vec3 ambientLight = u_ambientLight;
            #elif defined(ambientFlag)
                vec3 ambientLight = vec3(0.0);
            #endif

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

            #ifdef ambientFlag
                #ifdef separateAmbientFlag
                    v_ambientLight = ambientLight;
                    v_lightDiffuse = vec3(0.0);
                #else
                    v_lightDiffuse = ambientLight;
                #endif //separateAmbientFlag
            #else
                v_lightDiffuse = vec3(0.0);
            #endif //ambientFlag


            #ifdef specularFlag
                #ifdef pbrFlag
                    #if defined(specularColorFlag)
                        vec3 mga = u_specularColor.rgb;
                    #else
                        vec3 mga = vec3(0.0, u_shininess, 1.0);
                    #endif

                    float metal = mga.r;
                    float gloss = mga.g;
                    float ao = mga.b;
                #endif // pbrFlag

                v_lightSpecular = vec3(0.0);
                vec3 viewVec = normalize(u_cameraPosition.xyz - pos.xyz);
            #endif // specularFlag

            #if (numDirectionalLights > 0) && defined(normalFlag)
                for (int i = 0; i < numDirectionalLights; i++) {
                    vec3 lightDir = -u_dirLights[i].direction;
                    float NdotL = clamp(dot(normal, lightDir), 0.0, 1.0);
                    vec3 value = u_dirLights[i].color * NdotL;
                    #ifdef specularFlag
                        float halfDotView = max(0.0, dot(normal, normalize(lightDir + viewVec)));
                        #ifdef pbrFlag
                            v_lightDiffuse += value * (1.0 - metal);
                            v_lightSpecular += value * pow(halfDotView, gloss) * metal;
                        #else // ! pbrFlag
                            v_lightDiffuse += value;
                            v_lightSpecular += value * pow(halfDotView, u_shininess);
                        #endif // ! pbrFlag
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
                    v_lightDiffuse += value;
                    #ifdef specularFlag
                        float halfDotView = max(0.0, dot(normal, normalize(lightDir + viewVec)));
                        #ifdef pbrFlag
                            v_lightDiffuse += value * (1.0 - metal);
                            v_lightSpecular += value * pow(halfDotView, gloss) * metal;
                        #else // ! pbrFlag
                            v_lightDiffuse += value;
                            v_lightSpecular += value * pow(halfDotView, u_shininess);
                        #endif // ! pbrFlag
                    #endif // specularFlag
                }
            #endif // numPointLights
        #endif // ! fragmentLightingFlag
	#endif // lightingFlag
}
