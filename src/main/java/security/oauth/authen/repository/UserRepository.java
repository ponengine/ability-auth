package security.oauth.authen.repository;

import org.springframework.data.repository.CrudRepository;
import security.oauth.authen.entity.Users;

public interface UserRepository extends CrudRepository<Users, Long>{

	Users findByUsername(String Username);

}
