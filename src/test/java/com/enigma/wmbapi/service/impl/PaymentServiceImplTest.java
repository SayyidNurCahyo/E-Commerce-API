package com.enigma.wmbapi.service.impl;

import com.enigma.wmbapi.constant.TransTypeId;
import com.enigma.wmbapi.dto.request.PaymentDetailRequest;
import com.enigma.wmbapi.dto.request.PaymentItemDetailRequest;
import com.enigma.wmbapi.dto.request.PaymentRequest;
import com.enigma.wmbapi.entity.*;
import com.enigma.wmbapi.repository.PaymentRepository;
import com.enigma.wmbapi.service.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
@RestClientTest(PaymentService.class)
@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {
    @Mock
    private PaymentRepository paymentRepository;
    @Mock
    private RestClient restClient;
    private final String SECRET_KEY = "U0ItTWlkLXNlcnZlci1ybUhraVJFam42UzJTZnBxMWtocURPckY=";
    private final String BASE_URL_SNAP = "https://app.sandbox.midtrans.com/snap/v1/transactions";
    private PaymentService paymentService;
    @BeforeEach
    void setUp(){
        paymentService = new PaymentServiceImpl(paymentRepository,restClient, SECRET_KEY, BASE_URL_SNAP);
    }

    @Test
    void createPayment() {
//        Menu menu = Menu.builder().id("menuId").name("menuName").price(10L).build();
//        List<Image> image = List.of(Image.builder().id("imageId").menu(menu).path("/Users/Lenovo/OneDrive/Gambar/WMB_API/timeNow_image.jpg").contentType("image/jpeg").size(10L).build());
//        menu.setImages(image);
//        Customer customer = Customer.builder().id("custId").name("custName").phone("0888888").userAccount(new UserAccount()).build();
//        Transaction transaction = Transaction.builder().id("trId").table(Table.builder().id("tableId").name("tableName").build())
//                .transDate(new Date()).transType(TransType.builder().id(TransTypeId.EI).description("typeDesc").build()).customer(customer).build();
//        TransactionDetail transactionDetail = TransactionDetail.builder().id("detailId")
//                .menu(menu).qty(1).price(menu.getPrice()).transaction(transaction).build();
//        transaction.setTransactionDetails(Collections.singletonList(transactionDetail));
//        Long amount = transaction.getTransactionDetails().stream().map(value-> (
//                value.getQty()*value.getPrice()
//        )).reduce(0L, Long::sum);
//        List<PaymentItemDetailRequest> itemDetailRequestList = Collections.singletonList(PaymentItemDetailRequest.builder().name(transactionDetail.getMenu().getName())
//                .price(transactionDetail.getPrice())
//                .quantity(transactionDetail.getQty()).build());
//        PaymentRequest request = PaymentRequest.builder()
//                .paymentDetail(PaymentDetailRequest.builder()
//                        .orderId(UUID.randomUUID().toString())
//                        .amount(amount).build())
//                .paymentItemDetails(itemDetailRequestList)
//                .paymentMethod(List.of("credit_card", "cimb_clicks",
//                        "bca_klikbca", "bca_klikpay", "bri_epay", "echannel", "permata_va",
//                        "bca_va", "bni_va", "bri_va","cimb_va", "other_va", "gopay", "indomaret",
//                        "danamon_online", "akulaku", "shopeepay", "kredivo", "uob_ezpay")).build();
//        Map<String,String> body = Map.of("token", "token","redirect_url", "redirect");
//        ResponseEntity<Map<String, String>> responseEntity = ResponseEntity.ok(body);
//        when(restClient.post().uri(anyString()).body(any(PaymentRequest.class)).header(anyString(), anyString())
//                .retrieve().toEntity(ParameterizedTypeReference.class)).thenReturn(responseEntity);
////        when(restClient.post().uri(anyString()).body(Mockito.any()).header(anyString(), anyString()).retrieve().toEntity(Mockito.any(ParameterizedTypeReference.class))).thenReturn(responseEntity);
////
////        ResponseEntity<Map<String, String>> response = restClient.post()
////                .uri(BASE_URL_SNAP).body(request)
////                .header(HttpHeaders.AUTHORIZATION, "Basic " + SECRET_KEY)
////                .retrieve().toEntity(new ParameterizedTypeReference<Map<String, String>>() {});
////        Map<String, String> body = response.getBody();
//        Payment payment = Payment.builder().id(request.getPaymentDetail().getOrderId()).token(body.get("token"))
//                .redirectURL(body.get("redirect_url"))
//                .transactionStatus("ordered").build();
//        when(paymentRepository.saveAndFlush(Mockito.any(Payment.class))).thenReturn(payment);
//        Payment result = paymentService.createPayment(transaction);
//        assertNotNull(result.getId());
//        assertNotNull(result.getToken());
    }

    @Test
    void findById() {
    }

    @Test
    void checkFailedAndUpdateStatus() {
    }
}