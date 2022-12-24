package entities;

import org.joml.Vector2f;
import org.joml.Vector4f;

import engine.*;

public class BoardManager extends Entity {
	
	public boolean isSelected;
	public boolean isMoving;
	public Piece selectedPiece = null;
	
	
	@Override
	public void start() {
		tag = "manager";
		transform.order = -200;
	}

	@Override
	public void update() {
		if (input.mouseButtonDown(0) && isSelected && !isMoving) {
			Vector2f mousePosition = manager.rend.getWorldCoordinates(input.mousePosition());
			mousePosition.x = (float) Math.round(mousePosition.x);
			mousePosition.y = (float) Math.round(mousePosition.y);
			String message = (int)selectedPiece.transform.position.x + "," + (int)selectedPiece.transform.position.y +
		 			" " + (int)mousePosition.x + "," + (int)mousePosition.y;
			System.out.println(message);
			selectedPiece.move(mousePosition);
			if (manager.client != null) {
				manager.client.write(message);
			}
			isSelected = false;
			isMoving = true;
			selectedPiece.justFinished = false;
		}
	}

	public void pieceSelected(Piece piece) {
		if (isSelected || isMoving) return;
		selectedPiece = piece;
		System.out.println("ahesl");
		isSelected = true;
		piece.transform.order = 10;
	}
	
	public void finishedMovement(Piece piece) {
		if (manager.client != null)
			manager.client.write("movement complete");
		isMoving = false;
		piece.transform.order = 1;
		//selectedPiece = null;
	}
}
