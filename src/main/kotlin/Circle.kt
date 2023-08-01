import org.carte.kottygraphics.core.GlDataType
import org.carte.kottygraphics.core.Uniform
import org.carte.kottygraphics.core.math.Vector
import java.awt.Color
import kotlin.math.*;
data class Circle(
    val programRef: Int,
    val x: Float,
    val y: Float,
    val radius: Float,
    val color: Color,
) {
    private var colorU: Uniform<Vector>
    private var translU: Uniform<Vector>
    private var radiusU: Uniform<Float>
    var points: Int = 0;
    init {
        val red = color.red / 255.0;
        val green = color.green / 255.0;
        val blue = color.blue / 255.0;

        colorU = Uniform(
            GlDataType.VEC3,
            Vector(red, green, blue)
        ).apply {
            locateVariable(programRef, "color")
        }


        translU = Uniform(
            GlDataType.VEC3,
            Vector(x.toDouble(), y.toDouble(), 0.0)
        ).apply {
            locateVariable(programRef, "transl")
        }

        radiusU = Uniform(
            GlDataType.FLOAT,
            radius
        ).apply {
            locateVariable(programRef, "radius")
        }


    }
    companion object {
        fun getPoints(radius: Float): FloatArray {
            val stepRate = (1 / radius).toInt();

            var vertices = mutableListOf<Float>()
            for (i in 0..360 step(stepRate)) {
                vertices.add(cos(i.toDouble()).toFloat())
                vertices.add(sin(i.toDouble()).toFloat())
                vertices.add(0f)
            }
            return vertices.toFloatArray()
        }
    }



    fun uploadData() {

        colorU.uploadData();
        radiusU.uploadData();
        translU.uploadData();
    }
}