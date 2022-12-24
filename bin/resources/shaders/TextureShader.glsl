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

void main()
{
	if (texture(texture1, texCoord).a <= 0.1) {
		discard;
	}

	if (fullColorSet == 0.0) {
		fragColor = texture(texture1, texCoord) * color;
	} 
	if (fullColorSet == 1.0) {
		fragColor = color;
	}
};