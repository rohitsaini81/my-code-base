//
// Created by rohit on 25/11/24.
//

#include <SDL2/SDL.h>
#include <iostream>

int main(int argc, char* argv[]) {
    // Initialize SDL
    if (SDL_Init(SDL_INIT_VIDEO) != 0) {
        std::cerr << "SDL_Init Error: " << SDL_GetError() << std::endl;
        return 1;
    }


    SDL_Renderer* renderer;
    SDL_Texture* tex;

}


SDL_Texture *IMG_LoadTexture(SDL_Renderer *renderer, const char *file)
{
    SDL_Surface *IMG_Load(const char *file);
    SDL_Texture *texture = NULL;
    SDL_Surface *surface = IMG_Load(file);
    if (surface) {
        texture = SDL_CreateTextureFromSurface(renderer, surface);
        SDL_DestroySurface(surface);
    }
    return texture;
}
