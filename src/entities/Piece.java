package entities;

import java.util.ArrayList;

import org.joml.Vector2f;
import org.joml.Vector4f;

import engine.*;

import org.lwjgl.glfw.*;

public class Piece extends Entity {
	
	public enum PieceType { PAWN, KNIGHT, ROOK, BISHOP, KING, QUEEN };
	
	public PieceType type;
	
	public boolean isWhite = false;
	
	float xCounter = 0;
	float yCounter = 0;
	
	int xTime = 0;
	int yTime = 0;
	
	Vector2f destination;
	
	float moveSpeed = 0.05f;
	
	BoardManager boardManager;
	
	Vector4f mainColor = new Vector4f(1,1,1,1);
	
	public boolean justFinished;
	
	public void start() {
		tag = "piece";
		
		boardManager = (BoardManager)manager.findObjectWithTag("manager");
		
		texture = new Texture("src/resources/textures/ChessPieces.png");
		
		if (!isWhite) {
			color = new Vector4f(0.4f, 0.2f, 0.3f, 1.0f);
			mainColor = new Vector4f(0.4f, 0.2f, 0.3f, 1.0f);
		}
		
		colliderSize = new Vector2f(0.9f, 0.9f);
		
		spriteSheet = new SpriteSheet(300, 300, new Vector2f(0,0));
		
		transform.order = 1;
		
		switch (type) {
		case PAWN:
			spriteSheet.coords = new Vector2f(0,0);
			break;
		case ROOK:
			spriteSheet.coords = new Vector2f(1,0);
			break;
		case KNIGHT:
			spriteSheet.coords = new Vector2f(2,0);
			break;
		case BISHOP:
			spriteSheet.coords = new Vector2f(3, 0);
			break;
		case QUEEN:
			spriteSheet.coords = new Vector2f(4, 0);
			break;
		case KING:
			spriteSheet.coords = new Vector2f(5, 0);
			break;
		}
		
	}
	
	public void update() {
		if (xTime != 0 && xCounter < xTime) {
			xCounter += moveSpeed;
			transform.position.x = lerp(transform.position.x, destination.x, xCounter/xTime);
		} else if (xCounter >= xTime && yTime != 0) {
			if (yCounter < yTime) {
				yCounter += moveSpeed;
				transform.position.y = lerp(transform.position.y, destination.y, yCounter/yTime);
			}
		}
		
		if (yCounter >= yTime && xCounter >= xTime && (xTime != 0 || yTime != 0) && !justFinished) {
			ArrayList<Entity> destructables = getCollisions(transform.position, "piece");
			
			for (Entity entity : destructables) {
				manager.removeEntity(entity);
			}
			
			boardManager.finishedMovement(this);
			System.out.println("turn finished");
			justFinished = true;
		}
		
		if (isMouseOver() && isWhite == boardManager.isWhite && boardManager.turnActive && !boardManager.waiting) {
			if (input.mouseButtonDown(0)) {
				boardManager.pieceSelected(this);
			}
			
			if ((!boardManager.isSelected && !boardManager.isMoving) || boardManager.selectedPiece == this) {
				color = new Vector4f(mainColor).sub(new Vector4f(0.2f, 0.2f, 0.2f, 0.2f));
			}
		} else {
			if (boardManager.isSelected && boardManager.selectedPiece == this) {
				color = new Vector4f(mainColor).sub(new Vector4f(0.2f, 0.2f, 0.2f, 0.2f));
			}
			else {
				color = new Vector4f(mainColor);
			}
		}
	}
	
	public void move(Vector2f newPos) {
		destination = newPos;
		xCounter = 0;
		yCounter = 0;
		xTime = (int) Math.abs(transform.position.x - newPos.x);
		yTime = (int) Math.abs(transform.position.y - newPos.y);
	}
}
