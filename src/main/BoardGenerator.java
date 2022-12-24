package main;

import org.joml.Vector2f;

import engine.EntityManager;
import entities.*;
import entities.Tile;

public class BoardGenerator {
	
	void generateBoard(EntityManager entityManager, boolean isWhite) {
		BoardManager manager = new BoardManager();
		entityManager.addEntity(manager);
		
		boolean isBlack = true;
		for (int y = 0; y < 8; y++) {
			for (int x = 0; x < 8; x++) {
				Tile tile = new Tile();
				tile.transform.position = new Vector2f(x, y);
				entityManager.addEntity(tile);
				if (x != 0)
					isBlack = !isBlack;
				if (isBlack) {
					tile.swap();
				}
			}
		}
		
		for (int x = 0; x < 8; x++) {
			createPiece(entityManager, new Vector2f(x, 6), Piece.PieceType.PAWN, !isWhite);
			createPiece(entityManager, new Vector2f(x, 1), Piece.PieceType.PAWN, isWhite);
			
			if (x == 0 || x == 7) {
				createPiece(entityManager, new Vector2f(x, 0), Piece.PieceType.ROOK, isWhite);
				createPiece(entityManager, new Vector2f(x, 7), Piece.PieceType.ROOK, !isWhite);
			}
			
			if (x == 1 || x == 6) {
				createPiece(entityManager, new Vector2f(x, 0), Piece.PieceType.KNIGHT, isWhite);
				createPiece(entityManager, new Vector2f(x, 7), Piece.PieceType.KNIGHT, !isWhite);
			}
			
			if (x == 2 || x == 5) {
				createPiece(entityManager, new Vector2f(x, 0), Piece.PieceType.BISHOP, isWhite);
				createPiece(entityManager, new Vector2f(x, 7), Piece.PieceType.BISHOP, !isWhite);
			}
			
			if (x == 3) {
				if (isWhite) {
					createPiece(entityManager, new Vector2f(x, 0), Piece.PieceType.QUEEN, isWhite);
					createPiece(entityManager, new Vector2f(x, 7), Piece.PieceType.QUEEN, !isWhite);
				} else {
					createPiece(entityManager, new Vector2f(x, 0), Piece.PieceType.KING, isWhite);
					createPiece(entityManager, new Vector2f(x, 7), Piece.PieceType.KING, !isWhite);
				}
			}
			
			if (x == 4) {
				if (isWhite) {
					createPiece(entityManager, new Vector2f(x, 0), Piece.PieceType.KING, isWhite);
					createPiece(entityManager, new Vector2f(x, 7), Piece.PieceType.KING, !isWhite);
				} else {
					createPiece(entityManager, new Vector2f(x, 0), Piece.PieceType.QUEEN, isWhite);
					createPiece(entityManager, new Vector2f(x, 7), Piece.PieceType.QUEEN, !isWhite);
				}
			}
		}
	}
	
	void createPiece(EntityManager entityManager, Vector2f position, Piece.PieceType type, boolean isWhite) {
		Piece myPawn = new Piece();
		myPawn.type = type;
		myPawn.transform.position = position;
		myPawn.isWhite = isWhite;
		entityManager.addEntity(myPawn);
	}
}
