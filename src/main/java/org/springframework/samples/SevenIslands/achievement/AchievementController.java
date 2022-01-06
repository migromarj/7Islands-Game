package org.springframework.samples.SevenIslands.achievement;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.SevenIslands.util.SecurityService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/achievements")
public class AchievementController {

    @Autowired
    private AchievementService achievementService;

    @Autowired
	private SecurityService securityService;

    private static final String CREATE_OR_UPDATE_ACHIEVEMENTS_FORM = "achievements/createOrUpdateAchievementForm";

    @GetMapping()
    public String listAchievements(ModelMap modelMap, HttpServletRequest request) {

        if(securityService.isAdmin()) {
            String view = "achievements/achievements";
            securityService.insertIdUserModelMap(modelMap);
            modelMap.addAttribute("message", request.getSession().getAttribute("message"));
            
            Iterable<Achievement> achievements = achievementService.findAll();
            modelMap.addAttribute("achievements", achievements);
            request.getSession().removeAttribute("message");
            
            return view;
        
        } else {    // never should enter here because we specified that only admins have access to this page in SecurityConfiguration.java
            request.getSession().setAttribute("message", "You don't have permission to access this page!");
            return "redirect:/welcome";  
        } 

        
    }

    @GetMapping(path="/new")
    public String createAchievement(ModelMap modelMap){ 

        if(securityService.isAdmin()) {
            securityService.insertIdUserModelMap(modelMap);
            modelMap.addAttribute("achievement", new Achievement());
        
        }else{  
            return "/error";
        }
        return CREATE_OR_UPDATE_ACHIEVEMENTS_FORM;
    }

    @PostMapping(path="/save")
    public String saveAchievement(@Valid Achievement achievement, BindingResult result, ModelMap modelMap, HttpServletRequest request){

        if (securityService.isAdmin()) {
            if(result.hasErrors()){

                modelMap.addAttribute("achievement", achievement);
                return "achievements/editAchievement";
            }else{

                achievementService.save(achievement);
                request.getSession().setAttribute("message", "Achievement successfully saved!");
                
            }
        }else{
            return "/error";
        }
        return "redirect:/achievements";
    }


    @GetMapping(path="/delete/{achievementId}")
    public String deleteAchievement(@PathVariable("achievementId") int achievementId, ModelMap modelMap, HttpServletRequest request){

        if (securityService.isAdmin()) {
            securityService.insertIdUserModelMap(modelMap);
            Optional<Achievement> achievement = achievementService.findAchievementById(achievementId);
            if(achievement.isPresent()){    // porque es un optional
                achievementService.delete(achievement.get());
                request.getSession().setAttribute("message", "Achievement successfully deleted!");
            
            }else{
                request.getSession().setAttribute("message", "Achievement not found");                      
            }
        }else{
            return "/errors";
        }
        return "redirect:/achievements";

    }


    @GetMapping(path="/edit/{achievementId}")
    public String updateAchievement(@PathVariable("achievementId") int achievementId, ModelMap model, HttpServletRequest request) {

        
        securityService.insertIdUserModelMap(model);


        if (securityService.isAdmin()) {
            securityService.insertIdUserModelMap(model);
            Optional<Achievement> achievement = achievementService.findAchievementById(achievementId);
            if(achievement.isPresent()){
                model.put("achievement", achievement.get());
                
            } else {
                request.getSession().setAttribute("message", "Achievement not found!");
                return "redirect:/achievements";
            }

        }else {
            return "/errors";
        }
        return CREATE_OR_UPDATE_ACHIEVEMENTS_FORM;
    }

    /**
     *
     * @param achievement
     * @param result
     * @param achievementId
     * @param model
     * @param surname
     * @param firstName
     * @param model
     * @return
     */

    @PostMapping(value = "/edit/{achievementId}")
	public String processUpdateForm(@Valid Achievement achievement, BindingResult result,@PathVariable("achievementId") int achievementId, ModelMap model, HttpServletRequest request,
                                        @RequestParam(value="version", required = false) Integer version) {

        if (result.hasErrors()) {
			model.put("achievement", achievement);
			return CREATE_OR_UPDATE_ACHIEVEMENTS_FORM;
		
        } else {
            Achievement achievementToUpdate= achievementService.findAchievementById(achievementId).get();   
            // always present because if not, it should have redirected to achievements page with the message "Achievement not found!" in the @GetMapping before
			
            if(achievementToUpdate.getVersion()!=version){    //Version
                model.put("message", "Concurrent modification of achievement! Try again!");
                return updateAchievement(achievementToUpdate.getId(),model,request);
            }

            BeanUtils.copyProperties(achievement, achievementToUpdate, "id");                                                                                  
                    try {                    
                        achievementService.save(achievementToUpdate);        
                        request.getSession().setAttribute("message", "Achievement successfully updated!");            
                    
                    } catch (Exception ex) {
                        result.rejectValue("name", "duplicate", "already exists");
                        return CREATE_OR_UPDATE_ACHIEVEMENTS_FORM;
                    }
			return "redirect:/achievements";
		}
	}

}
        
