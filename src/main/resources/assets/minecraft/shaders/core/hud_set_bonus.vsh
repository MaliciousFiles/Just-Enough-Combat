#version 150

in vec3 Position;
in vec2 UV0;
in vec2 UV3;

uniform mat4 ModelViewMat;
uniform mat4 ProjMat;
uniform sampler2D Sampler3;

out vec2 texCoord0;
out vec2 texCoord3;

void main() {
    gl_Position = ProjMat * ModelViewMat * vec4(Position, 1.0);

    texCoord0 = UV0;
    texCoord3 = UV3;
}
