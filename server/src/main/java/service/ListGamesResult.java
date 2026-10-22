package service;
import model.*;
import java.util.Collection;

public record ListGamesResult(Collection<GameData> games){
}
