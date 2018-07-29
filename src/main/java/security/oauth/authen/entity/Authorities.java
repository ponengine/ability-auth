package security.oauth.authen.entity;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Authorities implements Serializable{
	@Id
	@GeneratedValue
	@Column(name="authorities_id")
	private Long id;
	private String username;
	private String authority;
	
	@OneToOne(mappedBy="authorities")
	private UserInfo userinfo;
	
	
		
	
}
