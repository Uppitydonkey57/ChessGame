package engine;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.FloatBuffer;
import java.util.Scanner;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;
import org.joml.*;

public class Shader {
	private int program;

	public Shader(String path) {
		program = GL20.glCreateProgram();
		String[] source = ParseShader(path);
		int fs = createShader(source[1], GL20.GL_FRAGMENT_SHADER);
		int vs = createShader(source[0], GL20.GL_VERTEX_SHADER);

		GL20.glAttachShader(program, fs);
		GL20.glAttachShader(program, vs);
		GL20.glLinkProgram(program);
		GL20.glValidateProgram(program);

		GL20.glDeleteShader(fs);
		GL20.glDeleteShader(vs);
	}

	public void bind() {
		GL20.glUseProgram(program);
	}

	public int getUniformLocation(String name) {
		return GL20.glGetUniformLocation(program, name);
	}

	public void setUniform1f(String name, float value) {
		GL20.glUniform1f(getUniformLocation(name), value);
	}

	public void setUniform1i(String name, int value) {
		GL20.glUniform1i(getUniformLocation(name), value);
	}

	public void setUniform4f(String name, float v0, float v1, float v2, float v3) {
		GL20.glUniform4f(getUniformLocation(name), v0, v1, v2, v3);
	}

	public void setUniform4f(String name, Vector4f value) {
		GL20.glUniform4f(getUniformLocation(name), value.x, value.y, value.z, value.w);
	}
	
	public void setUniform2f(String name, float v0, float v1) {
		GL20.glUniform2f(getUniformLocation(name), v0, v1);
	}

	public void setUniformMat4f(String name, Matrix4f value) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
		value.get(buffer);
		GL20.glUniformMatrix4fv(getUniformLocation(name), false, buffer);
	}

	private int createShader(String source, int type) {
		int shader = GL20.glCreateShader(type);
		CharSequence shaderSource = new StringBuffer(source);
		GL20.glShaderSource(shader, shaderSource);
		GL20.glCompileShader(shader);
		if (GL20.glGetShaderi(shader, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
			System.out.println(GL20.glGetShaderInfoLog(shader, 512));
			System.err.println("Couldn't compile the shader");
			System.exit(-1);
		}
		return shader;
	}

	private String[] ParseShader(String path) {

		String[] shaders = new String[2];
		shaders[0] = "";
		shaders[1] = "";

		int shaderType = -1;

		Scanner scanner = null;
		try {
			scanner = new Scanner(new File(path));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();

			if (line.indexOf("#shader") != -1) {
				if (line.indexOf("vertex") != -1) {
					shaderType = 0;
				}

				if (line.indexOf("fragment") != -1) {
					shaderType = 1;
				}
			} else {
				shaders[shaderType] += line + "\n";
			}
		}

		return shaders;

	}
}
