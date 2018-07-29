package security.oauth.authen.entity;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class DateAndTime {
	@Id
	@GeneratedValue
	private Long id;
	private Timestamp createDate;
	private Timestamp updateDate;
	
	@OneToOne(mappedBy="dateandtime")
	private UserInfo userinfo;

}
