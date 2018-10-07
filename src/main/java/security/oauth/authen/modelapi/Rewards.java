package security.oauth.authen.modelapi;

import java.time.LocalDate;
import java.time.LocalTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class Rewards {
	private Long rewardId;
	private String rewardName;
	private String filePic;
	private int point;
	private String createby;
	private LocalDate createDate;
	private LocalTime createTime;
	private LocalDate startDate;
	private LocalTime startTime;
	private LocalDate endDate;
	private LocalTime endTime;
	private LocalDate updateDate;
	private LocalTime updateTime;
	private String detail;
	private String typeTime;
	private String amountTime;
	private boolean condition_1;
	private boolean condition_2;
	private boolean condition_3;
	private boolean condition_4;
	private boolean condition_5;
	private int numPermiss;
	private String policy;
	private int productNumber;
	private String location;
	
	@Transient
	private MultipartFile uploadPic;
	@Transient
	private String createDateST;
	@Transient
	private String createTimeST;
	@Transient
	private String startDateST;
	@Transient
	private String startTimeST;
	@Transient
	private String endDateST;
	@Transient
	private String endTimeST;
	@Transient
	private String updateDateST;
	@Transient
	private String updateTimeST;

}


