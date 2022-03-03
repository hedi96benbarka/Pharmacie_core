package com.csys.pharmacie.securite.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.csys.pharmacie.securite.entity.AccessForm;
import com.csys.pharmacie.securite.entity.AccessFormPK;
import com.csys.pharmacie.securite.entity.AccessMenu;
import com.csys.pharmacie.securite.entity.AccessMenuPK;

public interface AccessFormRepository extends JpaRepository<AccessForm, AccessFormPK> {
	
	@Query(value="SELECT distinct Control  FROM dbo.ACCESS_FORM z WHERE Form=?2 AND (CHARINDEX([Grp]+',' , ((SELECT Grp from [Access Control] WHERE UserName=?1))) >0 or ( z.Grp in ( select RIGHT(a.Grp,LEN(z.grp) )   from [Access Control] a where a.UserName=?1 ) )) AND Module='777' And visible = '1'", nativeQuery = true)
	public List<String> findAccessFormByUser(String user,String form);


}
