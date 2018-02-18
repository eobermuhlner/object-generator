package ch.obermuhlner.planet.engine.screen.welcome

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import ktx.app.KtxScreen
import ktx.app.use

class WelcomeScreen : KtxScreen {
    val font = BitmapFont()
    val batch = SpriteBatch().apply {
        color = Color.WHITE
    }

    override fun render(delta: Float) {
        batch.use {
            font.draw(it, "Hello Kotlin", 100f, 100f)
        }
    }

    override fun dispose() {
        font.dispose()
        batch.dispose()
    }
}
