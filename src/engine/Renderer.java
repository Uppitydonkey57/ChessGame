package engine;

import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.*;

import java.io.FileNotFoundException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.opengl.*;
import org.lwjgl.opengl.GL;
import org.lwjgl.BufferUtils;

import static org.lwjgl.glfw.GLFW.*;

import org.joml.*;

public class Renderer {

	float vertices[] = {
			// positions // texture coordinates
			0.5f, 0.5f, 0.0f, 1.0f, 1.0f, // top right
			0.5f, -0.5f, 0.0f, 1.0f, 0.0f, // bottom right
			-0.5f, -0.5f, 0.0f, 0.0f, 0.0f, // bottom left
			-0.5f, 0.5f, 0.0f, 0.0f, 1.0f // top left
	};

	int[] indices = { 0, 1, 3, // first triangle
			1, 2, 3 // second triangle
	};

	int quadVBO;
	int quadIBO;

	int vao;

	private Matrix4f viewMatrix = new Matrix4f().identity();
	private Matrix4f projectionMatrix;// .scale(128);

	private Camera currentCamera = null;
	
	float screenWidth = 4f;
	float screenHeight = 3f;
	
	Vector2f dimensions = null;
	
	Window window;
	
	public Renderer(Window window, Vector2f dimensions) {
		this.window = window;
		glfwMakeContextCurrent(window.window);
		GL.createCapabilities();
		
		screenWidth = dimensions.x;
		screenHeight = dimensions.y;
		
		projectionMatrix =  new Matrix4f().ortho(-screenWidth, screenWidth, -screenHeight, screenHeight, -10.0f, 100.0f);
		
		// viewMatrix.translate(new Vector3f(0, 0, -10));

		// Set up the VBO, IBO and VAO since we'll only need one render object

		// Generate VAO
		vao = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(vao);

		// Generate Vertex Buffer
		quadVBO = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, quadVBO);
		
		FloatBuffer VBOBuffer = BufferUtils.createFloatBuffer(vertices.length);
		VBOBuffer.put(vertices);
		VBOBuffer.flip();
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, VBOBuffer, GL15.GL_DYNAMIC_DRAW);
		
		
		GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 5 * 4, 0);
		GL20.glVertexAttribPointer(1, 2, GL11.GL_FLOAT, false, 5 * 4, 3 * 4);

		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);

		// Generate Index Buffer
		quadIBO = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, quadIBO);
		IntBuffer IBOBuffer = BufferUtils.createIntBuffer(indices.length);
		IBOBuffer.put(indices);
		IBOBuffer.flip();
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, IBOBuffer, GL15.GL_STATIC_DRAW);
		

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}
	
	public void SetCamera(Camera camera) {
		currentCamera = camera;
	}

	public void clearScreen(Vector4f color) {
		GL11.glClearColor(color.x, color.y, color.z, color.w);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
	}

	public void render(Shader shader, Texture texture, Transform transform, Vector4f color) {
		Matrix4f modelMatrix = transform.getTransformMatrix();
		if (currentCamera != null) {			
			Vector2f cameraPos = currentCamera.position;
			modelMatrix.translate(new Vector3f(cameraPos.x * -1, cameraPos.y * -1, 0));
		}
		GL20.glBindBuffer(GL15.GL_ARRAY_BUFFER, quadVBO);
		GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 5 * 4, 0);
		GL20.glVertexAttribPointer(1, 2, GL11.GL_FLOAT, false, 5 * 4, 3 * 4);
		shader.bind();
		texture.bind(0);
		shader.setUniform1i("texture1", 0);
		shader.setUniformMat4f("modelMatrix", modelMatrix);
		shader.setUniformMat4f("viewMatrix", viewMatrix);
		shader.setUniformMat4f("projectionMatrix", projectionMatrix);
		shader.setUniform4f("color", color);
		GL11.glDrawElements(GL11.GL_TRIANGLES, indices.length, GL11.GL_UNSIGNED_INT, 0);
	}
	
	public void render(Shader shader, Texture texture, Transform transform, Vector4f color, int spriteWidth, int spriteHeight, Vector2f coords) {
		int spriteVBO = GL15.glGenBuffers();
		GL15.glBindBuffer(GL20.GL_ARRAY_BUFFER, spriteVBO);
		
		float spriteUnitX = (float)spriteWidth / (float)texture.getWidth();
		float spriteUnitY = (float)spriteHeight / (float)texture.getHeight();
		float rows =    (float)texture.getWidth() / (float)spriteWidth;
		float columns = (float)texture.getHeight() / (float)spriteHeight;
		
		float spriteData[] = {
				// positions // texture coordinates
				0.5f, 0.5f, 0.0f, spriteUnitX*(coords.x+1), spriteUnitY*(coords.y+1), // top right
				0.5f, -0.5f, 0.0f,spriteUnitX*(coords.x+1), spriteUnitY*coords.y,     // bottom right
			   -0.5f, -0.5f, 0.0f,spriteUnitX*coords.x,     spriteUnitY*coords.y,     // bottom left
			   -0.5f, 0.5f, 0.0f, spriteUnitX*coords.x,     spriteUnitY*(coords.y+1)  // top left
		};
		FloatBuffer spriteBuffer = BufferUtils.createFloatBuffer(spriteData.length);
		spriteBuffer.put(spriteData);
		spriteBuffer.flip();
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, spriteData, GL15.GL_DYNAMIC_DRAW);
		GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 5 * 4, 0);
		GL20.glVertexAttribPointer(1, 2, GL11.GL_FLOAT, false, 5 * 4, 3 * 4);
		Matrix4f modelMatrix = transform.getTransformMatrix();
		if (currentCamera != null) {			
			Vector2f cameraPos = currentCamera.position;
			modelMatrix.translate(new Vector3f(cameraPos.x * -1, cameraPos.y * -1, 0));
		}
		shader.bind();
		texture.bind(0);
		shader.setUniform1i("texture1", 0);
		shader.setUniformMat4f("modelMatrix", modelMatrix);
		shader.setUniformMat4f("viewMatrix", viewMatrix);
		shader.setUniformMat4f("projectionMatrix", projectionMatrix);
		shader.setUniform4f("color", color);
		GL11.glDrawElements(GL11.GL_TRIANGLES, indices.length, GL11.GL_UNSIGNED_INT, 0);
		GL20.glDeleteBuffers(spriteVBO);
	}
	
	
	public Vector2f getWorldCoordinates(Vector2f screenPos) {
		Vector2f position = new Vector2f((screenPos.x/window.width)*screenWidth, -(screenPos.y/window.height)*screenHeight);
		position.sub(new Vector2f(screenWidth/2, -screenHeight/2));
		position.mul(2);
		if (currentCamera != null) position.add(new Vector2f(currentCamera.position));
		return position;
	}
}
