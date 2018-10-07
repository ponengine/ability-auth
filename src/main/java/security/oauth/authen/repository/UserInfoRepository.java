package security.oauth.authen.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import security.oauth.authen.entity.UserInfo;


public interface UserInfoRepository extends CrudRepository<UserInfo, Long>{

	UserInfo findByPhone(String phone);
	List<UserInfo> findByUserType(String type);
	@Query("select u from UserInfo u where u.userType = ?1 and u.createDate = CURRENT_DATE ")
	List<UserInfo> findByUserTypeToday(String type);
}
