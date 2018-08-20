package security.oauth.authen.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import security.oauth.authen.config.LocalDateAttributeConverter;
import security.oauth.authen.config.LocalTimeAttributeConverter;

@Getter
@Setter
@Entity
@Table(name="w_user_info")
public class UserInfo implements Serializable{
	@Id
	@GeneratedValue
	@Column(name="userinfo_id")
	private Long id;
	private String phone;
	private String userType;
	@Convert(converter=LocalDateAttributeConverter.class)
	private LocalDate createDate;
	@Convert(converter=LocalTimeAttributeConverter.class)
	private LocalTime createTime;
	@Convert(converter=LocalDateAttributeConverter.class)
	private LocalDate updateDate;
	@Convert(converter=LocalTimeAttributeConverter.class)
	private LocalTime updateTime;
	
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name = "w_user_id")
	private Users users;
	
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name = "w_authorities_id")
	private Authorities authorities;
	
	

}
