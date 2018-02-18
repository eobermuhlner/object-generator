package ch.obermuhlner.planet.engine.render

import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.graphics.Mesh
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.ObjectMap
import com.badlogic.gdx.graphics.g3d.ModelBatch
import com.badlogic.gdx.graphics.g3d.model.MeshPart
import com.badlogic.gdx.graphics.g3d.utils.RenderContext
import com.badlogic.gdx.graphics.g3d.utils.RenderableSorter
import com.badlogic.gdx.graphics.g3d.utils.ShaderProvider


class FrustumCullingModelBatch : ModelBatch {

    private val radiuses = ObjectMap<Mesh, Float>()

    private val tmp = Vector3()

    constructor() : super()
    constructor(context: RenderContext?) : super(context)
    constructor(context: RenderContext?, shaderProvider: ShaderProvider?, sorter: RenderableSorter?) : super(context, shaderProvider, sorter)
    constructor(context: RenderContext?, shaderProvider: ShaderProvider?) : super(context, shaderProvider)
    constructor(context: RenderContext?, sorter: RenderableSorter?) : super(context, sorter)
    constructor(shaderProvider: ShaderProvider?, sorter: RenderableSorter?) : super(shaderProvider, sorter)
    constructor(shaderProvider: ShaderProvider?) : super(shaderProvider)
    constructor(sorter: RenderableSorter?) : super(sorter)
    constructor(vertexShader: FileHandle?, fragmentShader: FileHandle?) : super(vertexShader, fragmentShader)
    constructor(vertexShader: String?, fragmentShader: String?) : super(vertexShader, fragmentShader)

    override fun flush() {
        val iter = renderables.iterator()

        while (iter.hasNext()) {
            val renderable = iter.next()
            renderable.worldTransform.getTranslation(tmp)
            if (!camera.frustum.sphereInFrustumWithoutNearFar(tmp, getRadiusOfMesh(renderable.meshPart))) {
                iter.remove()
            }
        }
        super.flush()
    }

    private fun getRadiusOfMesh(meshPart: MeshPart): Float {
        var radius: Float? = radiuses.get(meshPart.mesh)
        if (radius != null) {
            return radius
        }

        val boundingBox = meshPart.mesh.calculateBoundingBox()
        radius = Math.max(Math.max(boundingBox.width, boundingBox.height), boundingBox.depth)

        radiuses.put(meshPart.mesh, radius)
        return radius!!
    }
}