package security.oauth.authen.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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

@Getter
@Setter
@Entity
public class UserInfo implements Serializable{
	@Id
	@GeneratedValue
	@Column(name="userinfo_id")
	private Long id;
	private String firstName;
	private String lastName;
	private String citicenid;
	private String address;
	private String phone;
	private String userType;
	
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name = "user_id")
	private Users users;
	
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name = "authorities_id")
	private Authorities authorities;
	
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name = "dateandtime_id")
	private DateAndTime dateandtime;
	
//	@OneToOne(cascade=CascadeType.ALL)
//	@JoinColumn(name = "phone_id")
//	private PhoneAuthen phone_authen;
}
