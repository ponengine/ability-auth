package security.oauth.authen.repository;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import security.oauth.authen.entity.PhoneAuthen;

public interface PhoneAuthenRepository extends CrudRepository<PhoneAuthen, Long>{
	@Query("select p from PhoneAuthen p where p.phone = ?1 and p.randomNum = ?2 and p.dateExpire = CURRENT_DATE and CURRENT_TIME < p.timeExpire")
	PhoneAuthen findByPhoneAndRandomNum(String phone,String message);
	
	PhoneAuthen findByPhone(String phone);
//	boolean check

}
