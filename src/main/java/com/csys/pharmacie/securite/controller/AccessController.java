package com.csys.pharmacie.securite.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.csys.pharmacie.securite.repository.AccessControlService;

@RestController
@RequestMapping("/AccessControl")

public class AccessController {
	
	@Autowired
	private AccessControlService accessControlService;
	
	@RequestMapping(value = "/Menu", method = RequestMethod.POST)
	public @ResponseBody String findAccessMenuByUser(@RequestBody String user) {
		List <String> list =  accessControlService.findAccessMenuByUser(user);

String html ="";
       
String  htmlBC = "", 
htmlBR = "",  htmlFR = "", htmlA = "", htmlEdit = "", htmlBP = "", htmlBf = "", htmlAF = "", htmlBS="",htmlBT="",htmlBTR="",htmlBRE=""
,htmlQT="",htmlAV="",htmlPRM="",htmlBD="",htmlPR="",htmlFPC="", htmlSF = "", htmlArt = "";
for (int i = 0; i < list.size(); i++) {
	
	  if (list.get(i).equals("10")) {
	        htmlArt += "                                            <a href='javascript:void(0);' class='jarvismetro-tile big-cubes  mainMenu prmArt' id='prmArt' style='margin-bottom:0px;margin-right:4px;'>"
	                + "                                                <span class='iconbox'> "
	                + "                                                    <i class='fa fa-dropbox fa-3x'></i> "
	                + "                                                    <span style='margin-top: -19px;'>Paramétrage article</span> "
	                + "                                                </span> "
	                + "                                            </a>";
	    }
	
	
	  else if (list.get(i).equals("11")) {
        htmlBC += ""
                + "                                            <a href='javascript:void(0);' class='jarvismetro-tile big-cubes  mainMenu' id='listbc' style='margin-bottom:0px;margin-right:4px;'> "
                + "                                                <span class='iconbox'> "
                + "                                                    <i class='fa fa-pencil-square-o fa-3x'></i> "
                + "                                                    <span style='margin-top: -19px;'>Bon de commande</span> "
                + "                                                </span> "
                + "                                            </a>"
                + "                                      ";
    } else if (list.get(i).equals("12")) {
        htmlBR += ""
                + "                                            <a href='javascript:void(0);' class='jarvismetro-tile big-cubes  mainMenu' id='listbr' style='margin-bottom:0px;margin-right:4px;'>"
                + "                                                <span class='iconbox'> "
                + "                                                    <i class='fa fa-truck fa-3x'></i> "
                + "                                                    <span style='margin-top: -19px;'>Bon de réception</span> "
                + "                                                </span> "
                + "                                            </a>"
                + "                                        ";
    } else if (list.get(i).equals("13")) {
        htmlBf = ""
                + "                                            <a href='javascript:void(0);' class='jarvismetro-tile big-cubes  mainMenu' id='listBonRetourFrs' style='margin-bottom:0px;margin-right:4px;'>"
                + "                                                <span class='iconbox'> "
                + "                                                    <i class='fa fa-list fa-3x'></i> "
                + "                                                    <span style='margin-top: -19px;'>Bon de retour fournisseur </span> "
                + "                                                </span> "
                + "                                            </a>"
                + "                                        ";
    }
    else if (list.get(i).equals("14")) {
        htmlFR += ""
                + "                                            <a href='javascript:void(0);' class='jarvismetro-tile big-cubes  mainMenu' id='facRecep' style='margin-bottom:0px;margin-right:4px;'>"
                + "                                                <span class='iconbox'> "
                + "                                                    <i class='fa fa-credit-card fa-3x'></i> "
                + "                                                    <span style='margin-top: -19px;'>Facturation de réception </span> "
                + "                                                </span> "
                + "                                            </a>"
                + "                                       ";
    } else if (list.get(i).equals("15")) {
        htmlA += ""
                + "                                            <a href='javascript:void(0);' class='jarvismetro-tile big-cubes   mainMenu' id='avoir' style='margin-bottom:0px;margin-right:4px;'>"
                + "                                                <span class='iconbox'> "
                + "                                                    <i class='fa fa-reply fa-3x'></i> "
                + "                                                    <span style='margin-top: -19px;'>Avoir fournisseur</span> "
                + "                                                </span> "
                + "                                            </a>"
                + "                                        ";
    }else if (list.get(i).equals("16")) {
        htmlAF = ""
                + "                                            <a href='javascript:void(0);' class='jarvismetro-tile big-cubes   mainMenu' id='listavoirsf' style='margin-bottom:0px;margin-right:4px;'>"
                + "                                                <span class='iconbox'> "
                + "                                                    <i class='fa fa-line-chart fa-3x'></i> "
                + "                                                    <span style='margin-top: -19px;'>Avoir financier </span> "
                + "                                                </span> "
                + "                                            </a>"
                + "                                        ";
    }
     else if (list.get(i).equals("17")) {
        htmlBS = ""
                + "                                            <a href='javascript:void(0);' class='jarvismetro-tile big-cubes   mainMenu 'id='bonRedress' style='margin-bottom:0px;margin-right:4px;'>"
                + "                                                <span class='iconbox'> "
                + "                                                    <i class='fa fa-refresh fa-3x'></i> "
                + "                                                    <span style='margin-top: -19px;'>Bon de redressement</span> "
                + "                                                </span> "
                + "                                            </a>"
                + "                                        ";
    }else if (list.get(i).equals("18")) {
        htmlBRE = ""
                + "                                            <a href='javascript:void(0);' class='jarvismetro-tile big-cubes   mainMenu 'id='bonReinteg' style='margin-bottom:0px;margin-right:4px;'>"
                + "                                                <span class='iconbox'> "
                + "                                                    <i class='fa fa-refresh fa-3x'></i> "
                + "                                                    <span style='margin-top: -19px;'>Bon de réintégration</span> "
                + "                                                </span> "
                + "                                            </a>"
                + "                                        ";
    } 
    else if (list.get(i).equals("19")) {
        htmlBT = ""//<li style='list-style-type:none;'>"
                + "                                            <a href='javascript:void(0);' class='jarvismetro-tile big-cubes   mainMenu' id='listTID' style='margin-bottom:0px;margin-right:4px;'> "
                + "                                                <span class='iconbox'> "
                + "                                                    <i class='fa fa-exchange fa-3x'></i> "
                + "                                                    <span style='margin-top: -19px;'>Bon de transfert inter-dépôt</span> "
                + "                                                </span> "
                + "                                            </a>"
                + "";
    } else if (list.get(i).equals("20")) {
        htmlBTR = ""//<li style='list-style-type:none;'>"
                + "                                            <a href='javascript:void(0);' class='jarvismetro-tile big-cubes   mainMenu' id='listTIDR' style='margin-bottom:0px;margin-right:4px;'> "
                + "                                                <span class='iconbox'> "
                + "                                                    <i class='fa fa-history fa-3x'></i> "
                + "                                                    <span style='margin-top: -19px;'>Bon de transfert de récupération</span> "
                + "                                                </span> "
                + "                                            </a>"
                + "";
    }  else if (list.get(i).equals("21")) {
        htmlQT = ""
                + "                                            <a href='javascript:void(0);' class='jarvismetro-tile big-cubes   mainMenu 'id='quittance' style='margin-bottom:0px;margin-right:4px;'>"
                + "                                                <span class='iconbox'> "
                + "                                                    <i class='fa  fa-stethoscope fa-3x'></i> "
                + "                                                    <span style='margin-top: -19px;'>Quittance</span> "
                + "                                                </span> "
                + "                                            </a>"
                + "                                        ";
    } else if (list.get(i).equals("22")) {
        htmlAV = ""
                + "                                            <a href='javascript:void(0);' class='jarvismetro-tile big-cubes   mainMenu 'id='avoir_vnt' style='margin-bottom:0px;margin-right:4px;'>"
                + "                                                <span class='iconbox'> "
                + "                                                    <i class='fa fa-refresh fa-3x'></i> "
                + "                                                    <span style='margin-top: -19px;'>Avoir client</span> "
                + "                                                </span> "
                + "                                            </a>"
                + "                                        ";
    } else if (list.get(i).equals("23")) {
        htmlBD = ""
                + "                                            <a href='javascript:void(0);' class='jarvismetro-tile big-cubes   mainMenu'id='bonDepot' style='margin-bottom:0px;margin-right:4px;'>"
                + "                                                <span class='iconbox'> "
                + "                                                    <i class='fa fa-download fa-3x'></i> "
                + "                                                    <span style='margin-top: -19px;'>Bon de dépôt FRS</span> "
                + "                                                </span> "
                + "                                            </a>"
                + "                                        ";
    } else if (list.get(i).equals("24")) {
        htmlPR = ""
                + "                                            <a href='javascript:void(0);' class='jarvismetro-tile big-cubes   mainMenu'id='bonPrelev' style='margin-bottom:0px;margin-right:4px;'>"
                + "                                                <span class='iconbox'> "
                + "                                                    <i class='fa fa-upload fa-3x'></i> "
                + "                                                    <span style='margin-top: -19px;'>Bon de prélèvement FRS</span> "
                + "                                                </span> "
                + "                                            </a>"
                + "                                        ";
    } else if (list.get(i).equals("25")) {
        htmlFPC += "                                            <a href='javascript:void(0);' class='jarvismetro-tile big-cubes   mainMenu' id='factPrelev' style='margin-bottom:0px;margin-right:4px;'>"
                + "                                                <span class='iconbox'> "
                + "                                                    <i class='fa fa-money fa-3x'></i> "
                + "                                                    <span style='margin-top: -19px;'>Facture prélèvement client</span> "
                + "                                                </span> "
                + "                                            </a>";
    } else if (list.get(i).equals("26")) {
        htmlSF += "                                            <a href='javascript:void(0);' class='jarvismetro-tile big-cubes   mainMenu' id='suppFac' style='margin-bottom:0px;margin-right:4px;'>"
                + "                                                <span class='iconbox'> "
                + "                                                    <i class='fa fa-money fa-3x'></i> "
                + "                                                    <span style='margin-top: -19px;'>Annulation de facture fournisseur</span> "
                + "                                                </span> "
                + "                                            </a>";
    }else if (list.get(i).equals("27")) {
        htmlEdit += ""
                + "                                            <a href='javascript:void(0);' class='jarvismetro-tile big-cubes mainMenu' id='editStat' style='margin-bottom:0px;margin-right:4px;'>"
                + "                                                <span class='iconbox'> "
                + "                                                    <i class='fa fa-bar-chart-o fa-3x'></i> "
                + "                                                    <span style='margin-top: -19px;'>Edition & statistique </span> "
                + "                                                </span> "
                + "                                            </a>"
                + "                                        ";
    }	  
    else if (list.get(i).equals("28")) {
        htmlPRM = ""
                + "                                            <a href='javascript:void(0);' class='jarvismetro-tile big-cubes mainMenu 'id='parametrage' style='margin-bottom:0px;margin-right:4px;'>"
                + "                                                <span class='iconbox'> "
                + "                                                    <i class='fa fa-gears fa-3x'></i> "
                + "                                                    <span style='margin-top: -19px;'>Paramétrage</span> "
                + "                                                </span> "
                + "                                            </a>"
                + "                                        ";
    }
}
html = htmlArt+htmlBC + htmlBR+htmlBf + htmlFR + htmlA + htmlAF+htmlBS+htmlBRE  + htmlBP  + 
	   htmlBT+htmlBTR + htmlQT+htmlAV+htmlBD+htmlPR+htmlFPC+ htmlSF+ htmlEdit+htmlPRM;//


		return html;
//		return accessControlService.findAccessMenuByUser(user);
	}
	@RequestMapping(value = "/Form", method = RequestMethod.POST)
	public @ResponseBody List<String> findDetailAvoirFinancier(@RequestBody String user,@RequestParam("form")String form) {
		return accessControlService.findAccessFormByUser(user, form);
	}
	

}
