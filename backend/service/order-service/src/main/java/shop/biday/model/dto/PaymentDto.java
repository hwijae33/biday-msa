package shop.biday.model.dto;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDto {

    private String orderId;

    private String userId;

    private Long awardId;

    private Long amount;
}
