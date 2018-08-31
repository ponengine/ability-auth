package security.oauth.authen.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import javax.persistence.Transient;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PromotionDTO {
    private Long id;
	private String promoName;
	private String createby;
	private LocalDate createDate;
	private LocalTime createTime;
	private LocalDate startDate;
	private LocalTime startTime;
	private LocalDate endDate;
	private LocalTime endTime;
	private LocalDate updateDate;
	private LocalTime updateTime;
	private String point;

    private String createDateST;
	private String createTimeST;
	private String startDateST;
	private String startTimeST;
	private String endDateST;
	private String endTimeST;
	private String updateDateST;
	private String updateTimeST;

	private String filePic;

	private MultipartFile uploadPic;

	private String detail;
	private String policy;

	private String pathFile;



}
