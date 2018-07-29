package security.oauth.authen.repository;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import security.oauth.authen.entity.DateAndTime;



public interface DateAndTimeRepository extends CrudRepository<DateAndTime, Long>{
	@Query("SELECT d FROM DateAndTime d where d.createDate=CURDATE()")
	List<DateAndTime> findUserToday();
	@Query("SELECT d FROM DateAndTime d where d.createDate <= ?1 AND d.createDate >= ?2")
	List<DateAndTime> findBySearchDate(Timestamp start,Timestamp end);
}
