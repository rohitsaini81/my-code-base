#include <iostream>
#include <GL/glew.h>
#include <GLFW/glfw3.h>

static unsigned int CompileShader(unsigned int type,const std::string& source){
  unsigned int id = glCreateShader(type);
  const char* src = source.c_str();
  glShaderSource(id, 1, &src,nullptr);
  glCompileShader(id);
  int result;
  glGetShaderiv(id, GL_COMPILE_STATUS, &result);
  // TODO: Error handling
  if(result == GL_FALSE){
    int length;
    glGetShaderiv(id, GL_INFO_LOG_LENGTH, &length);
    char* message = (char*)alloca(length * sizeof(char));
    glGetShaderInfoLog(id,length,&length,message);
    std::cout<<"faild to compile shader: "<<message<<std::endl;
    if(type == GL_VERTEX_SHADER){
      std::cout<<"successfully compiled vertex shader"<<std::endl;
    }
    if(type == GL_FRAGMENT_SHADER){
      std::cout<<"successfully compiled fragment shader"<<std::endl;
    }
//    glDeleteShader(id);
  }
  return id;
}

static unsigned int CreateShader(const std::string& vertexShader,const std::string& fragmentShader){
  unsigned int program = glCreateProgram();
  unsigned int vs = CompileShader(GL_VERTEX_SHADER,vertexShader);
  unsigned int fs = CompileShader(GL_FRAGMENT_SHADER,fragmentShader);

  glAttachShader(program,vs);
  glAttachShader(program,fs);
  glLinkProgram(program);
  glValidateProgram(program);
  glDeleteShader(vs);
  glDeleteShader(fs);
  return program;

}

int main()
{

    if (!glfwInit())
    {
        std::cerr << "Failed to initialize GLFW" << std::endl;
        return -1;
    }

    GLFWwindow *window = glfwCreateWindow(800, 600, "GLEW OPENGL", nullptr, nullptr);
    if (!window)
    {
        std::cerr << "Failed to create GLFW window" << std::endl;
        glfwTerminate();
        return -1;
    }

    std::cout<<"Window created"<<std::endl;
    std::cout<<"OpenGL version supported by your compiler"<<std::endl;

    if (glewInit() != GLEW_OK)
    {
        /* code */
//        std::cerr << "Failed to initialize GLEW" << std::endl;
//        return -1;
//        it's not initlisez yet
    }
    glfwMakeContextCurrent(window);

    // Initialize GLEW
    GLenum glewInitResult = glewInit();
    if (glewInitResult != GLEW_OK)
    {
        std::cerr << "Failed to initialize GLEW: " << glewGetErrorString(glewInitResult) << std::endl;
        return -1;
    }

    std::cout << "OpenGL version: " << glGetString(GL_VERSION) << std::endl;

    float position[6] = {
        -0.5f, -0.5f,
        0.0f, 0.5f,
        0.5, -0.5f};

    unsigned int buffer;
    glGenBuffers(1, &buffer);
    glBindBuffer(GL_ARRAY_BUFFER, buffer);
    glBufferData(GL_ARRAY_BUFFER, 6 * sizeof(float), position, GL_STATIC_DRAW);

    glEnableVertexAttribArray(0);
    glVertexAttribPointer(0,2,GL_FLOAT,GL_FALSE,sizeof(float)*2,0);
//    glBindBuffer(GL_ARRAY_BUFFER, 0);
    std::string vertexShader =
        "#version 330 core\n"
        "\n"
        "layout(location = 0) in vec4 position;\n"
        "void main()\n"
        "{\n"
        "gl_Position = position;\n"
        "}\n"
        ;
    std::string fragmentShader =
     "#version 330 core\n"
     "\n"
     "layout(location = 0) out vec4 color;\n"
     "void main()\n"
     "{\n"
      "color = vec4(1.0, 0.0, 0.0, 1.0);\n"
     "}\n"
     ;
    unsigned int shader = CreateShader(vertexShader,fragmentShader);
    glUseProgram(shader);
    while (!glfwWindowShouldClose(window))
    {
        glClear(GL_COLOR_BUFFER_BIT);

        // Render OpenGL content here
        glDrawArrays(GL_TRIANGLES,0,3);
        

        glfwSwapBuffers(window);
        glfwPollEvents();
    }
    std::cout<<"Program destroyed"<<std::endl;
    std::cout<<shader;
    glDeleteProgram(shader);

    glfwDestroyWindow(window);
    glfwTerminate();
    return 0;
}
