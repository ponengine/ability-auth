package security.oauth.authen.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="w_users")
public class Users implements Serializable{
	@Id
	@GeneratedValue
	@Column(name="user_id")
	private Long id;
	private String username;
	private String password;
	private boolean enabled;
	private String confirmationToken;
        private String token;
	@OneToOne(mappedBy="users")
	private UserInfo userinfo;
	

}
