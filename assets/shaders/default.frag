#version 330 core

in vec4 fColor;
in vec2 fUV;
in vec3 fNormal;
in float fTextureID;

uniform sampler2DArray uTextureArray;

out vec4 outColor;

void main() {
    vec3 arrayUVs = vec3(fUV, fTextureID);
    outColor = texture(uTextureArray, arrayUVs) * fColor;
}