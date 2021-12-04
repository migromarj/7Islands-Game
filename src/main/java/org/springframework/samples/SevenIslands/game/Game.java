package org.springframework.samples.SevenIslands.game;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;


import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.samples.SevenIslands.deck.Deck;
import org.springframework.samples.SevenIslands.model.NamedEntity;
import org.springframework.samples.SevenIslands.player.Player;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "games")
public class Game extends NamedEntity {

    @Column(unique = true, name = "code")
    private String code;
    
    @Column(name = "start_time") 
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime startTime = LocalDateTime.now();
    
    @Column(name = "end_time") 
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime endTime; //Do with a substraction

    @Column(name="duration")
    private Duration duration = (endTime == null || startTime == null ? Duration.ofSeconds(0) : Duration.between(startTime, endTime));
    
    // @Column(name = "number_of_players")   
    // private Integer numberOfPlayers;

    @Column(name = "actual_player")   
    private Integer actualPlayer;

    @Column(name = "number_of_turn")   
    private Integer numberOfTurn;

    // @Column(name = "remains_cards")   
    // private Integer remainsCards;

    // @Column(name = "deck")   
    // private String deck;

    // @Column(name = "name_of_players")   
    // private String nameOfPlayers;

    // @Column(name = "points")   
    // private String points; //pointsOfPlayers

    @Column(name = "privacity")   
    @Enumerated(EnumType.STRING)
    private PRIVACITY privacity;

    @Column(name="has_started")
    private boolean has_started;
    
    //RELATION WITH PLAYERS 
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(name = "games_players", joinColumns = @JoinColumn(name="game_id"), 
                inverseJoinColumns = @JoinColumn(name="player_id"))
    private List<Player> players;

    public void addPlayerinPlayers(Player player){
        if(this.getPlayers()==null){
            List<Player> l = new ArrayList<>();
            l.add(player);
            this.setPlayers(l);     
        }else{
            List<Player> l = this.getPlayers();
            l.add(player);
            this.setPlayers(l);
        }
        
    }

    public void deletePlayerOfGame(Player player){
        this.players.remove(player);
    }

    @ManyToOne(optional=false)
    private Player player;

    @OneToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.REMOVE}, optional=false)
    private Deck deck;

    public Deck getDeck() {
		return deck;
  }
   
}
