package org.springframework.samples.SevenIslands.game;

import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameService {
   
    @Autowired
    private GameRepository gameRepo;

    @Transactional
    public int gameCount(){
        return (int) gameRepo.count();
    }

    @Transactional
    public Iterable<Game> findAll(){
        return gameRepo.findAll();
    }

    @Transactional
    public Iterable<Game> findAllPublic(){
        return gameRepo.findAllPublic();
    }

    @Transactional
    public Collection<Game> findAllPublicPlaying() {
        return gameRepo.findAllPublicPlaying();
    }

    @Transactional
    public Collection<Game> findAllPlaying() {
        return gameRepo.findAllPlaying();
    }

    @Transactional
    public Optional<Game> findGameById(int id){
        return gameRepo.findById(id);
    }

    @Transactional
    public void save(Game game){
        gameRepo.save(game);
    }

    @Transactional
    public void delete(Game game){
        gameRepo.delete(game);
    }

    @Transactional
    public Collection<Game> findByOwnerId(int id){ //Find games where im a owner
        return gameRepo.findByOwnerId(id);
    }

    @Transactional
    public boolean isOwner(int playerId, int gameId){ //Check if im the owner of the game
        Collection<Game> games = findByOwnerId(playerId);
        if(games.contains(findGameById(gameId).get())){
            return true;
        } else {
            return false;
        }
    }

    
    @Transactional
    public List<Game> findGamesByPlayerId(int id){ //Find games where the player with this Id have played
        return gameRepo.findGamesByPlayerId(id);
    }
    
    @Transactional
    public Collection<Game> findGamesByRoomCode(String code){
        return gameRepo.findGamesByRoomCode(code);
    }

}
