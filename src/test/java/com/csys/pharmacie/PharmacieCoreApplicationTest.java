///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package com.csys.pharmacie;
//
//import java.util.Map;
//import javax.persistence.EntityManagerFactory;
//import javax.sql.DataSource;
//import org.junit.After;
//import org.junit.AfterClass;
//import org.junit.Before;
//import org.junit.BeforeClass;
//import org.junit.Test;
//import static org.junit.Assert.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
//import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
//import org.springframework.orm.jpa.vendor.AbstractJpaVendorAdapter;
//import org.springframework.test.web.servlet.MockMvc;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.transaction.PlatformTransactionManager;
//import org.springframework.web.context.WebApplicationContext;
//
///**
// *
// * @author Administrateur
// */
//public class PharmacieCoreApplicationTest {
//    
// @Autowired
//	private WebApplicationContext context;
//
//	private MockMvc mvc;
//
//	@Before
//	public void setUp() {
//		this.mvc = MockMvcBuilders.webAppContextSetup(this.context).build();
//	}
//
//	@Test
//	public void testHome() throws Exception {
//
//		this.mvc.perform(get("/")).andExpect(status().isOk())
//				.andExpect(content().string("Bath"));
//	}
//}
