package security.oauth.authen.modelapi;

import java.time.LocalDate;
import java.time.LocalTime;



import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class TransactionReport{
	

	private Long id;
	private LocalDate createDate;
	private LocalTime createTime;
	private double  money;
	private String status;
	private String payer;
	private String receiver;
	private String note;
	private String referenceId;
	private String referencetran;
	private String editBy;
	private LocalDate editDate;
	private LocalTime editTime;
	private LocalDate stasrtDate;
	private LocalDate endDate;
	public TransactionReport() {
	}
	
}
