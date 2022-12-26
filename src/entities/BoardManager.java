package entities;

import java.util.ArrayList;

import org.joml.Vector2f;
import org.joml.Vector4f;

import engine.*;

public class BoardManager extends Entity {
	
	public boolean isSelected;
	public boolean isMoving;
	public Piece selectedPiece = null;
	public boolean isWhite;
	public boolean turnActive;
	public boolean waiting;
	Vector2f mousePosition;
	
	@Override
	public void start() {
		tag = "manager";
		transform.order = -200;
		
		if (isWhite) {
			turnActive = true;
		}
	}

	@Override
	public void update() {
		if (input.mouseButtonDown(0) && isSelected && !isMoving) {
			mousePosition = manager.rend.getWorldCoordinates(input.mousePosition());
			mousePosition.x = (float) Math.round(mousePosition.x);
			mousePosition.y = (float) Math.round(mousePosition.y);
			String message;
			if (isWhite) {
				message = "INT_MOVE:" + (int)selectedPiece.transform.position.x + "," + (int)selectedPiece.transform.position.y +
		 			"," + (int)mousePosition.x + "," + (int)mousePosition.y;
			} else {
				Vector2f tempMousePos = new Vector2f(mousePosition);
				mousePosition = new Vector2f(7.0f-tempMousePos.x, 7.0f-tempMousePos.y);
				message = "INT_MOVE:" + (int)selectedPiece.transform.position.x + "," + (int)selectedPiece.transform.position.y +
			 			"," + (int)mousePosition.x + "," + (int)mousePosition.y;
			}
			System.out.println(message);
			if (manager.client != null) {
				manager.client.write(message);
			}
			isSelected = false;
			
			waiting = true;
		}
		
		if (waiting) {
			if (manager.parser.moveValidated != null) {
				if (manager.parser.moveValidated == true) {
					selectedPiece.move(mousePosition);
					manager.parser.moveValidated = null;
					isMoving = true;
					selectedPiece.justFinished = false;
				} else if (manager.parser.moveValidated == false){
					manager.parser.moveValidated = null;
					waiting = false;
				}
			}
		}
		
		if (!turnActive) {
			if (manager.parser.currentPosition != null) {
				ArrayList<Entity> pieces = manager.findObjectsWithTag("piece");
				
				for (Entity piece : pieces) {
					if (piece.transform.position == manager.parser.startPosition) {
						if (isWhite)
							((Piece)piece).move(manager.parser.currentPosition);
						else {
							Vector2f movePos = new Vector2f(manager.parser.currentPosition);
							movePos.x = Math.abs(movePos.x-7);
							movePos.y = Math.abs(movePos.y-7);
							((Piece)piece).move(movePos);
						}
						break;
					}
				}
				
				manager.parser.currentPosition = null;
				manager.parser.startPosition = null;
			}
		}
		
		if (manager.parser.currentTurn != null)
			turnActive = (manager.parser.currentTurn == "WHITE" && isWhite) || (manager.parser.currentTurn == "BLACK" && !isWhite);

	}

	public void pieceSelected(Piece piece) {
		if (isSelected || isMoving) return;
		selectedPiece = piece;
		isSelected = true;
		piece.transform.order = 10;
	}
	
	public void finishedMovement(Piece piece) {
		if (manager.client != null)
			manager.client.write("TURN_ENDED");
		isMoving = false;
		piece.transform.order = 1;
		//selectedPiece = null;
	}
}
