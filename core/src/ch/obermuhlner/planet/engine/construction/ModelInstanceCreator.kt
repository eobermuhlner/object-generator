package ch.obermuhlner.planet.engine.construction

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.TextureLoader
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.VertexAttributes
import com.badlogic.gdx.graphics.g3d.Material
import com.badlogic.gdx.graphics.g3d.ModelInstance
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder

import ktx.assets.load

class ModelInstanceCreator {
    val modelBuilder = ModelBuilder()

    val rotationStep = 90f

    val assetManager = AssetManager()

    val materialMap: MutableMap<MaterialInfo, Material> = mutableMapOf()

    init {
        loadAllTextures()
    }

    private fun loadTexture(name: String, textureType: String = "diffuse") : Texture {
        val path = texturePath(name, textureType)
        val wrapper = assetManager.load<Texture>(path)
        wrapper.finishLoading()
        return wrapper.asset
    }

    private fun getTexture(name: String, textureType: String = "diffuse") : Texture? {
        val path = texturePath(name, textureType)
        if (!assetManager.isLoaded(path)) {
            return null
        }
        return assetManager.get<Texture>(path)
    }

    private fun texturePath(name: String, textureType: String): String {
        return "textures/${name}_$textureType.jpg"
    }

    private fun getMaterial(color: Color?, specularColor: Color?, emissiveColor: Color?, textureName : String?): Material {
        val key = MaterialInfo(color, specularColor, emissiveColor, textureName)
        return materialMap.computeIfAbsent(key, {
            val material = Material()
            if (it.color != null) {
                material.set(ColorAttribute.createDiffuse(it.color))
            }
            if (it.specularColor != null) {
                material.set(ColorAttribute(ColorAttribute.Specular, it.specularColor))
            }
            if (it.emissiveColor != null) {
                material.set(ColorAttribute(ColorAttribute.Emissive, it.emissiveColor))
            }
            if (it.textureName != null) {
                val diffuseTexture = getTexture(it.textureName, "diffuse")
                if (diffuseTexture != null) {
                    material.set(TextureAttribute.createDiffuse(diffuseTexture))
                }
                val emissiveTexture = getTexture(it.textureName, "emissive")
                if (emissiveTexture != null) {
                    material.set(TextureAttribute.createEmissive(emissiveTexture))
                }
                val specularTexture = getTexture(it.textureName, "specular")
                if (specularTexture != null) {
                    material.set(TextureAttribute.createSpecular(specularTexture))
                }
                val normalTexture = getTexture(it.textureName, "normal")
                if (normalTexture != null) {
                    material.set(TextureAttribute.createNormal(normalTexture))
                }
            }
            material
        })
    }

    fun loadAllTextures() {
        val texturesDirectory = Gdx.files.internal("textures")

        val parameters = TextureLoader.TextureParameter()
        parameters.minFilter = Texture.TextureFilter.MipMapLinearNearest
        parameters.genMipMaps = true

        for (textureFile in texturesDirectory.list(".jpg")) {
            println(textureFile)

            assetManager.load<Texture>(textureFile.path(), parameters).finishLoading()
        }
    }

    fun create(construction: Construction) : ModelInstance {
        val attributes = (VertexAttributes.Usage.Position or VertexAttributes.Usage.Normal or VertexAttributes.Usage.TextureCoordinates).toLong()

        val blockSize = 1f

        modelBuilder.begin()

        /*
        val blockMap: MutableMap<Triple<Int, Int, Int>, Block> = mutableMapOf()
        for(block in construction.blocks) {
            blockMap[Triple(block.x, block.y, block.z)] = block
        }
        // TODO how to check for rotated neighbouring cubes?
        */

        for(block in construction.blocks) {
            val node = modelBuilder.node()
            node.rotation.setEulerAngles(block.rotY * rotationStep, block.rotX * rotationStep, block.rotZ * rotationStep)
            node.translation.set(block.x * blockSize, block.y * blockSize, block.z * blockSize)

            var currentPart: MeshPartBuilder? = null
            var currentMaterial: Material? = null

            for (sideIndex in block.shape.sides.indices) {
                val color = block.colors.getOrNull(sideIndex) ?: construction.defaultColor
                val specularColor = block.specularColors.getOrNull(sideIndex) ?: construction.defaultSpecularColor
                val emissiveColor = block.emissiveColors.getOrNull(sideIndex)
                val textureName = block.textures.getOrNull(sideIndex) ?: block.defaultTexture
                val material = getMaterial(color, specularColor, emissiveColor, textureName)
                if (currentPart == null || currentMaterial != material) {
                    currentPart = modelBuilder.part("$sideIndex", GL20.GL_TRIANGLES, attributes, material)
                    currentMaterial = material
                }

                if (currentPart != null) {
                    val side = block.shape.sides[sideIndex]
                    when (side) {
                        is Rect -> currentPart.rect(side.points[0], side.points[1], side.points[2], side.points[3])
                        is Triangle -> currentPart.triangle(side.points[0], side.points[1], side.points[2])
                    }
                }
            }
        }

        return ModelInstance(modelBuilder.end())
    }
}

data class MaterialInfo(
        val color: Color?,
        val specularColor: Color?,
        val emissiveColor: Color?,
        val textureName: String?)
