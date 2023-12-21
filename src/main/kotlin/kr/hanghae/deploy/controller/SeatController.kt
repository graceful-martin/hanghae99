package kr.hanghae.deploy.controller

import kr.hanghae.deploy.annotation.QueryStringArgResolver
import kr.hanghae.deploy.dto.ApiResponse
import kr.hanghae.deploy.dto.seat.SeatServiceRequest
import kr.hanghae.deploy.dto.seat.request.SeatRequest
import kr.hanghae.deploy.dto.seat.response.SeatResponse
import kr.hanghae.deploy.service.SeatService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class SeatController(
    private val seatService: SeatService,
) {
    @GetMapping("/api/v1/seat")
    fun getSeatsByDate(
        @QueryStringArgResolver request: SeatRequest,
    ): ApiResponse<List<SeatResponse>> {
        return ApiResponse.ok(data = seatService.getSeatsByDate(request = SeatServiceRequest.toService(request.date)))
    }
}