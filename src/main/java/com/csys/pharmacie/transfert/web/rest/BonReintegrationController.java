/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.transfert.web.rest;
///**
// *
// * @author Farouk
// */
//
//@RestController
//@RequestMapping("/BonReintegration")
//
//public class BonReintegrationController {
//
//	@Autowired
//	private BonReintegrationService bonReintegrationService;
//
//	@RequestMapping(method = RequestMethod.POST)
//	public @ResponseBody Boolean addBonRedressement(@RequestBody BonReintegrationDTO dto) throws ParseException {
//		return bonReintegrationService.addBonReintegration(dto);
//
//	}
//
//	@RequestMapping(value = "/findByCoddepAndDatbonBetween", method = RequestMethod.GET)
//	public @ResponseBody List<FactureBRProjection> findByDatbonBetween(@RequestParam("coddep") List<String> coddep,
//			@RequestParam("debut") String deb, @RequestParam("fin") String fin,@RequestParam("stup") boolean stup) throws ParseException {
//		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
//		Date datedeb = formatter.parse(deb);
//		Date datefin = formatter.parse(fin);
//
//		return bonReintegrationService.findByCoddepAndDatbonBetween(coddep, datedeb, datefin,stup);
//	}
//
//	@RequestMapping(value = "/{numbon}", method = RequestMethod.GET)
//	public @ResponseBody FactureBR findByNumbon(@PathVariable("numbon") String numbon) {
//		return bonReintegrationService.findByNumbon(numbon);
//	}
//
//	@RequestMapping(value = "/{numbon}/details", method = RequestMethod.GET)
//	public @ResponseBody List<MvtStoBRProjection> findDetailBonRedressement(@PathVariable("numbon") String numbon) {
//		return bonReintegrationService.findDetailBonReintegration(numbon);
//	}
//   	@RequestMapping(value = "/Article/{codart}/{coddep}", method = RequestMethod.GET)
//	public @ResponseBody List<MouvementReintegration> findListMouvement(@PathVariable("codart") String codart,@PathVariable("coddep") String coddep,@RequestParam("debut") String deb, @RequestParam("fin") String fin) throws ParseException {
//	
//		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
//		Date datedeb = formatter.parse(deb);
//		Date datefin = formatter.parse(fin);
//		
//		return bonReintegrationService.findListMouvement(codart,coddep,datedeb,datefin);
//	}
// 	@RequestMapping(value = "/FamilleArticle/{famart}/{coddep}", method = RequestMethod.GET)
// 	public @ResponseBody List<QteMouvement> findQuantiteMouvement(@PathVariable("famart") String famart,@PathVariable("coddep") String coddep,@RequestParam("debut") Long deb, @RequestParam("fin") Long fin) throws ParseException {
//   		Date datedeb = new Date(deb);
//		Date datefin = new Date(fin);	
//		return bonReintegrationService.findQuantiteMouvement(famart,coddep,datedeb,datefin);
//	}
// 	
// 	@RequestMapping(value = "/Article/TotalMouvement/{codart}/{coddep}", method = RequestMethod.GET)
//	public Integer findTotalMouvement(@PathVariable("codart") String codart,@PathVariable("coddep") String coddep,@RequestParam("debut") Long deb, @RequestParam("fin") Long fin) throws ParseException {
//
//		Date datedeb = new Date(deb);
//		Date datefin = new Date(fin);
//	
//		return bonReintegrationService.findTotalMouvement(codart,coddep,datedeb,datefin);
//	}
//}