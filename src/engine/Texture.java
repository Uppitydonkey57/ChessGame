package engine;

import java.io.File;
import java.nio.*;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.stb.*;
import org.lwjgl.BufferUtils;

public class Texture {
	private int texture;
	private ByteBuffer image;
	private IntBuffer width = BufferUtils.createIntBuffer(1);
	private IntBuffer height = BufferUtils.createIntBuffer(1);
	private IntBuffer channels = BufferUtils.createIntBuffer(1);
	private int imageWidth = 0;
	private int imageHeight = 0;

	public Texture(String path) {
		// if jar odesn't work check the file loading system
		texture = GL20.glGenTextures();
		GL20.glBindTexture(GL20.GL_TEXTURE_2D, texture);

		GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_WRAP_S, GL30.GL_REPEAT);
		GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_WRAP_T, GL30.GL_REPEAT);
		GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_MIN_FILTER, GL30.GL_LINEAR);
		GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_MAG_FILTER, GL30.GL_LINEAR);

		STBImage.nstbi_set_flip_vertically_on_load(1);
		File file = new File(path);
		String finalPath = file.getAbsolutePath();
		image = STBImage.stbi_load(finalPath, width, height, channels, 4);

		if (image != null) {
			GL30.glPixelStorei(GL30.GL_UNPACK_ALIGNMENT, 1);
			imageWidth = width.get(0);
			imageHeight = height.get(0);
			GL30.glTexImage2D(GL30.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, imageWidth, imageHeight, 0, GL11.GL_RGBA,
					GL11.GL_UNSIGNED_BYTE, image);
			GL30.glGenerateMipmap(texture);
		} else {
			System.err.println("Couldn't load texture " + path + "!");
		}
		GL20.glBindTexture(GL30.GL_TEXTURE_2D, 0);
	}

	public void bind(int slot) {
		GL30.glActiveTexture(GL30.GL_TEXTURE0 + slot);
		GL30.glBindTexture(GL20.GL_TEXTURE_2D, texture);
	}
	
	public int getWidth() {
		return imageWidth;
	}
	
	public int getHeight() {
		return imageHeight;
	}
}
