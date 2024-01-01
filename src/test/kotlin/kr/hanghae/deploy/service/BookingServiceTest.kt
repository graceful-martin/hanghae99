package kr.hanghae.deploy.service

import io.kotest.core.listeners.TestListener
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kr.hanghae.deploy.DatabaseCleanUpExecutor
import kr.hanghae.deploy.component.*
import kr.hanghae.deploy.domain.*
import kr.hanghae.deploy.dto.booking.BookingServiceRequest
import kr.hanghae.deploy.repository.BookingRepositoryImpl
import kr.hanghae.deploy.repository.ConcertRepositoryImpl
import kr.hanghae.deploy.repository.SeatRepositoryImpl
import kr.hanghae.deploy.repository.UserRepositoryImpl
import kr.hanghae.deploy.txContext
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcTemplate
import java.math.BigDecimal
import java.time.LocalDate
import java.util.concurrent.CountDownLatch
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@ExtendWith(MockKExtension::class)
internal class BookingServiceTest : DescribeSpec({

    val userReader = mockk<UserReader>()
    val seatReader = mockk<SeatReader>()
    val seatsValidator = mockk<SeatsValidator>()
    val bookingManager = mockk<BookingManager>()
    val concertReader = mockk<ConcertReader>()
    val bookingService = BookingService(userReader, seatReader, seatsValidator, bookingManager, concertReader)

    describe("requestBooking") {
        context("예약 가능 날짜와 좌석 번호들을 가지고") {

            val user = User(uuid = "uuid")
            val concert = Concert(name = "고척돔")
            val firstSeat = Seat(bookableDateId = 1, number = 1, price = 2000, grade = "A")
            val secondSeat = Seat(bookableDateId = 1, number = 2, price = 4000, grade = "B")
            val seats = mutableListOf(firstSeat, secondSeat)
            concert.updateBookableDates(
                bookableDates = mutableListOf(
                    BookableDate(
                        date = LocalDate.now(),
                        concert = concert
                    )
                )
            )

            every { concertReader.getByConcertNumberAndDate(any(), any()) } returns concert
            every { userReader.getByUUID(any()) } returns user
            every {
                seatReader.getByOrderAndDate(any(), any())
            } returns seats
            every { seatsValidator.validate(any(), any()) } just runs
            every {
                bookingManager.requestBooking(any(), any(), any())
            } returns Booking(userId = 1, seats = seats, date = LocalDate.now())

            it("좌석 예약을 수행한다") {
                val response = bookingService.requestBooking(
                    BookingServiceRequest.toService(
                        concertNumber = "1234",
                        date = LocalDate.now(),
                        seatNumbers = listOf(1, 2, 3),
                        uuid = "uuid"
                    )
                )

                response.uuid shouldBe "uuid"
                response.booking.seats[0].price shouldBe 2000
                response.booking.seats[0].number shouldBe 1
                response.booking.seats[0].grade shouldBe "A"
                response.booking.seats[1].price shouldBe 4000
                response.booking.seats[1].number shouldBe 2
                response.booking.seats[1].grade shouldBe "B"
                response.booking.date shouldBe LocalDate.now()
            }
        }
    }
})
