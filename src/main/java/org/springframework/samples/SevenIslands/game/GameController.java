package org.springframework.samples.SevenIslands.game;

import java.util.Collection;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.SevenIslands.admin.AdminController;
import org.springframework.samples.SevenIslands.board.BoardService;
import org.springframework.samples.SevenIslands.deck.Deck;
import org.springframework.samples.SevenIslands.deck.DeckService;
import org.springframework.samples.SevenIslands.player.Player;
import org.springframework.samples.SevenIslands.player.PlayerController;
import org.springframework.samples.SevenIslands.util.SecurityService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/games")
public class GameController {

    @Autowired
    private SecurityService securityService;

    @Autowired
    private GameService gameService;

    @Autowired
    private BoardService boardService;

    @Autowired
    private AdminController adminController;

    @Autowired
    private PlayerController playerController;

    @Autowired
    private DeckService deckService;

    private static final String VIEW_CREATE_OR_UPDATE_GAME_FORM = "games/createOrUpdateGameForm";
    
    @GetMapping(path = "/new")
    public String createGame(Player player, ModelMap modelMap, HttpServletRequest request) {
        if(securityService.isAdmin()) {
            request.getSession().setAttribute("message", "You must be a player to create a game");
            return "redirect:/games/rooms";
        
        } else if(securityService.isAuthenticatedUser() && !player.getInGame()) {
            modelMap.addAttribute("game", new Game());
            return VIEW_CREATE_OR_UPDATE_GAME_FORM;
        
        } else {
            return securityService.redirectToWelcome(request);
        }

    }

    @PostMapping(path = "/save")
    public String saveGame(@Valid Game game, BindingResult result, ModelMap modelMap) {
        //TODO PORQUE AQUI SI FUNCIONA PERO EN LA LINEA 79 NO        

        Deck deck = deckService.init(game.getName());
       
        //poner aqui las cartas de la isla
        
        //String view = "games/lobby";
        if(gameService.gameHasInappropiateWords(game)){
            modelMap.put("game", game);
            modelMap.addAttribute("message", "The room's name contains inappropiate words. Please, check your language.");
            return VIEW_CREATE_OR_UPDATE_GAME_FORM;

        }
      
        if (result.hasErrors()) {
            modelMap.addAttribute("game", game);
            return VIEW_CREATE_OR_UPDATE_GAME_FORM;
        } else {

            Player currentPlayer = securityService.getCurrentPlayer();
            game.setPlayer(currentPlayer); 
            game.addPlayerinPlayers(currentPlayer);
            currentPlayer.addGameinGames(game);
            game.setDeck(deck);
            System.out.println(game.getCode());
            

            boardService.init(game); //INITIALIZE BOARD FOR GAME

            gameService.save(game);

            modelMap.addAttribute("game", game);
            modelMap.addAttribute("player", currentPlayer);

        }
        return "redirect:/games/"+game.getCode()+"/lobby";
    }
    
    @GetMapping(path = "/exit/{gameId}")
    public String exitGame(@PathVariable("gameId") int gameId, HttpServletRequest request) { // to exit a game (not started)

        Optional<Game> game = gameService.findGameById(gameId);
        
        if(securityService.isAuthenticatedUser()) {
            return gameService.exitGame(game, request);
        
        } else {
            return securityService.redirectToWelcome(request);
        }
        
       
        
    }
  
  
    @GetMapping(path = "/delete/{gameId}")
    public String deleteGame(@PathVariable("gameId") int gameId, ModelMap modelMap, HttpServletRequest request) {

        Optional<Game> game = gameService.findGameById(gameId); 
        
        return gameService.deleteGame(game, gameId, modelMap, request);
    }


    @GetMapping(path = "/{code}/lobby")
    public String lobby(@PathVariable("code") String gameCode, ModelMap model,
        HttpServletRequest request) { // , HttpServletResponse response
        Player p = securityService.getCurrentPlayer();
        Game g = gameService.findGamesByRoomCode(gameCode).iterator().next();
        Boolean inGame = securityService.getCurrentPlayer().getInGame();
        //TODO refactorizar
        if(inGame && !g.getPlayers().contains(p)){ // FIXME: al empezar una partida, como se refresca, detecta que está en un juego y redirige a error
            return "/error";                                        //Hasta que no termine el juego que abandonó no puede unirse a otro
        }

        //Refresh 
        // response.addHeader("Refresh", "2");
		model.put("now", new Date());
        Iterable<Game> gameOpt = gameService.findGamesByRoomCode(gameCode);
        
        if(securityService.isAuthenticatedUser()) {
            return gameService.getLobby(gameOpt, model, request);
        
        } else {    // If user is not logged
            return securityService.redirectToWelcome(request);           
        }

        

    }

    /**
     *
     * @param game
     * @param result
     * @param gameId
     * @param model
     * @param name
     * @param code
     * @param model
     * @return
     */



    // ROOMS VIEW (PUBLIC ONES)
    @GetMapping(path = "/rooms")
    public String publicRooms(ModelMap modelMap, HttpServletRequest request) {
        if (securityService.isAuthenticatedUser()) {
            securityService.insertIdUserModelMap(modelMap);
             
            if (securityService.isAdmin()) {
                return adminController.rooms(modelMap, request);
            } else {
                return playerController.games(modelMap, request);
            }
            
        } else {

            return securityService.redirectToWelcome(request);

        }
        
    }

    //Games by room code
    @GetMapping(path = "/rooms/{code}")
    public String gameByCode(@PathVariable("code") String code, ModelMap modelMap, HttpServletRequest request) {
        
        if (securityService.isAuthenticatedUser()) {
            securityService.insertIdUserModelMap(modelMap);

            
            Iterable<Game> game = gameService.findGamesByRoomCode(code);
            
            gameService.addGamesToModelMap(game, modelMap);

            
            return  "games/publicRooms";
        } else {
            return securityService.redirectToWelcome(request);
        }

    }

    // Games currently playing
    @GetMapping(path = "/rooms/playing")
    public String currentlyPlaying(ModelMap modelMap, HttpServletRequest request) {

        Collection<Game> games;

        if(securityService.isAuthenticatedUser()) {
            securityService.insertIdUserModelMap(modelMap); 
            
                // If the user has admin perms then:
                if (securityService.isAdmin()) {
                    games = gameService.findAllPlaying();
                    modelMap.addAttribute("games", games);
                    // here we can see all the games currently being played, both public and private

                } else { 
                    games = gameService.findAllPublicPlaying();
                    modelMap.addAttribute("games", games);
                    // here we can see only the public games which are currently being played, in order to watch it by streaming

                }
            
                return "games/currentlyPlaying";
        
        } else {
            return securityService.redirectToWelcome(request);
        }

    }

}
