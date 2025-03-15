package com.example.demo.v1.representation.transfer;

import com.example.demo.v1.application.history.HistoryFacade;
import com.example.demo.v1.application.quote.QuoteFacade;
import com.example.demo.v1.application.transfer.TransferFacade;
import com.example.demo.v1.config.response.CommonResponse;
import com.example.demo.v1.config.security.CustomUserDetails;
import com.example.demo.v1.representation.transfer.dto.TransferDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/transfer")
@RequiredArgsConstructor
@Tag(name = "Transfer API", description = "송금 관련 API")
public class TransferController {
    private final QuoteFacade quoteFacade;

    private final TransferFacade transferFacade;

    private final HistoryFacade historyFacade;

    @PostMapping("/quote")
    @Operation(summary = "송금 견적서 생성", description = "사용자가 입력한 송금 금액과 대상 통화를 기반으로 송금 견적서를 생성합니다.")
    @ApiResponse(responseCode = "200", description = "견적서 생성 성공", content = @Content(schema = @Schema(implementation = TransferDto.QuoteResponse.class)))
    @ApiResponse(responseCode = "400", description = "잘못된 요청")
    @ApiResponse(responseCode = "401", description = "인증 필요")
    @ApiResponse(responseCode = "500", description = "내부 서버 에러")
    public TransferDto.QuoteResponse createQuote(@RequestBody TransferDto.QuoteRequest quoteRequest,
                                                 @AuthenticationPrincipal CustomUserDetails user) {
        return quoteFacade.createQuote(quoteRequest.getAmount(), quoteRequest.getTargetCurrency(), user.getUsername());
    }

    @PostMapping("/request")
    @Operation(summary = "송금 요청", description = "사용자가 선택한 견적서를 기반으로 송금 요청을 생성합니다.")
    @ApiResponse(responseCode = "200", description = "송금 요청 성공", content = @Content(schema = @Schema(implementation = CommonResponse.class)))
    @ApiResponse(responseCode = "400", description = "잘못된 요청")
    @ApiResponse(responseCode = "401", description = "인증 필요")
    @ApiResponse(responseCode = "500", description = "내부 서버 에러")
    public CommonResponse<Void> request(@RequestBody TransferDto.TransferRequest transferRequest,
                                        @AuthenticationPrincipal CustomUserDetails user) {
        transferFacade.createTransfer(user.getUsername(), transferRequest.getQuoteId());
        return CommonResponse.success();
    }

    @GetMapping("/list")
    @Operation(summary = "송금 이력 조회", description = "사용자의 송금 이력을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = TransferDto.HistoryResponse.class)))
    @ApiResponse(responseCode = "401", description = "인증 필요")
    @ApiResponse(responseCode = "500", description = "내부 서버 에러")
    public TransferDto.HistoryResponse list(@AuthenticationPrincipal CustomUserDetails user) {
        return historyFacade.getHistoryListByUserId(user.getUsername());
    }
}
