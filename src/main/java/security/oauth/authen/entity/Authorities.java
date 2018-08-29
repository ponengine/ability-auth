package security.oauth.authen.entity;

import java.io.Serializable;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="w_authorities")
public class Authorities implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	@Column(name="authorities_id")
	private Long id;
	private String username;
	private String authority;
	
	@OneToOne(mappedBy="authorities")
	private UserInfo userinfo;
	
	
		
	
}
