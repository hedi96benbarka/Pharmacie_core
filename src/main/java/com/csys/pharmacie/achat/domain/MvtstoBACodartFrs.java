package com.csys.pharmacie.achat.domain;



public class MvtstoBACodartFrs {

    private String codfrs;

    private Integer codart;
    
    private String raisoc;


	public String getCodfrs() {
		return codfrs;
	}

	public void setCodfrs(String codfrs) {
		this.codfrs = codfrs;
	}

	public MvtstoBACodartFrs() {
	}
	public MvtstoBACodartFrs(String codfrs) {
		super();
		this.codfrs = codfrs;
		
	}
	public MvtstoBACodartFrs(String codfrs, Integer codart,String raissoc) {
		super();
		this.codfrs = codfrs;
		this.codart = codart;
		this.raisoc = raissoc;
	}
        
        

	public Integer getCodart() {
		return codart;
	}

	public void setCodart(Integer codart) {
		this.codart = codart;
	}


	public String getRaisoc() {
		return raisoc;
	}

	public void setRaisoc(String raisoc) {
		this.raisoc = raisoc;
	}

}
