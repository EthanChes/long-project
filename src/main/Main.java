package main;
import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;
import java.nio.*;
import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

class HelloWorld {
    static float red = 0.0f;
    static float blue = 0.0f;
    static float green = 0.0f;
    static boolean up_color = true;

    // The window handle
    private long window;

    public void run() {
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");

        init();
        loop();

        // Free the window callbacks and destroy the window
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        // Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    private void init() {
        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if ( !glfwInit() )
            throw new IllegalStateException("Unable to initialize GLFW");

        // Configure GLFW
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable

        // Create the window
        window = glfwCreateWindow(1000, 1000, "Color Wheel", NULL, NULL);
        if ( window == NULL )
            throw new RuntimeException("Failed to create the GLFW window");



        // Change Color of Window by Hitting Space in Color Wheel Order.
        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {boolean up_col = up_color; float r = red ; float g = green ; float b = blue;
            if ( (key == GLFW_KEY_SPACE || key == GLFW_KEY_ESCAPE) && action == GLFW_RELEASE )
                if (r <= 1 && up_col){ r += .05;}
                else if (b <= 1 && up_col) { b += .05; }
                else if (g <= 1 && up_col) { g += .05; if (g >= .99) {up_col = false;} }
                else if (r > 0 && (! up_col)) {r -= .05;}
                else if (b > 0 && (! up_col)) {b -= .05;}
                else if (g > .1 && (! up_col)) {g -=.05;}
                else if (! up_col){ r = 0; g = 0; b = 0; up_col = true; }
            System.out.println("Color Wheel Data (red, green, blue) - " + r + " " + g + " " + b);
            glClearColor(r,g,b,0.0f);
            float place_hold = red = r;
            float place_hold2 = blue = b ;
            float place_hold3 = green = g ;
            boolean place_holder = up_color = up_col;
            if (key == GLFW_KEY_ESCAPE)
            {
                glfwSetWindowShouldClose(window, true);
            }
        });

        // Get the thread stack and push a new frame
        try ( MemoryStack stack = stackPush() ) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            // Get the window size passed to glfwCreateWindow
            glfwGetWindowSize(window, pWidth, pHeight);

            // Get the resolution of the primary monitor
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            // Center the window
            glfwSetWindowPos(
                    window,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );
        } // the stack frame is popped automatically

        // Make the OpenGL context current
        glfwMakeContextCurrent(window);
        // Enable v-sync
        glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(window);
    }

    private void loop() {
        GL.createCapabilities();
        glfwMakeContextCurrent(window);

        // Set the clear color
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.
        float cube_top_left_y = .5f;
        float cube_top_right_y = .5f;
        float cube_bottom_right_y = -.5f;
        float cube_bottom_left_y = -.5f;
        float cube_top_left_x = -.5f;
        float cube_top_right_x = .5f;
        float cube_bottom_right_x = .5f;
        float cube_bottom_left_x = -.5f;
        while (!glfwWindowShouldClose(window)) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

            // Poll for window events. The key callback above will only be
            // invoked during this call.
            glfwPollEvents();
            // Square Drawing with cool colors Arrows to extend/minimize cube
            glBegin(GL_QUADS);
            glColor4f(0.0f, 1.0f, 0.0f, 0.0f);
            glVertex2f(cube_top_left_x, cube_top_left_y); // -.5 .5
            glColor4f(1.0f, 0.0f, 0.0f, 0.0f);
            glVertex2f(cube_top_right_x, cube_top_right_y); // .5 .5
            glColor4f(0.0f, 0.0f, 1.0f, 0.0f);
            glVertex2f(cube_bottom_right_x, cube_bottom_right_y); // .5 -.5
            glColor4f(1.0f, 1.0f, 1.0f, 0.0f);
            glVertex2f(cube_bottom_left_x, cube_bottom_left_y); // -.5 -.5
            glEnd();
            // Change Size of Cube Using Arrows
            if (glfwGetKey(window, GLFW_KEY_UP) == GL_TRUE) {
                System.out.println("Key Up");
                cube_top_right_y += .01; cube_top_left_y += .01; cube_bottom_left_y -= .01; cube_bottom_right_y -=.01;
            }
           if (glfwGetKey(window, GLFW_KEY_RIGHT) == GL_TRUE) {
                System.out.println("Key Right");
                cube_top_right_x += .01; cube_bottom_right_x += .01; cube_top_left_x -= .01; cube_bottom_left_x -=.01;
            }
            if (glfwGetKey(window, GLFW_KEY_LEFT) == GL_TRUE) {
                System.out.println("Key Left");
                cube_bottom_left_x += .01; cube_top_left_x += .01; cube_top_right_x -= .01; cube_bottom_right_x -= .01;
            }
            if (glfwGetKey(window, GLFW_KEY_DOWN) == GL_TRUE) {
                System.out.println("Key Down");
                cube_bottom_left_y += .01; cube_bottom_right_y += .01; cube_top_right_y -= .01; cube_top_left_y -=.01;
            }
            // Change Location/Translate Cube using WASD
            if (glfwGetKey(window,GLFW_KEY_W) == GL_TRUE) {
                System.out.println("Translate Up");
                cube_top_right_y += .01; cube_top_left_y += .01; cube_bottom_right_y += .01; cube_bottom_left_y += .01;
            }
            if (glfwGetKey(window,GLFW_KEY_S) == GL_TRUE) {
                System.out.println("Translate Down");
                cube_top_right_y -= .01; cube_top_left_y -= .01; cube_bottom_right_y -= .01; cube_bottom_left_y -= .01;
            }
            if (glfwGetKey(window,GLFW_KEY_A) == GL_TRUE) {
                System.out.println("Translate Left");
                cube_top_right_x -= .01; cube_top_left_x -= .01; cube_bottom_right_x -= .01; cube_bottom_left_x -= .01;
            }
            if (glfwGetKey(window,GLFW_KEY_D) == GL_TRUE) {
                System.out.println("Translate Right");
                cube_top_right_x += .01; cube_top_left_x += .01; cube_bottom_right_x += .01; cube_bottom_left_x += .01;
            }
            glfwSwapBuffers(window); // swap the color buffers}}}}
        }
    }

    public static void main(String[] args) {
        new HelloWorld().run();
    }

}
