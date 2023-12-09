package com.rainbowbridge.reborn.service;

import com.rainbowbridge.reborn.domain.Company;
import com.rainbowbridge.reborn.domain.Pay;
import com.rainbowbridge.reborn.domain.Pet;
import com.rainbowbridge.reborn.domain.Product;
import com.rainbowbridge.reborn.domain.Reservation;
import com.rainbowbridge.reborn.domain.User;
import com.rainbowbridge.reborn.dto.pay.CompletePayAddRequestDto;
import com.rainbowbridge.reborn.dto.pay.PayAddRequestDto;
import com.rainbowbridge.reborn.dto.pay.PayAddResponseDto;
import com.rainbowbridge.reborn.repository.PayRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

@Service
@RequiredArgsConstructor
public class PayService {

    private final PayRepository payRepository;
    private final PetService petService;
    private final ProductService productService;
    private final ReservationService reservationService;
    private final CommonService commonService;

    public Pay addPay(PayAddRequestDto payDto) {
        return payRepository.save(payDto.toEntity());
    }

    public PayAddResponseDto addPayAndReservationAndPet(CompletePayAddRequestDto dto, HttpSession session) {
        User user = (User) session.getAttribute("user");
        Pet pet = petService.addPet(dto.getPetDto(), user);
        Pay pay = addPay(dto.getPayDto());
        Product product = productService.getProduct(dto.getReservationDto().getProduct_id());
        Company company = product.getCompany();
        Reservation reservation = reservationService.addReservation(dto.getReservationDto(), pet, pay, user, product, company);

        return PayAddResponseDto.builder()
                .payDt(commonService.convertLocalDateTimeFormat(pay.getPayDt()))
                .reservationDate(commonService.convertLocalDateFormat(reservation.getDate()))
                .reservationTime(commonService.convertHourTo12HourFormat(reservation.getTime()))
                .companyName(company.getName())
                .productName(product.getName())
                .totalPrice(pay.getTotalPrice())
                .build();
    }

}
