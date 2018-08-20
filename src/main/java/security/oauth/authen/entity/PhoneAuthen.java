package security.oauth.authen.entity;
import java.time.LocalDate;
import java.time.LocalTime;


import javax.persistence.Column;
import javax.persistence.Convert;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import security.oauth.authen.config.LocalDateAttributeConverter;
import security.oauth.authen.config.LocalTimeAttributeConverter;
@Getter
@Setter
@Entity
@Table(name="w_phone_authen")
public class PhoneAuthen {
	@Id
	@GeneratedValue
	@Column(name="phone_id")
	private Long id;
	private String phone;
	private String randomNum;
	@Convert(converter=LocalDateAttributeConverter.class)
	private LocalDate dateExpire;
	@Convert(converter=LocalTimeAttributeConverter.class)
	private LocalTime timeExpire;
	private boolean enabled=false;
	

}
