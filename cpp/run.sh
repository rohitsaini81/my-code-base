echo "Compiling"
g++ -o output glew_example.cpp -lGLEW -lGL -lglfw
# -LSDL2 for sdl library
echo "Running Program"                                                                                                      
g++ ./output 
echo "Exit Code 0"