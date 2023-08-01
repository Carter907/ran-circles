import org.carte.kottygraphics.core.Attribute
import org.carte.kottygraphics.core.GlApp
import org.carte.kottygraphics.core.GlDataType
import org.carte.kottygraphics.core.KeyHandler
import org.carte.kottygraphics.util.ProgramUtils
import org.carte.kottygraphics.util.ShaderUtils
import org.lwjgl.glfw.Callbacks.glfwFreeCallbacks
import org.lwjgl.opengl.GL33.*;
import org.lwjgl.glfw.GLFW.*;
import java.awt.Color
import java.util.*

class SomeGlApp : GlApp() {
    private lateinit var input: KeyHandler
    private var circles = mutableListOf<Circle>();
    private var vaoRef: Int = -1;
    private var circleVertices = 0;
    override fun onCreate() {


        input = KeyHandler(window);
        glClearColor(0f, 0f, 0f, 0f);

        programRef = ProgramUtils.initProgram(
            vertexShaderCode = ShaderUtils.getShaderCode("/vert.glsl"),
            fragmentShaderCode = ShaderUtils.getShaderCode("/frag.glsl")
        )
        vaoRef = glGenVertexArrays();
        glBindVertexArray(vaoRef);

        circles.add(Circle(programRef, .5f, .5f, .2f, Color(0f, 1f, 0f)))

        Attribute(GlDataType.VEC3, Circle.getPoints(1f).also { circleVertices = it.size })
            .associateVariable(programRef, "pos")

    }

    override fun onKeyEvent(window: Long, key: Int, action: Int) {


    }

    override fun onWindowResized(width: Int, height: Int) {
        glViewport(0, 0, width, height)

    }
    companion object {
        val rand = Random()
    }
    override fun render() {
        glClear(GL_COLOR_BUFFER_BIT.or(GL_DEPTH_BUFFER_BIT))
        glUseProgram(programRef);
        glBindVertexArray(vaoRef)

        if (time.toInt() % 100 in 0..20) {
            circles.add(Circle(programRef, rand.nextFloat()*2-1, rand.nextFloat()*2-1, rand.nextFloat(), Color(rand.nextFloat(), rand.nextFloat(), rand.nextFloat())))
        }

        for (circle in circles) {
            circle.uploadData();
            glDrawArrays(GL_TRIANGLE_FAN, 0, circleVertices);
        }
        if (circles.size > 40) {
            circles.subList(0, 5).clear();
        }


    }

    override fun shutdown() {

        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);
        glfwSetErrorCallback(null)!!.free();
        glfwTerminate();
    }

}