package com.csys.pharmacie.achat.web.rest;

//package com.csys.pharmacie.achat.controller;
//
//
//import java.text.DateFormat;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.csys.pharmacie.achat.domain.FactureCA;
//import com.csys.pharmacie.achat.domain.MvtStoCA;
//import com.csys.pharmacie.achat.domain.ProposeVO;
//import com.csys.pharmacie.achat.domain.VCommandedeJour;
//import com.csys.pharmacie.achat.repository.BonCommande;
//import com.csys.pharmacie.achat.dto.BonCommandedto;
//import com.csys.pharmacie.achat.service.FactureCAService;
//
//import helper.Articleqte;
//import helper.MouvementDuJour;
//import helper.QteMouvement;
//
//
//@RestController
//@RequestMapping("/BonCommande")
//public class BonCommandeController {
//	@Autowired
//	private FactureCAService factureCAService;
//	
//	@RequestMapping(value = "/findByDatbonBetween", method = RequestMethod.GET)
//	public @ResponseBody List<FactureCA> findByDatbonBetween(@RequestParam("debut") String debut,
//			@RequestParam("fin") String fin,@RequestParam("stup") boolean stup) throws ParseException {
//		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
//		Date datedeb = formatter.parse(debut);
//		Date datefin = formatter.parse(fin);
//		return factureCAService.findByDatbonBetween(datedeb, datefin,stup);
//	}
//
//
//	@RequestMapping(value = "/{numbon}", method = RequestMethod.GET)
//	public @ResponseBody BonCommande findfacturebynumbon(@PathVariable("numbon") String numbon) {
//		return factureCAService.findfacturebynumbon(numbon);
//	}
//
//	@RequestMapping(method = RequestMethod.POST)
//	public String addfacture(@RequestBody BonCommandedto bon) throws ParseException {
//		return factureCAService.addfacture(bon);
//	}
//
//	@RequestMapping(method = RequestMethod.PUT)
//	public boolean updatefacture(@RequestBody BonCommandedto bon) throws ParseException {
//		return factureCAService.updatefacture(bon);
//	}
//
//	@RequestMapping(value = "/annulerfacture/{numbon}", method = RequestMethod.PUT)
//	public boolean annulerfacture(@PathVariable("numbon") String numbon, @RequestParam String user) {
//		return factureCAService.annulerfacture(numbon, user);
//	}
//
//	@RequestMapping(value = "/{numbon}/details", method = RequestMethod.GET)
//	public List<MvtStoCA> findDetailsByNumbon(@PathVariable("numbon") String numbon) {
//		return factureCAService.findDetailsByNumbon(numbon);
//	}
//
//	
//	@RequestMapping(value = "/Commandedujour", method = RequestMethod.GET)
//	public  List<VCommandedeJour> findCommandedujour(@RequestParam("stup") boolean stup) {
//		return factureCAService.findAllCmdjour();
//	}
//	
//	@RequestMapping(value = "/CommandeEnCours", method = RequestMethod.POST)
//	public  List<QteMouvement> findCommandeEnCours(@RequestBody List<String> codarts) {
//		return factureCAService.CommandeEnCours(codarts);
//	}
//		
//	@RequestMapping(value = "/{numBon}/Imprimer", method = RequestMethod.PUT)
//	public Boolean imprimerBonCommande(@PathVariable("numBon")  String  numBon) {
//		return factureCAService.updateImprimer(numBon);
//	}
//	
//	@RequestMapping(value = "/MouvementDuJour", method = RequestMethod.GET)
//	public  MouvementDuJour findMouvementDuJour(@RequestParam("stup") boolean stup) throws ParseException {
//		return factureCAService.findMouvementDuJour(stup);
//	}
//	
//	@RequestMapping(value = "/TotalNST", method = RequestMethod.GET)
//	public  Integer findTotalNST(@RequestParam("stup") boolean stup) throws ParseException {
//		return factureCAService.findTotalBySatisf(stup,"NST");
//	}
//	@RequestMapping(value = "/TotalNSP", method = RequestMethod.GET)
//	public  Integer findTotalNSP(@RequestParam("stup") boolean stup) throws ParseException {
//		return factureCAService.findTotalBySatisf(stup,"NSP");
//	}
//
//
//}
