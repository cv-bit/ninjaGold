package com.cameron.ninjaGold.controllers;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Random;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class GoldController {
	@GetMapping("/")
	public String gold(HttpSession session, Model viewModel) {
		ArrayList<String> activity = new ArrayList<String>();
		if(session.getAttribute("gold") == null) {
			session.setAttribute("gold", 50);
		}
		if(session.getAttribute("activity") == null) {
			session.setAttribute("activity", activity);
		}
		viewModel.addAttribute("totalGold", session.getAttribute("gold"));
		viewModel.addAttribute("activity", session.getAttribute("activity"));
		return "index.jsp";
	}
	
	@PostMapping("/findGold")
	public String findGold(HttpSession session, @RequestParam("building") String building) {
		System.out.println(building);
		Random r = new Random();
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM/dd/yyyy 'at' h:mma z");
		ArrayList<String> activity = (ArrayList<String>)session.getAttribute("activity");
		ZonedDateTime zdt = ZonedDateTime.now();
		String zdtString = formatter.format(zdt);
		int gold = (int)session.getAttribute("gold");
		int goldThisTurn;
		if(building.equals("farm")) {
			goldThisTurn = r.nextInt((20 - 10) + 1) + 10;
			activity.add(String.format("You entered a farm and earned %d gold %s \n", goldThisTurn, zdtString));
		} else if(building.equals("cave")) {
			goldThisTurn = r.nextInt((10 - 5) + 1) + 5;
			activity.add(String.format("You entered a cave and earned %d gold %s \n", goldThisTurn, zdtString));
		} else if(building.equals("house")){
			goldThisTurn = r.nextInt((5 - 2) + 1) + 2;
			activity.add(String.format("You entered a house and earned %d gold %s \n", goldThisTurn, zdtString));
		} else {
			goldThisTurn = r.nextInt((50 + 50) + 1) - 50;
			if(goldThisTurn > 0) {
				activity.add(String.format("You entered a casino and won %d gold! %s \n", goldThisTurn, zdtString));
			}else {
				activity.add(String.format("You entered a casino and lost %d gold. ouch! %s \n", goldThisTurn, zdtString));
			}
		}
		int totalGold = gold += goldThisTurn;
		session.setAttribute("gold", totalGold);
		session.setAttribute("activity", activity);
		return "redirect:/";
	}
}
