package security.oauth.authen.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import security.oauth.authen.entity.Users;




public interface UserRepository extends CrudRepository<Users, Long>{

	Users findByUsername(String Username);
	Users findByConfirmationToken(String confirmationToken);
	List<Users> findByEnabled(boolean status);
	

	
}
