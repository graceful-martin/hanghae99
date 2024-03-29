package kr.hanghae.deploy.controller

import kr.hanghae.deploy.dto.ApiResponse
import kr.hanghae.deploy.dto.user.ChargeBalanceServiceRequest
import kr.hanghae.deploy.dto.user.GetBalanceServiceRequest
import kr.hanghae.deploy.dto.user.request.ChargeBalanceRequest
import kr.hanghae.deploy.dto.user.response.ChargeBalanceResponse
import kr.hanghae.deploy.dto.user.response.GenerateTokenResponse
import kr.hanghae.deploy.dto.user.response.GetBalanceResponse
import kr.hanghae.deploy.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter

@RestController
class UserController(
    private val userService: UserService,
) {

    @PostMapping("/api/v1/user/token")
    @ResponseStatus(HttpStatus.CREATED)
    fun generateToken(): ApiResponse<GenerateTokenResponse> {
        return ApiResponse.created(data = GenerateTokenResponse.of(userService.generateToken()))
    }

    @PutMapping("/api/v1/user/charge")
    fun chargeBalance(
        @RequestBody request: ChargeBalanceRequest,
        @RequestHeader("Authorization") uuid: String,
    ): ApiResponse<ChargeBalanceResponse> {
        return ApiResponse.ok(
            data = ChargeBalanceResponse.of(
                userService.chargeBalance(
                    ChargeBalanceServiceRequest.toService(
                        balance = request.balance,
                        uuid = uuid
                    )
                )
            )
        )
    }

    @GetMapping("/api/v1/user/balance")
    fun getBalance(
        @RequestHeader("Authorization") uuid: String,
    ): ApiResponse<GetBalanceResponse> {
        return ApiResponse.ok(
            data = GetBalanceResponse.of(
                userService.getBalance(
                    GetBalanceServiceRequest.toService(
                        uuid = uuid
                    )
                )
            )
        )
    }
}
