package ch.obermuhlner.planet.engine.screen.turtle

import ch.obermuhlner.planet.engine.construction.turtle.SideType
import ch.obermuhlner.planet.engine.construction.turtle.Turtle
import ch.obermuhlner.planet.engine.render.FrustumCullingModelBatch
import ch.obermuhlner.planet.engine.render.UberShaderProvider
import ch.obermuhlner.planet.engine.screen.turtle.model.SpaceShipGenerator
import ch.obermuhlner.planet.engine.util.Random
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.PerspectiveCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g3d.Environment
import com.badlogic.gdx.graphics.g3d.Material
import com.badlogic.gdx.graphics.g3d.Model
import com.badlogic.gdx.graphics.g3d.ModelInstance
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController
import ktx.app.KtxScreen
import ktx.app.use
import kotlin.math.roundToInt

class TurtleScreen : KtxScreen {
    val font = BitmapFont()
    val spriteBatch = SpriteBatch().apply {
        color = Color.WHITE
    }

    val assetManager = AssetManager()

    val modelBatch = FrustumCullingModelBatch(UberShaderProvider())
    val camera = PerspectiveCamera(67f, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat()).apply {
        val cameraDistance = 25f
        position.set(cameraDistance, cameraDistance, cameraDistance)
        lookAt(0f, 0f, 0f)
        near = 1f
        far = 300f
        update()
    }

    val modelInstances = mutableListOf<ModelInstance>().apply {
        val generator = SpaceShipGenerator()
        add(ModelInstance(generator.generate(Turtle(), Random())))
    }

    val fogColor = Color(0.0f, 0.0f, 0.2f, 1f)
    val ambientLightColor = Color(0.2f, 0.2f, 0.3f, 1.0f)

    val controller = CameraInputController(camera).apply {
        Gdx.input.inputProcessor = this
    }

    val environment = Environment().apply {
        set(ColorAttribute(ColorAttribute.AmbientLight, ambientLightColor))
        set(ColorAttribute(ColorAttribute.Fog, fogColor))
        add(DirectionalLight().set(Color.LIGHT_GRAY, -1f, -0.8f, -0.2f))
    }

    override fun render(delta: Float) {
        controller.update()

        Gdx.gl.glViewport(0, 0, Gdx.graphics.width, Gdx.graphics.height)
        Gdx.gl.glClearColor(fogColor.r, fogColor.g, fogColor.b, fogColor.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT + GL20.GL_DEPTH_BUFFER_BIT)

        modelBatch.begin(camera)
        modelBatch.render(modelInstances, environment)
        modelBatch.end()

        spriteBatch.use {
            val fps = (1f/delta).roundToInt()
            font.draw(it, "${modelInstances.size} model instances, $fps FPS", 10f, 10 + font.lineHeight)
        }

    }

    override fun dispose() {
        font.dispose()
        spriteBatch.dispose()

        modelBatch.dispose()
    }
}
