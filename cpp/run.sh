echo "Compiling"
g++ -o output glew_example.cpp -lGLEW -lGL -lglfw
echo "Running Program"                                                                                                      
g++ ./output 
echo "Exit Code 0"