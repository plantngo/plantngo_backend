package me.plantngo.backend.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import me.plantngo.backend.models.Merchant;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class VoucherPurchaseDTO {

    @NotNull
    Integer voucherId;

    @NotNull
    Integer merchantId;

}
