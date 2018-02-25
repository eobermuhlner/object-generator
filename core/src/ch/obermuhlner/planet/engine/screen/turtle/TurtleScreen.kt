package ch.obermuhlner.planet.engine.screen.turtle

import ch.obermuhlner.obj3d.turtle.Turtle
import ch.obermuhlner.planet.engine.render.FrustumCullingModelBatch
import ch.obermuhlner.planet.engine.render.UberShaderProvider
import ch.obermuhlner.planet.engine.screen.turtle.model.*
import ch.obermuhlner.planet.engine.util.Random
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.PerspectiveCamera
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g3d.Environment
import com.badlogic.gdx.graphics.g3d.ModelInstance
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Cell
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import ktx.app.KtxScreen
import ktx.app.use
import ktx.scene2d.*
import ktx.actors.*
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

    val stage = Stage()

    val controller = CameraInputController(camera)

    val modelInstances = mutableListOf<ModelInstance>()

    var modelGenerator: TurtleModelGenerator = RandomSpaceShipGenerator()

    init {
        Gdx.input.inputProcessor = InputMultiplexer(stage, controller)
        prepareStage()
    }

    val fogColor = Color(0.0f, 0.0f, 0.2f, 1f)
    val ambientLightColor = Color(0.2f, 0.2f, 0.3f, 1.0f)

    val environment = Environment().apply {
        set(ColorAttribute(ColorAttribute.AmbientLight, ambientLightColor))
        set(ColorAttribute(ColorAttribute.Fog, fogColor))
        add(DirectionalLight().set(Color.LIGHT_GRAY, -1f, -0.8f, -0.2f))
    }

    fun prepareStage() {
        val skin = Skin()
        skin.addRegions(TextureAtlas(Gdx.files.internal("data/ui/uiskin.atlas")))
        skin.add("small-font", BitmapFont())
        skin.add("default-font", BitmapFont())
        skin.add("large-font", BitmapFont())
        skin.load(Gdx.files.internal("data/ui/uiskin.json"))

        Scene2DSkin.defaultSkin = skin

        val root = table {
            selectBox<String, Cell<*>> {
                -"Random Spaceship"
                -"Spaceship"
                -"Spacestation"
                -"Test"
            }.onChangeEvent { _, actor -> pickModelGenerator(actor.selected); createContent() }
            button {
                label("Random")
            }.onClick { createContent() }
        }

        root.top()
        root.left()
        root.setFillParent(true)
        stage.addActor(root)

        createContent()
    }

    fun pickModelGenerator(name: String) {
        when (name) {
            "Random Spaceship" -> modelGenerator = RandomSpaceShipGenerator()
            "Spaceship" -> modelGenerator = SpaceShipGenerator()
            "Spacestation" -> modelGenerator = SpaceStationGenerator()
            "1est" -> modelGenerator = TestModelGenerator()
        }
    }

    fun createContent() {
        modelInstances.clear()
        modelInstances.add(ModelInstance(modelGenerator.generate(Turtle(), Random())))
    }

    override fun resize(width: Int, height: Int) {
        super.resize(width, height)
        stage.viewport.update(width, height)
    }

    override fun render(delta: Float) {
        controller.update()
        stage.act()

        Gdx.gl.glViewport(0, 0, Gdx.graphics.width, Gdx.graphics.height)
        Gdx.gl.glClearColor(fogColor.r, fogColor.g, fogColor.b, fogColor.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT + GL20.GL_DEPTH_BUFFER_BIT)

        modelBatch.begin(camera)
        modelBatch.render(modelInstances, environment)
        modelBatch.end()

        stage.draw()

        spriteBatch.use {
            val fps = (1f/delta).roundToInt()
            font.draw(it, "${modelInstances.size} model instances, $fps FPS", 10f, 10 + font.lineHeight)
        }
    }

    override fun dispose() {
        stage.dispose()
        font.dispose()
        spriteBatch.dispose()

        modelBatch.dispose()
    }
}
