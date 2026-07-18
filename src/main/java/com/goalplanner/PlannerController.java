package com.goalplanner;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PlannerController {

    private final ClaudeService claudeService;

    public PlannerController(ClaudeService claudeService) {
        this.claudeService = claudeService;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping("/plan")
    public String plan(@RequestParam String goal, Model model) {
        GoalPlan result = claudeService.planGoal(goal);
        model.addAttribute("goal", goal);
        model.addAttribute("planHtml", result.html());
        model.addAttribute("images", result.imageUrls());
        return "result";
    }
}
