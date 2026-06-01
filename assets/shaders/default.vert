#version 330 core

layout (location = 0) in vec3 aPos;
layout (location = 1) in vec4 aColor;
layout (location = 2) in vec2 aTextureCoordinates;
layout (location = 3) in vec3 aNormal;
layout (location = 4) in float aTextureID;

out vec4 fColor;
out vec2 fUV;
out vec3 fNormal;
out float fTextureID;

uniform mat4 uProjection;
uniform mat4 uView;

void main() {
    fColor = aColor;
    fUV = aTextureCoordinates;
    fNormal = aNormal;
    fTextureID = aTextureID;

    gl_Position = uProjection * uView * vec4(aPos, 1.0);
}