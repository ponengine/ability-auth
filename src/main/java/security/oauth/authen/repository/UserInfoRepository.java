package security.oauth.authen.repository;

import org.springframework.data.repository.CrudRepository;

import security.oauth.authen.entity.UserInfo;


public interface UserInfoRepository extends CrudRepository<UserInfo, Long>{

}
