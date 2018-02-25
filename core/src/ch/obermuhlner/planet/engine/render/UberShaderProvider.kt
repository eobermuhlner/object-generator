package ch.obermuhlner.planet.engine.render

import com.badlogic.gdx.graphics.g3d.Renderable
import com.badlogic.gdx.graphics.g3d.Shader
import com.badlogic.gdx.graphics.g3d.utils.BaseShaderProvider
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader
import com.badlogic.gdx.Gdx

class UberShaderProvider(private val shaderName: String = "default") : BaseShaderProvider() {
    override fun createShader(renderable: Renderable?): Shader {
        val vert = Gdx.files.internal("shaders/$shaderName.vertex.glsl").readString()
        val frag = Gdx.files.internal("shaders/$shaderName.fragment.glsl").readString()

        val config = DefaultShader.Config(vert, frag)
        return DefaultShader(renderable, config)
    }
}
