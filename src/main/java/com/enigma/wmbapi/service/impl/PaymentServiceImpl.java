package com.enigma.wmbapi.service.impl;

import com.enigma.wmbapi.constant.ResponseMessage;
import com.enigma.wmbapi.dto.request.PaymentDetailRequest;
import com.enigma.wmbapi.dto.request.PaymentItemDetailRequest;
import com.enigma.wmbapi.dto.request.PaymentRequest;
import com.enigma.wmbapi.entity.Menu;
import com.enigma.wmbapi.entity.Payment;
import com.enigma.wmbapi.entity.Transaction;
import com.enigma.wmbapi.entity.TransactionDetail;
import com.enigma.wmbapi.repository.PaymentRepository;
import com.enigma.wmbapi.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final RestClient restClient;
    private final String SECRET_KEY;
    private final String BASE_URL_SNAP;
    @Autowired
    public PaymentServiceImpl(PaymentRepository paymentRepository, RestClient restClient, @Value("${midtrans.api.key}") String secretkey, @Value("${midtrans.api.snap-url}") String baseUrlSnap) {
        this.paymentRepository = paymentRepository;
        this.restClient = restClient;
        SECRET_KEY = secretkey;
        BASE_URL_SNAP = baseUrlSnap;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Payment createPayment(Transaction transaction) {
        Long amount = transaction.getTransactionDetails().stream().map(value-> (
                value.getQty()*value.getPrice()
        )).reduce(0L, Long::sum);
        List<PaymentItemDetailRequest> itemDetailRequestList = transaction.getTransactionDetails().stream().map(transactionDetail ->
                PaymentItemDetailRequest.builder().name(transactionDetail.getMenu().getName())
                        .price(transactionDetail.getPrice())
                        .quantity(transactionDetail.getQty()).build()).toList();
        PaymentRequest request = PaymentRequest.builder()
                .paymentDetail(PaymentDetailRequest.builder()
                        .orderId(UUID.randomUUID().toString())
                        .amount(amount).build())
                .paymentItemDetails(itemDetailRequestList)
                .paymentMethod(List.of("credit_card", "cimb_clicks",
                        "bca_klikbca", "bca_klikpay", "bri_epay", "echannel", "permata_va",
                        "bca_va", "bni_va", "bri_va","cimb_va", "other_va", "gopay", "indomaret",
                        "danamon_online", "akulaku", "shopeepay", "kredivo", "uob_ezpay")).build();
        ResponseEntity<Map<String, String>> response = restClient.post()
                .uri(BASE_URL_SNAP).body(request)
                .header(HttpHeaders.AUTHORIZATION, "Basic " + SECRET_KEY)
                .retrieve().toEntity(new ParameterizedTypeReference<Map<String, String>>() {});
        Map<String, String> body = response.getBody();
        Payment payment = Payment.builder().id(request.getPaymentDetail().getOrderId()).token(body.get("token"))
                .redirectURL(body.get("redirect_url"))
                .transactionStatus("ordered").build();
        return paymentRepository.saveAndFlush(payment);
    }

    @Override
    public Payment findById(String id) {
        return paymentRepository.findById(id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, ResponseMessage.ERROR_NOT_FOUND));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void checkFailedAndUpdateStatus() {
        List<String> failedStatus = List.of("deny", "failure", "cancel", "expire");
        List<Payment> payments = paymentRepository.findAllByTransactionStatusIn(failedStatus);
        for (Payment payment:payments){
            payment.setTransactionStatus("canceled");
        }
    }
}
