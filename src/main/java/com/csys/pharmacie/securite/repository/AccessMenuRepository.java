package com.csys.pharmacie.securite.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.csys.pharmacie.securite.entity.AccessMenu;
import com.csys.pharmacie.securite.entity.AccessMenuPK;

public interface AccessMenuRepository extends JpaRepository<AccessMenu, AccessMenuPK> {
	
	@Query(value="SELECT DISTINCT MENU FROM dbo.ACCESS_MENU  z WHERE Module='777' and  ( CHARINDEX([Grp]+',' , (SELECT Grp from [Access Control] WHERE UserName=?1) )>0 or ( z.Grp in ( select RIGHT(a.Grp,LEN(z.grp) )   from [Access Control] a where a.UserName=?1 ) ) ) and visible = '1'", nativeQuery = true)
		public List<String> findAccessMenuByUser(String user);


}
