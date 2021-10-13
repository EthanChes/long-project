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
    static double time = 0 ;
    static boolean top_left = false ;
    static boolean top_right = false ;
    static boolean bottom_right = false ;
    static boolean bottom_left =  false ;

    // The window handle
    private long window;
    public double getMousePosX(long window) {
        DoubleBuffer x_pos = BufferUtils.createDoubleBuffer(1);
        glfwGetCursorPos(window,x_pos,null);
        return x_pos.get(0);
    }
    public double getMousePosY(long window) {
        DoubleBuffer y_pos = BufferUtils.createDoubleBuffer(1);
        glfwGetCursorPos(window,null,y_pos);
        return y_pos.get(0);
    }
    public double convertMousePosX(long window) {
        return (getMousePosX(window)/500 -1) ;
    }
    public double convertMousePosY(long window) {
        return -(getMousePosY(window)/500 - 1) ;
    }
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
        GL.createCapabilities(); // Make sure this stays at top
        glfwMakeContextCurrent(window);
        glEnable(GL_TEXTURE_2D);
        Texture tex = new Texture("./res/img.png");

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
        boolean move_to_cursor = false ;
        while (!glfwWindowShouldClose(window)) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
            // Poll for window events. The key callback above will only be
            // invoked during this call.
            glfwPollEvents();

            tex.bind();

            // Square Drawing with cool colors Arrows to extend/minimize cube
            glBegin(GL_QUADS);
            //glColor4f(0.0f, 1.0f, 0.0f, 0.0f);
            glTexCoord2f(0,0);
            glVertex2f(cube_top_left_x, cube_top_left_y); // -.5 .5
            //glColor4f(1.0f, 0.0f, 0.0f, 0.0f);
            glTexCoord2f(0,1);
            glVertex2f(cube_top_right_x, cube_top_right_y); // .5 .5
            //glColor4f(0.0f, 0.0f, 1.0f, 0.0f);
            glTexCoord2f(1,1);
            glVertex2f(cube_bottom_right_x, cube_bottom_right_y); // .5 -.5
            //glColor4f(1.0f, 1.0f, 1.0f, 0.0f);
            glTexCoord2f(1,0);
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
            if (glfwGetKey(window, GLFW_KEY_LEFT_SHIFT) == GL_TRUE) {
                System.out.println("Enabling Transfer of Vertex Selection");
                top_right = false ;
                top_left = false ;
                bottom_left = false ;
                bottom_right = false ;
            }
            if (move_to_cursor) { // Make Sure Boolean is true before running (Moves selected corner to cursor) Until Enter is pressed
                if (glfwGetMouseButton(window,GLFW_MOUSE_BUTTON_1) == GL_TRUE) {
                    if  ((convertMousePosX(window) > cube_top_right_x - .1 && convertMousePosX(window) < cube_top_right_x + .1 && convertMousePosY(window) > cube_top_right_y - .1 && convertMousePosY(window) < cube_top_right_y + .1) || top_right)
                    {
                        System.out.println("Top Right Selected");
                        if (! (top_left || bottom_right || bottom_left)) {
                            cube_top_right_x = (float) convertMousePosX(window);
                            cube_top_right_y = (float) convertMousePosY(window);
                            top_right = true;
                        }
                    }
                    if  ((convertMousePosX(window) > cube_bottom_right_x - .1 && convertMousePosX(window) < cube_bottom_right_x + .1 && convertMousePosY(window) > cube_bottom_right_y - .1 && convertMousePosY(window) < cube_bottom_right_y + .1) || bottom_right)
                    {
                        System.out.println("Bottom Right Selected");
                        if (! (top_left || top_right || bottom_left)) {
                            cube_bottom_right_x = (float) convertMousePosX(window);
                            cube_bottom_right_y = (float) convertMousePosY(window);
                            bottom_right = true;
                        }
                    }
                    if  ((convertMousePosX(window) > cube_top_left_x - .1 && convertMousePosX(window) < cube_top_left_x + .1 && convertMousePosY(window) > cube_top_left_y - .1 && convertMousePosY(window) < cube_top_left_y + .1) || top_left)
                    {
                        System.out.println("Top Left Selected");
                        if (! (top_right || bottom_right || bottom_left)) {
                            cube_top_left_x = (float) convertMousePosX(window);
                            cube_top_left_y = (float) convertMousePosY(window);
                            top_left = true;
                        }
                    }
                    if  ((convertMousePosX(window) > cube_bottom_left_x - .1 && convertMousePosX(window) < cube_bottom_left_x + .1 && convertMousePosY(window) > cube_bottom_left_y - .1 && convertMousePosY(window) < cube_bottom_left_y + .1) || bottom_left)
                    {
                        System.out.println("Bottom Left Selected");
                        if (! (top_right || bottom_right || top_left)) {
                            cube_bottom_left_x = (float) convertMousePosX(window);
                            cube_bottom_left_y = (float) convertMousePosY(window);
                            bottom_left = true;
                        }
                    }
                }
            }
            // Toggle Move Square With Cursor
            if (glfwGetKey(window,GLFW_KEY_ENTER) == GL_TRUE) {
                if (move_to_cursor && (glfwGetTime() - time > .1)) {
                    move_to_cursor = false ;
                    time = glfwGetTime();
                }
                else if (glfwGetTime() - time > .1) {
                    move_to_cursor = true ;
                    top_right = false ;
                    top_left = false ;
                    bottom_left = false ;
                    bottom_right = false ;
                    time = glfwGetTime();
                }
            }
            glfwSwapBuffers(window); // swap the color buffers}}}}
        }
    }

    public static void main(String[] args) {
        new HelloWorld().run();
    }

}