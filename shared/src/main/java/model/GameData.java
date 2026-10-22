package model;
import chess.*;

public record GameData(int gameID, String gameName, ChessGame game, String whiteUsername, String blackUsername) {
}
