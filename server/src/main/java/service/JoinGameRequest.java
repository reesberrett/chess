package service;
import chess.*;

public record JoinGameRequest(ChessGame.TeamColor color, int gameID) {
}
