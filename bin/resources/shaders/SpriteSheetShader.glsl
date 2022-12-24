#shader vertex
#version 330 core

layout(location = 0) in vec3 position;
layout(location = 1) in vec2 aTexCoord;

out vec2 texCoord;

uniform mat4 projectionMatrix;
uniform mat4 modelMatrix;
uniform mat4 viewMatrix;

void main()
{
	gl_Position = projectionMatrix * viewMatrix * modelMatrix * vec4(position, 1.0);
	texCoord = aTexCoord;
};

#shader fragment
#version 330 core

layout(location = 0) out vec4 fragColor;

in vec2 texCoord;

uniform vec4 color;

uniform sampler2D texture1;

uniform float fullColorSet = 0;

uniform vec2 textureSize;
uniform vec2 spriteSize;
uniform float index;



void main()
{
	float width = textureSize.x;
    float height = textureSize.y;

	float dx = spriteSize.x / width;
    float dy = spriteSize.y / height;
    float cols = width / spriteSize.x;
    float col = mod(index, cols);
    float row = floor(index / cols);
    vec2 uv = vec2(dx * texCoord.x + col * dx, 1.0 - dy - row * dy + dy * texCoord.y);

	if (texture(texture1, uv).a == 0) {
		discard;
	}

	if (fullColorSet == 0.0) {
		fragColor = texture(texture1, uv) * color;
	} 
	if (fullColorSet == 1.0) {
		fragColor = color;
	}
};