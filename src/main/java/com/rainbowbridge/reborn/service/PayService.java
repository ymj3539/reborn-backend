package com.rainbowbridge.reborn.service;

import com.rainbowbridge.reborn.Utils;
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
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PayService {

    private final PayRepository payRepository;
    private final PetService petService;
    private final ProductService productService;
    private final ReservationService reservationService;
    private final UserService userService;

    public Pay addPay(PayAddRequestDto payDto) {
        return payRepository.save(payDto.toEntity());
    }

    public PayAddResponseDto addPayAndReservationAndPet(CompletePayAddRequestDto dto, String token) {
        User user = userService.checkUser(token);

        if (user == null) {
            return null;
        }

        Pet pet = petService.addPet(dto.getPetDto(), user);
        Pay pay = addPay(dto.getPayDto());
        Product product = productService.getProduct(dto.getReservationDto().getProduct_id());
        Company company = product.getCompany();
        Reservation reservation = reservationService.addReservation(dto.getReservationDto(), pet, pay, user, product, company);

        return PayAddResponseDto.builder()
                .payDt(Utils.convertLocalDateTimeFormat(pay.getPayDt()))
                .reservationDate(Utils.convertLocalDateFormat(reservation.getDate()))
                .reservationTime(Utils.convertHourTo12HourFormat(reservation.getTime()))
                .companyName(company.getName())
                .companyImagePath(Utils.getImagePath(company.getNickname()))
                .productName(product.getName())
                .totalPrice(pay.getTotalPrice())
                .build();
    }

}
